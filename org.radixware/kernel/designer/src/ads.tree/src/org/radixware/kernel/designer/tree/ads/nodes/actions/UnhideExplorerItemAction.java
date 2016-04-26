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

import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.EChooseDefinitionDisplayMode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemsNode;


public class UnhideExplorerItemAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        public boolean isEnabled(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof AdsExplorerItemsNode) {
                AdsExplorerItemsNode explorerItemsNode = (AdsExplorerItemsNode) contextNode;
                ExplorerItems rootItems = explorerItemsNode.findRootItems();
                return explorerItemsNode.getRadixObject().getOwnerExplorerItems().getItemsOrder().hasHiddenChildren(rootItems);
            }
            return false;
        }

        public void execute(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof AdsExplorerItemsNode) {
                final AdsExplorerItemsNode explorerItemsNode = (AdsExplorerItemsNode) contextNode;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ExplorerItems rootItems = explorerItemsNode.findRootItems();

                        List<AdsExplorerItemDef> hiddenItems = explorerItemsNode.getRadixObject().getOwnerExplorerItems().getItemsOrder().listHiddenChildren(rootItems);
                        if (!hiddenItems.isEmpty()) {
                            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(hiddenItems);
                            cfg.setStepCount(1);
                            cfg.setDisplayMode(EChooseDefinitionDisplayMode.NAME_AND_LOCATION);
                            List<Definition> selection = ChooseDefinition.chooseDefinitions(cfg);
                            if (selection != null && !selection.isEmpty()) {
                                for (Definition def : selection) {
                                    AdsExplorerItemDef explorerItem = (AdsExplorerItemDef) def;
                                    explorerItemsNode.getRadixObject().getOwnerExplorerItems().getItemsOrder().setItemVisible(rootItems, explorerItem, true);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public UnhideExplorerItemAction() {
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
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            Cookie c = activatedNodes[0].getCookie(Cookie.class);
            if (c != null) {
                c.execute(activatedNodes[0]);
            }
        }
    }

    @Override
    public String getName() {
        return "Unhide children";
    }
}
