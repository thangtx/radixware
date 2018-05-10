/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.appearance;

import java.awt.Color;
import java.math.BigDecimal;
import javax.swing.JColorChooser;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.designer.ads.reports.AdsReportDialogsUtils;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;

/**
 *
 * @author avoloshchuk
 */
public class AdsReportBorderPropertyPanel extends javax.swing.JPanel {
    private boolean updating = false;

    /**
     * Creates new form AdsReportBorderPropertyPanel
     */
    public AdsReportBorderPropertyPanel() {
        initComponents();
        widthSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(0.1)));
        widthSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(widthSpinner));
    }
    
    public void open(double thiknesMm, EReportBorderStyle style, Color color) {
        updating = true;
        switch (style) {
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
        colorButton.setColor(color);
        widthSpinner.setValue(BigDecimal.valueOf(thiknesMm));
        updating = false;
    }

    public Color getColor() {
        return colorButton.getColor();
    }

    public double getThiknesMm() {
        return ((BigDecimal) widthSpinner.getValue()).doubleValue();
    }
    
    public EReportBorderStyle getStyle(){
        if (dashedRadioButton.isSelected()) {
            return EReportBorderStyle.DASHED;
        } else if (dottedRadioButton.isSelected()) {
            return EReportBorderStyle.DOTTED;
        }
        return EReportBorderStyle.SOLID;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        widthSpinner.setEnabled(enabled);
        colorButton.setEnabled(enabled);
        solidRadioButton.setEnabled(enabled);
        dashedRadioButton.setEnabled(enabled);
        dottedRadioButton.setEnabled(enabled);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        solidRadioButton = new javax.swing.JRadioButton();
        dashedRadioButton = new javax.swing.JRadioButton();
        dottedRadioButton = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        colorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();

        setMinimumSize(new java.awt.Dimension(105, 136));

        buttonGroup.add(solidRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(solidRadioButton, org.openide.util.NbBundle.getMessage(AdsReportBorderPropertyPanel.class, "AdsReportBorderPropertyPanel.solidRadioButton.text")); // NOI18N
        solidRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solidRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(dashedRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(dashedRadioButton, org.openide.util.NbBundle.getMessage(AdsReportBorderPropertyPanel.class, "AdsReportBorderPropertyPanel.dashedRadioButton.text")); // NOI18N
        dashedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashedRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(dottedRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(dottedRadioButton, org.openide.util.NbBundle.getMessage(AdsReportBorderPropertyPanel.class, "AdsReportBorderPropertyPanel.dottedRadioButton.text")); // NOI18N
        dottedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dottedRadioButtonActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(AdsReportBorderPropertyPanel.class, "AdsReportBorderPropertyPanel.jLabel6.text")); // NOI18N
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        widthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSpinnerStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(colorButton, org.openide.util.NbBundle.getMessage(AdsReportBorderPropertyPanel.class, "AdsReportBorderPropertyPanel.colorButton.text")); // NOI18N
        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(dashedRadioButton)
                    .addComponent(solidRadioButton)
                    .addComponent(dottedRadioButton)
                    .addComponent(widthSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(solidRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashedRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dottedRadioButton)
                .addGap(2, 2, 2)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(colorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void solidRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solidRadioButtonActionPerformed
        fireChanges();
    }//GEN-LAST:event_solidRadioButtonActionPerformed

    private void dashedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashedRadioButtonActionPerformed
        fireChanges();
    }//GEN-LAST:event_dashedRadioButtonActionPerformed

    private void dottedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dottedRadioButtonActionPerformed
        fireChanges();
    }//GEN-LAST:event_dottedRadioButtonActionPerformed

    private void widthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSpinnerStateChanged
        fireChanges();
    }//GEN-LAST:event_widthSpinnerStateChanged

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
       final Color clr = JColorChooser.showDialog(this, "Choose Border Color", colorButton.getColor());
        if (clr != null) {
            colorButton.setColor(clr);
            fireChanges();
        }
    }//GEN-LAST:event_colorButtonActionPerformed

    private void fireChanges(){
        if (!updating){
            firePropertyChange(AdsReportDialogsUtils.BORDER_CHANGE, false, true);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton colorButton;
    private javax.swing.JRadioButton dashedRadioButton;
    private javax.swing.JRadioButton dottedRadioButton;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton solidRadioButton;
    private javax.swing.JSpinner widthSpinner;
    // End of variables declaration//GEN-END:variables
}
