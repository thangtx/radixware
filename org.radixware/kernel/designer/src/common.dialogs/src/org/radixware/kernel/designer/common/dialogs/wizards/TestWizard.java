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

package org.radixware.kernel.designer.common.dialogs.wizards;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.wizards.TestWizardSettings.Setting;


public class TestWizard extends WizardSteps {

    @Override
    public Step createInitial() {
        return new TestStep(0);
    }

    public TestWizard() {
    }

    @Override
    public TestWizardSettings createSettings() {
        return new TestWizardSettings();
    }

    @Override
    public String getDisplayName() {
        return "Test Wizard";
    }

    public class TestStep extends Step<TestStepPanel> implements ChangeListener {

        private int ownIndex;
        public StateManager errorManager;
        public StateManager warningManager;

        public TestStep(int index) {
            ownIndex = index;

        }

        @Override
        protected TestStepPanel createVisualPanel() {
            TestStepPanel p = new TestStepPanel(this);
            p.changeSupport.addChangeListener(this);
            return p;
        }

        public void setComplete(boolean complete) {
            Setting s = getSetting();
            if (s != null) {
                s.isComplete = complete;
            }
            fireChange();
        }

        public void setErrorMessage(String msg) {
            Setting s = getSetting();
            if (s != null) {
                s.errorText = msg;
            }
            fireChange();
        }

        public void setWarningMessage(String msg) {
            Setting s = getSetting();
            if (s != null) {
                s.warningText = msg;
            }
            fireChange();
        }

        @Override
        public boolean isComplete() {
            Setting s = getSetting();
            boolean res = s.isComplete;
            if (!s.errorText.isEmpty()) {
                errorManager.error(s.errorText);
                res = false;
            } else {
                errorManager.ok();
            }

            if (!s.warningText.isEmpty()) {
                warningManager.warning(s.warningText);
            } else {
                warningManager.ok();
            }
            return res;

        }
        private TestWizardSettings settings;

        private Setting getSetting() {
            Setting s = this.settings.settings.get(getDisplayName());
            if (s == null) {
                s = new Setting();
                s.errorText = "error";
                s.warningText = "warning";
                s.isComplete = false;
                this.settings.settings.put(getDisplayName(), s);
            }
            return s;
        }

        @Override
        public void open(Object settings) {
            super.open(settings);
            this.settings = (TestWizardSettings) settings;
            getVisualPanel().chCompleted.setSelected(getSetting().isComplete);
            getVisualPanel().edError.setText(getSetting().errorText);
            getVisualPanel().edWarning.setText(getSetting().warningText);
        }

        @Override
        public boolean isFinishiable() {
            return ownIndex % 2 == 0 || !hasNextStep();
        }

        @Override
        public Step createNextStep() {
            return new TestStep(ownIndex + 1);
        }

        @Override
        public String getDisplayName() {
            return "Step#" + String.valueOf(ownIndex);
        }

        @Override
        public boolean hasNextStep() {
            return ownIndex < 5;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
}
