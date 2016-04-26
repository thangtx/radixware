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

package org.radixware.kernel.common.build.xbeans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.xmlbeans.GDate;


public class TimeStamp2GDate implements TypeMapper {

    @Override
    public Object convertTo(Object origin) {
        if (origin instanceof Timestamp) {
            //TWRBS-602 Edited by yremizov
            //old code:
            //Calendar c = Calendar.getInstance();
            //c.setTime((Timestamp) origin);
            //return c;
            //new code:
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Timestamp)origin).getTime());
            return new GDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND),
                    BigDecimal.valueOf(calendar.get(Calendar.MILLISECOND), 3));
            //??? xmlBeans bug ??? when using Calendar c = new XmlCalendar(); c.setTime((Timestamp) origin);
            //then result is wrong for 1900-1919 years (+24 seconds) ???
        } else {
            return null;
        }
    }

    @Override
    public Object convertFrom(Object converted) {
        final Calendar calendar;
        if (converted instanceof GDate) {
            final GDate gdate = (GDate) converted;
            if (gdate.hasTimeZone()){
                calendar = gdate.getCalendar();
            }
            else{
                calendar = Calendar.getInstance();
                calendar.set(gdate.getYear(),gdate.getMonth()-1,gdate.getDay(),gdate.getHour(),gdate.getMinute(),gdate.getSecond());
                calendar.set(Calendar.MILLISECOND, gdate.getMillisecond());
            }            
        } else if (converted instanceof Calendar) {
            calendar  = (Calendar)converted;            
        } else {
            return null;
        }
        return new Timestamp(calendar.getTimeInMillis());
    }
}
