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

package org.radixware.kernel.common.design.msdleditor.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.msdl.MsdlField;


public abstract class Children <T> {
    private DefaultMutableTreeNode parent;
    protected Tree tree;
    public Children(Tree tree, DefaultMutableTreeNode parent) {
        this.parent = parent;
        this.tree = tree;
    }
    public DefaultMutableTreeNode getParent() {
        return parent;
    }
    protected abstract void createKeys(List<T> itemsKeys);
    protected abstract DefaultMutableTreeNode createNodeForKey(T key);
    public void refresh () {
        parent.removeAllChildren();
        List<T> l = new ArrayList<T>();
        createKeys(l);
        for (T cur: l) {
           parent.add(createNodeForKey(cur));
        }
    }
    public void structureChanged() {
        tree.lockTreeSelectionListener = true;
        ((DefaultTreeModel)tree.tree.getModel()).nodeStructureChanged(parent);
        ((DefaultTreeModel)tree.tree.getModel()).nodeChanged(parent);
        tree.lockTreeSelectionListener = false;
        tree.validate();
    }

    void setFieldInTree(MsdlField field) {
        for (int i = 0; i < tree.tree.getRowCount(); i++) {
            TreePath path = tree.tree.getPathForRow(i);
            Object component = path.getLastPathComponent();
            if (component instanceof FieldNode) {
                FieldNode node = (FieldNode) component;                 
                if (node.getFieldModel() == field) {
                    tree.tree.setSelectionRow(i);
                    tree.tree.setSelectionPath(path);
                    break;
                }
            }
        }
    }
}
