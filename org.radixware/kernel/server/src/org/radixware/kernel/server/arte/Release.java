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
package org.radixware.kernel.server.arte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_FUNCTION;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_PACKAGE;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_REFERENCE;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_SEQUENCE;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_TABLE;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_TYPE;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixDefManager;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.common.meta.RadDomainDef;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

import org.radixware.kernel.server.meta.presentations.RadExplorerRootDef;
import org.radixware.kernel.server.meta.presentations.RadParagraphExplorerItemDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.EntityDetails;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;

/**
 * Stores shared meta for single revision. Release (as well as it's
 * complementary non-shared part ReleaseCache) should never be accessed
 * directly. All meta access should be done via DefManager.
 *
 */
public class Release {

    private static final int INITIAL_SIZE_S = 50;
    private static final int INITIAL_SIZE_M = 200;
    private static final int INITIAL_SIZE_L = 1000;
    private static final int INITIAL_SIZE_XL = 5000;
    //
    private static String META_TOP_LAYER_NAME = "$$meta.top.layer$$";
    //
    private final RevisionMeta revisionMeta;
    private final ReleaseRepository repository;
    private final ReleaseSharedClassloader classLoader;
    //dds
    private final RadixObject ddsLoadContext;
    private final Release.DdsDefLoader<DdsReferenceDef> refDefs;
    private final Release.DdsDefLoader<DdsTableDef> tabDefs;
    private final Release.DdsDefLoader<DdsFunctionDef> funcDefs;
    private final Release.DdsDefLoader<DdsSequenceDef> seqDefs;
    private final Release.DdsDefLoader<DdsPlSqlObjectDef> packDefs;
    private final Release.DdsDefLoader<DdsAccessPartitionFamilyDef> apfDefs;
    private final Map<String, DdsTableDef> tablesByDbName;
    private final Map<Id, DdsReferenceDef> masterRefByDetailTabId;
    private final Map<DdsReferenceDef, RadClassDef> detailRadClassDefByMdRef;
    //ads
    private final Release.AdsDefLoader<RadMlStringBundleDef> stringBundles;
    private final Release.AdsDefLoader<RadParagraphExplorerItemDef> explorerParagraphDefs;
    private final Release.AdsDefLoader<RadDomainDef> domainDefs;
    private final Release.AdsDefLoader<RadClassDef> classDefs;
    private final Release.AdsDefLoader<RadRoleDef> roleDefs;
    //other
    private final List<String> evCodeList;
    private final List<RadExplorerRootDef> accessibleExplorerRootDefs;
    private final List<RadRoleDef> accessibleRoleDefs;
    private final ILoadErrorsHandler errHandler;
    final List<EIsoLanguage> languages;
    final IRadixEnvironment releaseVirtualEnvironment;

//Constructor
    public Release(final RevisionMeta revMeta) {
        this(revMeta, null);
    }

