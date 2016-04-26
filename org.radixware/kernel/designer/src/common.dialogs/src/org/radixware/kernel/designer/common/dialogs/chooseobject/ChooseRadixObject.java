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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;

/**
 * Unititi class, allows to choose definitions from list.
 *
 */
public class ChooseRadixObject {

    private ChooseRadixObject() {
    }

    private static final List<RadixObject> chooseRadixObject(ChooseRadixObjectCfg cfg, boolean multipleSelectionAllowed) {
        final VisitorProvider provider = cfg.getProvider();
        if (provider != null) {
            provider.setCancelled(false);
        }

        final IChooseRadixObjectModalDisplayer modalDisplayer;
        if (cfg.getStepCount() > 1) {
            modalDisplayer = new ChooseRadixObjectWizard(cfg, multipleSelectionAllowed);
        } else {
            final ChooseRadixObjectPanel panel = new ChooseRadixObjectPanel();
            modalDisplayer = new ChooseRadixObjectModalDisplayer(panel, cfg, multipleSelectionAllowed);
        }

        if (modalDisplayer.showModal()) {
            return modalDisplayer.getResult();
        } else {
            if (provider != null) {
                provider.cancel();
            }
            return null;
        }
    }

    public static final RadixObject chooseRadixObject(ChooseRadixObjectCfg cfg) {
        List<? extends RadixObject> selectedObjects = chooseRadixObject(cfg, false);
        if (selectedObjects != null && !selectedObjects.isEmpty()) {
            return selectedObjects.get(0);
        } else {
            return null;
        }
    }

    public static final List<RadixObject> chooseRadixObjects(ChooseRadixObjectCfg cfg) {
        List<RadixObject> selectedObjects = chooseRadixObject(cfg, true);
        if (selectedObjects != null && !selectedObjects.isEmpty()) {
            return selectedObjects;
        } else {
            return null;
        }
    }
}
