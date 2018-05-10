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
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.Slider;
import org.radixware.wps.rwt.UIObject;


final class AlternativeBackgroundWidget extends SettingsWidget {

    private final Slider slider;
    private final String title;
    private final Container color1;
    private final Container color2;

    public AlternativeBackgroundWidget(final WpsEnvironment env,
            final UIObject parent,
            final String gr,
            final String sub,
            final String n,
            final String title) {
        super(env, parent, gr, sub, n);
        slider = new Slider();
        color1 = new Container();
        color2 = new Container();
        this.title = title;
        createUI();
    }

    private void createUI() {
        final GroupBox b = new GroupBox();
        add(b);

        final Container sliderContainer = new Container();
        b.add(0, sliderContainer);

        b.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        b.setHeight(125);

        b.setTitle(title);
        b.setTitleAlign(org.radixware.wps.rwt.Alignment.CENTER);

        sliderContainer.add(slider);

        final Container colorContainer = new Container();
        colorContainer.getHtml().setCss("border", "2px solid black");
        colorContainer.getHtml().setCss("margin-bottom", "5px");
        colorContainer.setWidth(25);

        colorContainer.add(color1);
        color1.setWidth(25);
        color1.setHeight(25);
        colorContainer.getHtml().setCss("position", "relative");
        colorContainer.getHtml().setCss("left", "10px");

        final Container colorContainer2 = new Container();
        colorContainer2.getHtml().setCss("border", "2px solid black");
        colorContainer2.setWidth(25);
        colorContainer2.getHtml().setCss("position", "relative");
        colorContainer2.getHtml().setCss("left", "10px");
        color2.getHtml().setAttr("role", "alterColor");
        colorContainer2.add(color2);
        color2.setWidth(25);
        color2.setHeight(25);
        color2.setBackground(Color.gray);

        b.add(colorContainer);
        b.add(colorContainer2);

        this.getHtml().layout("$RWT.altColorWidget.layout");
        slider.setStep(1);
        slider.showRange(false);

        this.setWidth(200);
        this.setHeight(150);
    }
    
    private int getDefaultValue(){
        final String valueAsStr = readDefaultValue();
        if (valueAsStr==null || valueAsStr.isEmpty()){
            return 4;
        }else{
            try{
                return Integer.parseInt(valueAsStr);
            }catch(NumberFormatException ex){
                return 4;
            }
        }
    }

    @Override
    public void readSettings(WpsSettings src) {
        int val = src.readInteger(getSettingCfgName(), getDefaultValue());
        if (src.getValue(getSettingCfgName()) == null || "".equals(src.getValue(getSettingCfgName()))) {
            val = getDefaultValue();
        }
        slider.setValue(val);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        dst.writeInteger(getSettingCfgName(), slider.getValue());
    }

    @Override
    public void restoreDefaults() {
        slider.setValue(getDefaultValue());
    }
}
