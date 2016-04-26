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
 * StructurePanel.java
 *
 * Created on 18.03.2009, 11:30:26
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.design.msdleditor.enums.EAlign;
import org.radixware.kernel.common.design.msdleditor.enums.EDateTimeFormat;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.design.msdleditor.enums.ESelfInclusive;
import org.radixware.kernel.common.design.msdleditor.enums.EShieldedFormat;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.design.msdleditor.field.RootPanel;
import org.radixware.kernel.common.msdl.EFieldsFormat;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.enums.EStrCharSet;
import org.radixware.schemas.msdl.Structure;


public class StructurePanel extends AbstractEditItem implements ActionListener {

    private JPanel childrenFormatPanel = null;
    private StructureFieldModel fieldModel;
    private Structure structure;
    private boolean isOpened;
    RootPanel rootPanel;
    /** Creates new form StructurePanel */
    public StructurePanel() {
        initComponents();
        ArrayList<JLabel> l = new ArrayList<>();
        l.add(jLabel3);
        ArrayList<JComponent> e = new ArrayList<>();
        e.add(jComboBoxFieldsFormatType);
        DefaultLayout.doLayout(jPanel1, l, e,true);

        //Common
        ArrayList<JLabel> lcommon = new ArrayList<>();
        lcommon.add(encodingLabel);
        lcommon.add(jLabel2);
        lcommon.add(jLabel1);
        ArrayList<JComponent> ecommon = new ArrayList<>();
        ecommon.add(encodingPanelCommon);
        ecommon.add(extHexPanelNullIndicator);
        ecommon.add(extHexPanelItemSeparator);
        DefaultLayout.doLayout(jPanelCommon, lcommon, ecommon,true);

        //Str
        ArrayList<JLabel> lstr = new ArrayList<>();
        lstr.add(jLabel20);
        lstr.add(jLabel22);
        lstr.add(jLabel23);
        lstr.add(jLabel33);
        lstr.add(jLabel34);
        ArrayList<JComponent> estr = new ArrayList<>();
        estr.add(encodingPanelStrField);
        estr.add(alignPanelStr);
        estr.add(strPadPanel);
        estr.add(charSetPanel);
        estr.add(charSetExp);
        DefaultLayout.doLayout(jPanelStr, lstr, estr,true);

        //Bin
        ArrayList<JLabel> lbin = new ArrayList<>();
        lbin.add(jLabel29);
        lbin.add(jLabel30);
        lbin.add(jLabel9);
        lbin.add(jLabel8);
        ArrayList<JComponent> ebin = new ArrayList<>();
        ebin.add(encodingPanelBin);
        ebin.add(alignPanelBin);
        ebin.add(unitBinPanel);
        ebin.add(extHexBinPanel);
        DefaultLayout.doLayout(jPanelBin, lbin, ebin,true);

        //BCH
        ArrayList<JLabel> lbch = new ArrayList<>();
        lbch.add(jLabel16);
        lbch.add(jLabel18);
        ArrayList<JComponent> ebch = new ArrayList<>();
        ebch.add(alignBCHPanel);
        ebch.add(extCharBCHPanel);
        DefaultLayout.doLayout(jPanelBCH, lbch, ebch, true);

        //DateTime
        ArrayList<JLabel> ldt = new ArrayList<>();
        ldt.add(jLabel32);
        ldt.add(jLabel19);
        ldt.add(jLabel21);
        ArrayList<JComponent> edt = new ArrayList<>();
        edt.add(encodingDateTimePanel);
        edt.add(dateTimeFormatPanel);
        edt.add(dateTimePatternPanel);
        DefaultLayout.doLayout(jPanelDateTime, ldt, edt,true);

        //Int,Num Fields
        ArrayList<JLabel> lint = new ArrayList<>();
        lint.add(jLabel6);
        lint.add(jLabel24);
        lint.add(jLabel25);
        lint.add(jLabel26);
        lint.add(jLabel27);
        lint.add(jLabel28);
        ArrayList<JComponent> eint = new ArrayList<>();
        eint.add(encodingIntNumPanel);
        eint.add(alignIntNumPanel);
        eint.add(plusSignPanel);
        eint.add(minusSigngPanel);
        eint.add(fractPointPanel);
        eint.add(padIntNumPanel);
        DefaultLayout.doLayout(jPanelIntNum, lint, eint,true);

        //Fixed Len
        ArrayList<JLabel> lfl = new ArrayList<>();
        lfl.add(jLabel4);
        lfl.add(jLabel5);
        lfl.add(jLabel7);
        lfl.add(jLabel10);
        ArrayList<JComponent> efl = new ArrayList<>();
        efl.add(unitFixedLenPanel);
        efl.add(alignFixedLenPanel);
        efl.add(padBinFixedLenPanel);
        efl.add(padCharFixedLenPanel);
        DefaultLayout.doLayout(jPanelFixedLen, lfl, efl,true);

        //Separated
        ArrayList<JLabel> lsep = new ArrayList<>();
        lsep.add(jLabel11);
        lsep.add(jLabel12);
        lsep.add(jLabel13);
        lsep.add(jLabel14);
        ArrayList<JComponent> esep = new ArrayList<>();
        esep.add(startSepPanel);
        esep.add(endSepPanel);
        esep.add(schieldedFormatPanel);
        esep.add(shieldPanel);
        DefaultLayout.doLayout(jPanelSeparated, lsep, esep,true);

        //EmbeddedLen
        ArrayList<JLabel> lel = new ArrayList<>();
        lel.add(jLabel15);
        ArrayList<JComponent> eel = new ArrayList<>();
        eel.add(selfInclusivePanel);
        DefaultLayout.doLayout(jPanelEmbeddedLen, lel, eel,true);
    }

