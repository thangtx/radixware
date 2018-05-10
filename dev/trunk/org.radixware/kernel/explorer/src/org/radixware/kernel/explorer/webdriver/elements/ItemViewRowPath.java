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
import java.util.Objects;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.explorer.utils.WidgetUtils;

final class ItemViewRowPath {
    
    private final ArrStr path = new ArrStr();
    
    private ItemViewRowPath(){
        
    }
    
    public static ItemViewRowPath newInstance(final QModelIndex index, final boolean useObjNames){
        final ItemViewRowPath instance = new ItemViewRowPath();
        final QAbstractItemModel model = index.model();
        if (model instanceof QAbstractListModel){
            instance.path.add(getPathItem(model, index, useObjNames));
        }else{
            for (QModelIndex idx=index; idx!=null; idx=idx.parent()){
                instance.path.add(0, getPathItem(model, idx, useObjNames));
            }
        }
        return instance;
    }
    
    private static String getPathItem(final QAbstractItemModel model, final QModelIndex index, final boolean useObjNames){
        final String rowName;
        final Object objName;        
        if (useObjNames){
            objName = model.data(index, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE);
            rowName = objName instanceof String ? (String)objName : null;
        }else{
            rowName = null;
        }
        if (rowName==null){
            return "index: "+String.valueOf(index.row());
        }else{
            return "name: "+rowName;
        }        
    }
    
    public QModelIndex resolve(final QAbstractItemModel model){
        QModelIndex result = null;
        int rowIndex, rowCount;
        String rowName;
        Object data;
        for(String row: path){
            rowCount = model.rowCount(result);
            if (row.startsWith("index: ")){
                rowIndex = Integer.valueOf(row.substring(7));
                if (rowCount<=rowIndex){
                    rowIndex = -1;
                }                
            }else{
                rowName = row.substring(6);
                rowIndex = -1;
                for (int r=0; r<rowCount; r++){
                    if (rowName.equals(model.data(result, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE))){
                        rowIndex = r;
                        break;
                    }                    
                }
            }
            if (rowIndex>-1){
                result = model.index(rowIndex, 0, result);
                if (result==null){
                    return null;
                }
            }else{
                return null;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return path.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemViewRowPath other = (ItemViewRowPath) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return path.toString();
    }    
}
