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

package org.radixware.kernel.designer.ads.editors.refactoring;

import java.awt.Dialog;
import org.openide.DialogDisplayer;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;


public final class RefactoringWizard {

    public static void showModal(IRefactoringProcessor processor) {
        final RefactoringSteps steps = processor.getSteps();
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(steps);

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
    
//    public static void doRefactor(AdsDefinition definition) {
//        UI.openRefactoringUI(new ReplaceRefactoring.ReplaceRefactoringUI(definition));
//    }

    private RefactoringWizard() {
    }
}
