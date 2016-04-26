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
package org.radixware.kernel.common.client.env;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.errors.BranchIsNotAccessibleError;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.starter.meta.DirectoryMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.msdl.MessageElementDocument;
import org.radixware.schemas.product.Definitions;
import org.radixware.schemas.product.DefinitionsDocument;
import org.radixware.schemas.product.Module;
import org.radixware.schemas.product.ModuleDocument;

public class ReleaseRepository extends org.radixware.kernel.release.fs.ReleaseRepository {

    private final Map<Id, MapEntry<String>> images = new HashMap<>();
    private final Map<Id, MapEntry<String>> metaClassNameByDefId;
    private final Map<Id, MapEntry<String>> executableClassNameByDefId;
    private final Map<Id, MapEntry<String>> xmlSchemeFileNamesById = new HashMap<>();
    private final Map<String, MapEntry<Id>> xmlSchemeIdsByTargetNamespace = new HashMap<>();
    private final Map<Id, MapEntry<String>> msdlSchemeFileNamesById = new HashMap<>();
    private final Map<Id, MapEntry<RootMsdlScheme>> msdlScemasById = new HashMap<>();
    private final Map<Id, MapEntry<DefinitionInfo>> definitions = new HashMap<>(512);
    private final IdListsById ancestorsIdsById = new IdListsById();
    private final IdListsById descenderIdsById = new IdListsById();
    private final IdListsById classIdsByDomainId = new IdListsById();
    private final static String DIRECTORY_XML_SHORT_FILE_NAME = "directory.xml";
    private final static String DEFINITIONS_XML_SHORT_FILE_NAME = "definitions.xml";
    private final static String MODULE_XML_SHORT_FILE_NAME = "module.xml";
    private final Object rootMsdlSchemeLock = new Object();
    private final RevisionMeta revisionMeta;
    // private final long revisionVersion;
    private final IClientApplication env;
    private final List<EIsoLanguage> languages;

    IdListsById getAncestorsIdsById() {
        return ancestorsIdsById;
    }

    @Override
    protected RevisionMeta getRevisionMeta() {
        return revisionMeta;
    }

    @Override
    public void processException(Throwable e) {
        env.getTracer().error(env.getMessageProvider().translate("ExplorerError", "Error occurred on loading definition: "), e, EEventSource.CLIENT_DEF_MANAGER);
    }

    public final static class DefinitionInfo {

        public final Id id;
        public final Id moduleId;
        public final String name;
        public final EDefType type;
        public final long subtype;
        public final LayerMeta layer;

        public DefinitionInfo(final Id id,
                final Id moduleId,
                final String name,
                final EDefType type,
                final long subtype,
                final LayerMeta layer) {
            this.id = id;
            this.moduleId = moduleId;
            this.name = name;
            this.type = type;
            this.subtype = subtype;
            this.layer = layer;
        }
    }

    static class MapEntry<T> {

        public final T value;
        public final LayerMeta layer;
        public final String moduleName;

        public MapEntry(T value, String moduleName, LayerMeta layer) {
            this.value = value;
            this.layer = layer;
            this.moduleName = moduleName;
        }
    }

    static class IdListsById {

        private final Map<Id, Collection<Id>> listsById = new HashMap<>();

        void put(final Id id, final Collection<Id> idList) {
            Collection<Id> list = getListById(id);
            for (Id newId : idList) {
                if (!list.contains(newId)) {
                    list.add(newId);
                }
            }
        }

        void put(final Id keyId, final Id valId) {
            Collection<Id> list = getListById(keyId);
            if (!list.contains(valId)) {
                list.add(valId);
            }
        }

        private Collection<Id> getListById(final Id keyId) {
            Collection<Id> lst = listsById.get(keyId);
            if (lst == null) {
                lst = new LinkedList<>();
                listsById.put(keyId, lst);
            }
            return lst;
        }

        Set<Id> keySet() {
            return listsById.keySet();
        }

        Collection<Id> getIdListById(final Id id) {
            if (listsById.containsKey(id)) {
                return Collections.unmodifiableCollection(listsById.get(id));
            } else {
                return Collections.emptyList();
            }
        }
    }

