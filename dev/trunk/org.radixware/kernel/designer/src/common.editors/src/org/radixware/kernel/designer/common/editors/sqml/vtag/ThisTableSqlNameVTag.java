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

import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ThisTableSqlNameTag;


public class ThisTableSqlNameVTag<T extends ThisTableSqlNameTag> extends SqmlVTag<T> {

    public ThisTableSqlNameVTag(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        return "tag-this-table-sql-name";
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        T tag = getTag();
        if (tag.findTable() != null) {
            cp.print("ThisTable");
        } else {
            cp.printError();
        }
    }
}