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
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import static org.radixware.kernel.common.client.env.ClientSettings.configVersion;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.starter.radixloader.RadixLoader;

/**
 * The ExplorerSettings class provides persistent explorer settings.
 * <p>
 */
public class ExplorerSettings extends QSettingsWrapper {    
    
    private final IExplorerSettings defaultSettings;
    private final QSettingsWrapper privateCopy;
    private final Map<String, ITextOptions> textOptionsCache = new HashMap<>();
    private final Map<String, QFont> fontCache = new HashMap<>();    
    private final Map<String, QColor> colorCache = new HashMap<>();
    private final IClientEnvironment environment;    

    public ExplorerSettings(final IClientEnvironment environment) {
        super(new QSettings(), environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);
        checkFormatVersion(configVersion);
        privateCopy = createPrivateSettings();
    }

    public ExplorerSettings(final IClientEnvironment environment, QObject parent) {
        super(  new QSettings(parent),
                   environment.getMessageProvider(),
                   environment.getTracer());
        this.environment = environment;        
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);        
        checkFormatVersion(configVersion);
        privateCopy = createPrivateSettings();
    }

    public ExplorerSettings(final IClientEnvironment environment, QSettings.Format format, QSettings.Scope scope, java.lang.String organization) {
        super(new QSettings(format, scope, organization),
                environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;        
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);        
        checkFormatVersion(configVersion);
        privateCopy = createPrivateSettings();
    }

    public ExplorerSettings(final IClientEnvironment environment, String fileName, QSettings.Format format, final IExplorerSettings defSettings, final int formatVersion) {
        super(new QSettings(fileName, format),
                environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;
        defaultSettings = defSettings==null ? EmptySettings.getInstance() : defSettings;
        checkFormatVersion(formatVersion);
        privateCopy = createPrivateSettings();
    }
    
    public ExplorerSettings(final IClientEnvironment environment, String fileName, QSettings.Format format) {
        this(environment,fileName,format,ExplorerDefaultSettings.getInstance(environment),configVersion);
    }

    public ExplorerSettings(final IClientEnvironment environment, final ExplorerSettings source) {
        super(source);
        this.environment = environment;        
        defaultSettings = source.defaultSettings;        
        privateCopy = createPrivateSettings();
        if (privateCopy!=null){
            privateCopy.beginGroup(source.group());
        }
    }
    
    protected ExplorerSettings(final IClientEnvironment env, final QSettings settings){
        super(settings, env.getMessageProvider(), env.getTracer());
        this.environment = env;        
        defaultSettings = EmptySettings.getInstance();
        privateCopy = null;
        checkFormatVersion(configVersion);
    }

    /////////////////////write methods

    @Override
    public void writeQFont(final String key, final QFont font) {
        super.writeQFont(key, font);
        if (privateCopy!=null){
            privateCopy.writeQFont(key, font);
        }
        fontCache.put(getCaheKey(key), font);
    }

    @Override
    public void writeQColor(final String key, final QColor color) {
        if (color != null) {
            super.writeQColor(key,color);
            if (privateCopy!=null){
                privateCopy.writeQColor(key, color);
            }
            colorCache.put(getCaheKey(key), color);
        }
    }
    
    @Override
    public void writeTextOptions(final String key, final ITextOptions options) {
        super.writeTextOptions(key, options);
        if (privateCopy!=null){
            privateCopy.writeTextOptions(key, options);
        }
        textOptionsCache.put(getCaheKey(key), options);
    } 

    @Override
    public void setValue(final String key, final Object value) {
        super.setValue(key, value);
        if (privateCopy!=null){
            privateCopy.setValue(key, value);
        }
    }

    @Override
    public void writeQByteArray(final String key, final QByteArray array) {
        super.writeQByteArray(key, array); 
        if (privateCopy!=null){
            privateCopy.writeQByteArray(key, array);
        }
    }

    @Override
    public void writeQSize(final String key, final QSize size) {
        super.writeQSize(key, size);
        if (privateCopy!=null){
            privateCopy.writeQSize(key, size);
        }
    }

    @Override
    public void writeQAlignmentFlag(final String key, final AlignmentFlag alignmentFlag) {
        super.writeQAlignmentFlag(key, alignmentFlag);
        if (privateCopy!=null){
            privateCopy.writeQAlignmentFlag(key, alignmentFlag);
        }
    }

    @Override
    public void writeQPoint(final String key, final QPoint point) {
        super.writeQPoint(key, point);
        if (privateCopy!=null){
            privateCopy.writeQPoint(key, point);
        }
    }

    @Override
    public void writeDouble(final String key, final double d) {
        super.writeDouble(key, d);
        if (privateCopy!=null){
            privateCopy.writeDouble(key, d);
        }
    }

    @Override
    public void writePid(final String key, final Pid pid) {
        super.writePid(key, pid);
        if (privateCopy!=null){
            privateCopy.writePid(key, pid);
        }
    }

    @Override
    public void writeId(final String key, final Id id) {
        super.writeId(key, id);
        if (privateCopy!=null){
            privateCopy.writeId(key, id);
        }
    }

    @Override
    public void writeString(final String key, final String s) {
        super.writeString(key, s);
        if (privateCopy!=null){
            privateCopy.writeString(key, s);
        }
    }

    @Override
    public void writeInteger(final String key, final int i) {
        super.writeInteger(key, i);
        if (privateCopy!=null){
            privateCopy.writeInteger(key, i);
        }
    }

    @Override
    public void writeBoolean(final String key, final boolean b) {
        super.writeBoolean(key, b);
        if (privateCopy!=null){
            privateCopy.writeBoolean(key, b);
        }
    }       
    
    ///////////////////// public readMethods

    public QFont readQFont(final String key, final QFont defaultQFont) {
        final QFont font;
        final String cacheKey = getCaheKey(key);
        if (fontCache.containsKey(cacheKey)) {
            font = fontCache.get(cacheKey);
        } else {
            font = privateCopy==null ? super.readQFont(key) : privateCopy.readQFont(key);
            fontCache.put(cacheKey, font);
        }
        return font != null ? font : defaultQFont;
    }

    @Override
    public QFont readQFont(final String key) {
        final String cacheKey = getCaheKey(key);
        if (fontCache.containsKey(cacheKey)) {
            return fontCache.get(cacheKey);
        }
        QFont font = privateCopy==null ? super.readQFont(key) : privateCopy.readQFont(key);
        if (font == null) {
            font = defaultSettings.readQFont(key);
            if (font != null) {
                font.setFamily(QApplication.font().family());
            }
        }
        fontCache.put(cacheKey, font);
        return font;
    }

    public QColor readQColor(final String key, final QColor defaultQColor) {
        final String cacheKey = getCaheKey(key);
        final QColor color;
        if (colorCache.containsKey(cacheKey)) {
            color = colorCache.get(cacheKey);
        } else {
            color = privateCopy==null ? super.readQColor(key) : privateCopy.readQColor(key);
            colorCache.put(cacheKey, color);
        }
        return color != null ? color : defaultQColor;
    }

    @Override
    public QColor readQColor(final String key) {
        final String cacheKey = getCaheKey(key);
        if (colorCache.containsKey(cacheKey)) {
            return colorCache.get(cacheKey);
        }
        QColor color = privateCopy==null ? super.readQColor(key) : privateCopy.readQColor(key);
        if (color == null) {
            color = defaultSettings.readQColor(key);
        }
        colorCache.put(cacheKey, color);
        return color;
    }

    public QSize readQSize(final String key, final QSize defaultQSize) {
        final QSize size = privateCopy==null ? super.readQSize(key) : privateCopy.readQSize(key);
        return size != null ? size : defaultQSize;
    }

    @Override
    public QSize readQSize(final String key) {
        final QSize size = privateCopy==null ? super.readQSize(key) : privateCopy.readQSize(key);
        return size != null ? size : defaultSettings.readQSize(key);
    }

    public QPoint readQPoint(final String key, final QPoint defaultQPoint) {
        final QPoint point = privateCopy==null ? super.readQPoint(key) : privateCopy.readQPoint(key);
        return point != null ? point : defaultQPoint;
    }

    @Override
    public QPoint readQPoint(final String key) {
        final QPoint point = privateCopy==null ? super.readQPoint(key) : privateCopy.readQPoint(key);
        return point != null ? point : defaultSettings.readQPoint( key);
    }

    @Override
    public QByteArray readQByteArray(final String key) {
        final QByteArray array = privateCopy==null ? super.readQByteArray(key) : privateCopy.readQByteArray(key);
        return array != null ? array : defaultSettings.readQByteArray(key);
    }

    public AlignmentFlag readAlignmentFlag(final String key, final AlignmentFlag defaultAlignmentFlag) {
        final AlignmentFlag flag = privateCopy==null ? super.readAlignmentFlag(key) : privateCopy.readAlignmentFlag(key);
        return flag != null ? flag : defaultAlignmentFlag;
    }

    @Override
    public AlignmentFlag readAlignmentFlag(final String key) {
        final AlignmentFlag flag = privateCopy==null ? super.readAlignmentFlag(key) : privateCopy.readAlignmentFlag(key);
        return flag != null ? flag : defaultSettings.readAlignmentFlag(key);
    }
    
    @Override
    public ExplorerTextOptions readTextOptions(String key) {
        final String cacheKey = getCaheKey(key);
        ExplorerTextOptions options = (ExplorerTextOptions)textOptionsCache.get(cacheKey);
        if (options==null){
            options = super.readTextOptions(key);//super.readTextOptions works throw overrided method readString
            textOptionsCache.put(cacheKey, options);
        }
        return options;
    }    

    @Override
    public boolean readBoolean(final String key) {
        Boolean value = privateCopy==null ? super.readBooleanObject(key) : privateCopy.readBooleanObject(key);
        return value==null ? defaultSettings.readBoolean(key) : value;
    }

    @Override
    public int readInteger(final String key) {
        Integer value = privateCopy==null ? super.readIntegerObject(key) : privateCopy.readIntegerObject(key);
        return value==null ? defaultSettings.readInteger(key) : value;
    }

    @Override
    public double readDouble(final String key) {
        Double value = privateCopy==null ? super.readDoubleObject(key) : privateCopy.readDoubleObject(key);
        return value==null ? defaultSettings.readDouble(key) : value;
    }

    @Override
    public String readString(final String key) {
        final String value = privateCopy==null ? super.readString(key) : privateCopy.readString(key);
        return value == null ? defaultSettings.readString(key) : value;
    }

    @Override
    public Object value(final String key) {
        if (contains(key)) {
            final Object value = privateCopy==null ? super.value(key) : privateCopy.value(key);
            return value==null ? defaultSettings.value(key) : value;
        }
        return defaultSettings.value(key);
    }

    @Override
    public boolean contains(final String key) {
        return privateCopy==null ? super.contains(key) : privateCopy.contains(key);
    }

    @Override
    public Dimension readSize(final String key) {
        return super.readSize(key);//super.readSize works throw overrided method readQSize
    }

    @Override
    public Object value(final String key, final Object defaultValue) {
        return privateCopy==null ? super.value(key, defaultValue) : privateCopy.value(key, defaultValue);
    }

    @Override
    public Pid readPid(final String key) {
        return super.readPid(key);//super.readPid works throw overrided method readString
    }

    @Override
    public Id readId(final String key) {
        return super.readId(key);//super.readId works throw overrided method readString
    }

    @Override
    public String readString(final String key, final String defaultString) {
        return privateCopy==null ? super.readString(key, defaultString) : privateCopy.readString(key, defaultString);
    }

    @Override
    public Double readDoubleObject(final String key) {
        return privateCopy==null ? super.readDoubleObject(key) : privateCopy.readDoubleObject(key);
    }

    @Override
    public double readDouble(final String key, final double defaultDouble) {
        return privateCopy==null ? super.readDouble(key, defaultDouble) : privateCopy.readDouble(key, defaultDouble);
    }

    @Override
    public Integer readIntegerObject(final String key) {
        return privateCopy==null ? super.readIntegerObject(key) : privateCopy.readIntegerObject(key);
    }

    @Override
    public int readInteger(final String key, final int defaultInteger) {
        return privateCopy==null ? super.readInteger(key, defaultInteger) : privateCopy.readInteger(key, defaultInteger);
    }

    @Override
    public Boolean readBooleanObject(final String key) {
        return privateCopy==null ? super.readBooleanObject(key) : privateCopy.readBooleanObject(key);
    }

    @Override
    public boolean readBoolean(final String key, final boolean defaultBoolean) {
        return privateCopy==null ? super.readBoolean(key, defaultBoolean) : privateCopy.readBoolean(key, defaultBoolean);
    }
        
    /////////////////////cursor methods

    @Override
    public void beginGroup(final String group) {
        super.beginGroup(group);
        if (privateCopy!=null){
            privateCopy.beginGroup(group);
        }
        defaultSettings.beginGroup(group);
    }

    @Override
    public void clear() {
        super.clear();
        if (privateCopy!=null){
            privateCopy.clear();
        }
        fontCache.clear();
        colorCache.clear();
    }

    @Override
    public void remove(final String group) {
        super.remove(group);
        if (privateCopy!=null){
            privateCopy.remove(group);
        }
    }        

    @Override
    public void endGroup() {
        super.endGroup();
        if (privateCopy!=null){
            privateCopy.endGroup();
        }
        defaultSettings.endGroup();
    }

    @Override
    public void endAllGroups() {
        super.endAllGroups();
        if (privateCopy!=null){
            privateCopy.endAllGroups();
        }
        while (defaultSettings.group() != null && !defaultSettings.group().isEmpty()) {
            defaultSettings.endGroup();
        }
    }

    @Override
    public void endArray() {        
        super.endArray();
        if (privateCopy!=null){
            privateCopy.endArray();
        }
    }

    @Override
    public String group() {
        return privateCopy==null ? super.group() : privateCopy.group();
    }

    @Override
    public void beginWriteArray(final String array, final int i) {
        super.beginWriteArray(array, i);
        if (privateCopy!=null){
            privateCopy.beginWriteArray(array, i);
        }        
    }

    @Override
    public void beginWriteArray(final String array) {
        super.beginWriteArray(array);
        if (privateCopy!=null){
            privateCopy.beginWriteArray(array);
        }
    }

    @Override
    public int beginReadArray(final String array) {
        final int result = super.beginReadArray(array);;
        return privateCopy==null ? result : privateCopy.beginReadArray(array);
    }

    @Override
    public void setArrayIndex(int i) {
        super.setArrayIndex(i);
        if (privateCopy!=null){
            privateCopy.setArrayIndex(i);
        }
    }

    @Override
    public void setConfigProfile(final String profile) {
        super.setConfigProfile(profile);
        if (privateCopy!=null){
            privateCopy.setConfigProfile(profile);
        }
    }    
    
    /////////////////////other methods

    @Override
    public String getDescription() {
        return privateCopy==null ? super.getDescription() : privateCopy.getDescription();
    }

    @Override
    public void sync() {
        super.sync();
        privateCopy.sync();
    }

    @Override
    public boolean isInvalid() {
        return super.isInvalid() || (privateCopy!=null && privateCopy.isInvalid());
    }

    @Override
    public boolean isWritable() {
        return super.isWritable() && (privateCopy==null || privateCopy.isWritable());
    }

    @Override
    public List<String> childKeys() {
        return privateCopy==null ? super.childKeys() : privateCopy.childKeys();
    }

    @Override
    public List<String> childGroups() {
        return privateCopy==null ? super.childGroups() : privateCopy.childGroups();
    }

    @Override
    public List<String> allKeys() {
        return privateCopy==null ? super.allKeys() : privateCopy.allKeys();
    }        
    
    private QSettingsWrapper createPrivateSettings(){
        final File privateSettingsFile;
        if (RadixLoader.getInstance()==null){
            try{
                privateSettingsFile = File.createTempFile("explorer_settings_", ".ini");
            }catch(IOException exception){
                final String stack = ClientException.exceptionStackToString(exception);
                String msg = environment.getMessageProvider().translate("ExplorerSettings", "Failed to create application settings file\n%s");
                traceWarning(String.format(msg, stack));
                return null;
            }
            privateSettingsFile.deleteOnExit();
        }else{
            privateSettingsFile  = RadixLoader.getInstance().createTempFile("explorer_settings_");
        }
        try{
            return copyFile(privateSettingsFile);
        }catch(IOException exception){
            final String messageTemplate = 
                environment.getMessageProvider().translate("ExplorerSettings", "Failed to create application settings file '%1$s'\n%2$s");
            final String stack = ClientException.exceptionStackToString(exception);
            traceWarning(String.format(messageTemplate, privateSettingsFile.getAbsoluteFile(), stack));
            return null;
        }
    }

    private void checkFormatVersion(final int formatVersion) {
        if (formatVersion>0){
            if (isEmptyIniFile()) {
                setValue(CONFIG_VERSION, formatVersion);
            } else if (isWritable() && readFormatVersion(getQSettings()) != formatVersion) {
                traceWarning(environment.getMessageProvider().translate("ExplorerSettings", "Old configuration format. clearing..."));
                clear();
                setValue(CONFIG_VERSION, formatVersion);
            }
        }
    }

    private static int readFormatVersion(QSettings settings) {
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
    
    private void traceWarning(final String warningMess) {
        environment.getTracer().put(EEventSeverity.WARNING, warningMess, EEventSource.EXPLORER);
    }        

    private String getCaheKey(final String key) {
        return normalizeKey(group() != null && !group().isEmpty() ? group() + "/" + key : key);
    }

    private static String normalizeKey(final String key) {
        String result = key.replace('\\', '/');
        while (result.contains("//")) {
            result = result.replaceFirst("//", "/");
        }
        return result;
    }
    
    protected final void clearCache(){
        textOptionsCache.clear();
        colorCache.clear();
        fontCache.clear();
    }
    
    private static final HashMap<String,QFont> FONT_CACHE = new HashMap<>();

    public static QFont getQFont(String qfontAsStr) {
        if (FONT_CACHE.containsKey(qfontAsStr))
            return FONT_CACHE.get(qfontAsStr);
        try {            
            final QFont font = new QFont();
            if (font.fromString(qfontAsStr)) {
                FONT_CACHE.put(qfontAsStr, font);
                return font;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static final HashMap<String,QColor> COLOR_CACHE = new HashMap<>();
    
    public static QColor getQColor(String colorAsStr) {
        QColor color = COLOR_CACHE.get(colorAsStr);
        if (color==null){
            try {
                color = new QColor();
                color.setNamedColor(colorAsStr);
                if (color.isValid()) {
                    COLOR_CACHE.put(colorAsStr, color);                    
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return color;
    }
    
    private static final HashMap<String, QBrush> BRUSH_CACHE = new HashMap<>();
    
    public static QBrush getQBrush(final String colorAsStr) {
        QBrush brush = BRUSH_CACHE.get(colorAsStr);
        if (brush==null){
            try {
                final QColor color = getQColor(colorAsStr);
                if (color==null){
                    return null;
                }else{
                    brush = new QBrush(color);
                    BRUSH_CACHE.put(colorAsStr, brush);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return brush;
    }    

    public static QSize getQSize(String sizeAsStr) {
        try {

            int delimiterIndex = sizeAsStr.indexOf(":");
            if (delimiterIndex == -1) {
                return null;	//config corrupted
            }
            final int width = Integer.parseInt(sizeAsStr.substring(0, delimiterIndex));
            final int height = Integer.parseInt(sizeAsStr.substring(delimiterIndex + 1));
            final QSize size = new QSize(width, height);
            if (size.isValid()) {
                return size;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean isEmptyIniFile(){        
        final QSettings qsettings = privateCopy==null ? getQSettings() : privateCopy.getQSettings();
        if (qsettings.format()==QSettings.Format.IniFormat){
            final String fileName = qsettings.fileName();
            if (fileName==null || fileName.isEmpty()){
                return true;
            }
            final File iniFile = new File(fileName);
            if (iniFile.exists()){
                return iniFile.isFile() && iniFile.length()==0;
            }else{
                return true;
            }
        }
        return false;        
    }
    
    public final IExplorerSettings getDefaultSettings(){
        if (defaultSettings==null || defaultSettings==EmptySettings.getInstance()){
            return ExplorerDefaultSettings.getInstance(environment);
        }else{
            return defaultSettings;
        }
    }
}
