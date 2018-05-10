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
 * StandartPageProps.java
 *
 * Created on 07.09.2009, 10:57:50
 */
package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.components.TreeView;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsProperiesGroupDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IPagePropertyGroup;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.FixedSizeSquareButton;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class StandartAndCustomPageProperties extends javax.swing.JPanel {

    /** Creates new form StandartPageProps */
    public StandartAndCustomPageProperties() {
        initComponents();
        pageGroups.setRootVisible(true);
        pageGroups.getRootNode().setDisplayName("Page");
    }
    private AdsEditorPageDef page;

    public void open(final AdsEditorPageDef page) {
        this.page = page;
        
        if (page.getType().equals(EEditorPageType.STANDARD)) {
            headerPropertyGroupPanel.setLayout(new MigLayout("fill"));
            headerPropertyGroupPanel.add(treeLabel, "top");
            headerPropertyGroupPanel.add(removeGroup, "east, gap unrelated");
            headerPropertyGroupPanel.add(addGroup, "east, gap unrelated");
            pageGroups.getRootNode().setIcon(page.getIcon().getIcon());
            addMenuItem.setAction(addGroupAction);
            String addTooltip = "Add child group";
            String removeTooltip = "Delete group";
            addMenuItem.setText(addTooltip);
            removeMenuItem.setAction(removeGroupAction);
            removeMenuItem.setText(removeTooltip);
            editMenuItem.setAction(editGroupAction);
            editMenuItem.setText("Edit");
            pageGroups.removeMouseListener(mouseListener);
            pageGroups.getRootNode().clear();
            pageGroups.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            jPanel1.add(pageGroups, BorderLayout.CENTER);
            for (AdsEditorPageDef.PagePropertyRef ref : page.getProperties()) {
                updateTree(ref, pageGroups.getRootNode());
            }
            final ICurrentGroupListener currentGroupListener = new ICurrentGroupListener() {

                @Override
                public IPagePropertyGroup getCurrentPagePropertyGroup() {
                    TreeView.Node node = pageGroups.getLastSelectedNode();
                    if (node != null) {
                        Object obj = node.getUserObject();
                        if (obj instanceof IPagePropertyGroup) {
                            return (IPagePropertyGroup) obj;
                        }
                    }

                    return page.getProperties();
                }

                @Override
                public void removeChildPropertyGroup(AdsProperiesGroupDef group) {
                    if (group == null){
                        return;
                    }
                    TreeView.Node parentNode = pageGroups.getLastSelectedNode();
                    IPagePropertyGroup parentGroup = null;
                    if (parentNode != null) {
                        if (parentNode == pageGroups.getRootNode()){
                            parentGroup = page.getProperties();
                        } else {
                            Object obj = parentNode.getUserObject();
                            if (obj instanceof IPagePropertyGroup) {
                                parentGroup = (IPagePropertyGroup) obj;
                            }
                        }
                    } else {
                        parentGroup = page.getProperties();
                        parentNode = pageGroups.getRootNode();
                    }
                    TreeView.Node node = null;
                    if (parentGroup != null) {
                        for (int i = 0; i < parentNode.getChildCount(); i++) {
                            TreeView.Node child = (TreeView.Node) parentNode.getChildAt(i);
                            Object obj = child.getUserObject();
                            if (obj == group) {
                                node = child;
                                break;
                            }
                        }
                    }
                    
                    if (node != null){
                        removeGroup(parentGroup, group, node);
                    }
                }
                
                
            };
            addGroup.setAction(addGroupAction);
            addGroup.setToolTipText(addTooltip);
            removeGroup.setAction(removeGroupAction);
            removeGroup.setToolTipText(removeTooltip);

            final UsedPropertiesTable usedTable = new UsedPropertiesTable(page);
            usedTable.addRemoveListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    chooser.update();
                }
            });
            usedTable.setCurrentGroupListener(currentGroupListener);
           
            pageGroups.addMouseListener(mouseListener);
            pageGroups.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    usedTable.updateContent();
                    removeGroup.setEnabled(currentGroupListener.getCurrentPagePropertyGroup() != page.getProperties());
                }
            });
            chooser.open(usedTable, new AvailableAdsEditorPageDefPropertiesList(page));
        }
        update();
    }
    
        
    private boolean showModal(final AdsProperiesGroupDef group, String title) {
        EditorPagePropertyGroupPanel panel = new EditorPagePropertyGroupPanel();
        panel.open(group, page);
        StateAbstractDialog dialog = new StateAbstractDialog(panel, title) {
                    };
        boolean result = dialog.showModal();
        if (result){
            panel.apply(group);
        }
        return result;
    }
    
    private void updateTree(AdsEditorPageDef.PagePropertyRef ref, TreeView.Node node) { 
        AdsProperiesGroupDef group = ref.getGroupDef();
        if (group != null) {
            TreeView.Node n = addNode(group, node);
            for (AdsEditorPageDef.PagePropertyRef pagePropertyRef : group.list()){
                updateTree(pagePropertyRef, n);
            }
        }
    }
    
    private void updateNode(TreeView.Node node, AdsProperiesGroupDef group){        
        node.setDisplayName(group.getName());
        node.setIcon(group.getIcon().getIcon());
        node.reload();
    }
    
    private TreeView.Node addNode(AdsProperiesGroupDef group, TreeView.Node parent) {
        TreeView.Node node = new TreeView.Node(group);
        updateNode(node, group);
        parent.add(node, false);
        return node;
    }

    public void update() {
        final boolean readonly = page.isReadOnly();
        boolean isVisible = page.getType().equals(EEditorPageType.STANDARD);
        pageItemsPanel.setVisible(isVisible);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                chooser.invalidate();
                pageItemsPanel.validate();
            }
        });
        chooser.setVisible(isVisible);
        if (chooser.isVisible()){
            chooser.setReadonly(readonly);
            chooser.update();
        }
        addGroup.setEnabled(!readonly);
        removeGroup.setEnabled(!readonly);
        
        titleAndIcon.open(page);
    }
    
    private final AbstractAction addGroupAction = new AbstractAction(null, RadixWareIcons.CREATE.ADD_ROW.getIcon()) {

        @Override
        public void actionPerformed(ActionEvent e) {
            TreeView.Node node = pageGroups.getLastSelectedNode();
            IPagePropertyGroup parent = page.getProperties();
            if (node != null) {
                Object obj = node.getUserObject();
                if (obj instanceof IPagePropertyGroup) {
                    parent = (IPagePropertyGroup) obj;
                } else {
                    node = pageGroups.getRootNode();
                }
            } else {
                node = pageGroups.getRootNode();
            }

            AdsProperiesGroupDef group = new AdsProperiesGroupDef();
            if (showModal(group, "Create property group")) {
                parent.addGroup(group);
                addNode(group, node);
                chooser.update();
            }
        }
    };
    
    private void removeGroup(IPagePropertyGroup parentGroup, AdsProperiesGroupDef group, TreeView.Node node) {
        if (parentGroup != null && node != null && group != null) {
            if (DialogUtils.messageConfirmation("Do you want to remove \'" + group.getName() + "\' ?")) {
                parentGroup.removeGroup(group);
                node.remove();
                chooser.update();
            }
        }
    }
    private final AbstractAction removeGroupAction = new AbstractAction(null, RadixWareIcons.DELETE.DELETE_ROW.getIcon()) {

        @Override
        public void actionPerformed(ActionEvent e) {
            TreeView.Node node = pageGroups.getLastSelectedNode();
            AdsProperiesGroupDef group = null;
            if (node != null) {
                Object obj = node.getUserObject();
                if (obj instanceof AdsProperiesGroupDef) {
                    group = (AdsProperiesGroupDef) obj;
                }

                if (node != pageGroups.getRootNode() && group != null) {
                    TreeView.Node p = node.getParent();
                    IPagePropertyGroup parentGroup = null;
                    if (p != null) {
                        Object pObj = p.getUserObject();
                        if (pObj instanceof IPagePropertyGroup) {
                            parentGroup = (IPagePropertyGroup) pObj;
                        } else {
                            parentGroup = page.getProperties();
                        }
                    }

                    removeGroup(parentGroup, group, node);
                }
            }
        }
    };
    private final AbstractAction editGroupAction = new AbstractAction(null, RadixWareIcons.EDIT.EDIT.getIcon()){

        @Override
        public void actionPerformed(ActionEvent e) {
            TreeView.Node node = pageGroups.getLastSelectedNode();
            if (node != null) {
                Object obj = node.getUserObject();
                if (obj instanceof AdsProperiesGroupDef) {
                    AdsProperiesGroupDef group = (AdsProperiesGroupDef) obj;
                    if (showModal(group, "Change property group")) {
                        updateNode(node, group);
                    }
                }
            }
        }
        
    };
    private TreeView pageGroups = new TreeView() {
        @Override
        public JPopupMenu getPopupMenu(TreeView.Node node) {
            if (node.isRoot()) {
                return null;
            }
            return treePopupMenu;
        }

    };
    
    private final MouseListener mouseListener = new MouseAdapter() {

        private static final int DOUBLE_CLICK = 2;
        private TreeView.Node lastSelectedNode = null;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == DOUBLE_CLICK) {
                editGroupAction.actionPerformed(null);
            }
        }
    };
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu1 = new java.awt.PopupMenu();
        treeLabel = new javax.swing.JLabel();
        addGroup = new FixedSizeSquareButton();
        removeGroup = new FixedSizeSquareButton();
        treePopupMenu = new javax.swing.JPopupMenu();
        editMenuItem = new javax.swing.JMenuItem();
        addMenuItem = new javax.swing.JMenuItem();
        removeMenuItem = new javax.swing.JMenuItem();
        titleAndIcon = new org.radixware.kernel.designer.ads.editors.pages.TitleAndIconPanel();
        pageItemsPanel = new javax.swing.JSplitPane();
        chooser = new org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel();
        jPanel1 = new javax.swing.JPanel();
        headerPropertyGroupPanel = new javax.swing.JPanel();

        popupMenu1.setLabel(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.popupMenu1.label")); // NOI18N

        treeLabel.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.treeLabel.text")); // NOI18N

        addGroup.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.addGroup.text")); // NOI18N
        addGroup.setToolTipText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.addGroup.toolTipText")); // NOI18N

        removeGroup.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.removeGroup.text")); // NOI18N
        removeGroup.setToolTipText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.removeGroup.toolTipText")); // NOI18N

        editMenuItem.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.editMenuItem.text")); // NOI18N
        treePopupMenu.add(editMenuItem);

        addMenuItem.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.addMenuItem.text")); // NOI18N
        addMenuItem.setToolTipText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.addMenuItem.toolTipText")); // NOI18N
        treePopupMenu.add(addMenuItem);

        removeMenuItem.setText(org.openide.util.NbBundle.getMessage(StandartAndCustomPageProperties.class, "StandartAndCustomPageProperties.removeMenuItem.text")); // NOI18N
        treePopupMenu.add(removeMenuItem);

        javax.swing.GroupLayout titleAndIconLayout = new javax.swing.GroupLayout(titleAndIcon);
        titleAndIcon.setLayout(titleAndIconLayout);
        titleAndIconLayout.setHorizontalGroup(
            titleAndIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        titleAndIconLayout.setVerticalGroup(
            titleAndIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout chooserLayout = new javax.swing.GroupLayout(chooser);
        chooser.setLayout(chooserLayout);
        chooserLayout.setHorizontalGroup(
            chooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chooserLayout.setVerticalGroup(
            chooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pageItemsPanel.setRightComponent(chooser);

        jPanel1.setLayout(new java.awt.BorderLayout());

        headerPropertyGroupPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));

        javax.swing.GroupLayout headerPropertyGroupPanelLayout = new javax.swing.GroupLayout(headerPropertyGroupPanel);
        headerPropertyGroupPanel.setLayout(headerPropertyGroupPanelLayout);
        headerPropertyGroupPanelLayout.setHorizontalGroup(
            headerPropertyGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        headerPropertyGroupPanelLayout.setVerticalGroup(
            headerPropertyGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(headerPropertyGroupPanel, java.awt.BorderLayout.PAGE_START);

        pageItemsPanel.setLeftComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(titleAndIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pageItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleAndIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(pageItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGroup;
    private javax.swing.JMenuItem addMenuItem;
    private org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel chooser;
    private javax.swing.JMenuItem editMenuItem;
    private javax.swing.JPanel headerPropertyGroupPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane pageItemsPanel;
    private java.awt.PopupMenu popupMenu1;
    private javax.swing.JButton removeGroup;
    private javax.swing.JMenuItem removeMenuItem;
    private org.radixware.kernel.designer.ads.editors.pages.TitleAndIconPanel titleAndIcon;
    private javax.swing.JLabel treeLabel;
    private javax.swing.JPopupMenu treePopupMenu;
    // End of variables declaration//GEN-END:variables
}
