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
 * NewPartitionPanel.java
 *
 * Created on Apr 9, 2009, 5:26:34 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Properties;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


public class PartitionSetupPanel extends StateAbstractDialog.StateAbstractPanel {

    private final List<DdsReferenceDef> resultListOfReferences;
    private Object resultObject = null;
    protected AdsEntityClassDef adsEntityClassDef = null;
    protected Definition selectedDdsAccessPartitionFamilyDefHead = null;
    protected DdsAccessPartitionFamilyDef selectedDdsAccessPartitionFamilyDef = null;
    private final AccessArea accessArea;

    /**
     * Creates new form NewPartitionPanel
     */
    public PartitionSetupPanel(AdsEntityClassDef adsEntityClassDef, AccessArea accessArea) {
        super();


        this.adsEntityClassDef = adsEntityClassDef;
        this.accessArea = accessArea;
        resultListOfReferences = new ArrayList<>();

        initComponents();

        definitionLinkEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                final Definition selectedDefinition = definitionLinkEditPanel.getDefinition();

                if (selectedDefinition == null || !(selectedDefinition instanceof DdsAccessPartitionFamilyDef)) {
                    selectedDdsAccessPartitionFamilyDef = null;
                    selectedDdsAccessPartitionFamilyDefHead = null;
                } else {
                    selectedDdsAccessPartitionFamilyDef = (DdsAccessPartitionFamilyDef) selectedDefinition;
                    selectedDdsAccessPartitionFamilyDefHead = selectedDdsAccessPartitionFamilyDef.getHead();
                }

                updateState();
            }
        });

        final List<Id> listToExclude = new ArrayList<>();

        final Partitions partitions = accessArea.getPartitions();
        for (Partition xPartition : partitions) {
            final DdsAccessPartitionFamilyDef apf = xPartition.findApf();
            if (apf != null) {
                listToExclude.add(apf.getId());
            }
        }

        definitionLinkEditPanel.open(new AccessPartitionFamiliesCfg(accessArea.getOwnerAreas().getOwnerClass().getModule().getSegment().getLayer(), new AccessPartitionFamiliesTunedProvider(listToExclude)), null, null);

        bodyTree.setCellRenderer(new ReferencesTreeRenderer());
        bodyTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

                final DefaultMutableTreeNode knot = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

                if (knot instanceof ReferenceDefaultMutableTreeNode) {

                    final Object userObject = knot.getUserObject();
                    if (userObject instanceof AdsEntityClassDef) {

                        final AdsEntityClassDef classDef = (AdsEntityClassDef) userObject;
                        knot.removeAllChildren();
                        appendAllNodes(knot, classDef);
                        /*if (knot.getChildCount() == 0) {
                         //exclude unusable node containing dummy userobject
                         final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) knot.getParent();
                         parent.remove(knot);
                         ((DefaultTreeModel) bodyTree.getModel()).nodeStructureChanged(parent);
                            
                         } else {*/
                        ((DefaultTreeModel) bodyTree.getModel()).nodeStructureChanged(knot);
                        //}
                    }
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                //do nothing
            }
        });

        bodyTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                final Object lastSelectedPathComponent = bodyTree.getLastSelectedPathComponent();
                if (lastSelectedPathComponent != null) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) bodyTree.getLastSelectedPathComponent();
                    if (node.isLeaf()) {
                        final Object userObject = node.getUserObject();
                        if ((userObject instanceof AdsInnateColumnPropertyDef) || (userObject instanceof DdsColumnDef)) {

                            PartitionSetupPanel.this.resultObject = userObject;

                            //calculate list of refs
                            PartitionSetupPanel.this.resultListOfReferences.clear();

                            TreeNode parent = node.getParent();
                            while (parent != null && (parent instanceof ReferenceDefaultMutableTreeNode)) {
                                PartitionSetupPanel.this.resultListOfReferences.add(((ReferenceDefaultMutableTreeNode) parent).getRef());
                                parent = parent.getParent();
                            }

                            java.util.Collections.reverse(resultListOfReferences);

                            stateManager.ok();
                            changeSupport.fireChange();
                            return;
                        }
                    }
                }

                stateManager.error("Property not selected");
                changeSupport.fireChange();
            }
        });


        bodyTree.setRootVisible(false);
        bodyTree.setShowsRootHandles(true);

        updateState();
    }

    protected final void clearNodesInTree() {

        final DefaultTreeModel treeModel = (DefaultTreeModel) bodyTree.getModel();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();

        rootNode.removeAllChildren();

        treeModel.nodeStructureChanged(rootNode);
    }

    private void updateState() {

        bodyTree.setVisible(false);
        clearNodesInTree();
        rebuildTree();
        bodyTree.setVisible(true);

        check();

        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        definitionLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bodyTree = new javax.swing.JTree();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        bodyTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        bodyTree.setAutoscrolls(true);
        jScrollPane1.setViewportView(bodyTree);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(PartitionSetupPanel.class, "PartitionSetupPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(PartitionSetupPanel.class, "PartitionSetupPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(definitionLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(definitionLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public Partition getPartition() {
        assert (!stateManager.isErrorneous());

        /* берем по колонке таблицу, по таблице класс
         * берем все св-ва класса
         * пробегаемса по ним
         * если св-во явл-ся AdsInnateColumnPropertyDef и колонка вытянутая по getColumnInfo() этого свойства равна нашей колонке, то мы нашли подходящее св-во, и возвращаем новую партицию, созданную по этому св-ву
         * если не нашли подходящего св-ва, то запрашиваем разрешение на создание нового AdsInnateColumnPropertyDef св-ва, и возвращаем новую партицию созданную по этому св-ву
         *
         * */

        if (resultObject instanceof AdsInnateColumnPropertyDef) {
            return accessArea.newPartition(selectedDdsAccessPartitionFamilyDef, (AdsInnateColumnPropertyDef) resultObject, resultListOfReferences);
        } else if (resultObject instanceof DdsColumnDef) {

            final DdsColumnDef ourColumn = (DdsColumnDef) resultObject;
            final DdsTableDef table = ourColumn.getOwnerTable();
            if (table != null) {

                final AdsSearcher.Factory.EntityClassSearcher searcher = AdsSearcher.Factory.newEntityClassSearcher(adsEntityClassDef.getModule());
                final AdsEntityClassDef entityClassDef = searcher.findEntityClass(table).get();
                if (entityClassDef != null) {
                    final Properties properties = entityClassDef.getProperties();
                    final List<AdsPropertyDef> listOfProperties = properties.get(EScope.ALL);

                    //try to find by the same column
                    for (AdsPropertyDef xProperty : listOfProperties) {
                        if (xProperty instanceof AdsInnateColumnPropertyDef) {
                            final AdsInnateColumnPropertyDef innateProperty = ((AdsInnateColumnPropertyDef) xProperty);
                            final DdsColumnDef xColumnDef = innateProperty.getColumnInfo().findColumn();
                            if (xColumnDef == ourColumn) {
                                return accessArea.newPartition(selectedDdsAccessPartitionFamilyDef, innateProperty, resultListOfReferences);
                            }
                        }
                    }

                    //if not found, ask for new AdsInnateColumnPropertyDef creation
                    if (JOptionPane.showConfirmDialog(new JFrame(), "Do you want to create property for column?", "Property Creation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        final AdsInnateColumnPropertyDef newProperty = AdsInnateColumnPropertyDef.Factory.newInstance(ourColumn);
                        properties.getLocal().add(newProperty);
                        return accessArea.newPartition(selectedDdsAccessPartitionFamilyDef, newProperty, resultListOfReferences);
                    }
                }
            }

        }

        return null;
    }

    protected final void rebuildTree() {

        final DefaultTreeModel treeModel = (DefaultTreeModel) bodyTree.getModel();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();

        if (selectedDdsAccessPartitionFamilyDef != null) {
            final DefaultMutableTreeNode newNode = createNodeForClass(adsEntityClassDef);
            rootNode.add(newNode);
        }
        treeModel.nodeStructureChanged(rootNode);
    }

    private void appendAllNodes(DefaultMutableTreeNode destinationNode, AdsEntityClassDef classDef) {

        if (selectedDdsAccessPartitionFamilyDefHead != null) {
            if (selectedDdsAccessPartitionFamilyDefHead instanceof AdsEnumDef) {
                //append child nodes for suitable class properties
                appendNodesForSuitableProperties(destinationNode, classDef);
            } else if (selectedDdsAccessPartitionFamilyDefHead instanceof DdsTableDef) {
                //append child nodes for suitable table's columns
                appendNodesForSuitableColumns(destinationNode, classDef);
            }
        }

        //append nodes for classes which've been found by references for current class
        appendReferencesNodes(destinationNode, classDef);

    }

    private DefaultMutableTreeNode createNodeForClass(AdsEntityClassDef classDef) {

        //create node for current class
        DefaultMutableTreeNode resultNode = new DefaultMutableTreeNode(classDef);

        //append all needed nodes and nodes resolved by the references
        appendAllNodes(resultNode, classDef);

        return resultNode;
    }

    private void appendReferencesNodes(DefaultMutableTreeNode destinationNode, AdsEntityClassDef classDef) {

        final DdsTableDef table = classDef.findTable(classDef);
        if (table != null) {

            final Set<DdsReferenceDef> outgoingReferences = table.collectOutgoingReferences();
            for (DdsReferenceDef xRef : outgoingReferences) {

                final DdsTableDef parentXrefTable = xRef.findParentTable(classDef);
                if (parentXrefTable != null) {
                    final AdsEntityClassDef xClassDef = AdsSearcher.Factory.newEntityClassSearcher(classDef.getModule()).findEntityClass(parentXrefTable).get();

                    if (xClassDef != null) {

                        final DefaultMutableTreeNode referenceNode = new ReferenceDefaultMutableTreeNode(xClassDef, xRef);
                        referenceNode.add(new DefaultMutableTreeNode()); //заглушка

                        destinationNode.add(referenceNode);
                    }
                }
            }
        }
    }

    private void appendNodesForSuitableColumns(DefaultMutableTreeNode destinationNode, AdsEntityClassDef classDef) {

        assert (selectedDdsAccessPartitionFamilyDefHead instanceof DdsTableDef);

        final DdsTableDef table = classDef.findTable(classDef);

        if (table != null && table.getId().equals(selectedDdsAccessPartitionFamilyDefHead.getId())) {
            final DdsPrimaryKeyDef primaryKey = table.getPrimaryKey();
            final DdsIndexDef.ColumnsInfo columnsInfo = primaryKey.getColumnsInfo();
            if (columnsInfo.size() == 1) {
                final DdsColumnDef column = columnsInfo.get(0).findColumn();
                if (column != null) {
                    final EValType columnsValType = column.getValType();
                    if ((columnsValType == EValType.STR) || (columnsValType == EValType.INT)) {
                        //i.e. we found suitable column
                        destinationNode.add(new DefaultMutableTreeNode(column));
                    }
                }
            }
        }
    }

    private void appendNodesForSuitableProperties(DefaultMutableTreeNode destinationNode, AdsEntityClassDef classDef) {

        assert (selectedDdsAccessPartitionFamilyDefHead instanceof AdsEnumDef);

        final List<AdsPropertyDef> properties = classDef.getProperties().get(EScope.ALL);
        for (AdsPropertyDef xProperty : properties) {
            if (xProperty instanceof AdsInnateColumnPropertyDef) {
                final AdsTypeDeclaration xAdsTypeDeclaration = xProperty.getValue().getType();
                final AdsType resolvedType = xAdsTypeDeclaration.resolve(xProperty).get();
                if (resolvedType instanceof AdsEnumType) {
                    final AdsEnumDef source = ((AdsEnumType) resolvedType).getSource();
                    final IEnumDef basis = source.findBasis();
                    if (basis != null && basis.equals(selectedDdsAccessPartitionFamilyDefHead)) {
                        //i.e. we found suitable property
                        destinationNode.add(new DefaultMutableTreeNode(new DefaultMutableTreeNode(xProperty)));
                    }
                }
            }
        }
    }

    @Override
    public final void check() {

        if (selectedDdsAccessPartitionFamilyDef == null) {
            stateManager.error("Error: family is not selected");
        } else if (resultObject == null) {
            stateManager.error("Error: Property is not selected");
        } else {
            stateManager.ok();
        }
        changeSupport.fireChange();
    }

    private static class ReferencesTreeRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.setBorderSelectionColor(super.getBackgroundSelectionColor()); //otherwise being running under GNOME there'll be unusable black border

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value != null) {

                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (!(node instanceof ReferenceDefaultMutableTreeNode)) {

                    final Object userObject = node.getUserObject();

                    if (userObject instanceof RadixObject) {
                        super.setText(((RadixObject) userObject).getName());


                        final RadixIcon radixIcon = ((RadixObject) userObject).getIcon();
                        if (radixIcon != null) {
                            super.setIcon(radixIcon.getIcon());
                        }

                    }
                }
            }

            return this;
        }
    }

    protected static class ReferenceDefaultMutableTreeNode extends DefaultMutableTreeNode {

        private DdsReferenceDef ref;

        public ReferenceDefaultMutableTreeNode(AdsEntityClassDef classDef, DdsReferenceDef ref) {
            super(classDef);
            this.ref = ref;
        }

        @Override
        public String toString() {

            if (userObject == null) {
                return null;
            } else {
                return ((RadixObject) userObject).getName() + " (" + ref.getName() + ")";
            }
        }

        public DdsReferenceDef getRef() {
            return ref;
        }
    }

    private class AccessPartitionFamiliesCfg extends ChooseDefinitionCfg {

        protected AccessPartitionFamiliesCfg(final RadixObject context, final VisitorProvider provider) {
            super(context, provider);
        }

        @Override
        public String getTypeTitle() {
            return "Family";
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTree bodyTree;
    protected org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel definitionLinkEditPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    // End of variables declaration//GEN-END:variables
}
