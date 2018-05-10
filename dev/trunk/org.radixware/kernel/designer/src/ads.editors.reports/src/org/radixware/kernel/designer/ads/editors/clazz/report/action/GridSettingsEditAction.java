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

package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.designer.ads.editors.clazz.report.GridSettingsDialog;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class GridSettingsEditAction extends AbstractAction {
    private final AdsReportForm form;
    
    public GridSettingsEditAction(final AdsReportForm form) {
        super();
        this.form = form;
        this.putValue(AbstractAction.SMALL_ICON, RadixWareIcons.REPORT.GRID.getIcon());
        this.putValue(AbstractAction.NAME, "Grid Settings");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Grid Settings");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final GridSettingsDialog gridSettingsDialog = new GridSettingsDialog();
        gridSettingsDialog.showDialog(form);
    }
}
