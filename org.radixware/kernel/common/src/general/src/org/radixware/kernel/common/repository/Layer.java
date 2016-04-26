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
package org.radixware.kernel.common.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELayerTrunkKind;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.fs.IRepositoryBranch;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.AccessibleDefinitions;
import org.radixware.schemas.product.Licenses;
import org.radixware.schemas.product.TargetDatabases;

public class Layer extends RadixObject implements IDirectoryRadixObject {

    private static final int DEFAULT_RADIXDOC_FORMAT_VERSION = 0;

    private static class BinaryCompatibilityOptions {

        private List<String> ignorePackages;

        public void loadFrom(org.radixware.schemas.product.BinaryCompatibilityOptions xDef) {
            if (xDef == null) {
                return;
            }
            org.radixware.schemas.product.BinaryCompatibilityOptions.IgnorePackages xPackages = xDef.getIgnorePackages();
            if (xPackages != null && xPackages.getPackageList() != null) {
                for (org.radixware.schemas.product.BinaryCompatibilityOptions.IgnorePackages.Package xPackage : xPackages.getPackageList()) {
                    if (ignorePackages == null) {
                        ignorePackages = new LinkedList<>();
                    }
                    if (!ignorePackages.contains(xPackage.getValue())) {
                        ignorePackages.add(xPackage.getValue());
                    }
                }
            }
        }

        public void appendTo(org.radixware.schemas.product.Layer xLayer) {
            if (ignorePackages != null && !ignorePackages.isEmpty()) {
                org.radixware.schemas.product.BinaryCompatibilityOptions xDef = xLayer.addNewBinaryCompatibilityOptions();
                org.radixware.schemas.product.BinaryCompatibilityOptions.IgnorePackages xPackages = xDef.addNewIgnorePackages();
                for (String pkg : ignorePackages) {
                    xPackages.addNewPackage().setValue(pkg);
                }
            }
        }
    }

    public static final class RootDefinition {

        public final String layerUri;
        public final Id defId;

        public RootDefinition(String LayerUri, Id paragraphId) {
            this.layerUri = LayerUri;
            this.defId = paragraphId;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 17 * hash + Objects.hashCode(this.layerUri);
            hash = 17 * hash + Objects.hashCode(this.defId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RootDefinition other = (RootDefinition) obj;
            if (!Objects.equals(this.layerUri, other.layerUri)) {
                return false;
            }
            if (!Objects.equals(this.defId, other.defId)) {
                return false;
            }
            return true;
        }
    }

    public static final class RootDefinitionList implements Iterable<RootDefinition> {

        private final List<RootDefinition> paragraphs;
        private final Layer layer;

        private RootDefinitionList(Layer layer) {
            paragraphs = new ArrayList<>();
            this.layer = layer;
        }

        @Override
        public Iterator<RootDefinition> iterator() {
            return paragraphs.iterator();
        }

        public int size() {
            return paragraphs.size();
        }
        private boolean isInherited;

        public boolean isInherited() {
            return isInherited;
        }

        public void setInherited(boolean inherited) {
            if (inherited) {
                paragraphs.clear();
            }
            isInherited = inherited;

            layer.setEditState(EEditState.MODIFIED);
        }

        public void setRoots(Collection<RootDefinition> roots) {
            setInherited(false);
            paragraphs.clear();
            if (roots != null) {
                paragraphs.addAll(roots);
            }

            layer.setEditState(EEditState.MODIFIED);
        }

        public boolean isEmpty() {
            return paragraphs.isEmpty();
        }

        public Map<String, Set<Id>> getRootMap() {
            if (isEmpty()) {
                return Collections.EMPTY_MAP;
            }

            final Map<String, Set<Id>> rootsMap = new HashMap<>();
            for (final Layer.RootDefinition rootParagraph : paragraphs) {
                Set<Id> ids = rootsMap.get(rootParagraph.layerUri);
                if (ids == null) {
                    ids = new HashSet<>();
                    rootsMap.put(rootParagraph.layerUri, ids);
                }
                ids.add(rootParagraph.defId);
            }

            return rootsMap;
        }

        public List<RootDefinition> asList() {
            return Collections.unmodifiableList(paragraphs);
        }

        public void load(AccessibleDefinitions roots) {
            if (roots != null) {
                final List<org.radixware.schemas.product.AccessibleDefinitions.Root> rootList = roots.getRootList();
                if (rootList != null) {
                    final List<RootDefinition> paragraphs = new ArrayList<>();
                    for (org.radixware.schemas.product.AccessibleDefinitions.Root root : rootList) {
                        final Id rootId = root.getId();
                        final String uri = root.getLayerUri();
                        if (rootId != null && uri != null) {
                            paragraphs.add(new RootDefinition(uri, rootId));
                        }
                    }

                    setRoots(paragraphs);
                }
            } else {
                setInherited(true);
            }
        }

        public void save(org.radixware.schemas.product.Layer xLayer) {
            if (!isInherited()) {
                org.radixware.schemas.product.AccessibleDefinitions roots = xLayer.addNewAccessibleRoots();
                if (!isEmpty()) {
                    for (final RootDefinition paragraph : this) {
                        org.radixware.schemas.product.AccessibleDefinitions.Root root = roots.addNewRoot();
                        root.setId(paragraph.defId);
                        root.setLayerUri(paragraph.layerUri);
                    }
                } else {
                    roots.setEmpty(true);
                }
            }
        }
    }
    public static final String ORG_RADIXWARE_LAYER_URI = "org.radixware";
    public static final String LOCALE_LAYER_SUFFIX = "$locale-";

    public static class Factory {

        private Factory() {
        }

        public static Layer newInstance() {
            return new Layer();
        }

        public static Layer loadFrom(org.radixware.schemas.product.Layer xLayer) {
            Layer layer = new Layer();
            layer.loadFrom(xLayer);
            return layer;
        }

        public static Layer loadFrom(InputStream layerXmlInputStream) throws XmlException, IOException {
            final org.radixware.schemas.product.Layer xLayer = org.radixware.schemas.product.LayerDocument.Factory.parse(layerXmlInputStream).getLayer();
            return loadFrom(xLayer);
        }

        public static Layer loadFromRepository(IRepositoryLayer layerDir) throws IOException {
            assert layerDir != null;

            Layer layer = new Layer();
            layer.reloadDescriptionFromRepository(layerDir);
            return layer;
        }
    }

    public static class HierarchyWalker extends org.radixware.kernel.common.defs.HierarchyWalker<Layer> {

        public interface DefaultAcceptor extends AbstractDefaultAcceptor<Layer> {
        }

        public interface Acceptor<T> extends org.radixware.kernel.common.defs.HierarchyWalker.Acceptor<Layer, T> {
        }

        public HierarchyWalker() {
            super(new Processor<Layer>() {
                @Override
                public List<Layer> listBaseObjects(Layer layer) {
                    return layer.listBaseLayers();
                }
            });
        }

