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
package org.radixware.kernel.server.reports;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.reports.xml.IReportWriter;
import org.radixware.schemas.reports.ColumnSettings;
import org.radixware.schemas.reports.ReportMsdlType;

public class ReportExportParameters {
    private final OutputStream stream;
    private final IReportFileController controller;
    
    private EReportExportFormat exportFormat = null;
    private String encoding = null;
    
    private Map<Id, Object> paramId2Value = null;
    
    //Multiformat report export parameters
    private List<EReportExportFormat> exportFormats = null;
    private Integer maxResultSetCacheSizeKb = null;
    
    //Custom report export parameters
    private IReportWriter reportWriter = null;
    
    //MSDl report export parameters
    private ReportMsdlType reportMsdl = null;
    
    //Report column settings
    private ColumnSettings columnSettings;

    public ReportExportParameters(OutputStream stream) {
        this.stream = stream;
        this.controller = null;
    }

    public ReportExportParameters(IReportFileController controller) {
        this.controller = controller;
        this.stream = null;
    }

    public OutputStream getStream() {
        return stream;
    }

    public IReportFileController getController() {
        return controller;
    }
    
    public EReportExportFormat getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(EReportExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<Id, Object> getParamId2Value() {
        return paramId2Value;
    }

    public void setParamId2Value(Map<Id, Object> paramId2Value) {
        this.paramId2Value = paramId2Value;
    }

    public List<EReportExportFormat> getExportFormats() {
        return exportFormats;
    }

    public void setExportFormats(List<EReportExportFormat> exportFormats) {
        this.exportFormats = exportFormats;
    }

    public Integer getMaxResultSetCacheSizeKb() {
        return maxResultSetCacheSizeKb;
    }

    public void setMaxResultSetCacheSizeKb(Integer maxResultSetCacheSizeKb) {
        this.maxResultSetCacheSizeKb = maxResultSetCacheSizeKb;
    }

    public IReportWriter getReportWriter() {
        return reportWriter;
    }

    public void setReportWriter(IReportWriter reportWriter) {
        this.reportWriter = reportWriter;
    }

    public ReportMsdlType getReportMsdl() {
        return reportMsdl;
    }

    public void setReportMsdl(ReportMsdlType reportMsdl) {
        this.reportMsdl = reportMsdl;
    }

    public ColumnSettings getColumnSettings() {
        return columnSettings;
    }

    public void setColumnSettings(ColumnSettings columnSettings) {
        this.columnSettings = columnSettings;
    }
}
