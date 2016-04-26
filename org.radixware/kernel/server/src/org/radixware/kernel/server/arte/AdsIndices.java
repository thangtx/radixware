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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.OraclePreparedStatement;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef.IItem;
import org.radixware.kernel.common.defs.IEnumDef.IItems;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.release.EntityUserReference;
import org.radixware.kernel.release.EventCodeMeta;
import org.radixware.kernel.server.dbq.SqmlTranslator;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.trace.IServerThread;

public class AdsIndices extends org.radixware.kernel.release.SrvAdsIndices {

    private final Release release;
    private final Map<Id, Collection<RadClassDef>> classDefListByTabId = new ConcurrentHashMap<>();

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

    public void deployIndicesToDb(final Arte arte) throws SQLException, IOException {
        trunkDeployedIndices(arte);

        deployClassAncestor(arte);

        deployDef2Domain(arte);

        deployEnumItem2Domain(arte);

        deployUserReferences(arte);

        deployArrUserReferences(arte);

        deployEventCodesMeta(arte);

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
        try {
            arte.commit();//to prevent loss of meta on rollback after error on next transaction
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
        }
    }

    private void deployEventCodesMeta(final Arte arte) throws SQLException {
        final OraclePreparedStatement qryDeployEventCodesMeta;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryDeployEventCodesMeta = (OraclePreparedStatement) arte.getDbConnection().get().prepareStatement(
                    "insert into RDX_EventCode2EventParams columns(versionnum, eventCode, eventSeverity, eventSource) values (?,?,?,?)");
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryDeployEventCodesMeta.setLong(1, getRelease().getVersion());
            qryDeployEventCodesMeta.setExecuteBatch(800);
            try {
                for (EventCodeMeta eventCodeMeta : getEventCodes()) {
                    qryDeployEventCodesMeta.setString(2, SqmlTranslator.translateEventCode(eventCodeMeta.getBundleId(), eventCodeMeta.getId()));
                    qryDeployEventCodesMeta.setLong(3, eventCodeMeta.getSeverity());
                    qryDeployEventCodesMeta.setString(4, eventCodeMeta.getEventSource());
                    qryDeployEventCodesMeta.execute();
                }
                qryDeployEventCodesMeta.sendBatch();
            } finally {
                qryDeployEventCodesMeta.close();
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
        final OraclePreparedStatement qryUserRefs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            final String tableName = refType == EValType.PARENT_REF ? "RDX_ENTITYUSERREF" : "RDX_ENTITYARRUSERREF";
            qryUserRefs = (OraclePreparedStatement) arte.getDbConnection().get().prepareStatement(
                    "insert into " + tableName + " columns(versionnum, tableId, userPropId, deleteMode) values (?,?,?,?)");
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryUserRefs.setLong(1, getRelease().getVersion());
            qryUserRefs.setExecuteBatch(800);
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
                qryUserRefs.sendBatch();
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
        final OraclePreparedStatement qryEnumIt2Domain;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryEnumIt2Domain = (OraclePreparedStatement) arte.getDbConnection().get().prepareStatement(
                    "insert into rdx_enumitem2domain columns(versionnum, enumid, enumitemvalasstr, domainid) values (?,?,?,?)");
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryEnumIt2Domain.setLong(1, getRelease().getVersion());
            qryEnumIt2Domain.setExecuteBatch(800);
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
                qryEnumIt2Domain.sendBatch();
            } finally {
                qryEnumIt2Domain.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void deployDef2Domain(final Arte arte) throws SQLException {
        final OraclePreparedStatement qryDefDomains;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryDefDomains = (OraclePreparedStatement) arte.getDbConnection().get().prepareStatement(
                    "insert into rdx_def2domain columns(versionnum, defid, domainid) values (?,?,?)");
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryDefDomains.setLong(1, getRelease().getVersion());
            qryDefDomains.setExecuteBatch(800);
            try {
                final Set<Id> domainIds = getDefIdsByDomainId().keySet();
                for (Id domainId : domainIds) {
                    for (Id defId : getDefIdsByDomainId().getCollection(domainId)) {
                        qryDefDomains.setString(2, defId.toString());
                        qryDefDomains.setString(3, domainId.toString());
                        qryDefDomains.execute();
                    }
                }
                qryDefDomains.sendBatch();
            } finally {
                qryDefDomains.close();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private void deployClassAncestor(final Arte arte) throws SQLException {
        final OraclePreparedStatement qryClsAncstrs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryClsAncstrs = (OraclePreparedStatement) arte.getDbConnection().get().prepareStatement(
                    "insert into rdx_classancestor columns(versionnum, classid, ancestorid) values (?,?,?)");
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryClsAncstrs.setLong(1, getRelease().getVersion());
            qryClsAncstrs.setExecuteBatch(800);
            try {
                final Set<Id> classIds = getDescenderIdsByClassId().keySet();
                for (Id ancestorId : classIds) {
                    for (Id classId : getDescenderIdsByClassId().getCollection(ancestorId)) {
                        qryClsAncstrs.setString(2, classId.toString());
                        qryClsAncstrs.setString(3, ancestorId.toString());
                        qryClsAncstrs.execute();
                    }
                }
                qryClsAncstrs.sendBatch();
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
            truncQry = arte.getDbConnection().get().prepareCall("begin RDX_ADS_META.truncDeployedIndices(); end;");
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
