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

package org.radixware.kernel.explorer.editors.profiling;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;


public class ItemDelegate extends QItemDelegate{

    @Override
    public QSize sizeHint(final QStyleOptionViewItem qsovi, final QModelIndex qmi) {
        final QSize size=super.sizeHint(qsovi, qmi);
        size.setHeight(22);
        return size;
    }

    public ItemDelegate(){
        super();
    }

    @Override
    protected void drawDisplay(final QPainter painter, final QStyleOptionViewItem option, final QRect rect, final String text) {
        super.drawDisplay(painter, option, rect, text);
        final int x = (rect.x()+rect.width()-1);
        painter.setPen(QColor.lightGray);        
        painter.drawLine(x, rect.y(), x, (rect.y()+rect.height()));       
    }

    @Override
    protected void drawFocus(final QPainter arg0, final QStyleOptionViewItem arg1, final QRect arg2) {
        //return;
    }

}
