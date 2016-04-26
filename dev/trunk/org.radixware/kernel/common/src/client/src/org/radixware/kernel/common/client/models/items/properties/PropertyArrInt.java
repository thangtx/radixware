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
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrInt;

public class PropertyArrInt extends PropertyArr<Arr<? extends Object>> {

    public PropertyArrInt(Model owner, RadPropertyDef propDef) {
        super(owner, propDef);
    }
    
    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }        

    @Override
    //Must be overriden if enum
    public Class<?> getValClass() {
        return ArrInt.class;
    }

    @Override
    public void setValObjectImpl(Object x) {
        if ((x == null) || (x instanceof Arr)) {
            setInternalVal((Arr<? extends Object>) x);
        } else {
            throw new IllegalArgumentException("array of integers or enums expected, but instance of \"" + x.getClass().getName() + "\" got");
        }
    }

    @Override
    public final EValType getType() {
        return EValType.ARR_INT;
    }
}
