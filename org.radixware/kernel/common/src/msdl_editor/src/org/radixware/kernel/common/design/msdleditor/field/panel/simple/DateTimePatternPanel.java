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
 * DateTimePatternPanel.java
 *
 * Created on 17.02.2009, 15:34:32
 */

package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;


public class DateTimePatternPanel extends AbstractEditItem {

    boolean enabled = true;
    ActionListener l;
    String parentValue;
    ExtendableTextField.ExtendableTextChangeListener tl1;

    /** Creates new form DateTimePatternPanel */
    public DateTimePatternPanel() {
        initComponents();
        JButton button = extendableTextField1.addButton("...");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frm = (Frame) Window.getOwnerlessWindows()[0];
                DateTimePatternDialog d = new DateTimePatternDialog(frm,true,(String)extendableTextField1.getValue(),enabled);
                d.pack();
                d.setLocationRelativeTo(null);
                d.setVisible(true);
                if (d.result != null) {
                    extendableTextField1.removeChangeListener(tl1);
                    extendableTextField1.setValue(d.result);
                    extendableTextField1.addChangeListener(tl1);
                    l.actionPerformed(null);
                }
                d.dispose();

            }
        });
    }

    public void addActionListener(final ActionListener al) {
        l = al;
        ExtendableTextField.ExtendableTextChangeListener tl = new ExtendableTextField.ExtendableTextChangeListener() {

            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                al.actionPerformed(null);
            }
        };
        tl1 = tl;
        extendableTextField1.addChangeListener(tl);
    }

    public void removeActionListener(final ActionListener l) {
    }

    public SetParentPanel getSetParentPanel() {
        return setParentPanel;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        extendableTextField1.setEnabled(enabled);
        this.enabled = enabled;
    }


    public void setPattern(String value, String parentValue) {
        this.parentValue = parentValue;
        setParentPanel.setSelected(false);
        if (value == null)
            extendableTextField1.setValue("");
        else
            extendableTextField1.setValue(value);
        getSetParentPanel().setEnabled(parentValue != null);
        if (value == null) {
            setParentPanel.setSelected(parentValue != null);
            if (parentValue == null)
                extendableTextField1.setValue("");
            else
                extendableTextField1.setValue(parentValue);
            setEnabled(parentValue == null);
        }
    }

    public String getString() {
        extendableTextField1.removeChangeListener(tl1);
        if (setParentPanel.getSelected()) {
            extendableTextField1.setValue(parentValue);
            setEnabled(false);
            extendableTextField1.addChangeListener(tl1);
            return null;
        }
        else {
            setEnabled(true);
            String result = (String)extendableTextField1.getValue();
            if (result.equals(""))
                result = null;
            if (result == null && parentValue != null) {
                extendableTextField1.setValue(parentValue);
                setEnabled(false);
                setParentPanel.setSelected(true);
            }
            extendableTextField1.addChangeListener(tl1);
            return result;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        extendableTextField1 = new org.radixware.kernel.common.components.ExtendableTextField();
        setParentPanel = new org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(extendableTextField1);
        add(setParentPanel);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField extendableTextField1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.simple.SetParentPanel setParentPanel;
    // End of variables declaration//GEN-END:variables

}
