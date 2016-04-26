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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsProgressHandleManager;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.StaticText;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;


public class ViewHolder extends VerticalBox {
    
    private class PresentationChangedHandler implements IPresentationChangedHandler{
        
        private final IPresentationChangedHandler oldPCH;
        
        public PresentationChangedHandler(final EntityModel currentEntity){
            oldPCH = currentEntity.getEntityContext().getPresentationChangedHandler();            
        }

        @Override
        public EntityModel onChangePresentation(RawEntityModelData rawData, Id newPresentationClassId, Id newPresentationId) {
            final EntityModel newEntityModel = 
                oldPCH.onChangePresentation(rawData, 
                                            newPresentationClassId, 
                                            newPresentationId);
            onChangePresentation = true;
            try{
                reopen();
            }
            finally{
                onChangePresentation = false;
            }
            return newEntityModel;                        
        }
        
        public IPresentationChangedHandler getOldPCH(){
            return oldPCH;
        }
   }

    private class ErrorView extends VerticalBox {

        private final WpsEnvironment environment;
        private final ToolBar toolBar = new ToolBar();
        private final StaticText errorDetails = new StaticText();
        private final Label label = new Label("Error");
        final RwtAction reopenAction;

        public ErrorView(WpsEnvironment userSession) {
            super();
            this.environment = userSession;

            final RwtAction showDetails = new RwtAction(null, "Show Details");
            showDetails.setTextShown(true);
            showDetails.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    if (errorDetails.isVisible()) {
                        errorDetails.setVisible(false);
                        action.setText("Show Details");
                    } else {
                        errorDetails.setVisible(true);
                        action.setText("Hide Details");
                    }

                }
            });
            toolBar.addAction(showDetails);
            reopenAction = new RwtAction(null, "Try to Reopen...");
            reopenAction.setTextShown(true);
            reopenAction.addActionListener(new Action.ActionListener() {

                @Override
                public void triggered(Action action) {
                    reopen();
                }
            });
            toolBar.addAction(reopenAction);

            add(label);
            add(toolBar);
            add(errorDetails);
            errorDetails.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            errorDetails.setVisible(false);
            errorDetails.getHtml().setCss("overflow", "auto");
        }

        public void setError(String error, Throwable e) {
            if (error != null) {
                label.setText(error);
            }
            if (e != null) {
                errorDetails.setText(ClientException.exceptionStackToString(e));
            }
        }

        public void setMessage(String message) {
            label.setText(message);
        }

        public void setCanReopen(boolean canReopen) {
            reopenAction.setEnabled(canReopen);
        }

        public void setTitle(String title) {
        }

        public void close() {
            setVisible(false);
        }
    }

    public interface CloseListener {

        public void closed(ViewHolder h);
    }
    
    private IProgressHandle OPENING_VIEW_PROGRESS;
    private final WpsEnvironment environment;
    private IView currentView = null;
    private boolean onChangePresentation;
    private final ErrorView errorView;
    private IMenu selectorMenu, editorMenu;
    private ExplorerTreeNode currentNode;
    private final List<CloseListener> closeListeners = new LinkedList<>();

    public ViewHolder(WpsEnvironment env) {
        this.environment = env;

        errorView = new ErrorView(environment);
        OPENING_VIEW_PROGRESS = environment.getProgressHandleManager().newStandardProgressHandle();

        errorView.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        add(errorView);

        setTitleBarHidden(true);
    }

    @SuppressWarnings("finally")
    public IView open(ExplorerTreeNode node) throws InterruptedException {

        if (editorMenu != null) {
            editorMenu.removeAllActions();
            editorMenu.setEnabled(false);
        }
        if (selectorMenu != null) {
            selectorMenu.removeAllActions();
            selectorMenu.setEnabled(false);
        }

        if (!node.isValid()) {
            errorView.setError(node.getCreationModelExceptionMessage(), node.getCreationModelException());
            errorView.setCanReopen(false);
            errorView.setVisible(true);
            //innerWidget.setCurrentIndex(0);
            //setWindowTitle(node.getTitle());
            return null;
        } else {
//            lockRedraw();
//            setUpdatesEnabled(false);
            try {
                errorView.setVisible(false);
                final IView previousView = getCurrentView();
                if (previousView != null) {//closing previous view

                    if (previousView instanceof ISelector) {
                        //          ((ISelector) previousView).setMenu(null, null);
                    } else if (previousView instanceof IEditor) {
                        //        ((IEditor) previousView).setMenu(null);
                    }

                    final Model previousModel = previousView.getModel();
                    try {
                        if (previousModel != null && !(previousModel instanceof EntityModel)) {
                            previousModel.clean();//clean model data and close model view
                        } else //entity model data is not clean and still aviable for other (child) models after closing view
                        {
                            restorePresentationChangedHandler((EntityModel)previousModel);
                            previousView.close(true);

                        }
                        remove((UIObject) previousView);
                    } catch (RuntimeException ex) {
                        processExceptionOnClose(previousModel.getEnvironment(), ex, previousModel);
                    }
                    final String source = currentNode==null ? null : currentNode.getPath();
                    checkConfigStoreCurrentGroup(source,false);
                    checkIfProgressUnblocked(source, false);
                }

                {//opening current view
                    OPENING_VIEW_PROGRESS.startProgress(getWaitMessage(node.getView().getModel()), true);
                    Model model = node.getView().getModel();
                    try {
                        if (model instanceof EntityModel && !onChangePresentation) {
                            //reread data for entity model
                            EntityModel entity = (EntityModel) model;
                            entity.clean();
                            entity.read();
                            //Повторно взять модель т.к. она могла измениться во время read
                            model = node.getView().getModel();
                            entity = (EntityModel) model;
                            entity.getEntityContext().setPresentationChangedHandler(new PresentationChangedHandler(entity));                            
                        }
                        if (OPENING_VIEW_PROGRESS.wasCanceled()) {
                            throw new InterruptedException();
                        }

                        final IView view = model.createView();
                        if (view != null) {
                            if (view instanceof ISelector) {
                                //  ((ISelector) view).setMenu(selectorMenu, editorMenu);
                            } else if (view instanceof IEditor) {
                                //  ((IEditor) view).setMenu(editorMenu);
                            }

                            view.open(model);
                            if (OPENING_VIEW_PROGRESS.wasCanceled()) {
                                throw new InterruptedException();
                            }

                            add((UIObject) view);
//                            innerWidget.addWidget(view.asQWidget());
//                            innerWidget.setCurrentWidget(view.asQWidget());
                            //setWindowTitle(node.getTitle());
                            ((UIObject) view).getHtml().setCss("position", "relative");
                            ((UIObject) view).getHtml().setCss("left", "0px");
                            ((UIObject) view).getHtml().setCss("top", "0px");
                            ((UIObject) view).getHtml().setCss("width", "100%");
                            ((UIObject) view).getHtml().setCss("height", "100%");
                        }
                        currentView = view;
                        return view;
                    } catch (Exception ex) {
                        model.clean();
                        if (ex instanceof InterruptedException) {
                            throw (InterruptedException) ex;
                        }
                        if (ex instanceof CantOpenSelectorError) {
                            if (ex.getCause() instanceof InterruptedException) {
                                throw (InterruptedException) ex.getCause();
                            }
                        }
                        final String errMessage = environment.getMessageProvider().translate("ExplorerTree", "Can't open explorer item \'%s\'");
                        ObjectNotFoundError objectNotFound = null;
                        for (Throwable err = ex; err != null && err.getCause() != err; err = err.getCause()) {
                            if (err instanceof ObjectNotFoundError) {
                                objectNotFound = (ObjectNotFoundError) err;
                                break;
                            }
                        }
                        if (objectNotFound != null) {
                            if ((model instanceof EntityModel) && objectNotFound.inContextOf(model)) {
                                objectNotFound.setContextEntity((EntityModel) model);
                                final String title = environment.getMessageProvider().translate("ExplorerTree", "Can't Open Editor");
                                environment.messageInformation(title, objectNotFound.getMessageToShow());

                                if (environment.getTreeManager() != null) {
                                    environment.getTreeManager().afterEntitiesRemoved(objectNotFound.getPidsToSearchInExplorerTree(), null);
                                }
                                errorView.setTitle(objectNotFound.getMessageToShow());
                            } else {
                                Model parentModel;
                                for (IExplorerTreeNode parentNode = node.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                                    parentModel = parentNode.getView().getModel();
                                    if (parentNode.isValid() && objectNotFound.inContextOf(parentModel)) {
                                        if (parentModel instanceof EntityModel) {
                                            final String parentNodeTitle = ((ExplorerTreeNode) parentNode).getTitle();
                                            objectNotFound.setContextEntity((EntityModel) parentModel);
                                            errorView.setTitle(String.format(errMessage, node.getTitle()));
                                            errorView.setMessage(String.format(environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist"), parentNodeTitle));
                                        }
                                        break;
                                    }
                                }
                                if (!objectNotFound.inKnownContext()) {
                                    errorView.setError(String.format(errMessage, node.getTitle()), objectNotFound);
                                }
                            }
                        } else {
                            errorView.setError(String.format(errMessage, node.getTitle()), ex);
                        }
                        errorView.setCanReopen(true);
                        errorView.setVisible(true);
                        return null;
                    } catch(UnsupportedDefinitionVersionError error){
                        try{
                            final String errMessage = environment.getMessageProvider().translate("ExplorerTree", "Can't open explorer item \'%s\'");
                            final String reason = 
                                getEnvironment().getMessageProvider().translate("ExplorerMessage", "Current version is no longer supported.\nIt is impossible to continue work until explorer will be restarted.");
                            errorView.setError(String.format(errMessage, node.getTitle())+"\n"+reason, null);
                            errorView.setCanReopen(false);
                        }finally{//It is important do not to lost this error
                            throw error;
                        }
                    } finally {
                        OPENING_VIEW_PROGRESS.finishProgress();
                        if (previousView != null) {
                            remove((UIObject) previousView);
                        }
                        checkConfigStoreCurrentGroup(node.getPath(), true);
                        checkIfProgressUnblocked(node.getPath(), true);
                        
                    }
                }//opening view
            } finally {
//                unlockRedraw();
//                setUpdatesEnabled(true);
                currentNode = node;
            }
        }
    }

    private void reopen() {
        if (currentNode != null) {
            if (currentNode.isValid()) {
                try {
                    open(currentNode);
                } catch (InterruptedException ex) {
                }
            } else {
                environment.checkForUpdatesAction.trigger();
            }
        }
    }

    public IView getCurrentView() {
        return currentView;
    }

    private void processExceptionOnClose(IClientEnvironment userSession, final Throwable exception, final Model closingModel) {
        final String message;
        if (closingModel != null) {
            message = userSession.getMessageProvider().translate("Explorer Error", String.format("Error occured during closing model %s", closingModel.getTitle()));
        } else {
            message = "Error occured during closing";
        }
        userSession.getTracer().error(message, exception);
        userSession.getTracer().debug(message + ":\n" + ClientException.exceptionStackToString(exception), false);
    }

    private static String getWaitMessage(final Model model) {
        if (model instanceof EntityModel) {
            return model.getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Editor...");
        } else if (model instanceof GroupModel) {
            return model.getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Selector...");
        } else if (model instanceof ParagraphModel) {
            return model.getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Paragraph...");
        } else {
            return model.getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Dialog...");
        }
    }

    public void forceClose() {
        final IView view = getCurrentView();
        if (view != null) {
            final Model closingModel = view.getModel();
            try {
                //closeModalDialogs();
                if (closingModel == null || closingModel instanceof EntityModel) {
                    view.close(true);
                    restorePresentationChangedHandler((EntityModel)closingModel);
                } else {
                    closingModel.clean();
                }
            } catch (Throwable exception) {
                processExceptionOnClose(environment, exception, closingModel);
            }
            final String source = currentNode==null ? null : currentNode.getPath();
            checkConfigStoreCurrentGroup(source,false);  
            checkIfProgressUnblocked(source,false);
            currentView = null;
            close();
        }
    }

    public boolean canSafetyClose() {
        final IView view = getCurrentView();
//        if (isModalDialogsOpened()) {
//            return false;
//        }
        return view == null || view.getModel() == null || view.getModel().canSafelyClean(CleanModelController.DEFAULT_INSTANCE);
    }

    public void close() {
        if (canSafetyClose())  {
            final IView view = getCurrentView();
            if (view != null) {
                final Model closingModel = view.getModel();
                try {
                    if (closingModel == null || closingModel instanceof EntityModel) {
                        view.close(true);
                        restorePresentationChangedHandler((EntityModel)closingModel);
                        remove((UIObject) view);
                    } else {
                        closingModel.clean();
                    }
                } catch (RuntimeException exception) {
                    processExceptionOnClose(environment, exception, closingModel);
                }
                currentView = null;
            }
            errorView.close();
            remove(errorView);
            if (selectorMenu != null) {
                selectorMenu.removeAllActions();
                selectorMenu.setEnabled(false);
                selectorMenu = null;
            }
            if (editorMenu != null) {
                editorMenu.removeAllActions();
                editorMenu.setEnabled(false);
                editorMenu = null;
            }
            notifyClosed();
            currentNode = null;
        }
    }

    private void notifyClosed() {
        synchronized (closeListeners) {
            for (CloseListener l : new ArrayList<>(closeListeners)) {
                l.closed(this);
            }
        }
    }

    public final void setTitleBarHidden(boolean hidden) {
    }

    public void setEditorMenu(IMenu menu) {
        editorMenu = menu;
    }

    public void setSelectorMenu(IMenu menu) {
        selectorMenu = menu;
    }

    public void addCloseListener(CloseListener l) {
        synchronized (closeListeners) {
            if (!closeListeners.contains(l)) {
                closeListeners.add(l);
            }
        }
    }

    public void removeCloseListener(CloseListener l) {
        synchronized (closeListeners) {
            closeListeners.remove(l);
        }
    }
    
    private void checkConfigStoreCurrentGroup(final String treeNode, final boolean afterOpenView){
        final String currentGroup = environment.getConfigStore().group();
        if (currentGroup!=null && !currentGroup.isEmpty()){
            environment.getConfigStore().endAllGroups();            
            final String traceMessage;
            if (treeNode==null || treeNode.isEmpty()){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed. Forcedly closing.");
                traceMessage = String.format(messageTemplate, currentGroup);
            }else{
                if (afterOpenView){
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed when opening \'%2$s\'. Forcedly closing.");
                    traceMessage = String.format(messageTemplate, currentGroup, treeNode);                    
                }else{
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed in \'%2$s\'. Forcedly closing.");
                    traceMessage = String.format(messageTemplate, currentGroup, treeNode);
                }
            }
            environment.getTracer().error(traceMessage);
        }
    }    
    
    private void checkIfProgressUnblocked(final String treeNode, final boolean afterOpenView){
        final String traceMessage;            
        if (treeNode==null || treeNode.isEmpty()){
            traceMessage =
                environment.getMessageProvider().translate("TraceMessage","Wait dialog was blocked. Forcedly unblocking.");
        }else{
            if (afterOpenView){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("TraceMessage","Wait dialog was blocked when opening \'%1$s\'. Forcedly unblocking.");
                traceMessage = String.format(messageTemplate, treeNode);
            }else{
                final String messageTemplate = 
                    environment.getMessageProvider().translate("TraceMessage","Wait dialog was blocked in \'%1$s\'. Forcedly unblocking.");
                traceMessage = String.format(messageTemplate, treeNode);
            }
        }
        ((WpsProgressHandleManager)environment.getProgressHandleManager()).assertProgressUnblocked(traceMessage);
    }
    
    
    private static void restorePresentationChangedHandler(final EntityModel entityModel){
        if (entityModel!=null && entityModel.getEntityContext().getPresentationChangedHandler() instanceof PresentationChangedHandler){
            final PresentationChangedHandler handler = 
                    (PresentationChangedHandler)entityModel.getEntityContext().getPresentationChangedHandler();
            entityModel.getEntityContext().setPresentationChangedHandler(handler.getOldPCH());
        }
    }    
}
