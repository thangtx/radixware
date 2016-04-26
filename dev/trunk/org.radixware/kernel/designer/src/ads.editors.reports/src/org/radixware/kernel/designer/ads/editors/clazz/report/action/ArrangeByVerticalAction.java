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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ArrangeByVerticalAction extends AbstractAction {

    private final AdsReportFormDiagram diagram;

    public ArrangeByVerticalAction(final AdsReportFormDiagram diagram) {
        super("Arrange by Vertical", RadixWareIcons.ARRANGE.BY_VERTICAL.getIcon());
        this.diagram = diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        double minTop = 1e100;
        double maxBottom = -1e100;
        double sum = 0;
        int moo = 0;
        final List<AdsReportCell> cells = new ArrayList<AdsReportCell>();
        for (RadixObject obj : diagram.getSelectedObjects()) {
            if (obj instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell)obj;
                cells.add(cell);
                minTop = Math.min(minTop, cell.getTopMm());
                maxBottom = Math.max(maxBottom, cell.getTopMm() + cell.getHeightMm());
                sum += cell.getHeightMm();
                ++moo;
            }
        }
        double dy;
        if (moo > 1) {
            dy = ((maxBottom - minTop) - sum) / (moo - 1);
        } else {
            dy = 0;
        }
        final AdsReportCell[] arrCells = cells.toArray(new AdsReportCell[moo]);
        if (dy >= 0) {
            Arrays.sort(arrCells, new Comparator<AdsReportCell>() {

                @Override
                public int compare(final AdsReportCell o1, final AdsReportCell o2) {
                    if (o1.getTopMm() < o2.getTopMm()) {
                        return -1;
                    } else if (o1.getTopMm() == o2.getTopMm()) {
                        if (o1.getLeftMm() < o2.getLeftMm()) {
                            return -1;
                        } else if (o1.getLeftMm() == o2.getLeftMm()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        return 1;
                    }
                }
            });
        } else {
            Arrays.sort(arrCells, new Comparator<AdsReportCell>() {

                @Override
                public int compare(final AdsReportCell o1,final AdsReportCell o2) {
                    if (o1.getLeftMm() < o2.getLeftMm()) {
                        return -1;
                    } else if (o1.getLeftMm() == o2.getLeftMm()) {
                        if (o1.getTopMm() < o2.getTopMm()) {
                            return -1;
                        } else if (o1.getTopMm() == o2.getTopMm()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        return 1;
                    }
                }
            });
        }
        for (AdsReportCell cell : cells) {
            cell.setTopMm(minTop);
            minTop += (cell.getHeightMm() + dy) > 0 ? (cell.getHeightMm() + dy) : 0;
        }
    }

}
