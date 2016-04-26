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
 * AdsPropertyCommandDefPanel.java
 *
 * Created on Feb 10, 2009, 11:02:20 AM
 */
package org.radixware.kernel.designer.ads.editors.scopecommand;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyCommandDef;
import org.radixware.kernel.common.types.Id;


public class AdsPropertyCommandDefPanel extends javax.swing.JPanel {

    private class NodeComparator<DefaultMutableTreeNode> implements Comparator<DefaultMutableTreeNode> {

        @Override
        public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
            PropertyCheckBox p1 = (PropertyCheckBox) ((javax.swing.tree.DefaultMutableTreeNode) o1).getUserObject();
            PropertyCheckBox p2 = (PropertyCheckBox) ((javax.swing.tree.DefaultMutableTreeNode) o2).getUserObject();

            String s1 = p1.propertyId.toString();
            String s2 = p2.propertyId.toString();
            if (p1.property != null){
                s1 = p1.property.getName();
            }
            if (p2.property != null){
                s2 = p2.property.getName();
            }
            int result = Collator.getInstance().compare(s1, s2);
            return result;
        }
    }
    DefaultMutableTreeNode root;

    /** Creates new form AdsPropertyCommandDefPanel */
    public AdsPropertyCommandDefPanel() {
        initComponents();
        propsTree.setModel(null);
        root = new DefaultMutableTreeNode("root", true);
        propsTree.setRootVisible(false);
        propsTree.setShowsRootHandles(true);
        propsTree.setEditable(true);
        propsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        PropCellRenderer renderer = new PropCellRenderer();
        PropCellEditor editor = new PropCellEditor(propsTree, renderer);
        propsTree.setCellRenderer(renderer);
        propsTree.setCellEditor(editor);
    }
    private AdsPropertyCommandDef command;
    private HashMap<Id, PropertyCheckBox> propTable = new HashMap<Id, PropertyCheckBox>();
    private List<Id> usedIds = new ArrayList<Id>();
    private final String UNKNOWN_TITLE = NbBundle.getMessage(AdsPropertyCommandDefPanel.class, "ScopeProperties-UnknownTip");

    public void open(AdsPropertyCommandDef command) {
        this.command = command;
        update();
    }

    private void prepareTree() {
        if (command != null
                && command.getOwnerClass() != null) {
            root = new DefaultMutableTreeNode("root", true);
            unknownOwner = new DefaultMutableTreeNode(UNKNOWN_TITLE, true);

            AdsClassDef owner = command.getOwnerClass();
            buildInheritanceTree(owner);
            DefaultTreeModel model = new DefaultTreeModel(root);
            propsTree.setModel(model);
            int rowCount = root.getChildCount() - 1;
            for (int i = 0; i <= rowCount; i++) {
                TreeNode[] path = ((DefaultMutableTreeNode) root.getChildAt(i)).getPath();
                TreePath extPath = new TreePath(path);
                propsTree.expandPath(extPath);
            }
            propsTree.repaint();
        }
    }
    private HashMap<AdsClassDef, DefaultMutableTreeNode> ownerMap = new HashMap<AdsClassDef, DefaultMutableTreeNode>();
    private DefaultMutableTreeNode unknownOwner;
    private HashMap<AdsClassDef, TreeSet<DefaultMutableTreeNode>> treemap = new HashMap<AdsClassDef, TreeSet<DefaultMutableTreeNode>>();

    private void buildInheritanceTree(AdsClassDef owner) {
        ownerMap.clear();
        propTable.clear();
        treemap.clear();

        fillTreeMap(owner);

//        List<AdsPropertyDef> props = owner.getProperties().get(EScope.ALL);
//        for (AdsPropertyDef p : props) {
//            if (!(propTable.containsKey(p.getId()))) {
//                PropertyCheckBox check = new PropertyCheckBox(p, p.getId());
//                propTable.put(p.getId(), check);
//                ItemListener checkListener = new ItemListener() {
//
//                    @Override
//                    public void itemStateChanged(ItemEvent e) {
//                        AdsPropertyCommandDefPanel.this.onPropertyBoxEvent(e);
//                    }
//                };
//                check.addItemListener(checkListener);
//                DefaultMutableTreeNode propNode = new DefaultMutableTreeNode(p.getName(), false);
//                propNode.setUserObject(check);
//                AdsClassDef currentOwner = p.getOwnerClass();
//                DefaultMutableTreeNode handlerNode = ownerMap.get(currentOwner);
//                if (handlerNode == null) {
//                    handlerNode = new DefaultMutableTreeNode(currentOwner.getQualifiedName(), true);
//                    handlerNode.setUserObject(currentOwner);
//                    ownerMap.put(currentOwner, handlerNode);
//                }
//                handlerNode.add(propNode);
//                if (!root.isNodeChild(propNode)) {
//                    root.add(handlerNode);
//                }
//            }
//        }
        for (AdsClassDef own : treemap.keySet()) {
            DefaultMutableTreeNode handlerNode = ownerMap.get(own);
            if (handlerNode == null) {
                handlerNode = new DefaultMutableTreeNode(own.getQualifiedName(), true);
                handlerNode.setUserObject(own);
                ownerMap.put(own, handlerNode);
            }
            TreeSet<DefaultMutableTreeNode> branch = treemap.get(own);
            for (DefaultMutableTreeNode node : branch){
                handlerNode.add(node);
            }

            if (!root.isNodeChild(handlerNode)) {
                root.add(handlerNode);
            }
        }
    }

    private void fillTreeMap(AdsClassDef owner) {
        List<AdsPropertyDef> props = owner.getProperties().get(EScope.ALL);
        for (AdsPropertyDef p : props) {
            if (!(propTable.containsKey(p.getId()))) {
                PropertyCheckBox check = new PropertyCheckBox(p, p.getId());
                propTable.put(p.getId(), check);
                ItemListener checkListener = new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        AdsPropertyCommandDefPanel.this.onPropertyBoxEvent(e);
                    }
                };
                check.addItemListener(checkListener);
                DefaultMutableTreeNode propNode = new DefaultMutableTreeNode(p.getName(), false);
                propNode.setUserObject(check);
                AdsClassDef currentOwner = p.getOwnerClass();
                if (!treemap.containsKey(currentOwner)) {
                    treemap.put(currentOwner, new TreeSet<DefaultMutableTreeNode>(new NodeComparator<DefaultMutableTreeNode>()));
                }
                TreeSet<DefaultMutableTreeNode> branch = treemap.get(currentOwner);
                branch.add(propNode);
            }
        }
    }

    private void onPropertyBoxEvent(ItemEvent e) {
        if (!AdsPropertyCommandDefPanel.this.isUpdate) {
            PropertyCheckBox box = (PropertyCheckBox) e.getSource();
            List<Id> newIds = AdsPropertyCommandDefPanel.this.usedIds;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                newIds.add(box.propertyId);
            } else {
                newIds.remove(box.propertyId);
            }
            AdsPropertyCommandDefPanel.this.command.setUsedPropIds(newIds);
            if (box.property == null) {
                AdsPropertyCommandDefPanel.this.update();
            }
        }
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;

        prepareTree();

        if (command != null) {
            for (PropertyCheckBox box : propTable.values()) {
                box.setSelected(false);
            }
            List<Id> usedProps = command.getUsedPropIds();
            usedIds.clear();
            for (Id i : usedProps) {
                usedIds.add(i);
                PropertyCheckBox box = propTable.get(i);
                if (box != null) {
                    box.setSelected(true);
                } else {
                    box = new PropertyCheckBox(null, i);
                    box.setSelected(true);
                    ItemListener checkListener = new ItemListener() {

                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            AdsPropertyCommandDefPanel.this.onPropertyBoxEvent(e);
                        }
                    };
                    box.addItemListener(checkListener);

                    unknownOwner.add(new DefaultMutableTreeNode(box, false));
                }
            }
            if (unknownOwner.getChildCount() > 0) {
                root.add(unknownOwner);
                DefaultTreeModel model = new DefaultTreeModel(root);
                propsTree.setModel(model);
                int rowCount = root.getChildCount() - 1;
                for (int i = 0; i <= rowCount; i++) {
                    TreeNode[] path = ((DefaultMutableTreeNode) root.getChildAt(i)).getPath();
                    TreePath extPath = new TreePath(path);
                    propsTree.expandPath(extPath);
                }
                propsTree.repaint();
            }
        }

        isUpdate = false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        propsTree = new javax.swing.JTree();

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setName(org.openide.util.NbBundle.getMessage(AdsPropertyCommandDefPanel.class, "ScopeProperties-PropTitle")); // NOI18N

        jScrollPane1.setViewportView(propsTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree propsTree;
    // End of variables declaration//GEN-END:variables

    private class PropertyCheckBox extends JCheckBox {

        AdsPropertyDef property;
        Id propertyId;

        PropertyCheckBox(AdsPropertyDef property, Id id) {
            super(property != null ? property.getName() : id.toString());
            this.property = property;
            this.propertyId = id;
        }
    }

    private class PropCellEditor extends DefaultTreeCellEditor {

        public PropCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            Component s = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (!node.isRoot()) {
                    if (node.getUserObject() instanceof PropertyCheckBox) {
                        Component user = (Component) node.getUserObject();
                        return user;
                    } else if (node.getUserObject() instanceof AdsClassDef) {
                        AdsClassDef user = (AdsClassDef) node.getUserObject();
                        JLabel l = new JLabel(user.getQualifiedName());
                        l.setBackground(s.getBackground());
                        l.setIcon(user.getIcon().getIcon());
                        return l;
                    } else if (node.getUserObject().equals(UNKNOWN_TITLE)) {
                        JLabel l = new JLabel(UNKNOWN_TITLE);
                        l.setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
                        l.setBackground(tree.getBackground());
                        return l;
                    }
                }
            }
            return s;
        }

        @Override
        public boolean isCellEditable(EventObject event) {
            return true;
        }
    }

    private class PropCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component s = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (!node.isRoot()) {
                    if (node.getUserObject() instanceof PropertyCheckBox) {
                        Component user = (Component) node.getUserObject();
                        user.setBackground(tree.getBackground());
                        return user;
                    } else {
                        if (node.getUserObject() instanceof AdsClassDef) {
                            AdsClassDef user = (AdsClassDef) node.getUserObject();
                            JLabel l = new JLabel(user.getQualifiedName());
                            l.setIcon(user.getIcon().getIcon());
                            l.setBackground(tree.getBackground());
                            return l;
                        } else if (node.getUserObject().equals(UNKNOWN_TITLE)) {
                            JLabel l = new JLabel(UNKNOWN_TITLE);
                            l.setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
                            l.setBackground(tree.getBackground());
                            return l;
                        }
                    }
                }
            }
            return s;
        }
    }
}
