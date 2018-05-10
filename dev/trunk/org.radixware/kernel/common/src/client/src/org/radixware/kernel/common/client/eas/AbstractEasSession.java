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

import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.ietf.jgss.GSSException;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.auth.ClientAuthUtils;
import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.dialogs.IChangePasswordDialog;
import org.radixware.kernel.common.client.dialogs.IEnterPasswordDialog;
import org.radixware.kernel.common.client.dialogs.ISelectEasSessionDialog;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.resources.TerminalResources;
import org.radixware.kernel.common.client.errors.AuthError;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.errors.MaxNumberOfSessionsExceededError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.PasswordExpiredError;
import org.radixware.kernel.common.client.errors.SessionDoesNotExistError;
import org.radixware.kernel.common.client.errors.UniqueConstraintViolationError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.errors.UserAccountLockedError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.NoSapsForCurrentVersion;
import org.radixware.kernel.common.client.exceptions.ServiceAuthenticationException;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.client.types.AggregateFunctionCall;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.CryptoUtils;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.trace.LocalTracer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallRecvException;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ShouldNeverHappenError;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.clientstate.ConnectionParams;
import org.radixware.schemas.eas.*;//NOPMD
import org.radixware.schemas.easWsdl.GetSecurityTokenDocument;


public abstract class AbstractEasSession implements IEasSession {
    
    public interface IResponseNotificationScheduler{
        void block();
        void unblock();
        void scheduleNotificationTask(Runnable task);
    }
    
    private final static class DefaultResponseNotificationScheduler implements IResponseNotificationScheduler{
        
        private final java.lang.Object semaphore = new java.lang.Object();
        private final List<Runnable> scheduledTasks = new LinkedList<>();
        private final IClientEnvironment environment;
        private int blockCounter;
        
        public DefaultResponseNotificationScheduler(final IClientEnvironment environment){            
            this.environment = environment;
        }

        @Override
        public void block() {
            synchronized(semaphore){
                blockCounter++;
            }
        }

        @Override
        public void unblock() {            
            synchronized(semaphore){
                blockCounter--;
                if (blockCounter==0){
                    final List<Runnable> tasks;
                    tasks = new LinkedList<>(scheduledTasks);
                    scheduledTasks.clear();
                    if (!tasks.isEmpty()){
                        runTasks(tasks);
                    }                    
                }
            }
        }
        
        @Override
        public void scheduleNotificationTask(final Runnable task) {            
            synchronized(semaphore){
                final List<Runnable> tasks;
                if (blockCounter>0){
                    scheduledTasks.add(task);
                    tasks = null;
                }else{
                    tasks = Collections.singletonList(task);
                }
                if (tasks!=null){
                    runTasks(tasks);
                }                
            }
        }
        
        private void runTasks(final List<Runnable> tasks){            
            for (Runnable task: tasks){
                try{
                    task.run();
                }catch (RuntimeException ex) {
                    final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
                    final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
                    environment.getTracer().error(String.format(message, exceptionStr));
                }
            }
        }
    }
    
    private static final class EnterPasswordDialogTask implements Callable<String>{
        
        private final String confirmTitle;
        private final String confirmMessage;
        private final IClientEnvironment environment;
        private final boolean isGuiThread;
        private final boolean confirmToDisconnect;
        private final String title;        
        
        public EnterPasswordDialogTask(final IClientEnvironment environment,
                                                         final String title,
                                                         final boolean confirmToDisconnect){
            final MessageProvider mp = environment.getMessageProvider();
            confirmTitle = mp.translate("ExplorerMessage", "Confirm to Disconnect");
            confirmMessage = mp.translate("ExplorerMessage", "Do you really want to disconnect?\nIf you answer 'Yes' all unsaved data will be lost.");
            this.title = title;
            this.environment = environment;
            this.confirmToDisconnect = confirmToDisconnect;
            isGuiThread = environment.getApplication().isInGuiThread();
        }

        @Override
        public String call() throws Exception {
            environment.getProgressHandleManager().blockProgress();
            try{
                for(int i=1; i<=100; i++){
                    final IEnterPasswordDialog passwordDialog =
                        environment.getApplication().getDialogFactory().newEnterPasswordDialog(environment);        
                    passwordDialog.setMessage(title);
                    if (passwordDialog.execDialog() == DialogResult.ACCEPTED){
                        return passwordDialog.getPassword();
                    }
                    if (!confirmToDisconnect || environment.messageConfirmation(confirmTitle, confirmMessage)){
                        if (!isGuiThread){
                            environment.processException(new CredentialsWasNotDefinedError());
                        }
                        return null;
                    }
                }
                return null;
            }finally{
                environment.getProgressHandleManager().unblockProgress();
            }            
        }        
    }    
    
    private static final class SelectSessionsDialogTask implements Callable<List<String>>{
        
        private final int maxNumber;
        private final List<SessionDescription> sessions;
        private final IClientEnvironment environment;
        
        public SelectSessionsDialogTask(final IClientEnvironment environment, final int maxSessions, final List<SessionDescription> selectFrom){
            this.environment = environment;
            this.maxNumber = maxSessions;
            this.sessions = selectFrom;
        }

        @Override
        public List<String> call() throws Exception {
            final ISelectEasSessionDialog dialog = 
                environment.getApplication().getDialogFactory().newSelectEasSessionDialog(environment, null);
            dialog.setSessionsList(sessions);
            dialog.setSelectionSize(sessions.size()-maxNumber+1);
            environment.getProgressHandleManager().blockProgress();
            try{
                if (dialog.execDialog()==DialogResult.ACCEPTED){
                    return dialog.getSelectedSessionIds();
                }else{
                    return Collections.emptyList();
                }
            }finally{
                environment.getProgressHandleManager().unblockProgress();
            }                
        }
        
    }
    
    private static final class ChangePasswordDialogResult{
        private final String oldPassword;
        private final String newPassword;
        
        public ChangePasswordDialogResult(final String oldPwd, final String newPwd){
            this.oldPassword = oldPwd;
            this.newPassword = newPwd;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }                
    }
    
    private static final class ChangePasswordDialogTask implements Callable<ChangePasswordDialogResult>{
        
        private final IClientEnvironment environment;
        private final String title;
        private final String message;
        private final org.radixware.kernel.common.auth.PasswordRequirements passwordRequiments;
        
        public ChangePasswordDialogTask(final IClientEnvironment env,                                         
                                        final org.radixware.kernel.common.auth.PasswordRequirements passwordRequiments){
            this.environment = env;
            this.passwordRequiments = passwordRequiments;
            title = environment.getMessageProvider().translate("ExplorerMessage", "Your Password has Expired!");
            message = environment.getMessageProvider().translate("ExplorerMessage", "Please change password or press cancel to exit");            
        }

        @Override
        public ChangePasswordDialogResult call() throws Exception {
            final IChangePasswordDialog dialog =
                environment.getApplication().getDialogFactory().newChangePasswordDialog(environment);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setPasswordRequirements(passwordRequiments);
            environment.getProgressHandleManager().blockProgress();
            try{
                if (dialog.execDialog()==DialogResult.ACCEPTED){
                    return new ChangePasswordDialogResult(dialog.getOldPassword(), dialog.getNewPassword());
                }else{
                    return null;
                }
            }finally{
                environment.getProgressHandleManager().unblockProgress();
                dialog.clear();
            }
        }
    }
    
    private static final class SignTextTask implements Callable<String>{
        
        private final IClientEnvironment environment;
        private final String textToSign;
        private final X509Certificate certificate;
        
        public SignTextTask(final IClientEnvironment env, final String text, final X509Certificate userCer){
            environment = env;
            textToSign = text;
            certificate = userCer;
        }

        @Override
        public String call() throws Exception {
            return environment.signText(textToSign, certificate);
        }                
    }

    private static final int INFINITE_RESPONSE_TIMEOUT = 0;
    private static final int MAX_REPEAT_RO_REQUEST=2;
    
    private static final List<Class<? extends XmlObject>> READONLY_RQ_MESSAGE_CLASSES  = 
                    Arrays.<Class<? extends XmlObject>>asList(ListEdPresVisibleExpItemsMess.class,
                                                                                        SelectMess.class,
                                                                                        ReadMess.class,
                                                                                        GetObjectTitlesMess.class,
                                                                                        PrepareCreateMess.class,
                                                                                        ListInstantiatableClassesMess.class,
                                                                                        CalcSelectionStatisticMess.class,
                                                                                        SetParentMess.class,
                                                                                        GetPasswordRequirementsMess.class,
                                                                                        GetDatabaseInfoMess.class
                                                                                        );
    
    volatile ITokenCalculator tokenCalculator;
    volatile RequestExecutor executor;
    volatile AbstractEasSessionParameters params;
    private volatile DatabaseInfo databaseInfo = DatabaseInfo.EMPTY_INSTANCE;
    private volatile EasMessageProcessor messageProcessor;
    private volatile EasMessageProcessor previousMessageProcessor;
    private volatile boolean opened;
    private volatile boolean canRestore = true;    
    private volatile boolean processShouldChangePassword = true;
    private volatile boolean credentialsDefined = true;
    private volatile SessionRestorePolicy.Enum sessionRestorePolicy;
    private volatile MessageProvider mp;
    private int isBusy;
    private final List<IEasSession.Listener> listeners = new LinkedList<>();
    private final java.lang.Object semaphore = new java.lang.Object();
    private final ISecretStore serverKeyStore;
    private final ISecretStore secretStore;
    private final IClientEnvironment environment;
    private final ClientTracer tracer;
    private final EasTrace easTrace;
    private final AbstractEasSession masterSession;
    private final IResponseNotificationScheduler scheduler;
    
    public AbstractEasSession(final IClientEnvironment environment, 
                                          final AbstractEasSession masterSession) {
        this(environment, null, null, masterSession);
    }    

