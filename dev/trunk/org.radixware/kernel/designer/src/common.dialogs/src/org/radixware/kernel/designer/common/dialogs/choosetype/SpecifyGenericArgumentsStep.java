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

import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;


public class SpecifyGenericArgumentsStep extends GeneralChooseTypeStep<GenericParametersPanel> {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseJavaClassStep.class, "TypeWizard-Step3-Name");
    }

    @Override
    protected GenericParametersPanel createVisualPanel() {
        GenericParametersPanel panel = new GenericParametersPanel();
        return panel;
    }

    @Override
    void apply(Settings settings) {
        AdsTypeDeclaration classType = null, type = typeModel.getType();
        if (type != null) {
            if (type.getTypeId() == EValType.JAVA_CLASS) {
                classType = AdsTypeDeclaration.Factory.newPlatformClass(typeModel.getType().getExtStr());
            } else if (type.getTypeId() == EValType.USER_CLASS) {
                AdsType adsType = type.resolve(settings.filter.getContext()).get();

                if (adsType instanceof ArrayType) {
                    adsType = ((ArrayType)adsType).getItemType();
                }
                if (adsType instanceof AdsClassType) {
                    AdsClassDef classDef = ((AdsClassType) adsType).getSource();
                    classType = AdsTypeDeclaration.Factory.newInstance(classDef);
                }
            }
        }

        if (classType != null) {
            settings.typeModel.setType(createGenericType(classType, getVisualPanel().getGenericParameters()));
        }
    }

    @Override
    void open(Settings settings) {
//        boolean onCreation = (settings.wizardModel.getMode() == ETypeWizardMode.CHOOSE_TYPE_NATURE);
        Definition definition = settings.filter.getContext();
        IAdsTypedObject typedObject = settings.filter.getTypedObject();

        getVisualPanel().open(definition, typedObject, settings.typeModel.getTypeArguments());
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

    private AdsTypeDeclaration createGenericType(AdsTypeDeclaration type, List<AdsTypeDeclaration.TypeArgument> oldArguments) {
        AdsTypeDeclaration.TypeArguments newArguments = AdsTypeDeclaration.TypeArguments.Factory.newInstance(type);
        for (AdsTypeDeclaration.TypeArgument arg : oldArguments) {
            newArguments.add(arg);
        }
        return type.toGenericType(newArguments);
    }
}
