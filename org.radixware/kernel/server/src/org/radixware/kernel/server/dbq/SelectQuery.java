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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.IRadClassInstance;

public class SelectQuery extends GroupQuery {

    SelectQuery(
            final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final List<Field> fields,
            final List<Param> params) {
        super(arte, table, fields, params, querySql);
    }

    @Override
    protected boolean isReadOnly() {
        return true;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    public final Result select(final EntityGroup group, final long fromRecNo, final int count, final Pid parentPid) throws FilterParamNotDefinedException {
        return select(group, fromRecNo, count, null, parentPid, true);
    }

    protected final Result select(final EntityGroup group, final long fromRecNo, final int count, final IRadClassInstance child, final Pid parentPid, final boolean reprepareOnException) throws FilterParamNotDefinedException {
        //Setting input parameters
        prepare(group, parentPid);
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputFromRecParam) {
                try {
                    query.setLong(i, fromRecNo);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputFromRecParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputToRecParam) {
                /*
                 Our pagination select request looks like "select * from (select qry.*, ROWNUM R_N from (select ...) qry where ROWNUM <= ?) where R_N >= ?".
                 So we selecting count+1 records (count records in page and one to detect if next page exist ).
                 */
                try {
                    query.setLong(i, fromRecNo + count);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputToRecParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputRecCountParam) {
                try {
                    query.setLong(i, count + 1);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputRecCountParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputPrevObjectPropParam) {
                final Entity previousEntity;
                try {
                     previousEntity = group.getArte().getEntityObject(group.getPreviousEntityPid(), group.getPreviousEntityClassId() == null || group.getPreviousEntityClassId().getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS ? null : group.getPreviousEntityClassId().toString());
                } catch (Exception ex) {
                    throw new DbQueryBuilderError("Can't set InputPrevObjectPropParam: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
                }
                final Id propId = ((InputPrevObjectPropParam) param).propId;
                final RadPropDef prop = previousEntity.getRadMeta().getPropById(propId);
                setParam(i, prop.getValType(), prop.getDbType(), previousEntity.getProp(propId), prop.getName());
            }
            i++;
        }
        //Query
        final ResultSet rs;
        try {
            query.setFetchSize(count + 1);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_SELECT);
            try {
                rs = query.executeQuery();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_SELECT);
            }
            //Reading fields
            if (rs != null) {
                try {
                    final ArrayList<EntityPropVals> rows = new ArrayList<>(count);
                    EntityPropVals row;
                    for (int j = 0; j < count && rs.next(); j++) {
                        row = new EntityPropVals();
                        for (Field field : fields) {
                            if (field.alias != null) {
                                row.putPropValById(field.id, field.getFieldVal(rs));
                            }
                        }
                        try {
                            row.putPropValById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID, rs.getString(QuerySqlBuilder.ALL_ROLES_FIELD_ALIAS));
                        } catch (SQLException ex) {
                            //do nothing
                            final String exceptionAsString = ExceptionTextFormatter.exceptionStackToString(ex);
                            arte.getTrace().put(EEventSeverity.DEBUG, exceptionAsString, EEventSource.APP_DB);
                        }
                        rows.add(row);
                    }
                    return new Result(rows, rs.next());
                } finally {
                    try {
                        //trying to read all records in this ResultSet to avoid empty ResultSet on next request (RADIX-14227)
                        for (int j = 1; j <= 1000; j++) {
                            if (!rs.next()) {
                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        //do nothing
                        final String exceptionAsString = ExceptionTextFormatter.exceptionStackToString(ex);
                        arte.getTrace().put(EEventSeverity.DEBUG, exceptionAsString, EEventSource.APP_DB);
                    }
                    rs.close();
                }
            } else {
                throw new DatabaseError("Select query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            if (reprepareOnException) {
                try {
                    reprepareQuery();
                    return select(group, fromRecNo, count, child, parentPid, false);
                } catch (DbQueryBuilderError ex) {
                    //log secondary exception but throw original exception
                    final String exceptionAsString = ExceptionTextFormatter.exceptionStackToString(ex);
                    arte.getTrace().put(EEventSeverity.DEBUG, exceptionAsString, EEventSource.APP_DB);
                }
            }
            throw new DatabaseError("Select query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public static final class InputFromRecParam extends InputParam {
    }

    public static final class InputToRecParam extends InputParam {
    }

    public static final class InputRecCountParam extends InputParam {
    }

    public static final class InputPrevObjectPropParam extends InputParam {

        private final Id propId;

        public InputPrevObjectPropParam(Id propId) {
            this.propId = propId;
        }

        public Id getPropId() {
            return propId;
        }
    }

    public static final class Result {

        public final boolean hasMore;
        public final List<EntityPropVals> rows;

        protected Result(
                final List<EntityPropVals> rows,
                final boolean hasMore) {
            this.hasMore = hasMore;
            this.rows = Collections.unmodifiableList(rows);
        }

        @Override
        public String toString() {
            return "SelectResult {\n\t HasMore =" + hasMore + "\n\t Rows = " + rows + "\n}";
        }
    }
}
