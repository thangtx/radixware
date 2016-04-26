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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.dbq.DbQuery.InputChildPropParam;
import org.radixware.kernel.server.dbq.DbQuery.InputGroupPropParam;
import org.radixware.kernel.server.dbq.DbQuery.InputParentPropParam;
import org.radixware.kernel.server.dbq.DbQuery.InputTypifiedValParam;
import org.radixware.kernel.server.dbq.DbQuery.Param;
import org.radixware.kernel.server.dbq.GroupQuery.FilterParam;
import org.radixware.kernel.server.dbq.GroupQuery.InputRequestedRoleIdsParam;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityGroup.TreeContext;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.SubQuery;

public final class PkSubQuery extends SubQuery {

    private final List<Param> queryParams;
    private final EntityGroup group;

    public PkSubQuery(final Arte arte, final String sql, final List<Param> queryParams, final EntityGroup group) {
        super(arte, sql, queryParams.size());
        this.queryParams = queryParams;
        this.group = group;
    }

    public PkSubQuery(final Arte arte, final DdsReferenceDef detRef, final PkSubQuery masterPkQry) {
        super(arte, getDetailPkQrySql(arte, detRef, masterPkQry.getSql()), masterPkQry.queryParams.size());
        this.queryParams = masterPkQry.queryParams;
        this.group = masterPkQry.group;
    }

    private static final String getDetailPkQrySql(final Arte arte, final DdsReferenceDef detRef, final String masterPkSql) {
        final StringBuilder detPkSql = new StringBuilder("select ");
        boolean isFirst = true;
        //final RadixObject ddsLoadContext = arte.getDefManager().getRelease().getDdsLoadContext();
        final DdsTableDef detRefChildTable = arte.getDefManager().getTableDef(detRef.getChildTableId());
        for (DdsIndexDef.ColumnInfo detPkProp : detRefChildTable.getPrimaryKey().getColumnsInfo()) {
            if (isFirst) {
                isFirst = false;
            } else {
                detPkSql.append(',');
            }
            detPkSql.append(detPkProp.getColumn().getDbName());
        }
        detPkSql.append(" from ");
        detPkSql.append(detRefChildTable.getDbName());
        detPkSql.append(" where (");
        final DdsTableDef detRefParentTable = arte.getDefManager().getTableDef(detRef.getParentTableId());
        if (DefManager.isRefByPrimaryKey(detRef)) {
            isFirst = true;
            for (DdsIndexDef.ColumnInfo masterPkProp : detRefParentTable.getPrimaryKey().getColumnsInfo()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    detPkSql.append(',');
                }
                for (DdsReferenceDef.ColumnsInfoItem refProp : detRef.getColumnsInfo()) {
                    if (refProp.getParentColumnId().equals(masterPkProp.getColumnId())) {
                        detPkSql.append(refProp.getChildColumn().getDbName());
                        break;
                    }
                }
            }
            detPkSql.append(") in (");
            detPkSql.append(masterPkSql);
            detPkSql.append(')');
        } else {
            final StringBuilder masterSecKey = new StringBuilder();

            for (DdsReferenceDef.ColumnsInfoItem refProp : detRef.getColumnsInfo()) {
                if (masterSecKey.length() != 0) {
                    detPkSql.append(',');
                    masterSecKey.append(',');
                }
                detPkSql.append(refProp.getChildColumn().getDbName());
                masterSecKey.append(refProp.getParentColumn().getDbName());
            }
            detPkSql.append(") in (select ");
            detPkSql.append(masterSecKey);
            detPkSql.append(" from  ");
            detPkSql.append(detRefParentTable.getDbName());
            detPkSql.append(" where (");
            isFirst = true;
            for (DdsIndexDef.ColumnInfo masterPkProp : detRefParentTable.getPrimaryKey().getColumnsInfo()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    detPkSql.append(',');
                }
                detPkSql.append(masterPkProp.getColumn().getDbName());
            }
            detPkSql.append(") in (");
            detPkSql.append(masterPkSql);
            detPkSql.append("))");
        }
        return detPkSql.toString();
    }

    @Override
    public void setParams(final PreparedStatement stmt, final int startIdx) {
        int i = startIdx;
        for (Param param : queryParams) {
            if (param instanceof InputChildPropParam) {
                if (!(group.getContext() instanceof EntityGroup.PropContext)) {
                    throw new WrongFormatError("Can't translate \"ChildColumn\" tag cause: wrong context");
                }
                try {
                    final Id propId = ((InputChildPropParam) param).propId;
                    final IRadClassInstance child = ((EntityGroup.PropContext) group.getContext()).getPropOwner();
                    final RadPropDef childProp = child.getRadMeta().getPropById(propId);
                    DbQuery.setParam(getArte(), stmt, i, childProp.getValType(), childProp.getDbType(), child.getProp(propId), childProp.getName());
                } catch (RuntimeException e) {// ChildProp может прийти только из Sqml-я
                    throw new WrongFormatError("Can't translate \"ChildColumn\" tag cause:" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            } else if (param instanceof InputParentPropParam) {
                final Pid parentPid;
                if (!(group.getContext() instanceof TreeContext)
                        || (parentPid = ((TreeContext) group.getContext()).getParentPid()) == null) {
                    throw new WrongFormatError("Can't translate \"ParentColumn\" tag cause context not specified", null);
                }
                try {
                    final Entity parent = getArte().getEntityObject(parentPid);
                    final Id propId = ((InputParentPropParam) param).propId;
                    final RadPropDef parentProp = parent.getRadMeta().getPropById(propId);
                    DbQuery.setParam(getArte(), stmt, i, parentProp.getValType(), parentProp.getDbType(), parent.getProp(propId), parentProp.getName());
                } catch (RuntimeException e) {
                    throw new WrongFormatError("Can't translate \"ParentColumn\" cause:" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            } else if (param instanceof InputGroupPropParam) {
                try {
                    final Id propId = ((InputGroupPropParam) param).propId;
                    final RadPropDef prop = group.getRadMeta().getPropById(propId);
                    final Object val = group.getProp(propId);
                    DbQuery.setParam(getArte(), stmt, i, prop.getValType(), prop.getDbType(), val, prop.getName());
                } catch (RuntimeException e) {
                    throw new IllegalUsageError("Can't translate \"GroupParameter\":" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            } else if (param instanceof FilterParam) {
                try {
                    GroupQuery.setFilterParam(getArte(), stmt, i, (FilterParam) param, group.getFltParamValsById());
                } catch (FilterParamNotDefinedException e) {
                    throw new DbQueryBuilderError("Invalid primary key select: filter parameter is requered but not defined", e);
                }
            } else if (param instanceof InputRequestedRoleIdsParam) {
                try {
                    stmt.setString(i, group.getRequestedRoleIds());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputRequestedRoleIdsParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputTypifiedValParam) {
                final InputTypifiedValParam p = (InputTypifiedValParam) param;
                DbQuery.setParam(getArte(), stmt, i, p.valueType, p.valueType.getDefaultDbType(), p.value, null);
            } else {
                throw new DbQueryBuilderError("Invalid primary key select: unsupported parameter type \"" + param.getClass().getName() + "\"", null);
            }
            i++;
        }
    }
}
