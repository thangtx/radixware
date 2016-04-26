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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;


class BoolEditor extends ValueEditor<Boolean> {

    private static class RadixBoolean {

        public static final RadixBoolean TRUE = new RadixBoolean(Boolean.TRUE);
        public static final RadixBoolean FALSE = new RadixBoolean(Boolean.FALSE);
        public static final RadixBoolean NULL = new RadixBoolean(null);

        public static RadixBoolean valueOf(Boolean b) {
            return b == null ? NULL : (b.equals(Boolean.TRUE) ? TRUE : FALSE);
        }
        private final Boolean val;

        protected RadixBoolean(Boolean val) {
            this.val = val;
        }

        public Boolean getValue() {
            return val;
        }

        @Override
        public String toString() {
            return val == null ? "<Not Defined>" : val.toString();
        }
    }

    private class NotNullBoolEditor extends NotNullEditor<Boolean> {

        private final JComboBox comboBox = new JComboBox(new DefaultComboBoxModel(
                new RadixBoolean[]{RadixBoolean.NULL, RadixBoolean.TRUE, RadixBoolean.FALSE})) {
            @Override
            public boolean requestFocusInWindow() {
                if (this.isValid() && this.isShowing()) {
                    this.showPopup();
                }
                return super.requestFocusInWindow();
            }

            @Override
            public void processKeyEvent(KeyEvent ev) {
                int keyCode = ev.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
                    this.getParent().dispatchEvent(ev);
                    ev.consume();
                } else {
                    super.processKeyEvent(ev);
                }
            }
        };

        public NotNullBoolEditor(ValueEditor valEditor) {
            super(valEditor);
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    fireValueChanged();
                }
            });
        }

        @Override
        public void setValue(Boolean value) {
            comboBox.setSelectedItem(RadixBoolean.valueOf(value));
            fireValueChanged();
        }

        @Override
        public Boolean getValue() {
            return ((RadixBoolean) comboBox.getSelectedItem()).getValue();
        }

        @Override
        public JComponent getEditor() {
            return comboBox;
        }

        public void setNullAble(boolean nullAble) {
            if (nullAble && comboBox.getItemCount() == 2) {
                comboBox.insertItemAt(RadixBoolean.NULL, 0);
            } else if (!nullAble && comboBox.getItemCount() == 3) {
                comboBox.removeItem(RadixBoolean.NULL);
            }
        }
    }
    private final NotNullBoolEditor notNullBoolEditor = new NotNullBoolEditor(this);

    public BoolEditor(ValAsStrEditor editor) {
        super(editor);
    }

    @Override
    public void setDefaultValue() {
        notNullBoolEditor.setValue(Boolean.FALSE);
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullBoolEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return null;
    }

    @Override
    public void setNullAble(boolean nullAble) {
        notNullBoolEditor.setNullAble(nullAble);
        super.setNullAble(nullAble);
    }
}
