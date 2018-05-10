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
package org.radixware.wps;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.kerberos.KerberosUtils;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.dialogs.IDialogDisplayer;

import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.MessageBox;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.uploading.UploadException;

class HttpSessionContext implements Serializable {

    private static final long serialVersionUID = 0;
    private static int RQ_TOKEN_LEN_IN_BYTES = 16;
    
    volatile WpsApplication application;
    private volatile WpsEnvironment userSession = null;
    private final Queue<HttpQuery> queries = new LinkedList<>();
    private final transient ResponseHandler responseHandler = new ResponseHandler();
    private final Lock wpsLock = new ReentrantLock(true);
    private final Object eventLoopLock = new Object();
    private final WebServerRunParams runParams;
    private volatile boolean eventLoopIsRunning = false;
    private volatile boolean prepareForDispose;
    final String remoteInfo;
    private final HttpConnectionOptions httpConnectionOptions;
    private long lastRsTime = -1;
    private long lastRqTime = -1;
    volatile long rqCheckDelay = 1000 * 15 * 60;
    volatile int processingQuery;
    volatile int finishRequest;    
    private final AtomicBoolean isDisposed = new AtomicBoolean(false);

    final IDialogDisplayer dialogDisplayer = new IDialogDisplayer() {
        @Override
        public DialogResult execModal(Dialog dialog) {
            return execModalDialog(dialog);
        }

        @Override
        public RootPanel getRootPanel() {
            return userSession.getMainWindow();
        }

        @Override
        public WpsEnvironment getEnvironment() {
            return userSession;
        }

        @Override
        public void showModal(Dialog dialog) {
            showModalDialog(dialog);
        }

        @Override
        public void processEvents() {
            final DialogHandler handler;
            synchronized (dialogs) {
                if (!dialogs.isEmpty()) {
                    handler = dialogs.get(dialogs.size() - 1);
                } else {
                    System.out.println("no opened dialogs found");
                    return;
                }
            }
            processAsyncDialogEvents(handler);
        }
    };

    @SuppressWarnings("LeakingThisInConstructor")
    HttpSessionContext(final WpsApplication server, 
                                  final HttpConnectionOptions connectionOptions, 
                                  final String remoteInfo,
                                  final WebServerRunParams runParams) {
        this.application = server;
        this.application.register(this);
        this.httpConnectionOptions = connectionOptions;
        this.remoteInfo = remoteInfo;
        this.runParams = runParams;
    }
    
    void beforeDispose(){
        responseHandler.prepareForDispose();
    }

    private boolean finishRequest() {
        if (userSession == null) {
            return false;
        }
        final IEasSession easSession = userSession.getEasSession();
        if (easSession != null && easSession.isBusy()) {
            easSession.breakRequest();
        }
        if (finishRequest == 1) {
            return false;
        } else if (finishRequest == 0) {
            finishRequest = 100;
        } else {
            finishRequest = 100;
        }
        return true;
    }
    
    public WebServerRunParams getRunParams(){
        return runParams;
    }

    public void dispose() {
        if (processingQuery > 0 && finishRequest()) {//try to return from UIObject.accept method
            return;
        }
        if (isDisposed.compareAndSet(false, true)) {
            WebServer.WsThread wsThread = null;
            try {
                if (userSession != null) {
                    try {                        
                        if (Thread.currentThread() instanceof WebServer.WsThread) {
                            wsThread = (WebServer.WsThread) Thread.currentThread();
                        }
                        try {
                            if (wsThread != null) {
                                wsThread.userSession = userSession;
                            }
                            userSession.dispose();
                        } finally {
                            if (wsThread != null) {
                                wsThread.userSession = null;
                            }
                        }
                    } catch(HttpSessionTerminatedError e){
                        //Already disposed - ignore
                    } catch (Throwable e) {
                        Logger.getLogger(HttpSessionContext.class.getName()).log(Level.SEVERE, "Unexpected exception on context dispose", e);
                    }
                }
            } finally {
                if (application != null) {
                    application.unregister(this);
                    application = null;
                }
            }
            if (wsThread==null){
                notifyDisposed();
            }
        }
    }

    static interface IHttpServletResponseHeaderWriter {

        void writeResponseHeader(HttpServletResponse rs);

        boolean isResponseContentAllowed();
        
        boolean isAuthDataRequested();
    }

    static interface IHttpServletResponseBodyWriter {

        void writeResponseBody(final HttpResponseContent response) throws IOException;
    }

    static interface IAuthDataReceiver {

        void authDataRequested();

        void authDataReady(ClientAuthData authData);
    }
    

    private final class ResponseHandler {                
        
        volatile CountDownLatch requestLatch = null;
        volatile CountDownLatch authLatch = null;
        private final Object progressHandleSemaphore = new Object();
        private final Object preparingForDisposeSemaphore = new Object();
        private WpsProgressHandleManager.ProgressHandleInfo progressHandleInfo = null;
        private IHttpServletResponseBodyWriter responseWriter;
        private IHttpServletResponseHeaderWriter responseHeader;
        private IAuthDataReceiver authDataListener;
        private String rqId;
        private long startProcessTime = 0;
        private boolean timerStarted;
        private ClientAuthData authData;
        private boolean invalidAuthData = false;
        private List<Runnable> scheduledTasks;
        private final SecureRandom random = new SecureRandom();
        private boolean preparingForDispose;
        private String token;        
        private String previousToken;

