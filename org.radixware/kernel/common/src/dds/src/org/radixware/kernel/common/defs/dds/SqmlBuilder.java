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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;


class SqmlBuilder {

    private final Sqml sqml;

    public SqmlBuilder() {
        this.sqml = Sqml.Factory.newInstance();
    }

    public void print(String sql) {
        final Sqml.Text text = Sqml.Text.Factory.newInstance(sql);
        sqml.getItems().add(text);
    }

    public void printTable(DdsTableDef table) {
        final TableSqlNameTag tag = TableSqlNameTag.Factory.newInstance();
        tag.setTableId(table.getId());
        tag.setSql(table.getDbName());
        sqml.getItems().add(tag);
    }

    private void printColumn(DdsColumnDef column, PropSqlNameTag.EOwnerType ownerType) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(ownerType);
        tag.setPropOwnerId(column.getOwnerTable().getId());
        tag.setPropId(column.getId());
        if (ownerType == PropSqlNameTag.EOwnerType.TABLE) {
            tag.setSql(column.getOwnerTable().getDbName() + "." + column.getDbName());
        } else {
            tag.setSql(column.getDbName());
        }
        sqml.getItems().add(tag);
    }

    public void printTableColumn(DdsColumnDef column) {
        printColumn(column, PropSqlNameTag.EOwnerType.TABLE);
    }

    public void printColumn(DdsColumnDef column) {
        printColumn(column, PropSqlNameTag.EOwnerType.NONE);
    }

    public void println(String sql) {
        print(sql + "\n");
    }

    public void println() {
        print("\n");
    }

    public Sqml getSqml() {
        return sqml;
    }
}
