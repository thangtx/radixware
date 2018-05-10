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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;


public final class RadixType extends AdsType {

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        //do nothing
    }

    public static final class Factory {

        public static final AdsType newInstance(EValType typeId) {
            if (typeId == null) {
                return null;
            }
            switch (typeId) {
                case PARENT_REF:
                    return ParentRefType.Factory.getDefault();
                case ARR_REF:
                    return ArrRefType.Factory.getDefault();
                case OBJECT:
                    return ObjectType.Factory.getDefault();
                case XML:
                    return XmlType.Factory.getDefault();
                case JAVA_TYPE:
                    return null;
                case USER_CLASS:
                    return null;
                case JAVA_CLASS:
                    return null;
                case NATIVE_DB_TYPE:
                    return null;
                //case ARR_OBJECT:
                //    return null;
                default:
                    return new RadixType(typeId);
            }
        }
    }
    private final EValType typeId;

    private RadixType(EValType typeId) {
        this.typeId = typeId;
    }

    public EValType getTypeId() {
        return typeId;
    }

    @Override
    public String getName() {
        return typeId.getName();
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName();
    }
    private TypeJavaSourceSupport support = null;
    private static final char[][] LOCAL_TYPE_NAME = new char[][]{
        "ArrBin".toCharArray(),
        "ArrBlob".toCharArray(),
        "ArrBool".toCharArray(),
        "ArrChar".toCharArray(),
        "ArrClob".toCharArray(),
        "ArrDateTime".toCharArray(),
        "ArrInt".toCharArray(),
        "ArrNum".toCharArray(),
        "ArrStr".toCharArray(),
        "Bin".toCharArray(),
        "Blob".toCharArray(),
        "Bool".toCharArray(),
        "Character".toCharArray(),
        "Clob".toCharArray(),
        "Timestamp".toCharArray(),
        "Int".toCharArray(),
        "Num".toCharArray(),
        "Str".toCharArray(),
    };
    private static final char[][] JAVA_SQL_PACKAGE = new char[][]{"java".toCharArray(), "sql".toCharArray()};

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        synchronized (this) {
            if (support == null) {
                support = new TypeJavaSourceSupport(this) {

                    @Override
                    public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
                        switch (typeId) {
                            case ARR_BIN:
                            case ARR_CHAR:
                            case ARR_NUM:
                            case ARR_BOOL:
                            case ARR_DATE_TIME:
                            case ARR_INT:
                            case ARR_STR:
                            case BIN:
                                return new char[][]{TypeJavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME};
                            case BOOL:
                            case CHAR:
                            case NUM:
                            case INT:
                            case STR:
                                return DEFAULT_PACKAGE;
                            case BLOB:
                                return env.getEnvironment().isClientEnv() ? new char[][]{TypeJavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME} : JAVA_SQL_PACKAGE;
                            case DATE_TIME:
                                return JAVA_SQL_PACKAGE;
                            case CLOB:
                                return env.getEnvironment().isClientEnv() ? DEFAULT_PACKAGE : JAVA_SQL_PACKAGE;
                            default:

                                switch (env.getEnvironment()) {

                                    case EXPLORER:
                                    case WEB:
                                    case COMMON_CLIENT:
                                        return new char[][]{TypeJavaSourceSupport.RADIX_EXPLORER_TYPES_PACKAGE_NAME};
                                    case SERVER:
                                        return new char[][]{TypeJavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME};
                                    case COMMON:
                                        return new char[][]{TypeJavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME};
                                    default:
                                        throw new DefinitionError("Unsupported usage purpose", RadixType.this);
                                }
                        }
                    }

                    @Override
                    public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
                        switch (typeId) {
                            case ARR_BIN:
                                return LOCAL_TYPE_NAME[0];
                            case ARR_BLOB:
                                return LOCAL_TYPE_NAME[1];
                            case ARR_BOOL:
                                return LOCAL_TYPE_NAME[2];
                            case ARR_CHAR:
                                return LOCAL_TYPE_NAME[3];
                            case ARR_CLOB:
                                return LOCAL_TYPE_NAME[4];
                            case ARR_DATE_TIME:
                                return LOCAL_TYPE_NAME[5];
                            case ARR_INT:
                                return LOCAL_TYPE_NAME[6];
                            case ARR_NUM:
                                return LOCAL_TYPE_NAME[7];
                            case ARR_STR:
                                return LOCAL_TYPE_NAME[8];
                            case BIN:
                                return LOCAL_TYPE_NAME[9];
                            case BLOB:
                                return env.getEnvironment().isClientEnv() ? LOCAL_TYPE_NAME[9] : LOCAL_TYPE_NAME[10];
                            case BOOL:
                                return LOCAL_TYPE_NAME[11];
                            case CHAR:
                                return LOCAL_TYPE_NAME[12];
                            case CLOB:
                                return env.getEnvironment().isClientEnv() ? LOCAL_TYPE_NAME[17] : LOCAL_TYPE_NAME[13];
                            case DATE_TIME:
                                return LOCAL_TYPE_NAME[14];
                            case INT:
                                return LOCAL_TYPE_NAME[15];
                            case NUM:
                                return LOCAL_TYPE_NAME[16];
                            case STR:
                                return LOCAL_TYPE_NAME[17];
                            default:
                                throw new DefinitionError("Unsupported type: " + typeId.getName(), RadixType.this);
                        }
                    }
                };
            }
            return support;
        }
    }

    @Override
    public String getToolTip() {
        return "<html><b>Radix Type Reference</b><br>Id: " + typeId.name() + "</html>";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            if (obj instanceof RadixType) {
                return ((RadixType) obj).typeId == typeId;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return this.typeId.hashCode();
    }

    @Override
    public char[] getFullJavaClassName(JavaSourceSupport.UsagePurpose purpose) {
        switch (typeId) {
            case BOOL:
                return JavaType.FixedNameSourceSupport.JAVA_TYPE_BOOLEAN;
            case STR:
                return JavaType.FixedNameSourceSupport.JAVA_TYPE_STRING;
            case CHAR:
                return JavaType.FixedNameSourceSupport.JAVA_TYPE_CHARACTER;
            case INT:
                return JavaType.FixedNameSourceSupport.JAVA_TYPE_LONG;
            case NUM:
                return JavaType.FixedNameSourceSupport.JAVA_TYPE_BIG_DECIMAL;
            case CLOB:
                if (purpose.getEnvironment().isClientEnv()) {
                    return JavaType.FixedNameSourceSupport.JAVA_TYPE_STRING;
                } else {
                    return super.getFullJavaClassName(purpose);
                }
            default:
                return super.getFullJavaClassName(purpose);
        }
    }
}