        public static <T> T walk(Layer root, Acceptor<T> acceptor) {
            return new HierarchyWalker().go(root, acceptor);
        }
    }
    public static final String LAYER_XML_FILE_NAME = "layer.xml";
    private String copyright;
    private String release;
    private List<String> prevLayerUris;
    private String title;
    private String uri;
    private String apiVersion;
    private boolean isCsm;
    private final List<EIsoLanguage> languages = new LinkedList<>();
    private EIsoLanguage defaultLanguage;
    private final Segment ads;
    private final Segment dds;
    private final Segment uds;
    private final Segment kernel;
    private String dbObjectNamesRestriction;
    private final List<TargetDatabase> targetDatabases = new ArrayList<>();
    private License licenses;
    private boolean isLocalizing = false;
    private RootDefinitionList accessibleRoots;
    private RootDefinitionList accessibleRoles;
    private BinaryCompatibilityOptions binaryCompatibilityOptions;

    protected Layer() {
        super("new.layer");
        this.uri = "new.layer";
        this.title = "";
        this.copyright = "shared";
        //this.release = "1.0";
        this.apiVersion = null;
        this.prevLayerUris = null;
        this.ads = createSegment(ERepositorySegmentType.ADS);
        this.dds = createSegment(ERepositorySegmentType.DDS);
        this.uds = createSegment(ERepositorySegmentType.UDS);
        this.kernel = createSegment(ERepositorySegmentType.KERNEL);
        this.languages.add(EIsoLanguage.ENGLISH);
        this.licenses = new License(uri, null, null, null, this);
        dbObjectNamesRestriction = "";

        accessibleRoots = new RootDefinitionList(this);
        accessibleRoles = new RootDefinitionList(this);
    }

    public RootDefinitionList getAccessibleRoots() {
        return accessibleRoots;
    }

    public RootDefinitionList getAccessibleRoles() {
        return accessibleRoles;
    }

    public boolean isLocalizing() {
        return isLocalizing;
    }

