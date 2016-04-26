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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;


public abstract class WizardSteps {

    private Object settings;
    WizardDescriptor wizardDescriptor = null;

    public static abstract class Step<V extends JComponent> {

        private V visualPanel;
        private WizardSteps ownerSteps = null;
        WizardDescriptor.Panel netbeansWizardPanel;

        public boolean hasNextStep() {
            return false;
        }

        public Step createNextStep() {
            return null;
        }

        public abstract String getDisplayName();

        protected abstract V createVisualPanel();

        /**
         * Returns visual representation of wizard
         */
        protected final V getVisualPanel() {
            synchronized (this) {
                if (visualPanel == null) {
                    visualPanel = createVisualPanel();
                }
                if (this.ownerSteps != null && this.ownerSteps.wizardDescriptor != null) {
                    IStateDisplayer.Locator.register(this.ownerSteps.wizardDescriptor.stateDisplayer, visualPanel);
                }
                return visualPanel;
            }
        }

        /**
         * if returns true it means finish button should be available if step is completed
         */
        public boolean isFinishiable() {
            return false;
        }

        /**
         * if returns true it means next button (or finish) should be available
         */
        public boolean isComplete() {
            return false;
        }

        public void open(Object settings) {
        }

        public void apply(Object settings) {
        }

        public void cancel(Object settings) {
        }

        private void free() {
            if (visualPanel != null && this.ownerSteps.wizardDescriptor != null) {
                IStateDisplayer.Locator.unregister(this.ownerSteps.wizardDescriptor.stateDisplayer, visualPanel);
            }
            this.ownerSteps = null;
        }

        public final void fireChange() {
            if (ownerSteps != null) {
                ownerSteps.updateStepSequence();
                this.ownerSteps.changeSupport.fireChange();
            }
        }

        public void doNextOrFinish() {
            final JButton nextOrFinishButton = getVisualPanel().getRootPane().getDefaultButton();
            if (nextOrFinishButton.isEnabled() && nextOrFinishButton.isVisible()) {
                nextOrFinishButton.doClick();
            }
        }
    }
    private final ArrayList<Step> steps = new ArrayList<>();
    private int currentIdx = -1;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private static Object defaultSettings = new Object();

    Step current() {
        synchronized (steps) {
            return steps.get(currentIdx);
        }
    }

    int currentIdx() {
        return currentIdx;
    }

    int count() {
        synchronized (steps) {
            return steps.size();
        }
    }

    private void updateStepSequence() {
        synchronized (steps) {
            ArrayList<Step> newSteps = new ArrayList<>(steps.size());

            for (int i = 0; i <= currentIdx; i++) {
                newSteps.add(steps.get(i));
            }
            for (int i = currentIdx + 1; i < steps.size(); i++) {
                steps.get(i).free();
            }

            Step last = newSteps.get(newSteps.size() - 1);

            while (last.hasNextStep()) {
                Step next = last.createNextStep();
                if (next == null) {
                    throw new RadixError("Wizard page " + last.getDisplayName() + " (" + last.getClass().getName() + ") expects the next page but did not create one\n hasNextStep() returns true but createNextStep() returns null");
                }
                next.ownerSteps = this;

                newSteps.add(next);
                last = next;
            }
            steps.clear();
            steps.addAll(newSteps);
        }
    }

    protected final List<Step> allSteps() {
        synchronized (this) {
            return Collections.unmodifiableList(steps);
        }
    }

    protected final List<Step> stepsToCurrent() {
        synchronized (this) {
            ArrayList<Step> range = new ArrayList<>();
            for (int i = 0; i <= currentIdx; i++) {
                range.add(steps.get(i));
            }
            return range;
        }
    }

    protected final void moveForward() {
        synchronized (steps) {
            if (currentIdx < steps.size()) {
                if (currentIdx < 0) {
                    currentIdx++;
                    Step init = createInitial();
                    init.ownerSteps = this;
                    steps.add(init);
                    init.open(getSettings());
                    updateStepSequence();
                } else {
                    current().apply(getSettings());
                    updateStepSequence();
                    currentIdx++;
                    current().open(getSettings());
                    updateStepSequence();
                }
                changeSupport.fireChange();
            }
        }
    }

    protected final void moveBackward() {
        synchronized (steps) {
            if (currentIdx > 0) {
                current().apply(getSettings());
                currentIdx--;
                current().open(getSettings());
                updateStepSequence();
                changeSupport.fireChange();
            }
        }
    }

    protected Object getSettings() {
        synchronized (this) {
            if (settings == null) {
                settings = createSettings();
                if (settings == null) {
                    settings = new Object();
                }
            }
            return settings;
        }
    }
    private boolean finished = false;
    private boolean cancelled = false;

    void finish() {
        synchronized (this) {
            if (!finished) {
                finished = true;
                current().apply(getSettings());
                finished();
            }
        }
    }

    void cancel() {
        synchronized (this) {
            if (!cancelled) {
                cancelled = true;
                current().cancel(getSettings());
                cancelled();
            }
        }
    }

    protected void finished() {
    }

    protected void cancelled() {
    }

    boolean hasNextStep() {
        synchronized (steps) {
            if (currentIdx >= 0) {
                return current().hasNextStep();
            } else {
                return true;
            }
        }
    }

    boolean hasPrevStep() {
        return currentIdx > 0;
    }

    void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public abstract Step createInitial();

    public abstract Object createSettings();

    public abstract String getDisplayName();
}
