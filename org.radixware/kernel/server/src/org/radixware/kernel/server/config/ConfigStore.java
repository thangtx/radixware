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

package org.radixware.kernel.server.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.radixware.kernel.server.widgets.TraceSettings;

import org.radixware.kernel.server.widgets.TraceView;

/**
 * Configuration manager
 *

 */
public class ConfigStore {

    public ConfigStore(final File file) {
        this.fileName = file.getAbsolutePath();
        this.props = new Properties();
        load(file);
    }
    private final Properties props;
    private final String fileName;

    /**
     * �������� �������
     */
    final public void load(final File file) {
        try {

            if (file.exists()) {
                FileInputStream ifs = new FileInputStream(file);
                try {
                    props.load(ifs);
                } finally {
                    ifs.close();
                }
            }
        } catch (FileNotFoundException ex) {
            return;
        } catch (IOException ex) {
            return;
        }

    }

    /**
     * ���������� �������
     *
     * @throws Exception ���� �� �������
     * ���������
     */
    public final void save() throws FileNotFoundException, IOException {
        final File f = new File(fileName);
        final String path = f.getParent();
        if (path != null && path.length() != 0) {
            final File dir = new File(path);
            dir.mkdirs();
        }
        final FileOutputStream ofs = new FileOutputStream(fileName);
        try {
            props.store(ofs, "");
        } finally {
            ofs.close();
        }
    }

    public final String getProperty(final String key, final String defVal) {
        return props.getProperty(key, defVal);
    }

    public final void setProperty(final String key, final String val) {
        props.setProperty(key, val);
    }
    private static final String _HEIGHT = "_height";
    private static final String _POS_X = "_pos_x";
    private static final String _POS_Y = "_pos_y";
    private static final String _WIDTH = "_width";

    public final void storeWindowBounds(final String key, final Window frame) {
        final Rectangle bounds = frame.getBounds();
        setProperty(key + _POS_X, String.valueOf(bounds.x));
        setProperty(key + _POS_Y, String.valueOf(bounds.y));
        setProperty(key + _WIDTH, String.valueOf(bounds.width));
        setProperty(key + _HEIGHT, String.valueOf(bounds.height));
    }

    public final void restoreWindowBounds(final String key, final Window frame) {
        final Rectangle bounds = frame.getBounds();
        frame.setBounds(
                new Rectangle(
                Integer.valueOf(getProperty(key + _POS_X, String.valueOf(bounds.x))).intValue(),
                Integer.valueOf(getProperty(key + _POS_Y, String.valueOf(bounds.y))).intValue(),
                Integer.valueOf(getProperty(key + _WIDTH, String.valueOf(bounds.width))).intValue(),
                Integer.valueOf(getProperty(key + _HEIGHT, String.valueOf(bounds.height))).intValue()));
    }
    private static final String _BG_COLOR_1 = "_bgColor1";
    private static final String _BG_COLOR_2 = "_bgColor2";
    private static final String _BG_SELECTION_COLOR = "_bgSelColor";
    private static final String _FG_ALARM_COLOR = "_alarmColor";
    private static final String _FG_DEBUG_COLOR = "_debugColor";
    private static final String _FG_ERROR_COLOR = "_errorColor";
    private static final String _FG_EVENT_COLOR = "_eventColor";
    private static final String _FG_SELECTION_COLOR = "_selColor";
    private static final String _FG_WARNING_COLOR = "_warningColor";
    private static final String _FONT_FAMILY = "_font_family";
    private static final String _MINIMIZE_TRACE_COLUMNS = "_minimize_trace_columns";
    private static final String _FONT_SIZE = "_font_size";
    private static final String _FONT_STYLE = "_font_style";

