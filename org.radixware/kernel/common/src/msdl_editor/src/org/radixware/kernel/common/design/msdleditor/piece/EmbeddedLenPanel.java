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
 * EmbeddedLenPanel.java
 *
 * Created on 18.03.2009, 16:02:53
 */

package org.radixware.kernel.common.design.msdleditor.piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.design.msdleditor.enums.EPieceType;
import org.radixware.kernel.common.design.msdleditor.enums.ESelfInclusive;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.schemas.msdl.EmbeddedLenDef;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.design.msdleditor.enums.EAlign;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;



public class EmbeddedLenPanel extends javax.swing.JPanel implements ActionListener, ChangeListener, PadFiller.ICoderProvider {

    private EmbeddedLenDef embeddedLenDef;
    private AbstractFieldModel field;
    private SmioCoder smioCoder;
    private PadFiller pf = null;
    private boolean inited = false;

    /** Creates new form EmbeddedLenPanel */
    public EmbeddedLenPanel() {
        initComponents();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EPieceType.FIXED_LEN);
        model.addElement(EPieceType.SEPARATED);
        piecePanel.setModel(model);
        DefaultComboBoxModel smodel = new DefaultComboBoxModel();
        smodel.addElement(EEncoding.NONE);
        smodel.addElement(EEncoding.HEX);
        smodel.addElement(EEncoding.BCD);
        smodel.addElement(EEncoding.ASCII);
        smodel.addElement(EEncoding.CP1251);
        smodel.addElement(EEncoding.CP1252);
        smodel.addElement(EEncoding.EBCDIC);
        smodel.addElement(EEncoding.CP866);
        smodel.addElement(EEncoding.UTF8);
        smodel.addElement(EEncoding.BIGENDIANBIN);
        smodel.addElement(EEncoding.LITTLEENDIANBIN);
        encodingPanel1.setEncodingModel(smodel);

        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabelSelf);
        l.add(jLabelUnit);
        l.add(jLabelEncoding);
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(selfInclusivePanel1);
        e.add(unitPanel1);
        e.add(encodingPanel1);
        //DefaultLayout.doLayout(jPanel1, l, e,true);
        setBoundsEnabled(false);
        boundedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == ActionEvent.ACTION_PERFORMED) {
                    boolean selected = boundedCheckBox.isSelected();
                    setBoundsEnabled(selected);
                    if (selected && !inited) {
                        inited = true;
                    }
                    save();
                }
            }
        });
    }

    public void open(AbstractFieldModel field, EmbeddedLenDef embeddedLenDef) {
        this.field = field;
        this.embeddedLenDef = embeddedLenDef;
        piecePanel.open(field, embeddedLenDef);
        update();
        addActionListeners();
    }

    public void update() {
        unitPanel1.setUnit(EUnit.getInstance(embeddedLenDef.getUnit()),
                EUnit.getInstance(field.getUnit(true,
                                new MsdlUnitContext(MsdlUnitContext.EContext.EMBEDDED_LEN))));
        selfInclusivePanel1.setSelfInclusive(ESelfInclusive.getInstance(embeddedLenDef.getIsSelfInclusive()),
                                        ESelfInclusive.getInstance(field.getSelfInclusive(true)));
        encodingPanel1.setEncoding(EEncoding.getInstance(embeddedLenDef.getEncoding()),
                                    EEncoding.getInstance(field.getIntEncoding(true)));
        
        if (embeddedLenDef.isSetBounds()) {
            readBounds();
        } else {
            clearBounds();
        }
        
        piecePanel.update();
    }

    public void save() {
        embeddedLenDef.setUnit(unitPanel1.getUnit().getValue());
        embeddedLenDef.setIsSelfInclusive(selfInclusivePanel1.getSelfInclusive().getValue());
        embeddedLenDef.setEncoding(encodingPanel1.getEncoding().getValue());
        if (field instanceof IntFieldModel) {
            ((IntFieldModel) field).getField().setEncoding(encodingPanel1.getEncoding().getValue());
        }

        if (boundedCheckBox.isSelected()) {
            if (getSpinnerValue(lowBoundSpinner) < getSpinnerValue(highBoundSpinner)) {
                EmbeddedLenDef.Bounds real = null;
                if (!embeddedLenDef.isSetBounds()) {
                    real = embeddedLenDef.addNewBounds();
                } else {
                    real = embeddedLenDef.getBounds();
                }
                real.setHighBound(getSpinnerValue(highBoundSpinner));
                real.setLowBound(getSpinnerValue(lowBoundSpinner));
                embeddedLenDef.setBounds(real);

                embeddedLenDef.setAlign(alignPanel.getAlign().getValue());

                //clear old padding and install new one
                if (embeddedLenDef.isSetPadBin()) {
                    embeddedLenDef.setPadBin(null);
                }
                if (embeddedLenDef.isSetPadChar()) {
                    embeddedLenDef.setPadChar(null);
                }

                initPf();
                pf.fillDefinition();
            } else {
                JOptionPane.showMessageDialog(null, "High bound must be greater than low bound.");
            }
        } else {
            if (embeddedLenDef.isSetBounds()) {
                embeddedLenDef.setBounds(null);
            }
            if (embeddedLenDef.isSetPadBin()) {
                embeddedLenDef.setPadBin(null);
            }
            if (embeddedLenDef.isSetPadChar()) {
                embeddedLenDef.setPadChar(null);
            }
        }
        field.setModified();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        piecePanel = new org.radixware.kernel.common.design.msdleditor.piece.PiecePanel();
        middlePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelUnit = new javax.swing.JLabel();
        jLabelSelf = new javax.swing.JLabel();
        unitPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel();
        selfInclusivePanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SelfInclusivePanel();
        encodingPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        jLabelEncoding = new javax.swing.JLabel();
        boundedCheckBox = new javax.swing.JCheckBox();
        jLabelLowBound = new javax.swing.JLabel();
        jLabelHighBound = new javax.swing.JLabel();
        lowBoundSpinner = new javax.swing.JSpinner();
        highBoundSpinner = new javax.swing.JSpinner();
        boundedPadPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        jLabelPad = new javax.swing.JLabel();
        alignPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel(new EAlign[]{EAlign.LEFT,EAlign.RIGHT});
        jLabelAlign = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("LENGTH"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 500));
        setLayout(new java.awt.BorderLayout());
        add(piecePanel, java.awt.BorderLayout.NORTH);

        middlePanel.setMinimumSize(new java.awt.Dimension(100, 80));
        middlePanel.setPreferredSize(new java.awt.Dimension(800, 90));
        middlePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        middlePanel.add(jPanel1, gridBagConstraints);

        jLabelUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabelUnit.setText(bundle1.getString("UNIT")); // NOI18N
        jLabelUnit.setMaximumSize(new java.awt.Dimension(60, 15));
        jLabelUnit.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        middlePanel.add(jLabelUnit, gridBagConstraints);

        jLabelSelf.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelSelf.setText(bundle1.getString("SELF_INCLUSIVE")); // NOI18N
        jLabelSelf.setMaximumSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        middlePanel.add(jLabelSelf, gridBagConstraints);

        unitPanel1.setPreferredSize(new java.awt.Dimension(300, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        middlePanel.add(unitPanel1, gridBagConstraints);

        selfInclusivePanel1.setPreferredSize(new java.awt.Dimension(300, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        middlePanel.add(selfInclusivePanel1, gridBagConstraints);

        encodingPanel1.setPreferredSize(new java.awt.Dimension(300, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        middlePanel.add(encodingPanel1, gridBagConstraints);

        jLabelEncoding.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        java.util.ResourceBundle bundle2 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/simple/Bundle"); // NOI18N
        jLabelEncoding.setText(bundle2.getString("SimpleFieldPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        middlePanel.add(jLabelEncoding, gridBagConstraints);

        boundedCheckBox.setText("Bounded");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        middlePanel.add(boundedCheckBox, gridBagConstraints);

        jLabelLowBound.setText("Low bound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        middlePanel.add(jLabelLowBound, gridBagConstraints);

        jLabelHighBound.setText("High bound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        middlePanel.add(jLabelHighBound, gridBagConstraints);

        lowBoundSpinner.setModel(new javax.swing.SpinnerNumberModel());
        lowBoundSpinner.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        middlePanel.add(lowBoundSpinner, gridBagConstraints);

        highBoundSpinner.setModel(new javax.swing.SpinnerNumberModel());
        highBoundSpinner.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        middlePanel.add(highBoundSpinner, gridBagConstraints);

        boundedPadPanel.setPreferredSize(new java.awt.Dimension(400, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        middlePanel.add(boundedPadPanel, gridBagConstraints);

        jLabelPad.setText("Padding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        middlePanel.add(jLabelPad, gridBagConstraints);

        alignPanel.setPreferredSize(new java.awt.Dimension(400, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        middlePanel.add(alignPanel, gridBagConstraints);

        jLabelAlign.setText("Alignment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 0);
        middlePanel.add(jLabelAlign, gridBagConstraints);

        add(middlePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignPanel;
    private javax.swing.JCheckBox boundedCheckBox;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel boundedPadPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingPanel1;
    private javax.swing.JSpinner highBoundSpinner;
    private javax.swing.JLabel jLabelAlign;
    private javax.swing.JLabel jLabelEncoding;
    private javax.swing.JLabel jLabelHighBound;
    private javax.swing.JLabel jLabelLowBound;
    private javax.swing.JLabel jLabelPad;
    private javax.swing.JLabel jLabelSelf;
    private javax.swing.JLabel jLabelUnit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner lowBoundSpinner;
    private javax.swing.JPanel middlePanel;
    private org.radixware.kernel.common.design.msdleditor.piece.PiecePanel piecePanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SelfInclusivePanel selfInclusivePanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel unitPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        save();
    }
    
    private void setBoundsEnabled(boolean status) {
        highBoundSpinner.setEnabled(status);
        lowBoundSpinner.setEnabled(status);
        alignPanel.setEnabled(status);
        boundedPadPanel.setEnabled(status);
    }
    
    private void readBounds() {
        boundedCheckBox.setSelected(true);
        setBoundsEnabled(true);
        
        lowBoundSpinner.setValue(embeddedLenDef.getBounds().getLowBound());
        highBoundSpinner.setValue(embeddedLenDef.getBounds().getHighBound());
        alignPanel.setAlign(EAlign.getInstance(embeddedLenDef.getAlign()), EAlign.getInstance(field.getAlign()));
        
        initPf();
        pf.fillWidgets();
        
        inited = true;
    }
    
    private void clearBounds() {
        boundedCheckBox.setSelected(false);
        
        lowBoundSpinner.setValue(0);
        highBoundSpinner.setValue(1);
        
        initPf();
        pf.fillWidgets();
        
        setBoundsEnabled(false);
        
        inited = true;
    }
    
    @Override
    public SmioCoder getCoder() {
        if (smioCoder == null) {
            String encoding = field.getEncoding();
            if (encoding != null) {
                smioCoder = new SmioCoder(encoding);
            }
        }
        return smioCoder;
    }

    private int getSpinnerValue(JSpinner spin) {
        return ((Integer)spin.getModel().getValue()).intValue();
    }
    
    private void initPf() {
        if (pf == null)
            try {
                pf = new PadFiller(embeddedLenDef, unitPanel1, boundedPadPanel, this, field);
            }
            catch(SmioException exc) {
                Logger.getLogger(EmbeddedLenPanel.class.getName()).log(Level.SEVERE, null, exc);
                pf = null;
            }
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if(inited)
            save();
    }
    
    void addActionListeners() {
        unitPanel1.addActionListener(this);
        unitPanel1.getSetParentPanel().addActionListener(this);
        selfInclusivePanel1.addActionListener(this);
        selfInclusivePanel1.getSetParentPanel().addActionListener(this);
        encodingPanel1.addActionListener(this);
        encodingPanel1.getSetParentPanel().addActionListener(this);
        lowBoundSpinner.addChangeListener(this);
        highBoundSpinner.addChangeListener(this);
        alignPanel.addActionListener(this);
        alignPanel.getSetParentPanel().addActionListener(this);
        boundedPadPanel.addActionListener(this);
    }
}
