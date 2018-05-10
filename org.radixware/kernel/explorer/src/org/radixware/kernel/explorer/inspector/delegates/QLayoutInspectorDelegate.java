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
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLayout;
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
import org.radixware.kernel.explorer.inspector.propertyEditors.QLayoutPropertyEditor;

public class QLayoutInspectorDelegate implements InspectorDelegate {

    private final IClientEnvironment environment;

    public QLayoutInspectorDelegate(IClientEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public IPropertyEditor createEditorForClass(Class cl, QWidget parent, IClientEnvironment environment) {
        return new QLayoutPropertyEditor(environment, parent);
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
        if (property.getValue() != null) {
            QLayout layout = (QLayout) property.getValue();
            WidgetProperty wdgtProp = new WidgetProperty("Count", layout.count(), property.getName(), new QtInspectorDelegate(), int.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("SizeConstraint", layout.sizeConstraint(), property.getName(), new QtInspectorDelegate(), QLayout.SizeConstraint.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Margins", layout.contentsMargins(), property.getName(), new QMarginsInspectorDelegate(), QMargins.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("ContentsRect", layout.contentsRect(), property.getName(), new QRectInspectorDelegate(environment), QRect.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("ExpandingDirections", layout.expandingDirections(), property.getName(), new QFlagsInspectorDelegate(), Qt.Orientations.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Empty", layout.isEmpty(), property.getName(), new QtInspectorDelegate(), boolean.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("Enabled", layout.isEnabled(), property.getName(), new QtInspectorDelegate(), boolean.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("MaximumSize", layout.maximumSize(), property.getName(), new QSizeInspectorDelegate(environment), QSize.class, true, null);
            wdgtPropList.add(wdgtProp);
            wdgtProp = new WidgetProperty("MinimumSize", layout.minimumSize(), property.getName(), new QSizeInspectorDelegate(environment), QSize.class, true, null);
            wdgtPropList.add(wdgtProp);
        }
        return wdgtPropList;
    }

    @Override
    public Set<Class> getSupportedClasses() {
        return Collections.<Class>singleton(QLayout.class);
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}
