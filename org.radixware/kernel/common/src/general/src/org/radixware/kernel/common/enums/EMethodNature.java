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

public enum EMethodNature implements IKernelIntEnum {

    ALGO_METHOD(12),
    ALGO_START(11),
    ALGO_STROB(13),
    ALGO_BLOCK(14),
    SYSTEM(15),
    COMMAND_HANDLER(1),
    PRESENTATION_SLOT(2),
    USER_DEFINED(0),
    RPC(21),
    EXTERNAL(666);//Methods of this kind belongs to dark side...
    private final Long val;

    private EMethodNature(long val) {
        this.val = val;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EMethodNature getForValue(final long val) {
        for (EMethodNature e : EMethodNature.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EMethodNature has no item with value: " + String.valueOf(val), val);
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
