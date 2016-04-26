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

package org.radixware.kernel.common.defs.uds;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.types.Id;


public class UdsSearcher {

    public static final class Factory {

        private Factory() {
        }

        public static DefinitionSearcher<AdsClassDef> newAdsClassSearcher(Definition context) {
            return new DefinitionSearcher<AdsClassDef>(context) {

                @Override
                public DefinitionSearcher<AdsClassDef> findSearcher(Module module) {
                    if (module instanceof AdsModule) {
                        return AdsSearcher.Factory.newAdsClassSearcher((AdsModule) module);
                    } else {
                        return null;
                    }
                }

                @Override
                public AdsClassDef findInsideById(Id id) {
                    return null;
                }
            };
        }
    }
}
