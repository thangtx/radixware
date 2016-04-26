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


package org.radixware.wps.rwt;

import java.awt.Color;
import org.radixware.kernel.common.html.Html;


public class ColorPicker extends UIObject {

    private ColorInput input = new ColorInput();
    private String value;

    private class ColorInput extends UIObject {

        public ColorInput() {
            super(new Html("input"));
            html.layout("$RWT.picker.install");
            html.setCss("border-radius", "4px");
            html.setCss("border", "2px solid black");
            html.setCss("cursor", "pointer");
            this.html.addClass("color {hash:true, pickerFace:15, pickerClosable:false}");//this.html.addClass("color {hash:true, adjust:true, pickerFace:15, pickerClosable:true, }");//hash tag + editable + close button
            this.html.setAttr("onchange", "$RWT.picker.processColorChange");
            this.html.setAttr("role", "colorPicker");
            this.setHeight(25);
            this.setWidth(25);
        }

        @Override
        protected String[] clientScriptsRequired() {
            return new String[]{"org/radixware/wps/rwt/jscolor.js"};
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (actionName.equals("change")) {
                value = actionParam;
            }
            super.processAction(actionName, actionParam);
        }

        @Override
        protected void setClientHandler(Html target, String customEventName, String code) {
            super.setClientHandler(this.html, customEventName, code);
        }
    }

    public ColorPicker() {
        super(new Html("form"));
        html.add(input.html);
        input.setParent(this);
    }

    public String getHexColor() {
        return value;
    }

    public int getIntegerColor() {
        return Integer.getInteger(value);
    }

    public Color getColor() {
        if (value != null) {
            return colorFromStr(value);
        }
        return null;
    }

    public void setColor(String color) {
        if (color != null) {
            if (!color.equalsIgnoreCase(value)) {
                value = color;
                input.html.setAttr("colorUpdate", color);
            }
        } else {
            value = null;
            input.getHtml().setCss("background-color", null);
        }
    }

    public void resetColor(String defaultColor) {
        if (defaultColor != null && !defaultColor.equals(value)) {
            value = defaultColor;
            input.html.setAttr("colorUpdate", value);
        }
    }

    public void setColor(Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        setColor(hex);
    }

    public void setColor(int color) {
        String hex = Integer.toHexString(color);
        setColor(hex);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject res = super.findObjectByHtmlId(id);
        if (res == null) {
            res = input.findObjectByHtmlId(id);
        }
        return res;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        input.visit(visitor);
    }
}