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
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;

public enum EEventSeverity implements IKernelIntEnum{
    DEBUG(0, "Debug"),
    EVENT(1, "Event"),
    WARNING(2, "Warning"),
    ERROR(3, "Error"),
    ALARM(4, "Alarm"),

    /**
     * Used in trace setup to ignore all events.
     */
    NONE(5, "None");

    private final Long val;
    private final String name;

    private EEventSeverity(long val, String title) {
        this.val = Long.valueOf(val);
        this.name = title;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EEventSeverity getForName(final String val) {
        for (EEventSeverity t : EEventSeverity.values()) {
            if (t.getName() == null && val == null || t.getName().equalsIgnoreCase(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EEventSeverity has no item with value: " + String.valueOf(val),val);
    }
    public static EEventSeverity getForValue(final Long val) {
        for (EEventSeverity t : EEventSeverity.values()) {
            if (t.getValue() == null && val == null || t.getValue().equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EEventSeverity has no item with value: " + String.valueOf(val),val);
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
