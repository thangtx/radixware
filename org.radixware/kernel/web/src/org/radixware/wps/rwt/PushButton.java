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
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.utils.Utils;


public class PushButton extends ButtonBase implements IPushButton {

    private boolean isDefault;
    private AbstractMenuContainer menuContainer;
    private RwtMenu menu;
    private String relativeElementId;
    private Html lbl;
    private boolean isArrow = true;
    
    private final RwtMenu.ClosedListener menuClosedListener = new RwtMenu.ClosedListener() {
        @Override
        public void close() {
            PushButton.this.closeDropDown();
        }
    };
    
    public PushButton() {
        this(null);
    }

    public PushButton(String caption) {
        super(new Html("button"));
        html.setCss("vertical-align", "middle");
        html.setCss("text-align", "center");
        html.addClass("rwt-push-button");
        setIconWidth(13);
        setIconHeight(13);
        setMinimumWidth(100);
        html.setCss("width", "auto");//to avoid extra padding in IE
        setMinimumHeight(20);
        if (caption != null) {
            setText(caption);
        }
        setTextWrapDisabled(true);//TWRBS-3989
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        Dialog dialog = null;
        for (UIObject parent = getParent(); parent != null && dialog == null; parent = parent.getParent()) {
            if (parent instanceof Dialog) {
                dialog = (Dialog) parent;
            }
        }
        if (dialog != null) {
            if (isDefault && getText() != null) {
                dialog.setDefaultAction(getText());
            } else if (Utils.equalsNotNull(dialog.getDefaultAction(), getText())) {
                dialog.setDefaultAction((String) null);
            }
        }
    }

    @Override
    public void setMenu(IMenu menu) {
        if (menu == null) {
            if (this.menu != null) {
                this.menu.removeMenuClosedListener();
                this.getHtml().setAttr("onmousedown", null);
                this.menu = null;
                this.getHtml().setAttr("relativeElemId", null);     
                this.html.setAttr("menuid", null);
                this.html.setAttr("onclick", "default"); 
                if (lbl != null) {
                    this.html.remove(lbl);
                    lbl = null;
                }
            }
        } else {
            installMenu((RwtMenu)menu);
        }
    }
    
    public void setMenu(IMenu menu, boolean isArrow) {
        this.isArrow = isArrow;
        setMenu(menu);
    }
    
    @Override
    public IMenu getMenu() {
        return menu;
    }
    
    private void installMenu(final RwtMenu newMenu) {
        final RwtMenu currentMenu = this.menu;
        if (newMenu!=currentMenu){
            if (currentMenu!=null){
                currentMenu.removeMenuClosedListener();
            }
            newMenu.setMenuClosedListener(menuClosedListener);
            this.html.setAttr("menuid", newMenu.getHtmlId());
            newMenu.getHtml().setAttr("handler_id", this.getHtmlId());
            this.getHtml().setAttr("onmousedown", "$RWT.dropDownButton.onmousedown");
            this.getHtml().setAttr("onclick", null);
            if (isArrow) {
                addLabel();
            }
            this.menu = newMenu;
        }
    }
    
    private void addLabel() {
        lbl = new Html("a");
        lbl.addClass("rwt-button-label");
        lbl.setInnerText("\u25BC");
        lbl.setCss("display", "block");
        lbl.setCss("top", "3px");
        lbl.setCss("font-size", "8px");
        lbl.setCss("position", "absolute");
        lbl.setCss("right", "2px");
        lbl.setCss("line-height", "15px");
        this.getHtml().add(lbl);
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("close-drop-down".equals(actionName)) {
            closeDropDown();
        } else if ("expose".equals(actionName)) {
            expose();
        } else {
            super.processAction(actionName, actionParam);
        }
    }
    
    private void closeDropDown() {
        this.getHtml().setAttr("relativeElemId", null);
        menuContainer.destroy();
        this.html.setAttr("onmouseenter", null);
        this.html.setAttr("onmouseleave", null);
    }
    
    private void expose() {
        menuContainer = new AbstractMenuContainer(menu, this.findRoot());
        menuContainer.html.setAttr("minWidth", "true");
        this.getHtml().setAttr("relativeElemId", this.relativeElementId == null ? this.getHtmlId() : this.relativeElementId);                
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id); 
        if (obj != null) {
            return obj;
        } else if (menuContainer != null){
            return menuContainer.findObjectByHtmlId(id);
        }
        return null;
    }
    
    public void setRelativeElementId(String id) {
        this.relativeElementId = id;
    }
}
