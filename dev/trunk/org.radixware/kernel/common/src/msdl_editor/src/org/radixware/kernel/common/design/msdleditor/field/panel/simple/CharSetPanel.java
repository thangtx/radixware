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
 * CharSetPanel.java
 *
 * Created on 22.01.2009, 11:21:49
 */

package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.enums.EStrCharSet;


public class CharSetPanel extends AbstractEditItem {

    private EStrCharSet parentValue;
    public static class CharSetModel extends DefaultComboBoxModel {
        public CharSetModel() {
            super(new EStrCharSet[] {EStrCharSet.None, EStrCharSet.Any, EStrCharSet.XML, EStrCharSet.User });
        }
        
        public CharSetModel(EStrCharSet availableALignments[]) {
            super(availableALignments);
        }
    }

    /** Creates new form AlignPanel */
    public CharSetPanel() {
        initComponents();
        setCharSetModel(new CharSetModel());
    }
    
    public CharSetPanel(EStrCharSet availableALignments[]) {
        initComponents();
        setCharSetModel(new CharSetModel(availableALignments));
    }

    public void addActionListener(final ActionListener lis) {
        jComboBoxCharSet.addActionListener(lis);
    }

    public void removeActionListener(final ActionListener lis) {
        jComboBoxCharSet.removeActionListener(lis);
    }

    public void setCharSetModel(ComboBoxModel model) {
        jComboBoxCharSet.setModel(model);
//        jComboBoxCharSet.setVisible(true);
    }

    public void setCharSet(EStrCharSet value, EStrCharSet parentValue) {
        this.parentValue = parentValue;
        getSetParentPanel().setSelected(false);
        jComboBoxCharSet.getModel().setSelectedItem(value);
        getSetParentPanel().setEnabled(parentValue != EStrCharSet.None);
        if (value == EStrCharSet.None) {
            getSetParentPanel().setSelected(parentValue != EStrCharSet.None);
            jComboBoxCharSet.getModel().setSelectedItem(parentValue);
            jComboBoxCharSet.setEnabled(parentValue == EStrCharSet.None);
        }
    }

    public EStrCharSet getCharSet() {
        if (getSetParentPanel().getSelected()) {
            jComboBoxCharSet.getModel().setSelectedItem(parentValue);
            jComboBoxCharSet.setEnabled(false);
            return EStrCharSet.None;
        } else {
            jComboBoxCharSet.setEnabled(true);
            final EStrCharSet result = (EStrCharSet)jComboBoxCharSet.getModel().getSelectedItem();
            if (result == EStrCharSet.None && parentValue != EStrCharSet.None) {
                jComboBoxCharSet.getModel().setSelectedItem(parentValue);
                jComboBoxCharSet.setEnabled(false);
                getSetParentPanel().setSelected(true);
            }
            return result;
        }
    }

    public SetParentPanel getSetParentPanel() {
        return setParentPanel;
    }

    @Override
    public Dimension getMaximumSize() {
        return jComboBoxCharSet.getPreferredSize();
    }
    
    @Override
    public void setEnabled(boolean status) {
        super.setEnabled(status);
        setParentPanel.setEnabled(status);
        jComboBoxCharSet.setEnabled(status);
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

        jComboBoxCharSet = new javax.swing.JComboBox();
        setParentPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel();

        setPreferredSize(new java.awt.Dimension(800, 24));
        setLayout(new java.awt.GridBagLayout());

        jComboBoxCharSet.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        add(jComboBoxCharSet, gridBagConstraints);
        add(setParentPanel, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void setParentPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setParentPanelActionPerformed
        // do nothing
    }//GEN-LAST:event_setParentPanelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxCharSet;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel setParentPanel;
    // End of variables declaration//GEN-END:variables
}