    public final void storeTraceListSettings(final String key) {
        setProperty(key + _MINIMIZE_TRACE_COLUMNS, String.valueOf(TraceSettings.getCurrentSettings().minimizeTraceColumns));
        setProperty(key + _FONT_FAMILY, TraceSettings.getCurrentSettings().traceFont.getFamily());
        setProperty(key + _FONT_STYLE, String.valueOf(TraceSettings.getCurrentSettings().traceFont.getStyle()));
        setProperty(key + _FONT_SIZE, String.valueOf(TraceSettings.getCurrentSettings().traceFont.getSize()));
        setProperty(key + _BG_COLOR_1, String.valueOf(TraceSettings.getCurrentSettings().bgColor1.getRGB()));
        setProperty(key + _BG_COLOR_2, String.valueOf(TraceSettings.getCurrentSettings().bgColor2.getRGB()));
        setProperty(key + _BG_SELECTION_COLOR, String.valueOf(TraceSettings.getCurrentSettings().bgSelColor.getRGB()));
        setProperty(key + _FG_DEBUG_COLOR, String.valueOf(TraceSettings.getCurrentSettings().debugColor.getRGB()));
        setProperty(key + _FG_EVENT_COLOR, String.valueOf(TraceSettings.getCurrentSettings().eventColor.getRGB()));
        setProperty(key + _FG_WARNING_COLOR, String.valueOf(TraceSettings.getCurrentSettings().warningColor.getRGB()));
        setProperty(key + _FG_ERROR_COLOR, String.valueOf(TraceSettings.getCurrentSettings().errorColor.getRGB()));
        setProperty(key + _FG_ALARM_COLOR, String.valueOf(TraceSettings.getCurrentSettings().alarmColor.getRGB()));
        setProperty(key + _FG_SELECTION_COLOR, String.valueOf(TraceSettings.getCurrentSettings().selColor.getRGB()));
    }

    public final void restoreTraceListSettings(final String key) {
        final String fontFamily = getProperty(key + _FONT_FAMILY, TraceSettings.defaultSettings.traceFont.getFamily());
        final int fontStyle = Integer.valueOf(getProperty(key + _FONT_STYLE, String.valueOf(TraceSettings.defaultSettings.traceFont.getStyle()))).intValue();
        final int fontSize = Integer.valueOf(getProperty(key + _FONT_SIZE, String.valueOf(TraceSettings.defaultSettings.traceFont.getSize()))).intValue();
        boolean minTraceCols = Boolean.valueOf(getProperty(key + _MINIMIZE_TRACE_COLUMNS, String.valueOf(TraceSettings.defaultSettings.minimizeTraceColumns)));
        TraceSettings.setCurrentSettings(
                new TraceSettings(
                new Font(fontFamily, fontStyle, fontSize),
                minTraceCols,
                Color.decode(getProperty(key + _BG_COLOR_1, String.valueOf(TraceSettings.defaultSettings.bgColor1.getRGB()))),
                Color.decode(getProperty(key + _BG_COLOR_2, String.valueOf(TraceSettings.defaultSettings.bgColor2.getRGB()))),
                Color.decode(getProperty(key + _BG_SELECTION_COLOR, String.valueOf(TraceSettings.defaultSettings.bgSelColor.getRGB()))),
                Color.decode(getProperty(key + _FG_DEBUG_COLOR, String.valueOf(TraceSettings.defaultSettings.debugColor.getRGB()))),
                Color.decode(getProperty(key + _FG_EVENT_COLOR, String.valueOf(TraceSettings.defaultSettings.eventColor.getRGB()))),
                Color.decode(getProperty(key + _FG_WARNING_COLOR, String.valueOf(TraceSettings.defaultSettings.warningColor.getRGB()))),
                Color.decode(getProperty(key + _FG_ERROR_COLOR, String.valueOf(TraceSettings.defaultSettings.errorColor.getRGB()))),
                Color.decode(getProperty(key + _FG_ALARM_COLOR, String.valueOf(TraceSettings.defaultSettings.alarmColor.getRGB()))),
                Color.decode(getProperty(key + _FG_SELECTION_COLOR, String.valueOf(TraceSettings.defaultSettings.selColor.getRGB())))));
    }
}
