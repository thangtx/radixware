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
 * ValueRangesPanel.java
 *
 * Created on November 7, 2008, 10:28 AM
 */
package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionEvent;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRange;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeListener;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRanges;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ValueRangesPanel extends JPanel {

    private AdsEnumDef adsEnumDef;
    private ValueRanges valueRanges;
    private EValType eValType;
    private boolean readonly;

    /** Creates new form ValueRangesPanel */
    public ValueRangesPanel() {
        super();


        this.adsEnumDef = null;
        this.valueRanges = null;
        this.readonly = false;

        initComponents();

        valueRangesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                ValueRangesPanel.this.updateState();
            }

        });

        regexpField.setEditable(true);
        regexpField.getChangeSupport().addEventListener(new ExtendableTextChangeListener() {

            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                if (!isUpdate){
                    valueRanges.setRegexp(regexpField.getValue().toString());
                }
            }
        });

        javax.swing.JButton clearBtn = regexpField.addButton();
        clearBtn.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(16, 16));
        clearBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 regexpField.setValue("");
                 valueRanges.setRegexp("");
            }
        });
    }

    public void setReadonly(boolean readonly) {

        this.readonly = readonly;
        updateState();
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
        valueRangesTable = new TunedTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        regexpLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        regexpField = new org.radixware.kernel.designer.common.dialogs.components.MultilinedTextField();

        jScrollPane1.setViewportView(valueRangesTable);

        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        addButton.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesPanel.addButton.text")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        removeButton.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesPanel.removeButton.text")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon());
        editButton.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesPanel.editButton.text")); // NOI18N
        editButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        clearButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        clearButton.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesPanel.clearButton.text")); // NOI18N
        clearButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        regexpLabel.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesRegExp")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ValueRangesPanel.class, "ValueRangesTable-Name")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editButton, javax.swing.GroupLayout.Alignment.LEADING, 0, 89, Short.MAX_VALUE)
                            .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(regexpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regexpField, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, clearButton, editButton, removeButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regexpLabel)
                    .addComponent(regexpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

        new StateValueRangeCreationDialog(valueRanges, eValType).showModal();
        ((javax.swing.table.AbstractTableModel) valueRangesTable.getModel()).fireTableDataChanged();
        if (valueRangesTable.getRowCount() > 0) {
            valueRangesTable.setRowSelectionInterval(valueRangesTable.getRowCount() - 1, valueRangesTable.getRowCount() - 1);
        }

        updateState();
}//GEN-LAST:event_addButtonActionPerformed

private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed

    //remove specified row
    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove this value range?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

        final int selectedRow = valueRangesTable.getSelectedRow();
        final int rowIndexInModel = valueRangesTable.convertRowIndexToModel(selectedRow);

        ((ValueRangesTableModel) valueRangesTable.getModel()).removeRow(rowIndexInModel);

        //any rows remain?
        if (valueRangesTable.getRowCount() > 0) {

            if (selectedRow == 0) {
                valueRangesTable.setRowSelectionInterval(0, 0);
            } else {
                valueRangesTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        }

        updateState();
    }
}//GEN-LAST:event_removeButtonActionPerformed

    public void open(AdsEnumDef adsEnumDef) {
        this.adsEnumDef = adsEnumDef;
        this.valueRanges = this.adsEnumDef.getValueRanges();
        this.eValType = this.adsEnumDef.getItemType();
        update();
    }

    private boolean isUpdate = false;
    public void update() {
        isUpdate = true;
        setupTable();
        updateState();
        isUpdate = false;
    }

    private void setupTable() {
        valueRangesTable.setModel(new ValueRangesTableModel(valueRanges, eValType));
        valueRangesTable.setRowHeight(24);
        valueRangesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        valueRangesTable.setAutoCreateColumnsFromModel(false);
        valueRangesTable.setColumnSelectionAllowed(false);
        valueRangesTable.setRowSelectionAllowed(true);
    }

private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed

    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove all value ranges?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

        //remove all rows
        final ValueRangesTableModel valueRangesTableModel = (ValueRangesTableModel) valueRangesTable.getModel();
        valueRangesTableModel.clear();
        updateState();
    }
}//GEN-LAST:event_clearButtonActionPerformed

    private ValueRange getSelectedValueRange() {

        final int selectedRow = valueRangesTable.getSelectedRow();
        return selectedRow != -1 ? valueRanges.get(selectedRow) : null;
    }

    private void updateState() {
        
        this.readonly = this.adsEnumDef.isReadOnly();

        boolean isStr = eValType.equals(EValType.STR);
        regexpLabel.setVisible(isStr);
        regexpField.setVisible(isStr);
        regexpField.setEnabled(!readonly);
        
        if (isStr){
            regexpField.setValue(valueRanges.getRegexp());
        }

        valueRangesTable.setEnabled(!readonly);
        addButton.setEnabled(!readonly);
        removeButton.setEnabled(!readonly && valueRangesTable.getSelectedRow() != -1);
        clearButton.setEnabled(!readonly && valueRangesTable.getRowCount() > 0);
        editButton.setEnabled(!readonly && valueRangesTable.getSelectedRow() != -1);
    }

private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed

    final int currentSelectedRow = valueRangesTable.getSelectedRow();

    new StateValueRangeModifyDialog(valueRanges, eValType, getSelectedValueRange()).showModal();
    ((javax.swing.table.AbstractTableModel) valueRangesTable.getModel()).fireTableDataChanged();

    valueRangesTable.setRowSelectionInterval(currentSelectedRow, currentSelectedRow);

    updateState();
}//GEN-LAST:event_editButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.designer.common.dialogs.components.MultilinedTextField regexpField;
    private javax.swing.JLabel regexpLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JTable valueRangesTable;
    // End of variables declaration//GEN-END:variables
}
