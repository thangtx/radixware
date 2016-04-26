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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsSegment;
import org.radixware.kernel.common.repository.ads.fs.IKernelLookup;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.Module;
import org.radixware.schemas.product.ModuleDocument;

public class UDSAdsSegmentRepository extends FSRepositoryAdsSegment {

    private Map<Id, FSRepositoryAdsModule> modules = new HashMap<>();
    private final UDSLayerRepository layer;
    private boolean loaded = false;

    public UDSAdsSegmentRepository(UDSLayerRepository layer, UDSAdsSegment segment) {
        super(segment);
        this.layer = layer;
    }

    @Override
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public IRepositoryModule<AdsModule>[] listModules() {
        synchronized (this) {
            if (!loaded) {
                try {
                    modules = new HashMap<>();
                    for (Id id : layer.loader.listModuleIds()) {
                        try {
                            registerModule(id, createModuleRepository(id, id.toString(), null), loaded);
                        } catch (IOException ex) {
                            //Ignore
                        }
                    }
                } finally {
                    loaded = true;
                }
            }
        }
        return modules.values().toArray(new IRepositoryModule[modules.size()]);
    }

    private FSRepositoryAdsModule createModuleRepository(Id id, String name, String description) throws IOException {
        File preparedDir = new File(getDirectory(), id.toString());
        preparedDir.mkdirs();
        if (!preparedDir.exists()) {
            throw new IOException("Unable to create directory for module");
        }
        ModuleDocument xModuleDoc = null;
        Module xModule = null;
        if (description != null) {
            try {
                xModuleDoc = ModuleDocument.Factory.parse(description);
                xModule = xModuleDoc.getModule();
            } catch (XmlException ex) {
            }
        }

        if (xModule == null) {
            xModuleDoc = ModuleDocument.Factory.newInstance();
            xModule = xModuleDoc.addNewModule();
        }
        xModule.setId(id.toString());
        xModule.setName(name);

        XmlUtils.saveXmlPretty(xModuleDoc, new File(preparedDir, org.radixware.kernel.common.defs.Module.MODULE_XML_FILE_NAME));

        return new UDSAdsModuleRepository(this, name, id, preparedDir, layer.loader);
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.ADS;
    }

    @Override
    public IKernelLookup getKernelLookup() {
        return null;
    }

    @Override
    public IRepositoryModule<AdsModule> getModuleRepository(AdsModule module) {
        listModules();
        FSRepositoryAdsModule repository = modules.get(module.getId());
        if (repository != null) {
            return repository;
        } else {
            if (module instanceof UDSAdsModule) {
                repository = new UDSAdsModuleRepository(this, module.getId().toString(), (UDSAdsModule) module, layer.loader);
                registerModule(module.getId(), repository, true);
                return repository;
            } else {
                return null;
            }
        }
    }

    public UDSLayerRepository getLayerRepository() {
        return layer;
    }

    private void registerModule(Id id, FSRepositoryAdsModule repository, boolean reportModule) {
        modules.put(id, repository);
//        if (reportModule) {
//            UserExtensionManager.getInstance().getUserReports().registerModule(repository);
//        }

    }
}
