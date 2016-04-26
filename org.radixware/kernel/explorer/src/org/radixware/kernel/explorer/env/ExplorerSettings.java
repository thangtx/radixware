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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import static org.radixware.kernel.common.client.env.ClientSettings.configVersion;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

/**
 * The ExplorerSettings class provides persistent explorer settings.
 * <p>
 */
public class ExplorerSettings extends QSettingsWrapper {

    private final IExplorerSettings defaultSettings;
    private final Map<String, ITextOptions> textOptionsCache = new HashMap<>();
    private final Map<String, QFont> fontCache = new HashMap<>();
    private static final Map<String, QColor> colorCache = new HashMap<>();
    private final IClientEnvironment environment;

    public ExplorerSettings(final IClientEnvironment environment) {
        super(new QSettings(), environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);
        checkFormatVersion(configVersion);
    }

    public ExplorerSettings(final IClientEnvironment environment, QObject parent) {
        super(  new QSettings(parent),
                   environment.getMessageProvider(),
                   environment.getTracer());
        this.environment = environment;        
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);
        checkFormatVersion(configVersion);
    }

    public ExplorerSettings(final IClientEnvironment environment, QSettings.Format format, QSettings.Scope scope, java.lang.String organization) {
        super(new QSettings(format, scope, organization),
                environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;        
        defaultSettings = ExplorerDefaultSettings.getInstance(environment);
        checkFormatVersion(configVersion);
    }

    public ExplorerSettings(final IClientEnvironment environment, String fileName, QSettings.Format format, final IExplorerSettings defSettings, final int formatVersion) {
        super(new QSettings(fileName, format),
                environment.getMessageProvider(), environment.getTracer());
        this.environment = environment;
        defaultSettings = defSettings==null ? EmptySettings.getInstance() : defSettings;
        checkFormatVersion(formatVersion);
    }
    
    public ExplorerSettings(final IClientEnvironment environment, String fileName, QSettings.Format format) {
        this(environment,fileName,format,ExplorerDefaultSettings.getInstance(environment),configVersion);
    }

    public ExplorerSettings(final IClientEnvironment environment, final ClientSettings source) {
        super((QSettingsWrapper)source);
        this.environment = environment;
        defaultSettings = ((ExplorerSettings) source).defaultSettings;
    }

    /////////////////////write methods

    @Override
    public void writeQFont(final String key, final QFont font) {
        super.writeQFont(key, font);
        fontCache.put(getCaheKey(key), font);
    }

    @Override
    public void writeQColor(final String key, final QColor color) {
        if (color != null) {
            super.writeQColor(key,color);
            colorCache.put(getCaheKey(key), color);
        }
    }

    ///////////////////// public readMethods

    public QFont readQFont(final String key, final QFont defaultQFont) {
        final QFont font;
        final String cacheKey = getCaheKey(key);
        if (fontCache.containsKey(cacheKey)) {
            font = fontCache.get(cacheKey);
        } else {
            font = super.readQFont(key);
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
        QFont font = super.readQFont(key);
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
            color = super.readQColor(key);
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
        QColor color = super.readQColor(key);
        if (color == null) {
            color = defaultSettings.readQColor(key);
        }
        colorCache.put(cacheKey, color);
        return color;
    }

    public QSize readQSize(final String key, final QSize defaultQSize) {
        final QSize size = super.readQSize(key);
        return size != null ? size : defaultQSize;
    }

    @Override
    public QSize readQSize(final String key) {
        final QSize size = super.readQSize(key);
        return size != null ? size : defaultSettings.readQSize(key);
    }

    public QPoint readQPoint(final String key, final QPoint defaultQPoint) {
        final QPoint point = super.readQPoint(key);
        return point != null ? point : defaultQPoint;
    }

    @Override
    public QPoint readQPoint(final String key) {
        final QPoint point = super.readQPoint(key);
        return point != null ? point : defaultSettings.readQPoint( key);
    }

    @Override
    public QByteArray readQByteArray(final String key) {
        final QByteArray array = super.readQByteArray(key);
        return array != null ? array : defaultSettings.readQByteArray(key);
    }

    public AlignmentFlag readAlignmentFlag(final String key, final AlignmentFlag defaultAlignmentFlag) {
        final AlignmentFlag flag = super.readAlignmentFlag(key);
        return flag != null ? flag : defaultAlignmentFlag;
    }

    @Override
    public AlignmentFlag readAlignmentFlag(final String key) {
        final AlignmentFlag flag = super.readAlignmentFlag(key);
        return flag != null ? flag : defaultSettings.readAlignmentFlag(key);
    }

    @Override
    public boolean readBoolean(final String key) {
        Boolean value = super.readBooleanObject(key);
        return value==null ? defaultSettings.readBoolean(key) : value;
    }

    @Override
    public int readInteger(final String key) {
        Integer value = super.readIntegerObject(key);
        return value==null ? defaultSettings.readInteger(key) : value;
    }

    @Override
    public double readDouble(final String key) {
        Double value = super.readDoubleObject(key);
        return value==null ? defaultSettings.readDouble(key) : value;
    }

    @Override
    public String readString(final String key) {
        final String value = super.readString(key);
        return value == null ? defaultSettings.readString(key) : value;
    }

    @Override
    public Object value(final String key) {
        if (contains(key)) {
            final Object value = super.value(key);
            return value==null ? defaultSettings.value(key) : value;
        }
        return defaultSettings.value(key);
    }

    private void checkFormatVersion(final int formatVersion) {
        if (formatVersion>0){
            if (allKeys().isEmpty()) {
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


    //////// QSettings interface

    @Override
    public void beginGroup(final String group) {
        super.beginGroup(group);
        defaultSettings.beginGroup(group);
    }

    @Override
    public void clear() {
        super.clear();
        fontCache.clear();
        colorCache.clear();
    }

    @Override
    public void endGroup() {
        super.endGroup();
        defaultSettings.endGroup();
    }

    @Override
    public void endAllGroups() {
        super.endAllGroups();

        while (defaultSettings.group() != null && !defaultSettings.group().isEmpty()) {
            defaultSettings.endGroup();
        }
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

    @Override
    public ExplorerTextOptions readTextOptions(String key) {
        final String cacheKey = getCaheKey(key);
        ExplorerTextOptions options = (ExplorerTextOptions)textOptionsCache.get(cacheKey);
        if (options==null){
            options = super.readTextOptions(key);
            textOptionsCache.put(cacheKey, options);
        }
        return options;
    }

    @Override
    public void writeTextOptions(final String key, final ITextOptions options) {
        super.writeTextOptions(key, options);
        textOptionsCache.put(getCaheKey(key), options);
    } 
    
    void clearCache(){
        textOptionsCache.clear();
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

}
