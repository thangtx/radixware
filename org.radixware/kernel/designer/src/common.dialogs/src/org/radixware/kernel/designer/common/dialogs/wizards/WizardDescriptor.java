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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static org.openide.NotifyDescriptor.CANCEL_OPTION;
import static org.openide.NotifyDescriptor.CLOSED_OPTION;
import static org.openide.WizardDescriptor.FINISH_OPTION;
import org.openide.util.HelpCtx;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager.State;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


public final class WizardDescriptor extends org.openide.WizardDescriptor {

    private static final class InternalPanel implements Panel, FinishablePanel {

        private WizardSteps.Step step;

        InternalPanel(WizardSteps.Step step) {
            this.step = step;
        }

        @Override
        public Component getComponent() {
            return step.getVisualPanel();
        }

        @Override
        public HelpCtx getHelp() {
            return null;
        }

        @Override
        public void readSettings(Object settings) {
        }

        @Override
        public void storeSettings(Object settings) {
        }

        @Override
        public boolean isValid() {
            return step.isComplete();
        }

        @Override
        public void addChangeListener(ChangeListener l) {
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
        }

        @Override
        public boolean isFinishPanel() {
            return step.isFinishiable();
        }
    }

    private static final class InternalIterator implements org.openide.WizardDescriptor.Iterator {

        private final WizardSteps steps;
        private WizardDescriptor desc;

        InternalIterator(WizardSteps steps) {
            this.steps = steps;
            this.steps.moveForward();
        }

        @Override
        public Panel current() {
            Panel p = this.steps.current().netbeansWizardPanel;
            if (p == null) {
                p = new InternalPanel(steps.current());
                steps.current().netbeansWizardPanel = p;
            }
            return p;
        }

        @Override
        public String name() {
            return steps.getDisplayName();
        }

        @Override
        public boolean hasNext() {
            return steps.hasNextStep();
        }

        @Override
        public boolean hasPrevious() {
            return steps.hasPrevStep();
        }

        @Override
        public void nextPanel() {
            desc.stateDisplayer.getStateContext().reset();
            steps.moveForward();
        }

        @Override
        public void previousPanel() {
            desc.stateDisplayer.getStateContext().reset();
            steps.moveBackward();
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            this.steps.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            this.steps.removeChangeListener(l);
        }
    }

    class StateDisplayer implements IStateDisplayer {

        private final StateContext context = new StateContext() {

            @Override
            public void updateMessage(final State state, final String newMessage) {
                putProperty(PROP_ERROR_MESSAGE, null);
                putProperty(PROP_WARNING_MESSAGE, null);
                switch (state) {
                    case ERROR:

                        putProperty(PROP_ERROR_MESSAGE, newMessage);
                        break;
                    case WARNING:

                        putProperty(PROP_WARNING_MESSAGE, newMessage);
                        break;
                    case OK:
                        break;
                }
            }
        };

        @Override
        public StateContext getStateContext() {
            return context;
        }
    }

    private class WizardListener implements ChangeListener, PropertyChangeListener {

        @Override
        public void stateChanged(final ChangeEvent e) {
            updateStepsInformation();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (PROP_VALUE.equals(evt.getPropertyName())) {
                Object newValue = evt.getNewValue();
                if (newValue == FINISH_OPTION) {
                    steps.finish();
                } else if (newValue == CANCEL_OPTION || newValue == CLOSED_OPTION) {
                    steps.cancel();
                // WORKAROUND to solve problem with FINISH_OPTION (since Netbeans 7.3)
                } else if (newValue == getClosingOptions()[0]) {
                    steps.finish();
                }
            }
        }
    }
    private WizardSteps steps;
    StateDisplayer stateDisplayer = new StateDisplayer();

    public WizardDescriptor(final WizardSteps steps) {
        this(new InternalIterator(steps));
        setTitle(steps.getDisplayName());
    }

    private WizardDescriptor(final InternalIterator iter) {
        super(iter);
        setTitleFormat(new MessageFormat("{0}"));

        iter.desc = this;
        this.steps = iter.steps;
        this.steps.wizardDescriptor = this;
        WizardListener listener = new WizardListener();
        updateStepsInformation();
        this.steps.addChangeListener(listener);
        updateState();
        this.addPropertyChangeListener(listener);
    }

    private void updateStepsInformation() {
        synchronized (this) {
            int stepIndex = 0;
            final List<Step> stepList = steps.allSteps();
            final String[] stepNames = new String[stepList.size()];
            for (WizardSteps.Step s : stepList) {
                stepNames[stepIndex] = s.getDisplayName();
                JComponent c = s.getVisualPanel();
                // Sets step number of a component
                c.putClientProperty(PROP_CONTENT_SELECTED_INDEX, new Integer(stepIndex));
                // Sets steps names for a panel
                c.putClientProperty(PROP_CONTENT_DATA, stepNames);
                // Turn on subtitle creation on each step
                c.putClientProperty(PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                // Show steps on the left side with the image on the background
                c.putClientProperty(PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                // Turn on numbering of all steps
                c.putClientProperty(PROP_CONTENT_NUMBERED, Boolean.TRUE);
                stepIndex++;
            }
            Step current = steps.current();
            String finalFormat;
            if (current != null) {
                finalFormat = current.getDisplayName();
            } else {
                finalFormat = "Unknown";
            }

            setTitleFormat(new MessageFormat(finalFormat));
        }
    }

    @Override
    protected synchronized void updateState() {
        super.updateState();
        setTitle(steps.getDisplayName());
    }
}
