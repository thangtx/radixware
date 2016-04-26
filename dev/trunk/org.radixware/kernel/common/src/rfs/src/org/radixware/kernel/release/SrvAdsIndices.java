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

package org.radixware.kernel.release;

import java.io.IOException;
import java.util.*;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.release.fs.ReleaseRepository;
import org.radixware.kernel.starter.meta.DirectoryMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.product.Classes;
import org.radixware.schemas.product.Classes.Implements;
import org.radixware.schemas.product.Definitions.Definition;
import org.radixware.schemas.product.DefinitionsDocument;
import org.radixware.schemas.product.JavaClasses;
import org.radixware.schemas.product.UserReference;

public class SrvAdsIndices {

    private final Map<Id, DefMeta> metaByDefId;
    private final DefIdsByType defIdsByType;
    private final IdListsById descenderIdsByClassId;
    private final IdListsById defIdsByDomainId;
    private final IdListsByName defIdsByModule;
    private final IdListsByName defIdsByLayer;
    private final Map<Id, Collection<EntityUserReference>> entityUserReferences;
    private final Map<String, EventCodeMeta> eventCodes;
    private final Set<String> executableClasses;
    private Set<Id> loadOnStartupClassIds;
    Collection<Id> rootDomainIds;

    public SrvAdsIndices(final RevisionMeta revisionMeta, final ReleaseRepository repository, final LocalTracer tracer) {
        super();
        final List<LayerMeta> layers = new ArrayList<>(revisionMeta.getAllLayersSortedFromBottom());
        Collections.reverse(layers);
        defIdsByType = new DefIdsByType();
        descenderIdsByClassId = new IdListsById();
        defIdsByDomainId = new IdListsById();
        rootDomainIds = new LinkedList<>();
        loadOnStartupClassIds = new HashSet<>();
        defIdsByLayer = new IdListsByName();
        defIdsByModule = new IdListsByName();
        eventCodes = new HashMap<>();
        executableClasses = new HashSet<>(5000);
        final Map<Id, LayerMeta> definingExLayer = new HashMap<>();//for class executable part ambiguity detection
        final Map<Id, LayerMeta> definingMetaLayer = new HashMap<>();//for class meta part ambiguity detection
        final Map<String, LayerMeta> definingLayer = new HashMap<>();//for other defs ambiguity detection
        final Map<Id, Collection<EntityUserReference>> userRefs = new HashMap<>();
        if (layers.isEmpty()) {
            metaByDefId = Collections.emptyMap();
        } else {
            metaByDefId = new HashMap<>();
            for (LayerMeta layerMeta : layers) {
                for (DirectoryMeta seg : layerMeta.getDirectories()) {
                    if (seg.getDirectory() == null || !seg.getDirectory().endsWith("/" + ERepositorySegmentType.ADS.getValue())) {
                        continue;
                    }
                    for (String moduleDirXmlFileName : seg.getIncludes()) {
                        if (!moduleDirXmlFileName.endsWith(DirectoryMeta.DIRECTORY_XML_FILE)) {
                            throw new WrongFormatError("Can\'t load modules: \"directory.xml\" was expected but was \"" + moduleDirXmlFileName + "\"");
                        }
                        final String moduleName = moduleDirXmlFileName.substring(0, moduleDirXmlFileName.length() - DirectoryMeta.DIRECTORY_XML_FILE.length() - 1);
                        final String moduleDirName = seg.getDirectory() + "/" + moduleName + "/";
                        final String defXmlName = moduleDirName + "definitions.xml";
                        try {
                            //parsing module's definitions.xml
                            final DefinitionsDocument defXml = DefinitionsDocument.Factory.parse(repository.getRepositoryFileStream(defXmlName));
                            if (defXml.getDefinitions() != null) {
                                final String factoryClassName = defXml.getDefinitions().getServerFactory();
                                final List<Definition> defList = defXml.getDefinitions().getDefinitionList();
                                for (Definition def : defList) {
                                    final List<Id> path = def.getPath();
                                    if (path != null && path.size() == 1) {
                                        //we need only first level definitions' info for server definitions loading
                                        final Id id = path.get(0);
                                        switch (id.getPrefix()) {
                                            case ADS_PRESENTATION_CLASS:
                                            case ADS_DIALOG_MODEL_CLASS:
                                            case ADS_ENTITY_MODEL_CLASS:
                                            case ADS_FORM_MODEL_CLASS:
                                            case ADS_GROUP_MODEL_CLASS:
                                            case ADS_PARAGRAPH_MODEL_CLASS:
                                            case ADS_PRESENTATION_EXCEPTION_CLASS:
                                            case ADS_PRESENTATION_INTERFACE_CLASS:
                                            case ADS_PROP_EDITOR_MODEL_CLASS:
                                            case CUSTOM_DIALOG:
                                            case CUSTOM_EDITOR:
                                            case CUSTOM_PARAG_EDITOR:
                                            case CUSTOM_EDITOR_PAGE:
                                            case CUSTOM_FORM_DIALOG:
                                            case CUSTOM_PROP_EDITOR:
                                            case CUSTOM_SELECTOR:
                                            case CUSTOM_WIDGET:
                                            case WIDGET:
                                            case EDITOR_PAGE:
                                                //RADIX-1813: skip presentation definitions
                                                continue;
                                            case ADS_DYNAMIC_CLASS:
                                            case ADS_INTERFACE_CLASS:
                                            case ADS_EXCEPTION_CLASS:
                                            case ADS_PREDEFINED_CLASS:
                                                //RADIX-1813: skip presentation definitions
                                                if (!def.isSetCommonClasses() && !def.isSetServerClasses()) {
                                                    continue;
                                                }
                                                break;
                                            default:
                                            //go on
                                        }
                                        defIdsByLayer.put(layerMeta.getUri(), id);
                                        defIdsByModule.put(moduleName, id);
//                                       
                                        DefMeta meta = metaByDefId.get(id);

                                        if (meta == null) {
                                            meta = new DefMeta();
                                            metaByDefId.put(id, meta);
                                            definingLayer.put(getDefKey(def), layerMeta);
                                        }

                                        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE && def.isSetEventCodes()) {//event codes are aggregated from all layers
                                            for (Definition.EventCodes.EventCode xCode : def.getEventCodes().getEventCodeList()) {
                                                final EventCodeMeta eventCodeMeta = new EventCodeMeta(id, xCode.getId(), xCode.getEventSource(), xCode.getEventSeverity());
                                                if (!eventCodes.containsKey(eventCodeMeta.getQualifiedId())) {
                                                    eventCodes.put(eventCodeMeta.getQualifiedId(), eventCodeMeta);
                                                }
                                            }
                                        }

                                        if (defIdsByType.getDefIdsByType(def.getType()).contains(id)) {
                                            if (def.getType() == EDefType.CLASS) {
                                                //we should process this case further:
                                                //Meta and executable part of the class can be defined in different layers (meta can be defined in higer layer)
                                                //so we have to load info about class definition in two steps - at the meta definition layer, and executable part definition layer.
                                                //Also, load user references that are defined at the bottom layers (user references are not copied to the higher layer on overwrite).
                                                //Plus, ambiguity check for classes is a bit more complicated
                                            } else {
                                                final LayerMeta alreadyDefinedIn = definingLayer.get(getDefKey(def));
                                                if (!revisionMeta.isBaseLayer(layerMeta, alreadyDefinedIn)) {
                                                    ambigiousException(id, null, layerMeta, alreadyDefinedIn);
                                                } else {
                                                    //this definition is overwritten and already processed
                                                    continue;
                                                }
                                            }
                                        }

                                        if (def.isSetUserReferences()) {
                                            for (UserReference xRef : def.getUserReferences().getReferenceList()) {
                                                Collection<EntityUserReference> references = userRefs.get(xRef.getTableId());
                                                if (references == null) {
                                                    references = new ArrayList<>();
                                                    userRefs.put(xRef.getTableId(), references);
                                                }
                                                references.add(new EntityUserReference(xRef.getTableId(), xRef.getUserPropId(), xRef.getType(), xRef.getDeleteMode(), xRef.getConfirmationRequired()));
                                            }
                                        }

                                        //do we actually need to load definition or we must only perform specific conflicts checking for classes
                                        final boolean needToProcess = def.getType() != EDefType.CLASS || (def.getType() == EDefType.CLASS && (meta.getExClassName() == null || meta.getMetaClassName() == null));

                                        if (needToProcess) {
                                            if (def.isSetCommonClasses()) {
                                                processClassesMeta(id, meta, def.getCommonClasses());
                                            }
                                            if (def.isSetServerClasses()) {
                                                processClassesMeta(id, meta, def.getServerClasses());
                                            }
                                        }

                                        if (meta.getExClassName() != null) {
                                            checkForAmbiguity(layerMeta, id, definingExLayer, revisionMeta, "executable");
                                        }
                                        if (meta.getMetaClassName() != null) {
                                            checkForAmbiguity(layerMeta, id, definingMetaLayer, revisionMeta, "meta");
                                        }

                                        if (needToProcess) {

                                            meta.setTitleId(def.getTitleId());
                                            meta.setSrvFactoryClassName(factoryClassName);

                                            defIdsByType.put(def.getType(), id);

                                            if (def.getType() == EDefType.CLASS && def.getUploadMode() == EDefinitionUploadMode.ON_STARTUP) {
                                                loadOnStartupClassIds.add(id);
                                            }


                                            if (def.isSetDomains()) {
                                                meta.regDomainIds(def.getDomains());
                                                for (Id domainId : def.getDomains()) {
                                                    defIdsByDomainId.put(domainId, id);
                                                }
                                                if (def.getType() == EDefType.DOMAIN) {
                                                    if (def.getDomains().isEmpty()) {
                                                        rootDomainIds.add(id);
                                                    }
                                                }
                                            } else if (def.getType() == EDefType.DOMAIN) {
                                                rootDomainIds.add(id);
                                            }
                                            if (id.getPrefix() == EDefinitionIdPrefix.XML_SCHEME
                                                    || id.getPrefix() == EDefinitionIdPrefix.MSDL_SCHEME
                                                    || id.getPrefix() == EDefinitionIdPrefix.IMAGE) { // memory optimization fileName used only for these defs
                                                meta.setFileName(def.getDefFileName());
                                            }
                                            if (id.getPrefix() == EDefinitionIdPrefix.XML_SCHEME
                                                    || id.getPrefix() == EDefinitionIdPrefix.MSDL_SCHEME) {
                                                meta.setTargetNamespaceUri(def.getXmlTargetNamespace());
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (XmlException ex) {
                            tracer.put(EEventSeverity.ERROR, "Can\'t load " + defXmlName + ": " + ExceptionTextFormatter.exceptionStackToString(ex), null, null, false);
                        } catch (IOException ex) {
                            tracer.put(EEventSeverity.ERROR, "Can\'t load " + defXmlName + ": " + ExceptionTextFormatter.exceptionStackToString(ex), null, null, false);
                        }
                    }
                }
            }
        }
        if (userRefs.isEmpty()) {
            entityUserReferences = Collections.emptyMap();
        } else {
            final Map<Id, Collection<EntityUserReference>> unmodifiableValuesMap = new HashMap<Id, Collection<EntityUserReference>>();
            for (Map.Entry<Id, Collection<EntityUserReference>> entry : userRefs.entrySet()) {
                unmodifiableValuesMap.put(entry.getKey(), Collections.unmodifiableCollection(entry.getValue()));
            }
            entityUserReferences = Collections.unmodifiableMap(unmodifiableValuesMap);
        }
        rootDomainIds = Collections.unmodifiableCollection(rootDomainIds);
        loadOnStartupClassIds = Collections.unmodifiableSet(loadOnStartupClassIds);
    }

    public String getExecClassNameByDefId(final Id id) {
        final String exClassName = getMeta(id).getExClassName();
        if (exClassName == null) {
            throw new DefinitionNotFoundError(id);
        }
        return exClassName;
    }

    private DefMeta getMeta(final Id id) throws DefinitionNotFoundError {
        final DefMeta meta = metaByDefId.get(id);
        if (meta == null) {
            throw new DefinitionNotFoundError(id);
        }
        return meta;
    }

    /**
     * @return the descenderIdsByClassId
     */
    protected IdListsById getDescenderIdsByClassId() {
        return descenderIdsByClassId;
    }

    public String getMetaClassNameByDefId(final Id id) {
        return getMeta(id).getMetaClassName();
    }

    public String getXmlFileNameByMsdlSchemaDefId(final Id id) {
        return getMeta(id).getFileName();
    }

    public String getTargetNamespaceUriBySchemaDefId(final Id id) {
        return getMeta(id).getTargetNamespaceUri();
    }

    public String getServerFactoryClassNameByDefId(final Id id) {
        return getMeta(id).getSrvFactoryClassName();
    }

    public String getImgFileNameByImgId(final Id id) {
        return getMeta(id).getFileName();
    }

    public Collection<EventCodeMeta> getEventCodes() {
        return Collections.unmodifiableCollection(eventCodes.values());
    }

    /**
     * @return the ancesstorsIdsById
     */
    public Collection<Id> getAncestorIdsByClassId(final Id classId) {
        final Collection<Id> ancestorIds = getMeta(classId).getAncestorIds();
        return ancestorIds != null
                ? Collections.unmodifiableCollection(ancestorIds)
                : Collections.<Id>emptyList();
    }

    public Collection<Id> getDomainIdsByDefId(final Id id) {
        final Collection<Id> domainIds = getMeta(id).getDomainIds();
        return domainIds != null
                ? Collections.unmodifiableCollection(domainIds)
                : Collections.<Id>emptyList();
    }

    public IdListsById getDefIdsByDomainId() {
        return defIdsByDomainId;
    }

    public IdListsByName getDefIdsByLayer() {
        return defIdsByLayer;
    }

    public IdListsByName getDefIdsByModule() {
        return defIdsByModule;
    }

    void close() {
    }

    public Collection<Id> getRootDomainIds() {
        return rootDomainIds;
    }

    private String getDefKey(final Definition def) {
        return def.getType().getName() + "#" + def.getPath().get(0);
    }

    private void checkForAmbiguity(final LayerMeta currentLayer, final Id id, final Map<Id, LayerMeta> definingLayer, final RevisionMeta revisionMeta, final String defPartName) throws RadixLoaderException {
        final LayerMeta exWasDefinedIn = definingLayer.get(id);
        if (exWasDefinedIn == null) {
            definingLayer.put(id, currentLayer);
        } else {
            if (!revisionMeta.isBaseLayer(currentLayer, exWasDefinedIn)) {
                ambigiousException(id, defPartName, currentLayer, exWasDefinedIn);
            }
        }
    }

    private void ambigiousException(final Id id, final String defPartName, final LayerMeta currentLayer, final LayerMeta definingLayer) throws RadixLoaderException {
        throw new RadixLoaderException("Ambiguous definition of " + (defPartName == null ? "" : defPartName + " part of ") + "the #" + id + " in '" + currentLayer.getUri() + "' and '" + definingLayer.getUri() + "'");
    }

    public Map<Id, Collection<EntityUserReference>> getEntityUserReferences() {
        return entityUserReferences;
    }

    public Collection<Id> getDefIdsByType(final EDefType type) {
        return defIdsByType.getDefIdsByType(type);
    }

    public Id getDefTitleIdById(final Id defId) {
        return getMeta(defId).getTitleId();
    }

    public Collection<Id> getLoadOnStartupClassIds() {
        return loadOnStartupClassIds;
    }

    public boolean isExecutableClass(final String className) {
        return executableClasses.contains(className) && className.contains("mdl");
    }

    private void processClassesMeta(final Id defId, final DefMeta meta, final Classes classesMeta) {
        if (classesMeta.isSetExtends()) {
            final List<Id> path = classesMeta.getExtends().getPath();
            if (path != null && path.size() == 1) {
                meta.regAncestorId(path.get(0));
                getDescenderIdsByClassId().put(path.get(0), defId);
            }
        }
        final List<Implements> implementsList = classesMeta.getImplementsList();
        if (implementsList != null) {
            for (Implements impl : implementsList) {
                final List<Id> path = impl.getPath();
                if (path != null && path.size() == 1) {
                    meta.regAncestorId(path.get(0));
                    getDescenderIdsByClassId().put(path.get(0), defId);
                }
            }
        }
        final JavaClasses exClassesXml = classesMeta.getExecutable();
        if (exClassesXml != null) {
            final List<JavaClasses.Class> clsXml = exClassesXml.getClass1List();
            if (!clsXml.isEmpty()) {
                String exClassName = clsXml.get(0).getName();
                if (exClassName != null) {
                    meta.setExClassName(exClassName);
                    executableClasses.add(exClassName);
                }
            }
        }
        final JavaClasses metaClassesXml = classesMeta.getMeta();
        if (metaClassesXml != null) {
            final List<JavaClasses.Class> clsXml = metaClassesXml.getClass1List();
            if (!clsXml.isEmpty()) {
                String metaClassName = clsXml.get(0).getName();
                if (metaClassName != null) {
                    meta.setMetaClassName(metaClassName);
                }
            }
        }
    }

    protected final static class DefIdsByType extends CollectionsMap<EDefType, Id> {

        private Collection<Id> getDefIdsByType(final EDefType type) {
            return super.getCollection(type);
        }
    }

    public static class IdListsById extends CollectionsMap<Id, Id> {

        public Collection<Id> getIdListById(final Id id) {
            return super.getCollection(id);
        }
    }

    public static class IdListsByName extends CollectionsMap<String, Id> {

        public Collection<Id> getIdListByName(final String name) {
            return super.getCollection(name);
        }
    }

    public static class CollectionsMap<MK, CI> {

        private final Map<MK, Collection<CI>> listsById = new HashMap<>();

        void put(final MK id, final Collection<CI> idList) {
            if (idList == null) {
                return;
            }
            Collection<CI> list = getListById(id);
            for (CI i : idList) {
                if (!list.contains(i)) {
                    list.add(i);
                }
            }
        }

        void put(final MK keyId, final CI valId) {
            Collection<CI> list = getListById(keyId);
            if (!list.contains(valId)) {
                list.add(valId);
            }
        }

        private Collection<CI> getListById(final MK keyId) {
            Collection<CI> lst = listsById.get(keyId);
            if (lst == null) {
                lst = new HashSet<>();
                listsById.put(keyId, lst);
            }
            return lst;
        }

        public Set<MK> keySet() {
            return listsById.keySet();
        }

        public Collection<CI> getCollection(final MK id) {
            final Collection<CI> ids = listsById.get(id);
            if (ids == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableCollection(ids);
        }
    }
}
