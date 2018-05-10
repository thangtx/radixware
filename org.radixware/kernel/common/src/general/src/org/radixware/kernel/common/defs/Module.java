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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * Base class for ads and dds modules
 *
 */
public abstract class Module extends Definition implements IDirectoryRadixObject, ILocalizedDef {

    private final RadixEventSource isTestSupport = new RadixEventSource();
    private final Object renameMutex = new Object();

    public void visivisitAll(VisitorProvider visitorProvider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static class HierarchyWalker<T extends Module> extends org.radixware.kernel.common.defs.HierarchyWalker<T> {

        public HierarchyWalker() {
            super(new Processor<T>() {
                @Override
                public List<T> listBaseObjects(T module) {
                    return (List<T>) module.findAllOverwritten();
                }
            });
        }
    }

    public static abstract class Factory<T extends Module> {

        public abstract T newInstance(Id moduleId, String moduleName);

        public T loadFromRepository(IRepositoryModule<T> moduleRepo) throws IOException {
            final org.radixware.schemas.product.Module xModule;

            try (InputStream stream = moduleRepo.getDescriptionData()) {
                xModule = org.radixware.schemas.product.ModuleDocument.Factory.parse(stream).getModule();
            } catch (XmlException cause) {
                throw new IOException("Unable to load module description.", cause);
            }

            final Id moduleId = Id.Factory.loadFrom(xModule.getId());
            final String moduleName = moduleRepo.getName();
            final T module = newInstance(moduleId, moduleName);
            module.reloadDescriptionFromRepository(moduleRepo, xModule);
            return module;
        }

        public T overwrite(T source) {
            final String newName = source.getName() + ".Overwrite";
            T module = newInstance(source.getId(), newName);
            module.setDescription("Overwrite of " + source.getQualifiedName());
            return module;
        }

        public T newInstance(String moduleName) {
            final Id moduleId = Id.Factory.newInstance(EDefinitionIdPrefix.MODULE);
            final T module = newInstance(moduleId, moduleName);
            return module;
        }

        public T loadFrom(org.radixware.schemas.product.Module xModule) {
            final Id moduleId = Id.Factory.loadFrom(xModule.getId());
            final String name = xModule.getName();
            final T module = newInstance(moduleId, name);
            module.loadFrom(xModule);
            return module;
        }

        public T loadFrom(InputStream moduleXmlInputStream) throws XmlException, IOException {
            org.radixware.schemas.product.Module xModule = org.radixware.schemas.product.ModuleDocument.Factory.parse(moduleXmlInputStream).getModule();
            return loadFrom(xModule);
        }
    }
    public static final String MODULE_XML_FILE_NAME = "module.xml";
    public static final String PREVIEW_DIR_NAME = "preview";
    private Dependences dependences;
    private boolean isTestModule = false;
    private boolean isDeprecated = false;
    private final Object dependencesLock = new Object();
    private boolean isAutoAploadAllowed = true;
    private boolean needsDoc = false;
    protected Id descriptionId;

    protected Module(Id id, String name) {
        super(id, name);
    }

    protected Dependences createDependences() {
        return new Dependences(this);
    }

    public boolean isInjection() {
        return getRepository().isInjection();
    }

    public boolean isAutoAploadAllowed() {
        return isAutoAploadAllowed;
    }

    public void enableAutoUpload(boolean enable) {
        isAutoAploadAllowed = enable;
    }

    public boolean isNeedsDoc() {
        return needsDoc;
    }

    public void setNeedsDoc(boolean needsDoc) {
        if (this.needsDoc != needsDoc) {
            this.needsDoc = needsDoc;
            setEditState(EEditState.MODIFIED);
        }

    }

    @Override
    public boolean setName(String newName) {
        synchronized (renameMutex) {
            final String oldName = getName();
            if (Utils.equals(oldName, newName)) {
                return false;
            }

            IRepositoryModule repository = getRepository();
            File dir = null;
            if (repository != null) {
                dir = repository.getDirectory();
            }
            if (dir != null) {
                try {
                    FileUtils.rename(dir, newName, "");
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to rename module.", this, cause);
                }
            }

            super.setName(newName);

            if (dir != null) {
                try {
                    saveDescription();
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to save module.", this, cause);
                }
            }
            return true;
        }
    }

    public Segment getSegment() {
        final RadixObject modules = getContainer();
        if (modules != null) {
            return (Segment) modules.getContainer();
        } else {
            return null;
        }

    }

    protected void loadFrom(org.radixware.schemas.product.Module xModule) {
        final String name = xModule.getName();
        this.setName(name);

        final String description = xModule.getDescription();
        this.setDescription((description == null ? "" : description));

        if (xModule.isSetDescriptionId()) {
            descriptionId = xModule.getDescriptionId();
        }

        isTestModule = xModule.isSetIsTest() ? xModule.getIsTest() : false;

        if (xModule.isSetNeedsDoc()) {
            needsDoc = xModule.getNeedsDoc();
        }

        if (xModule.getSuppressedWarnings() != null && !xModule.getSuppressedWarnings().isEmpty()) {
            warningsSupport = new ModuleWarningSuppressionSupport(this, xModule);
        }

        getDependencesImpl().loadFrom(xModule.getDependences());

        if (xModule.isSetIsDeprecated()) {
            isDeprecated = xModule.getIsDeprecated();
        }
    }

    private Dependences getDependencesImpl() {
        synchronized (dependencesLock) {
            if (dependences == null) {
                dependences = createDependences();
            }
            return dependences;
        }
    }

    public boolean isTest() {
        return isTestModule;
    }

    public void setIsTest(boolean isTest) {
        if (isTest != isTestModule) {
            isTestModule = isTest;
            isTestSupport.fireEvent(new RadixEvent());
            setEditState(EEditState.MODIFIED);
        }
    }

    public void removeIsTestListener(IRadixEventListener listener) {
        isTestSupport.removeEventListener(listener);
    }

    public void addIsTestListener(IRadixEventListener listener) {
        isTestSupport.addEventListener(listener);
    }

    void reloadDescriptionFromRepository(IRepositoryModule moduleRepo) throws IOException {
        reloadDescriptionFromRepository(moduleRepo, null);
    }

    public void reloadDescriptionFromRepository(IRepositoryModule moduleRepo, final org.radixware.schemas.product.Module xModulePreloaded) throws IOException {
        if (moduleRepo == null) {
            return;
        }
        final File file = moduleRepo.getDescriptionFile();

        long fileTime = -1;
        if (file != null) {
            fileTime = file.lastModified();
        }

        final org.radixware.schemas.product.Module xModule;

        if (xModulePreloaded == null) {
            try {
                try (InputStream stream = moduleRepo.getDescriptionData()) {
                    xModule = org.radixware.schemas.product.ModuleDocument.Factory.parse(stream).getModule();
                }
            } catch (XmlException cause) {
                throw new IOException("Unable to load module description.", cause);
            }
        } else {
            xModule = xModulePreloaded;
        }

        final String moduleName = xModule.getName();
        final String dirName = moduleRepo.getName();
        if (getSegmentType() != ERepositorySegmentType.KERNEL && !Utils.equals(moduleName, dirName)) {
            throw new IOException("Module name '" + moduleName + "' must be equal to module directory '" + dirName + "'.");
            // otherwise - problems in updater.
        }

        final Id moduleId = getId();
        final Id xModuleId = Id.Factory.loadFrom(xModule.getId());

        if (!Utils.equals(moduleId, xModuleId)) {
            throw new IOException("Module identifier '" + moduleId + "' must be equal to module identifier in module.xml file '" + xModuleId + "'.");

        }
        loadFrom(xModule);
        fileLastModifiedTime = fileTime;
        setEditState(EEditState.NONE);
    }

    public IRepositoryModule getRepository() {
        Segment segment = getSegment();
        if (segment != null) {
            final IRepositorySegment segmentRepository = segment.getRepository();
            if (segmentRepository != null) {
                return segmentRepository.getModuleRepository(this);
            }
        }
        return null;
    }

    public void reloadDescription() throws IOException {
        reloadDescriptionFromRepository(getRepository());
    }

    private class OverwrittenLink extends DefinitionLink<Module> {

        @Override
        protected Module search() {
            return collectOverwrittenModules(true, null);
        }
    }

    private class OverwrittenListLink extends DefinitionListLink<Module> {

        private List<Module> computedResults = null;

        @Override
        protected List<Module> search() {
            if (RadixObject.isChangeTrackingEnabled()) {
                computedResults = null;
                List<Module> results = new ArrayList<>(2);
                collectOverwrittenModules(false, results);
                return results;
            } else {
                if (computedResults != null) {
                    return computedResults;
                } else {
                    computedResults = new ArrayList<>(2);
                    collectOverwrittenModules(false, computedResults);
                    return computedResults;
                }
            }

        }
    }

    private Module collectOverwrittenModules(final boolean findFirst, final List<Module> ovrs) {
        final Segment segment = getSegment();
        if (segment == null) {
            return null;
        }

        final Layer thisLayer = segment.getLayer();

        return Layer.HierarchyWalker.walk(thisLayer, new Layer.HierarchyWalker.Acceptor<Module>() {
            @Override
            public void accept(Controller<Module> controller, Layer layer) {
                if (layer == thisLayer) {
                    return;
                }
                final Module module = layer.getSegmentByType(getSegmentType()).getModules().findById(getId());
                if (module != null) {
                    if (findFirst) {
                        controller.setResultAndStop(module);
                    } else {
                        ovrs.add(module);
                        controller.pathStop();
                    }
                }
            }
        });
    }
    private final OverwrittenLink overwrittenLink = new OverwrittenLink();
    private final OverwrittenListLink overwrittenListLink = new OverwrittenListLink();

    public Module findOverwritten() {
        return overwrittenLink.find();
    }

    public List<? extends Module> findAllOverwritten() {
        return overwrittenListLink.find();
    }

    /**
     * @return module segment type: ADS or DDS (kernel segment modules are not
     * supported)
     */
    public abstract ERepositorySegmentType getSegmentType();

    /**
     * @return listLocal of dependence listLocal for current module
     */
    public Dependences getDependences() {
        return getDependencesImpl();
    }

    /**
     * Get module directory location or null if module is not in layer.
     */
    @Override
    public File getDirectory() {
        IRepositoryModule repository = getRepository();
        return repository == null ? null : repository.getDirectory();
    }

    /**
     * Get module description file location or null if module is not in layer.
     */
    @Override
    public File getFile() {
        synchronized (renameMutex) {
            IRepositoryModule repository = getRepository();
            return repository == null ? null : repository.getDescriptionFile();
        }
    }

    protected void appendTo(final org.radixware.schemas.product.Module xModule) {
        xModule.setId(getId().toString());
        xModule.setName(getName());
        xModule.setDescription(getDescription());
        getDependencesImpl().appendTo(xModule);

        if (isDeprecated) {
            xModule.setIsDeprecated(isDeprecated);
        }

        if (descriptionId != null) {
            xModule.setDescriptionId(descriptionId);
        }
    }

    /**
     * Save module.xml file.
     *
     * @throws java.io.IOException
     */
    public void saveDescription() throws IOException {
        IRepositoryModule repository = getRepository();
        final File file = repository == null ? null : repository.getDescriptionFile();
        if (file == null) {
            throw new IOException("The module " + getQualifiedName() + " is not file based");
        }

        final org.radixware.schemas.product.ModuleDocument moduleDoc = org.radixware.schemas.product.ModuleDocument.Factory.newInstance();
        final org.radixware.schemas.product.Module xModule = moduleDoc.addNewModule();
        saveDescriptionXml(xModule);
        XmlFormatter.save(moduleDoc, file);

        this.fileLastModifiedTime = file.lastModified();

        setEditState(EEditState.NONE);
    }

    protected void saveDescriptionXml(final org.radixware.schemas.product.Module xModule) {
        xModule.setId(getId().toString());
        xModule.setName(getName());
        xModule.setDescription(getDescription());
        if (isTestModule) {
            xModule.setIsTest(true);
        }

        if (warningsSupport != null) {
            if (warningsSupport.getSuppressedWarnings().length > 0) {
                List<Integer> arrs = new LinkedList<>();
                for (int i : warningsSupport.getSuppressedWarnings()) {
                    arrs.add(i);
                }
                xModule.setSuppressedWarnings(arrs);
            }
        }

        getDependencesImpl().appendTo(xModule);

        if (needsDoc) {
            xModule.setNeedsDoc(true);
        }
        if (isDeprecated) {
            xModule.setIsDeprecated(isDeprecated);
        }

        if (descriptionId != null) {
            xModule.setDescriptionId(descriptionId);
        }
    }

    /**
     * Save module into its directory (create if it is nessesary)
     */
    @Override
    public void save() throws IOException {
        final File dir = getDirectory();

        if (dir == null) {
            throw new IOException("The module " + getQualifiedName() + " is not file based");
        }

        // FileUtils.mkDirs(dir);
        saveDescription();
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    long fileLastModifiedTime = 0L;

    public long getFileLastModifiedTime() {
        return fileLastModifiedTime;
    }

    @Override
    public boolean delete() {
        File dir = getDirectory();

        if (super.delete()) {
            try {
                FileUtils.deleteFileExt(dir);
            } catch (IOException cause) {
                throw new DefinitionError("Unable to delete module.", this, cause);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isModuleDir(final File dir) {
        final File file = new File(dir, MODULE_XML_FILE_NAME);
        return file.isFile();
    }
    public static final String MODULE_TYPE_TITLE = "Module";

    @Override
    public String getTypeTitle() {
        return MODULE_TYPE_TITLE;
    }
    public static final String MODULES_TYPES_TITLE = "Modules";

    @Override
    public String getTypesTitle() {
        return MODULES_TYPES_TITLE;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.MODULE;
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return null;
    }

    public abstract DefinitionSearcher<? extends Definition> getDefinitionSearcher();

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getDependencesImpl().visit(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        for (Dependence dependence : getDependences()) {
            final List<Module> m = dependence.findDependenceModule(this);
            if (m != null) {
                for (Module module : m) {
                    list.add(module);
                }
            }
        }
        final Module overwritten = findOverwritten();
        if (overwritten != null) {

            list.add(overwritten);

        }
    }

    @Override
    public Module getModule() {
        return this;
    }

    protected void collectAdditionalDependencies(Collection<Definition> deps) {
    }

    public static class ModuleWarningSuppressionSupport extends RadixProblem.WarningSuppressionSupport {

        public static final int DEPENDS_FROM_TEST_MODULE = 100;

        public ModuleWarningSuppressionSupport(Module owner) {
            super(owner);
        }

        public ModuleWarningSuppressionSupport(Module owner, org.radixware.schemas.product.Module xModule) {
            super(owner);
            if (xModule.isSetSuppressedWarnings()) {
                List<Integer> ints = new LinkedList<>();
                for (Object obj : xModule.getSuppressedWarnings()) {
                    ints.add((Integer) obj);
                }
                int[] intArr = new int[ints.size()];
                for (int i = 0; i < ints.size(); i++) {
                    intArr[i] = ints.get(i);
                }
                this.setSuppressedWarnings(intArr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            return code == DEPENDS_FROM_TEST_MODULE;
        }
    }
    private ModuleWarningSuppressionSupport warningsSupport = null;

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new ModuleWarningSuppressionSupport(this);
            }
            return warningsSupport;
        }
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);

            visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof Definition) {
                        radixObject.fireNameChange();
                    }
                }
            }, VisitorProviderFactory.createDefaultVisitorProvider());
        }
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

    @Override
    public Id getDescriptionId() {
        return descriptionId;
    }

    @Override
    public void setDescriptionId(Id id) {
        if (id != descriptionId) {
            descriptionId = id;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        if (descriptionId != null) {
            final IMultilingualStringDef string = findLocalizedString(descriptionId);
            return string == null ? null : string.getValue(language);
        } else {
            return null;
        }
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        return false;
    }

    @Override
    public void collectUsedMlStringIds(Collection<ILocalizedDef.MultilingualStringInfo> ids) {
        ids.add(new ILocalizedDef.MultilingualStringInfo(this) {
            @Override
            public String getContextDescription() {
                return "Desctription of " + getTypeTitle() + " " + getQualifiedName();
            }

            @Override
            public Id getId() {
                return descriptionId;
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PUBLIC;
            }

            @Override
            public void updateId(Id newId) {
                descriptionId = newId;
            }

            @Override
            public boolean isPublished() {
                return true;
            }

            @Override
            public EMultilingualStringKind getKind() {
                return EMultilingualStringKind.DESCRIPTION;
            }
        });
    }
}
