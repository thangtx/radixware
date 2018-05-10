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

package org.radixware.kernel.common.defs.uds.module;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.IAdsLocalizedDef;
import org.radixware.kernel.common.defs.uds.IInnerLocalizingDef;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsModule;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;


public class ModuleDefinitions extends org.radixware.kernel.common.defs.ads.module.ModuleDefinitions {

    ModuleDefinitions(UdsModule container, boolean upload) {
        super(container, upload);
    }

    public File getSourceFile(UdsDefinition def) {
        assert def.getOwnerDefinition() == getModule();
        IRepositoryModule rep = getModule().getRepository();
        if (rep != null) {
            IRepositoryDefinition defRep = rep.getDefinitionRepository(def);
            if (defRep != null) {
                return defRep.getFile();
            }
        }
        return null;
    }
    boolean doNotOptimizeDependencesOnAdding = false;

    @Override
    protected void onAdd(final AdsDefinition def) {
        if (def instanceof UdsDefinition) {
            baseOnAdd(def);
            if (!loading) {
                if (def.getFile() != null) {
                    try {
                        def.save();
                    } catch (IOException cause) {
                        throw new DefinitionError("Unable to save definition.", def, cause);
                    }
                    if (!doNotOptimizeDependencesOnAdding) {
                        getModule().getDependences().addRequired(); // add required dependencies after paste, and for new definitions.
                    }
                }
            }
        } else {
            super.onAdd(def);
        }
    }

    public void enableAutoDependences(boolean enable) {        
        this.doNotOptimizeDependencesOnAdding = !enable;        
        if (!this.doNotOptimizeDependencesOnAdding) {
            getModule().getDependences().addRequired();
        }
    }

    @Override
    public File getSourceFile(AdsDefinition def, AdsDefinition.ESaveMode saveMode) {
        if (def instanceof UdsDefinition) {
            assert def.getOwnerDefinition() == getModule();
            IRepositoryModule rep = getModule().getRepository();
            if (rep != null) {
                IRepositoryDefinition defRep = rep.getDefinitionRepository(def);
                if (defRep != null) {
                    return defRep.getFile();
                }
            }
            return null;
        } else {
            return super.getSourceFile(def, saveMode);
        }
    }

    @Override
    public void save(AdsDefinition def, AdsDefinition.ESaveMode mode) throws IOException {
        assert def.getOwnerDefinition() == getModule();

        if (def instanceof UdsDefinition) {
            File sourceFile = getSourceFile((UdsDefinition) def);

            boolean doNotSave = false;

            if (!doNotSave) {
                if (sourceFile == null) {
                    throw new IOException("Definition " + def.getQualifiedName() + " is not file-based");
                }
                FileUtils.mkDirs(sourceFile.getParentFile());

                UdsDefinitionDocument xDoc = UdsDefinitionDocument.Factory.newInstance();
                UdsDefinitionDocument.UdsDefinition xDef = xDoc.addNewUdsDefinition();
                ((UdsDefinition) def).appendTo(xDef);
                synchronized (def) {
                    XmlFormatter.save(xDoc, sourceFile);

                    def.setFileLastModifiedTime(sourceFile.lastModified());
                }

                def.setEditState(EEditState.NONE);
            }
        } else {
            super.save(def, mode);
        }
    }

    @Override
    public void savePreview(AdsDefinition def) {
    }
    
    

    @Override
    protected AdsDefinition loadFromRepository(IRepositoryAdsDefinition definitionRepository) throws IOException {
        try {
            return Loader.loadFromRepository(definitionRepository);
        } catch (DefinitionError e) {
            return super.loadFromRepository(definitionRepository);
        }
    }

    @Override
    public AdsDefinition reload(AdsDefinition oldDef) throws IOException {

        if (getModule().importLock.tryLock()) {
            try {
                return super.reload(oldDef);
            } finally {
                getModule().importLock.unlock();
            }
//            try {
//                assert oldDef.getContainer() == this;
//
//                final File file = oldDef.getFile();
//                assert file != null;
//
//                final IRepositoryDefinition definitionRepository = new FSRepositoryUdsDefinition(file);
//                final UdsDefinition newDef = Loader.loadFromRepository(definitionRepository);
//
////                if (!Utils.equals(oldDef.getId(), newDef.getId())) {
////                    return oldDef;
////                } // checked in loadFromRepository (file name must be equal to identifier in xml)
//
//                synchronized (this) {
//                    loading = true;
//                    try {
//                        this.remove(oldDef);
//                        super.add(newDef);
//                    } finally {
//                        loading = false;
//                    }
//                }
//
//                return newDef;
//            } finally {
//                getModule().importLock.unlock();
//            }
        } else {
            return oldDef;
        }
    }

//    void reload() {
//
//        if (getModule().importLock.tryLock()) {
//            try {
//                synchronized (this) {
//                    List<Definition> defs = this.list();
//
//                    for (Definition def : defs) {
//                        this.remove(def);
//                    }
//
//                    upload();
//                }
//            } finally {
//                getModule().importLock.unlock();
//            }
//        }
//    }
    private void upload() {
        if (getModule().importLock.tryLock()) {
            try {
                synchronized (this) {
                    IRepositoryUdsModule repository = getModule().getRepository();
                    if (repository != null) {
                        IRepositoryAdsDefinition[] definitionRepositories = repository.listDefinitions();
                        for (IRepositoryAdsDefinition definitionRepository : definitionRepositories) {
                            try {
                                addFromRepository(definitionRepository);
                            } catch (IOException ex) {
                                repository.processException(ex);
                            }
                        }
                    }

                }
            } finally {
                getModule().importLock.unlock();
            }
        }
    }

    @Override
    public AdsDefinition addFromRepository(IRepositoryAdsDefinition definitionRepository) throws IOException {
        AdsDefinition def;

        try {
            def = Loader.loadFromRepository(definitionRepository);
        } catch (Throwable e) {
            return super.addFromRepository(definitionRepository);
        }
        if (this.containsId(def.getId())) {
            throw new IllegalStateException("Definition with identifier #" + def.getId() + " is already loaded.");
        }

        synchronized (this) {
            loading = true;
            try {
                super.add(def);
            } finally {
                loading = false;
            }
        }

        return def;
    }

    @Override
    public UdsModule getModule() {
        return (UdsModule) super.getModule();
    }

    @Override
    public AdsLocalizingBundleDef findLocalizingBundleDef(IAdsLocalizedDef owner, boolean createIfNotExists) {
        if (owner instanceof IInnerLocalizingDef) {
            return ((IInnerLocalizingDef)owner).findLocalizingBundleImpl(createIfNotExists);
        }
        return super.findLocalizingBundleDef(owner, createIfNotExists);
    }
    
    
}
