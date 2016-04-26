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

package org.radixware.kernel.common.client.models.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public final class SelectorColumns implements Iterable<SelectorColumnModelItem>{

    private final static String ALL_COLUMNS_KEY = "selectorColumns";
    private final static String VISIBLE_COLUMNS_KEY = "visibleColumns";

    private final Map<Id, SelectorColumnModelItem> columns = new HashMap<>();
    private final GroupModel group;
    private ArrStr  initialVisibleColumns;

    public SelectorColumns(final GroupModel group){
        this.group = group;
        final ClientSettings settings = group.getEnvironment().getConfigStore();
        settings.beginGroup(group.getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);

        try {
            //Проверка изменился ли набор колонок с прошлого раза
            String str = settings.readString(ALL_COLUMNS_KEY);
            ArrStr storedColumns = str == null ? null : ArrStr.fromValAsStr(str);

            ArrayList<String> actualColumns = new ArrayList<>();
            final RadSelectorPresentationDef selectorPresentation = group.getSelectorPresentationDef();
            for (RadSelectorPresentationDef.SelectorColumn column : selectorPresentation.getSelectorColumns()) {
                columns.put(column.getPropertyId(), column.newSelectorColumn(group));
                if (column.getVisibility() != ESelectorColumnVisibility.NEVER) {
                    actualColumns.add(column.getPropertyId().toString());
                }
            }

            if (storedColumns == null || storedColumns.size() != actualColumns.size()
                    || !actualColumns.containsAll(storedColumns)) {
                //Набор колонок изменился
                settings.remove("");
                storedColumns = new ArrStr();
                for (String columnId : actualColumns) {
                    storedColumns.add(columnId);
                }
                //Сохранили новый набор колонок изменился
                settings.writeString(ALL_COLUMNS_KEY, storedColumns.toString());
            } else {
                //Если набор колонок не изменился - восстанавливаем видимость колонок
                str = settings.readString(VISIBLE_COLUMNS_KEY);
                initialVisibleColumns = str == null ? null : ArrStr.fromValAsStr(str);
                if (initialVisibleColumns != null) {
                    for (Id columnId : columns.keySet()) {
                        columns.get(columnId).setVisible(initialVisibleColumns.contains(columnId.toString()));
                    }
                }
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }


    @Override
    public Iterator<SelectorColumnModelItem> iterator() {
        return columns.values().iterator();
    }

    public SelectorColumnModelItem getColumnByPropertyId(final Id propertyId){
        if (columns.containsKey(propertyId)) {
            return columns.get(propertyId);
        }
        throw new IllegalArgumentError("Selector column for property #" + propertyId + " does not exists or not accessible");
    }

    public void storeSettings() {
        final ArrStr visibleColumns = new ArrStr();
        for (Entry<Id, SelectorColumnModelItem> pair : columns.entrySet()) {
            final SelectorColumnModelItem item = pair.getValue();
            if (item.isVisible()) {
                visibleColumns.add(item.getId().toString());
            }
        }

        if (initialVisibleColumns==null ||
            initialVisibleColumns.size()!=visibleColumns.size() ||
            !initialVisibleColumns.containsAll(visibleColumns)){
            initialVisibleColumns = visibleColumns;
            final ClientSettings settings = group.getEnvironment().getConfigStore();
            settings.beginGroup(group.getConfigStoreGroupName());
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);
            try{
                settings.writeString(VISIBLE_COLUMNS_KEY, visibleColumns.toString());
            }
            finally{
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }
    }

}
