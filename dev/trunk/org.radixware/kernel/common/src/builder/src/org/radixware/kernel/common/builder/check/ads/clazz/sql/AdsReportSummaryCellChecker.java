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

import java.text.SimpleDateFormat;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroupBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;


@RadixObjectCheckerRegistration
public class AdsReportSummaryCellChecker<T extends AdsReportSummaryCell> extends AdsReportCellChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportSummaryCell.class;
    }

    private static boolean checkSummaryGroupCount(AdsReportSummaryCell summaryCell) {
        final int cellGroupCount = summaryCell.getGroupCount();

        if (cellGroupCount == 0) {
            return true;
        }

        if (cellGroupCount < 0) {
            return false;
        }

        final AdsReportBand band = summaryCell.getOwnerBand();
        if (!(band instanceof AdsReportGroupBand)) {
            return false;
        }

        final AdsReportGroupBand groupBand = (AdsReportGroupBand) band;
        final int groupIdx = groupBand.getOwnerGroup().getIndex();
        return cellGroupCount <= groupIdx + 1;
    }

    @Override
    public void check(T cell, IProblemHandler problemHandler) {
        super.check(cell, problemHandler);

        if (cell.findProperty() == null) {
            error(cell, problemHandler, "Property not found: #" + String.valueOf(cell.getPropertyId()));
        }
        
        if(cell.findProperty()!=null &&  cell.findProperty().getValue()!=null ){
           if(cell.getPattern()!=null && !cell.getPattern().isEmpty()) {
                error(cell, problemHandler, "Pattern is defined for summary cell");
           }          
        }

        if (!checkSummaryGroupCount(cell)) {
            error(cell, problemHandler, "Summary group out of range: " + cell.getGroupCount());
        }
    }
}
