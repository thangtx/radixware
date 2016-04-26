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

import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.tree.ads.nodes.actions.RemoveBrokenExplorerItemAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;


public class BrokenExplorerItemNode extends AbstractNode {

    private RemoveBrokenExplorerItemAction.Cookie removeCookie = new RemoveBrokenExplorerItemAction.Cookie();
    private ExplorerItemsOrder.ExplorerItemRef ref;
    private InstanceContent lookupContent;
    private MixedNodeChildrenAdapter adapter;

    public BrokenExplorerItemNode(MixedNodeChildrenAdapter adapter, ExplorerItemsOrder.ExplorerItemRef ref) {
        this(ref, new InstanceContent());
        this.adapter = adapter;
    }

    private BrokenExplorerItemNode(ExplorerItemsOrder.ExplorerItemRef ref, InstanceContent lookup) {
        super(Children.LEAF, new AbstractLookup(lookup));
        this.ref = ref;
        this.lookupContent = lookup;
        this.lookupContent.add(removeCookie);
    }

    @Override
    public String getHtmlDisplayName() {
        return "<font color=\"#FF0000\">" + ref.getId().toString() + "</font>";
    }

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

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(RemoveBrokenExplorerItemAction.class)
        };
    }

    public ExplorerItems findParentItems() {
        Node parentNode = adapter.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof AdsExplorerItemsNode) {
                return ((AdsExplorerItemsNode) parentNode).getRadixObject().getOwnerExplorerItems();
            }
            parentNode = parentNode.getParentNode();
        }
        return null;
    }

    public Id getExplorerItemId() {
        return ref.getId();
    }
}
