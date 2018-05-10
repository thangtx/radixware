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
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo.ExportCsvColumn;

public class AdsCsvReportInfo extends AdsAbstractReportInfo {

    private final CsvExportColumns csvExportColumns;    
    private boolean exportToCsvEnabled = false;
    public static final String IS_CSV_ROW_VISIBLE_CONDITION_NAME = "isCsvRowVisible";
    private String delimiter = ";";

    public AdsCsvReportInfo() {
        super();
        csvExportColumns = new CsvExportColumns();
    }

    public AdsCsvReportInfo(final AdsReportClassDef report, ExportReportToCsvInfo xExportReportToCsv) {
        super();
        setContainer(report);
        csvExportColumns = new CsvExportColumns();
        if (xExportReportToCsv != null) {
            this.exportToCsvEnabled = true;
//            List<ExportCsvColumn> columns = xExportReportToCsv.getExportCsvColumnList();
//            for (ExportCsvColumn column : columns) {
//                AdsExportCsvColumn exportCsvColumn = new AdsExportCsvColumn(column);
//                csvExportColumns.add(exportCsvColumn);
//            }

            if (xExportReportToCsv.isSetIsRowVisible()) {
                rowVisibleCondition = Jml.Factory.loadFrom(this, xExportReportToCsv.getIsRowVisible(), IS_CSV_ROW_VISIBLE_CONDITION_NAME);
            } else {
                rowVisibleCondition = getDefaultJml(this, IS_CSV_ROW_VISIBLE_CONDITION_NAME);
            }
            if (xExportReportToCsv.isSetDelimiter()) {
                delimiter = xExportReportToCsv.getDelimiter();
            }
            if (xExportReportToCsv.isSetIsExportColumnNames()) {
                exportColumnNames = xExportReportToCsv.getIsExportColumnNames();
            }
        } else {
            rowVisibleCondition = null;
        }
    }

    public void appendTo(ExportReportToCsvInfo xExportReportToCsv, ESaveMode saveMode) {
        csvExportColumns.appendTo(xExportReportToCsv);
        if (rowVisibleCondition != null && !rowVisibleCondition.getItems().isEmpty()) {
            rowVisibleCondition.appendTo(xExportReportToCsv.addNewIsRowVisible(), saveMode);
        }
     //   String toSave = delimiter == null ? "" : delimiter.replace("\t", "&#x9;").replace("\t", "&#xA;").replace("\r", "&#xD;");

        xExportReportToCsv.setDelimiter(delimiter);
        xExportReportToCsv.setIsExportColumnNames(exportColumnNames);
    }

    public String getDelimiter() {
        return delimiter;
    }

    public char getCharDelimiter() {
        return delimiter != null && !delimiter.isEmpty() ? delimiter.charAt(0) : ';';
    }

    public void setDelimiter(String delimiter) {
        if (!this.delimiter.equals(delimiter)) {
            this.delimiter = delimiter;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isExportToCsvEnabled() {
        return exportToCsvEnabled;
    }

    public void setIsExportToCsvEnabled(boolean isExportToCsvEnabled) {
        if (this.exportToCsvEnabled != isExportToCsvEnabled) {
            this.exportToCsvEnabled = isExportToCsvEnabled;
            if (isExportToCsvEnabled && rowVisibleCondition == null) {
                rowVisibleCondition = getDefaultJml(this, IS_CSV_ROW_VISIBLE_CONDITION_NAME);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public CsvExportColumns getExportCsvColumns() {
        return csvExportColumns;
    }

    /**
     * @return property or null if not found.
     */
    public AdsPropertyDef findProperty(Id propId) {
        return AdsReportClassDef.ReportUtils.findProperty((AdsReportClassDef) getContainer(), propId);        
    }

    public static class CsvExportColumns extends RadixObjects<AdsExportCsvColumn> {

        void appendTo(ExportReportToCsvInfo xExportReportToCsv) {
            if (!isEmpty()) {
                for (AdsExportCsvColumn exportCsvColumn : this) {
                    ExportCsvColumn column = xExportReportToCsv.addNewExportCsvColumn();
                    exportCsvColumn.appendTo(column);
                }
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            if (!isEmpty()) {
                for (AdsExportCsvColumn csvColumn : this) {
                    csvColumn.collectDependences(list);
                }
            }
        }

    }
}
