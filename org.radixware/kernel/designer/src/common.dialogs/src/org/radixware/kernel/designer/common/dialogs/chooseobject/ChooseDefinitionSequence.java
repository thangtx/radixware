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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;



public class ChooseDefinitionSequence extends WizardSteps {

    public abstract static class ChooseDefinitionCfgs {

        protected final ChooseDefinitionCfg initialConfig;

        public ChooseDefinitionCfgs(final ChooseDefinitionCfg initialConfig) {
            this.initialConfig = initialConfig;
        }

        protected abstract ChooseDefinitionCfg nextConfig(Definition choosenDef);

        protected ChooseDefinitionCfg nextConfig(ChooseDefinitionCfg prevCfg, Definition choosenDef) {
            return nextConfig(choosenDef);
        }

        protected abstract boolean hasNextConfig(Definition choosenDef);

        protected abstract boolean isFinalTarget(Definition choosenDef);

        protected boolean isFinalTarget(ChooseDefinitionCfg config, Definition choosenDef) {
            return isFinalTarget(choosenDef);
        }

        public abstract String getDisplayName();
    }

    private class ChooseStep extends Step<ChooseDefinitionPanel> {

        private final transient ChooseDefinitionCfg cfg;
        private Definition choosenDef;
        private final ChooseDefinitionCfg prev;

        ChooseStep(final ChooseDefinitionCfg prev, final ChooseDefinitionCfg cfg) {
            this.cfg = cfg;
            this.prev = prev;
        }

        @Override
        public String getDisplayName() {
            return this.cfg.getTypesTitle();
        }

        private class PanelChangeListener implements ChangeListener {

            @Override
            public void stateChanged(final ChangeEvent e) {
                ChooseStep.this.fireChange();
            }
        }
        private final transient PanelChangeListener listener = new PanelChangeListener();

        @Override
        protected ChooseDefinitionPanel createVisualPanel() {
            final ChooseDefinitionPanel panel = new ChooseDefinitionPanel();
            panel.addChangeListener(listener);
            panel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    doNextOrFinish();
                }
            });
            return panel;
        }

        @Override
        public void apply(final Object settings) {
            super.apply(settings);
            choosenDef = getChoosenDef();
        }

        @Override
        public Step createNextStep() {
            final Definition choosen = getChoosenDef();
            if (choosen != null) {
                final ChooseDefinitionCfg nextCfg = configIterator.nextConfig(this.cfg, choosen);
                if (nextCfg == null) {
                    return null;
                } else {
                    return new ChooseStep(this.cfg, nextCfg);
                }
            } else {
                return null;
            }
        }

        private Definition getChoosenDef() {
            return getVisualPanel().getSelected();
        }

        @Override
        public boolean hasNextStep() {
            final Definition choosen = getChoosenDef();
            if (choosen != null) {
                return configIterator.hasNextConfig(choosen);
            } else {
                return false;
            }
        }

        @Override
        public boolean isComplete() {
            return getChoosenDef() != null;
        }

        @Override
        public boolean isFinishiable() {
            final Definition choosen = getChoosenDef();
            if (choosen != null) {
                return configIterator.isFinalTarget(this.cfg, choosen);
            } else {
                return false;
            }
        }

        @Override
        public void open(Object settings) {
            super.open(settings);
            getVisualPanel().open(cfg);
        }
    }
    ChooseDefinitionCfgs configIterator;

    public ChooseDefinitionSequence(ChooseDefinitionCfgs configIterator) {
        this.configIterator = configIterator;
    }

    @Override
    public Step createInitial() {
        return new ChooseStep(null, configIterator.initialConfig);
    }

    @Override
    public Object createSettings() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return configIterator.getDisplayName();
    }
    private List<Definition> selection = null;

    @Override
    protected void finished() {
        super.finished();
        selection = new ArrayList<Definition>();

        for (Step step : stepsToCurrent()) {
            if (step instanceof ChooseStep) {
                selection.add(((ChooseStep) step).choosenDef);
            }
        }
    }

    private List<Definition> getSelectedDefSequence() {
        if (selection == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(selection);
        }
    }

    public static final List<Definition> chooseDefinitionSequence(ChooseDefinitionCfgs configIterator) {
        ChooseDefinitionSequence steps = new ChooseDefinitionSequence(configIterator);
        WizardDescriptor wizard = new WizardDescriptor(steps);

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizard);
        dialog.setModal(true);
        dialog.setVisible(true);

        if (wizard.getValue() == WizardDescriptor.FINISH_OPTION) {
            return steps.getSelectedDefSequence();
        } else {
            return Collections.emptyList();
        }
    }
}
