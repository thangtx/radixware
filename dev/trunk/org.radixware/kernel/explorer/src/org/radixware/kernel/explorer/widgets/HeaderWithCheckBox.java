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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemSelectionModel;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HeaderWithCheckBox extends QHeaderView{
    
    private final Map<Integer,Integer> checkStateForSection = new HashMap<>();    
    private final Set<Integer> checkableSections = new HashSet<>();
    private final QStyleOptionButton checkBoxStyleOption = new QStyleOptionButton();    
    private final QRect checkBoxRect = new QRect();
    private final QRect textRect = new QRect();
    private final QRect sectionRect = new QRect();    
    private final QStyle.State checkBoxState = new QStyle.State(0);
    private final QStyle.State sectionState = new QStyle.State(0);
    private final int checkBoxStyleSize;
    private final int frameMargin;
    private final Qt.Orientation orientation;
    private final Qt.LayoutDirection layoutDirection;
    private final QStyleOptionHeader styleOptionHeader = new QStyleOptionHeader();
    
    public final Signal2<Integer,Boolean> checkBoxClicked = new Signal2<>();    
    
    public HeaderWithCheckBox(final Qt.Orientation orientation, final QWidget parent){
        super(orientation, parent);
        @SuppressWarnings("LeakingThisInConstructor")
        final int checkBoxWidth = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorWidth);
        final int checkBoxHeight = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorHeight);        
        checkBoxStyleSize = Math.min(checkBoxWidth, checkBoxHeight);
        this.orientation = orientation;
        frameMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin)+1;         
        this.initStyleOption(styleOptionHeader);
        layoutDirection = styleOptionHeader.direction();
     }
    
    public final void setSectionUserCheckable(final int section, final boolean isCheckable){
        if (isCheckable){
            checkableSections.add(section);            
        }else{
            checkableSections.remove(Integer.valueOf(section));
        }
    }

    public final boolean isSectionUserCheckable(final int section){
        return checkableSections.contains(section);
    }
    
    public final void setSectionCheckState(final int section, final Qt.CheckState checkState){        
        if (checkState==null){
            checkStateForSection.remove(section);
        }else{
            checkStateForSection.put(section,checkState.value());
        }
        headerDataChanged(orientation(), section, section);
    }
    
    public final Qt.CheckState getSectionCheckState(final int section){
        final Integer value = checkStateForSection.get(Integer.valueOf(section));
        if (value==null){
            return null;
        }else{
            return Qt.CheckState.resolve(value);
        }
    }
    
    private boolean hasTextOrDecoration(final int logicalIndex){
        final Object data = model().headerData(logicalIndex, orientation, Qt.ItemDataRole.DisplayRole);
        if (data instanceof String && !((String)data).isEmpty()){
            return true;
        }
        return model().headerData(logicalIndex, orientation, Qt.ItemDataRole.DisplayRole) instanceof QIcon;
    }

    @Override
    protected void paintSection(final QPainter painter, final QRect rect, final int sectionIndex) {        
        final Integer checkValue = checkStateForSection.get(Integer.valueOf(sectionIndex));
        if (checkValue==null){
          super.paintSection(painter, rect, sectionIndex);  
        }else{
            final Object display = model().headerData(sectionIndex, orientation, Qt.ItemDataRole.DisplayRole);
            final String text = display instanceof String ? (String)display : "";
            final Object decoration = model().headerData(sectionIndex, orientation, Qt.ItemDataRole.DecorationRole);
            final QIcon icon = decoration instanceof QIcon ? (QIcon)decoration : null;            
            final boolean hasTextOrDecoration = !text.isEmpty() || (icon!=null && !icon.isNull());
            if (hasTextOrDecoration){
                if (!rect.isValid()){
                    return;
                }
                painter.save();
                this.initStyleOption(styleOptionHeader);
                updateSectionState(rect, sectionIndex);
                updateSectionStyleOption(painter, rect, sectionIndex, ""/*paint with no text*/, icon);                
                style().drawControl(QStyle.ControlElement.CE_Header, styleOptionHeader, painter, this);
                painter.restore();
                
                if (!text.isEmpty()){
                    final Qt.TextElideMode elideMode = textElideMode();
                    if (elideMode!=Qt.TextElideMode.ElideNone){
                        final String elidedText = styleOptionHeader.fontMetrics().elidedText(text, elideMode, rect.width() - 4);
                        styleOptionHeader.setText(elidedText);
                    }else{
                        styleOptionHeader.setText(text);
                    }
                    setupTextRect(rect);
                    styleOptionHeader.setRect(textRect);
                    style().drawControl(QStyle.ControlElement.CE_HeaderLabel, styleOptionHeader, painter, this);
                    setupCheckBoxRect(rect, true);
                    drawCheckBox(painter, checkBoxRect, checkValue);
                }
            }else{
                painter.save();
                super.paintSection(painter, rect, sectionIndex);  //draw empty section
                painter.restore();
                setupCheckBoxRect(rect, false);
                drawCheckBox(painter, checkBoxRect, checkValue);
            }
        }
    }
    
    private void updateSectionState(final QRect rect, final int sectionIndex){
        sectionState.clearAll();
        if (isEnabled()){
            sectionState.set(QStyle.StateFlag.State_Enabled);
        }
        if (window().isActiveWindow()){
            sectionState.set(QStyle.StateFlag.State_Active);
        }
        if (isClickable()){
            final QPoint cursorPos = mapFromGlobal(QCursor.pos());
            final boolean containMouse = rect.contains(cursorPos);                          
            if (containMouse){
                sectionState.set(QStyle.StateFlag.State_MouseOver);
            }
            if (containMouse 
                && (QApplication.mouseButtons().isSet(Qt.MouseButton.LeftButton) || QApplication.mouseButtons().isSet(Qt.MouseButton.RightButton))){
                sectionState.set(QStyle.StateFlag.State_Sunken);
            }else if (highlightSections() && sectionIntersectsSelection(sectionIndex)){
                sectionState.set(QStyle.StateFlag.State_On);
            }
        }
    }
    
    private Qt.Alignment getAlignment(final int sectionIndex){
        final Object data = model().headerData(sectionIndex, orientation, Qt.ItemDataRole.TextAlignmentRole);
        if (data instanceof Qt.Alignment){            
            return (Qt.Alignment)data;
        }else if (data instanceof Integer && (Integer)data!=0){
            return new Qt.Alignment((Integer)data);
        }else{
            return defaultAlignment();
        }
    }
    
    private QBrush getForeground(final int sectionIndex){
        final Object data = model().headerData(sectionIndex, orientation, Qt.ItemDataRole.ForegroundRole);
        return data instanceof QBrush ? (QBrush)data : null;
    }
    
    private QBrush getBackground(final int sectionIndex){
        final Object data = model().headerData(sectionIndex, orientation, Qt.ItemDataRole.BackgroundRole);
        return data instanceof QBrush ? (QBrush)data : null;
    }    
    
    private void updateSectionStyleOption(final QPainter painter, 
                                                   final QRect rect,
                                                   final int sectionIndex,
                                                   final String text,
                                                   final QIcon icon){        
        if (isSortIndicatorShown() && sortIndicatorSection() == sectionIndex){
            if (sortIndicatorOrder()==Qt.SortOrder.AscendingOrder){
                styleOptionHeader.setSortIndicator(QStyleOptionHeader.SortIndicator.SortDown);
            }else{
                styleOptionHeader.setSortIndicator(QStyleOptionHeader.SortIndicator.SortUp);
            }            
        }
        
        // setup the style options structure
        Qt.Alignment textAlignment = getAlignment(sectionIndex);
        styleOptionHeader.setRect(rect);
        styleOptionHeader.setSection(sectionIndex);
        sectionState.set(styleOptionHeader.state());
        styleOptionHeader.setState(sectionState);
        styleOptionHeader.setTextAlignment(textAlignment);
        styleOptionHeader.setIconAlignment(Qt.AlignmentFlag.AlignVCenter);        
        final Qt.TextElideMode elideMode = textElideMode();
        if (elideMode!=Qt.TextElideMode.ElideNone && !text.isEmpty()){
            final String elidedText = styleOptionHeader.fontMetrics().elidedText(text, elideMode, rect.width() - 4);
        }else{
            styleOptionHeader.setText(text);
        }        
        styleOptionHeader.setIcon(icon);
        final QBrush foregroundBrush = getForeground(sectionIndex);
        final QPalette palette = new QPalette(styleOptionHeader.palette());                       
        boolean paletteChanged = false;
        if (foregroundBrush!=null){
            palette.setBrush(QPalette.ColorRole.ButtonText, foregroundBrush);
            paletteChanged = true;
        }
        final QBrush backgroundBrush = getBackground(sectionIndex);
        if (backgroundBrush!=null){
            palette.setBrush(QPalette.ColorRole.Button, backgroundBrush);
            palette.setBrush(QPalette.ColorRole.Window, backgroundBrush);
            paletteChanged = true;
            painter.setBrushOrigin(rect.topLeft());
        }
        if (paletteChanged){
            styleOptionHeader.setPalette(palette);
        }
               
        // the section position
        int visual = visualIndex(sectionIndex);
        final QStyleOptionHeader.SectionPosition sectionPosition;
        if (count() == 1){
            sectionPosition = QStyleOptionHeader.SectionPosition.OnlyOneSection;            
        }else if (visual == 0){
            sectionPosition = QStyleOptionHeader.SectionPosition.Beginning;            
        }else if (visual == count() - 1){
            sectionPosition = QStyleOptionHeader.SectionPosition.End;
        }else{
            sectionPosition = QStyleOptionHeader.SectionPosition.Middle;
        }
        styleOptionHeader.setPosition(sectionPosition);
        styleOptionHeader.setOrientation(orientation);   
        
        
        final boolean previousSelected = sectionIntersectsSelection(logicalIndex(visual - 1));
        final boolean nextSelected = sectionIntersectsSelection(logicalIndex(visual + 1));
        final QStyleOptionHeader.SelectedPosition selectedPosition;
        if (previousSelected && nextSelected){
            selectedPosition = QStyleOptionHeader.SelectedPosition.NextAndPreviousAreSelected;
        }else if (previousSelected){
            selectedPosition = QStyleOptionHeader.SelectedPosition.PreviousIsSelected;
        }else if (nextSelected){
            selectedPosition = QStyleOptionHeader.SelectedPosition.NextIsSelected;
        }else{
            selectedPosition = QStyleOptionHeader.SelectedPosition.NotAdjacent;
        }        
        styleOptionHeader.setSelectedPosition(selectedPosition);
    }
    
    protected void drawCheckBox(final QPainter painter, final QRect rect, final Integer checkValue){
        checkBoxStyleOption.setRect(rect);
        checkBoxState.clearAll();
        if (checkValue==Qt.CheckState.Checked.value()){
            checkBoxState.set(QStyle.StateFlag.State_On);
        }else if (checkValue==Qt.CheckState.PartiallyChecked.value()){
            checkBoxState.set(QStyle.StateFlag.State_NoChange);
        }else{
            checkBoxState.set(QStyle.StateFlag.State_Off);
        }
        if (isEnabled()) {
            checkBoxState.set(QStyle.StateFlag.State_Enabled);
        }
        if (isActiveWindow()) {
            checkBoxState.set(QStyle.StateFlag.State_Active);
        }
        checkBoxState.set(QStyle.StateFlag.State_Raised);
        checkBoxStyleOption.setState(checkBoxState);
        style().drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorCheckBox, checkBoxStyleOption, painter);
    }

    @Override
    protected QSize sectionSizeFromContents(final int sectionIndex) {
        final QSize defaultSize = super.sectionSizeFromContents(sectionIndex);
        if (checkStateForSection.get(Integer.valueOf(sectionIndex))!=null &&
            hasTextOrDecoration(sectionIndex)
           ){
            defaultSize.setWidth(defaultSize.width()+checkBoxStyleSize+frameMargin+1);
        }
        return defaultSize;
    }
       
    @Override
    protected void mousePressEvent(final QMouseEvent event) {
        final int visualIndex = visualIndexAt(orientation==Qt.Orientation.Horizontal ? event.x() : event.y());
        if (visualIndex>=0){
            final int logicalIndex = logicalIndex(visualIndex);
            if (checkableSections.contains(logicalIndex) 
                && checkStateForSection.containsKey(logicalIndex)
                && isPointInsideCheckBoxRect(event.x(), event.y(), logicalIndex)){
                final Integer checkValue = checkStateForSection.get(Integer.valueOf(logicalIndex));
                final Qt.CheckState newState;
                if (checkValue==Qt.CheckState.Unchecked.value()){
                    newState = Qt.CheckState.Checked;
                }else{
                    newState = Qt.CheckState.Unchecked;                    
                }
                setSectionCheckState(logicalIndex,newState);
                checkBoxClicked.emit(logicalIndex, newState==Qt.CheckState.Checked);
                headerDataChanged(orientation(), logicalIndex, logicalIndex);
            }else{
                super.mousePressEvent(event);
            }
        }else{
            super.mousePressEvent(event);
        }
    }
    
    private boolean isPointInsideCheckBoxRect(final int x, final int y, final int logicalIndex){
        final int sectionWidth = orientation==Qt.Orientation.Horizontal ? sectionSize(logicalIndex) : width();
        final int sectionHeight = orientation==Qt.Orientation.Horizontal ? height() : sectionSize(logicalIndex);
        sectionRect.setRect(0, 0, sectionWidth, sectionHeight);
        setupCheckBoxRect(sectionRect, hasTextOrDecoration(logicalIndex));
        final int sectionClickPosX;
        final int sectionClickPosY;
        if (orientation==Qt.Orientation.Horizontal){
            sectionClickPosX = x - sectionPosition(logicalIndex) + offset();
            sectionClickPosY = y;
        }else{
            sectionClickPosX = x;
            sectionClickPosY = y - sectionPosition(logicalIndex) + offset();
        }
        return checkBoxRect.contains(sectionClickPosX, sectionClickPosY);
    }   
    
    private void setupCheckBoxRect(final QRect sectionRect, final boolean hasTextOrDecoration){                                
        final int checkBoxSize = Math.min(checkBoxStyleSize, Math.min(sectionRect.width(), sectionRect.height())-4);
        checkBoxRect.setWidth(checkBoxSize);
        checkBoxRect.setHeight(checkBoxSize);
        checkBoxRect.moveCenter(sectionRect.center());        
        checkBoxRect.adjust(-1, 0, -1, 0);
        if (hasTextOrDecoration){
            if (layoutDirection==Qt.LayoutDirection.LeftToRight){
                checkBoxRect.moveLeft(sectionRect.left()+frameMargin);
            }else{
                checkBoxRect.moveRight(sectionRect.right()-frameMargin);
            }
        }
    }
    
    private void setupTextRect(final QRect sectionRect){
        textRect.setRect(sectionRect.x(), sectionRect.y(), sectionRect.width(), sectionRect.height());
        final int delta = checkBoxStyleSize+2*frameMargin;
        if (layoutDirection==Qt.LayoutDirection.LeftToRight){            
            textRect.adjust(delta, 0, 0, 0);
        }else{
            textRect.adjust(0, 0, -delta, 0);
        }
    }
    
    private boolean sectionIntersectsSelection(final int sectionIndex){        
        if (sectionIndex<0){
            return false;
        }
        final QItemSelectionModel selectionModel = selectionModel();
        if (selectionModel==null){
            return false;
        }else{
            final QModelIndex root = rootIndex();
            if (orientation==Qt.Orientation.Horizontal){
                return selectionModel.columnIntersectsSelection(sectionIndex, root);
            }else{
                return selectionModel.rowIntersectsSelection(sectionIndex, root);
            }
        }
    }    
}
