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

import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.InvocationTargetException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QSizePolicyPropertyEditor extends ValStrEditor implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();

    public QSizePolicyPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), true, true);
        this.setMinimumHeight(20);
    }

    @Override
    public void addValueListener(ValueListener listener) {
        valListenerController.add(listener);
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
        valListenerController.notifyListener(property.getValue());
        this.setValue("Horizontal Policy = " + ((QSizePolicy) property.getValue()).horizontalPolicy().toString() + ", VerticalPolicy = " + ((QSizePolicy) property.getValue()).verticalPolicy().toString()
                + ", Horizontal Stretch = " + ((QSizePolicy) property.getValue()).horizontalStretch() + ", Vertical Stretch = " + ((QSizePolicy) property.getValue()).verticalStretch());
        return true;
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        childPropEditor.addValueListener(new ValueListener() {
            @Override
            public void onChangeValue(Object newValue) {
                try {
                    childWdgtProperty.getSetterMethod().invoke(parentWdgtProperty.getValue(), newValue);
                    setPropertyValue(parentWdgtProperty);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    QSizePolicyPropertyEditor.super.getEnvironment().getTracer().error(ex);
                }
            }
        });
    }

}
