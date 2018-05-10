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

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.dialogs.IWaitForAadcMemberSwitchDialog;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.eas.resources.IFileResource;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.eas.resources.TerminalResources;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.AccessViolationError;
import org.radixware.kernel.common.client.errors.CantDeleteSubobjectsFault;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.DefinitionAccessViolationError;
import org.radixware.kernel.common.client.errors.LogonTimeRestrictionViolationError;
import org.radixware.kernel.common.client.errors.MaxNumberOfSessionsExceededError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.PasswordExpiredError;
import org.radixware.kernel.common.client.errors.SessionDoesNotExistError;
import org.radixware.kernel.common.client.errors.TemporaryPasswordExpiredError;
import org.radixware.kernel.common.client.exceptions.AllSapsBusyException;
import org.radixware.kernel.common.client.exceptions.AsyncRequestsLimitExceededException;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.KernelClassModifiedException;
import org.radixware.kernel.common.client.exceptions.NoSapsForCurrentVersion;
import org.radixware.kernel.common.client.exceptions.ServiceAuthenticationException;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EFileSeekOriginType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.sc.FailedSapsInfo;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.eas.*;//NOPMD
import org.radixware.schemas.easWsdl.*;//NOPMD


abstract class RequestExecutor {

    private static final int INFINITE_RESPONSE_TIMEOUT = 0;//infinite timeout
    private static final long UNSUPPORTED_VERSION_TIMEOUT_MSEC = 10000;
    private static final int ASYNC_TASKS_QUEUE_SIZE_LIMIT = 10;

    private abstract static class RequestTask implements Callable<XmlObject> {

        private final int timeoutSec;        
        private final boolean isCallbackResponse;
        private final RequestHandle requestHandle;
        private volatile ServiceClientException parseException;
        private volatile boolean isResend;
        private volatile boolean isCancelled;
        private Future<XmlObject> requestFuture;

        public RequestTask(final RequestHandle handle, final boolean isCallback, final int timeoutSec) {
            isCallbackResponse = isCallback;
            requestHandle = handle;
            this.timeoutSec = timeoutSec;
        }

        public final int getResponseTimeoutSec() {
            return timeoutSec;
        }

        public boolean isCallback() {
            return isCallbackResponse;
        }

        public RequestHandle getRequestHandle() {
            return requestHandle;
        }

        @Override
        public String toString() {
            return "task for " + getRequestHandle().getRequest().getClass().getSimpleName();
        }

        public void execute(final InternalThreadPoolExecutor executor) {
            if (executor.wasClosed()){
                this.requestFuture = new CancelledFuture<>();
                executor.afterExecute();
            }else{
                this.requestFuture = executor.submit(this);
            }
        }

        public boolean isDone() {
            return requestFuture != null && requestFuture.isDone();
        }

        public XmlObject getResult() throws InterruptedException, CancellationException, ExecutionException {
            if (parseException!=null){
                throw new ExecutionException(parseException);
            }
            return requestFuture.get();
        }
        
        public void setParseException(final ServiceClientException exception){
            parseException = exception;
        }

        public void setResend(final boolean resend) {
            isResend = resend;
        }

        public boolean isResend() {
            return isResend;
        }

        public void cancel() {
            isCancelled = true;
            if (requestFuture != null) {
                requestFuture.cancel(true);
            }
        }

        public boolean isCancelled() {
            return isCancelled;
        }
    };

    private final class PrimaryRequestTask extends RequestTask {
        
        private final boolean isInteractive;
        private final boolean isAsync;
               
        public PrimaryRequestTask(final RequestHandle handle, 
                                                 final int timeoutSec, 
                                                 final boolean isInteractive,
                                                 final boolean isAsync) {
            super(handle, false, timeoutSec);
            this.isInteractive = isInteractive;
            this.isAsync = isAsync;
        }

        @Override
        public XmlObject call() throws Exception {
            if (isCancelled()) {
                throw new InterruptedException("Request was cancelled");
            }
            final XmlObject request = getRequestHandle().getRequest();
            if (isAsync){
                easTrace.clear();
            }
            final XmlObject response =
                connection.sendRequest(prepareRequest(request), 
                                        getCurrentScpName(), 
                                        getCurrentDefinitionVersion(request),
                                        getResponseTimeoutSec(),
                                        isInteractive ? RequestExecutor.this.aadcMemberSwitchController : null);
            if (response instanceof GetSecurityTokenMess) {
                return processGetTokenCallback(((GetSecurityTokenMess) response).getGetSecurityTokenRq());
            }
            return response;
        }
    }

    private final class CallbackResponseTask extends RequestTask {

        private final XmlObject response;

        public CallbackResponseTask(final RequestHandle handle, final XmlObject response, final int timeoutSec) {
            super(handle, true, timeoutSec);
            this.response = response;
        }

        @Override
        public XmlObject call() throws Exception {
            return connection.sendCallbackResponse(response, getResponseTimeoutSec());
        }

        @Override
        public String toString() {
            return "callback task for " + response.getClass().getSimpleName();
        }
    }

    private final class SendFaultMessageTask extends RequestTask {

        private final String message;
        private final Throwable error;

        public SendFaultMessageTask(final RequestHandle handle, final String msg, final Throwable error, int timeoutSec) {
            super(handle, true, timeoutSec);
            this.message = msg;
            this.error = error;
        }

        @Override
        public XmlObject call() throws Exception {
            return connection.sendFaultMessage(message, error, getResponseTimeoutSec());
        }

        @Override
        public String toString() {
            return "fault message task " + message;
        }
    }

    private final static class RequestTaskDeque {

        private final Deque<RequestTask> asyncTasks = new ArrayDeque<>();
        private RequestTask testTask;
        private RequestTask syncTask;
        private RequestTask callBackTask;

        private void addTask(final RequestTask task, final boolean sync) {
            if (task.isCallbackResponse) {
                if (callBackTask == null) {
                    callBackTask = task;
                } else {
                    throw new IllegalStateException("Onlly one callback task can be scheduled");
                }
            } else {
                if (sync) {
                    syncTask = task;
                } else {
                    asyncTasks.add(task);
                }
            }
        }

        public void addSyncTask(final RequestTask task) {
            addTask(task, true);
        }

        public void addAsyncTask(final RequestTask task) throws AsyncRequestsLimitExceededException{
            if (asyncTasks.size()>=ASYNC_TASKS_QUEUE_SIZE_LIMIT){
                throw new AsyncRequestsLimitExceededException();
            }
            addTask(task, false);
        }

        public void addTestTask(final RequestTask task) {
            if (testTask == null) {
                testTask = task;
                testTask.getRequestHandle().activate();
            }
        }

        public void removeScheduledTestTask() {
            testTask = null;
        }

        public RequestTask takeHead() {
            if (!isEmpty() && testTask != null) {
                final RequestTask result = testTask;
                testTask = null;
                return result;
            } else if (callBackTask == null) {
                if (syncTask == null) {
                    return asyncTasks.pollFirst();
                } else {
                    final RequestTask result = syncTask;
                    syncTask = null;
                    return result;
                }
            } else {
                final RequestTask result = callBackTask;
                callBackTask = null;
                return result;
            }
        }

        public boolean isEmpty() {
            return callBackTask == null && syncTask == null && asyncTasks.isEmpty();
        }

        public boolean containsSyncTask() {
            return syncTask != null;
        }

        public boolean isTestRequestSheduled() {
            return testTask != null;
        }

        public void cancelSyncTask() {
            if (syncTask != null) {
                syncTask.cancel();
            }
        }
        
        public void dropSyncTask(){
            syncTask = null;
        }
        
        public int getAsyncTasksCount(){
            return asyncTasks.size();
        }
    }

    private final static class CallbackRequestHandle extends RequestHandle {
        
        private final RequestHandle generalRequestHandle;

        public CallbackRequestHandle(final RequestHandle generalRequestHandle) {
            super(generalRequestHandle.environment, null, null);
            this.generalRequestHandle = generalRequestHandle;
        }

        @Override
        public boolean hasListeners() {
            return true;//in any way we must to send response to server
        }

