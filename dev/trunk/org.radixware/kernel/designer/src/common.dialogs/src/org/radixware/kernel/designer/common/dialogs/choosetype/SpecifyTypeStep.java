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

import java.util.Collections;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;


class SpecifyTypeStep extends ChooseTypeFromListStep<SpecifyTypeSelector, Definition> {

    private Object radixClass;

    @Override
    public void open(Settings settings) {

        radixClass = settings.typeModel.getObject();
        assert radixClass instanceof EValType : "Invalid wizard state : SpecifyTypeStep";

        if (radixClass instanceof EValType) {
            getEditor().open(settings, currentValue);
        }
    }

    @Override
    public void apply(Settings settings) {

        if (radixClass instanceof EValType) {
            typeModel.setNature(ETypeNature.RADIX_TYPE);
            typeModel.setType(AdsTypeDeclaration.Factory.newInstance((EValType) radixClass, (IAdsTypeSource) currentValue, null, typeModel.getDimension()));
        }
    }

    @Override
    protected SpecifyTypeSelector createEditor() {
        SpecifyTypeSelector typeSelector = new SpecifyTypeSelector();
        typeSelector.addSelectionListener(this);
        return typeSelector;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SpecifyTypeStep.class, "TypeWizard-SpecifyTypeStep-DisplayName");
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

    @Override
    void selectionChanged(Definition newValue) {
        isComplete = newValue != null;
    }
}

final class SpecifyTypeSelector extends DefinitionSelector<Settings> {

    @Override
    protected void updateList() {
        Definition context = getContext().filter.getContext();
        IAdsTypedObject typedObject = getContext().filter.getTypedObject();

        EValType valType = (EValType) getContext().typeModel.getObject();

        if (typedObject != null) {
            definitionsPanel.open(ChooseDefinitionCfg.Factory.newInstance(context, typedObject.getTypeSourceProvider(valType)));
        } else {
            definitionsPanel.open(ChooseDefinitionCfg.Factory.newInstance(Collections.<Definition>emptyList()));
        }
    }
}