        private void engage(HttpQuery query) {
            if (this.requestLatch != null) {
                throw new IllegalStateException("Request processing not finished yet");
            }
            this.requestLatch = new CountDownLatch(1);
            rqId = query.getId();
            final ClientAuthData newAuthData = query.getAuthData();
            if (!Objects.equals(newAuthData, authData)){
                authData = newAuthData;
                invalidAuthData = false;
            }
            if (authDataListener != null) {
                authDataListener.authDataReady(authData);
                authDataListener = null;
                if (authLatch != null) {
                    authLatch.countDown();
                    authLatch = null;
                }
            }

            startProcessTime = System.currentTimeMillis();

            enqueueQuery(query);
        }
        
        private void fireWhenReady(HttpServletResponse rs) throws IOException {
            if (this.requestLatch == null) {
                throw new IllegalStateException("Request processor is not engaged");
            }
            try {
                requestLatch.await();
                if (responseHeader != null) {
                    responseHeader.writeResponseHeader(rs);
                    if (authDataListener != null) {
                        authLatch = new CountDownLatch(1);
                        authDataListener.authDataRequested();
                    }
                    if (!responseHeader.isResponseContentAllowed()) {
                        rs.setContentLength(0);
                        rs.flushBuffer();
                        startRqTimeout();
                        return;
                    }
                }
                //hint for IE (see http://msdn.microsoft.com/en-us/library/dd341152.aspx)
                rs.setHeader(HttpHeaderConstants.PERSISTENT_AUTH_HEADER, "true");
                if (!isPreparingForDispose()){
                    this.writeResponseContent(rs);
                } else {
                    FileUtils.writeString(rs.getOutputStream(), "<disposed/>", FileUtils.XML_ENCODING);
                }
                startRqTimeout();
            } catch (InterruptedException ex) {
            } catch (IOException ex) {
                throw ex;
            } finally {
                responseHeader = null;
                this.requestLatch = null;
            }
        }

        private void writeResponseContent(HttpServletResponse rs) throws IOException {
            synchronized (dialogs) {
                for (DialogHandler handler : dialogs) {
                    if (handler.dialog.wasClosed()) {
                        handler.closeWasSent = true;
                    }
                }
            }
            synchronized (this) {
                try {
                    rs.setCharacterEncoding(FileUtils.XML_ENCODING);
                    rs.setContentType("text/xml");

                    final OutputStream out = rs.getOutputStream();
                    try {
                        final BufferedOutputStream stream = new BufferedOutputStream(out);
                        final HttpResponseContent responseContent = 
                            new HttpResponseContent(out, rqId, getNextRequestToken(), startProcessTime);
                        final WpsProgressHandleManager.ProgressHandleInfo progressInfo = getProgressHandleInfo();
                        if (responseWriter != null) {
                            responseWriter.writeResponseBody(responseContent);
                            responseWriter = null;
                        } else if (progressInfo != null) {
                            progressInfo.saveChanges(responseContent);
                            setProgressHandleInfo(null);
                        } else {
                            if (userSession != null) {
                                final RootPanel root = userSession.getMainWindow();
                                try {
                                    root.saveChanges(responseContent);
                                } finally {
                                    stream.flush();
                                    stream.close();
                                }
                                root.notifySent();
                            } else {
                                stream.close();
                            }
                        }

                    } finally {
                        boolean connectionClosed = false;
                        try {
                            out.flush();
                        } catch (IOException exception) {
                            if ("org.apache.catalina.connector.ClientAbortException".equals(exception.getClass().getName())) {
                                Logger.getLogger(HttpSessionContext.class.getName()).log(Level.INFO, "Failed to send response to client: connection was closed");
                                connectionClosed = true;
                            } else {
                                throw exception;
                            }
                        }
                        try {
                            out.close();
                        }catch (IOException exception) {
                            if ("org.apache.catalina.connector.ClientAbortException".equals(exception.getClass().getName())) {
                                if (!connectionClosed){
                                    Logger.getLogger(HttpSessionContext.class.getName()).log(Level.INFO, "Failed to send response to client: connection was closed");
                                }
                            } else {
                                throw exception;
                            }
                        }
                    }

                } catch (IOException ex) {
                    throw ex;
                }
            }        
        }        
        
        private String getNextRequestToken(){   
            if (responseHeader!=null && responseHeader.isAuthDataRequested()){
                previousToken = token;
            }else{
                previousToken = null;
            }
            final byte[] bytes = new byte[RQ_TOKEN_LEN_IN_BYTES];
            random.nextBytes(bytes);
            token = Hex.encode(bytes);
            return token;
        }
        
        boolean checkToken(final String tokenToCheck, final boolean authDataReceived){
            if (previousToken!=null){                
                if (Objects.equals(previousToken, tokenToCheck)
                    || Objects.equals(token, tokenToCheck)){
                    previousToken = null;
                    return true;
                }else{
                    previousToken = null;
                    return false;                    
                }
            }else{
                return Objects.equals(tokenToCheck, this.token);
            }
        }

        void scheduleTask(final Runnable task) {
            if (task != null) {
                if (scheduledTasks == null) {
                    scheduledTasks = new LinkedList<>();
                }
                scheduledTasks.add(task);
            }
        }

        void ready() {
            ready(false);
        }
        
