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

package org.radixware.kernel.common.client.dashboard;


public class HistoricalDiagramSettings {

    public static enum TimeRange {
        LAST_PERIOD, INTERVAL
    }
    private double[] valScale = null;
    private boolean isAutoRange;
    private boolean isHistogram;
    private boolean isShowChangeRange = true;
    private DashTimeRangeProp timeRangeProp;

    public boolean isHistogram() {
        return isHistogram;
    }

    public void setIsHistogram(boolean isHistogram) {
        this.isHistogram = isHistogram;
    }

    public double[] getValueScale() {
        return valScale;
    }

    public void setValueScale(double minVal, double maxVal) {
        if (valScale == null) {
            valScale = new double[]{minVal, maxVal};
        } else {
            valScale[0] = minVal;
            valScale[1] = maxVal;
        }
    }

    public boolean isAutoValueRange() {
        return isAutoRange;
    }

    public void setAutoValueRange(boolean isAutoRange) {
        this.isAutoRange = isAutoRange;
    }

    public boolean isShowChangeRange() {
        return isShowChangeRange;
    }

    public void setIsShowChangeRange(boolean isShowChangeRange) {
        this.isShowChangeRange = isShowChangeRange;
    }

    public DashTimeRangeProp getTimeRangeProp() {
        return timeRangeProp;
    }

    public void setTimeRangeProp(DashTimeRangeProp timeRange) {
        this.timeRangeProp = timeRange;
    }
}
