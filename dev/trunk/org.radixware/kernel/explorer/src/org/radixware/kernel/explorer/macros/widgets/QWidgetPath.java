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

import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.explorer.macros.gui.ExplorerMacrosWindow;


public class QWidgetPath {

    private final List<QWidgetDescriptor> path = new LinkedList<QWidgetDescriptor>();

    public static final class Factory {

        private Factory() {
        }

        public static QWidgetPath newInstance(final QWidget widget) {
            if (widget instanceof IExplorerTree) {
                return new ExplorerTreeWidgetPath();
            }
            final List<QWidgetDescriptor> widgetDescriptors = new ArrayList<QWidgetDescriptor>();
            QWidgetDescriptor descriptor;
            for (QWidget parent = widget; parent != null; parent = parent.parentWidget()) {
                if (parent instanceof ExplorerMacrosWindow) {
                    return SelfWidgetPath.getInstance();
                } else if (parent instanceof IExplorerTree) {
                    return null;
                }
                descriptor = QWidgetDescriptor.Factory.newInstance(parent, parent.parentWidget());
                if (descriptor == null) {
                    return null;
                } else {
                    widgetDescriptors.add(0, descriptor);
                }
            }
            return new QWidgetPath(widgetDescriptors);
        }
    }

    protected QWidgetPath(final List<QWidgetDescriptor> widgetPath) {
        path.addAll(widgetPath);
    }

    public QWidget findWidget() {
        QWidget current = null;
        for (QWidgetDescriptor descriptor : path) {
            current = descriptor.findWidget(current);
            if (current == null) {
                return current;
            }
        }
        return current;
    }

    public final QWidgetPath findNearestParentWidget() {
        final List<QWidgetDescriptor> descriptors = new ArrayList<QWidgetDescriptor>();
        QWidget current = null;
        for (int i = 0; i < descriptors.size() - 1; i++) {
            current = descriptors.get(i).findWidget(current);
            if (current == null) {
                break;
            } else {
                descriptors.add(descriptors.get(i));
            }
        }
        return new QWidgetPath(descriptors);
    }

    public final boolean isEmpty() {
        return path.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                strBuilder.append("/");
            }
            strBuilder.append(path.get(i));
        }
        return strBuilder.toString();
    }

    public boolean isSelf() {
        return false;
    }
}
