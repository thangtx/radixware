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

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.WrongSecondaryKeyError;
import org.radixware.kernel.server.meta.clazzes.RadClassApJoins;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.clazzes.RadObjectUpValueRef;
import org.radixware.kernel.server.types.Entity;

public final class ObjectQuerySqlBuilder extends QuerySqlBuilder {

    private static final String SELECT_ = "select ";
    private static final String _FROM_ = " from ";
//Constructor

    public ObjectQuerySqlBuilder(
            final Entity entity) {
        super(entity, EQueryContextType.OTHER, EQueryBuilderAliasPolicy.USE_ALIASES);
    }

    public ObjectQuerySqlBuilder(
            final Arte arte,
            final DdsTableDef table) {
        super(arte, table, EQueryContextType.OTHER, EQueryBuilderAliasPolicy.USE_ALIASES);
    }

    public ObjectQuerySqlBuilder(
            final Arte arte,
            final DdsTableDef table,
            final String alias) {
        super(arte, table, alias, EQueryContextType.OTHER);
    }

    @Override
    public final RadClassDef getEntityClass() {
        if (entity != null) {
            return entity.getRadMeta();
        } else {
            if (entityClass == null) {
                entityClass = getArte().getDefManager().getClassDef(RadClassDef.getEntityClassIdByTableId(getTable().getId()));
            }
            return entityClass;
        }
    }
    private RadClassDef entityClass = null;

    private final boolean appendWhere(final boolean bUseAlias) {
        if (getTable().getPrimaryKey().getColumnsInfo().isEmpty()) {
            return false;
        }

        querySql.append(" where ");

        boolean isFirstPkProp = true;
        for (DdsIndexDef.ColumnInfo pkProp : getTable().getPrimaryKey().getColumnsInfo()) {
            if (isFirstPkProp) {
                isFirstPkProp = false;
            } else {
                querySql.append(" and ");
            }
            if (bUseAlias) {
                querySql.append(getAlias());
                querySql.append('.');
            }
            querySql.append(pkProp.getColumn().getDbName());
            querySql.append("=?");
            addParameter(new DbQuery.InputThisPropParam(pkProp.getColumnId()));
        }
        return true;
    }

    /**
     *
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     */
    private void appendForUpdate(final Long waitTimeSec, final boolean useAlias) {
        querySql.append(" for update");

        if (!getTable().getPrimaryKey().getColumnsInfo().isEmpty()) {
            querySql.append(" of ");
            if (useAlias) {
                querySql.append(getAlias());
                querySql.append(".");
            }
            querySql.append(getTable().getPrimaryKey().getColumnsInfo().get(0).getColumn().getDbName());
        }

        if (waitTimeSec != null) {
            if (waitTimeSec.longValue() == 0) {
                querySql.append(" nowait");
            } else {
                querySql.append(" wait ");
                querySql.append(waitTimeSec.toString());
            }
        } else {
            //by default select for update waits forever  
        }
    }

    /**
     *
     * @param bReadRights
     * @param forUpdate
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     * @return
     */
    private boolean buildReadSql(final List<Id> virtualColumns, final boolean forUpdate, final Long waitTimeSec) {
        RadClassApJoins apJoins = null;
        final boolean bReadRights = virtualColumns == null ? false : virtualColumns.contains(ALL_ROLES_FIELD_COL_ID);
        final boolean bReadAcsCoordinates = virtualColumns == null ? false : virtualColumns.contains(ACS_COORDINATES_AS_STR_COL_ID);
        if ((bReadRights || bReadAcsCoordinates) && getEntityClass().hasPartitionRights()) {
            apJoins = getEntityClass().getApJoins();
        }
        if (fieldsByPropId.isEmpty() && apJoins == null) {
            return false;
        }
        querySql = new StringBuilder(SELECT_);
        boolean hasFields = appendFieldsStr();
        if (!hasFields && apJoins == null) {
            return false;
        }

        if (bReadRights) {
            if (hasFields) {
                querySql.append(',');
            }
            querySql.append("RDX_ACS.getCurUserAllRolesForObject(");
            if (apJoins != null) {
                querySql.append(apJoins.getAreaListSql());
            } else {
                querySql.append("null");
            }
            querySql.append(") ");
            querySql.append(ALL_ROLES_FIELD_ALIAS);
            hasFields = true;
        }

        if (bReadAcsCoordinates) {
            if (hasFields) {
                querySql.append(", ");
            }
            querySql.append("RDX_ACS.areaListToStr(");
            if (apJoins != null) {
                querySql.append(apJoins.getAreaListSql());
            } else {
                querySql.append("null");
            }
            querySql.append(") ");
            querySql.append(ACS_COORDINATES_AS_STR_FIELD_ALIAS);
        }


        querySql.append(_FROM_);
        //????? ????????? ??????????? ??????????, ?????? ????? join-? ???? ???????? ??? ????? ?????
        //final StringBuffer hint =  apJoins == null || apJoins.joinsHintSql == null ? new StringBuffer() : new StringBuffer(apJoins.joinsHintSql); 
        appendTablesStr(/*hint*/);
        //if (hint.length() > 0)
        //	querySql.replace(6, 7, " /*+ " + hint + " */ " ); 

        if (apJoins != null && apJoins.getJoinsSql() != null) {
            querySql.append(apJoins.getJoinsSql());
        }

        appendWhere(true);

        if (forUpdate) {
            appendForUpdate(waitTimeSec, true);
        }

        return true;
    }

