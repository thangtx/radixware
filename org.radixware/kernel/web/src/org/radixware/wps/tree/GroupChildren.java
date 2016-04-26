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

package org.radixware.wps.tree;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.wps.rwt.tree.Node;


class GroupChildren extends Node.Children {

    private IExplorerTreeNode node;
    private WpsTree tree;

    public GroupChildren(WpsTree tree, IExplorerTreeNode node) {
        this.node = node;
        this.tree = tree;
    }

    @Override
    protected List<Node> createNodes() {
        List<Node> children = new LinkedList<Node>();
        if (node.getView().isGroupView()) {
            for (IExplorerTreeNode c : node.getChildNodes()) {
                children.add(new TreeNode(tree, c));
            }
        }
        return children;
    }
}
