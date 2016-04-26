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

package org.radixware.kernel.common.builder.check.ads.exploreritem;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public abstract class AdsExplorerItemChecker<T extends AdsExplorerItemDef> extends AdsDefinitionChecker<T> {

    @Override
    public void check(final T explorerItem, final IProblemHandler problemHandler) {
        super.check(explorerItem, problemHandler);
        CheckUtils.checkMLStringId(explorerItem, explorerItem.getTitleId(), problemHandler, "title");

        ERuntimeEnvironmentType ownerEnv = null;

        AdsDefinition owner = explorerItem.getOwnerDef();
        if (owner instanceof AdsEditorPresentationDef) {

            ownerEnv = ((AdsEditorPresentationDef) owner).getClientEnvironment();
        } else if (owner instanceof AdsParagraphExplorerItemDef) {
            ownerEnv = ((AdsParagraphExplorerItemDef) owner).getClientEnvironment();
        }
        if (ownerEnv != null) {
            if (ownerEnv != ERuntimeEnvironmentType.COMMON_CLIENT && explorerItem.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && ownerEnv != explorerItem.getClientEnvironment()) {
                error(explorerItem, problemHandler, "Client environment of explorer item (" + explorerItem.getClientEnvironment().getName() + " ) does not match to it's owner client environment (" + ownerEnv.getName() + ")");
            }
        }

//        AdsDefinition ownerDef = explorerItem.getOwnerDef();
//        if (ownerDef instanceof AdsEditorPresentationDef) {
//            AdsEditorPresentationDef presentation = (AdsEditorPresentationDef) ownerDef;
//            if (presentation.isExplorerItemsInherited()) {
//                AdsExplorerItemDef ovr = explorerItem.getHierarchy().findOverridden();
//                if (ovr == null) {
//                    problemHandler.accept(RadixProblem.Factory.newError(explorerItem, "Explorer item does not overrides any of explorer items of base editor presentation. Delete this item"));
//                } else {
//                    if (ovr.getClass() != explorerItem.getClass()) {
//                        problemHandler.accept(RadixProblem.Factory.newError(explorerItem, "Explorer item type does not match to type of overriden explorer item"));
//                    }
//                }
//            }
//        }
    }
}
