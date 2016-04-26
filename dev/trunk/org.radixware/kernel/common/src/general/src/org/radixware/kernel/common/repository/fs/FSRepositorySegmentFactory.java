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

package org.radixware.kernel.common.repository.fs;

import java.util.Iterator;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.services.RadixServiceRegistry;
import org.radixware.kernel.common.utils.Utils;


public abstract class FSRepositorySegmentFactory<T extends Module> {

    public abstract FSRepositorySegment<T> newInstance(Segment<T> segment);

    public abstract ERepositorySegmentType getType();

    private static <T extends Module> FSRepositorySegmentFactory<T> getFactory(Segment<T> segment) {
        final Iterator<FSRepositorySegmentFactory> segmentFactories = RadixServiceRegistry.getDefault().iterator(FSRepositorySegmentFactory.class);
        while (segmentFactories.hasNext()) {
            final FSRepositorySegmentFactory candidate = segmentFactories.next();
            if (Utils.equals(segment.getType(), candidate.getType())) {
                return candidate;
            }
        }
        throw new RadixError("Segment repository factory not found for " + String.valueOf(segment.getType()));
    }

    static <T extends Module> FSRepositorySegment<T> createInstance(Segment<T> segment) {
        FSRepositorySegmentFactory<T> f = getFactory(segment);
        return f.newInstance(segment);
    }
}
