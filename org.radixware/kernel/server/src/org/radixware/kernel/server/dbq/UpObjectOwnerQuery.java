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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.types.Pid;

public final class UpObjectOwnerQuery extends DbQuery {

    UpObjectOwnerQuery(
            final Arte arte,
            final DdsTableDef table,
            final String query,
            final List<Field> fields,
            final List<Param> params) {
        super(arte, table, fields, params, query);
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

    public static final class OwnerProp {

        public final Pid ownerPid;
        public final Id propId;

        public OwnerProp(final Pid ownerPid, final Id propId) {
            this.ownerPid = ownerPid;
            this.propId = propId;
        }
    }

//Public methods
    public OwnerProp getUpObjectOwner(final Pid pid) {
        prepareQuery();
        for (int i = 0; i < table.getPrimaryKey().getColumnsInfo().size(); i++) {
            final DdsColumnDef columnDef = table.getPrimaryKey().getColumnsInfo().get(i).getColumn();
            setParam(i + 1, columnDef.getValType(), columnDef.getDbType(), pid.getPkVals().get(i), null);
        }
        final ResultSet rs;
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                rs = query.executeQuery();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            if (rs != null) {
                try {
                    if (rs.next()) {
                        final Id propId = Id.Factory.loadFrom(rs.getString(3));
                        final Pid ownerPid = new Pid(arte, Id.Factory.loadFrom(rs.getString(1)), rs.getString(2));
                        return new OwnerProp(ownerPid, propId);
                    } else {
                        throw new EntityObjectNotExistsError(pid);
                    }
                } finally {
                    rs.close();
                }
            } else {
                throw new DatabaseError("User property owner query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            throw new DatabaseError("User property owner query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
