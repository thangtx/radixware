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

package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExportReportToCsvInfo.ExportCsvColumn;


public class AdsExportCsvColumn extends RadixObject {

    private Id propId;
    private String extName;
    private AdsReportFormat format;

    public AdsExportCsvColumn(ExportCsvColumn column) {
        propId = column.getId();
        extName = column.getExtName();
        if (column.isSetFormat()) {
            format = new AdsReportFormat(column.getFormat());
        } else {
            format = null;
        }
    }

    public AdsExportCsvColumn(Id propId, String extName, AdsReportFormat format) {
        this.propId = propId;
        this.extName = extName;
        this.format = format;
    }

    public AdsExportCsvColumn(Id propId, String extName) {
        this.propId = propId;
        this.extName = extName;
        this.format = null;
    }

    public AdsReportFormat getFormat() {
        return format;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String name) {
        extName = name;
    }

    public void setFormat(AdsReportFormat format) {
        this.format = format;
    }

    public Id getPropId() {
        return propId;
    }

    public void appendTo(ExportCsvColumn xExportCsvColumn) {
        xExportCsvColumn.setExtName(extName);
        xExportCsvColumn.setId(propId);
        if (format != null) {
            format.appendTo(xExportCsvColumn.addNewFormat(), getEditState());
        }
    }
}
