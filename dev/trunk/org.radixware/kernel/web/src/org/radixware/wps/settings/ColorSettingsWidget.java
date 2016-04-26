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
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.ColorPicker;
import org.radixware.wps.rwt.HorizontalBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;


public class ColorSettingsWidget extends SettingsWidget {

    private ColorPicker cc;
    private WpsEnvironment env;

    public ColorSettingsWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n, Object defVal) {
        this(env, parent, gr, sub, n, null, defVal);
    }

    public ColorSettingsWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n, String descr, Object defVal) {
        super(env, parent, gr, sub, n, defVal);
        this.env = env;
        this.name = n;
        this.defaultValue = (String) defVal;
        createUI(descr);
    }

    private void createUI(String descr) {
        cc = new ColorPicker();
        //defaultColor = getDefaultSettings().readString(getSettingCfgName());
        //cc.setParent(this);
        if (descr != null) {
            HorizontalBox box = new HorizontalBox();
            add(box);
            Label l = new Label(descr);
            box.add(l);
            l.setTextWrapDisabled(true);
            box.getHtml().setCss("padding", "5px");
            box.add(cc);
        } else {
            add(cc);
            cc.getHtml().setCss("padding", "5px");
        }
        this.setToolTip(env.getApplication().getMessageProvider().translate("Settings Dialog", "Choose a color"));
    }

    public final String getColor() {
        return cc.getHexColor() == null ? (String) defaultValue : cc.getHexColor();
    }

    public void setColor(Color color) {
        if (cc.getColor() != color) {
            cc.setColor(color);
        }
    }

    public void setColor(String color) {
        cc.setColor(color);
    }

    @Override
    public void readSettings(WpsSettings src) {
        String color = src.readString(getSettingCfgName());
        if (color == null || "".equals(color)) {
            color = defaultValue == null ? "#FFFFFF" : (String) defaultValue;
        }
        cc.setColor(color);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        dst.writeString(getSettingCfgName(), getColor());
    }

    @Override
    public void restoreDefaults() {
        cc.resetColor(defaultValue == null ? "#FFFFFF" : (String) defaultValue);
    }
}