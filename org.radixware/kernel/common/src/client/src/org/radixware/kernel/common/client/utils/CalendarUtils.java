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

package org.radixware.kernel.common.client.utils;

import java.util.Calendar;


public class CalendarUtils {
    
    private CalendarUtils(){
    }
    
    public static long getTimeZoneOffsetInMillis(){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        return cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET);        
    }
    
    public static long getNormalizedTimeInMillis(final long time){
        final Calendar cal = Calendar.getInstance();        
        cal.setTimeInMillis(time);
        final long hours = cal.get(Calendar.HOUR_OF_DAY);
        final long minutes = cal.get(Calendar.MINUTE);
        final long seconds = cal.get(Calendar.SECOND);
        final long mills = cal.get(Calendar.MILLISECOND);
        return hours*getMillisForUnit(Calendar.HOUR_OF_DAY)
              +minutes*getMillisForUnit(Calendar.MINUTE)
              +seconds*getMillisForUnit(Calendar.SECOND)
              +mills;        
    }
    
    public static long getMillisForUnit(final int unit){
        switch (unit) {
            case Calendar.MILLISECOND:
                return 1;
            case Calendar.SECOND:
                return 1000;
            case Calendar.MINUTE:
                return 60000;
            case Calendar.HOUR_OF_DAY:
                return 3600000;                
            case Calendar.DAY_OF_MONTH:
                return 86400000;
            default:
                return 0;
        }
    }
}
