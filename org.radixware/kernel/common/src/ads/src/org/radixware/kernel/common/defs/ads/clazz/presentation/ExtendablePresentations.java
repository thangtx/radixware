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

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;

import org.radixware.schemas.adsdef.ClassDefinition;


abstract class ExtendablePresentations<T extends AdsDefinition & IAdsClassMember> extends ExtendableMembers<T> implements IAdsClassMember {

    protected static class ExtendablePresentationsLocal<T extends AdsDefinition> extends AdsDefinitions<T> {

        protected ExtendablePresentationsLocal() {
            super();
        }
    }

    protected ExtendablePresentations(ClassPresentations owner, ClassDefinition.Presentations xDef) {
        this(owner, new ExtendablePresentationsLocal<T>(), xDef);
    }

    protected ExtendablePresentations(ClassPresentations owner, AdsDefinitions<T> locals, ClassDefinition.Presentations xDef) {
        super(owner, locals);
        if (xDef != null) {
            loadFrom(xDef);
        }
    }

    protected abstract void loadFrom(ClassDefinition.Presentations xDef);

    protected abstract ExtendableDefinitions<T> findInstance(ClassPresentations prs);

    ClassPresentations getOwnerClassPresentations() {
        return (ClassPresentations) getContainer();
    }

    @Override
    public AdsClassDef getOwnerClass() {
        return getOwnerClassPresentations().getOwnerClass();
    }

    @Override
    public ExtendableDefinitions<T> findInstance(AdsDefinition clazz) {
        if (clazz instanceof IAdsPresentableClass) {
            ClassPresentations cp = ((IAdsPresentableClass) clazz).getPresentations();
            if (cp != null) {
                return findInstance(cp);
            }
        }
        return null;
    }
}
