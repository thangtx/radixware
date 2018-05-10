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


final class ColorSettingsWidget extends SettingsWidget {

    private final ColorPicker cc = new ColorPicker();

    public ColorSettingsWidget(final WpsEnvironment env, final UIObject parent, final String gr, final String sub, final String n) {
        super(env, parent, gr, sub, n);
        createUI(null);
    }

    private void createUI(final String descr) {
        if (descr != null) {
            final HorizontalBox box = new HorizontalBox();
            add(box);
            final Label l = new Label(descr);
            box.add(l);
            l.setTextWrapDisabled(true);
            box.getHtml().setCss("padding", "5px");
            box.add(cc);
        } else {
            add(cc);
            cc.getHtml().setCss("padding", "5px");
        }
        this.setToolTip(getEnvironment().getApplication().getMessageProvider().translate("Settings Dialog", "Choose a color"));
    }
    
    String getDefaultValue(){
        final String valueAsStr = readDefaultValue();
        return valueAsStr==null || valueAsStr.isEmpty() ? "#FFFFFF" : valueAsStr;
    }

    public final String getColor() {
        return cc.getHexColor() == null ? getDefaultValue() : cc.getHexColor();
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
            color = getDefaultValue();
        }
        cc.setColor(color);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        dst.writeString(getSettingCfgName(), getColor());
    }

    @Override
    public void restoreDefaults() {
        cc.resetColor(getDefaultValue());
    }
}