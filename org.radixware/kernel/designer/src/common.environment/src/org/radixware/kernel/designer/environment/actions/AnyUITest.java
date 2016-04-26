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

package org.radixware.kernel.designer.environment.actions;

import java.awt.Dialog;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.designer.common.dialogs.wizards.TestWizard;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;

public final class AnyUITest extends CallableSystemAction{

    
    @Override
    public void performAction() {
        Dialog d = DialogDisplayer.getDefault().createDialog(new WizardDescriptor(new TestWizard()));
        d.setVisible(true);
        d.toFront();
    }

    @Override
    public String getName() {
        return "TestUIComponent";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
