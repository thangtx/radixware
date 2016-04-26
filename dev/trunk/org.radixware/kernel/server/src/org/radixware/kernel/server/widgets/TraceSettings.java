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
package org.radixware.kernel.server.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.radixware.kernel.common.enums.EEventSeverity;
import static org.radixware.kernel.common.enums.EEventSeverity.DEBUG;
import org.radixware.kernel.server.instance.Instance;

public final class TraceSettings {

    private static Map<String, ImageIcon> ICON_CACHE = new HashMap<>();

    public static interface SettingsListener {

        public void settingsChanged();
    }
    public volatile Font traceFont;
    public volatile Color bgColor1;
    public volatile Color bgColor2;
    public volatile Color bgSelColor;
    public volatile Color debugColor;
    public volatile Color eventColor;
    public volatile Color warningColor;
    public volatile Color errorColor;
    public volatile Color alarmColor;
    public volatile Color selColor;
    public volatile boolean minimizeTraceColumns;
    private final static Map<SettingsListener, Object> listeners = Collections.synchronizedMap(new WeakHashMap<SettingsListener, Object>());
    public final static TraceSettings defaultSettings = new TraceSettings(new Font("Tahoma", Font.PLAIN, 11), true,
            new Color(236, 233, 216), new Color(220, 220, 220), Color.BLUE, Color.GRAY, Color.BLUE,
            new Color(255, 100, 100), Color.RED, Color.RED, Color.WHITE);
    static volatile TraceSettings currentSettings = defaultSettings;

    public TraceSettings(Font newTraceFont, boolean minimizeTraceColumns, Color newBgColor1, Color newBgColor2, Color newBgSelColor,
            Color newDebugColor, Color newEventColor, Color newWarningColor, Color newErrorColor,
            Color newAlarmColor, Color newSelColor) {
        traceFont = newTraceFont;
        bgColor1 = newBgColor1;
        bgColor2 = newBgColor2;
        bgSelColor = newBgSelColor;
        debugColor = newDebugColor;
        eventColor = newEventColor;
        warningColor = newWarningColor;
        errorColor = newErrorColor;
        alarmColor = newAlarmColor;
        selColor = newSelColor;
        this.minimizeTraceColumns = minimizeTraceColumns;
    }

    public static void setCurrentSettings(TraceSettings newSettings) {
        currentSettings = newSettings;
        updateAll();
    }

    public static TraceSettings getCurrentSettings() {
        return currentSettings;
    }

    public void setTraceFont(Font newFont) {
        if (!newFont.equals(traceFont)) {
            traceFont = newFont;
            updateAll();
        }
    }

    public void setBgColor1(Color newColor) {
        if (!newColor.equals(bgColor1)) {
            bgColor1 = newColor;
            updateAll();
        }
    }

    public void setBgColor2(Color newColor) {
        if (!newColor.equals(bgColor2)) {
            bgColor2 = newColor;
            updateAll();
        }
    }

    public void setBgSelColor(Color newColor) {
        if (!newColor.equals(bgSelColor)) {
            bgSelColor = newColor;
            updateAll();
        }
    }

    public void setDebugColor(Color newColor) {
        if (!newColor.equals(debugColor)) {
            debugColor = newColor;
            updateAll();
        }
    }

    public void setEventColor(Color newColor) {
        if (!newColor.equals(eventColor)) {
            eventColor = newColor;
            updateAll();
        }
    }

    public void setWarningColor(Color newColor) {
        if (!newColor.equals(warningColor)) {
            warningColor = newColor;
            updateAll();
        }
    }

    public void setErrorColor(Color newColor) {
        if (!newColor.equals(errorColor)) {
            errorColor = newColor;
            updateAll();
        }
    }

    public void setAlarmColor(Color newColor) {
        if (!newColor.equals(alarmColor)) {
            alarmColor = newColor;
            updateAll();
        }
    }

    public void setSelColor(Color newColor) {
        if (!newColor.equals(selColor)) {
            selColor = newColor;
            updateAll();
        }
    }

    public static void addListener(final SettingsListener traceList) {
        listeners.put(traceList, null);
    }

    public static void removeListener(final SettingsListener traceList) {
        listeners.remove(traceList);
    }

    public static void updateAll() {
        for (SettingsListener traceList : listeners.keySet()) {
            traceList.settingsChanged();
        }
    }

    public static int getRowHeightForCurrentFont() {
        return Math.round((float) (TraceSettings.currentSettings.traceFont.getSize()) * 24 / 11);
    }

    public static boolean getIsMinimizeColumnWidth() {
        return TraceSettings.currentSettings.minimizeTraceColumns;
    }

    public static float getIconScaleFactor() {
        float scaleFactor = 1;
        try {
            scaleFactor = Float.parseFloat(System.getProperty("org.radixware.server.gui.icon.scale"));
        } catch (Exception ex) {
            //do nothing
            Logger.getLogger(TraceSettings.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return Math.max(scaleFactor, 1f);

    }

    private static String getHashKey(final Class clazz, final String resourceName) {
        return clazz.getName() + "~" + resourceName;
    }

    public static synchronized ImageIcon loadIcon(final Class clazz, final String resourceName) {
        final String hashKey = getHashKey(clazz, resourceName);
        final ImageIcon cachedIcon = ICON_CACHE.get(hashKey);
        if (cachedIcon != null) {
            return cachedIcon;
        }
        try {
            final BufferedImage image = ImageIO.read(clazz.getResource(resourceName));
            final float scaleFactor = getIconScaleFactor();
            final ImageIcon icon = new ImageIcon(scaleFactor != 1f ? image.getScaledInstance((int) (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor), 0) : image);
            ICON_CACHE.put(hashKey, icon);
            return icon;
        } catch (Exception ex) {
            final Instance inst = Instance.get();
            if (inst == null || !inst.isShuttingDown()) {
                throw new RuntimeException(ex);
            } else {
                return null;
            }
        }
    }

    public static Icon getIconForSeverity(final EEventSeverity severity) {
        switch (severity) {
            case DEBUG:
                return loadIcon(TraceView.class, "img/debug.png");
            case EVENT:
                return loadIcon(TraceView.class, "img/event.png");
            case WARNING:
                return loadIcon(TraceView.class, "img/warning.png");
            case ERROR:
                return loadIcon(TraceView.class, "img/error.png");
            case ALARM:
                return loadIcon(TraceView.class, "img/alarm.png");
            default:
                throw new IllegalArgumentException(String.valueOf(severity));
        }
    }
}