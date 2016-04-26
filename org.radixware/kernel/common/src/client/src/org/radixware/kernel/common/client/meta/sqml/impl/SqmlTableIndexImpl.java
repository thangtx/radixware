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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.dds.DdsColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;


final class SqmlTableIndexImpl implements ISqmlTableIndexDef {

    private final DdsIndexDef indexDef;
    private List<ISqmlColumnDef> indexColumns;
    final IClientEnvironment environment;

    public SqmlTableIndexImpl(final IClientEnvironment environment, final DdsIndexDef def) {
        indexDef = def;
        this.environment = environment;
    }

    @Override
    public Id getId() {
        return indexDef.getId();
    }

    @Override
    public String getShortName() {
        return indexDef.getName();
    }

    @Override
    public String getFullName() {
        return indexDef.getName();
    }

    @Override
    public String getTitle() {
        return getFullName();
    }

    @Override
    public String getDisplayableText(EDefinitionDisplayMode mode) {
        return getShortName();
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(DdsDefinitionIcon.INDEX);
    }

    @Override
    public boolean isPrimaryKey() {
        return indexDef instanceof DdsPrimaryKeyDef;
    }

    @Override
    public List<ISqmlColumnDef> getColumns() {
        if (indexColumns == null) {
            indexColumns = new ArrayList<ISqmlColumnDef>(16);
            final Id tableId = indexDef.getOwnerTable().getId();
            final ISqmlTableDef ownerTable =
                    environment.getSqmlDefinitions().findTableById(tableId);
            final DdsIndexDef.ColumnsInfo columns = indexDef.getColumnsInfo();
            ISqmlColumnDef column;
            for (DdsColumnInfo columnInfo : columns) {
                column = ownerTable.getColumns().getColumnById(columnInfo.getColumnId());
                if (column != null) {
                    indexColumns.add(column);
                }
            }
        }
        return Collections.unmodifiableList(indexColumns);
    }

    @Override
    public String getModuleName() {
        return indexDef.getModule().getQualifiedName();
    }

    @Override
    public Id[] getIdPath() {
        return indexDef.getIdPath();
    }
    
    @Override
    public boolean isDeprecated() {
        return indexDef.isDeprecated();
    }
}
