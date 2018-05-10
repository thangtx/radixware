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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.ReadMess;


public class RequestHandle {
    
    public final static class Factory {

        private Factory() {
        }

        public static CommandRequestHandle createForSendContextlessCommand(final IClientEnvironment environment, final Id commandId,
                final XmlObject input,
                final Class<? extends XmlObject> outputClass,
                final FormModel form) {
            final XmlObject request = RequestCreator.contextlessCommand(commandId, input, form);
            return new CommandRequestHandle(environment, request, commandId, null, outputClass);
        }

        public static CommandRequestHandle createForSendContextlessCommand(final IClientEnvironment environment, final Id commandId,
                final XmlObject input,
                final Class<? extends XmlObject> outputClass) {
            final XmlObject request = RequestCreator.contextlessCommand(commandId, input, null);
            return new CommandRequestHandle(environment, request, commandId, null, outputClass);
        }

        public static CommandRequestHandle createForSendContextCommand(final IClientEnvironment environment, final Id commandId,
                final Id propertyId,
                final Model contextModel,
                final XmlObject input,
                final Class<? extends XmlObject> outputClass,
                final FormModel form) {
            final XmlObject request = RequestCreator.command(commandId, propertyId, input, contextModel, form);
            return new CommandRequestHandle(environment, request, commandId, propertyId, outputClass);
        }

        public static CommandRequestHandle createForSendContextCommand(final IClientEnvironment environment, final Id commandId,
                final Id propertyId,
                final Model contextModel,
                final XmlObject input,
                final Class<? extends XmlObject> outputClass) {
            final XmlObject request = RequestCreator.command(commandId, propertyId, input, contextModel, null);
            return new CommandRequestHandle(environment, request, commandId, propertyId, outputClass);
        }

        public static RequestHandle createForReadEntityObject(final IClientEnvironment environment, final Pid pid,
                final Id classId,
                final Collection<Id> presentationIds,
                final IContext.Entity entityContext) {
            final XmlObject request = RequestCreator.read(pid, classId, presentationIds, null, entityContext, true);
            return new RequestHandle(environment, request, ReadMess.class);
        }

        public static RequestHandle createForReadEntityProperties(final IClientEnvironment environment, final Pid pid,
                final Id classId,
                final Collection<Id> presentationIds,
                final Collection<Id> propertyIds,
                final IContext.Entity entityContext) {
            final XmlObject request = RequestCreator.read(pid, classId, presentationIds, propertyIds, entityContext, false);
            return new RequestHandle(environment, request, ReadMess.class);
        }                

        public static SelectRequestHandle createForSelect(final GroupModel group, final int startIndex, final int rowCount, final boolean withSelectorAddons) {
            return new SelectRequestHandle(group, startIndex, rowCount, withSelectorAddons);
        }
        
        public static GetObjectTitlesRequestHandle createForGetObjectTitles(final IClientEnvironment environment,
                                                                            final Id tableId, 
                                                                            final Id defaultPresentationId, 
                                                                            final GetObjectTitlesRq.Objects objects
                                                                            ) {
            final GetObjectTitlesRq request = 
                RequestCreator.getObjectTitles(tableId, defaultPresentationId, objects, environment.getDefManager());
            return new GetObjectTitlesRequestHandle(environment, request);
        }        
        
        public static GetObjectTitlesRequestHandle createForGetObjectTitles(final IClientEnvironment environment,
                                                                            final Id tableId, 
                                                                            final IContext.Abstract context, 
                                                                            final GetObjectTitlesRq.Objects objects
                                                                            ) {
            final GetObjectTitlesRq request = 
                RequestCreator.getObjectTitles(tableId, context, objects, environment.getDefManager());
            return new GetObjectTitlesRequestHandle(environment, request);
        }               
    }       
    
    private abstract class AbstractNotificationTask implements Runnable{                
        
        private final IResponseListener listener;
        
        public AbstractNotificationTask(final IResponseListener listener){
            this.listener = listener;
        }
        
        @Override
        public final void run() {
            rListenersLock.lock();
            try{
                if (!listeners.contains(listener)){
                    return;
                }
            }finally{
                rListenersLock.unlock();
            }
            try{
                notifyListener(listener);
            }catch (Throwable ex) {
                final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
                final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
                environment.getTracer().error(String.format(message, exceptionStr));
            }
            finally{
                removeListener(listener);
            }
        }
        
        protected abstract void notifyListener(final IResponseListener listener);
    }
    
    private final class NotifyExceptionTask extends AbstractNotificationTask{
        
        private final ServiceClientException exception;
        
        public NotifyExceptionTask(final IResponseListener listener, final ServiceClientException exception){
            super(listener);
            this.exception = exception;
        }        

