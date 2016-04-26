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
 * AlignPanel.java
 *
 * Created on 22.01.2009, 11:21:49
 */

package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.enums.EAlign;


public class AlignPanel extends AbstractEditItem {

    private EAlign parentValue;
    public static class AlignModel extends DefaultComboBoxModel {
        public AlignModel() {
            super(new EAlign[]{EAlign.NOTDEFINED,EAlign.NONE,EAlign.LEFT,EAlign.RIGHT});
        }
        
        public AlignModel(EAlign availableALignments[]) {
            super (availableALignments);
        }
    }

    /** Creates new form AlignPanel */
    public AlignPanel() {
        initComponents();
        setAlignModel(new AlignModel());
    }
    
    public AlignPanel(EAlign availableALignments[]) {
        initComponents();
        setAlignModel(new AlignModel(availableALignments));
    }

    public void addActionListener(final ActionListener l) {
        jComboBoxAlign.addActionListener(l);
    }

    public void removeActionListener(final ActionListener l) {
        jComboBoxAlign.removeActionListener(l);
    }


    public void setAlignModel(ComboBoxModel model) {
        jComboBoxAlign.setModel(model);
    }

    public void setAlign(EAlign value, EAlign parentValue) {
        this.parentValue = parentValue;
        getSetParentPanel().setSelected(false);
        jComboBoxAlign.getModel().setSelectedItem(value);
        getSetParentPanel().setEnabled(parentValue != EAlign.NOTDEFINED);
        if (value == EAlign.NOTDEFINED) {
            getSetParentPanel().setSelected(parentValue != EAlign.NOTDEFINED);
            jComboBoxAlign.getModel().setSelectedItem(parentValue);
            jComboBoxAlign.setEnabled(parentValue == EAlign.NOTDEFINED);
        }
    }

    public EAlign getAlign() {
        if (getSetParentPanel().getSelected()) {
            jComboBoxAlign.getModel().setSelectedItem(parentValue);
            jComboBoxAlign.setEnabled(false);
            return EAlign.NOTDEFINED;
        }
        else {
            jComboBoxAlign.setEnabled(true);
            EAlign result = (EAlign)jComboBoxAlign.getModel().getSelectedItem();
            if (result == EAlign.NOTDEFINED && parentValue != EAlign.NOTDEFINED) {
                jComboBoxAlign.getModel().setSelectedItem(parentValue);
                jComboBoxAlign.setEnabled(false);
                getSetParentPanel().setSelected(true);
            }
            return result;
        }
    }

    public EAlign getComboBoxAlign() {
        return (EAlign)jComboBoxAlign.getModel().getSelectedItem();
    }

    public SetParentPanel getSetParentPanel() {
        return setParentPanel;
    }

    @Override
    public Dimension getMaximumSize() {
        return jComboBoxAlign.getPreferredSize();
    }
    
    @Override
    public void setEnabled(boolean status) {
        super.setEnabled(status);
        setParentPanel.setEnabled(status);
        jComboBoxAlign.setEnabled(status);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxAlign = new javax.swing.JComboBox();
        setParentPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel();

        setPreferredSize(new java.awt.Dimension(800, 24));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jComboBoxAlign.setPreferredSize(new java.awt.Dimension(100, 24));
        add(jComboBoxAlign);
        add(setParentPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void setParentPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setParentPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_setParentPanelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxAlign;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel setParentPanel;
    // End of variables declaration//GEN-END:variables

}