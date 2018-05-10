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
package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsCsvReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroupBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsXlsxReportInfo;
import org.radixware.kernel.common.jml.Jml;

public class AdsReportWriterUtils {

    public static String getGroupJavaName(AdsReportGroup group) {
        final int index = group.getIndex();
        return "Group" + String.valueOf(index);
    }

    public static String getBandJavaName(AdsReportBand band) {
        if (band instanceof AdsReportGroupBand) {
            final AdsReportGroup group = ((AdsReportGroupBand) band).getOwnerGroup();
            final String groupJavaName = getGroupJavaName(group);
            return groupJavaName + (group.getFooterBand() == band ? "Footer" : "Header") + "Band";
        } else {
            final String name = band.getName();
            return name.replaceAll(" ", "");
        }
    }

    public static String getCellJavaName(AdsReportCell cell) {
        final AdsReportBand band = cell.getOwnerBand();
        final int index = band.getWidgets().indexOf(cell);
        final String bandJavaName = getBandJavaName(band);
        return bandJavaName + "Cell" + index;
    }

    private static class JmlSearcher extends VisitorProvider {

        private final String marker;

        public JmlSearcher(final String marker) {
            this.marker = marker;
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            if (radixObject instanceof Jml) {
                final Jml jml = (Jml) radixObject;
                final String jmlMarker = getJmlMarker(jml);
                return this.marker.equals(jmlMarker);
            } else {
                return false;
            }
        }
    }

    public static String getJmlMarker(Jml jml) {
        final RadixObject owner = jml.getContainer();
        final String ownerJavaName;
        if (owner instanceof AdsReportCell) {
            ownerJavaName = getCellJavaName((AdsReportCell) owner);
        } else if (owner instanceof AdsReportBand) {
            ownerJavaName = getBandJavaName((AdsReportBand) owner);
        } else if (owner instanceof AdsReportGroup) {
            ownerJavaName = getGroupJavaName((AdsReportGroup) owner);
        } else if (owner instanceof AdsCsvReportInfo) {
            ownerJavaName = AdsCsvReportInfo.IS_CSV_ROW_VISIBLE_CONDITION_NAME;
        } else if (owner instanceof AdsXlsxReportInfo) {
            ownerJavaName = AdsXlsxReportInfo.IS_XLSX_ROW_VISIBLE_CONDITION_NAME;
        } else {
            throw new IllegalStateException(String.valueOf(owner));
        }
        return ownerJavaName + jml.getName();
    }

    public static Jml findJmlByMarker(final AdsReportForm form, final AdsCsvReportInfo csvInfo, final String marker) {
        final JmlSearcher searcher = new JmlSearcher(marker);
        Jml result = (Jml) form.find(searcher);
        if (result == null && csvInfo != null) {
            result = (Jml) csvInfo.find(searcher);
        }
        return result;
    }
}
