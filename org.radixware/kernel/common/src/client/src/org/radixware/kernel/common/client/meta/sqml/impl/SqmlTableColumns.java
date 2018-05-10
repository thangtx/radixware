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
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTablePropertyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


final class SqmlTableColumns implements ISqmlTableColumns {

    private final List<ISqmlColumnDef> columns = new ArrayList<>();
    private final IClientEnvironment environment;

    public SqmlTableColumns(final IClientEnvironment environment, final DdsTableDef tableDef) {
        this.environment = environment;
        fillColumns(tableDef, tableDef.getId());
    }

    public SqmlTableColumns(final IClientEnvironment environment, final AdsEntityObjectClassDef classDef) {
        //В сущности могут быть опубликованы не все колонки таблицы
        this.environment = environment;
        final List<AdsPropertyDef> properties = classDef.getProperties().get(EScope.ALL);
        AdsPropertyDef finalProperty;
        for (AdsPropertyDef property : properties) {
            if ((property instanceof AdsTablePropertyDef) 
                || (property instanceof AdsExpressionPropertyDef)) {
                finalProperty = property;
                //parent properties temporary restricted
                if (finalProperty instanceof AdsParentPropertyDef) {
                    continue;
                }
                //AdsParentPropertyDef.isUnsageForSqml()  - проверка что есть наследование значения или кастомные геттеры в цепочке связей
                /*while (finalProperty instanceof AdsParentPropertyDef){
                 finalProperty = ((AdsParentPropertyDef)finalProperty).getParentInfo().findOriginalProperty();
                 }*/

                if (getColumnById(property.getId()) == null) {
                    columns.add(new SqmlColumnDefImpl(environment, (AdsServerSidePropertyDef) property, classDef.getId()));
                }
            }
        }
        fillColumns(classDef.findTable(classDef), classDef.getId());
    }
    
    private void fillColumns(final DdsTableDef tableDef, final Id ownerId) {
        final List<DdsColumnDef> ddsColumns = tableDef.getColumns().get(EScope.LOCAL_AND_OVERWRITE);
        for (DdsColumnDef ddsColumn : ddsColumns) {
            if (getColumnById(ddsColumn.getId()) == null && !ddsColumn.isHidden()) {
                columns.add(new SqmlColumnDefImpl(environment, ddsColumn, ownerId));
            }
        }
    }

    @Override
    public ISqmlColumnDef getColumnById(final Id columnId) {
        for (ISqmlColumnDef columnDef : columns) {
            if (columnDef.getId().equals(columnId)) {
                return columnDef;
            }
        }
        return null;
    }

    @Override
    public ISqmlColumnDef getColumnByName(final String columnName) {
        for (ISqmlColumnDef columnDef : columns) {
            if (columnDef.getShortName().equals(columnName)) {
                return columnDef;
            }
        }
        return null;
    }

    @Override
    public Iterator<ISqmlColumnDef> iterator() {
        return columns.iterator();
    }

    @Override
    public int size() {
        return columns.size();
    }

    @Override
    public ISqmlColumnDef get(final int idx) {
        return columns.get(idx);
    }

    @Override
    public List<ISqmlColumnDef> getAll() {
        return Collections.unmodifiableList(columns);
    }
}
