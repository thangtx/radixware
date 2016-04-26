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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * Panel to use in visual editor with custom creation code.
 */
public class AdvancePanel extends JPanel {

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr != null) {
            super.setLayout(mgr);
        } else {
            setNullLayout();
        }
    }

    protected void setNullLayout() {
    }
}
