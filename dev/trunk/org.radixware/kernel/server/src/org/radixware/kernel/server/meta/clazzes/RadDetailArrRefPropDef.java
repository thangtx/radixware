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

public final class RadDetailArrRefPropDef extends RadDetailPropDef implements IRadRefPropertyDef {

    private final Id destClassId;
    private DdsTableDef destTable = null;

//	Constructor
    public RadDetailArrRefPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final Id detailReferenceId,
            final Id detailPropId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkValAsStr,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final Id destClassId,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, EValType.ARR_REF, null, detailReferenceId, detailPropId, isValInheritable, valInheritMarkValAsStr, valInheritPathes, initPolicy, initVal, accessor);
        this.destClassId = destClassId;
    }

    @Override
    public void link() {
        super.link();
        getDestinationTable();
    }

    @Override
    public final Id getDestinationEntityId() {
        return getDestinationTable().getId();
    }

    public final DdsTableDef getDestinationTable() {
        if (destTable == null) {
            destTable = getClassDef().getRelease().getClassDef(getDestinationClassId()).getTableDef();
        }
        return destTable;
    }

    @Override
    public DdsReferenceDef getReference() {
        return null;
    }

    @Override
    public final Id getDestinationClassId() {
        return destClassId;
    }
}
