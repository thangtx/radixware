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

import org.radixware.kernel.common.defs.ads.clazz.AbstractFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsReportModelClassDef extends AbstractFormModelClassDef<AdsReportClassDef> {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsReportModelClassDef loadFrom(final AdsReportClassDef owner, final ClassDefinition xDef) {
            return new AdsReportModelClassDef(owner, xDef);
        }

        public static AdsReportModelClassDef newInstance(final AdsReportClassDef owner) {
            return new AdsReportModelClassDef(owner);
        }
    }

    private AdsReportModelClassDef(final AdsReportClassDef owner, final ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS);
    }

    private AdsReportModelClassDef(final AdsReportClassDef owner) {
        super(owner, EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.REPORT_MODEL;
    }

    @Override
    public String getRuntimeLocalClassName() {
        AdsClassDef report = findServerSideClasDef();
        if (report instanceof AdsUserReportClassDef) {
            Id runtimeId = getRuntimeId();
            return runtimeId.toString();
        } else {
            return super.getRuntimeLocalClassName();
        }
    }

    public Id getRuntimeId() {
        AdsClassDef report = findServerSideClasDef();
        if (report instanceof AdsUserReportClassDef) {
            Id runtimeId = ((AdsUserReportClassDef) report).getRuntimeId();
            return Id.Factory.changePrefix(runtimeId, EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS);
        } else {
            return getId();
        }
    }
}
