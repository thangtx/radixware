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

package org.radixware.kernel.common.defs.dds.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.enums.OracleTypeNames;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Some operations on database type.
 *
 */
public class DbTypeUtils {

    /**
     * Remove unnecessary spaces, tabs, and so on characters from database type.
     * Required for database type comparing. For example: "VarChar2 \t\r\n(100
     * char)\n" => "VarChar2(100 char)".
     */
    public static String trimDbType(String dbType) {
        return dbType.trim().replaceAll("\\s+", " ").replaceAll("\\s\\(", "(").replaceAll("\\)\\s", ")");
    }

    /**
     * Compare database types.
     *
     * @return true if types are equals, false otherwise.
     */
    public static boolean isDbTypeEquals(String oldDbType, String newDbType) {
        if (oldDbType.equals(newDbType)) {
            return true;
        }
        String trimedOldDbType = trimDbType(oldDbType);
        String trimedNewDbType = trimDbType(newDbType);
        return trimedOldDbType.equalsIgnoreCase(trimedNewDbType);
    }

    public static boolean isDbTypeConvertable(String oldDbType, String newDbType) {
        if (isDbTypeEquals(oldDbType, newDbType)) {
            return true;
        }
        String trimedOldDbType = trimDbType(oldDbType);
        String trimedNewDbType = trimDbType(newDbType);
        int oldParPos = trimedOldDbType.indexOf("(");
        int newParPos = trimedNewDbType.indexOf("(");

        if (oldParPos != newParPos) {
            return false;
        }
        if (oldParPos <= 0) {
            return false;
        }

        String simpleOldDbType = trimedOldDbType.substring(0, oldParPos).toLowerCase();
        String simpleNewDbType = trimedOldDbType.substring(0, newParPos).toLowerCase();

        if (!Utils.equals(simpleOldDbType, simpleNewDbType)) {
            return false;
        }
        final String oldUnitName = getDbTypeUnitName(trimedOldDbType).toLowerCase();
        final String newUnitName = getDbTypeUnitName(trimedNewDbType).toLowerCase();

        if (!Utils.equals(oldUnitName, newUnitName)) {
            return false;
        }

        return true;
    }

    /**
     * @return true if value type is compatible with specified database type,
     * false otherwise.
     */
    public static boolean isCompatible(EValType valType, String dbType) {
        int pos = dbType.indexOf('(');
        String lowerDbType = dbType.toLowerCase();
        String lowerShortDbType = (pos >= 0 ? lowerDbType.substring(0, pos) : lowerDbType);

        switch (valType) {
            case BLOB:
                return Utils.equals("blob", lowerDbType);
            case BIN:
                return Utils.equals("blob", lowerDbType) || Utils.equals("raw", lowerShortDbType);
            case BOOL:
                return Utils.equals("number(1,0)", lowerDbType);
            case CLOB:
                return Utils.equals("clob", lowerShortDbType);
            case CHAR:
                return Utils.equals("char", lowerShortDbType);
            case DATE_TIME:
                return Utils.equals("date", lowerShortDbType)
                        || Utils.equals("time", lowerShortDbType)
                        || Utils.equals("datetime", lowerShortDbType)
                        || Utils.equals("timestamp", lowerShortDbType);
            case INT:
                return Utils.equals("number", lowerShortDbType);
            case NUM:
                return Utils.equals("number", lowerShortDbType);
            case STR:
                return Utils.equals("varchar2", lowerShortDbType);
            case NATIVE_DB_TYPE:
                return pos < 0;
            case ARR_BLOB:
            case ARR_BIN:
            case ARR_CLOB:
            case ARR_REF:
                return Utils.equals("clob", lowerShortDbType);
            case ARR_BOOL:
            case ARR_CHAR:
            case ARR_DATE_TIME:
            case ARR_INT:
            case ARR_NUM:
            case ARR_STR:
                return Utils.equals("clob", lowerShortDbType)
                        || Utils.equals("varchar2", lowerShortDbType);
            default:
                return false;

        }
    }