    public AbstractEasSession(final IClientEnvironment environment, 
                                          final IResponseNotificationScheduler scheduler,
                                          final AbstractEasSession masterSession) {
        this(environment, null, scheduler, masterSession);
    }
    
    public AbstractEasSession(final IClientEnvironment environment, 
                                          final ISecretStore secretStore, 
                                          final AbstractEasSession masterSession) {
        this(environment, secretStore, null, masterSession);
    }    

    public AbstractEasSession(final IClientEnvironment environment, 
                                          final ISecretStore secretStore, 
                                          final IResponseNotificationScheduler scheduler,
                                          final AbstractEasSession masterSession) {
        this.environment = environment;
        this.easTrace = new EasTrace(environment);
        this.secretStore = secretStore;
        tracer = environment.getTracer();
        mp = environment.getMessageProvider();
        serverKeyStore = environment.getApplication().newSecretStore();
        if (scheduler==null){
            if (masterSession==null){
                this.scheduler = new DefaultResponseNotificationScheduler(environment);
            }else{
                this.scheduler = masterSession.scheduler;
            }
        }else{
            this.scheduler = scheduler;
        }
        this.masterSession = masterSession;
    }

    @Override
    public LocalTracer getSessionTrace() {
        return easTrace;
    }        
    
    protected final XmlObject send(final RequestHandle handler, final boolean resend) throws ServiceClientException, InterruptedException {
        if (executor == null || params==null) {
            throw new ServiceClientException(mp.translate("ClientSessionException", "Can't send request to server: session is not opened"));
        }
        if (!credentialsDefined){
            throw new CredentialsWasNotDefinedError();
        }
        
        if (!isOpened() && !isCreateSessionRequest(handler) && !isLoginRequest(handler) && !isCloseSessionRequest(handler)){
            if (isInteractive()){
                final CreateSessionRs response = restore(resend, true);
                if (response==null){
                    credentialsDefined = false;
                    throw new CredentialsWasNotDefinedError();
                }else {
                    return response;
                }
            }else{
                createSession(resend);
                return send(handler, resend);
            }
        }
        
        if (resend){
            return execRequestAndSubmitNext(handler, true);
        }else{
            scheduler.block();
            try{                
                for (int tryNum=0; tryNum<=MAX_REPEAT_RO_REQUEST; tryNum++){
                    try{
                        return execRequestWithNotifications(handler);
                    }catch(ServiceCallSendException | ServiceCallRecvException exception){
                        if (isReadOnlyRequest(handler.getExpectedResponseMessageClass())
                            && tryNum<MAX_REPEAT_RO_REQUEST){
                            tracer.debug(environment.getMessageProvider().translate("TraceMessage", "Connection problem"), exception);
                        }else{
                            throw exception;
                        }
                    }
                }
                throw new ShouldNeverHappenError("");
            }finally{
                scheduler.unblock();
            }
        }
    }
    
    private XmlObject execRequestWithNotifications(final RequestHandle handler) throws ServiceClientException, InterruptedException {
        synchronized (semaphore) {
            isBusy++;
            if (isBusy == 1) {
                notifyBeforeProcess(handler);
            }                
        }
        try{
            return execRequestAndSubmitNext(handler, false);
        }finally{
            synchronized (semaphore) {
                isBusy--;
                if (isBusy == 0) {
                    notifyAfterProcess(handler);
                }
            }            
        }
    }
    
    private XmlObject execRequestAndSubmitNext(final RequestHandle handler, final boolean resend) throws ServiceClientException, InterruptedException {
        try{
            return execRequestAndFreeResources(handler, resend);
        }finally{
            if (executor != null) {//session may be closed at this moment
                executor.afterResponseWasReceived();
            }
        }
    }
    
    private XmlObject execRequestAndFreeResources(final RequestHandle handler, final boolean resend)throws ServiceClientException, InterruptedException {
        try{
            return execRequest(handler, resend);
        }finally{
            try{
                TerminalResources.getInstance(environment).freeAllResources(environment);
            }catch(TerminalResourceException exception){
                final String message = mp.translate("ExplorerError", "Can't free terminal resources");
                tracer.error(message, exception);                
            }
        }
    }        
    
    @SuppressWarnings("unchecked")
    private XmlObject execRequest(final RequestHandle handler, final boolean resend)throws ServiceClientException, InterruptedException {
        final XmlObject result;
        final AbstractTraceBuffer nativeBuffer = tracer.getBuffer();
        easTrace.clear();
        easTrace.getBuffer().setMaxSize(nativeBuffer.getMaxSize());
        
        try {            
            result = executor.execute(handler);
            //Put into explorer trace all messages from EAS-CLIENT trace
            //except of last one
            nativeBuffer.startMerge(easTrace.getBuffer());
            //preparse response and write server trace messages into explorer trace:
            return preprocessResponse(result, nativeBuffer);
        } catch (ServiceCallFault fault) {
            traceFault(fault,nativeBuffer);
            if (fault.getFaultString().equals(ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION.toString())) {//RADIX-3193
                throw new UnsupportedDefinitionVersionError(mp, fault);
            } else if (fault.getFaultString().equals(ExceptionEnum.INVALID_PASSWORD.toString())) {//RADIX-5082
                if (!resend
                        && params.getAuthType() != EAuthType.KERBEROS
                        && handler.getExpectedResponseMessageClass() != ChangePasswordMess.class
                        && handler.getExpectedResponseMessageClass() != LoginMess.class
                        && handler.getExpectedResponseMessageClass() != CloseSessionMess.class
                        && handler.getExpectedResponseMessageClass() != CreateSessionMess.class) {
                    return processInvalidPassswordFault(handler);
                } else {
                    throw fault;
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.USER_ACCOUNT_LOCKED.toString())) {//TWRBS-1994
                final String message = mp.translate("ExplorerError", "Account for user %s is locked");
                throw new UserAccountLockedError(String.format(message, fault.getMessage()), fault);
            } else if (fault.getFaultString().equals(ExceptionEnum.UNIQUE_CONSTRAINT_VIOLATION.toString())) {//RADIX-2487
                throw new UniqueConstraintViolationError(environment, fault);
            } else if (fault.getFaultString().equals(ExceptionEnum.KERBEROS_AUTHENTICATION_FAILED.toString())) {
                if (!resend) {
                    return processKerberosAuthFault(fault, handler);
                } else {
                    throw fault;
                }
            } else {
                throw fault;
            }
        } catch(PasswordExpiredError error){
            if (error.getSouceFault()!=null){
                traceFault(error.getSouceFault(),nativeBuffer);
            }            
            if (isInteractive() && !isCloseSessionRequest(handler)){
                final org.radixware.kernel.common.auth.PasswordRequirements requirements =
                    error.getPasswordRequirements(params.getUserName());
                if (processShouldChangePasswordFault(requirements)){
                    return send(handler, false);
                } else {
                    throw error;
                }
            }else{
                throw error;
            }            
        } catch (SessionDoesNotExistError error){
            if (error.getSouceFault()!=null){
                traceFault(error.getSouceFault(),nativeBuffer);
            }
            if (!resend) {
                try {
                    return processSessionDoesNotExistFault(error, handler);
                } catch (KerberosError krbErr) {
                    if (isInteractive() && !isCloseSessionRequest(handler)){
                        environment.processException(krbErr);
                    }else{
                        tracer.error(krbErr);
                    }
                    throw error;
                }
            } else {
                throw error;
            }
        } catch (NoSapsForCurrentVersion error){
            throw new UnsupportedDefinitionVersionError(mp, error, environment.getApplication().getRuntimeEnvironmentType()==ERuntimeEnvironmentType.WEB);
        } catch (EasError error){
            if (error.getSouceFault()!=null){
                traceFault(error.getSouceFault(), nativeBuffer);
            }
            throw error;
        } finally {
            if (nativeBuffer.getMergeWith() != null) {
                //Put into explorer trace the last message from EAS-Client
                //which contains full server response
                nativeBuffer.finishMerge();
            } else {
                nativeBuffer.add(easTrace.getBuffer());
            }
            //Flush all explorer trace messages
            easTrace.clear();
        }
    }        
    
    @SuppressWarnings("unchecked")
    private void traceFault(final ServiceCallFault fault, final AbstractTraceBuffer nativeBuffer){
        //Put into explorer trace all messages from EAS-CLIENT trace
        nativeBuffer.add(easTrace.getBuffer());
        easTrace.clear();        
        if (fault.getTrace() != null) {//Write server trace
            EEventSeverity severity;
            for (ServiceCallFault.TraceItem item : fault.getTrace()) {
                severity = EEventSeverity.getForName(item.level);
                try {
                    final java.lang.Object parts[] = TraceItem.parseMess(XmlUtils.parseSafeXmlString(item.text));
                    if (tracer.getProfile().itemMatch(severity, (String)parts[3])) {
                        nativeBuffer.put(severity, parts);
                    }
                } catch (ParseException ex) {
                    final String errorMessage = mp.translate("ExplorerError", "Can't parse trace message \'%s\':\n%s");
                    final String itemText = String.format(errorMessage, item.text, ex.toString());
                    if (tracer.getProfile().itemMatch(EEventSeverity.ERROR,EEventSource.CLIENT.getValue())) {
                        nativeBuffer.put(severity,itemText,EEventSource.CLIENT.getValue());
                    }
                }
            }
        }
        //Write client error
        final String faultMessageTitle = mp.translate("ExplorerError", "Service call fault.");
        final String traceMessage = faultMessageTitle + "\ncode: \'" + fault.getFaultCode() + "\'\nmessage: \'" + fault.getFaultString() + "\'" + "\ndetail:\n" + fault.getDetail().toString();
        final EEventSeverity severity = ClientException.getFaultSeverity(fault);
        if (tracer.getProfile().itemMatch(severity,EEventSource.CLIENT.getValue())) {
            nativeBuffer.put(severity,traceMessage,EEventSource.CLIENT.getValue());
        }
    }
    
