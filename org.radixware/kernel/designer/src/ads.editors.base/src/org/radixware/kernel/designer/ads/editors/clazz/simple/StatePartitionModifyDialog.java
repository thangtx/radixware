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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.Properties;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.simple.PartitionSetupPanel.ReferenceDefaultMutableTreeNode;


class StatePartitionModifyDialog extends StateAbstractDialog {

    private static class StatePartitionModifyPanel extends PartitionSetupPanel {

        private Partition currentEditingPartition;

        public StatePartitionModifyPanel(AdsEntityClassDef adsEntityClassDef, AccessArea accessArea, Partition currentEditingPartition) {
            super(adsEntityClassDef, accessArea);

            this.currentEditingPartition = currentEditingPartition;

            selectedDdsAccessPartitionFamilyDef = currentEditingPartition.findApf();
            selectedDdsAccessPartitionFamilyDefHead = selectedDdsAccessPartitionFamilyDef == null ? null : selectedDdsAccessPartitionFamilyDef.getHead();

            definitionLinkEditPanel.open(selectedDdsAccessPartitionFamilyDef, selectedDdsAccessPartitionFamilyDef == null ? null : selectedDdsAccessPartitionFamilyDef.getId());
            definitionLinkEditPanel.setEnabled(false);

            restorePathForSelectedProperty();
        }

        private void restorePathForSelectedProperty() {

            bodyTree.setVisible(false);
            clearNodesInTree();
            rebuildTree();

            final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) ((DefaultTreeModel) bodyTree.getModel()).getRoot();
            if (rootNode.getChildCount() > 0) {
                restoreTree((DefaultMutableTreeNode) rootNode.getChildAt(0));
            }
            bodyTree.setVisible(true);

            this.revalidate();
            this.repaint();
        }

        private void expandAndSelectNode(DefaultMutableTreeNode node) {
            final TreePath treePath = new TreePath(node.getPath());
            bodyTree.expandPath(treePath);
            bodyTree.setSelectionPath(treePath);
            bodyTree.scrollPathToVisible(treePath);
        }
        

        private void restoreTree(DefaultMutableTreeNode currentNode) {

            final List<Id> referenceIdsList = currentEditingPartition.getReferenceIds();
            final Id currentPropertyId = currentEditingPartition.getPropertyId();
            final int refsCount = referenceIdsList.size();

            int currentRefsIndex = 0;
            DefaultMutableTreeNode xNode = currentNode;

            //restore tree for all subsequent refs in referenceIdsList
            while (currentRefsIndex < refsCount) {

                boolean found = false;
                for (int i = 0, count = xNode.getChildCount(); i < count; ++i) {
                    final Object childNode = xNode.getChildAt(i);

                    if (childNode instanceof ReferenceDefaultMutableTreeNode) {
                        final ReferenceDefaultMutableTreeNode referenceDefaultMutableTreeNode = (ReferenceDefaultMutableTreeNode) childNode;
                        if (referenceIdsList.get(currentRefsIndex).equals(referenceDefaultMutableTreeNode.getRef().getId())) {
                            ++currentRefsIndex;
                            found = true;
                            bodyTree.expandPath(new TreePath(referenceDefaultMutableTreeNode.getPath()));
                            xNode = referenceDefaultMutableTreeNode;
                            break;
                        }
                    }
                }

                if (!found){ //prevent infinite loop
                    break;
                }
            }

            //try to select final leaf
            for (int i = 0, count = xNode.getChildCount(); i < count; ++i) {
                final Object childNode = xNode.getChildAt(i);

                if (childNode instanceof DefaultMutableTreeNode) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) childNode;
                    final Object userObject = node.getUserObject();
                    if (userObject instanceof AdsInnateColumnPropertyDef) {
                        if (((AdsInnateColumnPropertyDef) userObject).getId().equals(currentPropertyId)) {
                            expandAndSelectNode(node);
                            break;
                        }
                    } else if (userObject instanceof DdsColumnDef) {

                        final DdsColumnDef ourColumn = (DdsColumnDef) userObject;
                        final DdsTableDef table = ourColumn.getOwnerTable();
                        if (table != null) {

                            final AdsSearcher.Factory.EntityClassSearcher searcher = AdsSearcher.Factory.newEntityClassSearcher(adsEntityClassDef.getModule());
                            final AdsEntityClassDef entityClassDef = searcher.findEntityClass(table).get();
                            if (entityClassDef != null) {
                                final Properties properties = entityClassDef.getProperties();
                                final List<AdsPropertyDef> listOfProperties = properties.get(EScope.ALL);
                                for (AdsPropertyDef xProperty : listOfProperties) {
                                    if (xProperty.getId().equals(currentPropertyId)) {
                                        expandAndSelectNode(node);
                                        break;
                                    }
                                }
                            }
                        }

                        if (((DdsColumnDef) userObject).getId().equals(currentPropertyId)) {
                            expandAndSelectNode(node);
                            break;
                        }
                    }
                }
            }
        }
    }
    private AccessArea accessArea;
    private Partition currentEditingPartition;

    public StatePartitionModifyDialog(AdsEntityClassDef adsEntityClassDef, AccessArea accessArea, Partition currentEditingPartition) {
        super(new StatePartitionModifyPanel(adsEntityClassDef, accessArea, currentEditingPartition), "Edit Partition");
        this.currentEditingPartition = currentEditingPartition;
        this.accessArea = accessArea;
    }

    @Override
    protected void apply() {

        final Partition newPartition = ((PartitionSetupPanel) getComponent()).getPartition();
        if (newPartition != null) {
            final Partitions partitions = accessArea.getPartitions();
            partitions.remove(currentEditingPartition);
            partitions.add(newPartition);
        }
    }
}

