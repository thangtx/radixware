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


public enum EEditMaskType implements IKernelIntEnum {

    ENUM(1, "EditMaskConstSet"),
    INT(2, "EditMaskInt"),
    //NONE(3),
    NUM(4, "EditMaskNum"),
    LIST(5, "EditMaskList"),
    STR(6, "EditMaskStr"),
    DATE_TIME(7, "EditMaskDateTime"),
    TIME_INTERVAL(8, "EditMaskTimeInterval"),
    BOOL(9, "EditMaskBool"),
    FILE_PATH(10, "EditMaskFilePath"),
    OBJECT_REFERENCE(11, "EditMaskRef"),;
    private Long val;
    private String name;

    private EEditMaskType(long val, String name) {
        this.val = Long.valueOf(val);
        this.name = name;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EEditMaskType getForValue(final long val) {
        for (EEditMaskType e : EEditMaskType.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EEditMaskType has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public String getClassName() {
        return name;
    }

    public String getAsStr() {
        switch (this) {
            case ENUM:
                return "Enumeration";
            case INT:
                return "Integer number";
            case NUM:
                return "Real number";
            case LIST:
                return "List";
            case STR:
                return "String";
            case DATE_TIME:
                return "Date/time";
            case TIME_INTERVAL:
                return "Time interval";
            case BOOL:
                return "Boolean";
            case FILE_PATH:
                return "File path";
            case OBJECT_REFERENCE:
                return "Object reference";
            default:
                return "not defined";
        }
    }
}
