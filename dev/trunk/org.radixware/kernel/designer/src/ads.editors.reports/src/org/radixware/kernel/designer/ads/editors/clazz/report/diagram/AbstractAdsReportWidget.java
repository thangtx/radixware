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

import java.awt.*;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

public abstract class AbstractAdsReportWidget extends JComponent {

    private final RadixObject radixObject;
    private final AdsReportFormDiagram diagram;

    public AbstractAdsReportWidget(final AdsReportFormDiagram diagram, final RadixObject radixObject) {
        super();
        this.diagram = diagram;
        this.radixObject = radixObject;
    }

    public void edit() {
        EditorsManager.getDefault().open(radixObject);
        //update();  // RADIX-3370
        //repaint(); // --- // ---
    }

    public AdsReportFormDiagram getDiagram() {
        return diagram;
    }

    public AdsReportForm.Mode getDiagramMode() {
        return getDiagram().getMode();
    }

    protected abstract void update();

    protected void fireSelectionChanged() {
        for (Container container = this.getParent(); container != null; container = container.getParent()) {
            if (container instanceof AdsReportFormDiagram) {
                final AdsReportFormDiagram formDiagram = (AdsReportFormDiagram) container;
                formDiagram.fireSelectionChanged();
            }
        }
    }
    private static final Stroke DOTTED_STROKE = new BasicStroke(1, 0, 0, 1, new float[]{2, 2}, 0);
    private static final Stroke HASHED_STROKE = new BasicStroke(1, 0, 0, 1, new float[]{7, 2}, 0);
    private static final Stroke SOLID_STROKE = new BasicStroke();

    public static void paintBorder(final AdsReportAbstractAppearance.Border border, final Graphics g, final int width, final int height) {
        if (!border.isDisplayed()) {
            return;
        }

        g.setColor(border.getColor());

        final Graphics2D g2 = (Graphics2D) g;
        switch (border.getStyle()) {
            case DASHED:
                g2.setStroke(HASHED_STROKE);
                break;
            case DOTTED:
                g2.setStroke(DOTTED_STROKE);
                break;
            case SOLID:
                g2.setStroke(SOLID_STROKE);
                break;
            default:
                break;
        }

        if (border.isOnTop()) {
            g.drawLine(0, 0, width, 0);
        }

        if (border.isOnRight()) {
            g.drawLine(width - 1, 0, width - 1, height);
        }

        if (border.isOnBottom()) {
            g.drawLine(0, height - 1, width, height - 1);
        }

        if (border.isOnLeft()) {
            g.drawLine(0, 0, 0, height);
        }

        g2.setStroke(SOLID_STROKE);
    }
}
