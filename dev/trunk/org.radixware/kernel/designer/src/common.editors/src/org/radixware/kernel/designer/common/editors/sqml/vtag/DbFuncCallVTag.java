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

import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.types.Id;


public class DbFuncCallVTag<T extends DbFuncCallTag> extends SqmlVTag<T> {

    public DbFuncCallVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        final DdsFunctionDef func = tag.findFunction();
        final DdsPlSqlObjectDef plSqlObject = (func != null ? func.getOwnerPlSqlObject() : null);

        if (plSqlObject != null) {
            cp.print(plSqlObject.getName());
        } else {
            cp.printError();
        }

        cp.print('.');

        if (func != null) {
            cp.print(func.getName());
        } else {
            cp.printError();
        }

        if (tag.isParamsDefined()) {
            cp.print('(');
            boolean paramAddedFlag = false;
            for (DbFuncCallTag.ParamValue tagParam : tag.getParamValues()) {
                final Id paramId = tagParam.getParamId();
                final DdsParameterDef param = (func!=null ? func.getParameters().findById(paramId) : null);

                if (paramAddedFlag) {
                    cp.print(", ");
                } else {
                    paramAddedFlag = true;
                }


                if (param != null) {
                    cp.print(param.getName());
                } else {
                    cp.printError();
                }

                cp.print(" => ");
                cp.print(tagParam.getValue());
            }
            cp.print(')');
        }
    }

    @Override
    public String getTokenName() {
        return "tag-db-func-call";
    }
}

