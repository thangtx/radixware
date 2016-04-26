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

package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreePanel;


final class MembersTreePanel extends TreePanel {

    public MembersTreePanel() {
        super();
    }

    private Node getRootContext(AdsDefinition def, TreeNode.INodeSelector<RadixObject> filter) {
        return SubstituteTreeFactory.createSubstituteTree(def, filter);
    }

    public void open(AdsDefinition def, TreeNode.INodeSelector<RadixObject> filter) {
        super.open(getRootContext(def, filter));
    }

    @Override
    public AdsDefinition getSelectedItem() {
        Node[] selectedNodes = getExplorerManager().getSelectedNodes();

        if (selectedNodes != null && selectedNodes.length > 0) {
            for (Node node : selectedNodes) {
                if (node.isLeaf() && node instanceof TreeNode) {
                    final RadixObject object = (RadixObject) ((TreeNode) node).getObject();
                    if (object instanceof AdsDefinition) {
                        return (AdsDefinition) ((TreeNode) node).getObject();
                    }
                }
            }
        }
        
        return null;
    }
}