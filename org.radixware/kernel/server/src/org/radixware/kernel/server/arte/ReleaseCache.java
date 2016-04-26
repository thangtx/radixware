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
import java.security.AccessController;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.meta.RadEnumDef;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.BufferedPool;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.dbq.DbQueryBuilder;
import org.radixware.kernel.server.dbq.ObjectClassGuidQuery;
import org.radixware.kernel.server.dbq.ReadObjectQuery;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.LoadOldVersionFromDirError;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.instance.ObjectCache;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.SqlClass;
import org.radixware.kernel.starter.radixloader.RadixDirLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.msdl.MessageElementDocument;

/**
 * Container for versioned data owned by ARTE. Stores non-shared meta for single
 * revision. ReleaseCache (as well as it's complementary shared part Release)
 * should never be accessed directly. All data access should be done via
 * DefManager.
 *
 */
public class ReleaseCache {

    private static final Id TIMING_SECTION_ENUM_ID = Id.Factory.loadFrom("acsJIXOZF6PJJAM5AZKRLYLYFA75E");
    private static final Id CPU_DOMAIN_ID = Id.Factory.loadFrom("dmnHX5WB4PSUFAMFG4EOWUZNHDY5Q");
    private static final Id DB_DOMAIN_ID = Id.Factory.loadFrom("dmnM3XPCXZTCFGEZJMAAWHFAHN3L4");
    private static final Id EXT_DOMAIN_ID = Id.Factory.loadFrom("dmnRCDLCTU2I5ADFBCV2GUTC7FES4");
    //
    private final Arte arte;
    private final Release release;
    private final AdsClassLoader arteClassloader;
    private final ExistingObjectsPool existingObjects = new ExistingObjectsPool(BufferedPool.ERegistrationMode.IMMEDIATELY);
    private final Set<SqlClass.PreparedStatementsCache> registeredCaches = new HashSet<>();
    private final ReleaseCacheDbQueries dbQueries;
    private final DbQueryBuilder dbQueryBuilder;
    private final Map<EDrcServerResource, String> srvResRoleLists = new HashMap<>();
    private final UserRepMlbDefLoader userRepMlbDefLoader = new UserRepMlbDefLoader();
    private final Map<Id, RootMsdlScheme> msdlScemasById = new HashMap<>();
    private final AppRolesCache appRolesCache = new AppRolesCache();
    //enumeration meta is hard to share because it contains reference to enumeration executable part //TODO
    private final Release.AdsDefLoader<RadEnumDef> constSetDefs;
    //TODO: share contextless commands meta
    private final Release.AdsDefLoader<RadContextlessCommandDef> contextlessCommandDefs;
    private final Map<String, ArteProfiler.EWaitType> section2type;
    private final LoadErrorsLog errorsLog;
    private final Map<Id, RoleLoadError> appRoleLoadErrors = new HashMap<>();
    private final ObjectCache userCache = new ObjectCache();

    public ReleaseCache(final Arte arte, final Release release) {
        this.arte = arte;
        this.release = release;
        arteClassloader = AccessController.doPrivileged(new AdsClassLoader.CreateAction(this));
        if (arteClassloader == null) {
            if (release.getVersion() < arte.getActualVersion() && RadixLoader.getInstance() instanceof RadixDirLoader) { //RADIX-2571
                throw new LoadOldVersionFromDirError();
            } else {
                throw new RadixError("Can't create class loader for version #" + String.valueOf(release.getVersion()));
            }
        }
        dbQueryBuilder = new DbQueryBuilder(arte);
        dbQueries = new ReleaseCacheDbQueries(this);
        errorsLog = new LoadErrorsLog() {
            @Override
            protected void logError(Id defId, RadixError err) {
                arte.getTrace().put(EEventSeverity.WARNING, "Can't load definition #" + defId + ": " + getArte().getTrace().exceptionStackToString(err), EEventSource.DEF_MANAGER);
            }
        };
        if (errorsLog instanceof LoadErrorsLog) {
            ((LoadErrorsLog) errorsLog).setTryLogInsteadOfExceptionOnNotFound(true);
        }
        constSetDefs = new Release.AdsDefLoader<>(arteClassloader, EDefType.ENUMERATION, 1000, errorsLog);
        constSetDefs.loadAllDefs();
        contextlessCommandDefs = new Release.AdsDefLoader<>(arteClassloader, EDefType.CONTEXTLESS_COMMAND, 1000, errorsLog);
        contextlessCommandDefs.loadAllDefs();
        section2type = buildSection2Type();
        if (errorsLog instanceof LoadErrorsLog) {
            ((LoadErrorsLog) errorsLog).setTryLogInsteadOfExceptionOnNotFound(false);
        }
    }

