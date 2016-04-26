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

package org.radixware.kernel.common.defs.ads.clazz;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.MemberGroup;


public class PropertyGroups extends Members<AdsPropertyGroup> {

    PropertyGroups(AdsClassDef owner) {
        super(owner);
    }

    PropertyGroups(AdsClassDef owner, MemberGroup xDef) throws IOException {
        this(owner);
        if (xDef != null) {
            final List<MemberGroup> groups = xDef.getGroupList();
            if (groups != null && !groups.isEmpty()) {
                for (MemberGroup g : groups) {
                    super.add(AdsPropertyGroup.Factory.loadFrom(g));
                }
            }
        }
    }

    PropertyGroups(AdsPropertyGroup owner) {
        super(owner);
    }

    PropertyGroups(AdsPropertyGroup owner, MemberGroup xDef) {
        this(owner);
        if (xDef != null) {
            final List<MemberGroup> groups = xDef.getGroupList();
            if (groups != null && !groups.isEmpty()) {
                for (MemberGroup g : groups) {
                    super.add(AdsPropertyGroup.Factory.loadFrom(g));
                }
            }
        }
    }

    void appendTo(MemberGroup xDef, ESaveMode saveMode) {
        if (!isEmpty()) {
            for (AdsPropertyGroup g : this) {
                g.appendTo(xDef.addNewGroup(), saveMode);
            }
        }
    }

    void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        if (!isEmpty()) {
            MemberGroup propGroups = xDef.addNewPropertyGroup();
            for (AdsPropertyGroup g : this) {
                g.appendTo(propGroups.addNewGroup(), saveMode);
            }
        }
    }

    @Override
    AdsPropertyGroup getOwnerGroup() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsPropertyGroup) {
                return (AdsPropertyGroup) owner;
            }
        }
        return null;
    }

    @Override
    protected void onRemove(AdsPropertyGroup definition) {
        super.onRemove(definition);
        AdsPropertyGroup owner = getOwnerGroup();
        if (owner != null) {
            for (Id id : new ArrayList<Id>(definition.getMemberIds())) {
                AdsPropertyDef prop = getOwnerClass().getProperties().findById(id, EScope.ALL).get();
                if (prop != null) {
                    owner.addMember(prop);
                }
            }
        }
    }

    void fireOwnerClassChange() {
        for (AdsPropertyGroup pg : this) {
            pg.fireOwnerClassChange();
        }
    }
}