    private boolean processShouldChangePasswordFault(final org.radixware.kernel.common.auth.PasswordRequirements requirements) {
        while (true) {
            final ChangePasswordDialogResult result = askForChangePassword(requirements);
            if (result==null){
                return false;
            }
            try {
                changePassword(result.getOldPassword(), result.getNewPassword());
                break;
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } catch (ServiceCallFault fault) {
                final String title = mp.translate("ChangePasswordDialog", "Can`t Change Password");
                if (org.radixware.schemas.eas.ExceptionEnum.INVALID_PASSWORD.toString().equals(fault.getFaultString())) {
                    final String message = mp.translate("ChangePasswordDialog", "Current password is not correct");
                    environment.messageError(title, message);
                } else {
                    environment.processException(title, fault);
                }
            } catch (ServiceClientException ex) {
                final String title = mp.translate("ChangePasswordDialog", "Can`t Change Password");
                environment.processException(title, ex);
            }
        }
        final String title = mp.translate("ExplorerMessage", "Success!");
        final String message = mp.translate("ExplorerMessage", "Your password was successfully updated!");
        environment.messageInformation(title, message);
        return true;
    }
    
    @Override
    public void changePassword(final String oldPassword, final String newPassword) throws ServiceClientException, InterruptedException {
        final ChangePasswordRq request = ChangePasswordRq.Factory.newInstance();
        final EasMessageProcessor savedMessageProcessor = messageProcessor;
        final String userName = params.getUserName();
        final EAuthType authType = params.getAuthType();
        final PwdTokenCalculator oldPwdTokenCalculator = new PwdTokenCalculator(userName, oldPassword, params.getPwdHashAlgorithm());
        try{
            messageProcessor = messageProcessor.createCopy(oldPwdTokenCalculator, authType);
            final PasswordHash newPwdHash = PasswordHash.Factory.newInstance(userName, newPassword.toCharArray());
            try{
                request.setNewPwdHash( oldPwdTokenCalculator.createEncryptedHashForNewPassword(newPwdHash) );
                boolean passwordWasChanged = false;
                try {
                    send(new DefaultRequestHandle(environment, request, ChangePasswordMess.class), false);
                    if (params instanceof EasSessionPwdParameters) {
                        if (params.getPwdHashAlgorithm()!=PasswordHash.DEFAULT_ALGORITHM){
                            params = params.createCopy(null, PasswordHash.DEFAULT_ALGORITHM);
                        }
                        applyNewCredentials(userName, newPassword);
                    }else{
                        messageProcessor = savedMessageProcessor;
                    }
                    passwordWasChanged = true;
                } finally {
                    if (!passwordWasChanged){
                        messageProcessor = savedMessageProcessor;
                    }
                }
            }finally{
                newPwdHash.erase();
            }
        }finally{
            oldPwdTokenCalculator.dispose();
        }
    }    
    
    private void applyNewCredentials(final String userName, final String password){
        applyNewPasswordHashAndErase( PasswordHash.Factory.newInstance(userName, password) );        
    }
    
    private void applyNewPasswordHashAndErase(final PasswordHash pwdHash){
        try{
            if (tokenCalculator!=null){
                tokenCalculator.dispose();//this call will erase data in secretStore
            }
            if (secretStore!=null){
                writePasswordHashToSecretStore(pwdHash, secretStore);//for radixware designer
            }
            writePasswordHashToSecretStore(pwdHash, serverKeyStore);
            setTokenCalculator( ((EasSessionPwdParameters)params).createTokenCalculator(serverKeyStore) );
            messageProcessor = messageProcessor.createCopy(tokenCalculator, EAuthType.PASSWORD);
        }finally{
            pwdHash.erase();
        }
    }
    
    private void applyNewChallengeEncryptionKey(final byte[] serverEncKey) {
        serverKeyStore.setSecret(new TokenProcessor().encrypt(serverEncKey));
        Arrays.fill(serverEncKey, (byte) 0);
        setTokenCalculator( new KeyTokenCalculator(serverKeyStore) );
        messageProcessor = messageProcessor.createCopy(tokenCalculator, params.getAuthType());
    }    
    
    private static void writePasswordHashToSecretStore(final PasswordHash pwdHash, final ISecretStore secretStore){
        final byte[] newPwdHashData = pwdHash.export();
        try{
            final byte[] encryptedNewPwdHashData = new TokenProcessor().encrypt(newPwdHashData);
            secretStore.setSecret( encryptedNewPwdHashData );
            Arrays.fill(encryptedNewPwdHashData, (byte)0);
        }finally{
            Arrays.fill(newPwdHashData, (byte)0);
        }
    }
    
    private XmlObject processInvalidPassswordFault(final RequestHandle handle) throws InterruptedException, ServiceClientException {
        if (isInteractive()){
            final String dlgMessage = mp.translate("ExplorerMessage",
                    "You must reauthorize to continue working\nPlease enter your password or press cancel to disconnect");        
            final boolean isCreateSessionRequest = isCreateSessionRequest(handle);
            while (true) {
                final String password = askForPassword(dlgMessage, !isCreateSessionRequest);
                if (password==null){
                    throw new CredentialsWasNotDefinedError();
                }
                updatePassword(password);
                try {
                    return send(handle, true);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                } catch (ServiceCallFault fault) {
                    if (fault.getFaultString().equals(ExceptionEnum.INVALID_PASSWORD.toString())) {
                        final String message = mp.translate("ExplorerError", "Password is Invalid!");
                        environment.getProgressHandleManager().blockProgress();
                        environment.messageError(message);
                        environment.getProgressHandleManager().unblockProgress();
                        continue;
                    } else {
                        throw fault;
                    }
                }
            }
        }else{
            createSession(false); 
            return send(handle, true);
        }
    }
    
    private String askForPassword(final String title, final boolean confirmToDisconnect){
        try{
            final String result = environment.runInGuiThread(new EnterPasswordDialogTask(environment, title, confirmToDisconnect));
            if (result==null){
                credentialsDefined = false;
            }
            return result;
        }catch(InterruptedException exception){
            return null;
        }catch(ExecutionException exception){
            environment.getTracer().error(exception.getCause());
            return null;
        }
    }
    
    private List<String> askForSelectSessions(final List<SessionDescription> sessions, final int maxNumber){
        try{
            return environment.runInGuiThread(new SelectSessionsDialogTask(environment, maxNumber, sessions));
        }catch(InterruptedException exception){
            return Collections.emptyList();
        }catch(ExecutionException exception){
            environment.getTracer().error(exception.getCause());
            return Collections.emptyList();
        }
    }
    
    private ChangePasswordDialogResult askForChangePassword(final org.radixware.kernel.common.auth.PasswordRequirements requirements){
        try{
            return environment.runInGuiThread(new ChangePasswordDialogTask(environment, requirements));
        }catch(InterruptedException exception){
            return null;
        }catch(ExecutionException exception){
            environment.getTracer().error(exception.getCause());
            return null;
        }
    }
    
    private String askForSignText(final String text, final X509Certificate certificate) throws SignatureException{
        try{
            return environment.runInGuiThread(new SignTextTask(environment, text, certificate));
        }catch(ExecutionException exception){
            if (exception.getCause() instanceof SignatureException){
                throw (SignatureException)exception.getCause();
            }else{
                environment.getTracer().error(exception.getCause());
                return null;                
            }            
        }catch(InterruptedException exception){
            return null;
        }
    }
    
    public final boolean isInteractive(){
        return masterSession==null;
    }
    
    @Override
    public final boolean isOpened(){
        return opened;
    }
    
    protected final void setTokenCalculator(final ITokenCalculator calculator){
        tokenCalculator = calculator;
    }
    
    protected final ISecretStore getSecretStore(){
        return secretStore;
    }
        
    protected final void setConnection(IEasClient soapConnection){
        executor = createNewRequestExecutor(soapConnection);
    }
    
    protected final void changeMessageProcessorParams(final ITokenCalculator newTokenCalculator, final EAuthType authType){
        previousMessageProcessor = messageProcessor;
        final ITokenCalculator calculator = newTokenCalculator==null ? tokenCalculator : newTokenCalculator;
        messageProcessor = messageProcessor.createCopy(calculator, authType);        
    }
    
    protected final void restorePreviousMessageProcessorParams(){
        messageProcessor = previousMessageProcessor;
        previousMessageProcessor = null;
    }
    
    protected final AbstractEasSessionParameters getParameters(){
        return params;
    }
    
    protected final IClientEnvironment getEnvironment(){
        return environment;
    }

    private XmlObject processKerberosAuthFault(ServiceCallFault e, RequestHandle handler) throws ServiceClientException, InterruptedException {
        if (org.radixware.schemas.eas.KerberosAuthFaultMessage.RENEW_CREDENTIALS_REQUIRED.toString().equals(e.getMessage())) {
            try {
                ((KrbTokenCalculator) tokenCalculator).renewKerberosCredentials();
            } catch (KerberosException exception) {
                throw new KerberosError(exception);
            }
            return send(handler, true);
        }
        throw e;
    }

