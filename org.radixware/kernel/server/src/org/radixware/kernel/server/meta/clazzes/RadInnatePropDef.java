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
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.ERadixDefaultValueChoice;
import org.radixware.kernel.common.enums.EValType;

public class RadInnatePropDef extends RadPropDef {

    private DdsColumnDef column = null;

    public RadInnatePropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id constSetId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkValAsStr,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, constSetId, isValInheritable, valInheritMarkValAsStr, valInheritPathes, initPolicy, initVal, accessor);
    }

    @Override
    public void link() {
        super.link();
        getColumn();
        isDbInitValOverriden();
    }

    private final DdsColumnDef getColumn() {
        if (column == null) {
            column = getClassDef().getTableDef().getColumns().getById(getId(), EScope.ALL);
        }
        return column;
    }

    public boolean isDbInitValOverriden() {
        final RadixDefaultValue colInitVal = getColumn().getDefaultValue();
        if (initVal == null) {
            return colInitVal != null;
        }
        if (initVal.getChoice() == ERadixDefaultValueChoice.VAL_AS_STR) {
            return !initVal.equals(colInitVal);
        }
        return false;
    }

    @Override
    public String getDbName() {
        return getColumn().getDbName();
    }

    @Override
    public String getDbType() {
        return getColumn().getDbType();
    }

    @Override
    public boolean isGeneratedInDb() {
        return getColumn().isGeneratedInDb();
    }

    public int getDbPrecision() {
        return getColumn().getPrecision();
    }
}
