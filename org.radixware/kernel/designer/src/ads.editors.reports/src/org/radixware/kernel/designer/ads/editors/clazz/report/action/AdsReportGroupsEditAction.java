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
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsReportGroupsEditAction extends AbstractAction {

    private final AdsReportForm form;

    public AdsReportGroupsEditAction(final AdsReportForm form) {
        super();
        this.form = form;
        this.putValue(AbstractAction.SMALL_ICON, AdsDefinitionIcon.REPORT_GROUP.getIcon());
        this.putValue(AbstractAction.NAME, "Edit Groups");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Edit Groups");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        EditorsManager.getDefault().open(form, new OpenInfo(form.getGroups()));
    }
}
