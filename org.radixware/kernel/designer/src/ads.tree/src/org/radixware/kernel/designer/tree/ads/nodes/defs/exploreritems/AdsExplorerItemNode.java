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

import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.HideExplorerItemAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideExplorerItemAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.UninheritExplorerItemAction;

import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

public class AdsExplorerItemNode<T extends AdsExplorerItemDef> extends AdsMixedNode<T> {

    private final HideExplorerItemAction.Cookie hideCookie;
    private final OverrideExplorerItemAction.Cookie overrideCookie;

    public AdsExplorerItemNode(MixedNodeChildrenAdapter adapter, T definition) {
        super(definition, adapter);
        addCookie(hideCookie = new HideExplorerItemAction.Cookie());
        addCookie(overrideCookie = new OverrideExplorerItemAction.Cookie());
        addCookie(new UninheritExplorerItemAction.Cookie(this));
        checkColorings(true);
    }

    private void checkColorings(final boolean sync) {
        if (sync) {
            updateColorings(true);
        } else {
            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    updateColorings(false);
                }
            });
        }
    }

    private void updateColorings(boolean sync) {
        do {
            if (parentAdapter == null) {
                return;
            }
            final ExplorerItems ei = findRootItems();
            if (ei == null) {
                try {
                    if (sync) {
                        return;
                    }
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    return;
                }
            } else {
                final AdsExplorerItemDef item = getExplorerItem();

                final boolean grayed[] = new boolean[]{false};

                final ExplorerItems ownerItems = item.getOwnerExplorerItems();

                if (ownerItems != ei && !ei.isParentOf(ownerItems)) {
                    setGrayed(true);
                } else {
                    setGrayed(false);
                }

                if (item.getHierarchy().findOverridden().get() != null || item.getHierarchy().findOverwritten().get() != null) {
                    setBold(true);
                } else {
                    setBold(false);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireDisplayNameChange(null, null);
                    }
                });
                return;
            }
        } while (!sync);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return null;
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        if (isNodeInherited()) {
            actions.add(SystemAction.get(HideExplorerItemAction.class));
            actions.add(SystemAction.get(OverrideExplorerItemAction.class));
            actions.add(SystemAction.get(UninheritExplorerItemAction.class));
        } else {
            super.addCustomActions(actions);
        }
    }

    @Override
    protected DefinitionRenameAction.RenameCookie createRenameCookie() {
        if (isNodeInherited()) {
            return null;
        }
        return super.createRenameCookie(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canRename() {
        return !isNodeInherited();
    }

    @Override
    public boolean canCopy() {
        return super.canCopy();//!isNodeInherited(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canCut() {
        return super.canCut();//!isNodeInherited();
    }

    @Override
    public boolean canDestroy() {
        return !isNodeInherited();
    }

//    @Override
//    protected RenameCookie createRenameCookie() {
//        if (getRadixObject().getDefinitionType() == EDefType.PARAGRAPH || getRadixObject().getDefinitionType() == EDefType.PARAGRAPH_LINK) {
//            return super.createRenameCookie();
//        } else {
//            return null;
//        }
//    }
    @Override
    public boolean noFilter() {
        return true; //To change body of generated methods, choose Tools | Templates.
    }

    public ExplorerItems findRootItems() {
        if (parentAdapter == null) {
            return null;
        }
        Node parentNode = parentAdapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsExplorerItemsNode) {
                return ((AdsExplorerItemsNode) parentNode).findRootItems();
            }
            if (parentNode instanceof AdsMixedNode) {
                MixedNodeChildrenAdapter adapter = ((AdsMixedNode) parentNode).parentAdapter;
                if (adapter != null) {
                    parentNode = adapter.getParentNode();
                } else {
                    parentNode = parentNode.getParentNode();
                }
            } else {
                parentNode = parentNode.getParentNode();
            }
        }
        return null;
    }

    public ExplorerItems findParentItems() {
        if (parentAdapter == null) {
            return null;
        }
        Node parentNode = parentAdapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsExplorerItemsNode) {
                return ((AdsExplorerItemsNode) parentNode).getRadixObject().getOwnerExplorerItems();
            }
            if (parentNode instanceof AdsMixedNode) {
                MixedNodeChildrenAdapter adapter = ((AdsMixedNode) parentNode).parentAdapter;
                if (adapter != null) {
                    parentNode = adapter.getParentNode();
                } else {
                    parentNode = parentNode.getParentNode();
                }
            } else {
                parentNode = parentNode.getParentNode();
            }
        }
        return null;
    }

    public AdsExplorerItemDef getExplorerItem() {
        return getRadixObject();
    }

    public boolean isNodeInherited() {
        ExplorerItems peis = findParentItems();
        if (peis == null) {
            return false;
        }
        ExplorerItems ei = findRootItems();
        if (ei == null) {
            return false;
        }
        AdsExplorerItemDef item = getRadixObject();

        ExplorerItems ownerItems = item.getOwnerExplorerItems();
        if (ownerItems == null) {
            return false;
        }

        if (ownerItems != ei && !ei.isParentOf(ownerItems)) {
            return true;
        } else {
            return false;
        }
    }
}
