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
package org.radixware.kernel.server.dbq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.EPaginationMethod;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.translate.SqmlPreprocessor;
import org.radixware.kernel.common.types.AggregateFunctionCall;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SoftCache;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSortingDef;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.SqmlTranslateResult;

public final class DbQueryBuilder {

    private final Arte arte;

    public DbQueryBuilder(final Arte arte) {
        this.upExecStmts = new LinkedList<PreparedStatement>();
        this.arte = arte;

    }
    private final SoftCache<String, SelectQuery> selects = new SoftCache<>(9929, 10);
    private final SoftCache<String, SelectParentQuery> selectParents = new SoftCache<>(4603, 5);
    private final SoftCache<String, DeleteGroupQuery> deleteGrps = new SoftCache<>(509, 5);
    private final SoftCache<String, ReadObjectQuery> reads = new SoftCache<>(9929, 20);
    private final SoftCache<Id, ObjectClassGuidQuery> getClassGuidsByTabId = new SoftCache<>(2081, 20);
    private final SoftCache<Id, UpObjectOwnerQuery> upObjectOwnersByTabId = new SoftCache<>(23);
    private final SoftCache<Id, ObjectExistsQuery> existsByTabId = new SoftCache<>(2081, 10);
    private final SoftCache<String, TranLockObjectQuery> tranLocks = new SoftCache<>(2081, 10);
    private final SoftCache<Id, DeleteObjectQuery> deletes = new SoftCache<>(2081, 20);
    private final SoftCache<String, ObjectPkQuery> pkQueries = new SoftCache<>(23, 10);
    private final SoftCache<String, UpdateObjectQuery> updates = new SoftCache<>(23, 10);
    private final SoftCache<String, CreateObjectQuery> creates = new SoftCache<>(23, 10);
    private final SoftCache<Id, ReadObjectQuery> recordReads = new SoftCache<>(2081, 20);

