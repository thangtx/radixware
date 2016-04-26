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

/*
 * MethodUsagesPanel.java
 *
 * Created on Jan 12, 2012, 1:08:39 PM
 */
package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.CheckableNode;
import org.openide.explorer.view.OutlineView;
import org.openide.explorer.view.QuickSearchTableFilter;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;


final class ObjectUsagesPanel extends CardPanel implements ExplorerManager.Provider {

    private static final String USAGES_CARD = "Usages";

    private final class Filter {

        private ObjectUsagesStep.IFilter filter;

        public Filter(ObjectUsagesStep.IFilter filter) {
            this.filter = filter;
        }

        void select(boolean selected) {
            ObjectUsagesPanel.this.updateView();
        }

        public boolean isCheckable(RadixObject object, boolean isLeaf) {
            return filter.isCheckable(object, isLeaf);
        }

        public boolean isSelected(RadixObject object, boolean isLeaf) {
            return filter.isSelected(object, isLeaf);
        }
    }

    private static final class DescriptionProperty extends Node.Property<String> {

        static final String NAME = "Description";
        private final String value;

        public DescriptionProperty(String value) {
            super(String.class);
            this.value = value;
            setName(NAME);
        }

        @Override
        public boolean canRead() {
            return false;
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public boolean canWrite() {
            return false;
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static final class UsagesNode extends RadixObjectTreeNode<RadixObject> implements CheckableNode {

        private final boolean isCheckable;
        private boolean selected;
        private final Filter filter;

        public UsagesNode(Children children, RadixObject object, Filter filter) {
            super(children, object);

            this.isCheckable = isCheckable(children, object, filter);
            this.filter = filter;
            this.selected = isCheckable && filter.isSelected(object, isLeaf());

            if (isLeaf()) {
                final Sheet.Set set = new Sheet.Set();
                set.put(new DescriptionProperty(object.getTypeTitle()));
                getSheet().put(set);
            }
        }

        private boolean isCheckable(Children children, RadixObject object, Filter filter) {
            boolean hasChakableChildren = false;
            if (!isLeaf()) {
                for (Node node : children.getNodes()) {
                    if (node instanceof UsagesNode) {
                        if (((UsagesNode) node).isCheckable) {
                            hasChakableChildren = true;
                            break;
                        }
                    }
                }
            }

            return filter.isCheckable(object, isLeaf()) && (isLeaf() || hasChakableChildren);
        }

        @Override
        public boolean isCheckable() {
            return isCheckable;
        }

        @Override
        public boolean isCheckEnabled() {
            return true;
        }

        @Override
        public Boolean isSelected() {
            return selected;
        }

        @Override
        public void setSelected(Boolean selected) {
            this.selected = selected;
            final Node[] nodes = getChildren().getNodes(true);
            if (nodes != null && nodes.length > 0) {
                for (Node node : nodes) {
                    if (node instanceof UsagesNode) {
                        ((UsagesNode) node).select(selected);
                    }
                }
            }
            if (filter != null) {
                filter.select(selected);
            }
        }

        private void select(boolean selected) {
            if (isCheckable) {
                this.selected = selected;
            }
        }

        @Override
        public Image getIcon(final int type) {
            RadixObject obj = getDisplayableObject();
            
            final RadixIcon radixIcon =  obj == null ? null :obj.getIcon();
            if (radixIcon != null) {
                final Image image = radixIcon.getImage();
                if (image != null) {
                    return image;
                }
            }
            return RadixObjectIcon.UNKNOWN.getImage();
        }

        @Override
        public Image getOpenedIcon(final int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            if (isLeaf()) {
                final RadixObject displayDefinition = getDisplayableObject();
                return displayDefinition.getQualifiedName();
            }

            final boolean QNAME = true;
            final RadixObject displayDefinition = getDisplayableObject();

            if (displayDefinition instanceof AdsMethodDef) {
                return ((AdsMethodDef) displayDefinition).getProfile().getQualifiedName();
            }
            return QNAME ? displayDefinition.getQualifiedName() : displayDefinition.getName();
        }

        @Override
        protected String getLocation() {
            return "";
        }
    }

    private static final class RootChildren extends Children.Array {

        private static Collection<Node> createNodes(java.util.Map<RadixObject, List<RadixObject>> usages, Filter filter) {
            final List<Node> nodes = new ArrayList<>();

            for (final RadixObject object : usages.keySet()) {
                final List<RadixObject> children = usages.get(object);
                final Children usagesChildren = children.isEmpty() ? Children.LEAF : new UsagesChildren(children, filter);
                nodes.add(new UsagesNode(usagesChildren, object, filter));
            }

            return nodes;
        }

        public RootChildren(java.util.Map<RadixObject, List<RadixObject>> usages, Filter filter) {
            super(createNodes(usages, filter));
        }
    }

    private static final class UsagesChildren extends Children.Array {

        private static Collection<Node> createNodes(List<RadixObject> items, Filter filter) {
            final List<Node> nodes = new ArrayList<>();

            for (final RadixObject object : items) {
                nodes.add(new UsagesNode(Children.LEAF, object, filter));
            }

            return nodes;
        }

        public UsagesChildren(List<RadixObject> items, Filter filter) {
            super(createNodes(items, filter));
        }
    }
    private final OutlineView view;
    private final ExplorerManager manager = new ExplorerManager();
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public ObjectUsagesPanel() {

        view = new OutlineView();
        view.getOutline().setRootVisible(false);
        view.getOutline().setShowGrid(false);
        view.setBorder(BorderFactory.createEtchedBorder(1));
        view.addPropertyColumn(DescriptionProperty.NAME, DescriptionProperty.NAME);

        view.getOutline().getColumnModel().getColumn(0).setHeaderValue("Use");

        final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        view.getOutline().getColumn(DescriptionProperty.NAME).setCellRenderer(cellRenderer);

        view.setQuickSearchTableFilter(new QuickSearchTableFilter() {
            @Override
            public String getStringValueAt(int row, int col) {
                Object val = view.getOutline().getModel().getValueAt(row, col);
                if (col > 0) {
                    return "";
                }
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


        add(view, USAGES_CARD);
    }

    public void open(Map<RadixObject, List<RadixObject>> usages, ObjectUsagesStep.IFilter filter) {

        if (usages != null && !usages.isEmpty()) {

            getExplorerManager().setRootContext(new AbstractNode(new RootChildren(usages, new Filter(filter))));
            expandAll();

            view.getOutline().getColumn(DescriptionProperty.NAME).setPreferredWidth(this.getWidth() / 3);
            view.getOutline().getColumn(DescriptionProperty.NAME).setMaxWidth(this.getWidth() / 3);

            showCard(USAGES_CARD);
        } else {
            String message = NbBundle.getMessage(ObjectUsagesPanel.class, "ObjectUsagesPanel.lblStatus.NotUsed");
            setStatus(message);
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public void expandAll() {
        expandNode(getExplorerManager().getRootContext());
    }

    private void expandNode(Node node) {
        if (node.isLeaf()) {
            view.expandNode(node);
        } else {
            for (final Node n : node.getChildren().getNodes()) {
                expandNode(n);
            }
        }
    }

    private void updateView() {
        view.invalidate();
        view.repaint();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeSupport.fireChange();
            }
        });
    }

    public Map<RadixObject, List<RadixObject>> getSelectedDefinitions() {
        final Map<RadixObject, List<RadixObject>> selected = new HashMap<>();
        final Node root = getExplorerManager().getRootContext();

        if (root != null && !root.isLeaf()) {

            final Stack<Node> stack = new Stack<>();
            stack.push(root);

            while (!stack.isEmpty()) {

                final Node current = stack.pop();
                final Node[] nodes = current.getChildren().getNodes(true);
                for (Node node : nodes) {
                    if (!node.isLeaf()) {
                        stack.push(node);
                    } else if (node instanceof UsagesNode) {
                        final UsagesNode usagesNode = (UsagesNode) node;

                        if (usagesNode.isSelected()) {
                            final RadixObject parent = ((UsagesNode) current).getObject();
                            List<RadixObject> children = selected.get(parent);

                            if (children == null) {
                                children = new ArrayList<>();
                                selected.put(parent, children);
                            }

                            children.add(usagesNode.getObject());
                        }
                    }
                }
            }
        }

        return selected;
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }
}
