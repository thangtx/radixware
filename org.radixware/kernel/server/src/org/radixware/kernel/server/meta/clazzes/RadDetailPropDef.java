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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;

public class RadDetailPropDef extends RadJoinPropDef {

    private final Id detailReferenceId;
    private DdsReferenceDef reference = null;
    DdsColumnDef joinedCol = null;

    public RadDetailPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id constSetId,
            final Id detailReferenceId,
            final Id detailPropId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkVal,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, detailPropId, valType, constSetId, isValInheritable, valInheritMarkVal, valInheritPathes, initPolicy, initVal, accessor);
        this.detailReferenceId = detailReferenceId;
    }

    @Override
    public void link() {
        super.link();
        getDetailReference();
        getJoinedColumn();
    }

    @Override
    public Id getJoinedEntityId() {
        return getDetailReference().getChildTableId();
    }

    public DdsReferenceDef getDetailReference() {
        if (reference == null) {
            reference = getClassDef().getRelease().getReferenceDef(getDetailReferenceId());
        }
        return reference;
    }

    /**
     * @return the detailReferenceId
     */
    public Id getDetailReferenceId() {
        return detailReferenceId;
    }

    DdsColumnDef getJoinedColumn() {
        if (joinedCol == null) {
            joinedCol = getJoinedTable().getColumns().getById(getJoinedPropId(), EScope.ALL);
        }
        return joinedCol;
    }

    @Override
    public String getDbName() {
        return getJoinedColumn().getDbName();
    }

    @Override
    public String getDbType() {
        return getJoinedColumn().getDbType();
    }

    @Override
    public boolean isGeneratedInDb() {
        return getJoinedColumn().isGeneratedInDb();
    }
}
