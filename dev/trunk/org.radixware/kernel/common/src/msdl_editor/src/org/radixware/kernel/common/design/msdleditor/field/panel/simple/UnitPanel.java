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
 * UnitPanel.java
 *
 * Created on 22.01.2009, 11:21:49
 */
package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.BCHFieldModel;

public class UnitPanel extends AbstractEditItem {

    private EUnit parentValue;
    private final EnumSet<EUnit> allowedUnits;
    private static final EnumSet<EUnit> ALL_UNITS = 
                EnumSet.of(EUnit.NOTDEFINED, EUnit.CHAR, EUnit.BYTE, EUnit.ELEMENT);

    public static class UnitModel extends DefaultComboBoxModel {

        public UnitModel(EUnit[] types) {
            super(types);
        }
    }
    
    public UnitPanel(boolean isElementAllowed) {
        this(isElementAllowed, ALL_UNITS);
    }
    
    public UnitPanel(boolean isElementAllowed, EnumSet<EUnit> selectableUnits) {
        initComponents();
        allowedUnits = EnumSet.of(EUnit.NOTDEFINED, EUnit.CHAR, EUnit.BYTE);
        if (isElementAllowed) {
            allowedUnits.add(EUnit.ELEMENT);
        }
        jComboBoxUnit.setModel(new UnitModel(selectableUnits.toArray(new EUnit[selectableUnits.size()])));
        jComboBoxUnit.setRenderer(new UnitListCellRenderer());
        jComboBoxUnit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    changeSelectedItemColor((EUnit) e.getItem());
                }
            }
        });
    }

    public UnitPanel() {
        this(true);
    }
    
    public void setElementAllowed(AbstractFieldModel field) {
        final boolean isElementAllowed = field instanceof BCHFieldModel
                || EEncoding.getInstance(field.calcEncoding(true)) == EEncoding.BCD;
        setElementAllowed(isElementAllowed);
    }

    public void setElementAllowed(boolean isElementAllowed) {
        if (isElementAllowed) {
            allowedUnits.add(EUnit.ELEMENT);
        } else {
            allowedUnits.remove(EUnit.ELEMENT);
        }
        changeSelectedItemColor((EUnit) jComboBoxUnit.getModel().getSelectedItem());
    }

    public void addActionListener(final ActionListener l) {
        jComboBoxUnit.addActionListener(l);
    }

    public void removeActionListener(final ActionListener l) {
        jComboBoxUnit.removeActionListener(l);
    }

    public void setUnitModel(ComboBoxModel model) {
        jComboBoxUnit.setModel(model);
    }

    public void setUnit(EUnit value, EUnit parentValue) {
        this.parentValue = parentValue;
        getSetParentPanel().setSelected(false);
        getSetParentPanel().setEnabled(parentValue != EUnit.NOTDEFINED);
        EUnit val = value;
        if (value == EUnit.NOTDEFINED) {
            val = parentValue;
            getSetParentPanel().setSelected(parentValue != EUnit.NOTDEFINED);
            jComboBoxUnit.setEnabled(parentValue == EUnit.NOTDEFINED);
        }
        
        jComboBoxUnit.getModel().setSelectedItem(val);
    }
    
    private void changeSelectedItemColor(EUnit val) {
        if (!allowedUnits.contains(val)) {
            jComboBoxUnit.setForeground(Color.red);
        } else {
            jComboBoxUnit.setForeground(Color.black);
        }
    }

    public EUnit getUnit() {
        if (getSetParentPanel().getSelected()) {
            jComboBoxUnit.getModel().setSelectedItem(parentValue);
            jComboBoxUnit.setEnabled(false);
            return EUnit.NOTDEFINED;
        } else {
            jComboBoxUnit.setEnabled(true);
            EUnit result = (EUnit) jComboBoxUnit.getModel().getSelectedItem();
            if (result == EUnit.NOTDEFINED && parentValue != EUnit.NOTDEFINED) {
                jComboBoxUnit.getModel().setSelectedItem(parentValue);
                jComboBoxUnit.setEnabled(false);
                getSetParentPanel().setSelected(true);
            }
            return result;
        }
    }

    public EUnit getViewedUnit() {
        return (EUnit) jComboBoxUnit.getModel().getSelectedItem();
    }

    public SetParentPanel getSetParentPanel() {
        return setParentPanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxUnit = new javax.swing.JComboBox();
        setParentPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel();

        setPreferredSize(new java.awt.Dimension(100, 24));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(jComboBoxUnit);
        add(setParentPanel);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxUnit;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel setParentPanel;
    // End of variables declaration//GEN-END:variables

    class UnitListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (!allowedUnits.contains((EUnit) value)) {
                c.setForeground(Color.red);
            }
            return c;
        }
    }

}
