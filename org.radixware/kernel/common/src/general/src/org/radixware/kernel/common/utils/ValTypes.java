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

import java.util.*;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.*;

/**
 * Collections of EValType for comboboxes in editors.
 *
 */
public final class ValTypes {

    private ValTypes() {
    }
    public static final Set<EValType> ARRAY_TYPES = Collections.unmodifiableSet(EnumSet.of(
            ARR_BIN, ARR_BLOB, ARR_BOOL, ARR_CHAR, ARR_CLOB, ARR_DATE_TIME, ARR_INT, ARR_NUM, /*
             * ARR_OBJECT,
             */ ARR_STR, ARR_REF));
    public static final Set<EValType> ENUM_ASSIGNABLE_TYPES = Collections.unmodifiableSet(EnumSet.of(
            INT, STR, CHAR, ARR_INT, ARR_STR, ARR_CHAR));
    public static final Set<EValType> ENUM_TYPES = Collections.unmodifiableSet(EnumSet.of(
            INT, STR, CHAR));
    public static final Set<EValType> REFINABLE_TYPES = Collections.unmodifiableSet(EnumSet.of(
            INT, STR, CHAR, ARR_INT, ARR_STR, ARR_CHAR, ARR_REF, /*
             * ARR_OBJECT,
             */ PARENT_REF, OBJECT, USER_CLASS));
    public static final Set<EValType> INNATE_PROPERTY_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_REF));
    public static final Set<EValType> FORM_PROPERTY_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_REF, PARENT_REF, XML));
    public static final Set<EValType> DDS_COLUMN_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_REF, NATIVE_DB_TYPE));
    public static final Set<EValType> USER_PROPERTY_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB, PARENT_REF, OBJECT,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_BLOB, ARR_CLOB, ARR_REF, XML));
    public static final Set<EValType> DYNAMIC_PROPERTY_TYPES;

    static {
        EnumSet dt = EnumSet.allOf(EValType.class);
        dt.remove(EValType.OBJECT);
        DYNAMIC_PROPERTY_TYPES = Collections.unmodifiableSet(dt);

        EnumSet enumFieldTypes = EnumSet.allOf(EValType.class);
        enumFieldTypes.removeAll(Arrays.asList(ANY, CLOB, BLOB, PARENT_REF, OBJECT, NATIVE_DB_TYPE));
        ADS_ENUM_CLASS_FIELD_TYPES = Collections.unmodifiableSet(enumFieldTypes);
    }
    public static final Set<EValType> PRESENTATION_PROPERTY_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB, PARENT_REF, XML, OBJECT,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_BLOB, ARR_CLOB, ARR_REF));
    public static final Set<EValType> EAS_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, CHAR, STR, NUM, DATE_TIME, BIN, BLOB, CLOB, XML,
            ARR_BOOL, ARR_INT, ARR_CHAR, ARR_STR, ARR_NUM, ARR_DATE_TIME, ARR_BIN, ARR_BLOB, ARR_CLOB));
    public static final Set<EValType> METHOD_PARAM_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BIN, BLOB, BOOL, CHAR, CLOB, DATE_TIME, INT, JAVA_CLASS, JAVA_TYPE, NUM, STR, USER_CLASS, XML,
            ARR_BIN, ARR_BLOB, ARR_BOOL, ARR_CHAR, ARR_CLOB, ARR_DATE_TIME, ARR_INT, ARR_NUM, ARR_REF, ARR_STR));
    public static final Set<EValType> DDS_FUNCTION_RESULT_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, STR, NUM, DATE_TIME, NATIVE_DB_TYPE));
    public static final Set<EValType> DDS_FUNCTION_PARAM_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, STR, NUM, DATE_TIME, NATIVE_DB_TYPE));
    public static final Set<EValType> ADS_SQL_CLASS_PARAM_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, NUM, CHAR, STR, DATE_TIME, CLOB, BLOB, BIN,
            ARR_BOOL, ARR_INT, ARR_NUM, ARR_CHAR, ARR_STR, ARR_DATE_TIME, ARR_CLOB, ARR_BLOB, ARR_BIN,
            PARENT_REF, ARR_REF));
    public static final Set<EValType> ADS_PARAMETER_TAG_PROP_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, NUM, CHAR, STR, DATE_TIME));
    public static final Set<EValType> ADS_SQL_CLASS_FIELD_TYPES = Collections.unmodifiableSet(EnumSet.of(
            BOOL, INT, NUM, CHAR, STR, DATE_TIME, CLOB, BLOB,
            PARENT_REF, ARR_REF));
    public static final Set<EValType> ADS_ENUM_CLASS_FIELD_TYPES;
}
