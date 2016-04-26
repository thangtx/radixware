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
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.EditorPresentationDefinition;

/**
 * Editor presentations collection
 */
class EditorPresentations extends ExtendableEntityPresentations<AdsEditorPresentationDef> {

    EditorPresentations(EntityObjectPresentations owner, ClassDefinition.Presentations xClass) {
        super(owner, xClass);
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.EditorPresentations xEps = xPresentations.getEditorPresentations();
        if (xEps != null) {
            List<EditorPresentationDefinition> eps = xEps.getEditorPresentationList();
            if (eps != null && !eps.isEmpty()) {
                for (EditorPresentationDefinition ep : eps) {
                    getLocal().add(AdsEditorPresentationDef.Factory.loadFrom(ep));
                }
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsEditorPresentationDef> findInstance(EntityObjectPresentations prs) {
        return prs.getEditorPresentations();
    }

    @Override
    public void appendTo(ClassDefinition.Presentations xPresentations, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            ClassDefinition.Presentations.EditorPresentations eprs = xPresentations.addNewEditorPresentations();
            AdsClassDef clazz = getOwnerClass();
            for (AdsEditorPresentationDef epr : getLocal()) {
                //RADIX-9029: for app role setup
//                if (saveMode == ESaveMode.API) {
//                    if (!isSaveToAPI(clazz, epr)) {
//                        continue;
//                    }
//
//                }
                epr.appendTo(eprs.addNewEditorPresentation(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        return "Editor Presentations";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.EDITOR_PRESENTATION;
    }

    @Override
    public AdsEditorPresentationDef override(IOverridable<AdsEditorPresentationDef> source) {
        throw new RadixError("Override can't be applied for Presentations collection");
    }
}