    public Class<?> getClass(final Id id) {
        try {
            release.checkClassDefLoadError(id);
            return arteClassloader.loadExecutableClassById(id);
        } catch (ClassNotFoundException e) {
            throw new DefinitionNotFoundError(id, e);
        }
    }

    RootMsdlScheme getMsdlScheme(final Id id) {
        RootMsdlScheme schema = msdlScemasById.get(id);
        if (schema != null) {
            return schema;
        }
        final String fileName = getRelease().getRepository().getAdsIndices().getXmlFileNameByMsdlSchemaDefId(id);
        final MessageElementDocument xSchema;
        try {
            xSchema = MessageElementDocument.Factory.parse(getPrivateClassloader().getResourceAsStream(fileName));
        } catch (XmlException ex) {
            throw new DefinitionNotFoundError(id, ex);
        } catch (IOException ex) {
            throw new DefinitionNotFoundError(id, ex);
        }
        if (xSchema.getMessageElement() == null) {
            throw new DefinitionNotFoundError(id);
        }
        try {
            schema = new RootMsdlScheme(getPrivateClassloader(), xSchema.getMessageElement());
        } catch (SmioException ex) {
            throw new DefinitionNotFoundError(id, ex);
        }
        msdlScemasById.put(id, schema);
        return schema;
    }

    public EntityPropVals readOwnProps(final Pid pid) {
        final EntityPropVals propVals = new EntityPropVals();
        final DdsIndexDef.ColumnsInfo pkColumns = pid.getTable().getPrimaryKey().getColumnsInfo();
        final List<Object> pkVals = pid.getPkVals();
        for (int i = 0; i < pkColumns.size(); i++) {
            propVals.putPropValById(pkColumns.get(i).getColumnId(), pkVals.get(i));
        }
        final ReadObjectQuery readRecordQry = dbQueryBuilder.buildRead(arte, pid.getTable());
        try {
            readRecordQry.read(pid, propVals);
            return propVals;
        } finally {
            readRecordQry.free();
        }
    }

    public RadContextlessCommandDef getContextlessCommandDef(final Id id) {
        return contextlessCommandDefs.getDef(id);
    }

    public Collection<RadContextlessCommandDef> getContextlessCommandDefs() {
        return contextlessCommandDefs.getAllDefs();
    }

    public DbQueryBuilder getDbQueryBuilder() {
        return dbQueryBuilder;
    }

    public RadEnumDef getEnumDef(final Id id) {
        return constSetDefs.getDef(id);
    }

