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

package org.radixware.kernel.explorer.iad.sane.gui;

import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.IntegerOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


final class SaneListIntOptionWidget extends AbstractSaneListOptionWidget<IntegerOption,Integer>{
    
    public SaneListIntOptionWidget(final SaneDevice device,final IntegerOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);
    }
    
    @Override
    protected Integer readValue(){
        return device.getOptionIntegerValue(getOption());
    }
    
    @Override
    protected boolean writeValue(final Integer value){
        return device.setOptionIntegerValue(getOption(), value.intValue());
    }

    @Override
    protected String val2Str(final Integer value) {
        return value.toString();
    }    
}
