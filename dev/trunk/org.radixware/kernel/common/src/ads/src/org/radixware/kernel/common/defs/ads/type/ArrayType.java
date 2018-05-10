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

import java.util.Map;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.Utils;


public class ArrayType extends AdsType {

    private AdsType itemType;
    private int dim;

    public ArrayType(AdsType sourceType, int dim) {
        this.itemType = sourceType;
        this.dim = dim;
    }

    public AdsType getItemType() {
        return this.itemType;
    }
    public int getDimensions() {
        return this.dim;
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new TypeJavaSourceSupport(this) {

            @Override
            public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
                if (itemType != null) {
                    return itemType.getJavaSourceSupport().getPackageNameComponents(env, isHumanReadable);
                } else {
                    return TypeJavaSourceSupport.DEFAULT_PACKAGE;
                }
            }

            @Override
            public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
                if (itemType != null) {
                    char[] suffix = getDimensionSuffix();
                    return CharOperations.merge(itemType.getJavaSourceSupport().getLocalTypeName(env, isHumanReadable), suffix);
                } else {
                    return "???".toCharArray();
                }
            }

            @Override
            public char[] getQualifiedTypeName(UsagePurpose env, char separator, boolean isHumanReadable) {
                if (itemType != null) {
                    char[] suffix = getDimensionSuffix();
                    return CharOperations.merge(itemType.getJavaSourceSupport().getQualifiedTypeName(env, separator, false), suffix);
                } else {
                    return "???".toCharArray();
                }
            }

            @Override
            public char[] getQualifiedTypeName(UsagePurpose env, boolean isHumanReadable) {
                if (itemType != null) {
                    char[] suffix = getDimensionSuffix();
                    return CharOperations.merge(itemType.getJavaSourceSupport().getQualifiedTypeName(env, isHumanReadable), suffix);
                } else {
                    return "???".toCharArray();
                }
            }
        };
    }

    @Override
    public String getName() {
        if (itemType != null) {
            return itemType.getName()/*+  new String(getDimensionSuffix())*/;
        } else {
            return "<Undefined>"/*+ new String(getDimensionSuffix())*/;
        }
    }

    private char[] getDimensionSuffix() {
        char[] suffix = new char[dim * 2];
        for (int i = 0, len = dim * 2; i < len; i += 2) {
            suffix[i] = '[';
            suffix[i + 1] = ']';
        }
        return suffix;
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        if (itemType != null) {
            return itemType.getQualifiedName(context)/* + new String(getDimensionSuffix())*/;
        } else {
            return "<Undefined>" /*+ new String(getDimensionSuffix())*/;
        }
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler, Map<Object, Object> checkHistory) {
        if (itemType != null) {
            itemType.check(referenceContext, environment, problemHandler, checkHistory);
        } else {
            problemHandler.accept(RadixProblem.Factory.newError(referenceContext, "Indefined item type"));
        }
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler) {
        check(referenceContext, environment, problemHandler, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            if (obj instanceof ArrayType) {
                ArrayType other = (ArrayType) obj;
                if (dim == other.dim) {
                    return Utils.equals(itemType, other.itemType);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.itemType != null ? this.itemType.hashCode() : 0);
        hash = 17 * hash + this.dim;
        return hash;
    }

//    @Override
//    public boolean isSubclassOf(AdsType type) {
//        if (type instanceof ArrayType) {
//            ArrayType arrayType = (ArrayType) type;
//            if (dim == arrayType.dim) {
//                return itemType.isSubclassOf(arrayType.itemType);
//            }
//        }
//        return false;
//    }
}
