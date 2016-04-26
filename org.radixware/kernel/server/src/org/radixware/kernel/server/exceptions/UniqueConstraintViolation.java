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

package org.radixware.kernel.server.exceptions;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

public class UniqueConstraintViolation extends RadixError {

    private static final long serialVersionUID = 9060576457333835628L;
    private final DdsTableDef table;
    private final DdsIndexDef key;
    private final RadClassDef classMeta;

    public UniqueConstraintViolation(final RadClassDef classMeta, final DdsTableDef table, final DdsIndexDef key, final String message, final SQLException cause) {
        super(message, cause);
        this.key = key;
        this.table = table;
        this.classMeta = classMeta;
    }

    public DdsIndexDef getKey() {
        return key;
    }

    public DdsTableDef getTable() {
        return table;
    }
    
    public Id getClassId(){
        return classMeta.getId();
    }
    
    public Set<Id> getParentRefPropIdsForIndexColumn(final Id columnId){
        final Set<Id> result = new HashSet<>();
        for (RadPropDef prop: classMeta.getProps()){
            if (prop instanceof IRadRefPropertyDef) {
                final DdsReferenceDef ref = ((IRadRefPropertyDef) prop).getReference();
                if (ref!=null){
                    for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                        if (columnId.equals(refProp.getChildColumnId())){
                            result.add(prop.getId());
                            continue;
                        }
                    }
                }
            }
        }
        return result;
    }
}
