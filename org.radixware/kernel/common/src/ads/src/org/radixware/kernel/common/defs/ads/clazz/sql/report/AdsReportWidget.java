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

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.Utils;

public abstract class AdsReportWidget extends AdsReportAbstractAppearance implements IReportNavigatorObject<AdsReportWidget>{

    //  public GroupData positionGroup;
    //  public GroupData heightGroup;
    private static int DEFAULT_TXT_COL = 0;
    private static int DEFAULT_TXT_ROW = 0;
    private static int DEFAULT_TXT_WIDTH = 10;
    private static int DEFAULT_TXT_HEIGHT = 1;
    private double leftMm = 0.0, topMm = 0.0, widthMm = 50.0, heightMm = 12.5;
    private int textCol = DEFAULT_TXT_COL, textRow = DEFAULT_TXT_ROW, textWidth = DEFAULT_TXT_WIDTH, textHeight = DEFAULT_TXT_HEIGHT;
    private int row = -1;
    private int column = -1;
    private int rowSpan = 1, columnSpan = 1;
    protected boolean adjustHeight = false;
    protected boolean adjustWidth = false;

    public class GroupData {

        public double groupValue;
        public List<AdsReportWidget> members;

        public GroupData(double groupValue, List<AdsReportWidget> members) {
            this.groupValue = groupValue;
            this.members = members;
        }
    }

    protected AdsReportWidget() {
        super();
    }

