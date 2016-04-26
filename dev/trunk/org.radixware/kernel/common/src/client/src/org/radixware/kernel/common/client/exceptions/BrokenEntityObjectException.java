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

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.types.Pid;


public class BrokenEntityObjectException  extends Exception implements IClientError {
    
    static final long serialVersionUID = 5124147630311808124L;
    private final Pid pid;
    private final String causeExceptionClassName;
    private final String causeExceptionMessage;
    private final String causeExceptionStack;    
    
    public BrokenEntityObjectException(final BrokenEntityModel brokenEntity){
        pid = brokenEntity.getPid();
        causeExceptionClassName = brokenEntity.getExceptionClass();
        causeExceptionMessage = brokenEntity.getExceptionMessage();
        causeExceptionStack = brokenEntity.getExceptionStack();
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerException", "Broken Entity Object");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        final String message = messageProvider.translate("ExplorerException", "Error reading entity object \'%s\'");
        return String.format(message,pid.toString());
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        final StringBuilder messageBuilder = new StringBuilder();
        if (causeExceptionClassName!=null && !causeExceptionClassName.isEmpty()) {
            messageBuilder.append("exception class: ");
            messageBuilder.append(causeExceptionClassName);
            messageBuilder.append("\n");
        }
        if (causeExceptionMessage!=null && !causeExceptionMessage.isEmpty()){
            messageBuilder.append("exception message: ");
            messageBuilder.append(causeExceptionMessage);
            messageBuilder.append("\n");            
        }
        if (causeExceptionStack!=null && !causeExceptionStack.isEmpty()){
            messageBuilder.append(causeExceptionStack);
        }
        return messageBuilder.toString();
    }

    public String getCauseExceptionClassName() {
        return causeExceptionClassName;
    }

    public String getCauseExceptionMessage() {
        return causeExceptionMessage;
    }

    public String getCauseExceptionStack() {
        return causeExceptionStack;
    }

    public Pid getPid() {
        return pid;
    }
        
}
