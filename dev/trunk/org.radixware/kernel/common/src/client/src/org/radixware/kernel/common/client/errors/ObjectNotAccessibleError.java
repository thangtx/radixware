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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public final class ObjectNotAccessibleError  extends ServiceCallFault implements IClientError {
    
    static final long serialVersionUID = 1078030943336934039L;
    private final Pid pid;
    private String message;
    private String detailMessage;
    
    
    public ObjectNotAccessibleError(final IClientEnvironment environment, final Pid pid) {
        super("Client", org.radixware.schemas.eas.ExceptionEnum.ACCESS_VIOLATION.toString(), null);
        this.pid = pid;
        
        final RadClassPresentationDef classDef;
        try {
            classDef = environment.getApplication().getDefManager().getClassPresentationDef(pid.getTableId());
        } catch (DefinitionError error) {
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not accessible in table #%s");
                detailMessage = String.format(messageTemplate, pid.toString(), pid.getTableId().toString());
            }
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not accessible");
                message = String.format(messageTemplate, pid.toString());
            }
            return;
        }        
        message = String.format(environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not accessible"), pid.toString());
        final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not accessible for class #%s");
        detailMessage = String.format(messageTemplate, pid.toString(), classDef.getId().toString());
    }
    
    //getMessage() не стал перекрывать т.к. ServiceCallFault исключение
    //может обрабатываться в прикладном коде, который ожидает оригинальный
    //формат сообщения
    public String getMessageToShow() {
        return message;
    }

    public Pid getPid() {
        return pid;
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Object does not accessible");
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return detailMessage;
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return getDetailMessage(messageProvider);
    }    
    
}
