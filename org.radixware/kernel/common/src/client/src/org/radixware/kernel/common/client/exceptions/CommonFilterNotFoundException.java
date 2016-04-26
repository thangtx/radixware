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
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.types.Id;


public final class CommonFilterNotFoundException extends ServiceCallFault implements IClientError{
    
    private final Id filterId;
    private final String title, message;
    
    static final long serialVersionUID = -7435131288128291413L;
    
    public CommonFilterNotFoundException(final IClientEnvironment environment, final ServiceCallFault source, final RadCommonFilter currentFilter){
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());        
        title = environment.getMessageProvider().translate("ExplorerError", "Current Filter  does not Exist");
        final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Filter '%s' was removed. It is necessary to reread data");
        message = String.format(messageTemplate,currentFilter.getTitle());
        filterId = currentFilter.getId();
    }
    
    public CommonFilterNotFoundException(final IClientEnvironment environment, final RadCommonFilter currentFilter){
        super("Client", org.radixware.schemas.eas.ExceptionEnum.FILTER_NOT_FOUND.toString(), null);
        title = environment.getMessageProvider().translate("ExplorerError", "Current Filter  does not Exist");
        final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Filter '%s' was removed. It is necessary to reread data");
        message = String.format(messageTemplate,currentFilter.getTitle());
        filterId = currentFilter.getId();
    }    
    
    public Id getFilterId(){
        return filterId;
    }
    
    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return title;
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return message;
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return getLocalizedMessage(messageProvider);
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    
}
