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

package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef.UsedTable;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class SqlClassRadixdoc extends ClassRadixdoc {

    public SqlClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        AdsSqlClassDef sqlCl = (AdsSqlClassDef) source;
        int num = 1;
        for (UsedTable table : sqlCl.getUsedTables()) {
            getClassWriter().addStr2RefRow(overviewTable, "Used table #" + num++, table.findTable(), source);
        }
        getClassWriter().addStr2BoolRow(overviewTable, "Read Only", sqlCl.isDbReadOnly());
    }
}
