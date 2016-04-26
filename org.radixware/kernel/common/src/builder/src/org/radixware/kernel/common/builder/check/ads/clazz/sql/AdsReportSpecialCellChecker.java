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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.enums.EReportSpecialCellType;


@RadixObjectCheckerRegistration
public class AdsReportSpecialCellChecker<T extends AdsReportSpecialCell> extends AdsReportCellChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportSpecialCell.class;
    }

    @Override
    public void check(T cell, IProblemHandler problemHandler) {
        super.check(cell, problemHandler);
        if (cell.getSpecialType() != EReportSpecialCellType.GENERATION_TIME) {
            if (cell.getPattern() != null && !cell.getPattern().isEmpty()) {
                error(cell, problemHandler, "Pattern is defined for not GENERATION_TIME type special cell");
            }
        } else {
            if (cell.getPattern() != null && !cell.getPattern().isEmpty()) {
                try {
                    new SimpleDateFormat(cell.getPattern());
                } catch (java.lang.IllegalArgumentException ex) {
                    error(cell, problemHandler, "Wrong pattern for GENERATION_TIME type special cell");
                }
            }
        }
    }
}
