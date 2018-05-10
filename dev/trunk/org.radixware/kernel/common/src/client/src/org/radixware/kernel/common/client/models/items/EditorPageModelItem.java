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

package org.radixware.kernel.common.client.models.items;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantLoadCustomViewError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.editorpages.*;//NOPMD
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEditorPageWidget;
import org.radixware.kernel.common.client.views.ITabSetWidget;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public final class EditorPageModelItem extends ModelItem {

    final public RadEditorPageDef def;
    private boolean visible;
    private boolean restricted;
    private boolean enabled;
    private boolean isEdited;
    private WeakReference<IEditorPageView> viewRef;
    private String title;
    private Color titleColor;
    private Icon icon;
    private List<EditorPageModelItem> childPages = null;
    private List<Property> properties = null;
    private List<PropertiesGroupModelItem> propertyGroups = null;
    private List<Property> userProperties = new ArrayList<>();    

    @SuppressWarnings("UseSpecificCatch")
    public EditorPageModelItem(final ModelWithPages owner, final RadEditorPageDef page) {
        super(owner, page.getId());
        def = page;
        visible = def.isVisible;
        enabled = true;
        if (def instanceof RadContainerEditorPageDef) {
            final RadContainerEditorPageDef editorPageDef = (RadContainerEditorPageDef) def;
            final Id explorerItemId = editorPageDef.getExplorerItemId();

            try {
                if (!editorPageDef.hasOwnTitle()) {
                    final RadExplorerItems items;
                    if (owner.getDefinition() instanceof IExplorerItemsHolder){
                        items = ((IExplorerItemsHolder)owner.getDefinition()).getChildrenExplorerItems();
                    }else{
                        items = null;
                    }
                    final RadExplorerItemDef item = items==null ? null : items.findExplorerItem(explorerItemId);
                    if (item != null) {
                        if (item instanceof RadParentRefExplorerItemDef) {
                            //презентация редактора заранее неизвестна
                            if ((owner instanceof EntityModel) && ((EntityModel) owner).isExists() && owner.isExplorerItemAccessible(explorerItemId)) {
                                title = owner.getChildModel(explorerItemId).getDefinition().getTitle();
                            } else {
                                title = def.getTitle();
                            }
                        } else {
                            title = item.getTitle();
                        }
                    } else {
                        title = def.getTitle();
                    }
                } else {
                    title = def.getTitle();
                }
            }
            catch (RuntimeException ex) {
                title = def.getTitle();
            }
            catch (Exception ex) {
                title = def.getTitle();
            }

            try {
                if (!editorPageDef.hasOwnIcon() && owner.isExplorerItemAccessible(explorerItemId) && owner.getChildModel(explorerItemId) != null) {
                    icon = owner.getChildModel(explorerItemId).getIcon();
                } else {
                    icon = def.getIcon();
                }
            }
            catch (RuntimeException ex) {
                icon = def.getIcon();
            }
            catch (Exception ex) {
                icon = def.getIcon();
            }

        } else {
            title = def.getTitle();
            icon = def.getIcon();
        }
    }    

    @Override
    public ModelWithPages getOwner() {
        return (ModelWithPages)super.getOwner();
    }
        
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        afterModify();
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        afterModify();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        afterModify();
    }
    
    public Color getTitleColor(){
        return titleColor;
    }
    
    public void setTitleColor(final Color color){
        titleColor = color;
        afterModify();
    }

    public boolean isVisible() {
        return visible && !restricted && hasVisibleContent();
    }    
    
    private boolean hasVisibleContent(){
        if (def instanceof RadStandardEditorPageDef){
            for (Property property: getProperties()){
                if (property.isVisible()){
                    return true;
                }
            }
            for (EditorPageModelItem childPage: getChildPages()){
                if (childPage.isVisible()){
                    return true;
                }
            }
            for (PropertiesGroupModelItem childGroup: getPropertyGroups()){
                if (childGroup.isVisible()){
                    return true;
                }
            }
            return false;
        }else{
            return true;
        }
    }

    public RadEditorPageDef getDefinition() {
        return def;
    }

    public void setVisible(boolean visible) {        
        final boolean oldVisibility = isVisible();
        final List<EditorPageModelItem> parentPages = getParentPages();
        /*final Map<Id,Boolean> parentPagesVisibility = new HashMap<>();
        for (EditorPageModelItem parentPage: parentPages){
            parentPagesVisibility.put(parentPage.getId(), parentPage.isVisible());
        } */       
        this.visible = visible;
        if (oldVisibility!=isVisible()){
            afterModify();
            for (int i=parentPages.size()-1; i>=0; i--){
                parentPages.get(i).afterModify();
                /*final EditorPageModelItem parentPage = parentPages.get(i);
                if (parentPage.isVisible()!=parentPagesVisibility.get(parentPage.getId()).booleanValue()){
                    parentPage.afterModify();
                }*/
            }
        }
    }

    public IEditorPageWidget getEditorPageWidget() {
        return getEnvironment().getApplication().getStandardViewsFactory().newEditorPageWidget(this);
    }

    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
            if (def instanceof RadStandardEditorPageDef) {
                final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef) def;
                final Collection<RadStandardEditorPageDef.PageItem> pageItems =
                        pageDef.getRootPropertiesGroup().getPageItems();
                Property property;
                for (RadStandardEditorPageDef.PageItem item : pageItems) {
                    if (item.getItemId().getPrefix() != EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP && 
                        owner.getDefinition().isPropertyDefExistsById(item.getItemId())
                       ) {
                        property = owner.getProperty(item.getItemId());
                        properties.add(property);
                    }
                }
            }
        }
        final List<Property> result = new ArrayList<>();
        result.addAll(properties);
        result.addAll(userProperties);
        return Collections.unmodifiableList(result);
    }        

    public final void addProperty(final Id propertyId) {
        final Property property = owner.getProperty(propertyId);
        userProperties.add(property);
        afterModify();
    }

    public final boolean removeLastProperty() {
        if (!userProperties.isEmpty()) {
            userProperties.remove(userProperties.size() - 1);
            afterModify();
            return true;
        }
        return false;
    }

    public void setEdited(boolean wasChanged) {
        if (isEdited != wasChanged) {
            isEdited = wasChanged;
            afterModify();
        }
    }

    public boolean isEdited() {
        return isEdited;
    }

    @SuppressWarnings("UseSpecificCatch")
    public IEditorPageView createView() {
        final IView parentView = owner.getView();
        final IEditorPageView pageView;
        if (def instanceof RadCustomEditorPageDef) {
            RadCustomEditorPageDef page = (RadCustomEditorPageDef) def;
            Class<IEditorPageView> classView;
            try {
                classView = getEnvironment().getDefManager().getCustomEditorPageClass(page.getCustomDialogId());
                Constructor<IEditorPageView> constructor = classView.getConstructor(IClientEnvironment.class,IView.class, RadEditorPageDef.class);
                pageView = constructor.newInstance(getEnvironment(),parentView, def);
            } catch (Exception e) {
                throw new CantLoadCustomViewError(this, e);
            }
        } else {
            pageView = getEnvironment().getApplication().getStandardViewsFactory().newStandardEditorPage(getEnvironment(), parentView, def);
        }
        pageView.setObjectName("rx_editor_page_view_#"+getId().toString());
        viewRef = new WeakReference<>(pageView);
        return pageView;
    }

    public IEditorPageView getView() {
        final IEditorPageView pageView = viewRef == null ? null : viewRef.get();
        return pageView==null || !pageView.hasUI() ? null : pageView;
    }

    void disposeView() {
        if (viewRef != null && viewRef.get() != null) {
            unsubscribeAll();
            viewRef = null;
        }
    }

    public List<EditorPageModelItem> getChildPages() {
        if (childPages == null) {
            childPages = new ArrayList<>();
            final List<RadEditorPageDef> pages = def.getSubPages();
            for (RadEditorPageDef page : pages) {
                if(!(page instanceof RadContainerEditorPageDef)|| !(owner instanceof EntityModel)
                    || ((EntityModel) owner).isExists()) {//Для новой сущности подстраницы-контейнеры не добавляются
                    EditorPageModelItem childPage = getEditorPageModelItem(page.getId());
                    if (childPage==null){
                        childPage = page.newModelItem(getOwner());
                    }                        
                    if(page instanceof RadContainerEditorPageDef){
                        try{
                            if(!childPage.isAccessible()){
                                continue;
                            }
                        }catch (InterruptedException ex) {
                            break;
                        } catch (ServiceClientException error) {
                            final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot add editor page for explorer item #%s:\n %s"),
                                    reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), error),
                                    stack = ClientException.exceptionStackToString(error);
                            Id explorerItemId=((RadContainerEditorPageDef) page).getExplorerItemId();
                            getEnvironment().getTracer().put(EEventSeverity.ERROR, String.format(msg, explorerItemId.toString(), reason),
                                    EEventSource.EXPLORER);
                            getEnvironment().getTracer().put(EEventSeverity.DEBUG, stack,
                                    EEventSource.EXPLORER);
                            break;
                        }                        
                    }
                    if (childPage.def instanceof RadStandardEditorPageDef==false
                        || !childPage.getProperties().isEmpty()
                        || !childPage.getChildPages().isEmpty()){
                        childPages.add(childPage);
                    }
                }
            }
        }
        return Collections.unmodifiableList(childPages);
    }
    
    public Collection<PropertiesGroupModelItem> getPropertyGroups(){
        if (propertyGroups==null){
            propertyGroups = new ArrayList<>();
            if (def instanceof RadStandardEditorPageDef) {
                final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef) def;
                final Collection<RadStandardEditorPageDef.PageItem> pageItems =
                        pageDef.getRootPropertiesGroup().getPageItems();
                RadStandardEditorPageDef.PropertiesGroup groupDef;
                for (RadStandardEditorPageDef.PageItem item : pageItems) {
                    if (item.getItemId().getPrefix() == EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP
                        && getOwner().isPropertiesGroupExists(item.getItemId())) {
                        propertyGroups.add(getOwner().getPropertiesGroup(item.getItemId()));
                    }
                }
            }
        }
        return Collections.unmodifiableList(propertyGroups);
    }
    
    public boolean isAccessible() throws ServiceClientException, InterruptedException {
        if(def instanceof RadContainerEditorPageDef){
            //Для страниц-контейнеров проверяем, что владелец - существующая entity
            if (!(getOwner() instanceof EntityModel)
                    || !((EntityModel) getOwner()).isExists()) {
                return false;
            }
            //Для страниц-контейнеров проверяем, что доступен элемент проводника
            final Id explorerItemId = ((RadContainerEditorPageDef) def).getExplorerItemId();
            if (!getOwner().isExplorerItemAccessible(explorerItemId)) {
                final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Editor page for explorer item #%s is not accessible");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, explorerItemId.toString()),
                        EEventSource.EXPLORER);
                return false;
            }
        }
        return true;
    }
    
    public boolean isRestricted(){
        return restricted;
    }
    
    public void setRestricted(final boolean isRestricted){
        final List<EditorPageModelItem> parentPages = getParentPages();
        final Map<Id,Boolean> parentPagesVisibility = new HashMap<>();
        for (EditorPageModelItem parentPage: parentPages){
            parentPagesVisibility.put(parentPage.getId(), parentPage.isVisible());
        }        
        final boolean oldVisibility = isVisible();
        restricted = isRestricted;
        if (oldVisibility!=isVisible()){
            afterModify();
            for (int i=parentPages.size()-1; i>=0; i--){
                final EditorPageModelItem parentPage = parentPages.get(i);
                if (parentPage.isVisible()!=parentPagesVisibility.get(parentPage.getId()).booleanValue()){
                    parentPage.afterModify();
                }
            }            
        }
    };        

    public boolean setFocused() {        
        for (EditorPageModelItem parentPage: getParentPages()){
            if (!setFocusedImpl(parentPage)){
                return false;
            }
        }
        return setFocusedImpl(this);
    }
    
    public boolean activate(){
        for (EditorPageModelItem parentPage: getParentPages()){
            if (!activateImpl(parentPage)){
                return false;
            }
        }
        return activateImpl(this);
    }
    
    private boolean setFocusedImpl(final EditorPageModelItem page){
        if (page.isVisible() && page.widgets != null) {
            for (IModelWidget widget : page.widgets) {
                if ((widget instanceof ITabSetWidget) && ((ITabSetWidget) widget).setCurrentEditorPage(page.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean activateImpl(final EditorPageModelItem page){
        if (page.isVisible() && page.widgets != null) {
            for (IModelWidget widget : page.widgets) {
                if ((widget instanceof ITabSetWidget) && ((ITabSetWidget) widget).activateEditorPage(page.getId())) {
                    return true;
                }
            }
        }
        return false;
    }    
    
    private List<EditorPageModelItem> getParentPages(){
        final List<EditorPageModelItem> parentPages = new LinkedList<>();
        final RadEditorPages editorPages = ((IEditorPagesHolder)owner.getDefinition()).getEditorPages();        
        RadEditorPageDef parentPage = editorPages.getParentPageById(getId());
        while(parentPage!=null){
            parentPages.add(0,getEditorPageModelItem(parentPage.getId()));
            parentPage = editorPages.getParentPageById(parentPage.getId());
        }
        return parentPages;
    }    
    
    private EditorPageModelItem getEditorPageModelItem(final Id pageId){
        return getOwner().getEditorPage(pageId);
    }
    
    public EditorPageModelItem getParentPage(){
        final RadEditorPages editorPages = ((IEditorPagesHolder)owner.getDefinition()).getEditorPages();
        final RadEditorPageDef parentPage = editorPages.getParentPageById(getId());
        return parentPage==null ? null : getEditorPageModelItem(parentPage.getId());
    }
}