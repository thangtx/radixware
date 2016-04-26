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

package org.radixware.kernel.common.client.meta.explorerItems;

import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadUserSorting;
import org.radixware.kernel.common.client.meta.filters.RadContextFilter;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.userexploreritem.*;


public final class RadSelectorUserExplorerItemDef extends RadSelectorExplorerItemDef{
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static RadSelectorUserExplorerItemDef loadFromString(final IClientEnvironment environment,
                                                                    final RadSelectorExplorerItemDef sourceExplorerItem,
                                                                    final String explorerItemAsXmlStr) throws XmlException{
            final SelectorUserExplorerItemDocument document = 
                    SelectorUserExplorerItemDocument.Factory.parse(explorerItemAsXmlStr);
            final SelectorUserExplorerItem explorerItem = document.getSelectorUserExplorerItem();
            if (sourceExplorerItem==null){
                return new RadSelectorUserExplorerItemDef(explorerItem.getId(), 
                                                          explorerItem.getTitle(), 
                                                          explorerItem.getSourceId(),
                                                          explorerItemAsXmlStr);
            }else{
                final RadContextFilter filter = RadContextFilter.Factory.loadFromXml(explorerItem.getFilter());
                final RadSelectorPresentationDef presentationDef = sourceExplorerItem.getModelDefinition();                
                final RadSortingDef sortingDef;
                final Sorting sorting = explorerItem.getSorting();
                if (sorting==null){
                    sortingDef = null;
                }else{
                    final Id sortingId = explorerItem.getSorting().getId();
                    if (presentationDef.isSortingExists(sortingId)){
                        sortingDef = presentationDef.getSortingDefById(sortingId);
                    }else{
                        final RadUserSorting userSorting = 
                            RadUserSorting.Factory.newInstance(environment, presentationDef.getOwnerClassId(), sorting.getTitle());
                        final List<RadSortingDef.SortingItem> sortingItems = new LinkedList<>();
                        for (Sorting.Order.Item sortingItem: sorting.getOrder().getItemList()){
                            sortingItems.add(new RadSortingDef.SortingItem(sortingItem.getColumnId(), sortingItem.getDesc()));
                        }
                        userSorting.setSortingColumns(environment, sortingItems);
                        sortingDef = userSorting.isValid() ? userSorting : null;
                    }
                }
                return new RadSelectorUserExplorerItemDef(explorerItem.getId(), 
                                                          explorerItem.getTitle(), 
                                                          explorerItem.getSourceId(),
                                                          sourceExplorerItem, 
                                                          filter,
                                                          sortingDef, 
                                                          explorerItemAsXmlStr);
           }        
        }
        
