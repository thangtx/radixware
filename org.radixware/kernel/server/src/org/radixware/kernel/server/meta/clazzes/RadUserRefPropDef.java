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
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;

public final class RadUserRefPropDef extends RadUserPropDef implements IRadRefPropertyDef {

    private final Id destClassId;
    private Id destEntityId = null;
    private DdsTableDef destTable = null;

    public RadUserRefPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkValAsStr,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final Id ownerEntityId,
            final Id destClassId,
            final boolean isUpdateAuditOn,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, null, isValInheritable, valInheritMarkValAsStr, valInheritPathes, initPolicy, initVal, ownerEntityId, isUpdateAuditOn, accessor);
        this.destClassId = destClassId;
    }

    @Override
    public void link() {
        super.link();
        getDestinationClassId();
        getDestinationEntityId();
    }

    @Override
    public final Id getDestinationEntityId() {
        if (destEntityId == null) {
            destEntityId = getClassDef().getRelease().getClassEntityId(destClassId);
        }
        return destEntityId;
    }

    public final DdsTableDef getDestinationTable() {
        if (destTable == null) {
            destTable = getClassDef().getRelease().getTableDef(getDestinationEntityId());
        }
        return destTable;
    }

    @Override
    public DdsReferenceDef getReference() {
        return null;
    }

    @Override
    public Id getDestinationClassId() {
        return destClassId;
    }
}
