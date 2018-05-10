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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;

import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;

import org.radixware.wps.views.EmbeddedView;
import org.radixware.wps.views.ViewSupport;

public class StandardEditorPage extends VerticalBoxContainer implements IEditorPageView {
    
    private final IModelWidget generalPageWidget;
    private final ChildPagesWidget childPagesWidget;
    
    private final ViewSupport<StandardEditorPage> viewSupport;
    private final ViewRestrictions restrictions;
    private boolean isContentinited;
    private boolean wasClosed;
    private UIObject devider;
    private WpsEnvironment env;    
    private ModelWithPages model;    

    private void initContent() {
        clearContent();
        if (generalPageWidget!=null){
            add((UIObject)generalPageWidget);
        }
        if (childPagesWidget!=null){
            if (generalPageWidget!=null){
                devider = addSpace(6);
            }
            add((UIObject)childPagesWidget);
        }        
        isContentinited = true;
        updateGeometry();
    }
    
    private void clearContent() {
        if (isContentinited){
            if (generalPageWidget!=null){
                remove((UIObject)generalPageWidget);
            }
            if (childPagesWidget!=null){
                remove(childPagesWidget);
            }
            isContentinited = false;
        }
    }
    
    public void updateGeometry(){
        if (isContentinited){
            final boolean childPagesVisible = childPagesWidget!=null && childPagesWidget.isVisible();
            final boolean generalPageWidgetVisible = generalPageWidget instanceof UIObject && ((UIObject)generalPageWidget).isVisible();            
            setAutoSize(childPagesWidget, childPagesVisible && generalPageWidgetVisible);
            if (childPagesVisible && generalPageWidgetVisible){
                ((UIObject)generalPageWidget).setVSizePolicy(SizePolicy.MINIMUM_EXPAND);                
            }else if (childPagesVisible) {
                childPagesWidget.setVSizePolicy(SizePolicy.EXPAND);
            }else if (generalPageWidgetVisible){
                ((UIObject)generalPageWidget).setVSizePolicy(SizePolicy.EXPAND);
            }
            if (devider!=null){
                devider.setVisible(childPagesWidget.isVisible());
            }
        }
    }

