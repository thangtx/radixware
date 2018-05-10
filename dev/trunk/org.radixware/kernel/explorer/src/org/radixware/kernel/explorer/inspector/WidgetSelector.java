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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;

/**
 *
 * @author szotov
 */
public class WidgetSelector extends QEventFilter {

    private QWidget selectedWidget;
    private final QEventLoop waitingLoop = new QEventLoop(this);

    WidgetSelector(final QObject parent) {
        super(parent);
        setProcessableEventTypes(EnumSet.of(QEvent.Type.MouseButtonPress, QEvent.Type.KeyPress));//event types that pass throw eventFilter method
            }

    @Override
    public boolean eventFilter(final QObject target, final QEvent event) {
        if (event instanceof QMouseEvent // event type = MouseButtonPress
                && ((QMouseEvent) event).buttons().value() == Qt.MouseButton.LeftButton.value()
                && target instanceof QWidget) {
            selectedWidget = (QWidget) target;
            waitingLoop.exit();
            return true;//filter this event because it was processed here
        } else if (event instanceof QKeyEvent
                && ((QKeyEvent) event).key() == Qt.Key.Key_Escape.value()) {
            waitingLoop.exit();
            return true;//filter this event because it was processed here
                    }
        return false;//do not filter unprocessed event
    }

    public QWidget select() {
        QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.PointingHandCursor));
        QApplication.instance().installEventFilter(this);
        waitingLoop.exec();
        QApplication.instance().removeEventFilter(this);
        QApplication.restoreOverrideCursor();
        if (selectedWidget == null || selectedWidget.nativeId() == 0) {
            return null;
        }
        if (selectedWidget.window() instanceof InspectorDialog) {
            return null;
        } else {
            return selectedWidget;
        }
    }
}
