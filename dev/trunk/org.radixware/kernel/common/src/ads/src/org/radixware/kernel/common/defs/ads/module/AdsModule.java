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

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.ITopContainer;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.doc.DocReferences;
import org.radixware.kernel.common.defs.ads.doc.DocResources;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.radixdoc.AdsModuleRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport.FileWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.localization.IAdsLocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.UsagesDocument;
import org.radixware.schemas.product.Module.MetaInfServices;
import org.radixware.schemas.radixdoc.Page;

/**
 * An implementation of RadixWare Ads Segment module - named definition
 * container and dependence holder
 *
 */
public class AdsModule extends Module implements IJavaSource, IJavaModule, IRadixdocProvider, IAdsLocalizedDef {

    public static final String SOURCES_DIR_NAME = "src";
    public static final String DOCUMENTATION_DIR_NAME = "doc";
    public static final String DOCUMENTATION_RESOURCE_DIR_NAME = "resource";
    public static final String LOCALE_DIR_NAME = "locale";
    public static final String STRIP_SOURCES_DIR_NAME = "meta";
    public static final String IMAGES_DIR_NAME = "img";
    public static final String JAVA_SOURCES_DIR_NAME = "java_src";
    public static final String GEN_COMMON_DIR_NAME = "common";
    public static final String GEN_SERVER_DIR_NAME = "server";
    public static final String GEN_EXPLORER_DIR_NAME = "explorer";
    public static final String GEN_COMMON_CLIENT_DIR_NAME = "common_client";
    public static final String GEN_COMMON_CLIENT_JAR_NAME = "common-client";
    public static final String GEN_WEB_DIR_NAME = "web";
    public static final String IMG_JAR_NAME = "img.jar";
    public static final String COMMON_JAR_NAME = "common.jar";
    public static final String SERVER_JAR_NAME = "server.jar";
    public static final String EXPLORER_JAR_NAME = "explorer.jar";
    public static final char[] GEN_COMMON_DIR_NAME_C = GEN_COMMON_DIR_NAME.toCharArray();
    public static final char[] GEN_SERVER_DIR_NAME_C = GEN_SERVER_DIR_NAME.toCharArray();
    public static final char[] GEN_EXPLORER_DIR_NAME_C = GEN_EXPLORER_DIR_NAME.toCharArray();
    public static final char[] GEN_COMMON_CLIENT_DIR_NAME_C = GEN_COMMON_CLIENT_DIR_NAME.toCharArray();
    public static final char[] GEN_WEB_DIR_NAME_C = GEN_WEB_DIR_NAME.toCharArray();
    public static final String BINARIES_DIR_NAME = "bin";
    public static final String BUILD_DIR_NAME = "build";
    public static final String API_FILE_NAME = "api.xml";
    public static final String USAGES_FILE_NAME = "usages.xml";
    public static boolean definitionuploadimportmode = false;

    public static final Id UD_ROLES_MODULE_ID = Id.Factory.newInstance(EDefinitionIdPrefix.MODULE);
    private Id companionModuleId;

    public static class Factory extends Module.Factory<AdsModule> {

        private Factory() {
        }

        @Override
        public AdsModule newInstance(Id moduleId, String moduleName) {
            return new AdsModule(moduleId, moduleName);
        }
        private static final Factory FACTORY_INSTANCE = new Factory();

        public static Factory getDefault() {
            return FACTORY_INSTANCE;
        }
    }
    private ModuleDefinitions definitions;
    private ModuleImages images;
    private ModuleDocumentation documentation = new ModuleDocumentation(this);
    private MetaInfServicesCatalog servicesCatalog = null;
    private boolean isUnderConstruction = false;

    protected AdsModule(Id id, String name) {
        super(id, name);
    }

