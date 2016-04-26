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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.StringOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


final class SaneStringOptionWidget extends SaneOptionWidget<StringOption, String>{
    
    private final QLineEdit leValue  = new QLineEdit(this);
    private final QPushButton pbSet = new QPushButton(this);
    private final QPushButton pbReset = new QPushButton(this);
    
    public SaneStringOptionWidget(final SaneDevice device, final StringOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device, option,environment,parentWidget);
        pbSet.setText(environment.getMessageProvider().translate("IAD", "Set"));
        pbReset.setText(environment.getMessageProvider().translate("IAD", "Reset"));
        refreshWidget();
        pbSet.clicked.connect(this,"onSetButtonClicked()");
        pbReset.clicked.connect(this,"onResetButtonClicked()");
        device.addChangeOptionValueListener(option, new SaneDevice.IChangeOptionValueListener() {
            @Override
            public void optionValueChanged() {
                rereadValue();
            }
        });
    }

    @Override
    public void putIntoLayout(QGridLayout layout) {
        super.putIntoLayout(layout);
        final int row = layout.rowCount();
        layout.addWidget(leValue, row, 0, 1, 2);
        layout.addWidget(pbReset, row, 2);
        layout.addWidget(pbSet, row, 3);        
    }    

    @Override
    protected void refreshWidget() {
        if (!isHidden()){
            rereadValue();
        }
    }        

    @Override
    public void rereadValue() {
        final String value = device.getOptionStringValue(getOption());
        if (value!=null){
            leValue.setText(value);
        }
    }
        
    @SuppressWarnings("unused")
    private void onSetButtonClicked(){
        if (!device.setOptionStringValue(getOption(), leValue.text())){
            rereadValue();
        }
    }
    
    @SuppressWarnings("unused")
    private void onResetButtonClicked(){
        rereadValue();
    }
}
