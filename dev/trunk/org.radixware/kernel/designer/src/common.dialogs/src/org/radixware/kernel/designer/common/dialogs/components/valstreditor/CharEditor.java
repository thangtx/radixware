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

 
class CharEditor extends ValueEditor<Character> {

    private class NotNullCharEditor extends NotNullEditor<Character> {

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
                    int keyCode = ev.getKeyCode();
                    if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE
                            || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
                            || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
                            || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
                        super.processKeyEvent(ev);
                    } else if (keyCode == KeyEvent.VK_SPACE && ev.getModifiers() == KeyEvent.CTRL_MASK) {
                        super.processKeyEvent(ev);
                    } else if (getText().length() >= 1 && getSelectedText() == null) {
                        ev.consume();
                    } else {
                        super.processKeyEvent(ev);
                    }
                }
            }
        };

        public NotNullCharEditor(CharEditor editor) {
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
        public void setValue(Character value) {
            if (value != null) {
                if (!value.equals(getValue())) {
                    textField.setText(String.valueOf(value));
                  //  textField.selectAll();
                }
                fireValueChanged();
            }
        }

        @Override
        public Character getValue() {
            String text = textField.getText();
            if(text.isEmpty())
                return Character.MIN_VALUE;
            return text.charAt(0);
        }

        @Override
        public JComponent getEditor() {
            return textField;
        }
    }
    private final NotNullCharEditor notNullCharEditor = new NotNullCharEditor(this);
    private final NullEditor nullEditor = new NullEditor(this) {
        @Override
        protected boolean isValidChar(char c) {
            return Character.isLetterOrDigit(c);
        }

        @Override
        protected Object processString(String str) {
            return str.isEmpty()?null:str.charAt(0);
        }
    };

    public CharEditor(ValAsStrEditor editor) {
        super(editor);
        registerSwitchToNullValueHotKey();
    }

    @Override
    public void setDefaultValue() {
        notNullCharEditor.setValue('a');
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullCharEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return nullEditor;
    }
}