    public Release(final RevisionMeta revMeta, final ILoadErrorsHandler loadErrorsHandler) {
        repository = new ReleaseRepository(this);
        this.revisionMeta = revMeta;
        classLoader = new ReleaseSharedClassloader(this, RadixLoader.getInstance(), Server.class.getClassLoader());
        errHandler = loadErrorsHandler != null ? loadErrorsHandler : new LoadErrorsLog() {
            @Override
            protected void logError(Id defId, RadixError err) {
                LogFactory.getLog("Unable to load def '" + defId + "', rev #" + getVersion());
            }
        };
        if (errHandler instanceof LoadErrorsLog) {
            ((LoadErrorsLog) errHandler).setTryLogInsteadOfExceptionOnNotFound(true);
        }
        //dds
        ddsLoadContext = calcDdsLoadContext();
        refDefs = loadDdsDefs(DdsVisitorProviderFactory.newReferenceProvider());
        tabDefs = loadDdsDefs(DdsVisitorProviderFactory.newTableProvider());
        funcDefs = loadDdsDefs(DdsVisitorProviderFactory.newFunctionProvider());
        seqDefs = loadDdsDefs(DdsVisitorProviderFactory.newSequenceProvider());
        packDefs = loadDdsDefs(DdsVisitorProviderFactory.newPlSqlObjectProvider());
        apfDefs = loadDdsDefs(DdsVisitorProviderFactory.newAccessPartitionFamilyProvider());
        tablesByDbName = collectTablesByDbName();
        masterRefByDetailTabId = collectMasterReferenceDef();
        detailRadClassDefByMdRef = collectDetailRadMeta();//NOPMD
        //ads    
        //!! all ads meta should be first loaded and then linked
        stringBundles = loadAdsDefs(EDefType.LOCALIZING_BUNDLE, INITIAL_SIZE_L);
        explorerParagraphDefs = loadAdsDefs(EDefType.PARAGRAPH, INITIAL_SIZE_M);
        domainDefs = loadAdsDefs(EDefType.DOMAIN, INITIAL_SIZE_M);
        classDefs = loadAdsDefs(EDefType.CLASS, INITIAL_SIZE_XL);
        roleDefs = loadAdsDefs(EDefType.ROLE, INITIAL_SIZE_M);
        //other
        accessibleExplorerRootDefs = buildAccessbileExplorerRootDefs();//NOPMD
        accessibleRoleDefs = buildAccessibleRoleDefs();
        evCodeList = buildEventCodeList();
        languages = buildLanguages();
        releaseVirtualEnvironment = createReleaseVirtualEnvironment();
        //
        linkMeta();
        if (errHandler instanceof LoadErrorsLog) {
            ((LoadErrorsLog) errHandler).setTryLogInsteadOfExceptionOnNotFound(false);
        }
    }

    private <T extends RadDefinition> AdsDefLoader<T> loadAdsDefs(final EDefType defType, final int initialCollectionCapacitiy) {
        AdsDefLoader<T> loader = new AdsDefLoader<>(classLoader, defType, initialCollectionCapacitiy, errHandler);
        loader.loadAllDefs();
        return loader;
    }

    private <T extends DdsDefinition> DdsDefLoader<T> loadDdsDefs(final VisitorProvider visitorProvider) {
        DdsDefLoader<T> loader = new DdsDefLoader<>(visitorProvider);
        loader.getAllDefs();
        return loader;
    }

    private void linkMeta() {
        linkMeta(stringBundles);
        linkMeta(explorerParagraphDefs);
        linkMeta(domainDefs);
        linkMeta(classDefs);
        linkMeta(roleDefs);

        linkTableDefs();
    }

    private void linkTableDefs() {
        for (DdsTableDef tableDef : getTableDefs()) {
            tableDef.findClassGuidColumn();
        }
    }

    private void linkMeta(final AdsDefLoader<? extends RadDefinition> defLoader) {
        for (RadDefinition def : defLoader.getAllDefs()) {
            def.link();
        }
    }

    public ReleaseRepository getRepository() {
        return repository;
    }

    public RevisionMeta getRevisionMeta() {
        return revisionMeta;
    }

