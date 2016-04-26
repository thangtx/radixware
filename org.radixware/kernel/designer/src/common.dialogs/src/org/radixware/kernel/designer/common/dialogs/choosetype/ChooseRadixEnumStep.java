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
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;



class ChooseRadixEnumStep extends ChooseTypeFromListStep<RadixEnumSelector, Definition> {

    @Override
    void apply(Settings settings) {
        settings.typeModel.setObject(currentValue);
        settings.typeModel.setNature(ETypeNature.RADIX_TYPE);
        settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) currentValue));
    }

    @Override
    void open(Settings settings) {
        getEditor().open(settings.filter, currentValue);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixTypeStep.class, "TypeWizard-ChooseRadixEnumStep-DisplayName");
    }

    @Override
    protected RadixEnumSelector createEditor() {
        RadixEnumSelector classSelectorPanel = new RadixEnumSelector();
        classSelectorPanel.addSelectionListener(this);
        classSelectorPanel.addActionListener(createSelectActionListener());
        return classSelectorPanel;
    }

    @Override
    public void selectionChanged(Definition newValue) {

        assert newValue == null || newValue instanceof AdsEnumDef : "Incompatible definition type";

        isComplete = newValue instanceof AdsEnumDef;
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }
}

final class RadixEnumSelector extends DefinitionSelector<ITypeFilter> {

    @Override
    protected void updateList() {

        Definition context = getContext().getContext();
        IAdsTypedObject typedObject = getContext().getTypedObject();

        VisitorProvider provider = null;

        if (typedObject != null) {
            EValType suitable = null;
            for (EValType valType : RadixTypeItem.ENUM_TYPES) {
                if (typedObject.isTypeAllowed(valType)) {
                    if (suitable == null) {
                        suitable = valType;
                    } else {
                        suitable = null;
                        break;
                    }
                }
            }

            if (suitable != null) {
                provider = typedObject.getTypeSourceProvider(suitable);
            }
        }

        if (provider == null) {
            provider = new EnumChooseProvider();
        }

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(context, provider);
        definitionsPanel.open(cfg);
    }

    private static final class EnumChooseProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject obj) {
            return obj instanceof AdsEnumDef;
        }

        @Override
        public boolean isContainer(RadixObject object) {
            return true;
        }
    }
}