        void onTimeout(){
            ready(true);
        }

        void execScheduledTasks() {
            if (scheduledTasks != null) {
                final List<Runnable> tasks = new LinkedList<>(scheduledTasks);
                scheduledTasks = null;
                for (Runnable task : tasks) {
                    try {
                        task.run();
                    } catch (HttpSessionTerminatedError e){
                        throw e;
                    }catch (Throwable exception) {
                        userSession.getTracer().error(exception);
                    }
                }
            }
        }

        private void ready(final boolean timeout) {
            if (authLatch != null) {
                try {
                    authLatch.await();
                } catch (InterruptedException ex) {
                }
            }
            if (timeout) {
                prepareForDispose();
                dispose();
            }

            if (this.requestLatch == null && !timeout && !isDisposed.get()) {
                throw new HttpSessionTerminatedError();
            }

            if (this.requestLatch != null) {
                if (application!=null 
                    && !application.isInGuiThread()
                    && !isPreparingForDispose()){
                    final IllegalUsageError error = 
                        new IllegalArgumentError("Call of HttpSessionContext.ready from invalid thread");
                    Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected method call", error);
                }                
                this.requestLatch.countDown();
            }
        }

        void startTimer() {
            responseWriter = new IHttpServletResponseBodyWriter() {
                @Override
                public void writeResponseBody(final HttpResponseContent response) throws IOException {
                    final StringBuilder responseBuilder = new StringBuilder("<timer command = \"start\"");
                    response.writeResponseXmlAttrs(responseBuilder, false);
                    responseBuilder.append("/>");
                    response.writeString(responseBuilder.toString());
                }
            };
            timerStarted = true;
            ready();
        }

        boolean stopTimer() {
            if (timerStarted) {
                responseWriter = new IHttpServletResponseBodyWriter() {
                    @Override
                    public void writeResponseBody(final HttpResponseContent response) throws IOException {
                        final StringBuilder responseBuilder = new StringBuilder("<timer command = \"stop\"");
                        response.writeResponseXmlAttrs(responseBuilder, false);
                        responseBuilder.append("/>");
                        response.writeString(responseBuilder.toString());
                    }
                };
                timerStarted = false;
                ready();
                return true;
            } else {
                return false;
            }
        }

        private void signText(final String text, final String certificateDN, final String certificateThumbprint) {
            responseWriter = new IHttpServletResponseBodyWriter() {
                @Override
                public void writeResponseBody(final HttpResponseContent response) throws IOException {
                    final StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("<textsigner");
                    response.writeResponseXmlAttrs(responseBuilder, false);
                    responseBuilder.append(">\n<Text>");
                    responseBuilder.append(text);
                    responseBuilder.append("</Text>\n");
                    responseBuilder.append("<CertificateDN>");
                    responseBuilder.append(certificateDN);
                    responseBuilder.append("</CertificateDN>\n");
                    responseBuilder.append("<CertificateThumbprint>");
                    responseBuilder.append(certificateThumbprint);
                    responseBuilder.append("</CertificateThumbprint>\n");
                    responseBuilder.append("</textsigner>");
                    response.writeString(responseBuilder.toString());
                }
            };
            ready();
        }

        private void jsInvoke(final String nodeId, final String methodName, final String methodParam) {
            responseWriter = new IHttpServletResponseBodyWriter() {
                @Override
                public void writeResponseBody(final HttpResponseContent response) throws IOException {
                    final StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("<jsinvoke");
                    response.writeResponseXmlAttrs(responseBuilder, false);
                    responseBuilder.append(">\n<NodeId>");
                    responseBuilder.append(nodeId);
                    responseBuilder.append("</NodeId>\n");
                    responseBuilder.append("<MethodName>");
                    responseBuilder.append(methodName);
                    responseBuilder.append("</MethodName>\n");
                    if (methodParam != null && !methodParam.isEmpty()) {
                        responseBuilder.append("<MethodParam>");
                        responseBuilder.append(methodParam);
                        responseBuilder.append("\n</MethodParam>\n");
                    }
                    responseBuilder.append("</jsinvoke>");
                    response.writeString(responseBuilder.toString());
                }
            };
            ready();
        }

        void setResponseHeader(final IHttpServletResponseHeaderWriter writer) {
            responseHeader = writer;
        }

        void setAuthDataListener(final IAuthDataReceiver listener) {
            authDataListener = listener;            
        }

        boolean isAuthDataRequested() {
            return authDataListener != null;
        }

        ClientAuthData getLastQueryAuthData() {
            return invalidAuthData ? null : authData;
        }
        
        void markAuthDataAsInvalid(){
            invalidAuthData = true;
        }
        
        WpsProgressHandleManager.ProgressHandleInfo getProgressHandleInfo(){
            synchronized(progressHandleSemaphore){
                return progressHandleInfo;
            }
        }
        
        void setProgressHandleInfo(final WpsProgressHandleManager.ProgressHandleInfo info){
            synchronized(progressHandleSemaphore){
                if (progressHandleInfo!=null && info!=null){                    
                    info.merge(progressHandleInfo);
                }
                progressHandleInfo = info;
            }
        }
        
        private void prepareForDispose(){
            synchronized(preparingForDisposeSemaphore){
                preparingForDispose = true;
            }
        }
        
