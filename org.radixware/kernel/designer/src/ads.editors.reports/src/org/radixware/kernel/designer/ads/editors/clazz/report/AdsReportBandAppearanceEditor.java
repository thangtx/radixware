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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.math.BigDecimal;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportWidgetUtils;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.MmUtils;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;
import org.radixware.kernel.common.resources.RadixWareIcons;

class AdsReportBandAppearanceEditor extends JPanel implements ChangeListener {

    @Override
    public void stateChanged(final ChangeEvent e) {
        updateFont();
    }

//    private class FontComboBoxModel extends DefaultComboBoxModel<String> {
//
//        private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//
//        @Override
//        public int getSize() {
//            return fonts.length;
//        }
//
//        @Override
//        public String getElementAt(final int index) {
//            return fonts[index];
//        }
//    }
    private volatile boolean updating = false;
    private final AdsReportBand appearance;
    private Color zebraColor;
    private Color defaultZebraColor = Color.decode("#f2f2f2");
    private final AdsReportFontPanel innerFontPanel;

    /**
     * Creates new form AdsReportBandAppearanceEditor
     */
    public AdsReportBandAppearanceEditor(final AdsReportBand appearance) {
        super();
        this.appearance = appearance;
        initComponents();

        iconButton.setIcon(RadixWareIcons.DIALOG.ALL.getIcon());
        iconButton.setText("");

        innerFontPanel = new AdsReportFontPanel(appearance.getFont(), "Font");
        innerFontPanel.changeSupport.addChangeListener(this);
        fontPanel.setLayout(new BorderLayout());
        fontPanel.add(innerFontPanel, BorderLayout.NORTH);
        fontPanel.updateUI();

        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;

        innerFontPanel.setFont(appearance.getFont(), true);
        updateFont();

        //fontComboBox.setModel(new FontComboBoxModel());
        if (isTextMode) {
            heightSpinner.setModel(new SpinnerNumberModel(0, 0, 1000000, 1));
            //heightSpinner.setEditor(new SpinnerNumberEdCheckedBigDecimalSpinnerEditor(heightSpinner));
        } else {
            heightSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000000000), BigDecimal.valueOf(1)));
            heightSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(heightSpinner));
        }
