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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EReportExportFormat implements IKernelStrEnum {

    //constant values for compiling
    RTF("RTF", "application/rtf", "rtf"), // for testing
    PDF("PDF", "application/pdf", "pdf"),
    XML("XML", "text/xml", "xml"),
    TXT("TXT", "text/plain", "txt"),
    CSV("CSV", "text/csv", "csv"),
    OOXML("OOXML", "application/ooxml", "ooxml"),
    XLSX("XLSX", "application/xlsx", "xlsx"),
    MSDL("Structured File", "application/msdl", null),
    CUSTOM("Custom", "custom", "out");
    private final String name;
    private final String value;
    private final String ext;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    private EReportExportFormat(String name, String value, String ext) {
        this.name = name;
        this.value = value;
        this.ext = ext;
    }

    public static EReportExportFormat getForValue(final String val) {
        for (EReportExportFormat t : EReportExportFormat.values()) {
            if (t.value.equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EReportExportFormat has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
