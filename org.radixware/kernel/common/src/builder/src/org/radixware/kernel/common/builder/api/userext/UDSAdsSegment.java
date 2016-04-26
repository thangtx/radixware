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
package org.radixware.kernel.common.builder.api.userext;

import org.radixware.kernel.common.defs.Module.Factory;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;

public class UDSAdsSegment extends AdsSegment {

    public UDSAdsSegment(final Layer layer) {
        super(layer);

    }

    private UDSDefCustomLoader getLoader() {
        return ((UDSLayer) getLayer()).loader;
    }

    @Override
    protected synchronized Factory<AdsModule> getModuleFactory() {
        return new Factory<AdsModule>() {

            @Override
            public AdsModule newInstance(final Id moduleId, final String moduleName) {
                UDSAdsModule module = new UDSAdsModule(moduleId, moduleName, getLoader());
                return module;
            }
        };
    }

}
