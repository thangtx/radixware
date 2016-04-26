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
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

public enum EAccess implements IKernelIntEnum {

    //constant values for compiling
    //@Deprecated
    //PUBLISHED(new Long(5), "published"),
    //NONE(new Long(3)),
    PUBLIC(new Long(0), "public", null),
    PROTECTED(new Long(1), "protected", PUBLIC),
    DEFAULT(new Long(4), "internal", PROTECTED),
    PRIVATE(new Long(2), "private", DEFAULT);
    private final Long value;
    private final String name;
    public final EAccess next;
    //constructors

    private EAccess(Long x, String name, EAccess next) {
        value = x;
        this.name = name;
        this.next = next;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
    //public methods

    public boolean isLess(EAccess another) {
        if (another == this) {
            return false;
        }
        switch (this) {
//            case PUBLISHED:
//                return false;
            case PUBLIC:
                return false;//another == EAccess.PUBLISHED;
            case PROTECTED:
                return /*
                         * another == EAccess.PUBLISHED ||
                         */ another == EAccess.PUBLIC;
            case DEFAULT:
                return /*
                         * another == EAccess.PUBLISHED ||
                         */ another == EAccess.PUBLIC || another == EAccess.PROTECTED;
            default:
                return true;
        }
    }

    public static EAccess getForValue(final Long val) {
        for (EAccess e : EAccess.values()) {
            if (val == null) {
                break;
            }
            if (e.value.longValue() == val.longValue()) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EAccess has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EAccess min(EAccess access1, EAccess access2) {
        if (access1 == null || access2 == null) {
            return null;
        }
        return access1.isLess(access2) ? access1 : access2;
    }

    public String getAsStr() {
        switch (this) {
            case PUBLIC:
                return "Public";
            case PROTECTED:
                return "Protected";
            case PRIVATE:
                return "Private";
            case DEFAULT:
                return "Internal";
            default:
                throw new EnumConstantNotPresentException(EAccess.class, this.getName());
        }
    }
}
