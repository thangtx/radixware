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

package org.radixware.kernel.common.client.meta.editorpages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;

/**
 * Страница редактирования, содержащая набор редакторов свойств.
 *
 */
public class RadStandardEditorPageDef extends RadEditorPageDef {

    public static final class PageItem {

        private final int row, column, columnSpan;
        private final boolean glueToLeftItem, glueToRightItem;
        private Id itemId;

        public PageItem(final Id itemId, final int column, final int row, final int columnSpan, final boolean glueToLeftItem, final boolean glueToRightItem) {
            this.itemId = itemId;
            this.row = row;
            this.column = column;
            this.columnSpan = columnSpan;
            this.glueToLeftItem = glueToLeftItem;
            this.glueToRightItem = glueToRightItem;
        }
        
        public PageItem(final Id itemId, final int column, final int row) {
            this(itemId, column, row, 1, false, false);
        }
        

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public Id getItemId() {
            return itemId;
        }

        public int getColumnSpan() {
            return columnSpan;
        }

        public boolean isGlueToLeftItem() {
            return glueToLeftItem;
        }

        public boolean isGlueToRightItem() {
            return glueToRightItem;
        }                
    }

    public static class PropertiesGroup extends TitledDefinition {

        private final List<PageItem> items;

        public PropertiesGroup(final Id id,
                final Id titleId,
                final Id titleOwnerId,//совпадает с titleOwnerId EditorPage
                final PageItem[] pageItems//в массиве содержатся идентификаторы свойств
                //или вложенных PropertiesGroup
                //в порядке следования на странице (в группе)
                ) {
            super(id, null/*name*/, titleId, titleOwnerId);
            if (pageItems != null && pageItems.length > 0) {
                this.items = new ArrayList<PageItem>();
                Collections.addAll(items, pageItems);
                Collections.sort(items, new Comparator<PageItem>() {

                    public int compare(PageItem pageItem1, PageItem pageItem2) {
                        if (pageItem1.getColumn() < pageItem2.getColumn()) {
                            return -1;
                        }
                        if (pageItem1.getColumn() > pageItem2.getColumn()) {
                            return 1;
                        }
                        if (pageItem1.getRow() < pageItem2.getRow()) {
                            return -1;
                        }
                        if (pageItem1.getRow() > pageItem2.getRow()) {
                            return 1;
                        }
                        return 0;
                    }
                });
            } else {
                this.items = Collections.emptyList();
            }
        }

        public List<PageItem> getPageItems() {
            return Collections.unmodifiableList(items);
        }

        public boolean isPropertyDefined(final Id propertyId) {
            for (PageItem item : items) {
                if (item.getItemId().equals(propertyId)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private final Map<Id, PropertiesGroup> groups;
    private final Id rootGroupId;

    public RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final PageItem[] pageItems//Вытащенные на страницу свойства с указанием строки и колонки
            ) {
        this(id, name, titleOwnerId, titleId, iconId, arrToMap(titleOwnerId, pageItems), null, 1, null);
    }

    public RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final PropertiesGroup[] groups,//Все группы свойств, объявленные на странице.
            //Вложенность и порядок следования значения не имеют.
            //Если на странице есть хотябы одно свойство, то в массиве должна
            //присутствовать корневая группа
            final Id rootPropertiesGroupId,//Идентификатор корневой группы
            final int columnsCount//Количество колонок на странице. (По умолчанию = 1)
            ) {
        this(id, name, titleOwnerId, titleId, iconId, arrToMap(groups), rootPropertiesGroupId, columnsCount, null);
    }
    
    private RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final Map<Id, PropertiesGroup> propertyGroups,
            final Id rootPropertyGroupId,
            final int columnsCount,
            final List<RadEditorPageDef> childPages){
        super(id, name, titleOwnerId, titleId, iconId, childPages);
        groups = propertyGroups;
        if (groups.size()==1){
            rootGroupId = groups.keySet().iterator().next();
        }else{
            rootGroupId = rootPropertyGroupId;
        }
        checkRootGroup();
    }
    
    private static Map<Id,PropertiesGroup> arrToMap(final PropertiesGroup[] arr){
        final Map<Id,PropertiesGroup> result = new HashMap<>(8);
        if (arr != null && arr.length > 0) {
            for (PropertiesGroup group : arr) {
                result.put(group.getId(), group);
            }
        }
        return result;
    }
    
    private static Map<Id,PropertiesGroup> arrToMap(final Id titleOwnerId, final PageItem[] arr){
        final Map<Id,PropertiesGroup> result = new HashMap<>(1);
        final Id groupId = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_PROPERTY_GROUP);
        final PropertiesGroup main = new PropertiesGroup(groupId, null, titleOwnerId, arr);
        result.put(groupId, main);
        return result;
    }    

    private void checkRootGroup() {
        if (rootGroupId == null) {
            if (!groups.isEmpty()) {
                throw new WrongFormatError("root properties group identifier was not defined in editor page " + toString());
            }
        } else if (!groups.containsKey(rootGroupId)) {
            throw new WrongFormatError("root properties group #" + rootGroupId + " was not found in editor page " + toString());
        }
    }

    public PropertiesGroup getPropertiesGroup(Id id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.EDITOR_PAGE_PROPERTIES_GROUP, id);
    }

    public PropertiesGroup getRootPropertiesGroup() {
        return rootGroupId != null ? getPropertiesGroup(rootGroupId) : null;
    }
    
    public boolean hasProperties(){
        for (PropertiesGroup group : groups.values()) {
            for (PageItem item : group.getPageItems()) {
                if (item.getItemId().getPrefix() != EDefinitionIdPrefix.ADS_PROPERTY_GROUP) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return !hasProperties() && !hasSubPages();
    }

    public boolean isPropertyDefined(Id propertyId) {
        for (PropertiesGroup group : groups.values()) {
            if (group.isPropertyDefined(propertyId)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    RadStandardEditorPageDef createCopyWithSubPages(final List<RadEditorPageDef> childPages){
        return new RadStandardEditorPageDef(getId(),
                                            getName(),
                                            classId,
                                            titleId,
                                            iconId,
                                            groups,
                                            rootGroupId,
                                            1,
                                            childPages);
    }    
}
