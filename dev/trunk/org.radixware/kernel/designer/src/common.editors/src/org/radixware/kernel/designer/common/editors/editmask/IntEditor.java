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
 * IntEditor.java
 *
 * Created on Jul 24, 2009, 4:05:00 PM
 */
package org.radixware.kernel.designer.common.editors.editmask;

import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import javax.swing.JComboBox;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


class IntEditor extends Editor {

    private boolean readOnly = false;
    private Long maxDbValue = null;
    private Long minDbValue = null;

    /**
     * Creates new form IntEditor
     */
    public IntEditor(EditMaskInt editMaskInt) {
        initComponents();

        numberBaseSpinner.setEditor(new CheckedNumberSpinnerEditor(numberBaseSpinner));
        minimumLengthSpinner.setEditor(new CheckedNumberSpinnerEditor(minimumLengthSpinner));
        maximumValueSpinner.setEditor(new CheckedNumberSpinnerEditor(maximumValueSpinner));
        minimumValueSpinner.setEditor(new CheckedNumberSpinnerEditor(minimumValueSpinner));
        stepSizeSpinner.setEditor(new CheckedNumberSpinnerEditor(stepSizeSpinner));

        if (editMaskInt != null) {
            setupInitialValues(editMaskInt);
        }
        updateEnableState();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void setupInitialValues(EditMaskInt editMaskInt) {
        numberBaseSpinner.setValue(Long.valueOf(editMaskInt.getNumberBase()));
        if (editMaskInt.getMinLength() != null) {
            minimumLengthCheckBox.setSelected(true);
            minimumLengthSpinner.setValue(Long.valueOf(editMaskInt.getMinLength()));
            padCharTextField.setText(editMaskInt.getPadChar());
        } else {
            minimumLengthCheckBox.setSelected(false);
        }
        if (editMaskInt.getMinValue() != null) {
            minimumValueCheckBox.setSelected(true);
            minimumValueSpinner.setValue(editMaskInt.getMinValue());
        } else {
            minimumValueCheckBox.setSelected(false);
        }
        if (editMaskInt.getMaxValue() != null) {
            maximumValueCheckBox.setSelected(true);
            maximumValueSpinner.setValue(editMaskInt.getMaxValue());
        } else {
            maximumValueCheckBox.setSelected(false);
        }
        stepSizeSpinner.setValue(Long.valueOf(editMaskInt.getStepSize()));
        if (editMaskInt.getTriadDelimiter() != null) {
            triadDelimeterTextField.setText(editMaskInt.getTriadDelimiter());
        }
        maxDbValue = editMaskInt.getDbMaxValue();
        minDbValue = editMaskInt.getDbMinValue();

        getDelimTypeModel().setSelectedItemSource(editMaskInt.getTriadDelimeterType());
    }

    private void updateEnableState() {
        numberBaseLabel.setEnabled(!readOnly);
        numberBaseSpinner.setEnabled(!readOnly);
        minimumLengthCheckBox.setEnabled(!readOnly);
        minimumLengthSpinner.setEnabled(!readOnly && minimumLengthCheckBox.isSelected());
        padCharLabel.setEnabled(!readOnly && minimumLengthCheckBox.isSelected());
        padCharTextField.setEnabled(!readOnly && minimumLengthCheckBox.isSelected());
        minimumValueCheckBox.setEnabled(!readOnly);
        minimumValueSpinner.setEnabled(!readOnly && minimumValueCheckBox.isSelected());
        maximumValueCheckBox.setEnabled(!readOnly);
        maximumValueSpinner.setEnabled(!readOnly && maximumValueCheckBox.isSelected());
        stepLabel.setEnabled(!readOnly);
        stepSizeSpinner.setEnabled(!readOnly);
        btResetToDb.setEnabled(!readOnly && (maxDbValue != null || minDbValue != null));

        cmbDelimType.setEnabled(!readOnly);

        final ETriadDelimeterType delimeterType = getDelimTypeModel().getSelectedItemSource();
        final boolean specified = !readOnly && delimeterType == ETriadDelimeterType.SPECIFIED;
        triadDelimeterTextField.setEnabled(specified);
        lblTriadDelim.setEnabled(specified);
    }

    private KernelEnumComboBoxModel<ETriadDelimeterType> getDelimTypeModel() {
        return ((KernelEnumComboBoxModel<ETriadDelimeterType>) cmbDelimType.getModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numberBaseLabel = new javax.swing.JLabel();
        numberBaseSpinner = new javax.swing.JSpinner();
        minimumLengthCheckBox = new javax.swing.JCheckBox();
        minimumLengthSpinner = new javax.swing.JSpinner();
        padCharLabel = new javax.swing.JLabel();
        minimumValueCheckBox = new javax.swing.JCheckBox();
        minimumValueSpinner = new javax.swing.JSpinner();
        maximumValueCheckBox = new javax.swing.JCheckBox();
        maximumValueSpinner = new javax.swing.JSpinner();
        stepLabel = new javax.swing.JLabel();
        stepSizeSpinner = new javax.swing.JSpinner();
        padCharTextField = new javax.swing.JFormattedTextField();
        triadDelimeterTextField = new javax.swing.JFormattedTextField();
        btResetToDb = new javax.swing.JButton();
        cmbDelimType = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<ETriadDelimeterType>>();
        lblDelimType = new javax.swing.JLabel();
        lblTriadDelim = new javax.swing.JLabel();

        numberBaseLabel.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.numberBaseLabel.text")); // NOI18N

        numberBaseSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(10L), Long.valueOf(2L), Long.valueOf(36L), Long.valueOf(1L)));

        minimumLengthCheckBox.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.minimumLengthCheckBox.text")); // NOI18N
        minimumLengthCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                minimumLengthCheckBoxItemStateChanged(evt);
            }
        });

        minimumLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(1L), Long.valueOf(127L), Long.valueOf(1L)));

        padCharLabel.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.padCharLabel.text")); // NOI18N

        minimumValueCheckBox.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.minimumValueCheckBox.text")); // NOI18N
        minimumValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                minimumValueCheckBoxItemStateChanged(evt);
            }
        });

        minimumValueSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), null, null, Long.valueOf(1L)));

        maximumValueCheckBox.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.maximumValueCheckBox.text")); // NOI18N
        maximumValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                maximumValueCheckBoxItemStateChanged(evt);
            }
        });

        maximumValueSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), null, null, Long.valueOf(1L)));

        stepLabel.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.stepLabel.text")); // NOI18N

        stepSizeSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(0L), null, Long.valueOf(1L)));

        try {
            padCharTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            triadDelimeterTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btResetToDb.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.btResetToDb.text")); // NOI18N
        btResetToDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetToDb(evt);
            }
        });

        cmbDelimType.setModel(new KernelEnumComboBoxModel(ETriadDelimeterType.class, ETriadDelimeterType.DEFAULT));
        cmbDelimType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDelimTypeItemStateChanged(evt);
            }
        });

        lblDelimType.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.lblDelimType.text")); // NOI18N

        lblTriadDelim.setText(org.openide.util.NbBundle.getMessage(IntEditor.class, "IntEditor.lblTriadDelim.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(numberBaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numberBaseSpinner))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 124, Short.MAX_VALUE)
                        .addComponent(btResetToDb))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minimumLengthCheckBox)
                            .addComponent(minimumValueCheckBox)
                            .addComponent(maximumValueCheckBox))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbDelimType, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(padCharLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(padCharTextField))
                            .addComponent(minimumLengthSpinner)
                            .addComponent(minimumValueSpinner)
                            .addComponent(maximumValueSpinner)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDelimType)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stepLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stepSizeSpinner))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTriadDelim)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(triadDelimeterTextField)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberBaseLabel)
                    .addComponent(numberBaseSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(minimumLengthCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minimumLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(padCharLabel)
                    .addComponent(padCharTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(minimumValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minimumValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(maximumValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maximumValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stepLabel)
                    .addComponent(stepSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDelimType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDelimType, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTriadDelim)
                    .addComponent(triadDelimeterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(btResetToDb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void minimumLengthCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_minimumLengthCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_minimumLengthCheckBoxItemStateChanged

    private void minimumValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_minimumValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_minimumValueCheckBoxItemStateChanged

    private void maximumValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_maximumValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_maximumValueCheckBoxItemStateChanged

    private void resetToDb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetToDb
        if (this.maxDbValue != null) {
            maximumValueSpinner.setValue(this.maxDbValue);
            maximumValueCheckBox.setSelected(true);
        }
        if (this.minDbValue != null) {
            minimumValueSpinner.setValue(this.minDbValue);
            minimumValueCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_resetToDb

    private void cmbDelimTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDelimTypeItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_cmbDelimTypeItemStateChanged

    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskInt)) {
            return;
        }
        EditMaskInt editMaskInt = (EditMaskInt) editMask;

        editMaskInt.setNumberBase(((Long) numberBaseSpinner.getValue()).byteValue());

        if (minimumLengthCheckBox.isSelected()) {
            editMaskInt.setMinLength(((Long) minimumLengthSpinner.getValue()).byteValue());
            editMaskInt.setPadChar(padCharTextField.getText().isEmpty() ? null : padCharTextField.getText());
        } else {
            editMaskInt.setMinLength(null);
            editMaskInt.setPadChar(null);
        }

        if (minimumValueCheckBox.isSelected()) {
            editMaskInt.setMinValue((Long) minimumValueSpinner.getValue());
        } else {
            editMaskInt.setMinValue(null);
        }
        if (maximumValueCheckBox.isSelected()) {
            editMaskInt.setMaxValue((Long) maximumValueSpinner.getValue());
        } else {
            editMaskInt.setMaxValue(null);
        }
        editMaskInt.setStepSize(((Long) stepSizeSpinner.getValue()).longValue());

        final ETriadDelimeterType delimeterType = getDelimTypeModel().getSelectedItemSource();
        if (delimeterType == ETriadDelimeterType.SPECIFIED) {
            editMaskInt.setTriadDelimiter(triadDelimeterTextField.getText());
        }
        editMaskInt.setTriadDelimeterType(delimeterType);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btResetToDb;
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<ETriadDelimeterType>> cmbDelimType;
    private javax.swing.JLabel lblDelimType;
    private javax.swing.JLabel lblTriadDelim;
    private javax.swing.JCheckBox maximumValueCheckBox;
    private javax.swing.JSpinner maximumValueSpinner;
    private javax.swing.JCheckBox minimumLengthCheckBox;
    private javax.swing.JSpinner minimumLengthSpinner;
    private javax.swing.JCheckBox minimumValueCheckBox;
    private javax.swing.JSpinner minimumValueSpinner;
    private javax.swing.JLabel numberBaseLabel;
    private javax.swing.JSpinner numberBaseSpinner;
    private javax.swing.JLabel padCharLabel;
    private javax.swing.JFormattedTextField padCharTextField;
    private javax.swing.JLabel stepLabel;
    private javax.swing.JSpinner stepSizeSpinner;
    private javax.swing.JFormattedTextField triadDelimeterTextField;
    // End of variables declaration//GEN-END:variables
}
