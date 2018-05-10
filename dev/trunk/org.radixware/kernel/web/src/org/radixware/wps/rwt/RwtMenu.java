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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.views.RwtAction;

public class RwtMenu extends UIObject implements IMenu {
    private int iconWidth = 15;
    private int iconHeight = 15;
    private Map<Integer, Action> rowActionMap = new HashMap<>();  

    static interface ClosedListener {

        public void close();
    }

    public static interface ParamsEditedListener {

        public void changeParams(Icon icon, String title, boolean isEnabled);
    }
    
    private static class MenuItem{
        
        public static final MenuItem SEPARATOR = new MenuItem();
        
        private final Action action;
        private final IMenu menu;
        private final boolean isSeparator;
        
        public MenuItem(final Action action){
            this.action = action;
            menu = null;
            isSeparator = false;
        }
        
        public MenuItem(final IMenu menu){
            this.menu = menu;
            action = null;
            isSeparator = false;
        }
        
        private MenuItem(){
            menu = null;
            action = null;
            isSeparator = true;
        }

        public Action getAction() {
            return action;
        }

        public IMenu getMenu() {
            return menu;
        }
        
        public boolean isSeparator(){
            return isSeparator;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 13 * hash + Objects.hashCode(this.action);
            hash = 13 * hash + Objects.hashCode(this.menu);
            hash = 13 * hash + (this.isSeparator ? 1 : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MenuItem other = (MenuItem) obj;
            return action==other.action && menu==other.menu && isSeparator==other.isSeparator;
        }
        
    }
    
    private final TableLayout internalTableBox = new TableLayout();
    private final List<MenuItem> items = new ArrayList<>();
    private String title;
    private Icon icon;
    private ClosedListener closedListener;
    private ParamsEditedListener paramsEditedListener;   

    public RwtMenu(final String title) {
        this(title, (ClosedListener) null);
    }

    public RwtMenu(final String title, final ClosedListener closedListener) {
        this(title, null, closedListener);
    }

    public RwtMenu(String title, Icon icon) {
        this(title, icon, null);
    }

    public RwtMenu(String title, Icon icon, ClosedListener closedListener) {
        super(new Div());
        this.getHtml().addClass("rwt-list-box");
        this.getHtml().addClass("rwt-menu");
        this.getHtml().setCss("position", "absolute");
        this.getHtml().setAttr("active_element", null);
        internalTableBox.setParent(this);
        this.getHtml().add(internalTableBox.getHtml());
        this.title = title;
        this.icon = icon;
        this.closedListener = closedListener;
    }

    public RwtMenu() {
        this(null, null, null);
    }

    @Override
    public IMenu addSubMenu(String title) {
        return addSubMenu(null, title);
    }

    @Override
    public IMenu addSubMenu(Icon icon, String title) {
        final RwtMenu menu = new RwtMenu(title, icon, closedListener);
        items.add(new MenuItem(menu));
        return menu;
    }

    @Override
    public void addSubSeparator() {
        items.add(MenuItem.SEPARATOR);
    }

    @Override
    public void insertMenu(final Action before, final IMenu menu) {
        if (before==null){
            items.add(new MenuItem(menu));
        }else{
            final int index = items.indexOf(new MenuItem(before));
            if (index>=0){
                items.add(index, new MenuItem(menu));
            }
        }
    }

    @Override
    public void insertMenu(final IMenu before, final IMenu menu) {
        if (before==null){
            items.add(new MenuItem(menu));
        }else{
            final int index = items.indexOf(new MenuItem(before));
            if (index>=0){
                items.add(index, new MenuItem(menu));
            }
        }        
    }

    @Override
    public void insertSeparator(final IMenu before) {
        if (before == null) {
            addSubSeparator();
        } else {
            final int index = items.indexOf(new MenuItem(before));
            if (index>=0){
                items.add(index, MenuItem.SEPARATOR);
            }
        }
    }
    
    @Override
    public void insertAction(final Action before, final Action action) {
        if (before==null){
            addAction(action);
        }else{
            final int index = items.indexOf(new MenuItem(before));
            if (index>=0){
                items.add(index, new MenuItem(action));
            }
        }
    }

    public void insertAfterAction(final Action after, final Action action) {
        if (after==null){
            items.add(0, new MenuItem(action));
        }else{
            final int index = items.indexOf(new MenuItem(after));
            if (index>=0){
                items.add(index+1, new MenuItem(action));
            }
        }
    }

    @Override
    public void insertSeparator(final Action before) {
        if (before == null) {
            this.addSubSeparator();
        }else{
            final int index = items.indexOf(new MenuItem(before));
            if (index>=0){
                items.add(index, MenuItem.SEPARATOR);
            }            
        }
    }
    
    @Override
    public void addAction(final Action action) {
        items.add(new MenuItem(action));
    }

    @Override
    public void removeAction(final Action action) {
        final MenuItem item = new MenuItem(action);
        for (int i=items.size()-1; i>=0; i--){
            if (item.equals(items.get(i))){
                items.remove(i);
            }
        }
    }

