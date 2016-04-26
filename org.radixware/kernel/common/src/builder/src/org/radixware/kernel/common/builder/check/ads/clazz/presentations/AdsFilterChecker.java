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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


@RadixObjectCheckerRegistration
public class AdsFilterChecker extends AdsDefinitionChecker<AdsFilterDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsFilterDef.class;
    }

    @Override
    public void check(AdsFilterDef filter, IProblemHandler problemHandler) {
        super.check(filter, problemHandler);
        CheckUtils.checkMLStringId(filter, filter.getTitleId(), problemHandler, "title");
        RadixObjects<AdsFilterDef.EnabledSorting> enabledSortings = filter.getEnabledSortings();
        if (filter.getDefaultSortingId() == null) {
            if (!filter.isWarningSuppressed(AdsFilterDef.Problems.DEFAULT_SORTING_IS_NOT_SPECIFIED)) {
                warning(filter, problemHandler, AdsFilterDef.Problems.DEFAULT_SORTING_IS_NOT_SPECIFIED);
            }
        } else {
            AdsSortingDef sorting = filter.findDefaultSorting();
            if (sorting == null) {
                error(filter, problemHandler, "Can not find default sorting for filter");
            } else {
                AdsUtils.checkAccessibility(filter, sorting, false, problemHandler);
                if (!filter.isAnyBaseSortingEnabled()) {
                    boolean found = false;
                    for (AdsFilterDef.EnabledSorting es : enabledSortings) {
                        if (es.getSortingId() == sorting.getId()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        warning(filter, problemHandler, "Default sorting " + sorting.getQualifiedName() + " of filter " + filter.getQualifiedName() + " is not enabled for the filter");
                    }
                }
            }
        }

        for (AdsFilterDef.EnabledSorting es : enabledSortings) {
            AdsSortingDef sorting = es.findSorting();
            if (sorting == null) {
                error(filter, problemHandler, "Can not find enabled sorting");
            } else {
                AdsUtils.checkAccessibility(filter, sorting, false, problemHandler);
                //CheckUtils.checkExportedApiDatails(filter, sorting, problemHandler);
            }
        }
        if (!filter.getCondition().hasImportant()) {
            error(filter, problemHandler, "Filter condition must be defined");
        }

        if (filter.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {

            boolean isWebCustomViewUsed = filter.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
            boolean isExplorerCustomViewUsed = filter.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);

            if (isWebCustomViewUsed || isExplorerCustomViewUsed) {
                if (!isWebCustomViewUsed && !filter.isWarningSuppressed(AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW)) {
                    warning(filter, problemHandler, AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW);
                }
                if (!isExplorerCustomViewUsed && !filter.isWarningSuppressed(AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW)) {
                    warning(filter, problemHandler, AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW);
                }
            }
        }
    }
}