        @Override
        protected void notifyListener(final IResponseListener listener) {
            listener.onServiceClientException(exception, RequestHandle.this);
        }    
    }
    
    private final class NotifyResponceReceivedTask extends AbstractNotificationTask{
        
        private final XmlObject response;
        
        public NotifyResponceReceivedTask(final IResponseListener listener, final XmlObject response){
            super(listener);
            this.response = response;
        }

        @Override
        protected void notifyListener(final IResponseListener listener) {
            listener.onResponseReceived(response, RequestHandle.this);
        }
    }
    
    private final class NotifyRequestCancelledTask extends AbstractNotificationTask{
        
        private final XmlObject request;
        
        public NotifyRequestCancelledTask(final IResponseListener listener, final XmlObject request){
            super(listener);
            this.request = request;
        }

        @Override
        protected void notifyListener(final IResponseListener listener) {
            listener.onRequestCancelled(request, RequestHandle.this);
        }
    }    
    
    private static interface INotificationTaskFactory{
        AbstractNotificationTask newNotificationTask(final IResponseListener listener);
    }    
    
    private final class NotifyExceptionTaskFactory implements INotificationTaskFactory{
        
        private final ServiceClientException exception;
        
        public NotifyExceptionTaskFactory(final ServiceClientException exception){
            this.exception = exception;
        }

        @Override
        public NotifyExceptionTask newNotificationTask(final IResponseListener listener) {
            return new NotifyExceptionTask(listener, this.exception);
        }                
    }
    
    private final class NotifyResponceReceivedTaskFactory implements INotificationTaskFactory{
        
        private final XmlObject response;
        
        public NotifyResponceReceivedTaskFactory(final XmlObject response){
            this.response = response;
        }

        @Override
        public NotifyResponceReceivedTask newNotificationTask(final IResponseListener listener) {
            return new NotifyResponceReceivedTask(listener, this.response);
        }        
    }
    
    private final class NotifyRequestCancelledTaskFactory implements INotificationTaskFactory{
        
        private final XmlObject request;
        
        public NotifyRequestCancelledTaskFactory(final XmlObject request){
            this.request = request;                    
        }
        
        @Override
        public NotifyRequestCancelledTask newNotificationTask(final IResponseListener listener) {
            return new NotifyRequestCancelledTask(listener, this.request);
        }                
    }

    private final XmlObject request;
    private final Class<? extends XmlObject> expectedResponseMessageClass;
    private final ReentrantReadWriteLock rwStateLock = new ReentrantReadWriteLock();
    private final Lock rStateLock = rwStateLock.readLock();
    private final Lock wStateLock = rwStateLock.writeLock();
    private final ReentrantReadWriteLock rwListenersLock = new ReentrantReadWriteLock();
    private final Lock rListenersLock = rwListenersLock.readLock();
    private final Lock wListenersLock = rwListenersLock.writeLock();
    private XmlObject response;
    private ServiceClientException exception;
    private boolean isActive;
    private boolean wasCancelled;
    private RequestExecutor executor;
    private final List<IResponseListener> listeners = new ArrayList<>();
    private java.lang.Object userData;
    protected final IClientEnvironment environment;

    protected RequestHandle(final IClientEnvironment environment, final XmlObject request, final Class<? extends XmlObject> responseClass) {
        this.request = request;
        this.environment = environment;
        expectedResponseMessageClass = responseClass;
    }

    public final XmlObject getRequest() {
        return request;
    }

    public final Class<? extends XmlObject> getExpectedResponseMessageClass() {
        return expectedResponseMessageClass;
    }

    public final boolean isActive() {
        rStateLock.lock();
        try{        
            return isActive;
        }finally{
            rStateLock.unlock();
        }
    }

    final void activate() {
        wStateLock.lock();
        try{
            isActive = true;
            executor = null;
            response = null;
            exception = null;
            wasCancelled = false;
        }finally{
            wStateLock.unlock();
        }
    }

    public final boolean inProgress() {
        rStateLock.lock();
        try{
            return executor != null;
        }finally{
            rStateLock.unlock();
        }
    }

    final boolean beforeExecute(final RequestExecutor executor) {
        if (!hasListeners()){
            return false;
        }
        wStateLock.lock();
        try{
            if (isActive){
                this.executor = executor;                
                return true;
            }else{
                return false;
            }
        }finally{
            wStateLock.unlock();
        }
    }

    public final boolean wasCancelled() {
        rStateLock.lock();
        try{
            return wasCancelled;
        }finally{
            rStateLock.unlock();
        }
    }

    void onInterrupted() {
        wStateLock.lock();
        try{
            executor = null;
            isActive = false;
            wasCancelled = true;
        }finally{
            wStateLock.unlock();
        }
    }

