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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class DdsPrimaryKeyChecker<T extends DdsPrimaryKeyDef> extends DdsIndexChecker<T> {

    public DdsPrimaryKeyChecker() {
        super();
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsPrimaryKeyDef.class;
    }

    @Override
    public void check(T pk, IProblemHandler problemHandler) {
        super.check(pk, problemHandler);

        if (!pk.isGeneratedInDb() && !(pk.getOwnerTable() instanceof DdsViewDef) && pk.getOwnerTable().isGeneratedInDb()) {
            if (!pk.getIsGenerateInDbOption()) {
                error(pk, problemHandler, "Primary key must be generated in database");
            }
        }

        for (DdsIndexDef.ColumnInfo columnInfo : pk.getColumnsInfo()) {
            DdsColumnDef column = columnInfo.findColumn();
            if (column != null && !column.isNotNull()) {
                error(pk, problemHandler, "Primary key column '" + column.getName() + "' must be not null");
            }
            if (column.getAuditInfo().isSaveValues()) {
                error(pk, problemHandler, "Audit enabled for primary key column '" + column.getName() + "'");
            }
        }

        if (pk.getUniqueConstraint() == null) {
            error(pk, problemHandler, "Primary key must have uniqie constraint");
        }

        if (!pk.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE)) {
            error(pk, problemHandler, "Primary key must be unique");
        }

        if (pk.getDbOptions().contains(DdsIndexDef.EDbOption.BITMAP)) {
            error(pk, problemHandler, "Primary key must not be bitmap");
        }
    }
}
