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

package org.radixware.kernel.explorer.editors.editmask.controls;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;


public abstract class AbstractCheckableEditor<T> extends QWidget {
    protected final Signal1<Integer> stateChanged = new Signal1<Integer>();
    private final ValEditor<T> valueEditor;
    private final QCheckBox checkBox;
    
    public AbstractCheckableEditor(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        checkBox = new QCheckBox(this);
        checkBox.stateChanged.connect(stateChanged);
        valueEditor = initValueEditor(environment);
        
        final QHBoxLayout layout = new QHBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignRight));
        layout.addWidget(checkBox);
        layout.addWidget(valueEditor);
        layout.setMargin(0);
        this.setLayout(layout);
        
        setFocusProxy(valueEditor);
    }

    protected abstract ValEditor<T> initValueEditor(IClientEnvironment environment);
    
    public final void setText(final String text) {
        checkBox.setText(text);
    }
    
    public final T getValue() {
        return valueEditor.getValue();
    }
    
    public final void setValue(final T value) {
        valueEditor.setValue(value);
    }
    
    public final boolean isChecked() {
        return checkBox.isChecked();
    }
    
    public final void enable(final boolean flag) {
        if(flag) {
            setEnabled(flag); //enable the whole widget if it was disabled
        }
        valueEditor.setEnabled(flag);
        checkBox.setChecked(flag);
    }
    
    public final void setLabelWidth(final int width) {
        checkBox.setMinimumWidth(width);
    }
    
    public final int getLabelWidth() {
        return checkBox.sizeHint().width();
    }
    
    public final void setClientEditMask(EditMask editMask) {
        valueEditor.setEditMask(editMask);
    }
    
    public final boolean checkInput(){
        return valueEditor.isEnabled() ? valueEditor.checkInput() : true;
    }
}