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
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class UserPropertyValue extends PropertyValue {

    UserPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    UserPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    UserPropertyValue(AdsPropertyDef context, UserPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        return type != null && type.isAllowedForUserProperty();
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return type != null && (type.isEnumAssignableType() || type == EValType.PARENT_REF || type == EValType.ARR_REF || type == EValType.OBJECT || type == EValType.XML);
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine == null) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        if (toRefine.isEnumAssignableType()) {
            return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
        } else if (toRefine == EValType.PARENT_REF || toRefine == EValType.ARR_REF) {
            return AdsVisitorProviders.newEntityObjectTypeProvider(null);
        } else if (toRefine == EValType.OBJECT) {
            return AdsVisitorProviders.newUserPropObjectTypeProvider();
        } else if (toRefine == EValType.XML) {
            return AdsVisitorProviders.newXmlBasedTypesProvider(ERuntimeEnvironmentType.SERVER);
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }
}
