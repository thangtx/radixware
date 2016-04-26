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
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.enums.EOrder;


@RadixObjectCheckerRegistration
public class DdsIndexChecker<T extends DdsIndexDef> extends DdsDefinitionChecker<T> {

    public DdsIndexChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsIndexDef.class;
    }

    @Override
    public void check(T index, IProblemHandler problemHandler) {
        super.check(index, problemHandler);

        checkForDbNameDuplication(index, problemHandler);

        if (index.getColumnsInfo().size() == 0 && !(index instanceof DdsPrimaryKeyDef)) {
            error(index, problemHandler, "Index columns not specified");
        }

        final IFilter<DdsColumnDef> columnForIndexFilter = DdsVisitorProviderFactory.newColumnForIndexFilter(index);

        for (DdsIndexDef.ColumnInfo columnInfo : index.getColumnsInfo()) {
            DdsColumnDef column = columnInfo.findColumn();
            if (column != null) {
                if (!columnForIndexFilter.isTarget(column)) {
                    error(index, problemHandler, "Illegal column used in index: '" + column.getName() + "'");
                }
                if (columnInfo.getOrder() == EOrder.DESC && index.getUniqueConstraint() != null) {
                    // RADIX-5125
                    error(index, problemHandler, "Order of index column '" + column.getName() + "' must be ASC, because index with unique constraint");
                }
            } else {
                error(index, problemHandler, "Index column not found: #" + String.valueOf(columnInfo.getColumnId()));
            }
        }
    }
}
