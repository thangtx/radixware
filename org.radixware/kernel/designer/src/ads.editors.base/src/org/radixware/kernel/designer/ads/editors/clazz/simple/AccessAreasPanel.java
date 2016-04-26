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
 * AccessAreaPanel.java
 *
 * Created on Apr 3, 2009, 2:23:16 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.*;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;


public class AccessAreasPanel extends javax.swing.JPanel {

    private List<DdsReferenceDef> previousRefsList = new ArrayList<DdsReferenceDef>();
    private AdsEntityClassDef adsEntityClassDef = null;
    private AccessAreas accessAreas = null;
    private EAccessAreaType currentAccessAreaType = EAccessAreaType.NONE;
    private boolean firstTime, isUpdating;

    private class PartitionCover extends RadixObject {

        Partition partition;
        String title;

        PartitionCover(AccessAreas accessAreas, Partition partition) {
            title = partition.getName();
            this.partition = partition;
            final List<Id> referenceIdsList = partition.getReferenceIds();

            AdsClassDef destClassDef = accessAreas.getOwnerClass();
            if (referenceIdsList != null && !referenceIdsList.isEmpty()) {
                //append inherited references names
                final StringBuilder stringBuilder = new StringBuilder("References: ");
//                    for (DdsReferenceDef xRef : this.previousRefsList) {
//                        stringBuilder.append(xRef.getName());
//                        stringBuilder.append(", ");
//                    }

                //append own references names
                for (int i = 0, count = referenceIdsList.size(); i < count - 1; ++i) {
                    DdsReferenceDef xRef = (DdsReferenceDef) findByIdFromLayer(accessAreas.getOwnerClass().getModule().getSegment().getLayer(), referenceIdsList.get(i));
                    if (xRef != null) {
                        stringBuilder.append(xRef.getName());
                        stringBuilder.append(", ");
                    }
                }
                //.getModule().getSegment().getLayer()
                Layer layer = accessAreas.getOwnerClass().getModule().getSegment().getLayer();
                DdsReferenceDef lastXRef =
                        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<DdsReferenceDef>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<DdsReferenceDef> controller, Layer layer) {
                        DdsReferenceDef ref = (DdsReferenceDef) findByIdFromLayer(layer, referenceIdsList.get(referenceIdsList.size() - 1));
                        if (ref != null) {
                            controller.setResultAndStop(ref);
                        }
                    }
                });

                if (lastXRef != null) {
                    stringBuilder.append(lastXRef.getName());
                }

                //add references list node
                title += " " + stringBuilder.toString() + " ";
                //newPartitionNode.add(new DefaultMutableTreeNode(stringBuilder.toString()));

