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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.AuditLogDialog;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;

import org.radixware.wps.views.CommandToolBar;
import org.radixware.wps.views.ComponentModificationRegistrator;
import org.radixware.wps.views.ViewSupport;
import org.radixware.wps.views.RwtAction;


public abstract class Editor extends Container implements IEditor, IModificationListener {
    
    public static interface IModificationStateListener{
        void onModificationStateChanged(final boolean inModificationState);
    }

    private class ActionsImpl extends IEditor.Actions {

        public ActionsImpl(ActionController controller) {
            super(controller);
        }

        @Override
        protected Action createAction(ClientIcon icon, String title) {
            RwtAction action = new RwtAction(Editor.this.getEnvironment().getApplication().getImageManager().getIcon(icon));
            action.setToolTip(title);
            return action;
        }
    }        
    
    private final List<CloseHandler> closeHandlers = new LinkedList<>();
    private List<IModificationStateListener> modificationListeners;
    private final ViewSupport<Editor> viewSupport;
    protected final WpsEnvironment env;
    private final ActionsImpl actions;
    private final ActionController actionController;
    private final EditorController editorController;
    private final EditorUIController editorUIController;

    private static class ToolBarHandle extends AbstractContainer {

        public ToolBarHandle() {
            super(new Div());
            setSizePolicy(SizePolicy.EXPAND, SizePolicy.PREFERRED);
            setHeight(30);
            setTop(0);
            setLeft(0);
            setHCoverage(100);
            html.setCss("border-bottom", "solid 1px #BBB");
        }
    }
    private ToolBarHandle tbHandle = null;

