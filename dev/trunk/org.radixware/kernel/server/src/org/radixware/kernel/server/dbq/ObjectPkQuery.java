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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.WrongSecondaryKeyError;

final class ObjectPkQuery extends DbQuery {

    ObjectPkQuery(
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

    public List<Object> getPrimaryKeyVals(final DdsIndexDef key, final List<Object> keyPropVals) {
        prepareQuery();
        if (key.getColumnsInfo().size() != keyPropVals.size()) {
            throw new WrongSecondaryKeyError("Key property count is not equal to key property values");
        }
        try {
            for (int i = 0; i < keyPropVals.size(); i++) {
                final DdsColumnDef columnDef = key.getColumnsInfo().get(i).getColumn();
                setParam(i + 1, columnDef.getValType(), columnDef.getDbType(), keyPropVals.get(i), columnDef.getName());
            }
            final ResultSet rs = query.executeQuery();
            if (rs != null) {
                try {
                    if (rs.next()) {
                        final HashMap<Id, Object> tmpRes = new HashMap<Id, Object>();
                        for (Field field : fields) {
                            tmpRes.put(field.prop.getId(), field.getFieldVal(rs));
                        }
                        final ArrayList<Object> res = new ArrayList<Object>(table.getPrimaryKey().getColumnsInfo().size());
                        for (DdsIndexDef.ColumnInfo pkProp : table.getPrimaryKey().getColumnsInfo()) {
                            res.add(tmpRes.get(pkProp.getColumnId()));
                        }
                        if (rs.next()) {
                            throw new WrongSecondaryKeyError("Secondary key is not unique");
                        }
                        return Collections.unmodifiableList(res);
                    } else {
                        throw new EntityObjectNotExistsError(arte, table, key, keyPropVals);
                    }
                } finally {
                    rs.close();
                }
            } else {
                throw new DatabaseError("Primary key query error: ResultSet == null", null);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Primary key query error: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
}
