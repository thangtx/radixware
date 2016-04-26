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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.resources.RadixWareIcons;


class DefinitionScanningScope extends SelectionScanningScope {

    public DefinitionScanningScope() {
        super("Current Definition", "Show tasks for selected object's definition", RadixWareIcons.FILE.NEW_DOCUMENT.getImage());
    }

    @Override
    protected RadixObject filter(RadixObject obj) {
        if (obj == null) {
            return null;
        }
        Definition def = obj.getDefinition();
        if (def instanceof AdsDefinition) {
            return ((AdsDefinition) def).findTopLevelDef();
        } else {
            return def == null || def instanceof Module ? null : def.getModule();
        }
    }

    public static final SelectionScanningScope create() {
        return new DefinitionScanningScope();
    }
}
