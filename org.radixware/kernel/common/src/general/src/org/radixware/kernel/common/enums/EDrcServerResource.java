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

public enum EDrcServerResource implements IKernelStrEnum {

    EAS("csr_EAS______________________"),
    EAS_COLORING_CREATION("csr_EAS_COLORING_CREATION____"),
    EAS_FILTER_CREATION("csr_EAS_FILTER_CREATION______"),
    EAS_SERVER_FILES("csr_EAS_SERVER_FILES_________"),
    EAS_SORTING_CREATION("csr_EAS_SORTING_CREATION_____"),
    DEBUG("csr_DEBUG____________________"),
    TRACING("csr_TRACING__________________"),
    VIEW_AUDIT("csr_VIEW_AUDIT_______________"),
    USER_FUNC_DEV("csr_USER_FUNC_DEV____________");
    private final String value;

    private EDrcServerResource(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    public static EDrcServerResource getForValue(final String value) {
        for (EDrcServerResource dbOption : EDrcServerResource.values()) {
            if (dbOption.getValue().equals(value)) {
                return dbOption;
            }
        }
        throw new NoConstItemWithSuchValueError("EDrcServerResource has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public String getAsStr() {
        switch (this) {
            case EAS:
                return "Connect to Explorer Access Service";
            case EAS_COLORING_CREATION:
                return "Selector color schemes creation in Explorer";
            case EAS_FILTER_CREATION:
                return "Filter creation and refining in Explorer";
            case EAS_SERVER_FILES:
                return "Access to server file resources";
            case EAS_SORTING_CREATION:
                return "Sorting creation and refining in Explorer";
            case DEBUG:
                return "Debug";
            case TRACING:
                return "Trace";
            case VIEW_AUDIT:
                return "View audit";
            case USER_FUNC_DEV:
                return "User functions development";
            default:
                return "not defined";
        }
    }
    
    public boolean isDeprecated() {//RADIX-13126
        return EDrcServerResource.EAS_COLORING_CREATION.equals(this)
            || EDrcServerResource.EAS_SERVER_FILES.equals(this)
            || EDrcServerResource.DEBUG.equals(this);
    }
    
}