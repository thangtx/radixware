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


public class PropertySettingsWidget extends SettingsWidget {

    private final List<SettingsWidget> settingsArrayList = new ArrayList<>();
    private final ColorSettingsWidget bgrnd, fgrn;

    public PropertySettingsWidget(final WpsEnvironment environment, final UIObject parent,
            final String gr,
            final String sub,
            final String n,
            final String title,
            final boolean showBgrnColor, Map<String, String> map) {
        super(environment, parent, gr, sub, n, null);
        TableLayout table = new TableLayout();
        if (showBgrnColor) {
            bgrnd = new ColorSettingsWidget(environment, parent, gr, sub, n + "/" + SettingNames.TextOptions.BCOLOR, null);
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
        } else {
            bgrnd = null;
        }

        fgrn = new ColorSettingsWidget(environment, parent, gr, sub, n + "/" + SettingNames.TextOptions.FCOLOR, null);
        if (map != null) {
            fgrn.setDefaultValue(map.get(SettingNames.TextOptions.FCOLOR));
        }
        Label l2 = new Label(e.getMessageProvider().translate("Settings Dialog", "Foreground color"));
        TableLayout.Row row1 = table.addRow();
        TableLayout.Row.Cell cell = row1.addCell();
        cell.getHtml().addClass("rwt-table-cell-label");

        cell.add(l2);
        row1.addCell().add(fgrn);

        settingsArrayList.add(fgrn);

        GroupBox groupBox = new GroupBox();
        
        groupBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        groupBox.setTitle(title);
        groupBox.setTitleAlign(Alignment.CENTER);
        groupBox.add(table);

        add(groupBox);
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(WpsSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @Override    
    @SuppressWarnings("unchecked")
    protected void setDefaultValue(Object val) {
        super.setDefaultValue(val);
        if (val != null) {
            HashMap<String, String> map = (HashMap<String, String>) val;
            if (!map.isEmpty()) {
                fgrn.setDefaultValue(map.get(SettingNames.TextOptions.FCOLOR));
                bgrnd.setDefaultValue(map.get(SettingNames.TextOptions.BCOLOR));
            }
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }
    
    protected List<SettingsWidget> getPropertyWidgets(){
        return settingsArrayList;
    }

    public String getBackgroundColor() {
        return bgrnd != null ? (bgrnd.getColor() == null ? (String) bgrnd.defaultValue : bgrnd.getColor()) : "#FFFFFF";
    }

    public String getForegroundColor() {
        return fgrn != null ? (fgrn.getColor() == null ? (String) fgrn.defaultValue : fgrn.getColor()) : "#FFFFFF";
    }

    public void setBackgroundColor(String color) {
        bgrnd.setColor(color);
    }

    public void setForegroundColor(String color) {
        fgrn.setColor(color);
    }

    public WpsTextOptions getTextOptions() {
        Color bg = null, fg = null;
        if (getBackgroundColor() != null) {
            bg = Color.decode(getBackgroundColor());
            bgrnd.setColor(bg);
        }
        if (getForegroundColor() != null) {
            fg = Color.decode(getForegroundColor());
            fgrn.setColor(fg);
        }

        if (bg != null && fg != null) {
            return WpsTextOptions.Factory.getOptions(fg, bg);
        }
        return null;
    }
}
