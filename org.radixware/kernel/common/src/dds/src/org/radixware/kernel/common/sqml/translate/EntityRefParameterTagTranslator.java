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

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.EntityRefParameterTag;


public class EntityRefParameterTagTranslator<T extends EntityRefParameterTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        final IParameterDef param = tag.getParameter(); // for check
        if (tag.getOwnerSqml() != null) {
            final Sqml sqml = tag.getOwnerSqml();
            final DdsTableDef referencedTable = sqml.getEnvironment().findTableById(tag.getReferencedTableId());
            if (referencedTable != null) {
                final String paramName = tag.getParameter().getName();
                switch (tag.getPidTranslationMode()) {
                    case AS_STR:
                        printPid(paramName, cp);
                        break;
                    case PRIMARY_KEY_PROPS:
                        printPk(paramName, cp, referencedTable);
                        break;
                    case SECONDARY_KEY_PROPS:
                        printSk(paramName, cp, referencedTable, tag);
                        break;
                    default:
                        cp.print("Unknown pid translation mode");
                }
            } else {
                cp.printError();
            }
        } else {
            cp.printError();
        }
    }

    private void printPid(final String paramName, final CodePrinter cp) {
        cp.print(":");
        cp.print(paramName);
        cp.print(".PID");
    }

    private void printPk(final String paramName, final CodePrinter cp, final DdsTableDef referencedTable) {
        printIndex(paramName, referencedTable.getPrimaryKey(), cp);
    }

    private void printSk(final String paramName, final CodePrinter cp, final DdsTableDef referencedTable, final T tag) {
        final DdsIndexDef skDef = referencedTable.getIndices().findById(tag.getPidTranslationSecondaryKeyId(), EScope.ALL).get();
        if (skDef != null) {
            printIndex(paramName, skDef, cp);
        } else {
            cp.printError();
        }
    }

    private void printIndex(final String paramName, final DdsIndexDef index, final CodePrinter cp) {
        final List<ColumnInfo> columnInfoList = index.getColumnsInfo().list();
        boolean needBraces = false;
        if (columnInfoList.size() > 1) {
            needBraces = true;
        }
        if (needBraces) {
            cp.print('(');
        }
        boolean first = true;
        for (ColumnInfo columnInfo : columnInfoList) {
            if (first) {
                first = false;
            } else {
                cp.print(", ");
            }
            cp.print(':');
            cp.print(paramName);
            cp.print('.');
            cp.print(columnInfo.getColumn().getName());
        }
        if (needBraces) {
            cp.print(')');
        }
    }
}
