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
package org.radixware.kernel.common.defs.ads.module;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.platform.BuildPath;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;

public class ModuleDefinitions extends AdsDefinitions<AdsDefinition> {

    protected ModuleDefinitions(AdsModule owner, boolean upload) {
        super(owner);
        if (upload) {
            upload();
        }
    }

    public File getSourceDir(ESaveMode saveMode) {
        Module module = getModule();
        if (module != null) {
            File moduleDir = module.getDirectory();
            if (moduleDir != null) {
                return new File(moduleDir, saveMode == ESaveMode.NORMAL ? AdsModule.SOURCES_DIR_NAME : AdsModule.STRIP_SOURCES_DIR_NAME);
            }
        }
        return null;
    }
    protected boolean loading = false;

    @Override
    protected void onAdd(final AdsDefinition def) {
        baseOnAdd(def);

        if (def instanceof IPlatformClassPublisher) {
            getBuildPath().getPlatformPublishers().attach((IPlatformClassPublisher) def);
        }

        if (!loading) {
            def.setUploadMode(ESaveMode.NORMAL);
            if (def.getFile() != null) {
                try {
                    def.save();
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to save definition.", def, cause);
                }
                getModule().getDependences().addRequired(); // add required dependencies after paste, and for new definitions.
            }
        }
    }

    protected void baseOnAdd(final AdsDefinition def) {
        super.onAdd(def);
    }

    public AdsDefinition addFromRepository(IRepositoryAdsDefinition definitionRepository) throws IOException {
        if (getLayer() != null && getLayer().isLocalizing()) {
            return null;
        }
        final AdsDefinition def = Loader.loadFromRepository(getModule(), definitionRepository, definitionRepository.willLoadFromAPI());

        if (this.containsId(def.getId())) {
            throw new IllegalStateException("Definition with identifier #" + def.getId() + " is already loaded.");
        }

        synchronized (this) {
            loading = true;
            try {
                super.add(def);
                def.setUploadMode(getModule().isUserModule() ? ESaveMode.NORMAL : definitionRepository.getUploadMode());
            } finally {
                loading = false;
            }
        }

        return def;
    }

    /**
     * Try to load definition from its file and replace the current definition,
     * if succesfull.
     *
     * @return loaded definition.
     */
    public AdsDefinition reload(AdsDefinition oldDef) throws IOException {
        assert oldDef.getContainer() == this;
        final IRepositoryAdsDefinition definitionRepository = recreateRepository(oldDef);
        if (definitionRepository != null) {
            final AdsDefinition newDef = loadFromRepository(definitionRepository);

            assert Utils.equals(oldDef.getId(), newDef.getId()); // checked in loadFromRepository (file name must be equal to identifier in xml)

            safelyReplace(oldDef, newDef, definitionRepository.getUploadMode());

            return newDef;
        } else {
            return null;
        }
    }

    protected AdsDefinition loadFromRepository(IRepositoryAdsDefinition definitionRepository) throws IOException {
        return Loader.loadFromRepository(getModule(), definitionRepository, definitionRepository.willLoadFromAPI());
    }

    protected IRepositoryAdsDefinition recreateRepository(AdsDefinition oldDef) throws IOException {

        final File file = oldDef.getFile();
        assert file != null;

        return new FSRepositoryAdsDefinition(file);
    }

    protected void safelyReplace(AdsDefinition oldDef, AdsDefinition newDef, ESaveMode uploadMode) {
        synchronized (this) {
            loading = true;
            try {
                AdsLocalizingBundleDef bundle = findLocalizingBundleDef(oldDef, false);
                if (bundle != null) {
                    this.strings.remove(bundle);
                }
                this.remove(oldDef);
                this.stringLoadFailures.remove(oldDef.getLocalizingBundleId());
                super.add(newDef);
                newDef.setUploadMode(uploadMode);
            } finally {
                loading = false;
            }
        }
    }

    void reload() {
        synchronized (this) {
            List<AdsDefinition> defs = this.list();
            List<AdsLocalizingBundleDef> bundles = this.strings.list();
            for (AdsDefinition def : defs) {
                this.remove(def);
            }
            for (AdsLocalizingBundleDef bundle : bundles) {
                this.strings.remove(bundle);
            }
            upload();
        }
    }