        @Override
        void processAnswer() {
            generalRequestHandle.processAnswer(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        void setException(final ServiceClientException exception) {
            generalRequestHandle.setException(exception); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        void setResponse(final XmlObject response) {
            generalRequestHandle.setResponse(response);
        }

        @Override
        void onInterrupted() {
            generalRequestHandle.onInterrupted();
        }        
    }

    private static final class InternalThreadFactory implements ThreadFactory {

        private static long threadIndex = 0;

        @Override
        public Thread newThread(final Runnable r) {
            final Thread resultThread = new Thread(r, "RequestExecutorThread-" + String.valueOf(threadIndex++));//NOPMD
            resultThread.setContextClassLoader(RequestExecutor.class.getClassLoader());
            return resultThread;
        }
    }
    
    private final static class CancelledFuture<T> implements Future<T>{

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return true;
        }

        @Override
        public boolean isCancelled() {
            return true;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            throw new CancellationException("Request was cancelled");
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new CancellationException("Request was cancelled");
        }
        
    }

    private final class InternalThreadPoolExecutor extends ThreadPoolExecutor {                

        private boolean closed;

        public InternalThreadPoolExecutor() {
            super(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new InternalThreadFactory());
        }

        @Override
        protected void afterExecute(final Runnable r, final Throwable t) {
            synchronized (executorSemaphore) {
                if (!closed) {
                    afterExecute();
                }
            }
        }

        public void afterExecute() {//synchronized by executorSemaphore
            if (currentTask != null && currentTask.isDone()) {
                try {
                    processResponse();
                } catch (ServiceClientException ex) {
                    final String msg = environment.getMessageProvider().translate("ClientSessionException", "Can't process asynchronous request request: %s\n%s");
                    final String info = ClientException.getExceptionReason(environment.getMessageProvider(), ex);
                    final String stack = ClientException.exceptionStackToString(ex);
                    environment.getTracer().put(EEventSeverity.ERROR, String.format(msg, info, stack), EEventSource.CLIENT_SESSION);
                } catch (InterruptedException ex) {
                    environment.getTracer().put(EEventSeverity.DEBUG, "async request was cancelled", EEventSource.CLIENT_SESSION);
                }
            } else {
                debug_message("request finished but current task is done");
            }
            afterResponseWasReceived();
        }

        public void close() {//synchronized by executorSemaphore
            closed = true;
            shutdownNow();
        }
        
        public boolean wasClosed(){//synchronized by executorSemaphore
            return closed;
        }
    }
    
    private final static class ProcessAadcMemberSwitchTask implements Runnable{//this task will be executed in GUI thread        
        
        private final java.lang.Object sem = new java.lang.Object();
        private final CountDownLatch latch = new CountDownLatch(1);
        private final IClientEnvironment environment;
        private boolean cancelled;
        private volatile IWaitForAadcMemberSwitchDialog waitDialog;
        
        public ProcessAadcMemberSwitchTask(final IClientEnvironment environment){
            this.environment = environment;
        }

        @Override
        public void run() {
            try{
                synchronized(sem){
                    if (!cancelled){
                        waitDialog = 
                            environment.getApplication().getDialogFactory().newWaitForAadcMemberSwitchDialog(environment);                    
                        waitDialog.display();
                    }
                }                
            }finally{
                latch.countDown();
            }
        }
        
        public IWaitForAadcMemberSwitchDialog getWaitDialog(){
            synchronized(sem){
                return waitDialog;
            }
        }
        
        public void waitForFinish() throws InterruptedException{
            if (!latch.await(EasClient.KEEP_AADC_MEMBER_INTERVAL_MILLIS*2, TimeUnit.MILLISECONDS)){
                synchronized(sem){
                    cancelled = true;
                }
            }
        }
    }
    
    private final static class ObserveAadcMemberSwitchWaitDialogTask implements Runnable{//this task will be executed in GUI thread        
        
        private final java.lang.Object sem = new java.lang.Object();
        private final CountDownLatch latch = new CountDownLatch(1);
        private final IWaitForAadcMemberSwitchDialog dialog;
        private volatile IWaitForAadcMemberSwitchDialog.Button pressedButton;        
        
        public ObserveAadcMemberSwitchWaitDialogTask(final IWaitForAadcMemberSwitchDialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void run() {
            try{
                synchronized(sem){
                    pressedButton = dialog.getPressedButton();
                }
            }finally{
                latch.countDown();
            }
        }
        
        public IWaitForAadcMemberSwitchDialog.Button getPressedButton(){
            synchronized(sem){
                return pressedButton;
            }
        }
        
        public boolean waitForFinish() throws InterruptedException{
            return latch.await(EasClient.KEEP_AADC_MEMBER_INTERVAL_MILLIS*2, TimeUnit.MILLISECONDS);
        }
    }
    
    private final static class CloseAadcMemberSwitchWaitDialogTask implements Runnable{//this task will be executed in GUI thread        
        
        private final IWaitForAadcMemberSwitchDialog dialog;
        
        public CloseAadcMemberSwitchWaitDialogTask(final IWaitForAadcMemberSwitchDialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void run() {
            dialog.finish();
        }
    }    
    
    private final static class InteractiveAadcMemberSwitchController implements IAadcMemberSwitchController{
        
        private final IClientEnvironment environment;
        private final IEventLoop localEventLoop;
        private IWaitForAadcMemberSwitchDialog waitDialog;
        
        public InteractiveAadcMemberSwitchController(final IClientEnvironment environment, final IEventLoop localEventLoop){
            this.environment = environment;
            this.localEventLoop = localEventLoop;
        }

        @Override
        public boolean canSwitch() throws InterruptedException {
            if (localEventLoop!=null && localEventLoop.isInProgress()){
                if (waitDialog == null){
                    final ProcessAadcMemberSwitchTask task = 
                        new ProcessAadcMemberSwitchTask(environment);
                    localEventLoop.scheduleTask(task);
                    task.waitForFinish();
                    waitDialog = task.getWaitDialog();
                    return false;
                }else{
                    final ObserveAadcMemberSwitchWaitDialogTask task = 
                            new ObserveAadcMemberSwitchWaitDialogTask(waitDialog);
                    localEventLoop.scheduleTask(task);
                    task.waitForFinish();
                    final IWaitForAadcMemberSwitchDialog.Button button = task.getPressedButton();
                    if (button==null){
                        return false;
                    }else{
                        localEventLoop.scheduleTask(new CloseAadcMemberSwitchWaitDialogTask(waitDialog));
                        waitDialog = null;
                        if (button==IWaitForAadcMemberSwitchDialog.Button.IMMEDIATE_SWITCH){
                            return true;
                        }else{
                            throw new InterruptedException();
                        }                    
                    }                    
                }
            }else{
                return false;
            }
        }

        @Override
        public void stopSwitching() {
            if (waitDialog!=null && localEventLoop!=null && localEventLoop.isInProgress()){
                localEventLoop.scheduleTask(new CloseAadcMemberSwitchWaitDialogTask(waitDialog));
            }
            waitDialog = null;
        }
        
    }
    
    private RequestTask currentTask;//synchronized by executorSemaphore
    private final java.lang.Object executorSemaphore = new java.lang.Object();//to synchronize with ResponseWatcherTask
    private InternalThreadPoolExecutor executor = new InternalThreadPoolExecutor();
    private final IEasClient connection;
    private String currentScpName;    
    private final IClientEnvironment environment;
    private final MessageProvider mp;
    private final ClientTracer tracer;    
    private final IProgressHandle progressHandle;    
    private final IEventLoop localEventLoop;    
    private final boolean isInDevelopmentMode;
    private final boolean isWeb;
    private final boolean isInteractive;
    private EasTrace easTrace;
    private volatile FailedSapsInfo failedSaps;
    private volatile long unsupportedVersion = -1;
    private volatile long unsupportedVersionTime = 0;
    private final RequestTaskDeque tasks = new RequestTaskDeque();//synchronized by executorSemaphore
    //Набор задач на выполнение асинхронных запросов, на которые ответ уже получен,
    //но отдавать его обработчику нельзя, поскольку в очереди есть задача на выполнение
    //синхронного запроса
    private final Collection<RequestTask> completedTasks = new ArrayList<>();//synchronized by executorSemaphore
    private final List<String> debug_msg = new ArrayList<>();
    private final IAadcMemberSwitchController aadcMemberSwitchController;
    private volatile boolean interactiveCallBackRequestProcessing;    
    
    public RequestExecutor(final IClientEnvironment environment, 
                           final EasTrace easTrace, 
                           final IEasClient soapConnection,
                           final boolean isInteractive) {
        connection = soapConnection;
        this.environment = environment;
        this.isInteractive = isInteractive;        
        isInDevelopmentMode = RunParams.isDevelopmentMode();
        isWeb = environment.getApplication().getRuntimeEnvironmentType()==ERuntimeEnvironmentType.WEB;
        if (isInteractive){
            progressHandle = environment.getProgressHandleManager().newProgressHandle(new IProgressHandle.Cancellable() {
                @Override
                public void onCancel() {
                    breakRequest();
                }
            });
        }else{
            progressHandle = null;
        }
        mp = environment.getMessageProvider();
        tracer = environment.getTracer();        
        localEventLoop = isInteractive ? environment.newEventLoop() : new DummyEventLoop();
        this.easTrace = easTrace;
        aadcMemberSwitchController = new InteractiveAadcMemberSwitchController(environment, localEventLoop);
    }
    
    private IEventLoop getEventLoop(){
        return localEventLoop;
    }
    
    private boolean canUseGui(){
        return isInteractive && environment.getApplication().isInGuiThread();
    }

    public void breakRequest() {
        synchronized (executorSemaphore) {
            if (currentTask != null && !currentTask.isDone() && !currentTask.isCancelled()) {
                connection.close();
                debug_message("Cancel current task");
                currentTask.cancel();
                executor.close();
                if (tasks.containsSyncTask()) {
                    debug_message("Cancel sync task");
                    tasks.cancelSyncTask();
                }
                executor = new InternalThreadPoolExecutor();
                executor.afterExecute();
            }else if ((currentTask == null || (currentTask.isDone() && !currentTask.isCancelled()))
                         && getEventLoop().isInProgress()){
                debug_message("Cancel current task and stopping event loop");
                if (currentTask!=null){
                    currentTask.cancel();
                }
                getEventLoop().stop();
            }
        }
    }

    IEasClient getConnection() {
        return connection;
    }

    private static boolean isTestRequest(RequestHandle handler) {
        return handler.getExpectedResponseMessageClass() == TestMess.class;
    }
    
    private static boolean isCloseRequestTask(RequestTask task){
        return isCloseRequest(task.getRequestHandle());
    }    
    
    private static boolean isCloseRequest(RequestHandle handler){
        return handler.getExpectedResponseMessageClass()==CloseSessionMess.class;
    }     
    
    private static boolean isCreateSessionRequest(final RequestHandle handler){
        return handler.getExpectedResponseMessageClass() == CreateSessionMess.class;
    }

    public void setCurrentScpName(final String scpName) {
        synchronized(executorSemaphore){
            currentScpName = scpName;
        }
    }
    
    public void schedule(final RequestHandle requestHandle, final int timeoutSec) {
        synchronized (executorSemaphore) {
            if (isVersionSupported() || isCloseRequest(requestHandle)){
                try{
                    queueRequestTask(new PrimaryRequestTask(requestHandle, timeoutSec, false, true), true);
                }catch(AsyncRequestsLimitExceededException exception){
                    requestHandle.setException(exception);
                    requestHandle.processAnswer();
                }
            }else{
                requestHandle.setException(new NoSapsForCurrentVersion(failedSaps, mp));
                requestHandle.processAnswer();                
            }
        }
    }
    
    public void scheduleTestRequest() {
        synchronized (executorSemaphore) {
            if (isVersionSupported()){
                scheduleTestRequestImpl();
            }
        }
    }
    
    //synchronized by executorSemaphore
    private void scheduleTestRequestImpl(){
        if (!tasks.isTestRequestSheduled()) {
            final TestRq request = TestRq.Factory.newInstance();
            final RequestHandle handler = new DefaultRequestHandle(environment, request, TestMess.class);
            try {
                final RequestTask requestTask = createNextRequestTask(handler, null, INFINITE_RESPONSE_TIMEOUT);
                tasks.addTestTask(requestTask);
            } catch (InterruptedException ex) {
                easTrace.debug("Unexpected Interrupted Exception:\n" + ClientException.exceptionStackToString(ex), true);
            }
        }        
    }

    //synchronized by executorSemaphore
    @SuppressWarnings("unchecked")
    private void onAsyncRequestException(final ServiceClientException exception) {
        final RequestTask asyncTask = currentTask;
        debug_message("Ready for next task");
        currentTask = null;//ready to next task
        if (exception instanceof ServiceCallFault) {
            processFault((ServiceCallFault) exception);
        } else {
            tracer.getBuffer().add(easTrace.getBuffer());
        }
        easTrace.clear();
        asyncTask.getRequestHandle().setException(exception);
        if (!getEventLoop().isInProgress()) {
            asyncTask.getRequestHandle().processAnswer();
        } else {
            completedTasks.add(asyncTask);
        }
    }

    //synchronized by executorSemaphore
    @SuppressWarnings("unchecked")
    private void onAsyncRequestAnswer(final XmlObject answer) throws ServiceAuthenticationException {
        final RequestTask asyncTask = currentTask;
        debug_message("Ready for next task");
        currentTask = null;
        tracer.getBuffer().startMerge(easTrace.getBuffer());
        try{
            asyncTask.getRequestHandle().setResponse(onResponseReceived(answer));
        }finally{
            tracer.getBuffer().finishMerge();
            easTrace.clear();            
        }
        if (!getEventLoop().isInProgress()) {
            asyncTask.getRequestHandle().processAnswer();
        } else {
            completedTasks.add(asyncTask);
        }
    }

    //synchronized by executorSemaphore
    private void onAsyncRequestCancelled() {
        final RequestTask asyncTask = currentTask;
        debug_message("Ready for next task");
        currentTask = null;
        scheduleTestRequestImpl();
        asyncTask.getRequestHandle().onInterrupted();
        if (!getEventLoop().isInProgress()) {
            asyncTask.getRequestHandle().processAnswer();
        } else {
            completedTasks.add(asyncTask);
        }
    }

    protected abstract XmlObject prepareRequest(final XmlObject request) throws ServiceClientException;
    
    protected abstract long getCurrentDefinitionVersion(final XmlObject request);

    protected abstract XmlObject onResponseReceived(final XmlObject answer) throws ServiceAuthenticationException;

    protected abstract XmlObject processGetTokenCallback(GetSecurityTokenRq request) throws ServiceClientException, InterruptedException;
    
    public XmlObject execute(final RequestHandle handler) throws ServiceClientException, InterruptedException {
        synchronized (executorSemaphore) {
            if (getEventLoop().isInProgress() || tasks.containsSyncTask()) {
                final IllegalStateException exception = new IllegalStateException("Another request is already invoked");
                System.out.println("!!!!!!!! Another request is already invoked !!!!!!!!!!!!!!!!!!!!!!!");
                exception.printStackTrace(System.out);
                System.out.println("!!!!!!!! Another request is already invoked !!!!!!!!!!!!!!!!!!!!!!!");
                throw exception;
            }
            if (!isVersionSupported() && !isCloseRequest(handler)){
                throw new NoSapsForCurrentVersion(failedSaps, mp);
            }
        }
        XmlObject response = null;
        boolean tryAgain, isCallbackResponse;
        final boolean ownWaitDialog = canUseGui()
                                      && environment.getProgressHandleManager().getActive() == null;
        if (ownWaitDialog && progressHandle!=null) {
            progressHandle.startProgress(mp.translate("ExplorerMessage", "Waiting for server response"), true);
        }
        try {
            RequestTask requestTask = createNextRequestTask(handler, response, INFINITE_RESPONSE_TIMEOUT);
            do {
                tryAgain = false;
                //make request and try repeat it if some connection problem occurs
                do {
                    try {
                        response = executeSyncRequestTask(requestTask);
                        isCallbackResponse = EasMessageProcessor.isCallBackRequest(response);
                        if (isCallbackResponse) {
                            //currentTask must be not null here
                            requestTask = createNextRequestTask(handler, response, INFINITE_RESPONSE_TIMEOUT);
                        }
                        break;
                    } catch(NoSapsForCurrentVersion ex){                        
                        if (!isInDevelopmentMode && !isWeb && canUseGui() && !isCloseRequest(handler) && ex.canWaitForBusySaps()){
                            isCallbackResponse = false;
                            final String title = mp.translate("ClientSessionException", "Confirm to Update Version");
                            final String message;
                            message = mp.translate("ClientSessionException", "New version found.\nThere are no free servers to process request from application with current version at this moment.\nHowever there are servers that can process request from application with new version.\nYou may restart application to update version (all your unsaved data will be lost) or wait some time and repeat request.");
                            final IMessageBox messageBox = environment.newMessageBoxDialog(message, title, EDialogIconType.QUESTION, null);
                            messageBox.removeButton(EDialogButtonType.OK);
                            {
                                final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.REFRESH);
                                final String buttonText;
                                buttonText = mp.translate("ClientSessionException", "Restart application");
                                messageBox.addButton(EDialogButtonType.YES, buttonText, btnIcon);
                            }
                            {
                                final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.REDO);
                                final String buttonText = mp.translate("ClientSessionException", "Repeat request");
                                messageBox.addButton(EDialogButtonType.RETRY, buttonText, btnIcon);
                            }
                            {
                                final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_CANCEL);
                                final String buttonText = mp.translate("ClientSessionException", "Cancel operation");
                                messageBox.addButton(EDialogButtonType.CANCEL, buttonText, btnIcon);
                            }
                            switch (messageBox.execMessageBox()){
                                case YES:
                                    throw ex;
                                case RETRY:
                                    tryAgain = true;
                                    break;
                                default:
                                    throw new InterruptedException();
                            }
                        }else{
                            throw ex;
                        }
                    }catch (AllSapsBusyException | ServiceCallTimeout ex){
                        if (canUseGui() && !isCloseRequest(handler)){
                            isCallbackResponse = false;
                            final String title = mp.translate("ClientSessionException", "Connection Problem");
                            final String message = mp.translate("ClientSessionException", "Can't open connection\nTry again?");
                            if (!environment.messageConfirmation(title, message)) {
                                throw ex;
                            }
                            tryAgain = true;
                        }else{
                            throw ex;
                        }                    
                    } finally {
                        synchronized (executorSemaphore) {
                            debug_message("Ready for next task");
                            currentTask = null;
                        }
                    }
                } while (tryAgain);
            } while (isCallbackResponse);
            final Class<? extends XmlObject> resultClass = handler.getExpectedResponseMessageClass();
            if (resultClass != null && !resultClass.isInstance(response)) {
                response = castToResultClass(response, resultClass);
            }
        } finally {
            if (ownWaitDialog && progressHandle!=null) {
                progressHandle.finishProgress();
            }
        }
        return response;
    }

