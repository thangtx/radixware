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


public enum EReportChartSeriesType  implements IKernelStrEnum {
    PIE("Pie"),
    PIE_3D("Pie3D"),
    BAR("Bar"),
    BAR_3D("Bar3D"),
    BAR_STACKED("BarStacked"),
    BAR_STACKED_3D("BarStacked3D"),
    XY_BAR("XYBar"),
    XY_BAR_STACKED("XYBarStacked"),
    LINE("Line"),
    LINE_3D("Line3D"),
    XY_LINE("XYLine"),
    AREA("Area"),
    AREA_STACKED("AreaStacked"),
    XY_AREA("XYAres"),
    XY_AREA_STACKED("XYAresStacke");
    private final String value;

    private EReportChartSeriesType(final String val) {
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

    public static EReportChartSeriesType getForValue(final String val) {
        for (EReportChartSeriesType e : EReportChartSeriesType.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EReportChartType has no item with value: " + String.valueOf(val),val);
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
