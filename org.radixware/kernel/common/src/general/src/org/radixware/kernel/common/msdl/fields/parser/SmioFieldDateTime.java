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
package org.radixware.kernel.common.msdl.fields.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.DateTimeFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.utils.FloraTime;
import org.radixware.schemas.msdl.DateTimeField;
import org.radixware.schemas.msdl.DateTimeFormatDef;
import org.radixware.schemas.msdl.EncodingDef;
import org.radixware.schemas.types.DateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EMsdlTimeZoneType;
import org.radixware.kernel.common.utils.SystemPropUtils;

public final class SmioFieldDateTime extends SmioFieldSimple {

    private static final String SINGLE_YEAR_PATTERN = "yDDD";
    private static final String SINGLE_YEAR_PATTERN_REPLACEMENT = "yyyyDDD";
    private static final String EXPLICIT_LEAP_YEAR = SystemPropUtils.getStringSystemProp("rdx.msdl.default.year", "1972");
    private static final List<String> AVAILABLE_TIME_ZONE_IDS = Collections.unmodifiableList(Arrays.asList(TimeZone.getAvailableIDs()));
    private static final ConcurrentMap<String, TimeZone> TIME_ZONES_CACHE = new ConcurrentHashMap<>();
    private static final String DEFAULT_TIME_ZONE_KEY = "DEFAULT_TZ_KEY";

    private SimpleDateFormat formatPattern;
    private DateTime defaultVal;
    private DateTimeFormatDef.Enum format = null;
    private boolean isTimeZoneInited = false;
    private final boolean isLenient;
    private boolean isSingleYearPattern = false;

