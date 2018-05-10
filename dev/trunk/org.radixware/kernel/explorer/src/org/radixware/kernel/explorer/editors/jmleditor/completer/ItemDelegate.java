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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCDEStyle;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QMotifStyle;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyle.State;
import com.trolltech.qt.gui.QStyleFactory;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyleOptionViewItemV4;
import com.trolltech.qt.gui.QStyledItemDelegate;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QWidget;


public class ItemDelegate  extends QStyledItemDelegate{
    
    private final QTextDocument document = new QTextDocument();
    public final static int SPACE_HEIGHT=7;
    private final QSize DEFAULT_ITEM_SIZE;

    public ItemDelegate(QWidget widget){
        super();
        //There is problem in rendering this ItemDelegate on Motif and CDE styles.
        //On other styles method style.drawControl(type, opts, ...) doesn't respect 
        //opts.text() value and draw rectagle with right sizes for SE_ItemViewItemText.
        //On CDE and Motif opts.text() is important to draw rect for SE_ItemViewItemText.
        //In our case we use QTextDocument to draw content in SE_ItemViewItemText rect.
        //To avoid difficults with right calculation of SE_ItemViewItemText 
        //rectangle size we just use Plastique style in that case.
        QStyle style = widget.style();
        if (style instanceof QMotifStyle || style instanceof QCDEStyle) {
            style = QStyleFactory.create("plastique");
            widget.setStyle(style);
        }
        
        final int height = new QFontMetrics(widget.font()).height() + SPACE_HEIGHT;
        DEFAULT_ITEM_SIZE  = new QSize(200, height);
    }

    @Override
    public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
        return null;
    }

   @Override
    public void paint(final QPainter painter, final QStyleOptionViewItem  option, final QModelIndex index){
        final QStyleOptionViewItemV4 optionV4 =(QStyleOptionViewItemV4)option;

        //prevent draw focus
        final State state=optionV4.state();
        final int val=option.state().value() & (~QStyle.StateFlag.State_HasFocus.value());
        state.setValue(val);
        optionV4.setState(state);

        initStyleOption(optionV4,index);
        
        //if we have plain text CSS style not work, so wrap text in span
        String html = "<span>" + optionV4.text() + "</span>";
        if ((optionV4.state().value() & QStyle.StateFlag.State_Selected.value())>0){
            html = html.replaceAll("font color=\"#\\S{6}\"", "");
            document.setDefaultStyleSheet("* {color: white;}");
        } else {
            document.setDefaultStyleSheet("* {color: black;}");
        }
        
        document.setDefaultFont(option.font());
        document.setDocumentMargin(2);
        document.setHtml(html);

        optionV4.setText("");
        QStyle style = optionV4.widget()!=null? optionV4.widget().style() : QApplication.style();
        style.drawControl(QStyle.ControlElement.CE_ItemViewItem, optionV4, painter);

        painter.save();
        final QRect textRect = style.subElementRect(QStyle.SubElement.SE_ItemViewItemText, optionV4);
        painter.setClipRect(textRect);
        painter.translate(textRect.topLeft());
        document.drawContents(painter);
        painter.restore();
    }    

    @Override
    public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index){
//******************OLD CODE*******************************************************
//            final QStyleOptionViewItemV4 optionV4 = (QStyleOptionViewItemV4) option;
//            initStyleOption(optionV4, index);
//            document.setHtml(optionV4.text());
//            document.setTextWidth(optionV4.rect().width());
//            itemSize.setWidth((int) document.idealWidth());
//            itemSize.setHeight((option.fontMetrics().height() + SPACE_HEIGHT));
//******************OLD CODE*******************************************************
        //Always return default size for optimization purposes.
        return DEFAULT_ITEM_SIZE;
    }
}