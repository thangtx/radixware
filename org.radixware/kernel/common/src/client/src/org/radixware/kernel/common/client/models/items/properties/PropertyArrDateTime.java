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

package org.radixware.kernel.common.client.models.items.properties;

import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrDateTime;

public class PropertyArrDateTime extends PropertyArr<ArrDateTime> {

    public PropertyArrDateTime(Model owner, RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }    

    @Override
    public Class<?> getValClass() {
        return ArrDateTime.class;
    }

    @Override
    public void setValObjectImpl(Object x) {
        setInternalVal((ArrDateTime) x);
    }

    @Override
    public final EValType getType() {
        return EValType.ARR_DATE_TIME;
    }
}
