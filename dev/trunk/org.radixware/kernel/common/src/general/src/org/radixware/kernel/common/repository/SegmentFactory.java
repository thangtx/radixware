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

package org.radixware.kernel.common.repository;

import java.util.Iterator;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.services.RadixServiceRegistry;
import org.radixware.kernel.common.utils.Utils;


public abstract class SegmentFactory {

    public abstract Segment newSegment(Layer layer);

    public abstract ERepositorySegmentType getType();

    public static SegmentFactory getDefault(ERepositorySegmentType type) {
        final Iterator<SegmentFactory> segmentFactories = RadixServiceRegistry.getDefault().iterator(SegmentFactory.class);
        while (segmentFactories.hasNext()) {
            final SegmentFactory candidate = segmentFactories.next();
            if (Utils.equals(type, candidate.getType())) {
                return candidate;
            }
        }
        if (type == ERepositorySegmentType.KERNEL) {
            return null;
        }
        if (type == ERepositorySegmentType.UDS) {
            return null;
        }
        throw new RadixError("Segment factory not found for " + String.valueOf(type));
    }
}