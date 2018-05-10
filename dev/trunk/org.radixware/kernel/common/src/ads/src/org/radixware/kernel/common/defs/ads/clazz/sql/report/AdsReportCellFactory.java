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


public class AdsReportCellFactory {

    private AdsReportCellFactory() {
    }
    
    public static AdsReportTextCell newTextCell(boolean isNewStyle) {
        AdsReportTextCell cell = new AdsReportTextCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportPropertyCell newPropertyCell(boolean isNewStyle) {
        AdsReportPropertyCell cell = new AdsReportPropertyCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportImageCell newImageCell(boolean isNewStyle) {
        AdsReportImageCell cell = new AdsReportImageCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportDbImageCell newDbImageCell(boolean isNewStyle) {
        AdsReportDbImageCell cell = new AdsReportDbImageCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportSummaryCell newSummaryCell(boolean isNewStyle) {
        AdsReportSummaryCell cell = new AdsReportSummaryCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportSpecialCell newSpecialCell(boolean isNewStyle) {
        AdsReportSpecialCell cell = new AdsReportSpecialCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }

    public static AdsReportExpressionCell newExpressionCell(boolean isNewStyle) {
        AdsReportExpressionCell cell = new AdsReportExpressionCell();
        cell.setNewStyle(isNewStyle);
        return cell;
    }
    
    public static AdsReportChartCell newChartCell(boolean isNewStyle) {
        return new AdsReportChartCell();
    }

    public static AdsReportTextCell newTextCell() {
        return new AdsReportTextCell();
    }
        
    public static AdsReportPropertyCell newPropertyCell() {
        return new AdsReportPropertyCell();
    }

    public static AdsReportImageCell newImageCell() {
        return new AdsReportImageCell();
    }

    public static AdsReportDbImageCell newDbImageCell() {
        return new AdsReportDbImageCell();
    }

    public static AdsReportSummaryCell newSummaryCell() {
        return new AdsReportSummaryCell();
    }

    public static AdsReportSpecialCell newSpecialCell() {
        return new AdsReportSpecialCell();
    }

    public static AdsReportExpressionCell newExpressionCell() {
        return new AdsReportExpressionCell();
    }
    
    public static AdsReportChartCell newChartCell() {
        return new AdsReportChartCell();
    }

    protected static AdsReportCell loadFrom(org.radixware.schemas.adsdef.ReportCell xCell) {
        switch (xCell.getType()) {
            case TEXT:
                return new AdsReportTextCell(xCell);
            case PROPERTY:
                return new AdsReportPropertyCell(xCell);
            case SUMMARY:
                return new AdsReportSummaryCell(xCell);
            case SPECIAL:
                return new AdsReportSpecialCell(xCell);
            case EXPRESSION:
                return new AdsReportExpressionCell(xCell);
            case IMAGE:
                return new AdsReportImageCell(xCell);
            case DB_IMAGE:
                return new AdsReportDbImageCell(xCell);
            case CHART:
                return new AdsReportChartCell(xCell);
            default:
                throw new IllegalStateException(String.valueOf(xCell.getType()));
        }
    }
    
     protected static AdsReportCell loadFrom(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        switch (xCell.getType()) {
            case TEXT:
                return new AdsReportTextCell(xCell);
            case PROPERTY:
                return new AdsReportPropertyCell(xCell);
            case SUMMARY:
                return new AdsReportSummaryCell(xCell);
            case SPECIAL:
                return new AdsReportSpecialCell(xCell);
            case EXPRESSION:
                return new AdsReportExpressionCell(xCell);
            case IMAGE:
                return new AdsReportImageCell(xCell);
            case DB_IMAGE:
                return new AdsReportDbImageCell(xCell);
            case CHART:
                return new AdsReportChartCell(xCell);
            default:
                throw new IllegalStateException(String.valueOf(xCell.getType()));
        }
    }
}
