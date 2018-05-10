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

import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCommonStyle;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QRegion;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class SelectorHeaderStyle extends WidgetUtils.CustomStyle {
    
    public final static int SORTING_INDICATOR_MARGIN = 2;
            
    private final static Qt.Alignment ALIGNMENT_LEFT = new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter, Qt.AlignmentFlag.AlignLeft);
    private final static QTextOption  OPTION_ALIGNMENT_LEFT = new QTextOption(ALIGNMENT_LEFT);    
    private final static int ALIGNMENT_TOP = new Qt.Alignment(Qt.AlignmentFlag.AlignTop).value();
    private final static QTextOption OPTION_ALIGNMENT_TOP = new QTextOption(Qt.AlignmentFlag.AlignTop);
    private final static QCommonStyle COMMON_STYLE = new QCommonStyle();
    
    private final SelectorModel model;
    private final QRectF arrowRect = new QRectF();    
    
    public SelectorHeaderStyle(final QWidget sourceWidget, final SelectorModel model) {
        super(sourceWidget);
        this.model = model;
    }
    
    @Override
    public QRect subElementRect(final SubElement subElement, final QStyleOption styleOpt, final QWidget widget) {
        final QStyleOptionHeader headerOpt;
        if (styleOpt instanceof QStyleOptionHeader){
            headerOpt =  (QStyleOptionHeader) styleOpt;
            if (headerOpt.text()==null || headerOpt.text().isEmpty()){
                headerOpt.setIconAlignment(Qt.AlignmentFlag.AlignCenter);
            }            
        }else{
            headerOpt =  null;
        }
        if(headerOpt!=null && subElement == SubElement.SE_HeaderArrow) {            
            headerOpt.setTextAlignment(ALIGNMENT_LEFT);
            final QFont font = (QFont) model.headerData(headerOpt.section(), Qt.Orientation.Horizontal, Qt.ItemDataRole.FontRole);
            final String indicator = (String) model.headerData(headerOpt.section(), Qt.Orientation.Horizontal, SelectorModel.SORT_INDICATOR_ITEM_ROLE);
            return calcArrowRect(font.family(), indicator, styleOpt, widget);
        } else {
            return super.subElementRect(subElement, styleOpt, widget);
        }
    }
    
    private static QRect calcArrowRect(final String fontFamily, 
                                       final String indicator, 
                                       final QStyleOption styleOpt, 
                                       final QWidget widget){
        final QRect standartRect = COMMON_STYLE.subElementRect(SubElement.SE_HeaderArrow, styleOpt, widget);
        ExplorerFont arrowFont = ExplorerFont.Factory.getFont(fontFamily, 12);
        QFontMetrics fontMetrics = arrowFont.getQFontMetrics();            
        final String arrow = indicator.substring(0, 1);
        final QSize fontRectSize = fontMetrics.boundingRect(styleOpt.rect(), ALIGNMENT_LEFT.value(), arrow).size();
        final String priority = indicator.substring(1);
        fontMetrics = arrowFont.changeSize(7).getQFontMetrics();
        fontRectSize.setWidth(fontRectSize.width() + 
                fontMetrics.boundingRect(styleOpt.rect(), ALIGNMENT_TOP, priority).size().width());

        final int verticalAdjustAmount = standartRect.height() < fontRectSize.height() ? (standartRect.height() - fontRectSize.height())/2 : 0;
        standartRect.adjust(
                standartRect.width() < fontRectSize.width() ? standartRect.width() - fontRectSize.width(): 0,
                verticalAdjustAmount,
                0,
                -verticalAdjustAmount);
        return standartRect;
    }

    @Override
    public void drawPrimitive(final PrimitiveElement primitive, 
                                final QStyleOption options, 
                                final QPainter painter, 
                                final QWidget widget) {
        if(primitive == PrimitiveElement.PE_IndicatorHeaderArrow) {
            //Draws a sort indicator
            final QStyleOptionHeader headerOpt =  (QStyleOptionHeader) options;
            final String indicator = 
                (String) model.headerData(headerOpt.section(), Qt.Orientation.Horizontal, SelectorModel.SORT_INDICATOR_ITEM_ROLE);
            final String arrow = indicator.substring(0, 1);
            final String priority = indicator.substring(1);
                        
            painter.save();
            painter.setPen(QColor.darkGray);
            ExplorerFont arrowFont = ExplorerFont.Factory.getFont(painter.font().family(),12);
            painter.setFont(arrowFont.getQFont());
            
            final QRect rect = options.rect();
            arrowRect.setRect(rect.x(), rect.y(), rect.width(), rect.height());            
            painter.drawText(arrowRect, arrow, OPTION_ALIGNMENT_LEFT);
            final QRectF priorityRect = 
                arrowRect.adjusted(painter.fontMetrics().boundingRect(arrow).width() + 4, 0, 0, 0);
            painter.setFont(arrowFont.changeSize(7).getQFont());
            painter.drawText(priorityRect, priority, OPTION_ALIGNMENT_TOP);
            painter.restore();
        } else {
            super.drawPrimitive(primitive, options, painter, widget);
        }
    }

    @Override
    public void drawControl(final ControlElement element, 
                            final QStyleOption options, 
                            final QPainter painter, 
                            final QWidget widget) {
        if(element == ControlElement.CE_Header) {
            final QStyleOptionHeader headerOpt = (QStyleOptionHeader) options;            
            if (headerOpt.text()==null || headerOpt.text().isEmpty()){
                headerOpt.setIconAlignment(Qt.AlignmentFlag.AlignCenter);
            }
            final QRect labelRect = subElementRect(SubElement.SE_HeaderLabel, headerOpt, widget);
            final String headerData = (String) model.headerData(headerOpt.section(), Qt.Orientation.Horizontal, SelectorModel.SORT_INDICATOR_ITEM_ROLE);
            if(headerData != null) {
                if(headerData.contains(SelectorSortUtils.ASC_ARROW)) {
                    headerOpt.setSortIndicator(QStyleOptionHeader.SortIndicator.SortUp);
                } else if(headerData.contains(SelectorSortUtils.DESC_ARROW)) {
                    headerOpt.setSortIndicator(QStyleOptionHeader.SortIndicator.SortDown);
                } else {
                    headerOpt.setSortIndicator(QStyleOptionHeader.SortIndicator.None);
                }
            }
            
            final QRegion clipRegion = painter.clipRegion();
            painter.setClipRect(options.rect());
            drawControl(ControlElement.CE_HeaderSection, headerOpt, painter, widget);
            final QStyleOptionHeader subopt = headerOpt.clone();                   
            final QRect indicatorRect;
            if (headerOpt.sortIndicator() == QStyleOptionHeader.SortIndicator.None) {
                indicatorRect = null;                
            } else{
                indicatorRect = subElementRect(SubElement.SE_HeaderArrow, headerOpt, widget);   
                if (labelRect.isValid()){
                    final int width = indicatorRect.width();
                    indicatorRect.setLeft(labelRect.right()-width);
                    indicatorRect.setWidth(width);
                }
                subopt.setRect( indicatorRect  );
                drawPrimitive(PrimitiveElement.PE_IndicatorHeaderArrow, subopt, painter, widget);
            }
            if (labelRect.isValid()) {
                if (indicatorRect!=null) {
                    labelRect.setRight(labelRect.right()-indicatorRect.width()-SORTING_INDICATOR_MARGIN);
                }
                final String headerText = (String) model.headerData(headerOpt.section(), Qt.Orientation.Horizontal);
                final QRect textRect = painter.fontMetrics().boundingRect(headerOpt.rect(),Qt.AlignmentFlag.AlignCenter.value(), headerText);
                if(textRect.width() > labelRect.width()) {
                    subopt.setTextAlignment(Qt.AlignmentFlag.AlignLeft);
                    subopt.setText(painter.fontMetrics().elidedText(headerText, Qt.TextElideMode.ElideRight, labelRect.width()));
                }
                subopt.setRect(labelRect);
                drawControl(ControlElement.CE_HeaderLabel, subopt, painter, widget);
            }
            painter.setClipRegion(clipRegion);
        } else {
             super.drawControl(element, options, painter, widget);
        }
    }
}
