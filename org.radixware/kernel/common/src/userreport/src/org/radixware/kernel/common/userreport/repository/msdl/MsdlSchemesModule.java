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

package org.radixware.kernel.common.userreport.repository.msdl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtRepository;


public class MsdlSchemesModule extends AdsModule {

    public static final Id MODULE_ID = Id.Factory.newInstance(EDefinitionIdPrefix.MODULE);
    private static MsdlSchemesModule instance = null;

    public static MsdlSchemesModule getInstance() {
        synchronized (MODULE_ID) {
            if (instance == null) {
                instance = new MsdlSchemesModule();
            }
            return instance;
        }
    }

    public static void dispose() {
        synchronized (MODULE_ID) {
            instance = null;
        }
    }

    private MsdlSchemesModule() {
        super(MODULE_ID, "User Defined MsdlSchemes");
    }

    private void updateDependences(final Layer layer) {

        if (layer != null && getDependences().getModuleIds().isEmpty()) {

            final Layer.HierarchyWalker walker = new Layer.HierarchyWalker();
            walker.go(layer, new Layer.HierarchyWalker.Acceptor<AdsModule>() {

                @Override
                public void accept(final HierarchyWalker.Controller<AdsModule> controller,final Layer radixObject) {
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

    /*private void reloadFromRepository() {
        ((MsdlSchemesModule.MsdlSchemesModuleDefinitions) getDefinitions()).reloadFromRepository();
    }*/

    @Override
    protected ModuleDefinitions createDefinitinosList() {
        return new MsdlSchemesModule.MsdlSchemesModuleDefinitions(this, true);
    }

    private static class MsdlSchemesModuleDefinitions extends ModuleDefinitions {

        private final ReentrantLock loadFindLock = new ReentrantLock();

        public MsdlSchemesModuleDefinitions(final AdsModule owner,final boolean upload) {
            super(owner, upload);
        }

        private void reloadFromRepository() {
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

                if (oldDef instanceof AdsMsdlSchemeDef) {
                    final MsdlScheme msdlScheme = UserExtensionManagerCommon.getInstance().getMsdlSchemes().findMsdlScheme(oldDef.getId());

                    if (msdlScheme != null) {
                        msdlScheme.reload();
                        return msdlScheme.findMsdlSchemeDefinition();
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
            if (def instanceof AdsMsdlSchemeDef) {
                final MsdlScheme msdlScheme = UserExtensionManagerCommon.getInstance().getMsdlSchemes().findMsdlScheme(def.getId());
                if (msdlScheme != null && msdlScheme.findMsdlSchemeDefinition()== def) {
                    msdlScheme.save();
                }
            }
        }

        @Override
        public AdsDefinition findById(final Id schemeId) {
            try {
                loadFindLock.lock();
                final AdsDefinition def = super.findById(schemeId);
                if (def != null) {
                    return def;
                } //else {
                final MsdlScheme msdlScheme = UserExtensionManagerCommon.getInstance().getMsdlSchemes().findMsdlScheme(schemeId);
                if (msdlScheme != null) {
                    final File defFile = ((MsdlSchemesModuleRepository) getModule().getRepository()).uploadMsdlScheme(msdlScheme);
                    if (defFile != null) {
                        try {
                            addFromRepository(new FSRepositoryAdsDefinition(defFile));
                            return super.findById(schemeId);
                        } catch (final IOException ex) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    //Exceptions.printStackTrace(ex);
                                     UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                                }
                            });

                        }
                    }
                }
                return null;
                //}
            } finally {
                loadFindLock.unlock();
            }
        }
    }

    @Override
    public boolean isReadOnly(final AdsDefinition def) {
        if (def instanceof AdsMsdlSchemeDef) {
            final MsdlScheme msdlScheme = UserExtensionManagerCommon.getInstance().getMsdlSchemes().findMsdlScheme(def.getId());
            if (msdlScheme == null) {
                return true;
            } else {
                return msdlScheme.isReadOnly();
            }
        } else {
            return true;
        }
    }    
}
