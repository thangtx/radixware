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
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class ColumnValuesPanel extends javax.swing.JPanel implements IUpdateable {

    private DdsColumnDef column = null;
    private EValType columnType = null;
    private IEnumDef enumDef = null;
    private boolean readOnly = false;
    private boolean inherited = false;
    private volatile boolean updating = false;
    private final StateDisplayer stateDisplayer = new StateDisplayer();
    private final SqmlEditorPanel sqmlEditorPanel = new SqmlEditorPanel();

    /**
     * Creates new form ColumnValuesPanel
     */
    public ColumnValuesPanel() {
        initComponents();
        sqmlPanel.add(sqmlEditorPanel, BorderLayout.CENTER);
        IStateDisplayer.Locator.register(stateDisplayer, this);

        sequenceLinkEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (!updating) {
                    Id definitionId = sequenceLinkEditPanel.getDefinitionId();
                    column.setSequenceId(definitionId);
                }
            }
        });

        columnDefaultValueEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //FIXED: 16.11.09 #if (!updating && hasDefaultValue())#
                if (!updating && column != null) {
                    column.setDefaultValue(columnDefaultValueEditPanel.getValue());
                }
            }
        });

        checkConstraintDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && column.getCheckConstraint() != null && checkConstraintDbNameEditPanel.isComplete()) {
                    column.getCheckConstraint().setDbName(checkConstraintDbNameEditPanel.getDbName());
                }
            }
        });

        this.setVisible(false);
        stateDisplayer.setVisible(false);
    }

    public StateDisplayer getStateDisplayer() {
        return stateDisplayer;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private boolean hasDefaultValue() {
        return column != null ? (column.getDefaultValue() != null) : false;
    }

    private void updateEnableState() {
        sequenceLinkEditPanel.setEnabled(!readOnly && !inherited);
        defaultValueCheckBox.setEnabled(!readOnly && !inherited);
        columnDefaultValueEditPanel.setEnabled(hasDefaultValue() && !readOnly && !inherited);
        generateCheckConstraintCheckBox.setEnabled(!readOnly && !inherited);
        boolean hasCheckConstraint = column != null ? (column.getCheckConstraint() != null) : false;
        checkConstraintAutoDbNameCheckBox.setEnabled(!readOnly && !inherited && hasCheckConstraint);
        checkConstraintDbNameEditPanel.setEditable(!readOnly && !inherited && hasCheckConstraint && !column.getCheckConstraint().isAutoDbName());
        verificationRuleLabel.setEnabled(hasCheckConstraint);
        sqmlEditorPanel.setEnabled(hasCheckConstraint);
        sqmlEditorPanel.setEditable(!readOnly && !inherited);

        if (column != null) {
            final String dbType = column.getDbType().toUpperCase();
            final boolean isLOB = "BLOB".equals(dbType) || "CLOB".equals(dbType) || column.getValType() == EValType.NATIVE_DB_TYPE;

            auditSaveOnDeleteCheckBox.setEnabled(!readOnly && !inherited
                    && column.getOwnerTable().getAuditInfo().isEnabledForDelete()
                    && !(column.isPrimaryKey() && !auditSaveOnDeleteCheckBox.isSelected()));
            auditSaveOnUpdateCheckBox.setEnabled(!readOnly && !inherited
                    && column.getOwnerTable().getAuditInfo().isEnabledForUpdate()
                    && !(column.isPrimaryKey() && !auditSaveOnUpdateCheckBox.isSelected()));
            auditSaveOnInsertCheckBox.setEnabled(!readOnly && !inherited
                    && column.getOwnerTable().getAuditInfo().isEnabledForInsert()
                    && !(column.isPrimaryKey() && !auditSaveOnInsertCheckBox.isSelected()));

            auditUpdateCheckBox.setEnabled(!readOnly && !inherited
                    && (!isLOB || (isLOB && column.getAuditInfo().isOnUpdate()))
                    && column.getOwnerTable().getAuditInfo().isEnabledForUpdate()
                    && !(column.isPrimaryKey() && !auditUpdateCheckBox.isSelected()));
            if (isLOB && column.getAuditInfo().isOnUpdate()) {
                auditUpdateCheckBox.setForeground(Color.red);
            } else {
                auditUpdateCheckBox.setForeground(Color.black);
            }
        } else {
            auditSaveOnDeleteCheckBox.setEnabled(!readOnly && !inherited);
            auditSaveOnUpdateCheckBox.setEnabled(!readOnly && !inherited);
            auditSaveOnInsertCheckBox.setEnabled(!readOnly && !inherited);
            auditUpdateCheckBox.setEnabled(!readOnly && !inherited);
        }
    }

    /**
     *
     * @param column may be null
     */
    public void setColumn(DdsColumnDef column, boolean inherited) {
        if (Utils.equals(this.column, column)) {
            updateEnableState();
            return;
        }
        this.column = column;
        this.inherited = inherited;
        if (column == null) {
            this.setVisible(false);
            stateDisplayer.setVisible(false);
            return;
        }
        update();
        this.setVisible(true);
        stateDisplayer.setVisible(true);
    }

    public void updateDefaultValue() {
//        updating = true;
        if (column == null) {
            return;
        }
        final AdsEnumDef columnEnumDef = AdsEnumUtils.findColumnEnum(column);
        if (!Utils.equals(column.getValType(), columnType)) {
            column.setDefaultValue(null);
            List<ValAsStr> list = column.getInitialValues();
            for (int i = 0; i < list.size(); ++i) {
                list.set(i, null);
            }
            columnType = column.getValType();
            if (columnEnumDef != null) {
                columnDefaultValueEditPanel.setEnum(columnEnumDef, null);
            } else {
                columnDefaultValueEditPanel.setValue(columnType, null);
            }
        } else if (!Utils.equals(columnEnumDef, enumDef)) {
            column.setDefaultValue(null);
            List<ValAsStr> list = column.getInitialValues();
            for (int i = 0; i < list.size(); ++i) {
                list.set(i, null);
            }
            enumDef = columnEnumDef;
            if (enumDef != null) {
                columnDefaultValueEditPanel.setEnum(enumDef, null);
            } else {
                columnDefaultValueEditPanel.setValue(columnType, null);
            }
        } else if (columnEnumDef != null) {
            columnDefaultValueEditPanel.setEnum(columnEnumDef, column.getDefaultValue());
        } else {
            columnDefaultValueEditPanel.setValue(column.getValType(), column.getDefaultValue());
        }
        defaultValueCheckBox.setSelected(column.getDefaultValue() != null);
        updateEnableState();
//        columnDefaultValueEditPanel.setEnabled(column.getDefaultValue() != null);
//        updating = false;
    }

    private void updateCheckConstraint() {
//        updating = true;
        boolean hasCheckConstraint = column != null && column.getCheckConstraint() != null;
        generateCheckConstraintCheckBox.setSelected(hasCheckConstraint);
        if (hasCheckConstraint) {
            DdsCheckConstraintDef constraint = column.getCheckConstraint();
            checkConstraintAutoDbNameCheckBox.setSelected(constraint.isAutoDbName());
//            if (constraint.isAutoDbName())
//                constraint.setDbName(constraint.calcAutoDbName());
            checkConstraintDbNameEditPanel.setDbName(constraint.getDbName());
            sqmlEditorPanel.open(constraint.getCondition());
        } else {
            checkConstraintAutoDbNameCheckBox.setSelected(false);
            checkConstraintDbNameEditPanel.setDbName("");
        }
//        updating = false;
    }

    public void open(OpenInfo openInfo) {
        boolean hasCheckConstraint = column != null && column.getCheckConstraint() != null;
        if (hasCheckConstraint) {
            DdsCheckConstraintDef constraint = column.getCheckConstraint();
            sqmlEditorPanel.open(constraint.getCondition(), openInfo);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        defaultValueCheckBox = new javax.swing.JCheckBox();
        generateCheckConstraintCheckBox = new javax.swing.JCheckBox();
        columnDefaultValueEditPanel = new org.radixware.kernel.designer.common.editors.defaultvalue.RadixDefaultValueEditPanel();
        checkConstraintAutoDbNameCheckBox = new javax.swing.JCheckBox();
        checkConstraintDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        verificationRuleLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        auditSaveOnInsertCheckBox = new javax.swing.JCheckBox();
        auditSaveOnUpdateCheckBox = new javax.swing.JCheckBox();
        auditSaveOnDeleteCheckBox = new javax.swing.JCheckBox();
        auditUpdateCheckBox = new javax.swing.JCheckBox();
        sequenceLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        sqmlPanel = new javax.swing.JPanel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.jLabel1.text")); // NOI18N

        defaultValueCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.defaultValueCheckBox.text")); // NOI18N
        defaultValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                defaultValueCheckBoxItemStateChanged(evt);
            }
        });

        generateCheckConstraintCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.generateCheckConstraintCheckBox.text")); // NOI18N
        generateCheckConstraintCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateCheckConstraintCheckBoxItemStateChanged(evt);
            }
        });

        checkConstraintAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.checkConstraintAutoDbNameCheckBox.text")); // NOI18N
        checkConstraintAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkConstraintAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        verificationRuleLabel.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.verificationRuleLabel.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.jPanel1.border.title"))); // NOI18N

        auditSaveOnInsertCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.auditSaveOnInsertCheckBox.text")); // NOI18N
        auditSaveOnInsertCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                auditSaveOnInsertCheckBoxItemStateChanged(evt);
            }
        });

        auditSaveOnUpdateCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.auditSaveOnUpdateCheckBox.text")); // NOI18N
        auditSaveOnUpdateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                auditSaveOnUpdateCheckBoxItemStateChanged(evt);
            }
        });

        auditSaveOnDeleteCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.auditSaveOnDeleteCheckBox.text")); // NOI18N
        auditSaveOnDeleteCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                auditSaveOnDeleteCheckBoxItemStateChanged(evt);
            }
        });

        auditUpdateCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnValuesPanel.class, "ColumnValuesPanel.auditUpdateCheckBox.text")); // NOI18N
        auditUpdateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                auditUpdateStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(auditSaveOnUpdateCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(auditUpdateCheckBox))
                    .addComponent(auditSaveOnInsertCheckBox)
                    .addComponent(auditSaveOnDeleteCheckBox))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(auditSaveOnInsertCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(auditSaveOnUpdateCheckBox)
                    .addComponent(auditUpdateCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(auditSaveOnDeleteCheckBox)
                .addContainerGap())
        );

        sqmlPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(columnDefaultValueEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sequenceLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                            .addComponent(defaultValueCheckBox)
                            .addComponent(generateCheckConstraintCheckBox)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(checkConstraintAutoDbNameCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkConstraintDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(verificationRuleLabel)))
                .addContainerGap())
            .addComponent(sqmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sequenceLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defaultValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(columnDefaultValueEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateCheckConstraintCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkConstraintAutoDbNameCheckBox)
                    .addComponent(checkConstraintDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verificationRuleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sqmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void defaultValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_defaultValueCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        columnDefaultValueEditPanel.setEnabled(defaultValueCheckBox.isSelected());
        if (defaultValueCheckBox.isSelected()) {
            column.setDefaultValue(columnDefaultValueEditPanel.getValue());
        } else {
            column.setDefaultValue(null);
        }
    }//GEN-LAST:event_defaultValueCheckBoxItemStateChanged

    private void generateCheckConstraintCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateCheckConstraintCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (generateCheckConstraintCheckBox.isSelected()) {
            DdsCheckConstraintDef constraint = DdsCheckConstraintDef.Factory.newInstance();
            column.setCheckConstraint(constraint);
            DbNameUtils.updateAutoDbNames(constraint);
        } else {
            column.setCheckConstraint(null);
        }
        updating = true;
        updateCheckConstraint();
        updating = false;
        updateEnableState();
    }//GEN-LAST:event_generateCheckConstraintCheckBoxItemStateChanged

    private void checkConstraintAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkConstraintAutoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        DdsCheckConstraintDef constraint = column.getCheckConstraint();
        if (constraint == null) {
            return;
        }
        boolean isAutoDbName = checkConstraintAutoDbNameCheckBox.isSelected();
        checkConstraintDbNameEditPanel.setEditable(!isAutoDbName);
        constraint.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = constraint.calcAutoDbName();
            constraint.setDbName(dbName);
            updating = true;
            checkConstraintDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (checkConstraintAutoDbNameCheckBox.hasFocus()) {
                checkConstraintDbNameEditPanel.requestFocusInWindow();
                checkConstraintDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_checkConstraintAutoDbNameCheckBoxItemStateChanged

    private void auditSaveOnInsertCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_auditSaveOnInsertCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        column.getAuditInfo().setSaveValueOnInsert(auditSaveOnInsertCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_auditSaveOnInsertCheckBoxItemStateChanged

    private void auditSaveOnUpdateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_auditSaveOnUpdateCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        column.getAuditInfo().setSaveValuesOnUpdate(auditSaveOnUpdateCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_auditSaveOnUpdateCheckBoxItemStateChanged

    private void auditSaveOnDeleteCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_auditSaveOnDeleteCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        column.getAuditInfo().setSaveValueOnDelete(auditSaveOnDeleteCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_auditSaveOnDeleteCheckBoxItemStateChanged

    private void auditUpdateStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_auditUpdateStateChanged
        if (updating) {
            return;
        }
        column.getAuditInfo().setOnUpdate(auditUpdateCheckBox.isSelected());
        updateEnableState();
    }//GEN-LAST:event_auditUpdateStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox auditSaveOnDeleteCheckBox;
    private javax.swing.JCheckBox auditSaveOnInsertCheckBox;
    private javax.swing.JCheckBox auditSaveOnUpdateCheckBox;
    private javax.swing.JCheckBox auditUpdateCheckBox;
    private javax.swing.JCheckBox checkConstraintAutoDbNameCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel checkConstraintDbNameEditPanel;
    private org.radixware.kernel.designer.common.editors.defaultvalue.RadixDefaultValueEditPanel columnDefaultValueEditPanel;
    private javax.swing.JCheckBox defaultValueCheckBox;
    private javax.swing.JCheckBox generateCheckConstraintCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel sequenceLinkEditPanel;
    private javax.swing.JPanel sqmlPanel;
    private javax.swing.JLabel verificationRuleLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        if (column == null) {
            return;
        }
        updating = true;
        columnType = column.getValType();
        enumDef = AdsEnumUtils.findColumnEnum(column);

        VisitorProvider provider = DdsVisitorProviderFactory.newSequenceProvider();
        Id id = column.getSequenceId();
        Definition definition = column.findSequence();
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(column, provider);
        sequenceLinkEditPanel.open(cfg, definition, id);

        updateDefaultValue();
        updateCheckConstraint();

        auditSaveOnDeleteCheckBox.setSelected(column.getAuditInfo().isSaveValueOnDelete());
        auditSaveOnUpdateCheckBox.setSelected(column.getAuditInfo().isSaveValuesOnUpdate());
        auditSaveOnInsertCheckBox.setSelected(column.getAuditInfo().isSaveValueOnInsert());
        auditUpdateCheckBox.setSelected(column.getAuditInfo().isOnUpdate());

        updateEnableState();
        updating = false;
    }
}
