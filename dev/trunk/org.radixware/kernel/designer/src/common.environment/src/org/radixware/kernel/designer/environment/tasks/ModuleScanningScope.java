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

package org.radixware.kernel.designer.environment.tasks;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;


class ModuleScanningScope extends SelectionScanningScope {

    public ModuleScanningScope() {
        super("Current Module", "Show tasks for selected object's module", AdsDefinitionIcon.MODULE.getImage());
    }

    @Override
    protected RadixObject filter(RadixObject obj) {
        if (obj == null) {
            return null;
        }
        return obj.getModule();
    }

    public static final SelectionScanningScope create() {
        return new ModuleScanningScope();
    }
}
