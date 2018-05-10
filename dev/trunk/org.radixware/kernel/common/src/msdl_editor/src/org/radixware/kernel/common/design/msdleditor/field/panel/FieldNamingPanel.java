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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.design.msdleditor.AbstractMsdlPanel;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.design.msdleditor.enums.EPieceType;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.schemas.msdl.*;


public class FieldNamingPanel extends AbstractMsdlPanel implements ActionListener, AbstractMsdlPanel.IEncodingField {

    private StructureFieldModel field;
    private IntField defaultLenField = null;
    private IntField defaultLenFieldCopy = null;
    private final transient MsdlField.MsdlFieldStructureChangedListener changeListener;

    /**
     * Creates new form FieldNamingPanel
     */
    public FieldNamingPanel() {
        initComponents();
        changeListener = new MsdlStructChangedDefaultListener();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EPieceType.FIXED_LEN);
        model.addElement(EPieceType.SEPARATED);
        model.addElement(EPieceType.EMBEDDED_LEN);
        piecePanel1.setModel(model);

        DefaultComboBoxModel dlModel = new DefaultComboBoxModel();
        dlModel.addElement(EPieceType.FIXED_LEN);
        dlModel.addElement(EPieceType.SEPARATED);
        dlModel.addElement(EPieceType.EMBEDDED_LEN);
        lengthPiecePanel.setModel(dlModel);


        piecePanel1.setTitle("Field Name Format");
        lengthPiecePanel.setTitle("Default length format");

        setDefaultLenFormatEnabled(false);
        

        defaultLengthFormatCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setDefaultLenFormatEnabled(true);
                    
