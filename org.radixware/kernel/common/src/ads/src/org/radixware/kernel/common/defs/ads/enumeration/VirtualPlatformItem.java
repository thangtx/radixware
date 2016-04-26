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
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.EnumItemDefinition;


class VirtualPlatformItem extends AdsEnumItemDef {

    private String publishedName;

    public VirtualPlatformItem(RadixPlatformEnum.Item pItem) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CONST_ITEM), pItem.name, pItem.val);
        this.publishedName = pItem.name;
    }

    public VirtualPlatformItem(EnumItemDefinition xDef) {
        super(xDef);
        this.publishedName = xDef.getPublishedName();
    }

    VirtualPlatformItem(OverridePlatformItem source) {
        super(source.getId(), source.getName(), source.getValue());
        this.publishedName = source.getPlatformItemName();
    }

    @Override
    public AdsEnumItemDef findSourceItem() {
        return null;
    }

    @Override
    public AdsPlatformEnumDef getOwnerEnum() {
        return (AdsPlatformEnumDef) super.getOwnerEnum();
    }

    @Override
    public String getPlatformItemName() {
        return this.publishedName;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public EnumSet<EditPossibility> getEditPossibility() {
        return EnumSet.of(EditPossibility.OVERWRITE);
    }

    @Override
    public boolean isPlatformItemPublisher() {
        return true;
    }
}
