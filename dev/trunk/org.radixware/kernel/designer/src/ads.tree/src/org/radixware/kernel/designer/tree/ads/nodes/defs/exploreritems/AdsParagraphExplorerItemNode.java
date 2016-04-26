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

package org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation.CustomViewProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation.ExplorerItemListProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation.ModelClassProvider;


public class AdsParagraphExplorerItemNode extends AdsExplorerItemNode<AdsParagraphExplorerItemDef> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        ExplorerItems rootItems = findRootItems();
        ExplorerItems ownerItems = getRadixObject().getOwnerExplorerItems();
        if (rootItems == null || rootItems == ownerItems || rootItems.isParentOf(ownerItems)) {
            return new Class[]{
                ModelClassProvider.class,
                CustomViewProvider.class,
                ExplorerItemListProvider.class
            };
        } else {
            return new Class[]{
                ExplorerItemListProvider.class
            };
        }
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsParagraphExplorerItemDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsParagraphExplorerItemDef ddsPlSqlObject) {
            return new AdsParagraphExplorerItemNode(null, ddsPlSqlObject);
        }
    }

    @SuppressWarnings("unchecked")
    protected AdsParagraphExplorerItemNode(MixedNodeChildrenAdapter adapter, AdsParagraphExplorerItemDef paragraph) {
        super(adapter, paragraph);
    }    
}