    @Override
    protected void loadFrom(org.radixware.schemas.product.Module xModule) {
        super.loadFrom(xModule);
        if (xModule.isSetCompanionId()) {
            this.companionModuleId = xModule.getCompanionId();
        }

        final MetaInfServices services = xModule.getMetaInfServices();
        if (services != null) {
            synchronized (servicesCatalogLock) {
                servicesCatalog = new MetaInfServicesCatalog(this, services);
            }
        }

        isUnderConstruction = xModule.isSetIsUnderConstruction() ? xModule.getIsUnderConstruction() : false;
    }

    @Override
    public File getBinDirContainer() {
        return getRepository().getBinariesDirContainer();
    }

    @Override
    public File getSrcDirContainer() {
        return getRepository().getJavaSrcDirContainer();
    }

    public boolean isUserModule() {
        return false;
    }

    @Override
    protected void appendTo(org.radixware.schemas.product.Module xModule) {
        super.appendTo(xModule);

        synchronized (servicesCatalogLock) {
            if (servicesCatalog != null && !servicesCatalog.isEmpty()) {
                servicesCatalog.appendTo(xModule.addNewMetaInfServices());
            }
        }
    }
    private final Object servicesCatalogLock = new Object();

    public MetaInfServicesCatalog getServicesCatalog() {
        synchronized (servicesCatalogLock) {
            if (servicesCatalog == null) {
                servicesCatalog = new MetaInfServicesCatalog(this);
            }
        }
        return servicesCatalog;
    }

    public boolean isCompanionOf(Module another) {
        return findCompanionModule() == another;
    }

    public ModuleDocumentation getDocumentation() {
        return documentation;
    }

    public Id getCompanionModuleId() {
        AdsModule ovr = findOverwritten();
        if (ovr != null) {
            return null;
        } else {
            return companionModuleId;
        }
    }

