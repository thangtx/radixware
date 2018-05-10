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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef.IItem;
import org.radixware.kernel.common.defs.IEnumDef.IItems;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.release.EntityUserReference;
import org.radixware.kernel.release.EventCodeMeta;
import org.radixware.kernel.release.EventCodeMlsMeta;
import org.radixware.kernel.server.dbq.SqmlTranslator;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.jdbc.RadixPreparedStatement;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.trace.IServerThread;

public class AdsIndices extends org.radixware.kernel.release.SrvAdsIndices {

    private final Release release;
    private final Map<Id, Collection<RadClassDef>> classDefListByTabId = new ConcurrentHashMap<>();
    private final Set<EventCodeMlsMeta> eventCodesMlsMeta = new HashSet<>();
    private final IDbQueries delegate;

    private static final String qryDeployEventCodesMetaStmtSQL = "insert into RDX_EventCode2EventParams(versionnum, eventCode, eventSeverity, eventSource) values (?,?,?,?)";
    private static final Stmt qryDeployEventCodesMetaStmt = new Stmt(qryDeployEventCodesMetaStmtSQL, Types.BIGINT, Types.VARCHAR, Types.BIGINT, Types.VARCHAR);
    
    private static final String qryEventCodesMlsStmtSQL = "insert into RDX_EventCodeMls(VersionNum, BundleId, StringId, Language, StringValue) values (?, ?, ?, ?, ?)";
    private static final Stmt qryEventCodesMlsStmt = new Stmt(qryEventCodesMlsStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

    private static final String qryUserRefsStmtSQL = "insert into RDX_ENTITYUSERREF(versionnum, tableId, userPropId, deleteMode) values (?,?,?,?)";
    private static final Stmt qryUserRefsStmt = new Stmt(qryUserRefsStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

    private static final String qryArrUserRefsStmtSQL = "insert into RDX_ENTITYARRUSERREF(versionnum, tableId, userPropId, deleteMode) values (?,?,?,?)";
    private static final Stmt qryArrUserRefsStmt = new Stmt(qryArrUserRefsStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

    private static final String qryEnumIt2DomainStmtSQL = "insert into rdx_enumitem2domain(versionnum, enumid, enumitemvalasstr, domainid) values (?,?,?,?)";
    private static final Stmt qryEnumIt2DomainStmt = new Stmt(qryEnumIt2DomainStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

    private static final String qryDefDomainsStmtSQL = "insert into rdx_def2domain(versionnum, defid, domainid) values (?,?,?)";
    private static final Stmt qryDefDomainsStmt = new Stmt(qryDefDomainsStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR);

    private static final String qryClsAncstrsStmtSQL = "insert into rdx_classancestor(versionnum, classid, ancestorid) values (?,?,?)";
    private static final Stmt qryClsAncstrsStmt = new Stmt(qryClsAncstrsStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR);
    
    private static final String qryTruncDeployedIndicesStmtSQL = "begin RDX_ADS_META.truncDeployedIndices(); end;";
    private static final Stmt qryTruncDeployedIndicesStmt = new Stmt(new Stmt(qryTruncDeployedIndicesStmtSQL));

    private AdsIndices() {
        this.release = null;
        this.delegate = new DelegateDbQueries(this, null);
    }
    
    public AdsIndices(final Release release) {
        super(release.getRevisionMeta(), release.getRepository(), new LocalTracer() {
            private LocalTracer getDelegate() {
                LocalTracer result = null;
                if (Thread.currentThread() instanceof IServerThread) {
                    result = ((IServerThread) Thread.currentThread()).getLocalTracer();
                }
                if (result == null) {
                    result = Instance.get().getTrace().newTracer(EEventSource.INSTANCE.getValue());
                }
                return result;
            }

            @Override
            public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
                getDelegate().put(severity, localizedMess, code, words, isSensitive);
            }

            @Override
            public long getMinSeverity() {
                return getDelegate().getMinSeverity();
            }

            @Override
            public long getMinSeverity(final String eventSource) {
                return getDelegate().getMinSeverity(eventSource);
            }
        });
        this.release = release;
        this.delegate = new DelegateDbQueries(this, null);
    }

    Release getRelease() {
        return release;
    }

    public void addClassAndItsDescendersTo(final Collection<RadClassDef> lst, final Id classId) {
        lst.add(release.getClassDef(classId));
        for (Id desdId : getDescenderIdsByClassId().getCollection(classId)) {
            addClassAndItsDescendersTo(lst, desdId);
        }
    }

    Collection<RadClassDef> getAllClassDefsBasedOnTableByTabId(final Id tableId) {
        Collection<RadClassDef> lst = classDefListByTabId.get(tableId);
        if (lst == null) {
            //TODO: fine-grainded synchronization based on tableId
            synchronized (classDefListByTabId) {
                lst = classDefListByTabId.get(tableId);
                if (lst == null) {
                    lst = new ArrayList<>();
                    addClassAndItsDescendersTo(lst, RadClassDef.getEntityClassIdByTableId(tableId));
                    lst = Collections.unmodifiableCollection(lst);
                    classDefListByTabId.put(tableId, lst);
                }
            }
        }
        return lst;
    }

    Collection<Id> getAllClassIdsBasedOnTableByTabId(final Id tableId) {
        Collection<Id> lst = new ArrayList<>();
        addClassIdAndItsDescenderIdsTo(lst, RadClassDef.getEntityClassIdByTableId(tableId));
        return Collections.unmodifiableCollection(lst);
    }

    private void addClassIdAndItsDescenderIdsTo(final Collection<Id> collection, final Id classId) {
        if (collection != null) {
            collection.add(classId);
            for (Id descId : getDescenderIdsByClassId().getCollection(classId)) {
                addClassIdAndItsDescenderIdsTo(collection, descId);
            }
        }
    }

    public void deployIndicesToDb(final Arte arte) throws SQLException, IOException {
        trunkDeployedIndices(arte);

        deployClassAncestor(arte);

        deployDef2Domain(arte);

        deployEnumItem2Domain(arte);

        deployUserReferences(arte);

        deployArrUserReferences(arte);

        deployEventCodesMeta(arte);
        
        deployEventCodeMlsMeta(arte);
        
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
        try {
            arte.commit();//to prevent loss of meta on rollback after error on next transaction
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
        }
    }
    
    private void deployEventCodesMeta(final Arte arte) throws SQLException {
        final PreparedStatement qryDeployEventCodesMeta;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryDeployEventCodesMeta = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(qryDeployEventCodesMetaStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryDeployEventCodesMeta.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryDeployEventCodesMeta).setExecuteBatch(800);
            try {
                for (EventCodeMeta eventCodeMeta : getEventCodes()) {
                    qryDeployEventCodesMeta.setString(2, SqmlTranslator.translateEventCode(eventCodeMeta.getBundleId(), eventCodeMeta.getId()));
                    qryDeployEventCodesMeta.setLong(3, eventCodeMeta.getSeverity());
                    qryDeployEventCodesMeta.setString(4, eventCodeMeta.getEventSource());
                    qryDeployEventCodesMeta.execute();
                }
                ((RadixPreparedStatement) qryDeployEventCodesMeta).sendBatch();
            } finally {
                qryDeployEventCodesMeta.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    
    void fillEventCodeMlsMeta() {
        Release rel = getRelease();
        
        for (Map.Entry<String, EventCodeMeta> entry : eventCodes.entrySet()) {
            EventCodeMeta meta = entry.getValue();
            final Id bundleId = meta.getBundleId();
            final Id stringId = meta.getId();

            final RadMlStringBundleDef bundle = rel.getMlStringBundleDef(bundleId);
            if (bundle != null) {
                final RadMlStringBundleDef.MultilingualString str = (RadMlStringBundleDef.MultilingualString) bundle.getStringSet(stringId);
                if (str != null) {
                    Map<EIsoLanguage, String> all = str.getAllValues();
                    for (Map.Entry<EIsoLanguage, String> item : all.entrySet()) {
                        eventCodesMlsMeta.add(new EventCodeMlsMeta(bundleId.toString(), stringId.toString(), item.getKey(), item.getValue()));
                    }
                } else if (localTracer != null) {
                    localTracer.put(EEventSeverity.DEBUG, "Null mls-string: bundleId = " + bundleId + ", stringId = " + stringId, null, null, false);
                }
            } else if (localTracer != null) {
                localTracer.put(EEventSeverity.DEBUG, "Null mls-bundle: bundleId = " + bundleId, null, null, false);
            }
        }
    }
    
    private void deployEventCodeMlsMeta(final Arte arte) throws SQLException {
        final PreparedStatement qryDeployMlsMeta;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryDeployMlsMeta = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(qryEventCodesMlsStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryDeployMlsMeta.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryDeployMlsMeta).setExecuteBatch(800);
            try {
                
                for (EventCodeMlsMeta metaItem: eventCodesMlsMeta) {
                    qryDeployMlsMeta.setString(2, metaItem.bundleId);
                    qryDeployMlsMeta.setString(3, metaItem.stringId);
                    qryDeployMlsMeta.setString(4, metaItem.language.getValue());
                    qryDeployMlsMeta.setString(5, metaItem.value);
                    qryDeployMlsMeta.execute();
                }
                ((RadixPreparedStatement) qryDeployMlsMeta).sendBatch();
            } finally {
                qryDeployMlsMeta.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void deployUserReferences(final Arte arte) throws SQLException {
        deployUserReferencesImpl(arte, EValType.PARENT_REF);
    }

    private void deployArrUserReferences(final Arte arte) throws SQLException {
        deployUserReferencesImpl(arte, EValType.ARR_REF);
    }

    private void deployUserReferencesImpl(final Arte arte, final EValType refType) throws SQLException {
        final PreparedStatement qryUserRefs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
//            final String tableName = refType == EValType.PARENT_REF ? "RDX_ENTITYUSERREF" : "RDX_ENTITYARRUSERREF";
            qryUserRefs = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(refType == EValType.PARENT_REF ? qryUserRefsStmt : qryArrUserRefsStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryUserRefs.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryUserRefs).setExecuteBatch(800);
            try {
                for (Map.Entry<Id, Collection<EntityUserReference>> entry : getEntityUserReferences().entrySet()) {
                    qryUserRefs.setString(2, entry.getKey().toString());
                    final Map<Id, EntityUserReference> refsByPropId = new HashMap<>();
                    for (EntityUserReference ref : entry.getValue()) {
                        if (ref.getType() == refType && ref.getDeleteMode() != null && ref.getDeleteMode() != EDeleteMode.NONE) {
                            final EntityUserReference existingRef = refsByPropId.get(ref.getUserPropId());
                            if (existingRef == null) {
                                refsByPropId.put(ref.getUserPropId(), ref);
                                qryUserRefs.setString(3, ref.getUserPropId().toString());
                                qryUserRefs.setString(4, ref.getDeleteMode().getValue());
                                qryUserRefs.execute();
                            } else if (!existingRef.equals(ref)) {
                                logDuplicatedUserRef(arte, existingRef, ref);
                            }
                        }
                    }
                }
                ((RadixPreparedStatement) qryUserRefs).sendBatch();
            } finally {
                qryUserRefs.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void logDuplicatedUserRef(final Arte arte, final EntityUserReference existingRef, final EntityUserReference ref) {
        arte.getTrace().put(Messages.MLS_ID_DUPLICATED_USER_REF, new ArrStr(existingRef.toString(), ref.toString()));
    }

    private void deployEnumItem2Domain(final Arte arte) throws SQLException {
        final PreparedStatement qryEnumIt2Domain;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryEnumIt2Domain = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(qryEnumIt2DomainStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryEnumIt2Domain.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryEnumIt2Domain).setExecuteBatch(800);
            try {
                final Collection<Id> enumIds = getDefIdsByType(EDefType.ENUMERATION);
                for (Id enumId : enumIds) {
                    qryEnumIt2Domain.setString(2, enumId.toString());
                    final IItems<? extends IItem> items;
                    try {
                        items = arte.getDefManager().getEnumDef(enumId).getItems();
                    } catch (DefinitionNotFoundError e) {
                        continue;
                    }
                    for (IItem item : items.list(EScope.ALL)) {
                        final Collection<Id> domainIds = item.getDomainIds();
                        if (!domainIds.isEmpty()) {
                            qryEnumIt2Domain.setString(3, item.getValue().toString());
                            for (Id domainId : domainIds) {
                                qryEnumIt2Domain.setString(4, domainId.toString());
                                qryEnumIt2Domain.execute();
                            }
                        }
                    }
                }
                ((RadixPreparedStatement) qryEnumIt2Domain).sendBatch();
            } finally {
                qryEnumIt2Domain.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void deployDef2Domain(final Arte arte) throws SQLException {
        final PreparedStatement qryDefDomains;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryDefDomains = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(qryDefDomainsStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryDefDomains.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryDefDomains).setExecuteBatch(800);
            try {
                final Set<Id> domainIds = getDefIdsByDomainId().keySet();
                for (Id domainId : domainIds) {
                    for (Id defId : getDefIdsByDomainId().getCollection(domainId)) {
                        qryDefDomains.setString(2, defId.toString());
                        qryDefDomains.setString(3, domainId.toString());
                        qryDefDomains.execute();
                    }
                }
                ((RadixPreparedStatement) qryDefDomains).sendBatch();
            } finally {
                qryDefDomains.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void deployClassAncestor(final Arte arte) throws SQLException {
        final PreparedStatement qryClsAncstrs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryClsAncstrs = ((RadixConnection) arte.getDbConnection().get()).prepareStatement(qryClsAncstrsStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryClsAncstrs.setLong(1, getRelease().getVersion());
            ((RadixPreparedStatement) qryClsAncstrs).setExecuteBatch(800);
            try {
                final Set<Id> classIds = getDescenderIdsByClassId().keySet();
                for (Id ancestorId : classIds) {
                    for (Id classId : getDescenderIdsByClassId().getCollection(ancestorId)) {
                        qryClsAncstrs.setString(2, classId.toString());
                        qryClsAncstrs.setString(3, ancestorId.toString());
                        qryClsAncstrs.execute();
                    }
                }
                ((RadixPreparedStatement) qryClsAncstrs).sendBatch();
            } finally {
                qryClsAncstrs.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void trunkDeployedIndices(final Arte arte) throws SQLException {
        final CallableStatement truncQry;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            truncQry = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryTruncDeployedIndicesStmt);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            try {
                truncQry.execute();
            } finally {
                truncQry.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
}
