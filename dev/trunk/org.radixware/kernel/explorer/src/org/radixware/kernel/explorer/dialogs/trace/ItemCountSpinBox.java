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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.explorer.env.Application;


public class ItemCountSpinBox extends QSpinBox {

    public final Signal0 itemCountChanged = new Signal0();
    private final String all = Application.translate("TraceDialog", "all");
    private final ClientTracer tracer;

    @SuppressWarnings("LeakingThisInConstructor")
    public ItemCountSpinBox(final ClientTracer tracer, final QWidget parent) {
        super(parent);
        this.tracer = tracer;
        if (RunParams.isDevelopmentMode()){
            this.setRange(-1, 10000);
            setValue(-1);
        }else{
            this.setRange(0, 10000);
            setValue(500);            
        }
        this.editingFinished.connect(this, "maxItemCountChanged()");
    }

    @Override
    protected String textFromValue(final int val) {
        if (val < 0) {
            return all;
        }
        return String.valueOf(val);
    }

    @Override
    protected int valueFromText(final String str) {
        if (str == null || str.equals(all)) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @SuppressWarnings("unused")
    private void maxItemCountChanged() {
        tracer.getBuffer().setMaxSize(this.value());
        itemCountChanged.emit();
    }
}