    private void upload() {
        synchronized (this) {
            if (getModule().neverUpload()) {
                return;
            }
            if (getLayer() != null && getLayer().isLocalizing()) {
                return;
            }
            IRepositoryAdsModule repository = getModule().getRepository();
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
    }

    @Override
    public String getName() {
        return "Definitions";
    }

//    private static class LoaderRunnable implements Runnable {
//
//        private final IRepositoryAdsDefinition repository;
//        private AdsDefinition def = null;
//
//        public LoaderRunnable(IRepositoryAdsDefinition repository) {
//            this.repository = repository;
//        }
//
//        @Override
//        public void run() {
//            try {
//                def = Loader.loadFromRepository(repository);
//            } catch (IOException ex) {
//                repository.processException(ex);
//            }
//        }
//
//        public IRepositoryAdsDefinition getRepository() {
//            return repository;
//        }
//
//        public AdsDefinition getResult() {
//            return def;
//        }
//    }
//    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//    private void upload() {
//        synchronized (this) {
//            if (getModule().neverUpload()) {
//                return;
//            }
//            final IRepositoryAdsModule moduleRepository = getModule().getRepository();
//            if (moduleRepository == null) {
//                return;
//            }
//            final IRepositoryAdsDefinition[] repositories = moduleRepository.listDefinitions();
//            final int len = repositories.length;
//            final Future[] futuries = new Future[len];
//            final LoaderRunnable[] runnables = new LoaderRunnable[len];
//            for (int i = 0; i < len; i++) {
//                final IRepositoryAdsDefinition repository = repositories[i];
//                runnables[i] = new LoaderRunnable(repository);
//                futuries[i] = executorService.submit(runnables[i]);
//            }
//            for (int i = 0; i < len; i++) {
//                try {
//                    futuries[i].get();
//                    final AdsDefinition def = runnables[i].getResult();
//                    loading = true;
//                    try {
//                        super.add(def);
//                        def.setUploadMode(runnables[i].getRepository().getUploadMode());
//                    } finally {
//                        loading = false;
//                    }
//                } catch (ExecutionException ex) {
//                    return;
//                } catch (InterruptedException ex) {
//                    return;
//                }
//            }
//        }
//    }
    @Override
    public void add(AdsDefinition def) {
        if (containsId(def.getId())) {
            throw new DefinitionError("Duplicate definition: " + def.getId().toString(), def);
        }
        super.add(def);
    }
    private BuildPath buildPath = null;

    private BuildPath getBuildPath() {
        synchronized (this) {
            if (buildPath == null) {
                buildPath = ((AdsSegment) getModule().getSegment()).getBuildPath();
            }
            return buildPath;
        }
    }

    private class MLStrings extends Definitions<AdsLocalizingBundleDef> {

        private MLStrings() {
            super(ModuleDefinitions.this);
        }
    }
    private final MLStrings strings = new MLStrings();

    private static class LoadFailure {

        Id id;
        long fileTime;

        public LoadFailure(Id id, File file) {
            this.id = id;
            if (file != null) {
                fileTime = file.lastModified();
            } else {
                fileTime = -1;
            }
        }
    }
    private volatile Map<Id, LoadFailure> stringLoadFailures = new HashMap<Id, LoadFailure>(1);

    protected boolean checkStringLoadFailures() {
        return true;
    }

    private void failOnStringLoading(Id mlbId, File file) {
        if (checkStringLoadFailures()) {
            stringLoadFailures.put(mlbId, new LoadFailure(mlbId, file));
        }
    }

    private boolean loadFailed(Id mlbId) {
        if (checkStringLoadFailures()) {
            return stringLoadFailures.containsKey(mlbId);
        } else {
            return false;
        }
    }

    private AdsLocalizingBundleDef loadLocalizingBundle(AdsDefinition owner) {

        if (loadFailed(owner.getLocalizingBundleId())) {
            return null;
        }

        final IRepositoryAdsModule moduleRep = getModule().getRepository();

        if (moduleRep == null) {
            return null;
        }

        final IRepositoryAdsDefinition definitionRepository = moduleRep.getDefinitionRepository(owner);
        if (definitionRepository == null) {
            return null;
        }

        final IRepositoryAdsDefinition mlsRepository = definitionRepository.getMlsRepository();
        if (mlsRepository == null) {
            return null;
        }

        try {
            final AdsDefinition result = Loader.loadFromRepository(getModule(), mlsRepository, mlsRepository.willLoadFromAPI());
            if (result instanceof AdsLocalizingBundleDef) {
                return (AdsLocalizingBundleDef) result;
            } else {
                failOnStringLoading(owner.getLocalizingBundleId(), mlsRepository.getFile());
                return null; // TODO: throw exception - invalid format
            }
        } catch (IOException ex) {
            //moduleRep.processException(ex);String might not exist
            failOnStringLoading(owner.getLocalizingBundleId(), mlsRepository.getFile());
            return null; // TODO: throw exception
        }
    }

    public AdsLocalizingBundleDef findLocalizingBundleDef(AdsDefinition owner, boolean createIfNotExists) {
        if (owner instanceof AdsLocalizingBundleDef) {
            return null;
        }

        synchronized (strings) {
            AdsLocalizingBundleDef def = strings.findById(owner.getLocalizingBundleId());
            if (def == null) {//was not loaded yet
                def = loadLocalizingBundle(owner);
                boolean save = false;
                if (def == null) {
                    if (createIfNotExists) {
                        def = AdsLocalizingBundleDef.Factory.newInstance(owner.getId());
                        save = true;
                    } else {
                        return null;
                    }

                }
                strings.add(def);
                def.getStrings().updateStringsByLanguages();
                // Follwing string def.setUploadMode(owner.getSaveMode()); is commented to solve issue with
                // attempt to save strings to localizing layer in case when source string set belongs to read only layer
                
                // def.setUploadMode(owner.getSaveMode());
                if (save) {
                    try {
                        save(def, def.getSaveMode());
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        // save later
                    }
                }
            }
            return def;
        }
    }

    public void closeExternallyModifiedLocalizingBundles() {
        synchronized (strings) {
            File srcDir = getSourceDir(ESaveMode.NORMAL);
            if (srcDir != null && srcDir.isDirectory()) {
                final String prefix = EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue();
                File[] possibleMlbFiles = srcDir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isFile()) {
                            String name = pathname.getName();
                            if (name.startsWith(prefix) && name.endsWith(".xml")) {
                                return true;
                            }
                        }
                        return false;
                    }
                });