    private XmlObject processSessionDoesNotExistFault(final SessionDoesNotExistError error, final RequestHandle handler) throws ServiceClientException, InterruptedException {
        if (isCloseSessionRequest(handler)){
            throw error;
        }
        final SessionRestorePolicy.Enum restorePolicy = error.getRestoringPolicy();
        if (restorePolicy==null){
            final String faultMessage = error.getSouceFault().getMessage();
            final String itemText = "Unknown fault message \"%s\" ";
            tracer.put(EEventSeverity.WARNING,String.format(itemText, faultMessage),EEventSource.CLIENT_SESSION.getValue());
        }
        
        final boolean isCreateSessionRequest = isCreateSessionRequest(handler);
        final AbstractEasSession restoringSession;        
        synchronized(semaphore){
            if (masterSession!=null && isCreateSessionRequest){
                //trying to create background session when master session does not exists
                if (!canRestore){
                    throw error;
                }
                restoringSession = masterSession;            
            }else{
                restoringSession = this;
            }
            restoringSession.opened = false;
            restoringSession.sessionRestorePolicy = restorePolicy;
        }
        final CreateSessionRs response = restoringSession.restore(true, !isCreateSessionRequest);
        if (response==null){
            credentialsDefined = false;
            throw new CredentialsWasNotDefinedError();            
        }else{
            if (masterSession!=null && isCreateSessionRequest){
                return createSession(true);//cant use current request handler with old sessionId
            } else if (!isCreateSessionRequest) {
                return send(handler, true);
            } else{
                return response;
            }
        }        
    }
    
    private List<String> processSessionsLimitExceeded(final MaxNumberOfSessionsExceededError error) {
        if (isInteractive()){
            final int maxNumber =  error.getMaxNumberOfSessions();
            final List<SessionDescription> sessions = error.getOpenedSessions();
            if (sessions.size()==1){
                final String title = 
                    environment.getMessageProvider().translate("SelectEasSessionDialog", "Unable to establish connection");
                final String message = 
                    environment.getMessageProvider().translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate your previous session to create a new one.\nDo you want to terminate previous session and continue?");
                if (environment.messageConfirmation(title, message)){
                    return Collections.singletonList(sessions.get(0).getEncryptedId());
                }else{
                    throw error;
                }
            }else if (sessions.size()>1){
                final List<String> sessionIds = askForSelectSessions(sessions, maxNumber);
                if (sessionIds==null || sessionIds.isEmpty()){
                    throw error;
                }else{
                    return sessionIds;
                }                
            }else{
                throw error;
            }
        }else{
            synchronized(semaphore){
                canRestore = false;//restoring of master session was cancelled by user
            }
            throw  error;            
        }
    }

    private void updatePassword(final String newPassword) {
        final boolean isPwdAuth = params instanceof EasSessionPwdParameters;
        if (isPwdAuth){
            applyNewCredentials(params.getUserName(), newPassword);            
        }else if (secretStore != null){
            //password for keystore
            secretStore.setSecret(new TokenProcessor().encrypt(newPassword.toCharArray()));
        }
    }
    
    private CreateSessionRs restore(final boolean resend, final boolean confirmToDisconnect) throws ServiceClientException, InterruptedException {
        synchronized(semaphore){
            if (!canRestore){
                return null;
            }
            if (isOpened()){
                throw new IllegalStateException("Unable to restore opened session");
            }
            final boolean authWithoutPwd = params.getAuthType() == EAuthType.KERBEROS
                    || (params.getAuthType() == EAuthType.CERTIFICATE && params.hasUserCerts());
            final boolean noPwdRequired = sessionRestorePolicy!=SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED;
            if (noPwdRequired || authWithoutPwd || !isInteractive()){
                if (resend){
                    final String itemText = "Session identifier is wrong. Trying to reopen session.";
                    tracer.put(EEventSeverity.DEBUG,itemText,EEventSource.CLIENT_SESSION.getValue());
                }
                try {
                    return createSession(resend);                
                } catch (InterruptedException exception) {//NOPWD
                    return null;
                }            
            }

            final String itemText = "Session identifier is wrong. Trying to reopen session. User must reenter password. ";
            tracer.put(EEventSeverity.DEBUG,itemText,EEventSource.CLIENT_SESSION.getValue());        

            final ConnectionOptions.SslOptions sslOptions = environment.getSslOptions();
            final boolean isCertificateAuth = params.getAuthType()==EAuthType.CERTIFICATE && sslOptions!=null;
            final boolean isPKCS11 = isCertificateAuth && sslOptions.getKeyStoreType() == EKeyStoreType.PKCS11;
            
            final String title;
            if (isPKCS11){
                title = mp.translate("ExplorerDialog", "Please enter security device access password or press cancel to disconnect");
            }else if (isCertificateAuth){
                title = mp.translate("ExplorerDialog", "Please enter RSA-key access password or press cancel to disconnect");
            }else{
                title = mp.translate("ExplorerDialog", "Please enter your password or press cancel to disconnect");
            }
            while (true) {
                final String password = askForPassword(title, confirmToDisconnect);
                if (password==null){
                    return null;
                }                
                if (isCertificateAuth) {
                    try {
                        final char[] keyStorePassword = password.toCharArray();
                        renewSslContext(keyStorePassword);
                        Arrays.fill(keyStorePassword, '\0');
                    } catch (KeystoreControllerException | CertificateUtilsException exception) {
                        if (KeystoreController.isIncorrectPasswordException(exception)) {
                            final String message = mp.translate("ExplorerError", "Password is Invalid!");
                            environment.messageError(message);
                            continue;
                        } else {
                            throw new ServiceClientException(mp.translate("ClientSessionException", "Failed to create security context"), exception);
                        }
                    }
                }else{
                    updatePassword(password);
                }
                try {                    
                    return createSession(resend);
                } catch (InterruptedException ex) {//NOPMD
                    continue;
                } catch (ServiceCallFault newFault) {
                    boolean invalidPassword = newFault.getFaultString().equals(ExceptionEnum.INVALID_PASSWORD.toString())
                            || newFault.getFaultString().equals(ExceptionEnum.INVALID_CREDENTIALS.toString());
                    if (invalidPassword) {
                        final String message = mp.translate("ExplorerError", "Password is Invalid!");
                        environment.messageError(message);
                        continue;
                    } else {
                        throw newFault;
                    }
                }
            }        
        }
    }

    public CreateSessionRs copy(final AbstractEasSession src, final Id desiredExplorerRootId) throws ServiceClientException, InterruptedException {
        if (executor != null) {
            close(false);
        }
        setTokenCalculator( src.tokenCalculator.copy(environment) );
        final EasClient soapConnection = 
                new EasClient(mp, getSessionTrace(), (EasClient) src.executor.getConnection());
        executor = createNewRequestExecutor(soapConnection);
        return createSession(params, false, desiredExplorerRootId, null);
    }
    
    @Override
    public IEasSession createBackgroundSession(){
        final BackgroundEasSession newSession = new BackgroundEasSession(environment, isInteractive() ? this : masterSession);
        newSession.setTokenCalculator(tokenCalculator);
        final EasClient soapConnection = 
                new EasClient(mp, newSession.getSessionTrace(), (EasClient) executor.getConnection());
        newSession.executor = newSession.createNewRequestExecutor(soapConnection);
        newSession.params = params.createCopy(null,null);
        return newSession;
    }

    private XmlObject preprocessResponse(final XmlObject answer, final AbstractTraceBuffer traceBuffer) throws ServiceAuthenticationException {
        final XmlObject response = messageProcessor.parseResponce(answer, traceBuffer);
        return response;
    }

    RequestExecutor createNewRequestExecutor(final IEasClient soapConnection) {
        return new RequestExecutor(environment, easTrace, soapConnection, isInteractive()) {
            @Override
            protected XmlObject prepareRequest(final XmlObject request) throws ServiceClientException {
                return messageProcessor.prepareRequest(request);
            }

            @Override
            protected long getCurrentDefinitionVersion(final XmlObject requestBody) {
                return getEnvironment().getDefManager().getAdsVersion().getNumber();
            }

            @Override
            protected XmlObject onResponseReceived(final XmlObject answer) throws ServiceAuthenticationException {
                final XmlObject response = preprocessResponse(answer, tracer.getBuffer());
                executor.setCurrentScpName(messageProcessor.getCurrentScpName());
                return response;
            }

            @Override
            protected XmlObject processGetTokenCallback(final GetSecurityTokenRq request) throws ServiceClientException, InterruptedException {
                ITokenCalculator tCalculator = tokenCalculator;
                XmlObject response = null;
                GetSecurityTokenRq callbackRq;
                do {
                    callbackRq = response == null ? request : ((GetSecurityTokenMess) response).getGetSecurityTokenRq();
                    final GetSecurityTokenDocument document = GetSecurityTokenDocument.Factory.newInstance();
                    final byte[] inToken = callbackRq.getInputToken();
                    if (inToken == null || inToken.length == 0) {//kerberos initial token
                        final ITokenCalculator prevCalculator = tCalculator;
                        tCalculator = tCalculator.copy(environment);
                        prevCalculator.dispose();
                    }else if (tCalculator instanceof PwdTokenCalculator){
                        if (request.getHashAlgorithm()==null){
                            final String message = mp.translate("ExplorerError", "Unable to create authentication data. Server version is too old.");
                            response = executor.getConnection().sendFaultMessage(message, null, INFINITE_RESPONSE_TIMEOUT);
                            continue;                            
                        }else{
                            final PwdTokenCalculator pwdTokenCalculator = ((PwdTokenCalculator)tCalculator);
                            final PasswordHash.Algorithm requestedHashAlgorithm = 
                                PasswordHash.Algorithm.getForTitle( request.getHashAlgorithm().toString() );
                            if (requestedHashAlgorithm!=pwdTokenCalculator.getPasswordHashAlgorithm()){
                                tCalculator = pwdTokenCalculator.createCopyForHashAlgo(requestedHashAlgorithm);
                            }
                        }
                    }
                    final ITokenCalculator.SecurityToken result;
                    try{
                        result = tCalculator.calcToken(inToken);
                    }catch(IllegalUsageError | IllegalArgumentException ex){
                        environment.getTracer().debug(ex);
                        final String reason = ex.getMessage();
                        final String message;
                        if (reason!=null && !reason.isEmpty()){
                            message = mp.translate("ExplorerError", "Unable to get authentication data")+"\n"+reason;
                        }else{
                            message = mp.translate("ExplorerError", "Unable to get authentication data");
                        }
                        response = executor.getConnection().sendFaultMessage(message, null, INFINITE_RESPONSE_TIMEOUT);
                        continue;
                    }
                    if (result == null) {
                        final String message = mp.translate("ExplorerError", "Unable to get authentication data");
                        response = executor.getConnection().sendFaultMessage(message, null, INFINITE_RESPONSE_TIMEOUT);
                        continue;
                    }
                    result.write(document.addNewGetSecurityToken().addNewGetSecurityTokenRs());
                    result.clear();
                    response = executor.getConnection().sendCallbackResponse(document, INFINITE_RESPONSE_TIMEOUT);
                } while (response instanceof GetSecurityTokenMess);

                if (tCalculator != tokenCalculator) {
                    tokenCalculator = tCalculator;
                    messageProcessor = messageProcessor.createCopy(tokenCalculator, params.getAuthType());
                }
                return response;
            }
        };
    }
    
