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
import java.util.Calendar;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.SystemPropUtils;

public final class SmioFieldDateTime extends SmioFieldSimple {

    private static final String EXPLICIT_LEAP_YEAR = SystemPropUtils.getStringSystemProp("rdx.msdl.default.year", "1972");

    private SimpleDateFormat formatPattern;
    private DateTime defaultVal;
    private DateTimeFormatDef.Enum format = null;

    public SmioFieldDateTime(DateTimeFieldModel model) throws SmioError {
        super(model);
        try {
            String frm = getField().isSetFormat() ? getField().getFormat() : getModel().getDateTimeFormat(false);
            if (frm != null) {
                format = DateTimeFormatDef.Enum.forString(frm);
                if (format == DateTimeFormatDef.STR) {
                    String pattern = getField().isSetPattern() ? getField().getPattern() : getModel().getDateTimePattern(false);
                    formatPattern = null;
                    try {
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

    @Override
    public boolean getIsBSD() throws SmioException {
        return getCoder().encoding == EncodingDef.BCD;
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

    private Date tryParseDateByMask(final String dateStr) throws ParseException {
        if (dateStr == null) {
            return null;
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
                formatPattern.setLenient(true);
                return formatPattern.parse(dateStr);
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
            try {
                dt.setDateValue(tryParseDateByMask(s));
                String patt = formatPattern.toPattern();
                if (patt.equals("yy")) {
                    //RADIX-6601: set current centuary
                    Calendar cal = Calendar.getInstance();
                    int century = (cal.get(Calendar.YEAR) / 100);
                    Date currDate = dt.getDateValue();
                    cal.setTime(currDate);
                    int yy = cal.get(Calendar.YEAR) % 100;
                    cal.set(Calendar.YEAR, yy + century * 100);
                    dt.setDateValue(cal.getTime());
                }
            } catch (ParseException ex) {
                LogFactory.getLog(SmioFieldDateTime.class).error(null, ex);
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

    public String getString(DateTime val) {
        if (format == DateTimeFormatDef.STR) {
            return formatPattern.format(val.getDateValue());
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
}
