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

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QTreeView;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class ItemViewCellInfo {

    private final QAbstractItemView view;
    private final QModelIndex index;
    
    ItemViewCellInfo(final QAbstractItemView view, final QModelIndex index){
        this.view = view;
        this.index = index;
    }        
    
    private QHeaderView getHeader(){
        if (view instanceof QTableView){
            return ((QTableView)view).horizontalHeader();
        }else if (view instanceof QTreeView){
            return ((QTreeView)view).header();
        }else{
            return null;
        }        
    }
    
    private String getStringData(final int dataRole){
        final Object data = view.model().data(index, dataRole);
        return data instanceof String ? (String)data : null;
    }    
    
    private Boolean getBooleanData(final int dataRole){
        final Object data = view.model().data(index, dataRole);
        return data instanceof Boolean ? (Boolean)data : null;
    }    
    
    
    private String getColorData(final int dataRole){
        final Object data = view.model().data(index, dataRole);
        if (data instanceof QColor){
            return ((QColor)data).name();
        }else if (data instanceof QBrush){
            return ((QBrush)data).color().name();
        }else{
            return null;
        }        
    }
    
    public boolean isVisible(){
        final QHeaderView header = getHeader();
        return header==null ? true: !header.isSectionHidden(index.column());
    }
    
    public boolean isCurrent(){
        final QModelIndex currentIndex = view.currentIndex();
        return currentIndex!=null 
                   && currentIndex.row()==index.row() 
                   && currentIndex.column()==index.column() 
                   && currentIndex.internalId() == index.internalId();
    }    
    
    public int getLogicalIndex(){
        return index.column();
    }
    
    public int getVisualIndex(){
        final QHeaderView header = getHeader();
        return header==null ? index.column() : header.visualIndex(index.column());
    }
    
    public String getObjectName(){
        return getStringData(WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE);
    }
    
    public String getValueAsStr(){
        return getStringData(WidgetUtils.MODEL_ITEM_CELL_VALUE_DATA_ROLE);
    }
    
    public Boolean getValueIsNull(){
        return getBooleanData(WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE);
    }

    public String getDisplayText(){
        return getStringData(Qt.ItemDataRole.DisplayRole);
    }
    
    public String getToolTip(){
        return getStringData(Qt.ItemDataRole.ToolTipRole);
    }
    
    public String getStatusTip(){
        return getStringData(Qt.ItemDataRole.StatusTipRole);
    }
    
    public String getAccessibleText(){
        return getStringData(Qt.ItemDataRole.AccessibleTextRole);
    }
    
    public String getAccessibleDescription(){
        return getStringData(Qt.ItemDataRole.AccessibleDescriptionRole);
    }
    
    public String getForegroundColor(){
        return getColorData(Qt.ItemDataRole.ForegroundRole);
    }
    
    public String getBackgroundColor(){
        return getColorData(Qt.ItemDataRole.BackgroundRole);
    }
    
    public QFont getFont(){
        final Object data = view.model().data(index, Qt.ItemDataRole.FontRole);
        return data instanceof QFont ? (QFont)data : null;
    }
    
    public Qt.Alignment getTextAlignment(){
        final Object data = view.model().data(index, Qt.ItemDataRole.TextAlignmentRole);
        if (data instanceof Qt.Alignment){
            return (Qt.Alignment)data;
        }else if (data instanceof Qt.AlignmentFlag){
            return new Qt.Alignment((Qt.AlignmentFlag)data);
        }else if (data instanceof Integer){
            return new Qt.Alignment((Integer)data);
        }else{
            return null;
        }
    }
    
    public Qt.CheckState getCheckState(){
        final Object data = view.model().data(index, Qt.ItemDataRole.CheckStateRole);
        if (data instanceof Qt.CheckState){
            return (Qt.CheckState)data;
        }else if (data instanceof Integer){
            try{
                return Qt.CheckState.resolve((Integer)data);
            }catch(com.trolltech.qt.QNoSuchEnumValueException exception){
                return null;
            }
        }else{
            return null;
        }
    }
    
    public QSize getSizeHint(){        
        final Object data = view.model().data(index, Qt.ItemDataRole.SizeHintRole);
        return data instanceof QSize ? (QSize)data : null;
    }
    
    public Qt.ItemFlags getFlags(){
        return view.model().flags(index);
    }
    
    public UIElementReference getCellReference(final WebDrvUIElementsManager manager){
        return manager.getCellReference(view, index, false);
    }
}
