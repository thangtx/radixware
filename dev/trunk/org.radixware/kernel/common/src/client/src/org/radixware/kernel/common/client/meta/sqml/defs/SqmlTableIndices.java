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
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.types.Id;


final class SqmlTableIndices implements ISqmlTableIndices{
    
    public final static SqmlTableIndices EMPTY = new SqmlTableIndices();
    
    private final ISqmlTableIndexDef primaryIndex;
    private final List<ISqmlTableIndexDef> indices = new ArrayList<>();    
    
    private SqmlTableIndices(){
        primaryIndex = null;
    }    
    
    public SqmlTableIndices(final List<SqmlTableIndexImpl> indices){
        if (indices!=null && !indices.isEmpty()){
            int primaryKeyIndex = -1; 
            SqmlTableIndexImpl index;
            for (int i=0, count=indices.size(); i<count; i++){
                index = indices.get(i);
                if (index.isPrimaryKey()){
                    primaryKeyIndex = i;
                }else{
                    this.indices.add(index);
                }
            }
            primaryIndex = primaryKeyIndex<0 ? null : indices.get(primaryKeyIndex);
        }else{
            primaryIndex = null;
        }
    }

    @Override
    public int size() {
        return indices.size();
    }

    @Override
    public ISqmlTableIndexDef getPrimaryIndex() {
        return primaryIndex;
    }

    @Override
    public ISqmlTableIndexDef getIndexById(final Id indexId) {
        for (ISqmlTableIndexDef index: indices){
            if (indexId.equals(index.getId())){
                return index;
            }
        }
        return null;
    }

    @Override
    public ISqmlTableIndexDef get(final int idx) {
        return indices.get(idx);
    }

    @Override
    public Iterator<ISqmlTableIndexDef> iterator() {
        return indices.iterator();
    }

}
