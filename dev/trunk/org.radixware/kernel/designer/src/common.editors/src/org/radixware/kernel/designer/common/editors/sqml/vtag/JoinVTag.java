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

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.JoinTag;


public class JoinVTag<T extends JoinTag> extends SqmlVTag<T> {

    public JoinVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        final JoinTag.Type joinType = tag.getType();

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
        }

        final DdsReferenceDef ref = tag.findReference();
        final DdsTableDef childTable = (ref != null ? ref.findChildTable(tag) : null);
        final DdsTableDef parentTable = (ref != null ? ref.findParentTable(tag) : null);

        cp.print(" join ");

        if (parentTable != null) {
            cp.print(parentTable.getName());
        } else {
            cp.printError();
        }

        final String parentTableAlias = tag.getParentTableAlias();
        if (parentTableAlias != null && !parentTableAlias.isEmpty()) {
            cp.print(' ');
            cp.print(parentTableAlias);
        }

        cp.print(" on ");

        if (ref != null) {
            boolean columnAddedFlag = false;
            for (DdsReferenceDef.ColumnsInfoItem columnInfoItem : ref.getColumnsInfo()) {
                final DdsColumnDef childColumn = columnInfoItem.findChildColumn();
                final DdsColumnDef parentColumn = columnInfoItem.findParentColumn();

                if (columnAddedFlag) {
                    cp.print(" and ");
                } else {
                    columnAddedFlag = true;
                }

                final String childTableAlias = tag.getChildTableAlias();
                if (childTableAlias != null && !childTableAlias.isEmpty()) {
                    cp.print(childTableAlias);
                } else if (childTable != null) {
                    cp.print(childTable.getName());
                } else {
                    cp.printError();
                }

                cp.print('.');
                if (childColumn != null) {
                    cp.print(childColumn.getName());
                } else {
                    cp.printError();
                }
                cp.print('=');

                if (parentTableAlias != null && !parentTableAlias.isEmpty()) {
                    cp.print(parentTableAlias);
                } else if (parentTable != null) {
                    cp.print(parentTable.getName());
                } else {
                    cp.printError();
                }

                cp.print('.');

                if (parentColumn != null) {
                    cp.print(parentColumn.getName());
                } else {
                    cp.printError();
                }
            }
        } else {
            cp.printError();
        }
    }

    @Override
    public String getTokenName() {
        return "tag-join";
    }
}
