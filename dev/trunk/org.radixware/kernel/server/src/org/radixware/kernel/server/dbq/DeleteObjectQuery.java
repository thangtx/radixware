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
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import static org.radixware.kernel.server.dbq.DbQuery.checkIfItIsIntegrityConstraintViolated;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.types.Pid;


public final class DeleteObjectQuery extends DbQuery {

    DeleteObjectQuery(
            final Arte arte,
            final DdsTableDef table,
            final String sqlQuery,
            final List<Param> params) {
        super(arte, table, null, params, sqlQuery);
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    public void delete(final Pid pid) {
        prepareQuery();
        for (int i = 0; i < table.getPrimaryKey().getColumnsInfo().size(); i++) {
            final DdsColumnDef columnDef = table.getPrimaryKey().getColumnsInfo().get(i).getColumn();
            setParam(i + 1, columnDef.getValType(), columnDef.getDbType(), pid.getPkVals().get(i), null);
        }
        try {
            if (query.executeUpdate() != 1) {
                throw new EntityObjectNotExistsError(pid);
            }
        } catch (SQLException e) {
            checkIfItIsIntegrityConstraintViolated(arte, e, table);
            throw new DatabaseError("Delete query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
