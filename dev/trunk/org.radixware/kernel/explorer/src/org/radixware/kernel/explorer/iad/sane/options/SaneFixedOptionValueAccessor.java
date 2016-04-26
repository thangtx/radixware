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

import com.tuneology.sane.FixedOption;
import com.tuneology.sane.SaneException;
import org.radixware.kernel.common.client.IClientEnvironment;

final class SaneFixedOptionValueAccessor extends AbstractSaneIntOptionValueAccessor<FixedOption>{
        
    private final FixedOption option;

    public SaneFixedOptionValueAccessor(FixedOption optionDescriptor, IClientEnvironment env) {
        super(optionDescriptor, env);
        option = optionDescriptor;
    }        

    @Override
    protected int[] invokeGetValue() throws SaneException {
        return option.getValues();
    }

    @Override
    protected int invokeSetValue(int[] value) throws SaneException {
        return option.setValues(value);
    }        

    @Override
    public Double getDoubleValue() {
        final Integer value = getIntValue();
        return value==null ? null : Double.valueOf(SaneOptionValueUtils.fixedValue2Double(value.intValue()));
    }

    @Override
    public int setDoubleValue(double value) {
        final int fixedValue = SaneOptionValueUtils.double2fixedValue(value);
        return setIntValue(fixedValue);
    }
}
