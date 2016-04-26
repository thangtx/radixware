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

import java.awt.Font;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;


public class AdsReportWidgetUtils {
    
    public static void unselectCellsAndSubReports(final AdsReportBandWidget bandWidget) {
        //for (AdsReportSelectableWidget cellWidget : bandWidget.getCellWidgets()) {
        //    cellWidget.setSelected(false);
        //}
        unselectCells(bandWidget);
        for (AdsSubReportWidget subReportWidget : bandWidget.getSubReportWidgets()) {
            subReportWidget.setSelected(false);
        }
    }
    
    public static void unselectCells(final IReportCellContainer bandWidget) {
        for (AdsReportSelectableWidget cellWidget : bandWidget.getCellWidgets()) {
            cellWidget.setSelected(false);
        }
    }

    public static void selectForm(final AdsReportFormDiagram formDiagram) {
        for (AdsReportBandWidget bandWidget : formDiagram.getBandWidgets()) {
            bandWidget.setSelected(false);
            unselectCellsAndSubReports(bandWidget);
        }
    }

    public static void selectBand(final AdsReportBandWidget bandWidget) {
        final AdsReportFormDiagram formDiagram = bandWidget.getOwnerFormDiagram();

        for (AdsReportBandWidget sibling : formDiagram.getBandWidgets()) {
            sibling.setSelected(sibling.equals(bandWidget));
            unselectCellsAndSubReports(sibling);
        }
    }

    public static void selectCell(final AdsReportSelectableWidget cellWidget) {
        final AdsReportBandWidget bandWidget = cellWidget.getOwnerBandWidget();
        selectBand(bandWidget);
        cellWidget.setSelected(true);
    }

    public static void selectSubReport(final AdsSubReportWidget subReportWidget) {
        final AdsReportBandWidget bandWidget = subReportWidget.getOwnerBandWidget();
        selectBand(bandWidget);
        subReportWidget.setSelected(true);
    }

    public static Font reportFont2JavaFont(final AdsReportAbstractAppearance.Font reportFont,final JComponent context) {        
        final String name = reportFont.getName();
        final int style = (reportFont.isBold() ? Font.BOLD : 0) + (reportFont.isItalic() ? Font.ITALIC : 0);
        final int sizePx = MmUtils.mm2px(reportFont.getSizeMm());
        final Font result = new Font(name, style, sizePx);
        return result;
    }
}
