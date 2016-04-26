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

package org.radixware.kernel.designer.common.editors.sqml.vtag;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;


public class TableSqlNameVTag<T extends TableSqlNameTag> extends SqmlVTag<T> {

    public TableSqlNameVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        final DdsTableDef table = tag.findTable();

        if (table != null) {
            cp.print(table.getName());
        } else {
            cp.printError();
        }

        final String tableAlias = tag.getTableAlias();
        if (tableAlias != null && !tableAlias.isEmpty()) {
            cp.print(' ');
            cp.print(tableAlias);
        }
    }

    @Override
    public String getTokenName() {
        return "tag-table-sql-name";
    }
}
