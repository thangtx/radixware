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

package org.radixware.kernel.common.builder.check.ads.mml;

import java.util.HashMap;
import org.radixware.kernel.common.builder.check.common.CheckHistory;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class MmlChecker extends RadixObjectChecker<Mml> {

    
    public static class MmlSupport extends HashMap<Object, Object> {

        public static MmlSupport initialize(CheckHistory history) {
            MmlSupport support = history.findItemByClass(MmlSupport.class);
            if (support == null) {
                support = new MmlSupport();
                history.registerItemByClass(support);
            }
            return support;
        }
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Mml.class;
    }

    @Override
    public void check(final Mml mml, final IProblemHandler problemHandler) {
        MmlSupport support = MmlSupport.initialize(getHistory());
        mml.check(problemHandler, support);
    }
}
