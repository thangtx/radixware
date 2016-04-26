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

import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class DdsSequenceRadixdocSupport extends DdsDefinitionRadixdoc<DdsSequenceDef> {

    public DdsSequenceRadixdocSupport(DdsSequenceDef source, Page page, DocumentOptions options) {
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

    private void documentDetail(final Block detail) {
        final Block descriptionBlock = detail.addNewBlock();
        getWriter().appendStyle(descriptionBlock, DefaultStyle.CHAPTER);
        getWriter().addStrTitle(descriptionBlock, "Description");

        Table descrTable = descriptionBlock.addNewTable();
        descrTable.setStyle(DefaultStyle.MEMBERS);

        Table.Row dbName = descrTable.addNewRow();
        dbName.addNewCell().addNewText().setStringValue("Database Name");
        dbName.addNewCell().addNewText().setStringValue(source.getDbName());

        Table.Row maxValue = descrTable.addNewRow();
        maxValue.addNewCell().addNewText().setStringValue("Max Value");
        if (source.getMaxValue() != null) {
            maxValue.addNewCell().addNewText().setStringValue(source.getMaxValue().toString());
        } else {
            maxValue.addNewCell().addNewText().setStringValue("<Not defined>");
        }

        Table.Row minValue = descrTable.addNewRow();
        minValue.addNewCell().addNewText().setStringValue("Min Value");
        if (source.getMinValue() != null) {
            minValue.addNewCell().addNewText().setStringValue(source.getMinValue().toString());
        } else {
            minValue.addNewCell().addNewText().setStringValue("<Not defined>");
        }

        Table.Row startWithValue = descrTable.addNewRow();
        startWithValue.addNewCell().addNewText().setStringValue("Start With Value");
        if (source.getStartWith() != null) {
            startWithValue.addNewCell().addNewText().setStringValue(source.getStartWith().toString());
        } else {
            startWithValue.addNewCell().addNewText().setStringValue("<Not defined>");
        }
    }
}
