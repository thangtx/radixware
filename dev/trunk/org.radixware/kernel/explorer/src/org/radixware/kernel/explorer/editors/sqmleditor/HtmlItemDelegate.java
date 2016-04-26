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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractTextDocumentLayout_PaintContext;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyleOptionViewItemV4;
import com.trolltech.qt.gui.QStyledItemDelegate;
import com.trolltech.qt.gui.QTextDocument;


@Deprecated//RADIX-8347. Для дерева Sqml-дефиниций теперь нужно использовать SqmlDefinitionsTreeItemDelegate
public class HtmlItemDelegate extends QStyledItemDelegate {

        @Override
        public void paint(final QPainter painter,final QStyleOptionViewItem option,final QModelIndex index) {

            final QStyleOptionViewItemV4 options =(QStyleOptionViewItemV4) option;
            initStyleOption(options, index);
            painter.save();
            final QTextDocument  doc = new QTextDocument();
            doc.setHtml(options.text());
            options.setText("");
            options.widget().style().drawControl(QStyle.ControlElement.CE_ItemViewItem, options, painter);
             
            final QSize iconSize = options.icon().actualSize(options.rect().size());
            painter.translate(options.rect().left()+iconSize.width(), options.rect().top());
            final QRectF clip=new QRectF(0, 0, options.rect().width()+iconSize.width(), options.rect().height());
            painter.setClipRect(clip);
            
            final QAbstractTextDocumentLayout_PaintContext ctx = new QAbstractTextDocumentLayout_PaintContext();
            // set text color to red for selected item
            if (option.state().value() == QStyle.StateFlag.State_Selected.value()){
                ctx.palette().setColor(QPalette.ColorRole.Text, QColor.red);
            }
            ctx.setClip(clip);
            doc.documentLayout().draw(painter, ctx);

            painter.restore();
        }
}
