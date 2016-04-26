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
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


final class ChooseModelOwnerStep extends ChooseTypeFromListStep<ModelOwnerSelector, Definition> {

    @Override
    void selectionChanged(Definition newValue) {
        isComplete = newValue != null;

        if (newValue instanceof AdsParagraphExplorerItemDef
                || newValue instanceof AdsFormHandlerClassDef
                || newValue instanceof AdsCustomDialogDef
                || newValue instanceof AdsRwtCustomDialogDef) {
            isFinishable = true;
            typeModel.setObject(newValue);
        }
    }

    @Override
    void apply(Settings settings) {
        //REWORK  ETypeNature.RADIX_MODEL
        settings.typeModel.setNature(ETypeNature.RADIX_CLASS);

        settings.typeModel.setObject(currentValue);

        if (currentValue instanceof AdsParagraphExplorerItemDef) {
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(((AdsParagraphExplorerItemDef) currentValue).getModel()));
        } else if (currentValue instanceof AdsFormHandlerClassDef) {
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(((AdsFormHandlerClassDef) currentValue).getPresentations().getModel()));
        } else if (currentValue instanceof AdsCustomDialogDef) {
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(((AdsCustomDialogDef) currentValue).getModelClass()));
        }else if (currentValue instanceof AdsEntityObjectClassDef) {
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance((AdsEntityObjectClassDef) currentValue));
        }

    }

    @Override
    void open(Settings settings) {
        getEditor().open(settings.filter, currentValue);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SpecifyTypeStep.class, "TypeWizard-ChooseModelOwnerStep-DisplayName");
    }

    @Override
    protected ModelOwnerSelector createEditor() {
        ModelOwnerSelector modelSelector = new ModelOwnerSelector();
        modelSelector.addSelectionListener(this);
        modelSelector.addActionListener(createSelectActionListener());
        return modelSelector;
    }

    @Override
    public boolean isFinishiable() {
        return currentValue instanceof AdsEntityObjectClassDef;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public Step createNextStep() {
        return new ChooseRadixModelStep();
    }
}

final class ModelOwnerSelector extends DefinitionSelector<ITypeFilter> {

    @Override
    protected void updateList() {
        Definition context = getContext().getContext();

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(context, new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof IAdsPresentableClass
                        || radixObject instanceof AdsParagraphExplorerItemDef
                        || radixObject instanceof AdsCustomDialogDef)
                        && !(radixObject instanceof AdsEntityGroupClassDef);
            }
        });
        definitionsPanel.open(cfg);
    }
}
