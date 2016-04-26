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
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum ERadixIconType implements IKernelStrEnum {

    // order defined priority for image searching by ID
    SVG("svg"),
    PNG("png"),
    GIF("gif"),
    JPG("jpg"),
    // BMP("bmp"), Reading Microsoft Windows bitmap files is not officially supported in JDK 1.6: http://www.javaworld.com/javaworld/javatips/jw-javatip43.html
    UNKNOWN("img"); // unknown format, extension must be IMG to save somehow.
    private final String value;

    private ERadixIconType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return super.name();
    }

    public static ERadixIconType getForValue(final String val) {
        for (ERadixIconType e : ERadixIconType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(ERadixIconType.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
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

