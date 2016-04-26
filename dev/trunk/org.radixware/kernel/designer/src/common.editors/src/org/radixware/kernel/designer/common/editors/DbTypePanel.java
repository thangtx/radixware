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
 * DbTypePanel.java
 *
 * Created on Feb 7, 2011, 3:04:38 PM
 */
package org.radixware.kernel.designer.common.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDbType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.enums.EValType;


public class DbTypePanel extends javax.swing.JPanel {

    private IDbType dbType;
    //for dds templates editing
    private boolean ddsDefinitionEditor = false;
    private DdsColumnTemplateDef templateDef;

    /** Creates new form DbTypePanel */
    public DbTypePanel() {
        initComponents();

        lengthBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DbTypePanel.this.lengthIsUnlimitedCheckBoxChange();
            }
        });

        lengthSpinBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                DbTypePanel.this.lengthSpinBoxChanged();
            }
        });

        precisionSpinBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                DbTypePanel.this.precisionSpinBoxChanged();
            }
        });
    }

    private void lengthSpinBoxChanged() {
        dbType.setLength((Integer) lengthSpinBox.getValue());
        String autodbType = dbType.calcAutoDbType();
        if (dbType instanceof DdsColumnTemplateDef) {
            ((DdsColumnTemplateDef) dbType).setDbType(autodbType);
        }
        dbTypeField.setText(autodbType);
    }

    private void precisionSpinBoxChanged() {
        dbType.setPrecision((Integer) precisionSpinBox.getValue());
        String autodbType = dbType.calcAutoDbType();
        if (dbType instanceof DdsColumnTemplateDef) {
            ((DdsColumnTemplateDef) dbType).setDbType(autodbType);
        }
        dbTypeField.setText(autodbType);
    }

    private void lengthIsUnlimitedCheckBoxChange() {
        final boolean isSelected = lengthBox.isSelected();
        if (!isSelected) {
            dbType.setLength(0);
            lengthSpinBox.setEnabled(false);
        } else {
            EValType valType = dbType.getValType();
            if (valType == EValType.INT || valType == EValType.NUM) {
                dbType.setLength(18);
            } else if (valType == EValType.DATE_TIME) {
                dbType.setLength(6);
            } else if (valType == EValType.ARR_BIN
                    || valType == EValType.ARR_BLOB
                    || valType == EValType.ARR_BOOL
                    || valType == EValType.ARR_CHAR
                    || valType == EValType.ARR_CLOB
                    || valType == EValType.ARR_DATE_TIME
                    || valType == EValType.ARR_INT
                    || valType == EValType.ARR_NUM
                    || valType == EValType.ARR_REF
                    || valType == EValType.ARR_STR) {
                dbType.setLength(4000);
            } else {
                dbType.setLength(0);
            }
            lengthSpinBox.setEnabled(true);
        }
        lengthSpinBox.setValue(dbType.getLength());
    }

    public void open(IDbType dbType) {
        this.dbType = dbType;

        this.ddsDefinitionEditor = dbType instanceof DdsColumnTemplateDef;
        if (ddsDefinitionEditor) {
            boolean isTemplate = !(dbType instanceof DdsColumnDef);
            if (!isTemplate) {
                templateDef = ((DdsColumnDef) dbType).findTemplate();
            } else {
                templateDef = null;
            }
        } else {
            templateDef = null;
        }

        update();
    }

    private void updateEnabledState() {
        boolean ddsEnabled = ddsDefinitionEditor ? templateDef == null : true;
        EValType valType = dbType.getValType();

        boolean isPrecisionEnabled = !isReadonly() && ddsEnabled && (valType == EValType.NUM || valType == EValType.DATE_TIME);
        precisionLabel.setEnabled(isPrecisionEnabled);
        precisionSpinBox.setEnabled(isPrecisionEnabled);

        lengthBox.setEnabled(!isReadonly() && ddsEnabled && (valType != EValType.CHAR
                && valType != EValType.BOOL
                && valType != EValType.BLOB
                && valType != EValType.CLOB
                && valType != EValType.BIN
                && valType != EValType.STR
                && valType != EValType.INT
                && valType != EValType.NATIVE_DB_TYPE
                && valType != EValType.DATE_TIME));
        boolean isLengthEnabled = !isReadonly() && ddsEnabled
                && (valType != EValType.CHAR
                && valType != EValType.BOOL
                && valType != EValType.BLOB
                && valType != EValType.CLOB
                && valType != EValType.NATIVE_DB_TYPE)
                && valType != EValType.DATE_TIME;
        lengthSpinBox.setEnabled(isLengthEnabled && lengthBox.isSelected());
        lengthLabel.setEnabled(lengthBox.isEnabled() || lengthSpinBox.isEnabled());

    }

    public void update() {
        if (dbType != null) {
            dbTypeField.setText(dbType.getDbType());
            lengthSpinBox.setValue(dbType.getLength());
            lengthBox.setSelected(dbType.getLength() != 0);
            precisionSpinBox.setValue(dbType.getPrecision());

            updateEnabledState();
        } else {
            lengthBox.setEnabled(false);
            lengthSpinBox.setEnabled(false);
            precisionSpinBox.setEnabled(false);
            lengthBox.setSelected(false);
            lengthSpinBox.setValue(0);
            precisionSpinBox.setValue(0);
            dbTypeField.setText("");
        }
    }

    public void valTypeChanged() {
        if (dbType != null) {
            EValType valType = dbType.getValType();
            dbType.setPrecision(0);
            if (valType == EValType.BOOL || valType == EValType.CHAR) {
                dbType.setLength(1);
            } else if (valType == EValType.STR) {
                dbType.setLength(100);
            } else if (valType == EValType.BIN) {
                dbType.setLength(2000);
            } else if (valType == EValType.INT) {
                dbType.setLength(18); 
            } else {
                dbType.setLength(0);
            }
        }
        update();
    }
    private boolean readonly = false;

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        updateEnabledState();
    }

    private boolean isReadonly() {
        return this.readonly || (dbType != null && dbType instanceof Definition && ((Definition) dbType).isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lengthBox = new javax.swing.JCheckBox();
        lengthLabel = new javax.swing.JLabel();
        lengthSpinBox = new javax.swing.JSpinner();
        precisionLabel = new javax.swing.JLabel();
        precisionSpinBox = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        dbTypeField = new javax.swing.JTextField();

        lengthLabel.setText(org.openide.util.NbBundle.getMessage(DbTypePanel.class, "DbTypePanel-LengthLabel")); // NOI18N

        lengthSpinBox.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        precisionLabel.setText(org.openide.util.NbBundle.getMessage(DbTypePanel.class, "DbTypePanel-PrecisionLabel")); // NOI18N

        precisionSpinBox.setModel(new javax.swing.SpinnerNumberModel(0, -84, 127, 1));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DbTypePanel.class, "DbTypePanel-DbTypeLabel")); // NOI18N

        dbTypeField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(lengthBox)
                .addGap(0, 0, 0)
                .addComponent(lengthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lengthSpinBox, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precisionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precisionSpinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbTypeField, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lengthSpinBox, precisionSpinBox});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lengthBox)
                    .addComponent(lengthLabel)
                    .addComponent(lengthSpinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(precisionLabel)
                    .addComponent(precisionSpinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dbTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dbTypeField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox lengthBox;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JSpinner lengthSpinBox;
    private javax.swing.JLabel precisionLabel;
    private javax.swing.JSpinner precisionSpinBox;
    // End of variables declaration//GEN-END:variables
}
