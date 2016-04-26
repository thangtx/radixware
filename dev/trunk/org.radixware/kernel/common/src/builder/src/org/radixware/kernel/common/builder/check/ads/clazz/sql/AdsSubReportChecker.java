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

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.utils.Utils;

@RadixObjectCheckerRegistration
public class AdsSubReportChecker<T extends AdsSubReport> extends RadixObjectChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsSubReport.class;
    }

    @Override
    public void check(T subReport, IProblemHandler problemHandler) {
        super.check(subReport, problemHandler);

        final AdsReportClassDef report = subReport.findReport();
        final AdsReportClassDef ownerReport = subReport.getOwnerReport();
        if (report != null) {
            final int subReportPageWidth = report.getForm().getPageWidthMm();
            final int reportPageWidth = subReport.getOwnerReport().getForm().getPageWidthMm();
            if (subReportPageWidth != reportPageWidth) {
                warning(subReport, problemHandler, "Page width (" + reportPageWidth + ") not equal to subreport '" + report.getQualifiedName() + "' page width (" + subReportPageWidth + ")");
            }

            final Set<AdsParameterPropertyDef> associatedParameters = new HashSet<AdsParameterPropertyDef>();

            for (AdsSubReport.Association association : subReport.getAssociations()) {
                final AdsParameterPropertyDef parameter = association.findParameter();
                if (parameter != null) {
                    associatedParameters.add(parameter);
                } else {
                    error(association, problemHandler, "Subreport parameter '" + report.getQualifiedName() + "' not found: #" + String.valueOf(association.getParameterId()));
                }
                final AdsPropertyDef property = association.findProperty();
                if (property == null) {
                    error(association, problemHandler, "Subreport property '" + report.getQualifiedName() + "' not found: #" + String.valueOf(association.getPropertyId()));
                }
                if (parameter != null && property != null) {
                    final AdsTypeDeclaration parameterType = parameter.getValue().getType();
                    final AdsTypeDeclaration propertyType = property.getValue().getType();

                    if (!AdsTypeDeclaration.isAssignable(parameterType, propertyType, ownerReport)) {
                        error(association, problemHandler, "Property '" + property.getName() + "' not equal to parameter '" + parameter.getName() + "' for subreport '" + report.getQualifiedName() + "'");
                    }
                }
            }

            for (AdsParameterPropertyDef parameter : report.getInputParameters()) {
                if (!associatedParameters.contains(parameter)) {
                    // warning - will be successfully compiled, will use null at run time as parameter value.
                    warning(subReport, problemHandler, "Parameter '" + parameter.getName() + "' not associated for subreport '" + report.getQualifiedName() + "'");
                }
            }
        } else {
            error(subReport, problemHandler, "Subreport not found: #" + String.valueOf(subReport.getReportId()));
        }
    }
}
