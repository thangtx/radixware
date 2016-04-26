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

/*
 * 9/16/11 3:00 PM
 */
package org.radixware.kernel.common.builder.check.ads.clazz;

import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;


@RadixObjectCheckerRegistration
public class AdsEnumClassChecker extends AdsClassChecker<AdsEnumClassDef> {

    public AdsEnumClassChecker() {
        super();
    }

    @Override
    public Class<AdsEnumClassDef> getSupportedClass() {
        return AdsEnumClassDef.class;
    }

    @Override
    public void check(AdsEnumClassDef def, IProblemHandler problemHandler) {
        super.check(def, problemHandler);
    }
}
