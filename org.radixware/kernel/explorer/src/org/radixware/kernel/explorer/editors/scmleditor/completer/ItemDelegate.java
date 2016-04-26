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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractTextDocumentLayout_PaintContext;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyle.State;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyleOptionViewItemV4;
import com.trolltech.qt.gui.QStyledItemDelegate;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QWidget;


public class ItemDelegate  extends QStyledItemDelegate{
    private  QTextDocument document = new QTextDocument();
    public static final int height=22;

    public ItemDelegate(){
        super();
    }

    @Override
    public QWidget createEditor(QWidget parent, QStyleOptionViewItem option, QModelIndex index) {
        return null;
    }

   @Override
    public void paint(QPainter painter, QStyleOptionViewItem  option,  QModelIndex index){
        QStyleOptionViewItemV4 optionV4 =(QStyleOptionViewItemV4)option;

        boolean isInFocus=(option.state().value() & QStyle.StateFlag.State_HasFocus.value())>0;
        //prevent draw focus
        State state=optionV4.state();
        int val=option.state().value() & (~QStyle.StateFlag.State_HasFocus.value());
        state.setValue(val);
        optionV4.setState(state);

        initStyleOption(optionV4,index);
        document.setDefaultFont(option.font());
        document.setDocumentMargin(2);
        document.setHtml(optionV4.text());

        optionV4.setText("");
        QStyle style = optionV4.widget()!=null? optionV4.widget().style() : QApplication.style();
        style.drawControl(QStyle.ControlElement.CE_ItemViewItem, optionV4, painter);

        //highlight selected item text
        QAbstractTextDocumentLayout_PaintContext ctx=new QAbstractTextDocumentLayout_PaintContext();
        QPalette pal = optionV4.palette();
        if (isInFocus && (optionV4.state().value() & QStyle.StateFlag.State_Selected.value())>0){
            QColor color=optionV4.palette().color(QPalette.ColorGroup.Active, QPalette.ColorRole.HighlightedText);
            pal.setColor(QPalette.ColorGroup.Active,QPalette.ColorRole.Text, color);
        }
        ctx.setPalette(pal);

        painter.save();
        QRect textRect = style.subElementRect(QStyle.SubElement.SE_ItemViewItemText, optionV4);
        painter.setClipRect(textRect);
        painter.translate(textRect.topLeft());
        document.documentLayout().draw(painter, ctx);
        painter.restore();
    }

    @Override
    public QSize sizeHint( QStyleOptionViewItem option,  QModelIndex index){
        QStyleOptionViewItemV4 optionV4 = (QStyleOptionViewItemV4)option;
        initStyleOption(optionV4,index);
        document.setHtml(optionV4.text());
        document.setTextWidth(optionV4.rect().width());
        return new QSize((int)document.idealWidth(),height);
    }
}