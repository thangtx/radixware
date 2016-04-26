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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;


class BandSizes {

    private final Double bandHeight;
    private final Map<AdsReportWidget, Double> cell2Top = new HashMap<AdsReportWidget, Double>();
    private final Map<AdsReportWidget, Double> cell2Height = new HashMap<AdsReportWidget, Double>();

    public BandSizes(AdsReportBand band) {
        this.bandHeight = band.getHeightMm();
        for (AdsReportWidget cell : band.getWidgets()) {
            cell2Top.put(cell, cell.getTopMm());
            cell2Height.put(cell, cell.getHeightMm());
        }
    }

    public void assignTo(AdsReportBand band) {
        band.setHeightMm(bandHeight);
        for (Map.Entry<AdsReportWidget, Double> entry : cell2Top.entrySet()) {
            entry.getKey().setTopMm(entry.getValue());
        }
        for (Map.Entry<AdsReportWidget, Double> entry : cell2Height.entrySet()) {
            entry.getKey().setHeightMm(entry.getValue());
        }
    }
}
