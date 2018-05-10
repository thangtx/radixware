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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class BooleanPropertyEditor extends ValBoolEditor implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();

    public BooleanPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskNone(), true, false);
        valueChanged.connect(this, "castType(Object)");
    }

    @Override
    public void addValueListener(ValueListener listener) {
            valListenerController.add(listener);
    }

    @SuppressWarnings("unused")
    private void castType(Object value) {
        valListenerController.notifyListener(value);
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        super.setReadOnly(isReadOnly);
        this.setDisabled(isReadOnly);
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.setValue((Boolean) property.getValue());
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
