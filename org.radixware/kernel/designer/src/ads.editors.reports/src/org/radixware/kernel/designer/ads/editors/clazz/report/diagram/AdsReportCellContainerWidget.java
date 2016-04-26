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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;

public class AdsReportCellContainerWidget extends AdsReportBaseContainer {

    public AdsReportCellContainerWidget(final AdsReportFormDiagram diagram, final AdsReportWidgetContainer band) {
        super(diagram, band);
    }

    @Override
    public void update() {
        //super.update();
        if (reportWidget != null) {
            final int leftPx = MmUtils.mm2px(reportWidget.getLeftMm());
            final int topPx = MmUtils.mm2px(reportWidget.getTopMm());
            setLocation(leftPx, topPx);
            final int widthPx = MmUtils.mm2px(reportWidget.getWidthMm());
            final int heightPx = MmUtils.mm2px(reportWidget.getHeightMm());
            setSize(widthPx, heightPx);
        }
        removeOldCells();
        updateCells();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1, 0, 0, 1, new float[]{1, 1}, 0));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setStroke(new BasicStroke());
        //hightlinghInsertionPlaceRect(g2);
    }

    @Override
    protected void paintChildren(final Graphics g) {
        super.paintChildren(g);
        paintEdges(g);
        final Graphics2D g2 = (Graphics2D) g;
        hightlinghInsertionPlaceRect(g2);
    }
}
