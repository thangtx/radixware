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

import com.trolltech.qt.core.QMargins;
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
import org.radixware.kernel.explorer.inspector.propertyEditors.QMarginsPropertyEditor;

public class QMarginsInspectorDelegate implements InspectorDelegate {

    @Override
    public IPropertyEditor createEditorForClass(Class cl, QWidget parent, IClientEnvironment environment) {
        return new QMarginsPropertyEditor(environment, parent);
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
        QMargins margins = (QMargins) property.getValue();
        WidgetProperty wdgtProp = new WidgetProperty("Left", margins.left(), property.getName(), new QtInspectorDelegate(), int.class, true, null);
        wdgtPropList.add(wdgtProp);
        wdgtProp = new WidgetProperty("Right", margins.right(), property.getName(), new QtInspectorDelegate(), int.class, true, null);
        wdgtPropList.add(wdgtProp);
        wdgtProp = new WidgetProperty("Top", margins.top(), property.getName(), new QtInspectorDelegate(), int.class, true, null);
        wdgtPropList.add(wdgtProp);
        wdgtProp = new WidgetProperty("Bottom", margins.bottom(), property.getName(), new QtInspectorDelegate(), int.class, true, null);
        wdgtPropList.add(wdgtProp);
        return wdgtPropList;
    }

    @Override
    public Set<Class> getSupportedClasses() {
        return Collections.<Class>singleton(QMargins.class);
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}
