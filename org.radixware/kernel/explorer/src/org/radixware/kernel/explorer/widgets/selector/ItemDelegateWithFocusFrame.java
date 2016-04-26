/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;


class ItemDelegateWithFocusFrame extends QItemDelegate{

    private boolean drawFocusFrame;
    private long currentIndexId;
    private int currentColumn = -1;    
    private int firstVisibleColumn = -1;
    
    private final static QPen CELL_FRAME_PEN =
            new QPen(QColor.black, 2, Qt.PenStyle.SolidLine);
    
    private final static QPen ROW_FRAME_PEN =
            new QPen(QColor.darkBlue, 2, Qt.PenStyle.SolidLine);

    public ItemDelegateWithFocusFrame(final QObject parent){
        super(parent);
    }

    public void setFocusFrameVisible(final boolean isVisible) {
        drawFocusFrame = isVisible;
    }

    public void setCurrentCellFrameColor(final QColor color) {
        if (color != null) {
            CELL_FRAME_PEN.setColor(color);
        }
    }
    
    public void setCurrentRowFrameColor(final QColor color){
        if (color != null) {
            ROW_FRAME_PEN.setColor(color);
        }        
    }
    
    public void setCurrentIndex(final QModelIndex index){
        currentIndexId = index==null ? 0 : index.internalId();
        currentColumn = index==null ? -1 : index.column();
    }
    
    public void setFirstVisibleColumn(final int column){
        firstVisibleColumn = column;
    }
    
    private boolean isFirstVisibleColumn(final QModelIndex index){
        if (index.model() instanceof SelectorModel){
            return ((SelectorModel)index.model()).getFirstVisibleColumnIndex()==index.column();
        }
        return firstVisibleColumn==index.column();
    }
    
    private boolean isCurrentIndex(final QModelIndex index){        
        return index.internalId()==currentIndexId && index.column()==currentColumn;
    }
    
    private boolean isCurrentRow(final QModelIndex index){
        return index.internalId()==currentIndexId;
    }
    
    @Override
    public void paint(final QPainter painter, final QStyleOptionViewItem option, final QModelIndex index) {
        super.paint(painter, option, index);
        painter.save();        
        try{
            drawFocusFrame(painter,option,index);
        }finally{
            painter.restore();
        }
    }
    
    protected final void drawFocusFrame(final QPainter painter, final QStyleOptionViewItem option, final QModelIndex index){
        if (drawFocusFrame 
            //Вызов метода currentIndex() из QAbstractItemView снижает производительность
            && isCurrentRow(index)) {
            if (option.state().isSet(QStyle.StateFlag.State_HasFocus) || isCurrentIndex(index)){
                painter.setPen(CELL_FRAME_PEN);
                final QRect rect = option.rect();
                painter.drawRect(rect.adjusted(1, 1, -1, -1));
                painter.drawPoint(rect.topLeft());
                painter.drawPoint(rect.topRight());
            }else{
                painter.setPen(ROW_FRAME_PEN);
                final boolean needLeftBorder = isFirstVisibleColumn(index) && option.rect().x()>0;
                final QRect rect = option.rect().adjusted(needLeftBorder ? 1 : 0, 1, 0, 0);
                painter.drawLine(rect.topLeft(), rect.topRight());
                painter.drawLine(rect.bottomLeft(), rect.bottomRight());
                if (needLeftBorder){
                    painter.drawLine(rect.topLeft(), rect.bottomLeft());
                }
            }
        }
    }
}
