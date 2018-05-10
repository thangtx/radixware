/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.event.ActionListener;
import org.radixware.kernel.common.msdl.enums.EEncoding;

/**
 *
 * @author npopov
 */
public class SeparatorPanel extends javax.swing.JPanel {

    public SeparatorPanel() {
        initComponents();
    }
    
    public byte[] getStartSeparator() {
        return startSeparatorHexPanel.getValue();
    }
    
    public byte[] getEndSeparator() {
        return endSeparatorHexPanel.getValue();
    }
    
    public EEncoding getViewEncoding() {
        return startSeparatorHexPanel.getViewEncoding();
    }
    
    public void setLimit(int lim) {
        startSeparatorHexPanel.setLimit(lim);
        endSeparatorHexPanel.setLimit(lim);
    }
    
    public void setValue(byte[] startSeparator, byte[] endSeparator) {
        startSeparatorHexPanel.setValue(startSeparator);
        endSeparatorHexPanel.setValue(endSeparator);
    }
    
    public void addActionListener(ActionListener listener) {
       startSeparatorHexPanel.addActionListener(listener);
       endSeparatorHexPanel.addActionListener(listener);
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

        startSeparatorLabel = new javax.swing.JLabel();
        startSeparatorHexPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel();
        endSeparatorLabel = new javax.swing.JLabel();
        endSeparatorHexPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel();

        setLayout(new java.awt.GridBagLayout());

        startSeparatorLabel.setText("Start Separator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(startSeparatorLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(startSeparatorHexPanel, gridBagConstraints);

        endSeparatorLabel.setText("End Separator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(endSeparatorLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(endSeparatorHexPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel endSeparatorHexPanel;
    private javax.swing.JLabel endSeparatorLabel;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexPanel startSeparatorHexPanel;
    private javax.swing.JLabel startSeparatorLabel;
    // End of variables declaration//GEN-END:variables
}