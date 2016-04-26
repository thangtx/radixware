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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.schemas.adsdef.EnumItemDefinition;


abstract class OverrideItem extends AdsEnumItemDef {

    public OverrideItem(Id id, String name, ValAsStr value) {
        super(id, name, value);
    }

    public OverrideItem(String name) {
        super(name);
    }

    public OverrideItem(EnumItemDefinition xDef) {
        super(xDef);
    }

    @Override
    public boolean setValue(ValAsStr value) {
        throw new DefinitionError("Value of external item can not be changed.", this);
    }

    @Override
    public EnumSet<EditPossibility> getEditPossibility() {
        return EnumSet.of(EditPossibility.TITLE, EditPossibility.DEPRECATION, EditPossibility.ICON, EditPossibility.DOMAIN);
    }

    @Override
    public String getPlatformItemName() {
        final AdsEnumItemDef item = findSourceItem();
        if (item != null) {
            return item.getPlatformItemName();
        } else {
            return null;
        }
    }

    @Override
    public boolean isOverwrite() {
        return true;
    }
}
