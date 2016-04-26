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

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;


class StateAccessAreaCreationDialog extends StateAbstractSetupAccessAreaDialog {

    private static class StateAccessAreaCreationPanel extends StateAbstractSetupAccessAreaDialog.StateAbstractSetupAccessAreaPanel {

        public StateAccessAreaCreationPanel(AccessAreas accessAreas) {
            super(accessAreas);
        }

        @Override
        public void check() {
            final String name = getResultName();
            if (name.isEmpty()) {
                stateManager.error("Wrong name");
                changeSupport.fireChange();
            } else {

                for (AccessArea xAccessArea : accessAreas) {
                    if (xAccessArea.getName().equals(name)) {
                        stateManager.error("AccessArea with such name already exists");
                        changeSupport.fireChange();
                        return;
                    }
                }

                stateManager.ok();
                changeSupport.fireChange();
            }
        }
    }

    private AccessAreas accessAreas;

    public StateAccessAreaCreationDialog(AccessAreas accessAreas) {
        super(new StateAccessAreaCreationPanel(accessAreas), "New Access Area");
        this.accessAreas = accessAreas;
    }

    @Override
    protected void apply() {
        accessAreas.add(accessAreas.newAccessArea(getResultName()));
    }
}
