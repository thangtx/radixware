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
 * ColumnMainOptionsPanel.java
 *
 * Created on Feb 6, 2009, 11:37:33 AM
 */
package org.radixware.kernel.designer.dds.editors.table;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.DdsColumnTemplateEditor;


public class ColumnMainOptionsPanel extends javax.swing.JPanel implements IUpdateable {

    @Override
    public void update() {
        if (column == null) {
            return;
        }
        updating = true;
        initDdsColumnTemplateEditor();
        if (column.getExpression() != null) {
            sqmlEditorPanel.open(column.getExpression());
        }
        columnHiddenCheckBox.setSelected(column.isHidden());
        columnGenerateInDbCheckBox.setSelected(column.isGeneratedInDb());
        columnAutoDbNameCheckBox.setSelected(column.isAutoDbName());
//        if (column.isAutoDbName())
//            column.setDbName(column.calcAutoDbName());
        columnDbNameEditPanel.setDbName(column.getDbName());
        sqlExpressionCheckBox.setSelected(column.getExpression() != null);
        updateEnableState();
        updating = false;
    }

    public interface ColumnChangeListener {

        public void columnChanged(DdsColumnDef column);
    }
    private DdsColumnDef column = null;
    private DdsColumnTemplateEditor ddsColumnTemplateEditor = null;
    private boolean readOnly = false;
    private boolean inherited = false;
    private volatile boolean updating = false;
    private final StateDisplayer stateDisplayer = new StateDisplayer();
    private final ArrayList<ColumnChangeListener> listeners = new ArrayList<ColumnChangeListener>();

