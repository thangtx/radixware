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

package org.radixware.kernel.common.utils;

import java.text.SimpleDateFormat;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

/**
 * Object value of SQL converter, for example, String("str") => 'str'. Supports
 * only objects typified by {@link EValType}.
 *
 */
public class ValueToSqlConverter {

    protected ValueToSqlConverter() {
        super();
    }
    // SimpleDateFormat is not threadsafe
    //private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd.MM.yyyy' 'HH:mm:ss.SSS");
    private static final String SQL_DATE_FORMAT = "dd.MM.yyyy' 'HH:mm:ss.SSS";
    private static final String NULL = "NULL";

    /**
     * Convert char into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequence. Returs 4 chars NULL for null.
     */
    protected static String charToSql(Character c) {
        if (c == null) {
            return NULL;
        }
        if (Character.isISOControl(c) || c == '&') { // & - input value in TOAD and SQL Developer.
            return "chr(" + (int) c + ")";
        } else if (c == '\'') {
            return "''''";
        } else {
            return "'" + c + "'";
        }
    }

    /**
     * Convert string into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Returs 4 chars NULL for null.
     */
    protected static String strToSql(String str) {
        if (str == null) { // возможно в будущем ф-я станет public
            return NULL;
        }
        final StringBuilder res = new StringBuilder("'");
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isISOControl(c) || c == '&') {              // & - input value in TOAD and SQL Developer.
                res.append("' || chr(" + (int) c + ") || '");
            } else if (c == '\'') {
                res.append("''");
            } else {
                res.append(c);
            }
        }
        res.append("'");
        return res.toString();
    }

    // TODO: support of other value types.
    /**
     * Convert object into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Trunc date to seconds. Returs 4
     * chars NULL for null.
     */
    public static String toSql(Object val, EValType valType) {
        if (val == null) {
            return NULL;
        }
        switch (valType) {
            case BOOL:
                return (((Boolean) val).booleanValue() ? "1" : "0");
            case INT:
            case NUM:
                return String.valueOf(val);
            case DATE_TIME:
                return "TO_TIMESTAMP('" + new SimpleDateFormat(SQL_DATE_FORMAT).format((java.util.Date) val) + "', 'DD.MM.YYYY HH24:MI:SS.FF')";
            case CHAR:
                return charToSql((Character) val);
            case STR:
            case OBJECT:
            case PARENT_REF:
            case XML:
                return strToSql((String) val);
            default:
                throw new IllegalStateException("Unsupported by ObjectToSqlConverter value type (" + valType.getName() + ")");
        }
    }

    /**
     * Convert object into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Works like
     * {@linkplain #toSql(java.lang.Object, org.radixware.kernel.common.enums.EValType)}
     * with exception on blob and clob values Trunc date to seconds. Returs 4
     * chars NULL for null.
     */
    public static String toSqlDebug(Object val, EValType valType) {
        if (val == null) {
            return NULL;
        }
        switch (valType) {
            case BLOB:
                return "<BLOB-DATA>";
            case CLOB:
                return "<CLOB-DATA>";
            case BIN:
                return "<BINARY-DATA>";
            default:
                return toSql(val, valType);
        }
    }

    /**
     * Convert specified ValAsStr to SQL literal. Firstly, convert ValueAsStr to
     * object, then call
     * {@linkplain #toSql(java.lang.Object, org.radixware.kernel.common.enums.EValType)}.
     *
     * @return SQL literal or 4 letters NULL if object is null.
     */
    public static String toSql(ValAsStr valAsStr, EValType valType) {
        if (valAsStr == null) {
            return toSql((Object) null, valType);
        } else {
            Object obj = valAsStr.toObject(valType);
            return toSql(obj, valType);
        }
    }

    public static String toSqlDebug(ValAsStr valAsStr, EValType valType) {
        if (valAsStr == null) {
            return toSqlDebug((Object) null, valType);
        } else {
            Object obj = valAsStr.toObject(valType);
            return toSqlDebug(obj, valType);
        }
    }
}