    //Public methods
    public final ReadObjectQuery buildRead(final Entity entity, final List<Id> virtualColumns, final boolean forUpdate, final Long waitTime) {
        final String qryHashKey = getReadHashKey(entity, virtualColumns, forUpdate, waitTime);
        ReadObjectQuery qry = reads.get(qryHashKey);
        //if (qry == null && !reads.containsKey(qryHashKey) || qry != null && qry.isBusy){
        if (qry == null || qry.getIsBusy()) {
            final ReadObjectQuery oldQry = qry;
            final boolean bOk;
            final ObjectQuerySqlBuilder builder;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(entity);
                bOk = builder.buildRead(virtualColumns, forUpdate, waitTime);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                final String sql = builder.getQuerySql();
                arte.getTrace().put(EEventSeverity.DEBUG, "Read built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                qry = new ReadObjectQuery(
                        arte,
                        entity.getDdsMeta(),
                        sql,
                        builder.getQueryFields(),
                        builder.getQueryParams(),
                        entity.getRadMeta().getName());
            } else {
                qry = null;//throw new DbpDbQueryBuilderError("\"Read\" query requested but not built", null);
            }
            if (oldQry != null) {
                oldQry.deletedFromCache(true);
            }
            reads.put(qryHashKey, qry);
        }
        return qry;
    }

    public final ReadObjectQuery buildRead(final Arte arte, final DdsTableDef tableDef) {
        ReadObjectQuery qry = recordReads.get(tableDef.getId());
        if (qry == null || qry.getIsBusy()) {
            final ReadObjectQuery oldQry = qry;
            final boolean bOk;
            final ObjectQuerySqlBuilder builder;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, tableDef);
                bOk = builder.buildRead(null, false, 0l);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                final String sql = builder.getQuerySql();
                arte.getTrace().put(EEventSeverity.DEBUG, "Record read built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                qry = new ReadObjectQuery(
                        arte,
                        tableDef,
                        sql,
                        builder.getQueryFields(),
                        builder.getQueryParams(),
                        tableDef.getDbName());
            } else {
                qry = null;//throw new DbpDbQueryBuilderError("\"Read\" query requested but not built", null);
            }
            if (oldQry != null) {
                oldQry.deletedFromCache(true);
            }
            recordReads.put(tableDef.getId(), qry);
        }
        return qry;
    }

    public CallableStatement prepareCallWithProfiling(final String sql) throws SQLException {
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_PREPARE);
        try {
            return arte.getDbConnection().get().prepareCall(sql);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_PREPARE);
        }
    }

    public PreparedStatement prepareStatementWithProfiling(final String sql) throws SQLException {
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_PREPARE);
        try {
            return arte.getDbConnection().get().prepareStatement(sql);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_PREPARE);
        }
    }

    @Deprecated
    public final ObjectClassGuidQuery buildClassGuidQuery(final DdsTableDef table) {
        ObjectClassGuidQuery qry = getClassGuidsByTabId.get(table.getId());
        if (qry == null || qry.getIsBusy()) {
            final ObjectClassGuidQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, table);
                bOk = builder.buildClassGuidRead();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                qry = new ObjectClassGuidQuery(
                        arte,
                        table,
                        builder.getQuerySql(),
                        builder.getQueryFields(),
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                getClassGuidsByTabId.put(table.getId(), qry);
            } else {
                throw new DbQueryBuilderError("\"Class guid\" query requested but not built", null);
            }
        }
        return qry;
    }

    public final UpObjectOwnerQuery buildUpObjectOwnerQuery(final DdsTableDef table) {
        UpObjectOwnerQuery qry = upObjectOwnersByTabId.get(table.getId());
        if (qry == null || qry.getIsBusy()) {
            final UpObjectOwnerQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, table);
                bOk = builder.buildUpObjectOwnerRead();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                qry = new UpObjectOwnerQuery(
                        arte,
                        table,
                        builder.getQuerySql(),
                        builder.getQueryFields(),
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                upObjectOwnersByTabId.put(table.getId(), qry);
            } else {
                throw new DbQueryBuilderError("User property owner query requested but not built", null);
            }
        }
        return qry;
    }

    public final ObjectExistsQuery buildExsistsQuery(final DdsTableDef table) {
        ObjectExistsQuery qry = existsByTabId.get(table.getId());
        if (qry == null || qry.getIsBusy()) {
            final ObjectExistsQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, table);
                bOk = builder.buildExistsQry();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                qry = new ObjectExistsQuery(
                        arte,
                        table,
                        builder.getQuerySql(),
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                existsByTabId.put(table.getId(), qry);
            } else {
                throw new DbQueryBuilderError("\"Exsists\" query requested but not built", null);
            }
        }
        return qry;
    }

    public final TranLockObjectQuery buildTranLockObjectQuery(final DdsTableDef table, final Long waitTime) {
        final String qryHashKey = getLockObjectHashKey(EEntityLockMode.TRANSACTION, table, waitTime);
        TranLockObjectQuery qry = tranLocks.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final TranLockObjectQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, table);
                bOk = builder.buildTranLockObjectQry(waitTime);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (!bOk) {
                throw new DbQueryBuilderError("\"Lock\" query requested but not built", null);
            }
            qry = new TranLockObjectQuery(
                    arte,
                    table,
                    builder.getQuerySql(),
                    builder.getQueryParams());
            if (oldQry != null) {
                oldQry.deletedFromCache(true);
            }
            tranLocks.put(qryHashKey, qry);
        }
        return qry;
    }

    public final DeleteObjectQuery buildDeleteQuery(final DdsTableDef table) {
        DeleteObjectQuery qry = deletes.get(table.getId());
        if (qry == null || qry.getIsBusy()) {
            final DeleteObjectQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(arte, table);
                builder.buildDelete();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            qry = new DeleteObjectQuery(
                    arte,
                    table,
                    builder.getQuerySql(),
                    builder.getQueryParams());
            if (oldQry != null) {
                oldQry.deletedFromCache(true);
            }
            deletes.put(table.getId(), qry);
        }
        return qry;
    }

    public final DeleteGroupQuery buildDeleteGroupQuery(final EntityGroup<? extends Entity> group) {
        final String qryHashKey = getSelectHashKey(group);
        DeleteGroupQuery qry = deleteGrps.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final DeleteGroupQuery oldQry = qry;
            final GroupQuerySqlBuilder builder;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new GroupQuerySqlBuilder(group);
                builder.buildDeleteGroup();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            final String sql = builder.getQuerySql();
            arte.getTrace().put(EEventSeverity.DEBUG, "Delete group built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
            qry = new DeleteGroupQuery(
                    arte,
                    group.getDdsMeta(),
                    sql,
                    builder.getQueryParams());
            if (oldQry != null) {
                oldQry.deletedFromCache(true);
            }
            deleteGrps.put(qryHashKey, qry);
        }
        return qry;
    }

    public final UpdateGroupQuery buildUpdateGroupQuery(final EntityGroup<? extends Entity> group, final Set<Id> propIds) {
        final GroupQuerySqlBuilder builder;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        try {
            builder = new GroupQuerySqlBuilder(group);
            builder.buildUpdateGroup(propIds);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        }
        final String sql = builder.getQuerySql();
        arte.getTrace().put(EEventSeverity.DEBUG, "Update group built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
        final UpdateGroupQuery qry = new UpdateGroupQuery(
                arte,
                group.getDdsMeta(),
                sql,
                builder.getQueryParams());
        qry.deletedFromCache(false);
        return qry;
    }

    public final SelectQuery buildSelect(final EntityGroup<? extends Entity> group) {
        final String qryHashKey = getSelectHashKey(group);
        SelectQuery qry = selects.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final SelectQuery oldQry = qry;
            final GroupQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new GroupQuerySqlBuilder(group);
                bOk = builder.buildSelect(GroupQuerySqlBuilder.ESelectType.SELECTION_PAGE);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                final String sql = builder.getQuerySql();
                arte.getTrace().put(EEventSeverity.DEBUG, "Select built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                qry = new SelectQuery(
                        arte,
                        group.getDdsMeta(),
                        sql,
                        builder.getQueryFields(),
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                selects.put(qryHashKey, qry);
            } else {
                throw new DbQueryBuilderError("\"Select\" query requested but not built", null);
            }
        }
        return qry;
    }
    
    public final SelectQuery buildStatisticQuery(final EntityGroup<? extends Entity> group, final List<AggregateFunctionCall> functions){
        final GroupQuerySqlBuilder builder;
        final boolean bOk;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        try {
            builder = new GroupQuerySqlBuilder(group);
            builder.buildCalcStatistic(functions);                    
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        }
        final String sql = builder.getQuerySql();
        arte.getTrace().put(EEventSeverity.DEBUG, "Calc statistic built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
        final SelectQuery qry = new SelectQuery(
                arte,
                group.getDdsMeta(),
                sql,
                builder.getQueryFields(),
                builder.getQueryParams());
        return qry;
    }

    public PkSubQuery buildPkSelectQuery(final EntityGroup<? extends Entity> group, final boolean saveSelectionOrder) {
        final GroupQuerySqlBuilder builder;
        final boolean bOk;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        try {
            builder = new GroupQuerySqlBuilder(group);
            bOk = builder.buildSelect(saveSelectionOrder ? GroupQuerySqlBuilder.ESelectType.PRIMARY_KEY_IN_SELECTION_ORDER : GroupQuerySqlBuilder.ESelectType.PRIMARY_KEY);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
        }
        if (bOk) {
            final String sql = builder.getQuerySql();
            arte.getTrace().put(EEventSeverity.DEBUG, "Select primary key:\n" + sql, EEventSource.DB_QUERY_BUILDER);
            return new PkSubQuery(arte, sql, builder.getQueryParams(), group);
        }
        throw new DbQueryBuilderError("\"Select primary key\" query requested but not built", null);
    }

    public final SelectParentQuery buildSelectParent(final EntityGroup<? extends Entity> group) {
        final String qryHashKey = getSelectHashKey(group);
        SelectParentQuery qry = selectParents.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final SelectParentQuery oldQry = qry;
            final GroupQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new GroupQuerySqlBuilder(group);
                bOk = builder.buildSelect(GroupQuerySqlBuilder.ESelectType.SELECTION_PAGE);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                try {
                    final String sql = builder.getQuerySql();
                    arte.getTrace().put(EEventSeverity.DEBUG, "SelectParent built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                    qry = new SelectParentQuery(
                            arte,
                            group.getDdsMeta(),
                            sql,
                            prepareStatementWithProfiling(sql),
                            builder.getQueryFields(),
                            builder.getQueryParams());
                    if (oldQry != null) {
                        oldQry.deletedFromCache(true);
                    }
                    selectParents.put(qryHashKey, qry);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Wrong \"select\" query built: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else {
                throw new DbQueryBuilderError("\"SelectParent\" query requested but not built", null);
            }
        }
        return qry;
    }

    public final UpdateObjectQuery buildUpdate(final Entity entity, final SortedSet<Id> modifiedPropIds) {
        final String qryHashKey = getUpdateHashKey(entity, modifiedPropIds);
        UpdateObjectQuery qry = updates.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final UpdateObjectQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(entity);
                bOk = builder.buildUpdate(modifiedPropIds);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                final String sql = builder.getQuerySql();
                arte.getTrace().put(EEventSeverity.DEBUG, "Update built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                qry = new UpdateObjectQuery(
                        arte,
                        entity.getDdsMeta(),
                        sql,
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                updates.put(qryHashKey, qry);
            } else {
                return null;//throw new DbpDbQueryBuilderError("\"Update\" query requested but not built", null);
            }
        }
        qry.setExecuteBatch(updateBatchOptions.getBatchSize(entity.getEntityId()));
        return qry;
    }

    private String getUpdateHashKey(final Entity entity, final SortedSet<Id> modifiedPropIds) {
        //we do not sort modifiedPropIds because the same updates usualy prepared by the same algorithms
        //so modifications are done in the same order
        final StringBuilder s = new StringBuilder(modifiedPropIds.size() * 30 + 30);
        s.append(entity.getDdsMeta().getId().toString());
        for (Id id : modifiedPropIds) {
            s.append(SEP);
            s.append(id);
        }
        return s.toString();
    }

    private String getCreateHashKey(final Entity entity, final SortedSet<Id> modifiedPropIds, final boolean isCopied, final boolean isUpVal) {
        return getUpdateHashKey(entity, modifiedPropIds) + SEP + (isCopied ? "c" : "") + SEP + (isUpVal ? "u" : "");
    }

    public final CreateObjectQuery buildCreate(final Entity entity, final SortedSet<Id> modifiedPropIds, final Entity src, final boolean isUpVal) {
        final String qryHashKey = getCreateHashKey(entity, modifiedPropIds, src != null, isUpVal);
        CreateObjectQuery qry = creates.get(qryHashKey);
        if (qry == null || qry.getIsBusy()) {
            final CreateObjectQuery oldQry = qry;
            final ObjectQuerySqlBuilder builder;
            final boolean bOk;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            try {
                builder = new ObjectQuerySqlBuilder(entity);
                bOk = builder.buildInsert(modifiedPropIds, src, isUpVal);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
            }
            if (bOk) {
                final String sql = builder.getQuerySql();
                arte.getTrace().put(EEventSeverity.DEBUG, "Insert built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                qry = new CreateObjectQuery(
                        arte,
                        entity.getDdsMeta(),
                        sql,
                        builder.getQueryParams());
                if (oldQry != null) {
                    oldQry.deletedFromCache(true);
                }
                creates.put(qryHashKey, qry);
            } else {
                throw new DbQueryBuilderError("\"Insert\" query requested but not built", null);
            }
        }
        qry.setExecuteBatch(createBatchOptions.getBatchSize(entity.getEntityId()));
        return qry;
    }

    public final List<Object> getPkVals(final DdsTableDef table, final Map<Id, Object> knownPropValsByPropId) {
        NEXT_SK:
        for (DdsIndexDef idx : table.getIndices().get(EScope.ALL)) {
            if (idx.isSecondaryKey()) {
                final List<Object> keyPropVals = new ArrayList<Object>(3);
                for (DdsIndexDef.ColumnInfo c : idx.getColumnsInfo()) {
                    final Object val = knownPropValsByPropId.get(c.getColumnId());
                    if (val == null) // it is the key we are looking for
                    {
                        continue NEXT_SK;
                    }
                    keyPropVals.add(val);
                }
                return getPkValsBySK(table, idx, keyPropVals);
            }
        }
        throw new IllegalUsageError("No suitable secondary key was found");
    }

    private List<Object> getPkValsBySK(final DdsTableDef table, final DdsIndexDef sk, final List<Object> keyPropVals) {
        final String section = ETimingSection.RDX_ENTITY_DB_READ_PK.getValue() + ":" + table.getDbName();
        arte.getProfiler().enterTimingSection(section);
        try {
            final String hashKey = table.getId().toString() + SEP + sk.getId().toString();
            ObjectPkQuery qry = pkQueries.get(hashKey);
            if (qry == null || qry.getIsBusy()) {
                final ObjectPkQuery oldQry = qry;
                final ObjectQuerySqlBuilder builder;
                final boolean bOk;
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
                try {
                    builder = new ObjectQuerySqlBuilder(arte, table);
                    bOk = builder.buildPrimaryKeyRead(sk);
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_BUILDER_CONSTR);
                }
                if (bOk) {
                    final String sql = builder.getQuerySql();
                    arte.getTrace().put(EEventSeverity.DEBUG, "PrimaryKey read built:\n" + sql, EEventSource.DB_QUERY_BUILDER);
                    qry = new ObjectPkQuery(
                            arte,
                            table,
                            sql,
                            builder.getQueryFields(),
                            builder.getQueryParams());
                    if (oldQry != null) {
                        oldQry.deletedFromCache(true);
                    }
                    pkQueries.put(hashKey, qry);
                } else {
                    throw new DbQueryBuilderError("\"Primary key\" query requested but not built", null);
                }
            }
            try {
                return qry.getPrimaryKeyVals(sk, keyPropVals);
            } finally {
                qry.free();
            }
        } finally {
            arte.getProfiler().leaveTimingSection(section);
        }
    }

