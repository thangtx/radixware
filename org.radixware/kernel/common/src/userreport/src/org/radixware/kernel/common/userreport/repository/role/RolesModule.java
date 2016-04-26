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

package org.radixware.kernel.common.userreport.repository.role;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtRepository;



public class RolesModule extends AdsModule {

    public static final Id MODULE_ID = Id.Factory.newInstance(EDefinitionIdPrefix.MODULE);
    private static RolesModule instance = null;

    public static RolesModule getInstance() {
        synchronized (MODULE_ID) {
            if (instance == null) {
                instance = new RolesModule();
            }
            return instance;
        }
    }

    public static void dispose() {
        synchronized (MODULE_ID) {
            instance = null;
        }
    }

    private RolesModule() {
        super(MODULE_ID, "User Defined Roles");
    }

    private void updateDependences(final Layer layer) {

        if (layer != null && getDependences().getModuleIds().isEmpty()) {

            final Layer.HierarchyWalker walker = new Layer.HierarchyWalker();
            walker.go(layer, new Layer.HierarchyWalker.Acceptor<AdsModule>() {

                @Override
                public void accept(final Controller<AdsModule> controller, final Layer radixObject) {
                    if (radixObject != layer) {
                        for (Module m : radixObject.getAds().getModules()) {
                            getDependences().add(m);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void loadFrom(final org.radixware.schemas.product.Module xModule) {
        super.loadFrom(xModule);
        updateDependences(UserExtRepository.getInstance().getUserExtSegment().getLayer());
    
    }

    /*void reloadFromRepository() {
        ((RolesModuleDefinitions) getDefinitions()).reloadFromRepository();
    }*/

    @Override
    protected ModuleDefinitions createDefinitinosList() {
        return new RolesModuleDefinitions(this, true);
    }

    private static class RolesModuleDefinitions extends ModuleDefinitions {

        private final ReentrantLock loadFindLock = new ReentrantLock();

        public RolesModuleDefinitions(final AdsModule owner,final boolean upload) {
            super(owner, upload);
        }

        void reloadFromRepository() {
            final IRepositoryAdsModule repository = getModule().getRepository();
            if (repository != null) {
                final IRepositoryAdsDefinition[] defRepositories = repository.listDefinitions();
                for (IRepositoryAdsDefinition definitionRepository : defRepositories) {
                    try {
                        addFromRepository(definitionRepository);
                    } catch (IOException ex) {
                        repository.processException(ex);
                    }
                }
            }
        }

        @Override
        public AdsDefinition reload(final AdsDefinition oldDef) throws IOException {
            try {
                loadFindLock.lock();

                if (oldDef instanceof AdsRoleDef) {
                    final AppRole role = UserExtensionManagerCommon.getInstance().getAppRoles().findAppRole(oldDef.getId());

                    if (role != null) {
                        role.reload();
                        return role.findRoleDefinition();
                    }
                }
                return null;
            } finally {
                loadFindLock.unlock();
            }
        }

        @Override
        public void save(final AdsDefinition def,final AdsDefinition.ESaveMode saveMode) throws IOException {
            super.save(def, saveMode);
            if (def instanceof AdsRoleDef) {
                final AppRole role = UserExtensionManagerCommon.getInstance().getAppRoles().findAppRole(def.getId());
                if (role != null && role.findRoleDefinition() == def) {
                    role.save();
                }
            }
        }

        @Override
        public AdsDefinition findById(final Id roleId) {
            try {
                loadFindLock.lock();
                final AdsDefinition def = super.findById(roleId);
                if (def != null) {
                    return def;
                } //else {
                    final AppRole role = UserExtensionManagerCommon.getInstance().getAppRoles().findAppRole(roleId);
                    if (role != null) {
                        final File defFile = ((RolesModuleRepository) getModule().getRepository()).uploadRole(role);
                        if (defFile != null) {
                            try {
                                addFromRepository(new FSRepositoryAdsDefinition(defFile));
                                return super.findById(roleId);
                            } catch (final IOException ex) {
                                SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                       // Exceptions.printStackTrace(ex);
                                        UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                                    }
                                });

                            }
                        }
                    }

                    return null;
               // }
            } finally {
                loadFindLock.unlock();
            }
        }
    }

    @Override
    public boolean isReadOnly(final AdsDefinition def) {
        if (def instanceof AdsRoleDef) {
           final  AppRole appRole = UserExtensionManagerCommon.getInstance().getAppRoles().findAppRole(def.getId());
            if (appRole == null) {
                return true;
            } else {
                return appRole.isReadOnly();
            }
        } else {
            return true;
        }
    }
}
