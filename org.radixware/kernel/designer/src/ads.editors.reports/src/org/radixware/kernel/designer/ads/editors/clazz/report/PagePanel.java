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

import java.awt.Color;
import java.math.BigDecimal;
import javax.swing.AbstractSpinnerModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;

class PagePanel extends JPanel {
    
    private enum EPageFormat {
        
        A5("A5", 148, 210),
        A4("A4", 210, 297),
        A3("A3", 297, 420),
        //        INFINITE("Infinite", -1, -1),
        OTHER("<other>", -1, -1);
        String name;
        int width;
        int height;
        
        @Override
        public String toString() {
            return name;
        }
        
        EPageFormat(final String name, final int w, final int h) {
            this.name = name;
            width = w;
            height = h;
        }
        
        int getWidth() {
            return width;
        }
        
        int getHeight() {
            return height;
        }
        
        public static EPageFormat getPageFormatForValues(final int w, final int h) {
//            if (infinite) {
//                return INFINITE;
//            }
            for (EPageFormat format : EPageFormat.values()) {
                if (w != -1 && h != -1 && ((format.width == w && format.height == h) || (format.width == h && format.height == w))) {
                    return format;
                }
            }
            return OTHER;
        }
    }
    
    private class PageSizeComboBoxModel extends DefaultComboBoxModel<EPageFormat> {
        
        @Override
        public int getSize() {
            return EPageFormat.values().length;
        }
        
        @Override
        public EPageFormat getElementAt(final int index) {
            return EPageFormat.values()[index];
        }
    }
    
    private class IntSpinnerModel extends AbstractSpinnerModel {
        
        private int value = 1;
        
        @Override
        public Object getValue() {
            return Integer.valueOf(value);
        }
        
        @Override
        public void setValue(final Object value) {
            if (value == null) {
                this.value = 0;
            } else if (!(value instanceof Integer)) {
                this.value = Integer.valueOf(value.toString()).intValue();
            } else {
                this.value = ((Integer) value).intValue();
            }
            fireStateChanged();
        }
        
        @Override
        public Object getNextValue() {
            if (value < Integer.MAX_VALUE) {
                return Integer.valueOf(value + 1);
            }
            return getValue();
        }
        
        @Override
        public Object getPreviousValue() {
            if (value > 1) {
                return Integer.valueOf(value - 1);
            }
            return getValue();
        }
    }
    private final AdsReportForm form;
   
    private volatile boolean updating = false;
    private Color bgColor;
    private Color fgColor;


