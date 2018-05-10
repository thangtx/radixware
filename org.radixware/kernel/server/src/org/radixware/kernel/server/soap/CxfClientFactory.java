/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.soap;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.endpoint.UpfrontConduitSelector;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.enums.ESoapOption;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallRecvException;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.SyncClientConnection;
import org.radixware.kernel.common.soap.DefaultClientSoapMessageProcessor;
import org.radixware.kernel.common.soap.DefaultSyncClientSoapEngine;
import org.radixware.kernel.common.soap.IClientMessageProcessorFactory;
import org.radixware.kernel.common.soap.IClientSoapMessageProcessor;
import org.radixware.kernel.common.soap.ISyncClientSoapEngine;
import org.radixware.kernel.common.soap.ISyncClientSoapEngineFactory;
import org.radixware.kernel.common.soap.ProcessException;
import org.radixware.kernel.common.soap.RadixSoapHelper;
import org.radixware.kernel.common.soap.RadixSoapIOExceptionOnReceive;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.common.cache.ObjectCache;
import org.w3c.dom.Element;
import org.xmlsoap.schemas.soap.envelope.Detail;


public class CxfClientFactory implements ISyncClientSoapEngineFactory {

    private static final int DISPATCH_STORE_TIME_SECONDS = 60 * 60; //1 hour
    private static final String SERVICE_PROP_NAME = "rdx-service";
    private static final String CONDUIT_INITIATOR_MANAGER_PROP_NAME = "rdx-conduit-initiator-manager";
    private final Transformer transformer;
    private ICxfClientContext context;