    private CreateSessionRs createSession(final boolean resend) throws ServiceClientException, InterruptedException{
        return createSession(params, resend, null, null);
    }
    
    final void open() throws ServiceClientException, InterruptedException{
        synchronized(semaphore){
            if (!isOpened()){
                createSession(params, false, null, null);
            }
        }
    }

    protected final CreateSessionRs createSession(AbstractEasSessionParameters parameters,
            final boolean isResend,
            final Id desiredExplorerRootId,
            final Long replacedSessionId) throws ServiceClientException, InterruptedException {        
        if (parameters instanceof EasSessionKrbParameters) {
            final KrbTokenCalculator krbTokenCalculator = 
                ((EasSessionKrbParameters)parameters).createTokenCalculator(environment);                
            final ITokenCalculator saveTokenCalculator = tokenCalculator;
            final byte[] decryptedKey;
            final CreateSessionRs response;
            try {
                tokenCalculator = krbTokenCalculator;
                response =
                        createSessionImpl(parameters,
                        isResend,
                        desiredExplorerRootId,
                        replacedSessionId,
                        krbTokenCalculator.getKeyStorage());
                if (response.isSetEncKey()) {
                    final byte[] krbEncKey = response.getEncKey();
                    decryptedKey = krbTokenCalculator.decrypt(krbEncKey);
                } else {
                    decryptedKey = null;
                }
            } catch (ServiceClientException | InterruptedException exception) {
                tokenCalculator = saveTokenCalculator;
                if (messageProcessor != null) {
                    messageProcessor = messageProcessor.createCopy(tokenCalculator, params.getAuthType());
                }
                throw exception;
            } catch (GSSException exception) {
                tokenCalculator = saveTokenCalculator;
                if (messageProcessor != null) {
                    messageProcessor = messageProcessor.createCopy(tokenCalculator, params.getAuthType());
                }
                throw new KerberosError(exception);
            } finally {
                krbTokenCalculator.dispose(false);
            }
            if (decryptedKey != null) {
                applyNewChallengeEncryptionKey(decryptedKey);
            }
            return response;
        } else {
            final CreateSessionRs response =
                    createSessionImpl(parameters,
                    isResend,
                    desiredExplorerRootId,
                    replacedSessionId,
                    null);
            if (response.isSetEncKey()) {
                applyNewChallengeEncryptionKey( response.getEncKey() );
            }
            return response;
        }
    }

    private CreateSessionRs createSessionImpl(final AbstractEasSessionParameters parameters,
            final boolean isResend,
            final Id desiredExplorerRootId,
            final Long replacedSessionId,
            final ISecretStore clientSecretStore) throws ServiceClientException, InterruptedException {
        mp = environment.getMessageProvider();
        params = parameters;
        credentialsDefined = true;
        if (messageProcessor == null) {
            messageProcessor = new EasMessageProcessor(environment, tokenCalculator, parameters.getAuthType());
        } else {
            messageProcessor = messageProcessor.createCopy(tokenCalculator, parameters.getAuthType());
        }
        final CreateSessionRq request = RequestCreator.createSession(parameters.getUserName(),
                parameters.getAuthType(),
                parameters.getUserCertificates(),
                parameters.getStationName(),
                environment.getLanguage(),
                environment.getCountry(),
                environment.getApplication().getRuntimeEnvironmentType(),
                desiredExplorerRootId,
                replacedSessionId,
                isInteractive() ? null : masterSession.messageProcessor.getSessionId(),
                parameters.isWebDriverEnabled());
        if (parameters.getAuthType() == EAuthType.KERBEROS) {
            final ITokenCalculator.SecurityToken secToken = tokenCalculator.calcToken(new byte[]{});
            request.setKrbInitialToken(secToken.token);
            secToken.clear();
        }
        CreateSessionRs response = null;
        do{
            try{
                response =
                    (CreateSessionRs) send(new DefaultRequestHandle(environment, request, CreateSessionMess.class), isResend);
            }catch(MaxNumberOfSessionsExceededError error){
                final List<String> sessionIds =  processSessionsLimitExceeded(error);
                final CreateSessionRq.SessionsToTerminate sessionsToTerminate = request.addNewSessionsToTerminate();
                for (String sessionId: sessionIds){
                    sessionsToTerminate.addEncryptedSessionId(sessionId);
                }
            }
        }while(response==null);
        if (!isInteractive() && isResend){
            //detecting reenterant call of create background session. Process response in outer level
            return response;
        }
        processCreateSessionResponse(response, parameters, clientSecretStore, isResend);
        opened = true;
        if (!response.getShouldChangePassword() || params.getAuthType() != EAuthType.PASSWORD) {
            return response;
        } else {
            final org.radixware.kernel.common.auth.PasswordRequirements requirements;
            if (response.getPasswordRequirements()==null){
                requirements = null;
            }else{
                requirements = 
                    ClientAuthUtils.getPasswordRequirements(response.getPasswordRequirements(), params.getUserName());
            }
            if (processShouldChangePassword && processShouldChangePasswordFault(requirements)){
                return response;
            }else{
                close(false);
                if (isInteractive()){
                    throw new PasswordExpiredError();
                }else{
                    processShouldChangePassword = false;
                    throw new PasswordExpiredError();
                }
            }
        }
    }
    
    private void processCreateSessionResponse(final CreateSessionRs response,
                                              final AbstractEasSessionParameters parameters,
                                              final ISecretStore clientSecretStore,
                                              final boolean isResend) throws InterruptedException, ServiceClientException{
        sessionRestorePolicy = null;
        final String actualUserName = response.getUser();
        final PasswordHash.Algorithm actualPwdHashAlgo;
        if (response.getHashAlgorithm()==null){
            if (params.getAuthType()==EAuthType.PASSWORD){
                final String message = mp.translate("ExplorerError", "Unable to create authentication data. Server version is too old.");
                throw new InterruptedException(message);
            }else{
                actualPwdHashAlgo = null;
            }
        }else{
            actualPwdHashAlgo = PasswordHash.Algorithm.getForTitle( response.getHashAlgorithm().toString() );
        }
        final boolean hashAlgoChanged = actualPwdHashAlgo!=params.getPwdHashAlgorithm();
        if ( !Objects.equals(params.getUserName(), actualUserName) 
             || hashAlgoChanged
           ){
            params = params.createCopy(actualUserName, actualPwdHashAlgo);
        }
        boolean needToUpdateMessageProcessor = clientSecretStore != null;
        if (hashAlgoChanged && tokenCalculator instanceof PwdTokenCalculator){            
            setTokenCalculator( ((PwdTokenCalculator)tokenCalculator).createCopyForHashAlgo(actualPwdHashAlgo) );
            needToUpdateMessageProcessor = true;
        }
        if (needToUpdateMessageProcessor) {
            messageProcessor = messageProcessor.createCopy(tokenCalculator, clientSecretStore, parameters.getAuthType());
        }        
        final CreateSessionRs.ServerResources resources = response.getServerResources();
        boolean canTrace = false;
        if (resources != null && resources.getItemList() != null) {
            for (CreateSessionRs.ServerResources.Item item : resources.getItemList()) {
                try {
                    if (EDrcServerResource.getForValue(item.getId().toString()) == EDrcServerResource.TRACING) {
                        canTrace = true;
                    }
                } catch (NoConstItemWithSuchValueError err) {
                    continue;
                }
            }
        }
        tracer.setServerTraceAllowed(canTrace);
        if (response.getWarnings()!=null && response.getWarnings().getWarning()!=null){
            final String message = response.getWarnings().getWarning().getMessage();
            final String details = response.getWarnings().getWarning().getDetails();
            if (isInteractive()){
                final DialogFactory factory = environment.getApplication().getDialogFactory();
                final IDialog messageDialog = 
                    factory.newMessageWithDetailsDialog(environment, null/*parent*/, null/*title*/, message, details, EDialogIconType.WARNING);
                messageDialog.execDialog();
            }
            tracer.warning(message+"\n"+details);
        }
        if (params.hasUserCerts()) {
            final boolean isPwdTokenAllowed = response.getCanLoginWithPassword();
            final byte[] challenge = response.getChallenge();
            final byte[] pwdToken;
            String signedChallenge = null;
            if (isInteractive()){
                pwdToken = null;
                try {
                    signedChallenge = askForSignText(Hex.encode(challenge), params.getUserCertificates()[0]);
                    if (signedChallenge==null){
                        closeEasSession();//close incomplete session
                        throw new InterruptedException();
                    }
                } catch (SignatureException exception) {
                    switch (exception.getReason()) {
                        case REQUESTED_CERT_NOT_FOUND:
                        case FAILED_TO_SIGN:
                        case UNSUPPORTED_OPERATION:
                            if (isPwdTokenAllowed && isInteractive()) {
                                tracer.debug(exception.getMessage());
                                switchToPwdAuthType();
                            } else {
                                closeEasSession();//close incomplete session
                                throw exception;
                            }
                            break;
                        case USER_CANCELED:
                            tracer.debug(exception.getMessage());
                            closeEasSession();//close incomplete session
                            throw new InterruptedException();//NOPMD
                        default:
                            closeEasSession();//close incomplete session
                            throw exception;
                    }
                }
            }else{
                pwdToken = masterSession.tokenCalculator.calcToken(challenge).token;
            }
            try{
                while (!login(signedChallenge, pwdToken, isResend)) {
                    if (isInteractive()){
                        switchToPwdAuthType();
                    }else{
                        throw new InterruptedException("Password is invalid");
                    }
                }
            }catch(InterruptedException exception){
                closeEasSession();//close incomplete session
                throw exception;
            }catch(ServiceCallFault exception){
                closeEasSession();//close incomplete session
                throw exception;
            }catch(EasError | AuthError error){
                closeEasSession();//close incomplete session
                throw error;
            }
        }        
    }

