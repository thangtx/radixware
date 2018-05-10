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

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAbstractTableModel;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QTableView;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.selector.SelectorGrid;
import org.radixware.kernel.explorer.widgets.selector.SelectorTree;

public final class ItemViewRowInfo {
    
    private final QAbstractItemView view;
    private final QModelIndex index;
    
    ItemViewRowInfo(final QAbstractItemView view, final QModelIndex index){
        this.view = view;
        this.index = index;
    }
    
    private static List<ItemViewRowInfo> getRows(final QAbstractItemView view, final QModelIndex parent){
        final QAbstractItemModel model = view.model();
        final int rowCount = model.rowCount(parent);
        if (rowCount>0){
            final List<ItemViewRowInfo> children = new LinkedList<>();
            QModelIndex rowIndex;
            for (int row=0; row<rowCount; row++){
                rowIndex = model.index(row, 0, parent);
                if (rowIndex!=null){
                    children.add(new ItemViewRowInfo(view, rowIndex));
                }
            }
            return children;
        }else{
            return Collections.emptyList();
        }        
    }
    
    public List<ItemViewRowInfo> getChildRows(){
        return getRows(view, index);
    }
    
    public List<ItemViewCellInfo> getCells(){
        final QAbstractItemModel model = view.model();
        final int columnCount = model instanceof QAbstractListModel ? 1 : model.columnCount();
        final List<ItemViewCellInfo> cells = new LinkedList<>();
        final int row = index.row();
        final QModelIndex parentIndex;
        if (model instanceof QAbstractListModel
            || model instanceof QAbstractTableModel){
            parentIndex = null;
        }else{
            parentIndex = index.parent();
        }
        for (int col=0; col<columnCount; col++){
            cells.add(new ItemViewCellInfo(view, model.index(row, col, parentIndex)));
        }
        return cells;
    }     
    
    public boolean isCurrent(){
        final QModelIndex currentIndex = view.currentIndex();
        return currentIndex!=null 
                   && currentIndex.row()==index.row() 
                   && currentIndex.internalId() == index.internalId();
    }
    
    public int getLogicalIndex(){
        return index.row();
    }
    
    public int getVisualIndex(){
        if (view instanceof QTableView){
            final QHeaderView header = ((QTableView)view).verticalHeader();
            if (header==null){
                return index.row();
            }else{
                return header.visualIndex(index.row());
            }
        }else{
            return index.row();
        }
    }

    public boolean isVisible(){
        if (view instanceof QListView){
            return ((QListView)view).isRowHidden(index.row());
        }else if (view instanceof QTableView){
            final QHeaderView header = ((QTableView)view).verticalHeader();
            return header==null ? true : !header.isSectionHidden(index.row());
        }else{
            return true;
        }
    }        
    
    public String getObjectName(){
        final Object data = view.model().data(index, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE);
        return data instanceof String ? (String)data : null;
    }

    public UIElementReference getRowReference(final WebDrvUIElementsManager manager){
        return manager.getRowReference(view, index, false);
    }
    
    public EWidgetMarker getWidgetMarker(){
        if (view instanceof SelectorTree || view instanceof SelectorGrid){
            return EWidgetMarker.SELECTOR_ROW;
        }else{
            return null;
        }
    }
    
    public static List<ItemViewRowInfo> getTopLevelRows(final QAbstractItemView view){
        return getRows(view, null);
    }
}
