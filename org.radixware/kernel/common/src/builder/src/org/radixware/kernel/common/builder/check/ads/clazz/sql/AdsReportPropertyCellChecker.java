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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.enums.EValType;


@RadixObjectCheckerRegistration
public class AdsReportPropertyCellChecker<T extends AdsReportPropertyCell> extends AdsReportCellChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportPropertyCell.class;
    }

    @Override
    public void check(T cell, IProblemHandler problemHandler) {
        super.check(cell, problemHandler);

        if (cell.findProperty() == null) {
            error(cell, problemHandler, "Property not found: #" + String.valueOf(cell.getPropertyId()));
        }

        if (cell.findProperty() != null && cell.findProperty().getValue() != null) {
            if (cell.findProperty().getValue().getType().getTypeId() != EValType.DATE_TIME) {
                if (cell.getPattern() != null && !cell.getPattern().isEmpty()) {
                    error(cell, problemHandler, "Pattern is defined for not DateTime type property cell");
                }
            } else {
                if (cell.getPattern() != null && !cell.getPattern().isEmpty()) {
                    try {
                        new SimpleDateFormat(cell.getPattern());
                    } catch (java.lang.IllegalArgumentException ex) {
                        error(cell, problemHandler, "Wrong pattern for property cell");
                    }
                }
            }
        }

        if (cell.getNullTitleId() != null && cell.findNullTitle() == null) {
            error(cell, problemHandler, "Title for null value not found: #" + String.valueOf(cell.getNullTitleId()));
        }
    }
}
