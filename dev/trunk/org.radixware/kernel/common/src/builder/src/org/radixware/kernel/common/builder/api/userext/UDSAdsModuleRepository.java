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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author akrylov
 */
public class UDSAdsModuleRepository extends FSRepositoryAdsModule {

    private final String name;
    private final UDSAdsSegmentRepository segmentRepo;
    private volatile boolean loaded = false;
    private volatile boolean imagesLoaded = false;
    final UDSDefCustomLoader loader;
    private final Id id;

    public UDSAdsModuleRepository(UDSAdsSegmentRepository segmentRepo, String name, Id id, File moduleDir, UDSDefCustomLoader loader) {
        super(moduleDir);
        this.segmentRepo = segmentRepo;
        this.loader = loader;
        this.name = name;
        this.id = id;
    }

    public UDSAdsModuleRepository(UDSAdsSegmentRepository segmentRepo, String name, AdsModule module, UDSDefCustomLoader loader) {
        super(module);
        this.segmentRepo = segmentRepo;
        this.loader = loader;
        this.name = name;
        this.id = module.getId();
    }

    @Override
    public String getName() {
        return name;
    }

    public Id getId() {
        return id;
    }

    @Override
    public boolean containsDefinition(Id id) {
        listDefinitions();
        return super.containsDefinition(id);
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
        synchronized (this) {
            if (!loaded) {
                try {
                    IRepositoryAdsDefinition[] definitions = loader.listDefinitions(this);
                    if (definitions != null) {
                        Map<Definition, FSRepositoryAdsDefinition> map = new HashMap<>();
                        for (IRepositoryAdsDefinition repo : definitions) {
                            if (repo instanceof FSRepositoryAdsDefinition) {
                                FSRepositoryAdsDefinition fsRepo = (FSRepositoryAdsDefinition) repo;
                                map.put(fsRepo.getPreLoadedDefinition(), fsRepo);
                            }
                        }
                        register(map);
                        return definitions;
                    } else {
                        return new IRepositoryAdsDefinition[0];
                    }
                } finally {
                    loaded = true;
                }
            }
        }

        return new IRepositoryAdsDefinition[0];//super.listDefinitions();
    }

    @Override
    public IRepositoryAdsImageDefinition[] listImages() {
        synchronized (this) {
            if (!imagesLoaded) {
                try {
                    IRepositoryAdsImageDefinition[] definitions = loader.listImages(this);
                    if (definitions != null) {
                        Map<Definition, FSRepositoryAdsDefinition> map = new HashMap<>();
                        for (IRepositoryAdsDefinition repo : definitions) {
                            if (repo instanceof FSRepositoryAdsImageDefinition) {
                                FSRepositoryAdsImageDefinition fsRepo = (FSRepositoryAdsImageDefinition) repo;
                                map.put(fsRepo.getPreLoadedDefinition(), fsRepo);
                            }
                        }
                        register(map);

                        return definitions;
                    }
                } finally {
                    imagesLoaded = true;
                }
            }
        }
        return new IRepositoryAdsImageDefinition[0];
    }

    void reload() {
        synchronized (this) {
            if (loaded) {
                loaded = false;
            }
        }
    }

}
