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

import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EReportSpecialCellType;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Report special cell.
 */
public class AdsReportSpecialCell extends AdsReportFormattedCell{

    private EReportSpecialCellType specialType = EReportSpecialCellType.GENERATION_TIME;


    protected AdsReportSpecialCell() {
        super();
    }

    protected AdsReportSpecialCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);       

        if (xCell.isSetSpecialType()) {
            specialType = xCell.getSpecialType();
        }
    }
    
    protected AdsReportSpecialCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);       

        if (xCell.isSetSpecialType()) {
            specialType = xCell.getSpecialType();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);

        xCell.setSpecialType(specialType);
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.SPECIAL;
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()){
            return specialType.getValue();
        }
        return name;
    }

    @Override
    public String getDefaultName() {
        return specialType.getValue();
    }

    public EReportSpecialCellType getSpecialType() {
        return specialType;
    }

    public void setSpecialType(EReportSpecialCellType specialType) {
        assert specialType != null;
        if (!Utils.equals(this.specialType, specialType)) {
            this.specialType = specialType;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_SPECIAL_CELL;
    }   
}
