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


package org.radixware.kernel.common.defs.dds.radixdoc;

import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsTypeFieldDef;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Text;


public class DdsTypeRadixdocSupport extends DdsDefinitionRadixdoc<DdsTypeDef> {

    public DdsTypeRadixdocSupport(DdsTypeDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    public void documentContent(Page page) {
        final Block detailChapter = page.addNewBlock();
        detailChapter.setStyle(DefaultStyle.DETAIL);
        documentDetail(detailChapter);

        if (!source.getFields().isEmpty()) {
            Block fieldsBlock = page.addNewBlock();
            getWriter().appendStyle(fieldsBlock, DefaultStyle.CHAPTER);
            Text titleText = fieldsBlock.addNewText();
            titleText.setMeta(DefaultStyle.TITLE);
            titleText.setStringValue("Fields");

            Table fieldsTable = fieldsBlock.addNewTable();

            fieldsTable.setStyle(DefaultStyle.MEMBERS);
            Table.Row headFields = fieldsTable.addNewRow();
            headFields.setMeta("head");
            headFields.addNewCell().addNewText().setStringValue("Name");
            headFields.addNewCell().addNewText().setStringValue("Database Type");
            headFields.addNewCell().addNewText().setStringValue("Description");

            for (DdsTypeFieldDef field : source.getFields().list()) {
                final Table.Row row = fieldsTable.addNewRow();
                getWriter().setAttribute(row, DefaultAttributes.ANCHOR, field.getId().toString());
                row.addNewCell().addNewText().setStringValue(field.getName());
                row.addNewCell().addNewText().setStringValue(field.getDbType());
                if (!field.getDescription().isEmpty()) {
                    row.addNewCell().addNewText().setStringValue(field.getDescription());
                } else {
                    row.addNewCell().addNewText().setStringValue("<Not Defined>");
                }
            }
        }
    }

    private void documentDetail(final Block detail) {
        final Block descriptionBlock = detail.addNewBlock();
        getWriter().appendStyle(descriptionBlock, DefaultStyle.CHAPTER);
        Text overviewText = descriptionBlock.addNewText();
        overviewText.setMeta(DefaultStyle.TITLE);
        overviewText.setStringValue("Overview");

        Block typeBlock = descriptionBlock.addNewBlock();

        Table overviewTable = typeBlock.addNewTable();
        overviewTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row typeRow = overviewTable.addNewRow();
        typeRow.addNewCell().addNewText().setStringValue(" Type ");
        typeRow.addNewCell().addNewText().setStringValue(source.getDbType());

        Table.Row fieldsRow = overviewTable.addNewRow();
        fieldsRow.addNewCell().addNewText().setStringValue(" Fields ");
        Table.Row.Cell fieldsCell = fieldsRow.addNewCell();
        Text fieldText = fieldsCell.addNewText();
        if (source.getFields().isEmpty()) {
            fieldText.setStringValue("<Not Defined>");
        }
        for (int i = 0; i < source.getFields().list().size(); i++) {
            DdsTypeFieldDef field = source.getFields().list().get(i);
            if (fieldText.getStringValue().isEmpty()) {
                Ref ref = fieldsCell.addNewRef();
                ref.setPath(resolve(source, field));
                ref.addNewText().setStringValue(field.getName());
                if (i != source.getFields().list().size() - 1) {
                    fieldsCell.addNewText().setStringValue(", ");
                }
            }
        }
        Table.Row descriptionRow = overviewTable.addNewRow();
        descriptionRow.addNewCell().addNewText().setStringValue(" Description ");
        Table.Row.Cell descriptionCell = descriptionRow.addNewCell();
        Text descriptionText = descriptionCell.addNewText();
        if (!source.getDescription().isEmpty()) {
            descriptionText.setStringValue(source.getDescription());
        } else {
            descriptionText.setStringValue("<Not Defined>");
        }
    }
}
