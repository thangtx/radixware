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

package org.radixware.kernel.common.compiler.core;

import java.util.EnumSet;
import java.util.Set;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public class AdsLookup {

    public static final char[][] RADIX_TYPES_PACKAGE = new char[][]{
        "org".toCharArray(),
        "radixware".toCharArray(),
        "kernel".toCharArray(),
        "common".toCharArray(),
        "types_synthetic".toCharArray()};
    public static final Id RADIX_KERNEL_COMMON = Id.Factory.loadFrom("mdlCommon");
    public static final char[] RADIX_INT = "Int".toCharArray();
    public static final char[] RADIX_NUM = "Num".toCharArray();
    public static final char[] RADIX_STR = "Str".toCharArray();
    public static final char[] RADIX_CHAR = "Char".toCharArray();
    public static final char[] RADIX_BOOL = "Bool".toCharArray();
    public static final char[] RADIX_DATE_TIME = "DateTime".toCharArray();
    public static final Set<EValType> RADIX_TYPE_IDs = EnumSet.of(EValType.INT, EValType.NUM, EValType.STR, EValType.CHAR, EValType.BOOL, EValType.DATE_TIME);
    public static final char[][] RADIX_TYPES = {
        RADIX_BOOL,
        RADIX_CHAR,
        RADIX_DATE_TIME,
        RADIX_INT,
        RADIX_NUM,
        RADIX_STR
    };
    public static final char[][] JAVA_MATH_BIGDECIMAL = new char[][]{TypeConstants.JAVA, "math".toCharArray(), "BigDecimal".toCharArray()};
    public static final char[][] JAVA_SQL_TIMESTAMP = new char[][]{TypeConstants.JAVA, "sql".toCharArray(), "Timestamp".toCharArray()};
    public static final char[][] RADIX_TYPE_MIRRORS = {
        TypeConstants.JAVA_LANG_BOOLEAN[2],
        TypeConstants.JAVA_LANG_CHARACTER[2],
        JAVA_SQL_TIMESTAMP[2],
        TypeConstants.JAVA_LANG_LONG[2],
        JAVA_MATH_BIGDECIMAL[2],
        TypeConstants.JAVA_LANG_STRING[2]
    };
    public static final char[][][] RADIX_TYPE_MIRRORS_FULL = {
        TypeConstants.JAVA_LANG_BOOLEAN,
        TypeConstants.JAVA_LANG_CHARACTER,
        JAVA_SQL_TIMESTAMP,
        TypeConstants.JAVA_LANG_LONG,
        JAVA_MATH_BIGDECIMAL,
        TypeConstants.JAVA_LANG_STRING
    };
    public static final char[] RADIX_INT_ENUM_LOCAL = IKernelIntEnum.class.getSimpleName().toCharArray();
    public static final char[] RADIX_STR_ENUM_LOCAL = IKernelStrEnum.class.getSimpleName().toCharArray();
    public static final char[] RADIX_CHAR_ENUM_LOCAL = IKernelCharEnum.class.getSimpleName().toCharArray();
    public static final char[][] RADIX_INT_ENUM = {
        RADIX_TYPES_PACKAGE[0],
        RADIX_TYPES_PACKAGE[1],
        RADIX_TYPES_PACKAGE[2],
        RADIX_TYPES_PACKAGE[3],
        RADIX_TYPES_PACKAGE[4],
        RADIX_INT_ENUM_LOCAL
    };
    public static final char[][] RADIX_STR_ENUM = {
        RADIX_TYPES_PACKAGE[0],
        RADIX_TYPES_PACKAGE[1],
        RADIX_TYPES_PACKAGE[2],
        RADIX_TYPES_PACKAGE[3],
        RADIX_TYPES_PACKAGE[4],
        RADIX_STR_ENUM_LOCAL
    };
    public static final char[][] RADIX_CHAR_ENUM = {
        RADIX_TYPES_PACKAGE[0],
        RADIX_TYPES_PACKAGE[1],
        RADIX_TYPES_PACKAGE[2],
        RADIX_TYPES_PACKAGE[3],
        RADIX_TYPES_PACKAGE[4],
        RADIX_CHAR_ENUM_LOCAL
    };

    public static int isRadixSystemType(char[] typeName) {
        switch (typeName.length) {
            case 3:
                switch (typeName[0]) {
                    case 'I':
                        return typeName[1] == 'n'
                                && typeName[2] == 't'
                                ? 3 : -1;
                    case 'N':
                        return typeName[1] == 'u'
                                && typeName[2] == 'm'
                                ? 4 : -1;
                    case 'S':
                        return typeName[1] == 't'
                                && typeName[2] == 'r'
                                ? 5 : -1;
                }
                break;
            case 4:
                switch (typeName[0]) {
                    case 'C':
                        return typeName[1] == 'h'
                                && typeName[2] == 'a'
                                && typeName[3] == 'r'
                                ? 1 : -1;
                    case 'B':
                        return typeName[1] == 'o'
                                && typeName[2] == 'o'
                                && typeName[3] == 'l'
                                ? 0 : -1;
                }
                break;
            case 8:
                switch (typeName[0]) {
                    case 'D':
                        return typeName[1] == 'a'
                                && typeName[2] == 't'
                                && typeName[3] == 'e'
                                && typeName[4] == 'T'
                                && typeName[5] == 'i'
                                && typeName[6] == 'm'
                                && typeName[7] == 'e' ? 2 : -1;
                }

        }

        return -1;
    }
}
