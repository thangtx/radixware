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

public enum EDwfEditorContentSaving implements IKernelIntEnum {

    BOTH_CONTENTS(new Long(3)),
    NEW_CONTENT(new Long(2)),
    NONE(new Long(0)),
    OLD_CONTENT(new Long(1));
    private final Long value;

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }
    //constructors   

    private EDwfEditorContentSaving(Long x) {
        value = x;
    }

    public static EDwfEditorContentSaving getForValue(final long val) {
        for (EDwfEditorContentSaving e : EDwfEditorContentSaving.values()) {
            if (e.value.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDwfEditorContentSaving has no item with value: " + String.valueOf(val),val);
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