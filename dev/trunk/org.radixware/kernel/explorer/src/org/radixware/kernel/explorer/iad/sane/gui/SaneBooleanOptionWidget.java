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

import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.BooleanOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


final class SaneBooleanOptionWidget extends SaneOptionWidget<BooleanOption,Boolean>{
        
    private final QCheckBox checkBox = new QCheckBox(this);
    
    public SaneBooleanOptionWidget(final SaneDevice device, final BooleanOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);        
        setLabelText(localizer.getLocalizedString(option.getTitle()));
        refreshWidget();
        checkBox.toggled.connect(this,"writeValue(Boolean)");
        device.addChangeOptionValueListener(option, new SaneDevice.IChangeOptionValueListener() {
            @Override
            public void optionValueChanged() {
                rereadValue();
            }
        });
    }

    @Override
    public void putIntoLayout(final QGridLayout layout) {
        super.putIntoLayout(layout);
        final int row = layout.rowCount()-1;
        layout.addWidget(checkBox, row, 1);       
    }

    @Override
    protected void setLabelText(final String text) {
        super.setLabelText("");
        checkBox.setText(text);
    }

    @Override
    protected void refreshWidget() {
        rereadValue();
    }        
    
    @SuppressWarnings("unused")
    private void writeValue(Boolean value){
        device.setOptionIntegerValue(getOption(), value==Boolean.TRUE ? 1 : 0);
    }

    @Override
    public void rereadValue() {
        if (!isHidden()){
            final Integer value = device.getOptionIntegerValue(getOption());
            if (value!=null){
                checkBox.setChecked(value.intValue()!=0);                
            }
        }
    }
}
