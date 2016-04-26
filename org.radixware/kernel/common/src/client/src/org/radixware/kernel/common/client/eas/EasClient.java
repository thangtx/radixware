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

package org.radixware.kernel.common.client.eas;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.AllSapsBusyException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EChannelType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.JmsAddress;
import org.radixware.kernel.common.utils.net.SapAddress;

import org.radixware.schemas.eas.*;
import org.radixware.schemas.easWsdl.ReadManifestDocument;


public class EasClient extends ServiceClient implements IEasClient {

    private static final Long SYSTEM_ID = Long.valueOf(1);
    private static final String SERVICE_URI = "http://schemas.radixware.org/eas.wsdl";
    private static final int RESPONSE_TIMEOUT = 0;//infinite timeout
    private static final int KEEP_CONNECTION_SEC = 3;
    private static final int BUSY_SERVER_WAIT_TIMEOUT_MILLIS = 5000;
    private static final int SLEEP_ON_SERVER_BUSY_MILLIS = 500;
    private final Semaphore sem = new Semaphore(1);
    private final MessageProvider msgProvider;
    private final ISslContextFactory sslContextFactory;
    private final java.lang.Object sslContextSemaphore = new java.lang.Object();
    private volatile SSLContext sslContext;
    private volatile SubordinateClient innerClient;
    private long lastInvokeTimeMillis = -1;

    private static class SubordinateClient extends ServiceClient {//Separate client for read manifest rq

        private static final String INITIAL_SAP_NAME = "initial";
        
        private final List<SapClientOptions> initialSaps = new LinkedList<>();
        private final List<SapClientOptions> currentSaps = new ArrayList<>();
        private final Semaphore sem = new Semaphore(1);
        private final String stationName;
        private final EAuthType authType;
        private final EPortSecurityProtocol easSecurityProtocol; //security protocol requested by parent EASClient        
        private final SSLContext sslContext;
        private final MessageProvider msgProvider;
        private long lastInvokeTimeMillis = -1;

        public SubordinateClient(MessageProvider msgProvider, LocalTracer localTracer, final List<InetSocketAddress> initialAddresses, final String stationName, final EAuthType authType, final SSLContext sslContext) {
            super(localTracer, null, null, null);
            this.msgProvider = msgProvider;
            this.stationName = stationName;
            this.authType = authType;
            this.sslContext = sslContext;
            easSecurityProtocol = (sslContext == null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL);
            SapClientOptions initialSap;
            for (InetSocketAddress initialAddress : initialAddresses) {
                initialSap = new SapClientOptions();
                initialSap.setName(INITIAL_SAP_NAME + " " + initialAddress.toString());
                initialSap.setAddress(new SapAddress(initialAddress));
                initialSap.setPriority(1);
                initialSap.setBlockingPeriodMillis(600000);
                initialSap.setConnectTimeoutMillis(10000);
                initialSap.setSecurityProtocol(easSecurityProtocol);
                initialSaps.add(initialSap);
            }
        }

        @Override
        public XmlObject invokeService(RadixSoapMessage message) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
            lastInvokeTimeMillis = System.currentTimeMillis();
            return super.invokeService(message);
        }

        public String getScpName() {
            return scpName;
        }
        
        public SubordinateClient(SubordinateClient src, MessageProvider messageProvider, LocalTracer tracer, SSLContext newSslContext) {
            super(tracer, null, null, null);
            this.msgProvider = messageProvider;
            this.stationName = src.stationName;
            this.authType = src.authType;
            if (newSslContext==null){
                this.sslContext = src.sslContext;
            }else{
                this.sslContext = newSslContext;
            }
            this.easSecurityProtocol = (sslContext == null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL);
            SapClientOptions initialSap;
            for (SapClientOptions sap : src.initialSaps) {
                initialSap = new SapClientOptions();
                initialSap.setName(sap.getName());
                initialSap.setAddress(sap.getAddress());
                initialSap.setPriority(sap.getPriority());
                initialSap.setBlockingPeriodMillis(sap.getBlockingPeriodMillis());
                initialSap.setConnectTimeoutMillis(sap.getConnectTimeoutMillis());
                initialSap.setSecurityProtocol(sap.getSecurityProtocol());
                this.initialSaps.add(initialSap);
            }
        }

        @Override
        protected SSLContext prepareSslContext(final SapClientOptions sap) {
            return sslContext;
        }

        @Override
        protected boolean isSslPossible() {
            return sslContext != null;
        }

        @Override
        protected List<SapClientOptions> refresh(Long systemId, final Long thisInstanceId, String serviceUri) throws ServiceCallException {
            final List<SapClientOptions> saps = new ArrayList<>();
            saps.addAll(currentSaps);
            saps.addAll(initialSaps);
            return saps;
        }

