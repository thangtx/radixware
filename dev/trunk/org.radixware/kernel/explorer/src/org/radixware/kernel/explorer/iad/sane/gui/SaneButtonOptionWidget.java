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
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.ButtonOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


final class SaneButtonOptionWidget extends SaneOptionWidget<ButtonOption, Object>{
    
    private final QPushButton button;
    
    public SaneButtonOptionWidget(final SaneDevice device, final ButtonOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);
        setLabelText("");
        button = new QPushButton(localizer.getLocalizedString(option.getTitle()), parentWidget);
        button.clicked.connect(this,"writeValue()");
    }

    @Override
    public void putIntoLayout(final QGridLayout layout) {
        super.putIntoLayout(layout);
        final int row = layout.rowCount()-1;
        layout.addWidget(button, row, 1);
        layout.addWidget(new QWidget(this), row, 2);
   }    

    @Override
    protected void refreshWidget() {
    }

    @Override
    public void rereadValue() {
    }

    @SuppressWarnings("unused")
    private void writeValue(){
        device.pressButton(getOption());
    }
}