    /**
     * This factory uses {@linkplain ObjectCache} to obtain cached
     * {@linkplain  Dispatch} objects, if possible.
     *
     * @param dbConnection
     */
    public CxfClientFactory(final ICxfClientContext context) {
        this.context = context;
        try {
            this.transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            //should not happen
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ISyncClientSoapEngine create(final SapClientOptions sap, final LocalTracer tracer) throws IOException, WSDLException {
        if (sap.getSoapServiceOptions() == null || sap.getSoapServiceOptions().getWsdlSource() == null) {
            return new DefaultSyncClientSoapEngine(tracer);
        }
        final CxfServiceClient client = getCxfClient(sap, tracer);

        return new ISyncClientSoapEngine() {
            @Override
            public RadixSoapMessage invoke(final RadixSoapMessage message, final SyncClientConnection connection) throws ServiceCallSendException, ServiceCallRecvException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
                final DefaultResponceHandler handler = new DefaultResponceHandler();
                client.invoke(message, new SyncClientConduitDelegate(connection, message, tracer), handler);
                try {
                    return handler.getResponce();
                } catch (Exception ex) {
                    final String destinationInfo = (message.getDestinationInfo() == null || message.getDestinationInfo().isEmpty()) ? connection.toString() : message.getDestinationInfo();
                    processCxfException(ex, message.getType(), destinationInfo);
                    throw new ServiceCallRecvException("Unexpected error on interacting with " + destinationInfo, ex);
                }
            }
        };
    }

    public IClientMessageProcessorFactory createMessageProcessorFactory(final SapClientOptions sap, final LocalTracer tracer) throws IOException, WSDLException {
        return new IClientMessageProcessorFactory() {
            @Override
            public IClientSoapMessageProcessor createProcessor() throws Exception {
                if (sap.getSoapServiceOptions() == null || sap.getSoapServiceOptions().getWsdlSource() == null) {
                    return new DefaultClientSoapMessageProcessor();
                }
                final CxfServiceClient client = getCxfClient(sap, tracer);
                return new IClientSoapMessageProcessor() {
                    private final DefaultResponceHandler responceHandler = new DefaultResponceHandler();
                    private IResponceDataListener responceListener;
                    private final RadixSoapMessage message = new RadixSoapMessage();
                    boolean wrapped = false;

                    @Override
                    public byte[] wrapRequest(XmlObject requestPayload) throws ProcessException {
                        if (wrapped) {
                            throw new ProcessException("Wrap has already been called");
                        }
                        message.setAttrs(new HashMap<String, String>());
                        message.getAttrs().putAll(sap.getAdditionalAttrs());
                        message.setType(ESoapMessageType.REQUEST);
                        message.setBodyDoc(requestPayload);
                        final byte[][] requestDataContainer = new byte[1][];
                        client.invoke(message, new IConduitDelegate() {
                            @Override
                            public void setResponceDataListener(IResponceDataListener listener) {
                                responceListener = listener;
                            }

                            @Override
                            public void sendRequest(EndpointReferenceType endpointInfo, byte[] requestData) throws IOException {
                                requestDataContainer[0] = requestData;
                            }
                        }, responceHandler);
                        wrapped = true;
                        if (responceHandler.getException() != null) {
                            if (responceHandler.getException() instanceof ExecutionException) {
                                throw new ProcessException(((ExecutionException) responceHandler.getException()).getCause());
                            } else {
                                throw new ProcessException(responceHandler.getException());
                            }
                        }
                        if (requestDataContainer[0] == null) {
                            throw new ProcessException("Request data has not been formed");
                        }
                        return requestDataContainer[0];
                    }

                    @Override
                    public XmlObject unwrapResponce(byte[] responceData, Class resultClass) throws ProcessException, ServiceCallFault {
                        if (!wrapped) {
                            throw new ProcessException("There was no preceding wrap operation");
                        }
                        message.setResultClass(resultClass);
                        responceListener.onResponce(responceData);
                        try {
                            return responceHandler.getResponce().getBodyDocument();
                        } catch (Exception ex) {
                            try {
                                processCxfException(ex, ESoapMessageType.REQUEST, sap.getShortDescription());
                            } catch (Exception processedEx) {
                                if (processedEx instanceof ServiceCallFault) {
                                    throw (ServiceCallFault) processedEx;
                                }
                                throw new ProcessException(processedEx);
                            }
                            //shouldn't reach this
                            throw new ProcessException(ex);
                        }
                    }
                };
            }
        };
    }

    private CxfServiceClient getCxfClient(final SapClientOptions sap, final LocalTracer tracer) throws IOException, WSDLException {
        final String dispatchKey = ServerSoapUtils.getDispatchKey(sap);

        Dispatch<Source> dispatch = context.getObjectCache() == null ? null : context.getObjectCache().get(dispatchKey, Dispatch.class);
        if (dispatch != null) {
            return new CxfServiceClient(dispatch, sap, transformer, tracer);
        }

        final long startMillis = System.currentTimeMillis();

        BusFactory.setThreadDefaultBus(BusFactory.newInstance().createBus());//clear bus before creating new service

        final Bus bus = BusFactory.getThreadDefaultBus();

        final URL wsdlURL = context.getWsdlUrl(sap);

        final QName[] serviceAndPortName = ServerSoapUtils.getServiceAndPortName(sap.getSoapServiceOptions(), wsdlURL);

        Service service = Service.create(wsdlURL, serviceAndPortName[0]);

        final RadixClientConduitInitiatorManager conduitInitiatorManager = new RadixClientConduitInitiatorManager();

        bus.setExtension(conduitInitiatorManager, ConduitInitiatorManager.class);

        bus.setExtension(new ConfigurerImpl(), Configurer.class);

        dispatch = service.createDispatch(serviceAndPortName[1], Source.class, Service.Mode.PAYLOAD);

        dispatch.getRequestContext().put(SERVICE_PROP_NAME, service);
        dispatch.getRequestContext().put(CONDUIT_INITIATOR_MANAGER_PROP_NAME, conduitInitiatorManager);

        ServerSoapUtils.configureWsSecurity(dispatch.getRequestContext(), sap.getAdditionalAttrs(), tracer, sap.getShortDescription());

        if (context.getObjectCache() != null) {
            context.getObjectCache().putExpiringSinceLastUsage(dispatchKey, dispatch, DISPATCH_STORE_TIME_SECONDS);
        }

        tracer.debug("Dispatch " + dispatchKey + " was created in " + (System.currentTimeMillis() - startMillis) + " ms", true);

        return new CxfServiceClient(dispatch, sap, transformer, tracer);
    }

    private static class ConfigurerImpl implements Configurer {

        @Override
        public void configureBean(Object o) {
            configureBean(null, o);
        }

        @Override
        public void configureBean(String string, Object o) {
            if (o instanceof JaxWsProxyFactoryBean) {
                ((JaxWsProxyFactoryBean) o).setConduitSelector(new UpfrontConduitSelector() {
                    @Override
                    protected Conduit findCompatibleConduit(Message message) {
                        return null;
                    }

                    @Override
                    protected Conduit getSelectedConduit(Message message) {
                        try {
                            return super.getSelectedConduit(message);
                        } finally {
                            conduits.clear();//disable caching
                        }
                    }
                });
            }
        }
    }

    private static class CxfServiceClient {

        private final Dispatch<Source> dispatch;
        private final SapClientOptions sap;
        private final Transformer transformer;

        public CxfServiceClient(Dispatch<Source> dispatch, SapClientOptions sap, Transformer transformer, final LocalTracer tracer) {
            this.dispatch = dispatch;
            this.sap = sap;
            this.transformer = transformer;
        }

        void invoke(final RadixSoapMessage message, final IConduitDelegate conduitDelegate, final IResponceHandler responceHandler) {
            setupOperationName(message);

            final RadixClientConduitInitiatorManager conduitManager = (RadixClientConduitInitiatorManager) dispatch.getRequestContext().get(CONDUIT_INITIATOR_MANAGER_PROP_NAME);
            final RadixClientConduitInitiatorManager.RadixClientConduit conduit = conduitManager.prepare(conduitDelegate);
            try {

                if (sap.getAdditionalAttrs() != null) {//refill sap attributes because they can be overridden by previous message
                    dispatch.getRequestContext().putAll(sap.getAdditionalAttrs());
                }

                configureSecurityForMessage(message);

                dispatch.invokeAsync(new DOMSource(message.getBodyDocument().getDomNode()), new AsyncHandler<Source>() {
                    @Override
                    public void handleResponse(Response<Source> res) {
                        final RadixSoapMessage responceMessage;
                        try {
                            Source responceObject = null;
                            try {
                                responceObject = res.get();
                            } catch (NullPointerException ex) {
                                //responceObject is already null
                            }

                            if (responceObject == null) {
                                throw new RadixSoapIOExceptionOnReceive("Empty responce");
                            }

                            responceMessage = RadixSoapHelper.createMessageFromResponce(getResult(responceObject, message.getResultClass()), conduit.getLastResponceAttrs(), message);
                            responceHandler.onResponce(responceMessage);
                        } catch (Exception ex) {
                            responceHandler.onException(ex);
                            return;
                        }
                        responceHandler.onResponce(responceMessage);
                    }
                });
            } finally {
                conduitManager.reset();
            }
        }

        private void configureSecurityForMessage(final RadixSoapMessage message) {
            String encryptCertAlias = null;
            String signKeyAlias = null;

            if (message.getAttrs() != null) {
                encryptCertAlias = message.getAttrs().get(ESoapOption.WS_SECURITY_ENCRYPT_CERT_ALIAS.getValue());
                message.getAttrs().remove(ESoapOption.WS_SECURITY_ENCRYPT_CERT_ALIAS.getValue());
                signKeyAlias = message.getAttrs().get(ESoapOption.WS_SECURITY_SIGN_KEY_ALIAS.getValue());
                message.getAttrs().remove(ESoapOption.WS_SECURITY_SIGN_KEY_ALIAS.getValue());
            }

            if (encryptCertAlias != null) {
                dispatch.getRequestContext().put(ESoapOption.WS_SECURITY_ENCRYPT_CERT_ALIAS.getValue(), encryptCertAlias);
            }
            if (signKeyAlias != null) {
                dispatch.getRequestContext().put(ESoapOption.WS_SECURITY_SIGN_KEY_ALIAS.getValue(), signKeyAlias);
            }
        }

        private XmlObject getResult(final Source source, final Class resultClass) throws ServiceCallRecvException, ServiceCallFault {
            final XmlObject rs = XmlObject.Factory.newInstance();
            try {
                transformer.transform(source, new DOMResult(rs.getDomNode()));
                return SoapFormatter.parseResponse(rs, resultClass);
            } catch (TransformerException | IOException ex) {
                throw new ServiceCallRecvException("Erron on responce parsing", ex);
            }

        }

        private void setupOperationName(final RadixSoapMessage message) {
            if (message == null || message.getBodyDocument() == null) {
                return;
            }
            String operationName = message.getBodyDocument().getDomNode().getChildNodes().item(0).getLocalName();
            Service service = (Service) dispatch.getRequestContext().get(SERVICE_PROP_NAME);
            dispatch.getRequestContext().put(MessageContext.WSDL_OPERATION, new QName(service.getServiceName().getNamespaceURI(), operationName));
        }
    }

    private static void processCxfException(Exception ex, final ESoapMessageType operationType, final String destinationInfo) throws ServiceCallFault, ServiceCallSendException, InterruptedException, ServiceCallTimeout, ServiceCallRecvException {
        if (ex instanceof ExecutionException) {
            ex = getCauseOrThrowSCRE(ex);
        }
        if (ex instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) ex;
            throw new ServiceCallFault(soapFault.getFaultCode() == null ? "" : soapFault.getFaultCode().toString(), soapFault.getReason(), getFaultDetailFromDOM(soapFault.getDetail()), ex);
        }
        if (ex instanceof Fault) {
            ex = getCauseOrThrowSCRE(ex);
        }
        if (ex instanceof IOException) {
            RadixSoapHelper.processIOException((IOException) ex, operationType, destinationInfo);
        }
        if (ex instanceof SOAPFaultException) {
            SOAPFault fault = ((SOAPFaultException) ex).getFault();
            if (fault != null) {
                throw new ServiceCallFault(fault.getFaultCode(), fault.getFaultString(), getFaultDetailFromDOM(fault.getDetail()), ex);
            } else {
                throw new ServiceCallFault("", "", null);
            }
        }
        throw new ServiceCallRecvException("Unexpected error", ex);
    }

    private static Detail getFaultDetailFromDOM(final Element detail) {
        Detail xDetail = null;
        try {
            xDetail = Detail.Factory.parse(detail);
        } catch (Exception parseEx) {
            //ignore
        }
        return xDetail;
    }

    private static Exception getCauseOrThrowSCRE(Exception ex) throws ServiceCallRecvException {
        if (ex.getCause() instanceof Exception) {
            return (Exception) ex.getCause();
        } else {
            throw new ServiceCallRecvException("Unexptected error", ex.getCause() == null ? ex : ex.getCause());
        }
    }
}
