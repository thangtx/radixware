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

package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.tags.PropAbstractTag;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus.Event;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringResult;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringStepPanel;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringSteps;
import org.radixware.kernel.designer.ads.editors.refactoring.components.ObjectUsagesStep;
import org.radixware.kernel.designer.ads.editors.refactoring.components.RadixObjectTreeNode;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


final class ReplaceSteps extends RefactoringSteps implements ObjectUsagesStep.IObjectUsagesStep {

    public static final class ReplaceChange extends RefactoringResult.Change {

        private final RadixObject radixObject;

        public ReplaceChange(RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        @Override
        public void open() {
            DialogUtils.goToObject(radixObject);
        }

        @Override
        public String getDescription() {
            final RadixObject displayDefinition = getDisplayableObject();
            return RadixObjectTreeNode.getDisplayName(displayDefinition, RadixObjectTreeNode.getObjectLocation(displayDefinition));
        }

        private RadixObject getDisplayableObject() {
            return radixObject.getDefinition();
        }

        @Override
        public Image getIcon() {
            final RadixIcon radixIcon = getDisplayableObject().getIcon();
            if (radixIcon != null) {
                final Image image = radixIcon.getImage();
                if (image != null) {
                    return image;
                }
            }
            return RadixObjectIcon.UNKNOWN.getImage();
        }
    }

    private static class UsagesFilter implements ObjectUsagesStep.IFilter {

        private final AdsDefinition root;

        public UsagesFilter(AdsDefinition root) {
            this.root = root.getHierarchy().findOverwriteBase().get();
        }

        @Override
        public boolean accept(RadixObject object) {

            if (root instanceof AdsPropertyPresentationPropertyDef != object instanceof AdsPropertyPresentationPropertyDef) {
                return true;
            }

            if (object instanceof AdsDefinition) {
                AdsDefinition curr = (AdsDefinition) object;

                while (curr != null) {
                    curr = curr.getHierarchy().findOverwriteBase().get();
                    if (curr == root) {
                        return true;
                    }
                    curr = curr.getHierarchy().findOverridden().get();
                }
            }
            return false;
        }

        @Override
        public boolean isCheckable(RadixObject object, boolean isLeaf) {
            if (!isLeaf) {
                return true;
            }

            if (object.isReadOnly()) {
                return false;
            }

            if (object instanceof JmlTagId) {
                return true;
            }

            if (object instanceof AdsEditorPageDef.Properties) {
                return true;
            }
            if (object instanceof AdsSelectorPresentationDef.SelectorColumn) {
                return true;
            }
            if (object instanceof PropAbstractTag) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isSelected(RadixObject object, boolean isLeaf) {
            return isCheckable(object, isLeaf) && isLeaf;
        }

        @Override
        public boolean subSearch(RadixObject object) {
            return object instanceof AdsPropertyPresentationPropertyDef;
        }
    }

    public static final class ReplaceSettings extends Settings implements ObjectUsagesStep.IObjectUsagesSettings {

        private AdsDefinition substitute;
        private final AdsDefinition member;
        private Map<RadixObject, List<RadixObject>> allUsages;
        private Map<RadixObject, List<RadixObject>> selectedUsages;
        private final ObjectUsagesStep.IFilter usagesFilter;

        public ReplaceSettings(AdsDefinition member) {
            this.member = member;

            usagesFilter = new UsagesFilter(member);
        }

        @Override
        public AdsDefinition getDefinition() {
            return member;
        }

        AdsDefinition getSubstitute() {
            return substitute;
        }

