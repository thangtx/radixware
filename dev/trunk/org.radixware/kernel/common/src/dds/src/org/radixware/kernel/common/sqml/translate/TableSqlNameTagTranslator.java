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

package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;

class TableSqlNameTagTranslator<T extends TableSqlNameTag> extends SqmlTagTranslator<T> {

    public TableSqlNameTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(T tag, CodePrinter cp) {
        final DdsTableDef table = tag.getTable();

        if (!SqmlVisitorProviderFactory.newTableSqlNameTagProvider().isTarget(table)) {
            throw new TagTranslateError(tag, "Illegal table used: '" + table.getQualifiedName() + "'.");
        }

        checkDepecation(tag, table);
        cp.print(table.getDbName());
        final String tableAlias = tag.getTableAlias();
        if (tableAlias != null && !tableAlias.isEmpty()) {
            cp.print(' ');
            cp.print(tableAlias);
        }
    }
}
