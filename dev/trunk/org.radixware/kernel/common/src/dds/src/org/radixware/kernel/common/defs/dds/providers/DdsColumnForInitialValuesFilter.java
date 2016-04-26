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

package org.radixware.kernel.common.defs.dds.providers;

import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;


class DdsColumnForInitialValuesFilter implements IFilter<DdsColumnDef> {

    private final DdsTableDef table;

    public DdsColumnForInitialValuesFilter(DdsTableDef table) {
        this.table = table;
    }

    @Override
    public boolean isTarget(DdsColumnDef column) {
        if (!column.isGeneratedInDb()) {
            return false;
        }
        if (column.getExpression() != null) {
            return false;
        }
        return true;
    }
}
