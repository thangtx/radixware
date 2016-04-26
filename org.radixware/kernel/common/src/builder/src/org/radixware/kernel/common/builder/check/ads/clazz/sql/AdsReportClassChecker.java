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

import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


@RadixObjectCheckerRegistration
public class AdsReportClassChecker<T extends AdsReportClassDef> extends AdsSqlClassChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportClassDef.class;
    }

    @Override
    public void check(T report, IProblemHandler problemHandler) {
        super.check(report, problemHandler);

        AdsTypeDeclaration contextParameterType = null;
        if (report.getContextParameterId() != null) {
            final AdsPropertyDef prop = report.findContextParameter();
            if (prop != null) {
                if (!AdsVisitorProviders.newReportContextParameterFilter().isTarget(prop)) {
                    error(report, problemHandler, "Context parameter must be reference to entity.");
                }
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

        AdsCsvReportInfo csvInfo = report.getCsvInfo();
        if (csvInfo != null && csvInfo.getExportCsvColumns() != null && !csvInfo.getExportCsvColumns().isEmpty()) {
            for (AdsExportCsvColumn scvColumn : csvInfo.getExportCsvColumns()) {
                if (csvInfo.findProperty(scvColumn.getPropId()) == null) {
                    error(report, problemHandler, "Property not found: #" + String.valueOf(scvColumn.getPropId()));
                }
            }
        }
    }
}