    public static int[] getDbTypeLength(String dbType) {
        dbType = trimDbType(dbType);
        int par1 = dbType.indexOf("(");
        if (par1 < 0) {
            return new int[0];
        }
        int par2 = dbType.indexOf(")", par1);
        if (par2 < 0) {
            return new int[0];
        }
        String checkString = dbType.substring(par1 + 1, par2);
        String[] parts = checkString.split(" ");
        if (parts.length > 0) {
            String[] digits = parts[0].split(",");
            if (digits.length > 0) {
                int[] len = new int[digits.length];
                for (int i = 0; i < digits.length; i++) {
                    try {
                        len[i] = Integer.parseInt(digits[i]);
                    } catch (NumberFormatException e) {
                        Logger.getLogger(DbTypeUtils.class.getName()).log(Level.SEVERE, "Invalid number in " + i + "th position in specification of type " + dbType, e);
                        len[i] = 0;
                    }
                }
                return len;
            }
        }
        return new int[0];
    }

    public static String getDbTypeUnitName(String dbType) {
        dbType = trimDbType(dbType);
        int par1 = dbType.indexOf("(");
        if (par1 < 0) {
            return "";
        }
        int par2 = dbType.indexOf(")", par1);
        if (par2 < 0) {
            return "";
        }
        String checkString = dbType.substring(par1 + 1, par2);
        String[] parts = checkString.split(" ");
        if (parts.length > 1) {
            return parts[1];
        }
        return "";
    }

    public static String calcAutoDbType(EValType valType, int length, int precision, Id nativeDbTypeId, DdsModule context) {
        switch (valType) {
            case BOOL:
                return OracleTypeNames.NUMBER + "(1,0)";
            case CHAR:
                return OracleTypeNames.CHAR + "(1 char)";
            case DATE_TIME:
                if (precision != 0) {
                    return OracleTypeNames.TIMESTAMP + "(" + String.valueOf(precision) + ")";
                } else {
                    return OracleTypeNames.DATE;
                }
            case INT:
                if (length > 0) {
                    return OracleTypeNames.NUMBER + "(" + String.valueOf(length) + ",0)";
                } else {
                    return OracleTypeNames.NUMBER + "(*,0)";
                }
            case NUM:
                if (length > 0 && precision > 0) {
                    return OracleTypeNames.NUMBER + "(" + String.valueOf(length) + "," + String.valueOf(precision) + ")";
                } else if (length > 0) {
                    return OracleTypeNames.NUMBER + "(" + String.valueOf(length) + ")";
                } else if (precision > 0) {
                    return OracleTypeNames.NUMBER + "(*," + String.valueOf(precision) + ")";
                } else {
                    return OracleTypeNames.NUMBER;
                }
            case BLOB:
                return OracleTypeNames.BLOB;
            case BIN:
                // LONG RAW not required yet.
                return OracleTypeNames.RAW + "(" + String.valueOf(length) + ")";
            case STR:
                return OracleTypeNames.VARCHAR2 + "(" + String.valueOf(length) + " char)";
            case CLOB:
                return OracleTypeNames.CLOB;
            case ARR_BLOB:
            case ARR_BIN:
            case ARR_BOOL:
            case ARR_CLOB:
            case ARR_CHAR:
            case ARR_DATE_TIME:
            case ARR_INT:
            case ARR_NUM:
            case ARR_STR:
            case ARR_REF:
                if (length > 0) {
                    return OracleTypeNames.VARCHAR2 + "(" + String.valueOf(length) + " char)";
                } else {
                    return OracleTypeNames.CLOB;
                }
            case NATIVE_DB_TYPE:
                if (context == null) {
                    return "";
                }
                final DdsTypeDef nativeDbType = context.getDdsTypeSearcher().findById(nativeDbTypeId).get();
                return (nativeDbType != null ? nativeDbType.getDbName() : "");
            default:
                return "";
        }
    }
}
