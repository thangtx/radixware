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
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class FieldPropertyValue extends DbPropertyValue {

    FieldPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    FieldPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    FieldPropertyValue(AdsPropertyDef context, FieldPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        if (getProperty() instanceof AdsFieldRefPropertyDef) {
            return type == EValType.PARENT_REF;
        } else {
            return type != null && type.isAllowedForDdsColumn();
        }
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return type.isEnumAssignableType() || type == EValType.PARENT_REF;
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine == null) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        if (toRefine.isEnumAssignableType()) {
            return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
        } else if (toRefine == EValType.PARENT_REF || toRefine == EValType.ARR_REF) {
            return AdsVisitorProviders.newEntityTypeProvider(null);
        } else if (toRefine == EValType.XML) {
            return AdsVisitorProviders.newXmlBasedTypesProvider(this.getProperty().getUsageEnvironment());
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }

    @Override
    public void setType(AdsTypeDeclaration type) {
        super.setType(type);
        ServerPresentationSupport ps = ((IAdsPresentableProperty) getProperty()).getPresentationSupport();
        if (ps != null) {
            ps.checkPresentation();
        }
    }
}
