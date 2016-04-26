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
import static org.radixware.kernel.server.dbq.DbQuery.checkIfItIsIntegrityConstraintViolated;
import static org.radixware.kernel.server.dbq.DbQuery.checkIfItIsNullConstraintViolated;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.meta.clazzes.RadObjectUpValueRef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityPropVals;

public class CreateObjectQuery extends DbQuery {

    CreateObjectQuery(
            final Arte arte,
            final DdsTableDef table,
            final String createSql,
            final List<Param> params) {
        super(arte, table, null, params, createSql);
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return false;
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    public final void create(final Entity newEntity, final EntityPropVals propVals, final Entity src, final RadObjectUpValueRef upValRef) {
        //Setting input parameters
        prepare();
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputPidParam) {
                try {
                    query.setString(i, newEntity.getPid().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputPidParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputSrcPidParam) {
                if (src == null) {
                    throw new DbQueryBuilderError("Can't set InputSrcPidParam: src is null", null);
                }
                try {
                    query.setString(i, src.getPid().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputSrcPidParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputThisPropParam) {
                final Id propId = ((InputThisPropParam) param).propId;
                try {
                    final DdsColumnDef colDef = table.getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                    setParam(i, colDef.getValType(), colDef.getDbType(), propVals.getPropValById(propId), colDef.getName());
                } catch (PropNotLoadedException e) {
                    throw new DbQueryBuilderError("Input property is not inited", e);
                }
            } else if (param instanceof InputSrcPropParam) {
                if (src == null) {
                    throw new DbQueryBuilderError("Can't set InputSrcPropParam: src is null", null);
                }
                final Id propId = ((InputSrcPropParam) param).propId;
                final RadPropDef propDef = src.getRadMeta().getPropById(propId);
                setParam(i, propDef.getValType(), propDef.getDbType(), src.getProp(propId), propDef.getName());
            } else if (param instanceof UpDefIdParam) {
                if (upValRef == null) {
                    throw new DbQueryBuilderError("Can't set UpDefIdParam: user property value reference is null", null);
                }
                try {
                    query.setString(i, upValRef.getPropertyDefId().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set UpDefIdParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof UpValOwnerEntityIdParam) {
                if (upValRef == null) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerEntityIdParam: user property value reference is null", null);
                }
                if (upValRef.getValueOwner() == null) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerEntityIdParam: user property value owner is null", null);
                }
                try {
                    query.setString(i, upValRef.getValueOwner().getEntityId().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerEntityIdParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof UpValOwnerPidParam) {
                if (upValRef == null) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerPidParam: user property value reference is null", null);
                }
                if (upValRef.getValueOwner() == null) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerPidParam: user property value owner is null", null);
                }
                try {
                    query.setString(i, upValRef.getValueOwner().getPid().toString());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set UpValOwnerPidParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else {
                throw new DbQueryBuilderError("Unsupported input parameter type in create query", null);
            }
            i++;
        }
        //Query
        try {
            query.executeUpdate();
        } catch (SQLException e) {
            checkIfItIsUniqueConstraintViolated(arte, e, table, newEntity.getRadMeta());
            checkIfItIsNullConstraintViolated(arte, e, table);
            checkIfItIsIntegrityConstraintViolated(arte, e, table);
            throw new DatabaseError("Create query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    @Override
    public void free() {
        if (isDeletedFromCache()) {
            sendBatch(true);
        }
        super.free();
    }

    void sendBatch(final boolean bThrowExceptionOnDbErr) {
        if (asOracleStatement() == null) {
            return;
        }

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_CREATE_BATCH);
        try {
            if (arte.getDbConnection().get().isClosed()) {
                return;
            }
            asOracleStatement().sendBatch();
        } catch (SQLException e) {
            if (bThrowExceptionOnDbErr) {
                throw new DatabaseError("Create query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
            } else {
                arte.getTrace().put(EEventSeverity.DEBUG, "SQLException happend on CreateObjectQuery.sendBatch() is hidden: " + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.DB_QUERY_BUILDER);
            }
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_CREATE_BATCH);
        }
    }

    void setExecuteBatch(final int batchSize) {
        if (asOracleStatement() == null) {
            return;
        }
        try {
            asOracleStatement().setExecuteBatch(batchSize);
        } catch (SQLException e) {
            throw new DatabaseError("Create query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    int getExecuteBatch() {
        if (asOracleStatement() == null) {
            return 0;
        }
        return asOracleStatement().getExecuteBatch();
    }

    static final class InputSrcPidParam extends InputParam {
    }

    static final class InputSrcPropParam extends InputParam {

        final Id propId;

        public InputSrcPropParam(final Id propId) {
            this.propId = propId;
        }
    }
}
