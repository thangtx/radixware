/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.utils.SystemTools;


final class TableCornerButton extends QAbstractButton {
    
    private static final String WINDOWS_FONT_FAMILY = "Arial Unicode MS";
    public static final char BUTTON_SYMBOL = '\u2714';
    private final QStyleOptionHeader styleOptionHeader;
    private boolean fixedPos;
    private int posX;
    private int posY;
    private boolean clickEnabled = true;
    private final QFontMetrics fontMetrics;
    private final QFont font;
    private final QStyle.State state = new QStyle.State(0);

    @SuppressWarnings(value = "LeakingThisInConstructor")
    public TableCornerButton(final QWidget parentWidget) {
        super(parentWidget);
        styleOptionHeader = new QStyleOptionHeader();
        styleOptionHeader.setPosition(QStyleOptionHeader.SectionPosition.OnlyOneSection);
        styleOptionHeader.initFrom(this);
        font = new QFont(font());
        if (SystemTools.isWindows && (new QFontDatabase().hasFamily(WINDOWS_FONT_FAMILY))) {
            font.setFamily(WINDOWS_FONT_FAMILY);
        }
        font.setStyleStrategy(QFont.StyleStrategy.PreferAntialias);
        fontMetrics = new QFontMetrics(font);
        setObjectName("rx_selector_table_corner_button");
    }

    @Override
    protected void paintEvent(final QPaintEvent event) {
        styleOptionHeader.initFrom(this);
        state.clearAll();
        if (isEnabled()) {
            state.set(QStyle.StateFlag.State_Enabled);
        }
        if (isActiveWindow()) {
            state.set(QStyle.StateFlag.State_Active);
        }
        state.set(QStyle.StateFlag.State_Raised);
        styleOptionHeader.setState(state);
        styleOptionHeader.setRect(rect());
        if (clickEnabled) {
            styleOptionHeader.setFontMetrics(fontMetrics);
            styleOptionHeader.setText(String.valueOf(BUTTON_SYMBOL));
        } else {
            styleOptionHeader.setText("");
        }
        styleOptionHeader.setTextAlignment(Qt.AlignmentFlag.AlignCenter);
        final QPainter painter = new QPainter(this);
        painter.setFont(font);
        try {
            style().drawControl(QStyle.ControlElement.CE_Header, styleOptionHeader, painter, this);
        } finally {
            painter.end();
        }
    }

    public QFontMetrics getSymbolFontMetrics() {
        return fontMetrics;
    }

    public void setFixedPos(final int x, final int y) {
        posX = x;
        posY = y;
        fixedPos = true;
    }

    @Override
    protected void moveEvent(final QMoveEvent event) {
        super.moveEvent(event);
        final QPoint pos = pos();
        if (fixedPos) {
            if (pos.x() != posX && pos.y() != posY) {
                move(posX, posY);
            }
        }
    }

    public void setClickEnabled(final boolean isEnabled) {
        if (isEnabled != clickEnabled) {
            clickEnabled = isEnabled;
            blockSignals(!isEnabled);
            repaint();
        }
    }

    public boolean isClickEnabled() {
        return clickEnabled;
    }

}
