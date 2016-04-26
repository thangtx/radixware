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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public final class InvalidSortingException extends ServiceCallFault implements IClientError{
    
    private final String message, detailMessage;
    static final long serialVersionUID = -4273178845259988192L;
    
    public InvalidSortingException(final IClientEnvironment environment, final ServiceCallFault source, final FilterModel currentFilter, final RadSortingDef currentSorting){
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());
        final MessageProvider mp = environment.getMessageProvider();
        //"ExplorerError", "Current Filter is Obsolete"
        if (currentFilter==null){
            final String messageTemplate = 
                mp.translate("ExplorerError", "Sorting '%1$s' is restricted");
            final String detailMessageTemplate = 
                mp.translate("ExplorerError", "Sorting '%1$s' (#%2$s) is restricted");
            message = 
                String.format(messageTemplate,currentSorting.getTitle());
            detailMessage = 
                String.format(detailMessageTemplate,currentSorting.getTitle(), currentSorting.getId().toString());
        }
        else{
            final String messageTemplate = 
                mp.translate("ExplorerError", "Sorting '%1$s' cannot be used with filter '%2$s'");
            final String detailMessageTemplate = 
                mp.translate("ExplorerError", "Sorting '%1$s' (#%2$s) cannot be used with filter '%3$s' (#%4$s)");
            message = 
                String.format(messageTemplate,currentSorting.getTitle(),currentFilter.getTitle());
            detailMessage = 
                String.format(detailMessageTemplate,currentSorting.getTitle(), currentSorting.getId().toString(), currentFilter.getTitle(), currentFilter.getId().toString());
        }
    }

    @Override
    public String getTitle(MessageProvider messageProvider) {
        return messageProvider.translate("Selector", "Can't Apply Sorting");
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return message;
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return detailMessage;
    }    
}
