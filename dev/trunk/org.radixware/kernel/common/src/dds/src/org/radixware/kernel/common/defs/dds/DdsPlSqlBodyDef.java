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
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class DdsPlSqlBodyDef extends DdsPlSqlPartDef {

    private static class Items extends DdsPlSqlPartItems {

        public Items(DdsPlSqlBodyDef owner) {
            super(owner);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return (objectInClipboard.getObject() instanceof DdsPlSqlObjectItemDef) ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public String getName() {
            return BODY_TYPE_TITLE;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.BODY;
        }

        @Override
        public String getTypeTitle() {
            return BODY_TYPE_TITLE;
        }

        @Override
        public String getTypesTitle() {
            return BODY_TYPES_TITLE;
        }
    }
    private final Items items = new Items(this);

    protected DdsPlSqlBodyDef(DdsPlSqlObjectDef plSqlObjectDef) {
        super(plSqlObjectDef, "body");
    }

    public void loadFrom(org.radixware.schemas.ddsdef.PlSqlBody xBody) {
        items.clear();

        if (xBody != null) {
            List<org.radixware.schemas.ddsdef.PlSqlBody.Item> xItems = xBody.getItemList();
            for (org.radixware.schemas.ddsdef.PlSqlBody.Item xItem : xItems) {
                if (xItem.isSetFunction()) {
                    DdsFunctionDef function = DdsFunctionDef.Factory.loadFrom(xItem.getFunction());
                    items.add(function);
                } else if (xItem.isSetPrototype()) {
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
        return BODY_TYPE_TITLE;
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.BODY;
    }
    public static final String BODY_TYPE_TITLE = "Body";
    public static final String BODY_TYPES_TITLE = "Bodies";

    @Override
    public String getTypeTitle() {
        return BODY_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return BODY_TYPES_TITLE;
    }
}
