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

package org.radixware.kernel.explorer.tester;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyleOptionViewItemV4;
import com.trolltech.qt.gui.QStyledItemDelegate;


public class CheckBoxItemDelegate extends QStyledItemDelegate {

    private final int column;

    public CheckBoxItemDelegate(QObject parent, int column){
        super(parent);
        this.column = column;
    }

    @Override
    public void paint(QPainter painter, QStyleOptionViewItem option, QModelIndex index) {
        QStyleOptionViewItem copyOption = new QStyleOptionViewItemV4(option);

        if (index.column() == column) {
            final int textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin) + 1;
            QSize size = new QSize(option.decorationSize().width() + 5,option.decorationSize().height());
            QRect rect = new QRect(option.rect().x() + textMargin, option.rect().y(),
                                   option.rect().width() - (2 * textMargin),
                                   option.rect().height());

            QRect newRect = QStyle.alignedRect(option.direction(),
                                               new Qt.Alignment(Qt.AlignmentFlag.AlignCenter),
                                               size,
                                               rect);
            copyOption.setRect(newRect);

        }

        super.paint(painter, copyOption, index);
    }

    @Override
    public boolean editorEvent(QEvent event, QAbstractItemModel model, QStyleOptionViewItem option, QModelIndex index) {

        Qt.ItemFlags flags = model.flags(index);
        if (!(flags.isSet(Qt.ItemFlag.ItemIsUserCheckable)) || !(flags.isSet(Qt.ItemFlag.ItemIsEnabled)))
            return false;

        Object data = index.data(Qt.ItemDataRole.CheckStateRole);
        if (data != null &&
            !(data.equals(Qt.CheckState.Checked.value()) || data.equals(Qt.CheckState.Unchecked.value())))
            return false;

//        if (event instanceof QMouseEvent){
//            
//        }

        if (event.type().equals(QEvent.Type.MouseButtonRelease)) {
            final int textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin) + 1;

            QRect rect = new QRect(option.rect().x() + textMargin, option.rect().y(),
                                   option.rect().width() - (2 * textMargin),
                                   option.rect().height());

            QRect checkRect = QStyle.alignedRect(option.direction(),
                                                 new Qt.Alignment(Qt.AlignmentFlag.AlignCenter),
                                                 option.decorationSize(),
                                                 rect);

            if (!checkRect.contains(((QMouseEvent) event).pos()))
                return false;
            
        } else if (event.type().equals(QEvent.Type.KeyPress)) {
            if (((QKeyEvent) event).key() != Qt.Key.Key_Select.value() && 
                    ((QKeyEvent) event).key() != Qt.Key.Key_Space.value() &&
                    ((QKeyEvent) event).key() != Qt.Key.Key_F2.value())
                return false;
        } else {
            return false;
        }

        Integer state = (data.equals(Qt.CheckState.Checked.value()) ? Qt.CheckState.Unchecked.value() : Qt.CheckState.Checked.value());

        return model.setData(index, state, Qt.ItemDataRole.CheckStateRole);
    }

}
