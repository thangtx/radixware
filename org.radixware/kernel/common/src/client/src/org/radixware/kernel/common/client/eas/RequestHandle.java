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
import java.util.List;
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

    private final XmlObject request;
    private final Class<? extends XmlObject> expectedResponseMessageClass;
    private XmlObject response;
    private ServiceClientException exception;
    private boolean isActive;
    private boolean wasCancelled;
    private RequestExecutor executor;
    private final List<IResponseListener> listeners = new ArrayList<IResponseListener>();
    private java.lang.Object userData;
    protected final IClientEnvironment environment;

    protected RequestHandle(final IClientEnvironment environment, final XmlObject request, final Class<? extends XmlObject> responseClass) {
        this.request = request;
        this.environment = environment;
        expectedResponseMessageClass = responseClass;
    }

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

    public final XmlObject getRequest() {
        return request;
    }

    public final Class<? extends XmlObject> getExpectedResponseMessageClass() {
        return expectedResponseMessageClass;
    }

    public final boolean isActive() {
        return isActive;
    }

    final void activate() {
        isActive = true;
        executor = null;
        response = null;
        exception = null;
        wasCancelled = false;
//        for (IResponseListener listener : listeners) {
//            listener.registerRequestHandle(this);
//        }
    }

    public final boolean inProgress() {
        return executor != null;
    }

    void onExecuting(final RequestExecutor executor) {
        this.executor = executor;
    }

    public final boolean wasCancelled() {
        return wasCancelled;
    }

    void onInterrupted() {
        executor = null;
        isActive = false;
        wasCancelled = true;
    }

    public final boolean cancel() {
        if (!inProgress() && !isDone()) {
            isActive = false;
            wasCancelled = true;
            notifyListeners();

            return true;
        } else {
            if (executor != null) {
                executor.breakRequest();
                executor = null;
                isActive = false;
                wasCancelled = true;
                return true;
            } else {
//                wasCancelled = true;
                return false;
            }
        }
    }

    public final boolean isDone() {
        return response != null || exception != null;
    }

    void setResponse(final XmlObject response) {
        this.response = response;
        isActive = false;
        executor = null;
        wasCancelled = false;
    }

    void setException(final ServiceClientException exception) {
        this.exception = exception;
        isActive = false;
        executor = null;
        wasCancelled = false;
    }

    public final XmlObject getResponse() throws ServiceClientException, InterruptedException {
        if (!isDone()) {
            throw new IllegalStateException("Response is not received yet");
        } else if (wasCancelled) {
            throw new InterruptedException();
        } else if (exception != null) {
            throw exception;
        } else {
            return response;
        }
    }

    public final ServiceClientException getServiceClientException() {
        if (!isDone()) {
            throw new IllegalStateException("Response is not received yet");
        } else {
            return exception;
        }
    }

    public void setUserData(java.lang.Object usrData) {
        userData = usrData;
    }

    public java.lang.Object getUserData() {
        return userData;
    }

//    @Override
//    protected void customEvent(QEvent event) {
//        if (event instanceof NotifyListenerEvent) {
//            final IResponseListener listener = ((NotifyListenerEvent) event).listener;
//            if (!listeners.contains(listener)) {
//                return;
//            }
//            try {
//                if (wasCancelled) {
//                    listener.onRequestCancelled(request, this);
//                } else if (response != null) {
//                    listener.onResponseReceived(response, this);
//                } else {
//                    listener.onServiceClientException(exception, this);
//                }
//            } catch (Throwable ex) {
//                final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
//                final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
//                environment.getTracer().error(String.format(message, exceptionStr));
//            }
//            removeListener(listener);
//            if (!listeners.isEmpty()) {
//                QApplication.postEvent(this, new NotifyListenerEvent(listeners.get(0)));
//            }
//        }
//        super.customEvent(event);
//    }
    void processAnswer() {
        notifyListeners();
    }

    private class NotifyListenerTask implements Runnable{

        private final IResponseListener listener;

        public NotifyListenerTask(final IResponseListener listener){
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                synchronized(listeners){
                    if (!listeners.contains(listener))
                        return;
                }
                if (wasCancelled) {
                    listener.onRequestCancelled(request, RequestHandle.this);
                } else if (response != null) {
                    listener.onResponseReceived(response, RequestHandle.this);
                } else {
                    listener.onServiceClientException(exception, RequestHandle.this);
                }
            } catch (Throwable ex) {
                final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
                final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
                environment.getTracer().error(String.format(message, exceptionStr));
            }
            finally{
                removeListener(listener);
            }
        }
    }

    private void notifyListeners() {
        final List<IResponseListener> copyListeners = new ArrayList<IResponseListener>();
        synchronized (listeners) {
            copyListeners.addAll(listeners);
        }
        for (IResponseListener listener: copyListeners){
            try {
                environment.runInGuiThreadAsync(new NotifyListenerTask(listener));
            } catch (Throwable ex) {
                final String message = environment.getMessageProvider().translate("ExplorerError", "Unhandled exception on request result processing:\n%s");
                final String exceptionStr = ClientException.getExceptionReason(environment.getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
                environment.getTracer().error(String.format(message, exceptionStr));
            }
        }
    }

    public final void addListener(final IResponseListener listener) {
        synchronized (listeners) {
            if (listener != null && !listeners.contains(listener)) {
                listeners.add(listener);
                listener.registerRequestHandle(this);
            }
        }
    }

    public final void removeListener(final IResponseListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            listener.unregisterRequestHandle(this);
            if (!hasListeners() && inProgress()) {
                cancel();
            }
        }
    }

    public boolean hasListeners() {
        synchronized (listeners) {
            return !listeners.isEmpty();
        }
    }
}
