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

package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JPanel;


public class BuildingPanel<TComponentKey> extends JPanel {

    protected final ComponentSequence<TComponentKey> componentSequence;

    public BuildingPanel() {
        super();

        componentSequence = new ComponentSequence<>(this);
    }

    public BuildingPanel(LayoutManager layoutManager) {
        this();

        setLayout(layoutManager);
    }

    public final ComponentSequence<TComponentKey> getComponentSequence() {
        return componentSequence;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (final Component item : getComponents()) {
            item.setEnabled(enabled);
        }
    }
}
