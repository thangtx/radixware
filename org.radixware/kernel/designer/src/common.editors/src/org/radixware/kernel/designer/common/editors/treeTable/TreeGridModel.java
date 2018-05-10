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

package org.radixware.kernel.designer.common.editors.treeTable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.Utils;

public class TreeGridModel extends AbstractTreeTableModel {

    protected String[] cNames;
    protected Class[] cTypes;

    @Override
    public boolean isCellEditable(Object node, int column) {
        TreeGridNode currNode = (TreeGridNode) node;
        return currNode.gridItem.isMayModify(column);
        //return super.isCellEditable(node, column);
    }

    public TreeGridModel(TreeGridRow root_, String[] cNames, Class[] cTypes) {
        super(null);
        //root_
        this.cNames = cNames;
        this.cTypes = cTypes;
        openRoot(root_);
    }

    public void openRoot(TreeGridRow root_) {
        if (root_ != null) {
            root = new TreeGridNode(cNames.length, root_);
            reloadChildren(root);
        }
    }

    @Override
    public int getChildCount(Object node) {
        Object[] children = getChildren(node);
        return (children == null) ? 0 : children.length;
    }

    @Override
    public Object getChild(Object node, int i) {
        return getChildren(node)[i];
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((TreeGridNode) node).isLeaf();
    }

    @Override
    public int getColumnCount() {
        return cNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return cNames[column];
    }

    @Override
    public Class getColumnClass(int column) {
        return cTypes[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        TreeGridNode fn = (TreeGridNode) node;
        if (column == 0) {
            return fn;
        }
        return fn.values[column];
    }

    @Override
    public void setValueAt(Object aValue, Object node, int column) {
        if (column == 0) {
            return;
        }
        TreeGridNode tgn = (TreeGridNode) node;
        if (!Utils.equals(tgn.values[column], aValue)) {
            tgn.values[column] = aValue;
            tgn.gridItem.CellWasModified(column, aValue);
        }
    }

    public void reloadChildren(Object node) {
        (new NodeLoaderEx((TreeGridNode) node)).Do();
    }

    protected Object[] getChildren(Object node) {
        TreeGridNode fileNode = ((TreeGridNode) node);
        return fileNode.getOrCreateChilds();
        // OrCreateChilds()
    }
    private static TreeGridNode[] EMPTY_CHILDREN = new TreeGridNode[0];

    public class TreeGridNode {

        protected TreeGridRow gridItem;
        protected TreeGridNode parent;
        protected TreeGridNode[] children;
        protected Object[] values;

        public TreeGridRow getGridItem() {
            return gridItem;
        }

        protected TreeGridNode(int colCount, TreeGridRow item) {
            this(colCount, null, item);
        }

        protected TreeGridNode(int colCount, TreeGridNode parent, TreeGridRow gridItem) {
            this.parent = parent;
            this.gridItem = gridItem;
            values = new Object[colCount];
            gridItem.setRowEx(this);
            gridItem.loadValues();

        }

        public void setValues(Object[] val) {
            values = val;
            repaint();
        }

        public Object[] getValues() {
            return values;
        }

        @Override
        public String toString() {
            return gridItem.getTitle(0);
        }

        public TreeGridNode getParent() {
            return parent;
        }

        public boolean isLeaf() {
            return gridItem != null ? !gridItem.isHaveChilds() : false;
        }

        public TreeGridNode[] getChilds() {
            return children;
        }

        public TreeGridNode[] getOrCreateChilds() {
            if (children == null && gridItem.isHaveChilds()) {
                loadChildren();
            }
            return children;
        }

        protected void loadChildren() {
            children = createChildren();
        }

        protected TreeGridNode[] createChildren() {
            TreeGridNode[] retArray = null;

            try {
                List<? extends TreeGridRow> lst = gridItem == null ? null : gridItem.getChilds();
                if (lst != null) {
                    retArray = new TreeGridNode[lst.size()];
                    int i = 0;
                    for (TreeGridRow item : lst) {
                        retArray[i] = new TreeGridNode(values.length, this, item);
                        i++;
                    }
                }
            } catch (SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            if (retArray == null) {
                retArray = EMPTY_CHILDREN;
            }
            return retArray;
        }

        protected boolean loadedChildren() {
            return (!gridItem.isHaveChilds() || (children != null));
        }

        protected void setChildren(TreeGridNode[] newChildren) {
            children = newChildren;
        }
    }

    public class NodeLoaderEx {

        TreeGridNode node;

        NodeLoaderEx(TreeGridNode node) {
            this.node = node;
            node.setChildren(node.createChildren());
        }

        public void Do() {
            TreeGridNode[] children = node.getOrCreateChilds();
            for (int counter = children.length - 1; counter >= 0; counter--) {
                if (!children[counter].isLeaf()) {
                    loadChildrenEx(children[counter]);
                }
            }
        }

        void loadChildrenEx(TreeGridNode node) {
            // if (!node.isLoadChilds) return;
            if (!node.isLeaf()) {
                final TreeGridNode[] children = node.createChildren();


                for (int counter = children.length - 1; counter >= 0;
                        counter--) {
                    if (!children[counter].isLeaf()) {
                        children[counter].loadChildren();
                    }
                }
                node.setChildren(children);
            }
        }
    }
}
