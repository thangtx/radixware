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
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;

class DbFuncCallTagTranslator<T extends DbFuncCallTag> extends SqmlTagTranslator<T> {

    public DbFuncCallTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(T tag, CodePrinter cp) {
        final DdsFunctionDef func = tag.getFunction();

        if (!SqmlVisitorProviderFactory.newDbFuncCallTagProvider(tag.getOwnerSqml()).isTarget(func)) {
            throw new TagTranslateError(tag, "Illegal function used: '" + func.getQualifiedName() + "'.");
        }

        checkDepecation(tag, func);

        final DdsPlSqlObjectDef plSqlObject = func.getOwnerPlSqlObject();

        checkDepecation(tag, plSqlObject);

        cp.print(plSqlObject.getDbName());
        cp.print('.');
        cp.print(func.getDbName());

        if (tag.isParamsDefined()) {
            cp.print('(');
            boolean paramAddedFlag = false;
            for (DbFuncCallTag.ParamValue tagParam : tag.getParamValues()) {
                final Id paramId = tagParam.getParamId();
                final DdsParameterDef funcParam = func.getParameters().getById(paramId);

                if (paramAddedFlag) {
                    cp.print(", ");
                } else {
                    paramAddedFlag = true;

                }
                cp.print(funcParam.getDbName());
                cp.print(" => ");
                cp.print(tagParam.getValue());
            }
            cp.print(')');
        }
    }
}
