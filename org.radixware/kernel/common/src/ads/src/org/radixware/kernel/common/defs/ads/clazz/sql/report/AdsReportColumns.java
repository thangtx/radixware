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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.schemas.adsdef.Report.ReportColumns;
import org.radixware.schemas.adsdef.ReportColumnDefinition;

public class AdsReportColumns extends RadixObjects<AdsReportColumnDef> {

    public AdsReportColumns(RadixObject container) {
        super(container);
    }    

    void appendTo(ReportColumns columnsList) {
        for (AdsReportColumnDef column : this) {
            ReportColumnDefinition xColumn = columnsList.addNewReportColumn();
            column.appendTo(xColumn);                        
        }
    }
    
    @Override
    public void collectDependences(List<Definition> list) {
        if (!isEmpty()) {
            for (AdsReportColumnDef column : this) {
                column.collectDependences(list);
            }
        }
    }
}
