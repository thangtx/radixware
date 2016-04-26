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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.HashMap;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.repository.ads.AdsSegment;


class PlatformClassCache {

    public final HashMap<String, RadixPlatformClass> platformClasses = new HashMap<String, RadixPlatformClass>();

    public RadixPlatformClass findPlatformClass(AdsDefinition context, String name) {
        RadixPlatformClass result = platformClasses.get(name);
        if (result == null) {
            result = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(context.getUsageEnvironment()).findPlatformClass(name);
            if (result != null) {
                platformClasses.put(name, result);
            } 
        }
        return result;
    }
}
