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

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;


public abstract class RefactoringSteps extends WizardSteps implements IRefactoringSteps {
    
    public static abstract class RefactoringStep<ComponentType extends RefactoringStepPanel> extends WizardSteps.Step<ComponentType> implements ISettingProvider {

        protected abstract OperationStatus check();

        protected boolean isReady() {
            return true;
        }

        @Override
        public boolean isComplete() {
            final OperationStatus status = check();
            final Set<OperationStatus.Event> errors = status.getEvents(OperationStatus.EEventType.ERROR);
            if (!errors.isEmpty()) {
                final OperationStatus.Event error = errors.iterator().next();
                getVisualPanel().getManager().error(error.getMessage());
            } else {
                getVisualPanel().getManager().ok();
            }

            return isReady() && status.isValid();
        }

        @Override
        public final void apply(Object object) {
            final Settings settings = (Settings) object;
            settings.setStatus(this.getClass(), check());
            apply();
        }
        
        public void apply() {
        }
    }
    private ExecutorService executor;

    @Override
    protected final void finished() {
        final RefactoringResult result = perform();
        
        if (showResult()) {
            RefactoringTopComponent.findInstance().open(result);
        }
    }

    protected ExecutorService createExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Override
    public final ExecutorService getExecutor() {
        if (executor == null || executor.isTerminated() || executor.isShutdown()) {
            executor = createExecutor();
        }
        return executor;
    }

    @Override
    public Settings getSettings() {
        return (Settings) super.getSettings();
    }
    
    protected boolean showResult() {
        return false;
    }
    
    protected abstract RefactoringResult perform();

    @Override
    public abstract Settings createSettings();

}