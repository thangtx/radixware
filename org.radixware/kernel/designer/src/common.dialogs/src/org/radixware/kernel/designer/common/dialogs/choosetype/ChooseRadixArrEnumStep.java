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

import java.util.EnumMap;
import java.util.Map;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;


final class ChooseRadixArrEnumStep extends ChooseRadixEnumStep {

    @Override
    void apply(Settings settings) {
        settings.typeModel.setNature(ETypeNature.RADIX_TYPE);
        if (currentValue instanceof AdsEnumDef) {
            AdsEnumDef enumDef = (AdsEnumDef) currentValue;

            Map<EValType, EValType> typeMap = new EnumMap<EValType, EValType>(EValType.class);
            typeMap.put(EValType.INT, EValType.ARR_INT);
            typeMap.put(EValType.STR, EValType.ARR_STR);
            typeMap.put(EValType.CHAR, EValType.ARR_CHAR);

            EValType enumType = enumDef.getItemType();
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(typeMap.get(enumType), enumDef, null, settings.typeModel.getDimension()));
        }
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixTypeStep.class, "TypeWizard-Step3-EnumName");
    }
}
