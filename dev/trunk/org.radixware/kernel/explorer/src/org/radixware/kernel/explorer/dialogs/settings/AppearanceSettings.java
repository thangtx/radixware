/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSettings;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.starter.radixloader.RadixLoader;


final class AppearanceSettings extends ExplorerSettings{
    
    private final static List<String> EXCLUDE_LOAD = 
        Arrays.asList(SettingNames.ExplorerTree.Common.ROOT,
                            SettingNames.Selector.SELECTION_STATISTIC_EXPORT_DIR,
                            "export_exel_params");
    
    private final String filePath;
    private final IClientEnvironment environment;

    private AppearanceSettings(final IClientEnvironment environment,  final QSettings settings){
        super(environment, settings);
        filePath = settings.fileName();
        this.environment = environment;        
    }
    
    public static AppearanceSettings create(final IClientEnvironment environment, final QObject parent){
        final File privateSettingsFile;
        if (RadixLoader.getInstance()==null){
            try{
                privateSettingsFile = File.createTempFile("explorer_appearance_", ".ini");
            }catch(IOException exception){
                final String stack = ClientException.exceptionStackToString(exception);
                final String message = environment.getMessageProvider().translate("ExplorerSettings", "Failed to create application settings file");
                environment.getTracer().put(EEventSeverity.ERROR, message+"\n"+stack, EEventSource.EXPLORER);
                environment.messageError(message);
                return null;
            }
            privateSettingsFile.deleteOnExit();
        }else{
            privateSettingsFile  = RadixLoader.getInstance().createTempFile("explorer_appearance_");
        }
        if (!privateSettingsFile.exists()){
            try{
                privateSettingsFile.createNewFile();
            }catch(IOException exception){
                {
                    final String message = environment.getMessageProvider().translate("ExplorerSettings", "Failed to create application settings file");
                    environment.messageError(message);
                }
                final String messageTemplate = 
                    environment.getMessageProvider().translate("ExplorerSettings", "Failed to create application settings file '%1$s'\n%2$s");
                final String stack = ClientException.exceptionStackToString(exception);
                final String message = String.format(messageTemplate, privateSettingsFile.getAbsoluteFile(), stack);
                environment.getTracer().put(EEventSeverity.ERROR, message, EEventSource.EXPLORER);
                return null;
            }
        }
        final QSettings settings = new QSettings(privateSettingsFile.getAbsolutePath(), QSettings.Format.IniFormat, parent);
        final AppearanceSettings result = new AppearanceSettings(environment, settings);        
        final ExplorerSettings explorerSettings = (ExplorerSettings)environment.getConfigStore();        
        result.read(explorerSettings.getDefaultSettings());//write default settings        
        result.read(explorerSettings);//overwrite default settings with current settings
        return result;
    }
    
    private static void load(final ClientSettings source, final ClientSettings dest){
        dest.beginGroup(SettingNames.SYSTEM);
        source.beginGroup(SettingNames.SYSTEM);
        try{
            dest.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            source.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            try{
                loadGroup(source, dest, SettingNames.ExplorerTree.COMMON_GROUP);
                loadGroup(source, dest, SettingNames.ExplorerTree.PARAGRAPH_GROUP);
                loadGroup(source, dest, SettingNames.ExplorerTree.SELECTOR_GROUP);
                loadGroup(source, dest, SettingNames.ExplorerTree.EDITOR_GROUP);
                loadGroup(source, dest, SettingNames.ExplorerTree.USER_GROUP);
            }finally{
                source.endGroup();
                dest.endGroup();                
            }
            loadGroup(source, dest, SettingNames.EDITOR_GROUP);
            loadGroup(source, dest, SettingNames.SELECTOR_GROUP);
            loadGroup(source, dest, SettingNames.SOURCE_EDITOR);
            dest.remove(SettingNames.APP_STYLE);
            loadGroup(source, dest, SettingNames.APP_STYLE);
            dest.remove(SettingNames.FORMAT_SETTINGS);
            loadGroup(source, dest, SettingNames.FORMAT_SETTINGS);
        }finally{
            source.endGroup();
            dest.endGroup();
        }
    }        
    
    private static void loadGroup(final ClientSettings source, final ClientSettings dest, final String groupName){
        source.beginGroup(groupName);
        dest.beginGroup(groupName);
        try{
            final List<String> keys = source.allKeys();
            for (String key: keys){
                if (!EXCLUDE_LOAD.contains(key)){
                    dest.setValue(key, source.value(key));
                }
            }
        }finally{
            source.endGroup();
            dest.endGroup();
        }
    }
    
    public void read(final ClientSettings settings){
        clearCache();
        load(settings, this);
    }
    
    public void write(final ClientSettings settings){
        load(this, settings);
    }

    public void readFromFile(final String fileName){
        final QSettings settings = new QSettings(fileName, QSettings.Format.IniFormat);
        final AppearanceSettings source = new AppearanceSettings(environment, settings);
        source.beginGroup("Settings");
        read(source);        
        settings.disposeLater();
    }
    
    public void writeToFile(final String fileName){
        final File file = new File(fileName);
        if (file.exists() && !FileUtils.deleteFile(file)){
            environment.processException(new FileException(environment, FileException.EExceptionCode.CANT_WRITE, fileName));            
        }else{
            final QSettings settings = new QSettings(fileName, QSettings.Format.IniFormat);
            final AppearanceSettings dest = new AppearanceSettings(environment, settings);
            dest.beginGroup("Settings");
            write(dest);
            settings.disposeLater();
        }
    }    
    
    public void remove(){
        FileUtils.deleteFile(new File(filePath));
    }
}
