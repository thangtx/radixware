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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;

public class AdsAbstractUIRadixdoc extends AdsDefinitionRadixdoc<AdsAbstractUIDef> {

    public AdsAbstractUIRadixdoc(AdsAbstractUIDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void documentOverview(ContentContainer container) {
        final Block overview = container.addNewBlock();
        overview.setStyle(DefaultStyle.CHAPTER);
        getWriter().appendStyle(overview, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
        getWriter().addStrTitle(overview, "Overview");

        List<String> modifiers = new ArrayList<>();
        if (getSource().isFinal()) {
            modifiers.add("Final");
        }
        if (getSource().isDeprecated()) {
            modifiers.add("Deprecated");
        }
        if (!modifiers.isEmpty()) {
            Table overviewTable = overview.addNewTable();
            overviewTable.setStyle(DefaultStyle.MEMBERS);
            Table.Row headerRow = overviewTable.addNewRow();
            headerRow.setMeta("head");
            Table.Row.Cell headerCell = headerRow.addNewCell();
            headerCell.setColspan(2);
            headerCell.addNewText().setStringValue("General attributes");
            getWriter().addAllStrRow(overviewTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }

        Table modelClassTable = overview.addNewTable();
        modelClassTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row modelClassHeaderRow = modelClassTable.addNewRow();
        modelClassHeaderRow.setMeta("head");
        modelClassHeaderRow.addNewCell().addNewText().setStringValue("Model Class");
        Table.Row.Cell modelClassCell = modelClassTable.addNewRow().addNewCell();
        Ref modelClassRef = modelClassCell.addNewRef();
        modelClassRef.setPath(resolve(source, source.getModelClass()));
        final RadixIconResource icon = new RadixIconResource(source.getModelClass().getIcon());
        modelClassRef.addNewResource().setSource(icon.getKey());
        addResource(icon);
        modelClassRef.addNewText().setStringValue(source.getModelClass().getName());

    }
}
