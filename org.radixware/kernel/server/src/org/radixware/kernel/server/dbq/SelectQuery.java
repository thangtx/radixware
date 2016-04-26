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
import org.radixware.kernel.common.enums.ETimingSection;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
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
        prepare(group, parentPid);

        //Setting input parameters
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputFromRecParam) {
                try {
                    query.setLong(i, fromRecNo);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputFromRecParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputToRecParam) {
                try {
                    query.setLong(i, fromRecNo + count + 1);
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputToRecParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
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
                    final ArrayList<EntityPropVals> rows = new ArrayList<EntityPropVals>(count);
                    EntityPropVals row;
                    for (int j = 0; j < count && rs.next(); j++) {
                        row = new EntityPropVals();
                        for (Field field : fields) {
                            if (field.alias != null) {
                                row.putPropValById(field.prop.getId(), field.getFieldVal(rs));
                            }
                        }
                        try {
                            row.putPropValById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID, rs.getString(QuerySqlBuilder.ALL_ROLES_FIELD_ALIAS));
                        } catch (SQLException ex) {
                            //do nothing
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        rows.add(row);
                    }
                    return new Result(rows, rs.next());
                } finally {
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
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    //throw DatabasError
                }
            }
            throw new DatabaseError("Select query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    static final class InputFromRecParam extends InputParam {
    }

    static final class InputToRecParam extends InputParam {
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
