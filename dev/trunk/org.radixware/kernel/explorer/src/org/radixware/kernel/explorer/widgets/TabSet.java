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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.gui.QFocusEvent;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ArrId;
import org.radixware.kernel.common.client.views.ITabSetWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;

import org.radixware.kernel.explorer.views.IExplorerView;

public class TabSet extends QExtTabWidget implements ITabSetWidget, IExplorerModelWidget {
    
    private static final class CurrentTabChangeEvent extends QEvent{
        
        public CurrentTabChangeEvent(){
            super(QEvent.Type.User);            
        }
    }

    private final static String PAGE_ACTIVATED_KEY = "pageActivated";//legacy setting key
    private final static String RECENTLY_OPENED_PAGES_KEY = "recentlyOpenedPages";
            
    private final static Map<String, QFont> BOLD_FONT_CACHE = new HashMap<>();
    
    private final List<EditorPage> editorPages = new ArrayList<>();
    private final Collection<Id> subscribedProperties = new ArrayList<>();
    private final List<Id> visiblePages = new ArrayList<>();
    private final List<Id> openedPages = new ArrayList<>();
    private final List<Id> needForReread = new ArrayList<>();
    private ArrId recentlyOpenedPages;
    private String settingsKey;    
    private boolean closed = false;
    private boolean inited = false;
    private int scheduledCurrentTabChange = -1;
    private final IClientEnvironment environment;
    
    public TabSet(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        setObjectName("tabSet"); 
    }

    public TabSet(final IClientEnvironment environment, final QWidget parent, final IExplorerView parentView, final List<EditorPageModelItem> editorPageModelItems, final String name) {
        this(environment,parent);
        init(parentView, editorPageModelItems, name);
    }

    public TabSet(final IClientEnvironment environment, final IExplorerView parentView, final List<EditorPageModelItem> editorPageModelItems, final String name) {
        this(environment, (QWidget) parentView, parentView, editorPageModelItems, name);
    }
    