    public void open(StructureFieldModel field, RootPanel rootPanel) {
        super.open(field.getMsdlField());
        this.rootPanel = rootPanel;
        fieldModel = field;
        structure = field.getStructure();
        isOpened = false;
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EFieldsFormat.SEQUENTALL_ORDER);
        model.addElement(EFieldsFormat.BITMAP);
        model.addElement(EFieldsFormat.FIELD_NAMING);
        model.addElement(EFieldsFormat.BERTLV);
        if (field.getMsdlField() instanceof RootMsdlScheme) {
            model.addElement(EFieldsFormat.DBF);
        }
        jComboBoxFieldsFormatType.setModel(model);
        jComboBoxFieldsFormatType.getModel().setSelectedItem(field.getStructureType());
        addChildrenFormatPanel();

        DefaultComboBoxModel binModel = new DefaultComboBoxModel();
        binModel.addElement(EEncoding.NONE);
        binModel.addElement(EEncoding.BIN);
        binModel.addElement(EEncoding.HEX);
        binModel.addElement(EEncoding.HEXEBCDIC);
        binModel.addElement(EEncoding.DECIMAL);
        encodingPanelBin.setEncodingModel(binModel);

        DefaultComboBoxModel dmodel = new DefaultComboBoxModel();
        dmodel.addElement(EEncoding.NONE);
        dmodel.addElement(EEncoding.ASCII);
        dmodel.addElement(EEncoding.BCD);
        dmodel.addElement(EEncoding.CP1251);
        dmodel.addElement(EEncoding.CP1252);
        dmodel.addElement(EEncoding.CP866);
        dmodel.addElement(EEncoding.EBCDIC);
        dmodel.addElement(EEncoding.UTF8);
        encodingDateTimePanel.setEncodingModel(dmodel);

        DefaultComboBoxModel smodel = new DefaultComboBoxModel();
        smodel.addElement(EEncoding.NONE);
        smodel.addElement(EEncoding.ASCII);
        smodel.addElement(EEncoding.CP866);
        smodel.addElement(EEncoding.CP1251);
        smodel.addElement(EEncoding.CP1252);
        smodel.addElement(EEncoding.EBCDIC);
        smodel.addElement(EEncoding.UTF8);
        encodingPanelStrField.setEncodingModel(smodel);

        DefaultComboBoxModel cmodel = new DefaultComboBoxModel();
        cmodel.addElement(EStrCharSet.None);
        cmodel.addElement(EStrCharSet.Any);
        cmodel.addElement(EStrCharSet.XML);
        cmodel.addElement(EStrCharSet.User);
        charSetPanel.setCharSetModel(cmodel);
        
        //Common
        encodingPanelCommon.addActionListener(this);
        encodingPanelCommon.getSetParentPanel().addActionListener(this);
        extHexPanelNullIndicator.addActionListener(this);
        extHexPanelItemSeparator.addActionListener(this);
        
        if (!structure.isSetDefaultEncoding() && (fieldModel == null 
                || !(fieldModel.getContainer() instanceof RootMsdlScheme))) {
            encodingPanelCommon.setEnabled(false);
            encodingPanelCommon.setVisible(false);
            encodingLabel.setVisible(false);
        }
        else {
            encodingPanelCommon.setEnabled(true);
            encodingPanelCommon.setVisible(true);
            encodingLabel.setVisible(true);
        }

