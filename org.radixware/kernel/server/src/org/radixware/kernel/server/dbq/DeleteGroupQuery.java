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
import oracle.jdbc.OraclePreparedStatement;
import org.radixware.kernel.common.defs.dds.DdsTableDef;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;

public final class DeleteGroupQuery extends GroupQuery {


    DeleteGroupQuery(final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final List<Param> params) {
        super(arte, table, null, params, querySql);
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    
    public final void delete(final EntityGroup group, final Pid parentPid) throws FilterParamNotDefinedException {
        prepare(group, parentPid);
        try {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseError("Delete query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
