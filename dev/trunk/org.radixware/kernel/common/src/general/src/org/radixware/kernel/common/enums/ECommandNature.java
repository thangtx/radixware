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


public enum ECommandNature implements IKernelIntEnum {

    FORM_IN_OUT(1, "Form in Form out"),
    LOCAL(0, "Local"),
    XML_IN_FORM_OUT(3, "Xml in Form out"),
    XML_IN_OUT(2, "Xml in Xml out");
    private final Long val;
    private final String name;

    private ECommandNature(long val, String name) {
        this.val = Long.valueOf(val);
        this.name = name;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ECommandNature getForValue(
            final long val) {
        for (ECommandNature e : ECommandNature.values()) {
            if (e.val.longValue() == val) {
                return e;
            }

        }
        throw new NoConstItemWithSuchValueError("ECommandNature has no item with value: " + String.valueOf(val),val);
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
