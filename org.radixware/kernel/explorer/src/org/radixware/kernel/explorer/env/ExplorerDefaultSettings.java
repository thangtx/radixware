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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QFileInfo;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import static org.radixware.kernel.common.client.env.ClientSettings.CONFIG_VERSION;
import static org.radixware.kernel.common.client.env.ClientSettings.DEFAULT_SETTINGS_FILE_NAME;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.SystemTools;


final class ExplorerDefaultSettings implements IExplorerSettings{
    
    private static enum SETTINGS_SOURCE{
        FONT,OTHER
    }
    
    private final static String DEFAULT_MACOSX_SETTINGS_FILE_NAME = "default_macosx_fonts.ini";
        
    private final List<IExplorerSettings> settingsList;
    
    private ExplorerDefaultSettings(final List<IExplorerSettings> settings){
        this.settingsList = settings;
    }
    
    public static ExplorerDefaultSettings getInstance(final IClientEnvironment environment){
        final List<IExplorerSettings> defaultSettings = new LinkedList<>();
        if (SystemTools.isOSX){
            defaultSettings.add(initDefaults(DEFAULT_MACOSX_SETTINGS_FILE_NAME, environment));
        }
        defaultSettings.add(initDefaults(DEFAULT_SETTINGS_FILE_NAME, environment));
        return new ExplorerDefaultSettings(defaultSettings);
    }
    
    private static IExplorerSettings initDefaults(final String defaultSettingsFileName, final IClientEnvironment environment){
        final QDir workDir = environment.getWorkPath() != null ? new QDir(environment.getWorkPath()) : QDir.temp();
        final QFileInfo tmpFileInfo = new QFileInfo(workDir, defaultSettingsFileName);
        if (tmpFileInfo.exists()) {
            environment.getTracer().debug("Trying to read default settings file \"" + tmpFileInfo.absoluteFilePath() + "\"", false);
            final QSettings qsettings = new QSettings(tmpFileInfo.absoluteFilePath(), QSettings.Format.IniFormat);
            if (qsettings.status() != QSettings.Status.NoError) {
                environment.getTracer().debug("Default settings status is " + qsettings.status().name() + " - removing...", false);
                QFile.remove(tmpFileInfo.absoluteFilePath());
            } else if (readFormatVersion(qsettings) != defaultConfigVersion) {
                environment.getTracer().debug("Default settings file has invalid format version " + qsettings.status().name() + " - removing...", false);
                QFile.remove(tmpFileInfo.absoluteFilePath());
            } else {
                environment.getTracer().debug("Default settings status is " + qsettings.status().name(), false);
                return new QSettingsWrapper(qsettings, environment.getMessageProvider(), environment.getTracer());
            }
        }
        try {
            extractDefaultSettingsFile(defaultSettingsFileName, tmpFileInfo.absoluteFilePath());
        } catch (IOException ex) {
            throw new RadixError("Can't read default settings", ex);
        }
        environment.getTracer().debug("Trying to read default settings file \"" + tmpFileInfo.absoluteFilePath() + "\"", false);
        final QSettings qsettings = new QSettings(tmpFileInfo.absoluteFilePath(), QSettings.Format.IniFormat);
        environment.getTracer().debug("Default settings status is " + qsettings.status().name(), false);
        return new QSettingsWrapper(qsettings, environment.getMessageProvider(), environment.getTracer());
        
    }
    
    private static void extractDefaultSettingsFile(final String resourceName, final String extractTo) throws IOException {
        final File destination = new File(extractTo);
        if (!destination.exists() && !destination.createNewFile()) {
            throw new RadixError("Can't create settings file: \"" + destination.getAbsolutePath() + "\"");
        }
        final InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (iStream == null) {
            throw new RadixError("Can't read default settings");
        }
        final FileOutputStream oStream = new FileOutputStream(destination);
        try {
            FileUtils.copyStream(iStream, oStream);
            oStream.flush();
        } finally {
            try {
                iStream.close();
            } catch (IOException ex) {
            }
            try {
                oStream.close();
            } catch (IOException ex) {
            }
        }
    }   
    
    private static int readFormatVersion(final QSettings settings) {
        if (!settings.contains(CONFIG_VERSION)) {
            return -1;
        } else {
            try {
                final String intStrRepresentation = (String) settings.value(CONFIG_VERSION);
                return java.lang.Integer.parseInt(intStrRepresentation);
            } catch (Exception e) {
                return -1;
            }
        }
    }    

    @Override
    public void writeQFont(final String key, final QFont font) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeQPoint(final String key, final QPoint point) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeQAlignmentFlag(final String key, final Qt.AlignmentFlag alignmentFlag) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeQSize(final String key, final QSize size) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeQColor(final String key, final QColor color) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeQByteArray(final String key, final QByteArray array) {
        throw new UnsupportedOperationException("This operation is not supported");
    }
    
    private IExplorerSettings getSettingsForKey(final String key){
        for (IExplorerSettings settings: settingsList){
            if (settings.contains(key)){
                return settings;
            }
        }
        return EmptySettings.getInstance();
    }

    @Override
    public QFont readQFont(final String key) {        
        return getSettingsForKey(key).readQFont(key);
    }

    @Override
    public QPoint readQPoint(final String key) {
        return getSettingsForKey(key).readQPoint(key);
    }