    public final String buildReadRightsSql(final String username, final Long waitTimeSec) {
        RadClassApJoins apJoins = null;
        if (getEntityClass().hasPartitionRights()) {
            apJoins = getEntityClass().getApJoins();
        }
        if (fieldsByPropId.isEmpty() && apJoins == null) {
            return "";
        }
        querySql = new StringBuilder(SELECT_);
        final boolean hasFields = appendFieldsStr();
        if (!hasFields && apJoins == null) {
            return "";
        }

        //if (bReadRights) {
        if (hasFields) {
            querySql.append(',');
        }
        querySql.append("RDX_ACS.getAllRolesForObject(");
        querySql.append("\'" + username + "\',");
        if (apJoins != null) {
            querySql.append(apJoins.getAreaListSql());
        } else {
            querySql.append("null");
        }
        querySql.append(") ");
        querySql.append(ALL_ROLES_FIELD_ALIAS);
        //}

        querySql.append(_FROM_);
        //????? ????????? ??????????? ??????????, ?????? ????? join-? ???? ???????? ??? ????? ?????
        //final StringBuffer hint =  apJoins == null || apJoins.joinsHintSql == null ? new StringBuffer() : new StringBuffer(apJoins.joinsHintSql); 
        appendTablesStr(/*hint*/);
        //if (hint.length() > 0)
        //	querySql.replace(6, 7, " /*+ " + hint + " */ " ); 

        if (apJoins != null && apJoins.getJoinsSql() != null) {
            querySql.append(apJoins.getJoinsSql());
        }

        appendWhere(true);
        System.out.println("Query: " + querySql.toString());
        return querySql.toString();
    }

    /**
     *
     * @param readMask
     * @param forUpdate
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     * @return
     */
    public final boolean buildRead(final List<Id> virtualColumns, final boolean forUpdate, final Long waitTimeSec) {
        for (RadPropDef prop : getEntityClass().getProps()) {
            if (prop.isVisibleForArte()
                    && prop.getValType() != EValType.NATIVE_DB_TYPE
                    && (prop instanceof RadInnatePropDef || prop instanceof RadSqmlPropDef)) {
                addQueryProp(prop.getId(), EPropUsageType.FIELD);
            }
        }
        return buildReadSql(virtualColumns, forUpdate, waitTimeSec);
    }
//Object class GUID query 

    public final boolean buildClassGuidRead() {
        if (getTable().findClassGuidColumn() == null) {
            return false;
        }
        querySql = new StringBuilder(SELECT_);
        final DdsColumnDef classGuidColumn = getTable().getClassGuidColumn();
        querySql.append(classGuidColumn.getDbName());
        querySql.append(_FROM_);
        querySql.append(getTable().getDbName());
        appendWhere(false);
        fieldsByPropId.put(classGuidColumn.getId().toString(), new Field(this, classGuidColumn, classGuidColumn.getDbName()));
        return true;
    }

