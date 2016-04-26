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
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;


public enum EFileSelectionMode implements IKernelIntEnum {

    SELECT_FILE(0, "Select files only"),
    SELECT_DIRECTORY(1, "Select directories only");//explorer only
    private final Long selectionMode;
    private final String name;

    private EFileSelectionMode(long mode, String name) {
        this.selectionMode = Long.valueOf(mode);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return selectionMode;
    }

    public static EFileSelectionMode getForValue(final Long val) {
        for (EFileSelectionMode e : EFileSelectionMode.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EFileSelectionMode has no item with value: " + String.valueOf(val), val);
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
