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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import java.util.Collection;
import java.util.Collections;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemsNode;


public class ExplorerItemListProvider extends MixedNodeChildrenProvider<RadixObject> {

    RadixObject context;
    private static final Key ALWAYS_EXIST = new Key() {
    };

    @Override
    public Collection<Key> updateKeys() {
//        if (context instanceof AdsEditorPresentationDef) {
//            AdsEditorPresentationDef epr = (AdsEditorPresentationDef) context;
//            if (epr.isExplorerItemsInherited()) {
//                return Collections.emptySet();
//            }
//        }
        return Collections.singleton(ALWAYS_EXIST);
    }

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key == ALWAYS_EXIST) {
            if (context instanceof AdsEditorPresentationDef) {
                return new AdsExplorerItemsNode(adapter, ((AdsEditorPresentationDef) context).getExplorerItems().getChildren());
            } else {
                return new AdsExplorerItemsNode(adapter, ((AdsParagraphExplorerItemDef) context).getExplorerItems().getChildren());
            }
        } else {
            return null;
        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private IRadixEventListener eiInheritanceListener = null;
    @SuppressWarnings({"rawtypes", "unchecked"})
    private MixedNodeChildrenAdapter adapter;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean enterContext(MixedNodeChildrenAdapter adapter, RadixObject context) {
        this.adapter = adapter;
        if (context instanceof AdsEditorPresentationDef) {
            this.context = context;
//            this.eiInheritanceListener = new IRadixEventListener() {
//
//                @Override
//                public void onEvent(RadixEvent e) {
//                    if (ExplorerItemListProvider.this.adapter != null) {
//                        ExplorerItemListProvider.this.adapter.refresh(ExplorerItemListProvider.this);
//                    }
//                }
//            };
//            ((AdsEditorPresentationDef) context).getExplorerItemsInheritanceChangesSupport().addEventListener(eiInheritanceListener);
            return true;
        } else if (context instanceof AdsParagraphExplorerItemDef) {
            this.context = context;
            return true;
        } else {
            return false;
        }

    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        return null;
    }
}