        private boolean isPreparingForDispose(){
            synchronized(preparingForDisposeSemaphore){
                return preparingForDispose;
            }
        }
    }

    static final class NegotiateAuthResult {

        private final GSSContext context;

        NegotiateAuthResult(final GSSContext context) {
            this.context = context;
        }

        public boolean isNtlmRequested() {
            return context == null;
        }

        public String getUserName() throws KerberosException {
            return context == null ? null : KerberosUtils.extractInitiatorName(context);
        }

        public GSSContext getContext() {
            return context;
        }

        public void dispose() {
            if (context != null) {
                try {
                    context.dispose();
                } catch (GSSException exception) {
                    //ignore;
                }
            }
        }
    }

    static final class BasicAuthResult {

        private String userName;
        private char[] password;

        BasicAuthResult(final String userName, final char[] password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public char[] getPassword() {
            return password;
        }

        public void dispose() {
            Arrays.fill(password, ' ');
        }
    }

    DialogResult execModalDialog(Dialog dialog) {
        synchronized (responseHandler) {
            responseHandler.ready();
        }
        return dialogEventLoop(dialog, true);
    }

    void getAuthDataAsync(IAuthDataReceiver receiver) {
        synchronized (responseHandler) {
            if (responseHandler.getLastQueryAuthData() != null) {
                receiver.authDataReady(responseHandler.getLastQueryAuthData());
                return;
            }
            responseHandler.setAuthDataListener(receiver);
            responseHandler.setResponseHeader(new KrbAuthHttpHeaderWriter((byte[])null));
            userSession.forceEvents();
        }
    }

    ClientAuthData getAuthData(final byte[] token) {
        synchronized (responseHandler) {
            if (responseHandler.isAuthDataRequested()) {
                responseHandler.setAuthDataListener(null);
                responseHandler.setResponseHeader(null);
            }
        }
        if (token == null) {//first time
            userSession.blockEvents();
            startTimer();
        }
        final ClientAuthData authData = getAuthDataImpl(new KrbAuthHttpHeaderWriter(token));
        if (authData == null) {
            final String message
                    = userSession.getMessageProvider().translate("TraceMessage", "No authentication data provided for negotiate schema");
            userSession.getTracer().debug(message);
        }
        return authData;
    }

    private ClientAuthData getAuthDataImpl(final KrbAuthHttpHeaderWriter headerWriter) {
        synchronized (responseHandler) {
            if (responseHandler.getLastQueryAuthData() != null) {
                return responseHandler.getLastQueryAuthData();
            }
            if (!responseHandler.isAuthDataRequested()) {
                responseHandler.setResponseHeader(headerWriter);
                responseHandler.ready();
            }
        }        
        return getQuery(true).getAuthData();
    }

    void completeAuthentication() {
        stopTimer();
        userSession.unblockEvents();
    }

    private void startTimer() {
        synchronized (responseHandler) {
            responseHandler.startTimer();
        }
        getQuery(true);
    }

    private void stopTimer() {
        synchronized (responseHandler) {
            if (!responseHandler.stopTimer()) {
                return;
            }
        }
        getQuery(true);
    }

    void scheduleTask(final Runnable task) {
        synchronized (responseHandler) {
            responseHandler.scheduleTask(task);
        }
    }

