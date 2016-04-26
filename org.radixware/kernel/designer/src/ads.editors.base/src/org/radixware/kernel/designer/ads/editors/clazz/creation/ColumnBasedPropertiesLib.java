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

package org.radixware.kernel.designer.ads.editors.clazz.creation;

import java.awt.Component;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.netbeans.swing.outline.RowModel;
import org.openide.actions.ToolsAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ColumnBasedPropertiesPanel.PropNode;


public class ColumnBasedPropertiesLib {

    static class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //Component s = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            javax.swing.JLabel s = new javax.swing.JLabel(value != null ? value.toString() : "");
            s.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            s.setHorizontalTextPosition(javax.swing.JLabel.CENTER); 
            return s;
        }

    }

    static class PropColumnRenderer extends DefaultTableCellRenderer {

        private AdsClassDef context;
        PropColumnRenderer(AdsClassDef context){
            this.context = context;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component s = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            javax.swing.JLabel asLabel = (javax.swing.JLabel) s;
//            asLabel.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
//            asLabel.setHorizontalTextPosition(javax.swing.JLabel.LEFT);
            if (value instanceof AdsPropertyDef){
                AdsPropertyDef prop = (AdsPropertyDef) value;
                String text = "<html><body><font color=\"gray\">" + prop.getName() + "</font></body></html>";
                if (prop.getOwnerClass().equals(context)){
                    text = prop.getName();
                }
                asLabel.setText(text);
//                asLabel.setIcon(prop.getIcon().getIcon(16, 16));
//            } else if (value instanceof String){
//                asLabel.setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
//            } else {
//                asLabel.setIcon(null);
            }
            return asLabel;
        }

    }

    static class PropTreeModel implements TreeModel {

        private Node root;

        PropTreeModel(Node root) {
            this.root = root;
        }

        @Override
        public Object getRoot() {
            if (root == null) {
                root = new EmptyNode();
            }
            return root;
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent instanceof Node) {
                return ((Node) parent).getChildren().getNodesCount();
            }
            return 0;
        }

        @Override
        public Object getChild(Object parent, int index) {
            if (parent instanceof Node) {
                return ((Node) parent).getChildren().getNodes()[index];
            }
            return null;
        }

        @Override
        public boolean isLeaf(Object node) {
            if (node instanceof Node) {
                return ((Node) node).isLeaf();
            }
            return true;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            if (parent instanceof Node && child instanceof Node) {
                Children children = ((Node) parent).getChildren();
                List<Node> nodes = Arrays.asList(children.getNodes());
                return nodes.indexOf(child);
            }
            return -1;
        }
        private final List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
        }
    }

    static class PropRowModel implements RowModel {

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueFor(Object node, int i) {
            if (node instanceof PropNode) {
                AdsPropertyDef prop = ((PropNode) node).info.adsproperty;
                if (prop != null && ((PropNode) node).isSelected())
                    return prop;
                else
                    return "<Not Defined>";
            }
            return null;
        }

        @Override
        public Class getColumnClass(int i) {
            return AdsPropertyDef.class;
        }

        @Override
        public boolean isCellEditable(Object o, int i) {
            if (o instanceof PropNode){
                if (((PropNode) o).info.isNewTemporaryProperty){
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setValueFor(Object o, int i, Object o1) {
        }

        @Override
        public String getColumnName(int i) {
            return "ADS Property";
        }

    }

    static class PropCellEditor extends DefaultCellEditor{

        PropCellEditor(javax.swing.JTextField field){
            super(field);
        }

        private String oldValue;
        private AdsPropertyDef property;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component s = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            if (s instanceof javax.swing.JTextField){
                javax.swing.JTextField field = (javax.swing.JTextField) s;
                if (value != null && value instanceof AdsPropertyDef){
                    String text = ((AdsPropertyDef) value).getName();
                    this.oldValue = text;
                    this.property = (AdsPropertyDef) value;
                    field.setText(text);
                } else {
                    this.oldValue = "";
                }
            } else {
                this.oldValue = "";
            }
            return s;
        }

        @Override
        public boolean stopCellEditing() {
            javax.swing.JTextField field = (javax.swing.JTextField) getComponent();
            String text = field.getText();
            if (text.isEmpty() || !RadixObjectsUtils.isCorrectName(text)){
                return false;
            }
            if (!text.equals(oldValue) && property != null){
                property.setName(text);
            }
            return super.stopCellEditing();
        }

    }

    static class EmptyNode extends AbstractNode {

        public EmptyNode() {
            super(Children.LEAF);
        }

        @Override
        public String getHtmlDisplayName() {
            return "No objects found";
        }
    }
}
