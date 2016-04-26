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
 * ChooseLayersDialog.java
 *
 * Created on Oct 1, 2009, 6:28:00 PM
 */
package org.radixware.kernel.designer.ads.localization.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTable.BooleanEditor;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog.StateAbstractPanel;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class ChooseLayersDialog extends StateAbstractPanel {
    // private List<TreeTableModelRow> rows;

    private final org.jdesktop.swingx.JXTreeTable treeTable;
    private MyTreeTableModel tableModel;
    //private final List<Layer> selectedLayers;
    private Map<Layer, List<Module>> selectedLayerMap ;
    private final org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer;

    /**
     * Creates new form ChooseLayersDialog
     */
    public ChooseLayersDialog(final Map<Layer, List<Module>> selectedLayers) {
        this.selectedLayerMap = selectedLayers;
        initComponents();
        stateDisplayer = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        treeTable = new org.jdesktop.swingx.JXTreeTable();
        createTreeTableUi();
        jScrollPane1.setViewportView(treeTable);
        add(stateDisplayer, BorderLayout.SOUTH);
        //check();
    }

    private void createTreeTableUi() {
        treeTable.setRootVisible(false);
        treeTable.setShowsRootHandles(true);
        treeTable.setBackground(Color.white);
        treeTable.setTreeCellRenderer(new DomainsTreeCellRenderer());
        treeTable.getTableHeader().setReorderingAllowed(false);

        tableModel = new MyTreeTableModel();
        treeTable.setTreeTableModel(tableModel);
        treeTable.setDefaultEditor(Boolean.class, new BooleanEditor());
        final TableColumn checkboxesColumn = treeTable.getColumnModel().getColumn(1);
        checkboxesColumn.setPreferredWidth(60);
        checkboxesColumn.setMaxWidth(60);
        checkboxesColumn.setMinWidth(60);
        expand();
    }

    protected final void expand() {
        final TableUI tableUi = treeTable.getUI();
        try {
            treeTable.setUI(null);
            final TreeNode root = (TreeNode) treeTable.getTreeTableModel().getRoot();
            final TreePath parent = new TreePath(root);
            for (int i = 0; i < root.getChildCount(); ++i) {
                expandAll(parent.pathByAddingChild(root.getChildAt(i)));
            }
        } finally {
            treeTable.setUI(tableUi);
        }
    }

    @SuppressWarnings("unchecked")
    private void expandAll(final TreePath parent) {
        final TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {            
            for (Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
                expandAll(parent.pathByAddingChild(e.nextElement()));
            }
        }
        treeTable.expandPath(parent);
    }

    public Map<Layer, List<Module>> getSelectedLayers() {
        selectedLayerMap = new HashMap<>();
        Enumeration<? extends MutableTreeTableNode> children = ((MutableTreeTableNode) tableModel.getRoot()).children();
        MutableTreeTableNode layerNode;//=children.nextElement();
        while (children != null && children.hasMoreElements()) {
            layerNode = children.nextElement();
            if (layerNode.getUserObject() != null && layerNode.getUserObject() instanceof TreeTableModelRow) {
                if (((TreeTableModelRow) layerNode.getUserObject()).getValue() instanceof Layer) {
                    Layer layer = (Layer) ((TreeTableModelRow) layerNode.getUserObject()).getValue();
                    if (((TreeTableModelRow) layerNode.getUserObject()).isCheched()) {
                        selectedLayerMap.put(layer, null);
                    } else {
                        MutableTreeTableNode segmentNode; 
                        Enumeration<? extends MutableTreeTableNode> segmentChilderen = layerNode.children();
                        while (segmentChilderen != null && segmentChilderen.hasMoreElements()) {
                            segmentNode = segmentChilderen.nextElement();
                            List<Module> modules = getSelectedModules(segmentNode);
                            if (!modules.isEmpty()) {
                                selectedLayerMap.put(layer, modules);
                            }
                        }
                    }
                } else {
                    children = layerNode.children();
                }
            }
        }

        /* List<TreeTableModelRow> resultList=tableModel.getSelectedList();
         if(resultList==null)return null;
         List<Layer> layers=new ArrayList<>();
         for(TreeTableModelRow row: resultList){
         if(row.getValue() instanceof Layer){
         Layer layer=(Layer)row.getValue();
         layers.add(layer);
         }
         }*/
        return selectedLayerMap;
    }

    private List<Module> getSelectedModules(final MutableTreeTableNode layerNode) {
        List<Module> selectedModules = new ArrayList<>();
        Enumeration<? extends MutableTreeTableNode> children = layerNode.children();
        MutableTreeTableNode m;//=children.nextElement();
        while (children != null && children.hasMoreElements()) {
            m = children.nextElement();
            if (m.getUserObject() != null && m.getUserObject() instanceof TreeTableModelRow) {
                if (((TreeTableModelRow) m.getUserObject()).isCheched() && ((TreeTableModelRow) m.getUserObject()).getValue() instanceof Module) {
                    Module module = (Module) ((TreeTableModelRow) m.getUserObject()).getValue();
                    selectedModules.add(module);
                }
            }
        }
        return selectedModules;
    }

    public boolean isAllSelected() {
        return tableModel.isAllSelected();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void check() {
        Map<Layer, List<Module>> selectedLayers = getSelectedLayers();
        if ((selectedLayers.keySet() == null) || (selectedLayers.keySet().size() <= 0)) {
            stateManager.error(NbBundle.getMessage(ChooseLayersDialog.class, "LAYER_NOT_SELECTED"));
        } else {
            stateManager.ok();
        }
        changeSupport.fireChange();
    }

    private class MyTreeTableModel extends AbstractTreeTableModel {

        private boolean isAllSelected = false;
        //private List<TreeTableModelRow> result;
        private String[] columns = {"Name", "Assign"};
        private DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

        public MyTreeTableModel() {
            //result=new ArrayList<TreeTableModelRow>();
            Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
            for (Branch branch : openBranches) {
                TreeTableModelRow row = new TreeTableModelRow(branch, false);
                final DefaultMutableTreeTableNode topLevelNode = new DefaultMutableTreeTableNode(row);
                appendChildren(topLevelNode, (Branch) row.getValue());
                row.setChecked(isAllChildrenChecked(topLevelNode));
                rootNode.add(topLevelNode);
            }
        }

        private void appendChildren(final DefaultMutableTreeTableNode node, final Branch branch) {
            final List<Layer> layers = branch.getLayers().list();

            Map<Layer, List<Module>> map;
            if (selectedLayerMap == null) {
                map = Collections.emptyMap();
            } else {
                map = new HashMap<>(selectedLayerMap);
            }
            final Set<Layer> selectedLayers = map.keySet();
            final Set<Layer> appropriateLayers = new HashSet<>() ;
            for(Layer layer : layers){
               // if (!layer.isReadOnly()){ 
                    if(layer.isLocalizing() && !layer.listBaseLayers().isEmpty()){
                        final Layer baseLayer=layer.listBaseLayers().get(0);
                        if(baseLayer!=null && !appropriateLayers.contains(baseLayer)){
                            appropriateLayers.add(baseLayer);
                        }
                    }else{
                        if(!appropriateLayers.contains(layer))
                            appropriateLayers.add(layer);
                    }
                //}
            }
            for (Layer layer : appropriateLayers) {                
                final List<Module> selectedModules = map.get(layer);
                final TreeTableModelRow layerRow = new TreeTableModelRow(layer, false);
                if ((selectedLayers == null || selectedLayers.isEmpty()) || (selectedLayers.contains(layer) && selectedModules == null)) {
                    layerRow.setChecked(true);
                }
                final DefaultMutableTreeTableNode layerNode = new DefaultMutableTreeTableNode(layerRow);
                node.add(layerNode);
                                
                addModulesFromSegment(layer.getDds(), selectedModules, layerNode, layerRow.isCheched());
                addModulesFromSegment(layer.getAds(), selectedModules, layerNode, layerRow.isCheched());

                
            }
        }
        
        private void addModulesFromSegment(Segment segment, List<Module> selectedModules,DefaultMutableTreeTableNode layerNode, boolean isLayerCheched){
            List<Module> modules = new ArrayList<>(segment.getModules().list());
            if (!modules.isEmpty()){
                    Collections.sort(modules, new ComparatorImpl());
                    final TreeTableModelRow segmentRow = new TreeTableModelRow(segment, false);
                    final DefaultMutableTreeTableNode segmentNode = new DefaultMutableTreeTableNode(segmentRow);
                        layerNode.add(segmentNode);
                    int countSelectedModules = 0;
                    for (Module module : modules) {
                        final boolean isChecked = isLayerCheched || selectedModules!=null && selectedModules.contains(module);
                        if (isChecked) countSelectedModules++;    
                        TreeTableModelRow moduleRow = new TreeTableModelRow(module, isChecked);
                        final DefaultMutableTreeTableNode moduleNode = new DefaultMutableTreeTableNode(moduleRow);
                        segmentNode.add(moduleNode);
                    }
                    segmentRow.setChecked(modules.size() == countSelectedModules);
                }
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(final int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(final Object arg0, final int column) {
            if (column == 0) {
                return ((DefaultMutableTreeTableNode) arg0).getUserObject();
            } else {
                assert (column == 1);
                return ((TreeTableModelRow) ((DefaultMutableTreeTableNode) arg0).getUserObject()).isCheched();
            }
        }

        @Override
        public void setValueAt(final Object value, final Object node, final int column) {
            assert (column == 1);
            final Boolean flag = (Boolean) value;
            final DefaultMutableTreeTableNode current = (DefaultMutableTreeTableNode) node;
            final TreeTableModelRow curObj = (TreeTableModelRow) current.getUserObject();
            curObj.setChecked(flag);

            changeFlagForChildren(node, flag);
            changeFlagForParent(node);
            
            ChooseLayersDialog.this.treeTable.validate();
            ChooseLayersDialog.this.treeTable.repaint();
            ChooseLayersDialog.this.check();
        }

        private void changeFlagForParent(final Object node) {
            if (((DefaultMutableTreeTableNode) node).getParent() != null) {
                DefaultMutableTreeTableNode parent = (DefaultMutableTreeTableNode) ((DefaultMutableTreeTableNode) node).getParent();
                while (parent.getUserObject() != null) {
                    final TreeTableModelRow parentObj = (TreeTableModelRow) parent.getUserObject();
                    parentObj.setChecked(isAllChildrenChecked(parent));

                    parent = (DefaultMutableTreeTableNode) parent.getParent();
                }
            }
        }

        private void changeFlagForChildren(final Object node, final Boolean flag) {
            Enumeration<? extends MutableTreeTableNode> children = ((DefaultMutableTreeTableNode) node).children();
            if (children != null && (children.hasMoreElements())) {
                MutableTreeTableNode child;
                while ((children.hasMoreElements()) && ((child = children.nextElement()) != null)) {
                    TreeTableModelRow childObj = (TreeTableModelRow) child.getUserObject();
                    childObj.setChecked(flag);
                    //if (flag){
                    //    if(!result.contains(childObj))
                    //        result.add(childObj);
                    //}else{
                    //     result.remove(childObj);
                    //}
                    changeFlagForChildren(child, flag);
                }
            }
        }
        
        private boolean isAllChildrenChecked(DefaultMutableTreeTableNode node){
            Enumeration<? extends MutableTreeTableNode> children = ((DefaultMutableTreeTableNode) node).children();
            if (children != null) {
                MutableTreeTableNode child;
                while ((children.hasMoreElements()) && ((child = children.nextElement()) != null)) {
                    TreeTableModelRow childObj = (TreeTableModelRow) child.getUserObject();
                    if (!childObj.isCheched()) return false;
                }
                return true;
            }
            return true;
        }

        @Override
        public boolean isCellEditable(final Object node, final int column) {
            return column == 1;
        }

        @Override
        public Object getRoot() {
            return rootNode;
        }

        @Override
        public Object getChild(final Object parent, final int index) {
            assert (parent instanceof DefaultMutableTreeTableNode);
            return ((DefaultMutableTreeTableNode) parent).getChildAt(index);
        }

        @Override
        public int getChildCount(final Object parent) {
            assert (parent instanceof DefaultMutableTreeTableNode);
            return ((DefaultMutableTreeTableNode) parent).getChildCount();
        }

        @Override
        public int getIndexOfChild(final Object parent, final Object child) {
            if (parent == null || child == null) {
                return -1;
            }
            return ((DefaultMutableTreeTableNode) parent).getIndex((DefaultMutableTreeTableNode) child);
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            if (column == 0) {
                return String.class;
            } else {
                return Boolean.class;
            }
        }

        @Override
        public int getHierarchicalColumn() {
            return super.getHierarchicalColumn();
        }

        @Override
        public boolean isLeaf(final Object node) {
            assert (node instanceof DefaultMutableTreeTableNode);
            return ((DefaultMutableTreeTableNode) node).isLeaf();
        }

        //public List<TreeTableModelRow> getSelectedList() {
        //    return result;
        //}
        public boolean isAllSelected() {
            isAllSelected = true;
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                TreeTableNode node = rootNode.getChildAt(i);
                boolean isCheck = (Boolean) getValueAt(node, 1);
                if (!isCheck) {
                    isAllSelected = false;
                }
            }
            return isAllSelected;
        }

        private class ComparatorImpl implements Comparator<Module> {
            @Override
            public int compare(Module o1, Module o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }
    }

    class DomainsTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
            super.setBorderSelectionColor(super.getBackgroundSelectionColor()); //prevent unusable black border when run under GNOME desktop environment

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeTableNode) {
                final DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
                final TreeTableNode parentNode = node.getParent();
                if (parentNode != null) {
                    final Object userObject = node.getUserObject();
                    assert (userObject instanceof TreeTableModelRow);
                    final TreeTableModelRow rowObj = (TreeTableModelRow) userObject;
                    setIcon(rowObj.getIcon().getIcon());
                    if (parentNode.equals(tree.getModel().getRoot())) {
                        setText(rowObj.getQualifiedName());
                    } else {
                        setText(rowObj.getName());
                    }
                }
            }
            return this;
        }
    }
}
