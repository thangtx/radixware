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

package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


class StrEditor extends ValueEditor<String> {

    private class NotNullStrEditor extends NotNullEditor<String> {

        private final JTextField textField = new JTextField() {
            private volatile boolean locked = false;

            @Override
            protected void processKeyEvent(KeyEvent ev) {
                if (locked) {
                    ev.consume();
                    return;
                }
                char keyChar = ev.getKeyChar();
                int st = this.getSelectionStart();
                int en = this.getSelectionEnd();
                if (Character.isLetterOrDigit(keyChar) && st == 0 && en == this.getText().length()) {
                    ev.consume();
                    locked = true;
                    this.setText(String.valueOf(keyChar));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            locked = false;
                        }
                    });
                } else {
                    super.processKeyEvent(ev);
                }
            }
        };

        public NotNullStrEditor(StrEditor editor) {
            super(editor);
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    fireValueChanged();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    fireValueChanged();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    fireValueChanged();
                }
            });
        }

        @Override
        public void setValue(String value) {
            if (value != null) {
                if (!value.equals(getValue())) {
                    textField.setText(value);
                 //   textField.selectAll();
                }
                fireValueChanged();
            }
        }

        @Override
        public String getValue() {
            return textField.getText();
        }

        @Override
        public JComponent getEditor() {
            return textField;
        }
    }
    private final NotNullStrEditor notNullStrEditor = new NotNullStrEditor(this);
    private final NullEditor nullEditor = new NullEditor(this) {
        @Override
        protected boolean isValidChar(char c) {
            return Character.isLetterOrDigit(c);
        }

        @Override
        protected String processString(String str) {
            return str;
        }
    };

    public StrEditor(ValAsStrEditor editor) {
        super(editor);
        registerSwitchToNullValueHotKey();
    }

    @Override
    public void setDefaultValue() {
        notNullStrEditor.setValue("");
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullStrEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return nullEditor;
    }
}
