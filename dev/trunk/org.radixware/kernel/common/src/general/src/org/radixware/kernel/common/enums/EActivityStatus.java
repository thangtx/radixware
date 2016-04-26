/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EActivityStatus implements IKernelStrEnum {
    RUNNING("Running", "Running"),
    INACTIVE("Inactive", "Inactive"),
    STOPPED("Stopped", "Stopped");
    
    private final String name;
    private final String value;

    private EActivityStatus(final String name, final String value) {
        this.name = name;
        this.value = value;
    }    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static EActivityStatus getForValue(final String val) {
        for (EActivityStatus e : EActivityStatus.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        } 
        throw new NoConstItemWithSuchValueError("EActivityStatus has no item with value: " + val, val);
    }
}
