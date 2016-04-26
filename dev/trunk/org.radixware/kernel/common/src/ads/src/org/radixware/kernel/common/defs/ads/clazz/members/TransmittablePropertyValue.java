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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class TransmittablePropertyValue extends DynamicPropertyValue {

    TransmittablePropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    TransmittablePropertyValue(AdsPropertyDef context) {
        super(context);
    }

    TransmittablePropertyValue(AdsPropertyDef context, TransmittablePropertyValue source) {
        super(context, source);
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine == EValType.USER_CLASS) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        return super.getTypeSourceProvider(toRefine);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {

        boolean allowed = type != null && ValTypes.FORM_PROPERTY_TYPES.contains(type);
        if (allowed && getProperty().getNature() == EPropNature.GROUP_PROPERTY) {
            return type != EValType.PARENT_REF && type != EValType.ARR_REF;
        }
        return allowed;
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        if (type == EValType.USER_CLASS) {
            return false;
        }
        return super.isTypeRefineAllowed(type);
    }
}
