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

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.inspector.IPropertyValueReader;
import org.radixware.kernel.explorer.inspector.IPropertyValueWriter;
import org.radixware.kernel.explorer.inspector.WidgetProperty;
import org.radixware.kernel.explorer.inspector.propertyEditors.BooleanPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.CharPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.EnumPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.IPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.NumberPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.QIconPropertyEditor;
import org.radixware.kernel.explorer.inspector.propertyEditors.StringPropertyEditor;

public class QtInspectorDelegate implements InspectorDelegate {

    @Override
    @SuppressWarnings("unchecked")
    public IPropertyEditor createEditorForClass(Class cl, QWidget parent, IClientEnvironment environment) {
        if (cl.equals(String.class)) {
            return new StringPropertyEditor(environment, parent);
        } else if (cl.equals(Integer.class) || cl.equals(int.class)) {
            return new NumberPropertyEditor(environment, parent);
        } else if (cl.equals(Boolean.class) || cl.equals(boolean.class)) {
            return new BooleanPropertyEditor(environment, parent);
        } else if (cl.equals(QIcon.class)) {
            return new QIconPropertyEditor();
        } else if (cl.equals(char.class) || cl.equals(Character.class)) {
            return new CharPropertyEditor(environment, parent);
        } else {
            for (Class delegateClass = cl; delegateClass.getSuperclass() != null; delegateClass = delegateClass.getSuperclass()) {
                if (delegateClass.equals(Enum.class)) {
                    return new EnumPropertyEditor(cl, environment, parent);
                }
            }
        }
        return null;
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
        return String.valueOf(property.getValue());
    }

    @Override
    public List<WidgetProperty> getChildProperties(WidgetProperty property) {
        return Collections.emptyList();
    }

    @Override
    public Set<Class> getSupportedClasses() {
        Set<Class> supportedClasses = new LinkedHashSet<>();
        supportedClasses.add(String.class);
        supportedClasses.add(Integer.class);
        supportedClasses.add(int.class);
        supportedClasses.add(Boolean.class);
        supportedClasses.add(boolean.class);
        supportedClasses.add(QIcon.class);
        supportedClasses.add(char.class);
        supportedClasses.add(Character.class);
        return supportedClasses;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }
}
