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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;


class StatePartitionCreationDialog extends StateAbstractDialog{

    private AccessArea accessArea;

    public StatePartitionCreationDialog(AdsEntityClassDef adsEntityClassDef, AccessArea accessArea) {
        super(new PartitionSetupPanel(adsEntityClassDef, accessArea), "New Partition");
        this.accessArea = accessArea;
    }

    @Override
    protected void apply() {

        final Partition newPartition = ((PartitionSetupPanel) getComponent()).getPartition();
        if (newPartition != null) {
            accessArea.getPartitions().add(newPartition);
        }
    }
}