    /**
     * Creates new form ColumnMainOptionsPanel
     */
    public ColumnMainOptionsPanel() {
        initComponents();
        IStateDisplayer.Locator.register(stateDisplayer, this);


        columnDbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && columnDbNameEditPanel.isComplete()) {
                    column.setDbName(columnDbNameEditPanel.getDbName());
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

    private boolean canEditDbName() {
        if (column == null) {
            return false;
        }
        String dbName = column.getDbName();
        if (dbName.length() == 30 && dbName.substring(2).startsWith("$$")) {
            return false;
        }
        return true;
    }

    private void updateEnableState() {
        if (ddsColumnTemplateEditor != null) {
            ddsColumnTemplateEditor.setReadOnly(readOnly || inherited);
        }
        sqmlEditorPanel.setEnabled(column != null ? column.getExpression() != null : false);
        sqmlEditorPanel.setEditable(!readOnly && !inherited);
        columnHiddenCheckBox.setEnabled(!readOnly && !inherited);
        boolean editable = canEditDbName();
        columnGenerateInDbCheckBox.setEnabled(!readOnly && !inherited && editable);
        boolean isAutoDbName = column != null ? column.isAutoDbName() : false;
        boolean isGenerateInDb = column != null ? column.isGeneratedInDb() : false;
        columnAutoDbNameCheckBox.setEnabled(!readOnly && !inherited && isGenerateInDb && editable);
        columnDbNameEditPanel.setEditable(!readOnly && !inherited && !isAutoDbName && isGenerateInDb && editable);
        sqlExpressionCheckBox.setEnabled(!readOnly && !inherited);
    }

    /**
     *
     * @param column may be null
     */
    public void setColumn(DdsColumnDef column, boolean inherited) {
        if (Utils.equals(this.column, column)) {
            if (ddsColumnTemplateEditor != null) {
                ddsColumnTemplateEditor.updateName();
            }
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

    private void initDdsColumnTemplateEditor() {
        if (ddsColumnTemplateEditor != null) {
            ddsColumnTemplateEditor.setVisible(false);
            columnTemplateEditorPanel.getLayout().removeLayoutComponent(ddsColumnTemplateEditor);
            stateDisplayer.getStateContext().reset();
        }
        ddsColumnTemplateEditor = (new DdsColumnTemplateEditor.Factory()).newInstance(column);
        columnTemplateEditorPanel.add(ddsColumnTemplateEditor, BorderLayout.CENTER);
        ddsColumnTemplateEditor.addDdsColumnTemplateChangeListener(new DdsColumnTemplateEditor.DdsColumnTemplateChangeListener() {
            @Override
            public void ddsColumnTemplateChanged(DdsColumnTemplateDef column) {
                if (updating) {
                    return;
                }
                DdsColumnDef col = (DdsColumnDef) column;
                if (col.isAutoDbName()) {
                    //FIX 24.02.2010 of RADIX-2883
                    if (!col.isReadOnly()) {
                        DbNameUtils.updateAutoDbNames(col);
                    }
                    updating = true;
                    columnDbNameEditPanel.setDbName(col.getDbName());
                    updating = false;
                }
                for (ColumnChangeListener listener : listeners) {
                    listener.columnChanged(col);
                }
            }
        });
    }

    @Override
    public boolean requestFocusInWindow() {
        ddsColumnTemplateEditor.requestFocusInWindow();
        return super.requestFocusInWindow();
    }

    public void addColumnChangeListener(ColumnChangeListener listener) {
        listeners.add(listener);
    }

    public void removeColumnChangeListener(ColumnChangeListener listener) {
        listeners.remove(listener);
    }

    public void open(OpenInfo openInfo) {
        sqmlEditorPanel.open(column.getExpression(), openInfo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        columnTemplateEditorPanel = new javax.swing.JPanel();
        columnHiddenCheckBox = new javax.swing.JCheckBox();
        columnGenerateInDbCheckBox = new javax.swing.JCheckBox();
        columnAutoDbNameCheckBox = new javax.swing.JCheckBox();
        columnDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        sqlExpressionCheckBox = new javax.swing.JCheckBox();
        sqmlEditorPanel = new org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel();

        setLayout(new java.awt.GridBagLayout());

        columnTemplateEditorPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 0, 0));
        columnTemplateEditorPanel.setPreferredSize(new java.awt.Dimension(608, 240));
        columnTemplateEditorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(columnTemplateEditorPanel, gridBagConstraints);

        columnHiddenCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnMainOptionsPanel.class, "ColumnMainOptionsPanel.columnHiddenCheckBox.text")); // NOI18N
        columnHiddenCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                columnHiddenCheckBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        add(columnHiddenCheckBox, gridBagConstraints);

        columnGenerateInDbCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnMainOptionsPanel.class, "ColumnMainOptionsPanel.columnGenerateInDbCheckBox.text")); // NOI18N
        columnGenerateInDbCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                columnGenerateInDbCheckBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        add(columnGenerateInDbCheckBox, gridBagConstraints);

        columnAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnMainOptionsPanel.class, "ColumnMainOptionsPanel.columnAutoDbNameCheckBox.text")); // NOI18N
        columnAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                columnAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 24, 0, 0);
        add(columnAutoDbNameCheckBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        add(columnDbNameEditPanel, gridBagConstraints);

        sqlExpressionCheckBox.setText(org.openide.util.NbBundle.getMessage(ColumnMainOptionsPanel.class, "ColumnMainOptionsPanel.sqlExpressionCheckBox.text")); // NOI18N
        sqlExpressionCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sqlExpressionCheckBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        add(sqlExpressionCheckBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        add(sqmlEditorPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void columnHiddenCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_columnHiddenCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        column.setHidden(columnHiddenCheckBox.isSelected());
    }//GEN-LAST:event_columnHiddenCheckBoxItemStateChanged

    private void columnGenerateInDbCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_columnGenerateInDbCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isGenerateInDb = columnGenerateInDbCheckBox.isSelected();
        column.setGeneratedInDb(isGenerateInDb);
        columnAutoDbNameCheckBox.setEnabled(isGenerateInDb);
        columnDbNameEditPanel.setEditable(!column.isAutoDbName() && isGenerateInDb);
    }//GEN-LAST:event_columnGenerateInDbCheckBoxItemStateChanged

    private void columnAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_columnAutoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isAutoDbName = columnAutoDbNameCheckBox.isSelected();
        columnDbNameEditPanel.setEditable(!isAutoDbName);
        column.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = column.calcAutoDbName();
            column.setDbName(dbName);
            updating = true;
            columnDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (columnAutoDbNameCheckBox.hasFocus()) {
                columnDbNameEditPanel.requestFocusInWindow();
                columnDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_columnAutoDbNameCheckBoxItemStateChanged

    private void sqlExpressionCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sqlExpressionCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (sqlExpressionCheckBox.isSelected()) {
            column.createNewExpression();
        } else {
            column.removeExpression();
        }
        if (column.getExpression() != null) {
            sqmlEditorPanel.open(column.getExpression());
        }
        sqmlEditorPanel.setEnabled(column.getExpression() != null);
    }//GEN-LAST:event_sqlExpressionCheckBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox columnAutoDbNameCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel columnDbNameEditPanel;
    private javax.swing.JCheckBox columnGenerateInDbCheckBox;
    private javax.swing.JCheckBox columnHiddenCheckBox;
    private javax.swing.JPanel columnTemplateEditorPanel;
    private javax.swing.JCheckBox sqlExpressionCheckBox;
    private org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel sqmlEditorPanel;
    // End of variables declaration//GEN-END:variables
}
