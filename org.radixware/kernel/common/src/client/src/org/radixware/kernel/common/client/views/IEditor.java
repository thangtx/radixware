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
package org.radixware.kernel.common.client.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.EntityRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ExceptionEnum;

public interface IEditor extends IView {

    public interface EditorListener {

        public void entityUpdated();

        public void entityRemoved();
    }

    public abstract class Actions implements Iterable<Action> {

        private final ActionController controller;
        private final List<Action> allActions = new LinkedList<>();
        private Action rereadAction;
        private Action deleteAction;
        private Action updateAction;
        private Action cancelChangesAction;
        private Action copyAction;
        private Action viewAuditLogAction;
        private Action copyEditorPresIdAction;
        private final Action.ActionListener actionListener = new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                if (action == rereadAction) { 
                    controller.rereadAction();
                } else if (action == deleteAction) {
                    controller.deleteAction();
                } else if (action == updateAction) {
                    controller.updateAction();
                } else if (action == cancelChangesAction) {
                    controller.cancelChangesAction();                            
                } else if (action == copyAction) {
                    controller.editor.getController().copy();
                } else if (action == viewAuditLogAction) {
                    controller.viewAuditLogAction();
                } else if (action == copyEditorPresIdAction) {
                    controller.copyEditorPresId();
                }
            }
        };

        public Actions(ActionController controller) {
            this.controller = controller;
            initializeActions();
        }

        @Override
        public Iterator<Action> iterator() {
            return allActions.iterator();
        }

        private void initializeActions() {
            final IClientEnvironment environment = controller.environment;
            rereadAction = createActionImpl(ClientIcon.Editor.REREAD, environment.getMessageProvider().translate("Editor", "Refresh"));
            rereadAction.setToolTip(environment.getMessageProvider().translate("Editor", "Refresh"));
            rereadAction.addActionListener(actionListener);
            deleteAction = createActionImpl(ClientIcon.Editor.DELETE, environment.getMessageProvider().translate("Editor", "Delete"));
            deleteAction.setToolTip(environment.getMessageProvider().translate("Editor", "Delete Object"));
            deleteAction.addActionListener(actionListener);

            updateAction = createActionImpl(ClientIcon.Editor.SAVE,
                    environment.getMessageProvider().translate("Editor", "Apply"));
            updateAction.setEnabled(false);
            updateAction.setToolTip(environment.getMessageProvider().translate("Editor", "Apply Changes"));
            updateAction.addActionListener(actionListener);

            cancelChangesAction = createActionImpl(ClientIcon.Editor.CANCEL, environment.getMessageProvider().translate("Editor", "Cancel Changes"));
            cancelChangesAction.setEnabled(false);
            cancelChangesAction.setToolTip(environment.getMessageProvider().translate("Editor", "Cancel Changes"));
            cancelChangesAction.addActionListener(actionListener);

            copyAction = createActionImpl(ClientIcon.Editor.COPY, environment.getMessageProvider().translate("Editor", "Copy"));
            copyAction.setToolTip(environment.getMessageProvider().translate("Editor", "Copy"));
            copyAction.addActionListener(actionListener);

            viewAuditLogAction = createActionImpl(ClientIcon.Editor.AUDIT, environment.getMessageProvider().translate("Editor", "Audit Log"));
            viewAuditLogAction.setToolTip(environment.getMessageProvider().translate("Editor", "Audit Log"));
            viewAuditLogAction.addActionListener(actionListener);

            copyEditorPresIdAction = createActionImpl(ClientIcon.Definitions.EDITOR_ID,
                    environment.getMessageProvider().translate("Editor", "Copy Editor Presentation Id"));
            copyEditorPresIdAction.setToolTip(environment.getMessageProvider().translate("Editor", "Copy Editor Presentation Identifier to Clipboard"));
            copyEditorPresIdAction.addActionListener(actionListener);
        }

        protected abstract Action createAction(ClientIcon icon, String title);

        private Action createActionImpl(final ClientIcon icon, final String title) {
            final Action action = createAction(icon, title);
            allActions.add(action);
            return action;
        }

        public void refresh() {
            final EntityModel entity = controller.editor.getEntityModel();
            final EntityRestrictions restrictions = entity.getRestrictions();

            getDeleteAction().setEnabled(!restrictions.getIsDeleteRestricted());

            if (!restrictions.getIsCopyRestricted()
                    && entity.isExists()) {
                getCopyAction().setEnabled(true);
            } else {
                getCopyAction().setEnabled(false);
            }

            controller.refreshModifiedState();
            getViewAuditLogAction().setVisible(entity.isAuditEnabled() && entity.isExists());
        }

        public ActionController getController() {
            return controller;
        }

        public Action getUpdateAction() {
            return updateAction;
        }

        public Action getRereadAction() {
            return rereadAction;
        }

        public Action getDeleteAction() {
            return deleteAction;
        }

        public Action getCopyAction() {
            return copyAction;
        }

        public Action getCancelChangesAction() {
            return cancelChangesAction;
        }

        public Action getViewAuditLogAction() {
            return viewAuditLogAction;
        }

        public Action getCopyEditorPresIdAction() {
            return copyEditorPresIdAction;
        }

        public void close() {
            for (Action action : allActions) {
                action.close();
            }
            allActions.clear();
        }
    }

    public Actions getActions();

    public EntityModel getEntityModel();

    public void refresh();

    public boolean delete() throws ServiceClientException;

    public boolean applyChanges() throws ServiceClientException, ModelException;

    public void cancelChanges();

    public interface CloseHandler {

        public void onClose();
    }

    public interface OpenHandler {

        public void afterOpen();
    }

    public void addEditorListener(EditorListener l);

    public void removeEditorListener(EditorListener l);

    public abstract class EditorController extends AbstractViewController implements IModificationListener {

        private AbstractComponentModificationRegistrator modificationRegistrator;
        private final IEditor editor;
        private final ActionController actionController;
        private EntityModel entity;
        private final EditorUIController uiController;
        private boolean wasClosed;
        private final List<EditorListener> listeners = new LinkedList<>();

        public EditorController(final IEditor editor, final EditorUIController uiController) {
            super(editor.getEnvironment(), editor);
            this.editor = editor;
            this.uiController = uiController;
            this.actionController = editor.getActions().controller;
            uiController.createToolBars();
        }

        public void open(Model model_) {
            final EntityModel m = ((EntityModel) model_);
            entity = m;
            entity.setView(editor);
            uiController.initToolBars();
            editor.getActions().refresh();
        }

        public void setModificationRegistractor(AbstractComponentModificationRegistrator modificationRegistrator) {
            this.modificationRegistrator = modificationRegistrator;
        }

        public EntityModel getEntity() {
            return entity;
        }

        public boolean isEdited() {
            if (getEntity().isEdited()) {
                return true;
            }
            return isSynchronizedEmbeddedViewsInModifiedState();
        }

        @Override
        public void notifyComponentModificationStateChanged(final IModifableComponent childComponent, final boolean modified) {
            if (getEntity() == childComponent.getModel()
                    || (childComponent instanceof IEmbeddedView && ((IEmbeddedView) childComponent).isSynchronizedWithParentView())) {
                actionController.refreshModifiedState();
            }
            if (modificationRegistrator != null) {
                modificationRegistrator.notifyModificationStateChanged(childComponent, modified);
            }
        }

        @Override
        public void notifyPropertyModificationStateChanged(final Property property, final boolean modified) {
            if (modified) {
                actionController.setModifiedState(true);
            } else {
                final List<Property> editedProperties = getEntity().getEditedProperties();
                if (editedProperties.size() == 1 && editedProperties.get(0).getId().equals(property.getId())) {
                    actionController.setModifiedState(false);
                } else {
                    actionController.refreshModifiedState();
                }
            }
        }

        public void reread() throws ServiceClientException {            
            try {
                getEntity().read();
                rereadSynchronizedEmbeddedViews();                
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                actionController.refreshModifiedState();
            }
        }

        public void copy() {
            getEntity().finishEdit();
            getEntity().getEnvironment().getClipboard().push(getEntity());
        }

        public void cancelChanges() {
            try {
                /*Модель вложенного представления может изменять модель внешнего представления 
                 * (например, в перекрытом методе finishEdit). Поэтому сначала отменяются изменения во
                 * вложенных представлениях.
                 */
                cancelChangesInSynchronizedEmbeddedViews();
                getEntity().finishEdit();
                getEntity().cancelChanges();
            } finally {
                actionController.refreshModifiedState();
            }
        }

        public boolean delete() throws ServiceClientException {
            try {
                if (getEntity().delete(false)) {
                    entityRemovedImpl();
                    return true;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return false;
        }

        public boolean applyChanges() throws ServiceClientException, ModelException {
            try {
                if (getEntity().isNew()) {
                    if (getEntity().create() != EEntityCreationResult.CANCELED_BY_CLIENT) {
                        updateSynchronizedEmbeddedViews();
                        entityUpdatedImpl();
                        return true;
                    }
                } else {
                    if (getEntity().update()) {
                        updateSynchronizedEmbeddedViews();
                        entityUpdatedImpl();
                        return true;
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                actionController.refreshModifiedState();
            }
            return false;
        }

        public void addEditorListener(EditorListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeEditorListener(EditorListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        private void entityUpdatedImpl() {
            synchronized (listeners) {
                for (EditorListener l : listeners) {
                    l.entityUpdated();
                }
            }
            entityUpdated();
        }

        private void entityRemovedImpl() {
            synchronized (listeners) {
                for (EditorListener l : listeners) {
                    l.entityRemoved();
                }
            }
            entityRemoved();
        }

        protected abstract void entityUpdated();

        protected abstract void entityRemoved();

        @Override
        public void childComponentWasClosed(IModifableComponent childComponent) {
            if (modificationRegistrator != null) {
                modificationRegistrator.unregisterChildComponent(childComponent);
            }
        }

        public void finishEdit() {
            final Collection<Property> properties = getEntity().getActiveProperties();
            for (Property property : properties) {
                property.finishEdit();
            }
            finishEditInSynchronizedEmbeddedViews();
        }

        public boolean close(boolean forced) {
            if (wasClosed) {
                return true;
            }

            if (entity != null) {
                if (!forced && !entity.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
                    return false;
                }
            }

            closeEmbeddedViews();
            notifyClosed();

            if (entity != null) {
                entity.setView(null);
                cleanUpEditorAfterClose(entity);
                if (uiController.commandBar != null) {
                    uiController.commandBar.removeAllActions();
                    uiController.commandBar.close();
                }
                afterCloseView();
                entity = null;
            }

            synchronized (listeners) {
                listeners.clear();
            }

            return true;
        }

        public void setClosed() {
            wasClosed = true;
            editor.getActions().close();
        }

        protected abstract void notifyClosed();

        protected abstract void cleanUpEditorAfterClose(EntityModel entity);
    }

    public abstract class ActionController {

        private final IEditor editor;
        private final IClientEnvironment environment;
        private final String deleteActionError;
        private final String updateActionError;
        private final String rereadActionError;

        public ActionController(IEditor editor) {
            this.editor = editor;
            environment = editor.getEnvironment();
            deleteActionError = environment.getMessageProvider().translate("Editor", "Failed to delete object");
            updateActionError = environment.getMessageProvider().translate("Editor", "Failed to apply changes");
            rereadActionError = environment.getMessageProvider().translate("Editor", "Error on receiving data");
        }

        private Actions getActions() {
            return editor.getActions();
        }

        private EntityModel getEntity() {
            return editor.getController().getEntity();
        }

        protected abstract void modifiedStateChanged(boolean modified);

        protected abstract void execAuditLogDialog(IClientEnvironment env, IEditor editor, Id tableId, Pid pid, String title);

        public void refreshModifiedState() {
            if (getEntity() != null) {
                if (getEntity().getRestrictions().getIsUpdateRestricted()) {
                    getActions().getUpdateAction().setEnabled(false);
                    getActions().getCancelChangesAction().setEnabled(false);
                } else {
                    setModifiedState(editor.getController().isEdited());
                }
            }
        }

        public void setModifiedState(final boolean modified) {
            if (getEntity() != null && !getEntity().getRestrictions().getIsUpdateRestricted() && getActions().getUpdateAction().isEnabled() != modified) {
                getActions().getUpdateAction().setIcon(environment.getApplication().getImageManager().getIcon(modified ? ClientIcon.Editor.NEED_FOR_SAVE : ClientIcon.Editor.SAVE));
                getActions().getCancelChangesAction().setEnabled(modified);
                getActions().getUpdateAction().setEnabled(modified);
                modifiedStateChanged(modified);
            }
        }

        private void updateAction() {
            try {
                getEntity().finishEdit();
                editor.applyChanges();
            } catch (Exception ex) {
                if (editor != null && editor.getModel() != null) {
                    editor.getModel().showException(updateActionError, ex);
                }
            }
        }

        private void deleteAction() {
            try {
                getEntity().finishEdit();
                editor.delete();
            } catch (Exception ex) {
                editor.getModel().showException(deleteActionError, ex);
            }
        }

        private void rereadAction() {
            try {
                getEntity().finishEdit();
                if (!editor.getController().isEdited() || confirmToCancelChanges()) {
                    editor.reread();
                }                
            } catch (Exception ex) {
                editor.getModel().showException(rereadActionError, ex);
            }
        }
        
        private void cancelChangesAction(){
            getEntity().finishEdit();
            if (!editor.getController().isEdited() || confirmToCancelChanges()) {
                editor.getController().cancelChanges();
            }
        }
        
        private boolean confirmToCancelChanges(){
            final MessageProvider messageProvider = getEntity().getEnvironment().getMessageProvider();
            final String title = messageProvider.translate("Editor", "Confirm to Cancel Changes");
            final String message = messageProvider.translate("Editor", "Your changes will be lost. Do you want to continue?");
            return getEntity().getEnvironment().messageConfirmation(title, message);
        }

        private void viewAuditLogAction() {
            getEntity().finishEdit();
            final Id tableId = editor.getEntityModel().getClassPresentationDef().getTableId();
            execAuditLogDialog(environment, editor, tableId, editor.getEntityModel().getPid(), editor.getEntityModel().getTitle());
        }

        private void copyEditorPresId() {
            final Id presId = getEntity().getEditorPresentationDef().getId();
            editor.getController().uiController.putTextToSystemClipboard(presId.toString());
        }
    }

    public abstract class EditorUIController {

        private final static String EDITOR_GROUP_NAME_KEY = SettingNames.SYSTEM + "/MainWindow";
        private final IEditor editor;
        private IToolBar toolBar;
        private ICommandToolBar commandBar;

        public EditorUIController(IEditor editor) {
            this.editor = editor;
        }

        protected abstract IToolBar createToolBar();

        protected abstract ICommandToolBar createCommandToolBar(IClientEnvironment env, IEditor editor);

        private void createToolBars() {
            toolBar = createToolBar();
            toolBar.setObjectName("toolBar");
            Actions actions = editor.getActions();
            toolBar.addAction(actions.getDeleteAction());
            toolBar.addAction(actions.getCopyAction());
            toolBar.addAction(actions.getUpdateAction());
            toolBar.addAction(actions.getCancelChangesAction());
            toolBar.addAction(actions.getRereadAction());
            toolBar.addAction(actions.getViewAuditLogAction());
            commandBar = createCommandToolBar(editor.getEnvironment(), editor);
            commandBar.setObjectName("commandBar");
            //настройка размеров значков у toolBar
            applySettings(editor.getEnvironment(), toolBar, commandBar);
        }

        private void initToolBars() {
            //апдейт положения туллбар панелей
            final String configStoreGroupName = editor.getModel().getConfigStoreGroupName();
            final ClientSettings settings = editor.getEnvironment().getConfigStore();
            final String key = configStoreGroupName + "/" + EDITOR_GROUP_NAME_KEY;
            final boolean stateWasStored = settings.contains(key);
            if (stateWasStored) {
                restoreState(settings, key);
                toolBar.setVisible(true);
            }

            //затем связываем панель команд с моделью и она становится
            //видимой/невидимой в зависимости от наличия доступных команд
            //поверх сохраненных настроек
            commandBar.setModel(editor.getController().getEntity());
        }

        protected abstract void applySettings(IClientEnvironment env, IToolBar toolBar, ICommandToolBar commandToolBar);

        protected abstract void restoreState(ClientSettings settings, String key);

        protected abstract void putTextToSystemClipboard(final String text);

        public void setToolBarHidden(final boolean hidden) {
            toolBar.setHidden(hidden);
        }

        public void setCommandBarHidden(final boolean hidden) {
            if (commandBar != null) {
                commandBar.setPersistentHidden(hidden);
            }
        }

        public void setToolButtonsSize(int size) {
            toolBar.setIconSize(size, size);
            if (commandBar != null) {
                commandBar.setButtonSize(size, size);
            }
        }

        public int getToolButtonsSize() {
            return toolBar.getIconHeight();
        }

        public ICommandToolBar getCommandBar() {
            return commandBar;
        }

        public IToolBar getToolBar() {
            return toolBar;
        }
    }

    public EditorController getController();

    public void addCloseHandler(CloseHandler handler);

    public void removeCloseHandler(CloseHandler handler);

    public void addOpenHandler(OpenHandler handler);

    public void removeOpenHandler(OpenHandler handler);

    public void setToolBarHidden(boolean hidden);

    public void setMenuHidden(boolean hidden);

    public void setCommandBarHidden(final boolean hidden);
}
