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

package org.radixware.kernel.explorer.iad.sane.gui;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.types.Id;


public class SaneLocalization {
    
    private final static Id MESSAGE_PROVIDER_CLASS_ID = Id.Factory.loadFrom("adcGKHABUCLE5HA7DJZS3SMNZ3WIM");
    private MessageProvider messageProvider;
    
            
    public SaneLocalization(final IClientEnvironment environment){
        try{
            final Class<?> stringProviderClass = 
                environment.getDefManager().getDynamicClassById(MESSAGE_PROVIDER_CLASS_ID);
            messageProvider = (MessageProvider)stringProviderClass.newInstance();
        }catch(Exception exception){
            final String message = 
                environment.getMessageProvider().translate("IAD","Failed to load translation strings for SANE library");
            environment.getTracer().error(message,exception);
            messageProvider = null;
        }
    }
    
    public String getLocalizedString(final String sourceString){
        return messageProvider==null ? sourceString : messageProvider.translate("IAD",sourceString);
    }
}
