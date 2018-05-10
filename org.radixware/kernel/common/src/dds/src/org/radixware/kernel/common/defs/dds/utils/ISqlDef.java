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
package org.radixware.kernel.common.defs.dds.utils;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;

public interface ISqlDef {
    public Sqml getSqml();
    
    public IUsedTables<? extends IUsedTable> getUsedTables();
    
    public boolean isReadOnly();
    
    public Definition getDefinition();

    public abstract class  IUsedTable extends RadixObject{
        protected Id tableId;

        public IUsedTable(Id tableId) {
            this.tableId = tableId;
        }
        
        public Id getTableId(){
            return tableId;
        }

        public abstract String getAlias();
        
        public abstract void setAlias(String alias);
        
        public abstract DdsTableDef findTable();
        
        public abstract DdsTableDef getTable();
        
        public abstract boolean useAlias();
    }
    
    public abstract class  IUsedTables<T extends IUsedTable> extends RadixObjects<T>{

        public IUsedTables() {
        }

        public IUsedTables(RadixObject container) {
            super(container);
        }

        public abstract IUsedTable add(final Id tableId, final String alias);
        
        public IUsedTable add(final Id tableId){
            return add(tableId, "");
        }
        
        public IUsedTable add(final DdsTableDef table, final String alias) {
            return add(table.getId(), alias);
        }
        
        public boolean useAlias(){
            for (IUsedTable table: this){
                if (table.useAlias()){
                    return true;
                }
            }
            return false;
        }

        public boolean remove(IUsedTable object) {
            try {
                T obj = (T) object;
                return super.remove(obj);
            } catch (ClassCastException e) {
                return false;
            }
        }

        public void loadFrom(IUsedTables<T> usedTables){
            List<T> list = usedTables.list();
            if (!list.equals(list())){
                clear();
                for (T usedTable : usedTables){
                    add(usedTable);
                }
            }
        }
        
        public boolean containsId(Id tableId) {
            for (IUsedTable object : this) {
                if (object.getTableId() == tableId) {
                    return true;
                }
            }
            return false;
        }
    }
}
