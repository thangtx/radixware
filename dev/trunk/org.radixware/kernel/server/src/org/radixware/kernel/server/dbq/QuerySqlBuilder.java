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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.DbQuery.Param;
import org.radixware.kernel.server.types.Entity;

public abstract class QuerySqlBuilder extends SqlBuilder {
//Constants

    public static final String ALL_ROLES_FIELD_ALIAS = "DRC_AR";
    public static final Id ALL_ROLES_FIELD_COL_ID = Id.Factory.loadFrom(EDefinitionIdPrefix.DDS_COLUMN.getValue() + "DrcAllRoles_______________");
    public static final Id ACS_COORDINATES_AS_STR_COL_ID = Id.Factory.loadFrom(EDefinitionIdPrefix.DDS_COLUMN.getValue() + "AcsCoordinatesAsStr_______");
    public static final String ACS_COORDINATES_AS_STR_FIELD_ALIAS = "ACS_COORDS_AS_STR";
    public static final String MAIN_ALIAS = TABLE_ALIAS_PREFIX + "1";
//Private fields    
    protected final Entity entity;
    private int tabCount;
    private int fieldCount;
    private Vector<Param> params;
    protected StringBuilder querySql;

//Constructor
    public QuerySqlBuilder(
            final Entity entity,
            final EQueryContextType queryCntxType,
            final EQueryBuilderAliasPolicy aliasPolicy) {
        super(entity.getArte(), entity.getDdsMeta(), MAIN_ALIAS, queryCntxType, aliasPolicy);
        tabCount = 1;
        fieldCount = 0;
        this.entity = entity;
        params = null;
    }

    public QuerySqlBuilder(
            final Arte arte, final DdsTableDef table,
            final EQueryContextType queryCntxType,
            final EQueryBuilderAliasPolicy aliasPolicy) {
        super(arte, table, MAIN_ALIAS, queryCntxType, aliasPolicy);
        tabCount = 1;
        fieldCount = 0;
        entity = null;
        params = null;
    }

    public QuerySqlBuilder(
            final Arte arte,
            final DdsTableDef table,
            final String alias,
            final EQueryContextType queryCntxType) {
        super(arte, table, alias, queryCntxType, alias != null ? EQueryBuilderAliasPolicy.USE_ALIASES : EQueryBuilderAliasPolicy.DO_NOT_USE_ALIASES);
        tabCount = 1;
        fieldCount = 0;
        entity = null;
        params = null;
    }

//Public methods
    public final String getQuerySql() {
        return querySql.toString();
    }

    final List<DbQuery.Field> getQueryFields() {
        final List<DbQuery.Field> res = new ArrayList<DbQuery.Field>(fieldsByPropId.size());
        for (SqlBuilder.Field builderField : fieldsByPropId.values()) {
            while (builderField instanceof SqlBuilder.JoinField) {
                builderField = ((SqlBuilder.JoinField) builderField).joinField;
            }
            res.add(new DbQuery.Field(getArte(), builderField.getProp(), builderField.getAlias(), builderField.getIndex()));
        }
        return Collections.unmodifiableList(res);
    }

    final List<DbQuery.Param> getQueryParams() {
        if (params == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(params);
    }

//Protected methods
    protected final void addQueryProp(final Id propId, final EPropUsageType propUsageType) {
        if (propId.getPrefix() != EDefinitionIdPrefix.ADS_USER_PROP) {
            addProp(propId, propUsageType, null);
        }
    }

    protected int incrementTableCount() {
        return (++tabCount);
    }

    protected int incrementFieldCount() {
        return (++fieldCount);
    }

    @Override
    protected final QuerySqlBuilder getMainBuilder() {
        return this;
    }

    @Override
    public final void addParameter(final DbQuery.Param param) {
        if (params == null) {
            params = new Vector<Param>();
        }
        params.add(param);
    }

    final void insParameter(final DbQuery.Param param, final int index) {
        if (params == null) {
            params = new Vector<Param>();
        }
        params.insertElementAt(param, index);
    }
//Access Control tools
}
