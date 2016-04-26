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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.types.Id;


final class MergedGroupSettingsSource<T extends IGroupSetting, V extends Definition> implements IGroupSettingsSource<T>{
    
    private static final class BaseGroupSettingsSource<T extends IGroupSetting, V extends Definition> extends GroupSettingsSource<T,V>{
        
        private final GroupSettingsSource<T,V> derivedSource;
        
        public BaseGroupSettingsSource(final GroupSettingsSource<T,V> derivedSource, final String settingsContext){
            super(derivedSource.environment,
                  settingsContext,
                  derivedSource.settingsType,
                  derivedSource.settingsCache);
            this.derivedSource = derivedSource;
        }

        @Override
        protected V getCustomSettingDefinition(final T groupSetting) {
            return derivedSource.getCustomSettingDefinition(groupSetting);
        }

        @Override
        protected T createCustomGroupSetting(final V definition) {
            return derivedSource.createCustomGroupSetting(definition);
        }

        @Override
        protected String serializeCustomDefinition(final V definition) {
            return derivedSource.serializeCustomDefinition(definition);
        }

        @Override
        protected V loadCustomDefinition(final String definitionAsStr) {
            return derivedSource.loadCustomDefinition(definitionAsStr);
        }

        @Override
        public List<T> getPredefinedGroupSettings() {
            return derivedSource.getPredefinedGroupSettings();
        }

        @Override
        public boolean canCreateNew() {
            return derivedSource.canCreateNew();
        }

        @Override
        public void setLastUsedSettingIds(final List<Id> lastUsedSettingId) {
            throw new UnsupportedOperationException("This operation is not supported here.");
        }

        @Override
        public void setSettings(final Collection<GroupSettings.Group> groups, final List<GroupSettings.SettingItem> items, final List<T> customSettings) {
            throw new UnsupportedOperationException("This operation is not supported here.");
        }                
    }

    private final GroupSettingsSource<T,V> primarySource;
    private final RadSelectorUserExplorerItemDef contextExplorerItem;
    private final List<BaseGroupSettingsSource<T,V>> extendedSources = new LinkedList<>();
    private final List<Id> hiddenCustomSettingsIds = new LinkedList<>();
    private final List<Id> ownSettingsIds = new LinkedList<>();
    private final List<Id> inheritedCustomSettingsIds = new LinkedList<>();
    private String groupsSaveString;
    private String itemsSaveString;
    
    
    private MergedGroupSettingsSource(final GroupSettingsSource<T,V> primarySource,
                                     final RadSelectorUserExplorerItemDef explorerItem){
        this.primarySource = primarySource;
        contextExplorerItem = explorerItem;        
    }
    
    private void initExtendedSources(){
        final String primarySettingsContext = primarySource.settingsContext;
        final String contextExplorerItemIdAsStr = contextExplorerItem.getId().toString();
        RadSelectorExplorerItemDef sourceExplorerItemDef = contextExplorerItem.getSourceExplorerItem();
        String extendedSettingsContext = 
            primarySettingsContext.replace(contextExplorerItemIdAsStr, sourceExplorerItemDef.getId().toString());
        extendedSources.add(new BaseGroupSettingsSource<>(primarySource,extendedSettingsContext));
        while (sourceExplorerItemDef instanceof RadSelectorUserExplorerItemDef){
            sourceExplorerItemDef = ((RadSelectorUserExplorerItemDef)sourceExplorerItemDef).getSourceExplorerItem();
            extendedSettingsContext = 
                primarySettingsContext.replace(contextExplorerItemIdAsStr, sourceExplorerItemDef.getId().toString());            
            extendedSources.add(new BaseGroupSettingsSource<>(primarySource,extendedSettingsContext));
        }
        
    }

    @Override
    public final List<Id> getLastUsedSettingIds() {
        return primarySource.getLastUsedSettingIds();
    }

