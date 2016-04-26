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
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.macros.ModelDescriptor;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class QWidgetDescriptorByName extends QWidgetDescriptor {

    private final String className;
    private final String objectName;

    protected QWidgetDescriptorByName(final String className, final String objectName, final ModelDescriptor modelDescriptor) {
        super(modelDescriptor);
        if (objectName == null || className == null) {
            throw new NullPointerException();
        }
        this.className = className;
        this.objectName = objectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QWidgetDescriptorByName) {
            if (obj == this) {
                return true;
            }
            return objectName.equals(((QWidgetDescriptorByName) obj).objectName)
                    && className.equals(((QWidgetDescriptorByName) obj).className);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 79 * hash + (this.objectName != null ? this.objectName.hashCode() : 0);
        return hash;
    }

    @Override
    protected QWidget findQWidgetImpl(final QWidget parentWidget) {
        if (parentWidget == null) {
            final List<QWidget> widgets = QApplication.topLevelWidgets();
            for (QWidget widget : widgets) {
                if (className.equals(widget.getClass().getName())
                        && objectName.equals(widget.objectName())) {
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

            final List<QObject> childObjects = WidgetUtils.findChildren(parentWidget, cl, objectName);
            if (childObjects != null && childObjects.size() == 1) {
                return (QWidget) childObjects.get(0);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(className);
        strBuilder.append("[\"");
        strBuilder.append(objectName);
        strBuilder.append("\"]");
        if (modelDescriptor != null) {
            strBuilder.append(" (");
            strBuilder.append(modelDescriptor.toString());
            strBuilder.append(")");
        }
        return strBuilder.toString();
    }
}
