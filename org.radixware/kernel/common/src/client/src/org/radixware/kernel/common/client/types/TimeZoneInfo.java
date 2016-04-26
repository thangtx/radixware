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

package org.radixware.kernel.common.client.types;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public final class TimeZoneInfo {
    
    private final static class ServerTimeZone extends TimeZone implements Cloneable{
        
        private static final long serialVersionUID = 6437922163398713518L;
        
        private final TimeZone timeZone;
        private final int offsetDstMills;
        private final int offsetGmtMills;
        private final String[] zoneStrings;
        
        public ServerTimeZone(final TimeZone timeZone, final int gmtOffsetMills, final int dstOffsetMills){
            this.timeZone = timeZone;
            zoneStrings = null;
            offsetDstMills = dstOffsetMills;
            offsetGmtMills = gmtOffsetMills;
        }
        
        public ServerTimeZone(final String[] strings, final int gmtOffsetMills, final int dstOffsetMills){
            this.timeZone = null;
            this.zoneStrings = new String[5];
            System.arraycopy(strings, 0, this.zoneStrings, 0, strings.length);
            offsetDstMills = dstOffsetMills;
            offsetGmtMills = gmtOffsetMills;
        }        

        @Override
        public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
            return offsetDstMills+offsetGmtMills;
        }

        @Override
        public void setRawOffset(final int offsetMillis) {
            throw new UnsupportedOperationException("This operation is not supported");
        }

        @Override
        public int getRawOffset() {
            return offsetGmtMills;
        }

        @Override
        public boolean useDaylightTime() {
            return offsetDstMills!=0;
        }

        @Override
        public boolean inDaylightTime(final Date date) {
            return offsetDstMills!=0;
        }

        @Override
        public int getOffset(final long date) {
            return offsetDstMills+offsetGmtMills;
        }

        @Override
        public String getID() {
            return timeZone==null ? zoneStrings[0] : timeZone.getID();
        }

        @Override
        public void setID(final String ID) {
            throw new UnsupportedOperationException("This operation is not supported");
        }

        @Override
        public String getDisplayName(final boolean daylight, final int style, final Locale locale) {
            if (timeZone==null){
                int strIndex = style==TimeZone.LONG ? 1 : 2;
                if (daylight){
                    strIndex+=2;                    
                }
                return zoneStrings[strIndex];
            }else{
                return timeZone.getDisplayName(daylight, style, locale);
            }
        }

        @Override
        public int getDSTSavings() {
            return offsetDstMills;
        }

        @Override
        public boolean observesDaylightTime() {
            return offsetDstMills!=0;
        }

        @Override
        public boolean hasSameRules(final TimeZone other) {
            if (other instanceof ServerTimeZone){
                final ServerTimeZone otherTimeZone = (ServerTimeZone)other;
                return otherTimeZone.offsetDstMills==offsetDstMills && otherTimeZone.offsetGmtMills==offsetGmtMills;
            }else{
                return false;
            }
        }

        @Override
        public Object clone() {//NOPMD
            if (timeZone==null){
                return new ServerTimeZone(zoneStrings, offsetDstMills, offsetDstMills);
            }else{
                return new ServerTimeZone(timeZone, offsetGmtMills, offsetDstMills);
            }            
        }
        
        
        
    }
    
    private final String id;
    private final TimeZone timeZone;
    private final int offsetDstMills;
    private final int offsetGmtMills;    
    
    private TimeZoneInfo(final org.radixware.schemas.eas.TimeZone timeZone){
        id = timeZone.getId();
        offsetDstMills = timeZone.getDstOffsetMills();
        offsetGmtMills = timeZone.getOffsetMills();
        final List<String> availableZones = Arrays.<String>asList(TimeZone.getAvailableIDs());
        if (availableZones.contains(id)){
            final TimeZone candidate = TimeZone.getTimeZone(id);
            final Calendar calendar = Calendar.getInstance(candidate);
            calendar.setTimeInMillis(timeZone.getTimestamp().getTime());
            if (   calendar.get(Calendar.DST_OFFSET)==offsetDstMills 
                && calendar.get(Calendar.ZONE_OFFSET)==offsetGmtMills){
                this.timeZone = candidate;
            }else{
                this.timeZone = new ServerTimeZone(candidate, offsetGmtMills, offsetDstMills);
            }            
        }else{            
            final String zoneStrings[] = 
                new String[]{id,timeZone.getLongNameInStdTime(),timeZone.getShortNameInStdTime(),
                             timeZone.getLongNameInDlSavingTime(),timeZone.getShortNameInDlSavingTime()};
            this.timeZone = new ServerTimeZone(zoneStrings, offsetGmtMills, offsetDstMills);
        }        
    }        
    
    public TimeZoneInfo(final TimeZone timeZone, final Locale locale){
        id = timeZone.getID();
        this.timeZone = timeZone;
        final Calendar calendar = Calendar.getInstance(timeZone, locale);
        offsetDstMills = calendar.get(Calendar.DST_OFFSET);
        offsetGmtMills = calendar.get(Calendar.ZONE_OFFSET);
    }
    
    public String formatDate(final int dateStyle, final Locale locale, final Timestamp value){
        final DateFormat format = SimpleDateFormat.getDateInstance(dateStyle, locale);
        return formatValue(format,value);
    }
    
    public String formatTime(final int timeStyle, final Locale locale, final Timestamp value){
        final DateFormat format = SimpleDateFormat.getTimeInstance(timeStyle, locale);
        return formatValue(format,value);
    }
    
    public String formatDateTime(final int dateStyle, final int timeStyle, final Locale locale, final Timestamp value){
        final DateFormat format = SimpleDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        return formatValue(format,value);
    }
    
    public String formatDateTime(final String pattern, final Locale locale, final Timestamp value){
        final SimpleDateFormat format = new SimpleDateFormat(pattern, locale);        
        return formatValue(format,value);
    }
    
    private String formatValue(final DateFormat format, final Timestamp value){
        format.setTimeZone(timeZone);//set server time zone to get correct time zone name                
        //now DateFormat recalculates value into server time zone so translate it into client time zone before format.
        return format.format(translateToClientTimeZone(value));
    }
    
    private Timestamp translateToClientTimeZone(final Timestamp value){
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(value.getTime());
        final int selfOffset = c.get(Calendar.DST_OFFSET)+c.get(Calendar.ZONE_OFFSET);        
        final int timeOffset = timeZone.getOffset(value.getTime()) - selfOffset;
        return timeOffset==0 ? value : new Timestamp(value.getTime()-timeOffset);
    }
        
    public String getTimeZoneDisplayName(final int style, final Locale locale){
        return timeZone.getDisplayName(timeZone.inDaylightTime(Calendar.getInstance().getTime()), style, locale);
    }
    
    public int getOffsetMills(){
        return offsetDstMills+offsetGmtMills;
    }
    
    public String getId(){
        return id;
    }
    
    public static TimeZoneInfo parse(final org.radixware.schemas.eas.TimeZone timeZone){
        return new TimeZoneInfo(timeZone);
    }
}