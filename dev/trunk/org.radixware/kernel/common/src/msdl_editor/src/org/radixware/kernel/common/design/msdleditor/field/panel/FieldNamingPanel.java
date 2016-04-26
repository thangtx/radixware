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
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.design.msdleditor.enums.EPieceType;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.schemas.msdl.*;


public class FieldNamingPanel extends AbstractEditItem implements ActionListener {

    private StructureFieldModel field;
    private IntField defaultLenField = null;

    /**
     * Creates new form FieldNamingPanel
     */
    public FieldNamingPanel() {
        initComponents();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EPieceType.FIXED_LEN);
        model.addElement(EPieceType.SEPARATED);
        model.addElement(EPieceType.EMBEDDED_LEN);
        piecePanel1.setModel(model);
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(separatorLabel);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(fieldSeparatorPanel);

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

                        lengthEncodingPane.setEncoding(EEncoding.getInstance(dfl.getEncoding()), EEncoding.NONE);
                    } else {
                        dfl = IntField.Factory.newInstance();
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
                        field.getStructure().setDefaultLengthFormat(dfl);
                        field.setModified();
                    }
                    defaultLenField = dfl;

                } else {
                    boolean was = field.getStructure().getDefaultLengthFormat() != null;
                    setDefaultLenFormatEnabled(false);
                    
                    field.getStructure().unsetDefaultLengthFormat();
                    if (was)
                        field.setModified();
                }
                
            }
        });

    }

    public void open(StructureFieldModel field) {
        super.open(field.getMsdlField());
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

        setupDefaultLength();

        update();
        
        fieldSeparatorPanel.addActionListener(this);
        lengthEncodingPane.addActionListener(this);
    }

    @Override
    public void update() {
        if (fieldSeparatorPanel.getViewEncoding() == EEncoding.HEX)
            fieldSeparatorPanel.setLimit(2);
        else {
            fieldSeparatorPanel.setLimit(1);
        }
        fieldSeparatorPanel.setValue(field.getStructure().getFieldSeparator(), 
                EEncoding.getInstanceForHexViewType(field.getStructure().getFieldSeparatorViewType()));
        piecePanel1.update();

        if (defaultLengthFormatCheckBox.isSelected()) {
            lengthEncodingPane.update();
            lengthPiecePanel.update();
        }
        
        if(defaultLenField != null) {
            field.getStructure().setDefaultLengthFormat(defaultLenField);
        }

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
        jPanel2 = new javax.swing.JPanel();
        separatorLabel = new javax.swing.JLabel();
        fieldSeparatorPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel();
        defaultLengthFormatCheckBox = new javax.swing.JCheckBox();
        piecePanel1 = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();
        lengthEncodingPane = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        lengthPiecePanel = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();
        alignLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(500, 700));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(856, 100));

        defaultLengthPanel.setLayout(new java.awt.GridBagLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(269, 38));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        separatorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        separatorLabel.setText("Field Separator");
        jPanel2.add(separatorLabel);
        jPanel2.add(fieldSeparatorPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        defaultLengthPanel.add(jPanel2, gridBagConstraints);

        defaultLengthFormatCheckBox.setText("Default length format");
        defaultLengthFormatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultLengthFormatCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        defaultLengthPanel.add(defaultLengthFormatCheckBox, gridBagConstraints);

        piecePanel1.setMinimumSize(new java.awt.Dimension(100, 130));
        piecePanel1.setPreferredSize(new java.awt.Dimension(400, 130));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        defaultLengthPanel.add(piecePanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        defaultLengthPanel.add(lengthEncodingPane, gridBagConstraints);

        lengthPiecePanel.setMinimumSize(new java.awt.Dimension(100, 130));
        lengthPiecePanel.setPreferredSize(new java.awt.Dimension(400, 130));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        defaultLengthPanel.add(lengthPiecePanel, gridBagConstraints);

        alignLabel.setText("Encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        defaultLengthPanel.add(alignLabel, gridBagConstraints);

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
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel fieldSeparatorPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel lengthEncodingPane;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel lengthPiecePanel;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel piecePanel1;
    private javax.swing.JLabel separatorLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {      
        field.getStructure().setFieldSeparator(fieldSeparatorPanel.getValue());
        field.getStructure().setFieldSeparatorViewType(fieldSeparatorPanel.getViewEncoding().getValue());
        if(defaultLenField != null) {
            defaultLenField.setEncoding(lengthEncodingPane.getEncoding().getName());
            field.getStructure().setDefaultLengthFormat(defaultLenField);
        }
        
        field.setModified();
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
}