    private RequestTask createNextRequestTask(final RequestHandle handler, 
                                                                       final XmlObject response, 
                                                                       final int timoutSec) throws InterruptedException {
        RequestTask requestTask;
        if (response == null) {//make first request
            requestTask = new PrimaryRequestTask(handler, timoutSec, isInteractive, false);
        } else {//process callback request from server
            interactiveCallBackRequestProcessing = isInteractiveCallBackRequest(response);
            try {
                final XmlObject callbackResponse = processCallBackRequest(environment, response, isInteractive);
                requestTask = new CallbackResponseTask(handler, callbackResponse, timoutSec);
            } catch (RuntimeException ex) {
                final String message = "Exception occurred during callback request processing: \n"
                        + ClientException.exceptionStackToString(ex);
                tracer.error(message);
                requestTask = new SendFaultMessageTask(handler, message, ex, timoutSec);
            } catch (TerminalResourceException ex) {
                {
                    final String message = "Exception occurred during callback request processing: \n"
                            + ex.getMessage() + "\n" + ClientException.exceptionStackToString(ex);
                    tracer.error(message);
                }
                final String message = ex.getMessage();
                requestTask = new SendFaultMessageTask(handler, message, ex, timoutSec);
            } catch (IOException ex) {
                {
                    final String message = "Exception occurred during callback request processing: \n"
                            + ex.getMessage() + "\n" + ClientException.exceptionStackToString(ex);
                    tracer.error(message);
                }
                final String message = ex.getMessage();
                requestTask = new SendFaultMessageTask(handler, message, ex, timoutSec);
            } finally{
                interactiveCallBackRequestProcessing  = false;
            }
        }
        return requestTask;
    }
    
