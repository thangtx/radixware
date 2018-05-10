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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;

public class ReportColumnEditorUtils {   

    public static void copyColumnContent(AdsReportClassDef report, AdsReportColumnDef source, AdsReportColumnDef target) {
        target.setName(source.getName());        
        target.setPropertyId(source.getPropertyId());
        target.setCsvExportFormat(source.getCsvExportFormat());
        target.setXlsxExportParameters(source.getXlsxExportParameters());
        target.setLegacyCsvName(source.getLegacyCsvName());
        target.setTitleId(source.getTitleId());
    }

    public static void copyFormatContent(AdsReportFormat source, AdsReportFormat target) {
        target.setPattern(source.getPattern());
        target.setPrecission(source.getPrecission());
        target.setDesimalDelimeter( source.getDesimalDelimeter());
        target.setTriadDelimeter(source.getTriadDelimeter());
        target.setTriadDelimeterType(source.getTriadDelimeterType());

        target.setDateStyle(source.getDateStyle());
        target.setTimeStyle(source.getTimeStyle());
        target.setUseDefaultFormat(source.getUseDefaultFormat());
    }

    public static boolean isNameDefined(String name) {
        return !Utils.emptyOrNull(name) && !AdsReportColumnDef.UNDEFINED_NAME.equals(name);
    }

    public static List<AdsPropertyDef> getPropertiesForEditor(List<AdsPropertyDef> allProperties, List<AdsPropertyDef> usedProperties) {
        List<AdsPropertyDef> result = new ArrayList<>();
        for (AdsPropertyDef property : allProperties) {
            EPropNature nature = property.getNature();
            if (nature == EPropNature.DYNAMIC || nature == EPropNature.FIELD || nature == EPropNature.SQL_CLASS_PARAMETER) {
                if (!usedProperties.contains(property)) {
                    result.add(property);
                }
            }
        }

        RadixObjectsUtils.sortByName(result);
        return result;
    }
}
