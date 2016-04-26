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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.types.Id;


final class SqmlEnumItemImpl extends SqmlDefinitionImpl implements ISqmlEnumItem {

    private final AdsEnumItemDef enumItem;

    public SqmlEnumItemImpl(final IClientEnvironment environment, final AdsEnumItemDef enumItemDef) {
        super(environment, enumItemDef);
        enumItem = enumItemDef;
    }

    @Override
    public String getTitle() {
        if (enumItem.getTitleId() == null) {
            return enumItem.getName();
        }
        return checkTitle(enumItem.getTitle(environment.getApplication()));
    }

    @Override
    public Id getOwnerEnumId() {
        return enumItem.getOwnerEnum().getId();
    }

    @Override
    public String getValueAsString() {
        return enumItem.getValue().toString();
    }
}
