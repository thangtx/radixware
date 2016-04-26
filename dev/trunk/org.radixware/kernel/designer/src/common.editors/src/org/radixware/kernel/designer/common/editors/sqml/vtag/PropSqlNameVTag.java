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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentRefProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentReferenceInfo;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;


public class PropSqlNameVTag<T extends PropSqlNameTag> extends SqmlVTag<T> {

    public PropSqlNameVTag(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        return "tag-prop-sql-name";
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();
        final ISqmlProperty property = tag.findProperty();

        if (property == null) {
            cp.printError();
            return;
        }
        EOwnerType ownerType = tag.getOwnerType();

        String ownerPrefix = "";
        switch (ownerType) {
            case THIS:
                ownerPrefix = "This";
                break;
            case PARENT:
                ownerPrefix = "Parent";
                break;
            case CHILD:
                ownerPrefix = "Child";
                break;
            case TABLE:
                final String tableAlias = tag.getTableAlias();
                if (tableAlias != null && !tableAlias.isEmpty()) {
                    ownerPrefix = tableAlias;
                } else {
                    final DdsTableDef table = property.findOwnerTable();
                    if (table == null) {
                        cp.printError();
                        return;
                    }
                    ownerPrefix = table.getName();
                }
        }

        final Definition def = property.getDefinition();
        if (def instanceof ParentRefProperty) {
            final ParentRefProperty parentRefProperty = (ParentRefProperty) property.getDefinition();
            final ParentReferenceInfo refInfo = parentRefProperty.getParentReferenceInfo();
            if (refInfo == null) {
                cp.printError();
            } else {
                final DdsReferenceDef parentRefernece = refInfo.findParentReference();
                if (parentRefernece == null) {
                    cp.printError();
                } else {
                    cp.print(ownerPrefix);
                    cp.print(".");
                    cp.print(property.getName());
                    cp.print("(by ");
                    boolean first = true;
                    for (DdsReferenceDef.ColumnsInfoItem item : parentRefernece.getColumnsInfo().list()) {
                        if (first) {
                            first = false;
                        } else {
                            cp.print(' ');
                        }
                        if (!ownerPrefix.isEmpty()) {
                            cp.print(ownerPrefix);
                            cp.print(".");
                        }
                        cp.print(item.getChildColumn().getName());
                    }
                    cp.print(')');
                }
            }
        } else {
            if (ownerPrefix.length() > 0) {
                cp.print(ownerPrefix);
                cp.print(".");
            }
            cp.print(property.getName());
        }
    }
}