    public final boolean buildUpObjectOwnerRead() {
        if (!getTable().getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT)) {
            return false;
        }
        querySql = new StringBuilder(SELECT_);
        querySql.append(RadObjectUpValueRef.DbNotation.VAL_OWNER_ENTITY_ID_DB_COL_NAME);
        querySql.append(',');
        querySql.append(RadObjectUpValueRef.DbNotation.VAL_OWNER_PID_DB_COL_NAME);
        querySql.append(',');
        querySql.append(RadObjectUpValueRef.DbNotation.PROP_DEF_ID_DB_COL_NAME);
        querySql.append(_FROM_);
        querySql.append(getTable().getDbName());
        appendWhere(false);
        //There is no need to fill fieldsByPropId because the UpObjectOwnerQuery doesn't use it.
        //Skip the fieldsByPropId filling for optimization because finding property by DB name is slow.
        //fieldsByPropId.put(propId1, new Field(this, table.getPropById(propId1), VAL_OWNER_ENTITY_ID_DB_COL_NAME));
        //fieldsByPropId.put(propId2, new Field(this, table.getPropById(propId2), VAL_OWNER_PID_DB_COL_NAME));
        return true;
    }

//Object exists query  
    public final boolean buildExistsQry() {
        querySql = new StringBuilder("select 1 from ");
        querySql.append(getTable().getDbName());
        appendWhere(false);
        return true;
    }

    /**
     *
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     * @return
     */
//  Lock object query
    public final boolean buildTranLockObjectQry(final Long waitTimeSec) {
        querySql = new StringBuilder("select 1 from ");
        querySql.append(getTable().getDbName());
        appendWhere(false);
        appendForUpdate(waitTimeSec, false);
        return true;
    }

//Delete object query 
    public final void buildDelete() {
        querySql = new StringBuilder("delete from ");
        querySql.append(getTable().getDbName());
        appendWhere(false);
    }
//Object primary  key query    

    public final boolean buildPrimaryKeyRead(final DdsIndexDef key) {
        if (getTable().getPrimaryKey().getColumnsInfo().isEmpty() || key.getColumnsInfo().isEmpty()) {
            return false;
        }
        querySql = new StringBuilder(SELECT_);
        boolean isFirstPkProp = true;
        int propIdx = 1;
        for (DdsIndexDef.ColumnInfo pkProp : getTable().getPrimaryKey().getColumnsInfo()) {
            if (isFirstPkProp) {
                isFirstPkProp = false;
            } else {
                querySql.append(',');
            }
            querySql.append(pkProp.getColumn().getDbName());
            fieldsByPropId.put(pkProp.getColumnId().toString(), new Field(this, pkProp.getColumn(), pkProp.getColumn().getDbName()));
        }
        querySql.append(_FROM_);
        querySql.append(getTable().getDbName());
        querySql.append(" where ");
        isFirstPkProp = true;
        for (DdsIndexDef.ColumnInfo keyProp : key.getColumnsInfo()) {
            if (isFirstPkProp) {
                isFirstPkProp = false;
            } else {
                querySql.append(" and ");
            }
            final DdsColumnDef prop = keyProp.getColumn();
            if (prop.getDbName() == null) {
                throw new WrongSecondaryKeyError("Property #" + prop.getId().toString() + " can't be a key (Table #" + getTable().getId() + ")");
            }
            querySql.append(prop.getDbName());
            querySql.append("=?");
            addParameter(new DbQuery.InputThisPropParam(prop.getId()));
        }

        return true;
    }
// Update object query

    public final boolean buildUpdate(final SortedSet<Id> modifiedPropIds) {
        DdsColumnDef modifiedProp;
        for (Id id : modifiedPropIds) {
            try {
                modifiedProp = getTable().getColumns().getById(id, ExtendableDefinitions.EScope.ALL);
            } catch (DefinitionNotFoundError e) {
                continue;
            }
            if (modifiedProp.getDbName() != null
                    && !modifiedProp.isExpression() //RADIX-2377
                    ) {
                addQueryProp(id, EPropUsageType.OTHER);
            }
        }
//    	querySql = new StringBuilder("begin\nupdate ");
        querySql = new StringBuilder("update ");
        querySql.append(getTable().getDbName());
        querySql.append(" set ");
        boolean res = false;
        final Iterator<Field> iter = fieldsByPropId.values().iterator();
        Field field;
        while (iter.hasNext()) {
            field = iter.next();
            if (field.getProp() instanceof DdsColumnDef && !field.isExpression()) {
                if (res) {
                    querySql.append(',');
                }
                querySql.append(((DdsColumnDef) field.getProp()).getDbName());
                querySql.append("=?");
                addParameter(new DbQuery.InputThisPropParam(getMainPropId(field.getProp().getId())));
                res = true;
            }
        }
        if (!res) {
            return false;
        }
        appendWhere(false);
//        querySql.append(";\nif (sql%rowcount = 0) then raise NO_DATA_FOUND; end if;\nend;");
        return true;
    }
