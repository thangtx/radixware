/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector.propertyEditors;

import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QSizePolicyStretchPropertyEditor extends QSpinBox implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();

    public QSizePolicyStretchPropertyEditor() {
        this.setMaximum(Integer.MAX_VALUE);
        this.setMinimumHeight(20);
        this.valueStringChanged.connect(this, "castType(String)");
        this.setKeyboardTracking(false);
    }

    @Override
    public void addValueListener(ValueListener listener) {
            valListenerController.add(listener);
    }

    @SuppressWarnings("unused")
    private void castType(String value) {
        if (!value.isEmpty()) {
            Integer tmp = Integer.parseInt(value);
            valListenerController.notifyListener(tmp.byteValue());
        }
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public Object getValue() {
        return this.text();
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.setValue((int) property.getValue());
        return true;
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