    public ReleaseRepository(IClientApplication env, final RevisionMeta meta, final long version) {
        this.env = env;
        this.revisionMeta = meta;
        //    this.revisionVersion = version;
        List<String> langsAsStr = meta.getLanguages();
        if (langsAsStr != null) {
            this.languages = new ArrayList<>(langsAsStr.size());
            for (String lang : langsAsStr) {
                try {
                    this.languages.add(EIsoLanguage.getForValue(lang));
                } catch (NoConstItemWithSuchValueError ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } else {
            this.languages = Collections.emptyList();
        }

        final List<LayerMeta> layers = new ArrayList<>(meta.getAllLayersSortedFromBottom());
        Collections.reverse(layers);
        if (layers.isEmpty()) {
            metaClassNameByDefId = Collections.emptyMap();
            executableClassNameByDefId = Collections.emptyMap();
        } else {
            metaClassNameByDefId = new HashMap<>();
            executableClassNameByDefId = new HashMap<>();
            for (LayerMeta layerMeta : layers) {
                for (DirectoryMeta seg : layerMeta.getDirectories()) {
                    if (seg.getDirectory() == null || !seg.getDirectory().endsWith("/" + ERepositorySegmentType.ADS.getValue())) {
                        continue;
                    }
                    for (String moduleDirXmlFileName : seg.getIncludes()) {
                        if (!moduleDirXmlFileName.endsWith(DIRECTORY_XML_SHORT_FILE_NAME)) {
                            final String message = env.getMessageProvider().translate("TraceMessage", "Can\'t load modules in %s\n: \"%s\" was expected but was \"%s\"");
                            env.getTracer().error(String.format(message, seg.getDirectory(), DIRECTORY_XML_SHORT_FILE_NAME, moduleDirXmlFileName), (Throwable) null);
                            continue;
                        }
                        final String moduleShortName = moduleDirXmlFileName.substring(0, moduleDirXmlFileName.length() - DIRECTORY_XML_SHORT_FILE_NAME.length() - 1);

                        final String moduleDirName = seg.getDirectory() + "/" + moduleShortName + "/";
                        final String defXmlName = moduleDirName + DEFINITIONS_XML_SHORT_FILE_NAME;
                        final String moduleXmlName = moduleDirName + MODULE_XML_SHORT_FILE_NAME;
                        try {
                            DefinitionInfo moduleInfo = registerModule(getRepositoryFileStream(moduleXmlName), moduleShortName, layerMeta);
//                            makeImagesIndexation(getRepositoryFileStream(seg.getDirectory()+ File.separator +moduleDirXmlFileName),moduleDirName);
                            makeDefinitionsIndexation(getRepositoryFileStream(defXmlName), moduleInfo.id, moduleDirName, moduleShortName, layerMeta);
                        } catch (IOException exception) {
                            final String message = env.getMessageProvider().translate("TraceMessage", "Can\'t load modules in %s\n: Can\'t parse file %s");
                            env.getTracer().error(String.format(message, seg.getDirectory(), defXmlName), exception);
                            continue;
                        } catch (XmlException exception) {
                            final String message = env.getMessageProvider().translate("TraceMessage", "Can\'t load modules in %s\n: Can\'t parse file %s");
                            env.getTracer().error(String.format(message, seg.getDirectory(), defXmlName), exception);
                            continue;
                        }
                    }
                }
            }
        }
    }

    public List<EIsoLanguage> getLanguages() {
        return Collections.unmodifiableList(languages);
    }

    private InputStream getClassLoaderStream(final String fileName) throws RadixLoaderException {
        final ClassLoader loader;
        if (env.getDefManager() != null && env.getDefManager().getClassLoader() != null) {
            loader = env.getDefManager().getClassLoader();
        } else {
            loader = Thread.currentThread().getContextClassLoader();
        }
        final URL url = loader.getResource(fileName);
        if (url == null) {
            throw new RadixLoaderException("File not found: " + fileName);
        }
        final InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException ex) {
            throw new RadixLoaderException("Can't read file \"" + url.toExternalForm() + "\":\n", ex);
            //Environment.messageError("Read Error", "Can't read file \"" + url.toExternalForm() + "\":\n" + ex.getMessage());
            //return null;
        }
        return stream;
    }

    private boolean isTopLevelDefinition(Definitions.Definition definition) {
        return definition.getPath() != null && definition.getPath().size() == 1;
    }

    private DefinitionInfo registerModule(InputStream moduleStream, String moduleName, final LayerMeta layerMeta) throws XmlException, IOException {
        final ModuleDocument moduleDoc = ModuleDocument.Factory.parse(moduleStream);
        final Module module = moduleDoc.getModule();
        final Id moduleId = Id.Factory.loadFrom(module.getId());
        final DefinitionInfo result = new DefinitionInfo(moduleId,
                moduleId,
                module.getName(), EDefType.MODULE,
                -1,
                layerMeta);
        definitions.put(moduleId, new MapEntry<>(result, moduleName, layerMeta));
        return result;
    }
    private final static EnumSet<EDefType> DEF_TYPES_TO_INDEX
            = EnumSet.of(EDefType.CLASS, EDefType.CUSTOM_PAGE_EDITOR);

    private void makeDefinitionsIndexation(final InputStream definitionsStream, final Id moduleId, final String moduleDirName, String moduleShortName, final LayerMeta layerMeta) throws XmlException, IOException {
        final DefinitionsDocument defXml = DefinitionsDocument.Factory.parse(definitionsStream);
        if (defXml.getDefinitions() != null) {
            final List<Definitions.Definition> defList = defXml.getDefinitions().getDefinitionList();
            for (Definitions.Definition def : defList) {
                final List<Id> path = def.getPath();
                final Id id = path.get(path.size() - 1);

                if (isTopLevelDefinition(def) || DEF_TYPES_TO_INDEX.contains(def.getType())) {
                    final long subType = def.isSetSubType() ? def.getSubType() : -1;
                    if (def.getType() != EDefType.DOMAIN || def.getDomains() == null) {
                        checkAndPut(definitions, id, new DefinitionInfo(id, moduleId, def.getName(), def.getType(), subType, layerMeta), moduleShortName, layerMeta, "Definition");
                    }
                }
                if (def.getType() == EDefType.IMAGE) {
                    checkAndPut(images, id, def.getDefFileName(), moduleShortName, layerMeta, "Image");
                } else if (def.getType() == EDefType.XML_SCHEME) {
                    checkAndPut(xmlSchemeFileNamesById, id, def.getDefFileName(), moduleShortName, layerMeta, "XML Scheme");
                    checkAndPut(xmlSchemeIdsByTargetNamespace, def.getXmlTargetNamespace(), id, moduleShortName, layerMeta, "XML Sheme");
                } else if (def.getType() == EDefType.MSDL_SCHEME) {
                    checkAndPut(msdlSchemeFileNamesById, id, def.getDefFileName(), moduleShortName, layerMeta, "MSDL Scheme");
                    //По MSDL-схеме генерируется xsd-схема.
                    checkAndPut(xmlSchemeFileNamesById, id, def.getDefFileName(), moduleShortName, layerMeta, "XML Scheme");
                    checkAndPut(xmlSchemeIdsByTargetNamespace, def.getXmlTargetNamespace(), id, moduleShortName, layerMeta, "XML Scheme");
                }

                if (env.getRuntimeEnvironmentType() == ERuntimeEnvironmentType.EXPLORER) {
                    if (def.isSetExplorerClasses()) {
                        if (makeIndexation(def.getExplorerClasses(), id, moduleShortName, layerMeta)) {
                            makeAncestorsIndexation(def.getExplorerClasses(), id);
                        }
                    }
                } else if (env.getRuntimeEnvironmentType() == ERuntimeEnvironmentType.WEB) {
                    if (def.isSetWebClasses()) {
                        if (makeIndexation(def.getWebClasses(), id, moduleShortName, layerMeta)) {
                            makeAncestorsIndexation(def.getWebClasses(), id);
                        }
                    }
                }

                if (def.isSetCommonClientClasses()) {
                    if (makeIndexation(def.getCommonClientClasses(), id, moduleShortName, layerMeta)) {
                        makeAncestorsIndexation(def.getCommonClientClasses(), id);
                    }
                }
                if (def.isSetCommonClasses()) {
                    if (makeIndexation(def.getCommonClasses(), id, moduleShortName, layerMeta)) {
                        makeAncestorsIndexation(def.getCommonClasses(), id);
                    }
                }
                if (def.isSetServerClasses()) {
                    makeAncestorsIndexation(def.getServerClasses(), id);
                }

                if (def.isSetDomains() && !def.getDomains().isEmpty()) {
                    for (Id domainId : def.getDomains()) {
                        classIdsByDomainId.put(domainId, id);
                    }
                }
            }
        }
    }

    private <K, V> boolean checkAndPut(final Map<K, MapEntry<V>> map, K key, V value, final String currentModule, final LayerMeta currentLayer, final String itemDescription) throws RadixLoaderException {
        final MapEntry existingEntry = map.get(key);
        if (existingEntry != null) {
            //throw exception if defenition is redefinded in parallel layer. 
            //Do not throw exception, if currentLayer is the same as the layer of the existing entry,
            //because it can happen if definition has meta for few environments and we want to use the most specific
            //(the most specific meta is loaded first)
            if (!revisionMeta.isBaseLayer(currentLayer, existingEntry.layer) && (existingEntry.layer != currentLayer)) {
                throw new RadixLoaderException((itemDescription != null && !itemDescription.isEmpty() ? itemDescription + " " : "") + key.toString() + " is ambiguously defined in '" + existingEntry.layer.getUri() + "' and '" + currentLayer.getUri() + "'");
            }
            return false;
        } else {
            map.put(key, new MapEntry<>(value, currentModule, currentLayer));
            return true;
        }
    }

    private void makeAncestorsIndexation(final org.radixware.schemas.product.Classes classes, final Id id) {
        if (classes.isSetExtends()) {
            final List<Id> ancsPath = classes.getExtends().getPath();
            if (ancsPath != null && !ancsPath.isEmpty()) {
                ancestorsIdsById.put(id, ancsPath);
                descenderIdsById.put(ancsPath.get(ancsPath.size() - 1), id);
            }
        }
        if (classes.getImplementsList() != null) {
            for (org.radixware.schemas.product.Classes.Implements _implements : classes.getImplementsList()) {
                if (_implements.isSetPath()) {
                    final List<Id> implPath = _implements.getPath();
                    ancestorsIdsById.put(id, implPath);
                    if (implPath != null && !implPath.isEmpty()) {
                        descenderIdsById.put(implPath.get(implPath.size() - 1), id);
                    }
                }
            }
        }
    }

    private boolean makeIndexation(final org.radixware.schemas.product.Classes classes, final Id id, String moduleName, final LayerMeta layer) throws RadixLoaderException {
        boolean result = false;
        {
            final org.radixware.schemas.product.JavaClasses classesXml = classes.getExecutable();
            if (classesXml != null) {
                final List<org.radixware.schemas.product.JavaClasses.Class> clsXml = classesXml.getClass1List();
                if (!clsXml.isEmpty()) {
                    final String name = clsXml.get(0).getName();
                    result |= checkAndPut(executableClassNameByDefId, id, name, moduleName, layer, "Executable part of");
                }
            }
        }
        {
            final org.radixware.schemas.product.JavaClasses classesXml = classes.getMeta();
            if (classesXml != null) {
                final List<org.radixware.schemas.product.JavaClasses.Class> clsXml = classesXml.getClass1List();
                if (!clsXml.isEmpty()) {
                    final String name = clsXml.get(0).getName();
                    result |= checkAndPut(metaClassNameByDefId, id, name, moduleName, layer, "Meta part of");
                }
            }
        }
        return result;
    }

    public String getExecutableClassNameByDefId(Id id) {
        if (id == null) {
            throw new NullPointerException();
        }
        final MapEntry<String> className = executableClassNameByDefId.get(id);
        if (className == null) {
            throw new DefinitionError("Can\'t find ADS definition runtime for #" + id.toString());
        }
        return className.value;
    }

    public Collection<Id> getExecutableClassIds() {
        return Collections.unmodifiableSet(executableClassNameByDefId.keySet());
    }

    public String getModuleNameByClassId(Id id) {
        final MapEntry<String> classEntry = executableClassNameByDefId.get(id);
        if (classEntry == null) {
            return "";
        } else {
            return classEntry.moduleName;
        }
    }

    public String getLayerNameByClassId(Id id) {
        final MapEntry<String> classEntry = executableClassNameByDefId.get(id);
        if (classEntry == null) {
            return "";
        } else {
            return classEntry.layer.getTitle();
        }
    }

    public String getMetaClassNameByDefId(Id id) {
        if (id == null) {
            throw new NullPointerException();
        }
        final MapEntry<String> className = metaClassNameByDefId.get(id);
        if (className == null) {
            throw new DefinitionError("Can\'t find ADS definition runtime for #" + id.toString());
        }
        return className.value;
    }

    public String getImageFilePath(Id id) {
        if (id == null) {
            throw new NullPointerException();
        }
        final MapEntry<String> fileName = images.get(id);
        if (fileName == null) {
            throw new DefinitionError("Can\'t find image #" + id.toString());
        }
        return fileName.value;
    }

    public InputStream getInputStreamForXmlScheme(Id id) {
        if (id == null) {
            throw new NullPointerException();
        }
        if (!xmlSchemeFileNamesById.containsKey(id)) {
            throw new DefinitionError("Can\'t find xml scheme #" + id.toString());
        }
        try {
            return getClassLoaderStream(xmlSchemeFileNamesById.get(id).value);
        } catch (RadixLoaderException ex) {
            throw new DefinitionError("Can\'t read xml scheme #" + id.toString() + ": " + ex.getMessage());
        }
    }

    public InputStream getInputStreamForXmlScheme(String namespace) {
        final Id schemeId = findXmlSchemeIdByTargetNameSpace(namespace);
        if (schemeId == null) {
            throw new DefinitionError("Can\'t find xml scheme with target namespace \"" + namespace + "\"");
        }
        return getInputStreamForXmlScheme(schemeId);
    }

    public String getXmlSchemeFileName(String namespace) {
        final Id schemeId = findXmlSchemeIdByTargetNameSpace(namespace);
        if (schemeId == null) {
            throw new DefinitionError("Can\'t find xml scheme with target namespace \"" + namespace + "\"");
        }
        return xmlSchemeFileNamesById.get(schemeId).value;
    }

    public InputStream getInputStreamForMsdlScheme(Id id) {
        if (id == null) {
            throw new NullPointerException();
        }
        if (!msdlSchemeFileNamesById.containsKey(id)) {
            throw new DefinitionError("Can\'t find xml scheme #" + id.toString());
        }
        try {
            return getClassLoaderStream(msdlSchemeFileNamesById.get(id).value);
        } catch (RadixLoaderException ex) {
            throw new DefinitionError("Can\'t read xml scheme #" + id.toString() + ": " + ex.getMessage());
        }
    }

    public RootMsdlScheme getRootMsdlSchemeById(final Id schemeId) {
        synchronized (rootMsdlSchemeLock) {
            if (schemeId == null) {
                throw new NullPointerException();
            }

            final MapEntry<RootMsdlScheme> schemaEntry = msdlScemasById.get(schemeId);
            RootMsdlScheme schema = schemaEntry == null ? null : schemaEntry.value;
            if (schema == null) {
                final MessageElementDocument xSchema;

                try {
                    xSchema = MessageElementDocument.Factory.parse(getInputStreamForMsdlScheme(schemeId));
                } catch (XmlException ex) {
                    throw new DefinitionError("Can`t load msdl scheme #" + schemeId.toString() + ": " + ex.getMessage(), ex);
                } catch (IOException ex) {
                    throw new DefinitionError("Can`t load msdl scheme #" + schemeId.toString() + ": " + ex.getMessage(), ex);
                }

                if (xSchema == null || xSchema.getMessageElement() == null) {
                    throw new DefinitionError("Can`t load msdl scheme #" + schemeId.toString() + ": MessageElement is mandatory");
                }

                schema = new RootMsdlScheme(xSchema.getMessageElement());
                MapEntry<String> entry = msdlSchemeFileNamesById.get(schemeId);
                msdlScemasById.put(schemeId, new MapEntry<>(schema, entry.moduleName, entry.layer));
            }
            return schema;
        }
    }

    public DefinitionInfo findDefinitionByName(final String moduleName, final EDefType definitionType, final String definitionName) {
        final Id moduleId = findModuleIdByName(moduleName);
        if (moduleId != null) {
            for (MapEntry<DefinitionInfo> info : definitions.values()) {
                if (moduleId.equals(info.value.moduleId) && definitionName.equals(info.value.name) && definitionType == info.value.type) {
                    return info.value;
                }
            }
        }
        return null;
    }

    public Id findModuleIdByName(final String moduleName) {
        for (MapEntry<DefinitionInfo> info : definitions.values()) {
            if (info.value.type == EDefType.MODULE && moduleName.equals(info.value.name)) {
                return info.value.id;
            }
        }
        return null;
    }

    public Id findXmlSchemeIdByTargetNameSpace(final String namespace) {
        final MapEntry<Id> scheme = xmlSchemeIdsByTargetNamespace.get(namespace);
        return scheme == null ? null : scheme.value;
    }

    public Id findIdByExecutableClassName(final String className) {
        for (Map.Entry<Id, MapEntry<String>> entry : executableClassNameByDefId.entrySet()) {
            if (entry.getValue().value.equals(className)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void fillDefinitionIdsbyPackageName(final String packageName, final Collection<Id> definitionIds) {
        for (Map.Entry<Id, MapEntry<String>> entry : metaClassNameByDefId.entrySet()) {
            if (entry.getValue().value.startsWith(packageName)) {
                definitionIds.add(entry.getKey());
            }
        }
    }

    public Collection<DefinitionInfo> getDefinitions(EDefType type) {
        final Collection<DefinitionInfo> result = new ArrayList<>(256);
        for (MapEntry<DefinitionInfo> info : definitions.values()) {
            if (type == null || info.value.type == type) {
                result.add(info.value);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    public Collection<DefinitionInfo> getDefinitionsForDomain(final Id domainId, EDefType type) {
        final Collection<DefinitionInfo> result = new ArrayList<>(256);
        final Stack<Id> domains = new Stack<>();
        domains.push(domainId);
        Id currentDomainId;
        DefinitionInfo info;
        Collection<Id> classIds;
        while (!domains.isEmpty()) {
            currentDomainId = domains.pop();
            classIds = classIdsByDomainId.getIdListById(currentDomainId);
            for (Id id : classIds) {
                info = definitions.get(id).value;
                if (id.getPrefix() == EDefinitionIdPrefix.DOMAIN) {
                    domains.push(id);
                }
                if (info != null && (type == null || info.type == type)) {
                    result.add(info);
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    public Collection<DefinitionInfo> getDefinitionsForDomain(final Id domainId) {
        return getDefinitionsForDomain(domainId, null);
    }

    public boolean isDefinitionInDomain(final Id definitionId, final Id domainId) {
        final Stack<Id> domains = new Stack<>();
        domains.push(domainId);
        Id currentDomainId;
        DefinitionInfo info;
        Collection<Id> classIds;
        while (!domains.isEmpty()) {
            currentDomainId = domains.pop();
            classIds = classIdsByDomainId.getIdListById(currentDomainId);
            for (Id id : classIds) {
                if (Utils.equals(id, definitionId)) {
                    return true;
                }
                if (id.getPrefix() == EDefinitionIdPrefix.DOMAIN) {
                    domains.push(id);
                }
            }
        }
        return false;
    }

    public Collection<Id> getDefinitionIdsForJarFiles(final Set<String> jarFiles) {
        final Collection<Id> result = new ArrayList<>();
        final Collection<String> packages = new ArrayList<>();
        for (String jarFileName : jarFiles) {
            revisionMeta.getStorePackages(jarFileName, packages);
        }
        String packageName;
        for (String packagePath : packages) {
            packageName = packagePath.replace('/', '.').replace('\\', '.');
            fillDefinitionIdsbyPackageName(packageName, result);
        }
        return Collections.unmodifiableCollection(result);
    }

    public boolean isClassExtends(final Id derivedClassId, final Id ancestorClassId) {
        Collection<Id> direct_ancestors;
        final Stack<Id> stack = new Stack<>();
        stack.push(derivedClassId);
        Id currentId;
        while (!stack.isEmpty()) {
            currentId = stack.pop();
            direct_ancestors = ancestorsIdsById.getIdListById(currentId);
            if (direct_ancestors.contains(ancestorClassId)) {
                return true;
            } else if (!direct_ancestors.isEmpty()) {
                stack.addAll(direct_ancestors);
            }
        }
        return false;
    }

    public Id getAncestorId(Id classId) {
        Collection<Id> ancestorIds = ancestorsIdsById.getIdListById(classId);
        if (ancestorIds.isEmpty()) {
            return null;
        }
        for (Id id : ancestorIds) {
            if (id.getPrefix() != EDefinitionIdPrefix.ADS_INTERFACE_CLASS) {
                return id;
            }
        }
        return null;
    }

    public Collection<Id> getDirectDescenders(final Id classId) {
        return descenderIdsById.getIdListById(classId);
    }

    public Collection<Id> getDescendantsRecursively(final Id classId) {
        Collection<Id> direct_descenders;
        final List<Id> allDescenders = new LinkedList<>();
        final Stack<Id> stack = new Stack<>();
        stack.push(classId);
        Id currentId;
        while (!stack.isEmpty()) {
            currentId = stack.pop();
            direct_descenders = descenderIdsById.getIdListById(currentId);
            allDescenders.addAll(direct_descenders);
            stack.addAll(direct_descenders);
        }
        return Collections.<Id>unmodifiableCollection(allDescenders);
    }

    @Override
    public void close() {
        super.close();
    }
    
    @Override
    public Branch getBranch() throws IOException {
        if (!env.isExtendedMetaInformationAccessible()){
            throw new BranchIsNotAccessibleError();
        }
        return super.getBranch();
    }
}
