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
package org.radixware.kernel.common.defs.dds;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.fs.IRepositoryDdsModule;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.ddsdef.ModelDocument;

/**
 * Utility class for loading and management of fixed and modified DdsModelDef of
 * DdsModule.
 *
 * @ThreadSafe
 */
public class DdsModelManager {

    public static final String MODIFIED_MODEL_XML_FILE_NAME = "model_modified.xml";
    public static final String FIXED_MODEL_XML_FILE_NAME = "model.xml";
    private DdsModelDef fixedModel = null;
    private DdsModelDef modifiedModel = null;
    private final DdsModule module;
    private final Object modelLock = new Object();

    protected DdsModelManager(DdsModule module) {
        super();
        this.module = module;
    }

    private void loadModels() throws IOException {
        final IRepositoryDdsModule repository = module.getRepository();
        if (repository == null) {
            return;
        }
        Layer layer = module.getLayer();
        if (layer != null && layer.isLocalizing()){
            return;
        }

        initialized = true;
        InputStream stream = repository.getFixedModelData();
        DdsModelDef loadedFixedModel;
        try {
            try {
                // load and initialize fixed model
                loadedFixedModel = DdsModelDef.Factory.loadFrom(stream);
                loadedFixedModel.setOwnerModule(module);
                File fixedModelFile = repository.getFixedModelFile();
                if (fixedModelFile != null && fixedModelFile.exists()) {
                    loadedFixedModel.fileLastModifiedTime = fixedModelFile.lastModified();
                }
            } catch (XmlException ex) {
                throw new IOException(ex);
            }

        } finally {
            stream.close();
        }

        // load and initialize modified model
        stream = repository.getModifiedModelData();
        DdsModelDef loadedModifiedModel = null;
        if (stream != null) {

            try {
                loadedModifiedModel = DdsModelDef.Factory.loadFrom(stream);
                if (loadedModifiedModel != null) {
                    loadedModifiedModel.setOwnerModule(module);
                }
                File modifiedModelFile = module.getRepository().getModifiedModelFile();
                if (modifiedModelFile != null && modifiedModelFile.exists()) {
                    loadedModifiedModel.fileLastModifiedTime = modifiedModelFile.lastModified();
                }
            } catch (XmlException ex) {
                throw new IOException(ex);
            } finally {
                stream.close();
            }
        }
        // clear
        if (fixedModel != null) {
            fixedModel.setOwnerModule(null);
        }
        if (modifiedModel != null) {
            modifiedModel.setOwnerModule(null);
        }

        // assign
        fixedModel = loadedFixedModel;
        modifiedModel = loadedModifiedModel;

        // fire model changed
        if (modelSupport != null) {
            modelSupport.fireEvent(new RadixEvent());
        }
    }

    private void loadModelsIfNecessary() throws IOException {
        if (fixedModel == null) {
            loadModels();
        }
    }

    /**
     * Get {@link DdsModel DDS Model} of the module.
     */
    public DdsModelDef getModel() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();

