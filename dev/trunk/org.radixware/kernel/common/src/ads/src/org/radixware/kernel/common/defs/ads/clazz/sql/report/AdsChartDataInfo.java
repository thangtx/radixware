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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsChartDataInfo extends RadixObject {

    private Id propId;
    private int axisIndex = 0;

    public AdsChartDataInfo(AdsReportChartCell cell) {
        setContainer(cell);
    }

    public AdsChartDataInfo(org.radixware.schemas.adsdef.ChartDataInfo xChartDataInfo, final AdsReportChartCell cell) {
        setContainer(cell);
        propId = xChartDataInfo.getPropId();
        axisIndex = xChartDataInfo.getAxisIndex();
    }

    public AdsPropertyDef findProperty() {
        final AdsReportChartCell ownerCell = (AdsReportChartCell) getContainer();
        if (ownerCell != null) {
            AdsReportClassDef ownerReport = ownerCell.getOwnerReport();
            if (ownerReport != null) {
                return ownerReport.getProperties().findById(propId, EScope.LOCAL).get();
            }
        }
        return null;
    }

    public Id getPropId() {
        return propId;
    }

    public void setPropId(Id propId) {
        if (!Utils.equals(this.propId, propId)) {
            this.propId = propId;
            getContainer().setEditState(EEditState.MODIFIED);
        }
    }

    public int getAxisIndex() {
        return axisIndex;
    }

    public void setAxisIndex(int axisIndex) {
        if (!Utils.equals(this.axisIndex, axisIndex)) {
            this.axisIndex = axisIndex;
            getContainer().setEditState(EEditState.MODIFIED);
        }
    }

    public void appendTo(org.radixware.schemas.adsdef.ChartDataInfo xChartDataInfo) {
        if (propId != null) {
            xChartDataInfo.setPropId(propId);
        }
        if (axisIndex >= 0) {
            xChartDataInfo.setAxisIndex(axisIndex);
        }
    }
}