//Private    
    private static String getReadHashKey(final Entity entity, final List<Id> virtualColumns, final boolean forUpdate, final Long waitTime) {
        final Id classId = entity.getRadMeta() != null ? entity.getRadMeta().getId() : RadClassDef.getEntityClassIdByTableId(entity.getDdsMeta().getId());
        final StringBuilder virtualColumnsStr = new StringBuilder();
        if (virtualColumns != null) {
            for (Id id : virtualColumns) {
                virtualColumnsStr.append("#");
                virtualColumnsStr.append(String.valueOf(id));
            }
        }
        return classId + virtualColumnsStr.toString() + String.valueOf(forUpdate) + String.valueOf(waitTime);
    }

    private static String getLockObjectHashKey(final EEntityLockMode mode, final DdsTableDef table, final Long waitTime) {
        return table.getId() + mode.toString() + String.valueOf(waitTime);
    }
    private static final char SEP = '\00';

    private static String getSelectHashKey(final EntityGroup<? extends Entity> group) {
        final StringBuilder key = new StringBuilder(group.getPresentation().getId().toString());
        key.append(SEP);
        if (group.getContext() instanceof EntityGroup.TreeContext) {
            final RadExplorerItemDef ei = ((EntityGroup.TreeContext) group.getContext()).getExplorerItem();
            if (ei != null) {
                key.append(ei.getOwnerDefId());
                key.append('-');
                key.append(ei.getId().toString());
            }
        }
        key.append(SEP);
        final EntityGroup.PropContext propContext = (group.getContext() instanceof EntityGroup.PropContext) ? (EntityGroup.PropContext) group.getContext() : null;
        if (propContext != null) {

            // parent select condition and other options of parent selection can be overrided in
            // a derived class so we should take into account not only the prop id but its owner id too
            key.append(propContext.getPropOwner().getRadMeta().getId().toString());
            key.append('-');
            key.append(propContext.getParentRefPresPropId());
        }
        key.append(SEP);
        if (propContext != null) {
            for (DdsReferenceDef.ColumnsInfoItem fixedParentRefProp : propContext.getFixedParentRefProps()) {
                key.append(fixedParentRefProp.getParentColumnId());
            }
        }
        key.append(SEP);
        for (RadSortingDef.Item srtItem : group.getOrderBy()) {
            key.append(srtItem.getOrder().getValue());
            key.append(srtItem.getColumnId());
        }
        if (group.getPaginationMethod() == EPaginationMethod.RELATIVE) {
            key.append(EPaginationMethod.RELATIVE.getValue());
            key.append(group.getPreviousEntityPid() == null ? "0" : "1");
        }
        key.append(SEP);
        final Sqml hint = group.getHint();
        if (hint != null) {
            key.append(hint);
        }
        key.append(SEP);
        final Sqml additionalCond = group.getAdditionalCond();
        if (additionalCond != null) {
            final org.radixware.schemas.xscml.Sqml xSqml
                    = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            additionalCond.appendTo(xSqml);
            key.append(xSqml.xmlText());
        }
        final Sqml additionalFrom = group.getAdditionalFrom();
        if (additionalFrom !=null ){
            final org.radixware.schemas.xscml.Sqml xSqml
                    = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            additionalFrom.appendTo(xSqml);
            key.append(xSqml.xmlText()); 
        }
        key.append(SEP);
        final Map<Id, Object> parameters = group.getFltParamValsById();
        if (parameters != null && !parameters.isEmpty()) {

            boolean firstParameter = true;
            for (Map.Entry<Id, Object> parameter : parameters.entrySet()) {
                if (parameter.getValue() instanceof Arr) {
                    if (firstParameter) {
                        firstParameter = false;
                    } else {
                        key.append(';');
                    }
                    key.append(parameter.getKey().toString());
                    key.append('[');
                    key.append(String.valueOf(((Arr) parameter.getValue()).size()));
                    key.append(']');
                }
            }

            final Collection<IfParamTag> paramsForPreprocessorInCond = SqmlPreprocessor.getIfParamTags(additionalCond);
            final Collection<IfParamTag> paramsForPreprocessorInFrom = SqmlPreprocessor.getIfParamTags(additionalFrom);
            final Collection<IfParamTag> paramsForPreprocessorInHint = SqmlPreprocessor.getIfParamTags(group.getHint());
            final Collection<IfParamTag> allParamsForPreprocessor = new LinkedList<>();
            if (paramsForPreprocessorInCond != null) {
                allParamsForPreprocessor.addAll(paramsForPreprocessorInCond);
            }
            if (paramsForPreprocessorInHint != null) {
                allParamsForPreprocessor.addAll(paramsForPreprocessorInHint);
            }
            if (paramsForPreprocessorInFrom!=null){
                allParamsForPreprocessor.addAll(paramsForPreprocessorInFrom);
            }
            if (!allParamsForPreprocessor.isEmpty()) {
                key.append(SEP);
                for (IfParamTag ifParam : allParamsForPreprocessor) {
                    final Id paramId = ifParam.getParameterId();
                    if (!group.getFltParamValsById().containsKey(paramId)) {
                        throw new IllegalStateException("Parameter #" + paramId + " used in preprocessor is undefined");
                    }
                    key.append(paramId);
                    final Object paramValue = group.getFltParamValsById().get(paramId);
                    if (paramValue instanceof Arr) {
                        if (ifParam.getOperator()==EIfParamTagOperator.EQUAL ||
                            ifParam.getOperator()==EIfParamTagOperator.NOT_EQUAL
                           ){
                            key.append("=").append(paramValue.toString());
                        }else{
                            key.append("[").append(((Arr) paramValue).size()).append("]");
                        }
                    } else if (paramValue instanceof Pid) {
                        key.append(paramValue.toString());
                    } else if (paramValue instanceof Entity){
                        key.append(((Entity)paramValue).getPid().toString());
                    } else {
                        if (paramValue == null) {
                            key.append("null");
                        } else {
                            final String paramValueStr = SqmlPreprocessor.scalarParamValueAsStr(paramValue);
                            if (paramValueStr == null) {
                                throw new IllegalStateException("Unable to translate param #" + paramId + " used in preprocessor to string for query cache key");
                            }
                            key.append(paramValueStr.replaceAll(";", "\\;"));
                        }
                    }
                    key.append(";");
                }
            }
        }
        key.append(SEP);
        return key.toString();
    }

    public static String translateSqmlExpression(final Arte arte, final DdsTableDef table, final String tableAlias, final Sqml sqml) {
        return SqlBuilder.translateSqmlExpression(arte, table, tableAlias, sqml);
    }

    public static SqmlTranslateResult translateSqml(final Arte arte, final DdsTableDef table, final String tableAlias, final Sqml sqml) {
        final ObjectQuerySqlBuilder constr = new ObjectQuerySqlBuilder(arte, table, tableAlias);
        final CharSequence res = constr.translateSqml(sqml);
        return new SqmlTranslateResult(res == null ? null : res.toString(), null, tableAlias, constr.getQueryParams());
    }
