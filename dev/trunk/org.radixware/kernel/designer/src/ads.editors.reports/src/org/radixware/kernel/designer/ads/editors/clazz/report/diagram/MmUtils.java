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

import java.awt.Toolkit;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;


public class MmUtils {

    private static final double INCH2MM = 25.4;
    private static final double DPMM = 1.0 * Toolkit.getDefaultToolkit().getScreenResolution() / INCH2MM; // dot per millimeter

    public static double roundToTenth(final double d) {
        return Math.round(10.0 * d) / 10.0;
    }

    public static double snapToGrid(final double d) {
        if (AdsReportFormDiagramOptions.getDefault().isSnapToGrid()) {
            long c = Math.round(d / AdsReportBand.GRID_SIZE_MM);
            return AdsReportBand.GRID_SIZE_MM * c;
        } else {
            return d;
        }
    }

    public static int mm2px(final double mm) {
        final double k = 1.0 * AdsReportFormDiagramOptions.getDefault().getScalePercent() / 100.0;
        return (int) (mm * DPMM * k);
    }

    public static double px2mm(final int px) {
        final double k = 1.0 * AdsReportFormDiagramOptions.getDefault().getScalePercent() / 100.0;
        return 1.0 * px / DPMM / k;
    }
}
