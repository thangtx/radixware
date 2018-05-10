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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.UserReport;

public class AdsSubReportWidget extends AdsReportAbstractSelectableWidget {

    public static final double SUB_REPORT_WIDGET_HEIGHT_MM = 10.0;
    public static final int SUB_REPORT_WIDGET_HEIGHT_ROWS = 1;

    private static final Color SUB_REPORT_SELECTED_COLOR = new Color(176, 214, 225);
    private static final Color SUB_REPORT_GRID_COLOR = new Color(230, 230, 230);
    private final AdsSubReport subReport;
    //private final SubReportMouseListener mouseListener;

    public AdsSubReportWidget(final AdsReportFormDiagram diagram, final AdsSubReport subReport) {
        super(diagram, subReport);
        this.subReport = subReport;
        setOpaque(true);
        final SubReportMouseListener mouseListener = new SubReportMouseListener(this);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }
    
    protected void paintBackground(final Graphics g) {
        final int width = getWidth();
        final int height = getHeight();

        g.setColor(isSelected() ? SUB_REPORT_SELECTED_COLOR : Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(SUB_REPORT_GRID_COLOR);
        for (int x = -height; x < width + height; x += 7) {
            g.drawLine(x, 0, x + height, height);
            g.drawLine(x, 0, x - height, height);
        }

        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, height);
        g.drawLine(width - 1, 0, width - 1, height);
        final AdsReportBand ownerBand = subReport.getOwnerBand();
        if (ownerBand != null) {
            final boolean isPreReport = ownerBand.getPreReports().contains(subReport);
            if (isPreReport) {
                g.drawLine(0, height - 1, width, height - 1);
            } else {
                g.drawLine(0, 0, width, 0);
            }
        }
    }

    protected void paintName(final Graphics g) {
        final Font font = getFont().deriveFont((float) MmUtils.mm2px(SUB_REPORT_WIDGET_HEIGHT_MM / 2));
        g.setFont(font);
        final FontMetrics fontMetrics = getFontMetrics(font);

        String name = "UNDEFINED";
        AdsReportFormDiagram diagram = getDiagram();
        if (diagram != null) {
            AdsReportClassDef report;
            AdsReportForm form = diagram.getForm();
            final EDefinitionIdPrefix defPrefix = subReport.getReportId() != null ? subReport.getReportId().getPrefix() : null;
            if ((defPrefix == null || defPrefix == EDefinitionIdPrefix.USER_DEFINED_REPORT) &&
                    form != null && form.getModule() != null && ((AdsModule) form.getModule()).isUserModule()) {
                final UserReport rep = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(subReport.getReportId());
                final UserReport.ReportVersion curVersion = rep != null ? rep.getVersions().getCurrent() : null;
                report = curVersion != null ? curVersion.findReportDefinition() : null;
            } else {
                report = subReport.findReport();
            } 
            name = report == null ? ("UNDEFINED (#" + subReport.getReportId() + ")") : report.getQualifiedName();
        }

        final int textWidth = fontMetrics.stringWidth(name);
        final int left = (getWidth() - textWidth) / 2;
        final int baseLine = fontMetrics.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(name, left, baseLine);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        paintName(g);
    }

    public AdsReportBandWidget getOwnerBandWidget() {
        return (AdsReportBandWidget) getParent();
    }

    public AdsSubReport getSubReport() {
        return subReport;
    }

    @Override
    protected void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onSelected(boolean selected) {
        repaint();
    }

    @Override
    public RadixObject getRadixObject() {
        return getSubReport();
    }

}
