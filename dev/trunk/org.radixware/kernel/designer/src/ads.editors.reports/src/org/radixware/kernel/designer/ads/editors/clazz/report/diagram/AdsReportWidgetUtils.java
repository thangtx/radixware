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
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AdsReportWidgetUtils {
    
    @Deprecated
    public static void unselectCellsAndSubReports(final AdsReportBandWidget bandWidget) {
        //for (AdsReportSelectableWidget cellWidget : bandWidget.getCellWidgets()) {
        //    cellWidget.setSelected(false);
        //}
        unselectCells(bandWidget);
        for (AdsSubReportWidget subReportWidget : bandWidget.getSubReportWidgets()) {
            subReportWidget.setSelected(false);
        }
    }
    
    @Deprecated
    public static void unselectCells(final IReportCellContainer bandWidget) {
        for (AdsReportSelectableWidget cellWidget : bandWidget.getCellWidgets()) {
            cellWidget.setSelected(false);
        }
    }
    
    @Deprecated
    public static void selectForm(final AdsReportFormDiagram formDiagram) {
        for (AdsReportBandWidget bandWidget : formDiagram.getBandWidgets()) {
            bandWidget.setSelected(false);
            unselectCellsAndSubReports(bandWidget);
        }
    }

    @Deprecated
    public static void selectBand(final AdsReportBandWidget bandWidget) {
        final AdsReportFormDiagram formDiagram = bandWidget.getOwnerFormDiagram();

        for (AdsReportBandWidget sibling : formDiagram.getBandWidgets()) {
            sibling.setSelected(sibling.equals(bandWidget));
            unselectCellsAndSubReports(sibling);
        }
    }

    @Deprecated
    public static void selectCell(final AdsReportSelectableWidget cellWidget) {
        setSelectedCell(cellWidget, true);
    }
    
    @Deprecated
    public static void setSelectedCell(final AdsReportSelectableWidget cellWidget, boolean selected){
//        if (selected){
//            final AdsReportBandWidget bandWidget = cellWidget.getOwnerBandWidget();
////            selectBand(bandWidget);
//        }
        cellWidget.setSelected(selected);
    }

    @Deprecated
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
    
    public static JPopupMenu getPopupMenu(AbstractAdsReportWidget widget){
        return getPopupMenu(widget, 0, 0);
    }
    
    public static JPopupMenu getPopupMenu(AbstractAdsReportWidget widget, int x, int y){
        if (widget instanceof AdsReportFormDiagram || widget instanceof AdsSubReportWidget) {
            return DialogUtils.createPopupMenu(widget);
        }
        
        if (widget instanceof AdsReportBandWidget) {
            AdsReportBandWidget bandWidget = (AdsReportBandWidget) widget;
            final JPopupMenu popupMenu = DialogUtils.createPopupMenu(bandWidget);

            final AdsReportBand band = (AdsReportBand) bandWidget.getReportWidgetContainer();

            popupMenu.addSeparator();
            for (EReportCellType cellType : EReportCellType.values()) {
                popupMenu.add(new BandMouseListener.AddCellAction(bandWidget, band, cellType, x, y));
            }
            popupMenu.addSeparator();
            popupMenu.add(new BandMouseListener.AddSubReportAction(band.getPreReports(), "Pre"));
            popupMenu.add(new BandMouseListener.AddSubReportAction(band.getPostReports(), "Post"));
            return popupMenu;
        }
        
        if (widget instanceof AdsReportSelectableWidget) {
            final AdsReportSelectableWidget cellWidget = (AdsReportSelectableWidget) widget;
            final JPopupMenu popupMenu = DialogUtils.createPopupMenu(cellWidget);
            if (cellWidget.getCell() != null && cellWidget.getCell().isReportContainer()) {
                popupMenu.addSeparator();
                for (EReportCellType cellType : EReportCellType.values()) {
                    if (cellWidget.getOwnerBandWidget().getOwnerFormDiagram().getMode() == AdsReportForm.Mode.TEXT) {
                        switch (cellType) {
                            case IMAGE:
                            case DB_IMAGE:
                            case CHART:
                                continue;
                        }
                    }
                    popupMenu.add(new BandMouseListener.AddCellAction(cellWidget.getOwnerBandWidget(), (IReportWidgetContainer) cellWidget.getCell(), cellType, x, y));
                }
            }
            return popupMenu;
        }
        
        return null;
    }
}