    public final boolean inInteractiveCallBackRequestProcessing(){
        return interactiveCallBackRequestProcessing;
    }

    public final boolean isBusy() {
        //Ждем ответа или обрабатываем результат.
        synchronized (executorSemaphore) {
            return currentTask != null || getEventLoop().isInProgress();
        }
    }
    
    public final boolean isSupportedVersion(){
        synchronized (executorSemaphore){
            return failedSaps==null
                       || (System.currentTimeMillis() - unsupportedVersionTime)>UNSUPPORTED_VERSION_TIMEOUT_MSEC;
        }
    }

    private XmlObject executeSyncRequestTask(final RequestTask requestTask) throws ServiceClientException, InterruptedException, CredentialsWasNotDefinedError {
        if (canUseGui() && !isCloseRequestTask(requestTask) && !environment.getDefManager().getAdsVersion().isActualized()) {//RADIX-6501            
            try {
                environment.getDefManager().getAdsVersion().prepareSwitchVersion(environment, -1, true);
            } catch (CantUpdateVersionException exception) {
                if (requestTask.getRequestHandle() != null && requestTask.getRequestHandle().getRequest() instanceof CreateSessionRq) {
                    throw exception;
                } else {
                    tracer.error(mp.translate("ExplorerError", "Can't load runtime components"), exception);
                }
            }
        }
        boolean resend = false;
        do {
            synchronized (executorSemaphore) {
                if (isTestRequest(requestTask.getRequestHandle()) && tasks.isTestRequestSheduled()) {
                    tasks.removeScheduledTestTask();//remove automatically scheduled test task
                }
                tasks.addSyncTask(requestTask);
            }
            if (requestTask.getRequestHandle() != null) {
                requestTask.getRequestHandle().activate();
            }
            getEventLoop().scheduleTask(new Runnable() {
                @Override
                public void run() {
                    synchronized (executorSemaphore) {
                        if (!trySubmitNextTask()){
                            debug_message("Submiting task was rejected!!!");
                            getEventLoop().stop();
                        }
                    }
                }
            });
            debug_message("Start event loop");
            getEventLoop().start();            
            synchronized (executorSemaphore) {                
                if (currentTask==null || currentTask.isCancelled()){
                    onRequestCancelled();
                }else{
                    try {
                        return currentTask.getResult();
                    } catch (ExecutionException e) {
                        try {
                            processExecutionException(e, requestTask.getRequestHandle(), false, resend);
                            resend = true;
                        } finally {
                            debug_message("Ready for next task");
                            currentTask = null;
                        }
                    } catch (CancellationException e) {
                        onRequestCancelled();
                    }
                }
            }
        } while (resend);
        return null;
    }
    
    private void onRequestCancelled() throws InterruptedException{
        scheduleTestRequestImpl();
        tracer.put(EEventSeverity.DEBUG, "request was cancelled", EEventSource.CLIENT_SESSION);
        throw new InterruptedException("Eas request was cancelled");//NOPMD        
    }

    void afterResponseWasReceived() {
        synchronized (executorSemaphore) {
            if (currentTask == null) {
                if (!tasks.isEmpty()) {
                    trySubmitNextTask();
                } else {
                    debug_message("no more tasks scheduled");
                }
                if (!getEventLoop().isInProgress()) {
                    for (RequestTask asyncTask : completedTasks) {
                        asyncTask.getRequestHandle().processAnswer();
                    }
                    completedTasks.clear();
                }
            }
        }
    }

    //synchronized by executorSemaphore
    private void queueRequestTask(final RequestTask requestTask, final boolean isAsync) throws AsyncRequestsLimitExceededException {        
        if (isTestRequest(requestTask.getRequestHandle()) && tasks.isTestRequestSheduled()) {
            tasks.removeScheduledTestTask();//remove automatically scheduled test task
        }
        if (isAsync) {
            tasks.addAsyncTask(requestTask);
        } else {
            tasks.addSyncTask(requestTask);
        }

        if (requestTask.getRequestHandle() != null) {
            requestTask.getRequestHandle().activate();
        }
        if (currentTask == null) {
            trySubmitNextTask();
        }
    }

    //synchronized by executorSemaphore
    private boolean trySubmitNextTask() {
        if (currentTask!=null){
            return true;//some task already submitted
        }
        if (tasks.isEmpty()){
            return false;
        }
        RequestTask requestTask;
        do {
            requestTask = tasks.takeHead();
        } while (requestTask != null && !requestTask.getRequestHandle().beforeExecute(this));
        if (requestTask==null){
            return false;//failed to submit any task
        }else{
            debug_message("Start task executing");
            currentTask = requestTask;
            currentTask.execute(executor);
            return true;
        }
    }

    //synchronized by executorSemaphore
    private void processResponse() throws ServiceClientException, InterruptedException {
        if (getEventLoop().isInProgress()){
            if (tasks.containsSyncTask()){
                if (isTestRequest(currentTask.getRequestHandle())){
                    if (processTestResponse()){
                        debug_message("Ready for next task");
                        currentTask = null;
                    }else{
                        tasks.dropSyncTask();//cancel main request
                        scheduleTestRequestImpl();//repeat test request
                        debug_message("Drop sync task and stop event loop");
                        getEventLoop().stop();//process postponed exception after exit from event loop
                    }
                    return;
                }// else process response to async request
            }else{
                debug_message("Stop event loop");
                getEventLoop().stop();//processed after exit from event loop
                return;
            }
        }
        debug_message("Process response to async request");
        XmlObject response;
        final boolean isCallback;
        try {
            response = currentTask.getResult();
            isCallback = EasMessageProcessor.isCallBackRequest(response);
            if (!isCallback) {
                final Class<? extends XmlObject> resultClass = currentTask.getRequestHandle().getExpectedResponseMessageClass();
                if (resultClass != null && !resultClass.isInstance(response)) {
                    try {
                        response = castToResultClass(response, resultClass);
                    } catch (ServiceClientException exception) {
                        onAsyncRequestException(exception);
                        return;
                    }
                }
            }
        } catch (ExecutionException ex) {
            processExecutionException(ex, currentTask.getRequestHandle(), true, currentTask.isResend());
            return;
        } catch (InterruptedException | CancellationException ex) {
            //request was interrupted just log.
            tracer.put(EEventSeverity.DEBUG, "async request was cancelled", EEventSource.CLIENT_SESSION);
            onAsyncRequestCancelled();
            return;
        }

        if (isCallback) {
            final RequestTask nextTask;
            try {
                final CallbackRequestHandle callbackRqHandle = new CallbackRequestHandle(currentTask.getRequestHandle());
                nextTask = createNextRequestTask(callbackRqHandle, response, currentTask.getResponseTimeoutSec());
            } catch (InterruptedException ex) {
                //request was interrupted just log.
                tracer.put(EEventSeverity.DEBUG, "request was cancelled", EEventSource.CLIENT_SESSION);
                return;
            } finally {
                debug_message("Ready for next task");
                currentTask = null;//ready to next request;
            }                
            queueRequestTask(nextTask, false/*callback tasks is not async*/);
        } else {
            onAsyncRequestAnswer(response);
        }        
    }
    
