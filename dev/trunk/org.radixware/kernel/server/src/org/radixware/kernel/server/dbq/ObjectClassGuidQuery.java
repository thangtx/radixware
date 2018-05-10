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

import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.types.Pid;

@Deprecated
public final class ObjectClassGuidQuery extends DbQuery {

    ObjectClassGuidQuery(
            final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final List<Field> fields,
            final List<Param> params) {
        super(arte, table, fields, params, querySql);
    }

    @Override
    protected int getDefaultFetchSize() {
        return 1;
    }

    @Override
    protected boolean canCloseStatmentOnFree() {
        return true;
    }

    public Id getClassGuid(final Pid pid) {
        prepareQuery();
        int i = 0;
        for (DdsIndexDef.ColumnInfo pkProp : table.getPrimaryKey().getColumnsInfo()) {
            setParam(i + 1, pkProp.getColumn().getValType(), pkProp.getColumn().getDbType(), pid.getPkVals().get(i), null);
            i++;
        }
        final ResultSet rs;
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_READ_CLASSGUID);
            rs = query.executeQuery();
            if (rs != null) {
                try {
                    if (rs.next()) {
                        return Id.Factory.loadFrom(rs.getString(1));
                    } else {
                        throw new EntityObjectNotExistsError(pid);
                    }
                } finally {
                    rs.close();
                }
            } else {
                throw new DatabaseError("Class guid query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Class guid query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_READ_CLASSGUID);
        }
    }
}
