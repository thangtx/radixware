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

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.BrokenExplorerItemNode;


public class RemoveBrokenExplorerItemAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        public boolean isEnabled(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof BrokenExplorerItemNode) {
                BrokenExplorerItemNode node = (BrokenExplorerItemNode) contextNode;
                ExplorerItems rootItems = node.findRootItems();
                return rootItems != null && !rootItems.isReadOnly();
            } else {
                return false;
            }
        }

        public void remove(Node contextNode) {
            contextNode = NodeUtils.getOriginalNode(contextNode);
            if (contextNode instanceof BrokenExplorerItemNode) {
                BrokenExplorerItemNode node = (BrokenExplorerItemNode) contextNode;
                ExplorerItems rootItems = node.findRootItems();
                ExplorerItems parentItems = node.findParentItems();
                parentItems.getItemsOrder().removeBrokenReference(rootItems, node.getExplorerItemId());
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length != 1) {
            return false;
        } else {
            Cookie c = activatedNodes[0].getCookie(Cookie.class);
            return c != null && c.isEnabled(activatedNodes[0]);
        }

    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
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
                c.remove(activatedNodes[0]);
            }
        }
    }

    @Override
    public String getName() {
        return "Remove";
    }
}
