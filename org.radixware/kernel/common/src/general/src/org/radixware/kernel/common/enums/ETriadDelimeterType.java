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

/**
 * Стиль отображения разделителя разрядов в десятичном числе
 *
 */
public enum ETriadDelimeterType implements IKernelIntEnum {

    NONE(0l, "None"),//Do not use delimeter
    DEFAULT(1l, "Use locale defaults"),//Use locale defined delimeter
    SPECIFIED(2l, "Specify");//Use user defined delimeter
    private final Long value;
    private final String name;

    private ETriadDelimeterType(final long value, String name) {
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

    public static ETriadDelimeterType getForValue(final Long val) {
        for (ETriadDelimeterType e : ETriadDelimeterType.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ETriadDelimeterType has no item with value: " + String.valueOf(val), val);
    }
}
