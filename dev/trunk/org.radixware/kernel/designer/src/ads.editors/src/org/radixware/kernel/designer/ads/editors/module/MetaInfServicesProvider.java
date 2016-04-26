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

package org.radixware.kernel.designer.ads.editors.module;

import java.awt.Dialog;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;


public final class MetaInfServicesProvider {

    public static void addService(AdsModule module, ERuntimeEnvironmentType evnironment) {

        addImplementation(module, null, evnironment);
    }

    public static void addImplementation(AdsModule module, AdsPath serviceIdPath, ERuntimeEnvironmentType evnironment) {
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(new CreateServiceSteps(module, evnironment, serviceIdPath));

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
