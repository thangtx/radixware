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

import java.util.Set;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Text;

public class DdsReferenceRadixdocSupport extends DdsDefinitionRadixdoc<DdsReferenceDef> {

    public DdsReferenceRadixdocSupport(DdsReferenceDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    public IRadixdocPage document(org.radixware.schemas.radixdoc.Page page, DocumentOptions options) {
        return new DdsDefinitionRadixdoc(getSource(), page, options);
    }

    @Override
    public void documentContent(final Page page) {
        final Block detailChapter = page.addNewBlock();
        detailChapter.setStyle(DefaultStyle.DETAIL);
        documentDetail(detailChapter);
    }

    @SuppressWarnings("empty-statement")
    private void documentDetail(final Block detail) {
        final DdsReferenceDef ddsRef = (DdsReferenceDef) getSource();
        final Block descriptionBlock = detail.addNewBlock();
        getWriter().appendStyle(descriptionBlock, DefaultStyle.CHAPTER);
        getWriter().addStrTitle(descriptionBlock, "Description");

        Block block = descriptionBlock.addNewBlock();

        Table descriptionTable = block.addNewTable();
        descriptionTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headTypeTable = descriptionTable.addNewRow();
        headTypeTable.addNewCell().addNewText().setStringValue("Database Name");
        headTypeTable.addNewCell().addNewText().setStringValue(source.getDbName());
        Table.Row dbName = descriptionTable.addNewRow();
        dbName.addNewCell().addNewText().setStringValue("Type");
        dbName.addNewCell().addNewText().setStringValue(getAsStrForRefType(source.getType().name()));

        final Block rootChapter = descriptionBlock.addNewBlock();

        Text titleText = rootChapter.addNewText();
        titleText.setMeta(DefaultStyle.TITLE);
        titleText.setStringValue("Database Options");

        final Table dbOptionsTable = rootChapter.addNewTable();
        dbOptionsTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headTable = dbOptionsTable.addNewRow();
        headTable.setMeta("head");
        headTable.addNewCell().addNewText().setStringValue("Option");
        headTable.addNewCell().addNewText().setStringValue("Value");

        Set<EDdsConstraintDbOption> dbOpts = ddsRef.getDbOptions();

        for (EDdsConstraintDbOption dbOption : EDdsConstraintDbOption.values()) {
            Table.Row optRow = dbOptionsTable.addNewRow();
            optRow.addNewCell().addNewText().setStringValue(dbOption.getName());
            optRow.addNewCell().addNewText().setStringValue(dbOpts.contains(dbOption) ? "\u2714" : "\u2718");
        }
        final Block columnChapter = descriptionBlock.addNewBlock();

        Text columnText = columnChapter.addNewText();
        columnText.setMeta(DefaultStyle.TITLE);
        columnText.setStringValue("Columns");

        Table table = columnChapter.addNewTable();
        table.setStyle(DefaultStyle.MEMBERS);
        Table.Row head = table.addNewRow();
        head.setMeta("head");
        head.addNewCell().addNewText().setStringValue("Columns of " + source.findChildTable(source).getName());
        head.addNewCell().addNewText().setStringValue("Columns of " + source.findParentTable(source).getName());
        for (DdsReferenceDef.ColumnsInfoItem columnInfo : source.getColumnsInfo()) {
            DdsColumnDef childColumn = columnInfo.findChildColumn();
            final Table.Row row = table.addNewRow();

            if (childColumn == null) {
                continue;
            }
            DdsColumnDef parentColumn = columnInfo.findParentColumn();
            if (parentColumn == null) {
                continue;
            };
            Table.Row.Cell childCell = row.addNewCell();
            Ref childRef = childCell.addNewRef();
            childRef.setPath(resolve(source, childColumn));
            childRef.addNewText().setStringValue(childColumn.getName());
            Table.Row.Cell parentCell = row.addNewCell();
            Ref parentRef = parentCell.addNewRef();
            parentRef.setPath(resolve(source, parentColumn));
            parentRef.addNewText().setStringValue(parentColumn.getName());
        }
    }

    public String getAsStrForRefType(String name) {
        switch (DdsReferenceDef.EType.valueOf(name)) {
            case MASTER_DETAIL:
                return "Master detail";
            case LINK:
                return "Link";
            default:
                return "not defined";
        }
    }
}
