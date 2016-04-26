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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettings.SettingItem;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public abstract class GroupSettings<T extends IGroupSetting> {        

    public final static class SettingItem {

        public final String groupName;
        public final Id settingId;
        protected boolean autoPosition = false;
        
        private boolean isValid = true;
        private final boolean isHidden;

        public SettingItem(final String groupName) {
            this.groupName = groupName;
            settingId = null;
            isHidden = false;
        }

        public SettingItem(final Id settingId) {
            this(settingId,false);
        }
        
        public SettingItem(final Id settingId, final boolean isHidden) {
            this.settingId = settingId;
            this.isHidden = isHidden;
            groupName = null;
        }        

        protected SettingItem(final Group group) {
            this(group.getName());
        }

        protected SettingItem(final IGroupSetting setting) {
            this(setting.getId());
        }
        
        public boolean isHidden(){
            return isHidden;
        }
        
        public boolean isValid(){
            return isValid;
        }
        
        public void markAsValid(){
            isValid = true;
        }
        
        public void markAsInvalid(){
            isValid = false;
        }        
        
        public void store(final ClientSettings settings) {
            if (groupName != null) {
                settings.setValue("groupName", groupName);
            } else if (settingId != null) {
                settings.setValue("settingId", settingId.toString());
            }
            if (isHidden){
                settings.setValue("hidden", "1");
            }
        }
        
        public SettingItem copy(){
            if (groupName != null) {
                return new SettingItem(groupName);
            } else {
                return new SettingItem(settingId,isHidden);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this==o){
                return true;
            }
            if (o instanceof SettingItem){
                final SettingItem item = (SettingItem)o;
                return Utils.equals(settingId, item.settingId) && Utils.equals(groupName, item.groupName);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 43 * hash + (this.groupName != null ? this.groupName.hashCode() : 0);
            hash = 43 * hash + (this.settingId != null ? this.settingId.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            if (settingId!=null){
                return "setting: "+settingId.toString();
            }else{
                return "group: "+groupName;
            }
        }
        
        
    }

    public final static class Group {

        private String name;
        private final List<Id> addons = new ArrayList<>();
        private final List<Id> visibleAddons = new LinkedList<>();
        private boolean wasModified;
        
        private Group(final Group source){
            name = source.name;
            visibleAddons.addAll(source.visibleAddons);
            addons.addAll(source.visibleAddons);
        }

        protected Group(final String groupName, final String order) {
            name = groupName;
            final List<String> addonIds = ArrStr.fromValAsStr(order);            
            for (String addonIdAsStr : addonIds) {
                final Id addonId = Id.Factory.loadFrom(addonIdAsStr);
                if (!addons.contains(addonId)){
                    addons.add(addonId);
                }
            }
            visibleAddons.addAll(addons);
        }
        
        protected Group copy(){
            return new Group(this);
        }

        protected void store(final ClientSettings settings) {
            settings.setValue("name", name);
            final ArrStr addonIds = new ArrStr(visibleAddons.size());
            for (Id addonId : visibleAddons) {
                addonIds.add(addonId.toString());
            }
            settings.setValue("order", addonIds.toString());
            wasModified = false;
        }

        @Override
        public String toString() {
            final ArrStr addonIds = new ArrStr(visibleAddons.size());
            for (Id addonId : visibleAddons) {
                addonIds.add(addonId.toString());
            }
            return name+"-"+addonIds.toString();
        }
        
        
        
        public void hideAddon(final Id addonId){
            visibleAddons.remove(addonId);
        }
        
        public void showAddon(final Id addonId){            
            if (addons.contains(addonId) && !visibleAddons.contains(addonId)){
                final List<Id> newVisibleAddons = new LinkedList<>();
                for (Id id: addons){
                    if (visibleAddons.contains(id) || id.equals(addonId)){
                        newVisibleAddons.add(id);
                    }
                }
                visibleAddons.clear();
                visibleAddons.addAll(newVisibleAddons);
            }
        }

        public void setName(final String newName) {
            if (newName != null && !newName.isEmpty() && !name.isEmpty()) {
                name = newName;
            }
        }

        public String getName() {
            return name;
        }

        public List<Id> getSettingsByOrder() {
            return Collections.unmodifiableList(visibleAddons);
        }
        
        public List<Id> getAllSettings() {
            return Collections.unmodifiableList(addons);
        }        

        public void setOrder(final Id addonId, final int newIndex) {            
            final int index = visibleAddons.indexOf(addonId);
            if (index < 0) {
                final String mess = "Addon with name \'%s\' was not found in group \'%s\'";
                throw new IllegalArgumentError(String.format(mess, addonId, name));
            }
            final int logicalIndex = addons.indexOf(addonId);
            final int newLogicalIndex = mapToLogicalIndex(newIndex);            
            addons.set(logicalIndex, addons.set(newLogicalIndex, addonId));
            visibleAddons.set(index, visibleAddons.set(newIndex, addonId));            
            wasModified = true;
        }

        public void add(final Id addonId) {
            if (!addons.contains(addonId)){
                addons.add(addonId);
                visibleAddons.add(addonId);
                wasModified = true;
            }
        }

        public void add(final int index, final Id addonId) {
            if (!addons.contains(addonId)){
                addons.add(mapToLogicalIndex(index),addonId);
                visibleAddons.add(index, addonId);                
                wasModified = true;
            }
        }
        
        private int mapToLogicalIndex(final int visibleIndex){
            if (visibleIndex>0 && visibleIndex<visibleAddons.size()){
                return addons.indexOf(visibleAddons.get(visibleIndex));
            }
            else if (visibleIndex==visibleAddons.size()){
                return addons.size();
            }
            else{
                return visibleIndex;
            }
        }

        public boolean contains(final Id addonId) {
            return visibleAddons.contains(addonId);
        }

        public void remove(final Id addonId) {
            if (addons.remove(addonId)) {
                visibleAddons.remove(addonId);
                wasModified = true;
            }
        }

        public int size() {
            return visibleAddons.size();
        }

        public boolean isEmpty() {
            return visibleAddons.isEmpty();
        }

        public boolean wasModified() {
            return wasModified;
        }

        public void clear() {
            addons.clear();
            visibleAddons.clear();
            wasModified = true;
        }
    }
        
    private Map<Id, T> settingsById = new LinkedHashMap<>();
    private final Collection<Group> groups = new ArrayList<>();
    private final List<SettingItem> items = new ArrayList<>();
    private final List<SettingItem> orderedItems = new ArrayList<>();
    private final Collection<Id> predefinedSettingIds = new ArrayList<>();
    private final IGroupSettingsSource<T> settingsSource;    
    private List<T> lastUsed = null;
    private final static int LAST_USED_LIMIT = 10;
    private boolean wasModified;
    private boolean loaded;    
    
    protected GroupSettings(final IGroupSettingsSource<T> settingsSource) {
        this.settingsSource = settingsSource;
        final List<T> settings = settingsSource.getPredefinedGroupSettings();
        for (T setting: settings){
            predefinedSettingIds.add(setting.getId());
            settingsById.put(setting.getId(), setting);
        }
        load();
    }
    
    private void load(){
        groups.addAll(settingsSource.getSettingGroups());
        items.addAll(settingsSource.getSettingItems());
        orderedItems.addAll(items);
        final List<Id> customSettingIds = settingsSource.getCustomGroupSettingIds();
        {//delete obsolete custom settings
            final Collection<T> allSettings = new ArrayList<>(getAll());
            for (T setting: allSettings){
                if (!predefinedSettingIds.contains(setting.getId()) && 
                    !customSettingIds.contains(setting.getId())){
                    settingsById.remove(setting.getId());
                }
            }
        }
        {//register new custom settings
            T newCustomSetting;
            for (Id settingId: customSettingIds){
                if (!settingsById.containsKey(settingId)){
                    newCustomSetting = settingsSource.getCustomGroupSetting(settingId);
                    if (newCustomSetting!=null){
                        settingsById.put(settingId, newCustomSetting);
                    }
                }
            }
        }
        loaded = true;
        actualizeGroups();        
    }
    
    private void checkIfLoaded(){
        if (!loaded){
            load();
        }
    }

    public void invalidate(){
        groups.clear();
        items.clear();
        orderedItems.clear();
        for (Id inheritedCustomSettingId: settingsSource.getInheritedCustomGroupSettingIds()){
            settingsById.remove(inheritedCustomSettingId);
        }
        settingsSource.invalidate();
        lastUsed = null;
//        settingsById.clear();
        wasModified = false;
        loaded = false;
    }
    
    final void putSetting(final T setting){
        checkIfLoaded();
        settingsById.put(setting.getId(), setting);
    }
    
    final void removeSetting(final Id settingId){
        checkIfLoaded();
        settingsById.remove(settingId);
    }
    
    final Collection<T> getAll(){
        return settingsById.values();
    }       

    private void fillLastUsed() {
        final List<Id> lastUsedSettingsIds = settingsSource.getLastUsedSettingIds();
        lastUsed = new ArrayList<>(lastUsedSettingsIds.size());
        T setting;
        for (Id settingId: lastUsedSettingsIds){
            setting = findById(settingId);
            if (setting!=null){
                lastUsed.add(setting);
            }
        }
    }
    
    public final boolean canCreateNew(){
        return settingsSource.canCreateNew();
    }

    protected abstract T createNewSetting(final String name, IGroupSetting base, final IWidget parent);

    public abstract boolean isObligatory();

    public abstract T openSettingsManager(final IWidget parent);    
           
    public final T create(final String name, IGroupSetting base, final Group group, final int index, final IWidget parent) {
        checkIfLoaded();
        final T setting = createNewSetting(name, base, parent);
        if (setting != null) {
            add(setting, group, index);
        }
        return setting;
    }

    public final void add(T setting, final Group group, final int index) {
        checkIfLoaded();
        if (setting != null) {
            final Id settingId = setting.getId();
            settingsById.put(settingId, setting);
            if (group != null) {
                if (group.size() > index) {
                    group.add(index, settingId);
                } else {
                    group.add(settingId);
                }

            } else {
                if (items.size() > index) {
                    items.add(index, new SettingItem(setting));
                } else {
                    items.add(new SettingItem(setting));
                }
            }
        }
    }

    public final T remove(Id settingId) {
        checkIfLoaded();
        if (!settingsById.containsKey(settingId)) {
            final String mess = "User defined addon #%s was not found";
            throw new IllegalArgumentError(String.format(mess, settingId.toString()));
        }
        for (Group group : groups) {
            group.remove(settingId);
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            if (settingId.equals(items.get(i).settingId)) {
                items.remove(i);
                break;
            }
        }
        if (lastUsed != null) {
            lastUsed.remove(settingsById.get(settingId));
        }

        wasModified = true;
        return settingsById.remove(settingId);
    }

    public final void remove(T addon) {
        checkIfLoaded();
        remove(addon.getId());
    }

    public final T findById(final Id settingId) {
        checkIfLoaded();
        return settingsById.get(settingId);
    }

    public final Group findGroupByName(final String name) {
        checkIfLoaded();
        for (Group group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    public final Group findGroupForSetting(final Id settingId) {
        checkIfLoaded();
        for (Group group : groups) {
            if (group.contains(settingId)) {
                return group;
            }
        }
        return null;
    }

    public final List<T> getPredefinedAddonsByOrder() {
        checkIfLoaded();
        final List<T> result = new ArrayList<>();
        List<Id> settingIds;
        T setting;
        for (SettingItem item : getItemsByOrder()) {
            if (item.settingId != null) {
                setting = findById(item.settingId);
                if (setting != null && !setting.isUserDefined()) {
                    result.add(setting);
                }
            } else {
                final Group group = findGroupByName(item.groupName);
                if (group != null) {
                    settingIds = group.getSettingsByOrder();
                    for (Id settingId : settingIds) {
                        setting = findById(settingId);
                        if (setting != null && !setting.isUserDefined()) {
                            result.add(setting);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public final T getFirstPredefined() {
        checkIfLoaded();
        final List<T> predefinedByOrder = getPredefinedAddonsByOrder();
        return predefinedByOrder.isEmpty() ? null : predefinedByOrder.get(0);
    }

    public final List<SettingItem> getItemsByOrder() {
        checkIfLoaded();
        final List<SettingItem> validItems = new ArrayList<>();
        for (SettingItem item: items){
            if (item.isValid())
                validItems.add(item);
        }
        return Collections.unmodifiableList(validItems);
    }
    
    public final void setItemsOrder(final List<SettingItem> newOrder) {//reordering items        
        checkIfLoaded();
        items.clear();
        for (SettingItem item : newOrder) {//add items from new order and remove it from old order
            if (item.settingId != null && findById(item.settingId) == null) {
                throw new IllegalArgumentError("Setting #" + item.settingId.toString() + " not found");
            } else if (item.groupName != null) {
                final GroupSettings.Group group = findGroupByName(item.groupName);
                if (group==null){
                    throw new IllegalArgumentError("Group '" + item.groupName + "' not found");
                }
                final List<Id> settingsInGroup = group.getAllSettings();
                for (int i=orderedItems.size()-1; i>=0; i--){
                    if (settingsInGroup.contains(orderedItems.get(i).settingId)){
                        orderedItems.remove(i);
                    }
                }
            }
            item.autoPosition = false;
            items.add(item);            
            //find and remove item from old order
            orderedItems.remove(item);
        }
        for (SettingItem item : orderedItems) {//add other ordered items
            if ((item.settingId != null && findById(item.settingId) != null) ||
                (item.groupName != null && findGroupByName(item.groupName) != null)
               ) {
                item.autoPosition = false;
                items.add(item);                
            }
        }
        orderedItems.clear();
        //actualize orderedItems
        for (SettingItem item: items){
            if (item.isValid())
                orderedItems.add(item);
        }
        //write other items
        actualizeGroups();
        wasModified = true;
    }        

    public final int getSettingsCount() {
        checkIfLoaded();
        return settingsById.size();
    }

    public final Collection<String> getAllSettingTitles() {
        checkIfLoaded();
        final Collection<String> result = new ArrayList<>();
        for (IGroupSetting groupSetting : settingsById.values()) {
            result.add(groupSetting.getTitle());
        }
        return Collections.unmodifiableCollection(result);
    }

    public final Group addGroup(final String groupName) {
        checkIfLoaded();
        final Group newGroup = new Group(groupName, "");
        items.add(new SettingItem(newGroup));
        groups.add(newGroup);
        wasModified = true;
        return newGroup;
    }

    public final void removeGroup(final String groupName) {
        checkIfLoaded();
        final Group group = findGroupByName(groupName);
        if (group != null) {
            for (int i = items.size() - 1; i >= 0; i--) {
                if (groupName.equals(items.get(i).groupName)) {
                    items.remove(i);
                    final List<Id> settingIds = group.getSettingsByOrder();
                    for (Id settingId : settingIds) {
                        if (settingsById.get(settingId) != null) {
                            if (settingsById.get(settingId).isUserDefined()) {
                                settingsById.remove(settingId);
                            } else {
                                items.add(i, new SettingItem(settingId));
                            }
                        }
                    }
                    wasModified = true;
                    break;
                }
            }
            groups.remove(group);
        }
    }

    protected final void actualizeGroups() {        
        final List<String> existingGroups = new ArrayList<>();
        final List<Id> existingSettings = new ArrayList<>();
        SettingItem item;
        for (int i = items.size() - 1; i >= 0; i--) {
            item = items.get(i);
            if (item.settingId != null && findById(item.settingId) == null){
                item.markAsInvalid();
            }
            else if (item.groupName != null && findGroupByName(item.groupName) == null){
                items.remove(i);                
            } else if (item.groupName != null) {
                existingGroups.add(item.groupName);
            } else if (item.settingId != null) {
                if (item.autoPosition){
                    items.remove(i);//item will be reordered
                }
                else{
                    existingSettings.add(item.settingId);
                    item.markAsValid();
                }
            }
        }
        {
            List<Id> settingIds;
            Id settingId;
            for (Group group : groups) {
                settingIds = group.getAllSettings();                
                for (int i = settingIds.size() - 1; i >= 0; i--) {
                    settingId = settingIds.get(i);                    
                    if (findById(settingId) == null) {
                        group.hideAddon(settingId);
                    } else {
                        if (existingSettings.contains(settingId)){
                            //setting was registered in group so remove it from items
                            for (int j=items.size()-1; j>=0; j--){
                                if (settingId.equals(items.get(j).settingId)){
                                    items.remove(j);
                                    break;
                                }
                            }
                        }else{
                            existingSettings.add(settingId);                            
                        }
                        group.showAddon(settingId);
                    }
                }
                if (!existingGroups.contains(group.getName())) {
                    items.add(new SettingItem(group.getName()));
                }
            }
        }
        for (Id settingId : settingsById.keySet()) {
            if (!existingSettings.contains(settingId)) {
                final SettingItem newSettingItem = new SettingItem(settingId);
                newSettingItem.autoPosition = true;
                items.add(newSettingItem);
            }
        }
    }

    public final List<T> getLastUsed() {
        checkIfLoaded();
        if (lastUsed == null) {
            fillLastUsed();
        }
        return Collections.unmodifiableList(lastUsed);
    }

    public final void setAsLastUsed(T addon) {
        checkIfLoaded();
        if (lastUsed == null) {
            fillLastUsed();
        }
        if (lastUsed.contains(addon)) {
            lastUsed.remove(addon);
        }
        lastUsed.add(0, addon);
        while (lastUsed.size() > LAST_USED_LIMIT) {
            lastUsed.remove(lastUsed.size() - 1);
        }
    }

    public final boolean wasModified() {        
        if (wasModified) {
            return true;
        }
        for (Group group : groups) {
            if (group.wasModified) {
                return true;
            }
        }
        for (IGroupSetting groupSetting : settingsById.values()) {
            if (groupSetting.wasModified()) {
                return true;
            }
        }
        return false;
    }

    private void writeSettings() {
        final List<T> customSettings = new LinkedList<>();
        for (T groupSetting : settingsById.values()) {
            if (groupSetting.isUserDefined()) {
                customSettings.add(groupSetting);
            }
        }
        settingsSource.setSettings(groups, orderedItems, customSettings);
    }        

    public final void saveAll() {
        if (loaded){
            if (wasModified()) {
                writeSettings();
            }
            writeLastUsed();
        }
    }

    private void writeLastUsed() {
        if (lastUsed != null && !lastUsed.isEmpty()) {
            final List<Id> lastUsedSettingsIds = new ArrayList<>();
            for (T setting: lastUsed){
                lastUsedSettingsIds.add(setting.getId());
            }
            settingsSource.setLastUsedSettingIds(lastUsedSettingsIds);
        }
    }

    public boolean isEmpty() {
        checkIfLoaded();
        for (SettingItem item: items){
            if (item.isValid()){
                return false;
            }
        }
        return true;
    }

    public final boolean isSettingWithNameExist(final String name) {
        checkIfLoaded();
        for (IGroupSetting setting : settingsById.values()) {
            if (setting.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isSettingExist(final Id settingId) {
        checkIfLoaded();
        return settingsById.containsKey(settingId);
    }

    public final boolean isGroupExist(final String name) {
        checkIfLoaded();
        for (GroupSettings.Group group : groups) {
            if (group.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canOpenSettingsManager() {
        return !isEmpty() || (canCreateNew() && !settingsSource.isReadOnly());
    }    
}
