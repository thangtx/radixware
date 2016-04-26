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
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.enums.EValType;


class DdsColumnForIndexFilter implements IFilter<DdsColumnDef> {

    private final DdsIndexDef index;

    public DdsColumnForIndexFilter(DdsIndexDef index) {
        this.index = index;
    }

    @Override
    public boolean isTarget(final DdsColumnDef column) {
        if (index.isGeneratedInDb() && !column.isGeneratedInDb()) {
            return false;
        }

        final EValType valType = column.getValType();
        if (valType != EValType.BOOL &&
                valType != EValType.CHAR &&
                valType != EValType.DATE_TIME &&
                valType != EValType.INT &&
                valType != EValType.NUM &&
                valType != EValType.STR) {
            return false;
        }

        return true;
    }
}