    private void switchToPwdAuthType() throws InterruptedException {
        final String message = mp.translate("ExplorerDialog", "Please enter password for \"%1$s\" account");
        final String userName = params.getUserName();
        final String password = askForPassword(String.format(message, userName), false);
        if (password == null) {
            throw new InterruptedException("Password is invalid");
        } else {
            params = 
                new EasSessionPwdParameters(userName, params.getStationName(), params.getPwdHashAlgorithm(), params.isWebDriverEnabled());
            applyNewCredentials(userName, password);
        }
    }

    private boolean login(final String signedChallenge, final byte[] pwdToken, final boolean isResend) throws ServiceClientException, InterruptedException {
        final LoginRq request = LoginRq.Factory.newInstance();
        if (signedChallenge != null) {
            request.setSignedChallenge(Base64.decode(signedChallenge));
        }
        if (pwdToken!=null){
            request.setPwdToken(pwdToken);
        }
        LoginRs response = null;
        do{
            try {
                response = (LoginRs) send(new DefaultRequestHandle(environment, request, LoginMess.class), isResend);
            }catch (ServiceCallFault fault) {
                if (params.getAuthType() == EAuthType.PASSWORD && fault.getFaultString().equals(ExceptionEnum.INVALID_PASSWORD.toString())) {
                    if (isInteractive()){
                        final String message = mp.translate("ExplorerError", "Password is Invalid!");
                        environment.messageError(message);
                        return false;
                    }else{                        
                        throw fault;
                    }
                }                
                throw fault;
            }catch(MaxNumberOfSessionsExceededError error){
                final List<String> sessionIds =  processSessionsLimitExceeded(error);
                final LoginRq.SessionsToTerminate sessionsToTerminate = request.addNewSessionsToTerminate();
                for (String sessionId: sessionIds){
                    sessionsToTerminate.addEncryptedSessionId(sessionId);
                }
            }
        }while(response==null);
        if (response.isSetEncKey()) {
            applyNewChallengeEncryptionKey( response.getEncKey() );
        }
        return true;
    }
    
    public boolean inInteractiveCallBackRequestProcessing(){
        synchronized(semaphore){
            return executor!=null && executor.inInteractiveCallBackRequestProcessing();
        }
    }

    @Override
    public boolean isBusy() {
        synchronized (semaphore) {
            return isBusy > 0;
        }
    }

    @Override
    public ReadRs read(Pid pid, Id classId, Collection<Id> presentations,
            IContext.Entity entityContext, boolean withAccessibleExplorerItems)
            throws ServiceClientException, InterruptedException {
        final ReadRq request = RequestCreator.read(pid, classId, presentations, null, entityContext, withAccessibleExplorerItems);
        return (ReadRs) send(new DefaultRequestHandle(environment, request, ReadMess.class), false);
    }

    @Override
    public ReadRs readProp(Pid pid, Id classId, Collection<Id> presentations,
            IContext.Entity entityContext,
            Id propId)
            throws ServiceClientException, InterruptedException {
        final ReadRq request = RequestCreator.read(pid, classId, presentations, Collections.singletonList(propId), entityContext, false);
        return (ReadRs) send(new DefaultRequestHandle(environment, request, ReadMess.class), false);
    }

    @Override
    public SetParentRs setParent(final Model childModel, final Id parentPropId, final Pid parentPid) throws ServiceClientException, InterruptedException {
        final SetParentRq request = SetParentRq.Factory.newInstance();
        org.radixware.schemas.eas.PropertyList propertyList;
        if (childModel instanceof FormModel) {
            final org.radixware.schemas.eas.Form form = ((FormModel) childModel).toXml();
            request.setForm(form);
            propertyList = request.getForm().getProperties();
            if (propertyList == null) {
                propertyList = form.addNewProperties();
            }
        } else if (childModel instanceof ReportParamDialogModel) {
            final org.radixware.schemas.eas.Report report = ((ReportParamDialogModel) childModel).toXml();
            request.setReport(report);
            propertyList = request.getReport().getProperties();
            if (propertyList == null) {
                propertyList = report.addNewProperties();
            }
        } else if (childModel instanceof EntityModel) {
            final EntityModel entityModel = (EntityModel) childModel;
            request.setContext(((IContext.Entity) childModel.getContext()).toXml());
            request.addNewClass1().setId(entityModel.getClassId());
            if (!entityModel.isNew()) {
                request.setPID(entityModel.getPid().toString());
            } else if (entityModel.getSrcPid() != null) {
                request.setSrcPID(entityModel.getSrcPid().toString());
            }

            SetParentRq.CurrentData currentData = request.addNewCurrentData();
            if (!entityModel.isNew()) {
                currentData.setPID(entityModel.getPid().toString());
            }
            final Collection<Property> properties;
            if (entityModel.isNew()) {
                properties = entityModel.getActiveProperties();
            } else {
                properties = entityModel.getEditedProperties();
            }
            propertyList = currentData.addNewProperties();
            for (Property property : properties) {
                if (!property.isLocal()
                    && (!property.isValueModifiedByChangingParentRef() || entityModel.isNew())
                    && ( property.getType()!=EValType.OBJECT || !entityModel.isNew() )
                    ) {
                    property.writeValue2Xml(propertyList.addNewItem());
                }
            }
            request.addNewEditorPresentation().setId(entityModel.getDefinition().getId());
            /*
             * RADIX-5613 if (entity.getContext() instanceof IContext.SelectorRow) {
             * final GroupModel groupModel = ((IContext.SelectorRow)
             * entity.getContext()).parentGroupModel;
             * request.addNewSelectorPresentation().setId(groupModel.getDefinition().getId());
             * }
             */
            request.setRespWithLOBValues(true);
        } else if (childModel == null) {
            throw new NullPointerException("childModel must be not null");
        } else {
            throw new IllegalArgumentError("Can't execut set parent request for child model of " + childModel.getClass().getName() + " class");
        }
        if (propertyList.getItemList() != null) {
            for (int i = propertyList.getItemList().size() - 1; i >= 0; i--) {
                final PropertyList.Item property = propertyList.getItemList().get(i);
                if (parentPropId.equals(property.getId())) {
                    propertyList.removeItem(i);
                    break;
                }
            }
        }
        final PropertyList.Item parentRefItem = propertyList.addNewItem();
        parentRefItem.setId(parentPropId);
        if (parentPid != null) {
            parentRefItem.addNewRef().setPID(parentPid.toString());
        } else {
            parentRefItem.setNilRef();
        }

        request.addNewParentTitleProperty().setId(parentPropId);
        return (SetParentRs) send(new DefaultRequestHandle(environment, request, SetParentMess.class), false);
    }

    @Override
    public UpdateRs update(EntityModel entity) throws ServiceClientException, InterruptedException {
        final UpdateRq request = UpdateRq.Factory.newInstance();
        request.setContext(((IContext.Entity) entity.getContext()).toXml());
        request.setPID(entity.getPid().toString());
        request.addNewClass1().setId(entity.getClassId());
        request.addNewEditorPresentation().setId(entity.getEditorPresentationDef().getId());
        entity.writeToXml(request.addNewNewData(), false);
        if (entity.getContext() instanceof IContext.SelectorRow) {
            final GroupModel groupModel = ((IContext.SelectorRow) entity.getContext()).parentGroupModel;
            request.addNewSelectorPresentation().setId(groupModel.getDefinition().getId());
        } else if (entity.getContext() instanceof IContext.InSelectorEditing) {
            final GroupModel groupModel = ((IContext.InSelectorEditing) entity.getContext()).getGroupModel();
            request.addNewSelectorPresentation().setId(groupModel.getDefinition().getId());
        }
        request.setRespWithLOBValues(false);
        return (UpdateRs) send(new DefaultRequestHandle(environment, request, UpdateMess.class), false);
    }

    @Override
    public PrepareCreateRs prepareCreate(
            final Id creationPresentationId,
            final Id classId,
            final Pid srcPid,
            final IContext.Entity ctx,
            final Collection<Property> predefinedVals)
            throws ServiceClientException, InterruptedException {
        return prepareCreate(
                Collections.singletonList(creationPresentationId),
                classId,
                srcPid,
                ctx,
                predefinedVals);
    }

