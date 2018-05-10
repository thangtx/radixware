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
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author dsafonov
 */
public enum EAadcMember implements IKernelIntEnum {

    ANY(0l),
    FIRST(1l),
    SECOND(2l);

    private final Long value;

    private EAadcMember(Long x) {
        value = x;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Long getValue() {
        return value;
    }

    public static EAadcMember getForValue(final Long val) {
        for (EAadcMember e : EAadcMember.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EAadcMember has no item with value: " + String.valueOf(val), val);
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
