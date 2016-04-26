/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.items.properties.Property;


public final class UnacceptableInputError extends RuntimeException implements IClientError{
    
    private static final long serialVersionUID = 6475927583251807303L;
    
    private final Property property;
        
    public UnacceptableInputError(final Property property){
        super(buildMessage(property));
        this.property = property;
    }
    
    private static String buildMessage(final Property property){
        final MessageProvider messageProvider = property.getEnvironment().getMessageProvider();
        final String reason = property.getUnacceptableInput().getMessageText();
        final String messageTemplate = 
            messageProvider.translate("ExplorerDialog", "Field '%1$s' has unacceptable input.\n%2$s");
        return String.format(messageTemplate, property.getTitle(), reason);
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerException", "Unacceptable Input");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return getMessage();
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }

    public Property getProperty(){
        return property;
    }
}
