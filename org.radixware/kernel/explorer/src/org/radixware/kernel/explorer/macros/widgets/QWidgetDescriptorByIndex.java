/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.explorer.macros.widgets;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.explorer.macros.ModelDescriptor;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class QWidgetDescriptorByIndex extends QWidgetDescriptor {

    private final String className;
    private final int index;

    protected QWidgetDescriptorByIndex(final String name, final int idx, final ModelDescriptor modelDescriptor) {
        super(modelDescriptor);
        if (name == null) {
            throw new NullPointerException();
        }
        className = name;
        index = idx;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QWidgetDescriptorByIndex) {
            if (obj == this) {
                return true;
            }
            return className.equals(((QWidgetDescriptorByIndex) obj).className)
                    && index == ((QWidgetDescriptorByIndex) obj).index;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 83 * hash + this.index;
        return hash;
    }

    @Override
    protected QWidget findQWidgetImpl(final QWidget parentWidget) {
        if (parentWidget == null) {
            final List<QWidget> widgets = QApplication.topLevelWidgets();
            for (QWidget widget : widgets) {
                if (className.equals(widget.getClass().getName())) {
                    return widget;
                }
            }
        } else {
            final Class<?> cl;
            try {
                cl = modelDescriptor.environment().getApplication().getDefManager().getClassLoader().loadClass(className);
            } catch (ClassNotFoundException ex) {
                return null;
            }
            final List<QObject> childObjects = WidgetUtils.findChildren(parentWidget, cl);
            if (childObjects != null && index < childObjects.size()) {
                return (QWidget) childObjects.get(index);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(className);
        strBuilder.append("[");
        strBuilder.append(Integer.toString(index));
        strBuilder.append("]");
        if (modelDescriptor != null) {
            strBuilder.append(" (");
            strBuilder.append(modelDescriptor.toString());
            strBuilder.append(")");
        }
        return strBuilder.toString();
    }
}
