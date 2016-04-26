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

import java.awt.Graphics;
import java.awt.Graphics2D;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;

public class AdsReportBandSubWidget extends AdsReportBaseContainer {

    public AdsReportBandSubWidget(final AdsReportFormDiagram diagram, final AdsReportBand band) {
        super(diagram, band);
    }

    @Override
    public void update() {
        removeOldCells();
        updateCells();
    }

    @Override
    protected void paintBackground(final Graphics g) {
        //super.paintBackground(g);
        g.setColor(widgetContainer.getBgColor());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        AdsReportCellWidget.paintBorder(((AdsReportBand) widgetContainer).getBorder(), g, getWidth(), getHeight());
        final Graphics2D g2 = (Graphics2D) g;
        hightlinghInsertionPlaceRect(g2);
    }

    /*@Override
     protected void paintChildren(final Graphics g) {
     super.paintChildren(g);
     //Graphics2D g2=(Graphics2D)g;
     //hightlinghInsertionPlaceRect(g2);
     } */
}
