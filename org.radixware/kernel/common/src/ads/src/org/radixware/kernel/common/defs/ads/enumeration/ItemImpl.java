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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;


class ItemImpl extends AdsEnumItemDef {

    public ItemImpl(String name) {
        super(name);
    }

    public ItemImpl(EnumItemDefinition xDef) {
        super(xDef);
    }

    public ItemImpl(OverridePlatformItem source) {
        super(source.getId(), source.getName(), source.getValue());
        this.setTitleId(source.getTitleId());
        this.setIconId(source.getIconId());
    }

    @Override
    public AdsEnumItemDef findSourceItem() {
        return null;
    }

    @Override
    public boolean isOverwrite() {
        return isOverwrite;
    }

    @Override
    public boolean setOverwrite(boolean flag) {
        if (isOverwrite != flag) {
            isOverwrite = flag;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }
}
