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

package org.radixware.wps.views.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import java.util.Set;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ArrId;
import org.radixware.kernel.common.client.views.ITabSetWidget;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.TabLayout;


public class TabSet extends TabLayout implements ITabSetWidget {

    private WpsEnvironment env;
    private final List<EditorPage> editorPages = new ArrayList<>();
    private final List<Id> visiblePages = new LinkedList<>();
    private final Set<Id> openedPages = new HashSet<>();
    private final Set<Id> needForReread = new HashSet<>();
    private ArrId recentlyOpenedPages;
    private final Set<Id> subscribedProperties = new HashSet<>();
    private String settingsKey;
    private final static String RECENTLY_OPENED_PAGES_KEY = "recentlyOpenedPages";

    public TabSet(WpsEnvironment env, IView parentView, List<EditorPageModelItem> pages, Id ownerId) {
        this.env = env;
        setObjectName("rx_tab_set_#"+ownerId.toString());
        settingsKey = parentView.getModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + RECENTLY_OPENED_PAGES_KEY + "/" + ownerId;
        final ClientSettings settings = env.getConfigStore();
        final boolean needToRestoreActiveTab;
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EDITOR_GROUP);
        settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
        try {
            needToRestoreActiveTab = settings.readBoolean(SettingNames.Editor.Common.RESTORE_TAB, false);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }

        final String storedPages = settings.readString(settingsKey,"");

        if (storedPages==null || storedPages.isEmpty()){
            recentlyOpenedPages = new ArrId();
        }else{
            try{
                recentlyOpenedPages = ArrId.fromValAsStr(storedPages);
            }catch(WrongFormatError error){
                final String messageTemplage = 
                    env.getMessageProvider().translate("TraceMessage", "Unable to restore recently opened pages from string '%1$s'");
                env.getTracer().debug(String.format(messageTemplage, storedPages));
                recentlyOpenedPages = new ArrId();
            }
        }

