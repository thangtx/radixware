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
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class NumberPropertyEditor extends ValIntEditor implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();
    
    public NumberPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskInt((long)0, (long) Integer.MAX_VALUE, (byte)0,  null, 1, null, null, (byte)10), false, false);
        this.valueChanged.connect(this, "castType(Object)");
        this.setMinimumHeight(20);
    }
    
    
    @Override
    public void addValueListener(ValueListener listener) {
            valListenerController.add(listener);
    }

    @SuppressWarnings("unused")
    private void castType(Object value) {
        if (value != null) {
            valListenerController.notifyListener(((Long)value).intValue());
        }
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        valListenerController.remove(listener);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.setValue(((Integer)property.getValue()).longValue());
        return true;
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty wdgtProperty, final IPropertyEditor childrenPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
