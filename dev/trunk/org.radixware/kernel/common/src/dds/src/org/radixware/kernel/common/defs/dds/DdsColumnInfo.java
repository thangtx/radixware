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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.utils.Utils;

/**
 * Link to {@llinkplain DdsColumnDef column} by identifier.
 */
public abstract class DdsColumnInfo extends RadixObject {

    protected DdsColumnInfo() {
    }

    public DdsColumnInfo(Id columnId) {
        this.columnId = columnId;
    }
    private Id columnId = null;

    /**
     * Get column identifier.
     * @return column identifier or null if not defined.
     */
    public Id getColumnId() {
        return columnId;
    }

    /**
     * Set column identifier.
     */
    public void setColumnId(Id columnId) {
        if (!Utils.equals(this.columnId, columnId)) {
            this.columnId = columnId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Получить {@link DdsTriggerDef триггер} (владельца данного объекта).
     */
    protected abstract DdsTableDef findTable();

    /**
     * Найти колонку, на которую ссылаемся.
     * @return колонку, или null, если колонка не найдена.
     */
    public DdsColumnDef findColumn() {
        DdsTableDef table = findTable();
        if (table != null) {
            return table.getColumns().findById(columnId,EScope.LOCAL_AND_OVERWRITE).get();
        } else {
            return null;
        }
    }

    /**
     * Найти колонку, на которую ссылаемся.
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsColumnDef getColumn() {
        DdsColumnDef column = findColumn();
        if (column == null) {
            throw new DefinitionNotFoundError(columnId);
        }
        return column;
    }
}
