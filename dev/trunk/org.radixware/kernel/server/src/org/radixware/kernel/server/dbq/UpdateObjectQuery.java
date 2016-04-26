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

import java.sql.SQLException;
import java.util.List;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityPropVals;

public final class UpdateObjectQuery extends DbQuery {

    private int pendingUpdates = 0;

    UpdateObjectQuery(
            final Arte arte,
            final DdsTableDef table,
            final String query,
            final List<Param> params) {
        super(arte, table, null, params, query);
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return false;
    }

    public final void update(final Entity entity, final EntityPropVals propVals) {
        //Setting input parameters
        prepare();
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputPidParam) {
                try {
                    query.setString(i, entity.getPid().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputPidParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputThisPropParam) {
                final Id propId = ((InputThisPropParam) param).propId;
                try {
                    final DdsColumnDef columnDef = table.getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                    setParam(i, columnDef.getValType(), columnDef.getDbType(), propVals.getPropValById(propId), columnDef.getName());
                } catch (PropNotLoadedException e) {
                    throw new DbQueryBuilderError("Input property is not inited", e);
                }
            } else {
                throw new DbQueryBuilderError("Unsupported input parameter type in update query", null);
            }
            i++;
        }
        //Query
        try {
            final int updatedRowCount = query.executeUpdate();
            if (updatedRowCount == 0) {
                if (getExecuteBatch() <= 1) {
                    throw new EntityObjectNotExistsError(entity.getPid());
                } else {
                    pendingUpdates++;
                }
            } else if (pendingUpdates == 0) {
                if (updatedRowCount != 1) {
                    throw new DbQueryBuilderError("Wrong single object update query builded. Updated row count is " + String.valueOf(updatedRowCount), null);
                }
            } else {
                pendingUpdates++;
                afterBatchUpdate(updatedRowCount);
            }
        } catch (SQLException e) {
            onUpdateException(e, entity);
        }
    }

    private void afterBatchUpdate(int updatedRowCount) {
        try {
            if (pendingUpdates != updatedRowCount) {
                throw new DatabaseError("Unexpected count of updated entities during batch update: expected " + pendingUpdates + ", got " + updatedRowCount, null);
            }
        } finally {
            pendingUpdates = 0;//clear counter in any case
        }
    }

    @Override
    public void free() {
        if (isDeletedFromCache()) {
            sendBatch(true);
        }
        super.free();
    }

    private void onUpdateException(final SQLException e, final Entity entity) throws DatabaseError, EntityObjectNotExistsError {
        checkIfItIsUniqueConstraintViolated(arte, e, table, entity.getRadMeta());
        checkIfItIsNullConstraintViolated(arte, e, table);
        checkIfItIsIntegrityConstraintViolated(arte, e, table);
        if (e.getErrorCode() == OraExCodes.NO_DATA_FOUND) {
            throw new EntityObjectNotExistsError(entity.getPid());
        }
        throw new DatabaseError("Update query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
    }

    void sendBatch(final boolean bThrowExceptionOnDbErr) {
        if (asOracleStatement() == null) {
            return;
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_UPDATE_BATCH);
        try {
            if (arte.getDbConnection().get().isClosed()) {
                return;
            }
            int updatedCount = asOracleStatement().sendBatch();
            afterBatchUpdate(updatedCount);
        } catch (SQLException | DatabaseError e) {
            if (bThrowExceptionOnDbErr) {
                if (e instanceof DatabaseError) {
                    throw (DatabaseError) e;
                }
                throw new DatabaseError("Update query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
            } else {
                arte.getTrace().put(EEventSeverity.DEBUG, "Error happend on UpdateObjectQuery.sendBatch() is hidden: " + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.DB_QUERY_BUILDER);
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_UPDATE_BATCH);
        }
    }

    void setExecuteBatch(final int batchSize) {
        if (asOracleStatement() == null) {
            return;
        }
        try {
            asOracleStatement().setExecuteBatch(batchSize);
        } catch (SQLException e) {
            throw new DatabaseError("Update query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    int getExecuteBatch() {
        if (asOracleStatement() == null) {
            return 0;
        }
        return asOracleStatement().getExecuteBatch();
    }
}
