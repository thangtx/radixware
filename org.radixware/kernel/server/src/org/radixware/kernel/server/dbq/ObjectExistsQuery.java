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
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.Pid;

public final class ObjectExistsQuery extends DbQuery {

    ObjectExistsQuery(
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
    protected boolean isReadOnly() {
        return true;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    public boolean isExists(final Pid pid) {
        prepareQuery();
        for (int i = 0; i < table.getPrimaryKey().getColumnsInfo().size(); i++) {
            final DdsColumnDef columnDef = table.getPrimaryKey().getColumnsInfo().get(i).getColumn();
            setParam(i + 1, columnDef.getValType(), columnDef.getDbType(), pid.getPkVals().get(i), null);
        }

        ResultSet rs;
        try {
            final String profileSection = ETimingSection.RDX_ENTITY_DB_READ.getValue() + ":" + table.getDbName();
            arte.getProfiler().enterTimingSection(profileSection);
            try {
                rs = query.executeQuery();
            } finally {
                arte.getProfiler().leaveTimingSection(profileSection);
            }
            if (rs != null) {
                try {
                    return rs.next();
                } finally {
                    rs.close();
                }
            } else {
                throw new DatabaseError("\"Exists\" query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            throw new DatabaseError("\"Exists\" query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
