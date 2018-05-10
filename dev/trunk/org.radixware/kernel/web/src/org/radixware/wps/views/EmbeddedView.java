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

package org.radixware.wps.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.ISelector.SelectorListener;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;

import org.radixware.wps.views.editor.Editor;
import org.radixware.wps.views.editor.ErrorView;
import org.radixware.wps.views.selector.RwtSelector;


public class EmbeddedView extends UIObject implements IPresentationChangedHandler, org.radixware.kernel.common.client.eas.IResponseListener, IEmbeddedView {

    private WpsEnvironment env;
    protected IView embeddedView;
    protected Id childItemId;    
    protected Model model;
    protected Model parentModel;
    private ComponentModificationRegistrator modificationRegistrator;
    private boolean editorWasRestricted = false;
    private IPresentationChangedHandler oldPch;
    private boolean isOpened = false;
    private final IEmbeddedViewContext viewContext;
    private boolean synchronizedWithParentView;
    private final ViewRestrictions initialRestrictions = new ViewRestrictions();
    protected final List<RequestHandle> handles = new ArrayList<>();
    private ErrorView errWidget;    

    protected EmbeddedView(WpsEnvironment e) {
        this(e, null, null, false, null);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            if (embeddedView != null) {
                result = ((UIObject) embeddedView).findObjectByHtmlId(id);
                if (result!=null){
                    return result;
                }
            }
            if (errWidget != null){
                return errWidget.findObjectByHtmlId(id);
            }
            return null;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (embeddedView != null) {
            ((UIObject) embeddedView).visit(visitor);
        }
        if (errWidget != null){
            errWidget.visit(visitor);
        }        
    }

    public EmbeddedView(final IClientEnvironment environment, final IView parentView, final Id childItemId, final boolean synchronizeWithParentView, final IEmbeddedViewContext context) {
        super(new Div());
        this.env = (WpsEnvironment) environment;
        errWidget = new ErrorView();
        errWidget.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        errWidget.setParent(this);
        getHtml().add(errWidget.getHtml());
        errWidget.setVisible(false);
        this.viewContext = context == null ? new IEmbeddedViewContext.EmbeddedView() : context;
        this.synchronizedWithParentView = synchronizeWithParentView;
        parentModel = parentView == null ? null : parentView.getModel();
        this.childItemId = childItemId;
        setObjectName(childItemId == null ? "embedded_view" : "embedded_view_by_" + String.valueOf(childItemId));
    }

    public EmbeddedView(final IClientEnvironment environment, final IView parentView, final Id childItemId, final boolean synchronizeWithParentView) {
        this(environment, parentView, childItemId, synchronizeWithParentView, new IEmbeddedViewContext.EmbeddedView());
    }

    public EmbeddedView(WpsEnvironment environment, IView parentView, Id childItemId) {
        this(environment, parentView, childItemId, false, new IEmbeddedViewContext.EmbeddedView());
    }

    public Id getExplorerItemId() {
        return childItemId;
    }

    public void finishEdit() {
        if (embeddedView != null) {
            if (embeddedView.getModel() != null) {
                embeddedView.getModel().finishEdit();
            } else {
                embeddedView.finishEdit();
            }
        }
    }

