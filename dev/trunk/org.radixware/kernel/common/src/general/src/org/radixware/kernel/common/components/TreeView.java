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

package org.radixware.kernel.common.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Automaticaly ordered and updated JTree.
 * Possibility to expand orcollapse each node directly (node.setExpanded(boolean value))
 * Each node stored its expanded state even if readded.
 */
public class TreeView extends JScrollPane {
    
    public static class Node extends DefaultMutableTreeNode implements Comparable {

        private Color color = null;
        private String displayName = null;
        private Icon icon = null;
        private boolean expanded = true;
        
        public Node(Object userObject) {
            super(userObject);
        }

        public final Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public final String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public final Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public String getToolTipText() {
            return null;
        }

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof Node) {
                final Node node = (Node) obj;
                return getDisplayName().compareTo(node.getDisplayName());
            } else {
                throw new IllegalStateException();
            }
        }

        private int calcIndexForNewChild(Node newChildNode, boolean sorted) {
            int low = 0;
            int high = getChildCount() - 1;
            int mid = 0;
            if (!sorted){ 
                return getChildCount();
            }

            while (low <= high) {
                mid = (low + high) >>> 1;
                final Node midChildNode = (Node) getChildAt(mid);

                int cmp = midChildNode.compareTo(newChildNode);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    return mid;
                }
            }
            return low;
        }

        private void reExpandRecurs() {
            if (expanded) {
                expanded = false;
                setExpanded(true);
            }
            for (int i = 0; i < getChildCount(); i++) {
                final Node node = (Node) getChildAt(i);
                node.reExpandRecurs();
            }
        }

        public void add(Node childNode) {
            add(childNode, true) ;
        }
        
        public void add(Node childNode, boolean sorted) {
            int index = calcIndexForNewChild(childNode, sorted);
            final DefaultTreeModel model = findModel();
            if (model != null) {
                model.insertNodeInto(childNode, this, index);
                if (getChildCount() == 1 && expanded) {
                    reExpandRecurs();
                } else {
                    childNode.reExpandRecurs();
                }
            } else {
                insert(childNode, index);
            }
        }

        @Override
        public Node getParent() {
            return (Node) super.getParent();
        }

        public void remove() {
            final Node parentNode = this.getParent();
            if (parentNode == null) {
                throw new IllegalStateException("Attempt to remove node without parent.");
            }

            final DefaultTreeModel model = findModel();
            if (model != null) {
                model.removeNodeFromParent(this);
            } else {
                parentNode.remove(this);
            }
        }

        public final boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            if (this.expanded == expanded) {
                return;
            }

            this.expanded = expanded;
            final JTree tree = findTree();
            if (tree != null) {
                if (expanded) {
                    tree.expandPath(new TreePath(getPath()));
                } else {
                    tree.collapsePath(new TreePath(getPath()));
                }
            }
        }

        public void clear() {
            removeAllChildren();
            reload();
        }
        
        public void reload(){
            final DefaultTreeModel model = findModel();
            if (model != null) {
                model.reload(this);
            }
        }

        protected DefaultTreeModel findModel() {
            final Node parentNode = this.getParent();
            if (parentNode != null) {
                return parentNode.findModel();
            } else {
                return null;
            }
        }

        protected JTree findTree() {
            final Node parentNode = this.getParent();
            if (parentNode != null) {
                return parentNode.findTree();
            } else {
                return null;
            }
        }
        
        protected void beforePaint(){}
    }

    private static Node getNode(TreePath path) {
        return (Node) path.getLastPathComponent();
    }

    private class RootNode extends Node {

        public RootNode() {
            super(null);
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        protected DefaultTreeModel findModel() {
            return model;
        }

        @Override
        protected JTree findTree() {
            return tree;
        }

        @Override
        public void setExpanded(boolean expanded) {
            // impossible to collapse root
        }
    }

    private static class CellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.setBorderSelectionColor(super.getBackgroundSelectionColor());
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof Node) {
                final Node node = (Node) value;
                node.beforePaint();
                final String text = node.getDisplayName();
                final Icon icon = node.getIcon();
                final Color color = node.getColor();

                setText(text);
                setIcon(icon);
                if (color != null && !sel) {
                    setForeground(color);
                }
            }

            return this;
        }
    }
    private final RootNode rootNode;
    private final JTree tree;
    private final DefaultTreeModel model;
    private final TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            final Node node = getNode(event.getPath());
            node.setExpanded(true);
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            final Node node = getNode(event.getPath());
            node.setExpanded(false);
        }
    };
    private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {

        @Override
        public void mouseMoved(MouseEvent evt) {
            String tooltip = null;
            if (tree.getRowForLocation(evt.getX(), evt.getY()) != -1) {
                final TreePath curPath = tree.getPathForLocation(evt.getX(), evt.getY());
                if (curPath != null) {
                    final Node node = (Node) curPath.getLastPathComponent();
                    if (node != null) {
                        tooltip = node.getToolTipText();
                    }
                }

            }
            tree.setToolTipText(tooltip);
        }
    };
    private final MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                final TreePath treePath = tree.getClosestPathForLocation(e.getX(), e.getY());
                if (!tree.isPathSelected(treePath)) {
                    tree.setSelectionPath(treePath);
                }
                if (treePath == null) {
                    return;
                }
                Object object = treePath.getLastPathComponent();
                if (object != null && object instanceof Node){
                    final JPopupMenu popupMenu = getPopupMenu((Node) object);
                    if (popupMenu != null) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        }
    };

    public TreeView() {
        super();
        rootNode = new RootNode();
        model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new CellRenderer());
        tree.addTreeExpansionListener(treeExpansionListener);
        tree.addMouseMotionListener(mouseMotionListener);
        tree.addMouseListener(mouseListener);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        tree.setToggleClickCount(0); // disable expand/collapse through double click
        setViewportView(rootNode.findTree());

        final InputMap inputMap = tree.getInputMap(WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), DefaultEditorKit.copyAction);
    }
    
    public void setSelectionMode(int selectionMode){
        tree.getSelectionModel().setSelectionMode(selectionMode);
    }    

    public Node getRootNode() {
        return rootNode;
    }

    public Node getLastSelectedNode() {
        final Node result = (Node) tree.getLastSelectedPathComponent();
        return result;
    }

    public Object getLastSelectedUserObject() {
        final Node node = getLastSelectedNode();
        if (node != null) {
            return node.getUserObject();
        } else {
            return null;
        }
    }

    public Node[] getSelectedNodes() {
        final TreePath[] treePaths = tree.getSelectionPaths();

        if (treePaths == null || treePaths.length == 0) {
            return null;
        }

        final Node[] result = new Node[treePaths.length];
        for (int i = 0; i < treePaths.length; i++) {
            final Node node = (Node) treePaths[i].getLastPathComponent();
            result[i] = node;
        }
        return result;
    }

    public Object[] getSelectedUserObjects() {
        final Node[] selectedNodes = getSelectedNodes();
        if (selectedNodes == null || selectedNodes.length == 0) {
            return null;
        }

        final Object[] result = new Object[selectedNodes.length];
        for (int i = 0; i < selectedNodes.length; i++) {
            final Node node = selectedNodes[i];
            result[i] = node.getUserObject();
        }
        return result;
    }

    public JPopupMenu getPopupMenu() {
        return null;
    }
    
    public JPopupMenu getPopupMenu(Node node) {
        return getPopupMenu();
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        tree.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        tree.removeMouseListener(l);
    }

    public void addTreeSelectionListener(TreeSelectionListener l) {
        tree.addTreeSelectionListener(l);
    }

    public void removeTreeSelectionListener(TreeSelectionListener l) {
        tree.removeTreeSelectionListener(l);
    }

    private void fillPaths(final Node node, List<TreePath> paths, Set userObjects) {
        final Object userObject = node.getUserObject();
        if (userObjects.contains(userObject)) {
            final TreePath path = new TreePath(node.getPath());
            paths.add(path);
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            fillPaths((Node) node.getChildAt(i), paths, userObjects);
        }
    }

    public void setSelection(Set userObjects) {
        final List<TreePath> paths = new ArrayList<TreePath>();
        fillPaths(rootNode, paths, userObjects);
        tree.setSelectionPaths(paths.toArray(new TreePath[0]));
    }
    
    public void setRootVisible(boolean rootVisible) {
        tree.setRootVisible(rootVisible);
    }
    
    protected void addAction(Action a, String name, String key) {
        tree.getActionMap().put(name, a);
        tree.getInputMap().put(KeyStroke.getKeyStroke(key), name);
    }
}