    public final boolean cancel() {
        if (!inProgress() && !isDone()) {
            wStateLock.lock();
            try{
                isActive = false;
                wasCancelled = true;
            }finally{
                wStateLock.unlock();
            }
            notifyListeners();
            return true;
        } else {
            if (inProgress()) {
                executor.breakRequest();
                wStateLock.lock();
                try{
                    executor = null;
                    isActive = false;
                    wasCancelled = true;
                }finally{
                    wStateLock.unlock();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public final boolean isDone() {
        rStateLock.lock();
        try{
            return response != null || exception != null;
        }finally{
            rStateLock.unlock();
        }
    }

    void setResponse(final XmlObject response) {
        wStateLock.lock();
        try{
            this.response = response;
            isActive = false;
            executor = null;
            wasCancelled = false;
        }finally{
            wStateLock.unlock();
        }
    }

    void setException(final ServiceClientException exception) {
        wStateLock.lock();
        try{
            this.exception = exception;
            isActive = false;
            executor = null;
            wasCancelled = false;
        }finally{
            wStateLock.unlock();
        }
    }

    public final XmlObject getResponse() throws ServiceClientException, InterruptedException {
        rStateLock.lock();
        try{
            if (!isDone()) {
                throw new IllegalStateException("Response is not received yet");
            } else if (wasCancelled) {
                throw new InterruptedException();
            } else if (exception != null) {
                throw exception;
            } else {
                return response;
            }
        }finally{
            rStateLock.unlock();
        }
    }

    public final ServiceClientException getServiceClientException() {
        rStateLock.lock();
        try{
            if (!isDone()) {
                throw new IllegalStateException("Response is not received yet");
            } else {
                return exception;
            }
        }finally{
            rStateLock.unlock();
        }
    }

    public void setUserData(java.lang.Object usrData) {
        wStateLock.lock();
        try{
            userData = usrData;
        }finally{
            wStateLock.unlock();
        }
    }

    public java.lang.Object getUserData() {
        rStateLock.lock();
        try{
            return userData;
        }finally{
            rStateLock.unlock();
        }
    }

    void processAnswer() {
        notifyListeners();
    }

    private void notifyListeners() {
        final List<IResponseListener> copyListeners = new LinkedList<>();
        rListenersLock.lock();
        try{
            copyListeners.addAll(listeners);
        }finally{
            rListenersLock.unlock();
        }
        if (!copyListeners.isEmpty()){
            final INotificationTaskFactory taskFactory = createNotificationTaskFactory();
            final IEasSession session = environment.getEasSession();
            for (IResponseListener listener: copyListeners){
                session.scheduleResponseNotificationTask(taskFactory.newNotificationTask(listener));
            }
        }
    }
    
    private final INotificationTaskFactory createNotificationTaskFactory(){
        rStateLock.lock();
        try{
            if (wasCancelled){
                return new NotifyRequestCancelledTaskFactory(request);
            }else if (exception!=null){
                return new NotifyExceptionTaskFactory(exception);
            }else{
                return new NotifyResponceReceivedTaskFactory(response);
            }
        }finally{
            rStateLock.unlock();
        }        
    }

    public final void addListener(final IResponseListener listener) {
        if (listener!=null){
            rListenersLock.lock();
            try{
                if (listeners.contains(listener)){
                    return;
                }
            }finally{
                rListenersLock.unlock();
            }
            final boolean newRegistration;
            final boolean isDone;
            wListenersLock.lock();
            try{
                isDone  = isDone();
                if (listeners.contains(listener)){
                    newRegistration = false;                    
                }else{
                    newRegistration = true;
                    listeners.add(listener);
                }
            }finally{
                wListenersLock.unlock();
            }
            if (newRegistration){
                listener.registerRequestHandle(this);
                if (isDone){
                    final INotificationTaskFactory taskFactory = createNotificationTaskFactory();
                    environment.getEasSession().scheduleResponseNotificationTask(taskFactory.newNotificationTask(listener));
                }
            }
        }
    }

    public final void removeListener(final IResponseListener listener) {
        if (listener!=null){
            rListenersLock.lock();
            try{
                if (!listeners.contains(listener)){
                    return;
                }
            }finally{
                rListenersLock.unlock();
            }
            final boolean unregistered;
            wListenersLock.lock();
            try{
                unregistered = listeners.remove(listener);
            }finally{
                wListenersLock.unlock();
            }
            if (unregistered){
                listener.unregisterRequestHandle(this);
                rListenersLock.lock();
                try{
                    if (!hasListeners() && inProgress()){
                        cancel();
                    }
                }finally{
                    rListenersLock.unlock();
                }
            }            
        }
    }

    public boolean hasListeners() {
        rListenersLock.lock();
        try{
            return !listeners.isEmpty();
        }finally{
            rListenersLock.unlock();
        }
    }
}