    protected AdsReportWidget(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);
        if (xCell != null) {
            //org.radixware.schemas.adsdef.ReportWidget xWidget= xCell.getReportWidget();
            leftMm = xCell.getLeft();
            topMm = xCell.getTop();
            widthMm = xCell.getWidth();
            heightMm = xCell.getHeight();

            textCol = xCell.getLeftColumn();
            textRow = xCell.getTopRow();

            textHeight = xCell.getHeightRows();
            textWidth = xCell.getWidthColumns();

            if (xCell.isSetColumn()) {
                column = xCell.getColumn();
            }
            if (xCell.isSetRow()) {
                row = xCell.getRow();
            }
            if (xCell.isSetAdjustWidth()) {
                adjustWidth = xCell.getAdjustWidth();
            }
            if (xCell.isSetAdjustHeight()) {
                adjustHeight = xCell.getAdjustHeight();
            }
            rowSpan = xCell.getRowSpan();
            columnSpan = xCell.getColumnSpan();
        }
    }

    protected AdsReportWidget(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);
        leftMm = xCell.getLeft();
        topMm = xCell.getTop();
        widthMm = xCell.getWidth();
        heightMm = xCell.getHeight();

        textCol = xCell.getLeftColumn();
        textRow = xCell.getTopRow();

        textHeight = xCell.getHeightRows();
        textWidth = xCell.getWidthColumns();

    }

    public abstract boolean isReportContainer();
    
    @Override
    public RadixObject getParent() {
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof IReportWidgetContainer) {
                return container;
            }
        }
        return null;
    }
    

    public IReportWidgetContainer getOwnerWidget() {
        return (IReportWidgetContainer) getParent();
    }

    /**
     * @return owner band or null if cell instance is not in band.
     */
    public AdsReportBand getOwnerBand() {
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportBand) {
                return (AdsReportBand) container;
            }
        }
        return null;
    }

    /**
     * @return owner form or null if cell instance is not in form.
     */
    public AdsReportForm getOwnerForm() {
        final AdsReportBand band = getOwnerBand();
        if (band != null) {
            return band.getOwnerForm();
        } else {
            return null;
        }
    }

    /**
     * @return owner report or null if cell instance is not in report.
     */
    public AdsReportClassDef getOwnerReport() {
        final AdsReportForm form = getOwnerForm();
        if (form != null) {
            return form.getOwnerReport();
        } else {
            return null;
        }
    }

    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell) {
        if (xCell != null) {
            //   org.radixware.schemas.adsdef.ReportWidget xWidget=xCell.addNewReportWidget();
            xCell.setLeft(leftMm);
            xCell.setTop(topMm);
            xCell.setWidth(widthMm);
            xCell.setHeight(heightMm);

            xCell.setLeftColumn(textCol);
            xCell.setWidthColumns(textWidth);
            xCell.setTopRow(textRow);
            xCell.setHeightRows(textHeight);

            if (column != -1) {
                xCell.setColumn(column);
            }
            if (row != -1) {
                xCell.setRow(row);
            }
            if (adjustWidth) {
                xCell.setAdjustWidth(adjustWidth);
            }
            if (adjustHeight) {
                xCell.setAdjustHeight(adjustHeight);
            }
            if (columnSpan > 1) {
                xCell.setColumnSpan(columnSpan);
            }
            if (rowSpan > 1) {
                xCell.setRowSpan(rowSpan);
            }
        }

    }

    /**
     * Get indent from band left edge.
     *
     * @return cell left position in millimeters.
     */
    public double getLeftMm() {
        return leftMm;
    }

    public void setLeftMm(double leftMm) {
        if (!Utils.equals(this.leftMm, leftMm)) {
            this.leftMm = leftMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Get indent from band top edge.
     *
     * @return cell top position in millimeters.
     */
    public double getTopMm() {
        return topMm;
    }

    public void setTopMm(double topMm) {
        if (!Utils.equals(this.topMm, topMm)) {
            this.topMm = topMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return cell width in millimeters
     */
    public double getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(double widthMm) {
        if (!Utils.equals(this.widthMm, widthMm)) {
            this.widthMm = widthMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return cell height in millimeters
     */
    public double getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(double heightMm) {
        if (!Utils.equals(this.heightMm, heightMm)) {
            this.heightMm = heightMm;
            setEditState(EEditState.MODIFIED);
        }
    }

    // textual mode size support
    /**
     * Get indent from band left edge.
     *
     * @return cell left position in millimeters.
     */
    public int getLeftColumn() {
        return textCol;
    }

    public void setLeftColumn(int leftCol) {
        if (!Utils.equals(this.textCol, leftCol)) {
            this.textCol = leftCol;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Get indent from band top edge.
     *
     * @return cell top position in millimeters.
     */
    public int getTopRow() {
        return textRow;
    }

    public void setTopRow(int topRow) {
        if (!Utils.equals(this.textRow, topRow)) {
            this.textRow = topRow;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getHeightRows() {
        return textHeight;
    }

    public void setHeightRows(int rows) {
        if (textHeight != rows) {
            textHeight = rows;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return cell width in symbols
     */
    public int getWidthCols() {
        return textWidth;
    }

    public void setWidthCols(int widthCols) {
        if (!Utils.equals(this.textWidth, widthCols)) {
            this.textWidth = widthCols;
            setEditState(EEditState.MODIFIED);
        }
    }

    //Grid layout workaround
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRowSpan(int rowSpan) {
        if (!Utils.equals(this.rowSpan, rowSpan)) {
            this.rowSpan = rowSpan;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setColumnSpan(int columnSpan) {
        if (!Utils.equals(this.columnSpan, columnSpan)) {
            this.columnSpan = columnSpan;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isAdjustWidth() {
        return adjustWidth;
    }

    public void setAdjustWidth(boolean adjustWidth) {
        if (this.adjustWidth != adjustWidth) {
            this.adjustWidth = adjustWidth;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isAdjustHeight() {
        return adjustHeight;
    }

    public void setAdjustHeight(boolean adjustHeight) {
        if (this.adjustHeight != adjustHeight) {
            this.adjustHeight = adjustHeight;
            setEditState(EEditState.MODIFIED);
        }
    }

//    /**
//     * @return true if font inherited from band, false otherwise
//     */
    /* public boolean isFontInherited() {
     return fontInherited;
     }

     public boolean getFontInherited() { // for onAdding
     return fontInherited;
     }

     public void setFontInherited(boolean fontInherited) {
     // if (this.fontInherited != fontInherited) {
     this.fontInherited = fontInherited;
     //     setEditState(EEditState.MODIFIED);
     // }
     } */
    public void convertToTextMode() {
        if (textHeight == DEFAULT_TXT_HEIGHT
                && textWidth == DEFAULT_TXT_WIDTH
                && textCol == DEFAULT_TXT_COL
                && textRow == DEFAULT_TXT_ROW) {
            final double CONVERSION_Y = 5;
            final double CONVERSION_X = 5;
            this.textHeight = (int) Math.round(this.heightMm / CONVERSION_Y);
            this.textWidth = (int) Math.round(this.widthMm / CONVERSION_X);
            this.textCol = (int) Math.round(this.leftMm / CONVERSION_X);
            this.textRow = (int) Math.round(this.topMm / CONVERSION_Y);
        }
    }

    @Override
    public List<AdsReportWidget> getChildren() {
        return null;
    }

}