//        fontSizeSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1)));
//        fontSizeSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(fontSizeSpinner));
        widthSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(0.1)));
        widthSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(widthSpinner));
        //cellWrappingEnabledCheckBox.setVisible(false);

        if (isTextMode) {
            heightLabel.setText(heightLabel.getText().replace("mm", "rows"));
        }
        setupInitialValues();
    }

    private void updateFont() {
        final AdsReportAbstractAppearance.Font reportFont = appearance.getFont();
        final Font font = AdsReportWidgetUtils.reportFont2JavaFont(reportFont, textSampleLabel);
        textSampleLabel.setFont(font);
    }

    private void setupInitialValues() {
        updating = true;
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        if (isTextMode) {
            heightSpinner.setValue(Integer.valueOf(appearance.getHeightRows()));
        } else {
            heightSpinner.setValue(BigDecimal.valueOf(appearance.getHeightMm()));
        }
        inheritBgColorCheckBox.setSelected(appearance.isBgColorInherited());
        bgColorButton.setColor(appearance.getBgColor());
        textSamplePanel.setBackground(appearance.getBgColor());
        inheritFgColorCheckBox.setSelected(appearance.isFgColorInherited());
        fgColorButton.setColor(appearance.getFgColor());
        textSampleLabel.setForeground(appearance.getFgColor());
//        fontComboBox.setSelectedItem(appearance.getFont().getName());
//        boldButton.setSelected(appearance.getFont().isBold());
//        italicButton.setSelected(appearance.getFont().isItalic());
//        fontSizeSpinner.setValue(BigDecimal.valueOf(appearance.getFont().getSizePts()));
        innerFontPanel.setFont(appearance.getFont(), true);
        updateFont();

        switch (appearance.getBorder().getStyle()) {
            case SOLID:
                solidRadioButton.setSelected(true);
                break;
            case DASHED:
                dashedRadioButton.setSelected(true);
                break;
            case DOTTED:
                dottedRadioButton.setSelected(true);
                break;
        }
        topCheckBox.setSelected(appearance.getBorder().isOnTop());
        leftCheckBox.setSelected(appearance.getBorder().isOnLeft());
        rightCheckBox.setSelected(appearance.getBorder().isOnRight());
        bottomCheckBox.setSelected(appearance.getBorder().isOnBottom());
        colorButton.setColor(appearance.getBorder().getColor());
        widthSpinner.setValue(BigDecimal.valueOf(appearance.getBorder().getThicknessMm()));
        updateBorder();
        startCheckBox.setSelected(appearance.isStartOnNewPage());
        lastCheckBox.setSelected(appearance.isLastOnPage());
        multiCheckBox.setSelected(appearance.isMultiPage());
        cellWrappingEnabledCheckBox.setSelected(appearance.isCellWrappingEnabled());
//        cellLinkEditPanel.open(ChooseDefinitionCfg.Factory.newInstance(appearance.getOwnerBand().getCells().list()),
//                appearance.findHeightAdjustCell(), appearance.getHeightAdjustCellId());
        autoHeightCheckBox.setSelected(appearance.isAutoHeight());
        zebraColor = appearance.getAltBgColor();
        zebraColorButton.setColor(zebraColor);
        chUseZebra.setSelected(zebraColor != null);
        chInsideZebra.setSelected(appearance.isInsideAltColor());
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        boolean enabled = !appearance.isReadOnly();
        heightSpinner.setEnabled(enabled);
        inheritBgColorCheckBox.setEnabled(enabled && !isTextMode);
        bgColorButton.setEnabled(enabled && !inheritBgColorCheckBox.isSelected() && !isTextMode);
        inheritFgColorCheckBox.setEnabled(enabled && !isTextMode);
        fgColorButton.setEnabled(enabled && !inheritFgColorCheckBox.isSelected() && !isTextMode);
//        fontComboBox.setEnabled(enabled && !isTextMode);
//        boldButton.setEnabled(enabled && !isTextMode);
//        italicButton.setEnabled(enabled && !isTextMode);
//        fontSizeSpinner.setEnabled(enabled && !isTextMode);
        innerFontPanel.setPanelEnabled(enabled && !isTextMode);
        solidRadioButton.setEnabled(enabled && !isTextMode);
        dashedRadioButton.setEnabled(enabled && !isTextMode);
        dottedRadioButton.setEnabled(enabled && !isTextMode);
        topCheckBox.setEnabled(enabled && !isTextMode);
        leftCheckBox.setEnabled(enabled && !isTextMode);
        rightCheckBox.setEnabled(enabled && !isTextMode);
        bottomCheckBox.setEnabled(enabled && !isTextMode);
        colorButton.setEnabled(enabled && !isTextMode);
        widthSpinner.setEnabled(enabled && !isTextMode);
        boolean flag = appearance.getType() == EReportBandType.PAGE_HEADER || appearance.getType() == EReportBandType.PAGE_FOOTER;
        startCheckBox.setEnabled(enabled && !flag);
        lastCheckBox.setEnabled(enabled && !flag);
        multiCheckBox.setEnabled(enabled && !flag);
//        cellLinkEditPanel.setEnabled(enabled);
        autoHeightCheckBox.setEnabled(enabled);
        cellWrappingEnabledCheckBox.setEnabled(appearance.isMultiPage() && enabled);
        chUseZebra.setEnabled(enabled && !isTextMode);
        zebraColorButton.setEnabled(enabled && !isTextMode && zebraColor != null);
        chInsideZebra.setEnabled(enabled && !isTextMode && zebraColor != null);
    }

