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

package org.radixware.kernel.common.client.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public final class UserExplorerItemsStorage{
        
    private static class UserExplorerItemsCacheItem{

        private Map<Id,List<RadSelectorUserExplorerItemDef>> itemsBySourceItemId;
        private Map<Id,RadSelectorUserExplorerItemDef> itemsBySelfId;

        public UserExplorerItemsCacheItem(){
            itemsBySourceItemId = null;
            itemsBySelfId = null;
        }

        public UserExplorerItemsCacheItem(final RadSelectorUserExplorerItemDef explorerItem){
            itemsBySourceItemId = new HashMap<>(16);            
            final List<RadSelectorUserExplorerItemDef> list = new LinkedList<>();            
            list.add(explorerItem);
            itemsBySourceItemId.put(explorerItem.getTargetExplorerItem().getId(), list);
            itemsBySelfId = new HashMap<>(16);
            itemsBySelfId.put(explorerItem.getId(), explorerItem);
        }
        
        public UserExplorerItemsCacheItem(final Id sourceItemId, final List<RadSelectorUserExplorerItemDef> explorerItems){
            itemsBySourceItemId = new HashMap<>(16);
            itemsBySourceItemId.put(sourceItemId, explorerItems);
            itemsBySelfId = new HashMap<>(16);
            for (RadSelectorUserExplorerItemDef explorerItem: explorerItems){
                itemsBySelfId.put(explorerItem.getId(), explorerItem);
            }
        }          

        public List<RadSelectorUserExplorerItemDef> getUserExplorerItemsBySourceItemId(final Id sourceItemId){
            if (itemsBySourceItemId==null){
                return Collections.emptyList();
            }else{
                final List<RadSelectorUserExplorerItemDef> items = itemsBySourceItemId.get(sourceItemId);
                return items==null ? Collections.<RadSelectorUserExplorerItemDef>emptyList() : items;
            }
        }
        
        public RadSelectorUserExplorerItemDef findUserExplorerItemById(final Id explorerItemId){
            return itemsBySelfId==null ? null : itemsBySelfId.get(explorerItemId);
        }        

        public List<Id> putUserExplorerItem(final RadSelectorUserExplorerItemDef explorerItem){
            final List<Id> result;
            if (itemsBySourceItemId==null){
                itemsBySourceItemId = new HashMap<>(16);
                final List<RadSelectorUserExplorerItemDef> list = new LinkedList<>();
                list.add(explorerItem);
                itemsBySourceItemId.put(explorerItem.getTargetExplorerItem().getId(), list);
                result = Collections.singletonList(explorerItem.getId());
            }else{
                final Id targetItemId = explorerItem.getTargetExplorerItem().getId();
                List<RadSelectorUserExplorerItemDef> list = itemsBySourceItemId.get(targetItemId);
                if (list==null){
                    list = new LinkedList<>();
                    list.add(explorerItem);
                    itemsBySourceItemId.put(targetItemId, list);
                    result = Collections.singletonList(explorerItem.getId());
                }else{                    
                    final List<Id> itemIds = new LinkedList<>();
                    for (RadSelectorUserExplorerItemDef item: list){
                        itemIds.add(item.getId());
                    }
                    final int index = itemIds.indexOf(explorerItem.getSourceExplorerItemId());
                    if (index>-1){                        
                        list.add(index+1,explorerItem);
                        itemIds.add(index+1,explorerItem.getId());
                    }else{
                        list.add(explorerItem);
                        itemIds.add(explorerItem.getId());
                    }
                    result = Collections.unmodifiableList(itemIds);
                }
            }
            if (itemsBySelfId==null){
                itemsBySelfId = new HashMap<>(16);                
            }                        
            itemsBySelfId.put(explorerItem.getId(), explorerItem);
            return result;
        }
        
        public void putAllUserExplorerItems(final Id sourceItemId, final List<RadSelectorUserExplorerItemDef> explorerItems){
            if (itemsBySourceItemId==null){
                itemsBySourceItemId = new HashMap<>(16);
                itemsBySourceItemId.put(sourceItemId, explorerItems);
            }else{
                List<RadSelectorUserExplorerItemDef> list = itemsBySourceItemId.get(sourceItemId);
                if (list==null){
                    itemsBySourceItemId.put(sourceItemId, explorerItems);
                }else{
                    list.addAll(explorerItems);
                }
            }
            if (itemsBySelfId==null){
                itemsBySelfId = new HashMap<>(16);                
            }                        
            for (RadSelectorUserExplorerItemDef explorerItem: explorerItems){
                itemsBySelfId.put(explorerItem.getId(), explorerItem);
            }
        }
        

        public List<Id> removeUserExplorerItem(final Id sourceItemId, final Id explorerItemId){
            final List<Id> leftItemIds;
            if (itemsBySourceItemId!=null){
                final List<RadSelectorUserExplorerItemDef> list = itemsBySourceItemId.get(sourceItemId);
                if (list!=null){
                    leftItemIds = new LinkedList<>();
                    for (int i=list.size()-1; i>=0; i--){
                        if (list.get(i).getId().equals(explorerItemId)){
                            list.remove(i);                            
                        }else{
                            leftItemIds.add(0, list.get(i).getId());
                        }
                    }                    
                }else{
                    leftItemIds = Collections.emptyList();
                }
            }else{
                leftItemIds = Collections.emptyList();
            }
            if (itemsBySelfId!=null){
                itemsBySelfId.remove(explorerItemId);
            }
            return leftItemIds;
        } 
    }
    
    private static class UserExplorerItemsCache{
        
        private final static UserExplorerItemsCacheItem EMPTY_ITEM = new UserExplorerItemsCacheItem();
        
        private final Map<Id,UserExplorerItemsCacheItem> itemsByOwnerDefId = new HashMap<>(16);        
        
        private UserExplorerItemsCache(){
        }
        
        public List<RadSelectorUserExplorerItemDef> getUserExplorerItems(final Id ownerDefinitionId, final Id baseExplorerItemId){
            final UserExplorerItemsCacheItem cacheItem  = itemsByOwnerDefId.get(ownerDefinitionId);
            return cacheItem==null ? null : cacheItem.getUserExplorerItemsBySourceItemId(baseExplorerItemId);
        }
        
        public RadSelectorUserExplorerItemDef findUserExplorerItemById(final Id ownerDefinitionId, final Id explorerItemId){
            final UserExplorerItemsCacheItem cacheItem = 
                itemsByOwnerDefId==null ? null : itemsByOwnerDefId.get(ownerDefinitionId);
            return cacheItem == null ? null : cacheItem.findUserExplorerItemById(explorerItemId);
        }
        
        public boolean isUserExplorerItemsCached(final Id ownerDefinitionId){
            return itemsByOwnerDefId.containsKey(ownerDefinitionId);
        }
        
        public List<Id> putUserExplorerItem(final Id ownerDefinitionId, final RadSelectorUserExplorerItemDef explorerItem){
            final UserExplorerItemsCacheItem cacheItem  = itemsByOwnerDefId.get(ownerDefinitionId);
            if (cacheItem==null || cacheItem==EMPTY_ITEM){
                itemsByOwnerDefId.put(ownerDefinitionId, new UserExplorerItemsCacheItem(explorerItem));
                return Collections.singletonList(explorerItem.getId());
            }else{
                return cacheItem.putUserExplorerItem(explorerItem);
            }            
        }
        
        public void putAllUserExplorerItems(final Id ownerDefinitionId, final Id baseExplorerItemId, final List<RadSelectorUserExplorerItemDef> explorerItems){
            final UserExplorerItemsCacheItem cacheItem  = itemsByOwnerDefId.get(ownerDefinitionId);
            if (cacheItem==null || cacheItem==EMPTY_ITEM){
                itemsByOwnerDefId.put(ownerDefinitionId, new UserExplorerItemsCacheItem(baseExplorerItemId, explorerItems));
            }else{
                cacheItem.putAllUserExplorerItems(baseExplorerItemId, explorerItems);
            }
        }
        
        public void putEmptyItem(final Id ownerDefinitionId){
            itemsByOwnerDefId.put(ownerDefinitionId, EMPTY_ITEM);            
        }
        
        public List<Id> removeUserExplorerItem(final Id ownerDefinitionId, final Id baseExplorerItemId, final Id explorerItemId){
            final UserExplorerItemsCacheItem cacheItem  = itemsByOwnerDefId.get(ownerDefinitionId);
            if (cacheItem!=null && cacheItem!=EMPTY_ITEM){
                return cacheItem.removeUserExplorerItem(baseExplorerItemId, explorerItemId);
            }else{
                return Collections.emptyList();
            }
        }
        
        public void clear(){
            itemsByOwnerDefId.clear();
        }
    }
    
    private static interface UserExplorerItemLoadHandler{
        void onLoadUserExplorerItem(final Id sourceExplorerItemId, final RadSelectorUserExplorerItemDef item);
    }
    
    private final static String CUSTOM_EXPLORER_ITEMS_SETTING_NAME = SettingNames.SYSTEM+"/CustomExplorerItems";    
    private final static String ITEMS_ORDER_KEY_NAME = "items_order";
    private final static Map<IClientEnvironment,UserExplorerItemsStorage> EXPLORER_ITEMS_CACHE = new WeakHashMap<>(16);
    
    private final static Object SEMAPHORE = new Object();
    
    private final UserExplorerItemsCache cache = new UserExplorerItemsCache();
    private final List<Id> loadedExplorerItems = new LinkedList<>();
    private final List<String> loadedUserExplorerItems = new LinkedList<>();
                
    private final IClientEnvironment environment;    
    private final IClientEnvironment.ConnectionListener disconnectListener = new IClientEnvironment.ConnectionListener() {
            @Override
            public void afterCloseConnection(boolean forced) {
                loadedExplorerItems.clear();
                loadedUserExplorerItems.clear();
                cache.clear();
            }
        };
    
    private UserExplorerItemsStorage(final IClientEnvironment environment){
        this.environment = environment;
        this.environment.addConnectionListener(disconnectListener);
    }
    
    public List<RadSelectorUserExplorerItemDef> findUserExplorerItemsForSourceExplorerItem(final Id ownerDefinitionId, final Id baseExplorerItemId, final RadExplorerItems predefinedItems) {
        List<RadSelectorUserExplorerItemDef> explorerItemsList = cache.getUserExplorerItems(ownerDefinitionId, baseExplorerItemId);
        if ((explorerItemsList==null || explorerItemsList.isEmpty()) && !isUserExplorerItemsLoaded(ownerDefinitionId)){
            final List<RadSelectorUserExplorerItemDef> resultList = new LinkedList<>();
            final UserExplorerItemLoadHandler handler = new UserExplorerItemLoadHandler() {
                @Override
                public void onLoadUserExplorerItem(final Id sourceExplorerItemId, final RadSelectorUserExplorerItemDef item) {
                    if (sourceExplorerItemId.equals(baseExplorerItemId)){
                        resultList.add(item);
                    }
                }
            };
            if (loadUserExplorerItems(ownerDefinitionId, predefinedItems, handler)){
                if (resultList.isEmpty()){
                    cache.putAllUserExplorerItems(ownerDefinitionId, baseExplorerItemId, new LinkedList<RadSelectorUserExplorerItemDef>());
                }
            }else{
                cache.putEmptyItem(ownerDefinitionId);
            }
            return resultList;
        }
        return explorerItemsList;
    }    
    
    private boolean isUserExplorerItemsLoaded(final Id ownerDefinitionId){
        return loadedExplorerItems.contains(ownerDefinitionId);
    }
    
    private boolean isUserExplorerItemsLoaded(final Id ownerDefinitionId, final String sourceExplorerItemId){
        return loadedUserExplorerItems.contains(ownerDefinitionId.toString()+"-"+sourceExplorerItemId);
    }
    
    private boolean loadUserExplorerItems(final Id ownerDefinitionId, final RadExplorerItems predefinedItems, final UserExplorerItemLoadHandler loadHandler){
        final ClientSettings settings = environment.getConfigStore();        
        settings.beginGroup(CUSTOM_EXPLORER_ITEMS_SETTING_NAME+"/"+ownerDefinitionId.toString()); 
        try{
            final List<String> sourceExplorerItemIds = settings.childGroups();
            if (sourceExplorerItemIds==null || sourceExplorerItemIds.isEmpty()){
                cache.putEmptyItem(ownerDefinitionId);
                loadedExplorerItems.add(ownerDefinitionId);
                return false;
            }else{
                boolean allItemsLoaded = true;
                List<RadSelectorUserExplorerItemDef> resultList;
                final List<String> loadedSourceExplorerItems = new LinkedList<>();
                for (String IdAsStr: sourceExplorerItemIds){
                    if (!isUserExplorerItemsLoaded(ownerDefinitionId, IdAsStr)){
                        final Id explorerItemId = Id.Factory.loadFrom(IdAsStr);
                        final RadExplorerItemDef explorerItem = predefinedItems.findExplorerItem(explorerItemId);
                        if (explorerItem instanceof RadSelectorExplorerItemDef){
                            resultList = 
                                readUserExplorerItems(settings, (RadSelectorExplorerItemDef)explorerItem, loadHandler);                            
                            linkUserExplorerItems(resultList, (RadSelectorExplorerItemDef)explorerItem);
                            cache.putAllUserExplorerItems(ownerDefinitionId, explorerItemId, resultList);
                            loadedSourceExplorerItems.add(IdAsStr);
                        }else {
                            final List<RadSelectorUserExplorerItemDef> cachedExplorerItems = 
                                cache.getUserExplorerItems(ownerDefinitionId, explorerItemId);
                            if (cachedExplorerItems==null || cachedExplorerItems.isEmpty()){
                                allItemsLoaded = false;
                            }
                        }
                    }
                }
                if (allItemsLoaded){
                    loadedExplorerItems.add(ownerDefinitionId);
                }else{
                    for (String IdAsStr: loadedSourceExplorerItems){
                        loadedUserExplorerItems.add(ownerDefinitionId.toString()+"-"+IdAsStr);
                    }
                }
                return true;
            }
        }finally{
            settings.endGroup();
        }        
    }
    
    private void linkUserExplorerItems(final List<RadSelectorUserExplorerItemDef> userExplorerItems, final RadSelectorExplorerItemDef targetExplorerItem){
        final Map<Id,RadSelectorUserExplorerItemDef> userExplorerItemsById = new HashMap<>();
        for (RadSelectorUserExplorerItemDef item: userExplorerItems){
            userExplorerItemsById.put(item.getId(), item);
        }
        for (RadSelectorUserExplorerItemDef item: userExplorerItems){
            final Id sourceItemId = item.getSourceExplorerItemId();
            if (targetExplorerItem.getId().equals(sourceItemId) || !userExplorerItemsById.containsKey(sourceItemId)){
                item.linkWithSourceExplorerItem(targetExplorerItem);
            }else{
                item.linkWithSourceExplorerItem(userExplorerItemsById.get(sourceItemId));
            }
        }
    }
    
    private List<RadSelectorUserExplorerItemDef> readUserExplorerItems(final ClientSettings settings, final RadSelectorExplorerItemDef sourceExplorerItem, final UserExplorerItemLoadHandler loadHandler){
        final List<RadSelectorUserExplorerItemDef> result = new LinkedList<>();
        final ArrStr itemsOrder;
        settings.beginGroup(sourceExplorerItem.getId().toString());
        try{
            final List<String> keys = settings.childKeys();            
            if (keys!=null && !keys.isEmpty()){
                String itemsOrderAsStr = null;
                for (String key: keys){
                    if (ITEMS_ORDER_KEY_NAME.equals(key)){
                        itemsOrderAsStr = settings.readString(key);
                    }
                    else{
                        final String itemAsStr = settings.readString(key);
                        if (itemAsStr!=null && !itemAsStr.isEmpty()){
                            final RadSelectorUserExplorerItemDef explorerItem;
                            try{
                                explorerItem = 
                                    RadSelectorUserExplorerItemDef.Factory.loadFromString(environment, sourceExplorerItem, itemAsStr);
                            }catch(Exception exception){
                                final String traceMessageTemplate = 
                                    environment.getMessageProvider().translate("TraceMessage", "Failed to load user explorer item: %1$s");
                                final ExceptionMessage message = new ExceptionMessage(environment,exception);
                                final String traceMessage = String.format(traceMessageTemplate, message.getDialogMessage());                                
                                environment.getTracer().error(traceMessage,exception);
                                continue;
                            }
                            if (loadHandler!=null){
                                loadHandler.onLoadUserExplorerItem(sourceExplorerItem.getId(), explorerItem);
                            }
                            result.add(explorerItem);
                        }
                    }
                }//for
                if (itemsOrderAsStr!=null && !itemsOrderAsStr.isEmpty()){
                    itemsOrder = ArrStr.fromValAsStr(itemsOrderAsStr);
                }else{
                    itemsOrder = null;
                }
            }else{
                itemsOrder = null;
            }
        }finally{
            settings.endGroup();
        }
        if (result.size()>1 && itemsOrder!=null){
            final Comparator<RadSelectorUserExplorerItemDef> itemsComparator = new Comparator<RadSelectorUserExplorerItemDef>() {
                @Override
                public int compare(RadSelectorUserExplorerItemDef item1, RadSelectorUserExplorerItemDef item2) {
                    final int index1 = itemsOrder.indexOf(item1.getId().toString());
                    final int index2 = itemsOrder.indexOf(item2.getId().toString());
                    if (index2<0){
                        return 1;
                    }else if (index1<0){
                        return -1;
                    }else{
                        return Integer.compare(index1, index2);
                    }
                }
            };
            Collections.sort(result, itemsComparator);
        }
        return result;
    }
    
    public RadSelectorUserExplorerItemDef findUserExplorerItemById(final Id ownerDefinitionId, final RadExplorerItems predefinedItems, final Id explorerItemId){
        if (cache.isUserExplorerItemsCached(ownerDefinitionId)){
            final RadSelectorUserExplorerItemDef explorerItem = 
                cache.findUserExplorerItemById(ownerDefinitionId, explorerItemId);
            if (explorerItem!=null){
                return explorerItem;
            }
        }
        if (!isUserExplorerItemsLoaded(ownerDefinitionId)){
            final List<RadSelectorUserExplorerItemDef> result = new LinkedList<>();
            final UserExplorerItemLoadHandler handler = new UserExplorerItemLoadHandler() {
                @Override
                public void onLoadUserExplorerItem(final Id sourceExplorerItemId, final RadSelectorUserExplorerItemDef item) {
                    if (item.getId().equals(explorerItemId)){
                        result.add(item);
                    }
                }
            };
            loadUserExplorerItems(ownerDefinitionId, predefinedItems, handler);
            return result.isEmpty() ? null : result.get(0);
        }
        return null;
    }    
    
    public static Id getContextId(final IExplorerItemsHolder explorerItemsHolder){
        if (explorerItemsHolder instanceof RadEditorPresentationDef){
            return ((RadEditorPresentationDef)explorerItemsHolder).getTableId();
        }else{
            return explorerItemsHolder.getId();
        }
    }
    
    private boolean canSaveUserExplorerItem(){
        final ClientSettings settings = environment.getConfigStore();
        if (settings.isWritable()){
            final StringBuilder groupName = new StringBuilder();
            groupName.append(SettingNames.SYSTEM);
            groupName.append("/");
            groupName.append(SettingNames.EXPLORER_TREE_GROUP);
            groupName.append("/");
            groupName.append(SettingNames.ExplorerTree.COMMON_GROUP);
            settings.beginGroup(groupName.toString());
            try{
                return settings.readBoolean(SettingNames.ExplorerTree.Common.KEEP_USER_EI, true);                    
            }finally{
                settings.endGroup();
            }                
        }else{
            return false;
        }
    }

    public void registerUserExplorerItem(final Id ownerDefinitionId, final RadSelectorUserExplorerItemDef explorerItem){        
        final boolean writeable = canSaveUserExplorerItem();
        final StringBuilder groupName = new StringBuilder();
        final ClientSettings settings = environment.getConfigStore();
        if (writeable){
            groupName.append(CUSTOM_EXPLORER_ITEMS_SETTING_NAME);
            groupName.append("/");
            groupName.append(ownerDefinitionId.toString());
            groupName.append("/");
            groupName.append(explorerItem.getTargetExplorerItem().getId().toString());
            settings.beginGroup(groupName.toString());
            try{
                settings.writeString(explorerItem.getId().toString(), explorerItem.saveToString());
            }finally{
                settings.endGroup();
            }
        }
        final List<Id> explorerItemIds = cache.putUserExplorerItem(ownerDefinitionId, explorerItem);
        if (writeable && !explorerItemIds.isEmpty()){
            final ArrStr explorerItemsOrder = new ArrStr();
            for (Id itemId: explorerItemIds){
                explorerItemsOrder.add(itemId.toString());
            }
            settings.beginGroup(groupName.toString());
            try{
                settings.writeString(ITEMS_ORDER_KEY_NAME, explorerItemsOrder.toString());
            }finally{
                settings.endGroup();
            }
        }
    }
    
    public void removeUserExplorerItem(final Id ownerDefinitionId, final Id explorerItemId, final String settingsGroupName){
        final RadSelectorUserExplorerItemDef explorerItemDef = 
                cache.findUserExplorerItemById(ownerDefinitionId, explorerItemId);
        if (explorerItemDef!=null){            
            final Id targerExplorerItemId = explorerItemDef.getTargetExplorerItem().getId();
            final List<RadSelectorUserExplorerItemDef> relinkedExplorerItems = 
                relinkExplorerItems(cache.getUserExplorerItems(ownerDefinitionId, targerExplorerItemId), 
                                    explorerItemDef, explorerItemDef.getSourceExplorerItem());
            final ClientSettings settings = environment.getConfigStore();
            final boolean writeable = settings.isWritable();
            final StringBuilder groupName = new StringBuilder();
            if (writeable){
                groupName.append(CUSTOM_EXPLORER_ITEMS_SETTING_NAME);
                groupName.append("/");
                groupName.append(ownerDefinitionId.toString());
                groupName.append("/");
                groupName.append(targerExplorerItemId.toString());
                settings.beginGroup(groupName.toString());
                try{
                    if (settings.contains(explorerItemId.toString())){
                        settings.remove(explorerItemId.toString());
                    }
                    for (RadSelectorUserExplorerItemDef changedItem: relinkedExplorerItems){
                        settings.setValue(changedItem.getId().toString(), changedItem.saveToString());
                    }

                }finally{
                    settings.endGroup();
                }
            }
            final List<Id> newItemsOrder = 
                cache.removeUserExplorerItem(ownerDefinitionId, targerExplorerItemId, explorerItemId);
            settings.beginGroup(groupName.toString());
            if (writeable){
                try{
                    if (newItemsOrder.isEmpty()){
                        settings.remove(ITEMS_ORDER_KEY_NAME);
                    }else{
                        final ArrStr explorerItemsOrder = new ArrStr();
                        for (Id itemId: newItemsOrder){
                            explorerItemsOrder.add(itemId.toString());
                        }
                        settings.setValue(ITEMS_ORDER_KEY_NAME, explorerItemsOrder.toString());
                    }            
                }finally{
                    settings.endGroup();
                }
                if (settingsGroupName!=null){
                    settings.beginGroup(settingsGroupName);
                    settings.remove("");
                    settings.endGroup();
                }                
            }
        }
    }
    
    private List<RadSelectorUserExplorerItemDef> relinkExplorerItems(final List<RadSelectorUserExplorerItemDef> items, 
                                                                     final RadSelectorUserExplorerItemDef currentSourceItem,
                                                                     final RadSelectorExplorerItemDef newSourceItem){
        final List<RadSelectorUserExplorerItemDef> relinkedExplorerItems = new LinkedList<>();
        for (RadSelectorUserExplorerItemDef item: items){
            if (item.getSourceExplorerItemId().equals(currentSourceItem.getId())){
                item.linkWithSourceExplorerItem(newSourceItem);
                relinkedExplorerItems.add(item);
            }
        }
        return relinkedExplorerItems;
    }
    
    public static UserExplorerItemsStorage getInstance(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            UserExplorerItemsStorage storage = EXPLORER_ITEMS_CACHE.get(environment);
            if (storage==null){
                storage = new UserExplorerItemsStorage(environment);
                EXPLORER_ITEMS_CACHE.put(environment, storage);
            }
            return storage;
        }            
    }
    
    public static void clearCache(final IClientApplication application){
        synchronized(SEMAPHORE){
            final List<IClientEnvironment> environments = new LinkedList<>();
            for (IClientEnvironment environment: EXPLORER_ITEMS_CACHE.keySet()){
                if (environment.getApplication()==application){
                    environments.add(environment);
                }
            }
            for (IClientEnvironment environment: environments){
                EXPLORER_ITEMS_CACHE.remove(environment);
            }
        }
    }
    
    public static void clearCache(final IClientEnvironment environment){
        synchronized(SEMAPHORE){
            EXPLORER_ITEMS_CACHE.remove(environment);
        }
    }
}
