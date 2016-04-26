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

package org.radixware.kernel.common.client.exceptions;

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.WrongFormatException;


public class WrongAmPmFieldValue extends WrongFormatException implements IClientError{
    
    private static final long serialVersionUID = 7614457028648062366L;
    
    private final String amPmValue;
    
    public WrongAmPmFieldValue(final String value){
        super("Time is invalid");
        amPmValue = value;
    }
       
    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerException", "Input is Invalid");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        final String message =
            messageProvider.translate("ExplorerException", "Time is invalid: 'AM' or 'PM' expected but '%1$s' found");
        return String.format(message, amPmValue);
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }

}