    //synchronized by executorSemaphore
    private boolean processTestResponse(){
        debug_message("Process response to test request");
        XmlObject response;
        try {
            response = currentTask.getResult();
            if (!TestMess.class.isInstance(response)) {
                try {
                    response = castToResultClass(response, TestMess.class);
                } catch (ServiceClientException exception) {
                    currentTask.setParseException(exception);
                    return false;
                }
            }
        } catch (ExecutionException | InterruptedException | CancellationException ex) {
            return false;
        }
        try{
            onResponseReceived(response);
        }catch(ServiceAuthenticationException exception){
            currentTask.setParseException(exception);
            return false;
        }
        return true;
    }

    //synchronized by executorSemaphore
    private void processExecutionException(final ExecutionException ex, final RequestHandle handler, final boolean asyncRequest, final boolean resend) throws ServiceClientException, InterruptedException {
        final Throwable exception = ex.getCause();
        final boolean isTestRequest = isTestRequest(handler);
        if (exception instanceof ServiceCallFault) {
            final ServiceCallFault fault = (ServiceCallFault) exception;
            final boolean brokenChallenge = fault.getFaultString().equals(ExceptionEnum.INVALID_PASSWORD.toString())
                    || fault.getFaultString().equals(ExceptionEnum.INVALID_CHALLENGE.toString());
            if (brokenChallenge && !isTestRequest && !isCloseRequest(handler)) {
                final XmlObject request = handler.getRequest();
                final boolean isPwdRequest = (request instanceof ChangePasswordRq);
                final boolean isLoginRequest = (request instanceof LoginRq);
                if (!resend && !isPwdRequest && !isLoginRequest) {
                    processFault(fault);
                    tracer.put(EEventSeverity.DEBUG, "Token is wrong. Trying to restore token...", EEventSource.CLIENT_SESSION);
                    scheduleTestRequestImpl();
                    if (asyncRequest) {
                        final RequestTask repeatTask = createNextRequestTask(handler, null, currentTask.getResponseTimeoutSec());
                        repeatTask.setResend(true);
                        queueRequestTask(repeatTask, true);
                        debug_message("Ready for next task");
                        currentTask = null;
                    }
                    return;
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.INVALID_DEFINITION_VERSION.toString())) {
                if (canUseGui() && !isCloseRequest(handler)){
                    if (!processInvalidDefinitionVersionFault(fault.getMessage(), handler.getRequest())){
                        throw fault;
                    }
                    if (asyncRequest) {
                        final RequestTask repeatTask = createNextRequestTask(handler, null, currentTask.getResponseTimeoutSec());
                        queueRequestTask(repeatTask, true);
                        debug_message("Ready for next task");
                        currentTask = null;
                    }
                }else{
                    if (asyncRequest){
                        onAsyncRequestException(fault);
                        return;                        
                    }else{
                        throw fault;
                    }
                }
                return;
            } else if (fault.getFaultString().equals(ExceptionEnum.OBJECT_NOT_FOUND.toString())) {
                final ObjectNotFoundError error = new ObjectNotFoundError(environment, fault);
                if (asyncRequest) {
                    onAsyncRequestException(error);
                    return;
                } else {
                    throw error;
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.DEFINITION_ACCESS_VIOLATION.toString())) {
                final DefinitionAccessViolationError error = new DefinitionAccessViolationError(environment, fault);
                if (asyncRequest) {
                    onAsyncRequestException(error);
                    return;
                } else {
                    throw error;
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.ACCESS_VIOLATION.toString())) {
                final AccessViolationError error = new AccessViolationError(fault);
                if (asyncRequest) {
                    onAsyncRequestException(error);
                    return;
                } else {
                    throw error;
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.LOGON_TIME_RESTRICTION_VIOLATION.toString())){
                final boolean onCreate = isCreateSessionRequest(handler);
                if (asyncRequest){
                    onAsyncRequestException(fault);
                    return;
                }else{
                    throw new LogonTimeRestrictionViolationError(onCreate, fault);
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.TEMPORARY_PASSWORD_EXPIRED.toString())){
                final boolean onCreate = isCreateSessionRequest(handler);
                if (asyncRequest){
                    onAsyncRequestException(fault);
                    return;
                }else{
                    throw new TemporaryPasswordExpiredError(onCreate, fault);
                }
            }else if (fault.getFaultString().equals(ExceptionEnum.SESSION_DOES_NOT_EXIST.toString())) {
                if (asyncRequest){
                    onAsyncRequestException(fault);
                    return;
                }else{
                    throw new SessionDoesNotExistError(fault);
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.SESSIONS_LIMIT_EXCEED.toString())){
                final String message = fault.getMessage();
                final UserSessionsDocument userSessionsDocument;
                try{
                    userSessionsDocument = UserSessionsDocument.Factory.parse(message);
                    if (asyncRequest){
                        onAsyncRequestException(fault);
                    }else{
                        throw new MaxNumberOfSessionsExceededError(userSessionsDocument, fault);
                    }
                }catch(XmlException parseException){//NOPMD
                    //ignoring this exception
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.SHOULD_CHANGE_PASSWORD.toString())){
                if (asyncRequest){
                    onAsyncRequestException(fault);
                }else{
                    throw new PasswordExpiredError(fault);
                }
            } else if (fault.getFaultString().equals(ExceptionEnum.SUBOBJECTS_FOUND.toString())){
                if (asyncRequest){
                    onAsyncRequestException(fault);
                }else{
                    throw new CantDeleteSubobjectsFault(fault);
                }                
            } else if (fault.getFaultString().equals(ExceptionEnum.NO_CREDENTIALS.toString())){
                if (asyncRequest){
                    onAsyncRequestException(fault);
                }else{
                    throw new CredentialsWasNotDefinedError();
                }
            }
        }
        if (exception instanceof AllSapsBusyException){
            processAllSapsBusyException((AllSapsBusyException)exception, handler, asyncRequest);
            return;
        }else if (exception instanceof ServiceCallException) {
            final Throwable cause = exception.getCause();
            if (cause instanceof InterruptedException) {
                tracer.put(EEventSeverity.EVENT, mp.translate("TraceMessage", "Request was interrupted"), EEventSource.CLIENT_SESSION);
                if (asyncRequest) {
                    onAsyncRequestCancelled();                    
                    return;
                } else {
                    scheduleTestRequestImpl();
                    throw (InterruptedException) cause;
                }
            } else if (cause instanceof AllSapsBusyException) {
                processAllSapsBusyException((AllSapsBusyException)cause, handler, asyncRequest);
                return;                
            } else if (cause instanceof ServiceCallTimeout) {
                processServiceCallTimeout((ServiceCallTimeout) cause, asyncRequest, isTestRequest);
                return;
            }
            if (cause instanceof IOException && !tasks.isTestRequestSheduled() && !isTestRequest) {
                scheduleTestRequestImpl();
            }
            if ((cause instanceof ServiceCallException) && (cause.getCause() instanceof IOException) && !tasks.isTestRequestSheduled() && !isTestRequest) {
                scheduleTestRequestImpl();
            }
        }
        if (exception instanceof ServiceCallTimeout) {
            processServiceCallTimeout((ServiceCallTimeout) exception, asyncRequest, isTestRequest);
            return;
        }
        if (exception instanceof ServiceClientException) {
            if (asyncRequest) {
                onAsyncRequestException((ServiceClientException) exception);
                return;
            } else {
                throw (ServiceClientException) exception;
            }
        } else if (exception instanceof InterruptedException) {
            tracer.put(EEventSeverity.EVENT, mp.translate("TraceMessage", "Request was interrupted"), EEventSource.CLIENT_SESSION);
            if (asyncRequest) {
                onAsyncRequestCancelled();
                return;
            } else {
                scheduleTestRequestImpl();
                throw (InterruptedException) exception;
            }
        }
        final String msg = mp.translate("ClientSessionException", "Can't send request to server: %s");
        final String info = exception.getMessage() != null && !exception.getMessage().isEmpty() ? exception.getMessage() : exception.getClass().getName();
        if (asyncRequest) {
            onAsyncRequestException(new ServiceClientException(String.format(msg, info), exception));
        } else {
            throw new ServiceClientException(String.format(msg, info), exception);
        }
    }
    