        //Str
        encodingPanelStrField.addActionListener(this);
        encodingPanelStrField.getSetParentPanel().addActionListener(this);        
        alignPanelStr.addActionListener(this);
        alignPanelStr.getSetParentPanel().addActionListener(this);
        strPadPanel.addActionListener(this);
        charSetPanel.addActionListener(this);
        charSetPanel.getSetParentPanel().addActionListener(this);        
        charSetExp.addActionListener(this);
        final DocumentListener dls = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                actionPerformed(null);
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                actionPerformed(null);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                actionPerformed(null);
            }
        };
        charSetExp.getDocument().addDocumentListener(dls);
        
        //Bin
        encodingPanelBin.addActionListener(this);
        encodingPanelBin.getSetParentPanel().addActionListener(this);
        alignPanelBin.addActionListener(this);
        alignPanelBin.getSetParentPanel().addActionListener(this);
        unitBinPanel.addActionListener(this);
        unitBinPanel.getSetParentPanel().addActionListener(this);
        extHexBinPanel.addActionListener(this);

        //BCH
        alignBCHPanel.addActionListener(this);
        alignBCHPanel.getSetParentPanel().addActionListener(this);
        extCharBCHPanel.addActionListener(this);

        //DateTime
        encodingDateTimePanel.addActionListener(this);
        encodingDateTimePanel.getSetParentPanel().addActionListener(this);
        dateTimeFormatPanel.addActionListener(this);
        dateTimeFormatPanel.getSetParentPanel().addActionListener(this);
        dateTimePatternPanel.addActionListener(this);
        dateTimePatternPanel.getSetParentPanel().addActionListener(this);

        //Int,Num Fields
        encodingIntNumPanel.addActionListener(this);
        encodingIntNumPanel.getSetParentPanel().addActionListener(this);
        alignIntNumPanel.addActionListener(this);
        alignIntNumPanel.getSetParentPanel().addActionListener(this);
        plusSignPanel.addActionListener(this);
        minusSigngPanel.addActionListener(this);
        fractPointPanel.addActionListener(this);
        padIntNumPanel.addActionListener(this);

        //Fixed Len
        unitFixedLenPanel.addActionListener(this);
        unitFixedLenPanel.getSetParentPanel().addActionListener(this);
        alignFixedLenPanel.addActionListener(this);
        alignFixedLenPanel.getSetParentPanel().addActionListener(this);
        padBinFixedLenPanel.addActionListener(this);
        padCharFixedLenPanel.addActionListener(this);

        //Separated
        startSepPanel.addActionListener(this);
        endSepPanel.addActionListener(this);
        schieldedFormatPanel.addActionListener(this);
        schieldedFormatPanel.getSetParentPanel().addActionListener(this);
        shieldPanel.addActionListener(this);

        //EmbeddedLen
        selfInclusivePanel.addActionListener(this);
        selfInclusivePanel.getSetParentPanel().addActionListener(this);

        update();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelFieldsFormat = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxFieldsFormatType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jPanelChildrenType = new javax.swing.JPanel();
        jPanelFieldDefault = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelCommon = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        encodingPanelCommon = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        encodingLabel = new javax.swing.JLabel();
        extHexPanelNullIndicator = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        extHexPanelItemSeparator = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        jPanelStr = new javax.swing.JPanel();
        encodingPanelStrField = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        alignPanelStr = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        strPadPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        charSetExp = new javax.swing.JTextField();
        charSetPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.CharSetPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanelBin = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        encodingPanelBin = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        alignPanelBin = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        unitBinPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel();
        extHexBinPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        jPanelBCH = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        alignBCHPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        jLabel16 = new javax.swing.JLabel();
        extCharBCHPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jPanelDateTime = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        encodingDateTimePanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        jLabel32 = new javax.swing.JLabel();
        dateTimeFormatPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimeFormatPanel();
        dateTimePatternPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimePatternPanel();
        jPanelIntNum = new javax.swing.JPanel();
        encodingIntNumPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel();
        alignIntNumPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        plusSignPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel25 = new javax.swing.JLabel();
        minusSigngPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel26 = new javax.swing.JLabel();
        fractPointPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel27 = new javax.swing.JLabel();
        padIntNumPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanelFormattingDefault = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelFixedLen = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        unitFixedLenPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel();
        alignFixedLenPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel();
        padBinFixedLenPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        padCharFixedLenPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel();
        jPanelSeparated = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        startSepPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        endSepPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        schieldedFormatPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ShieldedFormatPanel();
        shieldPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel();
        jPanelEmbeddedLen = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        selfInclusivePanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SelfInclusivePanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(107, 170));
        setPreferredSize(new java.awt.Dimension(400, 260));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane.setMinimumSize(new java.awt.Dimension(107, 125));

        jComboBoxFieldsFormatType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxFieldsFormatType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFieldsFormatTypeActionPerformed(evt);
            }
        });

        jLabel3.setText("Type");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxFieldsFormatType, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxFieldsFormatType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelChildrenType.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelFieldsFormatLayout = new javax.swing.GroupLayout(jPanelFieldsFormat);
        jPanelFieldsFormat.setLayout(jPanelFieldsFormatLayout);
        jPanelFieldsFormatLayout.setHorizontalGroup(
            jPanelFieldsFormatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelChildrenType, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );
        jPanelFieldsFormatLayout.setVerticalGroup(
            jPanelFieldsFormatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFieldsFormatLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelChildrenType, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Child Fields Format", jPanelFieldsFormat);

        jPanelFieldDefault.setLayout(new java.awt.BorderLayout());

        jPanelCommon.setName(""); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("ITEM_SEPARATOR")); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText(bundle.getString("NULL_INDICATOR")); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        encodingPanelCommon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingPanelCommonActionPerformed(evt);
            }
        });

        encodingLabel.setText("Encoding");

        javax.swing.GroupLayout jPanelCommonLayout = new javax.swing.GroupLayout(jPanelCommon);
        jPanelCommon.setLayout(jPanelCommonLayout);
        jPanelCommonLayout.setHorizontalGroup(
            jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCommonLayout.createSequentialGroup()
                .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCommonLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(encodingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanelCommonLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(extHexPanelNullIndicator, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(encodingPanelCommon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, Short.MAX_VALUE)
                    .addComponent(extHexPanelItemSeparator, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        jPanelCommonLayout.setVerticalGroup(
            jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCommonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(encodingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(encodingPanelCommon, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(extHexPanelNullIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(extHexPanelItemSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(287, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Common", jPanelCommon);

        encodingPanelStrField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingPanelStrFieldActionPerformed(evt);
            }
        });

        alignPanelStr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignPanelStrActionPerformed(evt);
            }
        });

        jLabel20.setText("Encoding");

        jLabel22.setText("Align");

        jLabel23.setText("Pad");

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jLabel34.setText(bundle1.getString("StrPanel.jLabel3.text")); // NOI18N
        jLabel34.setToolTipText(bundle1.getString("StrPanel.jLabel3.tootip")); // NOI18N

        charSetExp.setText(bundle1.getString("StrPanel.jTextField2.text")); // NOI18N
        charSetExp.setToolTipText(bundle1.getString("StrPanel.jLabel3.tootip")); // NOI18N
        charSetExp.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                charSetExpCaretUpdate(evt);
            }
        });

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText(bundle1.getString("StrPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanelStrLayout = new javax.swing.GroupLayout(jPanelStr);
        jPanelStr.setLayout(jPanelStrLayout);
        jPanelStrLayout.setHorizontalGroup(
            jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStrLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel22)
                        .addComponent(jLabel23)
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(charSetExp)
                    .addComponent(charSetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(alignPanelStr, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(encodingPanelStrField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(strPadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                .addContainerGap(165, Short.MAX_VALUE))
        );
        jPanelStrLayout.setVerticalGroup(
            jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStrLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(encodingPanelStrField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignPanelStr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(strPadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(charSetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelStrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(charSetExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(231, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Str Field", jPanelStr);

        jLabel8.setText(bundle.getString("PAD_BIN")); // NOI18N

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText(bundle.getString("UNIT")); // NOI18N

        encodingPanelBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingPanelBinActionPerformed(evt);
            }
        });

        alignPanelBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignPanelBinActionPerformed(evt);
            }
        });

        jLabel29.setText("Encoding");

        jLabel30.setText("Align");

        javax.swing.GroupLayout jPanelBinLayout = new javax.swing.GroupLayout(jPanelBin);
        jPanelBin.setLayout(jPanelBinLayout);
        jPanelBinLayout.setHorizontalGroup(
            jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelBinLayout.createSequentialGroup()
                        .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(extHexBinPanel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(unitBinPanel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(alignPanelBin, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelBinLayout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(encodingPanelBin, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        jPanelBinLayout.setVerticalGroup(
            jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(encodingPanelBin, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignPanelBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitBinPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extHexBinPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(257, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bin Field", jPanelBin);

        jLabel18.setText(bundle.getString("PAD")); // NOI18N

        jLabel16.setText("Align");

        javax.swing.GroupLayout jPanelBCHLayout = new javax.swing.GroupLayout(jPanelBCH);
        jPanelBCH.setLayout(jPanelBCHLayout);
        jPanelBCHLayout.setHorizontalGroup(
            jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBCHLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(extCharBCHPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignBCHPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(64, 64, 64))
        );
        jPanelBCHLayout.setVerticalGroup(
            jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBCHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignBCHPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extCharBCHPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(317, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BCH Field", jPanelBCH);

        jLabel19.setText(bundle.getString("FORMAT")); // NOI18N

        jLabel21.setText(bundle.getString("PATTERN")); // NOI18N

        encodingDateTimePanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingDateTimePanelActionPerformed(evt);
            }
        });

        jLabel32.setText("Encoding");

        dateTimePatternPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateTimePatternPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDateTimeLayout = new javax.swing.GroupLayout(jPanelDateTime);
        jPanelDateTime.setLayout(jPanelDateTimeLayout);
        jPanelDateTimeLayout.setHorizontalGroup(
            jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDateTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDateTimeLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateTimePatternPanel, 0, 144, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDateTimeLayout.createSequentialGroup()
                        .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateTimeFormatPanel, 0, 0, Short.MAX_VALUE)
                            .addComponent(encodingDateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, Short.MAX_VALUE))))
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanelDateTimeLayout.setVerticalGroup(
            jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDateTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(encodingDateTimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateTimeFormatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateTimePatternPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(286, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Date Time Field", jPanelDateTime);

        encodingIntNumPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingIntNumPanelActionPerformed(evt);
            }
        });

        alignIntNumPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignIntNumPanelActionPerformed(evt);
            }
        });

        jLabel6.setText("Encoding");

        jLabel24.setText("Align");

        jLabel25.setText("Plus Sign");

        minusSigngPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusSigngPanelActionPerformed(evt);
            }
        });

        jLabel26.setText("Minus Sign");

        fractPointPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fractPointPanelActionPerformed(evt);
            }
        });

        jLabel27.setText("Fractional Point");

        padIntNumPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                padIntNumPanelActionPerformed(evt);
            }
        });

        jLabel28.setText("Pad");

        javax.swing.GroupLayout jPanelIntNumLayout = new javax.swing.GroupLayout(jPanelIntNum);
        jPanelIntNum.setLayout(jPanelIntNumLayout);
        jPanelIntNumLayout.setHorizontalGroup(
            jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelIntNumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26)
                    .addComponent(jLabel28)
                    .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanelIntNumLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(padIntNumPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fractPointPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minusSigngPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plusSignPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignIntNumPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(encodingIntNumPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(153, Short.MAX_VALUE))
        );
        jPanelIntNumLayout.setVerticalGroup(
            jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelIntNumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encodingIntNumPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alignIntNumPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(plusSignPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minusSigngPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fractPointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelIntNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(padIntNumPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(199, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Int, Num Fields", jPanelIntNum);

        jTabbedPane1.setSelectedComponent(jPanelCommon);

        jPanelFieldDefault.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Field Defaults", jPanelFieldDefault);

        jPanelFormattingDefault.setLayout(new java.awt.BorderLayout());

        jLabel4.setText(bundle.getString("UNIT")); // NOI18N

        jLabel5.setText(bundle.getString("ALIGN")); // NOI18N

        jLabel7.setText(bundle.getString("PAD_BIN")); // NOI18N

        jLabel10.setText(bundle.getString("PAD_CHAR")); // NOI18N

        javax.swing.GroupLayout jPanelFixedLenLayout = new javax.swing.GroupLayout(jPanelFixedLen);
        jPanelFixedLen.setLayout(jPanelFixedLenLayout);
        jPanelFixedLenLayout.setHorizontalGroup(
            jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFixedLenLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(padCharFixedLenPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(padBinFixedLenPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(alignFixedLenPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(unitFixedLenPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelFixedLenLayout.setVerticalGroup(
            jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFixedLenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitFixedLenPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(alignFixedLenPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(padBinFixedLenPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFixedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(padCharFixedLenPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Fixed Length", jPanelFixedLen);

        jLabel11.setText(bundle.getString("START_SEPARATOR")); // NOI18N

        jLabel12.setText(bundle.getString("END_SEPARATOR")); // NOI18N

        jLabel13.setText(bundle.getString("SHIELDED_FORMAT")); // NOI18N

        jLabel14.setText(bundle.getString("SHIELD")); // NOI18N

        javax.swing.GroupLayout jPanelSeparatedLayout = new javax.swing.GroupLayout(jPanelSeparated);
        jPanelSeparated.setLayout(jPanelSeparatedLayout);
        jPanelSeparatedLayout.setHorizontalGroup(
            jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeparatedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(shieldPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(endSepPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(startSepPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(schieldedFormatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        jPanelSeparatedLayout.setVerticalGroup(
            jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeparatedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startSepPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endSepPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(schieldedFormatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSeparatedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shieldPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(257, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Separated", jPanelSeparated);

        jLabel15.setText(bundle.getString("SELF_INCLUSIVE")); // NOI18N

        javax.swing.GroupLayout jPanelEmbeddedLenLayout = new javax.swing.GroupLayout(jPanelEmbeddedLen);
        jPanelEmbeddedLen.setLayout(jPanelEmbeddedLenLayout);
        jPanelEmbeddedLenLayout.setHorizontalGroup(
            jPanelEmbeddedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmbeddedLenLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selfInclusivePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );
        jPanelEmbeddedLenLayout.setVerticalGroup(
            jPanelEmbeddedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmbeddedLenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEmbeddedLenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selfInclusivePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(344, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Embedded Length", jPanelEmbeddedLen);

        jPanelFormattingDefault.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Formatting Defaults", jPanelFormattingDefault);

        add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Structure Settings");
        jLabel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.add(jLabel17, java.awt.BorderLayout.NORTH);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void alignIntNumPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignIntNumPanelActionPerformed
        // do nothing
}//GEN-LAST:event_alignIntNumPanelActionPerformed

    private void encodingIntNumPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingIntNumPanelActionPerformed
        // do nothing
}//GEN-LAST:event_encodingIntNumPanelActionPerformed

    private void alignPanelBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignPanelBinActionPerformed
        // do nothing
    }//GEN-LAST:event_alignPanelBinActionPerformed

    private void jComboBoxFieldsFormatTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFieldsFormatTypeActionPerformed
        if (!isOpened)
            return;
        EFieldsFormat format = (EFieldsFormat)jComboBoxFieldsFormatType.getSelectedItem();
        fieldModel.setStructureType(format);
        jPanelChildrenType.removeAll();
//        if (childrenFormatPanel != null) {
//            jPanelChildrenType.remove(childrenFormatPanel);
//            childrenFormatPanel = null;
//        }
        addChildrenFormatPanel();
        fieldModel.setModified();
    }//GEN-LAST:event_jComboBoxFieldsFormatTypeActionPerformed

    private void alignPanelStrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignPanelStrActionPerformed
        // do nothing
    }//GEN-LAST:event_alignPanelStrActionPerformed

    private void encodingPanelStrFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingPanelStrFieldActionPerformed
        // do nothing
    }//GEN-LAST:event_encodingPanelStrFieldActionPerformed

    private void encodingDateTimePanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingDateTimePanelActionPerformed
        // do nothing
}//GEN-LAST:event_encodingDateTimePanelActionPerformed

    private void encodingPanelCommonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingPanelCommonActionPerformed
        // do nothing
    }//GEN-LAST:event_encodingPanelCommonActionPerformed

    private void encodingPanelBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingPanelBinActionPerformed
        // do nothing
    }//GEN-LAST:event_encodingPanelBinActionPerformed

    private void fractPointPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fractPointPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_fractPointPanelActionPerformed

    private void minusSigngPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusSigngPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_minusSigngPanelActionPerformed

    private void padIntNumPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_padIntNumPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_padIntNumPanelActionPerformed

    private void dateTimePatternPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateTimePatternPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_dateTimePatternPanelActionPerformed

    private void charSetExpCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_charSetExpCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_charSetExpCaretUpdate

    private void addChildrenFormatPanel() {
        jPanelChildrenType.removeAll();
        jPanelChildrenType.validate();
        if (structure.isSetDbf()) {
           DbfPanel panel = new DbfPanel();
           panel.open(fieldModel);
           childrenFormatPanel = panel;
           jTabbedPane.setEnabledAt(1, false);
           jTabbedPane.setEnabledAt(2, false);
           if (rootPanel != null)
               rootPanel.setDbfViewMode(true);
        }
        else {
           jTabbedPane.setEnabledAt(1, true);
           jTabbedPane.setEnabledAt(2, true);
           if (rootPanel != null)
               rootPanel.setDbfViewMode(false);
            if (structure.isSetBitmap()) {
                BitmapPanel panel = new BitmapPanel();
                panel.open(fieldModel);
                childrenFormatPanel = panel;
            }
            else {
                if (structure.isSetFieldNaming()) {
                    if (structure.getFieldNaming().isSetPiece()) {
                        FieldNamingPanel panel = new FieldNamingPanel();
                        panel.open(fieldModel);
                        childrenFormatPanel = panel;
                    }
                    else if (structure.getFieldNaming().isSetBerTLV())  {
                        BerTLVPanel panel = null;
                        panel = new BerTLVPanel();
                        panel.open(fieldModel);
                        childrenFormatPanel = panel;
                    }
                }
                else {
                    SequentallyOrderPanel panel = new SequentallyOrderPanel();
                    panel.open(fieldModel);
                    childrenFormatPanel = panel;
                }
            }
        }
        jPanelChildrenType.add(childrenFormatPanel, BorderLayout.CENTER);
        //childrenFormatPanel.setBackground(Color.red);
        jPanelChildrenType.validate();
    }

private void save() {
    //Common
    structure.setDefaultEncoding(encodingPanelCommon.getEncoding().getValue());

    structure.setDefaultNullIndicator(extHexPanelNullIndicator.getValue());
    structure.setDefaultNullIndicatorViewType(extHexPanelNullIndicator.getViewEncodingAsStr());
    structure.setDefaultItemSeparator(extHexPanelItemSeparator.getValue());
    structure.setDefaultItemSeparatorViewType(extHexPanelItemSeparator.getViewEncodingAsStr());

    //Str
    structure.setDefaultStrEncoding(encodingPanelStrField.getEncoding().getValue());
    structure.setDefaultStrAlign(alignPanelStr.getAlign().getValue());
    structure.setDefaultStrPadChar(strPadPanel.getString());
    structure.setDefaultCharSetType(charSetPanel.getCharSet().getValue());
    structure.setDefaultCharSetExp(charSetExp.getText());
    charSetExp.setVisible(charSetPanel.getCharSet() == EStrCharSet.User);
    jLabel34.setVisible(charSetPanel.getCharSet() == EStrCharSet.User);

    //Bin
    structure.setDefaultBinEncoding(encodingPanelBin.getEncoding().getValue());
    structure.setDefaultBinAlign(alignPanelBin.getAlign().getValue());
    structure.setDefaultBinUnit(unitBinPanel.getUnit().getValue());
    structure.setDefaultBinPad(extHexBinPanel.getValue());
    structure.setDefaultBinPadViewType(extHexBinPanel.getViewEncodingAsStr());

    //BCH
    structure.setDefaultBCHAlign(alignBCHPanel.getAlign().getValue());
    structure.setDefaultBCHPadChar(extCharBCHPanel.getString());

    //DateTime
    structure.setDefaultDateTimeEncoding(encodingDateTimePanel.getEncoding().getValue());
    structure.setDefaultDateTimeFormat(dateTimeFormatPanel.getFormat().getValue());
    structure.setDefaultDateTimePattern(dateTimePatternPanel.getString());

    //Int,Num Fields
    structure.setDefaultIntNumEncoding(encodingIntNumPanel.getEncoding().getValue());
    structure.setDefaultIntNumAlign(alignIntNumPanel.getAlign().getValue());
    structure.setDefaultPlusSign(plusSignPanel.getCharacter());
    structure.setDefaultMinusSign(minusSigngPanel.getCharacter());
    structure.setDefaultFractionalPoint(fractPointPanel.getCharacter());
    structure.setDefaultIntNumPadChar(padIntNumPanel.getString());

    //Fixed Len
    structure.setDefaultUnit(unitFixedLenPanel.getUnit().getValue());
    structure.setAlign(alignFixedLenPanel.getAlign().getValue());
    structure.setPadBin(padBinFixedLenPanel.getValue());
    structure.setPadViewType(padBinFixedLenPanel.getViewEncodingAsStr());
    structure.setPadChar(padCharFixedLenPanel.getString());

    //Separated
    structure.setStartSeparator(startSepPanel.getValue());
    structure.setStartSeparatorViewType(startSepPanel.getViewEncodingAsStr());
    structure.setEndSeparator(endSepPanel.getValue());
    structure.setEndSeparatorViewType(endSepPanel.getViewEncodingAsStr());
    structure.setShield(shieldPanel.getValue());
    structure.setShieldViewType(shieldPanel.getViewEncodingAsStr());
    structure.setShieldedFormat(schieldedFormatPanel.getShieldedFormat().getValue());

    //EmbeddedLen
    structure.setIsSelfInclusive(selfInclusivePanel.getSelfInclusive().getValue());

    fieldModel.setModified();
}

@Override
public void update() {
    isOpened = false;
    
    boolean rootMsdl = fieldModel.isRootMsdlScheme();
    
    //Common
    encodingPanelCommon.setEncoding(EEncoding.getInstance(structure.getDefaultEncoding()),
                                    EEncoding.getInstance(fieldModel.getEncoding(false)));
    extHexPanelNullIndicator.setValue(structure.getDefaultNullIndicator(),
            fieldModel.getNullIndicator(false),
            EEncoding.getInstanceForHexViewType(structure.getDefaultNullIndicatorViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getNullIndicatorViewType(false)));
    extHexPanelItemSeparator.setValue(structure.getDefaultItemSeparator(),
            fieldModel.getItemSeparator(false),
            EEncoding.getInstanceForHexViewType(structure.getDefaultItemSeparatorViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getItemSeparatorViewType(false)));

    //Str    
    encodingPanelStrField.setEncoding(EEncoding.getInstance(structure.getDefaultStrEncoding()),
                          EEncoding.getInstance(fieldModel.getStrEncoding(rootMsdl)));
    
    alignPanelStr.setAlign(EAlign.getInstance(structure.getDefaultStrAlign()),
                          EAlign.getInstance(fieldModel.getStrAlign(false)));
    strPadPanel.setString(structure.getDefaultStrPadChar(), fieldModel.getStrPadChar(false));
    charSetPanel.setCharSet(EStrCharSet.getInstance(structure.getDefaultCharSetType()),
                          EStrCharSet.getInstance(fieldModel.getCharSetType(false)));
    charSetExp.setText(structure.getDefaultCharSetExp());
    charSetExp.setVisible(charSetPanel.getCharSet() == EStrCharSet.User);
    jLabel34.setVisible(charSetPanel.getCharSet() == EStrCharSet.User);
    
    //Bin
    encodingPanelBin.setEncoding(EEncoding.getInstance(structure.getDefaultBinEncoding()),
                                 EEncoding.getInstance(fieldModel.getBinEncoding(rootMsdl)));
    alignPanelBin.setAlign(EAlign.getInstance(structure.getDefaultBinAlign()),
                           EAlign.getInstance(fieldModel.getBinAlign(false)));
    unitBinPanel.setUnit(EUnit.getInstance(structure.getDefaultBinUnit()),
                         EUnit.getInstance(fieldModel.getBinUnit(false)));
    extHexBinPanel.setValue(structure.getDefaultBinPad(), fieldModel.getBinPad(false), 
            EEncoding.getInstanceForHexViewType(structure.getDefaultBinPadViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getBinPadViewType(false)));

    //BCH
    alignBCHPanel.setAlign(EAlign.getInstance(structure.getDefaultBCHAlign()),
                           EAlign.getInstance(fieldModel.getBCHAlign(false)));
    extCharBCHPanel.setString(structure.getDefaultBCHPadChar(), fieldModel.getBCHPad(false));


    encodingDateTimePanel.setEncoding(EEncoding.getInstance(structure.getDefaultDateTimeEncoding()),
                                    EEncoding.getInstance(fieldModel.getDateTimeEncoding(rootMsdl)));
    dateTimeFormatPanel.setFormat(EDateTimeFormat.getInstance(structure.getDefaultDateTimeFormat()),
                                    EDateTimeFormat.getInstance(fieldModel.getDateTimeFormat(false)));
    dateTimePatternPanel.setPattern(structure.getDefaultDateTimePattern(),
                                    fieldModel.getDateTimePattern(false));

    encodingIntNumPanel.setEncoding(EEncoding.getInstance(structure.getDefaultIntNumEncoding()),
                                    EEncoding.getInstance(fieldModel.getIntEncoding(rootMsdl)));
    alignIntNumPanel.setAlign(EAlign.getInstance(structure.getDefaultIntNumAlign()),
                           EAlign.getInstance(fieldModel.getIntNumAlign(false)));

    plusSignPanel.setCharacter(structure.getDefaultPlusSign(), fieldModel.getPlusSign(false));
    minusSigngPanel.setCharacter(structure.getDefaultMinusSign(), fieldModel.getMinusSign(false));
    fractPointPanel.setCharacter(structure.getDefaultFractionalPoint(), fieldModel.getFractionalPoint(false));
    padIntNumPanel.setString(structure.getDefaultIntNumPadChar(), fieldModel.getIntNumPadChar(false));

    //Fixed Len
    unitFixedLenPanel.setUnit(EUnit.getInstance(structure.getDefaultUnit()),
                              EUnit.getInstance(fieldModel.getUnit(false)));

    alignFixedLenPanel.setAlign(EAlign.getInstance(structure.getAlign()),
                           EAlign.getInstance(fieldModel.getAlign(false)));
    padBinFixedLenPanel.setValue(structure.getPadBin(), fieldModel.getBinPad(false), 
            EEncoding.getInstanceForHexViewType(structure.getPadViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getPadViewType(false)));
    padCharFixedLenPanel.setString(structure.getPadChar(), fieldModel.getPadChar(false));

    //Separated
    startSepPanel.setValue(structure.getStartSeparator(), fieldModel.getStartSeparator(false),
            EEncoding.getInstanceForHexViewType(structure.getStartSeparatorViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getStartSeparatorViewType(false)));
    endSepPanel.setValue(structure.getEndSeparator(), fieldModel.getEndSeparator(false),
            EEncoding.getInstanceForHexViewType(structure.getEndSeparatorViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getEndSeparatorViewType(false)));
    schieldedFormatPanel.setShieldedFormat(EShieldedFormat.getInstance(structure.getShieldedFormat()),
                                           EShieldedFormat.getInstance(fieldModel.getShieldedFormat(false)));
    shieldPanel.setValue(structure.getShield(), fieldModel.getShield(false),
            EEncoding.getInstanceForHexViewType(structure.getShieldViewType()),
            EEncoding.getInstanceForHexViewType(fieldModel.getShieldViewType(false)));

    //EmbeddedLen
    selfInclusivePanel.setSelfInclusive(ESelfInclusive.getInstance(structure.getIsSelfInclusive()),
                                        ESelfInclusive.getInstance(fieldModel.getSelfInclusive(false)));
    isOpened = true;
    super.update();
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignBCHPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignFixedLenPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignIntNumPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignPanelBin;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.AlignPanel alignPanelStr;
    private javax.swing.JTextField charSetExp;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.CharSetPanel charSetPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimeFormatPanel dateTimeFormatPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.DateTimePatternPanel dateTimePatternPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingDateTimePanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingIntNumPanel;
    private javax.swing.JLabel encodingLabel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingPanelBin;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingPanelCommon;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.EncodingPanel encodingPanelStrField;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel endSepPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel extCharBCHPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel extHexBinPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel extHexPanelItemSeparator;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel extHexPanelNullIndicator;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel fractPointPanel;
    private javax.swing.JComboBox jComboBoxFieldsFormatType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBCH;
    private javax.swing.JPanel jPanelBin;
    private javax.swing.JPanel jPanelChildrenType;
    private javax.swing.JPanel jPanelCommon;
    private javax.swing.JPanel jPanelDateTime;
    private javax.swing.JPanel jPanelEmbeddedLen;
    private javax.swing.JPanel jPanelFieldDefault;
    private javax.swing.JPanel jPanelFieldsFormat;
    private javax.swing.JPanel jPanelFixedLen;
    private javax.swing.JPanel jPanelFormattingDefault;
    private javax.swing.JPanel jPanelIntNum;
    private javax.swing.JPanel jPanelSeparated;
    private javax.swing.JPanel jPanelStr;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel minusSigngPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel padBinFixedLenPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel padCharFixedLenPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel padIntNumPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel plusSignPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ShieldedFormatPanel schieldedFormatPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SelfInclusivePanel selfInclusivePanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel shieldPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel startSepPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtCharPanel strPadPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel unitBinPanel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel unitFixedLenPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (!isOpened) {
            return;
        }
        save();
    }
}