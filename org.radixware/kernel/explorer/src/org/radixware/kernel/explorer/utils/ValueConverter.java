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

package org.radixware.kernel.explorer.utils;

import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.trolltech.qt.core.QDate;
import com.trolltech.qt.core.QDateTime;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.gui.QMessageBox;
import java.nio.ByteBuffer;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;

public final class ValueConverter extends org.radixware.kernel.common.client.utils.ValueConverter {
//	Converters

    private static final long timeZoneOffsetMillis = Calendar.getInstance().get(Calendar.DST_OFFSET) + Calendar.getInstance().get(Calendar.ZONE_OFFSET);
    private static final long floraTimeOffsetSec = 2177452800L; // seconds from 1901/01/01 00:00:00
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static QDateTime timestamp2QtTime(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        final int hours = cal.get(Calendar.HOUR_OF_DAY);
        final int minutes = cal.get(Calendar.MINUTE);
        final int seconds = cal.get(Calendar.SECOND);
        final int mills = cal.get(Calendar.MILLISECOND);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final QTime time = new QTime(hours, minutes, seconds, mills);
        final QDate date = new QDate(year, month, day);
        return new QDateTime(date, time);
    }

    public static QDate timestamp2QtDate(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final QDate date = new QDate(year, month, day);
        return date;
    }

    public static Timestamp qtTime2Timestamp(final QDateTime time) {
        if (time == null) {
            return null;
        }
        int hours = time.time().hour();
        int minutes = time.time().minute();
        int seconds = time.time().second();
        int mills = time.time().msec();
        int year = time.date().year();
        int month = time.date().month() - 1;
        int day = time.date().day();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day, hours, minutes, seconds);
        cal.set(Calendar.MILLISECOND, mills);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    public static Timestamp qtDate2Timestamp(final QDate date){
        if (date == null) {
            return null;
        }
        int year = date.year();
        int month = date.month() - 1;
        int day = date.day();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        return new Timestamp(cal.getTimeInMillis());        
    }

    public final static String objVal2SqlLiteral(final Object val, final EValType valType) {
        if (val == null) {
            return "NULL";
        }
        switch (valType) {
            case INT:
            case NUM:
                return String.valueOf(val);
            case DATE_TIME: {
                final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd.MM.yyyy' 'HH:mm:ss.SSS");
                return "TO_TIMESTAMP('" + sqlDateFormat.format((java.util.Date) val) + "', 'DD.MM.YYYY HH24:MI:SS.FF')";
            }
            case BOOL: {
                if (((Boolean) val).booleanValue()) {
                    return "1";
                } else {
                    return "0";
                }
            }
            case STR:
            case CHAR:
            case OBJECT:
            case PARENT_REF:
            case XML: {
                final String str = (String) val;
                final StringBuilder res = new StringBuilder("'");
                for (int i = 0; i < str.length(); i++) {
                    if (Character.isISOControl(str.charAt(i))) //str.charAt(i) < 32
                    {
                        res.append("' || chr(" + String.valueOf(str.charAt(i)) + ") || '");
                    } else if (str.charAt(i) == '\'') {
                        res.append("''");
                    } else {
                        res.append(str.charAt(i));
                    }
                }
                res.append("'");
                return res.toString();
            }
            default:
                throw new WrongFormatError("Can't convert value to SQL literal: value type \"" + valType.getName() + "\" is not supported in DbpValueConverter.objVal2SqlLiteral()", null);
        }
    }

    public static QMessageBox.StandardButton dialogButtonEnum2QtStandardButton(final EDialogButtonType buttonKind) {
        final QMessageBox.StandardButton button;
        switch (buttonKind) {
            case YES:
                button = QMessageBox.StandardButton.Yes;
                break;
            case NO:
                button = QMessageBox.StandardButton.No;
                break;
            case OK:
                button = QMessageBox.StandardButton.Ok;
                break;
            case CANCEL:
                button = QMessageBox.StandardButton.Cancel;
                break;
            case ABORT:
                button = QMessageBox.StandardButton.Abort;
                break;
            case RETRY:
                button = QMessageBox.StandardButton.Retry;
                break;
            case IGNORE:
                button = QMessageBox.StandardButton.Ignore;
                break;
            case SKIP:
                button = QMessageBox.StandardButton.Escape;
                break;
            case ALL:
                button = QMessageBox.StandardButton.YesToAll;
                break;
            case OPEN:
                button = QMessageBox.StandardButton.Open;
                break;
            case SAVE:
                button = QMessageBox.StandardButton.Save;
                break;
            case CLOSE:
                button = QMessageBox.StandardButton.Close;
                break;
            case HELP:
                button = QMessageBox.StandardButton.Help;
                break;
            default:
                throw new WrongFormatError("Cannot convert from ButtonEnum \"" + buttonKind.toString() + "\" into qt StandatdButton");
        }
        return button;
    }

    public static QMessageBox.Icon getQtDialogTypeByDialogTypeEnum(final EDialogType dialogType) {
        final QMessageBox.Icon icon;
        switch (dialogType) {
            case INFORMATION:
                icon = QMessageBox.Icon.Information;
                break;
            case CONFIRMATION:
                icon = QMessageBox.Icon.Question;
                break;
            case WARNING:
                icon = QMessageBox.Icon.Warning;
                break;
            case ERROR:
                icon = QMessageBox.Icon.Critical;
                break;
            default:
                icon = QMessageBox.Icon.NoIcon;
        }
        return icon;
    }
    @Deprecated
    public static byte[] str2ArrByte(final String s) {

        String a[] = ClientValueFormatter.split(s, ' '); // s.split(" ");
        for (String x : a) {
            if (x.length() != 2) {
                return null;
            }
        }
        byte b[] = new byte[a.length];
        int i = 0;
        for (String x : a) {
            b[i++] = (byte) (int) Integer.valueOf(x, 16);
        }
        return b;
    }
}
