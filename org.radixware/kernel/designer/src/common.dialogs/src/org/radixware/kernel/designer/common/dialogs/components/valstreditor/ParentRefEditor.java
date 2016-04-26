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
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.common.utils.Utils;

class ParentRefEditor extends ValueEditor<Pid> {

    private class NotNullParentRefEditor extends NotNullEditor<Pid> {

        private final ParentRefEditorComponent editor;

        public NotNullParentRefEditor(ParentRefEditor valEditor) {
            super(valEditor);
            editor = new ParentRefEditorComponent(valEditor, valEditor.targetTable);
            editor.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        editor.getParent().dispatchEvent(e);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
        }

        @Override
        public void setValue(Pid value) {
            if (!Utils.equals(value, getValue())) {
                editor.setValue(value);
            }
            fireValueChanged();
        }

        @Override
        public Pid getValue() {
            return editor.getValue();
        }

        @Override
        public JComponent getEditor() {
            return editor;
        }
    }
    private final NotNullParentRefEditor notNullRefEditor;
    private final DdsTableDef targetTable;

    public ParentRefEditor(ValAsStrEditor editor, DdsTableDef targetTable) {
        super(editor);
        this.targetTable = targetTable;
        notNullRefEditor = new NotNullParentRefEditor(this);
        registerSwitchToNullValueHotKey();
    }

    @Override
    public void setDefaultValue() {
        if (targetTable != null) {
            final Pid pid = new Pid(targetTable.getId(), "");
            notNullRefEditor.setValue(pid);
        } else {
        }
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullRefEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return null;
    }
}
