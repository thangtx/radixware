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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class AdsWrapperPropertyDef extends AdsPropertyDef {

    public AdsWrapperPropertyDef(AbstractPropertyDefinition xDef) {
        super(xDef);
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return null;
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.DYNAMIC;
    }
}