            if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                return modifiedModel;
            } else {
                return fixedModel;
            }
        }
    }

    /**
     * Get {@link DdsModel DDS Model} of the module.
     *
     * @return model or null if unable to load.
     */
    public DdsModelDef findModel() {
        try {
            return getModel();
        } catch (IOException ex) {
            module.getRepository().processException(ex);
            return null;
        }
    }

    public void reloadModels() throws IOException {
        synchronized (modelLock) {
            loadModels();
        }
    }
    /**
     * Return true if model created, loaded or tryed to load. It is recommended
     * to use only with getModelSupport (to except displaying of model when its
     * reloaded). Function is synchronised with model loading (will wait if
     * model is loading now).
     */
    private boolean initialized = false;

    public boolean isInitialized() {
        synchronized (modelLock) {
            return initialized;
        }
    }

    /**
     * Set {@link DdsModel DDS Model} of the module. Allows to dinamically
     * create model in memory. Also allows to close model.
     */
    public synchronized void setModel(DdsModelDef model) {
        synchronized (modelLock) {
            if (fixedModel == model && modifiedModel == null) {
                return;
            }

            initialized = true;

            if (fixedModel != null) {
                fixedModel.setOwnerModule(null);
                fixedModel = null;
            }
            if (modifiedModel != null) {
                modifiedModel.setOwnerModule(null);
                modifiedModel = null;
            }

            if (model != null) {
                model.setOwnerModule(module);
            }

            fixedModel = model;
            modifiedModel = null;

            if (modelSupport != null) {
                modelSupport.fireEvent(new RadixEvent());
            }
        }
    }

    /**
     * Get or load modified {@linkplain DdsModelDef}.
     *
     * @return model or null if model is in the fixed state.
     */
    public DdsModelDef getModifiedModel() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();
            return modifiedModel;
        }
    }

    /**
     * Get modified {@linkplain DdsModelDef} if its loaded. It is recommended to
     * use only with getModelSupport (to except displaying of model when its
     * reloaded). Function is synchronised with model loading (will wait if
     * model is loading now).
     *
     * @return model or null if not loaded.
     */
    public DdsModelDef getModifiedModelIfLoaded() {
        synchronized (modelLock) {
            return modifiedModel;
        }
    }

    /**
     * Get or load fixed {@linkplain DdsModelDef}.
     */
    public DdsModelDef getFixedModel() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();
            return fixedModel;
        }
    }

    /**
     * Get fixed {@linkplain DdsModelDef} if its loaded or assigned. It is
     * recommended to use only with getModelSupport (to except displaying of
     * model when its reloaded). Function is synchronised with model loading
     * (will wait if model is loading now).
     *
     * @return model or null if not loaded or assigned.
     */
    public DdsModelDef getFixedModelIfLoaded() {
        synchronized (modelLock) {
            return fixedModel;
        }
    }

    /**
     * Switch model to structure fixed state. Do nothing if model is not in
     * modification state. It is possible to fix own or alien model.
     *
     * @throws DefinitionError if modified model not exist or captured by
     * another user.
     */
    public void switchModelToFixedState() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();

            if (modifiedModel == null) {
                throw new DefinitionError("Attemp to fix not caputer module.", module);
            }

            if (!modifiedModel.getModifierInfo().isOwn()) {
                throw new DefinitionError("Attemp to fix module that captured by another user.", module);
            }

            final File modifiedModelFile = modifiedModel.getFile();

            // it is possible to fix only own model, so pointer to current model will be remained.
            fixedModel = modifiedModel;
            modifiedModel = null;
            fixedModel.getModifierInfo().clear();

            if (modelSupport != null) {
                modelSupport.fireEvent(new RadixEvent()); // update tree
            }

            fixedModel.enterFixedState();
            FileUtils.deleteFileExt(modifiedModelFile);
        }
    }

    public void cancelCapture() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();

            final File moduleDir = module.getDirectory();
            final File modifiedModelFile = new File(moduleDir, MODIFIED_MODEL_XML_FILE_NAME);

            if (modifiedModelFile.isFile()) {
                FileUtils.deleteFileExt(modifiedModelFile);
            }

            if (modifiedModel != null) {
                reloadModels();
            }
            module.saveDirectoryXml(); // update model.xml digist
        }
    }

    /**
     * Switch model to structure modification state.
     */
    public void switchModelToModificationState() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();
            
            if (modifiedModel != null) {
                if (modifiedModel.getModifierInfo().isOwn()) {
                    return; // already
                }
                modifiedModel.getModifierInfo().setCurrent();
                modifiedModel.save();
                loadModels(); // it is required to close non-actual editors and update tree.
            } else {
                final DdsModelDef newFixedModel = DdsModelDef.Factory.newInstance(fixedModel);
                newFixedModel.setOwnerModule(module);
                fixedModel.beginEnterModificationState();
                modifiedModel = fixedModel;
                fixedModel = newFixedModel;

                modifiedModel.getModifierInfo().setCurrent();
                
                modifiedModel.getStringBundle().reloadStrings();
                
                if (modelSupport != null) {
                    modelSupport.fireEvent(new RadixEvent()); // update tree
                }
                modifiedModel.enterModificationState();
            }
        }
    }

    static public class Support {

        //RADIX-8822
        static public DdsModule loadAndGetDdsModule(final File modelXml, final Reference<Branch> refBranch) throws IOException, XmlException {
            
            final File moduleDirFile = modelXml.getParentFile();
            final File moduleXmlFile = new File(moduleDirFile.getAbsolutePath() + "/" + FileNames.DDS_MODULE_XML);
            final File layerFile = moduleDirFile.getParentFile().getParentFile();
            final File branchAsFile = layerFile.getParentFile();
            final Reference<DdsModule> newDdsModule = new Reference();
       

            Branch branch = refBranch.get();
            if (branch == null) {
                branch = Branch.Factory.loadFromDir(branchAsFile);
                refBranch.set(branch);
            }

            branch.visit(new IVisitor() {
                @Override
                public void accept(final RadixObject radixObject) {
                    if (radixObject instanceof DdsModule) {
                        newDdsModule.set((DdsModule) radixObject);
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(final RadixObject radixObject) {
                    if (radixObject instanceof DdsModule) {
                        final DdsModule ddsModule = (DdsModule) radixObject;
                        if (ddsModule.getFile().equals(moduleXmlFile)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
          
            final DdsModule ddsModule = newDdsModule.get();
            return ddsModule;
        }
    }

    /**
     * Gets byte array of model_modified.xml for sending to Svn server
     */
    public byte[] getModelModificationState() throws IOException {
        synchronized (modelLock) {
            loadModelsIfNecessary();

            final ModelDocument modelDocument = ModelDocument.Factory.newInstance();
            final ModelDocument.Model model = modelDocument.addNewModel();

            final DdsModelDef currentModel = getCurrentModelCopy();
            currentModel.appendTo(model, true);
            currentModel.delete();

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            XmlFormatter.save(modelDocument, out);
            return out.toByteArray();
        }
    }

    private DdsModelDef getCurrentModelCopy() {
        final DdsModelDef modelCopy = DdsModelDef.Factory.newTemporaryInstance(modifiedModel != null ? modifiedModel : fixedModel);
        modelCopy.setOwnerModule(module);
        modelCopy.getModifierInfo().setCurrent();
        return modelCopy;
    }

    private class DdsModelEventSource extends RadixEventSource {

        public DdsModelEventSource() {
        }

        @Override
        public void fireEvent(RadixEvent event) {
            super.fireEvent(event);
            DdsSegment segment = DdsModelManager.this.module.getSegment();
            if (segment != null) {
                segment.getModelSupport().fireEvent(event);
            }
        }
    }
    private final RadixEventSource modelSupport = new DdsModelEventSource();

    /**
     * Get support to listen loading of DdsModelDef. Оповещает о факте загрузки
     * DDS модели, даже если загрузка не удалась (чтобы подсветить модуль
     * красным).
     */
    public RadixEventSource getModelSupport() {
        return modelSupport;
    }

    File findModelFile(DdsModelDef model) {
        final File moduleDir = module.getDirectory();
        if (moduleDir == null) {
            return null;
        }

        if (model == fixedModel) {
            return new File(moduleDir, FIXED_MODEL_XML_FILE_NAME);
        } else if (model == modifiedModel) {
            return new File(moduleDir, MODIFIED_MODEL_XML_FILE_NAME);
        } else {
            throw new IllegalStateException();
        }
    }
}
