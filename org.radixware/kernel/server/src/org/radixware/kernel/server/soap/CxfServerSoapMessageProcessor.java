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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Provider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ESoapOption;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.soap.IServerSoapMessageProcessor;
import org.radixware.kernel.common.soap.ProcessException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.SoapFormatter;
import org.radixware.kernel.server.instance.ObjectCache;
import org.radixware.kernel.server.sap.SapOptions;


public class CxfServerSoapMessageProcessor implements IServerSoapMessageProcessor {

    private static final String LAST_MESSAGE = "radix-paused-message";
    private static final String INTERCEPTED_EXCEPTION = "radix-intercepted-exception";
    private static final String SERVICE_CALL_FAULT_EXCEPTION = "radix-service-call-fault";
    private static final int SERVER_ENGINE_STORE_TIME_SECONDS = 60 * 60;//1 hour
    private final SapOptions sap;
    private final LocalTracer tracer;
    private IServerResponceSoapMessageProcessor responceProcessor;
    private final ObjectCache cache;
    private final IWsdlLoader wsdlLoader;

    public CxfServerSoapMessageProcessor(SapOptions sap, final LocalTracer tracer, final ObjectCache cache) {
        this(sap, tracer, cache, null);
    }

    public CxfServerSoapMessageProcessor(SapOptions sap, final LocalTracer tracer, final ObjectCache cache, final IWsdlLoader wsdlLoader) {
        this.sap = sap;
        this.tracer = tracer;
        this.cache = cache;
        this.wsdlLoader = wsdlLoader;
    }

    @Override
    public byte[] wrapResponce(final XmlObject object) throws ProcessException {
        if (responceProcessor != null) {
            return responceProcessor.wrapResponce(object);
        } else {
            return SoapFormatter.getBytes(object);
        }
    }

    @Override
    public byte[] wrapFault(ServiceProcessFault fault, List<SoapFormatter.ResponseTraceItem> traceBuffer) throws ProcessException {
        if (responceProcessor != null) {
            return responceProcessor.wrapFault(fault, traceBuffer);
        } else {
            return SoapFormatter.prepareFault(fault.code, fault.reason, fault.getFaultDetailWriter() == null ? new SoapFormatter.DefaultFaultDetailWriter(fault.getMessage(), fault.getCause(), fault.preprocessedCauseStack) : fault.getFaultDetailWriter(), traceBuffer);
        }
    }

    @Override
    public XmlObject unwrapRequest(byte[] dirtyData) throws ProcessException {
        try {
            final String serverEngineKey = getServerEngineKey(sap);

            CxfServerSoapEngine engine = null;
            if (cache != null) {
                engine = cache.get(serverEngineKey, CxfServerSoapEngine.class);
            }
            if (engine == null) {
                engine = new CxfServerSoapEngine(sap, tracer, wsdlLoader);
                if (cache != null) {
                    cache.putExpiringSinceCreation(serverEngineKey, engine, SERVER_ENGINE_STORE_TIME_SECONDS);
                }
            }

            responceProcessor = engine.process(dirtyData);

            return XmlObject.Factory.parse(new ByteArrayInputStream(responceProcessor.getRequestMessageBytes()));
        } catch (Exception ex) {
            if (ex instanceof ProcessException) {
                throw (ProcessException) ex;
            } else {
                throw new ProcessException(ex);
            }
        }
    }

    private String getServerEngineKey(final SapOptions sap) {
        return sap.getSapId() + "~" + sap.getSoapServiceOptions().getLastUpdateTimeMillis();
    }

    private static class CxfServerSoapEngine {

        private final RadixDestinationFactoryManager dfm;
        private final DummyProvider provider;
        private final Bus bus;
        private final SapOptions sap;

        public CxfServerSoapEngine(final SapOptions sap, final LocalTracer tracer, final IWsdlLoader wsdlLoader) throws IOException, WSDLException, TransformerConfigurationException {
            this.sap = sap;

            final QName[] serverAndPortName;

            final URL wsdlUrl;
            if (wsdlLoader != null) {
                wsdlUrl = wsdlLoader.getWsdlUrl(sap);
            } else {
                wsdlUrl = ServerSoapUtils.getWsdlUrl(sap.getSoapServiceOptions(), null, tracer);
            }
            
            if (wsdlUrl == null) {
                throw new IllegalStateException("Wsdl url is null");
            }

            serverAndPortName = ServerSoapUtils.getServiceAndPortName(sap.getSoapServiceOptions(), wsdlUrl);

            bus = BusFactory.newInstance().createBus();

            BusFactory.setThreadDefaultBus(bus);

            dfm = new RadixDestinationFactoryManager();

            BusFactory.getThreadDefaultBus().setExtension(dfm, DestinationFactoryManager.class);

            provider = new DummyProvider(TransformerFactory.newInstance().newTransformer());

            final EndpointImpl endpoint = new EndpointImpl(provider);

            endpoint.getProperties().put("javax.xml.ws.wsdl.description", wsdlUrl);
            endpoint.getProperties().put(javax.xml.ws.Endpoint.WSDL_SERVICE, serverAndPortName[0]);
            endpoint.getProperties().put(javax.xml.ws.Endpoint.WSDL_PORT, serverAndPortName[1]);

            ServerSoapUtils.configureWsSecurity(endpoint.getProperties(), sap.getAdditionalAttrs(), tracer, sap.getShortDescription());

            final FaultDetector faultDetector = new FaultDetector();
            BusFactory.getThreadDefaultBus().getOutFaultInterceptors().add(faultDetector);

            final PauseInterceptor pauseInterceptor = new PauseInterceptor();
            BusFactory.getThreadDefaultBus().getInInterceptors().add(pauseInterceptor);

            final FaultInitiator faultInitiator = new FaultInitiator();
            BusFactory.getThreadDefaultBus().getInInterceptors().add(faultInitiator);

            faultInitiator.getAfter().add(pauseInterceptor.getId());

            endpoint.publish("http://localhost/");
        }

