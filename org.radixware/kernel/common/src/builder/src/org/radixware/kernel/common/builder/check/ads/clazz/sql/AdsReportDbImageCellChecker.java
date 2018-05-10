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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.common.ReleaseUtils;
import org.radixware.kernel.common.enums.EValType;


@RadixObjectCheckerRegistration
public class AdsReportDbImageCellChecker<T extends AdsReportDbImageCell> extends AdsReportCellChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsReportDbImageCell.class;
    }

    @Override
    public void check(T cell, IProblemHandler problemHandler) {
        super.check(cell, problemHandler);

        final AdsPropertyDef imageDataProp = cell.findDataProperty();
        if (imageDataProp != null) {
            if (imageDataProp.getValue().getType().getTypeId() != EValType.BLOB) {
                error(cell, problemHandler, "Image data property must be BLOB");
            }
            ReleaseUtils.checkExprationRelease(cell, imageDataProp, problemHandler);
        } else {
            error(cell, problemHandler, "Image data property not found: #" + String.valueOf(cell.getDataPropertyId()));
        }

        final AdsPropertyDef mimeTypeProp = cell.findMimeTypeProperty();
        if (mimeTypeProp != null) {
            if (mimeTypeProp.getValue().getType().getTypeId() != EValType.STR) {
                error(cell, problemHandler, "Image mime-type property must be STR");
            }
            ReleaseUtils.checkExprationRelease(cell, mimeTypeProp, problemHandler);
        } else {
            error(cell, problemHandler, "Image mime-type property not found: #" + String.valueOf(cell.getMimeTypePropertyId()));
        }
    }
}
