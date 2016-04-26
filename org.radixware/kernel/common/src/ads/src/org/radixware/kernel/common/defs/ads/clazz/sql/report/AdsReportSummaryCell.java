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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Report summary cell.
 */
public class AdsReportSummaryCell extends AdsReportFormattedCell {

    private Id propertyId;
    private EReportSummaryCellType summaryType = EReportSummaryCellType.SUM;
    private int groupCount = 0;

    protected AdsReportSummaryCell() {
        super();
    }

    protected AdsReportSummaryCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);        

        if (xCell.isSetPropId()) {
            this.propertyId = xCell.getPropId();
        }

        if (xCell.isSetSummaryType()) {
            summaryType = xCell.getSummaryType();
        }

        if (xCell.isSetSummaryGroupNum()) {
            groupCount = xCell.getSummaryGroupNum();
        }
    }
    
    protected AdsReportSummaryCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);        

        if (xCell.isSetPropId()) {
            this.propertyId = xCell.getPropId();
        }

        if (xCell.isSetSummaryType()) {
            summaryType = xCell.getSummaryType();
        }

        if (xCell.isSetSummaryGroupNum()) {
            groupCount = xCell.getSummaryGroupNum();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);

        if (propertyId != null) {
            xCell.setPropId(propertyId);
        }

        xCell.setSummaryType(summaryType);
        xCell.setSummaryGroupNum(groupCount);
    }

    @Override
    public String getName() {
        if (getOwnerReport() == null) {
            return super.getName();
        }
        final AdsPropertyDef prop = findProperty();
        return (prop != null ? prop.getName() : String.valueOf(propertyId)) + getSummaryType().getValue();
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.SUMMARY;
    }

    
    public Id getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Id propertyId) {
        if (!Utils.equals(this.propertyId, propertyId)) {
            this.propertyId = propertyId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return property or null if not found.
     */
    public AdsPropertyDef findProperty() {
        final AdsReportBand ownerBand = getOwnerBand();
        if (ownerBand != null) {
            final AdsReportForm ownerForm = ownerBand.getOwnerForm();
            if (ownerForm != null) {
                final AdsReportClassDef ownerReport = ownerForm.getOwnerReport();
                if (ownerReport != null) {
                    return ownerReport.getProperties().findById(propertyId, EScope.LOCAL).get();
                }
            }
        }
        return null;
    }

    /**
     * @return property.
     * @throws DefinitionNotFoundError if not found.
     */
    public AdsPropertyDef getProperty() {
        final AdsPropertyDef property = findProperty();
        if (property != null) {
            return property;
        } else {
            throw new DefinitionNotFoundError(propertyId);
        }
    }

    public EReportSummaryCellType getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(EReportSummaryCellType summaryType) {
        assert (summaryType != null);
        if (!Utils.equals(this.summaryType, summaryType)) {
            this.summaryType = summaryType;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return count of groups for which summary calculated, zero - for all.
     */
    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        if (this.groupCount != groupCount) {
            this.groupCount = groupCount;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final AdsPropertyDef property = findProperty();
        if (property != null) {
            list.add(property);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_SUMMARY_CELL;
    }

    public AdsReportGroup findSummaryGroup() {
        if (groupCount > 0) {
            final AdsReportBand ownerBand = getOwnerBand();
            if (ownerBand != null) {
                final AdsReportForm ownerForm = ownerBand.getOwnerForm();
                if (ownerForm != null) {
                    if (groupCount - 1 < ownerForm.getGroups().size()) {
                        return ownerForm.getGroups().get(groupCount - 1);
                    }
                }
            }
        }
        return null;
    }   
   
}
