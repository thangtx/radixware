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

package org.radixware.kernel.common.sqml.tags;

import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - имя таблицы в базе данных.
 */
public class TableSqlNameTag extends TableAbstractTag implements ISqlTag {

    protected TableSqlNameTag() {
        super();
    }
    private String tableAlias = "";

    /**
     * Получить альяс таблицы.
     * Если задан (не null и не пустой), то тэг оттранслируется в альяс, иначе в имя таблицы в базе данных.
     */
    public String getTableAlias() {
        return tableAlias;
    }

    public final boolean isTableAliasDefined() {
        return tableAlias != null && !tableAlias.isEmpty();
    }

    public void setTableAlias(String tableAlias) {
        if (!Utils.equals(this.tableAlias, tableAlias)) {
            this.tableAlias = tableAlias;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static TableSqlNameTag newInstance() {
            return new TableSqlNameTag();
        }
    }
    public static final String TABLE_SQL_NAME_TAG_TYPE_TITLE = "Table SQL Name Tag";
    public static final String TABLE_SQL_NAME_TAG_TYPES_TITLE = "Table SQL Name Tags";

    @Override
    public String getTypeTitle() {
        return TABLE_SQL_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return TABLE_SQL_NAME_TAG_TYPES_TITLE;
    }
    private String sql;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void setSql(final String sql) {
        if (!Utils.equals(this.sql, sql)) {
            this.sql = sql;
            setEditState(EEditState.MODIFIED);
        }
    }
}
