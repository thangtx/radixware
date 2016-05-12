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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.schemas.adsdef.EnumItemDefinition;


class OverrideAdsItem extends OverrideItem {

    public OverrideAdsItem(AdsEnumItemDef source) {
        super(source.getId(), source.getName(), source.getValue());
    }

    public OverrideAdsItem(EnumItemDefinition xDef) {
        super(xDef);
    }

    @Override
    public AdsEnumItemDef findSourceItem() {
        AdsEnumDef ovr = getOwnerEnum().getHierarchy().findOverwritten().get();
        if (ovr != null) {
            return ovr.getItems().findById(getId(), EScope.ALL).get();
        } else {
            return null;
        }
    }
}