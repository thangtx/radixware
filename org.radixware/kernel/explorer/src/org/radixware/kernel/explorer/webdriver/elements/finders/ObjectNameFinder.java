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

package org.radixware.kernel.explorer.webdriver.elements.finders;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;

final class ObjectNameFinder extends UIElementFinder{
    
    private final static Pattern CELL_INDEX_NAME_PATTERN = Pattern.compile("rowPath: ([\\d+/]+);column: (\\d+)");
    private final static Pattern ROW_INDEX_NAME_PATTERN = Pattern.compile("rowPath: ([\\d+/]+)");
    
    private final String objectName;
    private final int column;
    private final int[] rowPath;
    
    public ObjectNameFinder(final String objectName){
        this.objectName = objectName;
        final Matcher cellIndexMatcher = CELL_INDEX_NAME_PATTERN.matcher(objectName);
        if (cellIndexMatcher.find()){
            rowPath = parseRowPath(cellIndexMatcher.group(1));
            column = Integer.valueOf(cellIndexMatcher.group(2));            
        }else{
            final Matcher rowIndexMatcher = ROW_INDEX_NAME_PATTERN.matcher(objectName);
            if (rowIndexMatcher.find()){
                rowPath = parseRowPath(cellIndexMatcher.group(1));
                column = -1;
            }else{
                column = -1;
                rowPath = null;
            }
        }
    }
    
    private static int[] parseRowPath(final String pathAsStr){
        final String[] rowArr = pathAsStr.split("/");
        final int[] path = new int[rowArr.length];
        for (int i=0; i<path.length; i++){
            path[i] = Integer.valueOf(rowArr[i]);
        }
        return path;
    }
       
    @Override
    protected boolean isTarget(final QWidget widget) {
        return objectName.equals(widget.objectName());
    }
    
    @Override
    protected boolean isTarget(final QAction action) {
        return objectName.equals(action.objectName());
    }    

    @Override
    protected Collection<UIElementFinder.ItemViewIndex> findIndexes(final QAbstractItemModel model, final boolean greedy, final boolean useObjNames) {
        final Collection<UIElementFinder.ItemViewIndex> result = super.findIndexes(model, greedy, true);
        final int columnIndex = column<0 ? 0 : column;
        final int columnCount = model instanceof QAbstractListModel ? 1 : model.columnCount();
        if (result.isEmpty() && rowPath!=null && columnIndex<columnCount){
            QModelIndex index = null;
            for (int row: rowPath){
                if (row>=model.rowCount(index)){
                    return result;
                }
                index = model.index(row, columnIndex, index);
                if (index==null){
                    return result;
                }
            }
            return Collections.singletonList(column<0 ? createRowIndex(index, false) : createCellIndex(index, false));
        }else{
            return result;
        }
    }

    @Override
    protected boolean isTargetRow(final QAbstractItemModel model, final QModelIndex index) {
        final Object data = model.data(index, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE); 
        return data instanceof String ? data.equals(objectName) : false;
    }
            
    @Override
    protected boolean isTargetCell(final QAbstractItemModel model, final QModelIndex index) {
        final Object data = model.data(index, WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE); 
        return data instanceof String ? data.equals(objectName) : false;
    }

    @Override
    protected boolean isTargetTabButton(final QTabBar tabSet, final int index) {
        return objectName.equals(WebDrvUIElementsManager.getTabName(tabSet, index));
    }      
}
