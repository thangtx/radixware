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

import org.radixware.kernel.common.html.Html;
import java.awt.Color;
import java.util.List;
import org.radixware.kernel.common.html.Div;



public class GroupBox extends Container {

    private final Html legend = new Html("label");
    private final Html header = new Div();
    private boolean isHeaderVisible = true;
    private boolean isFrameVisible = true;
    private final Container fieldset = new Container();

    public GroupBox() {
        super();
        
        html.addClass("rwt-ui-border-left");
        html.addClass("rwt-ui-border-bottom");
        html.addClass("rwt-ui-border-right");
        html.setCss("border-radius", "4px 4px 0px 0px");
        html.add(header);
        
        header.setCss("left", "0");
        header.setCss("top", "0");
        header.setCss("overflow", "hidden");
        header.setCss("border-radius", "4px 4px 0px 0px");
        header.setCss("height", "21px");
        header.addClass("rwt-ui-tab-layout-header");
        header.addClass("rwt-ui-border-top");
        header.setCss("background-color", "#ddd");
        
        header.add(legend);
        header.setAttr("role", "headerGroup");
        
        this.html.add(fieldset.html);
        fieldset.setParent(GroupBox.this);        

        legend.setCss("padding-left", "5px");
        legend.setCss("padding-right", "5px");
        legend.setCss("white-space", "nowrap");
        legend.setCss("padding-top", "3px");
        legend.setCss("display", "block");
        
        fieldset.html.setCss("left", "0px");        
        fieldset.html.setCss("box-sizing", "content-box");     
        fieldset.html.setAttr("role", "group");
        fieldset.html.addClass("rwt-ui-border-top");
        fieldset.setVSizePolicy(SizePolicy.MINIMUM_EXPAND);
        
        html.layout("$RWT.groupBox.layout");
        setHSizePolicy(SizePolicy.EXPAND);
    }

    public String getTitle() {
        return legend.getInnerText();
    }

    public void setTitle(final String title) {
        this.legend.setInnerText(title);
        setHeaderVisible(title!=null && !title.isEmpty());
    }
    
    private void setHeaderVisible(final boolean isVisible){
        if (isVisible!=isHeaderVisible){
            header.setCss("display", isVisible ? null : "none");
            if (isVisible){
                if (getHSizePolicy()==SizePolicy.EXPAND){
                    fieldset.html.setCss("top", "21px");
                }
            }else{
                fieldset.html.setCss("top", "0px");
            }
            isHeaderVisible = isVisible;
        }        
    }
    
    public void setFrameVisible(final boolean isVisible){
        if (isVisible!=isFrameVisible){
            if (isVisible){
                html.addClass("rwt-ui-border-left");
                html.addClass("rwt-ui-border-bottom");
                html.addClass("rwt-ui-border-right");
                header.addClass("rwt-ui-border-top");
                fieldset.html.addClass("rwt-ui-border-top");
            }else{
                html.removeClass("rwt-ui-border-left");
                html.removeClass("rwt-ui-border-bottom");
                html.removeClass("rwt-ui-border-right");
                header.removeClass("rwt-ui-border-top");
                fieldset.html.removeClass("rwt-ui-border-top");                
            }
            isFrameVisible = isVisible;
        }
    }

    public void setTitleForeground(Color c) {
        header.setCss("color", c == null ? null : color2Str(c));
    }

    public Color getTitleForeground() {
        final String css = legend.getCss("color");
        if (css == null) {
            return null;
        } else {
            return colorFromStr(css);
        }
    }

    @Override
    public final void setHSizePolicy(final SizePolicy hSizePolicy) {
        switch (hSizePolicy){
            case MINIMUM_EXPAND:
                header.setCss("position", "relative");
                header.setCss(Html.CSSRule.WIDTH, null);
                legend.setCss(Html.CSSRule.WIDTH, null);
                fieldset.html.setCss("position", "relative");
                fieldset.html.setCss("top", "0px");
                fieldset.setHSizePolicy(hSizePolicy);
                super.setHSizePolicy(hSizePolicy);
                break;
            case EXPAND:
                header.setCss("position", "absolute");
                header.setCss(Html.CSSRule.WIDTH, "100%");
                legend.setCss(Html.CSSRule.WIDTH, "100%");
                fieldset.html.setCss("position", "absolute");
                fieldset.html.setCss("top", "21px");
                fieldset.setHSizePolicy(hSizePolicy);           
                super.setHSizePolicy(hSizePolicy);
                break;
            default:
                super.setHSizePolicy(hSizePolicy);
        }
    }
       
    public Color getTitleBackground() {
        String css = legend.getCss("background-color");
        if (css == null) {
            return null;
        } else {
            return colorFromStr(css);
        }
    }    

    public void setTitleBackground(Color c) {
        header.setCss("background-color", c == null ? null : color2Str(c));
    }

    public Alignment getTitleAlign() {
        String val = legend.getCss("text-align");
        if ("center".equals(val)) {
            return Alignment.CENTER;
        } else if ("right".equals(val)) {
            return Alignment.RIGHT;
        } else {
            return Alignment.LEFT;
        }
    }

    public void setTitleAlign(Alignment a) {
        switch (a) {
            case CENTER:
                legend.setCss("text-align", "center");
                break;
            case LEFT:
                legend.setCss("text-align", null);
                break;
            case RIGHT:
                legend.setCss("text-align", "right");
                break;
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            return fieldset.findObjectByHtmlId(id);
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        fieldset.visit(visitor);
    }

    @Override
    public void add(UIObject child) {
        fieldset.add(child);
    }

    @Override
    public void add(int index, UIObject child) {
        fieldset.add(index, child);
    }

    @Override
    public void clear() {
        fieldset.clear();
    }

    @Override
    public List<UIObject> getChildren() {
        return fieldset.getChildren();
    }

    @Override
    public void remove(UIObject child) {
        fieldset.remove(child);
    }
}
