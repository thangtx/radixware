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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.awt.Color;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellVAlign;

public class AdsReportDefaultStyle {
    public static final double DEFAULT_FORM_MARGIN = 9.87;
    
    public static final double DEFAULT_PAGE_HEADER_SIZE = 2.4;
    public static final double DEFAULT_PAGE_HEADER_CELL_BOTTOM_MARGIN = 3.5;
    public static final double DEFAULT_PAGE_HEADER_CELL_LINE_SPACING = 0.7;
    
    public static final double DEFAULT_GROUP_FONT_SIZE = 3.51;
    public static final double DEFAULT_GROUP_CELL_TOP_MARGIN = 2.5;
    public static final double DEFAULT_GROUP_CELL_LEFT_MARGIN = 3;
    public static final double DEFAULT_GROUP_CELL_BOTTOM_MARGIN = 0.7;
    
    public static final double DEFAULT_COLUMN_HEADER_CELL_TOP_MARGIN = 2.1;
    public static final double DEFAULT_COLUMN_HEADER_CELL_LEFT_MARGIN = 1.4;
    public static final double DEFAULT_COLUMN_HEADER_CELL_BOTTOM_MARGIN = 2.4;
    public static final double DEFAULT_COLUMN_HEADER_CELL_RIGHT_MARGIN = 1.4;
    public static final double DEFAULT_COLUMN_HEADER_CELL_LINE_SPACING = 1.4;
    
    public static final double DEFAULT_DETAIL_CELL_TOP_MARGIN = 2.5;
    public static final double DEFAULT_DETAIL_CELL_BOTTOM_MARGIN = 2.5;
    public static final double DEFAULT_DETAIL_CELL_LINE_SPACING = 1.8;
    
    public static final double DEFAULT_SUMMARY_FONT_SIZE = 3.51;

    public static void setDefaultWidgetStyle(AdsReportWidget widget) {
        if (widget instanceof AdsReportCell) {
            setDefaultCellStyle((AdsReportCell) widget);
        }
    }
    
    public static void setDefaultCellStyle(AdsReportCell cell) {
        cell.getBorder().setColor(Color.WHITE);
        AdsReportBand band = cell.getOwnerBand();
        if (band == null || band.getOwnerBands() == null) {
            return;
        }
        switch (band.getType()) {
            case PAGE_HEADER:
                cell.getMarginMm().setBottomMm(DEFAULT_PAGE_HEADER_CELL_BOTTOM_MARGIN);
                cell.setLineSpacingMm(DEFAULT_PAGE_HEADER_CELL_LINE_SPACING);
                break;
            case  GROUP_HEADER: case GROUP_FOOTER:
                cell.getMarginMm().setTopMm(DEFAULT_GROUP_CELL_TOP_MARGIN);
                cell.getMarginMm().setLeftMm(DEFAULT_GROUP_CELL_LEFT_MARGIN);
                cell.getMarginMm().setBottomMm(DEFAULT_GROUP_CELL_BOTTOM_MARGIN);
                break;
            case COLUMN_HEADER:
                cell.setBgColorInherited(false);
                cell.setBgColor(Color.decode("#D3D4D5"));
                cell.getMarginMm().setTopMm(DEFAULT_COLUMN_HEADER_CELL_TOP_MARGIN);
                cell.getMarginMm().setLeftMm(DEFAULT_COLUMN_HEADER_CELL_LEFT_MARGIN);
                cell.getMarginMm().setBottomMm(DEFAULT_COLUMN_HEADER_CELL_BOTTOM_MARGIN);
                cell.getMarginMm().setRightMm(DEFAULT_COLUMN_HEADER_CELL_RIGHT_MARGIN);
                cell.setLineSpacingMm(DEFAULT_COLUMN_HEADER_CELL_LINE_SPACING);
                cell.setHAlign(EReportCellHAlign.CENTER);
                cell.setVAlign(EReportCellVAlign.MIDDLE);
                break;
            case DETAIL:
                cell.getMarginMm().setTopMm(DEFAULT_DETAIL_CELL_TOP_MARGIN);
                cell.getMarginMm().setBottomMm(DEFAULT_DETAIL_CELL_BOTTOM_MARGIN);
                cell.setLineSpacingMm(DEFAULT_DETAIL_CELL_LINE_SPACING);
                break;
        }
    }
    
    public static void setDefaultBandStyle(AdsReportBand band) {
        band.getBorder().setColor(Color.decode("#F2F2F2"));
        band.setAutoHeight(true);
        switch (band.getType()) {
            case PAGE_HEADER : case PAGE_FOOTER:
                band.getFont().setSizeMm(DEFAULT_PAGE_HEADER_SIZE);
                break;
            case GROUP_HEADER: case GROUP_FOOTER:
                band.getFont().setSizeMm(DEFAULT_GROUP_FONT_SIZE);
                break;
            case SUMMARY:
                band.getFont().setSizeMm(DEFAULT_SUMMARY_FONT_SIZE);
                break;
            case COLUMN_HEADER:
                band.getFont().setBold(true);
                break;
                
        }
    }
    
    public static void setDefaultFormStyle(AdsReportForm form) {
        form.getMargin().setMargin(DEFAULT_FORM_MARGIN, false);
        form.setRepeatGroupHeadersOnNewPage(true);
    }
}
