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

package org.radixware.kernel.designer.environment.files;

import java.io.File;
import java.net.URI;
import org.netbeans.api.queries.SharabilityQuery;
import org.netbeans.spi.queries.SharabilityQueryImplementation2;
import org.openide.util.Utilities;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileInfo;

/**
 * Filter for SVN.
 */
@org.openide.util.lookup.ServiceProvider(service=org.netbeans.spi.queries.SharabilityQueryImplementation2.class, position = 0)
public class RadixSharabilityQueryImplementation2 implements SharabilityQueryImplementation2 {

    public RadixSharabilityQueryImplementation2() {
    }

    @Override
    public SharabilityQuery.Sharability getSharability(URI uri) {
        final File file = Utilities.toFile(uri);
        if (RadixFileInfo.isShareable(file)) {
            if (file.isDirectory()) {
                return SharabilityQuery.Sharability.MIXED;
            } else {
                return SharabilityQuery.Sharability.SHARABLE;
            }
        } else {
            return SharabilityQuery.Sharability.NOT_SHARABLE;
        }
    }
}
