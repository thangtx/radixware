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
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг исползующий поиск какой-либо таблицы для своей трансляции.
 */
public abstract class TableAbstractTag extends Sqml.Tag {

    protected TableAbstractTag() {
        super();
    }
    private Id tableId = null;

    /**
     * Получить идентификатор таблицы.
     */
    public Id getTableId() {
        return tableId;
    }

    public void setTableId(Id tableId) {
        if (!Utils.equals(this.tableId, tableId)) {
            this.tableId = tableId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find table.
     * @return table or null if not found.
     */
    public DdsTableDef findTable() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findTableById(tableId);
        } else {
            return null;
        }
    }

    /**
     * Find table.
     * @throws DefinitionNotFoundError of not found.
     */
    public DdsTableDef getTable() {
        DdsTableDef table = findTable();
        if (table == null) {
            throw new DefinitionNotFoundError(tableId);
        }
        return table;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsTableDef table = findTable();
        if (table != null) {
            list.add(table);
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Table Id: " + String.valueOf(getTableId()));
    }
}
