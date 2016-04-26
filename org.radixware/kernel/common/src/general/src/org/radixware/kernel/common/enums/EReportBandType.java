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

/**
 * AdsReportBand type.
 */
public enum EReportBandType implements IKernelStrEnum {

    PAGE_HEADER("PageHeader"),
    TITLE("Title"),
    GROUP_HEADER("GroupHeader"),
    COLUMN_HEADER("ColumnHeader"),
    DETAIL("Detail"),
    GROUP_FOOTER("GroupFooter"),
    SUMMARY("Summary"),
    PAGE_FOOTER("PageFooter");
    private final String value;

    private EReportBandType(final String val) {
        this.value = val;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EReportBandType getForValue(final String val) {
        for (EReportBandType e : EReportBandType.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EReportBandType has no item with value: " + String.valueOf(val),val);
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
