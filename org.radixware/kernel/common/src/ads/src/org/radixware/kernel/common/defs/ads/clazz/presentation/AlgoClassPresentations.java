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

import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AlgoClassPresentations extends ClassPresentations {

    public AlgoClassPresentations(AdsAlgoClassDef ownerClass, ClassDefinition.Presentations xDef) {
        super(ownerClass, xDef);
    }

    public AlgoClassPresentations(AdsAlgoClassDef ownerClass) {
        super(ownerClass);
    }
    
}
