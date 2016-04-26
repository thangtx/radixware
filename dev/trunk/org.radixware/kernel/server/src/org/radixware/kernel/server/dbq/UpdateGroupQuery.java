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

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;

public final class UpdateGroupQuery extends GroupQuery {

    UpdateGroupQuery(
            final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final List<Param> params) {
        super(arte, table, null, params, querySql);
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    public final void update(
            final EntityGroup group,
            final Pid parentPid,
            final EntityPropVals propVals) throws FilterParamNotDefinedException {
        prepare(group, parentPid);
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputThisPropParam) {
                final Id propId = ((InputThisPropParam) param).propId;
                try {
                    final DdsColumnDef columnDef = table.getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                    setParam(i, columnDef.getValType(), columnDef.getDbType(), propVals.getPropValById(propId), columnDef.getName());
                } catch (PropNotLoadedException e) {
                    throw new DbQueryBuilderError("Input property is not inited", e);
                }
            }// else
            //    throw new DbpDbQueryBuilderError("Unsupported input parameter type in update group query", null);
            i++;
        }
        try {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseError("Update query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
