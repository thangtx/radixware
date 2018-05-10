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
package org.radixware.kernel.explorer.inspector.propertyEditors;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QColorDialog;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;

final class ColorPushBtn extends QPushButton {

    private final QPalette.ColorGroup colorGroup;
    private final QPalette.ColorRole colorRole;
    private QColor color;

    public ColorPushBtn(String str, QWidget qw, QPalette.ColorRole colorRole, QPalette.ColorGroup colorGroup, QColor color) {
        super(qw);
        this.setToolTip(str);
        this.color = color;
        this.colorGroup = colorGroup;
        this.colorRole = colorRole;
        this.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        this.setMinimumHeight(10);
        this.setMinimumWidth(80);
        init();
    }

    @SuppressWarnings("unused unchecked")
    private void btnClickedSlot() {
        QColor returnedColor = QColorDialog.getColor(this.color);
        if (returnedColor.isValid()) {
            this.setStyleSheet("background-color: rgb(" + returnedColor.red() + ", " + returnedColor.green() + ", " + returnedColor.blue() + ");");
            this.color = returnedColor;
        }
    }

    private void init() {
        this.setAutoFillBackground(true);
        this.setStyleSheet("background-color: rgb(" + color.red() + ", " + color.green() + ", " + color.blue() + ");");
        this.clicked.connect(this, "btnClickedSlot()");
    }
    
    public QColor getColor() {
        return this.color;
    }
    
    public QPalette.ColorGroup getColorGroup() {
        return this.colorGroup;
    }
    
    public QPalette.ColorRole getColorRole() {
        return this.colorRole;
    }
}
