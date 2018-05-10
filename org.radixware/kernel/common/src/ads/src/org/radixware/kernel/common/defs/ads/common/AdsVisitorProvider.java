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

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.ITopContainer;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;

/**
 * Abstract base class for visitor providers on ads segment
 */
public abstract class AdsVisitorProvider extends VisitorProvider {

    public static abstract class AdsTopLevelDefVisitorProvider extends AdsVisitorProvider {

        @Override
        public boolean isContainer(RadixObject object) {
            if (object instanceof ITopContainer) {
                return true;
            }if (object instanceof Module) {
                return object instanceof AdsModule;
            } else if (object instanceof Segment) {
                return object instanceof AdsSegment;
            } else if (object instanceof Layer) {
                return true;
            } else if (object instanceof Branch) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean isContainer(RadixObject object) {
        if (object instanceof Segment) {
            return object instanceof AdsSegment;
        }
        if (object instanceof Module) {
            return object instanceof AdsModule;
        }
        return super.isContainer(object);
    }
}
