/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IMenuBar;
import org.radixware.kernel.common.html.Div;

public class RwtMenuBar extends UIObject implements IMenuBar {

    private final HorizontalBoxContainer hLayout;
    private final List<RwtMenuButton> menuList = new LinkedList<>();
    private MenuContainer menuContainer;
    private final RwtMenu.ClosedListener menuClosedListener = new RwtMenu.ClosedListener() {
        @Override
        public void close() {
            RwtMenuBar.this.closeDropDown();
        }   
    };
    
    private class MenuContainer extends AbstractMenuContainer {
        public MenuContainer(RwtMenu menu, RootPanel root) {
            super(menu, root);
        }  
    }
    
    class MenuButton extends PushButton{
        private RwtMenu menu;
        public MenuButton(RwtMenu menu) {
            this.menu = menu;
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if ("openMenu".equals(actionName)) {
                if (menuContainer != null) {
                    closeDropDown();
                } else {
                    MenuButton.this.html.addClass("rwt-ui-choosable-pointed-blue");
                    RwtMenuBar.this.getHtml().setAttr("relativeElemId", MenuButton.this.getHtmlId());
                    for (UIObject pushButton : hLayout.getChildren()) {
                        if (pushButton instanceof PushButton && pushButton.isEnabled()) {
                            ((PushButton) pushButton).html.setAttr("onmouseenter", "$RWT.menuBarButton.onmouseenter");
                            ((PushButton) pushButton).html.setAttr("onmouseleave", "$RWT.menuBarButton.onmouseleave");
                        }
                    }
                    expose(RwtMenuBar.this, MenuButton.this.getHtmlId());
                }
            }
        }
    };
    
    public RwtMenuBar(UIObject parent) {
        super(new Div());
        this.getHtml().addClass("rwt-menu-bar");
        this.getHtml().setCss("position", "relative");
        this.setTop(0);
        this.setLeft(0);
        this.setSizePolicy(SizePolicy.EXPAND, SizePolicy.PREFERRED);
        this.setHeight(22);
        hLayout = new HorizontalBoxContainer();
        hLayout.setBackground(Color.WHITE);
        hLayout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        hLayout.setPersistenceKey("RwtMenuBar.hLayout");
        hLayout.getHtml().setCss("position", "absolute");
        hLayout.setPreferredHeight(parent.height());
        hLayout.setPreferredWidth(parent.width());
        hLayout.getHtml().setCss("overflow", "hidden");
        this.getHtml().add(hLayout.getHtml());
    }

    @Override
    public IMenu addSubMenu(String title) {
        final RwtMenu menu = new RwtMenu(title, menuClosedListener);
        menu.getHtml().setAttr("handler_id", this.getHtmlId());
        menu.getHtml().setAttr("relativeElem", "menuBarButton");
        PushButton tb = createPushButton(title, menu);
        hLayout.add(tb);
        this.menuList.add(new RwtMenuButton(menu, tb.getHtmlId()));
        return menu;
    }

    @Override
    public IMenu addSubMenu(Icon icon, String title) {
        final RwtMenu menu = new RwtMenu(title, menuClosedListener);
        menu.setIcon(icon);
        menu.getHtml().setAttr("handler_id", this.getHtmlId());
        menu.getHtml().setAttr("relativeElem", "menuBarButton");
        PushButton tb = createPushButton(title, menu);
        tb.setIcon(icon);
        hLayout.add(tb);
        this.menuList.add(new RwtMenuButton(menu, tb.getHtmlId()));
        return menu;
    }

    @Override
    public void addSubSeparator() {
        hLayout.addSpace();
    }

    @Override
    public void clear() {
        for (RwtMenuButton menuButton : menuList) {
            ((RwtMenu)menuButton.menu).removeParamsEditedListener();
            ((RwtMenu)menuButton.menu).removeMenuClosedListener();            
        }
        menuList.clear();
        menuContainer.clear();
        hLayout.clear();
    }

    @Override
    public void insertMenu(IMenu before, final IMenu menu) {
        ((RwtMenu) menu).getHtml().setAttr("handler_id", this.getHtmlId());
        ((RwtMenu) menu).getHtml().setAttr("relativeElem", "menuBarButton");
        for (RwtMenuButton menuButton : menuList) {
            if (menuButton.menu.equals(before)) {
                int beforeIndex = menuList.indexOf(menuButton);
                PushButton tb = createPushButton(((RwtMenu) menu).getTitle(), (RwtMenu) menu);
                hLayout.add(beforeIndex, tb);
                this.menuList.add(beforeIndex, new RwtMenuButton(menu, tb.getHtmlId()));
                setListeners((RwtMenu) menu);
                break;
            }
        }
    }

