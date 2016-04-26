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
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.radixdoc.RadixdocXmlPage;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

public class DdsDefinitionRadixdoc<T extends DdsDefinition> extends RadixdocXmlPage<T> {

    protected static class DdsPageWriter<T extends DdsDefinition> extends PageWriter<T> {

        public DdsPageWriter(DdsDefinitionRadixdoc<T> page) {
            super(page);
        }

        protected void documentDescription(ContentContainer root, DdsDefinition definition) {
            if (definition.getDescriptionId() != null || definition.getDescription() != null && !definition.getDescription().isEmpty()) {
                final Block descritprionBlock = root.addNewBlock();
                descritprionBlock.setStyle(DefaultStyle.DESCRIPTION);

                if (definition.getDescriptionId() != null && definition.findExistingLocalizingBundle() != null) {
                    addMslId(descritprionBlock, definition.findExistingLocalizingBundle().getId(), definition.getDescriptionId());
                } else {
                    addText(descritprionBlock, definition.getDescription());
                }
            }
        }

        public void addAllStrRow(Table detailsTable, String... values) {
            Table.Row newRow = detailsTable.addNewRow();
            for (String value : values) {
                newRow.addNewCell().addNewText().setStringValue(value);
            }
        }

        public void addStr2BoolRow(Table detailsTable, String property, Boolean value) {
            Table.Row newRow = detailsTable.addNewRow();
            newRow.addNewCell().addNewText().setStringValue(property);
            newRow.addNewCell().addNewText().setStringValue(boolAsStr(value));
        }

        public String boolAsStr(boolean boolVal) {
            return boolVal ? "\u2714" : "\u2718";
        }
    }

    public DdsDefinitionRadixdoc(T source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected DdsPageWriter<T> getWriter() {
        return new DdsPageWriter<T>(this);
    }

    @Override
    public void buildPage() {
        page.setTitle(source.getName());
        page.setName(source.getId().toString());
        if (source.getOwnerDefinition() instanceof DdsModelDef) {
            page.setTopLevel(true);
        } else {
            page.setTopLevel(false);
        }
        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        addResource(iconResource);
        page.setIcon(iconResource.getKey());
        documentHead(page);
        documentDescription(page);
        documentContent(page);
    }

    protected Block documentDescription(Page page) {
        final Block descriptionChapter = page.addNewBlock();
        descriptionChapter.setStyle(DefaultStyle.DESCRIPTION);
        documentDescriptionExtensions(descriptionChapter);
        documentOverview(descriptionChapter);
        return descriptionChapter;
    }

    protected void documentOverview(ContentContainer container) {
        if (source.getDescriptionId() != null || !source.getDescription().isEmpty()) {
            final Block overview = container.addNewBlock();
            overview.setStyle(DefaultStyle.CHAPTER);
            getWriter().appendStyle(overview, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
            getWriter().addStrTitle(overview, "Overview");
            getWriter().documentDescription(overview, source);
        }
    }

    protected void documentDescriptionExtensions(ContentContainer container) {
    }

    protected void documentContent(Page page) {
    }
}