    private IRadixEnvironment createReleaseVirtualEnvironment() {
        return new IRadixEnvironment() {
            @Override
            public EIsoLanguage getClientLanguage() {
                return Arte.get().getClientLanguage();
            }

            @Override
            public List<EIsoLanguage> getLanguages() {
                return languages;
            }

            @Override
            public IRadixDefManager getDefManager() {
                return new IRadixDefManager() {
                    @Override
                    public IMlStringBundle getStringBundleById(Id id) {
                        return Release.this.getMlStringBundleDef(id);
                    }

                    @Override
                    public boolean isDefInDomain(Id defId, Id domainId) {
                        return Release.this.isDefInDomain(defId, domainId);
                    }

                    @Override
                    public RootMsdlScheme getMsdlScheme(Id id) {
                        throw new UnsupportedOperationException("Unsupported in ReleaseVirtualEnvironment");
                    }
                };
            }

            @Override
            public IRadixTrace getTrace() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    public IRadixEnvironment getReleaseVirtualEnviroment() {
        return releaseVirtualEnvironment;
    }

    public DdsReferenceDef getMasterReferenceDef(final DdsTableDef table) {
        return masterRefByDetailTabId.get(table.getId());
    }

    private List<EIsoLanguage> buildLanguages() {
        final List<EIsoLanguage> result = new ArrayList<>();
        try {
            List<String> langsAsStr = getRevisionMeta().getLanguages();
            for (String lang : langsAsStr) {
                try {
                    result.add(EIsoLanguage.getForValue(lang));
                } catch (NoConstItemWithSuchValueError e) {
                }
            }
        } catch (Throwable e) {
            //ignore
        }
        return Collections.unmodifiableList(result);
    }

    private Map<Id, DdsReferenceDef> collectMasterReferenceDef() {
        final Map<Id, DdsReferenceDef> result = new HashMap<>(INITIAL_SIZE_M);
        for (DdsTableDef table : tabDefs.getAllDefs()) {
            for (DdsReferenceDef ref : table.collectOutgoingReferences()) {
                if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                    result.put(table.getId(), ref);
                }
            }
        }
        return Collections.unmodifiableMap(result);
    }

    public long getVersion() {
        return revisionMeta.getNum();
    }

    public List<String> getEventCodeList() {
        return evCodeList;
    }

    private List<String> buildEventCodeList() {
        final List<String> result = new ArrayList<>(2048);
        for (RadMlStringBundleDef b : stringBundles.getAllDefs()) {
            result.addAll(b.getEventCodeList());
        }
        return Collections.unmodifiableList(result);
    }

    public List<String> getEventCodeList(final String eventSource) {
        final List<String> list = new ArrayList<>(512);
        for (RadMlStringBundleDef b : stringBundles.getAllDefs()) {
            list.addAll(b.getEventCodeList(eventSource));
        }
        return list;
    }

    public RadMlStringBundleDef getMlStringBundleDef(final Id id) {
        return stringBundles.getDef(id);
    }

    final String getEventTitleByCode(final String code, final EIsoLanguage lang) {
        if (code == null) {
            return null;
        }
        final int delimPos = code.indexOf('-');
        if (delimPos < 0) {
            throw new WrongFormatError("Wrong format of event code: \"" + code + "\"", null);
        }
        final RadMlStringBundleDef bundle = getMlStringBundleDef(Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + code.substring(3, delimPos)));
        return bundle.get(Id.Factory.loadFrom(code.substring(delimPos + 1)), lang);
    }

