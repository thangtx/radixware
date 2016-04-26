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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.types.Id;


public abstract class PropertyUsage extends RadixObject implements IAdsClassMember {

    protected Id propertyId;

    protected PropertyUsage(AdsPropertyDef prop) {
        this.propertyId = prop.getId();
    }

    protected PropertyUsage(Id propId) {
        this.propertyId = propId;
    }

    public Id getPropertyId() {
        return this.propertyId;
    }

    public void setPropertyId(Id propertyId) {
        this.propertyId = propertyId;
    }

    public AdsPropertyDef findProperty() {
        AdsClassDef clazz = getOwnerClass();
        return  clazz == null ? null :clazz.getProperties().findById(propertyId, EScope.ALL).get();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsPropertyDef prop = findProperty();
        if (prop != null) {
            list.add(prop);
        }
    }
}
