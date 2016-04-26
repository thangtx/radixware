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
public enum ERuntimeEnvironmentType implements IKernelStrEnum {

    EXPLORER("explorer", "Desktop", true),
    SERVER("server", "Server", false),
    COMMON("common", "Common", false),
    COMMON_CLIENT("common-client", "Client-Common", true),
    WEB("web", "Web", true);
    private final String value;
    private final String name;
    private final boolean isClientEnv;

    private ERuntimeEnvironmentType(String value, String name, boolean isClientEnv) {
        this.value = value;
        this.name = name;
        this.isClientEnv = isClientEnv;
    }

    public boolean isClientEnv() {
        return isClientEnv;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ERuntimeEnvironmentType getForValue(final String val) {
        for (ERuntimeEnvironmentType t : ERuntimeEnvironmentType.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESystemComponent has no item with value: " + String.valueOf(val), val);
    }

    public static ERuntimeEnvironmentType getForName(final String name) {
        for (ERuntimeEnvironmentType t : ERuntimeEnvironmentType.values()) {
            if (t.getName() == null && name == null || t.getName().equals(name)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("ESystemComponent has no item with name: " + String.valueOf(name), name);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static boolean compatibility(ERuntimeEnvironmentType prototype, ERuntimeEnvironmentType candidate) {
        if (prototype == null || candidate == null) {
            return false;
        }
        if (candidate == prototype) {
            return true;
        }
        switch (candidate) {
            case COMMON:
                return true;
            case COMMON_CLIENT:
                return prototype == ERuntimeEnvironmentType.EXPLORER || prototype == ERuntimeEnvironmentType.WEB;
            default:
                return false;
        }
    }
}
