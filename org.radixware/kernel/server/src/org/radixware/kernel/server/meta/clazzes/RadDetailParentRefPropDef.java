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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.arte.Release;

public class RadDetailParentRefPropDef extends RadDetailPropDef implements IRadRefPropertyDef {

    private DdsReferenceDef destRef = null;
    private Id destClassId;

    public RadDetailParentRefPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id referenceId,
            final Id destRefId,
            final Id destClassId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkVal,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, EValType.PARENT_REF, null, referenceId, destRefId, isValInheritable, valInheritMarkVal, valInheritPathes, initPolicy, null, accessor);
        this.destClassId = destClassId;
    }

    @Override
    public void link() {
        super.link();
        getReference();
        getDestinationClassId();
    }
    
    

    @Override
    public DdsReferenceDef getReference() {
        if (destRef == null) {
            destRef = getClassDef().getRelease().getReferenceDef(getJoinedPropId());
        }
        return destRef;
    }

    @Override
    public Id getDestinationEntityId() {
        final Release release = getClassDef().getRelease();
        final DdsTableDef tab = release.getTableDef(getReference().getParentTableId());
        final DdsReferenceDef mdRef = release.getMasterReferenceDef(tab);
        if (mdRef != null) {
            return mdRef.getParentTableId();
        }
        return tab.getId();
    }

    @Override
    public String getDbType() {
        return null;
    }
    
    @Override
    public Id getDestinationClassId() {
        if (destClassId == null) {
            destClassId = RadClassDef.getEntityClassIdByTableId(getDestinationEntityId());
        }
        return destClassId;
    }
}