    //synchronized by executorSemaphore
    private void processAllSapsBusyException(final AllSapsBusyException exception, 
                                                                   final RequestHandle handler,
                                                                   final boolean asyncRequest) throws AllSapsBusyException{
        easTrace.put(EEventSeverity.EVENT, exception.getDetailedMessage(mp), null, null, false);
        if (exception instanceof NoSapsForCurrentVersion){
            final NoSapsForCurrentVersion noSaps = (NoSapsForCurrentVersion)exception;
            if (isInDevelopmentMode || isCreateSessionRequest(handler)){
                if (tryToUpdateDefinitionVersion(handler.getRequest(), noSaps.getRequiredVersion()) && !asyncRequest){
                    return;//try again with new version
                }
            }else{
                if (!noSaps.canWaitForBusySaps()){
                    debug_message("No SAPS for current version");
                    failedSaps = noSaps.getFailedSapsInfo();
                    unsupportedVersion = environment.getDefManager().getAdsVersion().getNumber();
                    unsupportedVersionTime = System.currentTimeMillis();
                }
            }
        }
        if (asyncRequest) {
            onAsyncRequestException(exception);
        } else {
            throw exception;
        }        
    }

    @SuppressWarnings("unchecked")
    private void processFault(final ServiceCallFault e) {
        //Put into explorer trace all messages from EAS-CLIENT trace
        final AbstractTraceBuffer nativeBuffer = tracer.getBuffer();
        nativeBuffer.add(easTrace.getBuffer());
        if (e.getTrace() != null) {//Write server trace
            EEventSeverity severity;
            for (ServiceCallFault.TraceItem item : e.getTrace()) {
                severity = EEventSeverity.getForName(item.level);
                try {
                    final java.lang.Object parts[] = TraceItem.parseMess(XmlUtils.parseSafeXmlString(item.text));
                    if (tracer.getProfile().itemMatch(severity, (String)parts[3])) {
                        nativeBuffer.put(severity,parts);
                    }
                } catch (ParseException ex) {
                    final String errorMessage = mp.translate("ExplorerError", "Can't parse trace message \'%s\':\n%s");
                    final String itemText = String.format(errorMessage, item.text, ex.toString());
                    if (tracer.getProfile().itemMatch(EEventSeverity.ERROR,EEventSource.CLIENT.getValue())) {
                        nativeBuffer.put(EEventSeverity.ERROR,itemText,EEventSource.CLIENT.getValue());
                    }
                }
            }
        }
        //Write client error
        final String faultMessageTitle = mp.translate("ExplorerError", "Service call fault.");
        final String traceMessage = faultMessageTitle + "\ncode: \'" + e.getFaultCode() + "\'\nmessage: \'" + e.getFaultString() + "\'" + "\ndetail:\n" + e.getDetail().toString();
        final EEventSeverity severity = ClientException.getFaultSeverity(e);
        if (tracer.getProfile().itemMatch(severity,EEventSource.CLIENT.getValue())) {
            nativeBuffer.put(severity,traceMessage,EEventSource.CLIENT.getValue());
        }
        //now all messages in explorer trace and we can clear EAS-CLIENT trace
        easTrace.clear();
    }

    //synchronized by executorSemaphore
    private void processServiceCallTimeout(final ServiceCallTimeout exception, final boolean asyncRequest, final boolean isTestRequest) throws ServiceCallTimeout {
        if (asyncRequest) {
            onAsyncRequestException(exception);
            if (!isTestRequest) {
                scheduleTestRequestImpl();
            }
        } else {
            if (!isTestRequest) {
                scheduleTestRequestImpl();
            }
            throw exception;
        }
    }
    
    private boolean tryToUpdateDefinitionVersion(final XmlObject request, final long targetVersion) {
        final AdsVersion adsVersion = environment.getDefManager().getAdsVersion();
        if (adsVersion.getTargetVersionNumber()!=targetVersion){
            final long currentVersion = adsVersion.getNumber();
            try{
                adsVersion.prepareSwitchVersion(environment, targetVersion, isWeb || isInDevelopmentMode);
            }catch(CantUpdateVersionException exception){
                tracer.debug(mp.translate("ExplorerError", "Can't load runtime components"), exception);
                return false;
            }
            if (adsVersion.getTargetVersionNumber()<0){
                return targetVersion==-1 || adsVersion.getNumber()==targetVersion;
            }
        }
        
        if (request instanceof CreateSessionRq
            && canUseGui()
            && !isWeb
            && (environment.getTreeManager() == null || environment.getTreeManager().getCurrentTree() == null)) {
            final long currentVersion = adsVersion.getNumber();
            try {
                adsVersion.switchToTargetVersion();
            }catch(KernelClassModifiedException exception){
                return false;
            }catch (CantUpdateVersionException exception) {                
                tracer.debug(mp.translate("ExplorerError", "Can't load runtime components"), exception);
                return false;
            }
            if (currentVersion!=adsVersion.getNumber()){
                final CreateSessionRq createSession = (CreateSessionRq) request;
                final CreateSessionRq.Platform platform = createSession.getPlatform();
                platform.setDefinitionsVer(adsVersion.getNumber());
                platform.setAppVersion(RadixLoader.getInstance().getCurrentRevisionMeta().getAppLayerVersionsString());
                platform.setKernelVersion(RadixLoader.getInstance().getCurrentRevisionMeta().getKernelLayerVersionsString());
                return true;
            }
        }
        return false;
    }

    private boolean processInvalidDefinitionVersionFault(final String serverVersionAsStr, final XmlObject request) throws ServiceClientException, CantUpdateVersionException {
        final AdsVersion adsVersion = environment.getDefManager().getAdsVersion();
        long serverVersion = -1;
        try{
            serverVersion = Long.parseLong(serverVersionAsStr);
        }catch (NumberFormatException ex) {
            final String errorMessage = mp.translate("ExplorerError", "Can't parse server definitions version: %s");
            tracer.put(EEventSeverity.WARNING, String.format(errorMessage, serverVersionAsStr), EEventSource.CLIENT_SESSION); 
        }
        try {
            adsVersion.prepareSwitchVersion(environment, isInDevelopmentMode ? -1 : serverVersion, isWeb || isInDevelopmentMode);
            final long clientVersion = adsVersion.getTargetVersionNumber()>-1 ? adsVersion.getTargetVersionNumber() : adsVersion.getNumber();
            if (isInDevelopmentMode && serverVersion>-1 && serverVersion > clientVersion){
                final String clientVersionAsStr = String.valueOf(clientVersion);
                final String errorMessage = mp.translate("ExplorerError", "Can't load version %s");
                final String detailMessage = mp.translate("TraceMessage", "Server ask to load version with number %s, but only version with number %s available for client");
                tracer.put(EEventSeverity.ERROR, String.format(detailMessage, serverVersionAsStr, clientVersionAsStr), EEventSource.CLIENT_SESSION);
                throw new ServiceClientException(String.format(errorMessage, serverVersionAsStr));
            }
            if (!isInDevelopmentMode && serverVersion>-1 && serverVersion != clientVersion){
                final String clientVersionAsStr = String.valueOf(clientVersion);
                final String errorMessage = mp.translate("ExplorerError", "Can't load version %s");
                final String detailMessage = mp.translate("TraceMessage", "Server ask to load version with number %s, but this version is not available for client");
                tracer.put(EEventSeverity.ERROR, String.format(detailMessage, serverVersionAsStr, clientVersionAsStr), EEventSource.CLIENT_SESSION);
                throw new ServiceClientException(String.format(errorMessage, serverVersionAsStr));                
            }
        } catch (CantUpdateVersionException error) {
            tracer.error(mp.translate("ExplorerError", "Can't load runtime components"), error);
            if (request instanceof CreateSessionRq) {
                throw new RadixError(error.getTitle(mp), error);
            }
        } catch (NumberFormatException ex) {
            final String errorMessage = mp.translate("ExplorerError", "Can't parse server definitions version: %s");
            tracer.put(EEventSeverity.WARNING, String.format(errorMessage, serverVersionAsStr), EEventSource.CLIENT_SESSION); 
        }

        if (request instanceof CreateSessionRq && canUseGui()) {
            if ((environment.getTreeManager() == null || environment.getTreeManager().getCurrentTree() == null)) {
                //opening connection
                try {
                    adsVersion.switchToTargetVersion();
                }catch(KernelClassModifiedException exception){
                    throw exception;//to make PMD happy
                }catch (CantUpdateVersionException exception) {
                    tracer.error(mp.translate("ExplorerError", "Can't load runtime components"), exception);
                }
            }else{
                return false;
            }
            final CreateSessionRq createSession = (CreateSessionRq) request;
            final CreateSessionRq.Platform platform = createSession.getPlatform();
            platform.setDefinitionsVer(adsVersion.getNumber());
            platform.setAppVersion(RadixLoader.getInstance().getCurrentRevisionMeta().getAppLayerVersionsString());
            platform.setKernelVersion(RadixLoader.getInstance().getCurrentRevisionMeta().getKernelLayerVersionsString());
        }
        return true;
    }

