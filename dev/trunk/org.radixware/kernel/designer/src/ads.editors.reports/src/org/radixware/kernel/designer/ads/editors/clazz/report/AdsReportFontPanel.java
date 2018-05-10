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

import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;

public class AdsReportFontPanel extends javax.swing.JPanel {

    private class FontComboBoxModel extends DefaultComboBoxModel<String> {

        private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        @Override
        public int getSize() {
            return fonts.length;
        }

        @Override
        public String getElementAt(final int index) {
            return fonts[index];
        }
    }

    //private AdsReportCell appearance;
    private Font font;
    private boolean updating = false;
    final ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * Creates new form AdsReportFontPanel
     */
    public AdsReportFontPanel(final Font font, final String title) {
        //this.appearance = appearance;
        this.font = font;
        initComponents();
        lbFont.setText(title);
        fontComboBox.setModel(new FontComboBoxModel());
        fontSizeSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1)));
        fontSizeSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(fontSizeSpinner));
        fontMMSizeSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(100), BigDecimal.valueOf(0.1)));
        fontMMSizeSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(fontMMSizeSpinner));
    }

    public void setFont(final Font font, final boolean updatePanel) {
        //this.appearance = appearance;
        this.font = font;
        if (updatePanel) {
            updatePanel();
        }
    }

    private void updatePanel() {
        updating = true;
        fontComboBox.setSelectedItem(font.getName());
        boldButton.setSelected(font.isBold());
        italicButton.setSelected(font.isItalic());
        fontSizeSpinner.setValue(BigDecimal.valueOf(font.getSizePts()));
        fontMMSizeSpinner.setValue(BigDecimal.valueOf(font.getSizeMm()));
        updating = false;
    }

    public void setPanelEnabled(final boolean enabled) {
        fontComboBox.setEnabled(enabled);
        boldButton.setEnabled(enabled);
        italicButton.setEnabled(enabled);
        fontSizeSpinner.setEnabled(enabled);
        fontMMSizeSpinner.setEnabled(enabled);
    }

    public void apply() {
        if (updating || font == null) {
            return;
        }
        if (fontComboBox.getSelectedItem() != null) {
            font.setName(fontComboBox.getSelectedItem().toString());
        }
        font.setBold(boldButton.isSelected());
        font.setItalic(italicButton.isSelected());
        font.setSizeMm(((BigDecimal) fontMMSizeSpinner.getValue()).doubleValue());
    }

    public final void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbFont = new javax.swing.JLabel();
        fontComboBox = new javax.swing.JComboBox<String>();
        boldButton = new javax.swing.JToggleButton();
        italicButton = new javax.swing.JToggleButton();
        fontSizeSpinner = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        fontMMSizeSpinner = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();

        lbFont.setText(org.openide.util.NbBundle.getMessage(AdsReportFontPanel.class, "AdsReportFontPanel.lbFont.text")); // NOI18N

        fontComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fontComboBoxItemStateChanged(evt);
            }
        });

        boldButton.setText(org.openide.util.NbBundle.getMessage(AdsReportFontPanel.class, "AdsReportFontPanel.boldButton.text")); // NOI18N
        boldButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        boldButton.setPreferredSize(new java.awt.Dimension(25, 25));
        boldButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                boldButtonItemStateChanged(evt);
            }
        });

        italicButton.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        italicButton.setText(org.openide.util.NbBundle.getMessage(AdsReportFontPanel.class, "AdsReportFontPanel.italicButton.text")); // NOI18N
        italicButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        italicButton.setPreferredSize(new java.awt.Dimension(25, 25));
        italicButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                italicButtonItemStateChanged(evt);
            }
        });

        fontSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeSpinnerStateChanged(evt);
            }
        });

        jLabel6.setText(org.openide.util.NbBundle.getMessage(AdsReportFontPanel.class, "AdsReportFontPanel.jLabel6.text")); // NOI18N

        fontMMSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontMMSizeSpinnerStateChanged(evt);
            }
        });

        jLabel7.setText(org.openide.util.NbBundle.getMessage(AdsReportFontPanel.class, "AdsReportFontPanel.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFont)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontComboBox, 0, 109, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boldButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(italicButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontMMSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(fontMMSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6)
                .addComponent(fontSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(italicButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(boldButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lbFont)
                .addComponent(fontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fontComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fontComboBoxItemStateChanged
        if (!updating) {
            apply();
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_fontComboBoxItemStateChanged

    private void boldButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_boldButtonItemStateChanged
        if (!updating) {
            apply();
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_boldButtonItemStateChanged

    private void italicButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_italicButtonItemStateChanged
        if (!updating) {
            apply();
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_italicButtonItemStateChanged

    private void fontSizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinnerStateChanged
        if (!updating) {
            updating = true;
            fontMMSizeSpinner.getModel().setValue(pts2mm(((BigDecimal) fontSizeSpinner.getValue())));
            updating = false;
            apply();
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_fontSizeSpinnerStateChanged

    private void fontMMSizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontMMSizeSpinnerStateChanged
        if (!updating) {
            updating = true;
            fontSizeSpinner.getModel().setValue(mm2pts(((BigDecimal) fontMMSizeSpinner.getValue())));
            updating = false;
            apply();
            changeSupport.fireChange();
        }
    }//GEN-LAST:event_fontMMSizeSpinnerStateChanged

    private static final double MM2PTS_CONST = 25.4 / 72;

    protected static BigDecimal mm2pts(BigDecimal mm) {
        return BigDecimal.valueOf(Math.floor(10.0 * mm.doubleValue() / MM2PTS_CONST) / 10.0);
    }

    protected static BigDecimal pts2mm(BigDecimal pts) {
        return BigDecimal.valueOf(Math.floor(10.0 * pts.doubleValue() * MM2PTS_CONST) / 10.0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton boldButton;
    private javax.swing.JComboBox<String> fontComboBox;
    private javax.swing.JSpinner fontMMSizeSpinner;
    private javax.swing.JSpinner fontSizeSpinner;
    private javax.swing.JToggleButton italicButton;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lbFont;
    // End of variables declaration//GEN-END:variables
}
