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


public class ArrangeByHorizontalAction extends AbstractAction {

    private final AdsReportFormDiagram diagram;

    public ArrangeByHorizontalAction(final AdsReportFormDiagram diagram) {
        super("Arrange by Horizontal", RadixWareIcons.ARRANGE.BY_HORIZONTAL.getIcon());
        this.diagram = diagram;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        double minLeft = 1e100;
        double maxRight = -1e100;
        double sum = 0;
        int moo = 0;
        final List<AdsReportCell> cells = new ArrayList<>();
        for (RadixObject obj : diagram.getSelectedObjects()) {
            if (obj instanceof AdsReportCell) {
                final AdsReportCell cell = (AdsReportCell)obj;
                cells.add(cell);
                minLeft = Math.min(minLeft, cell.getLeftMm());
                maxRight = Math.max(maxRight, cell.getLeftMm() + cell.getWidthMm());
                sum += cell.getWidthMm();
                ++moo;
            }
        }
        double dx;
        if (moo > 1) {
            dx = ((maxRight - minLeft) - sum) / (moo - 1);
        } else {
            dx = 0;
        }
        final AdsReportCell[] arrCells = cells.toArray(new AdsReportCell[moo]);
        if (dx >= 0) {
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
        } else {
            Arrays.sort(arrCells, new Comparator<AdsReportCell>() {

                @Override
                public int compare(final AdsReportCell o1,final AdsReportCell o2) {
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
        }
        for (AdsReportCell cell : arrCells) {
            cell.setLeftMm(minLeft);
            minLeft += (cell.getWidthMm() + dx) > 0 ? (cell.getWidthMm() + dx) : 0;
        }
    }

}
