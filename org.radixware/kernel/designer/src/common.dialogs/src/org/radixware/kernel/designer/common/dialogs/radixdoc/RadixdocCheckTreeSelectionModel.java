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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class RadixdocCheckTreeSelectionModel extends DefaultTreeSelectionModel {

    static final long serialVersionUID = 0;
    private final TreeModel model;

    public RadixdocCheckTreeSelectionModel(TreeModel model) {
        this.model = model;
        setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }

    public boolean isPartiallySelected(TreePath path) {
        if (isPathSelected(path, true)) {
            return false;
        }
        TreePath[] selectionPaths = getSelectionPaths();
        for (TreePath selectionPath : selectionPaths) {
            if (isDescendant(selectionPath, path)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPathSelected(TreePath path, boolean dig) {
        if (!dig) {
            return super.isPathSelected(path);
        }
         while (path != null && !super.isPathSelected(path)) {
            path = path.getParentPath();
        }
        return path != null;
    }

    private boolean isDescendant(TreePath path1, TreePath path2) {
        Object obj1[] = path1.getPath();
        Object obj2[] = path2.getPath();
        for (int i = 0; i < obj2.length; i++) {
            if (obj1[i] != obj2[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setSelectionPaths(TreePath[] pPaths) {
       TreePath[] selected = getSelectionPaths();
       for(TreePath path : selected) {
           removeSelectionPath(path);
       }
       for(TreePath path : selected) {
           addSelectionPath(path);
       }
    }
    
    @Override
    public void addSelectionPaths(TreePath[] paths) {
        for (TreePath path : paths) {
            TreePath[] selectionPaths = getSelectionPaths();
            ArrayList<TreePath> toBeRemoved = new ArrayList<>();
            for (TreePath selectionPath : selectionPaths) {
                if (isDescendant(selectionPath, path)) {
                    toBeRemoved.add(selectionPath);
                }
            }
            super.removeSelectionPaths((TreePath[]) toBeRemoved.toArray(new TreePath[toBeRemoved.size()]));
        }

        for (TreePath path : paths) {
            TreePath temp = null;
            while (areSiblingsSelected(path)) {
                temp = path;
                if (path.getParentPath() == null) {
                    break;
                }
                path = path.getParentPath();
            }
            if (temp != null) {
                if (temp.getParentPath() != null) {
                    addSelectionPath(temp.getParentPath());
                } else {
                    if (!isSelectionEmpty()) {
                        removeSelectionPaths(getSelectionPaths());
                    }
                    super.addSelectionPaths(new TreePath[]{temp});
                }
            } else {
                super.addSelectionPaths(new TreePath[]{path});
            }
        }
    }

    private boolean areSiblingsSelected(TreePath path) {
        TreePath parent = path.getParentPath();
        if (parent == null) {
            return true;
        }
        Object node = path.getLastPathComponent();
        Object parentNode = parent.getLastPathComponent();
        int childCount = model.getChildCount(parentNode);
        Boolean isParameters = false;
        Boolean isDescription = false;
        for (int i = 0; i < childCount; i++) {
            Object childNode = model.getChild(parentNode, i);
            if (childNode == node) {
                continue;
            }
            if (childCount == 2) {
                if (childNode.toString().equals("parameters") && model.isLeaf(childNode)) {
                    isParameters = true;
                }
                if (childNode.toString().equals("description") && model.isLeaf(childNode)) {
                    isDescription = true;
                }
            }
            if (!isPathSelected(parent.pathByAddingChild(childNode)) && !isParameters && !isDescription) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {
        for (TreePath path : paths) {
            if(path == null) {
                continue;
            }
            if (path.getPathCount() == 1) {
                super.removeSelectionPaths(new TreePath[]{path});
            } else {
                toggleRemoveSelection(path);
            }
        }
    }

    private void toggleRemoveSelection(TreePath path) {

        Stack<TreePath> stack = new Stack<>();
        TreePath parent = path.getParentPath();
        Boolean isParameters = false;
        Boolean isDescription = false;
        while (parent != null && !isPathSelected(parent)) {
            stack.push(parent);
            parent = parent.getParentPath();
        }
        if (parent != null) {
            stack.push(parent);
        } else {
            super.removeSelectionPaths(new TreePath[]{path});
            return;
        }
        while (!stack.isEmpty()) {
            TreePath temp = (TreePath) stack.pop();
            TreePath peekPath = stack.isEmpty() ? path : (TreePath) stack.peek();
            Object node = temp.getLastPathComponent();
            Object peekNode = peekPath.getLastPathComponent();
            int childCount = model.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                Object childNode = model.getChild(node, i);
                if (childNode.toString().equals("parameters") && model.isLeaf(childNode)) {
                    isParameters = true;
                }
                if (childNode.toString().equals("description") && model.isLeaf(childNode)) {
                    isDescription = true;
                }
                if (childNode != peekNode) {
                    if (!isParameters && !isDescription) {
                        super.addSelectionPaths(new TreePath[]{temp.pathByAddingChild(childNode)});
                    }
                }
            }
        }
        super.removeSelectionPaths(new TreePath[]{parent});
    }
    
    public RadixdocTreeModulesModel getModel() {
        return (RadixdocTreeModulesModel) model;
    }
}
