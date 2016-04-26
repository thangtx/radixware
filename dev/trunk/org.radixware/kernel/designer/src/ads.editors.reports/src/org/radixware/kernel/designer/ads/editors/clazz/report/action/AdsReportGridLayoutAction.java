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
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;


public class AdsReportGridLayoutAction extends AdsReportLayoutAction {

    public AdsReportGridLayoutAction(final AdsReportFormDiagram diagram) {
        super(diagram);
        this.putValue(AbstractAction.SMALL_ICON, AdsDefinitionIcon.WIDGETS.GRID_LAYOUT.getIcon());
        this.putValue(AbstractAction.NAME, "GridLayout");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Grid layout");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        super.actionPerformed(e);
        if (widgetContainer != null) {
            if (widgetContainer.getReportWidgetContainer().getLayout() == EReportLayout.GRID) {
                widgetContainer.getReportWidgetContainer().setLayout(EReportLayout.FREE);
            } else {
                widgetContainer.getReportWidgetContainer().setLayout(EReportLayout.GRID);
            }
            widgetContainer.updateLayout();
        }
    }
}
