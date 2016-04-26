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
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Id;


public abstract class GroupSettingsStorage {

    protected final ClientSettings settings;
    private final IClientEnvironment environment;

    public GroupSettingsStorage(IClientEnvironment environment) {
        this.environment = environment;

        settings = createSettings();
        if (settings.isInvalid()) {
            environment.getTracer().error("Group settings file '" + getStorageDescription() + "' settings status are invalid");
        }
        settings.beginGroup(environment.getUserName() + "_" + environment.getStationName());
    }

    public boolean isReadonly() {
        return settings.isInvalid() || !settings.isWritable();
    }
    private final static String GROUPS_SETTING_ARRAY_NAME = "groups";
    private final static String SETTING_ITEMS_ARRAY_NAME = "items";
    private final static String DEFINTION_SETTING_ARRAY_NAME = "definitions";

    public List<GroupSettings.SettingItem> loadSettingItems(final String context, final String configPrefix) {
        final List<GroupSettings.SettingItem> settingItems = new ArrayList<>();
        final List<String> groupNames = new ArrayList<>();
        final List<String> settingIds = new ArrayList<>();
        settings.beginGroup(context);
        settings.beginGroup(configPrefix);
        String settingItem;
        try {
            final int count = settings.beginReadArray(SETTING_ITEMS_ARRAY_NAME);
            for (int i = 0; i < count; i++) {
                settings.setArrayIndex(i);
                settingItem = settings.readString("groupName");
                if (settingItem != null && !settingItem.isEmpty() && !groupNames.contains(settingItem)) {
                    settingItems.add(new GroupSettings.SettingItem(settingItem));
                    groupNames.add(settingItem);
                } else {
                    settingItem = settings.readString("settingId");                    
                    if (settingItem != null && !settingItem.isEmpty() && !settingIds.contains(settingItem)) {
                        final boolean isHidden = "1".equals(settings.value("hidden", "0"));
                        settingItems.add(new GroupSettings.SettingItem(Id.Factory.loadFrom(settingItem), isHidden));
                        settingIds.add(settingItem);
                    }
                }
            }
        } catch (Exception exception) {
            traceWarning(SETTING_ITEMS_ARRAY_NAME, exception);
        } finally {
            settings.endArray();
            settings.endGroup();
            settings.endGroup();
        }

        return Collections.unmodifiableList(settingItems);
    }

    public List<GroupSettings.Group> loadGroups(final String context, final String configPrefix) {
        final List<GroupSettings.Group> groups = new ArrayList<>();

        settings.beginGroup(context);
        settings.beginGroup(configPrefix);
        try {
            final int count = settings.beginReadArray(GROUPS_SETTING_ARRAY_NAME);
            String groupName;
            for (int i = 0; i < count; i++) {
                settings.setArrayIndex(i);
                groupName = settings.readString("name");
                if (groupName != null && !groupName.isEmpty()) {
                    groups.add(new GroupSettings.Group(groupName,  settings.readString("order")));
                }
            }
        } catch (Exception exception) {
            traceWarning(GROUPS_SETTING_ARRAY_NAME, exception);
        } finally {
            settings.endArray();
            settings.endGroup();
            settings.endGroup();
        }

        return groups;
    }

    public void writeGroupSettings(final String context, final String configPrefix, final Collection<GroupSettings.Group> groups, final List<GroupSettings.SettingItem> items, final List<String> definitions) {
        if (!isReadonly()) {
            settings.beginGroup(context);
            settings.remove(configPrefix);
            settings.beginGroup(configPrefix);
            try {
                if (groups!=null){
                    settings.beginWriteArray(GROUPS_SETTING_ARRAY_NAME);
                    try {
                        int i = 0;
                        for (GroupSettings.Group group : groups) {
                            settings.setArrayIndex(i);
                            group.store(settings);
                            i++;
                        }
                    } finally {
                        settings.endArray();
                    }
                }
                if (items!=null){
                    settings.beginWriteArray(SETTING_ITEMS_ARRAY_NAME);
                    try {
                        for (int i = 0; i < items.size(); i++) {
                            settings.setArrayIndex(i);
                            items.get(i).store(settings);
                        }
                    } finally {
                        settings.endArray();
                    }
                }
                if (definitions!=null){
                    settings.beginWriteArray(DEFINTION_SETTING_ARRAY_NAME);
                    try {
                        for (int i = 0; i < definitions.size(); i++) {
                            settings.setArrayIndex(i);
                            settings.setValue("xml", definitions.get(i));
                        }
                    } finally {
                        settings.endArray();
                    }
                }
            } finally {
                settings.endGroup();
                settings.endGroup();
            }
            settings.sync();
        }
    }

    public List<String> loadCustomDefinitions(final String context, final String configPrefix) {
        final List<String> definitions = new ArrayList<>();
        settings.beginGroup(context);
        settings.beginGroup(configPrefix);
        try {
            final int count = settings.beginReadArray(DEFINTION_SETTING_ARRAY_NAME);
            for (int i = 0; i < count; i++) {
                settings.setArrayIndex(i);
                definitions.add(settings.readString("xml"));
            }
        } catch (Exception exception) {
            traceWarning("xml", exception);
        } finally {
            settings.endArray();
            settings.endGroup();
            settings.endGroup();
        }
        return definitions;
    }

    private void traceWarning(final String key, final Exception exception) {
        final String warningMess = environment.getMessageProvider().translate("ExplorerSettings", "Error occurred during reading value of '%s' key from \'%s\' file:\n %s");
        environment.getTracer().put(EEventSeverity.WARNING, String.format(warningMess, key, getStorageDescription(), ClientException.getExceptionReason(environment.getMessageProvider(), exception)), EEventSource.EXPLORER);
    }

    protected abstract String getStorageDescription();

    protected abstract ClientSettings createSettings();
}
