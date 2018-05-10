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
package org.radixware.kernel.server.reports.fo;

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.server.reports.CellWrapper;
import org.radixware.kernel.server.reports.CellWrapperFactory;
import org.radixware.schemas.reports.ReportColumnsList;

public class FoCellWrapperFactory implements CellWrapperFactory{

    @Override
    public CellWrapper newInstance(AdsReportCell cell) {
        return new CellWrapper(cell) {
            
            @Override
            public double getLeft() {
                return cell.getLeftMm();
            }
            
            @Override
            public double getTop() {
                return cell.getTopMm();
            }
            
            @Override
            public double getWidth() {
                return cell.getWidthMm();
            }
            
            @Override
            public void setLeft(double value) {
                cell.setLeftMm(value);
            }
            
            @Override
            public void setTop(double value) {
                cell.setTopMm(value);
            }
            
            @Override
            public void setWidth(double value) {
                cell.setWidthMm(value);
            }

            @Override
            public void applyWidthSettings() {
                if (widthSettings != null) {
                    cell.setWidthMm(widthSettings.getMillimeters().doubleValue());
                }
            }
        };
    }

    @Override
    public CellWrapper newInstance(AdsReportCell cell, ReportColumnsList.Column.Width initialWidth) {
        CellWrapper wrapper = newInstance(cell);
        if (initialWidth != null) {
            wrapper.setWidthSettings(initialWidth);
        }
        
        return wrapper;
    }
}
