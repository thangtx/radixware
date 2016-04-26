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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;


public class QWidgetProxy {

    public static String getObjectName(QWidget w) {
        return w.objectName();
    }

    public static org.radixware.kernel.common.client.widgets.IWidget getParentWindow(QWidget widget) {
        QWidget w = widget.parentWidget();
        while (w != null) {
            if (w instanceof IWidget) {
                return (org.radixware.kernel.common.client.widgets.IWidget) w;
            }
            w = w.parentWidget();
        }
        return null;
    }

    public static IView findParentView(QWidget widget) {
        QWidget w = widget.parentWidget();
        while (w != null) {
            if (w instanceof IView) {
                return (IView) w;
            }
            w = w.parentWidget();
        }
        return null;
    }

    public static boolean hasUI(QWidget w) {
        return w != null && w.nativePointer() != null;
    }

    public static QWidget getWindow(IView view) {
        return ((QWidget) view).window();
    }

    public static void setParent(QWidget widget, org.radixware.kernel.common.client.widgets.IWidget parent) {
        if (parent instanceof QWidget) {
            widget.setParent((QWidget) parent);
        }
    }
}
