package org.radixware.kernel.common.defs.ads.clazz.sql.report.utils;

public class AdsReportUtils {

    public static final double MM2PTS_CONST = 25.4 / 72;

    public static double mm2pts(double mm) {
        return Math.floor(10.0 * mm / MM2PTS_CONST) / 10.0;
    }

    public static double pts2mm(double pts) {
        return Math.floor(10.0 * pts * MM2PTS_CONST) / 10.0;
    }
}
