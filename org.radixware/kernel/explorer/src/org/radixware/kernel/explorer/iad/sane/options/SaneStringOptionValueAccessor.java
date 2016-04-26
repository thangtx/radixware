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

import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneException;
import com.tuneology.sane.StringOption;
import org.radixware.kernel.common.client.IClientEnvironment;


final class SaneStringOptionValueAccessor extends SaneOptionValueAccessor{
    
    private String cachedValue;
    private final StringOption option;
    
    public SaneStringOptionValueAccessor(StringOption optionDescriptor, IClientEnvironment env) {
        super(optionDescriptor, env);
        option = optionDescriptor;        
    }

    @Override
    public Integer getIntValue() {
        final String message = "Unable to read integer value of string option \'%1$s\'";
        throw new UnsupportedOperationException(String.format(message, option.getName()));
    }

    @Override
    public int setIntValue(int value) {
        return setStringValue(String.valueOf(value));
    }
    
    @Override
    public int setDoubleValue(double value) {
        return setStringValue(String.valueOf(value));
    }

    @Override
    public Double getDoubleValue() {
        final String message = "Unable to read doble value of string option \'%1$s\'";
        throw new UnsupportedOperationException(String.format(message, option.getName()));        
    }    

    @Override
    public String getStringValue() {
        if (cachedValue==null){
            final String value;
            try{
                value = option.getValue();
            }catch(SaneException exception){
                traceReadValueException(exception);
                return null;
            }        
            traceReadValue(value);
            cachedValue = value;
        }
        return cachedValue;
    }

    @Override
    public int setStringValue(final String value) {
        if (cachedValue==null || !cachedValue.equals(value)){
            final int result;
            try{
                result = option.setValue(value);
            }catch(SaneException exception){
                traceWriteValueException(exception, value);
                return -1;
            }
            traceWriteValue(value, result);
            notifyValueChanged();
            if ((result & Sane.INFO_INEXACT)==0){
                cachedValue = value;
            }
            return result;
        }else{
            return 0;
        }        
    }
    
    @Override
    public void clearCache() {
        cachedValue = null;
    }
}