        int index = 0;
        int indexInRecentlyOpenedPages = Integer.MAX_VALUE;
        for (EditorPageModelItem editorPageModelItem : pages) {
            if (editorPageModelItem.def instanceof RadContainerEditorPageDef) {
                try {
                    if (!editorPageModelItem.isAccessible()) {
                        continue;
                    }
                } catch (InterruptedException ex) {
                    break;
                } catch (ServiceClientException error) {
                    final String msg = env.getMessageProvider().translate("TraceMessage", "Cannot add editor page for explorer item #%s:\n %s"),
                            reason = ClientException.getExceptionReason(editorPageModelItem.getEnvironment().getMessageProvider(), error),
                            stack = ClientException.exceptionStackToString(error);
                    final Id explorerItemId = ((RadContainerEditorPageDef) editorPageModelItem.def).getExplorerItemId();
                    env.getTracer().put(EEventSeverity.ERROR, String.format(msg, explorerItemId.toString(), reason),
                            EEventSource.EXPLORER);
                    env.getTracer().put(EEventSeverity.DEBUG, stack,
                            EEventSource.EXPLORER);
                    break;
                }
            } else if (editorPageModelItem.def instanceof RadStandardEditorPageDef) {
                final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef)editorPageModelItem.def;                
                if (pageDef.isEmpty()) {
                    continue;
                }
            }
            final EditorPage editorPage = (EditorPage) editorPageModelItem.getEditorPageWidget();//new EditorPage(editorPageModelItem);
            editorPage.setObjectName("editor page #" + editorPageModelItem.getId());
            if (editorPageModelItem.isVisible()) {
                addTab(editorPage);
                final boolean isEnabled = editorPageModelItem.isEnabled();
                if (needToRestoreActiveTab && isEnabled){
                    final int storedIndex = recentlyOpenedPages.indexOf(editorPageModelItem.getId());
                    if (storedIndex>-1 && storedIndex<indexInRecentlyOpenedPages) {
                        indexInRecentlyOpenedPages = storedIndex;
                        index = visiblePages.size();
                    }
                }
                visiblePages.add(editorPage.getEditorPageId());
            }
            editorPages.add(editorPage);

        }
        if (index > 0) {
            this.setCurrentIndex(index);
        }
    }

    private Tab addTab(EditorPage editorPage) {
        return addTab(-1, editorPage);
    }

    private Tab addTab(int index, EditorPage editorPage) {
        final EditorPageModelItem modelItem = editorPage.getEditorPageModelItem();
        final TabLayout.Tab tab = addTab(index, calcEffectiveTitle(modelItem));
        if (modelItem.getDefinition() instanceof RadCustomEditorPageDef) {
            tab.getHtml().setCss("overflow-y", "auto");
        }
        tab.setEnabled(modelItem.isEnabled());
        tab.setIcon(modelItem.getIcon());
        tab.setTitleColor(modelItem.getTitleColor());
        tab.add(editorPage);
        tab.setUserData(editorPage);
        tab.setObjectName("editor_page_#"+modelItem.getId().toString());
        return tab;
    }

    @Override
    public boolean setCurrentEditorPage(final Id pageId) {
        if (visiblePages.contains(pageId)) {
            setCurrentTab(visiblePages.indexOf(pageId));
            return true;
        }
        return false;
    }

    @Override
    public boolean activateEditorPage(final Id pageId) {
        if (visiblePages.contains(pageId)) {
            final EditorPage page = getEditorPageById(pageId);
            if (!openedPages.contains(pageId)) {
                page.bind();
                openedPages.add(pageId);
            }
            return true;
        }else{
            return false;
        }
    }


    private EditorPage getEditorPageById(Id pageId) {
        for (EditorPage page : editorPages) {
            if (page.getEditorPageId() == pageId) {
                return page;
            }
        }
        return null;
    }

    private Tab getTabByPageId(Id pageId) {
        for (Tab tab : getTabs()) {
            EditorPage page = getEditorPage(tab);
            if (page.getEditorPageId() == pageId) {
                return tab;
            }
        }
        return null;
    }

    @Override
    public void refresh(ModelItem changedItem) {
        if (changedItem instanceof EditorPageModelItem) {
            final EditorPageModelItem modelItem = (EditorPageModelItem) changedItem;
            Tab tab = getTabByPageId(modelItem.getId());
            final EditorPage page = getEditorPageById(modelItem.getId());
            if (!modelItem.isVisible() && tab != null) {
                removeTab(tab);
                visiblePages.remove(modelItem.getId());
            } else if (modelItem.isVisible() && tab == null && page != null) {
                int index = pageIndex(page);
                tab = addTab(index, page);
                tab.setEnabled(modelItem.isEnabled());
                visiblePages.add(modelItem.getId());
            } else if (tab != null) {
                tab.setTitle(calcEffectiveTitle(modelItem));
                tab.setEnabled(modelItem.isEnabled());
                tab.setIcon(modelItem.getIcon());
                tab.setTitleColor(modelItem.getTitleColor());
            }
            updateSubscribedProperties(modelItem);
        }
    }

    private String calcEffectiveTitle(final EditorPageModelItem page){
        if (page.getDefinition() instanceof RadStandardEditorPageDef && !pageHasVisibleItem(page)){
            final List<EditorPageModelItem> childPages = page.getChildPages();
            EditorPageModelItem visiblePage = null;
            for (EditorPageModelItem childPage: childPages){
                if (childPage.isVisible()){
                    if (visiblePage==null){
                        visiblePage = childPage;
                    }else{
                        return page.getTitle();
                    }
                }
            }
            if (visiblePage==null){
                return page.getTitle();
            }else{
                final String pageTitle = page.getTitle();
                final String innerPageTitle = calcEffectiveTitle(visiblePage);
                if (pageTitle==null || pageTitle.isEmpty()){
                    return innerPageTitle;
                }else if (innerPageTitle==null || innerPageTitle.isEmpty()){
                    return pageTitle;
                }else{
                    return pageTitle+". "+innerPageTitle;
                }
            }
        }else{
            return page.getTitle();
        }
    }

    private static boolean pageHasVisibleItem(final EditorPageModelItem page){
        final Collection<Property> properties = page.getProperties();
        for (Property property: properties){
            if (property.isVisible()){
                return true;
            }
        }
        final Collection<PropertiesGroupModelItem> groups = page.getPropertyGroups();
        for (PropertiesGroupModelItem group: groups){
            if (group.isVisible()){
                return true;
            }
        }
        return false;
    }

    private int pageIndex(final EditorPage editorPage) {
        int index = 0;
        int count = getTabCount();
        for (EditorPage page : editorPages) {
            if (page == editorPage) {//NOPMD
                return Math.min(index, count);
            } else if (page.getEditorPageModelItem().isVisible()) {
                index++;
            }
        }
        return count;
    }

    @Override
    public boolean setFocus(Property property) {
        Id pageId;
        for (int i = 0; i < editorPages.size(); ++i) {
            pageId = editorPages.get(i).getEditorPageModelItem().getId();
            if (visiblePages.contains(pageId)) {
                final EditorPage page = getEditorPageById(pageId);
                if (!openedPages.contains(pageId)) {
                    page.bind();
                    openedPages.add(pageId);
                }
                if (page.setFocus(property)) {
                    setCurrentIndex(visiblePages.indexOf(pageId));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
        if (editorPages != null && !editorPages.isEmpty() && focused) {
            Tab tab = getCurrentTab();
            if (tab == null) {
                if (editorPages != null && !editorPages.isEmpty()) {
                    editorPages.get(0).setFocused(true);
                }
            } else {
                getEditorPage(tab).setFocused(true);
            }
        } else {
            super.setFocused(focused);
        }
    }

    @Override
    public void bind() {
        for (EditorPage editorPage : editorPages) {
            editorPage.getEditorPageModelItem().subscribe(this);
            updateSubscribedProperties(editorPage.getEditorPageModelItem());
        }
        listener.onCurrentTabChange(null, getCurrentTab());
        addTabListener(listener);

    }

    private EditorPage getEditorPage(Tab tab) {
        if (tab == null) {
            return null;
        }
        return (EditorPage) tab.getUserData();
    }
    private final TabListener listener = new TabListener() {
        @Override
        public void onCurrentTabChange(Tab oldTab, Tab newTab) {
            if (newTab != null) {
                final EditorPage page = getEditorPage(newTab);
                if (page != null) {
                    Id editorPageId = page.getEditorPageId();
                    if (!openedPages.contains(editorPageId)) {
                        page.bind();
                        openedPages.add(editorPageId);
                    } else if (needForReread.contains(editorPageId)) {
                        page.reread();
                        needForReread.remove(editorPageId);
                    }
                    page.setFocused(true);
                    page.getEditorPageModelItem().getOwner().afterActivateEditorPage(editorPageId);
                }
            }
        }
    };

    public void updateSubscribedProperties(final EditorPageModelItem pageItem) {
        final Collection<Property> pageProperties = pageItem.getProperties();
        for (Property property : pageProperties) {
            if (!subscribedProperties.contains(property.getId())) {
                property.subscribe(this);
                subscribedProperties.add(property.getId());
            }
        }
    }

    public void finishEdit() {
        for (EditorPage page : editorPages) {
            page.finishEdit();
        }
    }

    public void close() {
        final ClientSettings settings = env.getConfigStore();
        final int currentIndex = getCurrentIndex();
        if (currentIndex > -1 && currentIndex < visiblePages.size()) {
            final Id currentPageId = visiblePages.get(currentIndex);
            if (recentlyOpenedPages==null){
                recentlyOpenedPages=new ArrId(currentPageId);
            }else{
                recentlyOpenedPages.removeAll(Collections.singleton(currentPageId));
                recentlyOpenedPages.add(0, currentPageId);
                for (int i=recentlyOpenedPages.size(); i>10; i--){
                    recentlyOpenedPages.remove(i-1);
                }
            }
            settings.writeString(settingsKey, recentlyOpenedPages.toString());
        }

        Collection<Property> pageProperties;
        for (EditorPage page : editorPages) {
            pageProperties = page.getEditorPageModelItem().getProperties();
            for (Property property : pageProperties) {
                property.unsubscribe(this);
            }
            page.getEditorPageModelItem().unsubscribe(this);
            page.close();
        }
        clear();

        openedPages.clear();
        visiblePages.clear();
        editorPages.clear();
        needForReread.clear();
    }

    public void reread() {
        needForReread.clear();
        EditorPage currentPage = getEditorPage(getCurrentTab());

        Id curId = currentPage == null ? null : currentPage.getEditorPageId();

        for (Id pageId : openedPages) {
            if (Objects.equals(pageId,curId)) {
                needForReread.add(pageId);
            }
        }
        if (currentPage != null) {
            currentPage.reread();
        }

    }
}
