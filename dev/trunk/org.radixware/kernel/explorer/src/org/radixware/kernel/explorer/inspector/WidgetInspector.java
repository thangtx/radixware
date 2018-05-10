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
package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.QFlags;
import com.trolltech.qt.core.QObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.explorer.inspector.delegates.InspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QFlagsInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QtInspectorDelegate;

/**
 *
 * @author szotov
 */
public abstract class WidgetInspector<T> {

    private final Map<Class, InspectorDelegate> delegatesByClass = new HashMap<>();
    private final Map<Method, InspectorDelegate> delegatesByMethod = new HashMap<>();

    public abstract WidgetInfo<T> selectWidget(final QObject parent);

    public abstract List<T> getChildWidgets(T parent);

    public abstract T getParentWidget(T child);

    public abstract List<WidgetProperty> getWidgetProperties(T widget);

    public abstract boolean setWidgetProperty(T widget, Method setterMethod, Object value);

    public abstract boolean isExists(T widget);

    public abstract boolean isVisible(T widget);
    
    public abstract String getWidgetXPath(T widget);

    public abstract List<WidgetInfo<T>> getTopLevelWidgets();

    public abstract String getDescription(T widget);

    public void registerDelegate(InspectorDelegate delegate) {
        final Set<Class> classes = delegate.getSupportedClasses();
        for (Class cl : classes) {
            delegatesByClass.put(cl, delegate);
        }
    }

    public InspectorDelegate findDelegateForClass(final Class cl) {
        if (delegatesByClass.containsKey(cl)) {
            return delegatesByClass.get(cl);
        } else {
            for (Class delegateClass = cl; delegateClass.getSuperclass() != null; delegateClass = delegateClass.getSuperclass()) {
                if (delegateClass.equals(Enum.class)) {
                    return new QtInspectorDelegate();
                }
            }
        }
        for (Class delegateClass = cl; delegateClass.getSuperclass() != null; delegateClass = delegateClass.getSuperclass()) {
            if (delegateClass.equals(QFlags.class)) {
                return new QFlagsInspectorDelegate();
            }
        }
        return null;
    }

    public void registerDelegate(Method m, InspectorDelegate delegate) {
        delegatesByMethod.put(m, delegate);
    }

    public InspectorDelegate findDelegateForMethod(Method m) {
        if (delegatesByMethod.containsKey(m)) {
            return delegatesByMethod.get(m);
        }
        return null;
    }
}
