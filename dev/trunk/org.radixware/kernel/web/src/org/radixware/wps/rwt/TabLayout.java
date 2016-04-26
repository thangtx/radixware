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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;


public class TabLayout extends Container {

    public interface TabListener {

        public void onCurrentTabChange(Tab oldTab, Tab newTab);
    }

    public class Tab extends Container {

        private TabHandle handle;
        private Object userData;

        public Tab() {
            super();
            html.setCss("display", "none");
            html.setCss("width", "100%");
            html.setCss("height", "100%");
            html.setCss("position", "relative");
        }

        public Object getUserData() {
            return userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }

        public String getTitle() {
            return handle.getTitle();
        }

        public void setTitle(String title) {
            handle.setTitle(title);
        }

        public void setTitleColor(final Color color) {
            handle.setForeground(color);
        }

        public Icon getIcon() {
            return handle.getIcon();
        }

        public void setIcon(Icon icon) {
            handle.setIcon(icon);
        }

        @Override
        public void setVisible(boolean isVisible) {
            if (isVisible != isVisible()) {
                super.setVisible(isVisible);
                this.handle.setVisible(isVisible);

                if (TabLayout.this.getCurrentTab() == this) {
                    autoChangeCurrentTab();
                }
            }
        }

        @Override
        public boolean isVisible() {
            return handle.isVisible();
        }

        @Override
        public boolean isEnabled() {
            return handle.isEnabled();
        }

        @Override
        public void setEnabled(final boolean isEnabled) {
            if (this.isEnabled() != isEnabled) {
                handle.setEnabled(isEnabled);
                if (TabLayout.this.getCurrentTab() == this) {
                    autoChangeCurrentTab();
                }
            }
        }
    }

    private class TabHandle extends ToolButton {

        private Tab tab;

