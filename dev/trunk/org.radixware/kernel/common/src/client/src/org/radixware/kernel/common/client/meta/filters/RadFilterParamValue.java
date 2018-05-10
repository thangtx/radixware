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

package org.radixware.kernel.common.client.meta.filters;

import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public final class RadFilterParamValue {
    
    private final Id paramId;
    private final EValType type;
    private final ValAsStr value;
    
    public RadFilterParamValue(final Id paramId, final EValType type, final ValAsStr value){
        this.paramId = paramId;
        this.type = type;
        this.value = value;
    }

    public Id getParamId() {
        return paramId;
    }

    public EValType getType() {
        return type;
    }

    public ValAsStr getValue() {
        return value;
    }
    
    public RadFilterParamValue changeParamId(final Id newParamId){
        return new RadFilterParamValue(newParamId, type, value);
    }
    
    public org.radixware.schemas.eas.PropertyList.Item writeToXml(final org.radixware.schemas.eas.PropertyList.Item xml){        
        final org.radixware.schemas.eas.PropertyList.Item paramXml = xml==null ? org.radixware.schemas.eas.PropertyList.Item.Factory.newInstance() : xml;
        final Object rawValue = value==null ? null : ValueConverter.valAsStr2Obj(value, type);
        paramXml.setId(paramId);
        ValueConverter.objVal2EasPropXmlVal(rawValue, type, paramXml);
        return paramXml;
    }
}
