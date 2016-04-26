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

package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import java.util.*;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.ads.editors.refactoring.components.RadixObjectTreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreePanel;


final class ClassHierarchyTreePanel extends TreePanel {

    public ClassHierarchyTreePanel() {
        super();
    }

    private Node getRootContext(AdsClassDef def, TreeNode.INodeSelector<RadixObject> filter) {
        final Set<RadixObject> collision = new HashSet<>();
        final ClassHierarchyChildFactory factory = new ClassHierarchyChildFactory(def, filter, collision);
        final List<AdsClassDef> childs = new ArrayList<>();
        factory.createKeys(childs);
        if (childs.size() > 0) {
            synchronized (collision) {
                collision.clear();
            }
            return new RadixObjectTreeNode<>(Children.create(factory, false), def);
        } else {
            return new RadixObjectTreeNode<>(Children.LEAF, def);
        }
    }

    public void open(AdsClassDef def, TreeNode.INodeSelector<RadixObject> filter) {
        super.open(getRootContext(def, filter));
    }

    @Override
    public AdsClassDef getSelectedItem() {
        final RadixObject selectedItem = (RadixObject) super.getSelectedItem();
        if (selectedItem instanceof AdsClassDef) {
            return (AdsClassDef) selectedItem;
        }
        return null;
    }
}