//    private void updateFont() {
//        AdsReportAbstractAppearance.Font reportFont = appearance.getFont();
//        Font font = AdsReportWidgetUtils.reportFont2JavaFont(reportFont, textSampleLabel);
//        textSampleLabel.setFont(font);
////        String name = fontComboBox.getSelectedItem() != null ? fontComboBox.getSelectedItem().toString() : "Arial";
////        textSampleLabel.setFont(new Font(name,
////                (boldButton.isSelected() ? Font.BOLD : 0) |
////                (italicButton.isSelected() ? Font.ITALIC : 0),
////                (int) ((BigDecimal) fontSizeSpinner.getValue()).doubleValue()));
//    }
    private void updateBorder() {

        double ww = ((BigDecimal) widthSpinner.getValue()).doubleValue();
        int w = MmUtils.mm2px(ww);
        if (w == 0 && ww > 0) {
            w = 1;
        }
        textSamplePanel.setBorder(new MatteBorder(
                topCheckBox.isSelected() ? w : 0,
                leftCheckBox.isSelected() ? w : 0,
                bottomCheckBox.isSelected() ? w : 0,
                rightCheckBox.isSelected() ? w : 0,
                colorButton.getColor()));
        Insets ins = textSamplePanel.getInsets();
        if (dashedRadioButton.isSelected()) {
            textSamplePanel.setBorder(new DashedBorder(
                    topCheckBox.isSelected() ? w : 0,
                    leftCheckBox.isSelected() ? w : 0,
                    bottomCheckBox.isSelected() ? w : 0,
                    rightCheckBox.isSelected() ? w : 0,
                    colorButton.getColor(),
                    Math.max(w * 2, 10), ins));
        } else if (dottedRadioButton.isSelected()) {
            textSamplePanel.setBorder(new DashedBorder(
                    topCheckBox.isSelected() ? w : 0,
                    leftCheckBox.isSelected() ? w : 0,
                    bottomCheckBox.isSelected() ? w : 0,
                    rightCheckBox.isSelected() ? w : 0,
                    colorButton.getColor(),
                    Math.max(w, 1), ins));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        textSamplePanel = new javax.swing.JPanel();
        textSampleLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        heightSpinner = new javax.swing.JSpinner();
        inheritBgColorCheckBox = new javax.swing.JCheckBox();
        inheritFgColorCheckBox = new javax.swing.JCheckBox();
        bgColorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        fgColorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        chUseZebra = new javax.swing.JCheckBox();
        zebraColorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        chInsideZebra = new javax.swing.JCheckBox();
        fontPanel = new javax.swing.JPanel();
        borderPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        leftCheckBox = new javax.swing.JCheckBox();
        iconButton = new javax.swing.JButton();
        rightCheckBox = new javax.swing.JCheckBox();
        topCheckBox = new javax.swing.JCheckBox();
        bottomCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        colorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        solidRadioButton = new javax.swing.JRadioButton();
        dashedRadioButton = new javax.swing.JRadioButton();
        dottedRadioButton = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        startCheckBox = new javax.swing.JCheckBox();
        lastCheckBox = new javax.swing.JCheckBox();
        multiCheckBox = new javax.swing.JCheckBox();
        autoHeightCheckBox = new javax.swing.JCheckBox();
        cellWrappingEnabledCheckBox = new javax.swing.JCheckBox();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        textSamplePanel.setBackground(new java.awt.Color(255, 255, 255));

        textSampleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textSampleLabel.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.textSampleLabel.text")); // NOI18N

        javax.swing.GroupLayout textSamplePanelLayout = new javax.swing.GroupLayout(textSamplePanel);
        textSamplePanel.setLayout(textSamplePanelLayout);
        textSamplePanelLayout.setHorizontalGroup(
            textSamplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textSampleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );
        textSamplePanelLayout.setVerticalGroup(
            textSamplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textSampleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );

        heightLabel.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.heightLabel.text")); // NOI18N

        heightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                heightSpinnerStateChanged(evt);
            }
        });

        inheritBgColorCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.inheritBgColorCheckBox.text")); // NOI18N
        inheritBgColorCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                inheritBgColorCheckBoxItemStateChanged(evt);
            }
        });

        inheritFgColorCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.inheritFgColorCheckBox.text")); // NOI18N
        inheritFgColorCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                inheritFgColorCheckBoxItemStateChanged(evt);
            }
        });

        bgColorButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.bgColorButton.text")); // NOI18N
        bgColorButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bgColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgColorButtonActionPerformed(evt);
            }
        });

        fgColorButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.fgColorButton.text")); // NOI18N
        fgColorButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        fgColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fgColorButtonActionPerformed(evt);
            }
        });

        chUseZebra.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.chUseZebra.text")); // NOI18N
        chUseZebra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chUseZebraActionPerformed(evt);
            }
        });

        zebraColorButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.zebraColorButton.text")); // NOI18N
        zebraColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zebraColorButtonActionPerformed(evt);
            }
        });

        chInsideZebra.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.chInsideZebra.text")); // NOI18N
        chInsideZebra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chInsideZebraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(textSamplePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inheritBgColorCheckBox)
                                    .addComponent(inheritFgColorCheckBox))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(fgColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bgColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(heightLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zebraColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chUseZebra, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chInsideZebra))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightLabel)
                    .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chUseZebra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inheritBgColorCheckBox)
                    .addComponent(bgColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chInsideZebra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inheritFgColorCheckBox)
                    .addComponent(fgColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zebraColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textSamplePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fontPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.fontPanel.border.title"))); // NOI18N

        javax.swing.GroupLayout fontPanelLayout = new javax.swing.GroupLayout(fontPanel);
        fontPanel.setLayout(fontPanelLayout);
        fontPanelLayout.setHorizontalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 768, Short.MAX_VALUE)
        );
        fontPanelLayout.setVerticalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        borderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.borderPanel.border.title"))); // NOI18N

        jPanel4.setLayout(new java.awt.GridBagLayout());

        leftCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.leftCheckBox.text")); // NOI18N
        leftCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        leftCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                leftCheckBoxItemStateChanged(evt);
            }
        });

        iconButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.iconButton.text")); // NOI18N
        iconButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iconButtonActionPerformed(evt);
            }
        });

        rightCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.rightCheckBox.text")); // NOI18N
        rightCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rightCheckBoxItemStateChanged(evt);
            }
        });

        topCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.topCheckBox.text")); // NOI18N
        topCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                topCheckBoxItemStateChanged(evt);
            }
        });

        bottomCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.bottomCheckBox.text")); // NOI18N
        bottomCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bottomCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(leftCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomCheckBox)
                    .addComponent(topCheckBox)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(iconButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightCheckBox))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(topCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leftCheckBox)
                    .addComponent(iconButton)
                    .addComponent(rightCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomCheckBox))
        );

        jPanel4.add(jPanel5, new java.awt.GridBagConstraints());

        jLabel4.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.jLabel4.text")); // NOI18N

        widthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSpinnerStateChanged(evt);
            }
        });

        colorButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.colorButton.text")); // NOI18N
        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(solidRadioButton);
        solidRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.solidRadioButton.text")); // NOI18N
        solidRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solidRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(dashedRadioButton);
        dashedRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.dashedRadioButton.text")); // NOI18N
        dashedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashedRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(dottedRadioButton);
        dottedRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.dottedRadioButton.text")); // NOI18N
        dottedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dottedRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
        borderPanel.setLayout(borderPanelLayout);
        borderPanelLayout.setHorizontalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(solidRadioButton)
                    .addComponent(dashedRadioButton)
                    .addComponent(dottedRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(widthSpinner)
                    .addComponent(colorButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        borderPanelLayout.setVerticalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(borderPanelLayout.createSequentialGroup()
                        .addComponent(solidRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dashedRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dottedRadioButton))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(borderPanelLayout.createSequentialGroup()
                        .addComponent(colorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.jPanel6.border.title"))); // NOI18N

        startCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.startCheckBox.text")); // NOI18N
        startCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startCheckBoxActionPerformed(evt);
            }
        });

        lastCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.lastCheckBox.text")); // NOI18N
        lastCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastCheckBoxActionPerformed(evt);
            }
        });

        multiCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.multiCheckBox.text")); // NOI18N
        multiCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiCheckBoxActionPerformed(evt);
            }
        });

        autoHeightCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.autoHeightCheckBox.text")); // NOI18N
        autoHeightCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoHeightCheckBoxActionPerformed(evt);
            }
        });

        cellWrappingEnabledCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportBandAppearanceEditor.class, "AdsReportBandAppearanceEditor.cellWrappingEnabledCheckBox.text")); // NOI18N
        cellWrappingEnabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cellWrappingEnabledCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startCheckBox)
                    .addComponent(lastCheckBox))
                .addGap(66, 66, 66)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoHeightCheckBox)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(multiCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(cellWrappingEnabledCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startCheckBox)
                    .addComponent(multiCheckBox)
                    .addComponent(cellWrappingEnabledCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastCheckBox)
                    .addComponent(autoHeightCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(borderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fontPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(borderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void leftCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_leftCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_leftCheckBoxItemStateChanged

    private void topCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_topCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_topCheckBoxItemStateChanged

    private void rightCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rightCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_rightCheckBoxItemStateChanged

    private void bottomCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_bottomCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_bottomCheckBoxItemStateChanged

    private void widthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSpinnerStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_widthSpinnerStateChanged

    private void inheritBgColorCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_inheritBgColorCheckBoxItemStateChanged
        if (!updating) {
            apply();
            bgColorButton.setColor(appearance.getBgColor());
            textSamplePanel.setBackground(appearance.getBgColor());
            updateEnableState();
        }
    }//GEN-LAST:event_inheritBgColorCheckBoxItemStateChanged

    private void inheritFgColorCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_inheritFgColorCheckBoxItemStateChanged
        if (!updating) {
            apply();
            fgColorButton.setColor(appearance.getFgColor());
            textSampleLabel.setForeground(appearance.getFgColor());
            updateEnableState();
        }
    }//GEN-LAST:event_inheritFgColorCheckBoxItemStateChanged

    private void heightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_heightSpinnerStateChanged
        apply();
    }//GEN-LAST:event_heightSpinnerStateChanged

    private void startCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_startCheckBoxActionPerformed

    private void lastCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_lastCheckBoxActionPerformed

    private void multiCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiCheckBoxActionPerformed
        apply();
        updateEnableState();
    }//GEN-LAST:event_multiCheckBoxActionPerformed

    private void autoHeightCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoHeightCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_autoHeightCheckBoxActionPerformed

    private void iconButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iconButtonActionPerformed
        if (topCheckBox.isSelected() || leftCheckBox.isSelected()
                || rightCheckBox.isSelected() || bottomCheckBox.isSelected()) {
            topCheckBox.setSelected(false);
            leftCheckBox.setSelected(false);
            rightCheckBox.setSelected(false);
            bottomCheckBox.setSelected(false);
        } else {
            topCheckBox.setSelected(true);
            leftCheckBox.setSelected(true);
            rightCheckBox.setSelected(true);
            bottomCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_iconButtonActionPerformed

    private void bgColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgColorButtonActionPerformed
        Color clr = JColorChooser.showDialog(this, "Choose Background Color", bgColorButton.getColor());
        if (clr != null) {
            bgColorButton.setColor(clr);
            textSamplePanel.setBackground(clr);
            apply();
        }
    }//GEN-LAST:event_bgColorButtonActionPerformed

    private void fgColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fgColorButtonActionPerformed
        Color clr = JColorChooser.showDialog(this, "Choose Foreground Color", fgColorButton.getColor());
        if (clr != null) {
            fgColorButton.setColor(clr);
            textSampleLabel.setForeground(clr);
            apply();
        }
    }//GEN-LAST:event_fgColorButtonActionPerformed

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        Color clr = JColorChooser.showDialog(this, "Choose Border Color", colorButton.getColor());
        if (clr != null) {
            colorButton.setColor(clr);
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_colorButtonActionPerformed

    private void solidRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solidRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_solidRadioButtonActionPerformed

    private void dashedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashedRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_dashedRadioButtonActionPerformed

    private void dottedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dottedRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_dottedRadioButtonActionPerformed

    private void cellWrappingEnabledCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cellWrappingEnabledCheckBoxActionPerformed
        if (!updating) {
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_cellWrappingEnabledCheckBoxActionPerformed

    private void chUseZebraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chUseZebraActionPerformed
        if (chUseZebra.isSelected()) {
            zebraColor = defaultZebraColor;
            zebraColorButton.setEnabled(!appearance.isReadOnly());
            zebraColorButton.setColor(zebraColor);
            chInsideZebra.setEnabled(!appearance.isReadOnly());
        } else {
            zebraColor = null;
            zebraColorButton.setEnabled(false);
            chInsideZebra.setEnabled(false);
        }
        apply();
    }//GEN-LAST:event_chUseZebraActionPerformed

    private void zebraColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zebraColorButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Alternative Background Color", zebraColor);
        if (clr != null) {
            zebraColor = clr;
            zebraColorButton.setColor(zebraColor);
            apply();
        }
    }//GEN-LAST:event_zebraColorButtonActionPerformed

    private void chInsideZebraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chInsideZebraActionPerformed
        if (!updating) {
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_chInsideZebraActionPerformed

    private void apply() {
        if (updating) {
            return;
        }
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        if (isTextMode) {
            appearance.setHeightRows(((Number) heightSpinner.getValue()).intValue());
        } else {
            appearance.setHeightMm(((BigDecimal) heightSpinner.getValue()).doubleValue());
        }
        appearance.setBgColorInherited(inheritBgColorCheckBox.isSelected());
        appearance.setBgColor(bgColorButton.getColor());
        appearance.setFgColorInherited(inheritFgColorCheckBox.isSelected());
        appearance.setFgColor(fgColorButton.getColor());
        final AdsReportAbstractAppearance.Font reportFont = appearance.getFont();

        innerFontPanel.apply();
        if (solidRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.SOLID);
        } else if (dashedRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.DASHED);
        } else if (dottedRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.DOTTED);
        }
        appearance.getBorder().setOnTop(topCheckBox.isSelected());
        appearance.getBorder().setOnLeft(leftCheckBox.isSelected());
        appearance.getBorder().setOnBottom(bottomCheckBox.isSelected());
        appearance.getBorder().setOnRight(rightCheckBox.isSelected());
        appearance.getBorder().setColor(colorButton.getColor());
        appearance.getBorder().setThicknessMm(((BigDecimal) widthSpinner.getValue()).doubleValue());
        appearance.setStartOnNewPage(startCheckBox.isSelected());
        appearance.setLastOnPage(lastCheckBox.isSelected());
        appearance.setMultiPage(multiCheckBox.isSelected());
