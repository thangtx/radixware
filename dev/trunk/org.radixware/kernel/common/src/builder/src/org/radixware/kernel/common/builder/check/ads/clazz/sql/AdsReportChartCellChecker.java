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

package org.radixware.kernel.common.builder.check.ads.clazz.sql;

import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsChartDataInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell.AdsReportChartAxes;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartSeries;


@RadixObjectCheckerRegistration
public class AdsReportChartCellChecker<T extends AdsReportChartCell> extends AdsReportCellChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportChartCell.class;
    }

    @Override
    public void check(T cell, IProblemHandler problemHandler) {
        super.check(cell, problemHandler);
        
        if (cell.getTitleId() != null && cell.findTitle() == null) {
                error(cell, problemHandler, "Title for chart not found: #" + String.valueOf(cell.getTitleId()));
        }
        for(AdsReportChartSeries series: cell.getChartSeries()){
            checkChartData(cell, problemHandler,series.getDomainData());
            checkChartData(cell, problemHandler,series.getSeriesData());
            checkChartData(cell, problemHandler,series.getRangeData());
            if (series.getTitleId() != null && series.findTitle() == null) {
                error(cell, problemHandler, "Title for series not found: #" + String.valueOf(series.getTitleId()));
            }
        }
        checkChartAxes( cell,  problemHandler, cell.getDomainAxes());        
        checkChartAxes( cell,  problemHandler, cell.getRangeAxes());
    }
    
    private void checkChartData(T cell, IProblemHandler problemHandler,AdsChartDataInfo chartDataInfo){
        if (chartDataInfo!= null) {
             if( chartDataInfo.getPropId()!=null && chartDataInfo.findProperty() == null){
                  error(cell, problemHandler, "Property not found: #" + String.valueOf(chartDataInfo.getPropId()));
             }              
        }
    }
    
    private void checkChartAxes(T cell, IProblemHandler problemHandler,AdsReportChartAxes axes){
        for(AdsReportChartAxis axis: axes){
            if (axis.getTitleId() != null && axis.findTitle() == null) {
                error(cell, problemHandler, "Title for axis not found: #" + String.valueOf(axis.getTitleId()));
            }
        }
    }
}
