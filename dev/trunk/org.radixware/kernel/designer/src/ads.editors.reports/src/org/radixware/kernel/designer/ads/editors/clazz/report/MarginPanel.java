/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.math.BigDecimal;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginMm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.utils.AdsReportMarginTxt;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;

/**
 *
 * @author avoloshchuk
 */
public class MarginPanel extends javax.swing.JPanel {
    private final static BigDecimal A0_MAX_SIZE = BigDecimal.valueOf(1189);//A0 - 841 x 1189mm
    
    private final BigDecimalSpinnerModel topModel = new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, A0_MAX_SIZE, BigDecimal.ONE);
    private final BigDecimalSpinnerModel leftModel = new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, A0_MAX_SIZE, BigDecimal.ONE);
    private final BigDecimalSpinnerModel bottomModel = new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, A0_MAX_SIZE, BigDecimal.ONE);
    private final BigDecimalSpinnerModel rightModel = new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, A0_MAX_SIZE, BigDecimal.ONE);
   
    private volatile boolean updating = false;
    private AdsReportMarginMm marginMm;
    private AdsReportMarginTxt marginTxt;
    
    
    /**
     * Creates new form MarginPanel
     */
    public MarginPanel() {
        initComponents();
        
        topSpinner.setModel(topModel);
        bottomSpinner.setModel(bottomModel);
        rightSpinner.setModel(rightModel);
        leftSpinner.setModel(leftModel);
        topSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(topSpinner));
        bottomSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(bottomSpinner));
        rightSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(rightSpinner));
        leftSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(leftSpinner));
    }

    public void open(final AdsReportMarginMm marginMm){
        updating = true;
        
        this.marginTxt = null;
        this.marginMm = marginMm;
        
        topModel.setMaximum(A0_MAX_SIZE);
        leftModel.setMaximum(A0_MAX_SIZE);
        bottomModel.setMaximum(A0_MAX_SIZE);
        rightModel.setMaximum(A0_MAX_SIZE);
        
        updateLabels();
        
        topSpinner.setValue(BigDecimal.valueOf(marginMm.getTopMm()));
        bottomSpinner.setValue(BigDecimal.valueOf(marginMm.getBottomMm()));
        rightSpinner.setValue(BigDecimal.valueOf(marginMm.getRightMm()));
        leftSpinner.setValue(BigDecimal.valueOf(marginMm.getLeftMm()));
        
        updating = false;
    }
    
    public void open(final AdsReportMarginTxt marginTxt){
        updating = true;
        
        this.marginTxt = marginTxt;
        this.marginMm = null;
        
        topModel.setMaximum(BigDecimal.valueOf(Integer.MAX_VALUE));
        leftModel.setMaximum(BigDecimal.valueOf(Integer.MAX_VALUE));
        bottomModel.setMaximum(BigDecimal.valueOf(Integer.MAX_VALUE));
        rightModel.setMaximum(BigDecimal.valueOf(Integer.MAX_VALUE));
        
        updateLabels();
        
        topSpinner.setValue(BigDecimal.valueOf(marginTxt.getTopRows()));
        bottomSpinner.setValue(BigDecimal.valueOf(marginTxt.getBottomRows()));
        rightSpinner.setValue(BigDecimal.valueOf(marginTxt.getRightCols()));
        leftSpinner.setValue(BigDecimal.valueOf(marginTxt.getLeftCols()));
        
        topSpinner.setEnabled(false);
        bottomSpinner.setEnabled(false);
        rightSpinner.setEnabled(false);
        leftSpinner.setEnabled(false);
        updating = false;
    }
    
    private void updateLabels(){
        if (marginTxt != null){
            topLabel.setText(topLabel.getText().replace("(mm.)", "(rows)"));
            bottomLabel.setText(bottomLabel.getText().replace("(mm.)", "(rows)"));
            leftLabel.setText(leftLabel.getText().replace("(mm.)", "(cols)"));
            rightLabel.setText(rightLabel.getText().replace("(mm.)", "(cols)"));
        }
        if (marginMm != null){
            topLabel.setText(topLabel.getText().replace("(rows)", "(mm.)"));
            bottomLabel.setText(bottomLabel.getText().replace("(rows)", "(mm.)"));
            leftLabel.setText(leftLabel.getText().replace("(cols)", "(mm.)"));
            rightLabel.setText(rightLabel.getText().replace("(cols)", "(mm.)"));
        }
    }
    
    public void addTopSpinnerChangeListener(ChangeListener listener) {
        topSpinner.addChangeListener(listener);
    }
    
    public void addLeftSpinnerChangeListener(ChangeListener listener) {
        leftSpinner.addChangeListener(listener);
    }
    
    public void addBottomSpinnerChangeListener(ChangeListener listener) {
        bottomSpinner.addChangeListener(listener);
    }
    
    public void addRightSpinnerChangeListener(ChangeListener listener) {
        rightSpinner.addChangeListener(listener);
    }
    
    public BigDecimal getTopValue(){
        return topModel.getNumber();
    }
    
    public BigDecimal getLeftValue(){
        return leftModel.getNumber();
    }
    
    public BigDecimal getBottomValue(){
        return bottomModel.getNumber();
    }
    
    public BigDecimal getRightValue(){
        return rightModel.getNumber();
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

        middlePanel = new javax.swing.JPanel();
        topSpinner = new javax.swing.JSpinner();
        topLabel = new javax.swing.JLabel();
        bottomLabel = new javax.swing.JLabel();
        leftSpinner = new javax.swing.JSpinner();
        leftLabel = new javax.swing.JLabel();
        rightLabel = new javax.swing.JLabel();
        rightSpinner = new javax.swing.JSpinner();
        iconPanel = new javax.swing.JPanel();
        bottomSpinner = new javax.swing.JSpinner();

        setLayout(new java.awt.GridBagLayout());

        topSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        topSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                topSpinnerStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(topLabel, org.openide.util.NbBundle.getMessage(MarginPanel.class, "MarginPanel.topLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(bottomLabel, org.openide.util.NbBundle.getMessage(MarginPanel.class, "MarginPanel.bottomLabel.text")); // NOI18N

        leftSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        leftSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftSpinnerStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(leftLabel, org.openide.util.NbBundle.getMessage(MarginPanel.class, "MarginPanel.leftLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(rightLabel, org.openide.util.NbBundle.getMessage(MarginPanel.class, "MarginPanel.rightLabel.text")); // NOI18N

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
                    .addComponent(bottomLabel)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addComponent(leftLabel)
                        .addGap(6, 6, 6)
                        .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(topLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(rightLabel)
                        .addGap(6, 6, 6)
                        .addComponent(rightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        middlePanelLayout.setVerticalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanelLayout.createSequentialGroup()
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(topLabel))
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(leftLabel)
                            .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rightLabel)
                            .addComponent(rightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(middlePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bottomSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bottomLabel)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(middlePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

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

    private void rightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_rightSpinnerStateChanged

    private void bottomSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bottomSpinnerStateChanged
        if (!updating) {
            apply();
        }
    }//GEN-LAST:event_bottomSpinnerStateChanged

    private void apply() {
        if (marginMm != null){
            marginMm.setTopMm(((BigDecimal) topSpinner.getValue()).doubleValue());
            marginMm.setBottomMm(((BigDecimal) bottomSpinner.getValue()).doubleValue());
            marginMm.setLeftMm(((BigDecimal) leftSpinner.getValue()).doubleValue());
            marginMm.setRightMm(((BigDecimal) rightSpinner.getValue()).doubleValue());
        }
        if (marginTxt != null){
            marginTxt.setTopRows(((BigDecimal) topSpinner.getValue()).intValue());
            marginTxt.setBottomRows(((BigDecimal) bottomSpinner.getValue()).intValue());
            marginTxt.setLeftCols(((BigDecimal) leftSpinner.getValue()).intValue());
            marginTxt.setRightCols(((BigDecimal) rightSpinner.getValue()).intValue());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (marginMm != null){
            topSpinner.setEnabled(enabled);
            bottomSpinner.setEnabled(enabled);
            rightSpinner.setEnabled(enabled);
            leftSpinner.setEnabled(enabled);
        }
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bottomLabel;
    private javax.swing.JSpinner bottomSpinner;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JLabel leftLabel;
    private javax.swing.JSpinner leftSpinner;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JLabel rightLabel;
    private javax.swing.JSpinner rightSpinner;
    private javax.swing.JLabel topLabel;
    private javax.swing.JSpinner topSpinner;
    // End of variables declaration//GEN-END:variables
}
