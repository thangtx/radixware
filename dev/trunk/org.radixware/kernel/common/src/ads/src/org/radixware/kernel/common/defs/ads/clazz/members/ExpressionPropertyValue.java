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


public class ExpressionPropertyValue extends DbPropertyValue {

    ExpressionPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    ExpressionPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    ExpressionPropertyValue(AdsPropertyDef context, ExpressionPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        if (getProperty().isInvisibleForArte()) {
            return type == EValType.ANY;
        }
        return type != null && type.isAllowedForInnateProperty();
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        if (getProperty().isInvisibleForArte()) {
            return false;
        }
        return type.isEnumAssignableType() || type == EValType.PARENT_REF || type == EValType.ARR_REF || type == EValType.USER_CLASS;
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (getProperty().isInvisibleForArte()) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        if (toRefine == null) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        if (toRefine.isEnumAssignableType()) {
            return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
        } else if (toRefine == EValType.PARENT_REF || toRefine == EValType.ARR_REF) {
            return AdsVisitorProviders.newEntityObjectTypeProvider(null);
        } else if (toRefine == EValType.XML) {
            return AdsVisitorProviders.newXmlBasedTypesProvider(this.getProperty().getUsageEnvironment());
        } else if (toRefine == EValType.USER_CLASS) {
            return AdsVisitorProviders.newClassBasedTypesProvider(this.getProperty().getUsageEnvironment());
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }

    @Override
    public void setType(AdsTypeDeclaration type) {
        if (getProperty().isInvisibleForArte()) {
            return;
        }
        super.setType(type);
        ServerPresentationSupport ps = ((IAdsPresentableProperty) getProperty()).getPresentationSupport();
        if (ps != null) {
            ps.checkPresentation();
        }
    }

    @Override
    protected AdsExpressionPropertyDef getProperty() {
        return (AdsExpressionPropertyDef) super.getProperty();
    }

    @Override
    public AdsTypeDeclaration getType() {
        if (getProperty().isInvisibleForArte()) {
            return AdsTypeDeclaration.ANY;
        } else {
            return super.getType();
        }
    }

    @Override
    protected AdsTypeDeclaration getPersistentType() {
        return super.getType();
    }
}