                if (possibleMlbFiles != null) {

                    for (int i = 0; i < possibleMlbFiles.length; i++) {
                        File file = possibleMlbFiles[i];
                        Id id = AdsDefinition.fileName2DefinitionId(file);
                        LoadFailure failure = stringLoadFailures.get(id);
                        if (failure != null) {
                            if (failure.fileTime < 0 || failure.fileTime != file.lastModified()) {
                                stringLoadFailures.remove(id);
                            }
                        }
                    }

                }
            }
            for (AdsLocalizingBundleDef string : strings) {
                List<File> files = string.getFiles();
                long lastModifiedTime = 0L;
                if (!files.isEmpty()) {
                    for (File file : files) {
                        if (lastModifiedTime < file.lastModified()) {
                            lastModifiedTime = file.lastModified();
                        }
                    }
                    if (lastModifiedTime != string.getFileLastModifiedTime()) {
                        strings.remove(string);
                    }
                }
            }

        }
    }

    boolean isModifiedAfter(long time) {
        for (AdsDefinition def : this) {
            long dt = def.getFileLastModifiedTime();
            if (dt >= time) {
                return true;
            }
            AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
            if (bundle != null) {
                dt = bundle.getFileLastModifiedTime();
                if (dt >= time) {
                    return true;
                }
            }
        }
        return false;
    }

    void saveAPI(APIDocument.API xDef) {
        List<String> ids = new LinkedList<String>();

        List<AdsDefinition> defs = this.list();
        defs.addAll(getModule().getImages().list());
        Comparator<AdsDefinition> adsC = new DefinitionIdComparator<AdsDefinition>();
        Collections.sort(defs, adsC);
        for (AdsDefinition def : defs) {
            if (def.getDefinitionType() != EDefType.IMAGE) {
                def.appendTo(xDef.addNewDefinition(), ESaveMode.API);
            }
            ids.add(def.getId().toString());
        }
        /*List<AdsLocalizingBundleDef> stringDefs = this.strings.list();
        
         Collections.sort(stringDefs, adsC);
         for (AdsLocalizingBundleDef def : stringDefs) {
         def.appendTo(xDef.addNewDefinition(), ESaveMode.API);
         ids.add(def.getId().toString());
         }*/
        Collections.sort(ids);
        xDef.setIds(ids);
    }

    private class DefinitionIdComparator<T extends Definition> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }

    void saveUsages(Usages xDef) {
        UsagesSupport support = new UsagesSupport(getModule());
        support.saveUsages(null, xDef);
    }

    public void save(AdsDefinition def, ESaveMode saveMode) throws IOException {
        assert def.getOwnerDefinition() == getModule();

        if (def.getSaveMode() == ESaveMode.API) {//module is readonly
            throw new IOException("Definition " + def.getQualifiedName() + " is readonly");
        }

        File sourceFile = getSourceFile(def, saveMode);

        boolean doNotSave = false;
        if (saveMode == ESaveMode.API) {
            if (sourceFile != null && sourceFile.exists() && sourceFile.lastModified() >= def.getFileLastModifiedTime()) {
                doNotSave = true;
            }

        }

        if (!doNotSave) {
            if (sourceFile == null) {
                throw new IOException("Definition " + def.getQualifiedName() + " is not file-based");
            }
            //FileUtils.mkDirs(sourceFile.getParentFile());
            if (def instanceof AdsLocalizingBundleDef) {
                AdsLocalizingBundleDef localDef = (AdsLocalizingBundleDef) def;
                List<EIsoLanguage> mlStringLangs = localDef.getLanguages();
                List<EIsoLanguage> layerLangs = getLayer().getLanguages();
                if (getLayer().isReadOnly()) {
                    mlStringLangs.removeAll(layerLangs);
                } else {
                    if (!mlStringLangs.containsAll(layerLangs)) {
                        for (EIsoLanguage layerLang : layerLangs) {
                            if (!mlStringLangs.contains(layerLang)) {
                                mlStringLangs.add(layerLang);
                            }
                        }
                    }
                }
                IRepositoryAdsModule moduleRep = getModule().getRepository();
                for (EIsoLanguage lang : mlStringLangs) {
                    IRepositoryAdsDefinition defRep = moduleRep.getDefinitionRepository(def);
                    File file = null;
                    if (defRep != null && (defRep instanceof IRepositoryAdsLocaleDefinition)) {
                        IRepositoryAdsDefinition rep = ((IRepositoryAdsLocaleDefinition) defRep).getRepositories().get(lang);
                        if (rep != null) {
                            file = rep.getFile();// new File(langDir, def.getId().toString() + ".xml"); 
                            if (!layerLangs.contains(lang) && file != null){
                                FileUtils.deleteFile(file.getParentFile());
                                file = null;
                            }
                        }
                    }
                    
                    if (file == null) {
                        file = findFileForLang(localDef, lang, sourceFile);
                    }
                    if (file != null) {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        AdsDefinitionElementType xDef = xDoc.addNewAdsDefinition();
                        xDef.setFormatVersion(def.getFormatVersion());
                        localDef.appendTo(xDef.addNewAdsLocalizingBundleDefinition(), saveMode, lang);
                        save(xDoc, def, saveMode, file);
                        file.setLastModified(def.getFileLastModifiedTime());
                    }
                }
                File srcDir = new File(sourceFile.getParentFile(), AdsModule.SOURCES_DIR_NAME);
                if (srcDir.isDirectory()) {
                    File file = new File(srcDir, def.getId().toString() + ".xml");
                    FileUtils.deleteFile(file);
                }
            } else {
                AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                AdsDefinitionElementType xDef = xDoc.addNewAdsDefinition();
                xDef.setFormatVersion(def.getFormatVersion());
                def.appendTo(xDef, saveMode);

                save(xDoc, def, saveMode, sourceFile);
            }

            if (!(def instanceof AdsLocalizingBundleDef)) {
                AdsLocalizingBundleDef lb = findLocalizingBundleDef(def, false);
                if (lb != null) {
                    save(lb, saveMode);
                }

            }
        }
    }

    private File findFileForLang(final AdsLocalizingBundleDef localDef, final EIsoLanguage lang, final File sourceFile) throws IOException {
        if (localDef.getModule() != null && localDef.getModule().getDirectory() != null) {
            final File modileDir = localDef.getModule().getDirectory();
            final File layerDir = getLayer().getDirectory();
            final File branchDir = layerDir.getParentFile();
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String str = layerDir.getName() + Layer.LOCALE_LAYER_SUFFIX;
                    return name.startsWith(str) && lang.getValue().equals(name.substring(str.length()));
                }
            };
            File[] localizingLayerDirs = branchDir.listFiles(filter);
            if (localizingLayerDirs.length > 0) {
                File localizingLayerDir = localizingLayerDirs[0];
                String licalizingModuleDir = localizingLayerDir.getPath() + File.separatorChar + modileDir.getParentFile().getName() + File.separatorChar
                        + modileDir.getName().substring(modileDir.getName().indexOf(File.separatorChar) + 1) + File.separatorChar;
                String path = licalizingModuleDir + AdsModule.LOCALE_DIR_NAME + File.separatorChar + lang.getValue() + File.separatorChar + localDef.getId() + ".xml";
                File f = new File(new File(licalizingModuleDir), Module.MODULE_XML_FILE_NAME);
                FileUtils.copyFile(localDef.getModule().getFile(), f);
                return new File(path);
            }
        }
        File langDir = new File(sourceFile, lang.getValue());
        return new File(langDir, localDef.getId().toString() + ".xml");
    }

    private void save(AdsDefinitionDocument xDoc, AdsDefinition def, ESaveMode saveMode, File sourceFile) throws IOException {

        synchronized (def) {
            if (def instanceof IXmlDefinition) {
//                        XmlOptions options = new XmlOptions();
//                        options.setSaveOuter();
//                        options.setSaveInner();
                XmlUtils.saveWithoutCR(xDoc, null, sourceFile);
            } else {
                XmlFormatter.save(xDoc, sourceFile);
            }
//xDoc.save(sourceFile);
            if (saveMode == ESaveMode.NORMAL) {
                def.setFileLastModifiedTime(sourceFile.lastModified());
            }
        }
        def.setEditState(EEditState.NONE);
    }

    public File getSourceFile(AdsDefinition def, ESaveMode saveMode) {
        assert def.getOwnerDefinition() == getModule();
        if (saveMode == null) {
            saveMode = def.getSaveMode();
        }
        IRepositoryAdsModule rep = getModule().getRepository();
        if (rep != null) {
            IRepositoryAdsDefinition defRep = rep.getDefinitionRepository(def);
            if (defRep != null) {
                return defRep.getFile(saveMode);
            }
        }
        return null;

    }

    public File[] getDataFiles(AdsDefinition def, ESaveMode saveMode) {
        assert def.getOwnerDefinition() == getModule();
        if (saveMode == null) {
            saveMode = def.getSaveMode();
        }
        IRepositoryAdsModule rep = getModule().getRepository();
        if (rep != null) {
            IRepositoryAdsDefinition defRep = rep.getDefinitionRepository(def);
            if (defRep != null) {
                if (defRep instanceof IRepositoryAdsLocaleDefinition) {
                    List<IRepositoryAdsDefinition> reps = new ArrayList<>(((IRepositoryAdsLocaleDefinition) defRep).getRepositories().values());
                    if (reps.isEmpty()) {
                        return null;
                    } else {
                        File[] files = new File[reps.size()];
                        for (int i = 0; i < files.length; i++) {
                            files[i] = reps.get(i).getFile(saveMode);
                        }
                        return files;
                    }
                }
                return new File[]{defRep.getFile(saveMode)};
            }
        }
        return null;

    }

    @Override
    protected boolean isPersistent() {
        return false;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (!this.isEmpty()) {
            for (AdsDefinition def : this) {
                AdsLocalizingBundleDef bundle = findLocalizingBundleDef(def, false);
                if (bundle != null) {
                    bundle.visit(visitor, provider);
                }
            }
        } else if (getLayer() != null && getLayer().isLocalizing()) {
            for (AdsLocalizingBundleDef bundle : strings) {
                bundle.visit(visitor, provider);
            }
        }
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        CanPasteResult result = super.canPaste(transfers, resolver);
        if (result != CanPasteResult.YES) {
            return result;
        }
        for (Transfer transfer : transfers) {
            final AdsDefinition def = (AdsDefinition) transfer.getObject();
            if (findById(def.getId()) != null) {
                return CanPasteResult.NO_DUPLICATE;
            }
        }
        return CanPasteResult.YES;
    }
}
