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

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class DdsPlSqlHeaderDef extends DdsPlSqlPartDef {

    private static class Items extends DdsPlSqlPartItems {

        public Items(DdsPlSqlHeaderDef owner) {
            super(owner);
        }

        @Override
        protected CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return (objectInClipboard.getObject() instanceof DdsCustomTextDef)
                    || (objectInClipboard.getObject() instanceof DdsPrototypeDef) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public String getName() {
            return HEADER_TYPE_TITLE;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.HEADER;
        }

        @Override
        public String getTypeTitle() {
            return HEADER_TYPE_TITLE;
        }

        @Override
        public String getTypesTitle() {
            return HEADER_TYPES_TITLE;
        }
    }
    private final Items items = new Items(this);

    protected DdsPlSqlHeaderDef(DdsPlSqlObjectDef plSqlObjectDef) {
        super(plSqlObjectDef, "header");
    }

    public void loadFrom(org.radixware.schemas.ddsdef.PlSqlHeader xHeader) {
        items.clear();

        if (xHeader != null) {
            List<org.radixware.schemas.ddsdef.PlSqlHeader.Item> xItems = xHeader.getItemList();
            for (org.radixware.schemas.ddsdef.PlSqlHeader.Item xItem : xItems) {
                if (xItem.isSetPrototype()) {
                    DdsPrototypeDef prototype = DdsPrototypeDef.Factory.loadFrom(xItem.getPrototype());
                    items.add(prototype);
                } else if (xItem.isSetCustomText()) {
                    DdsCustomTextDef text = DdsCustomTextDef.Factory.loadFrom(xItem.getCustomText());
                    items.add(text);
                } else {
                    throw new java.lang.IllegalStateException();
                }
            }
        }
    }

    @Override
    public DdsDefinitions<DdsPlSqlObjectItemDef> getItems() {
        return items;
    }

    @Override
    public String getName() {
        return HEADER_TYPE_TITLE;
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.HEADER;
    }
    public static final String HEADER_TYPE_TITLE = "Header";
    public static final String HEADER_TYPES_TITLE = "Headers";

    @Override
    public String getTypeTitle() {
        return HEADER_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return HEADER_TYPES_TITLE;
    }
}
