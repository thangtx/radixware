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
package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.enums.EEncoding;

public class ExtHexOrCharsPanel extends AbstractEditItem implements ActionListener {

    private byte[] parentValueBytes;
    private String parentValueChars;
    private ActionListener al;
    private HexOrCharsPanel.IHexOrCharsPanelHelper helper;

    public ExtHexOrCharsPanel() {
        initComponents();
        setParentPanel1.setEnabled(false);
        setParentPanel1.addActionListener(this);
        hexPanel1.addActionListener(this);
    }

    public void setHelper(HexOrCharsPanel.IHexOrCharsPanelHelper helper) {
        this.helper = helper;
        hexPanel1.setHelper(helper);
    }

    public void addActionListener(final ActionListener l) {
        al = l;
    }

    public void removeActionListener(final ActionListener l) {
        al = null;
    }

    @Override
    public void setEnabled(boolean state) {
        hexPanel1.setEnabled(state);
        setParentPanel1.setEnabled(state);
    }

    private boolean parentValueBytesExists() {
        return parentValueBytes != null && parentValueBytes.length > 0;
    }

    private boolean parentValueCharsExists() {
        return parentValueChars != null && !parentValueChars.isEmpty();
    }

    public void setValue(byte[] value, byte[] parentValue, String parentValueChars) {
        this.parentValueBytes = parentValue;
        this.parentValueChars = parentValueChars;
        setParentPanel1.setSelected(false);
        setParentPanel1.setEnabled(parentValueBytesExists());
        if (value == null || value.length == 0) {
            setParentPanel1.setSelected(parentValueBytesExists());
            hexPanel1.setValue(parentValue);
            hexPanel1.setEnabled(!parentValueBytesExists());
        } else {
            hexPanel1.setValue(value);
        }
    }

    public void setValue(String value, String parentValue, byte[] parentValueBytes) {
        this.parentValueChars = parentValue;
        this.parentValueBytes = parentValueBytes;
        setParentPanel1.setSelected(false);
        setParentPanel1.setEnabled(parentValueCharsExists());
        if (value == null || value.isEmpty()) {
            setParentPanel1.setSelected(parentValueCharsExists());
            hexPanel1.setValue(parentValue);
            hexPanel1.setEnabled(!parentValueCharsExists());
        } else {
            hexPanel1.setValue(value);
        }
    }
    
    void onUnitChanged() {
        boolean isSetParentValue = false;
        if (setParentPanel1.getSelected()) {
            if (helper.isUnitChar()) {
                if (parentValueCharsExists()) {
                    isSetParentValue = true;
                    hexPanel1.setValue(parentValueChars);
                }
            } else {
                if (parentValueBytesExists()) {
                    isSetParentValue = true;
                    hexPanel1.setValue(parentValueBytes);
                }
            }
            if (!isSetParentValue) {
                setParentPanel1.setSelected(false);
                setParentPanel1.setEnabled(false);
                hexPanel1.setEnabled(true);
            }
        } else {
            setParentPanel1.setEnabled(helper.isUnitChar() ? parentValueCharsExists() : parentValueBytesExists());
        }

        hexPanel1.onUnitChanged(!isSetParentValue);
    }

    public byte[] getValue() {
        if (setParentPanel1.getSelected()) {
            return null;
        } else {
            return hexPanel1.getValue();
        }
    }

    public String getValueChars() {
        if (setParentPanel1.getSelected()) {
            return null;
        } else {
            return hexPanel1.getValueChars();
        }
    }

    public EEncoding getViewEncoding() {
        if (setParentPanel1.getSelected()) {
            return null;
        } else {
            return hexPanel1.getViewEncoding();
        }
    }

    public String getViewEncodingAsStr() {
        if (setParentPanel1.getSelected()) {
            return null;
        } else {
            return hexPanel1.getViewEncoding().getValue();
        }
    }

    public SetParentPanel getSetParentPanel() {
        return setParentPanel1;
    }

    public void setLimit(int limit) {
        hexPanel1.setLimit(limit);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hexPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexOrCharsPanel();
        setParentPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel();

        setPreferredSize(new java.awt.Dimension(100, 24));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
        add(hexPanel1);
        add(setParentPanel1);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.HexOrCharsPanel hexPanel1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel setParentPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            if (setParentPanel1.getSelected()) {
                if (helper.isUnitChar()) {
                    hexPanel1.setValue(parentValueChars);
                } else {
                    hexPanel1.setValue(parentValueBytes);
                }
                hexPanel1.setEnabled(false);
            } else {
                hexPanel1.setEnabled(true);
            }
        }
        if (al != null) {
            al.actionPerformed(new ActionEvent(this, 0, null));
        }
    }
}
