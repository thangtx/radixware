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
 * FieldNamingPanel.java
 *
 * Created on 20.03.2009, 11:48:06
 */
package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.IUnitValueStructure;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;

public class SequentallyOrderPanel extends AbstractEditItem implements ActionListener {

    private StructureFieldModel field;
    private boolean isOpened = false;

    public SequentallyOrderPanel() {
        initComponents();
        ArrayList<JLabel> l = new ArrayList<>();
        l.add(lbFieldSeparator);
        l.add(lbFieldSeparatorUnit);
        ArrayList<JComponent> e = new ArrayList<>();
        e.add(fieldSeparatorHexPanel);
        e.add(fieldSeparatorUnitPanel);
        DefaultLayout.doLayout(this, l, e, true);
    }

    public void open(StructureFieldModel field) {
        super.open(field.getMsdlField());
        this.field = field;
        fieldSeparatorHexPanel.open(new SequentallyOrderStruct(field, fieldSeparatorUnitPanel));
        update();
        
        fieldSeparatorHexPanel.addActionListener(this);
        fieldSeparatorUnitPanel.addActionListener(fieldSeparatorHexPanel);
        fieldSeparatorUnitPanel.getSetParentPanel().addActionListener(fieldSeparatorHexPanel);
        isOpened = true;
    }

    @Override
    public void update() {
        if (fieldSeparatorHexPanel.getViewEncoding() == EEncoding.HEX) {
            fieldSeparatorHexPanel.setLimit(2);
        } else {
            fieldSeparatorHexPanel.setLimit(1);
        }

        final EUnit sepUnit = field.getStructure().getFieldSeparatorUnit() != null
                ? EUnit.getInstance(field.getStructure().getFieldSeparatorUnit())
                : EUnit.BYTE;
        fieldSeparatorUnitPanel.setUnit(sepUnit, EUnit.NOTDEFINED);
        fieldSeparatorHexPanel.fillWidgets();

        super.update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbFieldSeparator = new javax.swing.JLabel();
        lbFieldSeparatorUnit = new javax.swing.JLabel();
        final EnumSet<EUnit> selectableUnits = EnumSet.of(EUnit.CHAR, EUnit.BYTE);
        fieldSeparatorUnitPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel(false, selectableUnits);
        fieldSeparatorHexPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitValuePanel();

        lbFieldSeparator.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbFieldSeparator.setText("Field Separator");

        lbFieldSeparatorUnit.setText("Field Separator Unit");

        fieldSeparatorUnitPanel.addActionListener(this);
        fieldSeparatorUnitPanel.getSetParentPanel().addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFieldSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldSeparatorHexPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFieldSeparatorUnit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldSeparatorUnitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbFieldSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSeparatorHexPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbFieldSeparatorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSeparatorUnitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitValuePanel fieldSeparatorHexPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel fieldSeparatorUnitPanel;
    private javax.swing.JLabel lbFieldSeparator;
    private javax.swing.JLabel lbFieldSeparatorUnit;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isOpened) {
            return;
        }
        field.getStructure().setFieldSeparatorUnit(fieldSeparatorUnitPanel.getUnit().getValue());
        fieldSeparatorHexPanel.fillDefinition();
        field.setModified();
    }

    private class SequentallyOrderStruct implements IUnitValueStructure {

        private final StructureFieldModel field;
        private final UnitPanel unitPanel;

        public SequentallyOrderStruct(StructureFieldModel field, UnitPanel unitPanel) {
            this.field = field;
            this.unitPanel = unitPanel;
        }

        @Override
        public String getPadChar() {
            return field.getStructure().getFieldSeparatorChar();
        }

        @Override
        public String getExtPadChar() {
            return null;
        }

        @Override
        public void setPadChar(String chars) {
            field.getStructure().setFieldSeparatorChar(chars);
        }

        @Override
        public byte[] getPadBin() {
            return field.getStructure().getFieldSeparator();
        }

        @Override
        public byte[] getExtPadBin() {
            return null;
        }

        @Override
        public void setPadBin(byte[] bytes) {
            field.getStructure().setFieldSeparator(bytes);
        }

        @Override
        public EUnit getViewedUnit() {
            return unitPanel.getViewedUnit();
        }
    }

}
