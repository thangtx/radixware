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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.DbNameTag;

class DbNameTagTranslator<T extends DbNameTag> extends SqmlTagTranslator<T> {

    public DbNameTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        final Definition target = tag.getTarget();
        if (target instanceof IDdsDbDefinition) {
            checkDepecation(tag, target);
            final IDdsDbDefinition dbDef = (IDdsDbDefinition) target;
            if (dbDef instanceof DdsFunctionDef) {
                final DdsFunctionDef func = (DdsFunctionDef) dbDef;
                final DdsPlSqlObjectDef plSqlObject = func.getOwnerPlSqlObject();
                cp.print(plSqlObject.getDbName());
                cp.print('.');
            }
            cp.print(dbDef.getDbName());

        } else {
            throw new TagTranslateError(tag, "Illegal object used in definition database name tag: '" + target.getQualifiedName() + "'.");
        }
    }
}
