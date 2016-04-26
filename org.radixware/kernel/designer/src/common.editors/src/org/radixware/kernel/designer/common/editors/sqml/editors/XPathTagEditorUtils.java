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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.awt.Component;
import java.awt.Image;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.xmlbeans.XmlObject;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.RowModel;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.XPathUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;


class XPathTagEditorUtils {

    static class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            javax.swing.JLabel s = new javax.swing.JLabel(value != null ? value.toString() : "");
            s.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            s.setHorizontalTextPosition(javax.swing.JLabel.CENTER);
            return s;
        }
    }

    static class PathNodeRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component s = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof PathNode) {
                PathNode node = (PathNode) value;
                if (node.isEditable()) {
                    JLabel label = (JLabel) s;
                    label.setHorizontalAlignment(JLabel.RIGHT);
                    label.setHorizontalTextPosition(JLabel.LEFT);
                    label.setText(node.index.toString());
                } else {
                    ((JLabel) s).setText("");
                }
            } else {
                ((JLabel) s).setText("");
            }
            return s;
        }
    }

    static class ConditionRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component s = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof PathNode) {
                PathNode node = (PathNode) value;
                JLabel label = (JLabel) s;
                if (node.condition != null) {
                    label.setText(node.condition);
                } else {
                    ((JLabel) s).setText("");
                }
            } else {
                ((JLabel) s).setText("");
            }
            return s;
        }
    }

    static class PathNodeCellEditor extends AbstractCellEditor implements TableCellEditor {

        private javax.swing.JSpinner spinner;
        private PathNode node;
        private ChangeSupport changeSupport = new ChangeSupport(this);

        public PathNodeCellEditor() {
            SpinnerModel model = new SpinnerNumberModel(0, 0, Byte.MAX_VALUE, 1);
            spinner = new javax.swing.JSpinner(model);
            spinner.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (node != null) {
                        node.index = (Integer) spinner.getValue();
                        changeSupport.fireChange();
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return node.index;
        }

        public void addChangeListener(ChangeListener listener) {
            changeSupport.addChangeListener(listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            changeSupport.removeChangeListener(listener);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (value instanceof PathNode) {
                node = (PathNode) value;
                spinner.setValue(node.index);
            } else {
                node = null;
                spinner.setValue(0);
            }
            return spinner;
        }
    }

    static class ConditionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private javax.swing.JTextField field;
        private PathNode node;

        ConditionCellEditor() {
            this.field = new javax.swing.JTextField();
            field.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (node != null) {
                        node.condition = field.getText();
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return node.condition;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof PathNode) {
                node = (PathNode) value;
                field.setText(node.condition != null ? node.condition : "");
            } else {
                node = null;
                field.setText("");
            }
            return field;
        }
    }

    static class RootNodeChildren extends Children.Keys {

        XmlObject schemaNode;
        AdsXmlSchemeDef context;
        Outline outline;
        LinkedList<Integer> used;
        private List<XPathUtils.NodeInfo> keys;
        private XPathTagEditor editor;

        RootNodeChildren(XPathTagEditor editor, XmlObject schemaNode, AdsXmlSchemeDef context, Outline outline, LinkedList<Integer> used) {
            super();
            this.schemaNode = schemaNode;
            this.context = context;
            this.outline = outline;
            this.used = used;
            this.editor = editor;

            this.keys = XPathUtils.getTopLevelElements(context, schemaNode);
        }

        @Override
        protected void addNotify() {
            setKeys(keys);
        }

        @Override
        public Node[] createNodes(Object t) {
            return new Node[]{new PathNode(editor, new PathNodeChildren(editor, (XPathUtils.NodeInfo) t, outline, used), (XPathUtils.NodeInfo) t, outline, false)};
        }
    }

    static class PathNodeChildren extends Children.Keys {

        XPathUtils.NodeInfo owner;
        LinkedList<Integer> used;
        Outline outline;
        private List<XPathUtils.NodeInfo> keys;
        private XPathTagEditor editor;

        PathNodeChildren(XPathTagEditor editor, XPathUtils.NodeInfo owner, Outline outline, LinkedList<Integer> used) {
            super();
            this.owner = owner;

            this.outline = outline;
            this.used = used;
            this.editor = editor;

            this.keys = XPathUtils.getAvailableChildren(owner.node, XPathUtils.getSchemaNodeTargetNamespace(owner.context.getXmlDocument()), owner.context);
        }

        PathNodeChildren(XPathTagEditor editor, XPathUtils.NodeInfo owner, Outline outline, LinkedList<Integer> used, List<XPathUtils.NodeInfo> chKeys) {
            super();
            this.owner = owner;
            this.outline = outline;
            this.used = used;
            this.editor = editor;

            this.keys = chKeys;
        }

        @Override
        protected void addNotify() {
            setKeys(keys);
        }

        @Override
        public Node[] createNodes(Object t) {
            XPathUtils.NodeInfo x = (XPathUtils.NodeInfo) t;
            if (XPathUtils.isAttribute(x.node)) {
                return new Node[]{new PathNode(editor, LEAF, x, outline, false)};
            } else {
                final int l = used.size();

                Node[] res = new Node[1];

                if (!used.contains(x.hashCode())) {
                    used.add(x.hashCode());
                    List<XPathUtils.NodeInfo> chKeys = XPathUtils.getAvailableChildren(x.node, XPathUtils.getSchemaNodeTargetNamespace(x.context.getXmlDocument()), x.context);
                    if (chKeys.size() > 0) {
                        res[0] = new PathNode(editor, new PathNodeChildren(editor, x, outline, used, chKeys), x, outline, false);
                    } else {
                        res[0] = new PathNode(editor, LEAF, x, outline, false);
                    }

                } else {
                    res[0] = new PathNode(editor, LEAF, x, outline, true);
                }
                if (used.size() > l) {
                    int cur = used.size();
                    int index = 1;
                    while (index <= cur - l
                            && used.size() >= l) {
                        used.remove(used.size() - 1);
                        index++;
                    }
                }
                return res;
            }
        }
    }

    static class RootNode extends AbstractNode {

        XmlObject schemaNode;

        RootNode(RootNodeChildren children, XmlObject schemaNode) {
            super(children);
            this.schemaNode = schemaNode;
        }

        @Override
        public String getDisplayName() {
            if (schemaNode != null) {
                return XPathUtils.getSchemaNodeTargetNamespace(schemaNode);
            }
            return super.getDisplayName();
        }

        @Override
        public Image getIcon(int type) {
            return AdsDefinitionIcon.XML_SCHEME.getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return this.getIcon(type);
        }
    }

    static class PathNode extends AbstractNode {

        final String RECURSIVE_MARK = "<font color=\"red\"> recursive element... </font>";
        XPathUtils.NodeInfo item;
        boolean isRecursiveEnd = false;
        Outline outline;
        Integer index = 0;
        String condition;

        public PathNode(XPathTagEditor editor, Children children, XPathUtils.NodeInfo item, Outline outline, boolean isRecursiveEnd) {
            this(editor, children, item, outline, isRecursiveEnd, new InstanceContent());
        }

        private PathNode(XPathTagEditor editor, Children children, XPathUtils.NodeInfo item, Outline outline, boolean isRecursiveEnd, InstanceContent content) {
            super(children, new AbstractLookup(content));
            this.item = item;
            this.outline = outline;
            this.isRecursiveEnd = isRecursiveEnd;


            content.add(new XPathTagEditor.CloseOnOkAction.Cookie(editor));
        }

        public AdsXmlSchemeDef getContext() {
            return item.context;
        }

        public boolean isAttribute() {
            return item != null ? XPathUtils.isAttribute(item.node) : false;
        }

        @Override
        public String getDisplayName() {
            if (isRecursiveEnd) {
                return "<html><body>" + XPathUtils.getDisplayName(item.node) + RECURSIVE_MARK + "</body></html>";
            }
            return XPathUtils.getDisplayName(item.node);
        }

        @Override
        public Image getIcon(int type) {
            if (item != null && XPathUtils.isAttribute(item.node)) {
                return RadixWareIcons.JML_EDITOR.XPATH_ATTRIBUTE.getImage(16, 16);
            } else {
                return RadixWareIcons.JML_EDITOR.XPATH_ANCESTOR.getImage(16, 16);
            }
        }

        @Override
        public Image getOpenedIcon(int type) {
            return this.getIcon(type);
        }

        @Override
        public String getShortDescription() {
            if (item != null) {
                return XPathUtils.getAnnotation(item.node);
            }
            return null;
        }

        boolean isEditable() {
            if (isAttribute()) {
                return false;
            }
            Integer min = XPathUtils.getMinOccurs(item.node);
            Integer max = XPathUtils.getMaxOccurs(item.node);

            if (max.equals(-1) && min > -1) {
                return true;
            }
            if (max > 0 && min > -1 && ((max - min) > 1)) {
                return true;
            }

            return false;
        }

        @Override
        public Action getPreferredAction() {
            return SystemAction.get(XPathTagEditor.CloseOnOkAction.class);
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[]{SystemAction.get(XPathTagEditor.CloseOnOkAction.class)};
        }
    }

    static class XPathTreeModel implements TreeModel {

        private Node root;

        XPathTreeModel(Node root) {
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

    static class XPathRowModel implements RowModel {

        @Override
        public Class getColumnClass(int i) {
            return Object.class;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int i) {
            if (i == 0) {
                return NbBundle.getMessage(XPathTagEditorUtils.class, "XPathTagEditor-ColumnTip");
            } else if (i == 1) {
                return NbBundle.getMessage(XPathTagEditorUtils.class, "XPathTagEditor-ConditionTip");
            }
            return "";
        }

        @Override
        public boolean isCellEditable(Object o, int i) {
            if (i == 0) {
                if (o instanceof PathNode) {
                    PathNode node = (PathNode) o;
                    return node.isEditable();
                }
            }
            return o instanceof PathNode && i == 1;
        }

        @Override
        public void setValueFor(Object o, int i, Object o1) {
            //TODO:
        }

        @Override
        public Object getValueFor(Object o, int i) {
            if (o instanceof PathNode) {
                return o;
            }
            return null;
        }
    }

    static class EmptyNode extends AbstractNode {

        public EmptyNode() {
            super(Children.LEAF);
        }

        @Override
        public String getHtmlDisplayName() {
            return "<No items>";
        }
    }

    static class WaitNode extends AbstractNode {

        public WaitNode() {
            super(Children.LEAF);
        }

        @Override
        public String getHtmlDisplayName() {
            return "<html><body><b>Building tree ...</b></body></html>";
        }

        @Override
        public Image getIcon(int type) {
            return RadixWareIcons.DIALOG.CHOOSE.getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return this.getIcon(type);
        }
    }
}
