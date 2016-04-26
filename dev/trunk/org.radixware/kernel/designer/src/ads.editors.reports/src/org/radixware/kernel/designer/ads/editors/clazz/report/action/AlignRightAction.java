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
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class AlignRightAction extends AbstractAction {

    private final AdsReportFormDiagram diagram;

    public AlignRightAction(final AdsReportFormDiagram diagram) {
        super("Align Right", RadixWareIcons.ALIGN.DIAGRAM.RIGHT.getIcon());
        this.diagram = diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        double maxRight = -1e100;
        final List<AdsReportCell> cells = new ArrayList<>();
        for (RadixObject obj : diagram.getSelectedObjects()) {
            if (obj instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell)obj;
                cells.add(cell);
                maxRight = Math.max(maxRight, cell.getLeftMm() + cell.getWidthMm());
            }
        }
        for (AdsReportCell cell : cells) {
            cell.setLeftMm(maxRight - cell.getWidthMm());
        }
    }

}
