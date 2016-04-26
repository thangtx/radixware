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

package org.radixware.kernel.designer.common.editors.editmask;

import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import java.math.BigDecimal;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


class NumEditor extends Editor {

    private boolean readOnly = false;
    private BigDecimal maxDbValue = null;
    private Long dbPrecision = null;

    /**
     * Creates new form NumEditor
     */
    public NumEditor(EditMaskNum editMaskNum) {
        initComponents();

        minValueSpinner.setModel(new BigDecimalSpinnerModel());
        maxValueSpinner.setModel(new BigDecimalSpinnerModel());

        precisionSpinner.setEditor(new CheckedNumberSpinnerEditor(precisionSpinner));
        minValueSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(minValueSpinner));
        maxValueSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(maxValueSpinner));
        scaleSpinner.setEditor(new CheckedNumberSpinnerEditor(scaleSpinner));
        
        if (editMaskNum != null) {
            maxDbValue = editMaskNum.getDbMaxValue();
            if (editMaskNum.getDbPrecision() != null) {
            dbPrecision = Long.valueOf(editMaskNum.getDbPrecision().byteValue());
        } 
            setupInitialValues(editMaskNum);
        }
        updateEnableState();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void setupInitialValues(EditMaskNum editMaskNum) {
        if (editMaskNum.getPrecision() != null) {
            precisionCheckBox.setSelected(true);
            precisionSpinner.setValue(Long.valueOf(editMaskNum.getPrecision().byteValue()));
        } else {
            precisionCheckBox.setSelected(false);
        }

        if (editMaskNum.getMinValue() != null) {
            minValueCheckBox.setSelected(true);
//            minValueSpinner.setValue(Double.valueOf(editMaskNum.getMinValue().doubleValue()));
            minValueSpinner.setValue(editMaskNum.getMinValue());
        } else {
            minValueCheckBox.setSelected(false);
//            minValueSpinner.setValue(Double.valueOf(0));
        }
        if (editMaskNum.getMaxValue() != null) {
            maxValueCheckBox.setSelected(true);
//            maxValueSpinner.setValue(Double.valueOf(editMaskNum.getMaxValue().doubleValue()));
            maxValueSpinner.setValue(editMaskNum.getMaxValue());
        } else {
            maxValueCheckBox.setSelected(false);
//            maxValueSpinner.setValue(Double.valueOf(0));
        }
        scaleSpinner.setValue(Long.valueOf(editMaskNum.getScale()));
        if (editMaskNum.getTriadDelimiter() != null) {
            triadDelimeterTextField.setText(editMaskNum.getTriadDelimiter());
        }

        getDelimTypeModel().setSelectedItemSource(editMaskNum.getTriadDelimeterType());
        final String decimalDelimiter = editMaskNum.getDecimalDelimiter();
        if (decimalDelimiter != null) {
            chbDecimDelim.setSelected(true);
            txtDecimDelim.setText(decimalDelimiter);
        } else {
            chbDecimDelim.setSelected(false);
        }
    }

    private void updateEnableState() {
        precisionCheckBox.setEnabled(!readOnly);
        precisionSpinner.setEnabled(!readOnly && precisionCheckBox.isSelected());
        minValueCheckBox.setEnabled(!readOnly);
        minValueSpinner.setEnabled(!readOnly && minValueCheckBox.isSelected());
        maxValueCheckBox.setEnabled(!readOnly);
        maxValueSpinner.setEnabled(!readOnly && maxValueCheckBox.isSelected());
        scaleLabel.setEnabled(!readOnly);
        scaleSpinner.setEnabled(!readOnly);
        btResetToDb.setEnabled(!readOnly && dbPrecision != null && maxDbValue != null);

        chbDecimDelim.setEnabled(!readOnly);
        cmbDelimType.setEnabled(!readOnly);

        final ETriadDelimeterType delimeterType = getDelimTypeModel().getSelectedItemSource();
        final boolean specified = !readOnly && delimeterType == ETriadDelimeterType.SPECIFIED;
        triadDelimeterTextField.setEnabled(specified);
        lblTriadDelim.setEnabled(specified);

        txtDecimDelim.setEnabled(!readOnly && chbDecimDelim.isSelected());
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

        precisionCheckBox = new javax.swing.JCheckBox();
        precisionSpinner = new javax.swing.JSpinner();
        minValueCheckBox = new javax.swing.JCheckBox();
        minValueSpinner = new javax.swing.JSpinner();
        maxValueCheckBox = new javax.swing.JCheckBox();
        maxValueSpinner = new javax.swing.JSpinner();
        scaleLabel = new javax.swing.JLabel();
        scaleSpinner = new javax.swing.JSpinner();
        btResetToDb = new javax.swing.JButton();
        lblDelimType = new javax.swing.JLabel();
        cmbDelimType = new javax.swing.JComboBox<KernelEnumComboBoxModel.Item<ETriadDelimeterType>>();
        triadDelimeterTextField = new javax.swing.JFormattedTextField();
        lblTriadDelim = new javax.swing.JLabel();
        chbDecimDelim = new javax.swing.JCheckBox();
        txtDecimDelim = new javax.swing.JFormattedTextField();

        precisionCheckBox.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.precisionCheckBox.text")); // NOI18N
        precisionCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                precisionCheckBoxItemStateChanged(evt);
            }
        });

        precisionSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(127L), Long.valueOf(1L)));

        minValueCheckBox.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.minValueCheckBox.text")); // NOI18N
        minValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                minValueCheckBoxItemStateChanged(evt);
            }
        });

        maxValueCheckBox.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.maxValueCheckBox.text")); // NOI18N
        maxValueCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                maxValueCheckBoxItemStateChanged(evt);
            }
        });

        scaleLabel.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.scaleLabel.text")); // NOI18N

        scaleSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), null, null, Long.valueOf(1L)));

        btResetToDb.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.btResetToDb.text")); // NOI18N
        btResetToDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetToDb(evt);
            }
        });

        lblDelimType.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.lblDelimType.text")); // NOI18N

        cmbDelimType.setModel(new org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel(ETriadDelimeterType.class, ETriadDelimeterType.DEFAULT));
        cmbDelimType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDelimTypeItemStateChanged(evt);
            }
        });

        try {
            triadDelimeterTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblTriadDelim.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.lblTriadDelim.text")); // NOI18N

        chbDecimDelim.setText(org.openide.util.NbBundle.getMessage(NumEditor.class, "NumEditor.chbDecimDelim.text")); // NOI18N
        chbDecimDelim.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbDecimDelimItemStateChanged(evt);
            }
        });
        chbDecimDelim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbDecimDelimActionPerformed(evt);
            }
        });

        try {
            txtDecimDelim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(precisionCheckBox)
                            .addComponent(minValueCheckBox)
                            .addComponent(maxValueCheckBox)
                            .addComponent(chbDecimDelim))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minValueSpinner)
                            .addComponent(precisionSpinner)
                            .addComponent(maxValueSpinner)
                            .addComponent(scaleSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 54, Short.MAX_VALUE)
                                .addComponent(btResetToDb))
                            .addComponent(txtDecimDelim)
                            .addComponent(cmbDelimType, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDelimType)
                                    .addComponent(scaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTriadDelim, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(triadDelimeterTextField)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(precisionCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precisionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(minValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(maxValueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxValueSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scaleLabel)
                .addGap(5, 5, 5)
                .addComponent(scaleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chbDecimDelim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDecimDelim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDelimType, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDelimType, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTriadDelim, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(triadDelimeterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btResetToDb)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void resetToDb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetToDb
        if (this.maxDbValue != null) {
            maxValueSpinner.setValue(maxDbValue);
            maxValueCheckBox.setSelected(true);
        }
        if (this.dbPrecision != null) {
            precisionSpinner.setValue(dbPrecision);
            precisionCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_resetToDb

    private void chbDecimDelimItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbDecimDelimItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chbDecimDelimItemStateChanged

    private void maxValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_maxValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_maxValueCheckBoxItemStateChanged

    private void precisionCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_precisionCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_precisionCheckBoxItemStateChanged

    private void minValueCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_minValueCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_minValueCheckBoxItemStateChanged

    private void cmbDelimTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDelimTypeItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_cmbDelimTypeItemStateChanged

    private void chbDecimDelimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbDecimDelimActionPerformed
        updateEnableState();
    }//GEN-LAST:event_chbDecimDelimActionPerformed

    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskNum)) {
            return;
        }
        EditMaskNum editMaskNum = (EditMaskNum) editMask;

        if (precisionCheckBox.isSelected()) {
            editMaskNum.setPrecision(((Long) precisionSpinner.getValue()).byteValue());
        } else {
            editMaskNum.setPrecision(null);
        }

        if (minValueCheckBox.isSelected()) //            editMaskNum.setMinValue(BigDecimal.valueOf(((Double)minValueSpinner.getValue()).doubleValue()));
        {
            editMaskNum.setMinValue((BigDecimal) minValueSpinner.getValue());
        } else {
            editMaskNum.setMinValue(null);
        }
        if (maxValueCheckBox.isSelected()) {
            editMaskNum.setMaxValue((BigDecimal) maxValueSpinner.getValue());
        } else {
            editMaskNum.setMaxValue(null);
        }
        editMaskNum.setScale(((Long) scaleSpinner.getValue()).longValue());

        final ETriadDelimeterType delimeterType = getDelimTypeModel().getSelectedItemSource();
        editMaskNum.setTriadDelimeterType(delimeterType);
        if (delimeterType == ETriadDelimeterType.SPECIFIED) {
            final String delimeter = triadDelimeterTextField.getText();
            editMaskNum.setTriadDelimiter(delimeter.isEmpty() ? null : delimeter);
        }

        if (chbDecimDelim.isSelected()) {
            editMaskNum.setDecimalDelimiter(txtDecimDelim.getText());
        } else {
            editMaskNum.setDecimalDelimiter(null);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btResetToDb;
    private javax.swing.JCheckBox chbDecimDelim;
    private javax.swing.JComboBox<KernelEnumComboBoxModel.Item<ETriadDelimeterType>> cmbDelimType;
    private javax.swing.JLabel lblDelimType;
    private javax.swing.JLabel lblTriadDelim;
    private javax.swing.JCheckBox maxValueCheckBox;
    private javax.swing.JSpinner maxValueSpinner;
    private javax.swing.JCheckBox minValueCheckBox;
    private javax.swing.JSpinner minValueSpinner;
    private javax.swing.JCheckBox precisionCheckBox;
    private javax.swing.JSpinner precisionSpinner;
    private javax.swing.JLabel scaleLabel;
    private javax.swing.JSpinner scaleSpinner;
    private javax.swing.JFormattedTextField triadDelimeterTextField;
    private javax.swing.JFormattedTextField txtDecimDelim;
    // End of variables declaration//GEN-END:variables
}