                    IntField dfl = field.getStructure().getDefaultLengthFormat();
                    if (dfl != null) {
                        IntFieldModel lenModel = new IntFieldModel(field.getMsdlField(), dfl);

                        lengthPiecePanel.open(lenModel, dfl);

                        lengthEncodingPane.setEncoding(EEncoding.getInstance(dfl.getEncoding()), EEncoding.getInstance(field.getEncoding(true)));
                        defaultLenFieldCopy = lenModel.getField();
                    } else {
                        dfl = field.getStructure().addNewDefaultLengthFormat();
                        dfl.setName("DefaultLengthFieldFormatThatNooneMustEverTouch");

                        FixedLenDef fld = FixedLenDef.Factory.newInstance();
                        fld.setAlign("Right");
                        fld.setLen(Long.valueOf(3));

                        byte[] b = new byte[1];
                        b[0] = 0x30;
                        fld.setPadBin(b);
                        fld.setPadChar(" ");
                        fld.setUnit("Char");

                        dfl.setFixedLen(fld);
                        dfl.setEncoding("Ascii");

                        IntFieldModel ifm = new IntFieldModel(field.getMsdlField(), dfl);


                        lengthPiecePanel.open(ifm, dfl);
                        lengthEncodingPane.open(ifm.getMsdlField());
                        lengthEncodingPane.setEncoding(EEncoding.getInstance(dfl.getEncoding()), EEncoding.getInstance(field.getEncoding(true)));
                        field.setModified();
                        defaultLenFieldCopy = ifm.getField();
                    }
                    defaultLenField = dfl;
                } else {
                    boolean was = field.getStructure().getDefaultLengthFormat() != null;
                    setDefaultLenFormatEnabled(false);
                    
                    field.getStructure().unsetDefaultLengthFormat();     
                    defaultLenField = null;
                    if (was)
                        field.setModified();
                }
                
            }
        });

    }

    public void open(StructureFieldModel field) {
        super.open(field, field.getMsdlField());
        this.field = field;
        
        Piece piece = field.getStructure().getFieldNaming().getPiece();
        if (!piece.isSetFixedLen() && !piece.isSetSeparated() && !piece.isSetEmbeddedLen()) {
            piece.addNewFixedLen().setLen(Long.valueOf(1));
        }
        piecePanel1.open(field, piece);
        
        lengthEncodingPane.setEnabled(false);
        lengthEncodingPane.setControlEnabled(false);
        
        lengthPiecePanel.setEnabled(false);
        lengthPiecePanel.setControlEnabled(false);

        if (field.getStructure().getDefaultLengthFormat() != null) {
            defaultLengthFormatCheckBox.setSelected(true);
        }

        update();
        
        field.getMsdlField().getStructureChangedSupport().addEventListener(changeListener);
        lengthEncodingPane.addActionListener(this);
        lengthEncodingPane.getSetParentPanel().addActionListener(this);
        tagUnitPanel.addActionListener(this);
        tagUnitPanel.getSetParentPanel().addActionListener(this);
    }

    @Override
    public void update() {
        if (separatorPanel.getViewEncoding() == EEncoding.HEX)
            separatorPanel.setLimit(2);
        else {
            separatorPanel.setLimit(1);
        }
        separatorPanel.setValue(field.getStructure().getFieldSeparatorStart(), field.getStructure().getFieldSeparator());
        piecePanel1.update();

        if (defaultLengthFormatCheckBox.isSelected()) {
            lengthEncodingPane.update();
            lengthPiecePanel.update();
        }
        
        if(defaultLenField != null) {
            field.getStructure().setDefaultLengthFormat(defaultLenField);
        }
        
        final EUnit tagUnit = field.getStructure().getFieldNaming().getExtIdUnit() != null
                ? EUnit.getInstance(field.getStructure().getFieldNaming().getExtIdUnit())
                : EUnit.BYTE;
        tagUnitPanel.setUnit(tagUnit, EUnit.NOTDEFINED);

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
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        defaultLengthPanel = new javax.swing.JPanel();
        defaultLengthFormatCheckBox = new javax.swing.JCheckBox();
        piecePanel1 = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();
        lengthEncodingPane = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        lengthPiecePanel = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();
        alignLabel = new javax.swing.JLabel();
        separatorPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.SeparatorPanel();
        tagUnitPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel();
        jLabel1 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(500, 700));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(856, 100));

        defaultLengthPanel.setLayout(new java.awt.GridBagLayout());

        defaultLengthFormatCheckBox.setText("Default length format");
        defaultLengthFormatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultLengthFormatCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        defaultLengthPanel.add(defaultLengthFormatCheckBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        defaultLengthPanel.add(piecePanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        defaultLengthPanel.add(lengthEncodingPane, gridBagConstraints);

        lengthPiecePanel.setMinimumSize(new java.awt.Dimension(100, 130));
        lengthPiecePanel.setPreferredSize(new java.awt.Dimension(400, 130));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        defaultLengthPanel.add(lengthPiecePanel, gridBagConstraints);

        alignLabel.setText("Encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        defaultLengthPanel.add(alignLabel, gridBagConstraints);

        separatorPanel.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        defaultLengthPanel.add(separatorPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        defaultLengthPanel.add(tagUnitPanel, gridBagConstraints);

        jLabel1.setText("Tag Unit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        defaultLengthPanel.add(jLabel1, gridBagConstraints);

        jScrollPane1.setViewportView(defaultLengthPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 745;
        gridBagConstraints.ipady = 521;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void defaultLengthFormatCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultLengthFormatCheckBoxActionPerformed
        // do nothing
    }//GEN-LAST:event_defaultLengthFormatCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alignLabel;
    private javax.swing.JCheckBox defaultLengthFormatCheckBox;
    private javax.swing.JPanel defaultLengthPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel lengthEncodingPane;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel lengthPiecePanel;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel piecePanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.SeparatorPanel separatorPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel tagUnitPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public EEncoding getEncoding() {
        if (defaultLenField != null && defaultLengthFormatCheckBox.isSelected()) {
            return lengthEncodingPane.getEncoding();
        }
        return null;
    }

    @Override
    protected void doSave() {
        field.getStructure().setFieldSeparatorStart(separatorPanel.getStartSeparator());
        field.getStructure().setFieldSeparator(separatorPanel.getEndSeparator());

        if(defaultLenField != null) {
            defaultLenField.setEncoding(lengthEncodingPane.getEncoding().getValue());
            defaultLenFieldCopy.setEncoding(lengthEncodingPane.getEncoding().getValue());
            field.getStructure().setDefaultLengthFormat(defaultLenField);
            
            if (defaultLengthFormatCheckBox.isSelected()) {
                lengthPiecePanel.update(true);
            }
        }
        field.getStructure().getFieldNaming().setExtIdUnit(tagUnitPanel.getUnit().getValue());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {      
        save();
    }

    private void setupDefaultLength() {
        if (field.getStructure().getDefaultLengthFormat() != null) {
            defaultLengthFormatCheckBox.setSelected(true);
            IntField dfl = field.getStructure().getDefaultLengthFormat();
            EEncoding enc = readDefaultLenEncoding(dfl);
            lengthEncodingPane.setEncoding(enc, EEncoding.NONE);
        }
    }

    private void setDefaultLenFormatEnabled(boolean status) {
        lengthEncodingPane.setEnabled(status);
        lengthEncodingPane.setControlEnabled(status);
        
        lengthPiecePanel.setEnabled(status);
        lengthPiecePanel.setControlEnabled(status);
    }

    private EEncoding readDefaultLenEncoding(IntField dlf) {
        String encName = dlf.getEncoding();
        EEncoding ret = EEncoding.getInstance(encName);
        return ret;
    }
    
    private class MsdlStructChangedDefaultListener implements MsdlField.MsdlFieldStructureChangedListener {

        @Override
        public void onEvent(MsdlField.MsdlFieldStructureChangedEvent e) {
            if (!piecePanel1.isValid()) {
                field.getMsdlField().getStructureChangedSupport().removeEventListener(this);
            } else {
                piecePanel1.update(true);
            }
        }
    }
}
