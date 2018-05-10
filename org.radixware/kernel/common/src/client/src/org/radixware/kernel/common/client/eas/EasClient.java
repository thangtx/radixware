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
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.AllSapsBusyException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.NoSapsForCurrentVersion;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EChannelType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.ESoapOption;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.sc.FailedSapsInfo;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.CompositeInetSocketAddress;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.JmsAddress;
import org.radixware.kernel.common.utils.net.SapAddress;

import org.radixware.schemas.eas.*;
import org.radixware.schemas.easWsdl.ReadManifestDocument;


public class EasClient extends ServiceClient implements IEasClient {
    
    private static final class DefaultAadcMemberSwitchController implements IAadcMemberSwitchController{
        
        public static final DefaultAadcMemberSwitchController INSTANCE = new DefaultAadcMemberSwitchController();

        @Override
        public boolean canSwitch() throws InterruptedException {
            return false;
        }

        @Override
        public void stopSwitching() {            
        }
        
    }    

    private static final Long SYSTEM_ID = Long.valueOf(1);
    private static final String SERVICE_URI = "http://schemas.radixware.org/eas.wsdl";
    private static final String DIRTY_DATA_LOGGING_SYS_PROP_NAME = "org.radixware.kernel.common.client.eas.dirty-data-logging";
    private static final int RESPONSE_TIMEOUT = 0;//infinite timeout
    private static final int KEEP_CONNECTION_SEC = 3;
    private static final int BUSY_SERVER_WAIT_TIMEOUT_MILLIS = 5000;
    private static final int SLEEP_ON_SERVER_BUSY_MILLIS = 500;
    public static final long KEEP_AADC_MEMBER_INTERVAL_MILLIS = 60 * 1000;//1 min    
    private static final boolean IS_DIRTY_DATA_LOGGING_ENABLED = 
            SystemPropUtils.getBooleanSystemProp(DIRTY_DATA_LOGGING_SYS_PROP_NAME,false)==true;
    private static final String INITIAL_SAP_NAME = "initial";
    private final Semaphore sem = new Semaphore(1);
    private final MessageProvider msgProvider;
    private final ISslContextFactory sslContextFactory;
    private final java.lang.Object sslContextSemaphore = new java.lang.Object();
    private final List<SapClientOptions> initialSaps;
    private final Map<String,String> headers = new HashMap<>();
    private volatile SSLContext sslContext;
    private volatile SubordinateClient innerClient;
    private volatile IAadcMemberSwitchController currentAadcMemberSwitchHandler;
    private boolean waitingForSwitchAadcMember;
    private AtomicBoolean wasClosed = new AtomicBoolean(false);
    private InterruptedException requestCancellation;
    private long currentDefinitionVersion;
    private long lastInvokeTimeMillis = -1;
    
    private static final class SubordinateClient extends ServiceClient {//Separate client for read manifest rq
        
        private final List<SapClientOptions> initialSaps;
        private final List<SapClientOptions> currentSaps = new ArrayList<>();
        private final Semaphore sem = new Semaphore(1);
        private final Map<String,String> headers = new HashMap<>();
        private final String stationName;
        private final String addressTranslationFilePath;
        private final EAuthType authType;
        private final EPortSecurityProtocol easSecurityProtocol; //security protocol requested by parent EASClient        
        private final SSLContext sslContext;
        private final MessageProvider msgProvider;
        private long lastInvokeTimeMillis = -1;

        public SubordinateClient(final MessageProvider msgProvider,
                                             final LocalTracer localTracer, 
                                             final List<SapClientOptions> initialSaps, 
                                             final String stationName, 
                                             final EAuthType authType, 
                                             final String addressTranslationFilePath,
                                             final SSLContext sslContext) {
            super(localTracer, null, null, null);
            this.msgProvider = msgProvider;
            this.stationName = stationName;
            this.authType = authType;
            this.sslContext = sslContext;
            this.addressTranslationFilePath = addressTranslationFilePath;
            easSecurityProtocol = (sslContext == null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL);
            this.initialSaps = new LinkedList<>(initialSaps);
        }

        @Override
        public XmlObject invokeService(RadixSoapMessage message, EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
            lastInvokeTimeMillis = System.currentTimeMillis();
            return super.invokeService(message, targetAadcMember);
        }

