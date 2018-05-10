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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.CommandRequestHandle;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public class ResponseListener implements org.radixware.kernel.common.client.eas.IResponseListener {

    private final List<org.radixware.kernel.common.client.eas.RequestHandle> handles = new ArrayList<RequestHandle>();
    private final IClientEnvironment environment;

    public ResponseListener(final IClientEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void registerRequestHandle(final RequestHandle handle) {
        handles.add(handle);
    }

    @Override
    public void unregisterRequestHandle(final RequestHandle handle) {
        handles.remove(handle);
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public IClientApplication getApplication() {
        return getEnvironment().getApplication();
    }

    public final CommandRequestHandle sendContextlessCommandAsync(final Id commandId,
            final XmlObject input,
            final Class<? extends XmlObject> expectedOutputClass,
            final java.lang.Object userData) {
        return sendContextlessCommandAsync(commandId, input, expectedOutputClass, userData, 0);
    }
    
    public final CommandRequestHandle sendContextlessCommandAsync(final Id commandId,
            final XmlObject input,
            final Class<? extends XmlObject> expectedOutputClass,
            final java.lang.Object userData,
            final int timeoutSec) {
        final CommandRequestHandle handle =
            RequestHandle.Factory.createForSendContextlessCommand(getEnvironment(), commandId, input, expectedOutputClass);
        handle.addListener(this);
        handle.setUserData(userData);
        getEnvironment().getEasSession().sendAsync(handle,timeoutSec);
        return handle;
    }

    @Override
    public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
        if (handle instanceof CommandRequestHandle) {
            final CommandRequestHandle commandHandler = (CommandRequestHandle) handle;
            try {
                onCommandResponse(commandHandler.getCommandId(),
                        commandHandler.getOutput(),
                        commandHandler.getUserData(),
                        commandHandler);
            } catch (ServiceClientException ex) {
                printError("Unexpected exception", ex);
            } catch (InterruptedException ex) {
                printError("Unexpected exception", ex);
            }
        }
    }

    @Override
    public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
        final String requestName = handle.getRequest().getClass().getSimpleName();
        printError("Error ocured during " + requestName + " request processing", exception);
    }

    @Override
    public void onRequestCancelled(final XmlObject request, final  RequestHandle handler) {
        final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Async request was cancelled");
        getEnvironment().getTracer().event(message);
    }

    private void printError(final String message, final Exception exception) {        
        final String messageError = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception) + "\n" + ClientException.exceptionStackToString(exception);
        final EEventSeverity severity = ClientException.getFaultSeverity(exception);                
        getEnvironment().getTracer().put(severity, message + ":\n" + messageError);
    }

    protected final Collection<RequestHandle> getRequestHandles(final Class<? extends XmlObject> expectedMessageClass) {
        final Collection<RequestHandle> result = new ArrayList<RequestHandle>();
        for (RequestHandle handle : handles) {
            if (expectedMessageClass == null
                    || handle.getExpectedResponseMessageClass() == expectedMessageClass) {
                result.add(handle);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    protected final void removeProcessedHandles(final Class<? extends XmlObject> expectedMessageClass) {
        final Collection<RequestHandle> handlesToRemove = getRequestHandles(expectedMessageClass);
        for (RequestHandle handle : handlesToRemove) {
            if (handle.inProgress() || handle.isDone()) {
                handle.removeListener(this);
            }
        }
    }

    protected final boolean closeActiveRequestHandles(final boolean forced) {
        RequestHandle handle;
        for (int i = handles.size() - 1; i >= 0; i--) {
            handle = handles.get(0);
            if (!handle.isActive() || forced || aboutToCloseActiveRequestHandle(handle)) {
                handle.removeListener(this);
            } else {
                return false;
            }
        }
        return true;
    }

    protected boolean aboutToCloseActiveRequestHandle(final  RequestHandle handle) {
        return true;
    }

    protected void onCommandResponse(final Id commandId,
            final XmlObject output,
            final java.lang.Object userData,
            final CommandRequestHandle handle) {
    }
}
