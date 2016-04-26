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

import java.util.List;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class ExplorerItemsChecker extends RadixObjectChecker<ExplorerItems> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return ExplorerItems.class;
    }

    @Override
    public void check(ExplorerItems radixObject, IProblemHandler problemHandler) {
        super.check(radixObject, problemHandler);
        Definition ownerDef = radixObject.getOwnerDefinition();
        boolean isRoot = false;
        if (ownerDef instanceof AdsEditorPresentationDef) {
            isRoot = true;
        } else if (ownerDef instanceof AdsParagraphExplorerItemDef && ((AdsParagraphExplorerItemDef) ownerDef).isTopLevelDefinition()) {
            isRoot = true;
        }
        if (isRoot) {
            ExplorerItemsOrder order = radixObject.getItemsOrder();
            checkRoots(radixObject, order, order.getOrderRoots(), problemHandler);
            checkRoots(radixObject, order, order.getVisibilityRoots(), problemHandler);
        }

    }

    private void checkRoots(ExplorerItems radixObject, ExplorerItemsOrder order, List<Id> roots, IProblemHandler problemHandler) {
        for (Id id : roots) {
            Definition root = findRoot(radixObject, id);
            if (root != null) {
                ExplorerItems lookuper = null;
                if (root instanceof AdsEditorPresentationDef) {
                    lookuper = ((AdsEditorPresentationDef) root).getExplorerItems();
                } else if (root instanceof AdsParagraphExplorerItemDef) {
                    lookuper = ((AdsParagraphExplorerItemDef) root).getExplorerItems();
                }
                for (ExplorerItemsOrder.ExplorerItemRef ref : radixObject.getItemsOrder().listChildren(lookuper)) {
                    if(ref.getExplorerItem()==null){
                        problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Child explorer item not found: #" + ref.getId()));
                    }
                }
            }
        }
    }

    private Definition findRoot(ExplorerItems root, Id id) {
        if (id == root.getOwnerDefinition().getId()) {
            return root.getOwnerDefinition();
        } else {
            return root.findChildParagraphById(id, ExtendableDefinitions.EScope.ALL);
        }
    }
}
