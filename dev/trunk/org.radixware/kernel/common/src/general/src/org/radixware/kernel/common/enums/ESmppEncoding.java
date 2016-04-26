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

public enum ESmppEncoding implements IKernelStrEnum {

    //constant values for compiling
    ALPHABET("Alphabet"),
    ASCII("ASCII"),
    BINARY("Binary"),
    HPROMAN8("HPRoman8"),
    LATIN("Latin"),
    UCS2("UCS2"),
    UTF16_LittleEndian("UTF16_LittleEndian"),
    UTF16_BigEndian("UTF16_BigEndian");

    private final String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }
    //constructors   

    private ESmppEncoding(String x) {
        value = x;
    }
    //public methods

    public static ESmppEncoding getForValue(final String val) {
        for (ESmppEncoding t : ESmppEncoding.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        return null;
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