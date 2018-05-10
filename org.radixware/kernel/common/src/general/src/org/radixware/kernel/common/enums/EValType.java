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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValTypes;

public enum EValType implements IKernelIntEnum { 

    BOOL(1, "Bool", OracleTypeNames.NUMBER),
    INT(2, "Int", OracleTypeNames.NUMBER),
    CHAR(3, "Char", OracleTypeNames.CHAR),
    NUM(11, "Num", OracleTypeNames.NUMBER),
    IMG(4, "Img", OracleTypeNames.NUMBER),
    DATE_TIME(12, "DateTime", OracleTypeNames.TIMESTAMP),
    STR(21, "Str", OracleTypeNames.VARCHAR2),
    BIN(22, "Bin", OracleTypeNames.RAW),
    CLOB(27, "Clob", OracleTypeNames.CLOB),
    BLOB(28, "Blob", OracleTypeNames.BLOB),
    PARENT_REF(31, "ParentRef", null),
    OBJECT(32, "Object", OracleTypeNames.VARCHAR2),
    NATIVE_DB_TYPE(50, "DbType", null),
    ARR_BOOL(201, "ArrBool", OracleTypeNames.CLOB),
    ARR_INT(202, "ArrInt", OracleTypeNames.CLOB),
    ARR_CHAR(203, "ArrChar", OracleTypeNames.CLOB),
    ARR_NUM(211, "ArrNum", OracleTypeNames.CLOB),
    ARR_DATE_TIME(212, "ArrDateTime", OracleTypeNames.CLOB),
    ARR_BIN(222, "ArrBin", OracleTypeNames.CLOB),
    ARR_CLOB(227, "ArrClob", OracleTypeNames.CLOB),
    ARR_STR(221, "ArrStr", OracleTypeNames.CLOB),
    ARR_BLOB(228, "ArrBlob", OracleTypeNames.CLOB),
    ARR_REF(231, "ArrParentRef", OracleTypeNames.CLOB),
    //ARR_OBJECT(232, "ArrObject"),
    JAVA_CLASS(400, "JavaClass", null),
    JAVA_TYPE(401, "JavaType", null),
    USER_CLASS(411, "UserClass", null),
    XML(451, "Xml", OracleTypeNames.CLOB),
    ANY(2012, "*", null);
    private final Long val;
    private final String title;
    private final String defDbType;

    private EValType(final long val, final String title, final String defDbType) {
        this.val = Long.valueOf(val);
        this.title = title;
        this.defDbType = defDbType;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return title;
    }

    public String getDefaultDbType() {
        return defDbType;
    }

    public static EValType getForValue(final Long val) {
        for (EValType t : EValType.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EPropValType has no item with value: " + String.valueOf(val), val);
    }

    public static EValType getForName(final String name) {
        for (EValType t : EValType.values()) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public EValType getArrayType() {
        switch (this) {
            case BIN:
                return ARR_BIN;
            case BLOB:
                return ARR_BLOB;
            case BOOL:
                return ARR_BOOL;
            case CHAR:
                return ARR_CHAR;
            case CLOB:
                return ARR_CLOB;
            case DATE_TIME:
                return ARR_DATE_TIME;
            case INT:
                return ARR_INT;
            case NUM:
                return ARR_NUM;
            //case OBJECT:
            //    return ARR_OBJECT;
            case PARENT_REF:
                return ARR_REF;
            case STR:
                return ARR_STR;
            default:
                return null;
        }
    }

    public EValType getArrayItemType() {
        switch (this) {
            case ARR_BIN:
                return BIN;
            case ARR_BLOB:
                return BLOB;
            case ARR_BOOL:
                return BOOL;
            case ARR_CHAR:
                return CHAR;
            case ARR_CLOB:
                return CLOB;
            case ARR_DATE_TIME:
                return DATE_TIME;
            case ARR_INT:
                return INT;
            case ARR_NUM:
                return NUM;
            //case ARR_OBJECT:
            //    return OBJECT;
            case ARR_REF:
                return PARENT_REF;
            case ARR_STR:
                return STR;
            default:
                return null;
        }
    }

    /**
     * returns true if type may be applied to column or innate property of class
     */
    public boolean allowedForInnateProperty() {
        return ValTypes.INNATE_PROPERTY_TYPES.contains(this);
    }

    public boolean isAllowedForDdsColumn() {
        return ValTypes.DDS_COLUMN_TYPES.contains(this);
    }

    public boolean isAllowedForInnateProperty() {
        return ValTypes.INNATE_PROPERTY_TYPES.contains(this);
    }

    /**
     * returns true if type may be applied to user property of class
     */
    public boolean isAllowedForUserProperty() {
        return ValTypes.USER_PROPERTY_TYPES.contains(this);
    }

    /**
     * returns true if type may be applied to dynamic property of class
     */
    public boolean isAllowedForDynamicProperty() {
        return ValTypes.DYNAMIC_PROPERTY_TYPES.contains(this);
    }

    /**
     * returns true if type may be applied to presentation property of class
     */
    public boolean isAllowedForPresentationProperty() {
        return ValTypes.PRESENTATION_PROPERTY_TYPES.contains(this);
    }

    /**
     * returns true if type may be applied to method parameter
     */
    public boolean isAllowedForMethodParameter() {
        return ValTypes.METHOD_PARAM_TYPES.contains(this);
    }

    /**
     * Returns is current type an array type or not
     * Array types are 
     * <ul>
     * <li>{@linkplain #ARR_BIN}</li>
     * <li>{@linkplain #ARR_BOOL}</li>
     * <li>{@linkplain #ARR_BLOB}</li>
     * <li>{@linkplain #ARR_CHAR}</li>
     * <li>{@linkplain #ARR_CLOB}</li>
     * <li>{@linkplain #ARR_DATE_TIME}</li>
     * <li>{@linkplain #ARR_INT}</li>
     * <li>{@linkplain #ARR_NUM}</li>
     * <li>{@linkplain #ARR_OBJECT}</li>
     * <li>{@linkplain #ARR_STR}</li>
     * <li>{@linkplain #ARR_REF}</li>
     * </ul>
     */
    public boolean isArrayType() {
        return ValTypes.ARRAY_TYPES.contains(this);
    }

    /**
     * Returns is current type may be used as base type for radix enumeration
     */
    public boolean isEnumAssignableType() {
        return ValTypes.ENUM_ASSIGNABLE_TYPES.contains(this);
    }

    /**
     * Returns is current type may be refined using definition(s)
     */
    public boolean isRefinableType() {
        return ValTypes.REFINABLE_TYPES.contains(this);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
