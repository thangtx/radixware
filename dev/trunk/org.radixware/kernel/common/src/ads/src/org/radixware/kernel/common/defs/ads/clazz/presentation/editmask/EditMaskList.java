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

package org.radixware.kernel.common.defs.ads.clazz.presentation.editmask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public class EditMaskList extends EditMask implements ILocalizedDef {

    public static class Item implements ILocalizedDef {

        private String title;
        private Id titleId;
        private String value;
        private EditMaskList em = null;

        private void modified() {
            if (em != null) {
                em.modified();
            }
        }

        protected Item() {
            titleId = null;
            title = "";
            value = null;
        }

        protected Item(Item item) {
            titleId = item.getTitleId();
            title = item.getTitle();
            value = item.getValue();
        }

        public String getTitle() {
            return title;
        }

        public Id getTitleId() {
            return titleId;
        }

        public void setTitleId(Id id) {
            this.titleId = id;
            modified();
        }

        public String getValue() {
            return value;
        }

        public void setTitle(String title) {
            this.title = title;
            modified();
        }

        public void setValue(String value) {
            this.value = value;
            modified();
        }

        public static class Factory {

            public static Item newInstance() {
                return new Item();
            }

            public static Item newInstance(Item item) {
                return new Item(item);
            }
        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            ids.add(new MultilingualStringInfo(this) {

                @Override
                public Id getId() {
                    return titleId;
                }

                @Override
                public void updateId(Id newId) {
                    titleId = newId;
                }

                @Override
                public EAccess getAccess() {
                    AdsDefinition def = em.getOwnerDef();
                    return def == null ? EAccess.DEFAULT : def.getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return "Edit Mask List Item Title";
                }

                @Override
                public boolean isPublished() {
                    AdsDefinition def = em.getOwnerDef();
                    return def == null ? false : def.isPublished();
                }
            });
        }

        @Override
        public AdsMultilingualStringDef findLocalizedString(Id stringId) {
            if (em == null || em.getOwnerDef() == null) {
                return null;
            } else {
                return em.getOwnerDef().findLocalizedString(stringId);
            }
        }
    }
    private ArrayList<Item> items = null;

    EditMaskList(RadixObject context, boolean virtual) {
        super(context, virtual);
    }

    EditMaskList(RadixObject context, org.radixware.schemas.editmask.EditMaskList xDef, boolean virtual) {
        super(context, virtual);
        List<org.radixware.schemas.editmask.EditMaskList.Item> xItems = xDef.getItemList();
        if (xItems != null && !xItems.isEmpty()) {
            items = new ArrayList<Item>();
            for (org.radixware.schemas.editmask.EditMaskList.Item xItem : xItems) {
                Item item = new Item();
                item.title = xItem.getTitle();
                item.titleId = xItem.getTitleId();
                item.value = xItem.getValue();
                item.em = this;
                items.add(item);
            }
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskList x = xDef.addNewList();
        if (items != null) {
            for (Item item : items) {
                org.radixware.schemas.editmask.EditMaskList.Item xItem = x.addNewItem();
                xItem.setTitle(item.title);
                xItem.setTitleId(item.titleId);
                xItem.setValue(item.value);
            }
        }
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return true;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.LIST;
    }

    public List<Item> getItems() {
        if (items == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Item>(items);
        }
    }

    public void clearItems() {
        if (items != null) {
            for (Item item : items) {
                item.em = null;
            }
        }
        this.items = null;
        modified();
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList<Item>();
        }
        if (!items.contains(item)) {
            items.add(item);
            item.em = this;
            modified();
        }
    }

    public void removeItem(Item item) {
        if (items != null) {
            items.remove(item);
            item.em = null;
            modified();
        }
    }

    @Override
    public boolean isDbRestrictionsAvailable() {
        return false;
    }

    @Override
    public void applyDbRestrictions() {
        //ignore
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        if (items != null) {
            for (Item item : items) {
                item.collectUsedMlStringIds(ids);
            }
        }
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (items != null) {
            for (Item item : items) {
                AdsMultilingualStringDef def = item.findLocalizedString(stringId);
                if (def != null) {
                    return def;
                }
            }
        }
        return null;
    }
}
