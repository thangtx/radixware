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

package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringResult;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringStepPanel;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringSteps;
import org.radixware.kernel.designer.ads.editors.refactoring.components.ObjectUsagesStep;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


final class PullUpSteps extends RefactoringSteps implements ObjectUsagesStep.IObjectUsagesStep {

    public static final class PullUpSettings extends Settings implements ObjectUsagesStep.IObjectUsagesSettings {

        private static class UsagesFilter implements ObjectUsagesStep.IFilter {

            @Override
            public boolean subSearch(RadixObject object) {
                return false;
            }

            @Override
            public boolean accept(RadixObject object) {
                return true;
            }

            @Override
            public boolean isCheckable(RadixObject object, boolean isLeaf) {
                return false;
            }

            @Override
            public boolean isSelected(RadixObject object, boolean isLeaf) {
                return true;
            }
        }
        private final AdsClassDef sourceClass;
        private AdsClassDef destinationClass;
        private final AdsDefinition member;
        private Map<RadixObject, List<RadixObject>> allUsages;
        private Map<RadixObject, List<RadixObject>> selectedUsages;
        private final ObjectUsagesStep.IFilter usagesFilter = new UsagesFilter();

        public PullUpSettings(AdsDefinition member) {
            this.member = member;

            if (member instanceof IAdsClassMember) {
                this.sourceClass = ((IAdsClassMember) member).getOwnerClass();
            } else {
                this.sourceClass = null;
            }
        }

        AdsClassDef getSourceClass() {
            return sourceClass;
        }

        @Override
        public AdsDefinition getDefinition() {
            return member;
        }

        AdsClassDef getDestinationClass() {
            return destinationClass;
        }

        private void setDestinationClass(AdsClassDef destinationClass) {
            this.destinationClass = destinationClass;
        }

        @Override
        public Map<RadixObject, List<RadixObject>> getAllUsages() {
            return allUsages;
        }

        @Override
        public Map<RadixObject, List<RadixObject>> getSelectedUsages() {
            return selectedUsages;
        }

        @Override
        public void setUsages(Map<RadixObject, List<RadixObject>> all, Map<RadixObject, List<RadixObject>> selected) {
            allUsages = all;
            selectedUsages = selected;
        }

        @Override
        public ObjectUsagesStep.IFilter getUsagesFilter() {
            return usagesFilter;
        }
    }

    private final class ChooseDestinationClassStep extends RefactoringStep<ChooseDestinationClassPanel> {

        @Override
        public String getDisplayName() {
            return "Choose destination class";
        }

        @Override
        protected ChooseDestinationClassPanel createVisualPanel() {
            ChooseDestinationClassPanel hierarcyTree = new ChooseDestinationClassPanel(getSettings());
            hierarcyTree.addPropertyChangeListener(RefactoringStepPanel.PROP_NAME_CHANGES, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    getSettings().setDestinationClass(getVisualPanel().getSelectedClass());
                    fireChange();
                }
            });
            return hierarcyTree;
        }

        @Override
        public boolean hasNextStep() {
            return true;
        }

        @Override
        public ObjectUsagesStep createNextStep() {
            return new ObjectUsagesStep(PullUpSteps.this, false, null);
        }

        @Override
        public void open(Object settings) {

            final AdsClassDef selectClass = getSettings().getDestinationClass();
            final AdsDefinition member = getSettings().getDefinition();

            if (member instanceof IAdsClassMember) {
                getVisualPanel().open(((IAdsClassMember) member).getOwnerClass(), selectClass);
            }
        }

        @Override
        public boolean isComplete() {
            return getSettings().getDestinationClass() != null;
        }

        @Override
        protected OperationStatus check() {

            final OperationStatus status = new OperationStatus();

            if (getSettings().getDestinationClass() == null) {
                status.addEvent(new OperationStatus.Event(OperationStatus.EEventType.ERROR, NbBundle.getMessage(PullUpSteps.class, "PullUp.Error.NotSelected")));
            }

            if (isDuplicate()) {
                status.addEvent(new OperationStatus.Event(OperationStatus.EEventType.ERROR, NbBundle.getMessage(PullUpSteps.class, "PullUp.Error.Duplicate")));
            }

            if (isStaticToInterface()) {
                status.addEvent(new OperationStatus.Event(OperationStatus.EEventType.ERROR, NbBundle.getMessage(PullUpSteps.class, "PullUp.Error.StaticToInterface")));
            }
            return status;

        }

        private boolean isDuplicate() {
            AdsClassDef destinationClass = getSettings().getDestinationClass();
            if (destinationClass != null) {
                return (getSettings().getDestinationClass().getMethods().findById(member.getId(), EScope.LOCAL).get() != null);
            }

            return false;
        }

        private boolean isStaticToInterface() {
            boolean isInterface = getSettings().getDestinationClass() instanceof AdsInterfaceClassDef,
                    isStatic = member instanceof AdsMethodDef && ((AdsMethodDef) member).getProfile().getAccessFlags().isStatic();
            return isInterface && isStatic;
        }

        @Override
        public PullUpSettings getSettings() {
            return PullUpSteps.this.getSettings();
        }
    }
    private final AdsDefinition member;

    public PullUpSteps(AdsDefinition method) {
        this.member = method;
    }

    @Override
    public ChooseDestinationClassStep createInitial() {
        return new ChooseDestinationClassStep();
    }

    @Override
    public PullUpSettings createSettings() {
        return new PullUpSettings(member);
    }

    @Override
    public String getDisplayName() {
        return "Pull Up";
    }

    @Override
    public PullUpSettings getSettings() {
        return (PullUpSettings) super.getSettings();
    }

    @Override
    protected RefactoringResult perform() {

        final PullUpOperation operation = PullUpOperation.Factory.newInstance(getSettings());
        final OperationStatus status = operation.perform();

        if (status == OperationStatus.OK) {
            final AdsDefinition result = operation.getResult();
            if (result != null) {
                selectInTree(result);
            }
        }

        RefactoringResult result = new RefactoringResult("PullUp");

        return result;
    }

    private void selectInTree(final RadixObject node) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NodesManager.selectInProjects(node);
            }
        });
    }
}
