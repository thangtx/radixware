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
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations;


abstract class ExtendableEntityPresentations<T extends AdsDefinition & IAdsClassMember> extends ExtendablePresentations<T> {

//    protected ExtendableEntityPresentations(IAdsClassMember owner, ClassDefinition.Presentations xDef) {
//        super(owner, xDef);
//    }
    protected ExtendableEntityPresentations(EntityObjectPresentations owner, ClassDefinition.Presentations xDef) {
        super(owner, xDef);
    }

    protected ExtendableEntityPresentations(ClassPresentations owner, AdsDefinitions<T> locals, Presentations xDef) {
        super(owner, locals, xDef);
    }

    protected abstract ExtendableDefinitions<T> findInstance(EntityObjectPresentations prs);

    @Override
    protected ExtendableDefinitions<T> findInstance(ClassPresentations prs) {
        if (prs instanceof EntityObjectPresentations) {
            return findInstance((EntityObjectPresentations) prs);
        } else {
            return null;
        }
    }

    abstract void appendTo(ClassDefinition.Presentations xDef, ESaveMode saveMode);

    protected boolean isSaveToAPI(AdsClassDef clazz, T cs) {
        if (cs.isPublished()) {
            switch (cs.getAccessMode()) {
                case PROTECTED:                    
                case PUBLIC:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