    /**
     * Creates new form ReportFormEditor
     */
    protected PagePanel(final AdsReportForm form) {
        this.form = form;
        initComponents();
        
        pageSizeComboBox.setModel(new PageSizeComboBoxModel());
        
        widthSpinner.setEditor(new CheckedNumberSpinnerEditor(widthSpinner));
//        heightEditor.setIntEditorModel(new IntSpinnerModel());
        heightEditor.setStep(Long.valueOf(1));
        heightEditor.setMinValue(Long.valueOf(1));
        heightEditor.setMaxValue(Long.valueOf(Integer.MAX_VALUE));
        
        topSpinner.setModel(new BigDecimalSpinnerModel());
        bottomSpinner.setModel(new BigDecimalSpinnerModel());
        rightSpinner.setModel(new BigDecimalSpinnerModel());
        leftSpinner.setModel(new BigDecimalSpinnerModel());
        topSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(topSpinner));
        bottomSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(bottomSpinner));
        rightSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(rightSpinner));
        leftSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(leftSpinner));
        
        if (form.getMode() == AdsReportForm.Mode.TEXT) {
            jLabel4.setText(jLabel4.getText().replace("(mm.)", "(cols)"));
            jLabel3.setText(jLabel3.getText().replace("(mm.)", "(rows)"));
        }
        heightEditor.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(final ChangeEvent e) {
                if (!updating) {
                    apply();
                }
            }
        });
        
        setupInitialValues();
    }
    
    private void setupInitialValues() {
        updating = true;
        pageSizeComboBox.setSelectedItem(EPageFormat.getPageFormatForValues(
                form.getPageWidthMm(), form.getPageHeightMm()/*, false form.isInfiniteHeight()*/));
        
        if (form.getMode() == AdsReportForm.Mode.TEXT) {
            widthSpinner.setValue(Long.valueOf(form.getPageWidthCols()));
            heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(form.getPageHeightRows()), EValType.INT));
            topSpinner.setValue(BigDecimal.valueOf(form.getMargin().getTopRows()));
            bottomSpinner.setValue(BigDecimal.valueOf(form.getMargin().getBottomRows()));
            rightSpinner.setValue(BigDecimal.valueOf(form.getMargin().getRightCols()));
            leftSpinner.setValue(BigDecimal.valueOf(form.getMargin().getLeftCols()));
        } else {
            
            if (form.getPageHeightMm() > form.getPageWidthMm()) {
                portraitRadioButton.setSelected(true);
            } else {
                landscapeRadioButton.setSelected(true);
            }
            widthSpinner.setValue(Long.valueOf(form.getPageWidthMm()));
            if (false/*appearance.isInfiniteHeight()*/) {
                heightEditor.setValue(EValType.STR, ValAsStr.Factory.newInstance("<infinite>", EValType.STR));
            } else {
                heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(form.getPageHeightMm()), EValType.INT));
            }
            
            topSpinner.setValue(BigDecimal.valueOf(form.getMargin().getTopMm()));
            bottomSpinner.setValue(BigDecimal.valueOf(form.getMargin().getBottomMm()));
            rightSpinner.setValue(BigDecimal.valueOf(form.getMargin().getRightMm()));
            leftSpinner.setValue(BigDecimal.valueOf(form.getMargin().getLeftMm()));
            bgColor = form.getBgColor();
            fgColor = form.getFgColor();
          
            bgColorButton.setColor(bgColor);
            textColorButton.setColor(fgColor);
        }
        updateEnableState();
        updating = false;
    }
    
    private void updateEnableState() {
        final boolean enabled = !form.isReadOnly();
        final boolean isTextMode = form.getMode() == AdsReportForm.Mode.TEXT;
        pageSizeComboBox.setEnabled(enabled && !isTextMode);
        EPageFormat format = (EPageFormat) pageSizeComboBox.getSelectedItem();
        final boolean en = enabled && /*format != EPageFormat.INFINITE &&*/ format != EPageFormat.OTHER;
//        jPanel2.setEnabled(en);
        portraitRadioButton.setEnabled(en && !isTextMode);
        landscapeRadioButton.setEnabled(en && !isTextMode);
        widthSpinner.setEnabled(enabled && (/*format == EPageFormat.INFINITE || */format == EPageFormat.OTHER || isTextMode));
        heightEditor.setEnabled(enabled && (format == EPageFormat.OTHER || isTextMode));
        topSpinner.setEnabled(enabled);
        bottomSpinner.setEnabled(enabled);
        rightSpinner.setEnabled(enabled);
        leftSpinner.setEnabled(enabled);
        bgColorButton.setEnabled(enabled && !isTextMode);
        textColorButton.setEnabled(enabled && !isTextMode);
       
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pageSizeComboBox = new javax.swing.JComboBox<EPageFormat>();
        jPanel2 = new javax.swing.JPanel();
        portraitRadioButton = new javax.swing.JRadioButton();
        landscapeRadioButton = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        heightEditor = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();
        jPanel4 = new javax.swing.JPanel();
        middlePanel = new javax.swing.JPanel();
        topSpinner = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        leftSpinner = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rightSpinner = new javax.swing.JSpinner();
        iconPanel = new javax.swing.JPanel();
        bottomSpinner = new javax.swing.JSpinner();
        jPanel9 = new javax.swing.JPanel();
        textColorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        bgColorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jPanel1.border.title"))); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel1.text")); // NOI18N

        pageSizeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pageSizeComboBoxItemStateChanged(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jPanel2.border.title"))); // NOI18N

        buttonGroup1.add(portraitRadioButton);
        portraitRadioButton.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.portraitRadioButton.text")); // NOI18N
        portraitRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portraitRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(landscapeRadioButton);
        landscapeRadioButton.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.landscapeRadioButton.text")); // NOI18N
        landscapeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                landscapeRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(portraitRadioButton)
                    .addComponent(landscapeRadioButton))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(portraitRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(landscapeRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jPanel3.border.title"))); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel4.text")); // NOI18N

        widthSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(1L), null, Long.valueOf(1L)));
        widthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSpinnerStateChanged(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(widthSpinner))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(heightEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pageSizeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pageSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jPanel4.border.title"))); // NOI18N
        jPanel4.setLayout(new java.awt.GridBagLayout());

        topSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        topSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                topSpinnerStateChanged(evt);
            }
        });

        jLabel8.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel8.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel5.text")); // NOI18N

        leftSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        leftSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftSpinnerStateChanged(evt);
            }
        });

        jLabel6.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel6.text")); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jLabel7.text")); // NOI18N

        rightSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        rightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout iconPanelLayout = new javax.swing.GroupLayout(iconPanel);
        iconPanel.setLayout(iconPanelLayout);
        iconPanelLayout.setHorizontalGroup(
            iconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        iconPanelLayout.setVerticalGroup(
            iconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );

        bottomSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        bottomSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bottomSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout middlePanelLayout = new javax.swing.GroupLayout(middlePanel);
        middlePanel.setLayout(middlePanelLayout);
        middlePanelLayout.setHorizontalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanelLayout.createSequentialGroup()
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        middlePanelLayout.setVerticalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanelLayout.createSequentialGroup()
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(rightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bottomSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel4.add(middlePanel, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.jPanel9.border.title"))); // NOI18N

        textColorButton.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.textColorButton.text")); // NOI18N
        textColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textColorButtonActionPerformed(evt);
            }
        });

        bgColorButton.setText(org.openide.util.NbBundle.getMessage(PagePanel.class, "PagePanel.bgColorButton.text")); // NOI18N
        bgColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgColorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bgColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(225, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bgColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pageSizeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pageSizeComboBoxItemStateChanged
        if (updating) {
            return;
        }
        final EPageFormat format = (EPageFormat) pageSizeComboBox.getSelectedItem();
        switch (format) {
            case A3:
            case A4:
            case A5:
                if (portraitRadioButton.isSelected()) {
                    widthSpinner.setValue(Long.valueOf(format.width));
                    heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(format.height), EValType.INT));
                } else {
                    widthSpinner.setValue(Long.valueOf(format.height));
                    heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(format.width), EValType.INT));
                }
                break;