    public Editor(final IClientEnvironment env) {
        this.env = (WpsEnvironment) env;
        this.viewSupport = new ViewSupport<>(this);
        this.actionController = new ActionController(this) {

            @Override
            protected void modifiedStateChanged(boolean modified) {
                notifyModificationStateListeners(modified);
            }

            @Override
            protected void execAuditLogDialog(IClientEnvironment env, IEditor editor, Id tableId, Pid pid, String title) {
                AuditLogDialog.execAuditLogDialog(env, tableId, pid, title);
            }
        };
        this.actions = new Editor.ActionsImpl(actionController);
        this.editorUIController = new EditorUIController(this) {

            @Override
            protected IToolBar createToolBar() {
                ToolBar toolBar = new ToolBar();
                addToolBar(toolBar);
                return toolBar;
            }

            @Override
            protected ICommandToolBar createCommandToolBar(IClientEnvironment env, IEditor editor) {
                CommandToolBar commandBar = new CommandToolBar();
                addToolBar(commandBar);

                return commandBar;
            }

            @Override
            protected void putTextToSystemClipboard(final String text) {
                throw new UnsupportedOperationException("Not supported yet.");
            }                        

            @Override
            protected void applySettings(IClientEnvironment env, IToolBar toolBar, ICommandToolBar commandToolBar) {
            }

            @Override
            protected void restoreState(ClientSettings settings, String key) {
            }
        };
        this.editorController = new EditorController(this, this.editorUIController) {

            @Override
            protected void entityUpdated() {
                defaultListener.entityUpdated();
            }

            @Override
            protected void entityRemoved() {
                defaultListener.entityRemoved();
            }

            @Override
            protected void notifyClosed() {
                defaulCloseHandler.onClose();
            }

            @Override
            protected void cleanUpEditorAfterClose(EntityModel entity) {
                if (modificationListeners!=null){
                    modificationListeners.clear();
                }
            }

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }
        };
    }

    private void addToolBar(ToolBar toolBar) {
        if (tbHandle == null) {
            tbHandle = new ToolBarHandle();
            addToolBarComponent(0, tbHandle);
        }
        tbHandle.add(toolBar);
        checkTbHandleVisibility();
    }

    abstract UIObject getMainComponent();

    int getMainComponentTop() {
        return isToolBarsVisible() ? 33 : 0;
    }

    private boolean isToolBarsVisible() {
        boolean isVisible = false;
        if (tbHandle != null) {
            for (UIObject obj : tbHandle.getChildren()) {
                if (obj.isVisible()) {
                    isVisible = true;
                    break;
                }
            }
        }
        return isVisible;
    }

    private void checkTbHandleVisibility() {
        boolean hideToolBar = !isToolBarsVisible();
        if (hideToolBar) {
            if (tbHandle != null) {
                tbHandle.setVisible(false);
            }
        } else {
            if (tbHandle != null) {
                tbHandle.setVisible(true);
            }
        }
        UIObject main = getMainComponent();
        if (main != null) {
            main.setTop(getMainComponentTop());
        }
    }

    void addToolBarComponent(int index, UIObject c) {
        add(0, c);
    }

    @Override
    public Actions getActions() {
        return actions;
    }

    @Override
    public boolean delete() throws ServiceClientException {
        return getController().delete();
    }

    @Override
    public EditorController getController() {
        return editorController;
    }

    @Override
    public EntityModel getEntityModel() {
        return getController().getEntity();
    }

    @Override
    public void visitChildren(final IView.Visitor visitor, final boolean recursively) {
        getController().visitChildren(visitor, recursively);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return getController().getViewRestrictions();
    }        

    @Override
    public void refresh() {
    }

    @Override
    public boolean applyChanges() throws ServiceClientException, ModelException {
        return getController().applyChanges();
    }

    @Override
    public void cancelChanges() {
        getController().cancelChanges();
    }

    @Override
    public void addEditorListener(final EditorListener l) {
        getController().addEditorListener(l);
    }

    @Override
    public void removeEditorListener(final EditorListener l) {
        getController().removeEditorListener(l);
    }

    @Override
    public void addCloseHandler(CloseHandler handler) {
        synchronized (closeHandlers) {
            if (!closeHandlers.contains(handler)) {
                closeHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeCloseHandler(CloseHandler handler) {
        synchronized (closeHandlers) {
            closeHandlers.remove(handler);
        }
    }
    private boolean isToolBarHidden;
    private boolean isCommandBarHidden;

    @Override
    public void setToolBarHidden(boolean hidden) {
        editorUIController.setToolBarHidden(hidden);
        isToolBarHidden = hidden;
        checkTbHandleVisibility();
    }

    @Override
    public void setMenuHidden(boolean hidden) {
    }

    @Override
    public void open(Model model) {
        getController().open(model);
        fireOpened();
    }
    
    public void open(final Model model_, final ComponentModificationRegistrator registrator) {
        open(model_);
        getController().setModificationRegistractor(registrator);
    }    

    protected void fireOpened() {
        defaulOpenHandler.afterOpen();
    }

    @Override
    public void setFocus() {
    }

    @Override
    public boolean setFocusedProperty(Id id) {
        return false;
    }

    @Override
    public boolean close(boolean forced) {
        try{
            return getController().close(forced);
        }finally{
            closeHandlers.clear();
            openHandlers.clear();
        }
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return getController().canSafelyClose(this, cleanController);
    }

    @Override
    public void finishEdit() {
        getController().finishEdit();
    }

    @Override
    public Model getModel() {
        return getController().getEntity();
    }

    @Override
    public void reread() throws ServiceClientException {
        getController().reread();
    }

    @Override
    public boolean hasUI() {
        return true;
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
    public boolean isDisabled() {
        return false;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public void setCommandBarHidden(boolean hidden) {
        editorUIController.setCommandBarHidden(hidden);
        isCommandBarHidden = hidden;
        checkTbHandleVisibility();
    }

    @Override
    public void addOpenHandler(OpenHandler handler) {
        synchronized (openHandlers) {
            if (!openHandlers.contains(handler)) {
                openHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeOpenHandler(OpenHandler handler) {
        synchronized (openHandlers) {
            openHandlers.remove(handler);
        }
    }
    private final List<OpenHandler> openHandlers = new LinkedList<>();
    private final List<IEditor.EditorListener> editorListeners = new LinkedList<>();
    private final OpenHandler defaulOpenHandler = new OpenHandler() {

        @Override
        public void afterOpen() {
            final List<OpenHandler> ls;
            synchronized (openHandlers) {
                ls = new ArrayList<>(openHandlers);
            }
            for (OpenHandler h : ls) {
                h.afterOpen();
            }
        }
    };
    private final CloseHandler defaulCloseHandler = new CloseHandler() {

        @Override
        public void onClose() {
            final List<CloseHandler> ls;
            synchronized (closeHandlers) {
                ls = new ArrayList<>(closeHandlers);
            }
            for (CloseHandler h : ls) {
                h.onClose();
            }
        }
    };
    private final IEditor.EditorListener defaultListener = new IEditor.EditorListener() {

        @Override
        public void entityUpdated() {
            final List<IEditor.EditorListener> ls;
            synchronized (editorListeners) {
                ls = new ArrayList<>(editorListeners);
            }
            for (IEditor.EditorListener l : ls) {
                l.entityUpdated();
            }
        }

        @Override
        public void entityRemoved() {
            final List<IEditor.EditorListener> ls;
            synchronized (editorListeners) {
                ls = new ArrayList<>(editorListeners);
            }
            for (IEditor.EditorListener l : ls) {
                l.entityRemoved();
            }
        }
    };

    @Override
    public void notifyPropertyModificationStateChanged(final Property property, final boolean modified) {
        getController().notifyPropertyModificationStateChanged(property, modified);
    }

    @Override
    public void notifyComponentModificationStateChanged(final IModifableComponent childComponent, final boolean modified) {
        getController().notifyComponentModificationStateChanged(childComponent, modified);
    }

    @Override
    public void childComponentWasClosed(final IModifableComponent childComponent) {
        getController().childComponentWasClosed(childComponent);
    }        
    
    public void addModificationStateListener(final IModificationStateListener listener){
        if (modificationListeners==null){
            modificationListeners = new LinkedList<>();
            modificationListeners.add(listener);
        }else if (!modificationListeners.contains(listener)){
            modificationListeners.add(listener);
        }
    }
    
    public void removeModificationStateListener(final IModificationStateListener listener){
        if (modificationListeners!=null){
            modificationListeners.remove(listener);
        }
    }
    
    private void notifyModificationStateListeners(final boolean inModificationState){
        if (modificationListeners!=null){
            final List<IModificationStateListener> listenersList = new LinkedList<>(modificationListeners);
            for (IModificationStateListener listener: listenersList){
                listener.onModificationStateChanged(inModificationState);
            }
        }
    }
    
    
}
