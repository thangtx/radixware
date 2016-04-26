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

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.schemas.adsdef.ClassDefinition;


public abstract class EntityBasedPresentations extends ClassPresentations {
    
    protected EntityBasedPresentations(AdsClassDef ownerClass, ClassDefinition.Presentations xDef) {
        super(ownerClass, xDef);
    }

    protected EntityBasedPresentations(AdsClassDef ownerClass) {
        super(ownerClass);
    }

    @Override
    public AdsEntityBasedClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEntityBasedClassDef) {
                return (AdsEntityBasedClassDef) owner;
            }
        }
        return null;
    }

    @Override
    public void appendTo(ClassDefinition.Presentations xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }
}
