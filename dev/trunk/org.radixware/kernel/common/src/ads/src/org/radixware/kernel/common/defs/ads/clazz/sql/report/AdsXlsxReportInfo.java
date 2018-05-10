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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExportReportToXlsxInfo;

public class AdsXlsxReportInfo extends AdsAbstractReportInfo {        
    private boolean exportToXlsxEnabled = false;
    private Id sheetNameId = null;
    public static final String IS_XLSX_ROW_VISIBLE_CONDITION_NAME = "isXlsxRowVisible";        

    public AdsXlsxReportInfo() {
        super();
    }
    
    public AdsXlsxReportInfo(final AdsReportClassDef report, ExportReportToXlsxInfo xExportReportToXlsx) {
        super();
        setContainer(report);        
        if (xExportReportToXlsx != null) {
            this.exportToXlsxEnabled = true;

            if (xExportReportToXlsx.isSetIsRowVisible()) {
                rowVisibleCondition = Jml.Factory.loadFrom(this, xExportReportToXlsx.getIsRowVisible(), IS_XLSX_ROW_VISIBLE_CONDITION_NAME);
            } else {
                rowVisibleCondition = getDefaultJml(this, IS_XLSX_ROW_VISIBLE_CONDITION_NAME);
            }            
            if (xExportReportToXlsx.isSetIsExportColumnNames()) {
                exportColumnNames = xExportReportToXlsx.getIsExportColumnNames();
            }
            if (xExportReportToXlsx.isSetSheetNameId()) {
                sheetNameId = xExportReportToXlsx.getSheetNameId();
            }
        } else {
            rowVisibleCondition = null;
        }
    }   

    public void appendTo(ExportReportToXlsxInfo xExportReportToXlsx, AdsDefinition.ESaveMode saveMode) {        
        if (rowVisibleCondition != null && !rowVisibleCondition.getItems().isEmpty()) {
            rowVisibleCondition.appendTo(xExportReportToXlsx.addNewIsRowVisible(), saveMode);
        }
        if (sheetNameId != null) {
            xExportReportToXlsx.setSheetNameId(sheetNameId);
        }
        
        xExportReportToXlsx.setIsExportColumnNames(exportColumnNames);        
    }

    public boolean isExportToXlsxEnabled() {
        return exportToXlsxEnabled;
    }

    public void setIsExportToXlsxEnabled(boolean isExportToXlsxEnabled) {
        if (this.exportToXlsxEnabled != isExportToXlsxEnabled) {
            this.exportToXlsxEnabled = isExportToXlsxEnabled;
            if (isExportToXlsxEnabled && rowVisibleCondition == null) {
                rowVisibleCondition = getDefaultJml(this, IS_XLSX_ROW_VISIBLE_CONDITION_NAME);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getSheetNameId() {
        return sheetNameId;
    }

    public void setSheetNameId(Id sheetNameId) {
        if (this.sheetNameId != sheetNameId) {
            this.sheetNameId = sheetNameId;
            setEditState(EEditState.MODIFIED);
        }
    }
}
