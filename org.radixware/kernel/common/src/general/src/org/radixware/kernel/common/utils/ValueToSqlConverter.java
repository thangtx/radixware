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

import java.math.BigDecimal;
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
    static final String SQL_DATE_FORMAT_4ORACLE = "DD.MM.YYYY HH24:MI:SS.FF";
    
    private static final String SQL_DATE_FORMAT = "dd.MM.yyyy' 'HH:mm:ss.SSS";
    private static final String NULL = "NULL";
    private static final String FALSE = "0";
    private static final String TRUE = "1";
    private static final String BLOB_DATA = "<BLOB-DATA>";
    private static final String CLOB_DATA = "<CLOB-DATA>";
    private static final String BINARY_DATA = "<BINARY-DATA>";

    /**
     * Convert char into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequence. Returs 4 chars NULL for null.
     */
    protected static String charToSql(Character c) {
        if (c == null) {
            return NULL;
        }
        else if (c == '\'') {
            return "''''";
        } else if (c == '&' || Character.isISOControl(c)) { // & - input value in TOAD and SQL Developer.
            return "chr(" + (int) c + ")";
        } else {
            return "'" + c + "'";
        }
    }

    /**
     * Convert string into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Returns 4 chars NULL for null.
     */
    protected static String strToSql(String str) {
        if (str == null) { // возможно в будущем ф-я станет public
            return NULL;
        }
        else {
            
            for (int i = 0, maxI = str.length(); i < maxI; i++) {
                char c = str.charAt(i);
                
                if (c == '\'' || c == '&' || Character.isISOControl(c)) {
                    final StringBuilder res = new StringBuilder("'").append(str,0,i);
                    
                    for (; i < maxI; i++) {
                        c = str.charAt(i);
                        if (c == '\'') {
                            res.append("''");
                        } else if (c == '&' || Character.isISOControl(c)) {              // & - input value in TOAD and SQL Developer.
                            res.append("' || chr(" + (int) c + ") || '");
                        } else {
                            res.append(c);
                        }
                    }
                    return res.append("'").toString();
                }
            }
            return '\''+str+'\'';
        }
    }

    // TODO: support of other value types.
    /**
     * Convert object into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Trunc date to seconds. Returs 4
     * chars NULL for null.
     */
    public static String toSql(final Object val, final EValType valType) {
        if (valType == null) {
            throw new IllegalArgumentException("Value type can't be null!");
        }
        else if (val == null) {
            return NULL;
        }
        else {
            switch (valType) {
                case BOOL:
                    if (val instanceof Boolean) {
                        return (((Boolean) val).booleanValue() ? TRUE : FALSE);
                    }
                    else {
                        throw new IllegalArgumentException("Object type ["+val.getClass()+"] is uncompatible with value type ["+valType.getName()+"]. 'Boolean' type awaited!");
                    }
                case INT:
                case NUM:
                    return String.valueOf(val);
                case DATE_TIME:
                    if (val instanceof java.util.Date) {
                        return "TO_TIMESTAMP('" + new SimpleDateFormat(SQL_DATE_FORMAT).format((java.util.Date) val) + "', '"+SQL_DATE_FORMAT_4ORACLE+"')";
                    }
                    else {
                        throw new IllegalArgumentException("Object type ["+val.getClass()+"] is uncompatible with value type ["+valType.getName()+"]. 'java.util.Date' type awaited!");
                    }
                case CHAR:
                    if (val instanceof Character) {
                        return charToSql((Character) val);
                    }
                    else {
                        throw new IllegalArgumentException("Object type ["+val.getClass()+"] is uncompatible with value type ["+valType.getName()+"]. 'Character' type awaited!");
                    }
                case STR:
                case OBJECT:
                case PARENT_REF:
                case XML:
                    if (val instanceof String) {
                        return strToSql((String) val);
                    }
                    else {
                        throw new IllegalArgumentException("Object type ["+val.getClass()+"] is uncompatible with value type ["+valType.getName()+"]. 'String' type awaited!");
                    }
                case ARR_STR:
                case ARR_CHAR:
                case ARR_NUM:
                case ARR_BOOL:
                case ARR_BIN:
                case ARR_INT:
                case ARR_DATE_TIME:
                case ARR_REF:
                    return strToSql(val.toString());
                default:
                    throw new IllegalStateException("Unsupported by ObjectToSqlConverter value type (" + valType.getName() + ")");
            }
        }
    }

    /**
     * Convert object into SQL literal. Usually - quite. Additional - special
     * characters replaced by ESC sequences. Works like
     * {@linkplain #toSql(java.lang.Object, org.radixware.kernel.common.enums.EValType)}
     * with exception on blob and clob values Trunc date to seconds. Returs 4
     * chars NULL for null.
     */
    public static String toSqlDebug(final Object val, final EValType valType) {
        if (valType == null) {
            throw new IllegalArgumentException("Value type can't be null!");
        }
        else if (val == null) {
            return NULL;
        }
        else {
            switch (valType) {
                case BLOB   : return BLOB_DATA;
                case CLOB   : return CLOB_DATA;
                case BIN    : return BINARY_DATA;
                default     : return toSql(val, valType);
            }
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
        if (valType == null) {
            throw new IllegalArgumentException("Value type can't be null!");
        }
        else if (valAsStr == null) {
            return NULL;
        } else {
            return toSql(valAsStr.toObject(valType), valType);
        }
    }

    public static String toSqlDebug(ValAsStr valAsStr, EValType valType) {
        if (valType == null) {
            throw new IllegalArgumentException("Value type can't be null!");
        }
        else if (valAsStr == null) {
            return NULL;
        } else {
            return toSqlDebug(valAsStr.toObject(valType), valType);
        }
    }
}
