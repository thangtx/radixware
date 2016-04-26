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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


final class SqmlEnumDefImpl extends SqmlDefinitionImpl implements ISqmlEnumDef {

    private final AdsEnumDef enumDef;
    private final List<ISqmlEnumItem> enumItems = new ArrayList<ISqmlEnumItem>(64);

    public SqmlEnumDefImpl(final IClientEnvironment environment, final AdsEnumDef adsEnum) {
        super(environment, adsEnum);
        enumDef = adsEnum;
        final List<AdsEnumItemDef> items = enumDef.getItems().get(EScope.LOCAL_AND_OVERWRITE);
        for (AdsEnumItemDef item : items) {
            enumItems.add(new SqmlEnumItemImpl(environment, item));
        }
    }

    @Override
    public String getTitle() {
        if (enumDef.getTitleId() == null) {
            return enumDef.getName();
        }
        return checkTitle(enumDef.getTitle(environment.getLanguage()));
    }

    @Override
    public EValType getItemType() {
        return enumDef.getItemType();
    }

    @Override
    public Iterator<ISqmlEnumItem> iterator() {
        return enumItems.iterator();
    }

    @Override
    public int size() {
        return enumItems.size();
    }

    @Override
    public ISqmlEnumItem get(final int idx) {
        return enumItems.get(idx);
    }

    @Override
    public ISqmlEnumItem findItemById(final Id itemId) {
        for (ISqmlEnumItem item : enumItems) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public ISqmlEnumItem findItemByValue(final Object value) {
        final ValAsStr valAsStr = ValAsStr.Factory.newInstance(value, enumDef.getItemType());
        for (ISqmlEnumItem item : enumItems) {
            if (item.getValueAsString().equals(valAsStr.toString())) {
                return item;
            }
        }
        return null;
    }
}
