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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class EnumPropertyEditor extends ValListEditor implements IPropertyEditor {

    private final ValueListenerController valListenerController = new ValueListenerController();
    private Object[] valuesArr;

    public EnumPropertyEditor(Class<? extends Enum> cl, IClientEnvironment environment, QWidget parent) {
        super(environment, parent, Collections.<EditMaskList.Item>emptyList());
        final EditMaskList editMask = new EditMaskList();
        try {
            Method method = cl.getMethod("values", (Class<?>[]) null);
            Object[] values = (Object[]) method.invoke(null, (Object[]) null);
            valuesArr = values;

            for (Object enumValue : values) {
                editMask.addItem(enumValue.toString(), enumValue.toString());
            }
            this.setEditMask(editMask);
            this.setMinimumHeight(20);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            environment.getTracer().error(ex);
        }
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
        this.setValue(property.getValue().toString());
        return true;
    }

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, WidgetProperty wdgtProperty, IPropertyEditor childrenPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onActivatedIndex(int index) {
        super.onActivatedIndex(index);
        valListenerController.notifyListener(valuesArr[index]);
    }

}
