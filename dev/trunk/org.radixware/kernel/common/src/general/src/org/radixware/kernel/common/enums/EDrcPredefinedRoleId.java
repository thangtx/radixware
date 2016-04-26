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

public enum EDrcPredefinedRoleId implements IKernelStrEnum {

    SUPER_ADMIN("rolSUPER_ADMIN_______________"),
    ACCEPTOR(   "rolACCEPTOR__________________")
    ;
    private final String value;

    private EDrcPredefinedRoleId(final String value) {
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

    public static EDrcPredefinedRoleId getForValue(final String value) {
        for (EDrcPredefinedRoleId dbOption : EDrcPredefinedRoleId.values()) {
            if (dbOption.getValue().equals(value)) {
                return dbOption;
            }
        }
        throw new NoConstItemWithSuchValueError("EDrcPredefinedRoleId has no item with value: " + String.valueOf(value),value);
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
