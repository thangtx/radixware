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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.ItemDelegatePainter;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class SelectorWidgetItemDelegate extends WrapModelDelegate{
    
    private final Map<Integer,QColor> darkerColorsCache = new HashMap<>();
    private final int textMargin;
    private int evenRowBgColorFactor;
    
    private final QRect tmpRect = new QRect();    
    
    private final QStyleOptionHeader options = new QStyleOptionHeader();
    private final QSize size = new QSize();
    private int minSectionHeight;

    public SelectorWidgetItemDelegate(final QAbstractItemView view){
        super(view);
        setEditorFrameVisible(true);
        minSectionHeight = calcHeaderHeight(view);
        setFocusFrameVisible(true);
        textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin);        
    }
    
    public void setEvenRowBgColorFactor(final int bgColorFactor){
        if (bgColorFactor!=evenRowBgColorFactor){
            evenRowBgColorFactor = bgColorFactor;
            darkerColorsCache.clear();
        }
    }    
    
    private static Rectangle rect(final String text, final ExplorerFont font){
        if (text==null || text.isEmpty()){
            return null;
        }else{  
            final QFontMetrics fontMetrics = font.getQFontMetrics();
            return new Rectangle(0, 0, WidgetUtils.calcTextWidth(text, fontMetrics, true), fontMetrics.height());
        }
    }
    

    @Override    
    public void paint(final QPainter painter, final QStyleOptionViewItem option, final QModelIndex index) {
        paintImpl(painter, option, index);
    }    
    
    private void paintImpl(final QPainter painter, 
                           final QStyleOptionViewItem option, 
                           final QModelIndex index){
        final SelectorModel model = (SelectorModel)index.model();
        final String text = model.getDisplay(index);
        final boolean isBrokenEntityModel = model.isBrokenEntity(index);
        if (isBrokenEntityModel && (text==null || text.isEmpty())){
            return;//surrounding of https://bugreports.qt.nokia.com//browse/QTBUG-4319
        }
        final ExplorerTextOptions textOptions = model.getTextOptions(index);
        //option.setShowDecorationSelected(false);
        final ExplorerFont font = textOptions.getFont();
        option.setFont(font.getQFont());
        option.setFontMetrics(font.getQFontMetrics());
        final Integer textAlignment = (Integer)model.data(index, Qt.ItemDataRole.TextAlignmentRole);
        if (textAlignment!=null){
            option.setDisplayAlignment(new Qt.Alignment(textAlignment.intValue()));
        }
        final QBrush textColor = textOptions.getForegroundBrush();
        if (textColor!=null){
            final QPalette palette = new QPalette(option.palette());
            palette.setBrush(QPalette.ColorRole.Text, textColor);
            option.setPalette(palette);
        }
        final QRect rect = option.rect();
        // prepare
        painter.save();
        try{
            painter.setClipRect(rect);

            // get the data and the rectangles        

            //final QPixmap pixmap;
            final Rectangle decorationRect;        
            final QIcon icon = (QIcon)model.data(index,Qt.ItemDataRole.DecorationRole);            
            if (icon==null) {            
                decorationRect = null;
            } else {
                decorationRect = new Rectangle(0, 0, option.decorationSize().width(), option.decorationSize().height());
            }
            
            final Rectangle displayRect = rect(text, font);

            final Rectangle checkRect;
            final Qt.CheckState checkState = 
                (Qt.CheckState)model.data(index, Qt.ItemDataRole.CheckStateRole);
            if (checkState==null) {            
                checkRect = null;
            }else{
                checkRect = 
                    new Rectangle(0, 0, TristateCheckBoxStyle.INDICATOR_SIZE, TristateCheckBoxStyle.INDICATOR_SIZE);
            }

            // do the layout
            final ItemDelegatePainter.CellLayout layout = 
                ItemDelegatePainter.getInstance().doLayout(option, new ItemDelegatePainter.CellLayout(checkRect, decorationRect, displayRect), textMargin);

            // draw the item
            final boolean isSelected = model.isSelected(index);
            drawBackGround(painter, 
                           option, 
                           isSelected, 
                           index.row(), 
                           textOptions.getBackground());
            if (checkState!=null){                
                drawCheck(painter, option, WidgetUtils.awtRect2QRect(layout.checkRect, tmpRect), checkState);
            }
            if (icon !=null ){
                ItemDelegatePainter.getInstance().drawDecoration(painter, option, icon, layout.decorationRect);
            }
            ItemDelegatePainter.getInstance().drawDisplay(painter, option, new Rectangle(layout.textRect), font.getQFont(), text, textMargin, hasClipping());            ;
            drawFocus(painter, option, WidgetUtils.awtRect2QRect(layout.textRect, tmpRect));
            option.setRect(rect);
            drawFocusFrame(painter,option,index);
            // done
        }finally{
            painter.restore();
        }        
    }
    
    protected void drawBackGround(final QPainter painter, 
                                  final QStyleOptionViewItem option, 
                                  final boolean isSelected,
                                  final int row,
                                  final QColor bgColor){        
        QColor rowColor;
        if (!isSelected && row % 2==0 && evenRowBgColorFactor>0){            
            if (!isSelected && row % 2==0 && evenRowBgColorFactor>0){
                rowColor = darkerColorsCache.get(bgColor.rgb());
                if (rowColor==null){                    
                    rowColor = ExplorerTextOptions.getDarker(ExplorerTextOptions.qtColor2awtColor(bgColor), evenRowBgColorFactor);
                    darkerColorsCache.put(bgColor.rgb(), rowColor);
                }
            }else{
                rowColor = bgColor;
            }            
        }else{
            rowColor = bgColor;
        }
        ItemDelegatePainter.getInstance().drawBackground(painter, option, rowColor);
    }

    @Override
    public QSize sizeHint(final QStyleOptionViewItem qsovi, final QModelIndex index) {
        final QSize sizeHiht = super.sizeHint(qsovi, index);
        final int selfSizeHeight = sizeHiht.height();
        if (selfSizeHeight<minSectionHeight){
            final int delta = minSectionHeight - selfSizeHeight;
            sizeHiht.setHeight(selfSizeHeight+delta/2);
        }
        return sizeHiht;
    }

    public void applySettings(final QAbstractItemView view){
        minSectionHeight = calcHeaderHeight(view);
    }

    private int calcHeaderHeight(final QAbstractItemView view){
        final ExplorerFont boldFont = ExplorerFont.Factory.getFont(view.font()).getBold();
        options.setFontMetrics(boldFont.getQFontMetrics());
        final QSize headerSize = 
            view.style().sizeFromContents(QStyle.ContentsType.CT_HeaderSection, options, size, view);
        return headerSize.height();
    }
}
