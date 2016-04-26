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

package org.radixware.kernel.license;

import java.util.Iterator;
import java.util.ServiceLoader;

public class LicenseCodeGenerator {

    public static ILicenseCodeGenerator createInstance() {
        final ServiceLoader<ILicenseCodeGenerator> loader = ServiceLoader.load(ILicenseCodeGenerator.class, LicenseCodeGenerator.class.getClassLoader());
        final Iterator<ILicenseCodeGenerator> iterator = loader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

}
