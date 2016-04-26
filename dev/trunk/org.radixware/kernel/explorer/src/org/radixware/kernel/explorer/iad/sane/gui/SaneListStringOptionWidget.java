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
import com.tuneology.sane.StringOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


final class SaneListStringOptionWidget extends AbstractSaneListOptionWidget<StringOption, String>{
    
    public SaneListStringOptionWidget(final SaneDevice device, final StringOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);
    }

    @Override
    protected String readValue(){
        return device.getOptionStringValue(getOption());
    }

    @Override
    protected boolean writeValue(final String value){
        return device.setOptionStringValue(getOption(), value);
    }

    @Override
    protected String val2Str(final String value) {
        return value;
    }        
}
