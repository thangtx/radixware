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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QWindowOpacityPropertyEditor extends QSlider implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();

    public QWindowOpacityPropertyEditor() {
        this.setMaximum(10);
        this.setMinimum(0);
        this.setOrientation(Qt.Orientation.Horizontal);
        this.setMinimumHeight(20);
        this.valueChanged.connect(this, "castType(Object)");
    }

    @Override
    public void addValueListener(ValueListener listener) {
            valListenerController.add(listener);
    }

    @SuppressWarnings("usused")
    private void castType(Object obj) {
        double d = (((Integer) obj)).doubleValue() / 10;
        this.setToolTip(String.valueOf(d));
        valListenerController.notifyListener(d);
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        this.setDisabled(isReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return !this.isEnabled();
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.setValue((int) ((double) property.getValue() * 10));
        this.setToolTip(String.valueOf(property.getValue()));
        return true;
    }

    @Override
    public Object getValue() {
        return ((double) this.value() / 10);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

}
