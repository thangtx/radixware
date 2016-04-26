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

package org.radixware.kernel.common.repository.uds.fs;

import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.FSRepositorySegment;
import org.radixware.kernel.common.repository.fs.FSRepositorySegmentFactory;


public class FSRepositoryUdsSegmentFactory extends FSRepositorySegmentFactory<AdsModule> {

    @Override
    public FSRepositorySegment<AdsModule> newInstance(Segment<AdsModule> segment) {
        return new FSRepositoryUdsSegment(segment);
    }

    @Override
    public ERepositorySegmentType getType() {
        return ERepositorySegmentType.UDS;
    }
}
