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
 * HexPanel.java
 *
 * Created on 22.01.2009, 14:03:40
 */
package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.utils.Hex;
import javax.swing.text.PlainDocument;


public class HexPanel extends AbstractEditItem {

    private EEncoding viewType = EEncoding.HEX;
    private byte[] value = null;
    private CaretListener caretListener = null;
    private ActionListener listener;
    private HexInputVerifier verifier = new HexInputVerifier();
    private LengthRestrictedDocument doc = new LengthRestrictedDocument();

    /** Creates new form HexPanel */
    public HexPanel() {
        initComponents();
        encodingComboBox.addItem(EEncoding.HEX);
        encodingComboBox.addItem(EEncoding.ASCII);
        encodingComboBox.addItem(EEncoding.EBCDIC);
        //verifier.shouldYieldFocus(valueTextField);
        valueTextField.setInputVerifier(verifier);
        valueTextField.setDocument(doc);
    }

    @Override
    public void setEnabled(boolean state) {
        valueTextField.setEnabled(state);
        encodingComboBox.setEnabled(state);
    }

    public void setEditable(boolean state) {
        valueTextField.setEditable(state);
    }
    
    public void setValue(byte[] val, EEncoding viewType) {
        value = null;
        if (val != null) {
            value = new byte[val.length];
            for (int i=0; i<val.length; i++)
                value[i] = val[i];
        }
        if(viewType != null && viewType != this.viewType) {
            this.viewType = viewType;
            encodingComboBox.setSelectedItem(this.viewType);
        }
        showValue();
    }

    private void showValue() {
        valueTextField.removeCaretListener(caretListener);
        if (value == null || value.length == 0) {
            valueTextField.setText("");
        } else {
            switch (viewType) {
                case ASCII:
                    try {
                        valueTextField.setText(new String(value, "US-ASCII"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HexPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case HEX:
                    valueTextField.setText(Hex.encode(value));
                    break;
                case EBCDIC:
                    try {
                        valueTextField.setText(new String(value, "IBM500"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HexPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
        valueTextField.addCaretListener(caretListener);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        super.setReadOnly(isReadOnly);
        encodingComboBox.setEnabled(true);
    }

    public byte[] getValue() {
        byte[] res = null;
        if (value != null) {
            res = new byte[value.length];
            for (int i=0; i<value.length; i++)
                res[i] = value[i];
        }
        return res;
    }

    private byte[] readContent() throws UnsupportedEncodingException {
        String txt = valueTextField.getText();
        if (txt.equals(""))
            return null;

        byte[] result = null;
        switch (viewType) {
            case ASCII:
                result = valueTextField.getText().getBytes("US-ASCII");
                break;
            case HEX:                
                result = Hex.decode(txt);
                break;
            case EBCDIC:
                result = valueTextField.getText().getBytes("IBM500");
                break;
        }
        return result;
    }

    public void addActionListener(final ActionListener l) {
        listener = l;
        caretListener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        byte[] newValue;
                        try {
                            newValue = readContent();
                        } catch (Exception ex) {
                            return;
                        }
                        if (!isEqual(newValue, value)) {
                            value = newValue;
                            l.actionPerformed(new ActionEvent(this, 0, ""));
                        }
                    }
                });
            }
        };
        valueTextField.addCaretListener(caretListener);
    }
    
    private boolean isEqual(byte[] value1, byte[] value2) {
        int len1 = 0;
        if (value1 != null && value1.length > 0)
            len1 = value1.length;
        int len2 = 0;
        if (value2 != null && value2.length > 0)
            len2 = value2.length;
        if (len1 != len2)
            return false;
        for (int i=0; i<len1; i++)
            if (value1[i]!=value2[i])
                return false;
        return true;
    }
    
    public void setLimit(int limit) {
        doc.setLimit(limit);
    }
    
    public int getLimit() {
        return doc.getLimit();
    }
    
   public EEncoding getViewEncoding() {
       return viewType;
   }
   
   private boolean changedToHex(EEncoding oldEncoding, EEncoding newEncoding) {
       return oldEncoding != EEncoding.HEX && newEncoding == EEncoding.HEX;
   }
   
   private boolean changedFromHex(EEncoding oldEncoding, EEncoding newEncoding) {
       return oldEncoding == EEncoding.HEX && newEncoding != EEncoding.HEX;
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valueTextField = new javax.swing.JFormattedTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        encodingComboBox = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(valueTextField);
        add(filler2);

        encodingComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodingComboBoxActionPerformed(evt);
            }
        });
        add(encodingComboBox);
    }// </editor-fold>//GEN-END:initComponents

    private void encodingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodingComboBoxActionPerformed

        EEncoding selectedEncoding = (EEncoding) encodingComboBox.getSelectedItem();
        if (doc.getLimit() > 0) {
            if (changedToHex(viewType, selectedEncoding)) {
                doc.setLimit(doc.getLimit() * 2);
            } else if (changedFromHex(viewType, selectedEncoding)) {
                doc.setLimit(doc.getLimit() / 2);
            }
        }

        viewType = selectedEncoding;
        if (viewType == EEncoding.HEX) {
            valueTextField.setInputVerifier(verifier);
        } else {
            valueTextField.setInputVerifier(null);
        }

        showValue();
        
        if(listener != null) {
            listener.actionPerformed(new ActionEvent(this, 0, ""));
        }
    }//GEN-LAST:event_encodingComboBoxActionPerformed

    private void jTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKeyPressed
    }//GEN-LAST:event_jTextFieldKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox encodingComboBox;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JFormattedTextField valueTextField;
    // End of variables declaration//GEN-END:variables
}


//stolen from StackOverflow and rewritten a bit
final class LengthRestrictedDocument extends PlainDocument {

    int limit;

    public LengthRestrictedDocument(int limit) {
        this.limit = limit;
    }
    
    public LengthRestrictedDocument() {
        this.limit = -1;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }

        if (limit < 0 || (getLength() + str.length()) <= limit) {
            super.insertString(offs, str, a);
        }
    }
}