    public SmioFieldDateTime(DateTimeFieldModel model) throws SmioError {
        super(model);
        try {
            final Boolean isLenientTmp = getField().isSetLenientParse() ? getField().getLenientParse() : getModel().getDateTimeLenientParse(false);
            isLenient = isLenientTmp != null ? isLenientTmp : true;
            
            String frm = getField().isSetFormat() ? getField().getFormat() : getModel().getDateTimeFormat(false);
            if (frm != null) {
                format = DateTimeFormatDef.Enum.forString(frm);
                if (format == DateTimeFormatDef.STR) {
                    String pattern = getField().isSetPattern() ? getField().getPattern() : getModel().getDateTimePattern(false);
                    formatPattern = null;
                    try {
                        isSingleYearPattern = SINGLE_YEAR_PATTERN.equals(pattern);
                        if (isSingleYearPattern) {
                            pattern = SINGLE_YEAR_PATTERN_REPLACEMENT;
                        }
                        formatPattern = new SimpleDateFormat(pattern);
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            if (getField().isSetDefaultVal()) {
                defaultVal = DateTime.Factory.newInstance();
                defaultVal.setDateValue(getField().getDefaultVal());
            }
        } catch (Throwable e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }
    
    private EMsdlTimeZoneType getTimeZoneType() {
        EMsdlTimeZoneType timeZoneType;
        if (getField().isSetTimeZoneType()) {
            timeZoneType = getField().getTimeZoneType();
        } else {
            timeZoneType = getModel().getTimeZoneType(false);
        }
        if (timeZoneType == null) {
            timeZoneType = EMsdlTimeZoneType.LOCAL;
        }
        return timeZoneType;
    }

    private void initTimeZone() {
        if (isTimeZoneInited) {
            return;
        }

        final EMsdlTimeZoneType tzType = getTimeZoneType();
        final TimeZone tz;
        switch (tzType) {
            case LOCAL:
                tz = getTimeZone(DEFAULT_TIME_ZONE_KEY);
                break;
            case UTC:
                tz = getTimeZone("UTC");
                break;
            case SPECIFIED:
                final String tzId = getModel().getSpecifiedTimeZoneId(false);
                if (tzId == null) {
                    throw new SmioError("Field time zone type is 'Specified', but specified time zone id is not defined in parameters structure", getModel().getName());
                }
                if (!isCorrectTimeZoneId(tzId)) {
                    throw new SmioError("Specified unknown time zone id: '" + tzId + "'", getModel().getName());
                }
                tz = getTimeZone(tzId);
                break;
            default:
                throw new IllegalStateException("Unknown time zone enum item: " + tzType);
        }
        formatPattern.setTimeZone(tz);
        isTimeZoneInited = true;
    }
    
    private TimeZone getTimeZone(String timeZoneKey) {
        TimeZone tz = TIME_ZONES_CACHE.get(timeZoneKey);
        if (tz == null) {
            if (timeZoneKey.equals(DEFAULT_TIME_ZONE_KEY)) {
                tz = TimeZone.getDefault();
            } else {
                tz = TimeZone.getTimeZone(timeZoneKey);
            }
            TIME_ZONES_CACHE.put(timeZoneKey, tz);
        }
        return tz;
    }
    
    @Override
    public boolean getIsBCD() {
        try {
            return getCoder().encoding == EncodingDef.BCD;
        } catch (SmioException ex) {
            return false;
        }
    }
    
    @Override
    public DateTimeField getField() {
        return (DateTimeField) getModel().getField();
    }

    @Override
    public void readAsDbfObject(XmlObject obj, Object object) throws SmioException, IOException {
//        try {
//            value.setDateValue((java.util.Date) object);
//            obj.set(value);
//        } catch (Throwable e) {
//            throw new SmioException(readError, e, getModel().getName());
//        }
    }

    @Override
    public Object writeAsDbfObject(final XmlObject o) throws SmioException, IOException {
//        try {
//            if (o == null) {
//                return null;
//            } else {
//                value = DateTime.Factory.parse(o.getDomNode());
//                return value.getDateValue();
//            }
//        } catch (Throwable e) {
//            throw new SmioException(writeError, e, getModel().getName());
//        }
        return null;
    }

    private Date tryParseDateByMask(String dateStr) throws ParseException {
        if (dateStr == null) {
            return null;
        }
        if (isSingleYearPattern) {
            if (dateStr.length() == SINGLE_YEAR_PATTERN.length()) {
                final Calendar curTime = Calendar.getInstance();
                final int base = curTime.get(Calendar.YEAR) / 10;
                dateStr = Integer.toString(base) + dateStr;
            } else {
                throw new ParseException("Date string: '" + dateStr + "' can not be parsed by pattern: " + SINGLE_YEAR_PATTERN, 0);
            }
        }
        formatPattern.setLenient(false);
        try {
            return formatPattern.parse(dateStr);
        } catch (Exception ex) {
            try {
                final SimpleDateFormat formatWithExplicitLeapYear = new SimpleDateFormat("yyyy " + formatPattern.toPattern());
                formatWithExplicitLeapYear.setLenient(false);
                return formatWithExplicitLeapYear.parse(EXPLICIT_LEAP_YEAR + " " + dateStr);
            } catch (Exception ex1) {
                if (this.isLenient) {
                    formatPattern.setLenient(true);
                    return formatPattern.parse(dateStr);
                } else {
                    throw ex;
                }
            }
        }
    }
    
    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        String s = parseToString(ids);
        if (s == null || s.isEmpty()) {
            obj.setNil();
            return;
        }
        DateTime dt = DateTime.Factory.newInstance();
        if (format == DateTimeFormatDef.STR) {
            initTimeZone();
            try {
                dt.setDateValue(tryParseDateByMask(s));
                String patt = formatPattern.toPattern();
                final int firstYearSymbol = patt.indexOf('y');
                final int lastYearSymbol = patt.lastIndexOf('y');
                final int yearPatternLenInSymbols = lastYearSymbol - firstYearSymbol + 1;
                if (isSingleYearPattern || yearPatternLenInSymbols == 2) { //'yDDD' or year pattern contains 'yy'
                    //RADIX-6601: normalize year according to current date
                    dt.setDateValue(normalizeDate(dt.getDateValue()));
                }
            } catch (ParseException ex) {
                if (isLenient) {
                    LogFactory.getLog(SmioFieldDateTime.class).error(null, ex);
                } else {
                    throw new SmioException("Error on parse date by pattern: " + formatPattern.toPattern(), ex);
                }
            }
        }
        if (format == DateTimeFormatDef.JAVA) {
            java.util.Date d = new java.util.Date(Long.parseLong(s));
            dt.setDateValue(d);
        }
        if (format == DateTimeFormatDef.FLORA) {
            BigDecimal time = new BigDecimal(s);
            java.util.Date dtime = FloraTime.toTimestamp(time);
            dt.setDateValue(dtime);
        }
        obj.set(dt);
    }
    
    private Date normalizeDate(final Date parsedValue) {
        final Calendar resultTime = Calendar.getInstance();
        resultTime.setTime(parsedValue);
        if (isSingleYearPattern) {
            //Corner cases:
            //Send date |Receive date|Message|Correct Answer
            //31.12.2019|01.01.2020  |9001   |01.01.2019
            //01.01.2020|31.12.2019  |0001   |01.01.2020
            //To avoid errors in corner cases we calculate all possible date values (3)
            //and choose date nearest to current date.
            final int dayOfYear = resultTime.get(Calendar.DAY_OF_YEAR);
            final long curTimeMillis = System.currentTimeMillis();
            final long curDateDiff = Math.abs(curTimeMillis - resultTime.getTimeInMillis());
            
            final Calendar tmpCalendar = (Calendar) resultTime.clone();
            tmpCalendar.add(Calendar.YEAR, -10);
            tmpCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            final long minDate = tmpCalendar.getTimeInMillis();
            final long minDateDiff = Math.abs(curTimeMillis - minDate);

            tmpCalendar.add(Calendar.YEAR, 20);
            tmpCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            final long maxDate = tmpCalendar.getTimeInMillis();
            final long maxDateDiff = Math.abs(curTimeMillis - maxDate);
                        
            final long minDiff = Math.min(Math.min(maxDateDiff, curDateDiff), minDateDiff);
            if (minDiff == maxDateDiff) {
                resultTime.setTimeInMillis(maxDate);
            } else if (minDiff == minDateDiff) {
                resultTime.setTimeInMillis(minDate);
            }
        } else { //pattern contains 'yy' (RADIX-6601)
            final Calendar curTime = Calendar.getInstance();
            final int divisor = 100;
            final int century = curTime.get(Calendar.YEAR) / divisor;
            final int remainder = resultTime.get(Calendar.YEAR) % divisor;
            resultTime.set(Calendar.YEAR, century * divisor + remainder);
        }
        return resultTime.getTime();
    }

    public String getString(DateTime val) {
        if (format == DateTimeFormatDef.STR) {
            initTimeZone();
            String formatStr = formatPattern.format(val.getDateValue());
            if (isSingleYearPattern) {
                formatStr = formatStr.substring(formatStr.length() - SINGLE_YEAR_PATTERN.length());
            }
            return formatStr;
        }
        if (format == DateTimeFormatDef.JAVA) {
            java.util.Date d = val.getDateValue();
            return Long.toString(d.getTime());
        }
        if (format == DateTimeFormatDef.FLORA) {
            java.util.Date dtime = val.getDateValue();
            return String.valueOf(FloraTime.getForValue(dtime));
        }
        return null;
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        DateTime v = null;
        try {
            v = DateTime.Factory.parse(obj.getDomNode());
        } catch (XmlException ex) {
            throw new SmioException("Wrong date format", ex);
        }
        return mergeFromString(getString(v));
    }

    @Override
    public XmlObject getDefaultVal() {
        return defaultVal;
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        try {
            if (format == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Date Time format not defined'"));
            }
            if (format != null && format == DateTimeFormatDef.STR && formatPattern == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' error: 'Format pattern not defined'"));
            }
        } catch (Throwable ex) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "error: '" + ex.getMessage() + "'"));
        }
    }
    
    public static boolean isCorrectTimeZoneId(String id) {
        return AVAILABLE_TIME_ZONE_IDS.contains(id);
    }
}
