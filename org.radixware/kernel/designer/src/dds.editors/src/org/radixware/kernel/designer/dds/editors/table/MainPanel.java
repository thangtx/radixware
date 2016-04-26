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

package org.radixware.kernel.designer.dds.editors.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDdsViewWithOption;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.sqlscript.actions.ShowReCreateScriptAction;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;


class MainPanel extends javax.swing.JPanel {
    
    private final DdsTableDef table;
    private final StateDisplayer stateDisplayer = new StateDisplayer();
    private final boolean inherited;
    private final boolean isView;
    private boolean readOnly = false;
    private final SqmlEditorPanel sqmlEditor = new SqmlEditorPanel();
    private volatile boolean updating = false;

    /**
     * Creates new form MainPanel
     */
    public MainPanel(final DdsTableDef table) {
        initComponents();
        tableTableSpaceDbNameEditPanel.setAllowEmptyName(true);
        sqmlEditorPanel.add(sqmlEditor, BorderLayout.CENTER);
        btShowReCreateScript.setAction(new ShowReCreateScriptAction(table));
        this.table = table;
        inherited = table.findOverwritten() != null;
        IStateDisplayer.Locator.register(stateDisplayer, this);
        
        isView = table instanceof DdsViewDef;
        if (isView) {
            additionalTablePanel.setVisible(false);
            distinctCheckBox.setVisible(false);
            btShowReCreateScript.setVisible(false);
//            tableTableSpaceCheckBox.setVisible(false);
//            tableTableSpaceDbNameEditPanel.setVisible(false);
//            partititoningLabel.setVisible(false);
//            partitioningScrollPane.setVisible(false);
//            tableGlobalTemporaryCheckBox.setVisible(false);
//            tablePreserveRowCheckBox.setVisible(false);
        } else {
            additionalViewPanel.setVisible(false);
//            distinctCheckBox.setVisible(false);
//            checkOptionCheckBox.setVisible(false);
//            readOnlyCheckBox.setVisible(false);
        }
        btCopyId.setIcon(RadixWareIcons.JML_EDITOR.ID.getIcon());
        
        tableNameEditPanel.setNameAcceptor(NameAcceptorFactory.newAcceptorForRename(table));
        
        tableNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && tableNameEditPanel.isComplete()) {
                    table.setName(tableNameEditPanel.getCurrentName());
                    if (table.isAutoDbName()) {
                        DbNameUtils.updateAutoDbNames(table);
                        updating = true;
                        tableDbNameEditPanel.setDbName(table.getDbName());
                        updating = false;
                    }
                }
            }
        });
        
        tableDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && tableDbNameEditPanel.isComplete()) {
                    table.setDbName(tableDbNameEditPanel.getDbName());
                }
            }
        });
        
        tableDbNameEditPanel.setNameAcceptor(NameAcceptorFactory.newDbNameAcceptor(table.getLayer()));
        
        tableTableSpaceDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && tableTableSpaceDbNameEditPanel.isComplete()) {
                    table.setTablespace(tableTableSpaceDbNameEditPanel.getDbName());
                }
            }
        });
        
        referenceLinkEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating) {
                    table.getAuditInfo().setAuditReferenceId(referenceLinkEditPanel.getDefinitionId());
                    updateEnableState();
                }
            }
        });
        chisDeprecated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    table.setDeprecated(chisDeprecated.isSelected());
                }
            }
        });
        
        updating = true;
        setInitialValues();
        updating = false;
    }
    
    public StateDisplayer getStateDisplayer() {
        return stateDisplayer;
    }
    
    private boolean badOverwritten() {
        return (table.findOverwritten() == null) == table.isOverwrite();
    }
    
    private boolean hasTableSpace() {
        return table.getTablespace() != null;//RADIX-8969 && !table.getTablespace().isEmpty();
    }
    
    private boolean isGlobalTemporary() {
        return table.isGlobalTemporary();
    }
    
    private void setInitialValues() {
        tableNameEditPanel.setCurrentName(table.getName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tableNameEditPanel.requestFocusInWindow();
            }
        });
        tableAutoDbNameCheckBox.setSelected(table.isAutoDbName());
        tableDbNameEditPanel.setDbName(table.getDbName());
        
        tableOverwriteCheckBox.setSelected(table.isOverwrite());
        if (badOverwritten()) {
            tableOverwriteCheckBox.setForeground(Color.RED);
        } else {
            tableOverwriteCheckBox.setForeground(tableAutoDbNameCheckBox.getForeground());
        }
        DdsTableDef overwritten = table.findOverwritten();
        if (overwritten != null) {
            tableOverwriteNameEditPanel.setCurrentName(overwritten.getQualifiedName());
        } else {
            tableOverwriteNameEditPanel.setCurrentName("");        
        }
        
        edDescription.open(table);
                
        tableGenerateInDbCheckBox.setSelected(table.isGeneratedInDb());
        tableHiddenCheckBox.setSelected(table.isHidden());
        
        if (isView) {
            DdsViewDef view = (DdsViewDef) table;
            distinctCheckBox.setSelected(view.isDistinct());
            checkOptionCheckBox.setSelected(view.getWithOption() == EDdsViewWithOption.CHECK_OPTION);
            readOnlyCheckBox.setSelected(view.getWithOption() == EDdsViewWithOption.READ_ONLY);
        } else {
            tableTableSpaceCheckBox.setSelected(hasTableSpace());
            if (hasTableSpace()) {
                tableTableSpaceDbNameEditPanel.setDbName(table.getTablespace());
            } else {
                tableTableSpaceDbNameEditPanel.setDbName(TablespaceCalculator.calcTablespaceForTable(table));
            }
            
            sqmlEditor.open(table.getPartition());

//            if (!table.getPartition().getItems().isEmpty()) {
//                partitioningEditorPane.setText(((Scml.Text)table.getPartition().getItems().get(0)).getText());
//            } else {
//                partitioningEditorPane.setText("");
//            }

            tableGlobalTemporaryCheckBox.setSelected(isGlobalTemporary());
            tablePreserveRowCheckBox.setSelected(table.isOnCommitPreserveRows());
        }
        
        tableAuditDeleteCheckBox.setSelected(table.getAuditInfo().isEnabledForDelete());
        tableAuditInsertCheckBox.setSelected(table.getAuditInfo().isEnabledForInsert());
        tableAuditUpdateCheckBox.setSelected(table.getAuditInfo().isEnabledForUpdate());
        referenceLinkEditPanel.open(ChooseDefinitionCfg.Factory.newInstance(
                table.collectOutgoingReferences(DdsVisitorProviderFactory.newReferenceForAuditFilter())),
                table.getAuditInfo().findAuditReference(), table.getAuditInfo().getAuditReferenceId());
        
        tableSupportUserPropertiesCheckBox.setSelected(table.getExtOptions().contains(EDdsTableExtOption.SUPPORT_USER_PROPERTIES));
        tableUseAsUserPropertiesObjectCheckBox.setSelected(table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT));
        tableEnableApplicationClassesCheckBox.setSelected(table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES));
        tableCheckUserConstraintsOnDeletion.setSelected(table.getExtOptions().contains(EDdsTableExtOption.CHECK_USER_CONSTRAINTS_ON_DELETION));
        chisDeprecated.setSelected(table.isDeprecated());
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }
    
    private void updateEnableState() {
        tableNameEditPanel.setEditable(!readOnly);
        tableAutoDbNameCheckBox.setEnabled(!readOnly && !inherited);
        tableDbNameEditPanel.setEditable(!readOnly && !table.isAutoDbName() && !inherited);
        
        tableOverwriteCheckBox.setEnabled(!readOnly && badOverwritten());
        tableOverwriteNameEditPanel.setEnabled(false);
        
        edDescription.setReadonly(readOnly);        
        tableGenerateInDbCheckBox.setEnabled(!readOnly && !inherited);
        tableHiddenCheckBox.setEnabled(!readOnly && !inherited);
        
        if (isView) {
            distinctCheckBox.setEnabled(!readOnly && !inherited);
            checkOptionCheckBox.setEnabled(!readOnly && !inherited);
            readOnlyCheckBox.setEnabled(!readOnly && !inherited);
        } else {
            tableTableSpaceCheckBox.setEnabled(!readOnly && !inherited);
            tableTableSpaceDbNameEditPanel.setEditable(!readOnly && hasTableSpace() && !inherited);

//            partitioningEditorPane.setEditable(!readOnly && !inherited);
            sqmlEditor.setEditable(!readOnly && !inherited);
            
            tableGlobalTemporaryCheckBox.setEnabled(!readOnly && !inherited);
            tablePreserveRowCheckBox.setEnabled(!readOnly && isGlobalTemporary() && !inherited);
        }
        
        
        tableAuditDeleteCheckBox.setEnabled(!readOnly && !inherited);
        tableAuditInsertCheckBox.setEnabled(!readOnly && !inherited);
        tableAuditUpdateCheckBox.setEnabled(!readOnly && !inherited);
        referenceLinkEditPanel.setEnabled(!readOnly && !inherited/* &&
                 (table.getAuditInfo().getAuditReferenceId() != null || tableAuditDeleteCheckBox.isSelected() ||
                 tableAuditInsertCheckBox.isSelected() || tableAuditUpdateCheckBox.isSelected())*/);
        
        boolean detail = table.isDetailTable();
        tableSupportUserPropertiesCheckBox.setEnabled(!readOnly && !inherited && !detail);
        tableUseAsUserPropertiesObjectCheckBox.setEnabled(!readOnly && !inherited && !detail);
        tableEnableApplicationClassesCheckBox.setEnabled(!readOnly && !inherited && !detail);
        tableCheckUserConstraintsOnDeletion.setEnabled(!readOnly && !inherited && !detail);
        chisDeprecated.setEnabled(!readOnly);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableGenerateInDbCheckBox = new javax.swing.JCheckBox();
        tableHiddenCheckBox = new javax.swing.JCheckBox();
        auditPanel = new javax.swing.JPanel();
        tableAuditInsertCheckBox = new javax.swing.JCheckBox();
        tableAuditUpdateCheckBox = new javax.swing.JCheckBox();
        tableAuditDeleteCheckBox = new javax.swing.JCheckBox();
        referenceLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        extensionsOptionsPanel = new javax.swing.JPanel();
        tableSupportUserPropertiesCheckBox = new javax.swing.JCheckBox();
        tableUseAsUserPropertiesObjectCheckBox = new javax.swing.JCheckBox();
        tableEnableApplicationClassesCheckBox = new javax.swing.JCheckBox();
        tableCheckUserConstraintsOnDeletion = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        tableNameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        tableOverwriteCheckBox = new javax.swing.JCheckBox();
        tableOverwriteNameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        tableAutoDbNameCheckBox = new javax.swing.JCheckBox();
        tableDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        additionalTablePanel = new javax.swing.JPanel();
        tableTableSpaceCheckBox = new javax.swing.JCheckBox();
        tableTableSpaceDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        partititoningLabel = new javax.swing.JLabel();
        tableGlobalTemporaryCheckBox = new javax.swing.JCheckBox();
        tablePreserveRowCheckBox = new javax.swing.JCheckBox();
        sqmlEditorPanel = new javax.swing.JPanel();
        additionalViewPanel = new javax.swing.JPanel();
        distinctCheckBox = new javax.swing.JCheckBox();
        checkOptionCheckBox = new javax.swing.JCheckBox();
        readOnlyCheckBox = new javax.swing.JCheckBox();
        chisDeprecated = new javax.swing.JCheckBox();
        btShowReCreateScript = new javax.swing.JButton();
        btCopyId = new javax.swing.JButton();
        edDescription = new org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor();

        tableGenerateInDbCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableGenerateInDbCheckBox.text")); // NOI18N
        tableGenerateInDbCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableGenerateInDbCheckBoxItemStateChanged(evt);
            }
        });

        tableHiddenCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableHiddenCheckBox.text")); // NOI18N
        tableHiddenCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableHiddenCheckBoxItemStateChanged(evt);
            }
        });

        auditPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.auditPanel.border.title"))); // NOI18N

        tableAuditInsertCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableAuditInsertCheckBox.text")); // NOI18N
        tableAuditInsertCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableAuditInsertCheckBoxItemStateChanged(evt);
            }
        });

        tableAuditUpdateCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableAuditUpdateCheckBox.text")); // NOI18N
        tableAuditUpdateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableAuditUpdateCheckBoxItemStateChanged(evt);
            }
        });

        tableAuditDeleteCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableAuditDeleteCheckBox.text")); // NOI18N
        tableAuditDeleteCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableAuditDeleteCheckBoxItemStateChanged(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout auditPanelLayout = new javax.swing.GroupLayout(auditPanel);
        auditPanel.setLayout(auditPanelLayout);
        auditPanelLayout.setHorizontalGroup(
            auditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(auditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(auditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(auditPanelLayout.createSequentialGroup()
                        .addComponent(tableAuditInsertCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(tableAuditUpdateCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(tableAuditDeleteCheckBox))
                    .addGroup(auditPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(referenceLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        auditPanelLayout.setVerticalGroup(
            auditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(auditPanelLayout.createSequentialGroup()
                .addGroup(auditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableAuditInsertCheckBox)
                    .addComponent(tableAuditUpdateCheckBox)
                    .addComponent(tableAuditDeleteCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(auditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(referenceLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        extensionsOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.extensionsOptionsPanel.border.title"))); // NOI18N

        tableSupportUserPropertiesCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableSupportUserPropertiesCheckBox.text")); // NOI18N
        tableSupportUserPropertiesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableSupportUserPropertiesCheckBoxItemStateChanged(evt);
            }
        });

        tableUseAsUserPropertiesObjectCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableUseAsUserPropertiesObjectCheckBox.text")); // NOI18N
        tableUseAsUserPropertiesObjectCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableUseAsUserPropertiesObjectCheckBoxItemStateChanged(evt);
            }
        });

        tableEnableApplicationClassesCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableEnableApplicationClassesCheckBox.text")); // NOI18N
        tableEnableApplicationClassesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableEnableApplicationClassesCheckBoxItemStateChanged(evt);
            }
        });

        tableCheckUserConstraintsOnDeletion.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableCheckUserConstraintsOnDeletion.text")); // NOI18N
        tableCheckUserConstraintsOnDeletion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableCheckUserConstraintsOnDeletionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout extensionsOptionsPanelLayout = new javax.swing.GroupLayout(extensionsOptionsPanel);
        extensionsOptionsPanel.setLayout(extensionsOptionsPanelLayout);
        extensionsOptionsPanelLayout.setHorizontalGroup(
            extensionsOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extensionsOptionsPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(extensionsOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableCheckUserConstraintsOnDeletion)
                    .addComponent(tableUseAsUserPropertiesObjectCheckBox)
                    .addComponent(tableSupportUserPropertiesCheckBox)
                    .addComponent(tableEnableApplicationClassesCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        extensionsOptionsPanelLayout.setVerticalGroup(
            extensionsOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extensionsOptionsPanelLayout.createSequentialGroup()
                .addComponent(tableSupportUserPropertiesCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableUseAsUserPropertiesObjectCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableEnableApplicationClassesCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableCheckUserConstraintsOnDeletion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setLabelFor(tableNameEditPanel);
        jLabel2.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.jLabel2.text")); // NOI18N

        tableOverwriteCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableOverwriteCheckBox.text")); // NOI18N
        tableOverwriteCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableOverwriteCheckBoxItemStateChanged(evt);
            }
        });

        tableAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableAutoDbNameCheckBox.text")); // NOI18N
        tableAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        additionalTablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.additionalTablePanel.border.title"))); // NOI18N

        tableTableSpaceCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableTableSpaceCheckBox.text")); // NOI18N
        tableTableSpaceCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableTableSpaceCheckBoxItemStateChanged(evt);
            }
        });

        partititoningLabel.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.partititoningLabel.text")); // NOI18N

        tableGlobalTemporaryCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tableGlobalTemporaryCheckBox.text")); // NOI18N
        tableGlobalTemporaryCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tableGlobalTemporaryCheckBoxItemStateChanged(evt);
            }
        });

        tablePreserveRowCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.tablePreserveRowCheckBox.text")); // NOI18N
        tablePreserveRowCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tablePreserveRowCheckBoxItemStateChanged(evt);
            }
        });

        sqmlEditorPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout additionalTablePanelLayout = new javax.swing.GroupLayout(additionalTablePanel);
        additionalTablePanel.setLayout(additionalTablePanelLayout);
        additionalTablePanelLayout.setHorizontalGroup(
            additionalTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, additionalTablePanelLayout.createSequentialGroup()
                .addGroup(additionalTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, additionalTablePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(sqmlEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(additionalTablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(additionalTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(additionalTablePanelLayout.createSequentialGroup()
                                .addComponent(tableTableSpaceCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tableTableSpaceDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(additionalTablePanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(tablePreserveRowCheckBox))
                            .addComponent(tableGlobalTemporaryCheckBox)
                            .addComponent(partititoningLabel))))
                .addContainerGap())
        );
        additionalTablePanelLayout.setVerticalGroup(
            additionalTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, additionalTablePanelLayout.createSequentialGroup()
                .addGroup(additionalTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(additionalTablePanelLayout.createSequentialGroup()
                        .addComponent(tableTableSpaceCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(partititoningLabel))
                    .addComponent(tableTableSpaceDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sqmlEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableGlobalTemporaryCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePreserveRowCheckBox)
                .addContainerGap())
        );

        additionalViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.additionalViewPanel.border.title"))); // NOI18N

        distinctCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.distinctCheckBox.text")); // NOI18N
        distinctCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                distinctCheckBoxItemStateChanged(evt);
            }
        });

        checkOptionCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.checkOptionCheckBox.text")); // NOI18N
        checkOptionCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkOptionCheckBoxItemStateChanged(evt);
            }
        });

        readOnlyCheckBox.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.readOnlyCheckBox.text")); // NOI18N
        readOnlyCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                readOnlyCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout additionalViewPanelLayout = new javax.swing.GroupLayout(additionalViewPanel);
        additionalViewPanel.setLayout(additionalViewPanelLayout);
        additionalViewPanelLayout.setHorizontalGroup(
            additionalViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(additionalViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(additionalViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(distinctCheckBox)
                    .addComponent(checkOptionCheckBox)
                    .addComponent(readOnlyCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        additionalViewPanelLayout.setVerticalGroup(
            additionalViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(additionalViewPanelLayout.createSequentialGroup()
                .addComponent(distinctCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkOptionCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(readOnlyCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chisDeprecated.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.chisDeprecated.text")); // NOI18N

        btShowReCreateScript.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.btShowReCreateScript.text")); // NOI18N

        btCopyId.setText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.btCopyId.text")); // NOI18N
        btCopyId.setToolTipText(org.openide.util.NbBundle.getMessage(MainPanel.class, "MainPanel.btCopyId.toolTipText")); // NOI18N
        btCopyId.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btCopyId.setMaximumSize(new java.awt.Dimension(20, 20));
        btCopyId.setMinimumSize(new java.awt.Dimension(20, 20));
        btCopyId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCopyIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(additionalTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(auditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(extensionsOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(tableOverwriteCheckBox)
                            .addComponent(tableAutoDbNameCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tableOverwriteNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tableDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(tableNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chisDeprecated)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCopyId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tableGenerateInDbCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btShowReCreateScript)
                                .addGap(65, 65, 65)
                                .addComponent(tableHiddenCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(additionalViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(100, 100, 100)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btCopyId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tableNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(chisDeprecated)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableOverwriteNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tableOverwriteCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableAutoDbNameCheckBox)
                    .addComponent(tableDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(edDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableHiddenCheckBox)
                    .addComponent(tableGenerateInDbCheckBox)
                    .addComponent(btShowReCreateScript))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(additionalViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(additionalTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(auditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(extensionsOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableGlobalTemporaryCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableGlobalTemporaryCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        tablePreserveRowCheckBox.setEnabled(tableGlobalTemporaryCheckBox.isSelected());
        table.setGlobalTemporary(tableGlobalTemporaryCheckBox.isSelected());
    }//GEN-LAST:event_tableGlobalTemporaryCheckBoxItemStateChanged
    
    private void tableOverwriteCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableOverwriteCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        table.setOverwrite(tableOverwriteCheckBox.isSelected());
        if (!badOverwritten()) {
            tableOverwriteCheckBox.setForeground(tableAutoDbNameCheckBox.getForeground());
            tableOverwriteCheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_tableOverwriteCheckBoxItemStateChanged
    
    private void tableAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableAutoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isAutoDbName = tableAutoDbNameCheckBox.isSelected();
        tableDbNameEditPanel.setEditable(!isAutoDbName);
        table.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = table.calcAutoDbName();
            table.setDbName(dbName);
            updating = true;
            tableDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (tableAutoDbNameCheckBox.hasFocus()) {
                tableDbNameEditPanel.requestFocusInWindow();
                tableDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_tableAutoDbNameCheckBoxItemStateChanged
    
    private void tableTableSpaceCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableTableSpaceCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        tableTableSpaceDbNameEditPanel.setEditable(tableTableSpaceCheckBox.isSelected());
        if (tableTableSpaceCheckBox.isSelected()) {
            if (table.getTablespace() == null) {
                table.setTablespace("");
            }
            if (tableTableSpaceDbNameEditPanel.isComplete()) {
                table.setTablespace(tableTableSpaceDbNameEditPanel.getDbName());
            }
            if (tableTableSpaceCheckBox.hasFocus()) {
                tableTableSpaceDbNameEditPanel.requestFocusInWindow();
                tableTableSpaceDbNameEditPanel.selectAll();
            }
        } else {
            table.setTablespace(null);
            updating = true;
            tableTableSpaceDbNameEditPanel.setDbName(TablespaceCalculator.calcTablespaceForTable(table));
            updating = false;
        }
    }//GEN-LAST:event_tableTableSpaceCheckBoxItemStateChanged
    
    private void tableGenerateInDbCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableGenerateInDbCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        table.setGeneratedInDb(tableGenerateInDbCheckBox.isSelected());
    }//GEN-LAST:event_tableGenerateInDbCheckBoxItemStateChanged
    
    private void tableHiddenCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableHiddenCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        table.setHidden(tableHiddenCheckBox.isSelected());
    }//GEN-LAST:event_tableHiddenCheckBoxItemStateChanged
    
    private void tablePreserveRowCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tablePreserveRowCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        table.setOnCommitPreserveRows(tablePreserveRowCheckBox.isSelected());
    }//GEN-LAST:event_tablePreserveRowCheckBoxItemStateChanged
    
    private void setupAuditReference(boolean isSelected) {
        DdsTableDef.AuditInfo auditInfo = table.getAuditInfo();
        if (isSelected
                && table.isDetailTable()) {
            DdsReferenceDef masterRef = table.findMasterReference();
            if (masterRef != null
                    && (auditInfo.getAuditReferenceId() == null
                    || !auditInfo.getAuditReferenceId().equals(masterRef.getId()))) {
                auditInfo.setAuditReferenceId(masterRef.getId());
                this.referenceLinkEditPanel.open(masterRef, masterRef.getId());
            }
        }
    }
    
    private void tableAuditInsertCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableAuditInsertCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        final boolean isSelected = tableAuditInsertCheckBox.isSelected();
        table.getAuditInfo().setEnabledForInsert(isSelected);
        setupAuditReference(isSelected);
        updateEnableState();
    }//GEN-LAST:event_tableAuditInsertCheckBoxItemStateChanged
    
    private void tableAuditUpdateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableAuditUpdateCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        final boolean isSelected = tableAuditUpdateCheckBox.isSelected();
        table.getAuditInfo().setEnabledForUpdate(isSelected);
        setupAuditReference(isSelected);
        updateEnableState();
    }//GEN-LAST:event_tableAuditUpdateCheckBoxItemStateChanged
    
    private void tableAuditDeleteCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableAuditDeleteCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        final boolean isSelected = tableAuditDeleteCheckBox.isSelected();
        table.getAuditInfo().setEnabledForDelete(isSelected);
        setupAuditReference(isSelected);
        updateEnableState();
    }//GEN-LAST:event_tableAuditDeleteCheckBoxItemStateChanged
    
    private void tableSupportUserPropertiesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableSupportUserPropertiesCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (tableSupportUserPropertiesCheckBox.isSelected()) {
            table.getExtOptions().add(EDdsTableExtOption.SUPPORT_USER_PROPERTIES);
        } else {
            table.getExtOptions().remove(EDdsTableExtOption.SUPPORT_USER_PROPERTIES);
        }
    }//GEN-LAST:event_tableSupportUserPropertiesCheckBoxItemStateChanged
    
    private void tableUseAsUserPropertiesObjectCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableUseAsUserPropertiesObjectCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (tableUseAsUserPropertiesObjectCheckBox.isSelected()) {
            table.getExtOptions().add(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (DialogUtils.messageConfirmation("Generate columns, index and reference for entity used as user property object?")) {
                        new UseAsUserPropertiesObjectGenerator().generate(table);
                    }
                }
            });
        } else {
            table.getExtOptions().remove(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT);
        }
    }//GEN-LAST:event_tableUseAsUserPropertiesObjectCheckBoxItemStateChanged
    
    private void tableEnableApplicationClassesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tableEnableApplicationClassesCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (tableEnableApplicationClassesCheckBox.isSelected()) {
            table.getExtOptions().add(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES);
        } else {
            table.getExtOptions().remove(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES);
        }
    }//GEN-LAST:event_tableEnableApplicationClassesCheckBoxItemStateChanged
    
    private void distinctCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_distinctCheckBoxItemStateChanged
        if (updating || !isView) {
            return;
        }
        ((DdsViewDef) table).setDistinct(distinctCheckBox.isSelected());
    }//GEN-LAST:event_distinctCheckBoxItemStateChanged
    
    private void checkOptionCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkOptionCheckBoxItemStateChanged
        if (updating || !isView) {
            return;
        }
        if (checkOptionCheckBox.isSelected()) {
            updating = true;
            readOnlyCheckBox.setSelected(false);
            updating = false;
            ((DdsViewDef) table).setWithOption(EDdsViewWithOption.CHECK_OPTION);
        } else {
            if (!readOnlyCheckBox.isSelected()) {
                ((DdsViewDef) table).setWithOption(EDdsViewWithOption.NONE);
            }
        }
    }//GEN-LAST:event_checkOptionCheckBoxItemStateChanged
    
    private void readOnlyCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_readOnlyCheckBoxItemStateChanged
        if (updating || !isView) {
            return;
        }
        if (readOnlyCheckBox.isSelected()) {
            updating = true;
            checkOptionCheckBox.setSelected(false);
            updating = false;
            ((DdsViewDef) table).setWithOption(EDdsViewWithOption.READ_ONLY);
        } else {
            if (!checkOptionCheckBox.isSelected()) {
                ((DdsViewDef) table).setWithOption(EDdsViewWithOption.NONE);
            }
        }
    }//GEN-LAST:event_readOnlyCheckBoxItemStateChanged
        
    private void tableCheckUserConstraintsOnDeletionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableCheckUserConstraintsOnDeletionActionPerformed
        if (updating) {
            return;
        }
        if (tableCheckUserConstraintsOnDeletion.isSelected()) {
            table.getExtOptions().add(EDdsTableExtOption.CHECK_USER_CONSTRAINTS_ON_DELETION);
        } else {
            table.getExtOptions().remove(EDdsTableExtOption.CHECK_USER_CONSTRAINTS_ON_DELETION);
        }
    }//GEN-LAST:event_tableCheckUserConstraintsOnDeletionActionPerformed
    
    private void btCopyIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCopyIdActionPerformed
        ClipboardUtils.copyToClipboard(table.getId().toString());
    }//GEN-LAST:event_btCopyIdActionPerformed

    /*if (updating) {
     return;
     }
     String text = partitioningEditorPane.getText();
     if (text.isEmpty())
     table.getPartition().getItems().clear();
     else
     table.getPartition().setSql(text);*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel additionalTablePanel;
    private javax.swing.JPanel additionalViewPanel;
    private javax.swing.JPanel auditPanel;
    private javax.swing.JButton btCopyId;
    private javax.swing.JButton btShowReCreateScript;
    private javax.swing.JCheckBox checkOptionCheckBox;
    private javax.swing.JCheckBox chisDeprecated;
    private javax.swing.JCheckBox distinctCheckBox;
    private org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor edDescription;
    private javax.swing.JPanel extensionsOptionsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel partititoningLabel;
    private javax.swing.JCheckBox readOnlyCheckBox;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel referenceLinkEditPanel;
    private javax.swing.JPanel sqmlEditorPanel;
    private javax.swing.JCheckBox tableAuditDeleteCheckBox;
    private javax.swing.JCheckBox tableAuditInsertCheckBox;
    private javax.swing.JCheckBox tableAuditUpdateCheckBox;
    private javax.swing.JCheckBox tableAutoDbNameCheckBox;
    private javax.swing.JCheckBox tableCheckUserConstraintsOnDeletion;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel tableDbNameEditPanel;
    private javax.swing.JCheckBox tableEnableApplicationClassesCheckBox;
    private javax.swing.JCheckBox tableGenerateInDbCheckBox;
    private javax.swing.JCheckBox tableGlobalTemporaryCheckBox;
    private javax.swing.JCheckBox tableHiddenCheckBox;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel tableNameEditPanel;
    private javax.swing.JCheckBox tableOverwriteCheckBox;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel tableOverwriteNameEditPanel;
    private javax.swing.JCheckBox tablePreserveRowCheckBox;
    private javax.swing.JCheckBox tableSupportUserPropertiesCheckBox;
    private javax.swing.JCheckBox tableTableSpaceCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel tableTableSpaceDbNameEditPanel;
    private javax.swing.JCheckBox tableUseAsUserPropertiesObjectCheckBox;
    // End of variables declaration//GEN-END:variables
}
