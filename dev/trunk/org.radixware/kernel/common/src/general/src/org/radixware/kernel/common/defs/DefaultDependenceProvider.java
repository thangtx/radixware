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

package org.radixware.kernel.common.defs;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.types.Id;

/**
 * Default dependence provider for definition. Taken into account dependencies
 * of definition module, dependencies of overwritten modules, ignored
 * overwrittens in current layer, etc.
 *
 */
public class DefaultDependenceProvider implements IDependenceProvider {

    private final Definition context;

    private class MapLink extends ObjectLink<Map<Id, Dependence>> {

        @Override
        protected Map<Id, Dependence> search() {
            Map<Id, Dependence> map = new HashMap<>();
            collect(map);
            return map;
        }

        public Map<Id, Dependence> get() {
            Map<Id, Dependence> map = find();
            if (map == null) {
                map = update();
            }
            return map;

        }
    }

    protected DefaultDependenceProvider(final Definition context) {
        this.context = context;
    }

    @Override
    public void collect(final Map<Id, Dependence> moduleId2Dependence) {
        Module module = context.getModule();

        while (module != null) {
            for (Dependence dep : module.getDependences()) {
                moduleId2Dependence.put(dep.getDependenceModuleId(), dep);
            }
            module = module.findOverwritten();
        }
    }
    private final MapLink link = new MapLink();

    @Override
    public Map<Id, Dependence> get() {
        return link.get();
    }
}
