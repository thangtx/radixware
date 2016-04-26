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
import org.radixware.kernel.common.sqml.tags.ThisTableSqlNameTag;

class ThisTableSqlNameTagTranslator<T extends ThisTableSqlNameTag> extends SqmlTagTranslator<T> {

    public ThisTableSqlNameTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(ThisTableSqlNameTag tag, CodePrinter cp) {
        final DdsTableDef thisTable = tag.findTable();
        if (thisTable == null) {
            throw new TagTranslateError(tag, "Illegal context for THIS table");
        }
        checkDepecation(tag, thisTable);
        cp.print(thisTable.getDbName());
    }
}
