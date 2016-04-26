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
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Sortings.Sorting;


class Sortings extends ExtendableEntityPresentations<AdsSortingDef> {

    Sortings(EntityObjectPresentations owner, ClassDefinition.Presentations xClass) {
        super(owner, xClass);
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.Sortings xSortings = xPresentations.getSortings();
        if (xSortings != null) {
            List<Sorting> sortings = xSortings.getSortingList();
            if (sortings != null && !sortings.isEmpty()) {
                for (Sorting s : sortings) {
                    getLocal().add(AdsSortingDef.Factory.loadFrom(s));
                }
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsSortingDef> findInstance(EntityObjectPresentations prs) {
        return prs.getSortings();
    }

    @Override
    void appendTo(Presentations xDef, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            Presentations.Sortings set = xDef.addNewSortings();
            AdsClassDef clazz = getOwnerClass();
            for (AdsSortingDef s : getLocal()) {
                if (saveMode == ESaveMode.API) {
                    if (!isSaveToAPI(clazz, s)) {
                        continue;
                    }
                }
                s.appendTo(set.addNewSorting(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        return "Sortings";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.SORTING;
    }
}