        private void setSubstitute(AdsDefinition substitute) {
            this.substitute = substitute;
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

    private final class ChooseDestinationSubstituteStep extends RefactoringStep<ChooseSubstitutePanel> {

        @Override
        public String getDisplayName() {
            return "Choose substitute";
        }

        @Override
        protected ChooseSubstitutePanel createVisualPanel() {
            ChooseSubstitutePanel hierarcyTree = new ChooseSubstitutePanel();
            hierarcyTree.addPropertyChangeListener(RefactoringStepPanel.PROP_NAME_CHANGES, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    getSettings().setSubstitute(getVisualPanel().getSelectedSubstitute());
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
            return new ObjectUsagesStep(ReplaceSteps.this, true, "Select what should be replaced");
        }

        @Override
        public void open(Object settings) {

            final AdsDefinition substitute = getSettings().getSubstitute();
            final AdsDefinition member = getSettings().getDefinition();

            if (member instanceof IAdsClassMember) {
                getVisualPanel().open(member, substitute);
            }
        }

        @Override
        protected OperationStatus check() {
            final OperationStatus status = new OperationStatus();
            final AdsDefinition substitute = getSettings().getSubstitute();

            if (substitute == null) {
                status.addEvent(new Event(OperationStatus.EEventType.ERROR, NbBundle.getMessage(ReplaceSteps.class, "Replace.Error.NotSelected")));

            } else {
                if (substitute.getAccessMode() == EAccess.PRIVATE) {
                    status.addEvent(new Event(OperationStatus.EEventType.WARNING,
                            NbBundle.getMessage(ReplaceSteps.class, "Replace.Warning.AccessLevelPrivate")));
                }

                if (substitute.getAccessMode() == EAccess.DEFAULT) {
                    status.addEvent(new Event(OperationStatus.EEventType.WARNING,
                            NbBundle.getMessage(ReplaceSteps.class, "Replace.Warning.AccessLevelDefault")));
                }

                final AdsDefinition definition = getSettings().getDefinition();
                if (substitute instanceof IAccessible && definition instanceof IAccessible) {
                    final boolean substituteStatic = ((IAccessible) substitute).getAccessFlags().isStatic();
                    final boolean definitionStatic = ((IAccessible) definition).getAccessFlags().isStatic();
                    if (substituteStatic && !definitionStatic) {
                        status.addEvent(new Event(OperationStatus.EEventType.WARNING,
                                NbBundle.getMessage(ReplaceSteps.class, "Replace.Warning.NonstaticToStatic")));
                    } else if (!substituteStatic && definitionStatic) {
                        status.addEvent(new Event(OperationStatus.EEventType.WARNING,
                                NbBundle.getMessage(ReplaceSteps.class, "Replace.Warning.StaticToNonstatic")));
                    }
                }
            }

            return status;
        }

        @Override
        public ReplaceSettings getSettings() {
            return ReplaceSteps.this.getSettings();
        }
    }
    private final AdsDefinition member;

    public ReplaceSteps(AdsDefinition method) {
        this.member = method;
    }

    @Override
    public ChooseDestinationSubstituteStep createInitial() {
        return new ChooseDestinationSubstituteStep();
    }

    @Override
    public ReplaceSettings createSettings() {
        return new ReplaceSettings(member);
    }

    @Override
    public String getDisplayName() {
        return "Replace";
    }

    @Override
    public ReplaceSettings getSettings() {
        return (ReplaceSettings) super.getSettings();
    }

    @Override
    protected RefactoringResult perform() {
        ReplaceOperation.Factory.newInstance(getSettings()).perform();

        final StringBuilder description = new StringBuilder();
        description.append("Replace '");

        final AdsDefinition definition = getSettings().getDefinition();
        final AdsDefinition substitute = getSettings().getSubstitute();

        description.append(RadixObjectTreeNode.getDisplayName(definition, ""));
        description.append("' to '");
        description.append(RadixObjectTreeNode.getDisplayName(substitute, ""));
        description.append("'");

        final RefactoringResult result = new RefactoringResult(description.toString());

        for (final RadixObject radixObject : UsagesFinder.toList(getSettings().getSelectedUsages())) {
            result.addChange(new ReplaceChange(radixObject));
        }

        return result;
    }

    @Override
    protected boolean showResult() {
        return true;
    }
}
