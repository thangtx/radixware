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
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;


class SelectorPresentations extends ExtendableEntityPresentations<AdsSelectorPresentationDef> {

    SelectorPresentations(EntityObjectPresentations owner, ClassDefinition.Presentations xClass) {
        super(owner, xClass);
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.SelectorPresentations xEps = xPresentations.getSelectorPresentations();
        if (xEps != null) {
            List<SelectorPresentationDefinition> eps = xEps.getSelectorPresentationList();
            if (eps != null && !eps.isEmpty()) {
                for (SelectorPresentationDefinition ep : eps) {
                    getLocal().add(AdsSelectorPresentationDef.Factory.loadFrom(ep));
                }
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsSelectorPresentationDef> findInstance(EntityObjectPresentations prs) {
        return prs.getSelectorPresentations();
    }

    @Override
    public void appendTo(ClassDefinition.Presentations xPresentations, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            ClassDefinition.Presentations.SelectorPresentations sprs = xPresentations.addNewSelectorPresentations();
            AdsClassDef clazz = getOwnerClass();
            for (AdsSelectorPresentationDef spr : getLocal()) {
                //RADIX-9029: for app role setup
//                if (saveMode == ESaveMode.API) {
//                    if (!isSaveToAPI(clazz, spr)) {
//                        continue;
//                    }
//                }
                spr.appendTo(sprs.addNewSelectorPresentation(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        return "Selector Presentations";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.SELECTOR_PRESENTATION;
    }
}
