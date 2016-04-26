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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRanges;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;


public abstract class StateAbstractValueRangeSetupDialog extends StateAbstractDialog{

    protected ValueRanges valueRanges;

    public StateAbstractValueRangeSetupDialog(ValueRangePanel innerPanel, ValueRanges valueRanges, String title) {
        super(innerPanel, title);
        this.valueRanges = valueRanges;

        getDialog().addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                final ValueRangePanel valueRangePanel =  (ValueRangePanel)getComponent();
                valueRangePanel.requestFocusInWindow();
                valueRangePanel.check();
            }
        });
    }
}
