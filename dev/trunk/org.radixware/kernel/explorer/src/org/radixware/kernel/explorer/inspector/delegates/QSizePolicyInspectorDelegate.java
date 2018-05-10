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
package org.radixware.kernel.explorer.inspector.delegates;

import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.inspector.IPropertyValueReader;
import org.radixware.kernel.explorer.inspector.IPropertyValueWriter;
import org.radixware.kernel.explorer.inspector.WidgetProperty;
import org.radixware.kernel.explorer.inspector.propertyEditors.IPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.QSizePolicyPropertyEditor;

public class QSizePolicyInspectorDelegate implements InspectorDelegate {

    private final IClientEnvironment environment;

    public QSizePolicyInspectorDelegate(IClientEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public IPropertyEditor createEditorForClass(Class cl, QWidget parent, IClientEnvironment environment) {
        return new QSizePolicyPropertyEditor(environment, parent);
    }

    @Override
    public IPropertyValueReader createReaderForClass(Class cl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IPropertyValueWriter createWriterForClass(Class cl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDisplayStringForValue(WidgetProperty property) {
        return property.getValue().toString();
    }

    @Override
    public List<WidgetProperty> getChildProperties(WidgetProperty property) {
        List<WidgetProperty> wdgtPropList = new LinkedList<>();
        try {
            WidgetProperty wdgtProp = new WidgetProperty("Horizontal Policy", ((QSizePolicy) property.getValue()).horizontalPolicy(), property.getName(), new QtInspectorDelegate(), QSizePolicy.Policy.class, property.isReadOnly(), (property.isReadOnly() == true ? null : QSizePolicy.class.getMethod("setHorizontalPolicy", QSizePolicy.Policy.class)));
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Vertical Policy", ((QSizePolicy) property.getValue()).verticalPolicy(), property.getName(), new QtInspectorDelegate(), QSizePolicy.Policy.class, property.isReadOnly(), (property.isReadOnly() == true ? null : QSizePolicy.class.getMethod("setVerticalPolicy", QSizePolicy.Policy.class)));
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Horizontal Stretch", ((QSizePolicy) property.getValue()).horizontalStretch(), property.getName(), new QSizePolicyStretchInspectorDelegate(), int.class, property.isReadOnly(), (property.isReadOnly() == true ? null : QSizePolicy.class.getMethod("setHorizontalStretch", byte.class)));
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Vertical Stretch", ((QSizePolicy) property.getValue()).verticalStretch(), property.getName(), new QSizePolicyStretchInspectorDelegate(), int.class, property.isReadOnly(), (property.isReadOnly() == true ? null : QSizePolicy.class.getMethod("setVerticalStretch", byte.class)));
            wdgtPropList.add(wdgtProp);

        } catch (NoSuchMethodException | SecurityException ex) {
            environment.getTracer().error(ex);
        }
        return wdgtPropList;

    }

    @Override
    public Set<Class> getSupportedClasses() {
        return Collections.<Class>singleton(QSizePolicy.class);
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}