    private XmlObject castToResultClass(final XmlObject response, final Class resultClass) throws ServiceClientException {
        final XmlObject result;
        try {								//try to reparse
            final Class<?> resultClassFactory = resultClass.getClassLoader().loadClass(resultClass.getName() + "$Factory");
            final Method parseMethod = resultClassFactory.getMethod("parse", new Class[]{org.w3c.dom.Node.class});
            result = (XmlObject) parseMethod.invoke(null, new java.lang.Object[]{response.getDomNode()});
        } catch (Exception e) {
            final String msg = mp.translate("ClientSessionException", "Can't parse response message: %s");
            final String info = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : e.getClass().getName();
            throw new ServiceClientException(String.format(msg, info), e);
        }
        if (!resultClass.isInstance(result)) {
            final String msg = mp.translate("ClientSessionException", "Invalid message class: expected \"%s\" got \"%s\"");
            throw new ServiceClientException(String.format(msg, resultClass.getName(), response.getClass().getName()));
        }
        return result;
    }
    
    private static boolean isInteractiveCallBackRequest(final XmlObject request){
        return request instanceof MessageDialogOpenMess
                   || request instanceof MessageDialogWaitButtonMess
                   || request instanceof FileSelectMess
                   || request instanceof ClientMethodInvocationRq;
    }

