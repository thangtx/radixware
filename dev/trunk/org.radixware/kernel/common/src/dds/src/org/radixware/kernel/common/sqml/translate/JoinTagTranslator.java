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

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.JoinTag;
import org.radixware.kernel.common.sqml.tags.JoinTag.Type;

class JoinTagTranslator<T extends JoinTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(T tag, CodePrinter cp) {
        final Type joinType = tag.getType();

        switch (joinType) {
            case FULL:
                cp.print("full");
                break;
            case INNER:
                cp.print("inner");
                break;
            case LEFT:
                cp.print("left");
                break;
            case RIGHT:
                cp.print("right");
                break;
            default:
                throw new TagTranslateError(tag, "Illegal join type: " + String.valueOf(joinType));
        }

        DdsReferenceDef ref = tag.getReference();
        DdsTableDef childTable = ref.getChildTable(tag);
        DdsTableDef parentTable = ref.getParentTable(tag);

        cp.print(" join ");
        cp.print(parentTable.getDbName());

        String parentTableAlias = tag.getParentTableAlias();
        if (parentTableAlias != null && !parentTableAlias.isEmpty()) {
            cp.print(' ');
            cp.print(parentTableAlias);
        }

        cp.print(" on ");

        boolean columnAddedFlag = false;
        for (DdsReferenceDef.ColumnsInfoItem columnInfoItem : ref.getColumnsInfo()) {
            DdsColumnDef childColumn = columnInfoItem.getChildColumn();
            DdsColumnDef parentColumn = columnInfoItem.getParentColumn();

            if (columnAddedFlag) {
                cp.print(" and ");
            } else {
                columnAddedFlag = true;
            }

            String childTableAlias = tag.getChildTableAlias();
            if (childTableAlias != null && !childTableAlias.isEmpty()) {
                cp.print(childTableAlias);
            } else {
                cp.print(childTable.getDbName());
            }

            cp.print('.');
            cp.print(childColumn.getDbName());
            cp.print('=');

            if (parentTableAlias != null && !parentTableAlias.isEmpty()) {
                cp.print(parentTableAlias);
            } else {
                cp.print(parentTable.getDbName());
            }
            cp.print('.');
            cp.print(parentColumn.getDbName());
        }
    }
}