    @Override
    public PrepareCreateRs prepareCreate(
            final List<Id> creationPresentationIds,
            final Id classId,
            final Pid srcPid,
            final IContext.Entity ctx,
            final Collection<Property> predefinedVals)
            throws ServiceClientException, InterruptedException {
        final PrepareCreateRq request = PrepareCreateRq.Factory.newInstance();
        request.setContext(ctx.toXml());        
        final PrepareCreateRq.Presentations presentations = request.addNewPresentations();
        for (Id presentationId : creationPresentationIds) {
            presentations.addNewItem().setId(presentationId);
        }
        final org.radixware.schemas.eas.Object objectXml = request.addNewObjectTemplate();        
        objectXml.setClassId(classId);
        if (srcPid != null) {
            objectXml.setSrcPID(srcPid.toString());
        }
        if (predefinedVals != null && !predefinedVals.isEmpty()) {
            final PropertyList properties = objectXml.addNewProperties();
            for (Property property : predefinedVals) {
                if (!property.isLocal() && property.getType()!=EValType.OBJECT) {
                    property.writeValue2Xml(properties.addNewItem());
                }
            }
        }
        return (PrepareCreateRs) send(new DefaultRequestHandle(environment, request, PrepareCreateMess.class), false);
    }
    
    @Override
    public PrepareCreateRs prepareCreate(
            final List<Id> creationPresentationIds,
            final IContext.Entity ctx,
            final List<EntityModel> templates)
            throws ServiceClientException, InterruptedException {
        final PrepareCreateRq request = PrepareCreateRq.Factory.newInstance();
        request.setContext(ctx.toXml());        
        final PrepareCreateRq.Presentations presentations = request.addNewPresentations();
        for (Id presentationId : creationPresentationIds) {
            presentations.addNewItem().setId(presentationId);
        }
        for (EntityModel template: templates){
            final org.radixware.schemas.eas.Object objectXml = request.addNewObjectTemplate();        
            objectXml.setClassId(template.getClassId());
            final Collection<Property> predefined = template.getActiveProperties();
            if (!predefined.isEmpty()) {
                final PropertyList properties = objectXml.addNewProperties();
                for (Property property : predefined) {
                    if (!property.isLocal() && property.getType()!=EValType.OBJECT) {
                        property.writeValue2Xml(properties.addNewItem());
                    }
                }
            }
        }
        return (PrepareCreateRs) send(new DefaultRequestHandle(environment, request, PrepareCreateMess.class), false);
    }    

    @Override
    public CreateRs create(EntityModel entity) throws ServiceClientException, InterruptedException {
        final CreateRq request = CreateRq.Factory.newInstance();
        request.setContext(((IContext.Entity) entity.getContext()).toXml());
        request.addNewClass1().setId(entity.getClassId());
        request.addNewPresentation().setId(entity.getDefinition().getId());
        final org.radixware.schemas.eas.Object entityObject = request.addNewData();
        entity.writeToXml(entityObject, false);
        for (Property property: entity.getActiveProperties()){
            if (property.getType()==EValType.OBJECT){
                property.writeValue2Xml(entityObject.getProperties().addNewItem());
            }
        }
        return (CreateRs) send(new DefaultRequestHandle(environment, request, CreateMess.class), false);
    }

    @Override
    public void deleteObject(Pid pid,
            Id classId,
            Id presentationId,
            IContext.Entity entityContext,
            boolean cascade) throws ServiceClientException, InterruptedException {
        final DeleteRq request = DeleteRq.Factory.newInstance();
        request.setContext(entityContext.toXml());
        request.addNewClass1().setId(classId);
        request.setPID(pid.toString());
        request.addNewPresentation().setId(presentationId);
        if (cascade) {
            request.setCascade(cascade);
        }
        send(new DefaultRequestHandle(environment, request, DeleteMess.class), false);
    }

    @Override
    public ContextlessCommandRs executeCommand(
            final FormModel form,
            final Id cmdId,
            final XmlObject rq)
            throws ServiceClientException, InterruptedException {
        final XmlObject request = RequestCreator.contextlessCommand(cmdId, rq, form);
        return (ContextlessCommandRs) send(new DefaultRequestHandle(environment, request, ContextlessCommandMess.class), false);
    }

    @Override
    public SelectRs select(
            final GroupModel group,
            final int startIndex,
            final int rowCount,
            final boolean withSelectorAddons,
            final boolean withInstantiatableClasses)
            throws ServiceClientException, InterruptedException {
        final SelectRq selectRequest =
                RequestCreator.select(group, startIndex, rowCount, withSelectorAddons, withInstantiatableClasses);
        return (SelectRs) send(new DefaultRequestHandle(environment, selectRequest, SelectMess.class), false);
    }

    @Override
    public ListInstantiatableClassesRs listInstantiatableClasses(
            final Id entityId,
            org.radixware.schemas.eas.Context context)
            throws ServiceClientException, InterruptedException {
        final ListInstantiatableClassesRq request = ListInstantiatableClassesRq.Factory.newInstance();
        request.addNewEntity().setId(entityId);
        request.setContext(context);
        return (ListInstantiatableClassesRs) send(new DefaultRequestHandle(environment, request, ListInstantiatableClassesMess.class), false);
    }

    @Override
    public ListEdPresVisibleExpItemsRs listEdPresVisibleExpItems(final EntityModel entity)
            throws ServiceClientException, InterruptedException {
        final ListEdPresVisibleExpItemsRq request = ListEdPresVisibleExpItemsRq.Factory.newInstance();
        request.setContext(((IContext.Entity) entity.getContext()).toXml());
        request.addNewClass1().setId(entity.getClassId());
        request.addNewPresentation().setId(entity.getDefinition().getId());
        request.setPID(entity.getPid().toString());
        return (ListEdPresVisibleExpItemsRs) send(new DefaultRequestHandle(environment, request, ListEdPresVisibleExpItemsMess.class), false);
    }

    @Override
    public List<Id> listVisibleExplorerItems(final Id explorerRootId)
            throws ServiceClientException, InterruptedException {
        final ListVisibleExplorerItemsRq request = ListVisibleExplorerItemsRq.Factory.newInstance();
        request.addNewExplorerRoot().setId(explorerRootId);
        final List<Id> result = new ArrayList<>();
        final ListVisibleExplorerItemsRs response =
                (ListVisibleExplorerItemsRs) send(new DefaultRequestHandle(environment, request, ListVisibleExplorerItemsMess.class), false);
        final List<Definition> items = response.getVisibleExplorerItems().getItemList();
        if (items != null && !items.isEmpty()) {
            for (Definition item : items) {
                result.add(item.getId());
            }
        }
        return result;
    }

    @Override
    public void deleteContext(final GroupModel group, final boolean cascade)
            throws ServiceClientException, InterruptedException {
        final DeleteRq request = RequestCreator.deleteMultipleObjects(group, null, cascade);
        send(new DefaultRequestHandle(environment, request, DeleteMess.class), false);
    }

    @Override
    public DeleteRs deleteSelectedObjects(final GroupModel group, final EntityObjectsSelection selection, final boolean cascade) 
            throws ServiceClientException, InterruptedException {
        final DeleteRq request = RequestCreator.deleteMultipleObjects(group, selection, cascade);
        return (DeleteRs) send(new DefaultRequestHandle(environment, request, DeleteMess.class), false);
    }

    //execute model command
    @Override
    public CommandRs executeCommand(
            final Model model,
            final FormModel form,
            final Id cmdId,
            final Id propertyId,
            final XmlObject inputParameters) throws ServiceClientException, InterruptedException {
        final XmlObject request = RequestCreator.command(cmdId, propertyId, inputParameters, model, form);
        return (CommandRs) send(new DefaultRequestHandle(environment, request, CommandMess.class), false);
    }

    //execute model command
    public CommandRs executeCommand(
            final Model model,
            final FormModel form,
            final Id cmdId,
            final XmlObject inputParameters) throws ServiceClientException, InterruptedException {
        return executeCommand(model, form, cmdId, null, inputParameters);
    }

    @Override
    public XmlObject executeContextlessCommand(
            final Id cmdId,
            final XmlObject rq,
            final Class<? extends XmlObject> responceClass)
            throws ServiceClientException, InterruptedException {
        ContextlessCommandRs response = executeCommand(null, cmdId, rq);
        final XmlObject out = XmlObjectProcessor.getXmlObjectFirstChild(response.getOutput());
        final ClassLoader classLoader = (ClassLoader) environment.getDefManager().getClassLoader();
        return XmlObjectProcessor.castToXmlClass(classLoader, out, responceClass);
    }

    @Override
    public String getEntityTitleByPid(final Id tableId,
            final Id presentationId,
            final String pid)
            throws ServiceClientException, InterruptedException {
        final GetObjectTitlesRq request = GetObjectTitlesRq.Factory.newInstance();
        request.addNewEntity().setId(tableId);
        final GetObjectTitlesRq.Objects.Item itemRq = request.addNewObjects().addNewItem();
        if (presentationId != null) {
            //RADIX-708
            final RadEditorPresentationDef editorPresentation = environment.getDefManager().getEditorPresentationDef(presentationId);
            final org.radixware.schemas.eas.Context.Editor xmlPresentation = request.addNewContext().addNewEditor();
            xmlPresentation.setClassId(editorPresentation.getOwnerClassId());
            xmlPresentation.setPresentationId(presentationId);
        }
        itemRq.setPID(pid);
        final GetObjectTitlesRs response = (GetObjectTitlesRs) send(new DefaultRequestHandle(environment, request, GetObjectTitlesMess.class), false);
        final GetObjectTitlesRs.ObjectTitles.Item itemRs = response.getObjectTitles().getItemList().get(0);
        if (itemRs.getState() == GetObjectTitleResultStateEnum.OBJECT_NOT_FOUND) {
            final org.xmlsoap.schemas.soap.envelope.Detail result = org.xmlsoap.schemas.soap.envelope.Detail.Factory.newInstance();
            final Node detail = result.getDomNode();
            final Element message = detail.getOwnerDocument().createElementNS("http://schemas.radixware.org/faultdetail.xsd", "Message");
            message.appendChild(detail.getOwnerDocument().createTextNode(tableId.toString() + "\nPID=" + pid));
            detail.appendChild(message);
            throw new ObjectNotFoundError(environment, new ServiceCallFault(ServiceProcessFault.FAULT_CODE_SERVER, ExceptionEnum.OBJECT_NOT_FOUND.toString(), result));
        } else if (itemRs.getState() == GetObjectTitleResultStateEnum.ACCESS_DENIED) {
            throw new ServiceCallFault(ServiceProcessFault.FAULT_CODE_SERVER, ExceptionEnum.ACCESS_VIOLATION.toString(), null);
        }
        return itemRs.getTitle();
    }