//User Property
    private final Map<String, PreparedStatement> upStmtsHash = new HashMap<>(); //concurrent hashmap because is used in finalize
    private final List<PreparedStatement> upExecStmts; //vector because is used in finalize

    public PreparedStatement getUpStmtFromHash(final String key, final String query) throws SQLException {
        PreparedStatement stmt = upStmtsHash.get(key);
        if (stmt == null) {
            stmt = prepareStatementWithProfiling(query);
            upStmtsHash.put(key, stmt);
        } else {
            if (upExecStmts.indexOf(stmt) >= 0) {
                // этот stmt уже выполняется в данный момент, нужно создать новый
                stmt = prepareStatementWithProfiling(query);
            }
        }
        return stmt;
    }

    public ResultSet execUpStmt(final PreparedStatement stmt, final boolean asUpdate) throws SQLException {
        ResultSet result = null;
        int index = 0;
        try {
            // регистрируем stmt чтобы избежать попытки повторного выполнения уже выполняющейся stmt ( падает Oracle )
            upExecStmts.add(stmt);
            index = upExecStmts.size();
            if (asUpdate) {
                stmt.executeUpdate();
            } else {
                result = stmt.executeQuery();
            }
        } finally {
            upExecStmts.remove(index - 1);
        }
        return result;
    }
    //Sequences
    private final Map<DdsSequenceDef, PreparedStatement> seqStmtsBySeq = new HashMap<>();

    public long getSeqNextVal(final DdsSequenceDef seq) {
        PreparedStatement qry = seqStmtsBySeq.get(seq);
        try {
            if (qry == null) {
                qry = prepareStatementWithProfiling("select " + seq.getDbName() + ".NextVal from dual");
                seqStmtsBySeq.put(seq, qry);
            }
            final ResultSet rs = qry.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    try {
                        qry.close();
                    } catch (SQLException e) {
                        //do nothing
                    }
                    seqStmtsBySeq.remove(seq);
                    throw new DatabaseError("Can't get sequence \"" + seq.getName() + "\" next value", null);
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            if (qry != null) {
                try {
                    qry.close();
                } catch (SQLException e2) {
                    //do nothing
                }
            }
            seqStmtsBySeq.remove(seq);
            throw new DatabaseError("Can't get sequence \"" + seq.getName() + "\" next value: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
    private volatile PreparedStatement qryUf = null; // volatile because it is read in finalize
    private volatile PreparedStatement qryUr = null;
    private volatile PreparedStatement qryUrId = null;

    public void close() {
        if (qryUf != null) {
            try {
                qryUf.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }
        for (PreparedStatement st : seqStmtsBySeq.values()) {
            try {
                st.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }
        seqStmtsBySeq.clear();
        for (PreparedStatement st : upStmtsHash.values()) {
            try {
                st.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }
        upStmtsHash.clear();
        upExecStmts.clear();

        clearCache(deleteGrps);
        clearCache(deletes);
        clearCache(existsByTabId);
        clearCache(getClassGuidsByTabId);
        clearCache(pkQueries);
        clearCache(updates);
        clearCache(creates);
        clearCache(reads);
        clearCache(selects);
        clearCache(selectParents);
        clearCache(tranLocks);
        clearCache(upObjectOwnersByTabId);
        clearCache(recordReads);

        clearBatchOptions();
    }

    private static void clearCache(final SoftCache<? extends Object, ? extends DbQuery> cache) {
        for (DbQuery q : cache.values()) {
            q.deletedFromCache(false);
            q.free();
        }
        cache.clear();
    }

//batches
    public void sendBatches(final boolean bThrowExceptionOnDbErr) {
        for (UpdateObjectQuery qry : updates.values()) {
            qry.sendBatch(bThrowExceptionOnDbErr);
        }
        for (CreateObjectQuery qry : creates.values()) {
            qry.sendBatch(bThrowExceptionOnDbErr);
        }
    }
    private final QueryBatchOptions updateBatchOptions = new QueryBatchOptions();
    private final QueryBatchOptions createBatchOptions = new QueryBatchOptions();

    public void clearBatchOptions() {
        updateBatchOptions.clear();
        createBatchOptions.clear();
    }

    protected void ensureValidId(final Id tableId) {
        if (tableId == null || tableId.getPrefix() != EDefinitionIdPrefix.DDS_TABLE) {
            throw new IllegalArgumentException("Expected " + EDefinitionIdPrefix.DDS_TABLE.getValue() + "..., got " + tableId);
        }
    }

    public void setUpdatesBatchSize(final Id tableId, final int size) {
        ensureValidId(tableId);
        updateBatchOptions.setBatchSize(tableId, size);
    }

    public void setCreatesBatchSize(final Id tableId, final int size) {
        ensureValidId(tableId);
        createBatchOptions.setBatchSize(tableId, size);
    }

    public final Map<String, byte[]> loadUserFuncJarByClassId(final Id id) {
        try {
            if (qryUf == null) {
                qryUf = prepareStatementWithProfiling("select JavaRuntime from rdx_userfunc where userClassGuid=?");
                qryUf.setFetchSize(1);
            }
            qryUf.setString(1, id.toString());
            final ResultSet rs = qryUf.executeQuery();
            try {
                if (!rs.next()) {
                    throw new DefinitionNotFoundError(id);
                }
                final InputStream s = rs.getBinaryStream("JavaRuntime");
                if (s == null) {
                    throw new DefinitionNotFoundError(id);
                }
                try {
                    final Map<String, byte[]> jarEntryDataByName = new HashMap<String, byte[]>();
                    loadJar2Map(s, jarEntryDataByName);
                    return Collections.unmodifiableMap(jarEntryDataByName);
                } catch (IOException ex) {
                    throw new DefinitionNotFoundError(id, ex);
                } finally {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        //do nothing
                    }
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't load UserFunc class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    public static class UserReportInfo {

        public final Map<String, byte[]> data;
        public final String execClassId;

        public UserReportInfo(Map<String, byte[]> data, String execClassId) {
            this.data = data;
            this.execClassId = execClassId;
        }
    }

    public final Map<String, byte[]> loadUserReportJarByClassId(final Id id) {
        try {
            if (qryUr == null) {
                qryUr = prepareStatementWithProfiling("select V.ReportBinary from rdx_userreportversion V inner join rdx_userreport R on r.guid=v.reportguId and v.version=r.curVersion where reportGuid=?");
                qryUr.setFetchSize(1);
            }
            qryUr.setString(1, id.toString());
            final ResultSet rs = qryUr.executeQuery();
            try {
                if (!rs.next()) {
                    throw new DefinitionNotFoundError(id);
                }
                final InputStream s = rs.getBinaryStream("ReportBinary");
                if (s == null) {
                    throw new DefinitionNotFoundError(id);
                }
                try {
                    final Map<String, byte[]> jarEntryDataByName = new HashMap<String, byte[]>();
                    loadJar2Map(s, jarEntryDataByName);
                    return jarEntryDataByName;
                } catch (IOException ex) {
                    throw new DefinitionNotFoundError(id, ex);
                } finally {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        //do nothing
                    }
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't load UserReport class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    public final DefManager.ImageData loadUserReportImageById(final Id id, Id imageId) {
        try {
            if (qryUr == null) {
                qryUr = prepareStatementWithProfiling("select V.ReportBinary from rdx_userreportversion V inner join rdx_userreport R on r.guid=v.reportguId and v.version=r.curVersion where reportGuid=?");
                qryUr.setFetchSize(1);
            }
            qryUr.setString(1, id.toString());
            final ResultSet rs = qryUr.executeQuery();
            try {
                if (!rs.next()) {
                    throw new DefinitionNotFoundError(id);
                }
                final InputStream s = rs.getBinaryStream("ReportBinary");
                if (s == null) {
                    throw new DefinitionNotFoundError(id);
                }
                try {
                    return loadImageJarEntryByPrefix(s, imageId.toString());
                } catch (IOException ex) {
                    throw new DefinitionNotFoundError(id, ex);
                } finally {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        //do nothing
                    }
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't load UserReport class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    private DefManager.ImageData loadImageJarEntryByPrefix(final InputStream stream, String prefix) throws IOException {
        JarInputStream jar = null;
        try {
            jar = new JarInputStream(stream);
            JarEntry e = jar.getNextJarEntry();
            while (e != null) {
                final String name = e.getName();
                if (name.startsWith(prefix)) {
                    String mimeType = "";
                    int dotIndex = name.lastIndexOf(".");
                    if (dotIndex > 0) {
                        String ext = name.substring(dotIndex + 1);
                        mimeType = "image/" + ext;
                        if ("svg".equals(ext.toLowerCase())) {
                            mimeType += "+xml";
                        }
                    }
                    return new DefManager.ImageData(mimeType, readJarEntryData(jar));
                }
                e = jar.getNextJarEntry();
            }
        } finally {
            if (jar != null) {
                jar.close();
            }
        }
        return null;
    }

    public final Id getUserReportClassGuid(final Id reportId) {
        try {
            if (qryUrId == null) {
                qryUrId = prepareStatementWithProfiling("select V.ReportClassGUID from rdx_userreportversion V inner join rdx_userreport R on r.guid=v.reportguId and v.version=r.curVersion where reportGuid=?");
                qryUrId.setFetchSize(1);
            }
            qryUrId.setString(1, reportId.toString());
            final ResultSet rs = qryUrId.executeQuery();
            try {
                if (!rs.next()) {
                    throw new DefinitionNotFoundError(reportId);
                }
                final String id = rs.getString("ReportClassGUID");
                if (id == null) {
                    throw new DefinitionNotFoundError(reportId);
                }
                return Id.Factory.loadFrom(id);
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't load UserReport class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    private void loadJar2Map(final InputStream stream, final Map<String, byte[]> jarEntryDataByName) throws IOException {
        JarInputStream jar = null;
        try {
            jar = new JarInputStream(stream);
            JarEntry e = jar.getNextJarEntry();
            while (e != null) {
                final String name = e.getName();
                if (name.contains("/explorer/") || name.contains("/web/") || name.contains("/common_client/")) {
                    e = jar.getNextJarEntry();
                    continue;
                }
                if (!e.isDirectory()) {
                    jarEntryDataByName.put(e.getName(), readJarEntryData(jar));
                }
                e = jar.getNextJarEntry();
            }
        } finally {
            if (jar != null) {
                jar.close();
            }
        }
    }
    private static final int READ_BLOCK_SIZE = 4 * 1024;

    private byte[] readJarEntryData(final JarInputStream jar) throws IOException {
        final ByteArrayOutputStream bstream = new ByteArrayOutputStream(32 * READ_BLOCK_SIZE);
        final byte[] block = new byte[READ_BLOCK_SIZE];
        int blockSize;
        for (;;) {
            blockSize = jar.read(block);
            if (blockSize == -1) {
                break;
            }
            if (blockSize != 0) {
                bstream.write(block, 0, blockSize);
            }
        }
        return bstream.toByteArray();
    }
    private volatile PreparedStatement qryListAppRoles = null;
    private volatile PreparedStatement qryReadAppRoleClass = null;
    private volatile PreparedStatement qryCheckAppRoleDef = null;

    public void readAppRoles(Map<Id, Timestamp> roleData) {
        try {
            if (qryListAppRoles == null) {
                qryListAppRoles = prepareStatementWithProfiling("select guid, lastupdatetime from rdx_ac_approle");
            }

            final ResultSet rs = qryListAppRoles.executeQuery();
            try {
                while (rs.next()) {
                    roleData.put(Id.Factory.loadFrom(rs.getString(1)), rs.getTimestamp(2));
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't read application role list: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    public static class AppRoleClassInfo {

        public final String classId;
        public Map<String, byte[]> data;

        public AppRoleClassInfo(String classId, Map<String, byte[]> data) {
            this.classId = classId;
            this.data = data;
        }
    }

    public static class AppRoleInfo {

        public final String name;
        public final String description;
        public final Timestamp lastModified;
        public final boolean runtimeDefined;

        public AppRoleInfo(String name, String description, Timestamp lastModified, boolean runtimeDefined) {
            this.name = name == null ? "" : name;
            this.description = description == null ? "" : description;
            this.lastModified = lastModified;
            this.runtimeDefined = runtimeDefined;
        }
    }

    public final AppRoleInfo checkAppRoleStatus(final Id id) {
        try {
            if (qryCheckAppRoleDef == null) {
                qryCheckAppRoleDef = prepareStatementWithProfiling("select title, description,lastUpdateTime,runtime from rdx_ac_approle where guid=?");
                qryCheckAppRoleDef.setFetchSize(1);
            }
            qryCheckAppRoleDef.setString(1, id.toString());
            final ResultSet rs = qryCheckAppRoleDef.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                rs.getObject(4);
                boolean runtimeDefined = !rs.wasNull();
                return new AppRoleInfo(rs.getString(1), rs.getString(2), rs.getTimestamp(3), runtimeDefined);
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't check Application Role class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    public final AppRoleClassInfo loadAppRoleJarByClassId(final Id id) {
        try {
            if (qryReadAppRoleClass == null) {
                qryReadAppRoleClass = prepareStatementWithProfiling("select runtime, roleClassGuid from rdx_ac_approle where guid=?");
                qryReadAppRoleClass.setFetchSize(1);
            }
            qryReadAppRoleClass.setString(1, id.toString());
            final ResultSet rs = qryReadAppRoleClass.executeQuery();
            try {
                if (!rs.next()) {
                    throw new DefinitionNotFoundError(id);
                }
                final InputStream s = rs.getBinaryStream(1);
                if (s == null) {
                    throw new DefinitionNotFoundError(id);
                }
                try {
                    final Map<String, byte[]> jarEntryDataByName = new HashMap<String, byte[]>();
                    loadJar2Map(s, jarEntryDataByName);
                    return new AppRoleClassInfo(rs.getString(2), jarEntryDataByName);
                } catch (IOException ex) {
                    throw new DefinitionNotFoundError(id, ex);
                } finally {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        //do nothing
                    }
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Can't load Application Role class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }
}
