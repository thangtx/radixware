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
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class EventCodePropertyValue extends DynamicPropertyValue {

    public EventCodePropertyValue(AdsPropertyDef context, DynamicPropertyValue source) {
        super(context, source);
    }

    public EventCodePropertyValue(AdsPropertyDef context) {
        super(context);
    }

    public EventCodePropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        return VisitorProviderFactory.createEmptyVisitorProvider();
    }

    @Override
    protected boolean isJmlInitValAllowed() {
        return false;
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        return type == EValType.STR;
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return false;
    }

    @Override
    public void setType(AdsTypeDeclaration type) {
        if (type.getTypeId() == EValType.STR && type.getArrayDimensionCount() == 0) {
            super.setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
        }
    }

    @Override
    public AdsValAsStr getInitial() {
        Id id = ((AdsEventCodePropertyDef) getProperty()).getEventId();
        if (id == null) {
            return null;
        }
        AdsMultilingualStringDef string = getProperty().findLocalizedString(id);
        if (string == null) {
            return null;
        }

        return AdsValAsStr.Factory.newInstance(string.getOwnerBundle().getId().toString() + "-" + string.getId().toString());
    }

    @Override
    public IValueController getInitialValueController() {
        return super.getInitialValueController();
    }
    
}