    @Override
    public GetObjectTitlesRs getEntityTitles(final Id tableId,
            final Id defaultPresentationId,
            final GetObjectTitlesRq.Objects objects)
            throws ServiceClientException, InterruptedException {
        final GetObjectTitlesRq request =
                RequestCreator.getObjectTitles(tableId, defaultPresentationId, objects, environment.getDefManager());
        return (GetObjectTitlesRs) send(new DefaultRequestHandle(environment, request, GetObjectTitlesMess.class), false);
    }
    
    @Override
    public GetObjectTitlesRs getEntityTitles(final Id tableId,
            final IContext.Abstract defaultContext,
            final GetObjectTitlesRq.Objects objects)
            throws ServiceClientException, InterruptedException {
        final GetObjectTitlesRq request =
            RequestCreator.getObjectTitles(tableId, defaultContext, objects, environment.getDefManager());
        return (GetObjectTitlesRs) send(new DefaultRequestHandle(environment, request, GetObjectTitlesMess.class), false);
    }    

    @Override
    public CalcSelectionStatisticRs calcSelectionStatistic(final GroupModel group, final List<AggregateFunctionCall> aggregateFunctions) throws ServiceClientException, InterruptedException {
        final CalcSelectionStatisticRq request = RequestCreator.getCalcSelectionStatistic(group, aggregateFunctions);
        return (CalcSelectionStatisticRs) send(new DefaultRequestHandle(environment, request, CalcSelectionStatisticMess.class), false);
    }        

    @Override
    public org.radixware.kernel.common.auth.PasswordRequirements getPasswordRequirements() throws ServiceClientException, InterruptedException {
        final GetPasswordRequirementsRq request = GetPasswordRequirementsRq.Factory.newInstance();
        final GetPasswordRequirementsRs response = 
            (GetPasswordRequirementsRs) send(new DefaultRequestHandle(environment, request, GetPasswordRequirementsMess.class), false);
        final PasswordRequirements requirements = response.getRequirements();        
        return ClientAuthUtils.getPasswordRequirements(requirements, params.getUserName());
    }

    @Override
    @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
    public void test() throws ServiceClientException, InterruptedException {
        final TestRq request = TestRq.Factory.newInstance();
        send(new DefaultRequestHandle(environment, request, TestMess.class), false);
    }

    @Override
    public void breakRequest() {
        if (executor != null) {
            executor.breakRequest();
        }
    }
    
    private void closeEasSession(){
        final CloseSessionRq request = CloseSessionRq.Factory.newInstance();
        try{
            send(new DefaultRequestHandle(environment, request, CloseSessionMess.class), false);
        }catch(InterruptedException | SessionDoesNotExistError error){//NOPMD
            //ignoring this errors
        }catch(ServiceCallFault fault){//NOPMD ignoring
        }catch(EasError error){
            if (error.getSouceFault()==null){
                tracer.error(error);
            }
        }catch(ServiceClientException | AuthError exception){
            tracer.error(exception);
        }        
    }

    @Override
    public void close(final boolean forced) {
        try{
            if (executor != null) {
                try{
                    if (!forced 
                        && isOpened() 
                        && (executor.isSupportedVersion() || environment.getApplication().getRuntimeEnvironmentType()==ERuntimeEnvironmentType.WEB)){
                        closeEasSession();
                    }
                }finally{
                    executor.close();
                    synchronized(semaphore){
                        executor = null;
                    }
                }
            }
        }finally{
            if (tokenCalculator != null && masterSession==null) {
                tokenCalculator.dispose();
                tokenCalculator = null;
            }
            if (messageProcessor != null) {
                messageProcessor = null;
            }
            previousMessageProcessor = null;
            opened = false;
            params = null;            
        }
    }
        
    private static boolean isCloseSessionRequest(final RequestHandle handle){
        return handle.getExpectedResponseMessageClass()==CloseSessionMess.class;
    }
    
    private static boolean isCreateSessionRequest(final RequestHandle handle){
        return handle.getExpectedResponseMessageClass()==CreateSessionMess.class;
    }
    
    private static boolean isLoginRequest(final RequestHandle handle){
        return handle.getExpectedResponseMessageClass()==LoginMess.class;
    }
    
    @Override
    public void sendAsync(final CommandRequestHandle handle) {
        sendAsync((RequestHandle) handle, INFINITE_RESPONSE_TIMEOUT);
    }

    @Override
    public void sendAsync(final CommandRequestHandle handle, final int timeoutSec) {
        sendAsync((RequestHandle) handle, timeoutSec < 0 ? INFINITE_RESPONSE_TIMEOUT : timeoutSec);

    }

    @Override
    public void sendAsync(final RequestHandle handle) {
        sendAsync(handle, INFINITE_RESPONSE_TIMEOUT);
    }

    @Override
    public void sendAsync(final RequestHandle handle, final int timeoutSec) {
        if (executor == null) {
            throw new IllegalStateException(mp.translate("ClientSessionException", "Can't send request to server: session is not opened"));
        }
        if (handle.isDone()) {
            throw new IllegalArgumentException("Can't use request handle one more time");
        }
        if (!isOpened() && !isBusy()){
            try{
                createSession(false);
            }catch(ServiceClientException exception){
                handle.setException(exception);
                handle.processAnswer();
                return;
            }catch(InterruptedException exceptin){
                handle.onInterrupted();
                handle.processAnswer();
                return;
            }
        }
        executor.schedule(handle, timeoutSec < 0 ? INFINITE_RESPONSE_TIMEOUT : timeoutSec);
    }

    public void renewSslContext(final char[] password) throws KeystoreControllerException, CertificateUtilsException {
        secretStore.setSecret(new TokenProcessor().encrypt(password));        
        executor.getConnection().renewSslContext(secretStore);
    }

    @Override
    public void addListener(Listener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }
        synchronized (semaphore) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    @Override
    public void removeListener(Listener listener) {
        synchronized (semaphore) {
            listeners.remove(listener);
        }
    }
    
    @Override
    public DatabaseInfo getDatabaseInfo(){
        final GetDatabaseInfoRq request = GetDatabaseInfoRq.Factory.newInstance();        
        final GetDatabaseInfoRs response;
        try{
            response =
                (GetDatabaseInfoRs)send(new DefaultRequestHandle(environment, request, GetDatabaseInfoMess.class), false);
        }catch(ServiceClientException exception){
            final String message = environment.getMessageProvider().translate("TraceMessage","Failed to get information about database");
            environment.getTracer().error(message, exception);
            return databaseInfo;
        }catch(InterruptedException ex){            
            return databaseInfo;
        }
        databaseInfo = new DatabaseInfo(response);
        return databaseInfo;
    }

    @Override
    public byte[] decryptBySessionKey(byte[] data){
        if (tokenCalculator instanceof PwdTokenCalculator){
            return ((PwdTokenCalculator)tokenCalculator).decryptData(data, params.getPwdHashAlgorithm());
        }else if (tokenCalculator instanceof KeyTokenCalculator){
            return ((KeyTokenCalculator)tokenCalculator).decryptData(data);
        }else{
            throw new IllegalStateException("Session key does not accessible");
        }        
    }

    @Override
    public byte[] encryptBySessionKey(byte[] data){
        if (tokenCalculator instanceof PwdTokenCalculator){
            return ((PwdTokenCalculator)tokenCalculator).encryptData(data, params.getPwdHashAlgorithm());
        }else if (tokenCalculator instanceof KeyTokenCalculator){
            return ((KeyTokenCalculator)tokenCalculator).encryptData(data);
        }else{
            throw new IllegalStateException("Session key does not accessible");
        }
    }

    @Override
    public boolean isSessionKeyAccessible(){
        return tokenCalculator instanceof PwdTokenCalculator;
    }    

    private void notifyBeforeProcess(final RequestHandle handle) {
        final List<IEasSession.Listener> localListeners = new LinkedList<>(listeners);
        for (IEasSession.Listener listener : localListeners) {
            listener.beforeProcessSyncRequest(handle);
        }
    }

    private void notifyAfterProcess(final RequestHandle handle) {
        final List<IEasSession.Listener> localListeners = new LinkedList<>(listeners);
        for (IEasSession.Listener listener : localListeners) {
            listener.afterSyncRequestProcessed(handle);
        }
    }        

    @Override
    public final void scheduleResponseNotificationTask(final Runnable task) {
        scheduler.scheduleNotificationTask(task);
    }    
    
    public void writeSessionId(final ConnectionParams connectionParams){
        if (messageProcessor!=null){            
            final long sessionId = messageProcessor.getSessionId();
            final ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);
            buffer.putLong(sessionId);
            final byte[] encryptedSessionId;
            if (secretStore!=null && !secretStore.isEmpty()){
                encryptedSessionId = CryptoUtils.encrypt3Des(buffer.array(), secretStore);
            }else{
                encryptedSessionId = CryptoUtils.encrypt3Des(buffer.array(), new byte[]{});
            }
            connectionParams.setEncSessionId(encryptedSessionId);
        }
    }
    
    private static boolean isReadOnlyRequest(final Class<? extends XmlObject> messageClass){        
        return READONLY_RQ_MESSAGE_CLASSES.contains(messageClass);
    }
    
}
