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

package org.radixware.kernel.common.repository.uds;

import org.radixware.kernel.common.defs.Module.Factory;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;


public class UdsSegment extends AdsSegment {

    public UdsSegment(Layer ownerLayer) {
        super(ownerLayer);
    }

    @Override
    protected Factory<AdsModule> getModuleFactory() {
        return new Factory<AdsModule>() {

            @Override
            public AdsModule newInstance(Id moduleId, String moduleName) {
                return new UdsModule(moduleId, moduleName);
            }
        };
    }

    @Override
    public ERepositorySegmentType getType() {
        return ERepositorySegmentType.UDS;
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.SEGMENT;
    }
}
