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

package org.radixware.kernel.designer.ads.common.sql;

import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;


public class AdsSqlClassVisitorProviderFactory {

    public static VisitorProvider newUsedTableProvider() {
        return new AdsSqlClassUsedTableProvider();
    }

    public static VisitorProvider newUsedPlSqlObjectProvider() {
        return new AdsSqlClassUsedPlSqlObjectProvider();
    }

    public static IFilter<DdsColumnDef> newColumnsFilter() {
        return new IFilter<DdsColumnDef>() {

            @Override
            public boolean isTarget(DdsColumnDef column) {
                return column.isGeneratedInDb();
            }
        };
    }

    public static VisitorProvider newEntityClassForPkParameterProvider() {
        return AdsVisitorProviders.newEntityTypeProvider();
    }

    public static IFilter<DdsIndexDef> newIndicesFilters() {
        return new IFilter<DdsIndexDef>() {

            @Override
            public boolean isTarget(DdsIndexDef index) {
                return index.isGeneratedInDb();
            }
        };
    }

    public static IFilter<AdsPropertyDef> newPropertyForPropertyCell() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef property) {
                return true;
            }
        };
    }

    public static IFilter<AdsPropertyDef> newPropertyForSummaryCell() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef property) {
                if (property.getNature() != EPropNature.SQL_CLASS_PARAMETER) {
                    final EValType valType = property.getValue().getType().getTypeId();
                    return (valType == EValType.INT || valType == EValType.NUM);
                } else {
                    return false;
                }
            }
        };
    }

    public static IFilter<AdsPropertyDef> newPropertyForPreprocessorTag() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef property) {
                if (property instanceof AdsParameterPropertyDef) {
                    final AdsParameterPropertyDef parameterProperty = (AdsParameterPropertyDef) property;
                    if (parameterProperty.calcDirection() == EParamDirection.OUT) {
                        return false;
                    }
                    return true;
                } else if (property instanceof AdsDynamicPropertyDef) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }
}