        @Override
        protected boolean checkChangeAadcMemberBeforeStickTimeout(final Integer oldMemberId) {
            return true;
        }        

        public String getScpName() {
            return scpName;
        }
        
        public SubordinateClient(final SubordinateClient src,
                                             final MessageProvider messageProvider, 
                                             final LocalTracer tracer, 
                                             final SSLContext newSslContext) {
            super(tracer, null, null, null);
            this.msgProvider = messageProvider;
            this.stationName = src.stationName;
            this.authType = src.authType;
            this.addressTranslationFilePath = src.addressTranslationFilePath;
            if (newSslContext==null){
                this.sslContext = src.sslContext;
            }else{
                this.sslContext = newSslContext;
            }
            this.easSecurityProtocol = (sslContext == null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL);
            this.initialSaps = new LinkedList<>(src.initialSaps);
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

        public List<SapClientOptions> updateSaps(final long definitionVersion) throws ServiceCallException, InterruptedException {
            final ReadManifestDocument document = ReadManifestDocument.Factory.newInstance();
            ReadManifestRq request = document.addNewReadManifest().addNewReadManifestRq();
            request.setStationName(stationName);
            request.setAuthType(authType);
            sem.acquire();
            headers.clear();
            if (definitionVersion>=0){
                HttpFormatter.appendRadixVersionHeaderAttr(headers, definitionVersion);
            }
            try {
                final ReadManifestMess message = (ReadManifestMess) invokeService(document, headers, null, SYSTEM_ID, null, SERVICE_URI, KEEP_CONNECTION_SEC, RESPONSE_TIMEOUT, null);
                final ReadManifestRs response = message.getReadManifestRs();
                currentSaps.clear();
                final ReadManifestRs.SAPs saps = response.getSAPs();
                scpName = response.getScpName();
                if (saps != null && saps.getItemList() != null) {
                    SapClientOptions sap;
                    EPortSecurityProtocol sapSecurityProtocol;
                    final AddressTranslationTable table = 
                            AddressTranslationTable.parseFile(addressTranslationFilePath, tracer, msgProvider);
                    if (!table.isEmpty()){
                        final String debugMessage = "Address translation table from '%1$s' file:\n%2$s";
                        tracer.debug(msgProvider.translate("EASClient", String.format(debugMessage, addressTranslationFilePath, table.toString())), false);
                    }
                    for (ReadManifestRs.SAPs.Item item : saps.getItemList()) {
                        sapSecurityProtocol = item.getSecurityProtocol() != null ? item.getSecurityProtocol() : EPortSecurityProtocol.NONE;
                        //discard SAP if it has a different security protocol
                        if (sapSecurityProtocol != easSecurityProtocol) {
                            final String sapSecurityProtocolName =
                                    sapSecurityProtocol == EPortSecurityProtocol.NONE ? "no" : sapSecurityProtocol.getName().toLowerCase();
                            final StringBuilder debugMessageBuilder = new StringBuilder();
                            debugMessageBuilder.append("sap with name \"%s\" has %s security protocol, but ");
                            if (easSecurityProtocol == EPortSecurityProtocol.NONE) {
                                debugMessageBuilder.append("no security protocol required.");
                            } else {
                                debugMessageBuilder.append(easSecurityProtocol.getName().toLowerCase());
                                debugMessageBuilder.append(" protocol required.");
                            }
                            debugMessageBuilder.append(" Ignoring this sap");
                            tracer.debug(String.format(debugMessageBuilder.toString(), item.getName(), sapSecurityProtocolName), false);
                            continue;
                        }
                        sap = new SapClientOptions();
                        sap.setName(item.getName());
                        try {
                            if (item.getChannelType() == EChannelType.TCP) {
                                final CompositeInetSocketAddress address =                                 
                                        ValueFormatter.parseCompositeInetSocketAddress(item.getAddress());
                                final CompositeInetSocketAddress replacedAddress = table.translate(scpName, address);
                                if (address.equals(replacedAddress)){
                                    sap.setAddress(new SapAddress(address));
                                }else{                                    
                                    if (replacedAddress==null){
                                        final String debugMessage = 
                                            msgProvider.translate("EASClient","Service access point address %1$s was blocked in address translation table");
                                        tracer.debug(String.format(debugMessage, address), false);
                                        continue;
                                    }else{
                                        final String debugMessage = 
                                            msgProvider.translate("EASClient","Service access point address %1$s was replaced with %2$s in address translation table");
                                        tracer.debug(String.format(debugMessage, address, replacedAddress), false);
                                        sap.setAddress(new SapAddress(replacedAddress));
                                    }
                                }                                
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
                        sap.setAadcMemberId(item.isSetAadcMemberId() ? item.getAadcMemberId().intValue() : null);
                        if (IS_DIRTY_DATA_LOGGING_ENABLED){
                            Map<String,String> attrs = sap.getAdditionalAttrs();
                            if (attrs==null){
                                attrs = new HashMap<>();
                                sap.setAdditionalAttrs(attrs);
                            }
                            attrs.put(ESoapOption.LOG_DIRTY_DATA.getValue(), "true");
                        }
                        currentSaps.add(sap);
                    }
                }
                return new ArrayList<>(currentSaps);
            } catch (ServiceClientException | InterruptedException ex) {
                throw new ServiceCallSendException("Cannot get system  manifest", ex);
            } finally {
                sem.release();
            }
        }

        @Override
        protected void onAllSapsBusy(final FailedSapsInfo info) throws InterruptedException, ServiceCallException {
            if (info.getBusyCount() > 0 && System.currentTimeMillis() - lastInvokeTimeMillis < BUSY_SERVER_WAIT_TIMEOUT_MILLIS) {
                Thread.sleep(SLEEP_ON_SERVER_BUSY_MILLIS);
            }      
            if (info.getInvalidVerCount() > 0){
                throw new NoSapsForCurrentVersion(info, msgProvider);
            }else{
                throw new AllSapsBusyException(info, msgProvider);
            }            
        }
    }

    public EasClient(final IClientEnvironment environment, 
                     final InetSocketAddress initialAddress, 
                     final String stationName, 
                     final EAuthType authType) {
        this(environment, Collections.singletonList(initialAddress), stationName, authType, null, true);
    }

    public EasClient(final IClientEnvironment environment, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType,
                     final String addressTranslationFilePath,
                     final boolean sapDiscoveryEnabled) {
        super(environment.getEasSession().getSessionTrace(), null, null, null, null);
        this.sslContextFactory = null;
        this.msgProvider = environment.getMessageProvider();
        setAadcMemberStickTimeoutMillis(KEEP_AADC_MEMBER_INTERVAL_MILLIS);
        if (sapDiscoveryEnabled){
            initialSaps = null;
            innerClient = new SubordinateClient(msgProvider, 
                                                environment.getEasSession().getSessionTrace(), 
                                                createSapsFromInitialAddresses(initialAddresses, EPortSecurityProtocol.NONE), 
                                                stationName, 
                                                authType, 
                                                addressTranslationFilePath, 
                                                null);            
        }else{
            initialSaps = createSapsFromInitialAddresses(initialAddresses, EPortSecurityProtocol.NONE);
            innerClient = null;
        }
    }

    public EasClient(final IClientEnvironment environment, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType, 
                     final String addressTranslationFilePath,
                     final boolean sapDiscoveryEnabled,
                     final ISslContextFactory sslContextFactory, 
                     final ISecretStore secretStore) throws KeystoreControllerException, CertificateUtilsException {
        this(environment.getMessageProvider(), 
             environment.getEasSession().getSessionTrace(), 
             initialAddresses, 
             stationName, 
             authType, 
             addressTranslationFilePath,
             sapDiscoveryEnabled,
             sslContextFactory, 
             secretStore);
    }

    public EasClient(final MessageProvider msgProvider, 
                     final LocalTracer localTracer, 
                     final List<InetSocketAddress> initialAddresses, 
                     final String stationName, 
                     final EAuthType authType, 
                     final String addressTranslationFilePath,
                     final boolean sapDiscoveryEnabled,
                     final ISslContextFactory sslContextFactory,
                     final ISecretStore secretStore) throws KeystoreControllerException, CertificateUtilsException {
        super(localTracer, null, null, null, null);
        this.sslContextFactory = sslContextFactory;
        this.msgProvider = msgProvider;        
        renewSslContext(secretStore);
        setAadcMemberStickTimeoutMillis(KEEP_AADC_MEMBER_INTERVAL_MILLIS);
        final EPortSecurityProtocol securityProtocol = sslContext==null ? EPortSecurityProtocol.NONE : EPortSecurityProtocol.SSL;
        if (sapDiscoveryEnabled){
            initialSaps = null;
            innerClient = new SubordinateClient(msgProvider, 
                                                                    localTracer, 
                                                                    createSapsFromInitialAddresses(initialAddresses, securityProtocol),
                                                                    stationName, 
                                                                    authType, 
                                                                    addressTranslationFilePath, 
                                                                    sslContext);
        }else{
            initialSaps = createSapsFromInitialAddresses(initialAddresses, securityProtocol);
            innerClient = null;
        }
    }

    EasClient(final MessageProvider messageProvider, final LocalTracer tracer, EasClient src) {
        super(tracer, null, null, null, null);
        this.sslContextFactory = src.sslContextFactory;
        this.sslContext = src.sslContext;
        this.msgProvider = messageProvider;
        setAadcMemberStickTimeoutMillis(KEEP_AADC_MEMBER_INTERVAL_MILLIS);
        if (src.innerClient==null){
            this.innerClient = null;
        }else{
            this.innerClient = new SubordinateClient(src.innerClient, msgProvider, tracer, null);        
        }
        if (src.initialSaps==null){
            this.initialSaps = null;
        }else{
            this.initialSaps = new LinkedList<>(src.initialSaps);
        }
    }

    @Override
    protected List<SapClientOptions> refresh(final Long systemId, 
                                                                   final Long thisInstanceId, 
                                                                   final String serviceUri) throws ServiceCallException {
        try {
            tracer.debug("Updating list of available access points", false);
            final List<SapClientOptions> res;
            synchronized(sslContextSemaphore){
                if (innerClient==null){
                    return initialSaps==null ? Collections.<SapClientOptions>emptyList() : new LinkedList<>(initialSaps);
                }else{
                    res = innerClient.updateSaps(currentDefinitionVersion);
                    scpName = innerClient.getScpName();
                }
            }
            final StringBuilder messageBuilder = new StringBuilder();
            if (res.isEmpty()) {
                if (scpName == null) {
                    messageBuilder.append(String.format(msgProvider.translate("TraceMessage", "for system %s and default profile no saps was found"), systemId));
                } else {
                    messageBuilder.append(String.format(msgProvider.translate("TraceMessage", "for system %s and profile \"%s\" no saps was found"), systemId, scpName));
                }
            } else {
                final StringBuilder sapNames = new StringBuilder();
                for (SapClientOptions sap : res) {
                    sapNames.append(sap.toString());
                    sapNames.append(" ");
                }
                if (scpName == null) {
                    messageBuilder.append(String.format(msgProvider.translate("TraceMessage", "for system %s and default profile following saps was found:\n %s"), systemId, sapNames.toString()));
                } else {
                    messageBuilder.append(String.format(msgProvider.translate("TraceMessage", "for system %s and profile \"%s\" following saps was found:\n %s"), systemId, scpName, sapNames.toString()));
                }                
            }            
            tracer.debug(messageBuilder.toString(), false);
            return res;
        } catch (InterruptedException ex) {
            throw new ServiceCallSendException("Cannot get system  manifest", ex);
        }
    }

    @Override
    public XmlObject invokeService(final RadixSoapMessage message, final EAadcMember targetAadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        lastInvokeTimeMillis = System.currentTimeMillis();
        return super.invokeService(message, targetAadcMember);
    }
    
    @Override
    protected void onAllSapsBusy(final FailedSapsInfo info) throws InterruptedException, ServiceCallException {        
        if (waitingForSwitchAadcMember){
            if (requestCancellation==null){
                super.onAllSapsBusy(info);
            }else{
                throw requestCancellation;
            }
        }else{
            if (info.getBusyCount() > 0 && System.currentTimeMillis() - lastInvokeTimeMillis < BUSY_SERVER_WAIT_TIMEOUT_MILLIS) {
                Thread.sleep(SLEEP_ON_SERVER_BUSY_MILLIS);
            }      
            if (info.getInvalidVerCount()>0){
                throw new NoSapsForCurrentVersion(info, msgProvider);
            }else{
                throw new AllSapsBusyException(info, msgProvider);
            }          
        }
    }

    @Override
    public boolean checkChangeAadcMemberBeforeStickTimeout(final Integer oldMemberId) {
        if (currentAadcMemberSwitchHandler==null){
            waitingForSwitchAadcMember = true;
            return false;
        }else{
            try{
                if (currentAadcMemberSwitchHandler.canSwitch()){
                    return true;
                }else{
                    waitingForSwitchAadcMember = true;
                    return false;
                }                
            }catch(InterruptedException exception){
                waitingForSwitchAadcMember = true;
                requestCancellation = exception;
                return false;                
            }
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
    public final void renewSslContext(final ISecretStore secretStore) throws KeystoreControllerException, CertificateUtilsException {        
        synchronized(sslContextSemaphore){
            if (sslContextFactory!=null){
                this.sslContext = sslContextFactory.createSslContext(secretStore);
                if (innerClient!=null){
                    innerClient = new SubordinateClient(innerClient, msgProvider, tracer, sslContext);
                }
            }
        }
    }
       
    @Override
    public XmlObject sendRequest(final XmlObject request, 
                                                  final String scpName, 
                                                  final long definitionVersion, 
                                                  final int timeoutSec,
                                                  final IAadcMemberSwitchController handler) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {

        if (scpName != null && !scpName.isEmpty() && !scpName.equals(this.scpName)) {
            final String message = String.format(msgProvider.translate("TraceMessage", "changing profile to \"%s\""), scpName);
            tracer.debug(message, false);
            setScpName(scpName);
        }                        
        headers.clear();
        if (definitionVersion > 0) {            
            HttpFormatter.appendRadixVersionHeaderAttr(headers, definitionVersion);
        }        
        sem.acquire();
        currentAadcMemberSwitchHandler = handler==null ? DefaultAadcMemberSwitchController.INSTANCE : handler;
        waitingForSwitchAadcMember = false;
        requestCancellation = null;
        currentDefinitionVersion = definitionVersion;
        try {
            return invokeService(request, headers, null, SYSTEM_ID, null, SERVICE_URI, KEEP_CONNECTION_SEC, timeoutSec, null);
        } finally {
            currentDefinitionVersion = -1;
            try{
                currentAadcMemberSwitchHandler.stopSwitching();
            }finally{
                waitingForSwitchAadcMember = false;
                requestCancellation = null;
                currentAadcMemberSwitchHandler  = null;
                sem.release();
            }
        }
    }

    @Override
    public XmlObject sendCallbackResponse(final XmlObject response, 
                                                                final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {
        sem.acquire();
        try {
            return sendCallbackResponse(response, null, SYSTEM_ID, null, SERVICE_URI, timeoutSec);
        } finally {
            sem.release();
        }
    }

    @Override
    public XmlObject sendFaultMessage(final String message, 
                                                         final Throwable error, 
                                                         final int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, ServiceClientException, InterruptedException {
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

    @Override
    public void close() {
        if (wasClosed.compareAndSet(false, true)){
            if (innerClient!=null){
                innerClient.close();
            }
            super.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
    
    private static List<SapClientOptions> createSapsFromInitialAddresses(final List<InetSocketAddress> initialAddresses,
                                                                                                            final EPortSecurityProtocol securityProtocol){
        final List<SapClientOptions> saps = new LinkedList<>();
        SapClientOptions sap;
        for (InetSocketAddress initialAddress : initialAddresses) {
            sap = new SapClientOptions();
            sap.setName(INITIAL_SAP_NAME + " " + initialAddress.toString());
            sap.setAddress(new SapAddress(initialAddress));
            sap.setPriority(1);
            sap.setBlockingPeriodMillis(600000);
            sap.setConnectTimeoutMillis(10000);
            sap.setSecurityProtocol(securityProtocol);
            if (IS_DIRTY_DATA_LOGGING_ENABLED){
                Map<String,String> attrs = sap.getAdditionalAttrs();
                if (attrs==null){
                    attrs = new HashMap<>();
                    sap.setAdditionalAttrs(attrs);
                }
                attrs.put(ESoapOption.LOG_DIRTY_DATA.getValue(), "true");
            }
            saps.add(sap);
        }
        return saps;
    }
}
