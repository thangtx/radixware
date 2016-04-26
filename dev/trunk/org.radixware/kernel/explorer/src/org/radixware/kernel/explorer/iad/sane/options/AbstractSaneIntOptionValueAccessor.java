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

import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneException;
import org.radixware.kernel.common.client.IClientEnvironment;


abstract class AbstractSaneIntOptionValueAccessor<T extends OptionDescriptor> extends SaneOptionValueAccessor{
    
    private Integer cachedValue;
    
    public AbstractSaneIntOptionValueAccessor(OptionDescriptor optionDescriptor, IClientEnvironment env) {
        super(optionDescriptor, env);
    }
    
    protected abstract int[] invokeGetValue() throws SaneException;
    
    protected abstract int invokeSetValue(int[] value) throws SaneException;

    @Override
    public Integer getIntValue() {
        if (cachedValue==null){
            final int[] saneValues;
            try{
                saneValues = invokeGetValue();
            }catch(SaneException exception){
                traceReadValueException(exception);
                return null;
            }        
            traceReadValue(saneValues);
            if (saneValues!=null && saneValues.length>0){
                cachedValue = Integer.valueOf(saneValues[0]);
            }
        }
        return cachedValue;
    }

    @Override
    public int setIntValue(int value) {
        if (cachedValue==null || cachedValue.intValue()!=value){
            final int result;
            try{
                result = invokeSetValue(new int[]{value});            
            }catch(SaneException exception){
                traceWriteValueException(exception, Integer.valueOf(value));
                return -1;
            }
            traceWriteValue(value, result);
            notifyValueChanged();
            if ((result & Sane.INFO_INEXACT)==0){
                cachedValue = Integer.valueOf(value);
            }
            return result;
        }else{
            return 0;
        }
    }

    @Override
    public String getStringValue() {
        final Integer value = getIntValue();
        return value==null ? null : value.toString();
    }

    @Override
    public int setStringValue(final String value) {
        return setIntValue(Integer.parseInt(value));
    }

    @Override
    public void clearCache() {
        cachedValue = null;
    }        
}
