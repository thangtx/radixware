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

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;

public class RadReportColumnDef extends RadDefinition{
    private final Id propertyId;
    private final Id reportId;

    private String legacyCsvName = null;
    private AdsReportFormat csvExportFormat = null;

    private Id titleId = null;
    private AdsReportColumnDef.XlsxExportParameters xlsxExportParameters = null;
    
    public RadReportColumnDef(
            final Id id,            
            final String name,
            final Id propertyId,
            final String legacyCsvName,
            final AdsReportFormat csvExportFormat,
            final Id titleId,            
            final Id reportId) {
        super(id, name);
                
        this.propertyId = propertyId;
        
        this.legacyCsvName = legacyCsvName;
        this.csvExportFormat = csvExportFormat;
        
        this.titleId = titleId;        
        this.reportId = reportId;
    }

    public Id getPropertyId() {
        return propertyId;
    }

    public String getLegacyCsvName() {
        return legacyCsvName;
    }

    public AdsReportFormat getCsvExportFormat() {
        return csvExportFormat;
    }

    public void setXlsxExportParameters(AdsReportColumnDef.XlsxExportParameters xlsxExportParameters) {
        this.xlsxExportParameters = xlsxExportParameters;
    }

    public AdsReportColumnDef.XlsxExportParameters getXlsxExportParameters() {
        return xlsxExportParameters;
    }
    
    public String getTitle() {
        return MultilingualString.get(Arte.get(), reportId, titleId);
    }
}
