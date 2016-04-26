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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;


abstract class WidgetMouseListener extends MouseInputAdapter {

    protected abstract void select(boolean expand);

    protected abstract void edit();

    @Override
    public void mouseClicked(final MouseEvent e) {
        final boolean ctrl = (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
        final boolean shift = (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;

        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            select(shift || ctrl);
        }

        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && !ctrl && !shift) {
            edit();
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        checkPopup(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        checkPopup(e);
    }

    private void checkPopup(final MouseEvent e) {
        if (e.isPopupTrigger()) {
            final int x = e.getX();
            final int y = e.getY();
            popup(e.getComponent(), x, y);
        }
    }

    protected abstract void popup(Component component, int x, int y);
}
