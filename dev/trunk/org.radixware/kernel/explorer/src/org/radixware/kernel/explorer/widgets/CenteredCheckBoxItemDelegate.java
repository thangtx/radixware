/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
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

import com.trolltech.qt.QNoSuchEnumValueException;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import static com.trolltech.qt.gui.QApplication.style;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;


public class CenteredCheckBoxItemDelegate extends QItemDelegate{

    private final int checkBoxStyleSize;
    private final QRect checkBoxRect = new QRect();
    
    public CenteredCheckBoxItemDelegate(final QObject parent){
        super(parent);
        final int checkBoxWidth = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorWidth);
        final int checkBoxHeight = style().pixelMetric(QStyle.PixelMetric.PM_IndicatorHeight);        
        checkBoxStyleSize = Math.min(checkBoxWidth, checkBoxHeight);        
    }
    
    private static Qt.CheckState getCheckState(final QAbstractItemModel model, final QModelIndex modelIndex){
        final Object data = model.data(modelIndex, Qt.ItemDataRole.CheckStateRole);
        if (data instanceof Qt.CheckState){
            return (Qt.CheckState)data;
        }else if (data instanceof Integer){            
            try{
                return Qt.CheckState.resolve((Integer)data);
            }catch(QNoSuchEnumValueException exception){
                return null;
            }
        }
        return null;
    }
    
    @Override
    public void paint(final QPainter painter, final QStyleOptionViewItem styleOption, final QModelIndex modelIndex) {        
        final QAbstractItemModel model = modelIndex.model();
        final String text = (String)model.data(modelIndex, Qt.ItemDataRole.DisplayRole);
        final QIcon icon =  (QIcon)model.data(modelIndex, Qt.ItemDataRole.DecorationRole);        
        final Qt.CheckState checkState = getCheckState(model, modelIndex);
        if ( (text==null || text.isEmpty())
             && icon==null && checkState!=null
           ){//draw only check box
            final QRect rect = styleOption.rect();
            // prepare
            painter.save();
            try{
                setupCheckBoxRect(rect);
                // draw the item
                drawBackground(painter, styleOption, modelIndex);
                drawCheck(painter, styleOption, checkBoxRect, checkState);                
            }finally{
                painter.restore();
            }
        }else{
            super.paint(painter, styleOption, modelIndex);
        }
    }

    @Override
    public boolean editorEvent(final QEvent event, 
                                            final QAbstractItemModel model, 
                                            final QStyleOptionViewItem option, 
                                            final QModelIndex index) {
        
        // make sure that the item is checkable
        final Qt.ItemFlags flags = model.flags(index);
        if (!flags.isSet(Qt.ItemFlag.ItemIsUserCheckable) 
            || !flags.isSet(Qt.ItemFlag.ItemIsEnabled) 
            || !option.state().isSet(QStyle.StateFlag.State_Enabled)
            ){
            return false;
        }
        
        final String text = (String)model.data(index, Qt.ItemDataRole.DisplayRole);
        final QIcon icon =  (QIcon)model.data(index, Qt.ItemDataRole.DecorationRole);        
        final Qt.CheckState checkState = getCheckState(model, index);
        if ( (text==null || text.isEmpty())
             && icon==null && checkState!=null
           ){            
            // make sure that we have the right event type
            final QEvent.Type eventType = event.type();
            if (event instanceof QMouseEvent) {
                setupCheckBoxRect(option.rect());
                final QMouseEvent mouseEvent = (QMouseEvent)event;
                if (mouseEvent.button()!=Qt.MouseButton.LeftButton || !checkBoxRect.contains(mouseEvent.pos()) ){
                    return false;
                }
                // eat the double click events inside the check rect
                if (eventType == QEvent.Type.MouseButtonPress || eventType == QEvent.Type.MouseButtonDblClick){
                    return true;
                }                
            }else if (event instanceof QKeyEvent && eventType==QEvent.Type.KeyPress){
                final QKeyEvent keyEvent = (QKeyEvent)event;
                if (keyEvent.key()!=Qt.Key.Key_Space.value() && keyEvent.key()!=Qt.Key.Key_Select.value()){
                    return false;
                }
            }
            final Qt.CheckState newCheckState = 
                checkState==Qt.CheckState.Checked ? Qt.CheckState.Unchecked : Qt.CheckState.Checked;
            return model.setData(index, newCheckState.value(), Qt.ItemDataRole.CheckStateRole);
        }else{
            return super.editorEvent(event, model, option, index);
        }
    }
    
    private void setupCheckBoxRect(final QRect sectionRect){
        final int checkBoxSize = Math.min(checkBoxStyleSize, Math.min(sectionRect.width(), sectionRect.height())-4);
        checkBoxRect.setWidth(checkBoxSize);
        checkBoxRect.setHeight(checkBoxSize);
        checkBoxRect.moveCenter(sectionRect.center());
        checkBoxRect.adjust(-1, 0, -1, 0);
    }
}
