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
package org.radixware.kernel.designer.tree.ads.nodes.actions;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemNode;

public class OverrideExplorerItemAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        public boolean isEnabled(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof AdsExplorerItemNode) {
                AdsExplorerItemNode filterNode = (AdsExplorerItemNode) contextNode;
                if (filterNode.isNodeInherited()) {
//                    ExplorerItems rootItems = filterNode.findRootItems();
//                    if (rootItems != null) {
                    ExplorerItems ownerItems = filterNode.findParentItems();
//                        if (rootItems == ownerItems) {

                    AdsExplorerItemDef explorerItem = filterNode.getExplorerItem();
                    if (explorerItem == null) {
                        return false;
                    }
                    if (explorerItem.getOwnerDef().getId() == ownerItems.getOwnerDefinition().getId()) {
                        return explorerItem.allowOverwrite();
                    } else {
                        return true;
                    }
//                        } else {
//                            return false;
//                        }
//                    } else {
//                        return false;
//                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @SuppressWarnings({"rawtypes"})
        public void override(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof AdsExplorerItemNode) {
                AdsExplorerItemNode filterNode = (AdsExplorerItemNode) contextNode;
                if (filterNode.isNodeInherited()) {
//                    ExplorerItems rootItems = filterNode.findRootItems();
//                    if (rootItems != null) {
                    ExplorerItems ownerItems = filterNode.findParentItems();
                    //if (rootItems == ownerItems) {//may overwrite
                    AdsExplorerItemDef explorerItem = filterNode.getExplorerItem();
                    final AdsExplorerItemDef result;
                    if (explorerItem.getOwnerDef().getId() == ownerItems.getOwnerDefinition().getId()) {//detect overwrite
                        result = ownerItems.getChildren().overwrite(explorerItem);
                    } else {
                        result = ownerItems.getChildren().override(explorerItem);
                    }

                    if (result != null) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                NodesManager.selectInProjects(result);
                            }
                        });
                    }
                }
                   // }
                // }
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            Cookie c = activatedNodes[0].getCookie(Cookie.class);
            return c != null && c.isEnabled(activatedNodes[0]);
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            Cookie c = activatedNodes[0].getCookie(Cookie.class);
            if (c != null) {
                c.override(activatedNodes[0]);
            }
        }
    }

    @Override
    public String getName() {
        return "Override";
    }
}
