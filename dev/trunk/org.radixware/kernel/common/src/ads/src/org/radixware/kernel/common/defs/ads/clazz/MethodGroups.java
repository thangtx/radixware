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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.MemberGroup;


public class MethodGroups extends Members<AdsMethodGroup> {

//    MethodGroups(AdsClassDef owner) {
//        super(owner);
//    }
//
//    MethodGroups(AdsClassDef owner, MemberGroup xDef) throws IOException {
//        this(owner);
//        if(xDef != null){
//            List<MemberGroup> groups = xDef.getGroupList();
//            if (groups != null && !groups.isEmpty()) {
//                for (MemberGroup g : groups) {
//                    super.add(AdsMethodGroup.Factory.loadFrom(g));
//                }
//            }
//        }
//    }
    MethodGroups(AdsMethodGroup owner) {
        super(owner);
    }

    MethodGroups(AdsMethodGroup owner, MemberGroup xDef) {
        this(owner);
        if (xDef != null) {
            final List<MemberGroup> groups = xDef.getGroupList();
            if (groups != null && !groups.isEmpty()) {
                for (MemberGroup g : groups) {
                    super.add(AdsMethodGroup.Factory.loadFrom(g));
                }
            }
        }
    }

    protected void appendTo(MemberGroup xDef, ESaveMode saveMode) {
        if (!isEmpty()) {
            for (AdsMethodGroup g : this) {
                g.appendTo(xDef.addNewGroup(), saveMode);
            }
        }
    }

    protected void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        if (!isEmpty()) {
            final MemberGroup root = xDef.addNewMethodGroup();
            for (AdsMethodGroup g : this) {
                g.appendTo(root.addNewGroup(), saveMode);
            }
        }
    }

    @Override
    protected AdsMethodGroup getOwnerGroup() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsMethodGroup) {
                return (AdsMethodGroup) owner;
            }
        }
        return null;
    }

    @Override
    protected void onRemove(AdsMethodGroup definition) {
        super.onRemove(definition);
        final AdsMethodGroup owner = getOwnerGroup();
        if (owner != null) {
            for (final Id id : new ArrayList<Id>(definition.getMemberIds())) {
                final AdsMethodDef method = getOwnerClass().getMethods().findById(id, EScope.ALL).get();
                if (method != null) {
                    owner.addMember(method);
                }
            }
        }
    }

    protected void fireOwnerClassChange() {
        for (final AdsMethodGroup mg : this) {
            mg.fireOwnerClassChange();
        }
    }
}
