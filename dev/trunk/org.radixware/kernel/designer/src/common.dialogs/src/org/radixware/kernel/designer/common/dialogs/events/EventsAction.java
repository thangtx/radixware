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

package org.radixware.kernel.designer.common.dialogs.events;

import javax.swing.Icon;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;

/**
 * Action - view events window.
 */
public class EventsAction extends CallableSystemAction {

    final TraceTableModel traceTableModel;

    public EventsAction() {
        this.traceTableModel = TraceTable.getTraceTableModel();

        traceTableModel.addMaxSeverityChangeListener(new TraceTableModel.MaxSeverityChangeListener() {

            @Override
            public void maxSeverityChanged() {
                updateIcon();
            }
        });

        updateIcon();
    }

    // overridded in toolbar action
    protected Icon getIcon(RadixIcon radixIcon) {
        return radixIcon.getIcon();
    }

    private synchronized void updateIcon() {
        final EEventSeverity maxSeverity = traceTableModel.getMaxSeverity();
        final RadixIcon radixIcon = RadixWareIcons.EVENT_LOG.getForSeverity(maxSeverity);
        final Icon icon = getIcon(radixIcon);
        this.setIcon(icon);
        this.firePropertyChange("iconBase", "old", "new"); // hack to update icon on toolbar
    }

    @Override
    public void performAction() {
        EventsTopComponent.findInstance().open();
        EventsTopComponent.findInstance().requestActive();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(EventsAction.class, "CTL_EventsAction");
    }

//    @Override
//    protected String iconResource() {
//        return "stub"; // hack to use SVG icon
//    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