    public final void init(final IExplorerView parentView, final List<EditorPageModelItem> editorPageModelItems, final String name){
        inited = true;
        settingsKey = parentView.getModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + RECENTLY_OPENED_PAGES_KEY + "/" + name;
        final String legacySettingsKey = parentView.getModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + PAGE_ACTIVATED_KEY + "/" + name;
        applySettings();	//Применение настроек шрифта и размеров значков

        final ClientSettings settings = environment.getConfigStore();
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
            final String storedPage = settings.readString(legacySettingsKey,"");
            if (storedPage==null || storedPage.isEmpty()){
                recentlyOpenedPages = new ArrId();
            }else{
                recentlyOpenedPages = new ArrId(storedPage);                
            }
        }else{
            try{
                recentlyOpenedPages = ArrId.fromValAsStr(storedPages);
            }catch(WrongFormatError error){
                final String messageTemplage = 
                    environment.getMessageProvider().translate("TraceMessage", "Unable to restore recently opened pages from string '%1$s'");
                environment.getTracer().debug(String.format(messageTemplage, storedPages));
                recentlyOpenedPages = new ArrId();
            }
        }
        
        
        int index = 0;
        int indexInRecentlyOpenedPages = Integer.MAX_VALUE;
        for (EditorPageModelItem editorPageModelItem : editorPageModelItems) {
            if (editorPageModelItem.def instanceof RadContainerEditorPageDef) {
                try {
                    if(!editorPageModelItem.isAccessible()){
                         continue;
                    }
                } catch (InterruptedException ex) {
                    break;
                } catch (ServiceClientException error) {
                    final String msg = environment.getMessageProvider().translate("TraceMessage", "Cannot add editor page for explorer item #%s:\n %s"),
                            reason = ClientException.getExceptionReason(editorPageModelItem.getEnvironment().getMessageProvider(), error),
                            stack = ClientException.exceptionStackToString(error);
                    Id explorerItemId=((RadContainerEditorPageDef)editorPageModelItem.def).getExplorerItemId();
                    environment.getTracer().put(EEventSeverity.ERROR, String.format(msg, explorerItemId.toString(), reason),
                            EEventSource.EXPLORER);
                    environment.getTracer().put(EEventSeverity.DEBUG, stack,
                            EEventSource.EXPLORER);
                    break;
                }
            } else if (editorPageModelItem.def instanceof RadStandardEditorPageDef) {                
                final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef)editorPageModelItem.def;                
                if (pageDef.isEmpty()) {
                    continue;
                }
            }
            final EditorPage editorPage = new EditorPage(editorPageModelItem);
            editorPage.setObjectName("editor page #" + editorPageModelItem.getId());
            editorPage.setParent(this);
            if (editorPageModelItem.isVisible()) {
                final int tabIndex =
                        addTab(editorPage, (RdxIcon) editorPageModelItem.getIcon(), editorPageModelItem.getTitle());
                final boolean isEnabled = editorPageModelItem.isEnabled();
                setTabEnabled(tabIndex, isEnabled);
                setTabTextColor(tabIndex, editorPageModelItem.getTitleColor());
                if (needToRestoreActiveTab && isEnabled){
                    final int storedIndex = recentlyOpenedPages.indexOf(editorPageModelItem.getId());
                    if (storedIndex>-1 && storedIndex<indexInRecentlyOpenedPages) {
                        indexInRecentlyOpenedPages = storedIndex;
                        index = visiblePages.size();
                    }
                }
                visiblePages.add(editorPageModelItem.getId());
            }
            editorPages.add(editorPage);
        }

        if (index > 0) {
            this.setCurrentIndex(index);
        }
        currentChanged.connect(this, "finishEdit()");        
    }
    
    public final boolean isInited(){
        return inited;
    }

    @Override
    public void bind() {
        for (EditorPage editorPage : editorPages) {
            editorPage.getEditorPageModelItem().subscribe(this);
            updateSubscribedProperties(editorPage.getEditorPageModelItem());
        }
        refreshTabBar();
        currentChanged.connect(this, "onCurrentChanged(int)");
        if (currentIndex() >= 0) {
            onCurrentChanged(currentIndex());
        }        
        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
    }

    public void updateSubscribedProperties(final EditorPageModelItem pageItem) {
        final Collection<Property> pageProperties = pageItem.getProperties();
        for (Property property : pageProperties) {
            if (!subscribedProperties.contains(property.getId())) {
                property.subscribe(this);
                subscribedProperties.add(property.getId());
            }
        }
    }

    private boolean hasTitledPage() {
        if (visiblePages.size() == 1) {
            final Id pageId = visiblePages.get(0);
            final EditorPageModelItem editorPage = getEditorPageById(pageId).getEditorPageModelItem();
            if (editorPage.getTitle()!=null && !editorPage.getTitle().isEmpty()){
                return false;
            }            
            final RadEditorPageDef def = editorPage.getDefinition();
            return def.hasTitle() || !Utils.equals(def.getTitle(), editorPage.getTitle());
        }
        return visiblePages.size() > 1;
    }

    private void onCurrentChanged(final int index) {
        if (environment.getEasSession().isBusy()){
            if (scheduledCurrentTabChange<0){
                Application.processEventWhenEasSessionReady(this, new CurrentTabChangeEvent());
            }
            scheduledCurrentTabChange = index;
        }else{
            processCurrentTabChanged(index);
        }
    }
    
    private void processCurrentTabChanged(final int index){
        if (index >= 0 && index < visiblePages.size()) {
            final Id pageId = visiblePages.get(index);
            final EditorPage page = getEditorPageById(pageId);
            if (!openedPages.contains(pageId)) {
                page.bind();
                openedPages.add(pageId);
            } else if (needForReread.contains(pageId)) {
                page.reread();
                needForReread.remove(pageId);
            }
            page.setFocus();            
            page.getEditorPageModelItem().getOwner().afterActivateEditorPage(pageId);
        }        
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        if (changedItem instanceof EditorPageModelItem) {
            final EditorPageModelItem modelItem = (EditorPageModelItem) changedItem;
            final EditorPage page = getEditorPageById(modelItem.getId());
            int idx = visiblePages.indexOf(modelItem.getId());
            if (!modelItem.isVisible() && idx >= 0) {
                removeTab(idx);
                visiblePages.remove(idx);
            } else if (modelItem.isVisible() && idx < 0) {
                idx = pageIndex(page);
                visiblePages.add(idx, modelItem.getId());
                insertTab(idx, page, (QIcon) modelItem.getIcon(), modelItem.getTitle());
                setTabEnabled(idx, modelItem.isEnabled());                
                setTabTextColor(idx, modelItem.getTitleColor());                
            } else if (idx >= 0) {
                setTabIcon(idx, (QIcon) modelItem.getIcon());
                setTabText(idx, modelItem.getTitle());
                setTabEnabled(idx, modelItem.isEnabled());
                setTabTextColor(idx, modelItem.getTitleColor());
            }
            updateSubscribedProperties(modelItem);
        }
        refreshTabBar();
    }
    
    private void setTabTextColor(int idx, final Color color){
        if (color==null){
            setTabTextColor(idx, (QColor)null);
        }else{
            setTabTextColor(idx, WidgetUtils.awtColor2qtColor(color));
        }
    }

    private int pageIndex(final EditorPage editorPage) {
        int index = 0;
        for (EditorPage page : editorPages) {
            if (page == editorPage) {//NOPMD
                return Math.min(index, count());
            } else if (page.getEditorPageModelItem().isVisible()) {
                index++;
            }
        }
        return count();
    }

    private void refreshTabBar() {
        setTabBarVisible(hasTitledPage());
    }

    private EditorPage getEditorPageById(final Id pageId) {
        for (EditorPage page : editorPages) {
            if (page.getEditorPageModelItem().getId().equals(pageId)) {
                return page;
            }
        }
        throw new DefinitionNotFoundError(pageId);
    }

    public void addEditorPage(final EditorPageModelItem page) {
    }

    public boolean setCurrentEditorPage(final EditorPageModelItem page) {
        return setCurrentEditorPage(page.getId());
    }

    @Override
    public boolean setCurrentEditorPage(final Id pageId) {
        if (visiblePages.contains(pageId)) {
            setCurrentIndex(visiblePages.indexOf(pageId));
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
        }
        return false;
    }        

    @Override
    public boolean setFocus(final Property property) {
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
    protected void focusInEvent(QFocusEvent event) {
        super.focusInEvent(event);
        if (!closed && currentIndex() > -1) {
            final Id currentPageId = visiblePages.get(currentIndex());
            getEditorPageById(currentPageId).setFocus();
        }
    }

    public void finishEdit() {
        for (EditorPage page : editorPages) {
            page.finishEdit();
        }
    }

    public final void applySettings() {
        //применение настроек размеров значков и шрифта
        final ClientSettings settings = environment.getConfigStore();

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EDITOR_GROUP);
        settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
        final QFont tabFont = 
            ExplorerSettings.getQFont(settings.readString(SettingNames.Editor.Common.FONT_IN_TABS));
//    	Icon size in tabs
        final QSize iconSize = ExplorerSettings.getQSize(settings.readString(SettingNames.Editor.Common.ICON_SIZE_IN_TABS));
        this.setIconSize(iconSize);

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();

        tabBar().setFont(isCurrentTabMarked() ? getBoldFont(tabFont) : tabFont);
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        closed = true;
        inited = false;
        scheduledCurrentTabChange = -1;
        final ClientSettings settings = environment.getConfigStore();
        final int currentIndex = currentIndex();
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
        blockSignals(true);
        Collection<Property> pageProperties;
        for (EditorPage page : editorPages) {
            pageProperties = page.getEditorPageModelItem().getProperties();
            for (Property property : pageProperties) {
                property.unsubscribe(this);
            }
            page.getEditorPageModelItem().unsubscribe(this);
            page.close();
        }

        openedPages.clear();
        visiblePages.clear();
        editorPages.clear();
        needForReread.clear();
        super.closeEvent(event);
    }

    public void reread() {
        needForReread.clear();
        if (currentIndex() > -1 && currentIndex() < visiblePages.size()) {
            final Id currentPageId = visiblePages.get(currentIndex());
            for (Id pageId : openedPages) {
                if (pageId.equals(currentPageId)) {
                    getEditorPageById(pageId).reread();
                } else {
                    needForReread.add(pageId);
                }
            }
        }
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
    
    private static QFont getBoldFont(final QFont font) {
        if (!font.bold()) {
            QFont boldFont = BOLD_FONT_CACHE.get(font.toString());
            if (boldFont == null) {
                boldFont = new QFont(font);
                boldFont.setBold(true);
                BOLD_FONT_CACHE.put(font.toString(), boldFont);
            }
            return boldFont;
        }
        return font;
    }    

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof CurrentTabChangeEvent){
            event.accept();
            if (scheduledCurrentTabChange>=0){
                try{
                    processCurrentTabChanged(scheduledCurrentTabChange);
                }catch(RuntimeException exception){
                    environment.getTracer().error(exception);
                }
            }
        }else{
            super.customEvent(event);
        }
    }
    
    
}