    public StandardEditorPage(final IClientEnvironment environment, final IView parentView, final RadEditorPageDef page) {
        this.env = (WpsEnvironment) environment;
        viewSupport = new ViewSupport<>(this);
        restrictions = new ViewRestrictions(this);
        if (parentView.getModel() instanceof ModelWithPages==false) {
            throw new IllegalArgumentError("parent view must be editor, form, report or filter");
        }
        final EditorPageModelItem editorPage =         
            ((ModelWithPages) parentView.getModel()).getEditorPage(page.getId());        
        if (page instanceof RadStandardEditorPageDef) {
            final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef)page;
            if (pageDef.hasProperties()){
                generalPageWidget = new PropertiesGrid(editorPage);                
                if (pageDef.hasSubPages()){
                    childPagesWidget = 
                        new ChildPagesWidget(env, this, parentView, editorPage.getChildPages(), pageDef.getId());
                }else{
                    childPagesWidget = null;
                }
            }else if (pageDef.hasSubPages()){
                generalPageWidget = new TabSet(env, parentView, editorPage.getChildPages(), page.getId());
                childPagesWidget = null;
            }else{
                generalPageWidget = null;
                childPagesWidget = null;
            }
        } else if (page instanceof RadContainerEditorPageDef) {
            final RadContainerEditorPageDef container = (RadContainerEditorPageDef) page;
            final Id explorerItemId = container.getExplorerItemId();
            final RadExplorerItems childItems;
            if (editorPage.getOwner().getDefinition() instanceof IExplorerItemsHolder){
                childItems = 
                    ((IExplorerItemsHolder)editorPage.getOwner().getDefinition()).getChildrenExplorerItems();
            }else{
                childItems = null;
            }
            final boolean isSynchronized = 
                childItems==null ? false : childItems.findExplorerItem(explorerItemId)  instanceof RadParentRefExplorerItemDef;
            generalPageWidget = new EmbeddedView(env, parentView, container.getExplorerItemId(), isSynchronized, new IEmbeddedViewContext.EditorPage(editorPage));            
            ((UIObject)generalPageWidget).setObjectName(editorPage.getTitle());
            ((UIObject)generalPageWidget).setTop(0);
            if (container.hasSubPages()){
                childPagesWidget = 
                    new ChildPagesWidget(env, this, parentView, editorPage.getChildPages(), container.getId());
            }else{
                childPagesWidget = null;
            }            
        } else {
            throw new IllegalArgumentError("Unknown page class " + page.getClass().getName());
        }
        initContent();
    }

    @Override
    public void open(final Model model) {
        if (model instanceof ModelWithPages==false){
            throw new IllegalUsageError("not supported model " + model.getClass().getSimpleName());
        }
        this.model = (ModelWithPages)model;
        if (!isContentinited){
            initContent();
        }
        if (generalPageWidget!=null){
            generalPageWidget.bind();
            if (generalPageWidget instanceof EmbeddedView) {
                final EmbeddedView embeddedView = (EmbeddedView) generalPageWidget;
                if (embeddedView.isOpened()) {
                    try {
                        final Model child = getModel().getChildModel(embeddedView.getExplorerItemId());
                        if (child.getView() instanceof Editor) {
                            //Прячем панель инструментов вложенного редактора
                            ((Editor) child.getView()).setToolBarHidden(true);
                        }
                    } catch (ServiceClientException | InterruptedException ex) {
                        //К этому моменту child уже должен быть создан
                    }
                } else {
                    try {
                        embeddedView.open();
                    } catch (ServiceClientException | InterruptedException ex) {
                    }
                }
            }

            ((UIObject)generalPageWidget).setVisible(true);
            ((UIObject)generalPageWidget).setFocused(true);
        }
        if (childPagesWidget!=null){
            childPagesWidget.bind();
        }
        notifyOpend();
    }

    private void notifyOpend() {
    }

    @Override
    public void setFocus() {
        if (generalPageWidget != null) {
            ((UIObject)generalPageWidget).setFocused(true);
        }
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        if (generalPageWidget != null) {
            final Property property;
            try {
                property = getModel().getProperty(propertyId);
            } catch (RuntimeException ex) {
                final String message =
                        getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
                final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
                final String stack = ClientException.exceptionStackToString(ex);
                getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
                return false;
            }
            return generalPageWidget.setFocus(property);
        }
        return false;
    }

    @Override
    public boolean close(final boolean forced) {
        if (generalPageWidget instanceof TabSet) {
            ((TabSet) generalPageWidget).close();
            return true;
        } else if (generalPageWidget instanceof EmbeddedView) {
            if (!((EmbeddedView) generalPageWidget).close(forced)) {
                return false;
            }
        }
        else if (generalPageWidget instanceof PropertiesGrid){
            ((PropertiesGrid) generalPageWidget).close();
        }
        if (childPagesWidget!=null){
            childPagesWidget.close();
        }
        clearContent();
        wasClosed = true;
        return true;
    }

    @Override
    public void finishEdit() {
        if (generalPageWidget instanceof TabSet) {
            ((TabSet) generalPageWidget).finishEdit();          
        } else if (generalPageWidget instanceof EmbeddedView) {
            ((EmbeddedView) generalPageWidget).finishEdit();           
        }
        if (childPagesWidget!=null){
            childPagesWidget.finishEdit();
        }

    }

    @Override
    public ModelWithPages getModel() {
        return model;
    }

    @Override
    public void reread() throws ServiceClientException {
        if (generalPageWidget instanceof EmbeddedView) {
            final EmbeddedView view = (EmbeddedView) generalPageWidget;
            if (view.isOpened()) {
                view.reread();
            } else if (getModel() != null) {
                open(getModel());
            }
        } else if (generalPageWidget instanceof TabSet) {
            ((TabSet) generalPageWidget).reread();
        }
        if (childPagesWidget!=null){
            childPagesWidget.reread();
        }
    }

    @Override
    public boolean hasUI() {
        return !wasClosed;
    }

    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }
    
    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        //do nothing
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return restrictions;
    }        

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }    

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }
}