    @Override
    public final void open() throws ServiceClientException, InterruptedException {
        if (modificationRegistrator == null) {
            modificationRegistrator = new ComponentModificationRegistrator(this, this);
        }

        removeErrWidget();
        if (embeddedView != null) {
            close(true);
        }
        try {
            if (model == null) {
                model = createModel();
                if (model==null){//entity object creation was cancelled in event handler
                    close(true);
                    return;
                }                
            }
            embeddedView = model.createView();
            UIObject viewWidget = ((UIObject) embeddedView);

            viewWidget.setVisible(false);
            try {
                html.add(viewWidget.getHtml());

                viewWidget.setParent(this);
                embeddedView.getRestrictions().add(initialRestrictions);
                onViewCreated();
                if (embeddedView instanceof Editor) {
                    ((Editor) embeddedView).open(model, modificationRegistrator);
                    if (childItemId != null) {
                        ((Editor) embeddedView).setToolBarHidden(true);//редактор родительского объекта использует панель инструменов внешнего редактора
                    }
                    ((Editor) embeddedView).addModificationStateListener(new Editor.IModificationStateListener() {
                        @Override
                        public void onModificationStateChanged(boolean inModificationState) {
                            onModifiedStateChanged();
                        }
                    });
                    if (inModifiedStateNow()) {
                        //Чтобы предусмотреть модификацию значений свойств, которая была во время открытия
                        onModifiedStateChanged();
                    }
                } else if (embeddedView instanceof RwtSelector) {
                    ((RwtSelector) embeddedView).open(model, modificationRegistrator);
                    ((RwtSelector) embeddedView).addSelectorListener(new SelectorListener() {
                        @Override
                        public void modifiedStateChanged(boolean modified) {
                            onModifiedStateChanged();
                        }
                    });
                    if (inModifiedStateNow()) {
                        //Чтобы предусмотреть модификацию значений свойств, которая была во время открытия
                        onModifiedStateChanged();
                    }
                } else {
                    embeddedView.open(model);
                }
                isOpened = true;
            } finally {
                viewWidget.setTop(0);
                viewWidget.setLeft(0);
                viewWidget.setVCoverage(100);
                viewWidget.setHCoverage(100);
                viewWidget.setVisible(true);
            }

        } catch (RuntimeException err) {
            if (err instanceof CantOpenSelectorError) {
                if (err.getCause() instanceof InterruptedException) {
                    close(true);
                    throw (InterruptedException) err.getCause();
                }
            }
            final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot open embedded view %s");
            getEnvironment().getTracer().error(String.format(msg, childItemId == null ? getObjectName() : "#" + childItemId.toString()), err);
            close(true);
            throw err;
        }
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public void setExplorerItem(Model parentModel, Id explorerItemId) {
        if (model != null && !close(false)) {
            return;
        }
        this.parentModel = parentModel;
        this.childItemId = explorerItemId;
    }

    protected void onViewCreated() {
    }

    protected Model createModel() throws ServiceClientException, InterruptedException {
        if (parentModel == null || childItemId == null) {
            throw new IllegalUsageError("Not enouth argumets for model creation");
        }
        final Model newModel = parentModel.getChildModel(childItemId);
        if (newModel instanceof GroupModel) {
            final GroupRestrictions restrictions = ((GroupModel) newModel).getRestrictions();
            if (!restrictions.getIsEditorRestricted()) {
                restrictions.setEditorRestricted(true);
                editorWasRestricted = true;
            }
        }else if (newModel instanceof EntityModel){
            final EntityModel entityModel = (EntityModel)newModel;
            if (!entityModel.wasRead()){
                entityModel.read();//read model to get visible editor pages         
            }
            if (entityModel.getEntityContext().getPresentationChangedHandler()!=this){//NOPMD
                oldPch = entityModel.getEntityContext().getPresentationChangedHandler();
                entityModel.getEntityContext().setPresentationChangedHandler(this);
            }
        }
        return newModel;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public boolean close(boolean forced) {
        if (model == null) {
            return true;
        }
        if (embeddedView != null) {
            if (!embeddedView.close(forced) && !forced) {
                return false;
            }
            onViewClosed();
            embeddedView = null;
            isOpened = false;
        }
        model.clean();
        if (model instanceof GroupModel) {
            final GroupRestrictions restrictions = ((GroupModel) model).getRestrictions();
            if (restrictions != null) {
                if (editorWasRestricted && restrictions.canBeAllowed(ERestriction.EDITOR)) {
                    restrictions.setEditorRestricted(false);
                    editorWasRestricted = false;
                }
            }
        }
        if (modificationRegistrator != null) {
            modificationRegistrator.close();
        }
        model = null;
        return true;
    }

    @Override
    public Model getModel() {
        return model;
    }

    protected void onViewClosed() {
        if (embeddedView != null) {
            UIObject obj = (UIObject) embeddedView;
            html.remove(obj.getHtml());
            obj.setParent(null);
        }
    }

    @Override
    public void bind() {
        try {
            open();
        } catch (ServiceClientException | InterruptedException ex) {
            processExceptionOnOpen(ex);
        }
    }

    @Override
    public void refresh(ModelItem aThis) {
        bind();
    }

    @Override
    public boolean setFocus(Property aThis) {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
        if (embeddedView != null && embeddedView instanceof RwtSelector && focused) {
            embeddedView.setFocus();
        } else {
            super.setFocused(focused); 
        }
    }

    @Override
    public void reread() {
        if (embeddedView != null) {
            try {
                embeddedView.reread();
            } catch (ServiceClientException ex) {
                model.showException(ex);
            }
        }
    }

    protected void processExceptionOnOpen(final Throwable exception) {
        ObjectNotFoundError objectNotFound = null;
        for (Throwable err = exception; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) err;
            }
        }
        if (objectNotFound != null) {
            if (model instanceof EntityModel) {
                final EntityModel entity = (EntityModel) model;
                if (objectNotFound.inContextOf(entity)) {
                    objectNotFound.setContextEntity(entity);
                }
            } else if (model instanceof GroupModel) {
                objectNotFound.inContextOf((GroupModel) model);
            }
        }

        final String name;
        if (getObjectName() != null && !getObjectName().isEmpty()){
            name = "\"" + getObjectName() + "\"";
        }else{
            name = getEnvironment().getMessageProvider().translate("EmbeddedViewErr", "widget");
        }
         final String message = getEnvironment().getMessageProvider().translate("EmbeddedViewErr", "Can't open %s");
         initErrWidget();         
         errWidget.setError(String.format(message, name), objectNotFound == null ? exception : objectNotFound);
    }    
    
