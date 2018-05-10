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

package org.radixware.kernel.explorer.webdriver;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.views.MainWindow;

public final class WebDriverNotifier extends  QPushButton {
    
    private int oldCountSession= -1;
    private MessageProvider mp;

    public WebDriverNotifier(MessageProvider mp1, MainWindow parent) {
        super(parent);
        mp=mp1;
        final QFont font = new QFont();
        font.setPointSize(10);
        font.setBold(true);
        setFont(font);
        setContentsMargins(5, 0, 5, 0);
        QPalette palette = new QPalette(palette());
        palette.setColor(QPalette.ColorRole.ButtonText, QColor.red);
        setPalette(palette);
        setMessageText();
        this.setFlat(true);
        this.setFocusPolicy(Qt.FocusPolicy.NoFocus);
        startTimer(1000);
        this.setMaximumHeight(parent.statusBar().sizeHint().height() - 2);
        parent.statusBar().addPermanentWidget(this);
        this.setEnabled(false);
        
    }

    @Override
    protected void mousePressEvent(QMouseEvent qme) {
        // чтобы не нажималась
    }
    
    @Override
    protected void mouseMoveEvent(QMouseEvent qme) {
        // чтобы не нажималась
    }
    
    private int getSessionCount()
    {
        return WebDrvSessionsManager.getInstance().getSessionCount();
    }

    private void setMessageText()
    {
        int countSession=getSessionCount();
        if(oldCountSession != countSession)
        {
            oldCountSession=countSession;
            String msg;
            if(countSession == 0) msg = mp.translate("StatusBar", "Remote control: no connections");
            else msg = String.format(mp.translate("StatusBar", "Remote control: connections %s"),countSession);
            setText(msg);
        }
    }

    @Override
    protected void timerEvent(QTimerEvent event) {
        setMessageText();

        QPalette palette = new QPalette(palette());
        if (palette().color(QPalette.ColorRole.ButtonText).equals(QColor.red)) {
            palette.setColor(QPalette.ColorRole.ButtonText, palette().color(QPalette.ColorRole.Button));
        } else {
            palette.setColor(QPalette.ColorRole.ButtonText, QColor.red);
        }
        setPalette(palette);
    }
}
