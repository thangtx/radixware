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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.msdl.MsdlSettings;
import org.radixware.kernel.common.msdl.enums.EViewTypeGroup;

public class HexOrCharsPanel extends AbstractEditItem {

    private byte[] valueBytes = null;
    private String valueChars = null;

    private EEncoding viewType = EEncoding.HEX;
    private final EViewTypeGroup viewTypeGroup;
    private IHexOrCharsPanelHelper helper;

    private DocumentListener docListener = null;
    private ActionListener encodingListener = null;

    private final HexInputVerifier verifier = new HexInputVerifier();
    private final LengthRestrictedDocument doc = new LengthRestrictedDocument();
    private final ComboBoxModel bytesModel = new DefaultComboBoxModel(new EEncoding[]{EEncoding.HEX, EEncoding.ASCII, EEncoding.EBCDIC});
    private final ComboBoxModel charsModel = new DefaultComboBoxModel(new String[]{"CHAR"});

    public HexOrCharsPanel() {
        this(EViewTypeGroup.OTHER);
    }

    public HexOrCharsPanel(EViewTypeGroup viewGroup) {
        initComponents();
        this.viewTypeGroup = viewGroup;

        encodingComboBox.setModel(bytesModel);
        encodingComboBox.setSelectedItem(EEncoding.HEX);
        valueTextField.setInputVerifier(verifier);
        valueTextField.setDocument(doc);

        encodingListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EEncoding newEncoding = (EEncoding) encodingComboBox.getSelectedItem();
                onEncodingValueChanged(newEncoding);
                MsdlSettings.getInstance().set(viewTypeGroup.getKey(), newEncoding.name());
            }
        };
        encodingComboBox.addActionListener(encodingListener);
    }

    void setHelper(IHexOrCharsPanelHelper helper) {
        this.helper = helper;
    }

    @Override
    public void setEnabled(boolean state) {
        valueTextField.setEnabled(state);
        encodingComboBox.setEnabled(state && !helper.isUnitChar());
    }

    void onUnitChanged(boolean doConversion) {
        if (doConversion) {
            if (helper.isUnitChar()) {
                valueChars = helper.toChars(valueBytes);
            } else {
                valueBytes = helper.toBytes(valueChars);
            }
        }
        restoreViewType();
    }

    private EEncoding getViewTypeForChar() {
        return EEncoding.NONE;
    }

    private EEncoding getLastUsedViewType() {
        String encoding = MsdlSettings.getInstance().get(viewTypeGroup.getKey());
        return encoding != null ? EEncoding.valueOf(encoding) : null;
    }

    public void setValue(byte[] val) {
        setValue(val, null);
    }

    public void setValue(String val) {
        setValue(null, val);
    }

    private void setValue(byte[] bytes, String chars) {
        if (helper.isUnitChar()) {
            valueChars = chars;
        } else {
            valueBytes = bytes == null ? null : Arrays.copyOf(bytes, bytes.length);
        }
        restoreViewType();
    }

    private void restoreViewType() {
        encodingComboBox.removeActionListener(encodingListener);
        if (helper.isUnitChar()) {
            encodingComboBox.setModel(charsModel);
            encodingComboBox.setSelectedIndex(0);
            onEncodingValueChanged(getViewTypeForChar());
            encodingComboBox.setEnabled(false);
        } else {
            EEncoding lastUsed = getLastUsedViewType();
            lastUsed = lastUsed == null ? EEncoding.HEX : lastUsed;
            encodingComboBox.setModel(bytesModel);
            encodingComboBox.setSelectedItem(lastUsed);
            onEncodingValueChanged(lastUsed);
            encodingComboBox.setEnabled(true);
        }
        encodingComboBox.addActionListener(encodingListener);
    }

    private void showValue() {
        doc.removeDocumentListener(docListener);
        try {
            if (helper.isUnitChar()) {
                valueTextField.setText(valueChars);
            } else {
                showByteValue();
            }
        } finally {
            doc.addDocumentListener(docListener);
        }
    }

    private void showByteValue() {
        if (valueBytes == null || valueBytes.length == 0) {
            valueTextField.setText("");
        } else {
            switch (viewType) {
                case ASCII:
                    try {
                        valueTextField.setText(new String(valueBytes, "US-ASCII"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HexOrCharsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case HEX:
                    valueTextField.setText(Hex.encode(valueBytes));
                    break;
                case EBCDIC:
                    try {
                        valueTextField.setText(new String(valueBytes, "IBM500"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HexOrCharsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        super.setReadOnly(isReadOnly);
        encodingComboBox.setEnabled(true);
    }

    public byte[] getValue() {
        return valueBytes == null ? null : Arrays.copyOf(valueBytes, valueBytes.length);
    }

    public String getValueChars() {
        return valueChars;
    }

    private byte[] readContent() throws UnsupportedEncodingException {
        String txt = valueTextField.getText();
        if (txt.equals("")) {
            return null;
        }

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

    private String readCharsContent() {
        String txt = valueTextField.getText();
        return txt.equals("") ? null : txt;
    }

    private void onDocumentUpdate(final ActionListener l) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (helper.isUnitChar()) {
                    String newValue = readCharsContent();
                    if (!Objects.equals(newValue, valueChars)) {
                        valueChars = newValue;
                        l.actionPerformed(new ActionEvent(this, 0, ""));
                    }
                } else {
                    byte[] newValue;
                    try {
                        newValue = readContent();
                    } catch (Exception ex) {
                        return;
                    }
                    if (!isEqual(newValue, valueBytes)) {
                        valueBytes = newValue;
                        l.actionPerformed(new ActionEvent(this, 0, ""));
                    }
                }
            }
        });
    }

    public void addActionListener(final ActionListener l) {
        if (docListener != null) {
            throw new IllegalStateException("Listener set more than once");
        }
        docListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                onDocumentUpdate(l);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onDocumentUpdate(l);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onDocumentUpdate(l);
            }
        };
        doc.addDocumentListener(docListener);
    }

    private boolean isEqual(byte[] value1, byte[] value2) {
        int len1 = 0;
        if (value1 != null && value1.length > 0) {
            len1 = value1.length;
        }
        int len2 = 0;
        if (value2 != null && value2.length > 0) {
            len2 = value2.length;
        }
        if (len1 != len2) {
            return false;
        }
        for (int i = 0; i < len1; i++) {
            if (value1[i] != value2[i]) {
                return false;
            }
        }
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

    private void onEncodingValueChanged(EEncoding newEncoding) {
        if (doc.getLimit() > 0) {
            if (changedToHex(viewType, newEncoding)) {
                doc.setLimit(doc.getLimit() * 2);
            } else if (changedFromHex(viewType, newEncoding)) {
                doc.setLimit(doc.getLimit() / 2);
            }
        }

        viewType = newEncoding;
        if (viewType == EEncoding.HEX) {
            valueTextField.setInputVerifier(verifier);
        } else {
            valueTextField.setInputVerifier(null);
        }

        showValue();
    }
    
    interface IHexOrCharsPanelHelper {

        boolean isUnitChar();

        byte[] toBytes(String str);

        String toChars(byte[] bytes);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valueTextField = new TextFieldForHandleSpace();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        encodingComboBox = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(valueTextField);
        add(filler2);

        add(encodingComboBox);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKeyPressed
    }//GEN-LAST:event_jTextFieldKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox encodingComboBox;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JTextField valueTextField;
    // End of variables declaration//GEN-END:variables
}
