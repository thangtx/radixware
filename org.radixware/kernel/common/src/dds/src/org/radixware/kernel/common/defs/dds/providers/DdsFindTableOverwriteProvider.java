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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


class DdsFindTableOverwriteProvider extends DdsTableProvider {

    private final DdsTableDef overwrittenTable;

    public DdsFindTableOverwriteProvider(DdsTableDef overwrittenTable) {
        this.overwrittenTable = overwrittenTable;
    }

    @Override
    public boolean isTarget(RadixObject object) {
        if (object instanceof DdsTableDef) {
            DdsTableDef table = (DdsTableDef) object;
            return table.findOverwritten() == overwrittenTable;
        } else {
            return false;
        }
    }

    @Override
    public boolean isContainer(RadixObject object) {
        if (!super.isContainer(object)) {
            return false;
        }

        if (object instanceof DdsModule) {
            final DdsModule module = (DdsModule) object;
            final Id moduleId = module.getId();
            final Id requiredModuleId = overwrittenTable.getModule().getId();
            return Utils.equals(moduleId, requiredModuleId);
        } else {
            return true;
        }
    }
}
