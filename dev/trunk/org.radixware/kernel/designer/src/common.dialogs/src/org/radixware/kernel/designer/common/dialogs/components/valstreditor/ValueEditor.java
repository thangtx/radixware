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
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.radixware.kernel.common.utils.Utils;


public abstract class ValueEditor<T> {

    public abstract void setDefaultValue();

    public abstract NotNullEditor getNotNullEditor();

    public abstract NullEditor getNullEditor();
    private T value = null;
    private boolean nullAble = true;
    protected final ValAsStrEditor editor;

    public ValueEditor(ValAsStrEditor editor) {
        this.editor = editor;
    }

    protected void registerSwitchToNullValueHotKey() {
        JComponent editor = getNotNullEditor().getEditor();
        InputMap inputMap = editor.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke("control SPACE"), "SWITCH_TO_NULL_EDITOR");

        inputMap = editor.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke("control SPACE"), "SWITCH_TO_NULL_EDITOR");

        ActionMap actionMap = editor.getActionMap();
        actionMap.put("SWITCH_TO_NULL_EDITOR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(null);
            }
        });
    }

    public void setValue(T value) {
        if (Utils.equals(this.value, value)) {
            return;
        }
        if (value == null && !nullAble) {
            setDefaultValue();
            return;
        }
        this.value = value;
        getNotNullEditor().setValue(value);
        editor.changeSupport.fireChange();
    }

    public void activateByKeyInput(KeyEvent e) {
        getNotNullEditor().activateByKeyInput(e);
    }
    
    public T getValue() {
        return value;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
        if (!nullAble && getValue() == null) {
            setDefaultValue();
        }
    }

    public void setModel(ValAsStrEditorModel model) {
        getNotNullEditor().setModel(model);
    }

    public String getNullIndicator() {
        return editor.getNullValueIndicator();
    }
}