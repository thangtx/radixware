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

import org.radixware.kernel.common.defs.IDependenceProvider;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.ClassCodeLocalDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;


public abstract class AdsClassMember extends AdsDefinition implements IAdsClassMember {

    private int apiUsageVariant;

    protected AdsClassMember(Id id, String name) {
        super(id, name);
    }

    public int getApiUsageVariant() {
        return apiUsageVariant;
    }

    public void setApiUsageVariant(int apiUsageVariant) {
        this.apiUsageVariant = apiUsageVariant;
        setEditState(EEditState.MODIFIED);
    }

    protected AdsClassMember(DescribedAdsDefinition xDef) {
        super(xDef);
        apiUsageVariant = xDef.isSetAPIUsageKind() ? xDef.getAPIUsageKind() : 0;
    }

    @Override
    public AdsClassDef getOwnerClass() {
        return RadixObjectsUtils.findContainer(getContainer(), AdsClassDef.class);
    }

    protected AdsClassMember findBaseMember() {
        @SuppressWarnings("unchecked")
        Hierarchy<AdsClassMember> h = getHierarchy();
        AdsClassMember base = h.findOverwritten().get();
        if (base == null) {
            return h.findOverridden().get();
        } else {
            return base;
        }
    }

    @Override
    public IDependenceProvider getDependenceProvider() {
        AdsClassDef clazz = getOwnerClass();
        if (clazz != null) {
            return clazz.getDependenceProvider();
        } else {
            return super.getDependenceProvider();
        }
    }

    @Override
    public void appendTo(DescribedAdsDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (apiUsageVariant != 0) {
            xDef.setAPIUsageKind(apiUsageVariant);
        }
    }

    @Override
    public boolean canDelete() {
        if (super.canDelete()) {
            return true;
        }
        RadixObject container = getContainer();
        if (container instanceof ClassCodeLocalDefinitions) {
            ClassCodeLocalDefinitions locals = (ClassCodeLocalDefinitions) container;
            if (locals.isSuperReadOnly()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
