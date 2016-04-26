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

package org.radixware.kernel.explorer.macros.actions;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.macros.MacrosSettings;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


final class MouseAction extends UserInputAction{

    private final QPoint pos, globalPos;
    private final QEvent.Type eventType;
    private final Qt.MouseButton button;
    private final Qt.MouseButtons buttons;
    private final Qt.KeyboardModifiers keyboardModifiers;

    MouseAction(final QMouseEvent event, final QWidgetPath path){
        super(path);
        eventType = event.type();
        pos = event.pos();
        globalPos = event.globalPos();
        button = event.button();
        buttons = event.buttons();
        keyboardModifiers = event.modifiers();
    }

    public MacroActionResult execute(final MacrosSettings settings) {
        final QWidget widget = widgetPath.findWidget();
        if (widget!=null){
            QApplication.postEvent(widget, new QMouseEvent(eventType, pos, globalPos, button, buttons, keyboardModifiers));
        }
        return null;
    }

    public QIcon getIcon() {
        return null;
    }

    public String getTitle() {
        return eventType.name()+" on "+widgetPath.toString();
    }

}
