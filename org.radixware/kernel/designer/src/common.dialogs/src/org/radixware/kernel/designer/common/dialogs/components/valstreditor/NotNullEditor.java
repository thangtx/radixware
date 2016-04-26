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


public abstract class NotNullEditor<T> {

    private final ValueEditor<T> editor;

    public NotNullEditor(ValueEditor<T> valEditor) {
        this.editor = valEditor;
    }

    public abstract void setValue(T value);

    public abstract T getValue();

    public abstract JComponent getEditor();

    public void setModel(ValAsStrEditorModel model) {
    }

    protected void fireValueChanged() {
        editor.setValue(getValue());
    }
    
    public void activateByKeyInput(KeyEvent e) {
    }
}