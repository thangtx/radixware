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
 * FixedLen.java
 *
 * Created on 5 Ноябрь 2008 г., 13:45
 */

package org.radixware.kernel.common.design.msdleditor.piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.radixware.kernel.common.design.msdleditor.AbstractMsdlPanel;
import org.radixware.kernel.common.design.msdleditor.enums.EAlign;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.IUnitValueStructure;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.schemas.msdl.FixedLenDef;


public class FixedLenPanel extends AbstractMsdlPanel implements ActionListener {
    private FixedLenDef fixedLenDef;
    private AbstractFieldModel fieldModel;
    private boolean opened = false;
    /** Creates new form FixedLen */

    public FixedLenPanel() {
        initComponents();
//        ArrayList<JLabel> l = new ArrayList<JLabel>();
//        l.add(jLabelLength);
//        l.add(jLabelUnit);
//        ArrayList<JComponent> e = new ArrayList<JComponent>();
//        e.add(jSpinnerLen);
//        e.add(selectUnitPanel);
//        //DefaultLayout.doLayout(jPanelHigh, l, e,true);
//
//        ArrayList<JLabel> ll = new ArrayList<JLabel>();
//        ll.add(jLabelAlign1);
//        ll.add(jLabelPad1);
//        ArrayList<JComponent> ee = new ArrayList<JComponent>();
//        ee.add(selectAlignPanel);
//        ee.add(selectPadPanel);
        //DefaultLayout.doLayout(jPanelLow, ll, ee,true);
    }

    public void open(AbstractFieldModel fieldModel, FixedLenDef fixedLenDef) {
        super.open(fieldModel, fieldModel.getMsdlField());
        this.fieldModel = fieldModel;
        this.fixedLenDef = fixedLenDef;
        selectPadPanel.open(new FixedLenStruct(fixedLenDef, fieldModel, unitPanel));
        
        update();
        selectAlignPanel.addActionListener(this);
        selectAlignPanel.getSetParentPanel().addActionListener(this);
        selectPadPanel.addActionListener(this);
        unitPanel.addActionListener(selectPadPanel);
        unitPanel.getSetParentPanel().addActionListener(selectPadPanel);
        trimToLenCheckBox.addActionListener(this);
        allowSmallerLenCheckBox.addActionListener(this);
        opened = true;
    }
    
    @Override
    public void update() {
        opened = false;
        
        jSpinnerLen.setValue(fixedLenDef.getLen());
        if(fixedLenDef.isSetTrimToLengthIfExceed()) {
            trimToLenCheckBox.setSelected(fixedLenDef.getTrimToLengthIfExceed());
        }
        if (fixedLenDef.isSetAllowSmallerLength()) {
            allowSmallerLenCheckBox.setSelected(fixedLenDef.getAllowSmallerLength());
        }
        selectAlignPanel.setAlign(EAlign.getInstance(fixedLenDef.getAlign()),
                             EAlign.getInstance(fieldModel.getAlign()));
        
        unitPanel.setElementAllowed(fieldModel);
        unitPanel.setUnit(EUnit.getInstance(fixedLenDef.getUnit()),
                            EUnit.getInstance(fieldModel.getUnit(true, 
                                    new MsdlUnitContext(MsdlUnitContext.EContext.FIXED_LEN))));

        boolean padEnabled = selectAlignPanel.getComboBoxAlign() != EAlign.NONE;
        selectPadPanel.setVisible(padEnabled);
        jLabelPad1.setVisible(padEnabled);
        if (padEnabled) {
            selectPadPanel.fillWidgets();
        }
        opened = true;
        super.update();
    }
    