    private Map<String, ArteProfiler.EWaitType> buildSection2Type() {
        final Map<String, ArteProfiler.EWaitType> result = new HashMap<>();
        final RadEnumDef timingSectionEnum = getEnumDef(TIMING_SECTION_ENUM_ID);
        if (timingSectionEnum == null) {
            return result;//error on loading enum, should be already logged
        }
        for (IEnumDef.IItem item : timingSectionEnum.getItems().list(ExtendableDefinitions.EScope.ALL)) {
            ArteProfiler.EWaitType type = null;
            if (item.getDomainIds() != null) {
                if (item.getDomainIds().contains(CPU_DOMAIN_ID)) {
                    type = ArteProfiler.EWaitType.CPU;
                } else if (item.getDomainIds().contains(DB_DOMAIN_ID)) {
                    type = ArteProfiler.EWaitType.DB;
                } else if (item.getDomainIds().contains(EXT_DOMAIN_ID)) {
                    type = ArteProfiler.EWaitType.EXT;
                }
            }
            if (type == null) {
                type = ArteProfiler.EWaitType.CPU;
            }
            result.put(item.getValue().toString(), type);
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * @return type of the section with given name. If enum item with this name
     * couldn't be found, null is returned. If enum item is found, but it
     * doesn't belong to CPU, DB or EXT domain, CPU type is returned. Results
     * are cached.
     */
    public ArteProfiler.EWaitType getSectionType(final String sectionId) {
        if (sectionId == null) {
            return null;
        }
        //value sectionName may be complex: there may be enumValue and some suffix separated by colon mark,        
        //so we should check it before looking for enumeration item
        String sectionName;
        int index = sectionId.indexOf(':');
        if (index > 0) {
            sectionName = sectionId.substring(0, index);
        } else {
            sectionName = sectionId;
        }

        return section2type.get(sectionName);
    }

    public RadClassDef getClassDef(final Id id) {
        if (id == null) {
            return null;
        }
        if (id.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            errorsLog.checkLoadError(id);
            return (RadClassDef) DefLoadUtils.loadDef(id, arteClassloader, errorsLog);
        }
        return release.getClassDef(id);
    }

    public Collection<RadClassDef> getClassDefs() {
        //unable to return full list with user class defs
        //because they are layzy-loaded
        return release.getClassDefs();
    }

    public RadMlStringBundleDef getStringBundleDef(final Id id) {
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE && (id.toString().startsWith("mlb" + EDefinitionIdPrefix.USER_DEFINED_REPORT.getValue())
                || id.toString().startsWith("mlb" + EDefinitionIdPrefix.USER_FUNC_CLASS.getValue()))) {
            errorsLog.checkLoadError(id);
            return (RadMlStringBundleDef) DefLoadUtils.loadDef(id, arteClassloader, errorsLog);
        }
        return release.getMlStringBundleDef(id);
    }

    public RoleLoadError getRoleLoadError(final Id id) {
        return appRoleLoadErrors.get(id);
    }

    public Arte getArte() {
        return arte;
    }

    public Release getRelease() {
        return release;
    }

    public ObjectCache getUserCache() {
        return userCache;
    }

    void clearDbQueriesCaches() {
        try {
            dbQueryBuilder.close();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " DbQueryBuiler");
        }
        try {
            dbQueries.close();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " DbQueries");
        }
        try {
            clearStatementCaches();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " Statements Cache");
        }
    }

    final void close() {
        try {
            clearDbQueriesCaches();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " DbQueries");
        }
        try {
            userCache.clear();
        } catch (Exception ex) {
            arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " UserCache");
        }
    }

    void resetServerResourceRoleIdsStrCache() {
        srvResRoleLists.clear();
    }

    public final String getServerResourceRoleIdsStr(final EDrcServerResource resource) {
        String roleList = srvResRoleLists.get(resource);
        if (roleList == null) {
            roleList = buildServerResourceRoleIdsStr(resource);
            srvResRoleLists.put(resource, roleList);
        }
        return roleList;

    }

    private String buildServerResourceRoleIdsStr(final EDrcServerResource resource) {
        final StringBuilder lst = new StringBuilder();
        final Collection<RadRoleDef> roles = getAppAndStandardRoles();
        if (!roles.isEmpty()) {
            final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, resource);
            Restrictions restr;
            for (RadRoleDef role : roles) {
                restr = role.getResourceRestrictions(resHashKey);
                if (!restr.getIsAccessRestricted()) {
                    lst.append(',');
                    lst.append(role.getId().toString());
                }
            }
        }
        return lst.toString();
    }

    /**
     * @return Application and standard roles
     */
    public Collection<RadRoleDef> getAppAndStandardRoles() {
        return getAppAndStandardRoles(false);
    }

    public List<RadRoleDef> getAppAndAccessibleStandardRoles() {
        return getAppAndStandardRoles(true);
    }

    private List<RadRoleDef> getAppAndStandardRoles(boolean onlyAccessible) {
        appRolesCache.updateAppRoles();
        Collection<RadRoleDef> standardRoleDefs = onlyAccessible ? release.getAccessibleRoleDefs() : release.getRoleDefs();
        Collection<RadRoleDef> appRoleDefs = appRolesCache.listLoaded();
        final ArrayList<RadRoleDef> result = new ArrayList<>((standardRoleDefs == null ? 0 : standardRoleDefs.size()) + (appRoleDefs == null ? 0 : appRoleDefs.size()));
        if (appRoleDefs != null) {
            result.addAll(appRoleDefs);
        }
        if (standardRoleDefs != null) {
            result.addAll(standardRoleDefs);
        }
        return result;
    }

    private void clearStatementCaches() {
        for (SqlClass.PreparedStatementsCache cache : registeredCaches) {
            try {
                cache.clearCache(arte);
            } catch (Exception ex) {
                arte.logCleanupError(ex, "ReleaseCache #" + getRelease().getVersion() + " PreparedStatementsCache");
            }
        }
        registeredCaches.clear();
    }

    public void sendStatementsBatches(final boolean bThrowExceptionOnDbErr) {
        for (SqlClass.PreparedStatementsCache cache : registeredCaches) {
            try {
                cache.sendBatch(arte);
            } catch (SQLException e) {
                if (bThrowExceptionOnDbErr) {
                    throw new DatabaseError("Statement execution error: " + ExceptionTextFormatter.getExceptionMess(e), e);
                } else {
                    arte.getTrace().put(EEventSeverity.DEBUG, "SQLException happend on sending statements batches is hidden: " + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.ARTE_DB);
                }
            }
        }
    }

    public void resetStatementsBatchSettings() {
        for (SqlClass.PreparedStatementsCache cache : registeredCaches) {
            cache.setBatchSize(1);
        }
    }

    void actualizeDbIndices() {
        try {
            if (dbQueries.isVersionIndicesDeployed()) {
                return;
            }
            release.getRepository().getAdsIndices().deployIndicesToDb(arte);
        } catch (SQLException e) {
            throw new DatabaseError("Can't deploy ADS Indices to database: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IOException e) {
            throw new DatabaseError("Can't deploy ADS Indices to database: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    //private HashMap singleClassIdByEntityId = null;
    @Deprecated
    public Id getRealEntityClassId(final Pid pid, final EntityPropVals loadedProps) {
        final DdsTableDef tab = pid.getTable();
        if (!tab.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
            return RadClassDef.getEntityClassIdByTableId(tab.getId());
        }
        if (loadedProps != null) {
            try {
                return Id.Factory.loadFrom((String) loadedProps.getPropValById(tab.getClassGuidColumn().getId()));
            } catch (PropNotLoadedException e) {
            }
        }
        final ObjectClassGuidQuery q = dbQueryBuilder.buildClassGuidQuery(pid.getTable());
        try {
            return q.getClassGuid(pid);
        } finally {
            q.free();
        }
    }

    ExistingObjectsPool getExistingObjects() {
        return existingObjects;
    }

    public void registerStatementsCache(final SqlClass.PreparedStatementsCache cache) {
        registeredCaches.add(cache);
    }

    public AdsClassLoader getPrivateClassloader() {
        return arteClassloader;
    }

    public RadRoleDef getAppRole(final Id id) {
        return appRolesCache.getAppRoleDef(id);
    }

    public RadMlStringBundleDef getUserRepMlbDef(final Id id) {
        return userRepMlbDefLoader.getUserRepMlbDef(id);
    }

    public class AppRolesCache {

        private long lastCheckTime = 0;
        private final long UPDATE_PERIOD = 30 * 1000;
        private Map<Id, RoleHolder> appRoles;

        private class RoleHolder {

            private Timestamp lastModified;
            private RadRoleDef def;

            public RoleHolder(Timestamp lastModified, RadRoleDef def) {
                this.lastModified = lastModified;
                this.def = def;
            }
        }

        private boolean mustCheckForUpdates() {
            return System.currentTimeMillis() - lastCheckTime > UPDATE_PERIOD || appRoles == null;
        }

        public List<RadRoleDef> listLoaded() {
            if (appRoles == null) {
                return Collections.emptyList();
            }
            final List<RadRoleDef> list = new ArrayList<>(appRoles.size());
            for (RoleHolder h : appRoles.values()) {
                list.add(h.def);
            }
            return list;
        }

        private RadRoleDef loadAppRole(Id id) {
            DbQueryBuilder.AppRoleInfo roleInfo = getDbQueryBuilder().checkAppRoleStatus(id);
            if (roleInfo == null) {
                arte.getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.getExceptionMess(new DefinitionNotFoundError(id)), EEventSource.DEF_MANAGER);
                return createVirtualInvalidRole(id);
            } else {
                if (roleInfo.runtimeDefined) {
                    try {
                        RadRoleDef roleDef = (RadRoleDef) DefLoadUtils.loadDef(id, arteClassloader, errorsLog);
                        roleDef.link();
                        return roleDef;
                    } catch (Exception ex) {
                        arte.getTrace().put(EEventSeverity.ERROR, "Unable to load role #" + id + " '" + roleInfo.name + "': " + ExceptionTextFormatter.throwableToString(ex), EEventSource.DEF_MANAGER);
                        appRoleLoadErrors.put(id, new RoleLoadError(id, roleInfo, ex));
                        return createVirtualInvalidRole(id);
                    }
                } else {
                    return new RadRoleDef(arte, id, roleInfo.name, roleInfo.description, null, null, null, null, false, false, true);
                }
            }
        }

        private RadRoleDef createVirtualInvalidRole(final Id id) {
            return new RadRoleDef(arte, id, "undefined-empty-role", "undefined-empty-role", null, null, null, null, false, false, true, true);
        }

        public RadRoleDef getAppRoleDef(Id id) {
            if (EDefinitionIdPrefix.APPLICATION_ROLE == id.getPrefix()) {
                updateAppRoles();
                RoleHolder holder = appRoles.get(id);
                if (holder == null) {
                    holder = new RoleHolder(new Timestamp(System.currentTimeMillis()), createVirtualInvalidRole(id));
                    arte.getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.getExceptionMess(new DefinitionNotFoundError(id)), EEventSource.DEF_MANAGER);
                    appRoles.put(id, holder);
                }
                return holder.def;
            }
            return null;
        }

        private void updateAppRoles() {
            if (mustCheckForUpdates()) {
                try {
                    Map<Id, Timestamp> roleData = new HashMap<>();
                    getDbQueryBuilder().readAppRoles(roleData);
                    if (appRoles == null) {//first attempt
                        if (!roleData.isEmpty()) {
                            appRoles = new HashMap<>();
                            for (Map.Entry<Id, Timestamp> roleInfo : roleData.entrySet()) {
                                final Id roleId = roleInfo.getKey();
                                RadRoleDef def = loadAppRole(roleId);
                                appRoles.put(roleInfo.getKey(), new RoleHolder(roleInfo.getValue(), def));
                            }
                        }
                    } else {
                        //remove missing and reload obsolete
                        for (Id id : new ArrayList<>(appRoles.keySet())) {
                            boolean exists = roleData.containsKey(id);
                            if (!exists) {
                                appRoles.remove(id);
                            } else {
                                Timestamp lastUpdated = roleData.get(id);
                                if (lastUpdated != null) {//was updated
                                    RoleHolder holder = appRoles.get(id);
                                    if (holder.lastModified == null || holder.lastModified.before(lastUpdated)) {
                                        RadRoleDef def = loadAppRole(id);
                                        appRoles.put(id, new RoleHolder(lastUpdated, def));
                                    }
                                }
                            }
                        }
                        //add new
                        for (Map.Entry<Id, Timestamp> roleInfo : roleData.entrySet()) {
                            final Id roleId = roleInfo.getKey();
                            if (appRoles.get(roleId) == null) {
                                RadRoleDef def = loadAppRole(roleId);
                                appRoles.put(roleInfo.getKey(), new RoleHolder(roleInfo.getValue(), def));
                            }
                        }
                    }

                } finally {
                    lastCheckTime = System.currentTimeMillis();
                }
            }
        }
    }

    public class UserRepMlbDefLoader {

        private Map<Id, RadMlStringBundleDef> bundles = new HashMap<>(100);

        public RadMlStringBundleDef getUserRepMlbDef(final Id id) {
            if (id == null || !id.toString().startsWith("mlbrpu")) {
                throw new IllegalStateException(id + " is not user mlb identifier");
            }
            RadMlStringBundleDef bundle = bundles.get(id);
            if (bundle != null) {
                return bundle;
            }
            errorsLog.checkLoadError(id);
            bundle = (RadMlStringBundleDef) DefLoadUtils.loadDef(id, arteClassloader, errorsLog);
            if (bundle != null) {
                bundles.put(id, bundle);
                return bundle;
            } else {
                errorsLog.checkLoadError(id);
            }
            throw new DefinitionNotFoundError(id);
        }
    }
}
