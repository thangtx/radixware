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

package org.radixware.kernel.common.userreport.extrepository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.userreport.common.IUserReportRequestExecutor;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsSegment;
import org.radixware.kernel.common.repository.ads.fs.IKernelLookup;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.ReportsModuleRepository;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModule;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModuleRepository;
import org.radixware.kernel.common.userreport.repository.role.RolesModule;
import org.radixware.kernel.common.userreport.repository.role.RolesModuleRepository;
import org.radixware.schemas.product.Module;
import org.radixware.schemas.product.ModuleDocument;


public class UserExtADSSegmentRepository extends FSRepositoryAdsSegment {

    private Map<Id, FSRepositoryAdsModule> modules = new HashMap<>();
    private final UserExtLayerRepository layer;
    private boolean loaded = false;
    private static int untitledModulesSequence = 0;

    public UserExtADSSegmentRepository(UserExtLayerRepository layer, UserExtAdsSegment segment) {
        super(segment);
        this.layer = layer;
    }       

    @Override
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public IRepositoryModule<AdsModule>[] listModules() {
        synchronized (this) {
            if (!loaded) {
                modules = new HashMap<>();
                try {
                    registerModule(RolesModule.MODULE_ID, createModuleRepository(RolesModule.MODULE_ID, RolesModuleRepository.NAME, null), false);
                    registerModule(MsdlSchemesModule.MODULE_ID, createModuleRepository(MsdlSchemesModule.MODULE_ID, MsdlSchemesModuleRepository.NAME, null), false);
                } catch (IOException ex) {
                    //Exceptions.printStackTrace(ex);
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);                    
                }
                List<IUserReportRequestExecutor.ModuleInfo> moduleInfos=UserExtensionManagerCommon.getInstance().getUFRequestExecutor().listModules();
                for(IUserReportRequestExecutor.ModuleInfo moduleInfo:moduleInfos){
                   try {
                       registerModule(moduleInfo.getModuleId(), createModuleRepository(moduleInfo.getModuleId(), moduleInfo.getModuleName(), moduleInfo.getModuleDescription()), true);
                   } catch (IOException e) {
                       UserExtensionManagerCommon.getInstance().getEnvironment().processException(e);
                   } 
                }                
                loaded = true;
            }
        }
        return modules.values().toArray(new IRepositoryModule[modules.size()]);
    }
    

    public void reload() {
        synchronized (this) {
            loaded = false;
            if (modules != null) {
                for (IRepositoryModule module : modules.values()) {
                    if (module instanceof RolesModuleRepository) {
                        continue;
                    }
                    File dir = module.getDirectory();
                    if (dir != null && dir.exists()) {
                        FileUtils.deleteDirectory(dir);
                    }
                }
                modules.clear();
                modules = null;

            }
            IRepositoryModule[] modules = listModules();
            for (IRepositoryModule rep : modules) {
                if (!(rep instanceof RolesModuleRepository) && !(rep instanceof MsdlSchemesModuleRepository)) {
                    try {
                        UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().addFromRepository(rep);
                    } catch (IOException ex) {
                        //Exceptions.printStackTrace(ex);
                        UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                    }
                }
            }
        }
    }

    private FSRepositoryAdsModule createModuleRepository(Id id, String name, String description) throws IOException {
        File preparedDir = new File(getDirectory(), id.toString());
        preparedDir.mkdirs();
        if (!preparedDir.exists()) {
            throw new IOException("Unable to create directory for module");
        }
        if(name == null) {
            name = "Untitled#" + untitledModulesSequence++;
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

        if (id == RolesModule.MODULE_ID) {
            return new RolesModuleRepository(this, preparedDir);
        } else if(id == MsdlSchemesModule.MODULE_ID){
            return new MsdlSchemesModuleRepository(this, preparedDir);
        } else {
            return new ReportsModuleRepository(this, preparedDir, id, name);
        }
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
            if (module instanceof ReportsModule) {
                repository = new ReportsModuleRepository(this, (ReportsModule) module);
                registerModule(module.getId(), repository, true);
                return repository;
            } else if (module instanceof RolesModule) {
                repository = new RolesModuleRepository(this, module);
                registerModule(module.getId(), repository, false);
                return repository;
            } else if (module instanceof MsdlSchemesModule) {
                repository = new MsdlSchemesModuleRepository(this, module);
                registerModule(module.getId(), repository, false);
                return repository;
            }else {
                return null;
            }
        }
    }

    public UserExtLayerRepository getLayerRepository() {
        return layer;
    }

    private void registerModule(Id id, FSRepositoryAdsModule repository, boolean reportModule) {
        modules.put(id, repository);
//        if (reportModule) {
//            UserExtensionManager.getInstance().getUserReports().registerModule(repository);
//        }

    }
}
