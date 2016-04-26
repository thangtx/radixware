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

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.server.dbq.DbQueryBuilder;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef;
import org.radixware.kernel.server.meta.presentations.RadParagraphExplorerItemDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.common.defs.IDefinitionFactory;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.environment.IRadixDefManager;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.meta.RadDomainDef;
import org.radixware.kernel.common.meta.RadEnumDef;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.ArteProfiler.EWaitType;
import org.radixware.kernel.server.instance.ReleaseLoadException;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

import org.radixware.kernel.server.meta.presentations.RadExplorerRootDef;

/**
 * Provides access to versioned meta.
 *
 *
 */
public class DefManager implements IRadixDefManager {

    private Long lastVersion = null;
    private final Arte arte;
    ReleaseCache releaseCache = null;
    private final ReleaseCachePool releasePool;

    protected Arte getArte() {
        return arte;
    }

    public static boolean isRefByPrimaryKey(final DdsReferenceDef detRef) {
        //TODO see TDbpReference isByPrimaryKey
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected DefManager(final Arte arte) {
        this(arte, new DefaultReleaseCacheFactory());
    }

    protected DefManager(final Arte arte, final ReleaseCacheFactory releaseFactory) {
        this.arte = arte;
        releasePool = new ReleaseCachePool(arte, releaseFactory);
    }

    public RadClassDef getDetailRadMeta(final DdsReferenceDef mdRef) {
        return getReleaseCache().getRelease().getDetailRadMeta(mdRef);
    }

    public Long getLastVersion() {
        return lastVersion;
    }

    public Id getMasterPropIdByDetailPropId(final DdsReferenceDef mdRef, final RadClassDef masterMeta, final Id detailsColumnId) {
        if (mdRef != null) {
            for (DdsReferenceDef.ColumnsInfoItem refProp : mdRef.getColumnsInfo()) {
                if (refProp.getChildColumnId().equals(detailsColumnId)) {
                    return refProp.getParentColumnId();
                }
            }
            for (RadPropDef prop : masterMeta.getProps()) {
                if (prop instanceof RadDetailPropDef) {
                    final RadDetailPropDef detProp = (RadDetailPropDef) prop;
                    if (detProp.getJoinedPropId().equals(detailsColumnId) && detProp.getDetailReference() == mdRef) {
                        return detProp.getId();
                    }
                }
            }
        }
        return null;
    }

    public RoleLoadError getRoleLoadError(final Id id) {
        return releaseCache.getRoleLoadError(id);//right now errors are registered only for app roles
    }

    public ReleaseCache getReleaseCache() {
        return releaseCache;
    }

    public RadixObject getDdsLoadContext() {
        return releaseCache.getRelease().getDdsLoadContext();
    }

    public List<Long> getCachedVersions() {
        final List<Long> result = new ArrayList<>();
        for (ReleaseCache rc : releasePool.getCachedVersions()) {
            result.add(rc.getRelease().getVersion());
        }
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }

    public final void setVersion(final Long version) {
        try {
            releaseCache = releasePool.get(version);
        } catch (ReleaseLoadException ex) {
            throw new IllegalStateException("Unable to load version #" + version, ex);
        }
        lastVersion = version;
        releaseCache.resetServerResourceRoleIdsStrCache();
        releaseCache.actualizeDbIndices();

    }

    final void clear() {
        if (releaseCache != null) {
            releaseCache.getDbQueryBuilder().clearBatchOptions();
            releaseCache.resetStatementsBatchSettings();
        }
    }

    EWaitType getSectionType(final String sectionName) {
        if (releaseCache == null) {
            return ArteProfiler.EWaitType.CPU;
        } else {
            return releaseCache.getSectionType(sectionName);
        }
    }

    public void sendBatches(final boolean bThrowExceptionOnDbErr) {
        getDbQueryBuilder().sendBatches(bThrowExceptionOnDbErr);
        releaseCache.sendStatementsBatches(bThrowExceptionOnDbErr);
    }

    public final DbQueryBuilder getDbQueryBuilder() {
        return releaseCache.getDbQueryBuilder();
    }

    @Override
    public RadMlStringBundleDef getStringBundleById(final Id id) {
        return releaseCache.getStringBundleDef(id);
    }

    public RadMlStringBundleDef getStringBundleDef(final Id id) {
        return getStringBundleById(id);
    }

    public String getEventTitleByCode(final String code, final EIsoLanguage lang) {
        try {
            ReleaseCache rc = getLastReleaseCache(true);
            if (rc != null) {
                return rc.getRelease().getEventTitleByCode(code, lang);
            }
        } catch (ReleaseLoadException ex) {
        }
        return null;
    }

    public EEventSeverity getEventSeverityByCode(final String code) {
        try {
            final ReleaseCache rc = getLastReleaseCache(true);
            if (rc != null) {
                return rc.getRelease().getEventSeverityByCode(code);
            }
        } catch (Throwable e) {
            //return EEventSeverity.ERROR;
        }
        return EEventSeverity.ERROR;
    }

    public String getEventSourceByCode(final String code) {
        try {
            final ReleaseCache rc = getLastReleaseCache(true);
            if (rc != null) {
                return rc.getRelease().getEventSourceByCode(code);
            }
        } catch (Throwable e) {
            //proceed
        }
        return EEventSource.ARTE.getValue();
    }

    String getAdsFactoryClassNameByIClassId(final Id classId) {
        return releaseCache.getRelease().getRepository().getAdsIndices().getServerFactoryClassNameByDefId(classId);
    }

    public Collection<RadClassDef> getAllClassDefsBasedOnTableByTabId(final Id tableId) {
        return releaseCache.getRelease().getRepository().getAdsIndices().getAllClassDefsBasedOnTableByTabId(tableId);
    }

    /*
     * public TDbuClassPresentation getEntityDef(final String id) { return
     * release.getEntityDef(id); }
     */
    public RadParagraphExplorerItemDef getExplorerParagraphDef(final Id id) {
        return releaseCache.getRelease().getExplorerParagraphDef(id);
    }

    public Collection<RadParagraphExplorerItemDef> getExplorerParagraphDefs() {
        return releaseCache.getRelease().getExplorerParagraphDefs();
    }

    public List<RadExplorerRootDef> getAccessibleExplorerRootDefs() {
        return releaseCache.getRelease().getAccessibleExplorerRootDefs();
    }

    public List<RadRoleDef> getAccessibleRoleDefs() {
        return releaseCache.getAppAndAccessibleStandardRoles();
    }

    public RadEnumDef getEnumDef(final Id id) {
        return releaseCache.getEnumDef(id);
    }

    @Override
    public RootMsdlScheme getMsdlScheme(final Id id) {
        return releaseCache.getMsdlScheme(id);
    }

    public RadClassDef getClassDef(final Id id) {
        return releaseCache.getClassDef(id);
    }

    public RadDomainDef getDomainDef(final Id id) {
        return releaseCache.getRelease().getDomainDef(id);
    }

    public Collection<RadClassDef> getClassDefs() {
        return releaseCache.getClassDefs();
    }

    public Collection<RadClassDef> getClassAndItsDescenders(final Id classId) {
        return releaseCache.getRelease().getClassAndItsDescenders(classId);
    }

    public String getXmlFileNameByMsdlSchemaDefId(final Id id) {
        return releaseCache.getRelease().getRepository().getAdsIndices().getXmlFileNameByMsdlSchemaDefId(id);
    }

    public RadContextlessCommandDef getContextlessCommandDef(final Id id) {
        return releaseCache.getContextlessCommandDef(id);
    }

    public Collection<RadContextlessCommandDef> getContextlessCommandDefs() {
        return releaseCache.getContextlessCommandDefs();
    }

    public RadRoleDef getRoleDef(final Id id) {
        if (EDefinitionIdPrefix.APPLICATION_ROLE == id.getPrefix()) {
            return releaseCache.getAppRole(id);
        }
        return releaseCache.getRelease().getRoleDef(id);
    }

    public Collection<RadRoleDef> getRoleDefs() {
        return releaseCache.getAppAndStandardRoles();
    }

    public Collection<RadRoleDef> getAvailableRoleDefs() {
        return getRoleDefs();
    }

    public Class<?> getClass(final Id id) {
        return releaseCache.getClass(id);
    }

    public ClassLoader getClassLoader() {
        return releaseCache.getPrivateClassloader();
    }

    public Id getClassEntityId(final Id classId) {
        return releaseCache.getRelease().getClassEntityId(classId);
    }

    @Deprecated
    public Id getRealEntityClassId(final Pid pid, final EntityPropVals loadedProps) {
        return releaseCache.getRealEntityClassId(pid, loadedProps);
    }

    public EntityPropVals readOwnProps(final Pid pid) {
        return releaseCache.readOwnProps(pid);
    }

    public List<String> getEventCodeList() {
        return releaseCache.getRelease().getEventCodeList();
    }

    public List<String> getEventCodeList(final String eventSource) {
        return releaseCache.getRelease().getEventCodeList(eventSource);
    }

    public void maintenanceAllUserCaches() {
        final Long curVersion = lastVersion;
        for (ReleaseCache rc : releasePool.getCachedVersions()) {
            try {
                setVersion(rc.getRelease().getVersion());
                rc.getUserCache().maintenance();
            } catch (Exception ex) {
                arte.getTrace().put(EEventSeverity.WARNING, "Error on maintenance user cache for version " + rc.getRelease().getVersion() + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
            }
        }
        for (ReleaseCache rc : releasePool.getSchedulledForClose()) {
            try {
                setVersion(rc.getRelease().getVersion());
                rc.close();//clears user cache
            } catch (Exception ex) {
                arte.getTrace().put(EEventSeverity.WARNING, "Error on closing version " + rc.getRelease().getVersion() + ": " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
            } finally {
                releasePool.schedulledVersionClosed(rc);//remove from pool anyway
            }
        }
        if (curVersion != null) {
            setVersion(curVersion);
        }
    }

    private ReleaseCache getLastReleaseCache(final boolean bUseDevIfNoOthersOrZeroRelease) throws ReleaseLoadException {
        if (releaseCache != null) {
            return releaseCache;
        } else if (lastVersion != null) {
            return releasePool.get(lastVersion);
        } else if (bUseDevIfNoOthersOrZeroRelease) {
            return releasePool.get(arte.getActualVersion());
        }
        return null;
    }

    private static final class ReleaseCachePool {

        private static final int MAX_SIZE = 2;
        private final ReleaseCacheFactory releaseFactory;
        private final Arte arte;
        private final List<ReleaseCache> pool;
        //In case when particular version should be thrown out of cache
        //and it's UserCache is not empty, we must call close() only when 
        //this particular version is set as current version, because
        //objects in user cache has "finalizers" which may depend
        //on context objects. One possible time for it - user caches maintenance
        private final List<ReleaseCache> schedulledForClose;

        ReleaseCachePool(final Arte arte, final ReleaseCacheFactory releaseFactory) {
            this.arte = arte;
            pool = new ArrayList<>();
            schedulledForClose = new ArrayList<>();
            this.releaseFactory = releaseFactory;
        }

        public ReleaseCache get(final Long version) throws ReleaseLoadException {
            if (version == null) {
                throw new IllegalUsageError("Version can not be null");
            }

            final List<ReleaseCache> allCached = new ArrayList<>(pool);
            allCached.addAll(schedulledForClose);
            for (ReleaseCache r : allCached) {
                if (r.getRelease().getVersion() == version.longValue()) {
                    return r;
                }
            }
            if (pool.size() == MAX_SIZE) {
                ReleaseCache oldReleaseCache = null;
                for (ReleaseCache r : pool) {
                    if (r.getRelease().getVersion() != arte.getActualVersion()) {
                        oldReleaseCache = r;
                        break;
                    }
                }
                if (oldReleaseCache == null) {
                    oldReleaseCache = pool.get(0);
                }
                pool.remove(oldReleaseCache);
                if (oldReleaseCache.getUserCache().size() == 0) {
                    oldReleaseCache.close();
                    arte.getTrace().put(EEventSeverity.DEBUG, "ReleaseCache (version = " + String.valueOf(oldReleaseCache.getRelease().getVersion()) + ") removed from pool", EEventSource.DEF_MANAGER);
                } else {
                    oldReleaseCache.clearDbQueriesCaches();
                    schedulledForClose.add(oldReleaseCache);
                    arte.getTrace().put(EEventSeverity.DEBUG, "ReleaseCache (version = " + String.valueOf(oldReleaseCache.getRelease().getVersion()) + ") schedulled for close", EEventSource.DEF_MANAGER);
                    arte.requestMaintenance();
                }

            }
            clearOldReleaseCaches(version.longValue()); //DBP-1580
            final ReleaseCache r = releaseFactory.newInstance(arte, version);
            pool.add(r);
            arte.getTrace().put(EEventSeverity.DEBUG, "ReleaseCache (version = " + version + ") created", EEventSource.DEF_MANAGER);
            for (Id id : r.getRelease().getRepository().getAdsIndices().getLoadOnStartupClassIds()) {//RADIX-2525
                try {
                    r.getClass(id);
                } catch (RuntimeException e) {
                    arte.getTrace().put(EEventSeverity.WARNING, "Can't load definition #" + id + ": " + arte.getTrace().exceptionStackToString(e), EEventSource.DEF_MANAGER);
                }
            }
            return r;
        }

        public List<ReleaseCache> getCachedVersions() {
            return new ArrayList<>(pool);
        }

        public List<ReleaseCache> getSchedulledForClose() {
            return new ArrayList<>(schedulledForClose);
        }

        public void schedulledVersionClosed(final ReleaseCache rc) {
            if (schedulledForClose.remove(rc)) {
                arte.getTrace().put(EEventSeverity.DEBUG, "ReleaseCache (version = " + String.valueOf(rc.getRelease().getVersion()) + ") removed from pool (schedulled)", EEventSource.DEF_MANAGER);
            }
        }

        private void clearOldReleaseCaches(final long newVersion) {
            for (ReleaseCache r : pool) {
                if (r.getRelease().getVersion() < newVersion) {
                    r.clearDbQueriesCaches();
                }
            }
        }

        public void close() {
            for (ReleaseCache r : pool) {
                try {
                    r.close();
                } catch (Exception ex) {
                    arte.logCleanupError(ex, "ReleaseCache frop pool");
                }
            }
            pool.clear();
        }
    }

    public void close() {
        if (releaseCache != null) {
            try {
                releaseCache.close();
            } catch (Exception ex) {
                arte.logCleanupError(ex, "Current ReleaseCache");
            }
        }
        try {
            this.releasePool.close();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "Release Pool");
        }
    }

    public String getAdsExecClassNameById(final Id id) {
        return releaseCache.getRelease().getAdsClassNameById(id);
    }

    public Collection<Id> getDefinitionIdsInDomain(final Id domainId) {
        return releaseCache.getRelease().getDefinitionIdsInDomain(domainId);
    }

    public Collection<DdsTableDef> getTableDefs() {
        return releaseCache.getRelease().getTableDefs();
    }

    public DdsTableDef getTableDefByDbName(final String dbName) {
        return releaseCache.getRelease().getTableDefByDbName(dbName);
    }

    //DRC
    public final String getServerResourceRoleIdsStr(final EDrcServerResource resource) {
        return releaseCache.getServerResourceRoleIdsStr(resource);
    }

    public void checkDdsStructureVersion(final Connection db) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public DdsSequenceDef getSequenceDef(final Id seqId) {
        return releaseCache.getRelease().getSequenceDef(seqId);
    }

    public DdsTableDef getTableDef(final Id id) {
        return releaseCache.getRelease().getTableDef(id);
    }

    public String getDdsDefDbName(final Id[] idPath) {
        return releaseCache.getRelease().getDdsDefDbName(idPath);
    }

    public DdsDefinition getDdsDef(final Id[] idPath) {
        return releaseCache.getRelease().getDdsDef(idPath);
    }

    public DdsAccessPartitionFamilyDef getAccessPartitionFamilyDef(final Id apfId) {
        return releaseCache.getRelease().getAccessPartitionFamilyDef(apfId);
    }

    public Collection<DdsAccessPartitionFamilyDef> getAccessPartitionFamilyDefs() {
        return releaseCache.getRelease().getAccessPartitionFamilyDefs();
    }

    public DdsReferenceDef getReferenceDef(final Id id) {
        return releaseCache.getRelease().getReferenceDef(id);
    }

    public final DdsPlSqlObjectDef getDdsPlSqlObjectDef(final Id id) {
        return releaseCache.getRelease().getDdsPlSqlObjectDef(id);
    }

    public DdsFunctionDef getFunctionDef(final Id funcId) {
        return releaseCache.getRelease().getFunctionDef(funcId);
    }

    public Id getMasterKeyPropIdByDetailKeyPropId(final DdsReferenceDef mdRef, final Id detKeyColId) {
        for (DdsReferenceDef.ColumnsInfoItem refProp : mdRef.getColumnsInfo()) {
            if (refProp.getChildColumnId().equals(detKeyColId)) {
                return refProp.getParentColumnId();
            }
        }
        return null;
    }

    public DdsReferenceDef getMasterReferenceDef(final DdsTableDef table) {
        return releaseCache.getRelease().getMasterReferenceDef(table);
    }

    @Override
    public boolean isDefInDomain(final Id defId, final Id domainId) {
        return releaseCache.getRelease().isDefInDomain(defId, domainId);
    }

    public Collection<Id> getRootDomainIds() {
        return releaseCache.getRelease().getRootDomainIds();
    }

    public Collection<Id> getDefIdsByType(final EDefType type) {
        return releaseCache.getRelease().getDefIdsByType(type);
    }

    public String getDefTitleById(final Id defId) {
        if (defId != null && defId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            RadClassDef classDef = getClassDef(defId);
            if (classDef != null) {
                return classDef.getTitle();
            }
        }
        return releaseCache.getRelease().getDefTitleById(defId);
    }

    public String getImgFileNameByImgId(final Id id) {
        return releaseCache.getRelease().getImgFileNameByImgId(id);
    }

    public static class ImageData {

        private final String mimeType;
        private final byte[] data;

        public ImageData(String mimeType, byte[] data) {
            this.mimeType = mimeType;
            this.data = data;
        }

        public String getMimeType() {
            return mimeType;
        }

        public byte[] getData() {
            return data;
        }
    }

    public ImageData getDbImageData(final Id contextId, final Id imageId) {
        return releaseCache.getPrivateClassloader().getDbImageData(contextId, imageId);
    }

    public String getTargetNamespaceUriBySchemaDefId(final Id schemaId) {
        return releaseCache.getRelease().getTargetNamespaceUriBySchemaDefId(schemaId);
    }

    public Object newClassInstance(final Id classId, final Object[] newInstanceArgs) {
        if (!arte.isInTransaction()) {
            throw new IllegalUsageError("ARTE transaction is not started");
        }
        Class c = null;
        try {
            final String factory = getAdsFactoryClassNameByIClassId(classId);
            if (factory == null) {
                throw new ClassNotFoundException("Unable to find factory for class # " + classId);
            }
            c = releaseCache.getPrivateClassloader().loadClass(factory);
            return ((IDefinitionFactory) c.newInstance()).newInstance(classId, newInstanceArgs);
        } catch (ClassNotFoundException e) {
            throw new RadixError("Can't instantiate class #" + String.valueOf(classId) + ": " + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalArgumentException e) {
            throw new RadixError("Can't instantiate class #" + String.valueOf(classId) + ": " + ExceptionTextFormatter.getExceptionMess(e), e);
//        } catch (InvocationTargetException e) {
//            throw new RadixError("Can't instantiate class #"+String.valueOf(classId)+": " + ExceptionTextFormatter.getExceptionMess(e), e);
//        } catch (NoSuchMethodException e) {
//            throw new IllegalUsageError("Default constructor is not defined for #" + classId, e);
        } catch (InstantiationException e) {
            if (c != null && (c.getModifiers() & Modifier.ABSTRACT) != 0) // abstract
            {
                throw new RadixError("Can't instantiate abstract entity class #" + String.valueOf(classId), e);
            } else {
                final Throwable cause = e.getCause() != null ? e.getCause() : e;
                throw new RadixError("Can't instantiate entity class #" + String.valueOf(classId) + ": " + ExceptionTextFormatter.getExceptionMess(cause), cause);
            }
        } catch (IllegalAccessException e) {
            throw new RadixError("Can't instantiate class #" + String.valueOf(classId) + ": " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public interface ReleaseCacheFactory {

        ReleaseCache newInstance(Arte arte, Long version) throws ReleaseLoadException;
    }

    public static final class DefaultReleaseCacheFactory implements ReleaseCacheFactory {

        @Override
        public ReleaseCache newInstance(final Arte arte, final Long version) throws ReleaseLoadException {
            return new ReleaseCache(arte, arte.getInstance().getReleasePool().get(version));
        }
    }
}