                //add properties list node
                Id destClassId = null;
                if (lastXRef != null) {
                    destClassId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_ENTITY_CLASS.getValue() + lastXRef.getParentTableId().toString().substring(3));
                    destClassDef = AdsSearcher.Factory.newAdsClassSearcher(destClassDef).findById(destClassId).get();
                }
            }
            final AdsPropertyDef propDef = destClassDef == null ? null : destClassDef.getProperties().findById(partition.getPropertyId(), EScope.ALL).get();
            title += " - " + (propDef == null ? "???" : propDef.getName());
            //newPartitionNode.add(new DefaultMutableTreeNode("Property: " + (propDef == null ? "???" : propDef.getName())));

        }
        //  @Override

        @Override
        public String toString() {
            return title;
        }

        @Override
        public String getName() {
            //final DdsAccessPartitionFamilyDef ddsAccessPartitionFamilyDef = findApf();
            //return ddsAccessPartitionFamilyDef == null ? "Wrong id " + familyId : ddsAccessPartitionFamilyDef.getName();
            return title;
        }

        @Override
        public RadixIcon getIcon() {
            return partition.getIcon();
        }
    }

    private void parentReferencesComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {

        if (!firstTime && !isUpdating) {

            final Object obj = evt.getItem();
            if (obj instanceof DdsReferenceDefWrapper) {
                accessAreas.setInheritReferenceId(((DdsReferenceDefWrapper) obj).getDdsReferenceDef().getId());
            } else {
                accessAreas.setInheritReferenceId(null);
            }
            updateState();
        }
    }

    private static AccessArea getAccessAreaRelatedToNode(DefaultMutableTreeNode node) {

        while (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof AccessArea) {
                return ((AccessArea) userObject);
            }

            node = (DefaultMutableTreeNode) node.getParent();
        }

        return null;
    }

    private AccessArea getSelectedAccessArea() {
        return getAccessAreaRelatedToNode((DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent());
    }

    private void updateButtons() {

        final boolean isOwnAccessArea = accessAreas.contains(getSelectedAccessArea());

        //areasMaxCount
        int rowCount = bodyTree.getRowCount();

        int areaCount = 0;
        for (int i = 0; i < rowCount; i++) {
            if (bodyTree.getPathForRow(i).getParentPath().getParentPath() == null) {
                areaCount++;
            }
        }


        addAccessAreaButton.setEnabled(
                areaCount < AdsEntityClassDef.AccessAreas.MAX_AREAS_COUNT
                && (accessAreas.getType().equals(EAccessAreaType.INHERITED)
                || accessAreas.getType().equals(EAccessAreaType.OWN)));
        addAccessPartitionButton.setEnabled(isOwnAccessArea);
        final boolean isEditableNode;
        if (isOwnAccessArea) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
            final Object userObject = node.getUserObject();
            isEditableNode = (userObject instanceof AccessArea) || (userObject instanceof PartitionCover);
        } else {
            isEditableNode = false;
        }

        deleteButton.setEnabled(isOwnAccessArea && isEditableNode);
        modifyButton.setEnabled(isOwnAccessArea && isEditableNode);
        attachChildsButton.setEnabled(currClassApf != null);
        detachChildsButton.setEnabled(currClassApf != null);
    }

    /**
     * Creates new form AccessAreaPanel
     */
    public AccessAreasPanel() {

        super();
        initComponents();

        parentReferencesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (isUpdating || firstTime) {
                    return;
                }
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (isUpdating){
//                            return;
//                        }
//                        parentReferencesComboBoxItemStateChanged(e);
//                    }
//                });
                parentReferencesComboBoxItemStateChanged(e);
            }
        });

        this.remove(middlePanel);
        this.remove(bodyPanel);

        bodyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        bodyTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                updateButtons();
            }
        });

        bodyTree.addMouseListener(new MouseAdapter() {
            private static final int DOUBLE_CLICK = 2;

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == DOUBLE_CLICK) {
                    final TreePath treePath = bodyTree.getPathForLocation(e.getX(), e.getY());
                    if (treePath != null) {
                        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                        if (node != null) {
                            final Object userObject = node.getUserObject();
                            if (userObject instanceof AccessArea || userObject instanceof PartitionCover) {
                                if (modifyButton.isEnabled()) {
                                    performButtonsClick();
                                }
                            }
                        }
                    }
                }
            }
        });

        bodyTree.setRootVisible(false);
        bodyTree.setShowsRootHandles(true);
    }

    private void performButtonsClick() {

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
        final Object userObject = node.getUserObject();
        if (userObject instanceof AccessArea) {
            //modify access area
            if (new StateAccessAreaModifyDialog(accessAreas, (AccessArea) userObject).showModal()) {
                updateState();
            }
        } else if (userObject instanceof PartitionCover) {
            //modify access partition
            if (new StatePartitionModifyDialog(adsEntityClassDef, getSelectedAccessArea(), ((PartitionCover) userObject).partition).showModal()) {
                updateState();
            }
        }
    }

    public void open(AdsEntityClassDef adsEntityClassDef) {

        firstTime = true;
        this.adsEntityClassDef = adsEntityClassDef;
        update();
        firstTime = false;
    }

    private void enableButtons(boolean enableFlag) {

        if (enableFlag) {
            //try to restore buttons state
            updateButtons();
        } else {
            addAccessAreaButton.setEnabled(false);
            addAccessPartitionButton.setEnabled(false);
            modifyButton.setEnabled(false);
            deleteButton.setEnabled(false);
            attachChildsButton.setEnabled(false);
            detachChildsButton.setEnabled(false);
        }
    }

    public void setReadonly(boolean readonly) {

        final boolean enabled = !readonly;

        if (currentAccessAreaType == EAccessAreaType.NONE) {
            accessAreaDefinitionKindComboBox.setEnabled(enabled);
        } else if (currentAccessAreaType == EAccessAreaType.INHERITED) {
            accessAreaDefinitionKindComboBox.setEnabled(enabled);
            parentReferencesComboBox.setEnabled(enabled);
            bodyTree.setEnabled(enabled);
            enableButtons(enabled);
        } else if (currentAccessAreaType == EAccessAreaType.OWN) {
            accessAreaDefinitionKindComboBox.setEnabled(enabled);
            bodyTree.setEnabled(enabled);
            enableButtons(enabled);
        }
    }

    private boolean isSelfReferencing(AdsEntityClassDef xDef) {
        return xDef.equals(adsEntityClassDef);
    }

    private boolean isOwnOrInheritedAccessAreasType(AdsEntityClassDef xDef) {
        final EAccessAreaType accessAreasType = xDef.getAccessAreas().getType();
        return (accessAreasType == EAccessAreaType.OWN) || (accessAreasType == EAccessAreaType.INHERITED);
    }

    private static AdsEntityClassDef findParentRefClass(AdsEntityClassDef xDef) {
        DdsReferenceDef ref = xDef.getAccessAreas().findInheritReference();
        if (ref != null) {
            final DdsTableDef parentTableDef = ref.findParentTable(xDef);
            return AdsUtils.findEntityClass(parentTableDef);
        }
        return null;
    }

    private boolean isReferenceSuitable(DdsReferenceDef xDef) {

        final DdsTableDef parentTableDef = xDef.findParentTable(xDef);
        if (parentTableDef != null) {
            //final AdsSearcher.Factory.EntityClassSearcher entityClassSearcher = AdsSearcher.Factory.newEntityClassSearcher(adsEntityClassDef.getModule());
            //final AdsEntityClassDef parentEntityClassDef = entityClassSearcher.findEntityClass(parentTableDef);
            AdsEntityClassDef parentEntityClassDef = AdsUtils.findEntityClass(parentTableDef);
            if (parentEntityClassDef != null) {
                parentEntityClassDef = (AdsEntityClassDef) findByIdFromLayer(accessAreas.getOwnerClass().getModule().getSegment().getLayer(), parentEntityClassDef.getId());
                return parentEntityClassDef != null && !isSelfReferencing(parentEntityClassDef) && isOwnOrInheritedAccessAreasType(parentEntityClassDef);
            }
        }
        return false;
    }

    private void fillParentReferencesComboBox() {

        parentReferencesComboBox.setModel(new DefaultComboBoxModel());
        final DdsTableDef parentTableDef = adsEntityClassDef.findTable(adsEntityClassDef);
        if (parentTableDef != null) {
            final Set<DdsReferenceDef> outgoingReferencesSet = parentTableDef.collectOutgoingReferences();

            for (final DdsReferenceDef xDef : outgoingReferencesSet) {

                //append definitions excluding ones referencing to ourself
                if (isReferenceSuitable(xDef)) {
                    parentReferencesComboBox.addItem(new DdsReferenceDefWrapper(xDef));
                }
            }
        }
    }
    DdsAccessPartitionFamilyDef currClassApf;

    public void update() {
        DdsTableDef tbl = this.adsEntityClassDef.findTable(adsEntityClassDef);
        currClassApf = null;
        if (tbl != null) {
            //isEnabled = true;
            //this.adsEntityClassDef.getModule().getSegment().getLayer().getDds().getModules().
            l:
            for (Module m : this.adsEntityClassDef.getModule().getSegment().getLayer().getDds().getModules()) {
                DdsModule ddsModule = (DdsModule) m;
                while (ddsModule != null) {
                    try {
                        DdsDefinitions<DdsAccessPartitionFamilyDef> apfs = ddsModule.getModelManager().getModel().getAccessPartitionFamilies();
                        for (DdsAccessPartitionFamilyDef apf : apfs) {
                            if (apf.findHead() == tbl) {
                                currClassApf = apf;
                                break l;
                            }
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    ddsModule = ddsModule.findOverwritten();
                }

            }


        }

        accessAreas = this.adsEntityClassDef.getAccessAreas();
        currentAccessAreaType = accessAreas.getType();
        isUpdating = true;
        fillParentReferencesComboBox();

        final AccessAreaTypeComboBoxItem[] accessAreaTypesComboBoxItems;
        boolean isMayNotOwerwrite = //this.adsEntityClassDef.isOverwrite() &&
                this.adsEntityClassDef.getHierarchy().findOverwritten() != null;
        if (!isMayNotOwerwrite) {
            if (currentAccessAreaType == EAccessAreaType.INHERITED || parentReferencesComboBox.getItemCount() > 0) {
                accessAreaTypesComboBoxItems = new AccessAreaTypeComboBoxItem[]{
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.OWN),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.INHERITED),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NONE)
                };
            } else {
                accessAreaTypesComboBoxItems = new AccessAreaTypeComboBoxItem[]{
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.OWN),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NONE)
                };
            }
        } else {
            if (currentAccessAreaType == EAccessAreaType.INHERITED || parentReferencesComboBox.getItemCount() > 0) {
                accessAreaTypesComboBoxItems = new AccessAreaTypeComboBoxItem[]{
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.OWN),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.INHERITED),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NONE),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NOT_OVERRIDDEN)
                };
            } else {
                accessAreaTypesComboBoxItems = new AccessAreaTypeComboBoxItem[]{
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.OWN),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NONE),
                    new AccessAreaTypeComboBoxItem(EAccessAreaType.NOT_OVERRIDDEN)
                };

            }
        }


        accessAreaDefinitionKindComboBox.setModel(new DefaultComboBoxModel(accessAreaTypesComboBoxItems));
        for (int i = 0, count = accessAreaDefinitionKindComboBox.getItemCount(); i < count; ++i) {
            if (((AccessAreaTypeComboBoxItem) accessAreaDefinitionKindComboBox.getItemAt(i)).getAccessAreaType() == currentAccessAreaType) {
                accessAreaDefinitionKindComboBox.setSelectedIndex(i);
                break;
            }
        }

        final DdsReferenceDef inheritReferenceDef = accessAreas.findInheritReference();

        if (inheritReferenceDef != null) {

            for (int i = 0, count = parentReferencesComboBox.getItemCount(); i < count; ++i) {
                final Object object = parentReferencesComboBox.getItemAt(i);
                if (((DdsReferenceDefWrapper) object).getDdsReferenceDef().equals(inheritReferenceDef)) {
                    parentReferencesComboBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            if (currentAccessAreaType == EAccessAreaType.INHERITED) {
                //we have incorrect object's condition, therefore provide additional "<Not defined>" item for parentReferencesComboBox
                Object items[] = new Object[parentReferencesComboBox.getItemCount() + 1];
                items[0] = "<Not defined>";
                for (int i = 0, count = parentReferencesComboBox.getItemCount(); i < count; ++i) {
                    items[i + 1] = parentReferencesComboBox.getItemAt(i);
                }

                parentReferencesComboBox.setModel(new DefaultComboBoxModel(items));
                parentReferencesComboBox.setSelectedIndex(0);
            }
        }

        updateState();
        isUpdating = false;
    }

    private void updateState() {

        this.removeAll();
        bodyTree.setVisible(false);
        clearNodesInTree();

        if (currentAccessAreaType == EAccessAreaType.NONE) {
            this.add(upperPanel);
        } else if (currentAccessAreaType == EAccessAreaType.NOT_OVERRIDDEN) {
            this.add(upperPanel);
            this.add(bodyPanel);
            rebuildTree();
        } else if (currentAccessAreaType == EAccessAreaType.INHERITED) {
            this.add(upperPanel);
            this.add(middlePanel);
            this.add(bodyPanel);
            rebuildTree();
        } else if (currentAccessAreaType == EAccessAreaType.OWN) {
            this.add(upperPanel);
            this.add(bodyPanel);
            rebuildTree();
        }

        expand();

        bodyTree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) ((DefaultTreeModel) bodyTree.getModel()).getRoot()).getPath()));
        bodyTree.setVisible(true);

        this.revalidate();
        this.repaint();
    }

    private void clearNodesInTree() {

        previousRefsList.clear();

        final DefaultTreeModel treeModel = (DefaultTreeModel) bodyTree.getModel();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();

        rootNode.removeAllChildren();

        treeModel.nodeStructureChanged(rootNode);
    }

    private class VisitorProviderFindById extends VisitorProvider {

        final private Id id;

        VisitorProviderFindById(Id id) {
            this.id = id;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof Definition) {
                if (((Definition) radixObject).getId().equals(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    private Definition findByIdFromLayer(Layer layer, Id id) {
        return (Definition) layer.find(new VisitorProviderFindById(id));
    }

    private void appendNode(AccessAreas accessAreas, DdsTableDef table/*
             * , boolean isNodeEnabled
             */) {

        assert (accessAreas != null && table != null);
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) bodyTree.getModel().getRoot();

        final List<AccessArea> list = accessAreas.list();
        for (AccessArea xAccessArea : list) {

            //add access area node
            final DefaultMutableTreeNode newAccessAreaNode = new DefaultMutableTreeNode(xAccessArea);
            final AccessArea.Partitions partitions = xAccessArea.getPartitions();

            for (Partition xPartition : partitions) {

                //add partition node
                final DefaultMutableTreeNode newPartitionNode = new DefaultMutableTreeNode(new PartitionCover(accessAreas, xPartition));
                newAccessAreaNode.add(newPartitionNode);
            }
            rootNode.add(newAccessAreaNode);
        }
    }

    private void rebuildTree() {

        appendNodesInTree(adsEntityClassDef/*
                 * , currentAccessAreaType == EAccessAreaType.OWN
                 */);

        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) bodyTree.getModel().getRoot();
        ((DefaultTreeModel) bodyTree.getModel()).nodeStructureChanged(rootNode);
    }

    private void appendNodesInTree(AdsEntityClassDef currentEntity/*
             * , boolean isNodeEnabled
             */) {

        final AdsModule currentModule = currentEntity.getModule();
        final AccessAreas currentAccessAreas = currentEntity.getAccessAreas();
        final EAccessAreaType currentAccessAreasType = currentAccessAreas.getType();

        if (currentAccessAreasType == EAccessAreaType.NOT_OVERRIDDEN) {
            appendNodesInTree((AdsEntityClassDef) currentEntity.getHierarchy().findOverwritten().get()/*
                     * , false
                     */);
        } else if (currentAccessAreasType == EAccessAreaType.OWN) {
            final DdsTableDef currentTable = currentEntity.findTable(adsEntityClassDef);
            if (currentTable != null) {
                appendNode(currentAccessAreas, currentTable/*
                         * , isNodeEnabled
                         */);
            }
        } else if (currentAccessAreasType == EAccessAreaType.INHERITED) {
            final DdsReferenceDef xDdsReferenceDef = currentAccessAreas.findInheritReference();
            if (xDdsReferenceDef != null) {
                previousRefsList.add(xDdsReferenceDef);
                final DdsTableDef xDdsTableDef = xDdsReferenceDef.findParentTable(currentEntity);
                if (xDdsTableDef != null) {
                    final AdsSearcher.Factory.EntityClassSearcher entityClassSearcher = AdsSearcher.Factory.newEntityClassSearcher(currentModule);
                    final AdsEntityClassDef xEntityClassDef = entityClassSearcher.findEntityClass(xDdsTableDef).get();
                    if (xEntityClassDef != null) {
                        if (currentEntity != xEntityClassDef) {
                            appendNodesInTree(xEntityClassDef/*
                                     * , false
                                     */);
                        }
                    }
                }
            }
            appendNode(currentAccessAreas, currentEntity.findTable(adsEntityClassDef)/*
                     * , isNodeEnabled
                     */);
        }
    }

    protected void expand() {
        final TreeUI treeUi = bodyTree.getUI();
        try {
            bodyTree.setUI(null);
            final TreeNode root = (TreeNode) bodyTree.getModel().getRoot();
            final TreePath parent = new TreePath(root);

            for (int i = 0; i < root.getChildCount(); ++i) {
                final TreePath path = parent.pathByAddingChild(root.getChildAt(i));
                expandAll(path);
            }
        } finally {
            bodyTree.setUI(treeUi);
        }
    }

    private void expandAll(TreePath parent) {

        final TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                final TreeNode n = (TreeNode) e.nextElement();
                final TreePath path = parent.pathByAddingChild(n);
                expandAll(path);
            }
        }

        bodyTree.expandPath(parent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        upperPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        accessAreaDefinitionKindComboBox = new javax.swing.JComboBox();
        middlePanel = new javax.swing.JPanel();
        parentReferenceLabel = new javax.swing.JLabel();
        parentReferencesComboBox = new javax.swing.JComboBox();
        bodyPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bodyTree = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        addAccessAreaButton = new javax.swing.JButton();
        addAccessPartitionButton = new javax.swing.JButton();
        modifyButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        attachChildsButton = new javax.swing.JButton();
        detachChildsButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(0, 42));
        setPreferredSize(new java.awt.Dimension(315, 377));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        upperPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 8, 10));
        upperPanel.setMaximumSize(new java.awt.Dimension(32768, 42));
        upperPanel.setMinimumSize(new java.awt.Dimension(0, 42));

        titleLabel.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.titleLabel.text")); // NOI18N

        accessAreaDefinitionKindComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                accessAreaDefinitionKindComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accessAreaDefinitionKindComboBox, 0, 378, Short.MAX_VALUE))
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(accessAreaDefinitionKindComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(titleLabel))
        );

        add(upperPanel);

        middlePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 10, 10));
        middlePanel.setMaximumSize(new java.awt.Dimension(32878, 32));

        parentReferenceLabel.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.parentReferenceLabel.text")); // NOI18N

        javax.swing.GroupLayout middlePanelLayout = new javax.swing.GroupLayout(middlePanel);
        middlePanel.setLayout(middlePanelLayout);
        middlePanelLayout.setHorizontalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanelLayout.createSequentialGroup()
                .addComponent(parentReferenceLabel)
                .addGap(8, 8, 8)
                .addComponent(parentReferencesComboBox, 0, 462, Short.MAX_VALUE))
        );
        middlePanelLayout.setVerticalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(parentReferencesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(parentReferenceLabel))
        );

        add(middlePanel);

        bodyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 12, 4));

        bodyTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 4));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        bodyTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        bodyTree.setAutoscrolls(true);
        bodyTree.setCellRenderer(new AccessAreasTreeRenderer());
        jScrollPane1.setViewportView(bodyTree);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 8, 8));

        addAccessAreaButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.addAccessAreaButton.text")); // NOI18N
        addAccessAreaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccessAreaButtonActionPerformed(evt);
            }
        });

        addAccessPartitionButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.addAccessPartitionButton.text")); // NOI18N
        addAccessPartitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccessPartitionButtonActionPerformed(evt);
            }
        });

        modifyButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.modifyButton.text")); // NOI18N
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });

        deleteButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.deleteButton.text")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        attachChildsButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.attachChildsButton.text")); // NOI18N
        attachChildsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachChildsButtonActionPerformed(evt);
            }
        });

        detachChildsButton.setText(org.openide.util.NbBundle.getMessage(AccessAreasPanel.class, "AccessAreasPanel.detachChildsButton.text")); // NOI18N
        detachChildsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detachChildsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detachChildsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
            .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
            .addComponent(modifyButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
            .addComponent(addAccessPartitionButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
            .addComponent(addAccessAreaButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
            .addComponent(attachChildsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(addAccessAreaButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addAccessPartitionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modifyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attachChildsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detachChildsButton)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );

        add(bodyPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void accessAreaDefinitionKindComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_accessAreaDefinitionKindComboBoxItemStateChanged

        if (!firstTime && !isUpdating) {
            final AccessAreaTypeComboBoxItem item = (AccessAreaTypeComboBoxItem) evt.getItem();

            this.currentAccessAreaType = item.getAccessAreaType();
            accessAreas.setType(currentAccessAreaType);
            fillParentReferencesComboBox();
            updateState();
        }
    }//GEN-LAST:event_accessAreaDefinitionKindComboBoxItemStateChanged

    private void addAccessPartitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccessPartitionButtonActionPerformed

        final AccessArea xAccessArea = getSelectedAccessArea();

        if (xAccessArea != null) {

            if (new StatePartitionCreationDialog(adsEntityClassDef, xAccessArea).showModal()) {
                updateState();
            }
        }
    }//GEN-LAST:event_addAccessPartitionButtonActionPerformed

    private void addAccessAreaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccessAreaButtonActionPerformed

        if (new StateAccessAreaCreationDialog(accessAreas).showModal()) {
            updateState();
        }
    }//GEN-LAST:event_addAccessAreaButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
        if (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof AccessArea) {

                if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove access area \"" + ((AccessArea) userObject).getName() + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    accessAreas.remove((AccessArea) userObject);
                    updateState();
                }
            } else if (userObject instanceof PartitionCover) {
                final Partition partition = ((PartitionCover) userObject).partition;
                if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove partition \"" + partition.getName() + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    final DefaultMutableTreeNode parentNode = ((DefaultMutableTreeNode) node.getParent());
                    final Object parentUserObject = parentNode.getUserObject();
                    assert (parentUserObject instanceof AccessArea);
                    final AccessArea parentAccessArea = (AccessArea) parentUserObject;
                    parentAccessArea.getPartitions().remove(partition);
                    updateState();
                }
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        performButtonsClick();
    }//GEN-LAST:event_modifyButtonActionPerformed

    private void attachChildsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachChildsButtonActionPerformed
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
        String areaName = null;
        if (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof AccessArea) {
                areaName = ((AccessArea) userObject).getName();
            }
            //isEditableNode = (userObject instanceof AccessArea) || (userObject instanceof PartitionCover);
        }

        AccessAreaChildDialog d = new AccessAreaChildDialog(adsEntityClassDef, areaName, currClassApf);
        d.setTitle("Hierarchy Entity Classes Concerning Access Areas");
        d.showModal();
        update();
    }//GEN-LAST:event_attachChildsButtonActionPerformed

    private void detachChildsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detachChildsButtonActionPerformed
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
        String sAreaName = null;
        if (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof AccessArea) {
                sAreaName = ((AccessArea) userObject).getName();
            }
            //isEditableNode = (userObject instanceof AccessArea) || (userObject instanceof PartitionCover);
        }

        sAreaName = JOptionPane.showInputDialog(this, "Enter Access Area Name: ", sAreaName);
        if (sAreaName == null || sAreaName.isEmpty()) {
            return;
        }
        //List <Partition> partitionList = new ArrayList<Partition>();
        final int[] partitionCount = new int[]{0};
        Id apfId = currClassApf.getId();
        Layer layer = adsEntityClassDef.getModule().getSegment().getLayer();

        final String areaName = sAreaName;
        final Id apfid = apfId;

        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                if (layer.isReadOnly()) {
                    controller.stop();
                    return;
                }
                for (Module mdl : layer.getAds().getModules()) {
                    AdsModule adsMdl = (AdsModule) mdl;
                    Iterator<AdsDefinition> iter = adsMdl.getDefinitions().iterator();
                    while (iter.hasNext()) {
                        AdsDefinition adsDef = iter.next();

                        if (adsDef instanceof AdsEntityClassDef) {
                            AdsEntityClassDef clazz = (AdsEntityClassDef) adsDef;
                            if (!clazz.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
                                //AccessArea currArea = null;
                                for (AccessArea area : clazz.getAccessAreas()) {
                                    if (area.getName().equals(areaName)) {
                                        //currArea = area;
                                        //break;
                                        for (Partition part : area.getPartitions()) {
                                            if (part.getFamilyId().equals(apfid)) {
                                                //partitionList.add(part);
                                                partitionCount[0]++;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        });

        if (partitionCount[0] == 0) {
            JOptionPane.showMessageDialog(this,
                    MessageFormat.format("Partition for the access partition family {0} in the area {1} not found",
                    currClassApf.getName(),
                    sAreaName));
        } else {
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                    this,
                    MessageFormat.format("Do you wont remove {0} partition(s)?", partitionCount[0]),
                    "Input box",
                    JOptionPane.OK_CANCEL_OPTION)) {


                layer = adsEntityClassDef.getModule().getSegment().getLayer();
                final String areaName2 = sAreaName;
                final Id apfid2 = apfId;
                Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                        if (layer.isReadOnly()) {
                            controller.stop();
                            return;
                        }
                        for (Module mdl : layer.getAds().getModules()) {
                            AdsModule adsMdl = (AdsModule) mdl;
                            Iterator<AdsDefinition> iter = adsMdl.getDefinitions().iterator();
                            while (iter.hasNext()) {
                                AdsDefinition adsDef = iter.next();

                                if (adsDef instanceof AdsEntityClassDef) {
                                    AdsEntityClassDef clazz = (AdsEntityClassDef) adsDef;
                                    if (!clazz.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
                                        //AccessArea currArea = null;
                                        for (AccessArea area : clazz.getAccessAreas()) {
                                            if (area.getName().equals(areaName2)) {
                                                //currArea = area;
                                                //break;
                                                for (Partition part : area.getPartitions()) {
                                                    if (part.getFamilyId().equals(apfid2)) {
                                                        area.getPartitions().remove(part);
                                                        break;
                                                    }
                                                }
                                                if (area.getPartitions().size() == 0) {
                                                    clazz.getAccessAreas().remove(area);
                                                }
                                                break;
                                            }
                                        }
                                        if (clazz.getAccessAreas().size() == 0) {
                                            if (clazz.getAccessAreas().getType().equals(EAccessAreaType.OWN)) {
                                                clazz.getAccessAreas().setType(EAccessAreaType.NONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                //пробежимся еще раз и удалим все инхерит класс которые ссылаются на
                //класс и EAccessAreaType.NONE
                layer = adsEntityClassDef.getModule().getSegment().getLayer();

                Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                        if (layer.isReadOnly()) {
                            controller.stop();
                            return;
                        }
                        for (Module mdl : layer.getAds().getModules()) {
                            AdsModule adsMdl = (AdsModule) mdl;
                            Iterator<AdsDefinition> iter = adsMdl.getDefinitions().iterator();
                            while (iter.hasNext()) {
                                AdsDefinition adsDef = iter.next();
                                if (adsDef instanceof AdsEntityClassDef) {
                                    AdsEntityClassDef clazz = (AdsEntityClassDef) adsDef;
                                    if (clazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED)) {
                                        if (!classMayInheritAccessArea(clazz)) {
                                            if (clazz.getAccessAreas().size() > 0) {
                                                clazz.getAccessAreas().setType(EAccessAreaType.OWN);
                                            } else {
                                                clazz.getAccessAreas().setType(EAccessAreaType.NONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                update();
            }
        }


    }//GEN-LAST:event_detachChildsButtonActionPerformed
    private static boolean classMayInheritAccessArea(AdsEntityClassDef clazz) {
        AdsEntityClassDef ownerClazz = findParentRefClass(clazz);
        if (ownerClazz == null) {
            return false;
        }
        if (clazz == ownerClazz) {
            return false;
        }
        if (ownerClazz.getAccessAreas().getType().equals(EAccessAreaType.NOT_OVERRIDDEN)) {
            return true;
        }
        if (ownerClazz.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
            return false;
        }
        if (ownerClazz.getAccessAreas().getType().equals(EAccessAreaType.OWN)) {
            return ownerClazz.getAccessAreas().size() > 0;
        }
        return classMayInheritAccessArea(ownerClazz);
    }

    private static class DdsReferenceDefWrapper {

        private final DdsReferenceDef xDef;

        public DdsReferenceDefWrapper(DdsReferenceDef xDef) {
            this.xDef = xDef;
        }

        @Override
        public String toString() {
            return xDef.getQualifiedName();
        }

        public DdsReferenceDef getDdsReferenceDef() {
            return xDef;
        }
    }

    private class AccessAreasTreeRenderer extends DefaultTreeCellRenderer {

        private Color defaultTextNonSelectionColor, defaultTextSelectionColor;

        public AccessAreasTreeRenderer() {
            super();
            defaultTextNonSelectionColor = super.getTextNonSelectionColor();
            defaultTextSelectionColor = super.getTextSelectionColor();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.setBorderSelectionColor(super.getBackgroundSelectionColor()); //prevent unusable black border when run under GNOME desktop environment

            if (value != null) {

                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                final Object userObject = node.getUserObject();

                if (accessAreas != null) {
                    if (accessAreas.contains(getAccessAreaRelatedToNode(node))) {
                        super.setTextNonSelectionColor(Color.BLACK);  //own nodes
                        super.setTextSelectionColor(Color.BLACK);     //
                    } else {
                        super.setTextNonSelectionColor(Color.GRAY);  //inherited nodes
                        super.setTextSelectionColor(Color.GRAY);     //
                    }
                } else {
                    super.setTextNonSelectionColor(defaultTextNonSelectionColor);
                    super.setTextSelectionColor(defaultTextSelectionColor);
                }

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (userObject instanceof AccessArea) {
                    super.setText(((AccessArea) userObject).getName());
                } else if (userObject instanceof Partition) {
                    super.setText("Family: " + ((Partition) userObject).getName());
                } else if (userObject instanceof RadixObject) {
                    super.setText(((RadixObject) userObject).getName());
                }

                if (userObject instanceof RadixObject) {
                    final RadixIcon radixIcon = ((RadixObject) userObject).getIcon();
                    if (radixIcon != null) {
                        super.setIcon(radixIcon.getIcon());
                    }
                }
            } else {
                super.setTextNonSelectionColor(defaultTextNonSelectionColor);
                super.setTextSelectionColor(defaultTextSelectionColor);

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }
            return this;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox accessAreaDefinitionKindComboBox;
    private javax.swing.JButton addAccessAreaButton;
    private javax.swing.JButton addAccessPartitionButton;
    private javax.swing.JButton attachChildsButton;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JTree bodyTree;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton detachChildsButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JButton modifyButton;
    private javax.swing.JLabel parentReferenceLabel;
    private javax.swing.JComboBox parentReferencesComboBox;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables
}
