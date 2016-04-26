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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.VisitorProvider;

/**
 * Unititi class, allows to choose definitions from list.
 */
public class ChooseDefinition {

    private ChooseDefinition() {
    }

    private static final List<Definition> chooseDefinition(ChooseDefinitionCfg cfg, boolean multipleSelectionAllowed) {
        final VisitorProvider provider = cfg.getProvider();
        if (provider != null) {
            provider.setCancelled(false);
        }

        final IChooseDefinitionModalDisplayer modalDisplayer;
        if (cfg.getStepCount() > 1) {
            modalDisplayer = new ChooseDefinitionWizard(cfg, multipleSelectionAllowed);
        } else {
            final ChooseDefinitionPanel panel = new ChooseDefinitionPanel();
            modalDisplayer = new ChooseDefinitionModalDisplayer(panel, cfg, multipleSelectionAllowed);
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

    public static final Definition chooseDefinition(ChooseDefinitionCfg cfg) {
        List<? extends Definition> selectedObjects = chooseDefinition(cfg, false);
        if (selectedObjects != null && !selectedObjects.isEmpty()) {
            return selectedObjects.get(0);
        } else {
            return null;
        }
    }

    public static final List<Definition> chooseDefinitions(ChooseDefinitionCfg cfg) {
        List<Definition> selectedObjects = chooseDefinition(cfg, true);
        if (selectedObjects != null && !selectedObjects.isEmpty()) {
            return selectedObjects;
        } else {
            return null;
        }
    }
}
