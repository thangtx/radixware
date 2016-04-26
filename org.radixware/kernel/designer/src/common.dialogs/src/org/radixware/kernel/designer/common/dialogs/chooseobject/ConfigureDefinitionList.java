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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 * Allows to edit list of definitions.
 */
public class ConfigureDefinitionList {

    private ConfigureDefinitionList() {
    }

    private static class DialogModalDisplayer extends ModalDisplayer {

        public DialogModalDisplayer(ConfigureDefinitionListPanel panel) {
            super(panel, "Choose Definitions");
        }

        @Override
        protected void apply() {
        }
    }

    /**
     * Edit list of Radix objects.
     * @param configurableDefinitions - list to edit.
     * @param cfg - configuration.
     */
    public static boolean configure(List<Id> configurableDefinitionIds, ConfigureDefinitionListCfg cfg) {
        ConfigureDefinitionListPanel panel = new ConfigureDefinitionListPanel();
        panel.open(configurableDefinitionIds, cfg);

        DialogModalDisplayer modalDisplayer = new DialogModalDisplayer(panel);
        String typesTitle = RadixObjectsUtils.getCommonTypeTitle(cfg.getAvailableDefinitions());
        modalDisplayer.getDialog().setTitle("Choose "+typesTitle);

        if (modalDisplayer.showModal()) {
            configurableDefinitionIds.clear();
            configurableDefinitionIds.addAll(panel.getConfigurableDefinitionIds());
            return true;
        } else {
            return false;
        }
    }
}
