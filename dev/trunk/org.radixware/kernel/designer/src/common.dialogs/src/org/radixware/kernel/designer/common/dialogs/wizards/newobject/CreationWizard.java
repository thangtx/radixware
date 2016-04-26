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
package org.radixware.kernel.designer.common.dialogs.wizards.newobject;

import java.awt.Dialog;
import java.util.Objects;
import org.openide.DialogDisplayer;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;

public class CreationWizard {

    public static final void execute(ICreatureGroup[] categories) {
        execute(categories, null);
    }

    public static final ICreature<?> execute(ICreatureGroup[] categories, ICreature initialCreature) {
        CreationWizardSteps setupSteps = null;

        if (initialCreature != null) {
            ICreature.WizardInfo wizardInfo = initialCreature.getWizardInfo();
            if (wizardInfo != null && wizardInfo.hasWizard()) {
                setupSteps = new CreationWizardSteps(categories, initialCreature);
            }
        } else {
            setupSteps = new CreationWizardSteps(categories, null);
        }

        if (setupSteps != null) {
            WizardDescriptor desc = new WizardDescriptor(setupSteps);
            Dialog d = DialogDisplayer.getDefault().createDialog(desc);
            d.setVisible(true);

            d.toFront();

            if (!Objects.equals(desc.getValue(), WizardDescriptor.FINISH_OPTION)) {
                return null;
            } else {
                return setupSteps.getCreature();
            }
        } else {
            return initialCreature;
        }
    }
}