    @Override
    public void removeAllActions() {
        for (int i=items.size()-1; i>=0; i--){
            if (items.get(i).getAction()!=null){
                items.remove(i);
            }
        }
    }

    @Override
    public Action[] getActions() {
        final List<Action> actionList = new LinkedList<>();
        Action action;
        for (int i=items.size()-1; i>=0; i--){
            action = items.get(i).getAction();
            if (action!=null){
               actionList.add(action);
            }
        }
        return actionList.toArray(new Action[0]);
    }    

    @Override
    public void clear() {
        internalTableBox.clearRows();
        items.clear();
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
        if (paramsEditedListener != null) {
            paramsEditedListener.changeParams(icon, title, this.isEnabled());
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setIcon(final Icon icon) {
        this.icon = icon;
        if (paramsEditedListener != null) {
            paramsEditedListener.changeParams(icon, title, this.isEnabled());
        }
    }

    public void setIconSize(int size) {
        this.iconHeight = this.iconWidth = size;
    }
    
    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);
        if (paramsEditedListener != null) {
            paramsEditedListener.changeParams(icon, title, this.isEnabled());
        }
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public void disconnect(IWidget w) {
    }

    void afterClose(RwtMenuBar menuBar) {
        if (this != null) {
            final RootPanel root = menuBar.findRoot();
            root.remove(this);
        }
    }

    List<RwtMenu> getMenuList() {
        final List<RwtMenu> menuList = new LinkedList<>();
        IMenu menu;
        for (MenuItem item: items){
            menu = item.getMenu();
            if (menu instanceof RwtMenu){
                menuList.add((RwtMenu)menu);
            }
        }
        return menuList;
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = internalTableBox.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        return result;
    }

    @Override
    public void visit(final Visitor visitor) {
        super.visit(visitor);
        internalTableBox.visit(visitor);
    }        

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("close-sub-menu".equals(actionName)) {
            closedListener.close();
        } else if ("menuItemClicked".equals(actionName)) {
            Action action = rowActionMap.get(Integer.valueOf(actionParam));
            if (action.isEnabled()) {
                closedListener.close();
                action.trigger();
            }
        }
        super.processAction(actionName, actionParam); 
    }

    void setMenuClosedListener(ClosedListener listener) {
        this.closedListener = listener;
    }

    void removeMenuClosedListener() {
        this.closedListener = null;
    }

    public void setParamsEditedListener(ParamsEditedListener listener) {
        this.paramsEditedListener = listener;
    }

    public void removeParamsEditedListener() {
        this.paramsEditedListener = null;
    }

    private void setupListeners(RwtMenu menu) {
        final List<RwtMenu> menuList = menu.getMenuList();
        menu.setMenuClosedListener(this.closedListener);
        if (!menuList.isEmpty()) {
            for (RwtMenu subMenu : menuList) {
                setupListeners(subMenu);
            }
        }
    }

    private TableLayout.Row createMenuItem(final RwtMenu menu, final String text, final Icon icon) {        
        final String actualText = text==null || text.isEmpty() ? menu.getTitle() : text;
        final TableLayout.Row tableBoxItem = internalTableBox.addRow();
        tableBoxItem.addCell();
        tableBoxItem.addCell();
        tableBoxItem.addCell();
        tableBoxItem.html.addClass("rwt-menu-item");
        setIcon(tableBoxItem, icon==null ? menu.getIcon() : icon);
        setText(tableBoxItem, actualText);
        tableBoxItem.setEnabled(menu.isEnabled());
        tableBoxItem.setVisible(menu.isVisible());
        
        Label lbl = new Label("\u25B6");
        lbl.html.setCss("float", "right");
        lbl.setVAlign(Alignment.MIDDLE);        
        lbl.setFont(lbl.getFont().changeSize(tableBoxItem.getFont().getSize()));
        lbl.setTabIndex(-1);
        lbl.html.addClass("rwt-non-choosable");
        tableBoxItem.getCell(2).add(lbl); 
        
        tableBoxItem.setParent(internalTableBox);
        tableBoxItem.setTabIndex(-1);
        tableBoxItem.html.setAttr("onmouseenter", "$RWT.menuButton.mouseenter");
        tableBoxItem.html.setAttr("onmouseleave", "$RWT.menuButton.mouseleave");
        tableBoxItem.html.setAttr("menuId", menu.getHtmlId());   
        setupListeners(menu);
        return tableBoxItem;
    }

