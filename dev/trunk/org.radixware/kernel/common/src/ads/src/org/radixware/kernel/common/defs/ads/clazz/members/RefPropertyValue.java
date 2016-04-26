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
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class RefPropertyValue extends PropertyValue {

    RefPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    RefPropertyValue(AdsPropertyDef context) {
        super(context);
    }
    RefPropertyValue(AdsPropertyDef context,RefPropertyValue source) {
        super(context,source);
    }
    @Override
    public boolean isTypeAllowed(EValType type) {
        return EValType.PARENT_REF == type;
    }
    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return true;
    }
    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine != null) {
            DdsReferenceDef ref = getParentReferenceInfo().findParentReference();
            if (ref != null) {
                return AdsVisitorProviders.newEntityObjectTypeProvider(ref.getParentTableId());
            } else {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            }
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }
    private ParentReferenceInfo getParentReferenceInfo() {
        return ((ParentRefProperty) getProperty()).getParentReferenceInfo();
    }
}
