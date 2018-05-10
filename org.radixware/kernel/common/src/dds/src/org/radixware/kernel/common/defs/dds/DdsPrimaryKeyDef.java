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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Primary key of {@link DdsTableDef}.
 *
 */
public class DdsPrimaryKeyDef extends DdsIndexDef {

    protected DdsPrimaryKeyDef(DdsTableDef ownerTable) {
        super("");
        this.setContainer(ownerTable);
        final DdsUniqueConstraintDef uc = DdsUniqueConstraintDef.Factory.newInstance();
        setUniqueConstraint(uc);
        getDbOptions().add(EDbOption.UNIQUE);
        setGeneratedInDb(!(ownerTable instanceof DdsViewDef));
    }

    protected DdsPrimaryKeyDef(DdsTableDef ownerTable, org.radixware.schemas.ddsdef.Index xIndex) {
        super(xIndex);
        this.setContainer(ownerTable);
    }

    @Override
    public String getName() {
        return "PrimaryKey";
    }

    @Override
    public final void setUniqueConstraint(DdsUniqueConstraintDef uniqueConstraint) {
        if (uniqueConstraint == null) {
            throw new DefinitionError("Attemp to remove primary key unique constraint.", this);
        }
        super.setUniqueConstraint(uniqueConstraint);
    }

    @Override
    public boolean isGeneratedInDb() {
        if (!isValidIndex()) {
            return false;
        }
        return super.isGeneratedInDb();
    }

    public boolean getIsGenerateInDbOption() {
        return super.isGeneratedInDb();
    }

    public boolean isValidIndex() {
        final DdsTableDef ownerTable = getOwnerTable();
        if (ownerTable instanceof DdsViewDef) {
            return false;
        }
        //RADIX-8021
        if (getColumnsInfo().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSecondaryKey() {
        return false;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsPrimaryKeyDef newInstance(DdsTableDef ownerTable) {
            return new DdsPrimaryKeyDef(ownerTable);
        }

        public static DdsPrimaryKeyDef newInstance(DdsTableDef ownerTable, org.radixware.schemas.ddsdef.Index xIndex) {
            return new DdsPrimaryKeyDef(ownerTable, xIndex);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.PRIMARY_KEY;
    }

    @Override
    public String calcAutoDbName() {
        DdsTableDef table = getOwnerTable();
        String tableDbName = table.getDbName();
        return DbNameUtils.calcAutoDbName("PK", tableDbName);
    }
}
