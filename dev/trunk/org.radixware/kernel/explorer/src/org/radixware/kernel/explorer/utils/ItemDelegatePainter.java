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

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTextLayout;
import com.trolltech.qt.gui.QTextLayout_FormatRange;
import com.trolltech.qt.gui.QTextLine;
import com.trolltech.qt.gui.QTextOption;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collections;


public final class ItemDelegatePainter {
    
    private final static ItemDelegatePainter INSTANCE = new ItemDelegatePainter();
    
    private final static Dimension NULL_SIZE = new Dimension(0,0);
    private final static Qt.Alignment ALIGNMENT_CENTER = new Qt.Alignment(Qt.AlignmentFlag.AlignCenter);    
    private final static QPointF START_POINTF = new QPointF(0,0); 
    
    private final QTextOption textOption = new QTextOption();
    private final QTextLayout textLayout = new QTextLayout();
    private final QRect tmpRect = new QRect();
    private final QRectF tmpRectF = new QRectF();
    
    
    private ItemDelegatePainter(){ 
        textOption.setWrapMode(QTextOption.WrapMode.NoWrap);
    }             
    
    public static ItemDelegatePainter getInstance(){
        return INSTANCE;
    }
        
    
    public final static class CellLayout{
        
        public final Rectangle checkRect;
        public final Rectangle decorationRect;
        public final Rectangle textRect;
        
        public CellLayout(final Rectangle checkRect, final Rectangle decorationRect, final Rectangle textRect){
            this.checkRect = checkRect;
            this.decorationRect = decorationRect;
            this.textRect = textRect;
        }

        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            if (checkRect!=null){
                result.append(checkRect.toString());
            }
            result.append('/');
            if (decorationRect!=null){
                result.append(decorationRect.toString());
            }
            result.append('/');
            if (textRect!=null){
                result.append(textRect.toString());
            }            
            return result.toString();
        }                        
        
