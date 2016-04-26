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

package org.radixware.kernel.explorer.iad.sane.options;

import com.tuneology.sane.BooleanOption;
import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneException;
import org.radixware.kernel.common.client.IClientEnvironment;


final class SaneBooleanOptionValueAccessor extends SaneOptionValueAccessor{
    
    private Boolean cachedValue;
    private final BooleanOption option;
    
    public SaneBooleanOptionValueAccessor(BooleanOption optionDescriptor, IClientEnvironment env) {
        super(optionDescriptor, env);
        option = optionDescriptor;        
    }

    @Override
    public Integer getIntValue() {
        if (cachedValue==null){
            final boolean[] saneValues;
            try{
                saneValues = option.getValues();
            }catch(SaneException exception){
                traceReadValueException(exception);
                return null;
            }
            traceReadValue(saneValues);
            if (saneValues!=null && saneValues.length>0){
                cachedValue = Boolean.valueOf(saneValues[0]);
            }
        }        
        return cachedValue==null ? null : Integer.valueOf(cachedValue==Boolean.TRUE ? 1 : 0);
    }

    @Override
    public int setIntValue(int value) {
        if (cachedValue==null || cachedValue.booleanValue()!=(value!=0)){
            final int result;
            try{
                result = option.setValues(new boolean[]{value!=0});
            }catch(SaneException exception){
                traceWriteValueException(exception, Boolean.valueOf(value!=0));
                return -1;
            }
            traceWriteValue(value, result);
            notifyValueChanged();
            if ((result & Sane.INFO_INEXACT)==0){
                cachedValue = Boolean.valueOf(value!=0);
            }
            return result;
        }else{
            return 0;
        }
    }
    
    @Override
    public int setDoubleValue(double value) {
        return setIntValue((int)value);
    }

    @Override
    public Double getDoubleValue() {
        final Integer value = getIntValue();
        return value==null ? null : Double.valueOf(value.intValue());
    }    

    @Override
    public String getStringValue() {
        final Integer value = getIntValue();
        return value==null ? null : (value.intValue()>0 ? "true" : "false");
    }

    @Override
    public int setStringValue(final String value) {
        if ("1".equals(value) || "true".equals(value.toLowerCase())){
            return setIntValue(Integer.valueOf(1));
        }else{
            return setIntValue(Integer.valueOf(0));
        }
    }

    @Override
    public void clearCache() {
        cachedValue = null;
    }        
}
