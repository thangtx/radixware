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
import org.radixware.kernel.common.defs.dds.DdsTableDef;

import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.IRadClassInstance;

public final class SelectParentQuery extends SelectQuery {

    SelectParentQuery(
            final Arte arte,
            final DdsTableDef table,
            final String querySql,
            final PreparedStatement query,
            final List<Field> fields,
            final List<Param> params) throws SQLException {
        super(arte, table, querySql, fields, params);
    }

    @Override
    protected boolean isReadOnly() {
        return true;
    }
    
    

    public final Result selectParent(final EntityGroup group, final long fromRecNo, final int count, final IRadClassInstance child) throws FilterParamNotDefinedException {
        return select(group, fromRecNo, count, child, null, true);
    }
}
