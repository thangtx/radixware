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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.LockTimeoutError;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;

public final class ReadObjectQuery extends DbQuery {

    private final String targetName;

    ReadObjectQuery(
            final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final List<Field> fields,
            final List<Param> params,
            final String targetName) {
        super(arte, table, fields, params, querySql);
        this.targetName = targetName;
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    @Override
    protected boolean isReadOnly() {
        return true;
    }

    public final void read(final Pid pid, final EntityPropVals propVals) {
        read(pid, propVals, true);
    }

    public final void read(final Pid pid, final EntityPropVals propVals, final boolean reprepareOnException) {
        //Setting input parameters
        prepare();
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputPidParam) {
                try {
                    query.setString(i, pid.toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputPidParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputThisPropParam) {
                final Id propId = ((InputThisPropParam) param).propId;
                final DdsColumnDef columnDef = table.getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                final EValType valType = columnDef.getValType();
                try {
                    setParam(i, valType, columnDef.getDbType(), propVals.getPropValById(propId), null);
                } catch (PropNotLoadedException e) {
                    throw new DbQueryBuilderError("Input property is not inited", e);
                }
            } else {
                throw new DbQueryBuilderError("Unsupported input parameter type in read query", null);
            }
            i++;
        }
        //Query
        final ResultSet rs;
        try {
            final String profileSection = ETimingSection.RDX_ENTITY_DB_READ.getValue() + (targetName == null || targetName.isEmpty() ? "" : ":" + targetName);
            arte.getProfiler().enterTimingSection(profileSection);
            try {
                rs = query.executeQuery();
            } finally {
                arte.getProfiler().leaveTimingSection(profileSection);
            }
            //Reading fields
            if (rs != null) {
                try {
                    if (rs.next()) {
                        for (Field field : fields) {
                            if (!propVals.containsPropById(field.id)) {//if read then store loaded and updated values
                                //adding or replacing (if reread)
                                propVals.putPropValById(field.id, field.getFieldVal(rs));
                            }
                        }
                        if (!propVals.containsPropById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID) && sqlQuery.contains(QuerySqlBuilder.ALL_ROLES_FIELD_ALIAS)) {
                            try {
                                propVals.putPropValById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID, rs.getString(QuerySqlBuilder.ALL_ROLES_FIELD_ALIAS));
                            } catch (SQLException ex) {
                                //do nothing
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                        if (!propVals.containsPropById(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID) && sqlQuery.contains(QuerySqlBuilder.ACS_COORDINATES_AS_STR_FIELD_ALIAS)) {
                            try {
                                propVals.putPropValById(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID, rs.getString(QuerySqlBuilder.ACS_COORDINATES_AS_STR_FIELD_ALIAS));
                            } catch (SQLException ex) {
                                //do nothing
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    } else {
                        throw new EntityObjectNotExistsError(pid);
                    }
                } finally {
                    rs.close();
                }
            } else {
                throw new DatabaseError("Read query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == OraExCodes.RESOURCE_BUSY || e.getErrorCode() == OraExCodes.RESOURCE_REQUEST_TIMEOUT) {
                throw new LockTimeoutError("Lock timeout exceeded", e);
            }
            if (reprepareOnException) {
                try {
                    reprepareQuery();
                    read(pid, propVals, false);
                } catch (DbQueryBuilderError ex) {
                    //throw DatabasError
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            throw new DatabaseError("Read query error (TableID: " + (pid == null ? null : pid.getEntityId()) + ", PID: '" + pid + "'): " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