//        appearance.setHeightAdjustCellId(cellLinkEditPanel.getDefinitionId());
        appearance.setAutoHeight(autoHeightCheckBox.isSelected());
        appearance.setCellWrappingEnabled(cellWrappingEnabledCheckBox.isSelected());
        appearance.setAltBgColor(zebraColor);
        appearance.setInsideAltColor(chInsideZebra.isSelected());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoHeightCheckBox;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton bgColorButton;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JCheckBox bottomCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cellWrappingEnabledCheckBox;
    private javax.swing.JCheckBox chInsideZebra;
    private javax.swing.JCheckBox chUseZebra;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton colorButton;
    private javax.swing.JRadioButton dashedRadioButton;
    private javax.swing.JRadioButton dottedRadioButton;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton fgColorButton;
    private javax.swing.JPanel fontPanel;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JSpinner heightSpinner;
    private javax.swing.JButton iconButton;
    private javax.swing.JCheckBox inheritBgColorCheckBox;
    private javax.swing.JCheckBox inheritFgColorCheckBox;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JCheckBox lastCheckBox;
    private javax.swing.JCheckBox leftCheckBox;
    private javax.swing.JCheckBox multiCheckBox;
    private javax.swing.JCheckBox rightCheckBox;
    private javax.swing.JRadioButton solidRadioButton;
    private javax.swing.JCheckBox startCheckBox;
    private javax.swing.JLabel textSampleLabel;
    private javax.swing.JPanel textSamplePanel;
    private javax.swing.JCheckBox topCheckBox;
    private javax.swing.JSpinner widthSpinner;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton zebraColorButton;
    // End of variables declaration//GEN-END:variables
}
