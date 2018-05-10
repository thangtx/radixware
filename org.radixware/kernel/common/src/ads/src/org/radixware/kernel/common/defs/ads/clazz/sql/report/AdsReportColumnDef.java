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

import java.util.Collection;
import java.util.Objects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.enums.EReportColumnResizeMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo;
import org.radixware.schemas.adsdef.ReportColumnDefinition;

public class AdsReportColumnDef extends AdsDefinition {

    public static final String UNDEFINED_NAME = "<undefined>";
    
    private Id propertyId;

    private String legacyCsvName = null;
    private AdsReportFormat csvExportFormat = null;

    private Id titleId = null;
    private XlsxExportParameters xlsxExportParameters = null;

    public AdsReportColumnDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.REPORT_COLUMN));
    }

    public AdsReportColumnDef(Id propertyId, String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.REPORT_COLUMN), name);
        this.propertyId = propertyId;
    }

    public AdsReportColumnDef(ReportColumnDefinition xDef) {
        super(xDef.getId(), xDef.getName());
        this.propertyId = xDef.getPropertyId();

        this.legacyCsvName = xDef.getLegacyCsvName();
        if (xDef.isSetCsvExportParameters() && xDef.getCsvExportParameters().isSetCsvExportFormat()) {
            this.csvExportFormat = new AdsReportFormat(xDef.getCsvExportParameters().getCsvExportFormat());
        }

        this.titleId = xDef.getTitleId();

        if (xDef.isSetXlsxExportParameters()) {
            this.xlsxExportParameters = new XlsxExportParameters(xDef.getXlsxExportParameters());
        }
    }

    public AdsReportColumnDef(ExportReportToCsvInfo.ExportCsvColumn csvColumn, String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.REPORT_COLUMN), name);
        this.propertyId = csvColumn.getId();

        this.legacyCsvName = csvColumn.getExtName();
        if (csvColumn.isSetFormat()) {
            this.csvExportFormat = new AdsReportFormat(csvColumn.getFormat());
        }
    }
    
    public AdsReportColumnDef(AdsReportColumnDef source) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.REPORT_COLUMN));
        if (source != null) {
            this.setName(source.getName());
            this.propertyId = source.propertyId;
            this.titleId = source.getTitleId();
            this.legacyCsvName = source.legacyCsvName;
            
            this.xlsxExportParameters = source.xlsxExportParameters == null ? new XlsxExportParameters() : new XlsxExportParameters(source.xlsxExportParameters);
            this.csvExportFormat = source.csvExportFormat == null ? new AdsReportFormat() : new AdsReportFormat(source.csvExportFormat);
        } else {
            this.setName(UNDEFINED_NAME);
            this.xlsxExportParameters = new XlsxExportParameters();
            this.csvExportFormat = new AdsReportFormat();
        }
    }

    public Id getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Id propertyId) {
        if (this.propertyId != propertyId) {
            this.propertyId = propertyId;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    public String getLegacyCsvName() {
        return legacyCsvName;
    }

    public void setLegacyCsvName(String legacyCsvName) {
        this.legacyCsvName = legacyCsvName;
    }

    public AdsReportFormat getCsvExportFormat() {
        return csvExportFormat;
    }

    public void setCsvExportFormat(AdsReportFormat csvExportFormat) {
        if (this.csvExportFormat != null) {
            if (!this.csvExportFormat.equals(csvExportFormat)) {
                this.csvExportFormat = csvExportFormat;
                this.setEditState(EEditState.MODIFIED);
            }
        } else {
            this.csvExportFormat = csvExportFormat;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    public Id getTitleId() {
        return titleId;
    }

    public void setTitleId(Id titleId) {
        if (this.titleId != titleId) {
            this.titleId = titleId;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    public XlsxExportParameters getXlsxExportParameters() {
        return xlsxExportParameters;
    }

    public void setXlsxExportParameters(XlsxExportParameters xlsxExportParameters) {
        if (this.xlsxExportParameters != null) {
            if (!this.xlsxExportParameters.equals(xlsxExportParameters)) {
                setEditState(EEditState.MODIFIED); 
                this.xlsxExportParameters = xlsxExportParameters;
            }            
        } else {
            this.xlsxExportParameters = xlsxExportParameters;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.REPORT_COLUMN;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids
    ) {
        super.collectUsedMlStringIds(ids);
        if (titleId != null) {
            ids.add(new MultilingualStringInfo(this) {

                @Override
                public String getContextDescription() {
                    return " Report Column";
                }

                @Override
                public Id getId() {
                    return titleId;
                }

                @Override
                public EAccess getAccess() {
                    return AdsReportColumnDef.this.getAccessMode();
                }

                @Override
                public void updateId(Id newId) {
                    setTitleId(titleId);
                }

                @Override
                public boolean isPublished() {
                    return AdsReportColumnDef.this.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.TITLE;
                }
            });
        }
    }

    public void appendTo(ReportColumnDefinition xDef) {
        xDef.setId(getId());
        xDef.setName(getName());

        xDef.setPropertyId(propertyId);

        if (legacyCsvName != null) {
            xDef.setLegacyCsvName(legacyCsvName);
        }

        if (csvExportFormat != null) {
            if (xDef.isSetCsvExportParameters()) {
                csvExportFormat.appendTo(xDef.getCsvExportParameters().addNewCsvExportFormat(), getEditState());
            } else {
                csvExportFormat.appendTo(xDef.addNewCsvExportParameters().addNewCsvExportFormat(), getEditState());
            }
        }

        if (titleId != null) {
            xDef.setTitleId(titleId);
        }

        if (xlsxExportParameters != null) {
            ReportColumnDefinition.XlsxExportParameters xParams = xDef.addNewXlsxExportParameters();
            xlsxExportParameters.appendTo(xParams);
        }
    }

    public static class XlsxExportParameters {

        private String xlsxExportFormat = null;

        private EReportColumnResizeMode resizeMode = EReportColumnResizeMode.NONE;
        private int width = 0;

        public XlsxExportParameters() {
        }

        public XlsxExportParameters(String xlsxExportFormat, EReportColumnResizeMode resizeMode, int width) {
            this.xlsxExportFormat = xlsxExportFormat;
            this.resizeMode = resizeMode == null ? EReportColumnResizeMode.NONE : resizeMode;
            this.width = width;
        }

        public XlsxExportParameters(XlsxExportParameters source) {
            this.xlsxExportFormat = source.xlsxExportFormat;
            this.resizeMode = source.resizeMode;
            this.width = source.width;
        }

        public XlsxExportParameters(ReportColumnDefinition.XlsxExportParameters xParams) {
            this.xlsxExportFormat = xParams.getXlsxExportFormat();
            this.resizeMode = xParams.getResizeMode() == null ? EReportColumnResizeMode.NONE : xParams.getResizeMode();
            this.width = xParams.getWidth();
        }

        public String getXlsxExportFormat() {
            return xlsxExportFormat;
        }

        public void setXlsxExportFormat(String xlsxExportFormat) {
            this.xlsxExportFormat = xlsxExportFormat;
        }

        public EReportColumnResizeMode getResizeMode() {
            return resizeMode;
        }

        public void setResizeMode(EReportColumnResizeMode resizeMode) {
            this.resizeMode = resizeMode;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void appendTo(ReportColumnDefinition.XlsxExportParameters xParams) {
            xParams.setXlsxExportFormat(xlsxExportFormat);
            xParams.setResizeMode(resizeMode);
            xParams.setWidth(width);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.xlsxExportFormat);
            hash = 23 * hash + Objects.hashCode(this.resizeMode);
            hash = 23 * hash + this.width;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final XlsxExportParameters other = (XlsxExportParameters) obj;
            if (!Objects.equals(this.xlsxExportFormat, other.xlsxExportFormat)) {
                return false;
            }
            if (this.resizeMode != other.resizeMode) {
                return false;
            }
            if (this.width != other.width) {
                return false;
            }
            return true;
        }
    }
}
