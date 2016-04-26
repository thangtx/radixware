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

package org.radixware.kernel.common.defs.dds.providers;

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.dds.DdsSegment;

/**
 * Abstract base class for visitor providers on DDS.
 */
public abstract class DdsVisitorProvider extends VisitorProvider {

    protected DdsVisitorProvider() {
    }

    @Override
    public boolean isContainer(RadixObject object) {
        if (object instanceof Segment) {
            return object instanceof DdsSegment;
        }
        if (object instanceof Module) {
            return object instanceof DdsModule;
        }
        return true;
    }
}
