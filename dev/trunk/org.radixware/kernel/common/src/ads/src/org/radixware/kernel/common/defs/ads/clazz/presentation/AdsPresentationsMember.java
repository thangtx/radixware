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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassTitledMember;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.TitledAdsDefinition;


public abstract class AdsPresentationsMember extends AdsClassTitledMember {

    protected AdsPresentationsMember(Id id, String name, Id titleId) {
        super(id, name, titleId);
    }

    protected AdsPresentationsMember(TitledAdsDefinition xDef) {
        super(xDef);
    }

    protected AdsPresentationsMember(DescribedAdsDefinition xDef) {
        super(xDef);
    }

    ClassPresentations getOwnerPresentations() {
        RadixObject container = getContainer();
        while (container != null) {
            if (container instanceof ClassPresentations) {
                return (ClassPresentations) container;
            } else if (container instanceof ExtendablePresentations) {
                return (ClassPresentations) container.getContainer();
            }
            container = container.getContainer();
        }
        return null;
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }
}
