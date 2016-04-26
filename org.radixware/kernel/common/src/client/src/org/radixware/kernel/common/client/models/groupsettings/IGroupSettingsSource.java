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
import java.util.List;
import org.radixware.kernel.common.types.Id;


public interface IGroupSettingsSource<T extends IGroupSetting> {        
    
    List<Id> getLastUsedSettingIds();
    
    Collection<GroupSettings.Group> getSettingGroups();
    
    List<GroupSettings.SettingItem> getSettingItems();
    
    List<T> getPredefinedGroupSettings();
    
    List<Id> getCustomGroupSettingIds();
    
    Collection<Id> getInheritedCustomGroupSettingIds();
    
    T getCustomGroupSetting(final Id id);
            
    void setLastUsedSettingIds(final List<Id> lastUsedSettingId);
    
    void setSettings(final Collection<GroupSettings.Group> groups, final List<GroupSettings.SettingItem> items, final List<T> customSettings);
    
    boolean canCreateNew();
    
    boolean isReadOnly();
    
    void invalidate();
}
