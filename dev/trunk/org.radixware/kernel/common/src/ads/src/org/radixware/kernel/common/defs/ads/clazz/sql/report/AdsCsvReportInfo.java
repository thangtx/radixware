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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo.ExportCsvColumn;

public class AdsCsvReportInfo extends RadixObject implements IProfileable {

    private final CsvExportColumns csvExportColumns;
    private Jml csvRowVisibleCondition;
    private boolean exportToCsvEnabled = false;
    private boolean exportColumnNames = true;
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
            List<ExportCsvColumn> columns = xExportReportToCsv.getExportCsvColumnList();
            for (ExportCsvColumn column : columns) {
                AdsExportCsvColumn exportCsvColumn = new AdsExportCsvColumn(column);
                csvExportColumns.add(exportCsvColumn);
            }

            if (xExportReportToCsv.isSetIsRowVisible()) {
                csvRowVisibleCondition = Jml.Factory.loadFrom(this, xExportReportToCsv.getIsRowVisible(), IS_CSV_ROW_VISIBLE_CONDITION_NAME);
            } else {
                csvRowVisibleCondition = getDefaultJml(this);
            }
            if (xExportReportToCsv.isSetDelimiter()) {
                delimiter = xExportReportToCsv.getDelimiter();
            }
            if (xExportReportToCsv.isSetIsExportColumnNames()) {
                exportColumnNames = xExportReportToCsv.getIsExportColumnNames();
            }
        } else {
            csvRowVisibleCondition = null;
        }
    }

    private static Jml getDefaultJml(RadixObject context) {
        Jml jml = Jml.Factory.newInstance(context, IS_CSV_ROW_VISIBLE_CONDITION_NAME);
        Jml.Item textItem = Jml.Text.Factory.newInstance("return true;");
        jml.getItems().add(textItem);
        return jml;
    }

    public void appendTo(ExportReportToCsvInfo xExportReportToCsv, ESaveMode saveMode) {
        csvExportColumns.appendTo(xExportReportToCsv);
        if (csvRowVisibleCondition != null && !csvRowVisibleCondition.getItems().isEmpty()) {
            csvRowVisibleCondition.appendTo(xExportReportToCsv.addNewIsRowVisible(), saveMode);
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
            if (isExportToCsvEnabled && csvRowVisibleCondition == null) {
                csvRowVisibleCondition = getDefaultJml(this);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isExportColumnName() {
        return exportColumnNames;
    }

    public void setIsExportColumnName(boolean isExportColumnNames) {
        if (this.exportColumnNames != isExportColumnNames) {
            this.exportColumnNames = isExportColumnNames;
            setEditState(EEditState.MODIFIED);
        }
    }

    public CsvExportColumns getExportCsvColumns() {
        return csvExportColumns;
    }

    public Jml getRowCondition() {
        return csvRowVisibleCondition;
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        csvExportColumns.visit(visitor, provider);
        if (csvRowVisibleCondition != null) {
            csvRowVisibleCondition.visit(visitor, provider);
        }
    }

    /**
     * @return property or null if not found.
     */
    public AdsPropertyDef findProperty(Id propId) {
        final AdsReportClassDef ownerReport = (AdsReportClassDef) getContainer();
        if (ownerReport != null) {
            return ownerReport.getProperties().findById(propId, EScope.LOCAL).get();
        }
        return null;
    }

    @Override
    public AdsProfileSupport getProfileSupport() {
        return new AdsProfileSupport(this);
    }

    @Override
    public boolean isProfileable() {
        return true;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return (AdsReportClassDef) getContainer();
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
