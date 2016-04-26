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

import java.util.Calendar;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.WrongFormatException;

public class DateTimeFieldNotDefinedException extends WrongFormatException implements IClientError{
    
    private static final long serialVersionUID = 5540492283054575327L;
    
    private final int calendarField;    

    public DateTimeFieldNotDefinedException(final int field){
        super("Date or time is invalid: required field was not defined");
        calendarField = field;
    }
    
    public int getUndefinedField(){
        return calendarField;
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerException", "Input is Invalid");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        switch(calendarField){
            case Calendar.YEAR:
                return messageProvider.translate("ExplorerException", "Date is invalid: year was not defined");
            case Calendar.MONTH:
                return messageProvider.translate("ExplorerException", "Date is invalid: month was not defined");
            case Calendar.DAY_OF_MONTH:
                return messageProvider.translate("ExplorerException", "Date is invalid: day of month was not defined");
            case Calendar.HOUR_OF_DAY:
                return messageProvider.translate("ExplorerException", "Time is invalid: hours was not defined");
            case Calendar.MINUTE:
                return messageProvider.translate("ExplorerException", "Time is invalid: minutes was not defined");
            case Calendar.SECOND:
                return messageProvider.translate("ExplorerException", "Time is invalid: seconds was not defined");
            case Calendar.MILLISECOND:
                return messageProvider.translate("ExplorerException", "Time is invalid: milliseconds was not defined");
           default:
               return messageProvider.translate("ExplorerException", "Date or time is invalid: required field was not defined");
        }
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }
    
}