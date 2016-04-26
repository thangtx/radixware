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

package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.sqml.tags.PropAbstractTag;
import org.radixware.kernel.designer.ads.editors.refactoring.IRefactoringOperation;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus.Event;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;


class ReplaceOperation implements IRefactoringOperation {

    public static class Factory {

        private Factory() {
        }

        static ReplaceOperation newInstance(ReplaceSteps.ReplaceSettings settings) {
            return new ReplaceOperation(settings);
        }
    }

    private final ReplaceSteps.ReplaceSettings settings;
    
    public ReplaceOperation(ReplaceSteps.ReplaceSettings settings) {
        this.settings = settings;
    }

    @Override
    public OperationStatus perform() {
        try {
            updateLinks(settings.getDefinition(), settings.getSubstitute());
            return OperationStatus.OK;
        } catch (Exception e) {
            return new OperationStatus(new Event(OperationStatus.EEventType.ERROR, e.getMessage()));
        }
    }

    private void updateLinks(AdsDefinition definition, AdsDefinition substitute) {
        final List<RadixObject> usages = UsagesFinder.toList(settings.getSelectedUsages());

        for (final RadixObject radixObject : usages) {
            if (radixObject.isReadOnly()) {
                continue;
            }
            if (radixObject instanceof JmlTagInvocation) {
                final JmlTagInvocation tag = (JmlTagInvocation) radixObject;
                tag.setPath(new AdsPath(substitute));
            } else if (radixObject instanceof JmlTagId) {
                final JmlTagId tag = (JmlTagId) radixObject;
                tag.setPath(new AdsPath(substitute));
            } else if (radixObject instanceof AdsSelectorPresentationDef.SelectorColumn) {
                final AdsSelectorPresentationDef.SelectorColumn column = (AdsSelectorPresentationDef.SelectorColumn) radixObject;
                column.setPropertyId(substitute.getId());
            } else if (radixObject instanceof AdsEditorPageDef.Properties) {
                AdsEditorPageDef.Properties props = (AdsEditorPageDef.Properties) radixObject;
                props.removeByPropId(definition.getId());
                props.addPropId(substitute.getId());
            } else if (radixObject instanceof PropAbstractTag) {
                PropAbstractTag tag = (PropAbstractTag) radixObject;
                tag.setPropId(substitute.getId());
//                tag.setPropOwnerId(substitute.getOwnerDef().getId());
            }
        }
    }
}
