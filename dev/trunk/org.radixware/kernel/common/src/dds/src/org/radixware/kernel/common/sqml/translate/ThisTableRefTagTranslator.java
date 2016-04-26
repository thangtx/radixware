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
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ThisTableRefTag;


public class ThisTableRefTagTranslator<T extends ThisTableRefTag> extends SqmlTagTranslator<T> {

    public ThisTableRefTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        if (tag.getOwnerSqml() == null) {
            throw new TagTranslateError(tag, "Can not find this table, bacause owner sqml is null");
        }
        final DdsTableDef referencedTable = tag.getOwnerSqml().getEnvironment().findThisTable();
        if (referencedTable == null) {
            throw new TagTranslateError(tag, "Can not find this table");
        }

        checkDepecation(tag, referencedTable);
        switch (tag.getPidTranslationMode()) {
            case AS_STR:
                printPid(cp, referencedTable);
                break;
            case PRIMARY_KEY_PROPS:
                printPk(cp, referencedTable);
                break;
            case SECONDARY_KEY_PROPS:
                printSk(tag, cp, referencedTable);
                break;
            default:
                throw new TagTranslateError(tag, "Unknown pid translation mode");
        }
    }

    private void printPid(final CodePrinter cp, final DdsTableDef referencedTable) {
        cp.print("this.PID");
    }

    private void printPk(final CodePrinter cp, final DdsTableDef referencedTable) {
        printIndex(referencedTable.getPrimaryKey(), cp);
    }

    private void printSk(final ThisTableRefTag tag, final CodePrinter cp, final DdsTableDef referencedTable) {
        final DdsIndexDef skDef = referencedTable.getIndices().findById(tag.getPidTranslationSecondaryKeyId(), EScope.ALL).get();
        if (skDef != null) {
            printIndex(skDef, cp);
        } else {
            cp.printError();
        }
    }

    private void printIndex(final DdsIndexDef index, final CodePrinter cp) {
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
            cp.print("this.");
            cp.print(columnInfo.getColumn().getName());
        }
        if (needBraces) {
            cp.print(")");
        }
    }
}
