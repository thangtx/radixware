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

/**
 * Radix ads types packages components
 */
public enum ERadixApplication implements IKernelStrEnum {

    EXPLORER("explorer", "Explorer"),
    SERVER("server", "Server"),
    REPORT_EDITOR("report-editor", "ReportEditor"),
    COMMON("common", "Common"),
    KEYSTORE_ADMIN("keyStoreAdmin", "KeyStoreAdmin");
    private final String value;
    private final String name;

    private ERadixApplication(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ERadixApplication getForValue(final String val) {
        for (ERadixApplication t : ERadixApplication.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ERadixApplication has no item with value: " + String.valueOf(val), val);
    }

    public static ERadixApplication getForName(final String name) {
        for (ERadixApplication t : ERadixApplication.values()) {
            if (t.getName() == null && name == null || t.getName().equals(name)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ERadixApplication has no item with name: " + String.valueOf(name), name);
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }
}