    private TableLayout.Row createAction(final Action action) {
        TableLayout.Row actionItem = internalTableBox.addRow();
        actionItem.addCell();
        actionItem.addCell(); 
        actionItem.addCell(); 
        actionItem.html.addClass("rwt-action-item");
        setText(actionItem, getActionText(action));
        actionItem.setToolTip(action.getToolTip());
        if (action.isCheckable() && action.getIcon() == null) {
            setCheckBox(actionItem, action.isChecked());
        } else {
            setIcon(actionItem, action.getIcon());
            if (action.isCheckable() && action.isChecked()) {
                actionItem.getCell(0).getHtml().setCss("border-left", "2px solid grey");
                actionItem.getCell(0).getHtml().setCss("border-top", "2px solid grey");
                actionItem.getCell(0).getHtml().setCss("border-right", "1px solid grey");
                actionItem.getCell(0).getHtml().setCss("border-bottom", "1px solid grey");
                
            }
        }
        actionItem.setVisible(action.isVisible());
        actionItem.setEnabled(action.isEnabled());
        actionItem.html.setAttr("onmouseenter", "$RWT.menuButton.mouseenter");
        actionItem.html.setAttr("onmouseleave", "$RWT.menuButton.mouseleave");
        rowActionMap.put(internalTableBox.getRows().indexOf(actionItem), action);
        setupListItemObjectName(actionItem, action);
        actionItem.getHtml().setAttr("onclick", "$RWT.menu.itemClick");
        return actionItem;
    }    

    private void createSeparatorItem() {
        TableLayout.Row separatorItem = internalTableBox.addRow();
        separatorItem.addCell().html.setCss("border-bottom", "1px solid black");
        separatorItem.addCell().html.setCss("border-bottom", "1px solid black");
        separatorItem.addCell().html.setCss("border-bottom", "1px solid black");
        separatorItem.html.removeClass("rwt-ui-element");
        separatorItem.html.setCss("height", "0px");
        separatorItem.html.setCss("padding-top", "0px");
        separatorItem.html.setCss("padding-bottom", "0px");
        separatorItem.html.addClass("rwt-separator-item");
    }
    
    private void addRow(final MenuItem item){
        if (item.isSeparator()){
            createSeparatorItem();
        }else if (item.getAction()!=null){
            final Action action = item.getAction();
            if (action.getActionMenu() instanceof RwtMenu){
                final TableLayout.Row resultItem = 
                    createMenuItem((RwtMenu)action.getActionMenu(), getActionText(action), action.getIcon());
                resultItem.setVisible(action.isVisible());                
                resultItem.setEnabled(action.isEnabled());
                resultItem.setToolTip(action.getToolTip());
                setupListItemObjectName(resultItem, action);
            }else{
                createAction(action);
            }
        } else {
            createMenuItem((RwtMenu)item.getMenu(),null,null);
        }
    }

    protected void actualize() {
        internalTableBox.clearRows();
        rowActionMap.clear();
        for (MenuItem item: items){            
            addRow(item);
        }        
    }
    
    private static void setupListItemObjectName(final TableLayout.Row item, final Action action){
        final String actionName = action.getObjectName();
        if (actionName != null && !actionName.isEmpty()) {
            item.setObjectName("rx_menu_item_" + actionName);
        } else {
            item.setObjectName("rx_menu_item_" + getActionText(action));
        }
    }
    
    private static String getActionText(final Action action){
        final String actionText;
        if (((RwtAction) action).isTextShown()) {            
            actionText = action.getText();
        } else {            
            ((RwtAction) action).setTextShown(true);
            actionText = action.getText();
            ((RwtAction) action).setTextShown(false);
        }
        return actionText;
    }  
    
    private void setIcon(TableLayout.Row row, Icon icon) {
        row.setHeight(iconHeight);
        Div div = new Div();
        div.setCss("height", (iconHeight + 4) + "px");
        if (icon != null) {
            div.setCss("width", (iconWidth + 5) + "px");
            Html iconElement = new Html("img");
            iconElement.setAttr("src", ((WpsIcon)icon).getURI(this));
            iconElement.setCss("display", "inline");
            iconElement.setCss("unselectable", "on");
            iconElement.setCss("vertical-align", "middle");
            iconElement.setCss("height", iconHeight + "px");
            iconElement.setCss("width", iconWidth + "px");
            iconElement.setAttr("height", iconHeight);
            iconElement.setAttr("width", iconWidth);
            iconElement.setCss("padding", "2px");
            iconElement.setCss("padding-right", "2px" );
            iconElement.setCss("padding-left", "3px");
            div.add(iconElement);
            row.getCell(0).getHtml().setCss("width", (iconWidth + 5) + "px");
        } 
        row.getCell(0).getHtml().add(div);
    }
    
    private void setCheckBox(TableLayout.Row row, boolean isChecked) {
        CheckBox checkBox = new CheckBox();
        checkBox.clearClickHandlers();
        checkBox.setSelected(isChecked);
        row.setHeight(iconHeight);
        Div div = new Div();
        div.setCss("height", (iconHeight + 4) + "px");
        div.setCss("width", (iconWidth + 5) + "px");
        div.add(checkBox.getHtml());
        row.getCell(0).getHtml().add(div);
    }
    
    private void setText(TableLayout.Row row, String actualText) {
        Html lbl = new Html("a");
        lbl.setCss("unselectable", "on");
        lbl.setCss("cursor", "pointer");
        lbl.setCss("padding-right", "2px");
        lbl.setCss("display", "inline");
        lbl.setCss("padding-left", "2px");
        lbl.addClass("rwt-non-choosable");
        lbl.setInnerText(actualText);
        row.getCell(1).getHtml().add(lbl);
    }    
}
