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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDockWidget;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.PasswordExpiredError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.errors.UserAccountLockedError;
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
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.WaitDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.progress.ExplorerProgressHandleManager;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;

import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class ViewHolder extends QDockWidget{
    
    private class PresentationChangedHandler implements IPresentationChangedHandler{
        
        private final IPresentationChangedHandler oldPCH;
        
        public PresentationChangedHandler(final EntityModel currentEntity){
            oldPCH = currentEntity.getEntityContext().getPresentationChangedHandler();            
        }

        @Override
        public EntityModel onChangePresentation(final RawEntityModelData rawData,
                                                final Id newPresentationClassId, 
                                                final Id newPresentationId) {
            final EntityModel newEntityModel = 
                oldPCH.onChangePresentation(rawData,
                                            newPresentationClassId, newPresentationId);
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

    private final static Qt.DockWidgetAreas ALLOWED_AREAS =
            new Qt.DockWidgetAreas(Qt.DockWidgetArea.LeftDockWidgetArea,
            Qt.DockWidgetArea.RightDockWidgetArea,
            Qt.DockWidgetArea.TopDockWidgetArea,
            Qt.DockWidgetArea.BottomDockWidgetArea);
    private final static QDockWidget.DockWidgetFeatures FEATURES =
            new QDockWidget.DockWidgetFeatures(QDockWidget.DockWidgetFeature.DockWidgetClosable);
    private final QStackedWidget innerWidget = new QStackedWidget();
    private final ErrorView errorView;
    private boolean closed;
    private int viewIndex = -1;
    private ExplorerTreeNode currentNode;
    private boolean onChangePresentation;
    public final Signal0 wasClosed = new Signal0();    
    private final IProgressHandle OPENING_VIEW_PROGRESS;
    private final IClientEnvironment environment;
    private ExplorerMenu selectorMenu, editorMenu;
    

    @SuppressWarnings("LeakingThisInConstructor")
    public ViewHolder(final IClientEnvironment environment) {
        super();
        this.environment = environment;
        errorView = new ErrorView(environment);
        OPENING_VIEW_PROGRESS = environment.getProgressHandleManager().newStandardProgressHandle();
        setFeatures(FEATURES);
        setAllowedAreas(ALLOWED_AREAS);
        errorView.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        errorView.tryReopen.connect(this, "reopen()");
        innerWidget.addWidget(errorView);
        setWidget(innerWidget);
        innerWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        setTitleBarHidden(true);
    }

    public void setEditorMenu(final ExplorerMenu menu) {
        editorMenu = menu;
    }

    public void setSelectorMenu(final ExplorerMenu menu) {
        selectorMenu = menu;
    }

    public final void setTitleBarHidden(final boolean isHidden) {
        if (isHidden) {
            final QWidget titleBar = new QWidget();
            titleBar.setObjectName("Rdx.ViewHolder.titleBar");
            setTitleBarWidget(titleBar);
            titleBar.setVisible(false);
        } else {
            setTitleBarWidget(null);
        }
    }

    private static void traceExceptionOnClose(final IClientEnvironment environment, final Throwable exception, final Model closingModel) {
        final String message;
        if (closingModel != null) {
            message = environment.getMessageProvider().translate("Explorer Error", String.format("Error occured during closing model %s", closingModel.getTitle()));
        } else {
            message = "Error occured during closing";
        }
        environment.getTracer().debug(message + ":\n" + ClientException.exceptionStackToString(exception), false);
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
    
    @SuppressWarnings("finally")
    public IExplorerView open(final ExplorerTreeNode node) throws InterruptedException {
        if (editorMenu != null) {
            editorMenu.removeAllActions();
            editorMenu.clear();
            editorMenu.setDisabled(true);
        }
        if (selectorMenu != null) {
            selectorMenu.removeAllActions();
            selectorMenu.clear();
            selectorMenu.setDisabled(true);
        }

        if (!node.isValid()) {
            errorView.setError(node.getCreationModelExceptionMessage(), node.getCreationModelException());
            errorView.setCanReopen(false);
            innerWidget.setCurrentIndex(0);
            setWindowTitle(node.getTitle());
            return null;
        } else {
//            lockRedraw();
//            setUpdatesEnabled(false);
            final long time = System.currentTimeMillis();
            {
                final String message = 
                    environment.getMessageProvider().translate("TraceMessage", "Start opening view for \'%1$s\'");
                environment.getTracer().debug(String.format(message, node.toString()));
            }
            try {
                errorView.clear();
                final IExplorerView previousView = currentView();
                if (previousView != null) {//closing previous view

                    if (previousView instanceof Selector) {
                        ((Selector) previousView).setMenu(null, null);
                    } else if (previousView instanceof Editor) {
                        ((Editor) previousView).setMenu(null);
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
                    } catch (RuntimeException ex) {
                        if (previousModel!=null){
                            traceExceptionOnClose(previousModel.getEnvironment(), ex, previousModel);
                        }
                    }
                    final String source = currentNode==null ? null : currentNode.getPath();
                    checkConfigStoreCurrentGroup(source,false);
                    checkIfProgressUnblocked(source, false);
                    if (currentNode!=null){
                        environment.getClipboard().removeAllChangeListeners(currentNode);
                    }
                    LeakedWidgetsDetector.getInstance().findLeakedWidgets(environment,source);
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

                        final IExplorerView view = (IExplorerView) model.createView();
                        if (view != null) {
                            if (view instanceof Selector) {
                                ((Selector) view).setMenu(selectorMenu, editorMenu);
                            } else if (view instanceof Editor) {
                                ((Editor) view).setMenu(editorMenu);
                            }
                            view.asQWidget().setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
                            innerWidget.addWidget(view.asQWidget());
                            model.getEnvironment().getClipboard().setListenersContext(node);
                            boolean viewOpened = false;                            
                            try{
                                view.open(model);
                                viewOpened = true;
                            }finally{                                
                                if (!viewOpened){                                    
                                    innerWidget.removeWidget(view.asQWidget());
                                    if (view.asQWidget().nativeId()!=0){
                                        view.asQWidget().close();
                                    }
                                    model.getEnvironment().getClipboard().removeAllChangeListeners(node);                                    
                                }
                            }
                            if (OPENING_VIEW_PROGRESS.wasCanceled()) {
                                throw new InterruptedException();
                            }
                            if (viewOpened && !closed){
                                innerWidget.setCurrentWidget(view.asQWidget());
                                setWindowTitle(node.getTitle());
                            }else{
                                return null;
                            }
                        }
                        return view;
                    }catch(InterruptedException ex){//NOPMD
                        model.clean();
                        throw ex;
                    }catch(CantOpenSelectorError ex){
                        model.clean();
                        if (ex.getCause() instanceof InterruptedException) {
                            throw (InterruptedException) ex.getCause();
                        }
                        processOpenNodeException(node, model, ex);
                        return null;
                    } catch (Exception ex) {
                        model.clean();
                        processOpenNodeException(node, model, ex);
                        return null;
                    } catch(UnsupportedDefinitionVersionError error){
                        try{
                            final String errMessage = environment.getMessageProvider().translate("ExplorerTree", "Can't open explorer item \'%s\'");
                            final String reason = 
                                environment.getMessageProvider().translate("ExplorerMessage", "Current version is no longer supported.\nIt is impossible to continue work until explorer will be restarted.");
                            errorView.setMessage(String.format(errMessage, node.getTitle())+"\n"+reason);
                            errorView.setCanReopen(false);
                        }finally{//NOPMD It is important do not to lost this error                                                        
                            throw error;
                        }
                    }catch(UserAccountLockedError error){
                        final String errMessage = environment.getMessageProvider().translate("ExplorerTree", "Can't open explorer item \'%s\'");
                        final String reason = error.getLocalizedMessage();
                        errorView.setMessage(String.format(errMessage, node.getTitle())+"\n"+reason);
                        errorView.setCanReopen(true);
                        return null;
                    }catch (PasswordExpiredError error){
                        final String errMessage = environment.getMessageProvider().translate("ExplorerTree", "Can't open explorer item \'%s\'");
                        final String exceptionMessage = error.getLocalizedMessage();
                        final String reason =  exceptionMessage.substring(0, 1).toUpperCase() + exceptionMessage.substring(1);
                        errorView.setMessage(String.format(errMessage, node.getTitle())+"\n"+reason);
                        errorView.setCanReopen(true);
                        return null;                        
                    }finally {
                        OPENING_VIEW_PROGRESS.finishProgress();
                        if (!closed){
                            if (previousView != null && previousView.asQWidget() != null) {
                                innerWidget.removeWidget(previousView.asQWidget());
                                //previousView.asQWidget().setParent(null);
                                //removing native resources to avoid widget usage in native code when java resources was already removed by GC.
                                //previousView.asQWidget().disposeLater();
                            }
                            viewIndex = innerWidget.count() - 1;
                        }
                        checkConfigStoreCurrentGroup(node.getPath(), true);
                        checkIfProgressUnblocked(node.getPath(), true);                        
                    }
                }//opening view                
            } finally {
//                unlockRedraw();
//                setUpdatesEnabled(true);
                if (!closed){
                    currentNode = node;
                    final long elapsedTime = System.currentTimeMillis()-time;
                    final String message = 
                        environment.getMessageProvider().translate("TraceMessage", "Opening view for \'%1$s\' finished. Elapsed time: %2$s ms");
                    environment.getTracer().debug(String.format(message, node.toString(), elapsedTime));
                }
            }
        }
    }
    
    private void processOpenNodeException(final ExplorerTreeNode node,final Model model, final Exception ex){
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
                Application.messageInformation(title, objectNotFound.getMessageToShow());

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
        innerWidget.setCurrentIndex(0);
        setWindowTitle(node.getTitle());        
    }

    @SuppressWarnings("unused")
    private void reopen() {
        if (currentNode != null) {
            if (currentNode.isValid()) {
                try {
                    open(currentNode);
                } catch (InterruptedException ex) {
                }
            } else {
                Application.getInstance().getActions().checkForUpdates.trigger();
            }
        }
    }

    private void closeModalDialogs() {
        int tryCount = 0;
        QWidget activeWidget = QApplication.activeModalWidget();
        while (activeWidget != null && activeWidget != window() && tryCount<100) {
            if (activeWidget instanceof ExplorerDialog) {
                ((ExplorerDialog) activeWidget).forceClose();
            } else {
                if (!activeWidget.close()) {
                    break;
                }
            }            
            activeWidget = QApplication.activeModalWidget();
            tryCount++;
        }
    }

    private boolean isModalDialogsOpened() {
        QWidget activeWidget = QApplication.activeModalWidget();
        if (activeWidget instanceof WaitDialog) {
            final ExplorerProgressHandleManager manager =
                (ExplorerProgressHandleManager)environment.getProgressHandleManager();            
            manager.setProgressViewHidden(true);
            try {
                activeWidget = QApplication.activeModalWidget();
            } finally {
                manager.setProgressViewHidden(false);
            }
        }
        return activeWidget != null && activeWidget != window();
    }

    public boolean canSafetyClose() {
        if (isModalDialogsOpened()) {
            return false;
        }        
        final IExplorerView view = currentView();
        return view == null || view.getModel() == null || view.getModel().canSafelyClean(CleanModelController.DEFAULT_INSTANCE);
    }

    public void forceClose() {
        final IExplorerView view = currentView();
        if (view != null) {
            final Model closingModel = view.getModel();
            try {
                closeModalDialogs();
                if (closingModel == null || closingModel instanceof EntityModel) {
                    restorePresentationChangedHandler((EntityModel)closingModel);
                    view.close(true);
                } else {
                    closingModel.clean();
                }
            } catch (Throwable exception) {//NOPMD
                traceExceptionOnClose(environment, exception, closingModel);
            }
            innerWidget.removeWidget(view.asQWidget());
            viewIndex--;
            final String source = currentNode==null ? null : currentNode.getPath();
            checkConfigStoreCurrentGroup(source,false);
            checkIfProgressUnblocked(source, false);
            if (currentNode!=null){
                environment.getClipboard().removeAllChangeListeners(currentNode);
            }            
            LeakedWidgetsDetector.getInstance().findLeakedWidgets(environment,source);            
            close();
        }
    }

    private IExplorerView currentView() {
        while (viewIndex > 0) {
            if (innerWidget.widget(viewIndex) != null) {
                return (IExplorerView) innerWidget.widget(viewIndex);
            }
            viewIndex--;
        }
        return null;
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (!canSafetyClose()) {
            event.ignore();
        } else {
            final IExplorerView view = currentView();
            if (view != null) {
                final Model closingModel = view.getModel();
                try {
                    if (closingModel == null || closingModel instanceof EntityModel) {
                        restorePresentationChangedHandler((EntityModel)closingModel);
                        view.close(true);                        
                    } else {
                        closingModel.clean();
                    }
                } catch (RuntimeException exception) {
                    traceExceptionOnClose(environment, exception, closingModel);
                }
            }
            errorView.close();
            QWidget widget;
            for (int i = innerWidget.count() - 1; i >= 0; i--) {
                widget = innerWidget.widget(i);
                innerWidget.removeWidget(widget);
                widget.close();
            }
            if (selectorMenu != null) {
                selectorMenu.removeAllActions();
                selectorMenu.clear();
                selectorMenu.setDisabled(true);
                selectorMenu = null;
            }
            if (editorMenu != null) {
                editorMenu.removeAllActions();
                editorMenu.clear();
                editorMenu.setDisabled(true);
                editorMenu = null;
            }
            closed = true;
            wasClosed.emit();
            currentNode = null;
            super.closeEvent(event);
        }
    }
    
    private void checkConfigStoreCurrentGroup(final String treeNode, final boolean afterOpenView){
        final String configProfile = environment.getUserName() + "_" + environment.getStationName();
        final String currentGroup = environment.getConfigStore().group();
        if (!configProfile.equals(currentGroup)){            
            environment.getConfigStore().setConfigProfile(configProfile);
            final String groupToDisplay;
            if (currentGroup.startsWith(configProfile)){
                groupToDisplay = currentGroup.substring(configProfile.length()+1);
            }else{
                groupToDisplay = currentGroup;
            }
            final String traceMessage;
            if (treeNode==null || treeNode.isEmpty()){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed. Forcedly closing.");
                traceMessage = String.format(messageTemplate, groupToDisplay);
            }else{
                if (afterOpenView){
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed when opening \'%2$s\'. Forcedly closing.");
                    traceMessage = String.format(messageTemplate, groupToDisplay, treeNode);                    
                }else{
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("TraceMessage","Config store state is invalid. Settings group \'%1$s\' was not closed in \'%2$s\'. Forcedly closing.");
                    traceMessage = String.format(messageTemplate, groupToDisplay, treeNode);
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
            ((ExplorerProgressHandleManager)environment.getProgressHandleManager()).assertProgressUnblocked(traceMessage);            
    }        
    
    private static void restorePresentationChangedHandler(final EntityModel entityModel){
        if (entityModel!=null && entityModel.getEntityContext().getPresentationChangedHandler() instanceof PresentationChangedHandler){
            final PresentationChangedHandler handler = 
                    (PresentationChangedHandler)entityModel.getEntityContext().getPresentationChangedHandler();
            entityModel.getEntityContext().setPresentationChangedHandler(handler.getOldPCH());
        }
    }             
}