        @SuppressWarnings("LeakingThisInConstructor")
        public TabHandle(int index, String title) {
            super(new Html("li"));
            html.setCss("list-style-type", "none");
            html.setCss("display", "inline-block");
            html.setCss("float", null);
            html.removeClass("rwt-tool-button");
            this.tab = new Tab();
            tab.handle = this;
            tabsHeader.add(index,this);
            tabsArea.add(index,tab);
            setParent(TabLayout.this);
            tab.setParent(TabLayout.this);
            setText(title);
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton button) {
                    TabLayout.this.setCurrentTab(tab);
                }
            });
            setIconHeight(12);
            setIconWidth(12);
            setHeight(20);
        }

        @Override
        protected Html createLabelElement() {
            Html label = super.createLabelElement();
            label.setCss("vertical-align", "middle");
            return label;
        }

        private void destroy() {
            tabsArea.remove(tab);
            //tabsArea.remove(tab.html);
            //tabsHeader.remove(html);
            tabsHeader.remove(this);
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            result = tab.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            return null;
        }
    }
    private final List<TabHandle> tabs = new LinkedList<>();
    private final Container tabsArea = new Container();
    private final Container tabsHeader = new Container();    
    private final Container scroller = new Container();
    private final ToolButton prevBtn = new ToolButton();
    private final ToolButton nextBtn = new ToolButton();
    private TabHandle current = null;

    public TabLayout() {
        super();
        
        //html.setCss("background-color", "#DDD");
        
        add(tabsHeader);
        addScroller(/*true*/);//--> edited class .rwt-ui-tab-layout-header
        add(tabsArea);

        html.setCss("width", "100%");
        html.setCss("height", "100%");
        html.setCss("overflow", "hidden");

        tabsHeader.html.setCss("padding", "0px");
        tabsHeader.html.setCss("overflow", "hidden");
        tabsHeader.html.setCss("margin", "0px");
        tabsHeader.html.setCss("float", "left");
        tabsHeader.html.setCss("height", "21px");
        
        tabsHeader.html.setAttr("role", "header");
        tabsHeader.html.setCss("white-space", "nowrap");
        tabsHeader.html.setAttr("display", "inline");//inline

        //tabsHeader.setCss("margin-top", "2px");          
        tabsArea.html.setCss("display", "block");
        tabsArea.html.setCss("width", "100%");
        tabsArea.html.setCss("clear", "left");
        tabsArea.html.setAttr("role", "tabs");
        tabsArea.html.setCss("overflow", "hidden");
        tabsArea.html.addClass("rwt-ui-tab-layout-tab-page");
        tabsHeader.html.addClass("rwt-ui-tab-layout-header");
        tabsHeader.html.setCss("display", "none");
        html.layout("$RWT.tab_layout._layout");
    }

    public final void addScroller(/*boolean showTabsList*/) {
        add(scroller);
        TableLayout table = new TableLayout();
        TableLayout.Row row;
        scroller.add(table);
        
        row = table.addRow();

        scroller.html.setAttr("role", "scroller");
        scroller.html.setCss("display", "none");
        scroller.html.addClass("rwt-ui-tabs-scroller");
        /*scroller.getAnchors().setLeft(new Anchors.Anchor(1f, -40, tabsHeader));
        scroller.getAnchors().setTop(new Anchors.Anchor(0f, 0, tabsHeader));*/

        row.addCell().add(prevBtn);
        row.addCell().add(nextBtn);

        /*Icon prevIcon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.LEFT);//U+25C4
         prevBtn.setIcon(prevIcon);
         prevBtn.setIconSize(10, 10);
         Icon nextIcon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.RIGHT);//U+25BA
         nextBtn.setIconSize(10, 10);
         nextBtn.setIcon(nextIcon);*/

        prevBtn.setTitle("\u25c4");
        prevBtn.getHtml().setCss("width", "15px");
        prevBtn.getHtml().setCss("height", "20px");
        prevBtn.getHtml().setCss("border", null);
        prevBtn.html.setAttr("scroll", "left");

        nextBtn.setTitle("\u25ba");
        nextBtn.getHtml().setCss("width", "15px");
        nextBtn.getHtml().setCss("height", "20px");
        nextBtn.getHtml().setCss("border", null);
        nextBtn.html.setAttr("scroll", "right");
        
    }

    public final void removeScroller() {
        scroller.setParent(null);
        remove(scroller);
        html.remove(scroller.html);
    }

    public Tab addTab(String title) {
        synchronized (this) {
            TabHandle handle = new TabHandle(-1, title);
            tabs.add(handle);
            if (current == null) {
                setCurrentTab(handle.tab);
            } else {
                setCurrentTab(current.tab);
            }
            if (tabs.size() > 1) {
                tabsHeader.html.setCss("display", null);
            }
            return handle.tab;
        }
    }

    public Tab addTab(int index, String title) {
        synchronized (this) {
            TabHandle handle = new TabHandle(index, title);
            int realIndex = tabsHeader.html.children().indexOf(handle.html);
            tabs.add(realIndex, handle);
            if (current == null) {
                setCurrentTab(handle.tab);
            } else {
                setCurrentTab(current.tab);
            }
            if (tabs.size() > 1) {
                tabsHeader.html.setCss("display", null);
            }
            return handle.tab;
        }
    }

    public void removeTab(Tab tab) {
        synchronized (this) {
            if (this.tabs.contains(tab.handle)) {
                if (getCurrentTab() == tab) {
                    autoChangeCurrentTab();
                }

                tab.handle.destroy();
                this.tabs.remove(tab.handle);
                if (tabs.size() < 2) {
                    tabsHeader.html.setCss("display", "none");
                }
            }
        }
    }

    private void autoChangeCurrentTab() {
        int index = TabLayout.this.getCurrentIndex() - 1;
        boolean done = false;
        while (index >= 0) {
            if (TabLayout.this.getTabs().get(index).handle.isVisible() && TabLayout.this.getTabs().get(index).handle.isEnabled()) {
                TabLayout.this.setCurrentTab(index);
                done = true;
                break;
            }
            index--;
        }
        if (!done) {
            index = TabLayout.this.getCurrentIndex() + 1;
            while (index < getTabCount()) {
                if (TabLayout.this.getTabs().get(index).handle.isVisible() && TabLayout.this.getTabs().get(index).handle.isEnabled()) {
                    TabLayout.this.setCurrentTab(index);
                    done = true;
                    break;
                }
                index++;
            }
        }
        if (!done && TabLayout.this.getCurrentIndex()==0 && TabLayout.this.getTabs().size()==1){
            TabLayout.this.setCurrentTab(null);
        }
    }

    public int getTabIndex(Tab tab) {
        if (tab == null) {
            return -1;
        }
        return tabs.indexOf(tab.handle);
    }

    public int getTabCount() {
        return tabs.size();
    }

    public void setCurrentTab(int index) {
        if (index >= 0 && index < tabs.size()) {
            setCurrentTab(tabs.get(index).tab);
        }
    }

    public void setCurrentIndex(int index) {
        setCurrentTab(index);
    }

    public void setCurrentTab(Tab tab) {
        synchronized (this) {
            Tab oldTab = current == null ? null : current.tab;
            if (tab == null) {
                this.current = null;
                fireTabChange(oldTab, null);
            } else {
                if (tabs.contains(tab.handle)) {
                    this.current = tab.handle;
                    for (TabHandle h : tabs) {
                        if (h.tab == tab) {
                            h.tab.html.setCss("display", "block");
                            h.html.addClass("rwt-ui-tab-layout-tab-active");
                            //h.html.setCss("background-color", "#FFFFFF");
                            h.html.removeClass("rwt-ui-tab-layout-tab-inactive");
                        } else {
                            h.tab.html.setCss("display", "none");
                            h.html.removeClass("rwt-ui-tab-layout-tab-active");
                            h.html.addClass("rwt-ui-tab-layout-tab-inactive");
                            //h.html.setCss("background-color", "#DDD");
                        }
                    }
                    if (oldTab != tab) {
                        fireTabChange(oldTab, tab);
                    }
                }
            }

            //updateScroller(oldTab, tab);

        }
    }

   /* private void updateScroller(Tab oldTab, Tab newTab) {
        int size = getTabCount();
        int currIndex = getTabIndex(oldTab);
        if (newTab == null) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            return;
        } else {
            int newIndex = getTabIndex(newTab);

            if (newIndex <= 0) {
                prevBtn.setEnabled(false);
            } else {
                prevBtn.setEnabled(true);
            }
            if (newIndex >= size - 1) {
                nextBtn.setEnabled(false);
            } else {
                nextBtn.setEnabled(true);
            }
        }
    }*/

    public Tab getCurrentTab() {
        return current == null ? null : current.tab;
    }

    public int getCurrentIndex() {
        Tab tab = getCurrentTab();
        if (tab == null) {
            return -1;
        } else {
            return getTabs().indexOf(tab);
        }
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/tab-layout.js"};
    }

    @Override
    public void clear() {
        for (TabHandle th : tabs) {
            th.destroy();
        }
        tabs.clear();
        setCurrentTab(null);
    }

    public void adjustToContent(boolean adjust) {
        if (adjust) {
            this.html.setAttr("adjustHeight", true);
        } else {
            this.html.setAttr("adjustHeight", false);
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        if (this.tabsHeader.html.getId().equals(id)) {
            return this;
        }
        if (this.tabsArea.html.getId().equals(id)) {
            return this;
        }
        if (scroller.html != null) {
            if (this.scroller.html.getId().equals(id)) {
                return this;
            }
        }
        result = scroller.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        for (TabHandle h : tabs) {
            result = h.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (scroller != null) {
            scroller.visit(visitor);
        }
        for (TabHandle h : tabs) {
            h.visit(visitor);
        }
    }

    public List<Tab> getTabs() {
        synchronized (this) {
            List<Tab> tabss = new LinkedList<>();
            for (TabHandle handle : tabs) {
                tabss.add(handle.tab);
            }
            return tabss;
        }
    }

    public Tab getTab(int index) {
        synchronized (this) {
            if (index >= 0 && index < getTabCount()) {
                return getTabs().get(index);
            } else {
                return null;
            }
        }
    }

    private static class DefaultTabListener implements TabListener {

        private final List<TabListener> listeners = new LinkedList<>();

        @Override
        public void onCurrentTabChange(Tab oldTab, Tab newTab) {
            List<TabListener> list;
            synchronized (listeners) {
                list = new ArrayList<>(listeners);
            }
            for (TabListener l : list) {
                l.onCurrentTabChange(oldTab, newTab);
            }
        }

        public void addTabListener(TabListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeTabListener(TabListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }
    private DefaultTabListener listener = null;

    public void removeTabListener(TabListener l) {
        if (listener != null) {
            listener.removeTabListener(l);
        }
    }

    private void fireTabChange(Tab oldTab, Tab newTab) {
        synchronized (this) {
            if (listener != null) {
                listener.onCurrentTabChange(oldTab, newTab);
            }
        }
    }

    public void addTabListener(TabListener l) {
        synchronized (this) {
            if (listener == null) {
                listener = new DefaultTabListener();
            }
        }
        listener.addTabListener(l);
    }
}
