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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.gui.QCloseEvent;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantOpenEditorError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.EditorPage;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import org.radixware.kernel.explorer.widgets.TabSet;
import org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor;

public class StandardEditor extends Editor implements ICachableWidget{
    
    private final TabSet tabSet;
    private boolean inCache;
    private boolean opening;

    public StandardEditor(IClientEnvironment environment) {
        super(environment);
        tabSet = new TabSet(environment, this);
        final QVBoxLayout layout = WidgetUtils.createVBoxLayout(content);
        content.setLayout(layout);
        layout.addWidget(tabSet);
        tabSet.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);        
        content.setFocusProxy(tabSet);        
    }                
    
    private Editor.CloseHandler closeHandler = new Editor.CloseHandler() {
        @Override
        public void onClose() {
            if (tabSet.isInited()){
                tabSet.close();
            }
        }
    };

    @Override
    public void open(Model model_) {
        Application.getInstance().getStandardViewsFactory().blockScheduledViewCreation();
        opening = true;
        try{
            super.open(model_);
            afterOpen(model_);
        }finally{
            opening = false;
            Application.getInstance().getStandardViewsFactory().unblockScheduledViewCreation();
        }
    }

    @Override
    public boolean close(boolean forced) {
        if (opening){
            getEnvironment().getTracer().error(new IllegalStateException("Closing editor inside of it's open method"));
        }
        return super.close(forced);
    }        

    private void afterOpen(final Model model_) {
        final EntityModel entityModel = getEntityModel();
        RadEditorPresentationDef def = entityModel.getEditorPresentationDef();
        List<Id> idsWithLevelOne = new ArrayList<>();

        for (RadEditorPageDef page : def.getEditorPages().getTopLevelPages()) {
            idsWithLevelOne.add(page.getId());
        }

        final List<EditorPageModelItem> modelItems = new ArrayList<>();
        for (Id id : idsWithLevelOne) {
            EditorPageModelItem modelItem = entityModel.getEditorPage(id);
            modelItems.add(modelItem);
        }
        tabSet.init(this, modelItems, model_.getDefinition().getId().toString());
        try {
            tabSet.bind();
        } catch (java.lang.RuntimeException ex) {
            throw new CantOpenEditorError(entityModel, ex);
        }     
        addCloseHandler(closeHandler);
        opened.emit(content);
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        Property property = null;
        try {
            property = getEntityModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message =
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this && tabSet != null) {
            return tabSet.setFocus(property);
        }
        return false;
    }

    @Override
    public void finishEdit() {
        if (tabSet.isInited()) {
            tabSet.finishEdit();
        }
        super.finishEdit();
    }

    @Override
    public void reread() throws ServiceClientException {
        super.reread();
        if (tabSet.isInited()) {
            tabSet.reread();
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (tabSet.isInited()) {
            tabSet.close();
        }
        removeCloseHandler(closeHandler);
        super.closeEvent(event);
    }
    
    public void setInCache(final boolean inCache){
        this.inCache = inCache;
    }

    @Override
    public boolean isInCache() {
        return inCache;
    }
    
    public AbstractPropEditor getCurrentPropEditor(){
        if (tabSet!=null && tabSet.isInited()){
            final QWidget currentTab = tabSet.currentWidget();
            if (currentTab instanceof EditorPage){
                final IEditorPageView pageView = ((EditorPage)currentTab).getView();
                if (pageView instanceof StandardEditorPage){
                    final IModelWidget pageWidget = ((StandardEditorPage)pageView).getWidget();
                    if (pageWidget instanceof PropertiesGrid){
                        return ((PropertiesGrid)pageWidget).getCurrentPropEditor();
                    }
                }
            }
        }
        return null;
    }
}