        public Dimension unitedSize(){
            return decorationRect.union(textRect).union(checkRect).getSize();
        }
    
    }           
    
    public CellLayout doLayout(final QStyleOptionViewItem option, 
                               final CellLayout layout, 
                               final int focusFrameMargin){
            final Qt.LayoutDirection direction = option.direction();
        final QStyleOptionViewItem.Position position = option.decorationPosition();
        final Qt.Alignment decorationAlignment = option.decorationAlignment();
        final Qt.Alignment displayAlignment = option.displayAlignment();
        final boolean showDecorationSelected = option.showDecorationSelected();
        final int fontHeight = option.fontMetrics().height();
        final QRect rect = option.rect();        
        CellLayout resultLayout = null;        
        if (resultLayout==null){
            final boolean hasCheck = layout.checkRect!=null;
            final boolean hasPixmap = layout.decorationRect!=null;
            final boolean hasText = layout.textRect!=null;
            final int focusFrameHMargin = focusFrameMargin;
            final int textMargin = hasText ? focusFrameHMargin + 1 : 0;
            final int pixmapMargin = hasPixmap ? focusFrameHMargin + 1 : 0;
            final int checkMargin = hasCheck ? focusFrameHMargin + 1 : 0;        
            final int x = rect.left();
            final int y = rect.top();
            int w;
            int h;               

            if (hasText){
                layout.textRect.grow(-textMargin, 0);// add width padding
                if (layout.textRect.height==0  && !hasPixmap){
                    layout.textRect.height = fontHeight;
                }
            }

            int textRectHeight = layout.textRect==null ? 0 : layout.textRect.height;            

            final Dimension pm;
            if (hasPixmap) {
                pm = layout.decorationRect.getSize();
                pm.width+= 2 * pixmapMargin;
            }else{
                pm = new Dimension(0, 0);
            }

            w = rect.width();
            h = rect.height();

            final Rectangle check;
            final int cw;
            if (hasCheck) {
                cw = layout.checkRect.width + 2 * checkMargin;
                if (direction == Qt.LayoutDirection.RightToLeft) {
                    check = new Rectangle(x + w - cw, y, cw, h);
                } else {
                    check = new Rectangle(x + checkMargin, y, cw, h);
                }
            }else{
                check = new Rectangle(0,0,0,0);
                cw = 0;
            }

            // at this point w should be the *total* width

            final Rectangle display;
            final Rectangle decoration;        
            switch (position) {
                case Top: {
                    if (hasPixmap){
                        pm.height+=pixmapMargin; // add space
                    }
                    h = h - pm.height;
                    if (direction == Qt.LayoutDirection.RightToLeft) {
                        decoration = new Rectangle(x, y, w - cw, pm.height);
                        display = new Rectangle(x, y + pm.height, w - cw, h);
                    } else {
                        decoration = new Rectangle(x + cw, y, w - cw, pm.height);
                        display = new Rectangle(x + cw, y + pm.height, w - cw, h);
                    }
                    break; 
                }
                case Bottom: {
                    if (hasText){
                        layout.textRect.height = textRectHeight + textMargin;// add space
                        textRectHeight = layout.textRect.height;
                    }                    
                    if (direction == Qt.LayoutDirection.RightToLeft) {
                        display = new Rectangle(x, y, w - cw, textRectHeight);
                        decoration = new Rectangle(x, y + textRectHeight, w - cw, h - textRectHeight);
                    } else {
                        display = new Rectangle(x + cw, y, w - cw, textRectHeight);
                        decoration = new Rectangle(x + cw, y + textRectHeight, w - cw, h - textRectHeight);
                    }
                    break; 
                }
                case Left: {
                    if (direction == Qt.LayoutDirection.LeftToRight) {
                        decoration = new Rectangle(x + cw, y, pm.width, h);
                        display = new Rectangle(decoration.x + decoration.width + 1, y, w - pm.width - cw, h);
                    } else {
                        display = new Rectangle(x, y, w - pm.width - cw, h);
                        decoration = new Rectangle(display.x + display.width + 1, y, pm.width, h);
                    }
                    break; 
                }
                case Right: {
                    if (direction == Qt.LayoutDirection.LeftToRight) {
                        display = new Rectangle(x + cw, y, w - pm.width - cw, h);
                        decoration = new Rectangle(display.x + display.width + 1, y, pm.width, h);
                    } else {
                        decoration = new Rectangle(x, y, pm.width, h);
                        display = new Rectangle(decoration.x + decoration.width + 1, y, w - pm.width - cw, h);
                    }
                    break; 
                }default:{
                    decoration = layout.decorationRect==null ? new Rectangle(0,0,0,0) : layout.decorationRect;
                    display = layout.textRect==null ? new Rectangle(0,0,0,0) : layout.textRect;
                }                
            }

            final Dimension checkRectSize = layout.checkRect==null ? NULL_SIZE : layout.checkRect.getSize();
            final Rectangle resultCheckRect = 
                alignedRect(direction, ALIGNMENT_CENTER, checkRectSize, check);
            final Dimension pixmapRectSize = layout.decorationRect==null ? NULL_SIZE : layout.decorationRect.getSize();
            final Rectangle resultDecorationRect = 
                alignedRect(direction, decorationAlignment, pixmapRectSize, decoration);
            // the text takes up all available space, unless the decoration is not shown as selected
            final Rectangle resultTextRect;
            if (showDecorationSelected){
                resultTextRect = display;
            }else{
                final Dimension textRectSize = layout.textRect==null ? NULL_SIZE : layout.textRect.getSize();                
                resultTextRect = 
                    alignedRect(direction, displayAlignment, boundedTo(textRectSize, display.getSize()), display);
            }
            resultLayout = new CellLayout(resultCheckRect, resultDecorationRect, resultTextRect);
        }
        return resultLayout;
    }

    private static Rectangle alignedRect(final Qt.LayoutDirection direction, 
                                         final Qt.Alignment alignment, 
                                         final Dimension size, 
                                         final Rectangle rectangle){
        final Qt.Alignment actualAlignment = visualAlignment(direction, new Qt.Alignment(alignment.value()));
        int x = rectangle.x;
        int y = rectangle.y;
        final int w = size.width;
        final int h = size.height;
        if (actualAlignment.isSet(Qt.AlignmentFlag.AlignVCenter)){
            y += rectangle.height/2 - h/2;
        }else if (actualAlignment.isSet(Qt.AlignmentFlag.AlignBottom)){
            y += rectangle.height - h;
        }
        if (actualAlignment.isSet(Qt.AlignmentFlag.AlignRight)){
            x += rectangle.width - w;
        }
        else if (actualAlignment.isSet(Qt.AlignmentFlag.AlignHCenter)){
            x += rectangle.width/2 - w/2;
        }
        return new Rectangle(x, y, w, h);
    }    
    
    private static Dimension boundedTo(final Dimension size, final Dimension boundedTo){
        return new Dimension(Math.min(size.width, boundedTo.width), Math.min(size.height, boundedTo.height));
    }
    
    private static Qt.Alignment visualAlignment(final Qt.LayoutDirection direction, final Qt.Alignment alignment){
        if (!WidgetUtils.isSetHorizontalAlignmentFlag(alignment)){
            alignment.set(Qt.AlignmentFlag.AlignLeft);
        }
        if (!alignment.isSet(Qt.AlignmentFlag.AlignAbsolute)
            && (alignment.isSet(Qt.AlignmentFlag.AlignRight) || (alignment.isSet(Qt.AlignmentFlag.AlignLeft)) )
           ){
            if (direction==Qt.LayoutDirection.RightToLeft){
                alignment.clear(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignRight);
            }
            alignment.set(Qt.AlignmentFlag.AlignAbsolute);
        }
        return alignment;
    }    
    
    public void drawBackground(final QPainter painter, 
                               final QStyleOptionViewItem option, 
                               final QColor bgColor){
        final QStyle.State state = option.state();
        if (option.showDecorationSelected() && (state.isSet(QStyle.StateFlag.State_Selected))) {
            final QPalette.ColorGroup cg;
            if (state.isSet(QStyle.StateFlag.State_Enabled)){
                if (!state.isSet(QStyle.StateFlag.State_Active)){
                    cg = QPalette.ColorGroup.Inactive;
                }else{
                    cg = QPalette.ColorGroup.Active;
                }
            }else{
                cg = QPalette.ColorGroup.Disabled;
            }
            painter.fillRect(option.rect(), option.palette().brush(cg, QPalette.ColorRole.Highlight));
        } else {
            final QPoint oldBO = painter.brushOrigin();
            painter.setBrushOrigin(option.rect().topLeft());
            painter.fillRect(option.rect(), bgColor);
            painter.setBrushOrigin(oldBO);
        }        
    }    
    
    private static QIcon.Mode styleState2IconMode(final QStyle.State state){
        if (!state.isSet(QStyle.StateFlag.State_Enabled)){
            return QIcon.Mode.Disabled;
        }else if (state.isSet(QStyle.StateFlag.State_Selected)){
            return QIcon.Mode.Selected;
        }else{
            return QIcon.Mode.Normal;
        }
    }
    
    private static QIcon.State styleState2IconState(final QStyle.State state){
        return state.isSet(QStyle.StateFlag.State_Open) ? QIcon.State.On : QIcon.State.Off;
    }    
    
    
    public void drawDecoration(final QPainter painter, 
                               final QStyleOptionViewItem option,
                               final QIcon icon,
                               final Rectangle rect){
        drawDecoration(painter, option, icon, WidgetUtils.awtRect2QRect(rect, tmpRect));
    }

    public void drawDecoration(final QPainter painter, 
                               final QStyleOptionViewItem option,
                               final QIcon icon,
                               final QRect rect){
        final QIcon.Mode iconMode = styleState2IconMode(option.state());
        final QIcon.State iconState = styleState2IconState(option.state());
        icon.paint(painter, rect, option.decorationAlignment(), iconMode, iconState);        
    }
    
    public void drawDisplay(final QPainter painter, 
                            final QStyleOptionViewItem option, 
                            final Rectangle rect, 
                            final QFont font,
                            final String text,
                            final int focusFrameMargin,
                            final boolean hasClipping) {        
        final QPalette palette = option.palette();
        final QStyle.State state = option.state();
        final QPalette.ColorGroup cg;
        if (state.isSet(QStyle.StateFlag.State_Enabled)){
            if (state.isSet(QStyle.StateFlag.State_Active)){
                cg = QPalette.ColorGroup.Active;
            }else{
                cg = QPalette.ColorGroup.Inactive;
            }
        }else{
            cg = QPalette.ColorGroup.Disabled;
        }
        if (state.isSet(QStyle.StateFlag.State_Selected)){            
            painter.fillRect(WidgetUtils.awtRect2QRect(rect, tmpRect), palette.brush(cg, QPalette.ColorRole.Highlight));
            painter.setPen(palette.color(cg, QPalette.ColorRole.HighlightedText));
        }else{
            painter.setPen(palette.color(cg, QPalette.ColorRole.Text));
        }        

        if (text==null || text.isEmpty()){
            return;
        }

        if (state.isSet(QStyle.StateFlag.State_Editing)) {
            painter.save();
            painter.setPen(palette.color(cg, QPalette.ColorRole.Text));            
            painter.drawRect(WidgetUtils.awtRect2QRect(rect, tmpRect).adjusted(0, 0, -1, -1));
            painter.restore();
        }

        final Qt.LayoutDirection direction = option.direction();
        final Qt.Alignment alignment = option.displayAlignment();
        final QFontMetrics fontMetrics = option.fontMetrics();
        final int textMargin = focusFrameMargin + 1;
        rect.grow(-textMargin, 0);
        final int width = rect.width;
        final int height = rect.height;
        
        Dimension textLayoutSize = doTextLayout(textLayout, direction, alignment, font, text, width);

        if (width < textLayoutSize.width || width < textLayoutSize.height) {
            final String elided = 
                fontMetrics.elidedText(text, option.textElideMode(), width);            
            textLayoutSize = doTextLayout(textLayout, direction, alignment, font, elided, width);
        }

        final Dimension layoutSize = new Dimension(width, textLayoutSize.height);
        final Rectangle textRect = alignedRect(direction, alignment, layoutSize, rect);
        
        WidgetUtils.awtRect2TmpRectF(textRect, tmpRectF);
        // if we still overflow even after eliding the text, enable clipping                
        if (!hasClipping && (width < textLayoutSize.width || height < textLayoutSize.height)) {
            painter.save();
            painter.setClipRect(tmpRectF);
            textLayout.draw(painter, tmpRectF.topLeft(), Collections.<QTextLayout_FormatRange>emptyList(), tmpRectF);
            painter.restore();
        } else {
            textLayout.draw(painter, tmpRectF.topLeft(), Collections.<QTextLayout_FormatRange>emptyList(), tmpRectF);
        }        
    }      
    
    private Dimension doTextLayout(final QTextLayout textLayout,
                                   final Qt.LayoutDirection textDirection,
                                   final Qt.Alignment textAlignment,
                                   final QFont font,
                                   final String text,
                                   final int lineWidth){        
        textOption.setTextDirection(textDirection);
        textOption.setAlignment(QStyle.visualAlignment(textDirection, textAlignment));
        textLayout.setTextOption(textOption);
        textLayout.setFont(font);
        textLayout.setText(text);
        textLayout.beginLayout();
        final QTextLine line = textLayout.createLine();
        line.setLineWidth(lineWidth);
        line.setPosition(START_POINTF);
        final double height = line.height();
        final double widthUsed = line.naturalTextWidth();
        textLayout.endLayout();               
        return new Dimension((int)widthUsed, (int)height);
    }
    
}
