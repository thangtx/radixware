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
import java.util.List;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsVarObject;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


class ChooseDefinitionWizard implements IChooseDefinitionModalDisplayer {

    private final ChooseDefinitionCfg cfg;
    private final boolean multipleSelectionAllowed;
    List<Definition> result = null;

    public ChooseDefinitionWizard(ChooseDefinitionCfg cfg, boolean multipleSelectionAllowed) {
        this.cfg = cfg;
        this.multipleSelectionAllowed = multipleSelectionAllowed;
    }

    private final class ChooseDefinitionStep extends Step<ChooseDefinitionPanel> {

        private final int currentStepNumber;
        private final Definition commonOwner;

        public ChooseDefinitionStep(int currentStepNumber, Definition commonOwner) {
            this.currentStepNumber = currentStepNumber;
            this.commonOwner = commonOwner;
        }

        public ChooseDefinitionStep() {
            this.currentStepNumber = ChooseDefinitionCfg.FIRST_STEP;
            this.commonOwner = null;
        }

        @Override
        protected ChooseDefinitionPanel createVisualPanel() {
            final ChooseDefinitionPanel chooseDefinitionPanel = new ChooseDefinitionPanel();

            chooseDefinitionPanel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ChooseDefinitionStep.this.fireChange();
                }
            });

            chooseDefinitionPanel.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    doNextOrFinish();
                }
            });

            return chooseDefinitionPanel;
        }

        @Override
        public void open(Object settings) {
            super.open(settings);
            final boolean multy = !hasNextStep() && multipleSelectionAllowed;
            getVisualPanel().open(cfg, multy, currentStepNumber, commonOwner);
        }

        @Override
        public String getDisplayName() {
            if (hasNextStep()) {
                return "Choose Owner";
            } else {
                return "Choose " + (multipleSelectionAllowed ? cfg.getTypesTitle() : cfg.getTypeTitle());
            }
        }

        @Override
        public void apply(Object settings) {
            if (!hasNextStep()) {
                result = getVisualPanel().getSelection();
            }
        }

        @Override
        public Step createNextStep() {
            final Definition nextCommonOwner = getVisualPanel().getSelected();
            return new ChooseDefinitionStep(currentStepNumber + 1, nextCommonOwner);
        }

        private boolean isAlgoProp() {
            final Definition def = getVisualPanel().getSelected();
            return (cfg.isForAlgo() && (
                    def instanceof AdsVarObject ||
                    def instanceof AdsAppObject.Prop ||                        
                    def instanceof AdsIncludeObject.Param ||                        
                    def instanceof AdsAlgoClassDef.Param ||
                    def instanceof AdsAlgoClassDef.Var));
        }
               
        @Override
        public boolean hasNextStep() {
            return currentStepNumber < cfg.getStepCount() && !isAlgoProp();
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().hasSelection();
        }

        @Override
        public boolean isFinishiable() {
            return !hasNextStep();
        }
    }

    private final class Steps extends WizardSteps {

        private final String title;

        public Steps(String title) {
            this.title = title;
        }

        @Override
        public Step createInitial() {
            return new ChooseDefinitionStep();
        }

        @Override
        public Object createSettings() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return title;
        }
    }

    @Override
    public boolean showModal() {
        final String title = "Choose " + (multipleSelectionAllowed ? cfg.getTypesTitle() : cfg.getTypeTitle());
        final WizardSteps steps = new Steps(title);
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(steps);

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setModal(true);
        dialog.setVisible(true);

        if (wizardDescriptor.getValue() == WizardDescriptor.FINISH_OPTION) {
            return (result != null);
        } else {
            return false;
        }
    }

    @Override
    public List<Definition> getResult() {
        return result;
    }
}
