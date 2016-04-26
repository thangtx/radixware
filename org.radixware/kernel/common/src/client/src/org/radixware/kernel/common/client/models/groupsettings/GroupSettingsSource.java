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

package org.radixware.kernel.common.client.models.groupsettings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.types.Id;


abstract class GroupSettingsSource<T extends IGroupSetting, V extends Definition> implements IGroupSettingsSource<T>{
    
    private final static int LAST_USED_LIMIT = 10;
    private final static String LAST_USED_SETTING_NAME = "lastUsed";    
    
    final IClientEnvironment environment;
    final String settingsContext;
    final String settingsType;
    final GroupSettingsCache<V> settingsCache;
    
    private Map<Id,V> customDefinitionsById;
    
    public GroupSettingsSource(final IClientEnvironment environment, 
                               final String settingsContext, 
                               final String settingsType, 
                               final GroupSettingsCache<V> cache){
        this.environment = environment;
        this.settingsContext = settingsContext;
        this.settingsType = settingsType;
        settingsCache = cache;
    }

    @Override
    public List<Id> getLastUsedSettingIds() {
        final ClientSettings settings = environment.getConfigStore();
        settings.beginGroup(settingsContext);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(settingsType);
        try {
            final int lastUsedCount = Math.min(settings.beginReadArray(LAST_USED_SETTING_NAME), LAST_USED_LIMIT);
            final List<Id> result = new LinkedList<>();
            try{
                Id settingId;
                for (int i = 0; i < lastUsedCount; i++) {
                    settings.setArrayIndex(i);
                    settingId = Id.Factory.loadFrom(settings.readString("id", null));
                    if (settingId != null){
                        result.add(settingId);
                    }
                }
            }finally{
                settings.endArray();
            }
            return result;
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }        
    }

    @Override
    public Collection<GroupSettings.Group> getSettingGroups() {        
        Collection<GroupSettings.Group> groups = settingsCache.getGroups(settingsContext);
        if (groups==null){
            final GroupSettingsStorage storage = environment.getGroupSettingsStorage();
            groups = storage.loadGroups(settingsContext, settingsType);
            settingsCache.storeGroups(settingsContext, groups);
        }
        return groups;
    }

    @Override
    public List<GroupSettings.SettingItem> getSettingItems() {        
        List<GroupSettings.SettingItem> items = settingsCache.getItems(settingsContext);
        if (items==null){
            final GroupSettingsStorage storage = environment.getGroupSettingsStorage();
            items = storage.loadSettingItems(settingsContext, settingsType);
            settingsCache.storeItems(settingsContext, items);
        }
        return items;
    }

    @Override
    public void setLastUsedSettingIds(List<Id> lastUsedSettingId) {
        final ClientSettings settings = environment.getConfigStore();
        settings.beginGroup(settingsContext);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(settingsType);
        try {
            settings.beginWriteArray(LAST_USED_SETTING_NAME);
            for (int i = 0; i < lastUsedSettingId.size(); i++) {
                settings.setArrayIndex(i);
                settings.writeString("id", lastUsedSettingId.get(i).toString());
            }
            settings.endArray();
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    @Override
    public void setSettings(Collection<GroupSettings.Group> groups, List<GroupSettings.SettingItem> items, List<T> customSettings) {
        final List<V> customDefinitions = new LinkedList<>();
        final List<String> serializedDefinitions = new LinkedList<>();
        if (customSettings!=null){
            V definition;
            String definitionAsStr;
            for (T setting: customSettings){
                definition = getCustomSettingDefinition(setting);
                if (definition!=null){
                    definitionAsStr = serializeCustomDefinition(definition);
                    customDefinitions.add(definition);
                    serializedDefinitions.add(definitionAsStr);
                }
            }
        }
        environment.getGroupSettingsStorage().writeGroupSettings(settingsContext,
                                                                 settingsType,
                                                                 groups,
                                                                 items, 
                                                                 serializedDefinitions);
        settingsCache.store(settingsContext, groups, items, customDefinitions);
    }
    
    private Map<Id,V> getCustomDefinitions(){
        if (customDefinitionsById==null){
            List<V> definitions = settingsCache.getCustomDefinitions(settingsContext);
            if (definitions==null){
                final List<String> serializedDefnitions =
                    environment.getGroupSettingsStorage().loadCustomDefinitions(settingsContext, settingsType);
                definitions = new LinkedList<>();
                V definition;
                for (String definitionAsStr: serializedDefnitions){
                    definition = loadCustomDefinition(definitionAsStr);
                    if (definition!=null){
                        definitions.add(definition);
                    }                
                }            
                settingsCache.storeCustomDefinitions(settingsContext, definitions);
            }
            customDefinitionsById = new HashMap<>();
            for (V definition: definitions){
                customDefinitionsById.put(definition.getId(), definition);
            }
        }
        return customDefinitionsById;
    }

    @Override
    public List<Id> getCustomGroupSettingIds() {
        if (canCreateNew()){
            final Map<Id,V> customDefinitions = getCustomDefinitions();
            final List<Id> customGroupSettingIds = new LinkedList<>();
            customGroupSettingIds.addAll(customDefinitions.keySet());
            return customGroupSettingIds;
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Id> getInheritedCustomGroupSettingIds() {
        return Collections.emptyList();
    }        
    
    @Override
    public T getCustomGroupSetting(final Id settingId){
        final Map<Id,V> customDefinitions = getCustomDefinitions();
        final V definition = customDefinitions.get(settingId);
        return definition==null ? null : createCustomGroupSetting(definition);
    }

    @Override
    public void invalidate() {
        customDefinitionsById = null;
    }        
    
    @Override
    public boolean isReadOnly() {
        return environment.getGroupSettingsStorage().isReadonly();
    }
        
    
    protected abstract V getCustomSettingDefinition(T groupSetting);
    protected abstract T createCustomGroupSetting(V definition);
    
    protected abstract String serializeCustomDefinition(V definition);
    protected abstract V loadCustomDefinition(final String definitionAsStr);    
}