    public void setCompanionModuleId(Id companionModuleId) {
        if (this.companionModuleId != companionModuleId) {
            AdsModule ovr = findOverwritten();
            if (ovr == null) {
                this.companionModuleId = companionModuleId;
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public AdsModule findCompanionModule() {
        AdsModule ovr = findOverwritten();
        if (ovr != null) {
            return null;
        }
        Segment s = getSegment();
        if (s != null) {
            return ((AdsSegment) s).getModules().findById(companionModuleId);
        } else {
            return null;
        }
    }

    @Override
    public ModuleDefinitions getDefinitions() {
        synchronized (this) {
            if (definitions == null) {
                definitions = createDefinitinosList();
            }
            return definitions;
        }
    }

    protected void reset() {
        synchronized (this) {
            if (definitions != null) {
                definitions.reload();
            }
        }
    }

    protected ModuleDefinitions createDefinitinosList() {
        return new ModuleDefinitions(this, true);
    }

    /**
     * For user functions
     */
    protected boolean neverUpload() {
        return false;
    }

    public ModuleDefinitions getDefinitionsIfLoaded() {
        synchronized (this) {
            return definitions;
        }
    }

    public ModuleImages getImages() {
        synchronized (this) {
            if (images == null) {
                images = createImages();
            }
            return this.images;
        }
    }

    protected ModuleImages createImages() {
        return new ModuleImages(this);
    }

    public ModuleImages getImagesIfLoaded() {
        synchronized (this) {
            return images;
        }
    }

    boolean containsDefinitionInRepository(Id id, AdsDefinition[] result) {
        if (definitions == null) {
            final IRepositoryAdsModule repository = getRepository();
            if (repository == null) // by BAO
            {
                return false;
            }
            return repository.containsDefinition(id);
        } else {
            AdsDefinition def = getTopContainer().findById(id);
            result[0] = def;
            return def != null;
        }
    }

    public List<Id> getDefinitionIdsByIdPrefix(EDefinitionIdPrefix prefix) {
        if (definitions == null) {
            final IRepositoryAdsModule repository = getRepository();
            if (repository == null)//by BAO
            {
                return Collections.emptyList();
            }
            return repository.getDefinitionIdsByIdPrefix(prefix);
        } else {
            final List<Id> result = new LinkedList<>();
            if (prefix == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
                for (AdsDefinition def : getDefinitions()) {
                    AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
                    if (bundle != null) {
                        result.add(bundle.getId());
                    }
                }
            } else {
                for (AdsDefinition def : getDefinitions()) {
                    if (def.getId().getPrefix() == prefix) {
                        result.add(def.getId());
                    }
                }
            }
            return result;
        }
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.ADS;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        final Branch branch = getBranch();

        if (branch == null || branch.isClosing()) {
            synchronized (this) {
                if (definitions != null) {
                    definitions.visit(visitor, provider);
                }
                if (images != null) {
                    images.visit(visitor, provider);
                }
            }
        } else {
            getDefinitions().visit(visitor, provider);
            getImages().visit(visitor, provider);
        }
    }

    @Override
    public AdsModule findOverwritten() {
        return (AdsModule) super.findOverwritten();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AdsModule> findAllOverwritten() {
        return (List<AdsModule>) super.findAllOverwritten();
    }

    @Override
    public RadixIcon getIcon() {
        return isTest() ? AdsDefinitionIcon.TEST_MODULE : AdsDefinitionIcon.MODULE;
    }

    @Override
    public DefinitionSearcher<? extends Definition> getDefinitionSearcher() {
        return AdsSearcher.Factory.newAdsDefinitionSearcher(this);
    }

    @Override
    public AdsModule getModule() {
        return this;
    }

    public void reload() throws IOException {
        reloadDescription();
        this.getDefinitions().reload();
        this.getImages().reload();
    }

    public boolean generateAPI(boolean force) throws IOException {
        if (isReadOnly()) {
            return false;
        }
        File apiFile = new File(getDirectory(), AdsModule.API_FILE_NAME);
        boolean update = false;
        if (!force && apiFile.exists()) {
            update = getDefinitions().isModifiedAfter(apiFile.lastModified());
        } else {
            update = true;
        }

        if (update) {
            APIDocument xDoc = APIDocument.Factory.newInstance();
            getDefinitions().saveAPI(xDoc.addNewAPI());
            XmlFormatter.save(xDoc, apiFile);
            //XmlUtils.saveXmlPretty(xDoc, apiFile);
        }
        return update;
    }

    public boolean generateUsages(boolean force) throws IOException {
        if (isReadOnly()) {
            return false;
        }
        File usagesFile = new File(getDirectory(), AdsModule.USAGES_FILE_NAME);
        boolean update = false;
        if (!force && usagesFile.exists()) {
            update = getDefinitions().isModifiedAfter(usagesFile.lastModified());
        } else {
            update = true;
        }

        if (update) {
            final UsagesDocument xDoc = UsagesDocument.Factory.newInstance();
            getDefinitions().saveUsages(xDoc.addNewUsages());
            XmlFormatter.save(xDoc, usagesFile);
        }
        return update;
    }

    @Override
    public IRepositoryAdsModule getRepository() {
        return (IRepositoryAdsModule) super.getRepository();
    }

    protected class AdsModuleClipboardSupport extends ClipboardSupport<AdsModule> {

        public AdsModuleClipboardSupport() {
            super(AdsModule.this);
        }

        private class AdsModuleTransfer extends Transfer<AdsModule> {

            public AdsModuleTransfer(AdsModule radixObject) {
                super(radixObject);
            }

            @Override
            protected boolean beforePaste(RadixObjects container) {
                File adsDir = container.getLayer().getAds().getDirectory();
                if (!adsDir.isDirectory()) {
                    return false;
                }

                if (getInitialObject().findOverwritten() != null) {
                    //module id MUST be unchanged, so pasete allowed into higher layer only
                    if (container.getLayer().getAds().getModules().findById(getInitialObject().getId()) != null) {//module already exists. can not paste
                        return false;
                    }
                    if (!container.getLayer().isHigherThan(getInitialObject().getLayer())) {//overwrite is not possible. continue
                        return false;
                    }
                }

                String name = getInitialObject().getName();
                File moduleDir = new File(adsDir, name);
                int index = 1;
                while (moduleDir.exists()) {
                    String newLocalName = name + String.valueOf(index);
                    moduleDir = new File(adsDir, newLocalName);
                    index++;
                }
                getObject().setName(moduleDir.getName());
                return true;
            }

            @Override
            protected void afterPaste() {
                super.afterPaste();
                AdsModule srcModule = getInitialObject();

                boolean isOverwriteModule = srcModule.findOverwritten() != null;

                for (AdsDefinition def : srcModule.getDefinitions()) {
                    if (isOverwriteModule) {
                        Transferable t = def.getClipboardSupport().createTransferable(ETransferType.COPY);
                        if (t != null) {
                            getObject().getClipboardSupport().paste(t, new DefaultDuplicationResolver());
                        }
                    } else {
                        if (def instanceof AdsEntityClassDef || def instanceof AdsEntityGroupClassDef || def instanceof AdsPresentationEntityAdapterClassDef) {
                            continue;
                        }
                        Transferable t = def.getClipboardSupport().createTransferable(ETransferType.DUPLICATE);
                        if (t != null) {
                            getObject().getClipboardSupport().paste(t, new DefaultDuplicationResolver());
                        }
                    }
                }
            }
        }

        @Override
        protected boolean isIdChangeRequired(RadixObject copyRoot) {
            if (radixObject.findOverwritten() != null) {
                return false;
            } else {
                return super.isIdChangeRequired(copyRoot); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        protected Transfer<AdsModule> newTransferInstance() {
            return new AdsModuleTransfer(radixObject); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected XmlObject copyToXml() {
            final org.radixware.schemas.product.Module xModule = org.radixware.schemas.product.Module.Factory.newInstance();
            AdsModule.this.appendTo(xModule);
            return xModule;
        }

        @Override
        protected AdsModule loadFrom(XmlObject xmlObject) {
            final org.radixware.schemas.product.Module xModule = (org.radixware.schemas.product.Module) xmlObject;
            return Factory.getDefault().loadFrom(xModule);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (getDefinitions().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                return CanPasteResult.YES;
            } else if (getImages().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                return CanPasteResult.YES;
            } else {
                return super.canPaste(transfers, resolver);
            }
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (getDefinitions().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                getDefinitions().getClipboardSupport().paste(transfers, resolver);
            } else if (getImages().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                getImages().getClipboardSupport().paste(transfers, resolver);
            } else {
                super.paste(transfers, resolver);
            }
        }
    }

    @Override
    public ClipboardSupport<? extends AdsModule> getClipboardSupport() {
        return new AdsModuleClipboardSupport();
    }

    @Override
    public String getTypeTitle() {
        return "ADS Module";
    }

    @Override
    public String getTypesTitle() {
        return "ADS Modules";
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JSS(this);
    }

    public char[][] getFactoryClassNameComponents() {
        char[][] components = JavaSourceSupport.getPackageNameComponents(this, false, JavaSourceSupport.UsagePurpose.SERVER_EXECUTABLE);
        char[][] result = new char[components.length + 1][];
        System.arraycopy(components, 0, result, 0, components.length);
        result[components.length] = ModuleFactoryWriter.getFactoryClassName(this).toCharArray();
        return result;
    }

    public String getFactoryClassName() {
        return new String(CharOperations.merge(getFactoryClassNameComponents(), '.'));
    }

    private class JSS extends JavaSourceSupport {

        class FactoryFileWriter extends SourceFileWriter {

            private final AdsModule module;

            public FactoryFileWriter(AdsModule module) {
                this.module = module;
            }

//            @Override
//            public boolean write(File packagesRoot, UsagePurpose purpose, boolean force, IProblemHandler problemHandler, Collection<File> writtenFiles) throws IOException {
//                File packageDir = getPackageDir(packagesRoot, purpose);//
//                File file = new File(packagesRoot, ModuleFactoryWriter.getFactoryClassName(module) + SuffixConstants.SUFFIX_STRING_java);
//                CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
//                if (module.getJavaSourceSupport().getCodeWriter(purpose).writeCode(printer)) {
//                    FileUtils.writeString(file, new String(printer.getContents()), FileUtils.XML_ENCODING);
//                    writtenFiles.add(file);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
            @Override
            public boolean canWrite(ERuntimeEnvironmentType env) {
                return env == ERuntimeEnvironmentType.SERVER;
            }

            @Override
            public IJavaSource getJavaSource() {
                return module;
            }

            @Override
            protected String getDefFileName() {
                return ModuleFactoryWriter.getFactoryClassName(module);
            }
        }

        public JSS(IJavaSource source) {
            super(source);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new ModuleFactoryWriter(this, purpose, AdsModule.this);
        }

        @Override
        public FileWriter getSourceFileWriter() {
            return new FactoryFileWriter(AdsModule.this);
        }

        @Override
        public boolean isSeparateFilesRequired(ERuntimeEnvironmentType sc) {
            return sc == ERuntimeEnvironmentType.SERVER && ModuleFactoryWriter.factoryRequired(AdsModule.this);
        }

        @Override
        public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
            return Collections.singleton(CodeType.EXCUTABLE);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (getCompanionModuleId() != null) {
            AdsModule module = findCompanionModule();
            if (module != null) {
                list.add(module);
            }

        }
    }

    @Override
    protected void saveDescriptionXml(final org.radixware.schemas.product.Module xModule) {
        super.saveDescriptionXml(xModule);
        if (getCompanionModuleId() != null) {
            xModule.setCompanionId(getCompanionModuleId());
        }

        if (servicesCatalog != null && !servicesCatalog.isEmpty()) {
            servicesCatalog.appendTo(xModule.addNewMetaInfServices());
        }

        if (isUnderConstruction) {
            xModule.setIsUnderConstruction(true);
        }
    }

    @Override
    protected void collectAdditionalDependencies(Collection<Definition> deps) {
        if (getCompanionModuleId() != null) {
            AdsModule module = findCompanionModule();
            if (module != null) {
                deps.add(module);
            }
        }
    }

    public boolean isUnderConstruction() {
        return isUnderConstruction;
    }

    public boolean isUnderConstruction(ExtendableDefinitions.EScope scope) {
        if (isUnderConstruction()) {
            return true;
        }
        if (scope == ExtendableDefinitions.EScope.LOCAL) {
            return false;
        }

        final List<AdsModule> allOverwritten = findAllOverwritten();
        if (allOverwritten != null && !allOverwritten.isEmpty()) {
            for (final AdsModule over : allOverwritten) {
                if (over.isUnderConstruction(scope)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setUnderConstruction(boolean on) {
        if (on != isUnderConstruction) {
            isUnderConstruction = on;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isReadOnly(AdsDefinition def) {
        return false;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<Module>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new AdsModuleRadixdoc(AdsModule.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public final AdsLocalizingBundleDef findExistingLocalizingBundle() {
        return findLocalizingBundleImpl(false);
    }

    @Override
    public final AdsLocalizingBundleDef findLocalizingBundle() {
        return findLocalizingBundleImpl(true);
    }

    protected AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        return getDefinitions().findLocalizingBundleDef(this, createIfNotExists);
    }

    @Override
    public Id getLocalizingBundleId() {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + getId().toString());
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        if (descriptionId != null) {
            final IMultilingualStringDef string = findLocalizedString(descriptionId);
            if (string != null) {
                string.setValue(language, description);
                this.setEditState(EEditState.MODIFIED);
                return true;
            }
        }
        return false;
    }

    public ITopContainer getTopContainer() {
        return getDefinitions();
    }

    public void save(AdsDefinition def) throws IOException {
        getDefinitions().save(def, def.getSaveMode());
    }

    public File getSourceFile(AdsDefinition def) {
        return getDefinitions().getSourceFile(def, def.getSaveMode());
    }
}