        public static RadSelectorUserExplorerItemDef newInstance(final String title,
                                                                 final RadSelectorExplorerItemDef sourceItem,
                                                                 final RadContextFilter filter,
                                                                 final RadSortingDef initialSorting){
            RadSelectorExplorerItemDef targetExplorerItem = sourceItem;
            if (targetExplorerItem instanceof RadSelectorUserExplorerItemDef){
                targetExplorerItem = ((RadSelectorUserExplorerItemDef)targetExplorerItem).getTargetExplorerItem();
            }
            if (targetExplorerItem==null){
                throw new IllegalArgumentException("Can't find target explorer item");            
            }            
            final RadSelectorUserExplorerItemDef item = 
                new RadSelectorUserExplorerItemDef(Id.Factory.newInstance(EDefinitionIdPrefix.USER_EXPLORER_ITEM),
                                                      title,
                                                      sourceItem.getId(),
                                                      targetExplorerItem,
                                                      filter,
                                                      initialSorting,
                                                      null);                    
            item.linkWithSourceExplorerItem(sourceItem);
            return item;
        }        
    }
    
    private final RadSelectorExplorerItemDef targetItem;
    private final Id sourceItemId;
    private final String title;
    private final RadContextFilter contextFilter;
    private final RadSortingDef sorting;
    private RadSelectorExplorerItemDef sourceExplorerItem;
    private String asXml;

    private RadSelectorUserExplorerItemDef(final Id id,
                                           final String title,
                                           final Id sourceItemId,
                                           final RadSelectorExplorerItemDef sourceItem,
                                           final RadContextFilter filter,
                                           final RadSortingDef initialSorting,
                                           final String asXmlString
                                           ) {
        super(id,
              null,//titleOwnerId
              null,//titleId
              sourceItem.getModelDefinitionClassId(),//targetClassId
              sourceItem.getModelDefinitionId(),//presentationId
              0,//restrictionsMask
              null,//enabledCommands
              0,//inheritanceMask
              true//visible
            );
        this.targetItem = sourceItem;
        this.sourceItemId = sourceItemId;
        this.title = title;
        this.contextFilter = filter;
        this.sorting = initialSorting;
        asXml = asXmlString;
    }
    
    private RadSelectorUserExplorerItemDef(final Id id, 
                                           final String title, 
                                           final Id sourceItemId,
                                           final String asXml){
        super(id,
              null,//titleOwnerId
              null,//titleId
              null,//targetClassId
              null,//presentationId
              0,//restrictionsMask
              null,//enabledCommands
              0,//inheritanceMask
              true//visible
            );
        this.targetItem = null;
        this.sourceItemId = sourceItemId;
        this.title = title;
        this.contextFilter = null;
        this.sorting = null;
        this.asXml = asXml;
    }        
    
    public Id getSourceExplorerItemId(){
        return sourceExplorerItem==null ? sourceItemId : sourceExplorerItem.getId();
    }
    
    public RadSelectorExplorerItemDef getTargetExplorerItem(){
        return targetItem;
    }
    
    public RadSelectorExplorerItemDef getSourceExplorerItem(){
        return sourceExplorerItem==null ? targetItem : sourceExplorerItem;
    }
    
    public void linkWithSourceExplorerItem(final RadSelectorExplorerItemDef item){        
        sourceExplorerItem = item;
        if (!sourceExplorerItem.getId().equals(sourceItemId)){
            asXml = null;
        }
    }

    @Override
    public Restrictions getRestrictions() {
        return targetItem.getRestrictions();
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "user explorer item %s for open selector presentation #%s in class #%s");
        return String.format(desc, toString(), getModelDefinitionId(), getModelDefinitionClassId());
    }

    @Override
    public boolean isValid() {        
        return targetItem!=null && targetItem.isValid() && contextFilter!=null;
    }

    @Override
    public boolean hasTitle() {
        return title!=null && !title.isEmpty();
    }    

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Icon getIcon() {
        return targetItem.getIcon();
    }

    @Override
    public RadContextFilter getContextFilter(){
        return contextFilter;
    }

    @Override
    public RadSortingDef getInitialSorting() {
        return sorting;
    }        
    
    public String saveToString(){
        if (asXml==null){
            final SelectorUserExplorerItemDocument document =
                SelectorUserExplorerItemDocument.Factory.newInstance();
            final SelectorUserExplorerItem userei = document.addNewSelectorUserExplorerItem();
            userei.setId(getId());
            userei.setSourceId(getSourceExplorerItemId());
            userei.setTitle(title);
            if (contextFilter!=null){
                contextFilter.writeToXml(userei.addNewFilter());
            }
            if (sorting!=null){
                final Sorting xmlSorting = userei.addNewSorting();
                xmlSorting.setId(sorting.getId());
                xmlSorting.setTitle(sorting.getTitle());
                final Sorting.Order sortingOrder = xmlSorting.addNewOrder();
                for (RadSortingDef.SortingItem item: sorting.getSortingColumns()){
                    final Sorting.Order.Item sortingOrderItem = sortingOrder.addNewItem();
                    sortingOrderItem.setColumnId(item.propId);
                    if (item.sortDesc){
                        sortingOrderItem.setDesc(true);
                    }
                }
            }
            asXml=document.xmlText();
        }
        return asXml;
    }
}
