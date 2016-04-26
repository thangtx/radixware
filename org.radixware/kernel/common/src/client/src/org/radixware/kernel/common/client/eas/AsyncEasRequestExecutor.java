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

import java.util.List;
import java.util.LinkedList;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.MaxNumberOfSessionsExceededError;
import org.radixware.kernel.common.client.errors.PasswordExpiredError;
import org.radixware.kernel.common.client.errors.SessionDoesNotExistError;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class AsyncEasRequestExecutor implements AutoCloseable{
    
    private static final int INFINITE_RESPONSE_TIMEOUT = 0;
    
    private final class ResponseListener implements IResponseListener{
        
        private final IEasSession session;
        
        public ResponseListener(final IEasSession activeSession){
            session = activeSession;
        }

        @Override
        public void registerRequestHandle(RequestHandle handle) {
        }

        @Override
        public void unregisterRequestHandle(RequestHandle handle) {
        }

        @Override
        public void onResponseReceived(XmlObject response, RequestHandle handle) {
            onRequestFinished(session);
        }

        @Override
        public void onServiceClientException(ServiceClientException exception, RequestHandle handle) {
            onRequestFinished(session);
        }

        @Override
        public void onRequestCancelled(XmlObject request, RequestHandle handler) {
            onRequestFinished(session);
        }        
    }
    
    private final static class ScheduledTask{
        
        private final RequestHandle requestHandle;
        private final int timeout;
        
        public ScheduledTask(final RequestHandle rqHandle, final int timeout){
            requestHandle = rqHandle;
            this.timeout = timeout;
        }

        public RequestHandle getRequestHandle() {
            return requestHandle;
        }

        public int getTimeout() {
            return timeout;
        }
    }
    
    private final IClientEnvironment environment;
    private final int sessionsLimit;
    private final List<IEasSession> openedSessions = new LinkedList<>();
    private final List<IEasSession> freeSessions = new LinkedList<>();
    private final List<ScheduledTask> scheduledTasks = new LinkedList<>();    
    private volatile boolean closed;
    private volatile boolean closeOnFinish;
    private volatile boolean canCreateNewSession = true;
    private final Object semaphore = new Object();
    
    public AsyncEasRequestExecutor(final IClientEnvironment environment, final int maxEasSessionsCount){
        this.environment = environment;
        this.sessionsLimit = maxEasSessionsCount;
    }
        
    public void sendAsync(final CommandRequestHandle handle) {
        sendAsync((RequestHandle) handle, INFINITE_RESPONSE_TIMEOUT);
    }
    
    public void sendAsync(final CommandRequestHandle handle, final int timeoutSec) {
        sendAsync((RequestHandle) handle, timeoutSec < 0 ? INFINITE_RESPONSE_TIMEOUT : timeoutSec);
    }

    public void sendAsync(final RequestHandle handle) {
        sendAsync(handle, INFINITE_RESPONSE_TIMEOUT);
    }
    
    public void sendAsync(final RequestHandle handle, final int timeoutSec) {
        final IEasSession freeSession;
        try{
            freeSession = getFreeSession();
        }catch(InterruptedException exception){
            handle.onInterrupted();
            handle.processAnswer();
            return;
        }catch(ServiceClientException exception){
            handle.setException(exception);
            handle.processAnswer();
            return;
        }catch(SessionDoesNotExistError | MaxNumberOfSessionsExceededError | PasswordExpiredError error){
            //this errors cause dialog when opening session
            synchronized(semaphore){
                if (openedSessions.size()>0){
                    canCreateNewSession = false;
                    scheduledTasks.add(new ScheduledTask(handle,timeoutSec));
                    return;
                }
            }
            if (error.getSouceFault()==null){                
                throw error;
            }
            handle.setException(error.getSouceFault());
            handle.processAnswer();
            throw error;
        }catch(EasError error){
            if (error.getSouceFault()==null){
                throw error;
            }
            handle.setException(error.getSouceFault());
            handle.processAnswer();
            return;
        }
        if (freeSession==null){
            synchronized(semaphore){
                if (closed){
                    throw new IllegalStateException("AsyncEasRequestExecutor was closed");
                }else{
                    scheduledTasks.add(new ScheduledTask(handle,timeoutSec));
                }
            }
        }else{
            doSendRequest(freeSession,handle,timeoutSec);            
        }
    }

    @Override
    public void close(){
        synchronized(semaphore){
            doClose();
        }
    }
    
    private void doClose(){
        for (ScheduledTask task: scheduledTasks){
            task.getRequestHandle().cancel();
        }
        scheduledTasks.clear();
        for (IEasSession session: openedSessions){
            session.close(false);
        }
        openedSessions.clear();
        closed = true;        
    }
    
    public void closeWhenLastRequestFinished(){
        synchronized(semaphore){
            closeOnFinish = true;
        }
    }
    
    public final int getFressSessionsCount(){
        synchronized(semaphore){
            return freeSessions.size();
        }
    }
    
    public final int getOpenedSessionsCount(){
        synchronized(semaphore){
            return openedSessions.size();
        }
    }
    
    public final int getScheduledTasksCount(){
        synchronized(semaphore){
            return scheduledTasks.size();
        }
    }
    
    private IEasSession getFreeSession() throws ServiceClientException, InterruptedException{
        synchronized(semaphore){
            if (closed || !canCreateNewSession){
                return null;
            }
            if (!freeSessions.isEmpty()){
                return freeSessions.remove(freeSessions.size()-1);
            }            
            if (sessionsLimit<=0 || openedSessions.size()<sessionsLimit){
                final IEasSession newSession = environment.getEasSession().createBackgroundSession();
                if (newSession instanceof AbstractEasSession){                
                    ((AbstractEasSession)newSession).open();
                }
                openedSessions.add(newSession);
                return newSession;
            }
        }
        return null;
    }
    
    private void onRequestFinished(final IEasSession session){
        synchronized(semaphore){
            if (closed){
                return;
            }
            if (!scheduledTasks.isEmpty()){
                final ScheduledTask task = scheduledTasks.remove(0);
                doSendRequest(session, task.getRequestHandle(), task.getTimeout());
            }else{
                freeSessions.add(session);
                if (closeOnFinish && freeSessions.size()==openedSessions.size()){
                    doClose();
                }
            }
        }
    }
    
    private void doSendRequest(final IEasSession session, final RequestHandle handle, final int timeout){
        handle.addListener(new ResponseListener(session));
        session.sendAsync(handle, timeout);
    }
}
