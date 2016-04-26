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

package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.*;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.explorer.view.QuickSearchTableFilter;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;
import org.openide.util.Lookup;


public class TreePanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    public static class DefaultFilter<T> implements TreeNode.INodeSelector<T> {

        @Override
        public boolean acceptClass(Class<? extends T> nodeClass) {
            return true;
        }

        @Override
        public boolean acceptNode(T node) {
            return true;
        }

        @Override
        public boolean acceptNode(TreeNode<T> treeNode) {
            return true;
        }
    }
    private OutlineView treeView;
    private final Lookup lookup;
    private final ExplorerManager explorerManager = new ExplorerManager();

    public TreePanel() {
        initComponents();

        lookup = ExplorerUtils.createLookup(explorerManager, new ActionMap());

        explorerManager.addPropertyChangeListener(new SelectNodeListener());
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

        treeView = new OutlineView("");


        treeView.getOutline().setRootVisible(false);
        treeView.getOutline().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeView.setBorder(BorderFactory.createEtchedBorder());
        treeView.getOutline().setTableHeader(null);
        treeView.getOutline().setShowGrid(false);

        treeView.setQuickSearchTableFilter(new QuickSearchTableFilter() {
            @Override
            public String getStringValueAt(int row, int col) {
                Object val = treeView.getOutline().getModel().getValueAt(row, col);
                Node node = Visualizer.findNode(val);
                if (node != null) {
                    String title = node.getDisplayName();
                    int spaceIndex = title.indexOf(" ");
                    if (spaceIndex > 0) {
                        title = title.substring(spaceIndex + 1);
                    }
                    return title;
                }
                return "";
            }
        }, true);

        //treeView.setQuickSearchAllowed(false);


        add(treeView);
    }

    public void open(Node root) {
        explorerManager.setRootContext(root);
        expandAll();
    }

    public Object getSelectedItem() {
        Node[] selectedNodes = explorerManager.getSelectedNodes();
        if (selectedNodes.length > 0) {
            Node node = selectedNodes[0];
            if (node instanceof TreeNode) {
                return ((TreeNode) node).getObject();
            }
        }
        return null;
    }

    private final class SelectNodeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
                Object val = evt.getNewValue();
                if (val instanceof Node[]) {
                    if (((Node[]) val).length == 0) {
                        return;
                    }
                }
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        }
    }

    /**
     * Expands all tree nodes by depth-first search
     */
    public void expandAll() {
        Node root = explorerManager.getRootContext();
        if (root != null && !root.isLeaf()) {

            Stack<Node> stack = new Stack<>();
            stack.push(root);

            while (!stack.isEmpty()) {

                Node current = stack.pop();
                boolean expand = true;
                Node[] nodes = current.getChildren().getNodes(true);
                for (Node node : nodes) {
                    if (!node.isLeaf()) {
                        stack.push(node);
                        expand = false;
                    }
                }
                if (expand) {
                    treeView.expandNode(current);
                }
            }
        }
    }

    private interface IVisitor<T> {

        boolean accept(T target);
    }

    /**
     * Breadth-first search
     *
     * @param visitor
     */
    private void search(IVisitor<Node> visitor) {
        Node root = explorerManager.getRootContext();
        if (root != null && !root.isLeaf()) {

            Queue<Node> stack = new LinkedList<>();
            stack.add(root);

            while (!stack.isEmpty()) {

                Node current = stack.poll();

                @SuppressWarnings("unchecked")
                Enumeration<Node> children = current.getChildren().nodes();
                while (children.hasMoreElements()) {
                    Node node = children.nextElement();
                    stack.add(node);
                }

                if (visitor.accept(current)) {
                    return;
                }
            }
        }
    }

    public void selectNodeBy(final Object object) {
        if (object != null) {
            search(new IVisitor<Node>() {
                @Override
                public boolean accept(Node target) {
                    if (target instanceof TreeNode && Objects.equals(object, ((TreeNode) target).getObject())) {
                        try {
                            explorerManager.setSelectedNodes(new Node[]{target});
                            return true;
                        } catch (PropertyVetoException ex) {
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public final void setRootVisible(boolean visible) {
        treeView.getOutline().setRootVisible(visible);
    }
}