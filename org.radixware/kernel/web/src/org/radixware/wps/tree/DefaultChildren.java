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
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.wps.rwt.tree.Node;


class DefaultChildren extends Node.Children {

    private final IExplorerTreeNode paragraphNode;
    private final WpsTree tree;
    private List<Node> nodes = null;

    public DefaultChildren(final WpsTree tree, final IExplorerTreeNode paragraphNode) {
        this.tree = tree;
        this.paragraphNode = paragraphNode;
    }

    @Override
    public List<Node> createNodes() {
        synchronized (this) {
            if (nodes == null) {
                try {
                    nodes = new LinkedList<>();

                    List<IExplorerTreeNode> childNodes = paragraphNode.getChildNodes();
                    for (IExplorerTreeNode childNode : childNodes) {
                        nodes.add(new TreeNode(tree, childNode));
                    }
                } catch (DefinitionNotFoundError e) {
                    nodes = new LinkedList<>();
                    throw e;
                }
            }
        }
        return nodes;
    }

    @Override
    public void reset() {
        super.reset();
        if (nodes != null) {
            nodes.clear();
            nodes = null;
        }
    }
}
