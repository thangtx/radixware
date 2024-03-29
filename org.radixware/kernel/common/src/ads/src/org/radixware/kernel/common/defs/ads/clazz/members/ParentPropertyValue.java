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
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class ParentPropertyValue extends PropertyValue {

    ParentPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    ParentPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    ParentPropertyValue(AdsPropertyDef context, ParentPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        AdsPropertyDef source = getParentProperty().getParentInfo().findOriginalProperty();
        if (source != null) {
            return source.getValue().getType().isBasedOn(type);
        } else {
            return false;
        }
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return false;
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
//        if (toRefine == null) {
//            return VisitorProviderFactory.createEmptyVisitorProvider();
//        }
//        AdsPropertyDef source = getParentProperty().getParentInfo().findOriginalProperty();
//        if (source != null) {
//            return source.getValue().getTypeSourceProvider(toRefine);
//        } else {
        return VisitorProviderFactory.createEmptyVisitorProvider();
        //}
    }

    private AdsParentPropertyDef getParentProperty() {
        return (AdsParentPropertyDef) getProperty();
    }
}
