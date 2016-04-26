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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ListItemSelector;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;



final class ChooseRadixTypeStep extends ChooseTypeFromListStep<RadixTypeSelector, RadixTypeItem> {

    private Step next;

    ChooseRadixTypeStep() {
        super();
    }

    @Override
    void open(TypeWizard.Settings settings) {
        currNature = ETypeNature.RADIX_TYPE;
        getEditor().open(settings.filter, currentValue);
    }

    private Object currClass = null;
    private ETypeNature currNature;

    @Override
    public void selectionChanged(RadixTypeItem newValue) {

        if (newValue != null) {
            isComplete = true;
            hasNextStep = true;
            isFinishable = true;
            state.ok();

            if (newValue.isSimple() || newValue.isEnumeration() || newValue.isArrEnumeration() || newValue.isModel()) {
                currClass = newValue.getType();
                if (currClass != null) {
                    boolean isStrictlyRefinable = RadixTypeItem.STRICTLY_REFINABLE.contains((EValType) currClass);
                    hasNextStep = isStrictlyRefinable;

                    if (currClass == EValType.XML) {
                        currNature = ETypeNature.RADIX_XML;
                        next = new ChooseRadixXmlSchemeStep();
                    } else if (currClass == EValType.USER_CLASS) {
                        currNature = ETypeNature.RADIX_CLASS;
                        next = new ChooseRadixClassStep();
                    } else {
                        currNature = ETypeNature.RADIX_TYPE;// ???
                        if (currClass == EValType.PARENT_REF || currClass == EValType.ARR_REF || currClass == EValType.OBJECT) {
                            next = new SpecifyTypeStep();
                        }
                    }

                    isFinishable = !isStrictlyRefinable || currClass == EValType.XML;
                } else if (newValue.isModel()) {
                    currNature = ETypeNature.RADIX_TYPE;
                    next = new ChooseModelOwnerStep();
                    isFinishable = false;
                } else {
                    currNature = ETypeNature.RADIX_ENUM;
                    next = newValue.isEnumeration() ? new ChooseRadixEnumStep() : new ChooseRadixArrEnumStep();
                    isFinishable = false;
                }
            } else {
                hasNextStep = false;
                isComplete = false;
            }
        } else {
            hasNextStep = false;
            isComplete = false;
        }
    }

    @Override
    void apply(TypeWizard.Settings settings) {

        typeModel.setObject(currClass);
        typeModel.setNature(currNature);

        if (currClass instanceof AdsTypeDeclaration) {
            typeModel.setType((AdsTypeDeclaration) currClass);
        } else if (currClass instanceof EValType) {
            typeModel.setType(AdsTypeDeclaration.Factory.newInstance((EValType) currClass));
        } else if (currClass instanceof IAdsTypeSource) {
            typeModel.setType(AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) currClass));
        }
    }

    @Override
    public Step createNextStep() {
        return next;
    }

    @Override
    protected RadixTypeSelector createEditor() {
        RadixTypeSelector typeSelectorPanel = new RadixTypeSelector();
        typeSelectorPanel.addSelectionListener(this);
        typeSelectorPanel.addActionListener(createSelectActionListener());
        return typeSelectorPanel;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixTypeStep.class, "TypeWizard-ChooseRadixTypeStep-DisplayName");
    }

}

final class RadixTypeSelector extends TypeListSelector<RadixTypeItem, ITypeFilter> {

    public RadixTypeSelector() {
        super(new ItemNameFilter<RadixTypeItem>());
        setListItemRender(new RadixTypeRender(itemList));
    }

    static final class RadixTypeRender extends RadixTypeCellRender {

        public RadixTypeRender(JList list) {
            super(list);
        }

        @Override
        public RadixIcon getObjectIcon(Object object) {

            if (object instanceof RadixTypeItem) {
                RadixTypeItem item = (RadixTypeItem) object;
                RadixIcon radixIcon = null;

                if (item.isSimple()) {
                    radixIcon = RadixObjectIcon.getForValType(item.getType());
                } else if (item.isEnumeration()) {
                    radixIcon = RadixObjectIcon.ENUM;
                } else if (item.isArrEnumeration()) {
                    radixIcon = RadixObjectIcon.ARR_ENUM;
                } else if (item.isModel()) {
                    radixIcon = AdsDefinitionIcon.CLASS_MODEL;
                } else if (item.getDefinition() != null) {
                    radixIcon = item.getDefinition().getIcon();
                }

                return radixIcon;
            }
            return null;
        }

    }
    private RadixTypeItem.TypeListModel listModel;

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {

            @Override
            protected ListModel assemblyModel() {
                if (listModel == null) {
                    ITypeFilter context = getContext();
                    listModel = RadixTypeItem.getSystemTypesModel(context.getTypedObject(), context.getContext());
                }

                Set<RadixTypeItem> items = new HashSet<RadixTypeItem>();
                for (RadixTypeItem item : listModel.getItems()) {
                    if (getState().get() == TaskState.CANCELLED || getState().get() == TaskState.RESTARTED) {
                        return null;
                    }
                    if (accept(item)) {
                        items.add(item);
                    }
                }

                return RadixTypeItem.getModelFor(items);
            }
        };
    }
}
