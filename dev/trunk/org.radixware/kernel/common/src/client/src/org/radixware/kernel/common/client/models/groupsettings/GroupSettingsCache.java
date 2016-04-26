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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.meta.Definition;


final class GroupSettingsCache<T extends Definition>{
    
    private final static class Entry<T extends Definition>{
        
        private List<GroupSettings.Group> groups;
        private List<GroupSettings.SettingItem> items;
        private List<T> customDefinitions;
        
        public Entry(final Collection<GroupSettings.Group> groups, final List<GroupSettings.SettingItem> items, final List<T> customDefinitions){
            if (groups!=null){
                setGroups(groups);
            }
            if (items!=null){
                setItems(items);
            }
            if (customDefinitions!=null){
                setCustomDefinitions(customDefinitions);
            }
        }
        
        public Collection<GroupSettings.Group> getGroups(){
            if (groups==null){
                return null;
            }
            final List<GroupSettings.Group> result = new ArrayList<>();
            for (GroupSettings.Group group: groups){
                result.add(group.copy());
            }
            return result;
        }
        
        public List<GroupSettings.SettingItem> getItems(){
            if (items==null){
                return null;
            }
            final List<GroupSettings.SettingItem> result = new ArrayList<>();
            for (GroupSettings.SettingItem item: items){
                result.add(item.copy());
            }
            return result;
        }
        
        public List<T> getCustomDefinitions(){
            return customDefinitions==null ? null : new ArrayList<>(customDefinitions);
        }
        
        public void setGroups(final Collection<GroupSettings.Group> newGroups){
            groups = new ArrayList<>();
            if (newGroups!=null){
                for (GroupSettings.Group group: newGroups){
                    groups.add(group.copy());
                }
            }
        }
        
        public void setItems(final List<GroupSettings.SettingItem> newItems){
            items = new ArrayList<>();
            if (newItems!=null){
                for (GroupSettings.SettingItem item: newItems){
                    items.add(item.copy());
                }
            }
        }
        
        public void setCustomDefinitions(final List<T> newDefinitions){
            customDefinitions = new ArrayList<>();
            if (newDefinitions!=null){
                customDefinitions.addAll(newDefinitions);
            }
        }
    }
    
    private final Map<String,Entry<T>> entriesByKey = new HashMap<>();
    private final String keyPrefix;
    private final IClientEnvironment.ConnectionListener connectionListener;
    private final AdsVersion.VersionListener versionListener;
    
    public GroupSettingsCache(final String settingsClass, 
                              final IClientEnvironment.ConnectionListener connectionListener,
                              final AdsVersion.VersionListener versionListener
                              ){
        keyPrefix = settingsClass;
        this.connectionListener = connectionListener;
        this.versionListener = versionListener;
    }
        
    public Collection<GroupSettings.Group> getGroups(final String context){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        return entry == null ? null : entry.getGroups();
    }
    
    public List<GroupSettings.SettingItem> getItems(final String context){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        return entry == null ? null : entry.getItems();
    }
    
    public List<T> getCustomDefinitions(final String context){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        return entry == null ? null : entry.getCustomDefinitions();
    }
    
    public void store(final String context,
                      final Collection<GroupSettings.Group> groups, 
                      final List<GroupSettings.SettingItem> items, 
                      final List<T> customDefiniitions){
        entriesByKey.put(getKey(context), new Entry<>(groups,items,customDefiniitions));
    }
    
    public void storeGroups(final String context, final Collection<GroupSettings.Group> groups){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        if (entry==null){
            entriesByKey.put(getKey(context), new Entry<T>(groups,null,null));
        }else{
            entry.setGroups(groups);
        }        
    }
    
    public void storeItems(final String context, final List<GroupSettings.SettingItem> items){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        if (entry==null){
            entriesByKey.put(getKey(context), new Entry<T>(null,items,null));
        }else{
            entry.setItems(items);
        }                
    }
    
    public void storeCustomDefinitions(final String context, final List<T> definitions){
        final Entry<T> entry = entriesByKey.get(getKey(context));
        if (entry==null){
            entriesByKey.put(getKey(context), new Entry<>(null,null,definitions));
        }else{
            entry.setCustomDefinitions(definitions);
        }                
    }    
    
    private String getKey(final String context){
        return keyPrefix+"-"+context;
    }
    
    void removeListeners(final IClientEnvironment environment){
        environment.removeConnectionListener(connectionListener);
        environment.getDefManager().getAdsVersion().removeVersionListener(versionListener);
    }
    
}
