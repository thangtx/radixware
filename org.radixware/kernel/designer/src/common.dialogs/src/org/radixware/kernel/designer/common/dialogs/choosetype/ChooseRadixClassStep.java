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

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.type.interfacing.RadixClassInterfacingVisitiorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


final class ChooseRadixClassStep extends ChooseTypeFromListStep<RadixClassSelector, Definition> {

    @Override
    void apply(Settings settings) {
        settings.typeModel.setObject(currentValue);
        settings.typeModel.setNature(ETypeNature.RADIX_TYPE);
        settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) currentValue));
    }

    @Override
    void open(Settings settings) {
        if (settings.wizardModel.isRefine()) {
            getEditor().enableRefine();
        }

        getEditor().open(settings.filter, currentValue);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixTypeStep.class, "TypeWizard-ChooseRadixClassStep-DisplayName");
    }

    @Override
    protected RadixClassSelector createEditor() {
        RadixClassSelector radixClassSelector = new RadixClassSelector();
        radixClassSelector.addSelectionListener(this);
        radixClassSelector.addActionListener(createSelectActionListener());
        return radixClassSelector;
    }

    @Override
    public void selectionChanged(Definition newValue) {

        if (newValue instanceof AdsClassDef) {
            typeModel.setTypeArguments(((AdsClassDef) newValue).getTypeArguments());
            boolean isGeneric = typeModel.isGeneric();
            hasNextStep = isGeneric;
            isFinishable = !isGeneric;
            isComplete = true;
        } else if (newValue instanceof AdsAbstractUIDef) {
            hasNextStep = false;
            isComplete = true;
        } else {
            hasNextStep = false;
            isComplete = false;
        }
    }

    @Override
    public Step createNextStep() {
        return new SpecifyGenericArgumentsStep();
    }
}

final class RadixClassSelector extends DefinitionSelector<ITypeFilter> {

    private boolean isRefine;

    public void enableRefine() {
        isRefine = true;
    }

    public boolean isRefine() {
        return isRefine;
    }

    @Override
    protected void updateList() {
        Definition context = getContext().getContext();
        IAdsTypedObject typedObject = getContext().getTypedObject();

        ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
        if (context instanceof IEnvDependent) {
            env = ((IEnvDependent) context).getUsageEnvironment();
        }

        final VisitorProvider provider = typedObject != null
                ? typedObject.getTypeSourceProvider(EValType.USER_CLASS)
                : AdsVisitorProviders.newClassBasedTypesProvider(env);

        VisitorProvider wrapperProvider = new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (!provider.isTarget(radixObject)) {
                    return false;
                } else {
                    if (radixObject instanceof AdsUserReportClassDef) {
                        return false;
                    } else {
                        return true;
                    }
                }

            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return provider.isContainer(radixObject);
            }
        };

        if (isRefine()) {
            AdsTypeDeclaration baseType = getContext().getBaseType();
            AdsClassType adsType = (AdsClassType) baseType.resolve(context).get();

            wrapperProvider = new RadixClassInterfacingVisitiorProvider(wrapperProvider, adsType.getSource());
        }

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(context, wrapperProvider);
        definitionsPanel.open(cfg);
    }
}
