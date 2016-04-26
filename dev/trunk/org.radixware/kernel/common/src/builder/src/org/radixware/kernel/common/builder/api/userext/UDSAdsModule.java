/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.api.userext;

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author akrylov
 */
public class UDSAdsModule extends AdsModule {

    private final UDSDefCustomLoader loader;
    private boolean dependecesUpdated = false;

    public UDSAdsModule(Id id, String name, UDSDefCustomLoader loader) {
        super(id, name);
        this.loader = loader;

    }

    @Override
    protected ModuleDefinitions createDefinitinosList() {
        return new UDSADSModuleDefinitions(this, true);
    }

    private class UDSADSModuleDefinitions extends ModuleDefinitions {

        public UDSADSModuleDefinitions(AdsModule owner, boolean upload) {
            super(owner, upload);
        }

        @Override
        public AdsDefinition findById(Id id) {
            updateDependencesIfNeeded();
            final AdsDefinition def = super.findById(id);
            if (def != null) {
                return def;
            } else {
                if (loader.loadDefinitionToModuleContext(UDSAdsModule.this)) {
                    return super.findById(id);
                } else {
                    return null;
                }
            }
        }
    }

    private void updateDependencesIfNeeded() {
        synchronized (this) {
            if (!dependecesUpdated) {
                updateDependences(getLayer());
                dependecesUpdated = true;
            }
        }
    }

    private void updateDependences(final Layer layer) {

        if (layer != null && getDependences().getModuleIds().isEmpty()) {

            final Layer.HierarchyWalker walker = new Layer.HierarchyWalker();
            walker.go(layer, new Layer.HierarchyWalker.Acceptor<AdsModule>() {

                @Override
                public void accept(final org.radixware.kernel.common.defs.HierarchyWalker.Controller<AdsModule> controller, final Layer radixObject) {
                    if (radixObject != layer) {
                        for (Module m : radixObject.getAds().getModules()) {
                            getDependences().add(m);
                        }
                        for (Module m : radixObject.getDds().getModules()) {
                            getDependences().add(m);
                        }
                    }
                }
            });
        }
    }
}
