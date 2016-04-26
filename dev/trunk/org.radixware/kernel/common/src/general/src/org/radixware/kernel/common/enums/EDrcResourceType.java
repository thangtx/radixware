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

public enum EDrcResourceType implements IKernelIntEnum {

    
    CONTEXTLESS_COMMAND(5),
    EDITOR_PRESENTATION(3),
    EXPLORER_ROOT_ITEM(2),
    SELECTOR_PRESENTATION(4),
    SERVER_RESOURCE(1);
    private Long val;

    private EDrcResourceType(long val) {
        this.val = Long.valueOf(val);
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EDrcResourceType getForValue(final long val) {
        for (EDrcResourceType e : EDrcResourceType.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDrcResourceType has no item with value: " + String.valueOf(val),val);
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
