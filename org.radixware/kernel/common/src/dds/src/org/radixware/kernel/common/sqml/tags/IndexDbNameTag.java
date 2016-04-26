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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - имя индекса в базе данных.
 * Транслируется в имя индекса в базе данных.
 */
public class IndexDbNameTag extends TableAbstractTag implements ISqlTag {

    protected IndexDbNameTag() {
        super();
    }
    private Id indexId = null;

    /**
     * Получить идентификатор индекса.
     * @return идентификатор индекса или null, если используется первичный ключ.
     */
    public Id getIndexId() {
        return indexId;
    }

    public void setIndexId(Id indexId) {
        if (!Utils.equals(this.indexId, indexId)) {
            this.indexId = indexId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static IndexDbNameTag newInstance() {
            return new IndexDbNameTag();
        }
    }

    /**
     * Find index.
     * @return index or null if not found.
     */
    public DdsIndexDef findIndex() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            final DdsTableDef table = environment.findTableById(getTableId());
            if (table != null) {
                final DdsPrimaryKeyDef pk = table.getPrimaryKey();
                if (indexId == null || Utils.equals(indexId, pk.getId())) {
                    return pk;
                } else {
                    return table.getIndices().findById(indexId, EScope.LOCAL_AND_OVERWRITE).get();
                }
            }
        }
        return null;
    }

    /**
     * Find index.
     * @throw DefinitionNotFoundError if not found.
     */
    public DdsIndexDef getIndex() {
        DdsIndexDef index = findIndex();
        if (index == null) {
            throw new DefinitionNotFoundError(indexId);
        }
        return index;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        // do not collect table
        //super.collectDependences(list);

        DdsIndexDef index = findIndex();
        if (index != null) {
            list.add(index);
        }
    }
    public static final String INDEX_DB_NAME_TAG_TYPE_TITLE = "Index SQL Name Tag";
    public static final String INDEX_DB_NAME_TAG_TYPES_TITLE = "Index SQL Name Tags";

    @Override
    public String getTypeTitle() {
        return INDEX_DB_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return INDEX_DB_NAME_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Index Id: " + String.valueOf(getIndexId()));
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
