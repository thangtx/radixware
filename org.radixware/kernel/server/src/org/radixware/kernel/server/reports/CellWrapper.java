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
package org.radixware.kernel.server.reports;

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.schemas.reports.ReportColumnsList;

public abstract class CellWrapper {
    
    protected ReportColumnsList.Column.Width widthSettings;

    public abstract double getLeft();

    public abstract double getTop();

    public abstract double getWidth();

    public abstract void setLeft(double value);

    public abstract void setTop(double value);

    public abstract void setWidth(double value);
    
    public abstract void applyWidthSettings();

    protected final AdsReportCell cell;

    public CellWrapper(AdsReportCell cell) {
        this.cell = cell;
    }
    
    public void setWidthSettings(ReportColumnsList.Column.Width widthSettings) {
        this.widthSettings = widthSettings;
    }
}
