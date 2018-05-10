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

package org.radixware.kernel.common.builder.check.ads.algo;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsAlgoClassParamChecker extends AdsDefinitionChecker<AdsAlgoClassDef.Param> {

    public AdsAlgoClassParamChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsAlgoClassDef.Param.class;
    }

    @Override
    public void check(AdsAlgoClassDef.Param param, IProblemHandler problemHandler) {
        super.check(param, problemHandler);
        param.getType().check(param, problemHandler, getHistory().getMap());
    }
}
