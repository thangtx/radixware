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

package org.radixware.kernel.common.client.models;

import java.awt.Dimension;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;


public final class ModelSettings implements ClientSettings {
    
    private final String rootGroup;
    private final String restrictGroup;
    private final ClientSettings clientSettings;
    private String currentGroup = "";
    private String clientSettingsState = "";
    private final String illegalUsageMessage;
    private final Stack<String> groupStack = new Stack<String>();
    private final Stack<String> keepGroupStack = new Stack<String>();
    
    
    public ModelSettings(final IClientEnvironment environment, final Model model) {
        this.rootGroup = model.getConfigStoreGroupName();
        this.restrictGroup = this.rootGroup + "/" + SettingNames.SYSTEM;
        this.clientSettings = environment.getConfigStore();
        beginGroup(rootGroup);
        illegalUsageMessage = "Can't change group " + restrictGroup;
    }
    
    @Override
    public boolean contains(final String key) {
        boolean result;
        switchClientSettings();
        result = clientSettings.contains(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public void writeBoolean(final String key, final boolean b) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            
                switchClientSettings();
                clientSettings.writeBoolean(key, b);
                restoreClientSettings();
            
        }
    }

    @Override
    public void writeInteger(final String key, final int i) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
           
                switchClientSettings();
                clientSettings.writeInteger(key, i);
                restoreClientSettings();
            
        }
    }

    @Override
    public void writeString(final String key, final String s) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            
                switchClientSettings();
                clientSettings.writeString(key, s);
                restoreClientSettings();
            
        }
    }

    @Override
    public void writeId(final String key, final Id id) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            
                switchClientSettings();
                clientSettings.writeId(key, id);
                restoreClientSettings();
            
        }
    }

    @Override
    public void writePid(final String key, final Pid pid) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            
                switchClientSettings();
                clientSettings.writePid(key, pid);
                restoreClientSettings();
            
        }
    }

    @Override
    public void writeDouble(final String key, final double d) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
           
                switchClientSettings();
                clientSettings.writeDouble(key, d);
                restoreClientSettings();
            
        }
    }

    @Override
    public void setValue(final String key, final Object value) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            
                switchClientSettings();
                clientSettings.setValue(key, value);
                restoreClientSettings();
            
        }
    }

    @Override
    public boolean readBoolean(final String key, final boolean defaultBoolean) {
        switchClientSettings();
        final boolean result = clientSettings.readBoolean(key, defaultBoolean);
        restoreClientSettings();
        return result;
    }

    @Override
    public boolean readBoolean(final String key) {
        switchClientSettings();
        final boolean result = clientSettings.readBoolean(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public int readInteger(final String key, final int defaultInteger) {
        switchClientSettings();
        final int result = clientSettings.readInteger(key, defaultInteger);
        restoreClientSettings();
        return result;
    }

    @Override
    public int readInteger(final String key) {
        switchClientSettings();
        final int result = clientSettings.readInteger(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public double readDouble(final String key, final double defaultDouble) {
        switchClientSettings();
        final double result = clientSettings.readDouble(key, defaultDouble);
        restoreClientSettings();
        return result;
    }

    @Override
    public double readDouble(final String key) {
        switchClientSettings();
        final double result = clientSettings.readDouble(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public String readString(final String key) {
        switchClientSettings();
        final String result = clientSettings.readString(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public String readString(final String key, final String defaultString) {
        switchClientSettings();
        final String result = clientSettings.readString(key, defaultString);
        restoreClientSettings();
        return result;
    }

    @Override
    public Id readId(final String key) {
        switchClientSettings();
        final Id result = clientSettings.readId(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public Pid readPid(final String key) {
        switchClientSettings();
        final Pid result = clientSettings.readPid(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public Object value(final String key) {
        switchClientSettings();
        final Object result = clientSettings.value(key);
        restoreClientSettings();
        return result;
    }

    @Override
    public Object value(final String key, final Object defaultValue) {
        switchClientSettings();
        final Object result = clientSettings.value(key, defaultValue);
        restoreClientSettings();
        return result;
    }

    @Override
    public List<String> allKeys() {
        List<String> allKeys;
        
            switchClientSettings();
            allKeys = clientSettings.allKeys();
            restoreClientSettings();
        
        return allKeys;
    }

    @Override
    public void beginGroup(final String group) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
            switchClientSettings();
            clientSettings.beginGroup(group);
            restoreClientSettings();
            groupStack.push(group);
            currentGroup = currentGroup.isEmpty() ? group : group() + "/" + group;
        }
    }

    @Override
    public void pushGroup() {
        keepGroupStack.push(currentGroup);
    }

    @Override
    public String popGroup() {
        final String group = keepGroupStack.pop();
        if (!Objects.equals(group, currentGroup)){
            beginGroup(group);
        }
        return group;
    }
        

    @Override
    public void setConfigProfile(final String profile) {
        throw new UnsupportedOperationException("Unsupported operation: setConfigProfile(String)");
    }

    @Override
    public String getConfigProfile() {
        return "";
    }

    @Override
    public int beginReadArray(final String array) {
        return clientSettings.beginReadArray(array);
    }

    @Override
    public void beginWriteArray(final String array) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        }
        clientSettings.beginWriteArray(array);
    }

    @Override
    public void beginWriteArray(final String array, final int i) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        }
        clientSettings.beginWriteArray(array, i);
    }

    @Override
    public List<String> childGroups() {
        List<String> childGroups;
        
            switchClientSettings();
            childGroups = clientSettings.childGroups();
            restoreClientSettings();
        
        return childGroups;
    }

    @Override
    public List<String> childKeys() {
        List<String> childKeys;
        
            switchClientSettings();
            childKeys = clientSettings.childKeys();
            restoreClientSettings();
        
        return childKeys;
    }

    @Override
    public void clear() {
        currentGroup = rootGroup;
        switchClientSettings();
        final List<String> childGroups = clientSettings.childGroups();
        for(String s : childGroups) {
            if(s.equals(SettingNames.SYSTEM)) {
                continue;
            } else {
                clientSettings.remove(s);
            }
        }
        restoreClientSettings();
    }

    @Override
    public String group() {
        return currentGroup;
    }

    @Override
    public void endArray() {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError(rootGroup);
        }
        clientSettings.endArray();
    }

    @Override
    public void endGroup() {
        switchClientSettings();
        clientSettings.endGroup();
        restoreClientSettings();
        
        final int last = group().lastIndexOf('/');
        if(last != -1) {
            currentGroup = group().substring(0, last);
        }
    }

    @Override
    public void endAllGroups() {
        while(!group().equals(rootGroup)) {
            endGroup();
        }
    }

    @Override
    public boolean isWritable() {
        return !group().equals(restrictGroup) && clientSettings.isWritable();
    }

    @Override
    public boolean isInvalid() {
        return clientSettings.isInvalid();
    }

    @Override
    public void sync() {
        clientSettings.sync();
    }

    @Override
    public void remove(final String group) {
        if(group().contains(restrictGroup)) {
            throw new IllegalUsageError(illegalUsageMessage);
        } else {
           
                switchClientSettings();
                clientSettings.remove(group);
                restoreClientSettings();
            
        }
    }

    @Override
    public void setArrayIndex(final int i) {
        clientSettings.setArrayIndex(i);
    }
    
    @Override
    public ITextOptions readTextOptions(final String path){
        switchClientSettings();
        final ITextOptions result = clientSettings.readTextOptions(path);
        restoreClientSettings();
        return result;
    }

    @Override
    public void writeTextOptions(final String key, final ITextOptions textSettings) {
        if(group().equals(restrictGroup)) {
            throw new IllegalUsageError("Can't change group " + restrictGroup);
        }
        switchClientSettings();
        clientSettings.writeTextOptions(key, textSettings);
        restoreClientSettings();
    }
        

    @Override
    public String getDescription() {
        return clientSettings.getDescription();
    }

    @Override
    public Dimension readSize(final String key) {
        switchClientSettings();
        final Dimension result = clientSettings.readSize(key);
        restoreClientSettings();
        return result;
    }
    
    private void switchClientSettings() {
        clientSettingsState = clientSettings.group();
        clientSettings.endAllGroups();
        clientSettings.beginGroup(group());
    }
    
    private void restoreClientSettings() {
        clientSettings.endAllGroups();
        clientSettings.beginGroup(clientSettingsState);
    }
    
    
}
