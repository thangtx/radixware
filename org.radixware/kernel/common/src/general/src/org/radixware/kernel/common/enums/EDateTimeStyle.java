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
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

/**
 * Стиль отображения значений типа дата/время
 */
public enum EDateTimeStyle implements IKernelIntEnum {

    NONE(0l, "Do not show"),
    DEFAULT(1l, "Default"),
    SHORT(2l, "Short"),
    MEDIUM(3l, "Medium"),
    LONG(4l, "Long"),
    FULL(5l, "Full"),
    CUSTOM(6l, "User defined");

    private final Long value;
    private final String name;

    private EDateTimeStyle(final long value, String name){
        this.value = Long.valueOf(value);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }

    public static EDateTimeStyle getForValue(final Long val) {
        for (EDateTimeStyle e : EDateTimeStyle.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDateTimeStyle has no item with value: " + String.valueOf(val),val);
    }

    public int getJavaDateTimeStyle(){
        switch (this){
            case DEFAULT:
                return java.text.DateFormat.DEFAULT;
            case SHORT:
                return java.text.DateFormat.SHORT;
            case MEDIUM:
                return java.text.DateFormat.MEDIUM;
            case LONG:
                return java.text.DateFormat.LONG;
            case FULL:
                return java.text.DateFormat.FULL;
            default:
                throw new IllegalUsageError("Cannot convert "+this.getName()+" style to java date style");
        }
    }
}