    private void initErrWidget(){
        if (errWidget==null){
            errWidget = new ErrorView();
            errWidget.setParent(this);
            errWidget.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            getHtml().add(errWidget.getHtml());
        }
        if (embeddedView!=null){
            embeddedView.setVisible(false);
        }
    }
    
    private void removeErrWidget(){
        if (errWidget!=null){
            getHtml().remove(errWidget.getHtml());
            errWidget.setParent(null);
            errWidget = null;
        }
        if (embeddedView!=null){
            embeddedView.setVisible(true);
        }        
    }

    @Override
    public void registerRequestHandle(final RequestHandle handle) {
        handles.add(handle);
    }

    @Override
    public void unregisterRequestHandle(final RequestHandle handle) {
        handles.remove(handle);
    }

    @Override
    public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
    }

    @Override
    public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
        close(true);
        processExceptionOnOpen(exception);
    }

    @Override
    public void onRequestCancelled(final XmlObject request, final RequestHandle handler) {
        close(true);
    }

    @Override
    public boolean close() {
        return close(true);
    }

    @Override
    public boolean isHidden() {
        return !isVisible();
    }

    @Override
    public void setUpdatesEnabled(boolean enabled) {
        //
    }

    @Override
    public boolean inModifiedStateNow() {
        if ((embeddedView instanceof IEditor) && ((IEditor) embeddedView).getActions().getUpdateAction().isEnabled()) {
            return true;
        }
        if ((embeddedView instanceof ISelector) && ((ISelector) embeddedView).getActions().getUpdateAction().isEnabled()) {
            return true;
        }
        return modificationRegistrator == null ? false : modificationRegistrator.isSomeChildComponentInModifiedState();
    }

    @Override
    public IView getView() {
        return embeddedView;
    }

    @Override
    public IEmbeddedViewContext getViewContext() {
        return viewContext;
    }

    @Override
    public boolean isSynchronizedWithParentView() {
        return synchronizedWithParentView;
    }

    @Override
    public void setSynchronizedWithParentView(final boolean isSynchronized) {
        synchronizedWithParentView = isSynchronized;
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        if (isReadOnly) {
            getRestrictions().add(ViewRestrictions.READ_ONLY);
        } else {
            getRestrictions().remove(ViewRestrictions.READ_ONLY);
        }
    }

    @Override
    public boolean isReadOnly() {
        return getRestrictions().getIsUpdateRestricted()
                && getRestrictions().getIsDeleteRestricted()
                && getRestrictions().getIsCreateRestricted();
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return isOpened() ? getView().getRestrictions() : initialRestrictions;
    }

    protected void onModifiedStateChanged() {
        if (modificationRegistrator != null) {
            modificationRegistrator.getListener().notifyComponentModificationStateChanged(this, inModifiedStateNow());
        }
    }
    
    @Override
    public final Collection<IModifableComponent> getInnerComponents() {
        return modificationRegistrator.getRegisteredComponents();
    }
    
    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
                                            final Id newPresentationClassId, 
                                            final Id newPresentationId) {
        if (oldPch==null){
            final EntityModel entity = (EntityModel)getModel();
            final IContext.Entity context = (IContext.Entity) entity.getContext();
            final RadEditorPresentationDef presentation = 
                getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
            setUpdatesEnabled(false);
            try {
                close(true);
                EntityModel newEntity = presentation.createModel(context);
                newEntity.activate(rawData);
                model = newEntity;
                bind();
                return newEntity;
            } finally {
                setUpdatesEnabled(true);
            }            
        }else{
            setUpdatesEnabled(false);
            try {
                close(true);
                EntityModel newEntity = 
                    oldPch.onChangePresentation(rawData, newPresentationClassId, newPresentationId);
                model = newEntity;
                bind();
                return newEntity;
            } finally {
                setUpdatesEnabled(true);
            }                        
        }
    }             
}
