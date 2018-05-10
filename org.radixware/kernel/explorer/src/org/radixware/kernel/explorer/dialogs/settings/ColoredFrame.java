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
package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QColorDialog;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.gui.QCursor;
import org.radixware.kernel.explorer.env.Application;

public class ColoredFrame extends QFrame {

    private QColor color;
    public final Signal1<QColor> setColorSignal = new Signal1<>();

    public ColoredFrame(QWidget parent, QColor color, boolean showTooltip) {
        super(parent);

        this.color = color;
        setAutoFillBackground(true);
        setBackgroundRole(com.trolltech.qt.gui.QPalette.ColorRole.Window);
        if (showTooltip) {
            setToolTip(Application.translate("Settings Dialog", "Choose a color"));
        }
        setFrameShape(QFrame.Shape.WinPanel);
        setColor(this.color);
        resize(25, 25);
        setFixedSize(25, 25);

        this.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        this.setCursor(new QCursor(com.trolltech.qt.core.Qt.CursorShape.PointingHandCursor));
        show();
    }

    private void getNewColor() {
        final QColor newColor = color.isValid() ? QColorDialog.getColor(color) : QColorDialog.getColor();
        if (newColor.isValid()) {
            setColor(newColor);
        }
    }

    @Override
    protected void mousePressEvent(QMouseEvent event) {
        getNewColor();
    }

    @Override
    protected void keyPressEvent(QKeyEvent event) {
        //backspace?
        if (event.key() == Key.Key_Space.value()) {
            getNewColor();
        } else {
            event.ignore();
        }
    }

    public QColor getColor() {
        return color;
    }

    public void setColor(QColor color) {
        this.color = color;
        setAutoFillBackground(true);
        final QPalette p = new QPalette(palette());
        p.setColor(backgroundRole(), color);
        setPalette(p);
        update();
        setColorSignal.emit(color);
    }
}
