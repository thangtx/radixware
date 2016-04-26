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

package org.radixware.kernel.server.meta.clazzes;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;

public abstract class RadJoinPropDef extends RadPropDef {

    private final Id joinedPropId;
    RadPropDef joinedProp = null;

    public RadJoinPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id joinedPropId,
            final EValType valType,
            final Id constSetId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkVal,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, constSetId, isValInheritable, valInheritMarkVal, valInheritPathes, initPolicy, initVal, accessor);
        this.joinedPropId = joinedPropId;
    }

    @Override
    public void link() {
        super.link();
        getJoinedProp();
    }

    public abstract Id getJoinedEntityId();

    public Id getJoinedClassId() {
        return RadClassDef.getEntityClassIdByTableId(getJoinedEntityId());
    }

    /**
     * @return the joinedPropId
     */
    public Id getJoinedPropId() {
        return joinedPropId;
    }

    @Override
    public DdsTableDef findOwnerTable() {
        return getJoinedTable();
    }

    protected DdsTableDef getJoinedTable() {
        return getClassDef().getRelease().getTableDef(getJoinedEntityId());
    }

    RadPropDef getJoinedProp() {
        if (joinedProp == null) {
            joinedProp = getClassDef().getRelease().getClassDef(getJoinedClassId()).getPropById(getJoinedPropId());
        }
        return joinedProp;
    }

    @Override
    public String getDbName() {
        return getJoinedProp().getDbName();
    }

    @Override
    public String getDbType() {
        return getJoinedProp().getDbType();
    }

    @Override
    public boolean isGeneratedInDb() {
        return getJoinedProp().isGeneratedInDb();
    }
}
