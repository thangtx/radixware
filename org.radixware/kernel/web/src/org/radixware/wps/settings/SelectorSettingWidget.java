/*
 *  Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *  This Source Code is distributed WITHOUT ANY WARRANTY; including any
 *  implied warranties but not limited to warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 *  License, v. 2.0. for more details.
 */
package org.radixware.wps.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.text.WpsTextOptions;

public class SelectorSettingWidget extends SettingsWidget {

    private final List<SettingsWidget> settingsArrayList = new ArrayList<>();
    private final ColorSettingsWidget bgrnd, df, ndf;

    public SelectorSettingWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n, final String title, Map<String, String> map) {

        super(env, parent, gr, sub, title, null);
        TableLayout table = new TableLayout();
        bgrnd = new ColorSettingsWidget(env, parent, gr, sub, n + "/" + SettingNames.TextOptions.BCOLOR, null);
        if (map != null) {
            bgrnd.setDefaultValue(map.get(SettingNames.TextOptions.BCOLOR));
        }
        Label l1 = new Label(e.getMessageProvider().translate("Settings Dialog", "Background color"));

        TableLayout.Row row0 = table.addRow();
        TableLayout.Row.Cell cell = row0.addCell();
        cell.getHtml().addClass("rwt-table-cell-label");
        cell.add(l1);

        row0.addCell(1).add(bgrnd);
        settingsArrayList.add(bgrnd);

        final Label l = new Label(e.getMessageProvider().translate("Settings Dialog", "Foreground color"));
        TableLayout.Row row = table.addRow();
        row.addCell();
        row.addCell().add(l);
        
        df = new ColorSettingsWidget(env, parent, gr, sub, n + "/" + SettingNames.TextOptions.FCOLOR, null);
        if (map != null) {
            df.setDefaultValue(map.get(SettingNames.TextOptions.FCOLOR));
        }

        Label l2 = new Label(e.getMessageProvider().translate("Settings Dialog", "Value defined"));
        TableLayout.Row row1 = table.addRow();
        TableLayout.Row.Cell cell1 = row1.addCell();
        cell1.getHtml().addClass("rwt-table-cell-label");

        cell1.add(l2);
        row1.addCell().add(df);

        settingsArrayList.add(df);

        Label l3 = new Label(e.getMessageProvider().translate("Settings Dialog", "Value undefined"));
        TableLayout.Row row2 = table.addRow();
        TableLayout.Row.Cell cell2 = row2.addCell();
        cell2.getHtml().addClass("rwt-table-cell-label");

        ndf = new ColorSettingsWidget(env, parent, gr, sub, n + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED, null);
        if (map != null) {
            ndf.setDefaultValue(SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED);
        }

        cell2.add(l3);
        row2.addCell().add(ndf);

        settingsArrayList.add(ndf);

        GroupBox groupBox = new GroupBox();

        groupBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        groupBox.setTitle(title);
        groupBox.setTitleAlign(Alignment.CENTER);
        groupBox.add(table);

        add(groupBox);
    }

    @Override
    public void readSettings(WpsSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    protected List<SettingsWidget> getPropertyWidgets() {
        return settingsArrayList;
    }

    public String getBackgroundColor() {
        return bgrnd != null ? (bgrnd.getColor() == null ? (String) bgrnd.defaultValue : bgrnd.getColor()) : "#FFFFFF";
    }

    public String getDefinedColor() {
        return df != null ? (df.getColor() == null ? (String) df.defaultValue : df.getColor()) : "#FFFFFF";
    }

    public String getUndefinedColor() {
        return ndf != null ? (ndf.getColor() == null ? (String) ndf.defaultValue : ndf.getColor()) : "#B3B2B1";
    }

    public void setBackgroundColor(String color) {
        bgrnd.setColor(color);
    }

    public void setDefinedColor(String color) {
        df.setColor(color);
    }

    public void setUndefinedColor(String color) {
        ndf.setColor(color);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setDefaultValue(Object val) {
        super.setDefaultValue(val);
        if (val != null) {
            HashMap<String, String> map = (HashMap<String, String>) val;
            if (!map.isEmpty()) {
                df.setDefaultValue(map.get(SettingNames.TextOptions.FCOLOR));
                ndf.setDefaultValue(map.get(SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED));
                bgrnd.setDefaultValue(map.get(SettingNames.TextOptions.BCOLOR));
            }
        }
    }

    public WpsTextOptions getTextOptions(final boolean valueDefined) {
        Color bg = null, def = null, ndef = null;
        if (getBackgroundColor() != null) {
            bg = Color.decode(getBackgroundColor());
            bgrnd.setColor(bg);
        }
        if (getDefinedColor() != null) {
            def = Color.decode(getDefinedColor());
            df.setColor(def);
        }
        if (getUndefinedColor() != null) {
            ndef = Color.decode(getUndefinedColor());
            ndf.setColor(ndef);
        }
        if (bg != null && def != null && ndef != null) {
            if (valueDefined) {
                return WpsTextOptions.Factory.getOptions(def, bg);
            } else {
                return WpsTextOptions.Factory.getOptions(ndef, bg);
            }
        }
        return null;
    }

}