    @Override
    public Qt.AlignmentFlag readAlignmentFlag(final String key) {
        return getSettingsForKey(key).readAlignmentFlag(key);
    }

    @Override
    public QSize readQSize(final String key) {
        return getSettingsForKey(key).readQSize(key);
    }

    @Override
    public QColor readQColor(final String key) {
        return getSettingsForKey(key).readQColor(key);
    }

    @Override
    public QByteArray readQByteArray(final String key) {
        return getSettingsForKey(key).readQByteArray(key);
    }

    @Override
    public boolean contains(final String key) {
        for (IExplorerSettings settings: settingsList){
            if (settings.contains(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeBoolean(final String key, final boolean b) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeInteger(final String key, final int i) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeString(final String key, final String s) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeId(final String key, final Id id) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writePid(final String key, final Pid pid) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void writeDouble(final String key, final double d) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void setValue(final String key, final Object value) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public boolean readBoolean(final String key, final boolean defaultBoolean) {
        return getSettingsForKey(key).readBoolean(key, defaultBoolean);        
    }

    @Override
    public boolean readBoolean(final String key) {
        return getSettingsForKey(key).readBoolean(key);
    }

    @Override
    public int readInteger(final String key, final int defaultInteger) {
        return getSettingsForKey(key).readInteger(key, defaultInteger);
    }

    @Override
    public int readInteger(final String key) {
        return getSettingsForKey(key).readInteger(key);
    }

    @Override
    public double readDouble(final String key, final double defaultDouble) {
        return getSettingsForKey(key).readDouble(key, defaultDouble);
    }

    @Override
    public double readDouble(final String key) {
        return getSettingsForKey(key).readDouble(key);
    }

    @Override
    public String readString(final String key) {
        return getSettingsForKey(key).readString(key);
    }

    @Override
    public String readString(final String key, final String defaultString) {
        return getSettingsForKey(key).readString(key, defaultString);
    }

    @Override
    public Id readId(final String key) {
        return getSettingsForKey(key).readId(key);
    }

    @Override
    public Pid readPid(final String key) {
        return getSettingsForKey(key).readPid(key);
    }

    @Override
    public Object value(final String key) {
        return getSettingsForKey(key).value(key);
    }

    @Override
    public Object value(final String key, final Object defaultValue) {
        return getSettingsForKey(key).value(key,defaultValue);
    }

    @Override
    public List<String> allKeys() {
        final ArrayList<String> keys = new ArrayList<>();
        for (IExplorerSettings settings: settingsList){
            final List<String> settingKeys = settings.allKeys();
            for (String key: settingKeys){
                if (!keys.contains(key)){
                    keys.add(key);
                }
            }
        }
        return Collections.unmodifiableList(keys);
    }

    @Override
    public void beginGroup(final String group) {
        for (IExplorerSettings settings: settingsList){
            settings.beginGroup(group);
        }
    }

    @Override
    public void setConfigProfile(final String profile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public int beginReadArray(final String array) {
        int result = -1;
        for (IExplorerSettings settings: settingsList){
            if (result<0 && settings.contains(array)){
                result = settings.beginReadArray(array);
            }else{
                settings.beginReadArray(array);
            }
        }
        return result;
    }

    @Override
    public void beginWriteArray(final String array) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void beginWriteArray(final String array, final int i) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public List<String> childGroups() {
        final List<String> childGroups = new ArrayList<>();
        for (IExplorerSettings settings: settingsList){
            final List<String> settingsGroups = settings.childGroups();
            for (String groupName: settingsGroups){
                if (!childGroups.contains(groupName)){
                    childGroups.add(groupName);
                }
            }
        }        
        return Collections.unmodifiableList(childGroups);
    }

    @Override
    public List<String> childKeys() {
        final List<String> childKeys = new ArrayList<>();
        for (IExplorerSettings settings: settingsList){
            final List<String> settingsKeys = settings.childKeys();
            for (String keyName: settingsKeys){
                if (!childKeys.contains(keyName)){
                    childKeys.add(keyName);
                }
            }
        }
        return Collections.unmodifiableList(childKeys);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public String group() {
        return settingsList.get(settingsList.size()-1).group();
    }

    @Override
    public void endArray() {
        for (IExplorerSettings settings: settingsList){
            settings.endArray();
        }
    }

    @Override
    public void endGroup() {
        for (IExplorerSettings settings: settingsList){
            settings.endGroup();
        }
    }

    @Override
    public void endAllGroups() {
        for (IExplorerSettings settings: settingsList){
            settings.endAllGroups();
        }
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        for (IExplorerSettings settings: settingsList){        
            if (settings.isInvalid()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void sync() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void remove(final String group) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void setArrayIndex(final int i) {
        for (IExplorerSettings settings: settingsList){
            settings.setArrayIndex(i);
        }
    }

    @Override
    public ITextOptions readTextOptions(final String key) {
        return getSettingsForKey(key).readTextOptions(key);
    }

    @Override
    public void writeTextOptions(final String key, final ITextOptions textSettings) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public String getDescription() {
        final StringBuilder description = new StringBuilder();
        description.append("[");
        for (IExplorerSettings settings: settingsList){
            if (description.length()>1){
                description.append(", ");
            }
            description.append(settings.getDescription());
        }
        description.append("]");
        return description.toString();
    }

    @Override
    public Dimension readSize(final String key) {
        return getSettingsForKey(key).readSize(key);
    }    
}