    @Override
    public Object findChild (Class<?> childClass, String childObjectName) {
        return hLayout.findChild(childClass, childObjectName);
    }
    
    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        return super.startTimer(handler);
    }

    @Override
    public void killTimer(IPeriodicalTask task) {
        super.killTimer(task);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = hLayout.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        if (menuContainer != null) {
            result = menuContainer.findObjectByHtmlId(id);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private final void expose(final RwtMenuBar menuBar, final String activeButtonId) {
        final RootPanel root = menuBar.findRoot();
        RwtMenu menu = null;
        for (RwtMenuButton rwtMenuButton : menuList) {
            if (rwtMenuButton.getButtonId().equals(activeButtonId)) {
                menuContainer = new MenuContainer((RwtMenu)rwtMenuButton.menu, root);
            }
        }
        for (RwtMenuButton rwtMenuButton : menuList) {
            if (!rwtMenuButton.getButtonId().equals(activeButtonId)) {
                menu = (RwtMenu) rwtMenuButton.menu;
                menu.html.setAttr("activeMenu", "false");
                menu = (RwtMenu) rwtMenuButton.getMenu();
                menu.getHtml().setAttr("firstLevelMenu", true);
                menu.getHtml().renew();
                menuContainer.add(menu);
            } 
        }
    }

    static class RwtMenuButton {

        private final IMenu menu;
        private final String buttonId;

        public RwtMenuButton(IMenu menu, String buttonId) {
            this.menu = menu;
            this.buttonId = buttonId;
        }

        public IMenu getMenu() {
            return menu;
        }

        public String getButtonId() {
            return buttonId;
        }
    }

    private void closeDropDown() {                    
        if (menuContainer != null) {
            menuContainer.destroy();
            menuContainer = null;
        }
        RwtMenuBar.this.getHtml().setAttr("relativeElemId", null);
        for (UIObject pushButton : hLayout.getChildren()) {
            if (pushButton instanceof PushButton) {
                ((PushButton) pushButton).html.addClass("rwt-ui-choosable-pointed-blue");
                ((PushButton) pushButton).html.removeClass("rwt-ui-choosable-pointed-blue");
                ((PushButton) pushButton).html.setAttr("onmouseenter", null);
                ((PushButton) pushButton).html.setAttr("onmouseleave", null);
            }
        }
    }

    @Override
    public void processAction(final String actionName, final String actionParam) {
        if ("close-drop-down".equals(actionName)) {
            closeDropDown();
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    private void setListeners(RwtMenu menu) {
        List<RwtMenu> menuList = menu.getMenuList();
        menu.setMenuClosedListener(menuClosedListener);
        if (!menuList.isEmpty()) {
            for (RwtMenu subMenu : menuList) {
                setListeners(subMenu);
            }
        }
    }

    private PushButton createPushButton(String title, final RwtMenu rwtMenu) {
        final MenuButton pb = new MenuButton(rwtMenu);
        pb.html.setCss("display", "table-cell");
        pb.html.setCss("vertical-align", "middle");
        pb.setParent(this);
        pb.getHtml().setCss("border", "none");
        pb.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.EXPAND);
        pb.setTitle(title);
        pb.getHtml().setAttr("menuId", rwtMenu.getHtmlId());
        pb.getHtml().setAttr("onclick", "$RWT.menuBarButton.onclick");
        pb.getHtml().setAttr("onmousedown", "$RWT.menuBarButton.onmousedown");
        pb.getHtml().addClass("$RWT.menuBarButton");
        pb.html.setCss("outline", "none");
        pb.html.setCss("position", "relative");
        pb.html.setCss("min-width", "0");
        pb.setBackground(Color.WHITE);
        rwtMenu.setParamsEditedListener(new RwtMenu.ParamsEditedListener() {

            @Override
            public void changeParams(Icon icon, String title, boolean isEnabled) {
                pb.setIcon(icon);
                pb.setTitle(title);
                pb.setEnabled(isEnabled);
            }
        });
        return pb;
    }
    
}
