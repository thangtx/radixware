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

package org.radixware.kernel.common.client.exceptions;

import java.io.EOFException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.IAlarm;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.ParentRefSetterError;
import org.radixware.kernel.common.client.errors.UnacceptableInputError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallRecvException;


public final class ExceptionMessage {
    
    private final String exceptionDetails;
    private final String exceptionTrace;
    private final String exceptionMessage;
    private final String exceptionTitle;
    private final EEventSeverity severity;
    private final EEventSeverity traceMessageSeverity;
    private final Model contextModel;
    private final Throwable exception;
    private final IClientEnvironment environment;
    private final boolean hasDialogMessage;
    
    public ExceptionMessage(final IClientEnvironment environment, final Throwable exception){
        final MessageProvider mp = environment.getMessageProvider();
        this.exception = exception;
        this.environment = environment;
        if (ClientException.isSpecialFault(exception)) {
            exceptionMessage = ClientException.getSpecialFaultMessage(environment, exception);
            hasDialogMessage = true;
            exceptionDetails = "";
            exceptionTrace = ClientException.exceptionStackToString(exception);
            severity = ClientException.isInformationMessage(exception) ? EEventSeverity.EVENT : EEventSeverity.ERROR;
            contextModel = null;
            exceptionTitle = ClientException.getExceptionTitle(mp, exception);
            traceMessageSeverity = ClientException.getFaultSeverity(exception);
        }else{
            String message = "", title = "";
            EEventSeverity sev = null;
            EEventSeverity traceSev = null;
            boolean showDialog = true;
            boolean showExceptionDetails = true;
            Model model = null;
            for (Throwable err = exception; err != null && err.getCause() != err; err = err.getCause()) {
                if (err.getClass()==AppError.class){
                    if (err.getMessage() != null && !err.getMessage().isEmpty()) {
                        message = err.getMessage();
                    } else{
                        message = ClientException.getExceptionReason(mp, exception);
                    }
                    showDialog = true;
                    showExceptionDetails = false;
                    break;
                }else if (err instanceof ParentRefSetterError) {
                    final ParentRefSetterError error = (ParentRefSetterError) err;
                    if (error.getSourceModel() != null) {
                        model = error.getSourceModel();
                        error.setSourceModel(null);
                        title = mp.translate("ExplorerError", "Setting Property Value Error");
                        break;
                    }
                }
                if (err instanceof CredentialsWasNotDefinedError){
                    traceSev = EEventSeverity.EVENT;
                    sev = EEventSeverity.ALARM;
                    showDialog = false;
                    title = ((CredentialsWasNotDefinedError)err).getTitle(mp);
                    message = mp.translate("ExplorerMessage", "Closing connection.")+"\n"+
                            ((CredentialsWasNotDefinedError)err).getLocalizedMessage(mp);
                    break;
                }else if (err instanceof IAlarm) {                    
                    title = mp.translate("ExplorerMessage", "Error");
                    message = err.getMessage();
                    sev = EEventSeverity.ALARM;
                    break;
                } else if (err instanceof NoInstantiatableClassesException) {
                    title = mp.translate("ExplorerError", "Failed to create object");
                    message = mp.translate("ExplorerError", "It is impossible to create objects in this context");
                    break;
                } else if (err instanceof ServiceCallRecvException && err.getCause() instanceof EOFException){
                    title = mp.translate("ExplorerMessage", "Connection Problem");
                    message = mp.translate("ExplorerError", "Unable to receive server response: broken network connection");
                }
            }
            hasDialogMessage = showDialog && message!=null;
            if (message==null || message.isEmpty()){
                if (exception instanceof ObjectNotFoundError){
                    exceptionMessage = ((ObjectNotFoundError)exception).getMessageToShow();
                }else if (exception instanceof IClientError){
                    exceptionMessage = ((IClientError)exception).getLocalizedMessage(mp);
                }else{
                    exceptionMessage =  ClientException.getExceptionReason(mp, exception);
                }
            }else{
                exceptionMessage = message;
            }
            exceptionTitle = title.isEmpty() ? ClientException.getExceptionTitle(mp, exception) : title;
            severity = sev==null ? EEventSeverity.ERROR : sev;
            traceMessageSeverity = traceSev==null ? EEventSeverity.ERROR : traceSev;
            contextModel = model;
            if (showExceptionDetails){
                if (exception instanceof ModelException){
                    exceptionDetails = "";
                    exceptionTrace = ClientException.exceptionStackToString(exception);
                }
                else{
                    if (exception instanceof IClientError) {                        
                        exceptionDetails = 
                            ((IClientError) exception).getDetailMessage(mp);
                        exceptionTrace = ClientException.exceptionStackToString(exception);
                    } else if (exception instanceof ServiceCallFault) {
                        exceptionDetails = 
                            ClientException.getSerivceCallFaultDetails((ServiceCallFault)exception);
                        exceptionTrace = ((ServiceCallFault) exception).getCauseExStack();
                    } else {
                        exceptionDetails = "exception: " + exception.getClass().getName()
                                + "\nexception message: " + exception.getMessage();
                        exceptionTrace = ClientException.exceptionStackToString(exception);
                    }

                }
            }else{
                exceptionDetails = "";
                exceptionTrace = ClientException.exceptionStackToString(exception);
            }
        }
    }
    
    public String getDetails(){
        return exceptionDetails==null ? "": exceptionDetails;
    }
    
    public String getTrace(){
        return exceptionTrace;
    }
    
    public String getDialogMessage(){
        return exceptionMessage;
    }
    
    public boolean hasDialogMessage(){
        return hasDialogMessage;
    }
    
    public String getDialogTitle(){
        return ClientValueFormatter.capitalizeIfNecessary(environment,exceptionTitle);
    }
    
    public EEventSeverity getSeverity(){
        return severity;
    }        
    
    public void display(final String title, final IWidget parentWidget){
        final String dialogTitle = 
            ClientValueFormatter.capitalizeIfNecessary(environment, title==null || title.isEmpty() ? exceptionTitle : title);
        if (getSeverity()==EEventSeverity.EVENT){
            environment.messageInformation(dialogTitle, getDialogMessage());
        }else{
            if (contextModel!=null){
                contextModel.showException(dialogTitle, exception);
            }
            else if (getDetails().isEmpty()){
                if (environment.getMessageProvider().translate("ExplorerError", "Unhandled Exception").equals(dialogTitle)){
                    environment.messageError(getDialogMessage());
                }else{
                    environment.messageError(dialogTitle, getDialogMessage());
                }
                if (exception instanceof UnacceptableInputError){
                    ((UnacceptableInputError)exception).getProperty().setFocused();
                }
            }
            else{
                final IDialog dialog = 
                    environment.getApplication().getDialogFactory().newExceptionBoxDialog(environment, parentWidget, dialogTitle, exceptionMessage, exceptionDetails, exceptionTrace);                
                dialog.execDialog();
            }
        }
    }
    
    public void display(final IWidget parentWidget){
        display(null, parentWidget);
    }
    
    public void trace(final ClientTracer tracer, final String title){        
        if (traceMessageSeverity==EEventSeverity.EVENT){
            tracer.put(EEventSeverity.EVENT, exceptionMessage);
        }else if (traceMessageSeverity==EEventSeverity.DEBUG){
            tracer.debug(title, exception);
        }else{
            if (title==null || title.isEmpty()){
                tracer.put(exception);
            }else{
                tracer.error(title, exception);
            }
        }
    }
}
