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


package org.radixware.kernel.designer.ads.editors.refactoring.components;

import org.radixware.kernel.designer.ads.editors.refactoring.IRefactoringSteps;
import org.radixware.kernel.designer.ads.editors.refactoring.IRefactoringSteps.Settings;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringSteps;


public class RefactoringStatusStep extends RefactoringSteps.RefactoringStep<RefactoringStatusPanel> {

    private OperationStatus status;
    
    @Override
    protected OperationStatus check() {
        return OperationStatus.OK;
    }

    @Override
    public String getDisplayName() {
        return "Refactoring status";
    }

    @Override
    public boolean isComplete() {
        return status != null && status.isValid();
    }
    
    @Override
    protected RefactoringStatusPanel createVisualPanel() {
        return new RefactoringStatusPanel();
    }

    @Override
    public void open(Object settings) {
        status = ((Settings) settings).getStatus();
        final RefactoringStatusPanel visualPanel = getVisualPanel();
        visualPanel.open(status);
    }
    
    @Override
    public boolean hasNextStep() {
        return false;
    }
    
    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public IRefactoringSteps.ISettings getSettings() {
        return null;
    }
}
