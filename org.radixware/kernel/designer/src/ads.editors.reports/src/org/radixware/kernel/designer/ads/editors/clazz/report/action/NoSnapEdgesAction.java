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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;


public class NoSnapEdgesAction  extends AbstractAction {

    private final AdsReportFormDiagram diagram;

    public NoSnapEdgesAction(final AdsReportFormDiagram diagram) {
        super("Do not Snap Edges",RadixWareIcons.EDIT.NO_ICON.getIcon());
        this.diagram = diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        for (RadixObject obj : diagram.getSelectedObjects()) {
            if (obj instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell)obj;
                cell.setSnapTopEdge(false);
                cell.setSnapBottomEdge(false);
            }
        }
    }

}