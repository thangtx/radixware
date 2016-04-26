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

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class ParameterPropertyValue extends DynamicPropertyValue {

    ParameterPropertyValue(final AdsPropertyDef context, final AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    ParameterPropertyValue(final AdsPropertyDef context) {
        super(context);
    }

    ParameterPropertyValue(final AdsPropertyDef context, final ParameterPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(final EValType type) {
        if (getProperty().getOwnerClass() instanceof AdsReportClassDef){
            return super.isTypeAllowed(type);
        } else {
            return ValTypes.ADS_SQL_CLASS_PARAM_TYPES.contains(type);
        }
    }

    @Override
    protected AdsParameterPropertyDef getProperty() {
        return (AdsParameterPropertyDef) super.getProperty();
    }
}