    public static XmlObject processCallBackRequest(final IClientEnvironment environment,
                                                   final XmlObject request,
                                                   final boolean isInteractive) throws TerminalResourceException, IOException, InterruptedException {
        final TerminalResources resources = TerminalResources.getInstance(environment);
        if (request instanceof MessageDialogOpenMess) {
            assertIsInteractive(isInteractive);            
            MessageDialogOpenRq callbackRq = ((MessageDialogOpenMess) request).getMessageDialogOpenRq();
            final String id = resources.openMessageDialogResource(environment, callbackRq);
            final MessageDialogOpenDocument document = MessageDialogOpenDocument.Factory.newInstance();
            document.addNewMessageDialogOpen().addNewMessageDialogOpenRs().setId(id);
            return document;
        } else if (request instanceof MessageDialogWaitButtonMess) {
            assertIsInteractive(isInteractive);
            MessageDialogWaitButtonRq callbackRq = ((MessageDialogWaitButtonMess) request).getMessageDialogWaitButtonRq();
            final IMessageDialogResource dlg = (IMessageDialogResource) resources.getResource(callbackRq.getId());
            final String buttonName = dlg.wait(callbackRq.getTimeout() * 1000);
            final MessageDialogWaitButtonDocument document = MessageDialogWaitButtonDocument.Factory.newInstance();
            final MessageDialogWaitButtonRs callbackRs = document.addNewMessageDialogWaitButton().addNewMessageDialogWaitButtonRs();
            if (buttonName != null) {
                callbackRs.setPressedButtonId(buttonName);
                resources.freeResource(environment, callbackRq.getId());
            }
            return document;
        } else if (request instanceof ProgressDialogStartProcessMess) {
            assertIsInteractive(isInteractive);
            final ProgressDialogStartProcessRq callBackRq = ((ProgressDialogStartProcessMess) request).getProgressDialogStartProcessRq();
            final String processId = resources.startProgressDialogProcess(environment, callBackRq);
            final ProgressDialogStartProcessDocument document = ProgressDialogStartProcessDocument.Factory.newInstance();
            document.addNewProgressDialogStartProcess().addNewProgressDialogStartProcessRs().setId(processId);
            return document;
        } else if (request instanceof ProgressDialogSetMess) {
            assertIsInteractive(isInteractive);
            final ProgressDialogSetRq callBackRq = ((ProgressDialogSetMess) request).getProgressDialogSetRq();
            final boolean wasCancelled = !resources.updateProgressDialog(environment, callBackRq);
            final ProgressDialogSetDocument document = ProgressDialogSetDocument.Factory.newInstance();
            document.addNewProgressDialogSet().addNewProgressDialogSetRs().setCancelled(wasCancelled);
            return document;
        } else if (request instanceof ProgressDialogTraceMess) {
            assertIsInteractive(isInteractive);
            final ProgressDialogTraceRq callBackRq = ((ProgressDialogTraceMess) request).getProgressDialogTraceRq();
            final boolean wasCancelled = !resources.addTraceInProgressDilog(environment, callBackRq);
            final ProgressDialogTraceDocument document = ProgressDialogTraceDocument.Factory.newInstance();
            document.addNewProgressDialogTrace().addNewProgressDialogTraceRs().setCancelled(wasCancelled);
            return document;
        } else if (request instanceof ProgressDialogFinishProcessMess) {
            assertIsInteractive(isInteractive);
            final ProgressDialogFinishProcessDocument document = ProgressDialogFinishProcessDocument.Factory.newInstance();
            document.addNewProgressDialogFinishProcess().addNewProgressDialogFinishProcessRs();
            resources.finishProgressDialogProcess(environment);
            return document;
        } else if (request instanceof FileSelectMess) {
            assertIsInteractive(isInteractive);
            final FileSelectRq callbackRq = ((FileSelectMess) request).getFileSelectRq();
            final FileSelectDocument document = FileSelectDocument.Factory.newInstance();
            final FileSelectRs callbackRs = document.addNewFileSelect().addNewFileSelectRs();
            final String fileName = environment.getResourceManager().selectFile(callbackRq);
            if (fileName != null) {
                callbackRs.setFileName(fileName);
            }
            return document;
        } else if (request instanceof FileAccessMess) {
            final FileAccessRq callbackRq = ((FileAccessMess) request).getFileAccessRq();
            final FileAccessDocument document = FileAccessDocument.Factory.newInstance();
            final FileAccessRs callbackRs = document.addNewFileAccess().addNewFileAccessRs();
            callbackRs.setOK(environment.getResourceManager().checkFileAccess(callbackRq.getFileName(), callbackRq.getAccess()));
            return document;
        } else if (request instanceof FileSizeMess) {
            final FileSizeRq callbackRq = ((FileSizeMess) request).getFileSizeRq();
            final FileSizeDocument document = FileSizeDocument.Factory.newInstance();
            final FileSizeRs callbackRs = document.addNewFileSize().addNewFileSizeRs();
            callbackRs.setSize(environment.getResourceManager().getFileSize(callbackRq.getFileName()));
            return document;
        } else if (request instanceof FileOpenMess) {
            final FileOpenRq callbackRq = ((FileOpenMess) request).getFileOpenRq();
            final FileOpenDocument document = FileOpenDocument.Factory.newInstance();
            final FileOpenRs callbackRs = document.addNewFileOpen().addNewFileOpenRs();
            callbackRs.setId(resources.openFileResource(environment, callbackRq));
            return document;
        } else if (request instanceof FileTransitMess) {
            final FileTransitRq callbackRq = ((FileTransitMess) request).getFileTransitRq();
            final FileTransitDocument document = FileTransitDocument.Factory.newInstance();
            final FileTransitRs callbackRs = document.addNewFileTransit().addNewFileTransitRs();
            callbackRs.setId(resources.startFileTransit(environment, callbackRq));
            return document;
        } else if (request instanceof FileCloseMess) {
            final FileCloseRq callbackRq = ((FileCloseMess) request).getFileCloseRq();
            final FileCloseDocument document = FileCloseDocument.Factory.newInstance();
            document.addNewFileClose().addNewFileCloseRs();
            resources.freeResource(environment, callbackRq.getId());
            return document;
        } else if (request instanceof FileReadMess) {
            final FileReadRq callbackRq = ((FileReadMess) request).getFileReadRq();
            final FileReadDocument document = FileReadDocument.Factory.newInstance();
            final FileReadRs callbackRs = document.addNewFileRead().addNewFileReadRs();
            final IFileResource file = (IFileResource) resources.getResource(callbackRq.getId());
            int length = (int) callbackRq.getLen();
            byte[] buf = new byte[length];
            int len = file.read(buf);
            if (len > 0) {
                callbackRs.setByteArrayValue(Arrays.copyOf(buf, len));
            }
            if (file.isEof()) {
                callbackRs.setEOF(true);
            }
            return document;
        } else if (request instanceof FileWriteMess) {
            final FileWriteRq callbackRq = ((FileWriteMess) request).getFileWriteRq();
            final FileWriteDocument document = FileWriteDocument.Factory.newInstance();
            document.addNewFileWrite().addNewFileWriteRs();
            final IFileResource file = (IFileResource) resources.getResource(callbackRq.getId());
            file.write(callbackRq.getByteArrayValue());
            return document;
        } else if (request instanceof FileSeekMess) {
            final FileSeekRq callbackRq = ((FileSeekMess) request).getFileSeekRq();
            final FileSeekDocument document = FileSeekDocument.Factory.newInstance();
            final FileSeekRs callbackRs = document.addNewFileSeek().addNewFileSeekRs();
            final IFileResource file = (IFileResource) resources.getResource(callbackRq.getId());
            final EFileSeekOriginType originType = EFileSeekOriginType.getForValue(callbackRq.getOrigin().toString());
            callbackRs.setPos(file.seek(callbackRq.getOffset(), originType));
            return document;
        } else if (request instanceof FileDeleteMess) {
            final FileDeleteRq callbackRq = ((FileDeleteMess) request).getFileDeleteRq();
            final FileDeleteDocument document = FileDeleteDocument.Factory.newInstance();
            document.addNewFileDelete().addNewFileDeleteRs();
            environment.getResourceManager().deleteFile(callbackRq.getFileName());
            return document;
        } else if (request instanceof FileMoveMess) {
            final FileMoveRq callbackRq = ((FileMoveMess) request).getFileMoveRq();
            final FileMoveDocument document = FileMoveDocument.Factory.newInstance();
            document.addNewFileMove().addNewFileMoveRs();
            environment.getResourceManager().moveFile(callbackRq.getFileName(), callbackRq.getTargetFileName(), callbackRq.getOverwrite());
            return document;
        } else if (request instanceof FileCopyMess) {
            final FileCopyRq callbackRq = ((FileCopyMess) request).getFileCopyRq();
            final FileCopyDocument document = FileCopyDocument.Factory.newInstance();
            document.addNewFileCopy().addNewFileCopyRs();
            environment.getResourceManager().copyFile(callbackRq.getFileName(), callbackRq.getTargetFileName(), callbackRq.getOverwrite());
            return document;
        } else if (request instanceof FileDirSelectMess) {
            assertIsInteractive(isInteractive);
            final FileDirSelectRq callbackRq = ((FileDirSelectMess) request).getFileDirSelectRq();
            final FileDirSelectDocument document = FileDirSelectDocument.Factory.newInstance();
            final FileDirSelectRs callbackRs = document.addNewFileDirSelect().addNewFileDirSelectRs();
            final String selectedDir =
                    resources.getFileDirResource(environment).select(environment, callbackRq.getTitle(), callbackRq.getDirName());
            if (selectedDir != null && !selectedDir.isEmpty()) {
                callbackRs.setDirName(selectedDir);
            }
            return document;
        } else if (request instanceof FileDirReadMess) {
            final FileDirReadRq callbackRq = ((FileDirReadMess) request).getFileDirReadRq();
            final List<FileDirReadItem> entries =
                    resources.getFileDirResource(environment).read(callbackRq.getDirName(),
                    callbackRq.getAttributeFilter(),
                    callbackRq.getMask(),
                    callbackRq.getProperties(),
                    callbackRq.getSortBy(),
                    callbackRq.getSortOptions());
            final FileDirReadDocument document = FileDirReadDocument.Factory.newInstance();
            final FileDirReadRs callbackRs = document.addNewFileDirRead().addNewFileDirReadRs();
            if (!entries.isEmpty()) {
                final FileDirReadItem[] items = new FileDirReadItem[entries.size()];
                entries.toArray(items);
                callbackRs.setItemArray(items);
            }
            return document;
        } else if (request instanceof FileDirCreateMess) {
            final FileDirCreateRq callbackRq = ((FileDirCreateMess) request).getFileDirCreateRq();
            resources.getFileDirResource(environment).create(callbackRq.getDirName());
            final FileDirCreateDocument document = FileDirCreateDocument.Factory.newInstance();
            document.addNewFileDirCreate().addNewFileDirCreateRs();
            return document;
        } else if (request instanceof FileDirDeleteMess) {
            final FileDirDeleteRq callbackRq = ((FileDirDeleteMess) request).getFileDirDeleteRq();
            resources.getFileDirResource(environment).delete(callbackRq.getDirName());
            final FileDirDeleteDocument document = FileDirDeleteDocument.Factory.newInstance();
            document.addNewFileDirDelete().addNewFileDirDeleteRs();
            return document;
        } else if (request instanceof FileDirMoveMess) {
            final FileDirMoveRq callbackRq = ((FileDirMoveMess) request).getFileDirMoveRq();
            resources.getFileDirResource(environment).move(callbackRq.getDirName(), callbackRq.getNewName());
            final FileDirMoveDocument document = FileDirMoveDocument.Factory.newInstance();
            document.addNewFileDirMove().addNewFileDirMoveRs();
            return document;
        } else if (request instanceof FileDirGetUserHomeMess){
            final FileDirGetUserHomeDocument document = FileDirGetUserHomeDocument.Factory.newInstance();
            final String userHomeDirPath = resources.getFileDirResource(environment).getUserHomeDirPath();
            document.addNewFileDirGetUserHome().addNewFileDirGetUserHomeRs().setDirName(userHomeDirPath);
            return document;
        } else if (request instanceof GetUserDownloadsDirMess){
            final GetUserDownloadsDirDocument document = GetUserDownloadsDirDocument.Factory.newInstance();
            final String downloadsDirPath = resources.getFileDirResource(environment).getUserDownloadsDirPath();
            document.addNewGetUserDownloadsDir().addNewGetUserDownloadsDirRs().setDirName(downloadsDirPath);
            return document;
        } else if (request instanceof TestIfFileExistsMess){
           final String filePath = ((TestIfFileExistsMess)request).getTestIfFileExistsRq().getFilePath();
           final TestIfFileExistsDocument document =  TestIfFileExistsDocument.Factory.newInstance();
           final boolean isExists = environment.getResourceManager().isFileExists(filePath);
           document.addNewTestIfFileExists().addNewTestIfFileExistsRs().setIsExists(isExists);
           return document;
        } else if (request instanceof TestIfDirExistsMess){
            final String dirPath = ((TestIfDirExistsMess)request).getTestIfDirExistsRq().getDirPath();
            final TestIfDirExistsDocument document = TestIfDirExistsDocument.Factory.newInstance();
            final boolean isExists = resources.getFileDirResource(environment).isExists(dirPath);
            document.addNewTestIfDirExists().addNewTestIfDirExistsRs().setIsExists(isExists);
            return document;
        } else if (request instanceof ClientMethodInvocationMess) {
            final ClientMethodInvocationRq callbackRq = ((ClientMethodInvocationMess) request).getClientMethodInvocationRq();
            try {
                final ClientMethodInvocationRs callbackRs = resources.getRpcServer(environment).processRequest(callbackRq);
                final ClientMethodInvocationDocument document = ClientMethodInvocationDocument.Factory.newInstance();
                document.addNewClientMethodInvocation().setClientMethodInvocationRs(callbackRs);
                return document;
            } catch (Exception exception) {
                throw new TerminalResourceException("RPC request process exception", exception);
            }
        }
        throw new TerminalResourceException("resource request \"" + request.getDomNode().getLocalName() + "\" is not supported");
    }
    
    private static void assertIsInteractive(final boolean isInteractive) throws TerminalResourceException{
        if (!isInteractive){
            throw new TerminalResourceException("Unable to process this callback request in background mode");
        }
    }   

    public void close() {
        debug_message("closing executor");
        try{
            executor.close();
            connection.close();
        }finally{
            if (localEventLoop.isInProgress()) {
                debug_message("Stop event loop");
                localEventLoop.stop();
            }
        }
    }

    private void debug_message(final String message) {
        if (isInDevelopmentMode) {
            synchronized (executorSemaphore) {
                if (debug_msg.size() == 50) {
                    debug_msg.remove(0);
                }
                debug_msg.add(ClientException.exceptionStackToString(new AppError(message)));
            }
        }
    }        
    
    private String getCurrentScpName(){
        synchronized(executorSemaphore){
            return currentScpName;
        }
    }

    //synchronized by executorSemaphore
    private boolean isVersionSupported(){
        return failedSaps==null 
                  || unsupportedVersion<0 
                  || unsupportedVersion!=environment.getDefManager().getAdsVersion().getNumber()
                  || (System.currentTimeMillis() - unsupportedVersionTime)>UNSUPPORTED_VERSION_TIMEOUT_MSEC;
    }
}