    final EEventSeverity getEventSeverityByCode(final String code) {
        final int delimPos = code.indexOf('-');
        if (delimPos < 0) {
            throw new WrongFormatError("Wrong format of event code: \"" + code + "\"", null);
        }
        try {
            final RadMlStringBundleDef bundle = getMlStringBundleDef(Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + code.substring(3, delimPos)));
            return bundle.getEventSeverity(Id.Factory.loadFrom(code.substring(delimPos + 1)));
        } catch (DefinitionNotFoundError e) {
            return EEventSeverity.ERROR;
        }
    }

    final String getEventSourceByCode(final String code) {
        final int delimPos = code.indexOf('-');
        if (delimPos < 0) {
            throw new WrongFormatError("Wrong format of event code: \"" + code + "\"", null);
        }
        try {
            final RadMlStringBundleDef bundle = getMlStringBundleDef(Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + code.substring(3, delimPos)));
            return bundle.getEventSource(Id.Factory.loadFrom(code.substring(delimPos + 1)));
        } catch (DefinitionNotFoundError e) {
            return EEventSource.ARTE.toString();
        }
    }

    public RadParagraphExplorerItemDef getExplorerParagraphDef(final Id id) {
        return explorerParagraphDefs.getDef(id);
    }

    public Collection<RadParagraphExplorerItemDef> getExplorerParagraphDefs() {
        return explorerParagraphDefs.getAllDefs();
    }

    public List<RadExplorerRootDef> getAccessibleExplorerRootDefs() {
        return Collections.unmodifiableList(accessibleExplorerRootDefs);
    }

    public List<RadRoleDef> getAccessibleRoleDefs() {
        return Collections.unmodifiableList(accessibleRoleDefs);
    }

    private List<RadRoleDef> buildAccessibleRoleDefs() {
        List<RadRoleDef> result;
        final Map<String, String[]> accessibleRoleIdsByLayerUri = getRepository().getRevisionMeta().getAccessibleRoleIds();
        result = new LinkedList<>();
        for (RadRoleDef roleDef : getRoleDefs()) {
            final String[] accessibleRoleIds = accessibleRoleIdsByLayerUri.get(roleDef.getLayerUri());
            if (accessibleRoleIds == null || Arrays.asList(accessibleRoleIds).contains(roleDef.getId().toString())) {
                result.add(roleDef);
            }
        }
        return result;
    }

    private List<RadExplorerRootDef> buildAccessbileExplorerRootDefs() {
        List<RadExplorerRootDef> result;
        final Map<String, String[]> accessibleExplorerRootIdsByLayerUri = getRepository().getRevisionMeta().getAccessibleExplorerRootIds();
        result = new LinkedList<>();
        for (RadParagraphExplorerItemDef paragraphDef : getExplorerParagraphDefs()) {
            if (paragraphDef instanceof RadExplorerRootDef) {
                final String[] accessibleExplorerRootIds = accessibleExplorerRootIdsByLayerUri.get(paragraphDef.getLayerUri());

                if (accessibleExplorerRootIds == null
                        || Arrays.asList(accessibleExplorerRootIds).contains(paragraphDef.getId().toString())) {
                    result.add((RadExplorerRootDef) paragraphDef);
                }
            }
        }
        return result;
    }

    public RadDomainDef getDomainDef(final Id id) {
        return domainDefs.getDef(id);
    }

    public RadClassDef getClassDef(final Id id) {
        return classDefs.getDef(id);
    }

    public Collection<RadClassDef> getClassDefs() {
        return classDefs.getAllDefs();
    }

    public RadRoleDef getRoleDef(final Id id) {
        return roleDefs.getDef(id);
    }

    public Collection<RadRoleDef> getRoleDefs() {
        return roleDefs.getAllDefs();
    }

    public Id getClassEntityId(final Id classId) {
        if (classId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
            return Id.Factory.loadFrom(EDefinitionIdPrefix.DDS_TABLE.getValue() + classId.toString().substring(3));
        }
        return getClassDef(classId).getEntityId();
    }

    void checkClassDefLoadError(final Id id) {
        classDefs.checkLoadError(id);
    }

    public final ReleaseSharedClassloader getSharedClassLoader() {
        return classLoader;
    }

    final void close() {
        repository.close();
    }

    public Collection<RadClassDef> getAllClassDefsBasedOnTableByTabId(final Id tableId) {
        return repository.getAdsIndices().getAllClassDefsBasedOnTableByTabId(tableId);
    }

    //dds
    public String getDdsDefDbName(final Id[] idPath) {
        final DdsDefinition def = getDdsDef(idPath);
        if (def instanceof IDdsDbDefinition) {
            return ((IDdsDbDefinition) def).getDbName();
        }
        return null;
    }

    public DdsDefinition getDdsDef(final Id[] idPath) {
        if (idPath == null) {
            throw new IllegalArgumentException("Parameter idPath is null");
        }
        if (idPath.length > 2 || idPath.length < 1) {
            throw new IllegalArgumentException("Unsupported idPath length: " + String.valueOf(idPath.length));
        }
        final DdsDefinition def;
        switch (idPath[0].getPrefix()) {
            case DDS_TABLE: {
                final DdsTableDef tab = getTableDef(idPath[0]);
                if (idPath.length == 1) {
                    def = tab;
                } else {
                    if (idPath[1].getPrefix() == EDefinitionIdPrefix.DDS_COLUMN) {
                        def = tab.getColumns().getById(idPath[1], EScope.ALL);
                    } else if (idPath[1].getPrefix() == EDefinitionIdPrefix.DDS_INDEX) {
                        if (tab.getPrimaryKey().getId().equals(idPath[1])) {
                            def = tab.getPrimaryKey();//RADIX-4886
                        } else {
                            def = tab.getIndices().getById(idPath[1], EScope.ALL);
                        }
                    } else {
                        throw new UnsupportedOperationException("getDdsDefDbName() doesn't support " + idPath[1].getPrefix().toString() + " as subobject");
                    }
                }
                break;
            }
            case DDS_SEQUENCE: {
                if (idPath.length != 1) {
                    throw new UnsupportedOperationException("getDdsDefDbName() doesn't support sequence's subdefinitions");
                }
                def = getSequenceDef(idPath[0]);
                break;
            }
            case DDS_TYPE:
            case DDS_PACKAGE: {
                if (idPath.length == 2 && idPath[1].getPrefix() == EDefinitionIdPrefix.DDS_FUNCTION) {
                    def = getFunctionDef(idPath[1]);
                    break;
                } else if (idPath.length != 1) {
                    throw new UnsupportedOperationException("getDdsDefDbName() doesn't support function's subdefinitions");
                }
                def = getDdsPlSqlObjectDef(idPath[0]);
                break;
            }
            case DDS_FUNCTION: {
                if (idPath.length != 1) {
                    throw new UnsupportedOperationException("getDdsDefDbName() doesn't support function's subdefinitions");
                }
                def = getFunctionDef(idPath[0]);
                break;
            }
            case DDS_REFERENCE: {
                if (idPath.length != 1) {
                    throw new UnsupportedOperationException("getDdsDefDbName() doesn't support reference's subdefinitions");
                }
                def = getReferenceDef(idPath[0]);
                break;
            }
            default:
                throw new UnsupportedOperationException("getDdsDefDbName() doesn't support " + idPath[0].getPrefix().toString());
        }
        return def;
    }

    public DdsSequenceDef getSequenceDef(final Id seqId) {
        return seqDefs.getDef(seqId);
    }

    public final DdsPlSqlObjectDef getDdsPlSqlObjectDef(final Id id) {
        return packDefs.getDef(id);
    }

    public DdsFunctionDef getFunctionDef(final Id funcId) {
        return funcDefs.getDef(funcId);
    }

    public DdsTableDef getTableDef(final Id tabId) {
        return tabDefs.getDef(tabId);
    }

    final Collection<DdsTableDef> getTableDefs() {
        return tabDefs.getAllDefs();
    }

    public DdsAccessPartitionFamilyDef getAccessPartitionFamilyDef(final Id apfId) {
        return apfDefs.getDef(apfId);
    }

    public Collection<DdsAccessPartitionFamilyDef> getAccessPartitionFamilyDefs() {
        return apfDefs.getAllDefs();
    }

    public DdsReferenceDef getReferenceDef(final Id refId) {
        return refDefs.getDef(refId);
    }

    String getAdsClassNameById(final Id id) {
        return repository.getAdsIndices().getExecClassNameByDefId(id);
    }

    String getAdsMetaClassNameById(final Id id) {
        return repository.getAdsIndices().getMetaClassNameByDefId(id);
    }

    public boolean isDefInDomain(final Id defId, final Id domainId) {
        if (defId.equals(domainId)) {
            return true;
        }
        //process hierarchy
        for (Id id : repository.getAdsIndices().getDomainIdsByDefId(defId)) {
            if (isDefInDomain(id, domainId)) {
                return true;
            }
        }
        return false;
    }

    public Collection<Id> getRootDomainIds() {
        return repository.getAdsIndices().getRootDomainIds();
    }

    public String getImgFileNameByImgId(final Id id) {
        return repository.getAdsIndices().getImgFileNameByImgId(id);
    }

    public DdsTableDef getTableDefByDbName(final String dbName) {
        final DdsTableDef tab = tablesByDbName.get(dbName);
        if (tab != null) {
            return tab;
        }
        for (DdsTableDef t : getTableDefs()) {
            if (Utils.equals(t.getDbName(), dbName)) {
                tablesByDbName.put(dbName, t);
                return t;
            }
        }
        throw new DefinitionNotFoundError(Id.Factory.loadFrom(dbName));
    }

    private Map<String, DdsTableDef> collectTablesByDbName() {
        final Map<String, DdsTableDef> result = new HashMap<>(INITIAL_SIZE_L);
        for (DdsTableDef t : getTableDefs()) {
            result.put(t.getDbName(), t);
        }
        return Collections.unmodifiableMap(result);
    }

    private Collection<Id> getDefinitionIdsInDomain(final Id domainId, final int nesting) {
        if (nesting == 100) {
            throw new IllegalUsageError("Domain tree nesting limit (100) is exeeded.");
        }
        final Collection<Id> ownDefIds = repository.getAdsIndices().getDefIdsByDomainId().getCollection(domainId);
        if (ownDefIds == null || ownDefIds.isEmpty()) {
            return Collections.emptyList();
        }
        final Collection<Id> subDomainDefIds = new ArrayList<Id>();
        for (Id defId : ownDefIds) {
            if (defId.getPrefix() == EDefinitionIdPrefix.DOMAIN) {
                subDomainDefIds.addAll(getDefinitionIdsInDomain(defId, nesting + 1));
            }
        }
        if (subDomainDefIds.isEmpty()) {
            return Collections.unmodifiableCollection(ownDefIds);
        }
        subDomainDefIds.addAll(ownDefIds);
        return Collections.unmodifiableCollection(subDomainDefIds);
    }

    public Collection<Id> getDefinitionIdsInDomain(final Id domainId) {
        return getDefinitionIdsInDomain(domainId, 0);
    }

    String getAdsFactoryClassNameByIClassId(final Id classId) {
        return getRepository().getAdsIndices().getServerFactoryClassNameByDefId(classId);
    }

    String getTargetNamespaceUriBySchemaDefId(final Id schemaId) {
        return getRepository().getAdsIndices().getTargetNamespaceUriBySchemaDefId(schemaId);
    }

    public Collection<Id> getDefIdsByType(final EDefType type) {
        return repository.getAdsIndices().getDefIdsByType(type);
    }

    public Collection<RadClassDef> getClassAndItsDescenders(final Id classId) {
        final Collection<RadClassDef> lst = new ArrayList<>();
        repository.getAdsIndices().addClassAndItsDescendersTo(lst, classId);
        return lst;
    }

    public String getDefTitleById(final Id defId) {
        final Id titleId = repository.getAdsIndices().getDefTitleIdById(defId);
        if (titleId != null) {
            return MultilingualString.get(getReleaseVirtualEnviroment(), defId, titleId);
        }
        return null;
    }

    public RadixObject getDdsLoadContext() {
        return ddsLoadContext;
    }

    private RadixObject calcDdsLoadContext() {
        RadixObject result = null;
        try {

            if (revisionMeta.getTopLayers().size() == 1) {
                result
                        = repository.getBranch().getLayers().findByURI(
                                revisionMeta.getTopLayers().get(0).getUri());
            } else {
                Layer metaLayer = repository.getBranch().getLayers().findByURI(META_TOP_LAYER_NAME);
                if (metaLayer == null) {
                    metaLayer = repository.getBranch().getLayers().addNew(META_TOP_LAYER_NAME, META_TOP_LAYER_NAME, META_TOP_LAYER_NAME, null, null);
                    for (LayerMeta topLayerMeta : revisionMeta.getTopLayers()) {
                        metaLayer.addBaseLayer(topLayerMeta.getUri());
                    }
                }
                result = metaLayer;
            }

        } catch (IOException ex) {
            throw new RadixError(ex.getMessage(), ex);
        }
        return result;
    }

    public RadClassDef getDetailRadMeta(final DdsReferenceDef mdRef) {
        return detailRadClassDefByMdRef.get(mdRef);
    }

    protected RadClassDef createDetailRadMeta(final DdsReferenceDef mdRef) {
        final DdsTableDef ddsMeta = mdRef.getChildTable(getDdsLoadContext());
        final List<DdsColumnDef> columns = ddsMeta.getColumns().get(EScope.ALL);
        final RadPropDef[] props = new RadPropDef[columns.size()];
        int i = 0;
        for (DdsColumnDef column : columns) {
            props[i] = new RadInnatePropDef(column.getId(), column.getId().toString(), null, column.getValType(), null, false, null, null, null, null, new EntityDetails.PropAccessor(column.getId()));
            i++;
        }

        return new RadClassDef(
                this,
                RadClassDef.getEntityClassIdByTableId(ddsMeta.getId()),
                ddsMeta.getName(),
                null,
                EClassType.ENTITY,
                null,
                ddsMeta.getId(),
                null,
                props,
                null,
                null,
                EAccessAreaType.INHERITED,
                null,
                null);
    }

    private Map<DdsReferenceDef, RadClassDef> collectDetailRadMeta() {
        final Map<DdsReferenceDef, RadClassDef> result = new HashMap<>(INITIAL_SIZE_M);
        for (DdsReferenceDef mdRef : masterRefByDetailTabId.values()) {
            result.put(mdRef, createDetailRadMeta(mdRef));
        }
        return result;
    }

    public static class AdsDefLoader<T extends RadDefinition> {

        protected Map<Id, T> defs;
        final EDefType defType;
        private boolean allDefsLoaded = false;
        private Map<Id, RadixError> loadErrors = null;
        private final RadixClassLoader classloader;
        private final IVersionedClassloader classloaderAsVersionedClassloader;
        private final IRadixClassLoader classloaderAsRadixClassLoader;
        private final ILoadErrorsHandler errLog;

        public <U extends RadixClassLoader & IVersionedClassloader & IRadixClassLoader> AdsDefLoader(final U classloader, final EDefType defType, final int initialCapacity, final ILoadErrorsHandler errLog) {
            this.defs = new HashMap<>(initialCapacity);
            this.defType = defType;
            this.classloader = classloader;
            this.classloaderAsRadixClassLoader = classloader;
            this.classloaderAsVersionedClassloader = classloader;
            this.errLog = errLog;
        }

        public <U extends RadixClassLoader & IVersionedClassloader & IRadixClassLoader> AdsDefLoader(final U classloader, final EDefType defType, final ILoadErrorsHandler errLog) {
            this(classloader, defType, 20, errLog);
        }

        @SuppressWarnings("unchecked")
        public T getDef(final Id id) {
            T d = defs.get(id);
            if (d != null) {
                return d;
            }
            checkLoadError(id);
            if (!allDefsLoaded) {
                d = (T) loadDef(id);
                if (d != null) {
                    defs.put(id, d);
                    return d;
                } else {
                    checkLoadError(id);
                }
            }
            if (errLog != null) {
                errLog.onDefNotFound(id);
                return null;
            } else {
                throw new DefinitionNotFoundError(id);
            }
        }

        public Collection<T> getAllDefs() {
            loadAllDefs();
            return defs.values();
        }

        protected Object loadDef(final Id id) {
            return DefLoadUtils.loadDef(id, classloaderAsRadixClassLoader, errLog);
        }
        /*
         * private Object loadXmlDef(final Id id) { throw new
         * UnsupportedOperationException("Not realized yet"); }
         */

        @SuppressWarnings("unchecked")
        public void loadAllDefs() {
            if (!allDefsLoaded) {
                final Collection<Id> defIds = classloaderAsVersionedClassloader.getRelease().getRepository().getAdsIndices().getDefIdsByType(defType);
                for (Id defId : defIds) {
                    if (defs.get(defId) == null) {
                        final Object def = loadDef(defId);
                        if (def != null) {
                            defs.put(defId, (T) def);
                        }
                    }
                }
                defs = Collections.unmodifiableMap(defs);
                allDefsLoaded = true;
            }
        }

        protected void checkLoadError(final Id defId) throws RadixError {
            if (loadErrors != null) {
                final RadixError err = loadErrors.get(defId);
                if (err != null) {
                    throw err;
                }
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " [" + defType + "], " + defs.size() + " loaded";
        }
    }

    private class DdsDefLoader<T extends DdsDefinition> {

        VisitorProvider visitorProvider;
        private Map<Id, T> defsById = null;

        public DdsDefLoader(final VisitorProvider visitorProvider) {
            this.visitorProvider = visitorProvider;
        }

        public T getDef(final Id id) {
            final T d = getIdxById().get(id);
            if (d == null) {
                throw new DefinitionNotFoundError(id);
            }
            return d;
        }

        private Map<Id, T> getIdxById() {
            if (defsById == null) {
                try {
                    defsById = DdsUtils.<T>createIdx(repository.getBranch(), visitorProvider);
                } catch (IOException ex) {
                    throw new RadixError("Unable to create definition index", ex);
                }
            }
            return defsById;
        }

        Collection<T> getAllDefs() {
            return Collections.unmodifiableCollection(getIdxById().values());
        }
    }
}
