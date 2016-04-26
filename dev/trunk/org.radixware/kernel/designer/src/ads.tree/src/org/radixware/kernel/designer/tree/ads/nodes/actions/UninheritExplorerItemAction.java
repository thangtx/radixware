/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.tree.ads.nodes.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemNode;

/**
 *
 * @author akrylov
 */
public class UninheritExplorerItemAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        private final AdsExplorerItemNode node;

        public Cookie(AdsExplorerItemNode node) {
            this.node = node;
        }

        public void perform() {
            ExplorerItems items = node.findParentItems();
            if (items == null) {
                return;
            }
            if (items.isReadOnly()) {
                return;
            }
            AdsExplorerItemDef explorerItem = (AdsExplorerItemDef) node.getRadixObject();
            if (explorerItem == null) {
                return;
            }
            items.uninheritChild(explorerItem.getId());
        }

        public boolean isEnabled() {
            ExplorerItems items = node.findParentItems();
            if (items == null) {
                return false;
            }
            if (items.isReadOnly()) {
                return false;
            }

            if (items.getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                AdsEditorPresentationDef epr = (AdsEditorPresentationDef) items.getOwnerDefinition();
                if (epr.isExplorerItemsInherited()) {
                    return false;
                }
                AdsExplorerItemDef explorerItem = (AdsExplorerItemDef) node.getRadixObject();
                if (explorerItem == null) {
                    return false;
                }
                if (explorerItem.getOwnerExplorerItems() != items) {
                    return true;
                }
            } else if (items.getOwnerDefinition() instanceof AdsParagraphExplorerItemDef) {
                AdsParagraphExplorerItemDef paragraph = (AdsParagraphExplorerItemDef) items.getOwnerDefinition();
                if (paragraph.isExplorerItemsInherited()) {
                    return false;
                }
                AdsExplorerItemDef explorerItem = (AdsExplorerItemDef) node.getRadixObject();
                if (explorerItem == null) {
                    return false;
                }
                if (explorerItem.getOwnerExplorerItems() != items) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        if (c == null) {
            return false;
        }
        return c.isEnabled();
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
    protected void performAction(Node[] nodes) {
        if (nodes.length < 1) {
            return;
        }
        final Cookie c = nodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.perform();
        }
    }

    @Override
    public String getName() {
        return "Uninherit Item";
    }

}
