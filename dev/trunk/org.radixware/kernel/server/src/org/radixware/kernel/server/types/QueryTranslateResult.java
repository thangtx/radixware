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

package org.radixware.kernel.server.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.DbQuery.Param;
import org.radixware.kernel.server.dbq.GroupQuery;
import org.radixware.kernel.server.dbq.GroupQuery.FilterParam;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;


public class QueryTranslateResult {

    private final String sql;
    private final List<Param> parameters;

    public QueryTranslateResult(final String sql, final List<Param> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    private void bindFilterParameters(final Arte arte, final PreparedStatement statement, final Map<Id, Object> values) throws FilterParamNotDefinedException {
        for (int i = 0; i < parameters.size(); i++) {
            GroupQuery.setFilterParam(arte, statement, i + 1, (FilterParam) parameters.get(i), values);
        }
    }

    public PreparedStatement prepare(final Arte arte, final Map<Id, Object> values) throws SQLException, FilterParamNotDefinedException {
        final PreparedStatement statement = arte.getDbConnection().get().prepareStatement(sql);
        bindFilterParameters(arte, statement, values);
        return statement;
    }
    
    public String getSql() {
        return sql;
    }
}
