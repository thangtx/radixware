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
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


class QSettingsWrapper implements IExplorerSettings {

    private final QSettings settings;
    private final Stack<String> groups = new Stack<>();
    final MessageProvider msgProvider;
    final ClientTracer tracer;
    private String configProfile;

    public QSettingsWrapper(final QSettings settings, final MessageProvider msgProvider, final ClientTracer tracer) {
        this.settings = settings;
        this.msgProvider = msgProvider;
        this.tracer = tracer;
    }

    public QSettingsWrapper(final QSettingsWrapper source) {
        settings = new QSettings(source.settings.fileName(), source.settings.format());
        settings.beginGroup(source.settings.group());
        msgProvider = source.msgProvider;
        tracer = source.tracer;
    }
    
    public QSettingsWrapper copyFile(final File copyTo) throws IOException{
        final String fileName = settings.status()==QSettings.Status.NoError ? settings.fileName() : null;
        if (fileName!=null && !fileName.isEmpty()){            
            final File settingsFile = new File(fileName);
            if (settingsFile.isFile() && settingsFile.canRead()){
                FileUtils.copyFile(settingsFile, copyTo);
            }
        }
        return new QSettingsWrapper(new QSettings(copyTo.getAbsolutePath(), settings.format(), settings.parent()), msgProvider, tracer);
    }

    protected final QSettings getQSettings() {
        return settings;
    }

    @Override
    public boolean contains(final String key) {
        return settings.contains(key);
    }

    @Override
    public void writeBoolean(final String key, boolean b) {
        settings.setValue(key, java.lang.String.valueOf(b));
    }

    @Override
    public void writeInteger(final String key, int i) {
        settings.setValue(key, java.lang.String.valueOf(i));
    }

    @Override
    public void writeString(final String key, String s) {
        settings.setValue(key, s);
    }

    @Override
    public void writeId(String key, Id id) {
        settings.setValue(key, id==null ? null : id.toString());
    }

    @Override
    public void writePid(String key, Pid pid) {
        settings.setValue(key, pid==null ? null : pid.toStr());
    }

    @Override
    public void writeDouble(String key, double d) {
        settings.setValue(key, java.lang.String.valueOf(d));
    }

    @Override
    public void writeQFont(final String key, final QFont font) {
        settings.setValue(key, font==null ? null : font.toString());
    }

    @Override
    public void writeQPoint(final String key, final QPoint point) {
        settings.setValue(key, point==null ? null : point.x() + ":" + point.y());
    }

    @Override
    public void writeQAlignmentFlag(final String key, final Qt.AlignmentFlag alignmentFlag) {
        settings.setValue(key, alignmentFlag==null ? null : alignmentFlag.name());
    }

    @Override
    public void writeQSize(final String key, final QSize size) {
        settings.setValue(key, size==null ? null : size.width() + ":" + size.height());
    }

    @Override
    public void writeQColor(final String key, final QColor color) {
        settings.setValue(key, color==null ? null : color.name());
    }

    @Override
    public void writeQByteArray(final String key, final QByteArray array) {
        settings.setValue(key, array);
    }

    @Override
    public void setValue(final String key, final Object value) {
        settings.setValue(key, value);
    }

