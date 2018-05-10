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
package org.radixware.kernel.common.builder.check.ads.clazz.sql;

import java.util.List;
import org.apache.poi.ss.util.WorkbookUtil;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsXlsxReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportUtils;
import org.radixware.kernel.common.defs.ads.common.ReleaseUtils;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.types.Id;

@RadixObjectCheckerRegistration
public class AdsReportClassChecker<T extends AdsReportClassDef> extends AdsSqlClassChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportClassDef.class;
    }

    @Override
    public void check(T report, IProblemHandler problemHandler) {
        super.check(report, problemHandler);
        AdsModule m = report.getModule();
        if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null && m != null && m.getSegmentType() == ERepositorySegmentType.ADS) {
            if (EDefinitionIdPrefix.USER_DEFINED_REPORT.equals(report.getId().getPrefix())) {
                final String userReportWarinig = System.getProperty("rdx.disigner.checker.report.rpu");
                final StringBuilder sb = new StringBuilder(65);  
                sb.append("Report with id #").append(report.getId()).append(" must be user report");
                if (userReportWarinig == null) {
                    error(report, problemHandler, sb.toString());
                } else {
                    warning(report, problemHandler, sb.toString());
                }
            }
        }
        AdsTypeDeclaration contextParameterType = null;
        if (report.getContextParameterId() != null) {
            final AdsPropertyDef prop = report.findContextParameter();
            if (prop != null) {
                if (!AdsVisitorProviders.newReportContextParameterFilter().isTarget(prop)) {
                    error(report, problemHandler, "Context parameter must be reference to entity.");
                }
                ReleaseUtils.checkExprationRelease(report, m, problemHandler);
                contextParameterType = prop.getValue().getType();
            } else {
                error(report, problemHandler, "Context parameter not found: #" + String.valueOf(report.getContextParameterId()));
            }
        }
        if (report.getContextParamTypeRestriction() != null) {
            if (!report.getContextParamTypeRestriction().isSuitableTypeForParameter(contextParameterType)) {
                if (contextParameterType == null) {
                    error(report, problemHandler, "Context parameter is requred");
                } else {
                    error(report, problemHandler, "Type of report version context parameter does not match with type, defined for context parameter in report");
                }
            }
        }

//        AdsCsvReportInfo csvInfo = report.getCsvInfo();
//        if (csvInfo != null && csvInfo.getExportCsvColumns() != null && !csvInfo.getExportCsvColumns().isEmpty()) {
//            for (AdsExportCsvColumn scvColumn : csvInfo.getExportCsvColumns()) {
//                if (csvInfo.findProperty(scvColumn.getPropId()) == null) {
//                    error(report, problemHandler, "Property not found: #" + String.valueOf(scvColumn.getPropId()));
//                }
//            }
//        }
        if (report.getColumns() != null && !report.getColumns().isEmpty()) {
            for (AdsReportColumnDef column : report.getColumns()) {
                if (column.getPropertyId() != null) {
                    if (AdsReportClassDef.ReportUtils.findProperty(report, column.getPropertyId()) == null) {
                        error(report, problemHandler, "Property not found: #" + String.valueOf(column.getPropertyId()));
                    }
                }
            }
        }

        if (report.getXlsxReportInfo() != null && report.getXlsxReportInfo().getSheetNameId() != null) {
            Id sheetNameId = report.getXlsxReportInfo().getSheetNameId();
            AdsMultilingualStringDef sheetName = report.findLocalizedString(sheetNameId);
            if (sheetName != null) {
                List<IMultilingualStringDef.StringStorage> sheetNameValues = sheetName.getValues(ExtendableDefinitions.EScope.LOCAL);
                if (sheetNameValues == null || sheetNameValues.isEmpty()) {
                    error(report, problemHandler, "Sheet name must not be null");
                } else {
                    for (IMultilingualStringDef.StringStorage sheetNameStorage : sheetNameValues) {
                        try {
                            WorkbookUtil.validateSheetName(sheetNameStorage.getValue());
                        } catch (IllegalArgumentException e) {
                            error(report, problemHandler, e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
