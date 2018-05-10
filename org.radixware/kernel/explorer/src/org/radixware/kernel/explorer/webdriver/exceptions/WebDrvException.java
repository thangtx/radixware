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

package org.radixware.kernel.explorer.webdriver.exceptions;

import org.radixware.kernel.common.client.exceptions.ClientException;

public class WebDrvException extends Exception{
    
    private static final long serialVersionUID = 2085385498938836306L;
    private final EWebDrvErrorCode errorCode;    
    
    public WebDrvException(final EWebDrvErrorCode code){
        this(code,null);
    }
    
    public WebDrvException(final String message, final Throwable cause){
        super(message==null || message.isEmpty() ? EWebDrvErrorCode.UNKNOWN_ERROR.getDescription() : message, cause);
        errorCode = EWebDrvErrorCode.UNKNOWN_ERROR;
    }
    
    public WebDrvException(final EWebDrvErrorCode code, final String message){
        super(message==null || message.isEmpty() ? code.getDescription() : message);
        errorCode = code;
    }
    
    public int getHttpStatus(){
        return errorCode.getHttpStatus();
    }
    
    public String getHttpBody(){
        final String stackTrace = ClientException.exceptionStackToString(this);
        return errorCode.getHttpBody(getMessage(), stackTrace);
    }
    
    public boolean isRecoverable(){
        return errorCode!=null && errorCode.isRecoverable();
    }
}