    @SuppressWarnings("ConvertToStringSwitch")
    String signText(final String text, final X509Certificate certificate) throws SignatureException {
        final String clientDN, certThumbPrint;
        final HttpQuery query;
        userSession.blockEvents();
        final IProgressHandle progressHandle = userSession.getProgressHandleManager().newProgressHandle();
        progressHandle.startProgress("User Identification", false);
        ((WpsProgressHandleManager) userSession.getProgressHandleManager()).ping();
        try {
            synchronized (responseHandler) {
                if (certificate == null) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "User certificate was not provided");
                    throw new SignatureException(SignatureException.EReason.NO_CERT, message);
                }
                try {
                    if (certificate.getIssuerDN() == null) {
                        clientDN = convert2MozillaDN(certificate.getSubjectDN().getName());
                    } else {
                        clientDN = convert2MozillaDN(certificate.getIssuerDN().getName());
                    }
                } catch (InvalidNameException exception) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Unable to parse certificate distinguished name");
                    throw new SignatureException(SignatureException.EReason.FAILED_TO_GET_CERT_DN, message, exception);
                }
                try {
                    certThumbPrint = CertificateUtils.calcCertificateThumbPrint(certificate);
                } catch (NoSuchAlgorithmException | CertificateEncodingException exception) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Failed to calculate user certificate hash");
                    throw new SignatureException(SignatureException.EReason.FAILED_TO_CALC_CERT_HASH, message, exception);
                }
                responseHandler.signText(text, clientDN, certThumbPrint);
            }
            query = getQuery(true);
        } finally {
            progressHandle.finishProgress();
            userSession.unblockEvents();
        }
        if (!query.getEvents().isEmpty() && org.radixware.wps.rwt.Events.isActionEvent(query.getEvents().get(0).getEventName())) {
            final String actionName
                    = org.radixware.wps.rwt.Events.getActionName(query.getEvents().get(0));
            final String actionParam = query.getEvents().get(0).getEventParam();
            if ("sign".equals(actionName)) {
                return actionParam;
            } else if ("error".equals(actionName)) {
                if ("canceled".equals(actionParam)) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Failed to sign message: operation was canceled by user");
                    throw new SignatureException(SignatureException.EReason.USER_CANCELED, message);
                } else if ("noMatchingCert".equals(actionParam)) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Certificate with distinguished name \"%1$s\" and hash \"%2$s\" was not found");
                    final String formattedMessage = String.format(message, clientDN, certThumbPrint);
                    throw new SignatureException(SignatureException.EReason.REQUESTED_CERT_NOT_FOUND, formattedMessage);
                } else if ("unsupported".equals(actionParam)) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Failed to sign message: operation is not supported");
                    throw new SignatureException(SignatureException.EReason.UNSUPPORTED_OPERATION, message);
                } else if ("internal".equals(actionParam) || actionParam == null || actionParam.isEmpty()) {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Failed to sign message: internal browser error");
                    throw new SignatureException(SignatureException.EReason.FAILED_TO_SIGN, message);
                } else {
                    final String message
                            = userSession.getMessageProvider().translate("ExplorerError", "Failed to sign message: %1$s");
                    final String formattedMessage = String.format(message, actionParam);
                    throw new SignatureException(SignatureException.EReason.FAILED_TO_SIGN, formattedMessage);
                }
            }
        }
        final String message
                = userSession.getMessageProvider().translate("ExplorerError", "Unexpected AJAX request: %1$s");
        final String formattedMessage = String.format(message, query.toString());
        throw new SignatureException(SignatureException.EReason.UNEXPECTED_AJAX_REQUEST, formattedMessage);
    }

    @SuppressWarnings("ConvertToStringSwitch")
    String jsInvoke(final String nodeId, final String methodName, final String methodParam) throws InvocationTargetException {
        final HttpQuery query;
        userSession.blockEvents();
        try {
            synchronized (responseHandler) {
                responseHandler.jsInvoke(nodeId, methodName, methodParam);
            }
            query = getQuery(true);
        } finally {
            userSession.unblockEvents();
        }
        if (!query.getEvents().isEmpty() && org.radixware.wps.rwt.Events.isActionEvent(query.getEvents().get(0).getEventName())) {
            final String actionName
                    = org.radixware.wps.rwt.Events.getActionName(query.getEvents().get(0));
            final String actionParam = query.getEvents().get(0).getEventParam();
            if ("return".equals(actionName)) {
                return actionParam;
            } else if ("error".equals(actionName)) {
                if (actionParam != null && !actionParam.isEmpty()) {
                    throw new InvocationTargetException(null, actionParam);
                } else {
                    throw new InvocationTargetException(null, "Unknown problem");
                }
            } else {
                throw new InvocationTargetException(null, "Unexpected action name: \'" + actionName + "\'");
            }
        } else {
            final String message = "Unexpected AJAX request: %1$s";
            throw new InvocationTargetException(null, String.format(message, query.toString()));
        }
    }

    private static String convert2MozillaDN(final String certificateDN) throws InvalidNameException {
        final StringBuilder dn = new StringBuilder();
        final LdapName ldapName = new LdapName(certificateDN);
        final List<Rdn> rdns = new ArrayList<>(ldapName.getRdns());
        Collections.reverse(rdns);
        for (Rdn rdn : rdns) {
            final NamingEnumeration<? extends Attribute> attributes = rdn.toAttributes().getAll();
            try {
                while (attributes.hasMore()) {
                    final Attribute attribute = attributes.next();
                    if (dn.length() > 0) {
                        dn.append(",");
                    }
                    dn.append(attribute.getID());
                    dn.append("=");
                    final String unescapedValue = String.valueOf(attribute.get());
                    final String escapedValue = String.valueOf(Rdn.escapeValue(unescapedValue));
                    if (escapedValue.equals(unescapedValue)) {
                        dn.append(escapedValue);
                    } else {
                        dn.append("\"");
                        dn.append(unescapedValue);
                        dn.append("\"");
                    }
                }
            } catch (NamingException exception) {
            }
        }
        return dn.toString();
    }

    NegotiateAuthResult negotiateAuthentication(final KerberosCredentials serviceCredentials) {
        final IProgressHandle authProgress = userSession.getProgressHandleManager().newProgressHandle();
        authProgress.startProgress("User Identification", false, true);
        try {
            Thread.sleep(200);//browser need some time to display whait box
            ClientAuthData authData = getAuthData(null);
            if (authData != null && authData.isNtlmAuth()) {
                return new NegotiateAuthResult(null);
            }
            final byte[] firstToken = authData == null ? null : authData.getToken();
            if (firstToken != null && firstToken.length > 0) {
                try {
                    final GSSContext gssContext = serviceCredentials.createSecurityContext();
                    byte[] token;
                    try{
                        token = serviceCredentials.getNextHandshakeToken(gssContext, firstToken);
                    }catch(KerberosException ex){
                        userSession.getTracer().error(ex);
                        return null;
                    }
                    while (token != null && token.length > 0 && !gssContext.isEstablished()) {
                        authData = getAuthData(token);
                        token = authData == null ? null : authData.getToken();
                        if (token != null && token.length > 0) {
                            token = serviceCredentials.getNextHandshakeToken(gssContext, token);
                        }
                    }
                    if (gssContext.isEstablished()) {
                        return new NegotiateAuthResult(gssContext);
                    }
                } catch (KerberosException ex) {
                    completeAuthentication();
                    userSession.processException(new KerberosError(ex));
                }
            }
            return null;
        } catch (InterruptedException ex) {
            return null;
        } finally {
            completeAuthentication();
            authProgress.finishProgress();
        }
    }

    public HttpConnectionOptions getHttpConnectionOptions() {
        return httpConnectionOptions;
    }

    void setAfterError() {
        responseHandler.markAuthDataAsInvalid();
    }
    
    public BasicAuthResult basicAuthentication(final String realm) {
        final ClientAuthData authData;
        userSession.blockEvents();
        try {
            authData = getAuthDataImpl(new KrbAuthHttpHeaderWriter(realm));
        } finally {
            userSession.unblockEvents();
        }
        if (authData == null || !authData.isBasicAuth()) {
            final String message
                    = userSession.getMessageProvider().translate("TraceMessage", "No authentication data provided for basic schema");
            userSession.getTracer().debug(message);
            return null;
        }
        final byte[] token = authData.getToken();

        if (token == null || 0 == token.length) {
            final String message
                    = userSession.getMessageProvider().translate("TraceMessage", "No authentication data provided for basic schema");
            userSession.getTracer().debug(message);
            return null;
        }

        final char[] basicData = ValueConverter.arrByte2arrChar(token, null);
        Arrays.fill(token, (byte) 0);
        final int deviderIndex = Utils.indexOf(basicData, ':', 0);
        if (deviderIndex <= 0) {
            final String message
                    = userSession.getMessageProvider().translate("ExplorerMessage", "Wrong format of authentication data");
            userSession.messageError("Failed to Identify User", message);
            Arrays.fill(basicData, ' ');
            return null;
        }
        if (deviderIndex <= 0 || Utils.indexOf(basicData, ':', deviderIndex + 1) > 0) {
            final String message;
            if (deviderIndex <= 0) {
                message
                        = userSession.getMessageProvider().translate("ExplorerMessage", "Wrong format of authentication data");
            } else {
                message
                        = userSession.getMessageProvider().translate("ExplorerMessage", "Wrong format of authentication data.\nUsername or password may have contained an invalid character");
            }
            userSession.messageError("Failed to Identify User", message);
            Arrays.fill(basicData, ' ');
            return null;
        }
        final int domainDeviderIndex = Utils.indexOf(basicData, '\\', 0);
        final String userName;
        if (domainDeviderIndex > 0 && domainDeviderIndex < deviderIndex) {
            userName = new String(Arrays.copyOf(basicData, domainDeviderIndex));
        } else {
            userName = new String(Arrays.copyOf(basicData, deviderIndex));
        }

        final BasicAuthResult result
                = new BasicAuthResult(userName, Arrays.copyOfRange(basicData, deviderIndex + 1, basicData.length));
        Arrays.fill(basicData, ' ');
        return result;
    }

    void showModalDialog(Dialog dialog) {
        synchronized (responseHandler) {
            responseHandler.ready();
        }
        dialogEventLoop(dialog, false);
    }

    void updateProgressManagerState(final WpsProgressHandleManager.ProgressHandleInfo info) {
        synchronized (responseHandler) {
            responseHandler.setProgressHandleInfo(info);
            if (application==null || !application.isInGuiThread()){
                return;
            }
            responseHandler.ready();
        }
        progressManagerEventLoop();
    }

    public void processRequest(final HttpQuery httpQuery, final HttpServletRequest rq, final HttpServletResponse rs) {
        try {
            wpsLock.lock();
            final long startTime = System.currentTimeMillis();
            responseHandler.engage(httpQuery);
            Logger.getLogger(HttpSessionContext.class.getName()).log(Level.FINE, "Engaged {0} : {1}", new Object[]{String.valueOf(responseHandler.rqId), httpQuery.toString()});
            try {
                responseHandler.fireWhenReady(rs);
            } catch (IOException ex) {
                if (!httpQuery.isDisposeRequest()) {
                    final long overallTime = System.currentTimeMillis() - startTime;
                    final String exceptionStack = ClientException.exceptionStackToString(ex);
                    final Object[] messageParams = new Object[]{exceptionStack, MessageFormat.format("{0,time,mm:ss:S}", overallTime)};
                    Logger.getLogger(HttpSessionContext.class.getName()).log(Level.SEVERE, "Possible lost of connection, will not write response. Disposing context forcedly.\n{0}\nRequest processing time: {1}", messageParams);
                    //RADIX-9819 State of GUI at server does not match to state of GUI at browser now. So disposing context forcedly.
                    dispose();
                }
            }
        } finally {
            wpsLock.unlock();
        }
    }

    private void eventLoopStarted() {
        synchronized (eventLoopLock) {
            eventLoopIsRunning = true;
        }
    }

    private void eventLoopStopped() {
        synchronized (eventLoopLock) {
            eventLoopIsRunning = false;
            ((WebServer.WsThread) Thread.currentThread()).userSession = null;
            Thread.currentThread().setContextClassLoader(null);
        }
    }

    private void startEventLoop() {
        this.application.server.threadPool.submit(new Runnable() {
            @Override
            public void run() {

                eventLoop();
            }
        });
    }
    
    private void notifyDisposed(){
        synchronized (queries) {
            queries.add(HttpQuery.DISPOSE_QUERY);
            queries.notify();
        }
    }

    private void enqueueQuery(HttpQuery query) {
        synchronized (queries) {
            queries.add(query);
            queries.notifyAll();
        }
        synchronized (eventLoopLock) {
            if (!eventLoopIsRunning) {
                startEventLoop();
            }
        }
    }

    private void startRqTimeout() {
        lastRsTime = System.currentTimeMillis();
    }

    private HttpQuery getQuery(boolean wait) {
        synchronized (queries) {
            if (finishRequest > 1) {
                if (finishRequest()) {
                    return HttpQuery.DISPOSE_QUERY;
                } else {
                    dispose();
                    return null;
                }
            }
            if (isDisposed.get()) {
                return null;
            }
            for (;;) {
                if (queries.isEmpty()) {
                    if (!wait) {
                        return HttpQuery.NO_QUERY;
                    }
                    try {
                        if (!waitForNextRequest()) {//timeout
                            responseHandler.onTimeout();
                            return null;
                        }
                    } catch (InterruptedException ex) {
                        return null;
                    }
                } else {
                    lastRqTime = System.currentTimeMillis();
                    HttpQuery query = queries.remove();
                    if (query != null && query.isDisposeRequest()) {
                        /*isDisposed = true;
                         if (userSession != null && userSession.getEasSession()!=null) {
                         try {
                         if (userSession.getEasSession().isBusy()){
                         userSession.getEasSession().breakRequest();
                         }else{                                
                         userSession.getConfigStore().syncDb(false);
                         }
                         } catch (Throwable e) {
                         userSession.getTracer().error(e);
                         }
                         }*/

                        responseHandler.ready();
                        dispose();
                        return null;
                    } else {
                        return query;
                    }
                }
            }
        }
    }

    private boolean waitForNextRequest() throws InterruptedException {
        synchronized (queries) {
            long startTime = System.currentTimeMillis();
            long awaitTime = this.rqCheckDelay;
            try {
                queries.wait(awaitTime);
                if (System.currentTimeMillis() - startTime > awaitTime) {
                    return false;
                } else {
                    return true;
                }
            } catch (InterruptedException e) {
                return false;
            }
        }
    }
    private final List<DialogHandler> dialogs = new LinkedList<>();

    private class DialogHandler implements Callable<DialogResult> {

        private final Dialog dialog;
        private boolean closeWasSent = false;
        private final boolean asyncMode;

        public boolean closeWasSent() {
            return closeWasSent;
        }

        public DialogHandler(Dialog dialog, boolean asyncMode) {
            this.dialog = dialog;
            this.asyncMode = asyncMode;
        }

        @Override
        public DialogResult call() {
            for (;;) {
                HttpQuery query = getQuery(!asyncMode);
                if (asyncMode && query == HttpQuery.NO_QUERY) {
                    return null;
                }                
                if (query == null || query==HttpQuery.DISPOSE_QUERY) {//THREAD WAS INTERRUPTED
                    throw new HttpSessionTerminatedError();
                } else {

                    if (dialog != null) {
                        if (dialog.wasClosed()) {
                            return dialog.getDialogResult();
                        } else {
                            try {
                                responseHandler.execScheduledTasks();
                                dialog.accept(query);
                                if (dialog.wasClosed() && closeWasSent()) {
                                    return dialog.getDialogResult();
                                }
                            } catch(HttpSessionTerminatedError e){
                                e.trace();
                            }catch (Throwable e) {
                                //ignore
                                Logger.getLogger(HttpSessionContext.class.getName()).log(Level.FINE, "Error on request processing", e);
                            }

                            responseHandler.ready();
                            if (asyncMode && dialog.wasClosed()) {//now dialog was sent                               
                                return dialog.getDialogResult();
                            }
                        }
                    }
                }
            }
        }
    }

    private DialogResult dialogEventLoop(final Dialog dialog, boolean exec) {
        if (exec) {
            final DialogHandler handler = new DialogHandler(dialog, false);

            synchronized (dialogs) {
                dialogs.add(handler);
            }
            try {

                return handler.call();

            } finally {
                synchronized (dialogs) {
                    dialogs.remove(handler);
                }
            }
        } else {
            //always returns null, because async
            final DialogHandler handler = new DialogHandler(dialog, true);

            synchronized (dialogs) {
                dialogs.add(handler);
            }
            return processAsyncDialogEvents(handler);
        }
    }

    private DialogResult processAsyncDialogEvents(final DialogHandler handler) {
        if (handler.asyncMode) {
            if (handler.dialog.isNotModalExec()) {
                HttpQuery query = getQuery(true);
                if (query==null || query==HttpQuery.DISPOSE_QUERY){
                    throw new HttpSessionTerminatedError();
                }
                if (query != HttpQuery.NO_QUERY) {
                    handler.dialog.accept(query);
                }                
                return DialogResult.NONE;
            } else {
                DialogResult result = handler.call();
                if (result != null) {
                    synchronized (dialogs) {
                        dialogs.remove(handler);
                    }
                    ((WpsProgressHandleManager) userSession.getProgressHandleManager()).unblockProgress(handler.dialog);
                    return handler.dialog.getDialogResult();
                }
                return result;
            }
        } else {
            Logger.getLogger(HttpSessionContext.class.getName()).log(Level.FINE, "dialog is not async");
            return null;
        }
    }

    String getRootId() {
        return userSession != null ? userSession.getMainWindow().getHtmlId() : null;
    }

    private void progressManagerEventLoop() {
        final HttpQuery query = getQuery(true);
        if (query != null) {//THREAD WAS NOT INTERRUPTED 
            final WpsProgressHandleManager.WpsProgressHandle progressHandle;
            if (userSession==null || userSession.getProgressHandleManager()==null){
                progressHandle = null;                            
            }else{
                progressHandle = 
                    (WpsProgressHandleManager.WpsProgressHandle)userSession.getProgressHandleManager().getActive();
            }
            if (progressHandle!=null){
                progressHandle.processRequest(query);
            }
        }
    }
    
    private static final long SYNC_SETTINGS_INTERVAL = 600000;

    boolean wasDisposed() {
        return isDisposed.get();
    }

    private void eventLoop() {
        eventLoopStarted();
        try {
            for (;;) {
                HttpQuery query;
                synchronized (eventLoopLock) {
                    query = getQuery(false);
                    if (query == HttpQuery.NO_QUERY) {
                        return;
                    }
                }
                if (query == null || query==HttpQuery.DISPOSE_QUERY) {//THREAD WAS INTERRUPTED           
                    return;
                } else {
                    try {
                        if (userSession == null) {
                            userSession = new WpsEnvironment(this);
                            ((WebServer.WsThread) Thread.currentThread()).userSession = userSession;
                            //server.register(userSession);
                        } else {
                            ((WebServer.WsThread) Thread.currentThread()).userSession = userSession;
                            userSession.checkThreadState();
                        }
                        responseHandler.execScheduledTasks();
                        processingQuery++;
                        try {
                            userSession.getMainWindow().accept(query);
                        } finally {
                            processingQuery--;
                        }
                        if (finishRequest > 0) {
                            if (processingQuery == 0 || finishRequest == 1) {
                                dispose();
                            } else {
                                continue;
                            }
                        }
                        if (!isDisposed.get()) {//my be disposed if modal dialog timeeut occurs                            
                            if (userSession.getUserName()==null){
                                userSession.getMainWindow().notifyOldVersion(false);
                            }else{
                                userSession.getMainWindow().notifyOldVersion(isNewVersionAvailable());
                            }
                            if (System.currentTimeMillis() - userSession.getConfigStore().lastSyncTime > SYNC_SETTINGS_INTERVAL
                                    && userSession.getEasSession() != null
                                    && !userSession.getEasSession().isBusy()
                                    && userSession.getDefManager().getAdsVersion().isSupported()) {
                                userSession.getConfigStore().syncDb(false);
                            }
                        }
                    } 
                    catch (HttpSessionTerminatedError e){
                        e.trace();
                    }catch (Throwable e) {
                        if (userSession != null) {
                            userSession.getTracer().error(e);
                            userSession.messageException("Unhandled Exception", "Unhandled Exception", e);
                        } else {
                            MessageBox.exceptionBox(dialogDisplayer, e).execDialog();
                        }

                    } finally {
                        responseHandler.ready();
                    }
                }
            }
        } finally {
            eventLoopStopped();
        }
    }

    public EIsoLanguage getLanguage() {
        return EIsoLanguage.ENGLISH;
    }

    void requestUpload(String itemId, String uploadId, Object wrapper) {
        application.server.requestUpload(itemId, uploadId, wrapper);
        responseHandler.ready();
        final HttpQuery query = getQuery(true);
        userSession.getMainWindow().accept(query);
    }

    @SuppressWarnings("ConvertToStringSwitch")
    WebServer.UploadSystemHandler startUpload(final String uploadContextId, final String uploadId, final String fileName) throws UploadException {
        final WebServer.UploadSystemHandler handler
                = application.server.requestUpload(uploadContextId, uploadId);
        responseHandler.ready();
        final HttpQuery query = getQuery(true);//check if "upload-started" event
        if (!query.getEvents().isEmpty() && org.radixware.wps.rwt.Events.isActionEvent(query.getEvents().get(0).getEventName())) {
            final String actionName
                    = org.radixware.wps.rwt.Events.getActionName(query.getEvents().get(0));
            final String actionParam = query.getEvents().get(0).getEventParam();
            if ("upload-started".equals(actionName)) {
                userSession.getMainWindow().accept(query);
                return handler;
            } else if ("upload-rejected".equals(actionName)) {
                userSession.getMainWindow().accept(query);
                handler.dispose();
                if (actionParam != null && !actionParam.isEmpty()) {
                    throw new UploadException(actionParam, fileName);
                } else {
                    throw new UploadException("unknown reason", fileName);
                }
            } else {
                handler.dispose();
                throw new UploadException("Unexpected action name: \'" + actionName + "\'", fileName);
            }
        } else {
            handler.dispose();
            final String message = "Unexpected AJAX request: %1$s";
            throw new UploadException(String.format(message, query.toString()), fileName);
        }
    }

    void disposeUpload(String itemId, String uploadId) {
        application.server.disposeUpload(itemId, uploadId);
    }

    boolean isNewVersionAvailable() {
        long currentVersion = application.getAdsVersionNumber();
        if (currentVersion >= 0) {
            return application.getDefManager().getAdsVersion().getTargetVersionNumber()>0;
        } else {
            return false;
        }
    }
    
    boolean checkQueryToken(final HttpQuery query){
        return responseHandler.checkToken(query.getToken(),  query.getAuthData()!=null);
    }
}
