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
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.views.RwtAction.IActionPresenter;


public class ToolButton extends ButtonBase implements IActionPresenter, IToolButton {

    private final Action action;
    private AbstractMenuContainer menuContainer;
    private RwtMenu menu;
    private RwtMenu actionMenu;
    private PushButton triangleButton;
    private ToolButtonPopupMode popupMode = ToolButtonPopupMode.DelayedPopup;
    private boolean autoRaise;
    private Html lbl;
    private WpsIcon icon = WsIcons.SPIN_DOWN;
    private final RwtMenu.ClosedListener menuClosedListener = new RwtMenu.ClosedListener() {
        @Override
        public void close() {
            ToolButton.this.closeDropDown();
        }
    };
    
    public ToolButton() {
        this(new Div(), null);
    }

    public ToolButton(final Action action) {
        this(new Div(), action);
    }

    protected ToolButton(final Html element) {
        this(element, null);
    }
    
    private ToolButton(final Html element, final Action action) {
        super(element);
        getContentElement().setCss("width", "100%");
        getContentElement().setCss("height", "100%");
        html.addClass("rwt-tool-button");
        html.setAttr("tabIndex", "-1");
        html.layout("$RWT.toolButton.layout");
        html.setAttr("rwt_f_minsize", "$RWT.toolButton.size");
        setHeight(25);
        setPreferredHeight(25);
        html.setCss("float", "left");
        html.markAsChoosable();
        this.action = action;
        if (action!=null){
            actionStateChanged(action);
            action.addActionStateListener(new Action.ActionStateListener() {
                @Override
                public void changed(final Action action) {
                    actionStateChanged(action);
                }
            });
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    ToolButton.this.action.trigger();
                }
            });

            final String actionName = action.getObjectName();
            if (actionName != null && !actionName.isEmpty()) {
                setObjectName("rx_tbtn_" + actionName);
            }
        }
    }

    @Override
    protected Html createLabelElement() {
        final Html le = new Html("a");
        le.setCss("cursor", "pointer");
        le.setCss("white-space", "nowrap");
        return le;
    }

    @Override
    public final void actionStateChanged(final Action a) {
        setIcon(a.getIcon());
        setToolTip(a.getToolTip());
        setVisible(a.isVisible());
        setEnabled(a.isEnabled());
        setText(a.getText());        
        if (actionMenu!=a.getActionMenu()){
            if (this.menu==null){
                if (a.getActionMenu()==null){
                    if (actionMenu!=null){
                        actionMenu.removeMenuClosedListener();
                        menuContainer.clear();
                        menuContainer = null;
                        removePopUp();
                    }
                }else{
                    installMenu((RwtMenu)a.getActionMenu());                    
                }
            }
            actionMenu = (RwtMenu)a.getActionMenu();
        }
    }

    public Action getAction() {
        return action;
    }

    @Override
    public ToolButtonPopupMode getPopupMode() {
        return popupMode;
    }

    @Override
    public boolean isAutoRaise() {
        return autoRaise;
    }

    @Override
    public void setAutoRaise(boolean isAutoRaise) {
        autoRaise = isAutoRaise;
    }

    @Override
    public void setPopupMode(final ToolButtonPopupMode mode) {
        if (mode != popupMode && mode!=null) {
            if (getActualMenu() == null) {
                popupMode = mode;
            } else {
                removePopUp();
                popupMode = mode;
                applyPopUp(getActualMenu());
            }
        }
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        if (triangleButton != null) {
            triangleButton.setEnabled(isEnabled);
        }
    }
    
    private RwtMenu getActualMenu(){
        return menu==null ? actionMenu : menu;
    }

    @Override
    public void setMenu(final IMenu menu) {
        if (menu == null) {
            if (this.menu != null) {
                this.menu.removeMenuClosedListener();
                this.menu = null;
                if (actionMenu==null){
                    menuContainer.clear();
                    menuContainer = null;
                    removePopUp();
                }
            }
        } else {
            installMenu((RwtMenu)menu);
            this.menu = (RwtMenu) menu;
        }
    }
    
    private void installMenu(final RwtMenu newMenu){
        final RwtMenu currentMenu = getActualMenu();
        if (newMenu!=currentMenu){
            if (currentMenu!=null){
                currentMenu.removeMenuClosedListener();
            }
            newMenu.setMenuClosedListener(menuClosedListener);
            this.html.setAttr("menuid", newMenu.getHtmlId());
            newMenu.getHtml().setAttr("handler_id", this.getHtmlId());
            if (currentMenu==null){
                applyPopUp(newMenu);
            }
        }
    }
    
    @Override
    public IMenu getMenu() {
        return menu;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        final UIObject obj = super.findObjectByHtmlId(id);
        if (obj == null) {
            if (triangleButton != null && triangleButton.getHtmlId().toString().equals(id)) {
                return triangleButton;
            } else if (menuContainer != null) {
                return menuContainer.findObjectByHtmlId(id);
            }
        } else {
            return obj;
        }
        return null;
    }

    @Override
    public void processAction(final String actionName, final String actionParam) {
        if ("close-drop-down".equals(actionName)) {
            closeDropDown();
        } else if ("expose".equals(actionName)) {
            expose();
        } else if ("doAction".equals(actionName)) {
            this.action.trigger();
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    private void closeDropDown() {
        this.getHtml().setAttr("relativeElemId", null);
        menuContainer.destroy();
    }

    private void expose() {
        menuContainer = new AbstractMenuContainer(getActualMenu(), this.findRoot());
        menuContainer.html.setAttr("minWidth", "true");
        if (triangleButton != null) {
            triangleButton.getHtml().setAttr("relativeElemId", this.getHtmlId());        
        } else {
            this.getHtml().setAttr("relativeElemId", this.getHtmlId());                
        }
    }

    private void addTriangleButton(RwtMenu menu) {
        triangleButton = new PushButton();
        triangleButton.setParent(this);
        triangleButton.setMenu(menu, false);
        triangleButton.setRelativeElementId(this.getHtmlId());
        triangleButton.setMinimumWidth(5);
        triangleButton.html.setAttr("menuId", menu.getHtmlId());
        triangleButton.getHtml().setAttr("onmousedown", "$RWT.dropDownButton.onmousedown");
        triangleButton.setText("\u25BC");
        triangleButton.getHtml().setCss("margin-left", "5px");
        triangleButton.html.addClass("$RWT.toolButton");
        triangleButton.html.setCss("float", "right");
        triangleButton.html.setCss("display", "inline");
        triangleButton.html.setCss("background-color", "transparent");
        triangleButton.html.setCss("position", "relative");
        triangleButton.setEnabled(this.isEnabled());
        this.html.add(triangleButton.html);
    }

    private void addLabel() {
        lbl = new Html("a");
        lbl.addClass("rwt-button-label");
        lbl.setInnerText("\u25BC");
        lbl.setCss("font-size", "8px");
        this.getHtml().add(lbl);
    }

    private void applyPopUp(RwtMenu menu) {
        if (this.popupMode.equals(IToolButton.ToolButtonPopupMode.MenuButtonPopup)) {
            addTriangleButton(menu);
            this.html.removeClass("rwt-tool-button-special");
            this.html.addClass("rwt-tool-button-menu-popup");
            this.html.setAttr("onclick", "default");
        } else if (this.popupMode.equals(IToolButton.ToolButtonPopupMode.InstantPopup)) {
            this.html.addClass("rwt-tool-button-special");
            this.getHtml().setAttr("onmousedown", "$RWT.dropDownButton.onmousedown");
            addLabel();
            this.getHtml().setAttr("onclick", null);
        } else if (this.popupMode.equals(IToolButton.ToolButtonPopupMode.DelayedPopup)) {
            this.html.addClass("rwt-tool-button-special");
            this.getHtml().setAttr("onmousedown", "$RWT.delayedPopupBtn.onmousedown");
            addLabel();
            this.html.setAttr("onclick", "default");
        }
    }

    private void removePopUp() {
        if (this.popupMode == ToolButtonPopupMode.MenuButtonPopup) {
            this.html.remove(triangleButton.html);
            triangleButton = null;
        } else if (this.popupMode == ToolButtonPopupMode.DelayedPopup || this.popupMode == ToolButtonPopupMode.InstantPopup) {
            this.html.remove(lbl);
            lbl = null;
        }
        this.html.removeClass("rwt-tool-button-special");
        this.html.setAttr("onclick", "default");
        this.getHtml().setAttr("onmousedown", null);
        this.getHtml().removeClass("rwt-tool-button-menu-popup");
    }
}
