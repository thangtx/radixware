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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXDatePicker;
import org.radixware.kernel.common.utils.Utils;


class DateEditor extends ValueEditor<Date> {

    private class NotNullDateEditor extends NotNullEditor<Date> {

        private final JXDatePicker datePicker = new JXDatePicker() {
            @Override
            public boolean requestFocusInWindow() {
                return this.getEditor().requestFocusInWindow();
            }
        };

        public NotNullDateEditor(ValueEditor editor) {
            super(editor);
            datePicker.setFormats(new String[]{
                //                        "dd MMMMM yyyy HH:mm:ss:SSS",
                //                        "dd MMMMM yyyy HH:mm:ss",
                "d MMMMM yyyy"
            });
            datePicker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireValueChanged();
                }
            });
            datePicker.getEditor().addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        datePicker.getParent().dispatchEvent(e);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    char keyChar = e.getKeyChar();
                    String str = datePicker.getEditor().getText().trim();
                    if (!(Character.isLetterOrDigit(keyChar) || keyCode == KeyEvent.VK_BACK_SPACE
                            || keyCode == KeyEvent.VK_DELETE) || str.startsWith("0 ")
                            || (str.length() > 4 && str.charAt(str.length() - 5) != ' ')) {
                        return;
                    }
                    try {
                        if (datePicker.isEditValid()) {
                            int pos = datePicker.getEditor().getCaretPosition();
                            datePicker.commitEdit();
                            int len = datePicker.getEditor().getText().length();
                            datePicker.getEditor().setCaretPosition(Math.min(pos, len));
                            fireValueChanged();
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            });
        }

        @Override
        public void setValue(Date value) {
            if (!Utils.equals(value, getValue())) {
                datePicker.setDate(value);
            }
            fireValueChanged();
        }

        @Override
        public Date getValue() {
            return datePicker.getDate();
        }

        @Override
        public JComponent getEditor() {
            return datePicker;
        }
    }
    private final NotNullDateEditor notNullDateEditor = new NotNullDateEditor(this);
    private final NullEditor nullEditor = new NullEditor(this) {
        @Override
        protected boolean isValidChar(char c) {
            return Character.isLetterOrDigit(c);
        }

        @Override
        protected Date processString(String str) {
            return new Date();
        }
    };

    public DateEditor(ValAsStrEditor editor) {
        super(editor);
        registerSwitchToNullValueHotKey();
    }

    @Override
    public void setDefaultValue() {
        notNullDateEditor.setValue(new Date());
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullDateEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return nullEditor;
    }
}
