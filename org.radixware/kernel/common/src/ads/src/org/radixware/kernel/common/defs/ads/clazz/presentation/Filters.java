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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Filters.Filter;

/**
 * Filters Collection
 */
class Filters extends ExtendableEntityPresentations<AdsFilterDef> {

    Filters(EntityObjectPresentations owner, ClassDefinition.Presentations xDef) {
        super(owner, xDef);
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.Filters xFilters = xPresentations.getFilters();
        if (xFilters != null) {
            List<Filter> filters = xFilters.getFilterList();
            if (filters != null && !filters.isEmpty()) {
                for (Filter f : filters) {
                    getLocal().add(AdsFilterDef.Factory.loadFrom(f));
                }
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsFilterDef> findInstance(EntityObjectPresentations prs) {
        return prs.getFilters();
    }

    @Override
    void appendTo(Presentations xDef, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            Presentations.Filters set = xDef.addNewFilters();
            AdsClassDef clazz = getOwnerClass();
            for (AdsFilterDef f : getLocal()) {
                if (saveMode == ESaveMode.API) {
                    if (!isSaveToAPI(clazz, f)) {
                        continue;
                    }
                }
                f.appendTo(set.addNewFilter(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        return "Filters";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.FILTER;
    }
}
