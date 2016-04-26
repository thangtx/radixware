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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;

public class ClassCatalogsRadixdoc extends AdsDefinitionRadixdoc<AdsClassCatalogDef> {

    public ClassCatalogsRadixdoc(AdsClassCatalogDef source, Page page, DocumentOptions options) {
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
        Table classTable = overview.addNewTable();
        classTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerRow = classTable.addNewRow();
        headerRow.setMeta("head");
        Table.Row.Cell headerCell = headerRow.addNewCell();
        headerCell.addNewText().setStringValue("Class");
        for (AdsClassCatalogDef classRef : source.getAll()) {
            for (int i = 0; i < classRef.getClassRefList().size(); i++) {
                AdsClassDef classDef = classRef.getClassRefList().get(i).findReferencedClass();
                Table.Row.Cell classCell = classTable.addNewRow().addNewCell();
                Ref ref = classCell.addNewRef();
                final RadixIconResource icon = new RadixIconResource(classDef.getIcon());
                ref.addNewResource().setSource(icon.getKey());
                addResource(icon);
                ref.setPath(resolve(source, classDef));
                ref.addNewText().setStringValue(classDef.getQualifiedName());
            }
        }
    }
}
