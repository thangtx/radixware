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

public class DateTimeFieldOutOfBoundsException extends WrongFormatException implements IClientError{
    
    private static final long serialVersionUID = -8801115350125854428L;
    
    private final int calendarField;
    private final int minValue;
    private final int maxValue;
    private final int currentValue;    
    
    public DateTimeFieldOutOfBoundsException(final int field, 
                                             final int minVal, 
                                             final int maxVal,
                                             final int curVal,
                                             final Throwable cause){
        super("Date or time is invalid",cause);
        calendarField = field;
        minValue = minVal;
        maxValue = maxVal;
        currentValue = curVal;
    }

    public int getField() {
        return calendarField;
    }

    public int getMinimumValue() {
        return minValue;
    }

    public int getMaximumValue() {
        return maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }        

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerException", "Input is Invalid");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        final String messageTemplate;
        switch(calendarField){
            case Calendar.YEAR:
                messageTemplate =  messageProvider.translate("ExplorerException", "Date is invalid: year must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.MONTH:
                messageTemplate =  messageProvider.translate("ExplorerException", "Date is invalid: month must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.DAY_OF_MONTH:
                messageTemplate =  messageProvider.translate("ExplorerException", "Date is invalid: day of month must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.HOUR_OF_DAY:
                messageTemplate =  messageProvider.translate("ExplorerException", "Time is invalid: hours must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.MINUTE:
                messageTemplate =  messageProvider.translate("ExplorerException", "Time is invalid: minutes must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.SECOND:
                messageTemplate =   messageProvider.translate("ExplorerException", "Time is invalid: seconds must be between %1$s and %2$s (current value is %3$s)");
                break;
            case Calendar.MILLISECOND:
                messageTemplate =  messageProvider.translate("ExplorerException", "Time is invalid: milliseconds must be between %1$s and %2$s (current value is %3$s)");
                break;
           default:
               return messageProvider.translate("ExplorerException", "Date or time is invalid");
        }
        return String.format(messageTemplate, String.valueOf(minValue), String.valueOf(maxValue), String.valueOf(currentValue));
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }
}