// Insert object query    

    public final boolean buildInsert(final SortedSet<Id> modifiedPropIds, final Entity src, final boolean isUpVal) {
        DdsColumnDef modifiedProp;
        for (Id id : modifiedPropIds) {
            try {
                modifiedProp = getTable().getColumns().getById(id, ExtendableDefinitions.EScope.ALL);
            } catch (DefinitionNotFoundError e) {
                continue;
            }
            //if(modifiedProp instanceof TDbpNativeChildRefProperty)
            //	modifiedProp = ((TDbpNativeChildRefProperty)modifiedProp).getChildProp();
            if (modifiedProp.getDbName() != null
                    && !modifiedProp.isExpression() //RADIX-2377
                    ) {
                addQueryProp(modifiedProp.getId(), EPropUsageType.OTHER);
            }
        }
        querySql = new StringBuilder("insert into ");
        querySql.append(getTable().getDbName());
        querySql.append(" columns(");
        final StringBuilder valsStr = new StringBuilder();
        final Iterator<Field> fieldsIter = fieldsByPropId.values().iterator();
        Field field;
        while (fieldsIter.hasNext()) {
            field = fieldsIter.next();
            if ((field.getProp() instanceof DdsColumnDef)
                    && ((DdsColumnDef) field.getProp()).getDbName() != null
                    && !field.isExpression() //RADIX-2377
                    ) {
                final String propDbName = ((DdsColumnDef) field.getProp()).getDbName();
                if (isUpVal && RadObjectUpValueRef.DbNotation.isDbNotationColumn(propDbName)) {
                    continue;
                }
                if (valsStr.length() != 0) {
                    querySql.append(',');
                    valsStr.append(',');
                }
                querySql.append(propDbName);
                valsStr.append('?');
                addParameter(new DbQuery.InputThisPropParam(getMainPropId(field.getProp().getId())));
            }
        }
        if (isUpVal) {
            if (valsStr.length() != 0) {
                querySql.append(',');
                valsStr.append(',');
            }
            querySql.append(RadObjectUpValueRef.DbNotation.PROP_DEF_ID_DB_COL_NAME);
            valsStr.append('?');
            addParameter(new DbQuery.UpDefIdParam());

            querySql.append(',');
            valsStr.append(',');
            querySql.append(RadObjectUpValueRef.DbNotation.VAL_OWNER_ENTITY_ID_DB_COL_NAME);
            valsStr.append('?');
            addParameter(new DbQuery.UpValOwnerEntityIdParam());

            querySql.append(',');
            valsStr.append(',');
            querySql.append(RadObjectUpValueRef.DbNotation.VAL_OWNER_PID_DB_COL_NAME);
            valsStr.append('?');
            addParameter(new DbQuery.UpValOwnerPidParam());
        }
        boolean bNeedSelectFromSrc = false;
        if (src != null) { //copying all unmodified native property from src
            for (DdsColumnDef prop : getTable().getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                if (prop.getDbName() != null
                        && !prop.isExpression() && //RADIX-2377
                        !fieldsByPropId.containsKey(prop.getId().toString())) {
                    if (isUpVal && RadObjectUpValueRef.DbNotation.isDbNotationColumn(prop.getDbName())) {
                        continue;
                    }
                    if (valsStr.length() != 0) {
                        querySql.append(',');
                        valsStr.append(',');
                    }
                    querySql.append(prop.getDbName());
                    valsStr.append(prop.getDbName());
                    bNeedSelectFromSrc = true;
                }
            }
        }
        if (valsStr.length() != 0) {
            if (bNeedSelectFromSrc) {
                querySql.append(") (select ");
                querySql.append(valsStr);
                querySql.append(_FROM_);
                querySql.append(getTable().getDbName());
                querySql.append(" where ");
                boolean isFirstPkProp = true;
                for (DdsIndexDef.ColumnInfo pkProp : getTable().getPrimaryKey().getColumnsInfo()) {
                    if (isFirstPkProp) {
                        isFirstPkProp = false;
                    } else {
                        querySql.append(" and ");
                    }
                    querySql.append(pkProp.getColumn().getDbName());
                    querySql.append("=?");
                    addParameter(new CreateObjectQuery.InputSrcPropParam(pkProp.getColumnId()));
                }
            } else {
                querySql.append(") values (");
                querySql.append(valsStr);
            }
            querySql.append(')');
            //qry.append(");\n");
            return true;
        }
        //return "insert into " + table.getDbName() + ";\n";
        return false;
    }
}
