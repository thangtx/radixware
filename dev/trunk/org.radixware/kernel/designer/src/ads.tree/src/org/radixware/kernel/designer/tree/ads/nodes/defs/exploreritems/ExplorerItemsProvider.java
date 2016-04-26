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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.EntireChangesSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder;
import org.radixware.kernel.designer.ads.editors.creation.AdsExplorerItemCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider.Key;


public class ExplorerItemsProvider extends MixedNodeChildrenProvider<RadixObject> {

    private MixedNodeChildrenAdapter adapter;
    ExplorerItems.Children children;
    
    private final EntireChangesSupport.EntireChangeListener inheritanceListener = new EntireChangesSupport.EntireChangeListener() {
        @Override
        public void onEvent(EntireChangesSupport.EntireChangeEvent e) {
            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setContextClassLoader(null);
                    if (adapter != null) {
                        adapter.refresh(ExplorerItemsProvider.this);
                    }
                }
            });
        }
    };

    public ExplorerItemsProvider() {
        EntireChangesSupport.getInstance(AdsExplorerItemDef.class).addEventListener(inheritanceListener);
    }
    private final ContainerChangesListener containerListener = new ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            if (children != null && adapter != null) {
                adapter.refresh(ExplorerItemsProvider.this);
            }
        }
    };

    public ExplorerItems findRootItems() {
        Node parentNode = adapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsExplorerItemsNode) {
                return ((AdsExplorerItemsNode) parentNode).findRootItems();
            }
            parentNode = parentNode.getParentNode();
        }
        return null;
    }
    private List<Key> lastComputedKeys = null;

    @Override
    public Collection<Key> updateKeys() {
        ArrayList<Key> keys = new ArrayList<>();
        ArrayList<Key> old = null;
        synchronized (this) {
            if (lastComputedKeys != null) {
                old = new ArrayList<>(lastComputedKeys);
            }
        }
        java.util.Map<AdsExplorerItemDef, Key> keysByItem = new HashMap<>();
        if (old != null) {
            for (Key key : old) {
                if (key instanceof AnyKey && ((AnyKey) key).obj instanceof ExplorerItemsOrder.ExplorerItemRef) {
                    AdsExplorerItemDef item = ((ExplorerItemsOrder.ExplorerItemRef) ((AnyKey) key).obj).getExplorerItem();
                    if (item != null) {
                        keysByItem.put(item, key);
                    }
                }
            }
        }
        for (ExplorerItemsOrder.ExplorerItemRef ref : children.getOwnerExplorerItems().getItemsOrder().listChildren(findRootItems())) {
            if (ref.getExplorerItem() != null) {
                Key exKey = keysByItem.get(ref.getExplorerItem());
                if (exKey != null) {
                    keys.add(exKey);
                    continue;
                }
            }
            keys.add(Key.Factory.wrap(ref));
        }
        synchronized (this) {
            lastComputedKeys = keys;
        }
        return keys;
    }
    private final java.util.Map<AdsExplorerItemDef, Node> item2node = new WeakHashMap<>();

    @Override
    protected Node findOrCreateNode(Key key) {
        if (key instanceof ObjKey) {
            AdsExplorerItemDef item = (AdsExplorerItemDef) ((ObjKey) key).getObject();
            if (item != null) {
                return NodesManager.findOrCreateNode(item);
            } else {
                return null;
            }
        } else if (key instanceof AnyKey) {
            ExplorerItemsOrder.ExplorerItemRef ref = ((ExplorerItemsOrder.ExplorerItemRef) ((AnyKey) key).obj);
            AdsExplorerItemDef explorerItem = ref.getExplorerItem();
            if (explorerItem != null) {
                synchronized (item2node) {
                    Node node = item2node.get(explorerItem);
                    if (node == null) {
                        if (explorerItem instanceof AdsChildRefExplorerItemDef) {
                            node = new AdsChildRefExplorerItemNode(adapter, (AdsChildRefExplorerItemDef) explorerItem);
                        } else if (explorerItem instanceof AdsEntityExplorerItemDef) {
                            node = new AdsEntityExplorerItemNode(adapter, (AdsEntityExplorerItemDef) explorerItem);
                        } else if (explorerItem instanceof AdsParagraphExplorerItemDef) {
                            node = new AdsParagraphExplorerItemNode(adapter, (AdsParagraphExplorerItemDef) explorerItem);
                        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
                            node = new AdsParagraphLinkExplorerItemNode(adapter, (AdsParagraphLinkExplorerItemDef) explorerItem);
                        } else if (explorerItem instanceof AdsParentRefExplorerItemDef) {
                            node = new AdsParentRefExplorerItemNode(adapter, (AdsParentRefExplorerItemDef) explorerItem);
                        }
                        if (node != null) {
                            item2node.put(explorerItem, node);
                        }
                    }
                    return node;
                }
            } else {
                return new BrokenExplorerItemNode(adapter, ref);
            }
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean enterContext(MixedNodeChildrenAdapter adapter, RadixObject context) {
        this.adapter = adapter;
        children = ((ExplorerItems.Children) context);
        children.getLocal().getContainerChangesSupport().addEventListener(containerListener);
        return true;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups() {
        if (children == null || isInherited()) {
            return null;
        }
        return new ICreatureGroup[]{
            new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    return AdsExplorerItemCreature.Factory.createInstances((AdsDefinitions) children.getLocal());
                }

                @Override
                public String getDisplayName() {
                    return "Explorer Items";
                }
            }
        };
    }

    @Override
    protected boolean isSorted() {
        return true;
    }

    private boolean isInherited() {
        Node parentNode = adapter.getParentNode();
        return parentNode instanceof AdsExplorerItemsNode && ((AdsExplorerItemsNode) parentNode).isNodeInherited();
    }

    @Override
    protected void reorder(int[] perm) {
        children.getOwnerExplorerItems().getItemsOrder().reorder(findRootItems(), perm);
        //children.getLocal().reorder(perm);
    }
}
