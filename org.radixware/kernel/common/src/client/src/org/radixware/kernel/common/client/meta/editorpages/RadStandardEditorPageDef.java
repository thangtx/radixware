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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
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
        private final boolean checkable;
        private final boolean frameIsVisible;

        public PropertiesGroup(final Id id,
                final Id titleId,
                final Id titleOwnerId,//совпадает с titleOwnerId EditorPage
                final PageItem[] pageItems//в массиве содержатся идентификаторы свойств
                //или вложенных PropertiesGroup
                //в порядке следования на странице (в группе)
                ) {
            this(id,null/*name*/,titleId,titleOwnerId,false,true,pageItems);
        }
        
        public PropertiesGroup(final Id id,
                final String name,
                final Id titleId,
                final Id titleOwnerId,//совпадает с titleOwnerId EditorPage
                final boolean checkable,//позволяет добавить checkbox в заголовке, который устанавливает доступность содержимого
                final boolean frameIsVisible,
                final PageItem[] pageItems//в массиве содержатся идентификаторы свойств
                //или вложенных PropertiesGroup
                //в порядке следования на странице (в группе)
                ) {
            super(id, name, titleOwnerId, titleId);
            this.checkable = checkable;
            this.frameIsVisible = frameIsVisible;
            if (pageItems != null && pageItems.length > 0) {
                this.items = new ArrayList<>();
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
        
        public boolean isFrameVisible(){
            return frameIsVisible;
        }
        
        public boolean hasProperties(){
            for (PageItem item : items) {
                if (item.getItemId().getPrefix() != EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP) {
                    return true;
                }
            } 
            return false;
        }
    }
    
    private final Map<Id, PropertiesGroup> groups;
    private final PropertiesGroup rootPropertiesGroup;

    public RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final PageItem[] pageItems//Вытащенные на страницу свойства с указанием строки и колонки
            ) {
        this(id, name, titleOwnerId, titleId, iconId, createRootPropertiesGroup(titleOwnerId, pageItems), null, null);
    }

    public RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,            
            final PageItem[] pageItems,//Вытащенные на страницу свойства с указанием строки и колонки
            final PropertiesGroup[] groups//Все группы свойств, объявленные на странице.
            //Вложенность и порядок следования значения не имеют.            
            ) {
        this(id, name, titleOwnerId, titleId, iconId, createRootPropertiesGroup(titleOwnerId, pageItems), arrToMap(groups), null);
    }
    
    private RadStandardEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final PropertiesGroup rootPropertyGroup,            
            final Map<Id, PropertiesGroup> childPropertiesGroups,
            final List<RadEditorPageDef> childPages){
        super(id, name, titleOwnerId, titleId, iconId, childPages);
        this.rootPropertiesGroup = rootPropertyGroup;
        if (childPropertiesGroups==null){
            groups = new HashMap<>();
        }else{
            groups = childPropertiesGroups;
        }
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
    
    private static PropertiesGroup createRootPropertiesGroup(final Id titleOwnerId, final PageItem[] items){        
        final Id groupId = Id.Factory.newInstance(EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP);
        return new PropertiesGroup(groupId, null, titleOwnerId, items);
    }

    public PropertiesGroup getPropertiesGroup(final Id id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        if (rootPropertiesGroup!=null && rootPropertiesGroup.getId().equals(id)){
            return rootPropertiesGroup;
        }
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.EDITOR_PAGE_PROPERTIES_GROUP, id);
    }
    
    public boolean isPropertyGroupExists(final Id id){
        return groups.containsKey(id) || (rootPropertiesGroup!=null && rootPropertiesGroup.getId().equals(id));
    }
    
    public Collection<PropertiesGroup> getPropertiesGroups(){
        return groups.values();
    }

    public PropertiesGroup getRootPropertiesGroup() {
        return rootPropertiesGroup;
    }
    
    public boolean hasProperties(){
        for (PropertiesGroup group : groups.values()) {
            if (group.hasProperties()){
                return true;
            }
        }
        return rootPropertiesGroup==null ? false : rootPropertiesGroup.hasProperties();
    }

    public boolean isEmpty() {
        return !hasProperties() && !hasSubPages();
    }

    public boolean isPropertyDefined(final Id propertyId) {
        for (PropertiesGroup group : groups.values()) {
            if (group.isPropertyDefined(propertyId)) {
                return true;
            }
        }
        return rootPropertiesGroup==null ? false : rootPropertiesGroup.isPropertyDefined(propertyId);
    }
    
    @Override
    RadStandardEditorPageDef createCopyWithSubPages(final List<RadEditorPageDef> childPages){
        return new RadStandardEditorPageDef(getId(),
                                            getName(),
                                            classId,
                                            titleId,
                                            iconId,
                                            rootPropertiesGroup,
                                            groups,
                                            childPages);
    }    
}