//            case INFINITE:
//                heightEditor.setValue(EValType.STR, ValAsStr.Factory.newInstance("<infinite>", EValType.STR));
//                break;
            case OTHER:
                if (heightEditor.getValue().toString().equals("<infinite>")) {
                    heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(
                            Long.valueOf(form.getPageHeightMm()), EValType.INT));
                }
                break;
        }
        apply();
        updateEnableState();
    }//GEN-LAST:event_pageSizeComboBoxItemStateChanged

    private void portraitRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portraitRadioButtonActionPerformed
        if (portraitRadioButton.isSelected()) {
            EPageFormat format = (EPageFormat) pageSizeComboBox.getSelectedItem();
            if (format == EPageFormat.A3 || format == EPageFormat.A4 || format == EPageFormat.A5) {
                widthSpinner.setValue(Long.valueOf(format.width));
                heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(format.height), EValType.INT));
                apply();
            }
        }
    }//GEN-LAST:event_portraitRadioButtonActionPerformed

    private void landscapeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_landscapeRadioButtonActionPerformed
        if (landscapeRadioButton.isSelected()) {
            EPageFormat format = (EPageFormat) pageSizeComboBox.getSelectedItem();
            if (format == EPageFormat.A3 || format == EPageFormat.A4 || format == EPageFormat.A5) {
                widthSpinner.setValue(Long.valueOf(format.height));
                heightEditor.setValue(EValType.INT, ValAsStr.Factory.newInstance(Long.valueOf(format.width), EValType.INT));
                apply();
            }
        }
    }//GEN-LAST:event_landscapeRadioButtonActionPerformed

    private void widthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_widthSpinnerStateChanged

    private void topSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_topSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_topSpinnerStateChanged

    private void leftSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_leftSpinnerStateChanged

    private void bottomSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bottomSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_bottomSpinnerStateChanged

    private void rightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_rightSpinnerStateChanged

    private void bgColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgColorButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Background Color", bgColor);
        if (clr != null) {
            bgColor = clr;
            bgColorButton.setColor(bgColor);
            apply();
        }
    }//GEN-LAST:event_bgColorButtonActionPerformed

    private void textColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textColorButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Foreground Color", fgColor);
        if (clr != null) {
            fgColor = clr;
            textColorButton.setColor(fgColor);
            apply();
        }
    }//GEN-LAST:event_textColorButtonActionPerformed
    
    private void apply() {
        boolean isTextMode = form.getMode() == AdsReportForm.Mode.TEXT;
        if (isTextMode) {
            form.setPageWidthCols(((Long) widthSpinner.getValue()).intValue());
            form.setPageHeightRows((int) ((Long) heightEditor.getValue().toObject(EValType.INT)).longValue());
            form.getMargin().setTopRows(((BigDecimal) topSpinner.getValue()).intValue());
            form.getMargin().setBottomRows(((BigDecimal) bottomSpinner.getValue()).intValue());
            form.getMargin().setLeftCols(((BigDecimal) leftSpinner.getValue()).intValue());
            form.getMargin().setRightCols(((BigDecimal) rightSpinner.getValue()).intValue());
        } else {
            form.setPageWidthMm(((Long) widthSpinner.getValue()).intValue());
            form.setPageHeightMm((int) ((Long) heightEditor.getValue().toObject(EValType.INT)).longValue());
            form.getMargin().setTopMm(((BigDecimal) topSpinner.getValue()).doubleValue());
            form.getMargin().setBottomMm(((BigDecimal) bottomSpinner.getValue()).doubleValue());
            form.getMargin().setLeftMm(((BigDecimal) leftSpinner.getValue()).doubleValue());
            form.getMargin().setRightMm(((BigDecimal) rightSpinner.getValue()).doubleValue());
            form.setBgColor(bgColor);
            form.setFgColor(fgColor);
           
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton bgColorButton;
    private javax.swing.JSpinner bottomSpinner;
    private javax.swing.ButtonGroup buttonGroup1;
    private org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel heightEditor;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton landscapeRadioButton;
    private javax.swing.JSpinner leftSpinner;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JComboBox<EPageFormat> pageSizeComboBox;
    private javax.swing.JRadioButton portraitRadioButton;
    private javax.swing.JSpinner rightSpinner;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton textColorButton;
    private javax.swing.JSpinner topSpinner;
    private javax.swing.JSpinner widthSpinner;
    // End of variables declaration//GEN-END:variables
}