        public List<SapClientOptions> updateSaps() throws ServiceCallException, InterruptedException {
            final ReadManifestDocument document = ReadManifestDocument.Factory.newInstance();
            ReadManifestRq request = document.addNewReadManifest().addNewReadManifestRq();
            request.setStationName(stationName);
            request.setAuthType(authType);
            sem.acquire();
            try {
                final ReadManifestMess message = (ReadManifestMess) invokeService(document, null, SYSTEM_ID, null, SERVICE_URI, KEEP_CONNECTION_SEC, RESPONSE_TIMEOUT);
                final ReadManifestRs response = message.getReadManifestRs();
                currentSaps.clear();
                final ReadManifestRs.SAPs saps = response.getSAPs();
                if (saps != null && saps.getItemList() != null) {
                    SapClientOptions sap;
                    EPortSecurityProtocol sapSecurityProtocol;
                    for (ReadManifestRs.SAPs.Item item : saps.getItemList()) {
                        sapSecurityProtocol = item.getSecurityProtocol() != null ? item.getSecurityProtocol() : EPortSecurityProtocol.NONE;
                        //discard SAP if it has a different security protocol
                        if (sapSecurityProtocol != easSecurityProtocol) {
                            final String sapSecurityProtocolName =
                                    sapSecurityProtocol == EPortSecurityProtocol.NONE ? "no" : sapSecurityProtocol.getName().toLowerCase();
                            String debugMessage = "sap with name \"%s\" has %s security protocol, but ";
                            if (easSecurityProtocol == EPortSecurityProtocol.NONE) {
                                debugMessage += "no security protocol required.";
                            } else {
                                debugMessage += easSecurityProtocol.getName().toLowerCase() + " protocol required.";
                            }
                            debugMessage += " Ignoring this sap";
                            tracer.debug(String.format(debugMessage, item.getName(), sapSecurityProtocolName), false);
                            continue;
                        }
                        sap = new SapClientOptions();
                        sap.setName(item.getName());
                        try {
                            if (item.getChannelType() == EChannelType.TCP) {
                                sap.setAddress(new SapAddress(ValueFormatter.parseCompositeInetSocketAddress(item.getAddress())));
                            } else if (item.getChannelType() == EChannelType.JMS) {
                                sap.setAddress(new SapAddress(new JmsAddress(item.getAddressCDATA())));
                            } else {
                                throw new IllegalArgumentException("Unsupported channel type: '" + item.getChannelType().getValue() + "'");
                            }
                        } catch (IllegalArgumentException ex) {
                            final String errMessage = msgProvider.translate("EASClient", "Can't read parameters of access point \'%s\': %s");
                            tracer.put(EEventSeverity.WARNING, String.format(errMessage, sap.getName(), ex.getMessage()), null, null, false);
                            continue;
                        }
                        sap.setBlockingPeriodMillis(1000 * (item.isSetBlockingPeriod() ? item.getBlockingPeriod() : 600));
                        sap.setConnectTimeoutMillis(1000 * (item.isSetConnectTimeout() ? item.getConnectTimeout().intValue() : 10));
                        sap.setPriority(item.getPriority().intValue());
                        if (item.getSecurityProtocol() != null) {
                            sap.setSecurityProtocol(item.getSecurityProtocol());
                        }
                        currentSaps.add(sap);
                    }
                    scpName = response.getScpName();
                }
                return currentSaps;
            } catch (ServiceClientException ex) {
                throw new ServiceCallSendException("Cannot get system  manifest", ex);
            } catch (InterruptedException ex) {
                throw new ServiceCallSendException("Cannot get system  manifest", ex);
            } finally {
                sem.release();
            }
        }

