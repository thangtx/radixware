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

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QRegion;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.inspector.IPropertyValueReader;
import org.radixware.kernel.explorer.inspector.IPropertyValueWriter;
import org.radixware.kernel.explorer.inspector.WidgetProperty;
import org.radixware.kernel.explorer.inspector.propertyEditors.IPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.QRegionPropertyEditor;

public class QRegionInspectorDelegate implements InspectorDelegate {

    private final IClientEnvironment environment;

    public QRegionInspectorDelegate(IClientEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public IPropertyEditor createEditorForClass(Class cl, QWidget parent, IClientEnvironment environment) {
        return new QRegionPropertyEditor(environment, parent);
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
        return this.toString();
    }

    @Override
    public List<WidgetProperty> getChildProperties(WidgetProperty property) {
        List<WidgetProperty> wdgtPropList = new LinkedList<>();
        try {
            if (((QRegion) property.getValue()).rectCount() != 0) {
                ArrayList<QRect> qRectList = (ArrayList<QRect>) ((QRegion) property.getValue()).rects();
                for (int i = 0; i < qRectList.size(); i++) {
                    WidgetProperty wdgtProp = new WidgetProperty("Rect" + i, qRectList.get(i), property.getName(), new QRectInspectorDelegate(environment), QRect.class, true, null);
                    wdgtPropList.add(wdgtProp);
                }
            }
        } catch (SecurityException ex) {
            environment.getTracer().error(ex);
        }
        return wdgtPropList;
    }

    @Override
    public Set<Class> getSupportedClasses() {
        return Collections.<Class>singleton(QRegion.class);
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}
