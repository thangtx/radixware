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
import org.radixware.kernel.explorer.views.IExplorerView;


public abstract class QWidgetDescriptor {

    public static final class Factory {

        private Factory() {
        }

        public static QWidgetDescriptor newInstance(final QWidget widget, final QWidget parentWidget) {
            final ModelDescriptor modelDescriptor;
            if (widget instanceof IExplorerView) {
                final IExplorerView view = (IExplorerView) widget;
                if (view.getModel() == null) {
                    return null;//представление было закрыто
                } else {
                    modelDescriptor = ModelDescriptor.Factory.newInstance(view.getModel());
                }
            } else {
                modelDescriptor = null;
            }
            if (widget.objectName() != null && !widget.objectName().isEmpty()) {
                if (parentWidget == null) {
                    return new QWidgetDescriptorByName(widget.getClass().getName(), widget.objectName(), modelDescriptor);
                } else {
                    final List<QObject> children = WidgetUtils.findChildren(parentWidget, widget.getClass(), widget.objectName());
                    if (children.size() == 1) {
                        return new QWidgetDescriptorByName(widget.getClass().getName(), widget.objectName(), modelDescriptor);
                    }
                }
            }
            if (parentWidget == null) {
                final List<QWidget> widgets = QApplication.topLevelWidgets();
                final int index = widgets.indexOf(widget);
                if (index > -1) {
                    return new QWidgetDescriptorByIndex(widget.getClass().getName(), index, modelDescriptor);
                }
            } else {
                final List<QObject> children = WidgetUtils.findChildren(parentWidget, widget.getClass());
                final int index = children.indexOf(widget);
                if (index > -1) {
                    return new QWidgetDescriptorByIndex(widget.getClass().getName(), index, modelDescriptor);
                }
            }
            return null;
        }
    }
    protected final ModelDescriptor modelDescriptor;

    protected QWidgetDescriptor(final ModelDescriptor modelDescriptor) {
        this.modelDescriptor = modelDescriptor;
    }

    public final QWidget findWidget(final QWidget parentWidget) {
        final QWidget widget = findQWidgetImpl(parentWidget);
        if (widget != null && modelDescriptor != null
                && (widget instanceof IExplorerView) && !modelDescriptor.isSameModel(((IExplorerView) widget).getModel())) {
            return null;
        }
        return widget;
    }

    protected abstract QWidget findQWidgetImpl(final QWidget parentWidget);
}
