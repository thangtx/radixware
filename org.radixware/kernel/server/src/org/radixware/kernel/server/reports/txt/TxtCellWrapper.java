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
package org.radixware.kernel.server.reports.txt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.server.reports.CellWrapper;

public class TxtCellWrapper extends CellWrapper {

    private static boolean isRoundHalfUp = true;
    
    public static void reset() {
        isRoundHalfUp = true;
    }

    public TxtCellWrapper(AdsReportCell cell) {
        super(cell);
    }

    @Override
    public double getLeft() {
        return (double) cell.getLeftColumn();
    }

    @Override
    public double getTop() {
        return (double) cell.getTopRow();
    }

    @Override
    public double getWidth() {
        return (double) cell.getWidthCols();
    }

    @Override
    public void setLeft(double value) {
        cell.setLeftColumn((int) value);
    }

    @Override
    public void setTop(double value) {
        cell.setTopRow((int) value);
    }

    @Override
    public void setWidth(double value) {
        RoundingMode mode = isRoundHalfUp ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN;
        BigDecimal roundedValue = new BigDecimal(value).setScale(0, mode);

        cell.setWidthCols(roundedValue.intValue());
        if (value - Math.floor(value) == 0.5) {
            isRoundHalfUp = !isRoundHalfUp;
        }
    }

    @Override
    public void applyWidthSettings() {
        if (widthSettings != null) {
            cell.setWidthCols(widthSettings.getColumns());
        }
    }
}