        public IServerResponceSoapMessageProcessor process(final byte[] requestData) throws ProcessException {

            dfm.process(requestData);

            try {
                final Exception receiveException = (Exception) bus.getProperty(INTERCEPTED_EXCEPTION);
                if (receiveException != null) {
                    throw new ProcessException(receiveException);
                }
            } finally {
                bus.setProperty(INTERCEPTED_EXCEPTION, null);
            }

            return new IServerResponceSoapMessageProcessor() {
                private final byte[] requestMessageBytes = provider.requestMessageBytes;
                private final Message inMessage = (Message) bus.getProperty(LAST_MESSAGE);

                @Override
                public byte[] wrapResponce(final XmlObject object) throws ProcessException {
                    return doWrap(object, null);
                }

                @Override
                public byte[] wrapFault(ServiceProcessFault fault, List<SoapFormatter.ResponseTraceItem> traceBuffer) throws ProcessException {
                    return doWrap(null, fault);
                }

                private byte[] doWrap(final XmlObject object, final ServiceProcessFault fault) throws ProcessException {
                    if (fault != null) {
                        inMessage.getExchange().getOutMessage().put(SERVICE_CALL_FAULT_EXCEPTION, fault);
                    } else {
                        inMessage.getExchange().getOutMessage().setContent(List.class, Arrays.asList(new DOMSource(object.getDomNode())));
                    }
                    configureWsSecurityForOutMessage(inMessage.getExchange().getOutMessage());
                    inMessage.getInterceptorChain().resume();
                    try {
                        final Exception sendException = (Exception) inMessage.getExchange().getBus().getProperty(INTERCEPTED_EXCEPTION);
                        if (sendException != null) {
                            throw new ProcessException(sendException);
                        }
                    } finally {
                        inMessage.getExchange().getBus().setProperty(INTERCEPTED_EXCEPTION, null);
                    }
                    return dfm.getOutMessageBytes();
                }

                private void configureWsSecurityForOutMessage(final Message message) {
                    message.putAll(sap.getAdditionalAttrs());
                    if (message.get(ESoapOption.WS_SECURITY_ENCRYPT_CERT_ALIAS.getValue()) == null) {
                        message.put(ESoapOption.WS_SECURITY_ENCRYPT_CERT_ALIAS.getValue(), WSHandlerConstants.USE_REQ_SIG_CERT);
                    }
                }

                @Override
                public XmlObject unwrapRequest(byte[] dirtyData) throws ProcessException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public byte[] getRequestMessageBytes() {
                    return requestMessageBytes;
                }
            };

        }
    }

    private static interface IServerResponceSoapMessageProcessor extends IServerSoapMessageProcessor {

        public byte[] getRequestMessageBytes();
    }

    private static class PauseInterceptor extends AbstractPhaseInterceptor<Message> {

        public PauseInterceptor() {
            super("PauseInterceptor", Phase.POST_INVOKE, true);
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            message.getInterceptorChain().pause();
            message.getExchange().getBus().setProperty(LAST_MESSAGE, message);
        }
    }

    private static class FaultDetector extends AbstractPhaseInterceptor<Message> {

        public FaultDetector() {
            super(Phase.PRE_LOGICAL);
        }

        @Override
        public void handleMessage(Message t) throws Fault {
            if (t.get(SERVICE_CALL_FAULT_EXCEPTION) != null) {
                //continue wrapping service call fault
                return;
            }
            t.getExchange().getBus().setProperty(INTERCEPTED_EXCEPTION, t.getContent(Exception.class));
            t.getInterceptorChain().abort();
        }
    }

    private static class FaultInitiator extends AbstractPhaseInterceptor<Message> {

        public FaultInitiator() {
            super("FaultInitiator", Phase.POST_INVOKE, true);
        }

        @Override
        public void handleMessage(Message t) throws Fault {
            final Object faultObj = t.getExchange().getOutMessage().get(SERVICE_CALL_FAULT_EXCEPTION);
            if (faultObj instanceof ServiceProcessFault) {
                final ServiceProcessFault fault = (ServiceProcessFault) faultObj;
                throw new SoapFault(fault.getMessage(), fault, new QName(fault.code));
            }
        }
    }

    private static class DummyProvider implements Provider<Source> {

        private byte[] requestMessageBytes;
        private final Transformer transformer;

        public DummyProvider(Transformer transformer) {
            this.transformer = transformer;
        }

        @Override
        public Source invoke(Source request) {
            final ByteArrayOutputStream requestBos = new ByteArrayOutputStream();
            final StreamResult result = new StreamResult(requestBos);
            try {
                transformer.transform(request, result);
            } catch (Exception ex) {
                throw new RuntimeException("Error on transforming request to byte array", ex);

            }
            requestMessageBytes = requestBos.toByteArray();
            return new DOMSource();
        }
    }
}