    @Override
    public Collection<GroupSettings.Group> getSettingGroups() {
        Collection<GroupSettings.Group> groups = primarySource.getSettingGroups();
        if (groups==null || groups.isEmpty()){
            if (extendedSources.isEmpty()){
                initExtendedSources();
            }
            for (BaseGroupSettingsSource<T,V> source: extendedSources){
                groups = source.getSettingGroups();
                if (groups!=null && !groups.isEmpty()){
                    groupsSaveString = getGroupsAsStr(groups);
                    return groups;
                }
            }
            groupsSaveString = "";
        }else{
            groupsSaveString = null;            
        }
        return groups;
    }   

    @Override
    public List<GroupSettings.SettingItem> getSettingItems() {
        List<GroupSettings.SettingItem> items = primarySource.getSettingItems();
        if (items!=null && !items.isEmpty()){
            items = new LinkedList<>(items);
            for (int i=items.size()-1; i>=0; i--){
                if (items.get(i).isHidden()){
                    hiddenCustomSettingsIds.add(items.get(i).settingId);
                    items.remove(i);
                }
            }
            if (!items.isEmpty()){
                itemsSaveString = null;
                return items;
            }
        }
        if (extendedSources.isEmpty()){
            initExtendedSources();
        }
        for (BaseGroupSettingsSource<T,V> source: extendedSources){                
            items = source.getSettingItems();
            if (items!=null && !items.isEmpty()){
                items = new LinkedList<>(items);
                for (int i=items.size()-1; i>=0; i--){
                    if (items.get(i).isHidden()){
                        items.remove(i);//скрытие настроек не наследуется
                    }
                }
                if (!items.isEmpty()){
                    itemsSaveString = getSettingItemsAsStr(items);
                    return items;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setLastUsedSettingIds(final List<Id> lastUsedSettingId) {
        primarySource.setLastUsedSettingIds(lastUsedSettingId);
    }

    @Override
    public void setSettings(final Collection<GroupSettings.Group> groups, 
                            final List<GroupSettings.SettingItem> items, 
                            final List<T> customSettings) {        
        List<GroupSettings.SettingItem> itemsToWrite = null;
        final List<Id> customSettingsIds = new LinkedList<>();
        if (customSettings!=null && !customSettings.isEmpty()){
            for (T setting: customSettings){
                customSettingsIds.add(setting.getId());
            }
        }
        final boolean needToWriteSettingsTree = groupsSaveString==null || itemsSaveString==null 
                                                || !groupsSaveString.equals(getGroupsAsStr(groups))
                                                || !itemsSaveString.equals(getSettingItemsAsStr(items));                
        for (Id customSettingId: inheritedCustomSettingsIds){
            if (!customSettingsIds.contains(customSettingId)){
                //была удалена унаследованная насройка - помечаем ее как скрытую
                if (itemsToWrite==null){
                    if (needToWriteSettingsTree){
                        itemsToWrite = new LinkedList<>(items);
                    }else{
                        itemsToWrite = new LinkedList<>();
                    }
                }
                itemsToWrite.add(new GroupSettings.SettingItem(customSettingId,true));
            }
        }
        final List<T> settingsToWrite = new LinkedList<>();        
        for (T setting: customSettings){            
            if (ownSettingsIds.contains(setting.getId()) 
                || !inheritedCustomSettingsIds.contains(setting.getId()) 
                || setting.wasModified()){
                settingsToWrite.add(setting);
            }
        }
        if (itemsToWrite!=null){
            primarySource.setSettings(groups, itemsToWrite, settingsToWrite);
        }else if (needToWriteSettingsTree){
            primarySource.setSettings(groups, items, settingsToWrite);
        }else{
            primarySource.setSettings(null, null, settingsToWrite);
        }
    }

    @Override
    public List<Id> getCustomGroupSettingIds() {
        final List<Id> mergedSettingsIds = new LinkedList<>(primarySource.getCustomGroupSettingIds());
        ownSettingsIds.clear();
        ownSettingsIds.addAll(mergedSettingsIds);
        inheritedCustomSettingsIds.clear();
        if (extendedSources.isEmpty()){
            initExtendedSources();
        }        
        for (BaseGroupSettingsSource<T,V> source: extendedSources){
            final List<Id> extSettingsIds = source.getCustomGroupSettingIds();        
            for (Id id: extSettingsIds){
                if (!inheritedCustomSettingsIds.contains(id)){
                    inheritedCustomSettingsIds.add(id);
                    if (!mergedSettingsIds.contains(id) && !hiddenCustomSettingsIds.contains(id)){
                        mergedSettingsIds.add(id);
                    }
                }
            }
        }
        return mergedSettingsIds;
    }

    @Override
    public Collection<Id> getInheritedCustomGroupSettingIds() {
        return Collections.unmodifiableList(inheritedCustomSettingsIds);
    }

    @Override
    public T getCustomGroupSetting(final Id settingId) {
        T setting = primarySource.getCustomGroupSetting(settingId);
        if (setting==null && !hiddenCustomSettingsIds.contains(settingId)){
            if (extendedSources.isEmpty()){
                initExtendedSources();
            }            
            for (BaseGroupSettingsSource<T,V> source: extendedSources){
                setting = source.getCustomGroupSetting(settingId);
                if (setting!=null){
                    if (!inheritedCustomSettingsIds.contains(setting.getId())){
                        inheritedCustomSettingsIds.add(setting.getId());
                    }
                    //create a copy of custom setting to prevent modification of original
                    final String settingAsStr = customGroupSettingAsString(setting);                    
                    final V defCopy = primarySource.loadCustomDefinition(settingAsStr);
                    final T settingCopy = primarySource.createCustomGroupSetting(defCopy);                    
                    return settingCopy;
                }
            }
        }
        return setting;
    }

    @Override
    public void invalidate() {
        primarySource.invalidate();
        for (BaseGroupSettingsSource<T,V> source: extendedSources){
            source.invalidate();
        }
        inheritedCustomSettingsIds.clear();
        ownSettingsIds.clear();
        hiddenCustomSettingsIds.clear();
        groupsSaveString = null;
        itemsSaveString = null;
        extendedSources.clear();
    }

    @Override
    public boolean isReadOnly() {
        return primarySource.isReadOnly();
    }

    private static String getGroupsAsStr(final Collection<GroupSettings.Group> groups){
        if (groups==null || groups.isEmpty()){
            return "";
        }
        final StringBuilder strBuilder = new StringBuilder();
        for (GroupSettings.Group group: groups){
            if (strBuilder.length()>0){
                strBuilder.append("\n");
            }
            strBuilder.append(group.toString());
        }
        return strBuilder.toString();
    }
    
    private static String getSettingItemsAsStr(final List<GroupSettings.SettingItem> items){
        if (items==null || items.isEmpty()){
            return "";
        }
        final StringBuilder strBuilder = new StringBuilder();
        for (GroupSettings.SettingItem item: items){
            if (strBuilder.length()>0){
                strBuilder.append("\n");
            }
            strBuilder.append(item.toString());            
        }
        return strBuilder.toString();
    }

    @Override
    public List<T> getPredefinedGroupSettings() {
        return primarySource.getPredefinedGroupSettings();
    }

    @Override
    public boolean canCreateNew() {
        return primarySource.canCreateNew();
    }
    
    private String customGroupSettingAsString(final T setting){
        final V customDefinition = primarySource.getCustomSettingDefinition(setting);
        return primarySource.serializeCustomDefinition(customDefinition);
    }        
    
    static <T extends IGroupSetting, V extends Definition> IGroupSettingsSource<T> mergeIfNecessary(final GroupModel group,
                                                                                                        final GroupSettingsSource<T,V> primarySource) {
        if (group.getContext() instanceof IContext.TableSelect){
            RadExplorerItemDef explorerItemDef = ((IContext.TableSelect)group.getContext()).explorerItemDef;
            if (explorerItemDef instanceof RadSelectorUserExplorerItemDef){
                return new MergedGroupSettingsSource<>(primarySource, (RadSelectorUserExplorerItemDef)explorerItemDef);
            }
        }
        return primarySource;
    }    
}