    @Override
    protected void doSave() {
        if (!opened)
            return;
        fixedLenDef.setLen((Long)jSpinnerLen.getValue());
        fixedLenDef.setTrimToLengthIfExceed(trimToLenCheckBox.isSelected() ? true : null);
        fixedLenDef.setAllowSmallerLength(allowSmallerLenCheckBox.isSelected() ? true : null);
        fixedLenDef.setAlign(selectAlignPanel.getAlign().getValue());
        fixedLenDef.setUnit(unitPanel.getUnit().getValue());
        boolean padEnabled = selectAlignPanel.getComboBoxAlign() != EAlign.NONE;
        selectPadPanel.setVisible(padEnabled);
        jLabelPad1.setVisible(padEnabled);
        if (padEnabled) {
            selectPadPanel.fillDefinition();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentsPanel = new javax.swing.JPanel();
        jLabelLength = new javax.swing.JLabel();
        jSpinnerLen = new javax.swing.JSpinner();
        jLabelAlign1 = new javax.swing.JLabel();
        jLabelPad1 = new javax.swing.JLabel();
        trimToLenCheckBox = new javax.swing.JCheckBox();
        selectAlignPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        allowSmallerLenCheckBox = new javax.swing.JCheckBox();
        unitPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel();
        jLabelUnit = new javax.swing.JLabel();
        selectPadPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitValuePanel();

        setLayout(new java.awt.BorderLayout());

        jLabelLength.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabelLength.setText(bundle.getString("LENGTH")); // NOI18N

        jSpinnerLen.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(0L), null, Long.valueOf(1L)));
        jSpinnerLen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerLenStateChanged(evt);
            }
        });

        jLabelAlign1.setText(bundle.getString("ALIGN")); // NOI18N
        jLabelAlign1.setMaximumSize(new java.awt.Dimension(50, 15));
        jLabelAlign1.setMinimumSize(new java.awt.Dimension(50, 15));

        jLabelPad1.setText(bundle.getString("PAD_BIN")); // NOI18N
        jLabelPad1.setMaximumSize(new java.awt.Dimension(50, 15));
        jLabelPad1.setMinimumSize(new java.awt.Dimension(50, 15));

        trimToLenCheckBox.setText("Trim to length if exceeds");
        trimToLenCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        trimToLenCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        trimToLenCheckBox.setMargin(new java.awt.Insets(1, 0, 1, 1));

        allowSmallerLenCheckBox.setText("Allow smaller length");
        allowSmallerLenCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        allowSmallerLenCheckBox.setMargin(new java.awt.Insets(1, 0, 1, 1));
        allowSmallerLenCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowSmallerLenCheckBoxActionPerformed(evt);
            }
        });

        jLabelUnit.setText("Unit:");

        javax.swing.GroupLayout contentsPanelLayout = new javax.swing.GroupLayout(contentsPanel);
        contentsPanel.setLayout(contentsPanelLayout);
        contentsPanelLayout.setHorizontalGroup(
            contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentsPanelLayout.createSequentialGroup()
                        .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelAlign1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPad1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelLength, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectAlignPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(contentsPanelLayout.createSequentialGroup()
                                .addComponent(jSpinnerLen)
                                .addGap(20, 20, 20))
                            .addComponent(unitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectPadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(contentsPanelLayout.createSequentialGroup()
                        .addComponent(trimToLenCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(allowSmallerLenCheckBox)
                        .addContainerGap(29, Short.MAX_VALUE))))
        );
        contentsPanelLayout.setVerticalGroup(
            contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerLen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLength))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(unitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jLabelUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectAlignPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAlign1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectPadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPad1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trimToLenCheckBox)
                    .addComponent(allowSmallerLenCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(contentsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void allowSmallerLenCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowSmallerLenCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_allowSmallerLenCheckBoxActionPerformed

    private void jSpinnerLenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerLenStateChanged
        if (!opened)
            return;
        save();
    }//GEN-LAST:event_jSpinnerLenStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allowSmallerLenCheckBox;
    private javax.swing.JPanel contentsPanel;
    private javax.swing.JLabel jLabelAlign1;
    private javax.swing.JLabel jLabelLength;
    private javax.swing.JLabel jLabelPad1;
    private javax.swing.JLabel jLabelUnit;
    private javax.swing.JSpinner jSpinnerLen;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel selectAlignPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitValuePanel selectPadPanel;
    private javax.swing.JCheckBox trimToLenCheckBox;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel unitPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!opened)
            return;
        save();
    }
    
    private class FixedLenStruct implements IUnitValueStructure {
        
        private final FixedLenDef xDef;
        private final AbstractFieldModel model;
        private final UnitPanel unitPanel;
        
        public FixedLenStruct(FixedLenDef xDef, AbstractFieldModel model, UnitPanel unitPanel) {
            this.xDef = xDef;
            this.model = model;
            this.unitPanel = unitPanel;
        }

        @Override
        public String getPadChar() {
            return xDef.getPadChar();
        }

        @Override
        public String getExtPadChar() {
            return model.getPadChar();
        }

        @Override
        public void setPadChar(String chars) {
            xDef.setPadChar(chars);
        }

        @Override
        public byte[] getPadBin() {
            return xDef.getPadBin();
        }

        @Override
        public byte[] getExtPadBin() {
            return model.getPadBin();
        }

        @Override
        public void setPadBin(byte[] bytes) {
            xDef.setPadBin(bytes);
        }

        @Override
        public EUnit getViewedUnit() {
            return unitPanel.getViewedUnit();
        }
        
    }
}
