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

package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class ReportClassRadixdoc extends SqlClassRadixdoc {

    public ReportClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        super.writeClassDefInfo(overview, overviewTable);
        
        AdsReportClassDef reportDef = (AdsReportClassDef) source;
        if (reportDef.getContextParameter() != null) {
            getClassWriter().addStr2RefRow(overviewTable, "Context parameter", reportDef.getContextParameter(), source);
        } else {
            getClassWriter().addAllStrRow(overviewTable, "Context parameter", "<Not Defined>");
        }
        getClassWriter().addStr2BoolRow(overviewTable, "Use custom view", reportDef.getPresentations().isCustomViewInherited());
        getClassWriter().addStr2BoolRow(overviewTable, "Subreport", reportDef.isSubreport());

        writeExportToCsvInfo(overview, overviewTable, reportDef.getCsvInfo());
    }

    private void writeExportToCsvInfo(ContentContainer overview, Table overviewTable, AdsCsvReportInfo csvInfo) {
        if (csvInfo != null && csvInfo.isExportToCsvEnabled()) {
            if (csvInfo.getDelimiter() != null && !csvInfo.getDelimiter().isEmpty()) {
                getClassWriter().addAllStrRow(overviewTable, "Delimiter", csvInfo.getDelimiter());
            }

            if (!csvInfo.getExportCsvColumns().isEmpty()) {
                Table columnForCsv = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Export to CSV", "Property", "External Name", "Format");
                for (AdsExportCsvColumn col : csvInfo.getExportCsvColumns()) {
                    AdsPropertyDef prop = csvInfo.findProperty(col.getPropId());
                    if (prop != null) {
                        String format = col.getFormat() != null ? col.getFormat().getStrValue(prop.getValue().getType().getTypeId()) : "";
                        getClassWriter().addAllStrRow(columnForCsv, prop.getName(), col.getExtName(), format);
                    }
                }
            }
        }
    }
}