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

package org.radixware.kernel.designer.ads.editors.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JPopupMenu;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import org.radixware.kernel.designer.common.dialogs.components.MenuButton;


final class ServicesCatalogEditor extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static final KernelEnumComboBoxModel.Item<ERuntimeEnvironmentType> ALL = new KernelEnumComboBoxModel.SpecialItem<>("<all>");
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final KernelEnumComboBoxModel<ERuntimeEnvironmentType> model = new KernelEnumComboBoxModel<>(
            ERuntimeEnvironmentType.class, Arrays.asList(ALL),
            Collections.<ERuntimeEnvironmentType>emptyList(), ERuntimeEnvironmentType.COMMON);
    private AdsModule module;

    public ServicesCatalogEditor() {
        initComponents();

        cmbEnvironment.setModel(model);

        ((BeanTreeView) servicesTree).setRootVisible(false);
        ((BeanTreeView) servicesTree).setPopupAllowed(false);
        ((BeanTreeView) servicesTree).setDragSource(false);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public void open(AdsModule module) {
        this.module = module;
        if (module != null) {
            explorerManager.setRootContext(new AbstractNode(Children.create(
                    new ServiceNodeFactory(module, model.getSelectedItemSource()), true)));

            cmdAdd.setEnabled(!module.isReadOnly());
            cmdRemove.setEnabled(!module.isReadOnly());
        }
    }

    private JPopupMenu getAddPoupMenu() {

        final JPopupMenu root = new JPopupMenu("test");
        root.add("Service").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MetaInfServicesProvider.addService(module, model.getSelectedItemSource());
            }
        });

        final DefinitionNode selectedNode = (DefinitionNode) getSelectedNode();
        if (selectedNode != null) {
            root.add("Implementation").addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    final AdsPath idPath;

                    if (selectedNode instanceof ServiceNode) {
                        idPath = selectedNode.getDefinitionIdPath();
                    } else if (selectedNode instanceof ImplementationNode) {
                        idPath = ((DefinitionNode) selectedNode.getParentNode()).getDefinitionIdPath();
                    } else {
                        idPath = null;
                    }

                    MetaInfServicesProvider.addImplementation(module, idPath, model.getSelectedItemSource());
                }
            });
        }

        return root;
    }

    private Node getSelectedNode() {
        final Node[] selectedNodes = explorerManager.getSelectedNodes();
        if (selectedNodes.length > 0) {
            return selectedNodes[0];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cmbEnvironment = new javax.swing.JComboBox();
        cmdRemove = new javax.swing.JButton();
        cmdAdd = new MenuButton(
            "Add",
            RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13),
            null) {

            protected JPopupMenu getPopupMenu() {
                return getAddPoupMenu();
            }
        };
        servicesTree = new BeanTreeView();
        lblEnvironment = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        setLayout(new java.awt.GridBagLayout());

        cmbEnvironment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEnvironmentItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 8);
        add(cmbEnvironment, gridBagConstraints);

        cmdRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        cmdRemove.setText(org.openide.util.NbBundle.getMessage(ServicesCatalogEditor.class, "ServicesCatalogEditor.cmdRemove.text")); // NOI18N
        cmdRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(cmdRemove, gridBagConstraints);

        cmdAdd.setText(org.openide.util.NbBundle.getMessage(ServicesCatalogEditor.class, "ServicesCatalogEditor.cmdAdd.text")); // NOI18N
        cmdAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(cmdAdd, gridBagConstraints);

        servicesTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(servicesTree, gridBagConstraints);

        lblEnvironment.setText(org.openide.util.NbBundle.getMessage(ServicesCatalogEditor.class, "ServicesCatalogEditor.lblEnvironment.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        add(lblEnvironment, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        removeService();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmbEnvironmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEnvironmentItemStateChanged
        environmentChange();
    }//GEN-LAST:event_cmbEnvironmentItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbEnvironment;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel lblEnvironment;
    private javax.swing.JScrollPane servicesTree;
    // End of variables declaration//GEN-END:variables

    private void removeService() {
        final Node[] selectedNodes = explorerManager.getSelectedNodes();
        if (selectedNodes.length > 0) {
            for (final Node node : selectedNodes) {

                if (node instanceof ServiceNode) {
                    module.getServicesCatalog().removeService(((ServiceNode) node).getDefinitionIdPath(), model.getSelectedItemSource());
                } else if (node instanceof ImplementationNode) {
                    final ServiceNode serviceNode = (ServiceNode) node.getParentNode();
                    module.getServicesCatalog().removeService(serviceNode.getDefinitionIdPath(),
                            ((ImplementationNode) node).getDefinitionIdPath());
                }
            }
        }
    }

    private void environmentChange() {
        open(module);

        updateButtonsState();
    }

    private void updateButtonsState() {
        cmdAdd.setEnabled(model.getSelectedItemSource() != null && !module.isReadOnly());
    }
}