    public void setIsLocalizing(boolean isLocalizing) {
        if (isLocalizing != this.isLocalizing) {
            this.isLocalizing = isLocalizing;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected Segment createSegment(ERepositorySegmentType type) {
        SegmentFactory factory = SegmentFactory.getDefault(type);
        return factory == null ? null : factory.newSegment(this);
    }

    protected void loadFrom(final org.radixware.schemas.product.Layer xLayer) {
        if (xLayer.getName() != null) {
            setName(xLayer.getName());
        }
        this.uri = xLayer.getUri();
        this.copyright = xLayer.getCopyright();
        if (xLayer.isSetReleaseNumber()) {
            this.release = xLayer.getReleaseNumber();
        }
        if (xLayer.isSetPrevLayerUri()) {
            this.prevLayerUris = new ArrayList<>(Arrays.asList(new String[]{xLayer.getPrevLayerUri()}));
        } else {
            if (xLayer.isSetBaseLayerURIs()) {
                this.prevLayerUris = new ArrayList<>(xLayer.getBaseLayerURIs());
            } else {
                this.prevLayerUris = new LinkedList<>();
            }
        }

        this.title = xLayer.getTitle();
        this.languages.clear();
        this.apiVersion = xLayer.getAPIVersion();
        this.isCsm = xLayer.getIsCsm();
        if (xLayer.getLanguages() != null) {
            this.languages.addAll(xLayer.getLanguages());
            Collections.sort(this.languages);
        }

        extensions.clear();
        if (xLayer.isSetExtensions()) {
            for (org.radixware.schemas.product.Extensions.Extension ex : xLayer.getExtensions().getExtensionList()) {
                if (!Utils.emptyOrNull(ex.getFile())) {
                    extensions.add(new Extension(ex.getTitle(), ex.getFile(), ex.getInstaller()));
                }
            }
        }

        if (xLayer.isSetDbObjectNamesRestriction()) {
            dbObjectNamesRestriction = xLayer.getDbObjectNamesRestriction();
        }

        if (xLayer.isSetLicenses()) {
            licenses = new License(uri, null, loadLicenses(xLayer.getLicenses().getLicenseList(), uri), null, this);
        } else {
            licenses = new License(uri, null, null, null, this);
        }

        targetDatabases.clear();
        if (xLayer.isSetTargetDatabases()) {
            targetDatabases.addAll(loadTargetDatabases(xLayer));
        }
        if (xLayer.isSetIsLocalizing()) {
            isLocalizing = xLayer.getIsLocalizing();
        }
        accessibleRoots.load(xLayer.getAccessibleRoots());
        accessibleRoles.load(xLayer.getAccessibleRoles());

        if (xLayer.getBinaryCompatibilityOptions() != null) {
            this.binaryCompatibilityOptions = new BinaryCompatibilityOptions();
            this.binaryCompatibilityOptions.loadFrom(xLayer.getBinaryCompatibilityOptions());
        }

        if (xLayer.isSetForRelease() && !xLayer.getForRelease()) {
            isForRelease = false;
        }

        if (xLayer.isSetRadixdocFormatVersion()) {
            radixdocFormatVersion = xLayer.getRadixdocFormatVersion();
        }
    }

    private List<TargetDatabase> loadTargetDatabases(final org.radixware.schemas.product.Layer xLayer) {
        final List<TargetDatabase> loadedTargetDatabases = new ArrayList<>();
        for (org.radixware.schemas.product.TargetDatabase xTargetDb : xLayer.getTargetDatabases().getTargetDatabaseList()) {
            final List<DatabaseOption> options = new ArrayList();
            if (xTargetDb.getOptions() != null) {
                for (org.radixware.schemas.product.TargetDatabase.Options.Option xOption : xTargetDb.getOptions().getOptionList()) {
                    final List<DatabaseOptionDependency> dependencies = new ArrayList<>();
                    if (xOption.getDependencyList() != null) {
                        for (org.radixware.schemas.product.DatabaseOptionDependency xDep : xOption.getDependencyList()) {
                            dependencies.add(new DatabaseOptionDependency(xDep.getOption(), xDep.getDefaultMode(), xDep.getEditable(), xDep.getDefaultUpgradeMode(), xDep.getEditableOnUpgrade()));
                        }
                    }
                    options.add(new DatabaseOption(this, xOption.getName(), xOption.getDescription(), xOption.getDefaultMode(), xOption.getDefaultUpgradeMode(), xOption.getEditableOnUpgrade(), dependencies));
                }
            }
            final List<DatabaseOptionDependency> commonDependencies = new ArrayList<>();
            if (xTargetDb.getDependencies() != null && xTargetDb.getDependencies().getDependencyList() != null) {
                for (org.radixware.schemas.product.DatabaseOptionDependency xDep : xTargetDb.getDependencies().getDependencyList()) {
                    commonDependencies.add(new DatabaseOptionDependency(xDep.getOption(), xDep.getDefaultMode(), xDep.getEditable(), xDep.getDefaultUpgradeMode(), xDep.getEditableOnUpgrade()));
                }
            }
            loadedTargetDatabases.add(new TargetDatabase(this, xTargetDb.getType(), xTargetDb.getSupportedVersions(), options, commonDependencies));
        }
        return loadedTargetDatabases;
    }

    private List<License> loadLicenses(final List<org.radixware.schemas.product.License> xLicenses, final String parentPath) {
        if (xLicenses == null || xLicenses.isEmpty()) {
            return null;
        }
        final List<License> licenses = new ArrayList<>();
        for (org.radixware.schemas.product.License xLicense : xLicenses) {
            final List<String> dependencies;
            if (xLicense.isSetDependencies()) {
                dependencies = Arrays.asList(xLicense.getDependencies().split(","));
            } else {
                dependencies = null;
            }
            licenses.add(new License(xLicense.getName(), parentPath, loadLicenses(xLicense.getLicenseList(), parentPath == null ? xLicense.getName() : parentPath + License.SEPARATOR + xLicense.getName()), dependencies, this));
        }
        return licenses;
    }

    public String getDbObjectNamesRestriction() {
        return dbObjectNamesRestriction;
    }

    public void setDbObjectNamesRestriction(String pattern) {
        if (!Utils.equals(dbObjectNamesRestriction, pattern)) {
            dbObjectNamesRestriction = pattern;
            setEditState(EEditState.MODIFIED);
        }
    }

    private boolean checkLoader(final ClassLoader classLoader) {
        try {
            // for debug only
            final Class<?> clazz = classLoader.loadClass("org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Item");
        } catch (ClassNotFoundException ex) {
            return false;
        }
        return true;
    }

    public class Extension {

        private class ExcensionClassLoader extends ClassLoader {

            private final File jarFilePath;

            public ExcensionClassLoader(File jarFilePath, ClassLoader parent) {
                super(parent);
                this.jarFilePath = jarFilePath;
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    JarFile jar = new JarFile(jarFilePath);
                    try {
                        final Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            final JarEntry e = entries.nextElement();
                            if (e.isDirectory()) {
                                continue;
                            }
                            final String entryName = e.getName();
                            if (entryName.endsWith(".class")) {
                                String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                                if (Utils.equals(name, className)) {
                                    byte[] entryBytes = FileUtils.getZipEntryByteContent(e, jar);
                                    return defineClass(name, entryBytes, 0, entryBytes.length);
                                }
                            }
                        }
                    } finally {
                        try {
                            jar.close();
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return super.findClass(name); //To change body of generated methods, choose Tools | Templates.
            }
        }
        private String title;
        private String file;
        private String installer;
        private ExcensionClassLoader classLoader = null;
        private IExtensionInstaller extensionInstaller = null;

        public Extension(String title, String file, String installer) {
            this.title = title;
            this.file = file;
            this.installer = installer;
        }

        public Layer getLayer() {
            return Layer.this;
        }

        private void initialize() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            classLoader = null;
            extensionInstaller = null;

            final File directory = getDirectory();
            if (directory == null) {
                return;
            }

            File jar = new File(directory, file);
            if (!jar.exists()) {
                throw new IOException("File '" + file + "' does not exists");
            }

            classLoader = new ExcensionClassLoader(jar, Thread.currentThread().getContextClassLoader());
            if (!checkLoader(classLoader)) {
                classLoader = null;
                return;
            }

            if (!Utils.emptyOrNull(installer)) {
                final Class<?> clazz = classLoader.loadClass(uri + "." + installer);
                extensionInstaller = (IExtensionInstaller) clazz.newInstance();
            }
        }

        public ClassLoader getClassLoader() {
            return classLoader;
        }

        public IExtensionInstaller getExtensionInstaller() {
            return extensionInstaller;
        }

        public String getTitle() {
            return title;
        }

        public String getFile() {
            return file;
        }

        public String getInstaller() {
            return installer;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public void setFile(final String file) {
            this.file = file;
        }

        public void setInstaller(final String installer) {
            this.installer = installer;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Extension) {
                Extension e = (Extension) o;
                return Utils.equals(title, e.title)
                        && Utils.equals(file, e.file)
                        && Utils.equals(installer, e.installer);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.title != null ? this.title.hashCode() : 0);
            hash = 83 * hash + (this.file != null ? this.file.hashCode() : 0);
            hash = 83 * hash + (this.installer != null ? this.installer.hashCode() : 0);
            return hash;
        }
    }
    private List<Extension> extensions = new ArrayList<Extension>();

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) throws IOException {
        if (!Utils.equals(this.extensions, extensions)) {
            uninstallExtensions();
            this.extensions = extensions;
            installExtensions();
            setEditState(EEditState.MODIFIED);
        }
    }

    protected void installExtensions() throws IOException {
        for (Extension ex : extensions) {
            try {
                ex.initialize();
                IExtensionInstaller installer = ex.getExtensionInstaller();
                if (installer != null) {
                    installer.install(this);
                }
            } catch (Throwable cause) {
                System.err.println("Layer '" + uri + "' extension '" + String.valueOf(ex.getTitle()) + "' install error: " + cause.getMessage());
                //throw new IOException("Layer '" + uri + "' extension '" + String.valueOf(ex.getTitle()) + "' install error", cause);
            }
        }
    }

    public EIsoLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(EIsoLanguage defaultLanguage) {
        if (this.defaultLanguage != defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected void uninstallExtensions() throws IOException {
        for (Extension ex : extensions) {
            try {
                IExtensionInstaller installer = ex.getExtensionInstaller();
                if (installer != null) {
                    installer.uninstall(this);
                }
            } catch (Exception cause) {
                System.err.println("Layer '" + uri + "' extension '" + String.valueOf(ex.getTitle()) + "' uninstall error: " + cause.getMessage());
//                throw new IOException("Layer '" + uri + "' extension '" + String.valueOf(ex.getTitle()) + "' uninstall error", cause);
            }
        }
    }

    public IRepositoryLayer getRepository() {
        final Branch branch = getBranch();
        if (branch != null) {
            return branch.getRepository().getLayerRepository(this);
        } else {
            return null;
        }
    }

    protected void reloadDescriptionFromRepository(final IRepositoryLayer layerRepository) throws IOException {
        assert (layerRepository != null);

        final File file = layerRepository.getDescriptionFile();
        long fileTime = -1;
        if (file != null) {
            fileTime = file.lastModified();
        }
        final org.radixware.schemas.product.Layer xLayer;
        try {
            InputStream stream = layerRepository.getDescriptionData();
            try {
                xLayer = org.radixware.schemas.product.LayerDocument.Factory.parse(stream).getLayer();
            } finally {
                stream.close();
            }
        } catch (XmlException cause) {
            throw new IOException(RadixResourceBundle.getMessage(Branch.class, "InvalidLayerDescriptionFormat-Exception"), cause);
        }

        final String uriInFile = xLayer.getUri();
        if (!Utils.equals(uriInFile, layerRepository.getName())) {
            throw new IOException("Layer URI '" + this.uri + "' must be equal to its directory name '" + layerRepository.getName() + "'.");
            // otherwise - problems in updater.
        }

        loadFrom(xLayer);
        fileLastModifiedTime = fileTime;
        setEditState(EEditState.NONE);
        getAds().setEditState(EEditState.NONE);
        getDds().setEditState(EEditState.NONE);
        if (getUds() != null) {
            getUds().setEditState(EEditState.NONE);
        }
        if (kernel != null) {
            kernel.setEditState(EEditState.NONE);
        }
    }

    public void reloadDescription() throws IOException {
        reloadDescriptionFromRepository(getRepository());
    }

    /**
     * @return pointer to ads segment for current layer
     */
    public Segment<? extends Module> getAds() {
        return ads;
    }

    public Segment<? extends Module> getKernel() {
        return kernel;
    }

    /**
     * @return pointer to dds segment for current layer
     */
    public Segment<? extends Module> getDds() {
        return dds;
    }

    public Segment<? extends Module> getUds() {
        return uds;
    }

    public Segment<? extends Module> getSegmentByType(ERepositorySegmentType segmentType) {
        switch (segmentType) {
            case ADS:
                return ads;
            case DDS:
                return dds;
            case UDS:
                return uds;
            case KERNEL:
                return kernel;
            default:
                throw new IllegalStateException();
        }
    }

    public License getLicenses() {
        return licenses;
    }

    public void setLicenses(License licenses) {
        if (!Utils.equals(this.licenses, licenses)) {
            this.licenses = licenses;
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<TargetDatabase> getTargetDatabases() {
        if (targetDatabases == null || targetDatabases.isEmpty()) {
            return calcDefaultTargetDatabases();
        }
        return Collections.unmodifiableList(targetDatabases);
    }

    private List<TargetDatabase> calcDefaultTargetDatabases() {
        BigDecimal minVersion = BigDecimal.ZERO;
        if (getBaseLayerURIs() != null && !getBaseLayerURIs().isEmpty()) {
            for (Layer layer : getBranch().getLayers()) {
                if (getBaseLayerURIs().contains(layer.getURI())) {
                    for (TargetDatabase baseTargetDb : layer.getTargetDatabases()) {
                        if (TargetDatabase.ORACLE_DB_TYPE.equals(baseTargetDb.getType())) {
                            if (minVersion.compareTo(baseTargetDb.getMinVersion()) < 0) {
                                minVersion = baseTargetDb.getMinVersion();
                            }
                        }
                    }
                }
            }
        }
        return Collections.singletonList(new TargetDatabase(this, TargetDatabase.ORACLE_DB_TYPE, Collections.singletonList(minVersion), null, Collections.singletonList(new DatabaseOptionDependency(TargetDatabase.PARTITIONING_OPTION, EOptionMode.ENABLED, true, EOptionMode.ENABLED, true))));
    }

    public void setTargetDatabases(final List<TargetDatabase> targetDatabases) {
        if (!Utils.equals(this.targetDatabases, targetDatabases)) {
            this.targetDatabases.clear();
            this.targetDatabases.addAll(targetDatabases);
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Get layer URI.
     */
    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        if (!Utils.equals(this.uri, uri)) {
            this.uri = uri;
            if (this.title == null || this.title.isEmpty()) {
                this.title = uri;
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Get layer title.
     *
     * @return layer title or URI if title doesn't specified.
     */
    public String getTitle() {
        return title == null ? getURI() : title;
    }

    public void setTitle(String title) {
        if (!Utils.equals(this.title, title)) {
            this.title = title;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Map<String, WeakReference<Layer>> linkedBaseLayers = null;

    public List<String> getBaseLayerURIs() {
        return getBaseLayerURIsImpl();
    }

    private Layer findNearestLocalization(String layerUri, EIsoLanguage lang) {
        String exactMatchURI = layerUri + LOCALE_LAYER_SUFFIX + lang.getValue();
        Layer exactMatch = getBranch().getLayers().findByURI(exactMatchURI);
        if (exactMatch != null) {
            return exactMatch;
        }
        Layer layer = getBranch().getLayers().findByURI(layerUri);
        if (layer == null) {
            return null;
        }
        for (Layer base : layer.listBaseLayers()) {
            Layer res = findNearestLocalization(base.getURI(), lang);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    private List<String> getBaseLayerURIsImpl() {
        if (isLocalizing) {
            String uri = getURI();
            if (uri == null) {
                return Collections.emptyList();
            }
            int pos = uri.indexOf("$");
            if (pos <= 0) {
                return Collections.emptyList();
            }
            String baseLayerUri = uri.substring(0, pos);
            List<String> result = new LinkedList<>();
            result.add(baseLayerUri);
            Layer layer = getBranch().getLayers().findByURI(baseLayerUri);
            if (layer == null) {
                return result;
            }
            for (EIsoLanguage lang : getLanguages()) {
                for (String baseUri : layer.getBaseLayerURIs()) {
                    Layer baseLocale = findNearestLocalization(baseUri, lang);
                    if (baseLocale != null) {
                        result.add(baseLocale.getURI());
                    }
                }
            }
            return result;

        } else {
            if (prevLayerUris == null) {
                return Collections.emptyList();
            } else {
                return Collections.unmodifiableList(prevLayerUris);
            }
        }
    }
//    private class Link extends ObjectLink<List<Layer>> {
//
//        private final List<Layer> result = new ArrayList<>(3);
//
//        @Override
//        protected List<Layer> search() {
//            synchronized (Layer.this) {
//                result.clear();
//                if (prevLayerUris == null || prevLayerUris.isEmpty()) {
//                    return result;
//                }
//                if (linkedBaseLayers == null) {
//                    linkedBaseLayers = new HashMap<>();
//                }
//
//
//                for (final String baseUri : prevLayerUris) {
//                    WeakReference<Layer> ref = linkedBaseLayers.get(baseUri);
//                    Layer l = null;
//                    if (ref != null) {
//                        l = ref.get();
//                        if (l == null) {
//                            linkedBaseLayers.remove(baseUri);
//                        }
//                    }
//                    if (l == null) {
//                        l = getBranch().getLayers().findByURI(baseUri);
//                        if (l == null) {
//                            continue;
//                        } else {
//                            linkedBaseLayers.put(baseUri, new WeakReference<>(l));
//                        }
//                    }
//                    result.add(l);
//                }
//                return result;
//            }
//        }
//
//        public List<Layer> get() {
//            List<Layer> list = link.find();
//            if (list == null) {
//                list = link.update();
//            }
//            return list;
//        }
//    }
    //private final Link link = new Link();
    private List<Layer> result = null;
    private final Object baseListLock = new Object();

    final void layersChanged() {
        synchronized (baseListLock) {
            result = null;
        }
    }

    public List<Layer> listFinalBaseLayers() {
        final List<Layer> rez = new ArrayList();
        synchronized (baseListLock) {
            final Layer l = this;
            final Layer.HierarchyWalker w = new Layer.HierarchyWalker();
            w.go(l, new Layer.HierarchyWalker.Acceptor<Module>() {
                @Override
                public void accept(org.radixware.kernel.common.defs.HierarchyWalker.Controller<Module> controller, Layer radixObject) {
                    if (!rez.contains(radixObject)) {
                        rez.add(radixObject);
                    }
                }
            });
        }
        return rez;
    }

    public List<Layer> listBaseLayers() {

        //return new ArrayList<>(link.get());
        synchronized (baseListLock) {
            if (result == null) {
                final List<String> baseLayerUris = getBaseLayerURIsImpl();
                if (baseLayerUris == null || baseLayerUris.isEmpty()) {
                    result = Collections.emptyList();
                } else {
                    result = new ArrayList<>(baseLayerUris.size());
                    if (linkedBaseLayers == null) {
                        linkedBaseLayers = new HashMap<>();
                    }
                    for (final String baseUri : baseLayerUris) {
                        WeakReference<Layer> ref = linkedBaseLayers.get(baseUri);
                        Layer l = null;
                        if (ref != null) {
                            l = ref.get();
                            if (l == null) {
                                linkedBaseLayers.remove(baseUri);
                            }
                        }
                        if (l == null) {
                            final Branch branch = getBranch();
                            if (branch != null) {
                                l = branch.getLayers().findByURI(baseUri);
                                if (l == null) {
                                    continue;
                                } else {
                                    linkedBaseLayers.put(baseUri, new WeakReference<>(l));
                                    result.add(l);
                                }
                            } else {
                                continue;
                            }
                        } else {
                            result.add(l);
                        }
                    }
                }
            }
            return new ArrayList<>(result);
        }
    }

    public boolean addBaseLayer(Layer layer) {
        if (layer == null) {
            return false;
        }

        return addBaseLayer(layer.getURI());

    }

    public boolean addBaseLayer(String uri) {
        synchronized (this) {
            if (prevLayerUris == null || !prevLayerUris.contains(uri)) {
                linkedBaseLayers = null;
                if (prevLayerUris == null) {
                    prevLayerUris = new LinkedList<>();
                }
                prevLayerUris.add(uri);
                layersChanged();
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    public void clearBaseLayers() {
        if (prevLayerUris != null) {
            prevLayerUris.clear();
            layersChanged();
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean removeBaseLayer(Layer layer) {
        if (layer == null) {
            return false;
        } else {
            return removeBaseLayer(layer.getURI());
        }
    }

    public boolean removeBaseLayer(String uri) {
        synchronized (this) {
            if (prevLayerUris != null && prevLayerUris.contains(uri)) {
                prevLayerUris.remove(uri);
                linkedBaseLayers = null;
                layersChanged();
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Find base layer for this one.
     */
//    public Layer findPrevLayer() {
//        if (this.prevLayerUri == null) {
//            return null;
//        }
//        if (linkedPrevLayer == null || linkedPrevLayer.getContainer() != this.getContainer()) { // not linked or removed
//            Branch branch = getBranch();
//            linkedPrevLayer = (branch != null ? branch.getLayers().findByURI(this.prevLayerUri) : null);
//        }
//        return linkedPrevLayer;
//    }
//
//    public String getPrevLayerUri() {
//        return this.prevLayerUri;
//    }
//    public void setPrevLayer(final Layer prevLayer) {
//        final String newPrevLayerUri = prevLayer == null ? null : prevLayer.getURI();
//        if (!Utils.equals(this.prevLayerUri, newPrevLayerUri)) {
//            this.prevLayerUri = newPrevLayerUri;
//            this.linkedPrevLayer = null;
//            setEditState(EEditState.MODIFIED);
//        }
//    }
    /**
     * Get layer directory location or null if layer is not in branch.
     */
    @Override
    public File getDirectory() {
        final IRepositoryLayer layerRepository = getRepository();
        return (layerRepository != null ? layerRepository.getDirectory() : null);
    }

    /**
     * Get layer description file location or null if layer is not in branch.
     */
    @Override
    public File getFile() {
        final IRepositoryLayer layerRepository = getRepository();
        return (layerRepository != null ? layerRepository.getDescriptionFile() : null);
    }

    private String getBaseUriForLocalizingLayer() {
        String uri = getURI();
        if (uri == null) {
            return null;
        }
        int pos = uri.indexOf("$");
        if (pos <= 0) {
            return null;
        }
        return uri.substring(0, pos);
    }

    /**
     * Save the layer to the current directory.
     */
    @Override
    public void save() throws IOException {
        final File dir = getDirectory();

        if (dir == null) {
            throw new IOException("The layer " + getURI() + "is not file-based");
        }

        //FileUtils.mkDirs(dir);
        final org.radixware.schemas.product.LayerDocument layerDoc = org.radixware.schemas.product.LayerDocument.Factory.newInstance();
        final org.radixware.schemas.product.Layer xLayer = layerDoc.addNewLayer();

        xLayer.setCopyright(copyright);
        if (isLocalizing) {
            String baseUri = getBaseUriForLocalizingLayer();
            if (baseUri != null) {
                xLayer.setBaseLayerURIs(Collections.singletonList(baseUri));
            }
        } else {
            if (prevLayerUris != null && !prevLayerUris.isEmpty()) {
                xLayer.setBaseLayerURIs(prevLayerUris);
            }
        }
        if (release != null) {
            xLayer.setReleaseNumber(release);
        }
        xLayer.setUri(uri);
        xLayer.setName(getName());
        xLayer.setTitle(title);
        xLayer.setLanguages(new LinkedList<EIsoLanguage>(languages));
        xLayer.setIsLocalizing(isLocalizing);
        //xLayer.setAPIVersion(getAPIVersion());

        if (extensions != null && !extensions.isEmpty()) {
            org.radixware.schemas.product.Extensions xExtensions = xLayer.addNewExtensions();
            for (Extension ex : extensions) {
                org.radixware.schemas.product.Extensions.Extension xExtension = xExtensions.addNewExtension();
                xExtension.setTitle(ex.title);
                xExtension.setFile(ex.file);
                xExtension.setInstaller(ex.installer);
            }
        }

        if (dbObjectNamesRestriction != null && !dbObjectNamesRestriction.isEmpty()) {
            xLayer.setDbObjectNamesRestriction(dbObjectNamesRestriction);
        }

        if (licenses != null) {
            if (licenses.getChildren() != null && !licenses.getChildren().isEmpty()) {
                Licenses xLicenses = xLayer.addNewLicenses();
                for (License childLicense : licenses.getChildren()) {
                    saveLicense(childLicense, xLicenses.addNewLicense());
                }
            }
        }

        if (!getTargetDatabases().isEmpty()) {
            TargetDatabases xTargetDatabases = xLayer.addNewTargetDatabases();
            for (TargetDatabase targetDb : getTargetDatabases()) {
                org.radixware.schemas.product.TargetDatabase xTargetDb = xTargetDatabases.addNewTargetDatabase();
                xTargetDb.setType(targetDb.getType());
                xTargetDb.setSupportedVersions(StringUtils.join(targetDb.getSupportedVersions(), ","));
                if (targetDb.getOptions() != null && !targetDb.getOptions().isEmpty()) {
                    org.radixware.schemas.product.TargetDatabase.Options xOptions = xTargetDb.addNewOptions();
                    for (DatabaseOption option : targetDb.getOptions()) {
                        org.radixware.schemas.product.TargetDatabase.Options.Option xOption = xOptions.addNewOption();
                        xOption.setName(option.getName());
                        if (option.getDescription() != null) {
                            xOption.setDescription(option.getDescription());
                        }
                        xOption.setDefaultMode(option.getDefaultMode() == null ? EOptionMode.ENABLED : option.getDefaultMode());                        
                        xOption.setDefaultUpgradeMode(option.getDefaultUpgradeMode() == null ? EOptionMode.ENABLED : option.getDefaultUpgradeMode());
                        xOption.setEditableOnUpgrade(option.getEditableOnUpgrade() == null ? true : option.getEditableOnUpgrade());
                        if (option.getDependencies() != null && !option.getDependencies().isEmpty()) {
                            for (DatabaseOptionDependency dep : option.getDependencies()) {
                                org.radixware.schemas.product.DatabaseOptionDependency xDep = xOption.addNewDependency();
                                xDep.setOption(dep.getOptionName());
                                xDep.setDefaultMode(dep.getDefaultMode());
                            }
                        }
                    }
                }
                if (targetDb.getDependencies() != null && !targetDb.getDependencies().isEmpty()) {
                    org.radixware.schemas.product.TargetDatabase.Dependencies xDeps = xTargetDb.addNewDependencies();
                    for (DatabaseOptionDependency dep : targetDb.getDependencies()) {
                        org.radixware.schemas.product.DatabaseOptionDependency xDep = xDeps.addNewDependency();
                        xDep.setOption(dep.getOptionName());
                        xDep.setDefaultMode(dep.getDefaultMode());
                        xDep.setEditable(dep.getEditable());
                        xDep.setDefaultUpgradeMode(dep.getDefaultUpgradeMode());
                        xDep.setEditableOnUpgrade(dep.getEditableOnUpgrade());
                    }
                }
            }
        }
        accessibleRoots.save(xLayer);
        accessibleRoles.save(xLayer);

        if (!isForRelease) {
            xLayer.setForRelease(false);
        }
        if (radixdocFormatVersion != DEFAULT_RADIXDOC_FORMAT_VERSION) {
            xLayer.setRadixdocFormatVersion(radixdocFormatVersion);
        }

        if (binaryCompatibilityOptions != null) {
            binaryCompatibilityOptions.appendTo(xLayer);
        }

        final File file = getFile();

        synchronized (this) {
            XmlUtils.saveXmlPretty(layerDoc, file);//formatter is not used because of release making procedure compatibility requirements
            //XmlFormatter.save(layerDoc, file);
            this.fileLastModifiedTime = file.lastModified();
        }

        setEditState(EEditState.NONE);

        // Create segment directories. With layer, because segments created automatically with layer.
        getAds().save();
        getDds().save();
    }

    private void saveLicense(final License license, final org.radixware.schemas.product.License xLicense) {
        xLicense.setName(license.getOwnName());
        final String depStr = license.getDependenciesAsStr();
        if (depStr != null && !depStr.isEmpty()) {
            xLicense.setDependencies(depStr);
        }
        if (license.getChildren() != null) {
            for (License childLicense : license.getChildren()) {
                saveLicense(childLicense, xLicense.addNewLicense());
            }
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    long fileLastModifiedTime = 0L;

    public long getFileLastModifiedTime() {
        synchronized (this) { // wait for save
            return fileLastModifiedTime;
        }
    }

    // final for optimization
    @Override
    public final Branch getBranch() {
        final RadixObject layers = getContainer();
        if (layers != null) {
            return (Branch) layers.getContainer();
        } else {
            return null;
        }
    } 

    public String getReleaseNumber() {
        return release;
    }

    public void setReleaseNumber(String release) {
        if (!Utils.equals(this.release, release)) {
            this.release = release;
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<EIsoLanguage> getLanguages() {
        return new ArrayList<>(languages);
    }

    public void setLanguages(List<EIsoLanguage> languages) {
        if (!Utils.equals(this.languages, languages)) {
            this.languages.clear();
            if (languages != null) {
                this.languages.addAll(languages);
               // Collections.sort(this.languages);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        if (!Utils.equals(this.copyright, copyright)) {
            this.copyright = copyright;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        dds.visit(visitor, provider);
        ads.visit(visitor, provider);
        if (uds != null) {
            uds.visit(visitor, provider);
        }
        if (kernel != null) {
            kernel.visit(visitor, provider);
        }
    }

    public boolean isHigherThan(Layer anotherLayer) {
        if (anotherLayer == this) {
            return false;
        }
        List<Layer> baseLayers = listBaseLayers();

        for (Layer l : baseLayers) {
            if (anotherLayer == l) {
                return true;
            }
        }
        for (Layer l : baseLayers) {
            if (l.isHigherThan(anotherLayer)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.LAYER;
    }

    @Override
    public boolean isReadOnly() {
        final Branch branch = getBranch();
        if (branch == null) {//object is not in branch we can not control it
            return false;
        } else {
            // for testing
            if (branch.isReadOnly()) {
                return true;
            }
            IRepositoryLayer rep = getRepository();
            if (rep == null || !rep.isReadOnly()) {
                IRepositoryBranch br = branch.getRepository();
                if (br != null && br.isBaseDevUriMeaningful()) {
                    final Layer baseDevLayer = branch.getBaseDevelopmentLayer();
                    if (!(baseDevLayer == this || isHigherThan(baseDevLayer))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

    }
    public static final String LAYER_TYPE_TITLE = "Layer";

    @Override
    public String getTypeTitle() {
        return LAYER_TYPE_TITLE;
    }
    public static final String LAYERS_TYPES_TITLE = "Layers";

    @Override
    public String getTypesTitle() {
        return LAYERS_TYPES_TITLE;

    }

    public static boolean isLayerDir(File dir) {
        final File file = new File(dir, LAYER_XML_FILE_NAME);
        return file.isFile();
    }

    // switch to public access
    @Override
    public boolean setName(String name) {
        return super.setName(name);
    }

    private class LayerClipboardSupport extends ClipboardSupport<Layer> {

        public LayerClipboardSupport() {
            super(Layer.this);
        }

        @Override
        public boolean canCopy() {
            return false;
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (getAds().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                return CanPasteResult.YES;
            } else if (getDds().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                return CanPasteResult.YES;
            } else {
                return super.canPaste(transfers, resolver);
            }
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (getAds().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                getAds().getClipboardSupport().paste(transfers, resolver);
            } else if (getDds().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                getDds().getClipboardSupport().paste(transfers, resolver);
            } else {
                super.paste(transfers, resolver);
            }
        }
    }

    @Override
    public ClipboardSupport<? extends Layer> getClipboardSupport() {
        return new LayerClipboardSupport();
    }

    @Override
    public boolean delete() {
        final File dir = getDirectory();

        if (super.delete()) {
            try {
                FileUtils.deleteFileExt(dir);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to delete layer.", this, cause);
            }
            return true;
        } else {
            return false;
        }
    }

    public String getAPIVersion() {
        return new APIVersionProcessor(apiVersion).getVersion();
    }

    public String getAPIMajorVersion() {
        return new APIVersionProcessor(apiVersion).major;
    }

    public String getAPIMinorVersion() {
        return new APIVersionProcessor(apiVersion).minor;
    }

    private class APIVersionProcessor {

        private final String major;
        private final String minor;

        APIVersionProcessor(String versionString) {
            if (versionString == null || versionString.isEmpty() || versionString.indexOf("/") < 0) {
                Branch branch = getBranch();
                if (branch != null && branch.getLastReleaseVersion() != null && !branch.getLastReleaseVersion().isEmpty()) {
                    major = minor = branch.getLastReleaseVersion();
                } else {
                    major = minor = "0.0";
                }
            } else {
                int index = versionString.indexOf("/");
                major = versionString.substring(0, index);
                minor = versionString.substring(index + 1);
            }
        }

        private String getVersion() {
            return major + "/" + minor;
        }
    }

    public ELayerTrunkKind getTrunkKind() {
        return getBranch().getBaseDistURIs().contains(ORG_RADIXWARE_LAYER_URI) ? ELayerTrunkKind.PRIMARY : ELayerTrunkKind.SECONDARY;
    }

    public boolean isDistributable() {
        return checkDistributable(getBranch().getBaseDistURIs()) && isForRelease();
    }

    private boolean checkDistributable(final List<String> uris) {
        if (uris.contains(getURI())) {
            return true;
        } else {
            List<Layer> prevs = listBaseLayers();
            for (Layer l : prevs) {
                if (l.isDistributable()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return uri;
    }

    public boolean isTrunkLayer() {
        return getTrunkKind() == ELayerTrunkKind.PRIMARY;
    }

    public boolean isCsm() {
        return isCsm;
    }

    public void setCsm(boolean isCsm) {
        if (this.isCsm != isCsm) {
            this.isCsm = isCsm;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean isForRelease = true;
    private int radixdocFormatVersion = DEFAULT_RADIXDOC_FORMAT_VERSION;

    public int getRadixdocFormatVersion() {
        return radixdocFormatVersion;
    }

    public boolean isForRelease() {
        if (!isForRelease) {
            return false;
        }

        for (final Layer layer : listBaseLayers()) {
            if (!layer.isForRelease()) {
                return false;
            }
        }

        return true;
    }

    public void setForRelease(boolean isForRelease) {
        if (this.isForRelease != isForRelease) {
            this.isForRelease = isForRelease;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static class License {

        public static final String SEPARATOR = "/";
        private final Layer layer;
        private final String ownName;
        private final List<License> children;
        private final List<String> dependencies;
        private final String parentFullName;

        public License(final String ownName, final String parentFullName, final List<License> children, final List<String> dependencies, final Layer layer) {
            this.ownName = ownName;
            this.children = children == null ? Collections.<License>emptyList() : Collections.unmodifiableList(new ArrayList<>(children));
            this.dependencies = dependencies == null ? Collections.<String>emptyList() : Collections.unmodifiableList(new ArrayList<>(dependencies));
            this.layer = layer;
            this.parentFullName = parentFullName;
        }

        public List<License> getChildren() {
            return children;
        }

        public String getOwnName() {
            return ownName;
        }

        public String getDependenciesAsStr() {
            if (getDependencies() != null) {
                final StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (String dep : getDependencies()) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                    sb.append(dep);
                }
                if (sb.length() > 0) {
                    return sb.toString();
                }
            }
            return null;
        }

        public String getParentFullName() {
            return parentFullName;
        }

        public String getFullName() {
            return parentFullName == null ? ownName : parentFullName + SEPARATOR + ownName;
        }

        public List<String> getDependencies() {
            return dependencies;
        }

        public Map<String, License> collectFullNameToLicenseMap() {
            final Map<String, License> fullName2License = new HashMap<>();
            collect(this, fullName2License);
            return fullName2License;
        }

        private void collect(final License license, final Map<String, License> fullName2License) {
            if (license == null || fullName2License == null) {
                return;
            }
            fullName2License.put(license.getFullName(), license);
            if (license.getChildren() != null) {
                for (License childLicense : license.getChildren()) {
                    collect(childLicense, fullName2License);
                }
            }
        }

        public Layer getLayer() {
            return layer;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + Objects.hashCode(this.layer);
            hash = 47 * hash + Objects.hashCode(this.ownName);
            hash = 47 * hash + Objects.hashCode(this.children);
            hash = 47 * hash + Objects.hashCode(this.dependencies);
            hash = 47 * hash + Objects.hashCode(this.parentFullName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final License other = (License) obj;
            if (!Objects.equals(this.layer, other.layer)) {
                return false;
            }
            if (!Objects.equals(this.ownName, other.ownName)) {
                return false;
            }
            if (!Objects.equals(this.children, other.children)) {
                return false;
            }
            if (!Objects.equals(this.dependencies, other.dependencies)) {
                return false;
            }
            if (!Objects.equals(this.parentFullName, other.parentFullName)) {
                return false;
            }
            return true;
        }

        public boolean contains(final String licenseFullName) {
            if (licenseFullName == null || licenseFullName.isEmpty()) {
                return false;
            }
            if (licenseFullName.startsWith(getOwnName())) {
                if (getOwnName().equals(licenseFullName)) {
                    return true;
                }
                if (getChildren() != null) {
                    final String subLicenseStr = licenseFullName.substring(getOwnName().length() + 1);
                    for (License childLicense : getChildren()) {
                        if (childLicense.contains(subLicenseStr)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return getFullName() + System.identityHashCode(this);
        }
    }

    public static class DatabaseOptionDependency implements Serializable {

        private final String optionName;        
        private final EOptionMode defaultMode;
        private final Boolean editable;        
        private final EOptionMode defaultUpgradeMode;
        private final Boolean editableOnUpgrade;

        public DatabaseOptionDependency(String optionName, EOptionMode defaultMode) {
            this.optionName = optionName;
            this.defaultMode = defaultMode;            
            this.editable = null;
            this.defaultUpgradeMode = null;
            this.editableOnUpgrade = null;
        }
        
        public DatabaseOptionDependency(String optionName, EOptionMode defaultMode, Boolean editable, EOptionMode defaultUpgradeMode, Boolean editableOnUpgrade) {
            this.optionName = optionName;
            this.defaultMode = defaultMode;
            this.editable = editable;
            this.defaultUpgradeMode = defaultUpgradeMode;
            this.editableOnUpgrade = editableOnUpgrade;
        }

        public String getOptionName() {
            return optionName;
        }

        public EOptionMode getDefaultMode() {
            return defaultMode;
        }
        
        public Boolean getEditable() {
            return editable;
        }

        public EOptionMode getDefaultUpgradeMode() {
            return defaultUpgradeMode;
        }

        public Boolean getEditableOnUpgrade() {
            return editableOnUpgrade;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.optionName);
            hash = 97 * hash + Objects.hashCode(this.defaultMode);
            hash = 97 * hash + Objects.hashCode(this.editable);
            hash = 97 * hash + Objects.hashCode(this.defaultUpgradeMode);
            hash = 97 * hash + Objects.hashCode(this.editableOnUpgrade);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DatabaseOptionDependency other = (DatabaseOptionDependency) obj;
            if (!Objects.equals(this.optionName, other.optionName)) {
                return false;
            }
            if (this.defaultMode != other.defaultMode) {
                return false;
            }
            if (!Objects.equals(this.editable, other.editable)) {
                return false;
            }
            if (this.defaultUpgradeMode != other.defaultUpgradeMode) {
                return false;
            }
            if (!Objects.equals(this.editableOnUpgrade, other.editableOnUpgrade)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "DatabaseOptionDependency{" + "optionName=" + optionName + ", mode=" + defaultMode + '}';
        }
    }

    public static class DatabaseOption {

        public static final String TARGET_DB_VERSION = "DB_VERSION";
        public static final String TARGET_DB_TYPE = "DB_TYPE";
        private final Layer layer;
        private final String name;
        private final String description;
        private final List<DatabaseOptionDependency> dependencies;
        private final String qualifiedName;
        private final EOptionMode defaultMode;
        private final EOptionMode defaultUpgradeMode;
        private final Boolean editableOnUpgrade;

        public DatabaseOption(final Layer layer, String name, String description, final EOptionMode defaultMode, final EOptionMode defaultUpgradeMode, Boolean editableOnUpgrade, List<DatabaseOptionDependency> dependencies) {
            this.layer = layer;
            this.name = name;
            this.dependencies = new ArrayList<>(dependencies == null ? Collections.<DatabaseOptionDependency>emptyList() : dependencies);
            this.qualifiedName = getQualifiedOptionName(layer.getURI(), name);
            this.description = description;
            this.defaultMode = defaultMode;
            this.defaultUpgradeMode = defaultUpgradeMode;
            this.editableOnUpgrade = editableOnUpgrade;
        }

        public Layer getLayer() {
            return layer;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        /**
         * Reserved for future use, now unused.
         *
         * @return
         */
        public List<DatabaseOptionDependency> getDependencies() {
            return dependencies;
        }

        public EOptionMode getDefaultMode() {
            return defaultMode;
        }
        
        public EOptionMode getDefaultUpgradeMode() {
            return defaultUpgradeMode;
        }
        
        public Boolean getEditableOnUpgrade() {
            return editableOnUpgrade;
        }

        /**
         * @return
         * {@linkplain #getQualifiedOptionName(java.lang.String, java.lang.String)}
         */
        public String getQualifiedName() {
            return qualifiedName;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.layer);
            hash = 83 * hash + Objects.hashCode(this.name);
            hash = 83 * hash + Objects.hashCode(this.description);
            hash = 83 * hash + Objects.hashCode(this.dependencies);
            hash = 83 * hash + Objects.hashCode(this.qualifiedName);
            hash = 83 * hash + Objects.hashCode(this.defaultMode);
            hash = 83 * hash + Objects.hashCode(this.defaultUpgradeMode);
            hash = 83 * hash + Objects.hashCode(this.editableOnUpgrade);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DatabaseOption other = (DatabaseOption) obj;
            if (!Objects.equals(this.layer, other.layer)) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.description, other.description)) {
                return false;
            }
            if (!Objects.equals(this.dependencies, other.dependencies)) {
                return false;
            }
            if (!Objects.equals(this.qualifiedName, other.qualifiedName)) {
                return false;
            }
            if (this.defaultMode != other.defaultMode) {
                return false;
            }
            if (this.defaultUpgradeMode != other.defaultUpgradeMode) {
                return false;
            }
            if (!Objects.equals(this.editableOnUpgrade, other.editableOnUpgrade)) {
                return false;
            }
            return true;
        }

        /**
         * @return layerUri + '\' + name
         */
        public static String getQualifiedOptionName(final String layerUri, final String localName) {
            return layerUri + "\\" + localName;
        }
    }

    public static class TargetDatabase {

        public static final String ORACLE_DB_TYPE = "ORACLE";
        public static final BigDecimal MIN_ORACLE_VERSION = BigDecimal.valueOf(111, 1);
        public static final String PARTITIONING_OPTION = "org.radixware\\Partitioning";
        private final String type;
        private final List<BigDecimal> supportedVersions;
        private final List<DatabaseOption> options;
        private final List<DatabaseOptionDependency> dependencies;
        private final Layer layer;
        private String supportedVersionsStr;

        public TargetDatabase(Layer layer, String type, String supportedVersions, List<DatabaseOption> supportedOptions, List<DatabaseOptionDependency> dependencies) {
            this.layer = layer;
            this.type = type;
            this.options = Collections.unmodifiableList(supportedOptions == null ? Collections.<DatabaseOption>emptyList() : new ArrayList<>(supportedOptions));
            this.supportedVersionsStr = supportedVersions;
            supportedVersions = supportedVersions.trim();
            if (supportedVersions != null && !supportedVersions.isEmpty()) {
                String[] versionStrings = supportedVersions.split(",");
                List<BigDecimal> versions = new ArrayList<>();
                for (String str : versionStrings) {
                    BigDecimal verDecimal;
                    try {
                        verDecimal = new BigDecimal(str);
                    } catch (NumberFormatException ex) {
                        verDecimal = BigDecimal.valueOf(-1);
                    }
                    versions.add(verDecimal);
                }
                this.supportedVersions = Collections.unmodifiableList(versions);
            } else {
                this.supportedVersions = Collections.unmodifiableList(Collections.<BigDecimal>emptyList());
            }
            this.dependencies = new ArrayList<>();
            if (dependencies != null) {
                this.dependencies.addAll(dependencies);
            }
        }

        public TargetDatabase(Layer layer, String type, List<BigDecimal> supportedVersions, List<DatabaseOption> supportedOptions, List<DatabaseOptionDependency> dependencies) {
            this.layer = layer;
            this.type = type;
            this.supportedVersions = Collections.unmodifiableList(new ArrayList<>(supportedVersions == null ? Collections.<BigDecimal>emptyList() : supportedVersions));
            this.options = Collections.unmodifiableList(supportedOptions == null ? Collections.<DatabaseOption>emptyList() : new ArrayList<>(supportedOptions));
            this.dependencies = Collections.unmodifiableList(dependencies == null ? Collections.<DatabaseOptionDependency>emptyList() : dependencies);
            this.supportedVersionsStr = StringUtils.join(supportedVersions, ",");
        }

        public Layer getLayer() {
            return layer;
        }

        public BigDecimal getMinVersion() {
            if (supportedVersions.isEmpty()) {
                return BigDecimal.ZERO;
            }
            return supportedVersions.get(0);
        }

        public String getType() {
            return type;
        }

        public List<BigDecimal> getSupportedVersions() {
            return supportedVersions;
        }

        public List<DatabaseOptionDependency> getDependencies() {
            return dependencies;
        }

        public String getSupportedVersionsStr() {
            return supportedVersionsStr;
        }

        public List<DatabaseOption> getOptions() {
            return options;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.type);
            hash = 17 * hash + Objects.hashCode(this.options);
            hash = 17 * hash + Objects.hashCode(this.dependencies);
            hash = 17 * hash + Objects.hashCode(this.supportedVersionsStr);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TargetDatabase other = (TargetDatabase) obj;
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            if (!Objects.equals(this.options, other.options)) {
                return false;
            }
            if (!Objects.equals(this.dependencies, other.dependencies)) {
                return false;
            }
            if (!Objects.equals(this.supportedVersionsStr, other.supportedVersionsStr)) {
                return false;
            }
            return true;
        }
    }
}
