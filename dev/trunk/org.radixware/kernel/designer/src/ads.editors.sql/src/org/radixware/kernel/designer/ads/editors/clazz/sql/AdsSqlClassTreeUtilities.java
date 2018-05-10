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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import javax.swing.tree.TreeNode;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef.IUsedTable;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Node;


public class AdsSqlClassTreeUtilities {

    public static Node getParentUsedTableNode(TreeNode treeNode) {
        while (treeNode != null) {
            if (treeNode instanceof Node) {
                Node node = (Node) treeNode;
                if (node.getNodeInfo() != null && node.getNodeInfo().getObject() instanceof IUsedTable) {
                    return node;
                }
            }
            treeNode = treeNode.getParent();
        }
        return null;
    }
}
