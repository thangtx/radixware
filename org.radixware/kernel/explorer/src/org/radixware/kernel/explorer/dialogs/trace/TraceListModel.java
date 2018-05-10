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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPalette;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;


final class TraceListModel extends QAbstractListModel{
    
    //private static final QColor DEBUG_COLOR=QColor.black;
    private static final QColor EVENT_COLOR=QColor.blue;
    private static final QColor WARNING_COLOR=QColor.fromRgb(243, 111, 20);
    private static final QColor ERROR_COLOR=QColor.red;
    private static final QColor ALARM_COLOR=QColor.darkRed;
    
    private final IClientEnvironment environment;
    private final QSize itemSizeHint = new QSize();
    private int maxSize;
    private final List<ExplorerTraceItem> traceItems = new ArrayList<>();
    
    public TraceListModel(final int maxSize, final QObject parent, final IClientEnvironment env){
        super(parent);        
        this.maxSize = maxSize;
        environment = env;
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        if (index == null) {
            return null;
        }
        final int row = index.row();
        if (row<0 || row>=traceItems.size()){
            return null;
        }
        switch (role) {
            case ItemDataRole.DecorationRole:
                return traceItems.get(row).getIcon(environment);
            case ItemDataRole.DisplayRole:
                return traceItems.get(row).getDisplayText();
            case ItemDataRole.SizeHintRole:
                return getCachedSizeHint(traceItems.get(row));
            case ItemDataRole.UserRole:
                return traceItems.get(row);
            case ItemDataRole.ForegroundRole: {
                return getForeground(traceItems.get(row));
            }
            default:
                return null;
        }
    }
        
    private QColor getForeground(final ExplorerTraceItem traceItem){
        switch (traceItem.getSeverity()){
            case DEBUG:
                return QApplication.palette().color(QPalette.ColorRole.WindowText);
            case EVENT:
                return EVENT_COLOR;
            case WARNING:
                return WARNING_COLOR;
            case ERROR:
                return ERROR_COLOR;
            case ALARM:
                return ALARM_COLOR;
            default:
                return QApplication.palette().color(QPalette.ColorRole.WindowText);
        }
    }
    
    private QSize getCachedSizeHint(final ExplorerTraceItem traceItem){
        final Dimension cachedSize = traceItem.getCachedSize();
        if (cachedSize==null){
            return null;
        }else{
            itemSizeHint.setHeight(cachedSize.height);
            itemSizeHint.setWidth(cachedSize.width);
            return itemSizeHint;
        }
    }

    @Override
    public int rowCount(final QModelIndex parent) {
        return traceItems.size();
    }    
    
    public final void addTraceItems(final List<ExplorerTraceItem> newTraceItems){
        if (newTraceItems!=null && !newTraceItems.isEmpty()){
            if (maxSize>0 && newTraceItems.size()>=maxSize){
                traceItems.clear();
                final int limit = Math.max(newTraceItems.size() - maxSize, 0);
                for(int i=newTraceItems.size()-1;i>=limit;i--){
                    traceItems.add(newTraceItems.get(i));
                }
                reset();
            }else{
                checkSize(traceItems.size()+newTraceItems.size());
                beginInsertRows(null, 0, newTraceItems.size()-1);
                for (ExplorerTraceItem traceItem: newTraceItems){
                    traceItems.add(0, traceItem);
                }
                endInsertRows();
            }
        }
    }
    
    private final boolean checkSize(final int newSize){
        if (maxSize>=0){
            final int delta = newSize-maxSize;
            if (delta>0){
                final int from = traceItems.size()-delta;
                if (from>0){
                    final int to = traceItems.size()-1;
                    beginRemoveRows(null, from, to);
                    for(int i=to;i>=from;i--){
                        traceItems.remove(i);
                    }
                    endRemoveRows();
                    return false;
                }else{
                    traceItems.clear();
                    reset();
                    return false;
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
    
    public final void setMaxSize(final int newMaxSize){
        maxSize = newMaxSize;
        checkSize(traceItems.size());
    }
    
    public final void clear(){
        traceItems.clear();
        reset();
    }
    
    public final ExplorerTraceItem getTraceItem(final int row){
        return traceItems.get(row);
    }
}