        @Override
        protected void onAllSapsBusy(int availableButBusyCount) throws InterruptedException, ServiceCallException {
            if (availableButBusyCount > 0 && System.currentTimeMillis() - lastInvokeTimeMillis < BUSY_SERVER_WAIT_TIMEOUT_MILLIS) {
                Thread.sleep(SLEEP_ON_SERVER_BUSY_MILLIS);
            } else {
                throw new AllSapsBusyException(msgProvider);
            }
        }
    }

    public EasClient(final IClientEnvironment environment, 
                     final InetSocketAddress initialAddress, 
                     final String stationName, 
                     final EAuthType authType) {
        this(environment, Collections.singletonList(initialAddress), stationName, authType);
    }

    public EasClient(final IClientEnvironment environment, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType) {
        super(environment.getEasSession().getSessionTrace(), null, null, null);
        this.sslContextFactory = null;
        this.msgProvider = environment.getMessageProvider();
        innerClient = new SubordinateClient(msgProvider, 
                                            environment.getEasSession().getSessionTrace(), 
                                            initialAddresses, stationName, authType, null);
    }

    public EasClient(final IClientEnvironment environment, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType, 
                     final ISslContextFactory sslContextFactory, 
                     final char[] keyStorePwd) throws KeystoreControllerException, CertificateUtilsException {
        this(environment.getMessageProvider(), 
             environment.getEasSession().getSessionTrace(), 
             initialAddresses, 
             stationName, 
             authType, 
             sslContextFactory, 
             keyStorePwd);
    }

    public EasClient(final MessageProvider msgProvider, 
                     final LocalTracer localTracer, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType, 
                     final ISslContextFactory sslContextFactory,
                     final char[] keyStorePwd) throws KeystoreControllerException, CertificateUtilsException {
        super(localTracer, null, null, null);
        this.sslContextFactory = sslContextFactory;
        this.msgProvider = msgProvider;
        renewSslContext(keyStorePwd);
        innerClient = new SubordinateClient(msgProvider, localTracer, initialAddresses, stationName, authType, sslContext);
    }

    EasClient(final MessageProvider messageProvider, final LocalTracer tracer, EasClient src) {
        super(tracer, null, null, null);
        this.sslContextFactory = src.sslContextFactory;
        this.sslContext = src.sslContext;
        this.msgProvider = messageProvider;
        this.innerClient = new SubordinateClient(src.innerClient, msgProvider, tracer, null);
    }

    @Override
    protected List<SapClientOptions> refresh(final Long systemId, final Long thisInstanceId, final String serviceUri) throws ServiceCallException {
        try {
            tracer.debug("Updating list of available access points", false);
            final List<SapClientOptions> res;
            synchronized(sslContextSemaphore){
                res = innerClient.updateSaps();
                scpName = innerClient.getScpName();
            }
            final String message;
            if (res.isEmpty()) {
                if (scpName == null) {
                    message = String.format(msgProvider.translate("TraceMessage", "for system %s and default profile no saps was found"), systemId);
                } else {
                    message = String.format(msgProvider.translate("TraceMessage", "for system %s and profile \"%s\" no saps was found"), systemId, scpName);
                }
            } else {
                final StringBuffer sapNames = new StringBuffer("");
                for (SapClientOptions sap : res) {
                    sapNames.append(sap.toString());
                    sapNames.append(" ");
                }
                if (scpName == null) {
                    message = String.format(msgProvider.translate("TraceMessage", "for system %s and default profile following saps was found:\n %s"), systemId, sapNames.toString());
                } else {
                    message = String.format(msgProvider.translate("TraceMessage", "for system %s and profile \"%s\" following saps was found:\n %s"), systemId, scpName, sapNames.toString());
                }
            }
            tracer.debug(message, false);
            return res;
        } catch (InterruptedException ex) {
            throw new ServiceCallSendException("Cannot get system  manifest", ex);
        }
    }

    @Override
    public XmlObject invokeService(RadixSoapMessage message) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        lastInvokeTimeMillis = System.currentTimeMillis();
        return super.invokeService(message);
    }

    @Override
    protected void onAllSapsBusy(int availableButBusyCount) throws InterruptedException, ServiceCallException {
        if (availableButBusyCount > 0 && System.currentTimeMillis() - lastInvokeTimeMillis < BUSY_SERVER_WAIT_TIMEOUT_MILLIS) {
            Thread.sleep(SLEEP_ON_SERVER_BUSY_MILLIS);
        } else {
            throw new AllSapsBusyException(msgProvider);
        }
    }

    @Override
    protected SSLContext prepareSslContext(SapClientOptions sap) {
        synchronized(sslContextSemaphore){
            return sslContext;
        }
    }

    @Override
    protected boolean isSslPossible() {
        synchronized(sslContextSemaphore){
            return sslContext != null;
        }
    }

    @Override
    public final void renewSslContext(final char[] keyStorePwd) throws KeystoreControllerException, CertificateUtilsException {        
        synchronized(sslContextSemaphore){
            if (sslContextFactory!=null && keyStorePwd!=null){
                this.sslContext = sslContextFactory.createSslContext(keyStorePwd);
                if (innerClient!=null){
                    innerClient = new SubordinateClient(innerClient, msgProvider, tracer, sslContext);
                }
            }
        }
    }
       
    @Override
    public XmlObject sendRequest(final XmlObject request, final String scpName, final long definitionVersion, final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {

        if (scpName != null && !scpName.isEmpty() && !scpName.equals(this.scpName)) {
            final String message = String.format(msgProvider.translate("TraceMessage", "changing profile to \"%s\""), scpName);
            tracer.debug(message, false);
            setScpName(scpName);
        }                
        final Map<String,String> headers = new HashMap<>();
        if (definitionVersion > 0) {
            HttpFormatter.appendRadixVersionHeaderAttr(headers, definitionVersion);
        }
        
        sem.acquire();
        try {
            return invokeService(request, headers, null, SYSTEM_ID, null, SERVICE_URI, KEEP_CONNECTION_SEC, timeoutSec);
        } finally {
            sem.release();
        }
    }

    @Override
    public XmlObject sendCallbackResponse(XmlObject response, final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {
        sem.acquire();
        try {
            return sendCallbackResponse(response, null, SYSTEM_ID, null, SERVICE_URI, timeoutSec);
        } finally {
            sem.release();
        }
    }

    @Override
    public XmlObject sendFaultMessage(final String message, final Throwable error, final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {
        sem.acquire();
        try {
            return sendCallbackFault("SOAP-ENV:Server",
                    "ApplicationError",
                    message,
                    error,//Throwable cause
                    error==null ? null : ClientException.exceptionStackToString(error),//String preprocessedCauseStack
                    null,//List<ResponseTraceItem> traceBuffer
                    null,//Class invokeResultClass
                    SYSTEM_ID,
                    null,//current instance id
                    SERVICE_URI,
                    timeoutSec);
        } finally {
            sem.release();
        }
    }
}
