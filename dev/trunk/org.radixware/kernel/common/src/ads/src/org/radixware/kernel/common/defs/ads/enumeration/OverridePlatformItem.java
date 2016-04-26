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

package org.radixware.kernel.common.defs.ads.enumeration;

import java.util.EnumSet;
import org.radixware.schemas.adsdef.EnumItemDefinition;


class OverridePlatformItem extends OverrideItem {

    private String publishedName;

    public OverridePlatformItem(EnumItemDefinition xDef) {
        super(xDef);
        this.publishedName = xDef.getPublishedName();
    }

    public OverridePlatformItem(VirtualPlatformItem def) {
        super(def.getId(), def.getName(), def.getValue());
        this.publishedName = def.getPlatformItemName();
    }

   

    @Override
    public EnumSet<EditPossibility> getEditPossibility() {
        EnumSet<EditPossibility> set = super.getEditPossibility();
        set.add(EditPossibility.NAME);
        return set;
    }

    @Override
    public AdsPlatformEnumDef getOwnerEnum() {
        return (AdsPlatformEnumDef) super.getOwnerEnum();
    }

    @Override
    public AdsEnumItemDef findSourceItem() {
        return null;
    }

    @Override
    public String getPlatformItemName() {
        return this.publishedName;
    }

    @Override
    public boolean isPlatformItemPublisher() {
        return true;
    }

    @Override
    public boolean delete() {
        AdsEnumDef ownerEnum = getOwnerEnum();

        if (ownerEnum != null && super.delete()) {
            VirtualPlatformItem item = new VirtualPlatformItem(this);
            ownerEnum.getItems().getLocal().add(item);
            return true;
        } else {
            return false;
        }
    }
}
