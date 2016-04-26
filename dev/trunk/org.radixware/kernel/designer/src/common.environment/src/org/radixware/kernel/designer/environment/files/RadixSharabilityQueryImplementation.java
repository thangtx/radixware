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

import org.radixware.kernel.designer.common.general.filesystem.RadixFileInfo;
import java.io.File;
import org.netbeans.api.queries.SharabilityQuery;
import org.netbeans.spi.queries.SharabilityQueryImplementation;

/**
 * Filter for SVN.
 * @deprecated replaced by {@link RadixSharabilityQueryImplementation2}
 */
@Deprecated
public class RadixSharabilityQueryImplementation implements SharabilityQueryImplementation {

    public RadixSharabilityQueryImplementation() {
    }

    @Override
    public int getSharability(File file) {
        if (RadixFileInfo.isShareable(file)) {
            if (file.isDirectory()) {
                return SharabilityQuery.MIXED;
            } else {
                return SharabilityQuery.SHARABLE;
            }
        } else {
            return SharabilityQuery.NOT_SHARABLE;
        }
    }
}
