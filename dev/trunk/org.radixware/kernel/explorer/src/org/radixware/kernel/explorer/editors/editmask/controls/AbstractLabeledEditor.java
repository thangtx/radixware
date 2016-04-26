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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;


public abstract class AbstractLabeledEditor<T> extends QWidget {
    private final QLabel label;
    private final ValEditor<T> valueEditor;
    public final Signal1<Object> valueChanged = new Signal1<Object>();
    
    public AbstractLabeledEditor(final IClientEnvironment environment, final QWidget parent, final Qt.Orientation orientation) {
        super(parent);
        QLayout layout = null;
        switch(orientation) {
            case Vertical: layout = new QVBoxLayout(); break;
            case Horizontal: 
            default:    
                layout = new QHBoxLayout(); break;
        }
        label = new QLabel(this);
        
        valueEditor = initValueEditor(environment);
        valueEditor.valueChanged.connect(this.valueChanged);
        layout.addWidget(label);
        layout.addWidget(valueEditor);
        layout.setMargin(0);
        setFocusProxy(valueEditor);
        this.setLayout(layout);
    }
    
    protected abstract ValEditor<T> initValueEditor(IClientEnvironment environment);
    
    public final void setLabelText(final String text) {
        label.setText(text);
    }
    
    public final T getValue() {
        return valueEditor.getValue();
    }
    
    public final void setValue(final T value) {
        valueEditor.setValue(value);
    }
    
}
