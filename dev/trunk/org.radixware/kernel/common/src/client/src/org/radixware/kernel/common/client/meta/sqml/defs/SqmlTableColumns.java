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

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.types.Id;

final class SqmlTableColumns implements ISqmlTableColumns{
            
    private final List<ISqmlColumnDef> columns = new ArrayList<>();
    private int firstLinkedColumnIndex = -1;
    
    public SqmlTableColumns(final List<ISqmlColumnDef> columns){
        this.columns.addAll(columns);        
    }
    
    public void linkWithTableColumns(final ISqmlTableColumns tableColumns){
        if (firstLinkedColumnIndex>-1){
            for (int i=columns.size()-1; i>=firstLinkedColumnIndex; i--){
                columns.remove(i);
            }
            firstLinkedColumnIndex = -1;
        }
        if (tableColumns.size()>0){            
            for (ISqmlColumnDef column: tableColumns){
                if (getColumnById(column.getId())==null){
                    if (firstLinkedColumnIndex<0){
                        firstLinkedColumnIndex = columns.size();
                    }
                    columns.add(column);
                }                
            }
        }else{
            firstLinkedColumnIndex = -1;
        }
    }

    @Override
    public int size() {
        return columns.size();
    }

    @Override
    public ISqmlColumnDef getColumnById(final Id columnId) {
        for (ISqmlColumnDef column: columns){
            if (columnId.equals(column.getId())){
                return column;
            }
        }
        return null;
    }

    @Override
    public ISqmlColumnDef getColumnByName(final String columnName) {
        for (ISqmlColumnDef column: columns){
            if (columnName.equals(column.getShortName())){
                return column;
            }
        }
        return null;
    }

    @Override
    public ISqmlColumnDef get(final int idx) {
        return columns.get(idx);
    }

    @Override
    public List<ISqmlColumnDef> getAll() {
        return Collections.unmodifiableList(columns);
    }

    @Override
    public Iterator<ISqmlColumnDef> iterator() {
        return columns.iterator();
    }
}