    @Override
    public boolean readBoolean(final String key, final boolean defaultBoolean) {
        if (settings.contains(key)) {
            try {
                return Boolean.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Boolean.class, key, e); //config corrupted
            }
        }
        return defaultBoolean;
    }

    @Override
    public boolean readBoolean(final String key) {
        if (settings.contains(key)) {
            try {
                return Boolean.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Boolean.class, key, e); //config corrupted
            }
        }
        return false;
    }

    public Boolean readBooleanObject(final String key) {
        if (settings.contains(key)) {
            try {
                return Boolean.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Boolean.class, key, e); //config corrupted
            }
        }
        return null;
    }

    @Override
    public int readInteger(final String key, final int defaultInteger) {
        if (settings.contains(key)) {
            try {
                final Object value = settings.value(key);
                if (value==null){
                    return defaultInteger;
                }else if (value instanceof Integer){
                    return ((Integer)value).intValue();
                }else if (value instanceof String){
                    return Integer.valueOf((String) value).intValue();
                }else{
                    final WrongFormatError error = 
                        new WrongFormatError("Unexpected value class: "+value.getClass().getName());
                    traceWarning(Integer.class, key, error);
                    return defaultInteger;
                }
            } catch (Exception e) {
                traceWarning(Integer.class, key, e); //config corrupted
            }
        }
        return defaultInteger;
    }

    @Override
    public int readInteger(String key) {
        if (settings.contains(key)) {
            try {
                final Object value = settings.value(key);
                if (value==null){
                    return 0;
                }else if (value instanceof Integer){
                    return ((Integer)value).intValue();
                }else if (value instanceof String){
                    return Integer.valueOf((String) value).intValue();
                }else{
                    final WrongFormatError error = 
                        new WrongFormatError("Unexpected value class: "+value.getClass().getName());
                    traceWarning(Integer.class, key, error);
                    return 0;
                }
            } catch (Exception e) {
                traceWarning(Integer.class, key, e); //config corrupted
            }
        }
        return 0;
    }

    public Integer readIntegerObject(String key) {
        if (settings.contains(key)) {
            try {
                final Object value = settings.value(key);
                if (value==null){
                    return null;
                }else if (value instanceof Integer){
                    return (Integer)value;
                }else if (value instanceof String){
                    return Integer.valueOf((String) value);
                }else{
                    final WrongFormatError error = 
                        new WrongFormatError("Unexpected value class: "+value.getClass().getName());
                    traceWarning(Integer.class, key, error);
                    return null;
                }
            } catch (Exception e) {
                traceWarning(Integer.class, key, e); //config corrupted
            }
        }
        return null;
    }

    @Override
    public double readDouble(String key, double defaultDouble) {
        if (settings.contains(key)) {
            try {
                return Double.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Double.class, key, e); //config corrupted
            }
        }
        return defaultDouble;
    }

    @Override
    public double readDouble(String key) {
        if (settings.contains(key)) {
            try {
                return Double.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Double.class, key, e); //config corrupted
            }
        }
        return Double.NaN;
    }

    public Double readDoubleObject(String key) {
        if (settings.contains(key)) {
            try {
                return Double.valueOf((String) settings.value(key));
            } catch (Exception e) {
                traceWarning(Double.class, key, e); //config corrupted
            }
        }
        return null;
    }

    @Override
    public String readString(String key) {
        if (settings.contains(key)) {
            try {
                return (String) settings.value(key);
            } catch (Exception e) {
                traceWarning(String.class, key, e); //config corrupted
            }
        }
        return null;
    }

    @Override
    public String readString(String key, String defaultString) {
        if (settings.contains(key)) {
            try {
                return (String) settings.value(key);
            } catch (Exception e) {
                traceWarning(String.class, key, e); //config corrupted
            }
        }
        return defaultString;
    }

    @Override
    public Id readId(String key) {
        final String value = readString(key);
        return value == null || value.isEmpty() ? null : Id.Factory.loadFrom(value);
    }

    @Override
    public Pid readPid(String key) {
        final String value = readString(key);
        if (value != null && !value.isEmpty()) {
            final StringTokenizer tokenizer = new StringTokenizer(value, "\n");
            final String tableId = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
            if (tableId == null || tableId.isEmpty()) {
                return null;
            }
            final String pidAsStr = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
            if (pidAsStr == null || pidAsStr.isEmpty()) {
                return null;
            }
            return new Pid(Id.Factory.loadFrom(tableId), pidAsStr);
        }
        return null;
    }

    @Override
    public QFont readQFont(String key) {
        if (settings.contains(key)) {
            try {
                final String fontAsStr = (String) settings.value(key);
                if (fontAsStr!=null && !fontAsStr.isEmpty()){                    
                    final QFont font = ExplorerSettings.getQFont(fontAsStr);
                    if (font==null) {
                        traceWarning(QFont.class, key);                        
                    } else {
                        return font;                        
                    }
                }
            } catch (Exception e) {
                traceWarning(QFont.class, key, e);
            }
        }
        return null;
    }

    @Override
    public QPoint readQPoint(String key) {
        if (settings.contains(key)) {
            try {
                final String pointAsStr = (String) settings.value(key);
                if (pointAsStr!=null && !pointAsStr.isEmpty()){
                    int delimiterIndex = pointAsStr.indexOf(":");
                    if (delimiterIndex == -1) {
                        traceWarning(QPoint.class, key);
                        return null;	//config corrupted
                    }
                    final int x = Integer.parseInt(pointAsStr.substring(0, delimiterIndex));
                    final int y = Integer.parseInt(pointAsStr.substring(delimiterIndex + 1));
                    return new QPoint(x, y);
                }
            } catch (Exception e) {
                traceWarning(QPoint.class, key, e);	//config corrupted
            }
        }
        return null;
    }

    @Override
    public AlignmentFlag readAlignmentFlag(String key) {
        if (settings.contains(key)) {
            try {
                final String alignmentFlagAsStr = (String) settings.value(key);
                return alignmentFlagAsStr==null || alignmentFlagAsStr.isEmpty() ? null : AlignmentFlag.valueOf(alignmentFlagAsStr);
            } catch (Exception e) {
                traceWarning(AlignmentFlag.class, key, e);	//config corrupted
            }
        }
        return null;
    }

    @Override
    public QSize readQSize(String key) {
        if (settings.contains(key)) {
            try {
                final String sizeAsStr = (String) settings.value(key);
                if (sizeAsStr!=null && !sizeAsStr.isEmpty()){
                    int delimiterIndex = sizeAsStr.indexOf(":");
                    if (delimiterIndex == -1) {
                        traceWarning(QSize.class, key);
                        return null;	//config corrupted
                    }
                    final int width = Integer.parseInt(sizeAsStr.substring(0, delimiterIndex));
                    final int height = Integer.parseInt(sizeAsStr.substring(delimiterIndex + 1));
                    final QSize size = new QSize(width, height);
                    if (size.isValid()) {
                        return size;
                    } else {
                        traceWarning(QSize.class, key);
                    }
                }
            } catch (Exception e) {
                traceWarning(QSize.class, key, e);	//config corrupted
            }
        }
        return null;
    }

    @Override
    public QColor readQColor(String key) {
        if (settings.contains(key)) {
            try {
                final String colorAsStr = (String) settings.value(key);
                if (colorAsStr!=null && !colorAsStr.isEmpty()){
                    final QColor color = ExplorerSettings.getQColor(colorAsStr);                    
                    if (color==null) {
                        traceWarning(QColor.class, key);	//config corrupted
                    } else {
                        return color;
                    }
                }
            } catch (Exception e) {
                traceWarning(QColor.class, key, e);	//config corrupted
            }
        }
        return null;
    }

    @Override
    public QByteArray readQByteArray(String key) {
        if (settings.contains(key)) {
            try {
                return (QByteArray) settings.value(key);
            } catch (Exception e) {
                traceWarning(QColor.class, key, e);	//config corrupted
            }
        }
        return null;
    }

    @Override
    public Object value(String key) {
        if (settings.contains(key)) {
            try {
                return settings.value(key);
            } catch (Exception ex) {
                traceWarning(Object.class, key, ex);
            }
        }
        return null;
    }

    @Override
    public Object value(String key, Object defaultValue) {
        if (settings.contains(key)) {
            try {
                return settings.value(key);
            } catch (Exception ex) {
                traceWarning(Object.class, key, ex);
            }
        }
        return defaultValue;
    }

    @Override
    public List<String> allKeys() {
        return settings.allKeys();
    }

    @Override
    public void beginGroup(final String group) {
        settings.beginGroup(group);
    }

    @Override
    public void pushGroup() {
        final String group = group();
        if (group.equals(configProfile)){
            groups.push("");
        }else if (group.startsWith(configProfile+"/")){
            groups.push(group.substring(configProfile.length()+1));
        }else{
            groups.push(group());
        }
    }

    @Override
    public String popGroup() {
        final String group = groups.pop();
        if (!group.isEmpty() && !group.equals(group())){
            beginGroup(group);
        }
        return group;
    }

    @Override
    public void setConfigProfile(final String profile) {
        while (settings.group() != null && !settings.group().isEmpty()){
            settings.endGroup();
        }
        configProfile = profile;
        settings.beginGroup(profile);
    }

    @Override
    public String getConfigProfile() {
        return configProfile;
    }
        
    @Override
    public int beginReadArray(String array) {
        return settings.beginReadArray(array);
    }

    @Override
    public void beginWriteArray(String array) {
        settings.beginWriteArray(array);
    }

    @Override
    public void beginWriteArray(String array, int i) {
        settings.beginWriteArray(array, i);
    }

    @Override
    public List<String> childGroups() {
        return settings.childGroups();
    }

    @Override
    public List<String> childKeys() {
        return settings.childKeys();
    }

    @Override
    public void clear() {
        settings.clear();
    }

    @Override
    public String group() {
        return settings.group();
    }

    @Override
    public void endArray() {
        settings.endArray();
    }

    @Override
    public void endGroup() {
        if (configProfile==null || configProfile.isEmpty() || !Objects.equals(settings.group(), configProfile)){
            settings.endGroup();
        }
    }

    @Override
    public void endAllGroups() {
        String group = settings.group();
        while (group != null && !group.isEmpty() && !Objects.equals(group, configProfile)) {
            settings.endGroup();
            group = settings.group();
        }
    }

    @Override
    public boolean isWritable() {
        return settings.isWritable();
    }

    @Override
    public boolean isInvalid() {
        return settings.status() != QSettings.Status.NoError;
    }

    @Override
    public void sync() {
        settings.sync();
    }

    @Override
    public void remove(String group) {
        settings.remove(group);
    }

    @Override
    public void setArrayIndex(int i) {
        settings.setArrayIndex(i);
    }

    @Override
    public ExplorerTextOptions readTextOptions(final String key) {
        final String fontAsStr = readString(key + "/" + SettingNames.TextOptions.FONT);
        final String alignmentAsStr = readString(key + "/" + SettingNames.TextOptions.ALIGNMENT);
        final String bColorAsStr = readString(key + "/" + SettingNames.TextOptions.BCOLOR);
        final String fColorAsStr = readString(key + "/" + SettingNames.TextOptions.FCOLOR);        
        ExplorerFont font = null;        
        try{
            font = fontAsStr==null || fontAsStr.isEmpty() ? null : ExplorerFont.Factory.fromStr(fontAsStr);
        }catch(IllegalArgumentException exception){
            traceWarning(ExplorerFont.class, key + "/" + SettingNames.TextOptions.FONT, exception);
        }
        ETextAlignment alignment = null;
        try{
            alignment = alignmentAsStr==null || alignmentAsStr.isEmpty() ? null : ETextAlignment.valueOf(alignmentAsStr);
        }catch(IllegalArgumentException exception){
            traceWarning(ETextAlignment.class, key + "/" + SettingNames.TextOptions.ALIGNMENT, exception);
        }
        Color foreground = null;
        try{
            foreground = fColorAsStr==null || fColorAsStr.isEmpty() ? null : Color.decode(fColorAsStr);
        }catch(IllegalArgumentException exception){
            traceWarning(ETextAlignment.class, key + "/" + SettingNames.TextOptions.FCOLOR, exception);
        }
        Color bacground = null;
        try{
            bacground = bColorAsStr==null || bColorAsStr.isEmpty() ? null : Color.decode(bColorAsStr);
        }catch(IllegalArgumentException exception){
            traceWarning(ETextAlignment.class, key + "/" + SettingNames.TextOptions.BCOLOR, exception);
        }
        return ExplorerTextOptions.Factory.getOptions(font, alignment, foreground, bacground);
    }

    @Override
    public void writeTextOptions(final String key, final ITextOptions options) {
        if (options.getFont()!=null){
            writeString(key + "/" + SettingNames.TextOptions.FONT, options.getFont().asStr());
        }else{
            writeString(key + "/" + SettingNames.TextOptions.FONT, null);
        }
        if (options.getAlignment()!=null){
            writeString(key + "/" + SettingNames.TextOptions.ALIGNMENT, options.getAlignment().name());
        }else{
            writeString(key + "/" + SettingNames.TextOptions.ALIGNMENT, null);
        }        
        if (options.getForegroundColor()!=null){
            writeString(key + "/" + SettingNames.TextOptions.FCOLOR, TextOptions.color2Str(options.getForegroundColor()));
        }else{
            writeString(key + "/" + SettingNames.TextOptions.FCOLOR, null);
        }
        if (options.getBackgroundColor()!=null){
            writeString(key + "/" + SettingNames.TextOptions.BCOLOR, TextOptions.color2Str(options.getBackgroundColor()));
        }else{
            writeString(key + "/" + SettingNames.TextOptions.BCOLOR, null);
        }
    }        

    @Override
    public String getDescription() {
        return settings.fileName();
    }

    private void traceWarning(Class<?> valClass, final String key) {
        final String warningMess = msgProvider.translate("ExplorerSettings", "Error occurred during reading %s value from \'%s\'");
        tracer.put(EEventSeverity.WARNING, String.format(warningMess, valClass.getSimpleName(), key), EEventSource.EXPLORER);
    }

    private void traceWarning(Class<?> valClass, final String key, Exception ex) {
        final String warningMess = msgProvider.translate("ExplorerSettings", "Error occurred during reading %s value from \'%s\':\n %s");
        tracer.put(EEventSeverity.WARNING, String.format(warningMess, valClass.getSimpleName(), key, ClientException.getExceptionReason(msgProvider, ex)), EEventSource.EXPLORER);
    }

    @Override
    public Dimension readSize(String key) {
        QSize qsize = readQSize(key);
        if (qsize == null) {
            return null;
        }
        return new Dimension(qsize.width(), qsize.height());
    }
}
