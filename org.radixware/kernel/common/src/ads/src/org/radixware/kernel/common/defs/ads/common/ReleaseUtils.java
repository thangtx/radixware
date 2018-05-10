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

package org.radixware.kernel.common.defs.ads.common;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.ReleaseUtilsCommon;

public class ReleaseUtils extends ReleaseUtilsCommon {

    public static boolean isExpired(Branch branch, AdsAccessFlags flags) {
        return isExpired(branch.getLastReleaseVersion(), flags.getExpirationRelease());
    }
    
        /**
     * @param context - checked definition 
     * @param def - definition which may be expired
     * @param problemHandler
    */
    public static void checkExprationRelease(RadixObject context, Definition def, IProblemHandler problemHandler) {
        if (def instanceof IAccessible && problemHandler != null) {
            Branch b = def.getBranch();
            final AdsAccessFlags flag = ((IAccessible) def).getAccessFlags();
            if (ReleaseUtils.isExpired(b, flag)) {
                problemHandler.accept(RadixProblem.Factory.newError(context, "Referenced definition is expired since " + flag.getExpirationRelease()));
            }
        }
    }
}
