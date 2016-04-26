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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;


class StateAccessAreaModifyDialog extends StateAbstractSetupAccessAreaDialog{

    private static class StateAccessAreaModifyPanel extends StateAbstractSetupAccessAreaDialog.StateAbstractSetupAccessAreaPanel {

        private AccessArea currentEditingAccessArea;

        public StateAccessAreaModifyPanel(AccessAreas accessAreas, AccessArea currentEditingAccessArea) {
            super(accessAreas);
            this.currentEditingAccessArea = currentEditingAccessArea;
        }

        @Override
        public void check() {

            final String name = getResultName();
            if (name.isEmpty()) {
                stateManager.error("Wrong name");
                changeSupport.fireChange();
            } else {

                if (!name.equals(currentEditingAccessArea.getName())){
                    for (AccessArea xAccessArea : accessAreas) {
                        if (xAccessArea.getName().equals(name)) {
                            stateManager.error("AccessArea with such name already exists");
                            changeSupport.fireChange();
                            return;
                        }
                    }
                }

                stateManager.ok();
                changeSupport.fireChange();
            }
        }
    }

    private AccessArea currentAccessArea;

    public StateAccessAreaModifyDialog(AccessAreas accessAreas, final AccessArea currentAccessArea) {
        super(new StateAccessAreaModifyPanel(accessAreas, currentAccessArea), "Edit Access Area");
        this.currentAccessArea = currentAccessArea;

        getDialog().addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
               ((StateAccessAreaModifyPanel) getComponent()).setResultName(currentAccessArea.getName());
            }
        });
    }

    @Override
    protected void apply() {

        final String newName = getResultName();
        if (!currentAccessArea.getName().equals(newName)){
            currentAccessArea.setName(newName);
        }
    }
}
