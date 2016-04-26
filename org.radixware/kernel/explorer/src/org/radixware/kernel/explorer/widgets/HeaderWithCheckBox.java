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

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HeaderWithCheckBox extends QHeaderView{
    
    private final Map<Integer,Integer> checkStateForSection = new HashMap<>();    
    private final List<Integer> checkableSections = new LinkedList<>();
    private final QStyleOptionButton checkBoxStyleOption = new QStyleOptionButton();    
    private final QRect checkBoxRect = new QRect();
    private final QRect sectionRect = new QRect();
    private final QStyle.State state = new QStyle.State(0);
    private final int checkBoxStyleSize;
    
    public final Signal2<Integer,Boolean> checkBoxClicked = new Signal2<>();    
    
    public HeaderWithCheckBox(final Qt.Orientation orientation, final QWidget parent){
        super(orientation, parent);
        @SuppressWarnings("LeakingThisInConstructor")
        final int checkBoxWidth = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorWidth);
        final int checkBoxHeight = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorHeight);        
        checkBoxStyleSize = Math.min(checkBoxWidth, checkBoxHeight);
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

    @Override
    protected void paintSection(final QPainter painter, final QRect rect, final int logicalIndex) {        
        final Integer checkValue = checkStateForSection.get(Integer.valueOf(logicalIndex));
        if (checkValue==null){
          super.paintSection(painter, rect, logicalIndex);  
        }else{
            painter.save();
            super.paintSection(painter, rect, logicalIndex);  
            painter.restore();
            setupCheckBoxRect(rect);
            checkBoxStyleOption.setRect(checkBoxRect);
            state.clearAll();
            if (checkValue==Qt.CheckState.Checked.value()){
                state.set(QStyle.StateFlag.State_On);
            }else if (checkValue==Qt.CheckState.PartiallyChecked.value()){
                state.set(QStyle.StateFlag.State_NoChange);
            }else{
                state.set(QStyle.StateFlag.State_Off);
            }
            if (isEnabled()) {
                state.set(QStyle.StateFlag.State_Enabled);
            }
            if (isActiveWindow()) {
                state.set(QStyle.StateFlag.State_Active);
            }
            state.set(QStyle.StateFlag.State_Raised);
            checkBoxStyleOption.setState(state);
            style().drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorCheckBox, checkBoxStyleOption, painter);
        }
    }

    @Override
    protected void mousePressEvent(final QMouseEvent event) {
        final int visualIndex = visualIndexAt(event.x());
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
        final int sectionWidth = sectionSize(logicalIndex);
        final int sectionHeight = height();
        sectionRect.setRect(0, 0, sectionWidth, sectionHeight);
        setupCheckBoxRect(sectionRect);
        final int sectionClickPosX = x - sectionPosition(logicalIndex);
        return checkBoxRect.contains(sectionClickPosX, y);
    }
    
    private void setupCheckBoxRect(final QRect sectionRect){                        
        final int checkBoxSize = Math.min(checkBoxStyleSize, Math.min(sectionRect.width(), sectionRect.height())-4);
        checkBoxRect.setWidth(checkBoxSize);
        checkBoxRect.setHeight(checkBoxSize);
        checkBoxRect.moveCenter(sectionRect.center());
        checkBoxRect.adjust(-1, 0, -1, 0);
    }
}
