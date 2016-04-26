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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.localization.MessageProvider;

public final class OldVersionTrayItem extends QPushButton {

    public OldVersionTrayItem(MessageProvider mp, QWidget parent) {
        super(parent);

        final QFont font = new QFont();
        font.setPointSize(10);
        font.setBold(true);
        setFont(font);
        setContentsMargins(5, 0, 5, 0);
        QPalette palette = new QPalette(palette());
        palette.setColor(QPalette.ColorRole.ButtonText, QColor.red);
        setPalette(palette);
        setText(mp.translate("StatusBar", "Old Version"));
        startTimer(1000);
        this.setMaximumHeight(Application.getMainWindow().statusBar().sizeHint().height() - 5);
    }

    @Override
    protected void timerEvent(QTimerEvent event) {
        QPalette palette = new QPalette(palette());
        if (palette().color(QPalette.ColorRole.ButtonText).equals(QColor.red)) {
            palette.setColor(QPalette.ColorRole.ButtonText, palette().color(QPalette.ColorRole.Button));
        } else {
            palette.setColor(QPalette.ColorRole.ButtonText, QColor.red);
        }
        setPalette(palette);
    }
}
