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

package org.radixware.kernel.server.arte.services.aas;

import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.aas.ExceptionEnum;


final class AasFaults {
//Std faults
    static ServiceProcessFault exception2Fault(final Arte arte, final Throwable e, final String defPresenterComment) throws InterruptedException{
    	if (e instanceof InterruptedException)
    		throw (InterruptedException)e;
    	return ex2Flt(arte, e,defPresenterComment);
    }
    static ServiceProcessFault exception2Fault(final Arte arte, final RuntimeException e, final String defPresenterComment){
    	return ex2Flt(arte, e,defPresenterComment);
    }
    private static ServiceProcessFault ex2Flt(final Arte arte, final Throwable e, final String defPresenterComment){
        if(e instanceof ServiceProcessFault)
            return (ServiceProcessFault)e;

		String mess = ExceptionTextFormatter.getExceptionMess(e);

        ExceptionEnum.Enum clientFaultReason = null, srvFaultReason = null;
        if (e instanceof EntityObjectNotExistsError){
        	clientFaultReason = ExceptionEnum.OBJECT_NOT_FOUND;
        	final EntityObjectNotExistsError ex = (EntityObjectNotExistsError)e;
        	mess = String.valueOf(ex.getTable().getId()) + "\n" + String.valueOf(ex.getKeyPres());
        }	
        else if (
        	e instanceof DefinitionNotFoundError ||
        	e instanceof NoConstItemWithSuchValueError 
        )
        	clientFaultReason = ExceptionEnum.DEFINITION_NOT_FOUND;
        else if (
          	e instanceof IllegalUsageError 
        )
           	clientFaultReason = ExceptionEnum.APPLICATION_ERROR;
        else if (
           	e instanceof XmlException
        )
            clientFaultReason = ExceptionEnum.FORMAT_ERROR;
        else if (
        	e instanceof RuntimeException ||
        	e instanceof Error
        )
        	srvFaultReason = ExceptionEnum.SERVER_MALFUNCTION;
        else 
        	srvFaultReason = ExceptionEnum.SERVER_EXCEPTION;
		final String preprocessedExceptionStack = arte.getTrace().exceptionStackToString(e);
        if(clientFaultReason != null){
	        arte.getTrace().put(EEventSeverity.DEBUG, "Client fault caused by: " + preprocessedExceptionStack, EEventSource.AAS);
        	return new ServiceProcessClientFault(clientFaultReason.toString(), mess, e, preprocessedExceptionStack);
        }
        if(srvFaultReason != null) {
        	arte.getTrace().put(EEventSeverity.DEBUG, "Server fault caused by: " + preprocessedExceptionStack, EEventSource.AAS);
        	return new ServiceProcessServerFault(srvFaultReason.toString(), mess, e, preprocessedExceptionStack);
        } else {//Others
        	arte.getTrace().put(EEventSeverity.WARNING, "Can't find corresponding faultstring for " + preprocessedExceptionStack, EEventSource.AAS);
	        return new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), defPresenterComment + ": " + mess, e, preprocessedExceptionStack);        
        }
    }

    static final ServiceProcessClientFault newWrongParamValFault(final String param, final String val){
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\""+param+"\" value can't be \""+val+"\"", null, null);
    }

    static final ServiceProcessClientFault newParamRequiedFault(final String param, final String place){
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\""+param+"\" is required in \""+place+"\"", null, null);
    }    
}
