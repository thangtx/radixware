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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ActivatingPropertyError;
import org.radixware.kernel.common.client.errors.CantOpenEditorError;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.*;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler;
import org.radixware.kernel.common.client.views.ISelector.ISelectorMainWindow;
import org.radixware.kernel.common.client.views.ISelector.IToolBarsManager;
import org.radixware.kernel.common.client.views.ISelector.SelectorListener;
import org.radixware.kernel.common.client.widgets.IBlocakbleWidget;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.ISplitter;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ExceptionEnum;

public abstract class SelectorController extends AbstractViewController {

    public static abstract class UIController {

        private boolean splitterInited = false;

        protected abstract ISplitter getSplitter();

        protected abstract void loadSplitterSettings();

        protected abstract void disconnect();

        protected abstract void storeSettings();

        protected abstract void restoreSettings(GroupModel group);

        protected abstract void forceUnblockRedraw();

        protected abstract void closeSelectorWidget(ISelectorWidget widget);

        protected abstract void closeChildWidgets(GroupModel group);

        protected abstract IEmbeddedEditor createEntityEditor();

        protected abstract ISelector.IEditorSpace getEditorSpace();

        protected abstract boolean isUIUpdatesEnabled();

        protected abstract void putTextToSystemClipboard(final String text);
        
        protected abstract void showBatchOperationResult(final AbstractBatchOperationResult result, final String message);
    }
            
    private final ISelector selector;
    GroupModel group;
    ParentModelsCollector parentModels;
    private SelectorListener listener;
    private final EmbeddedEditorListener editorListener;
    private final IProgressHandle progressHandle;    
    private final UIController uiController;
    private IEmbeddedEditor cee_;
    private IEntityEditorDialog currentEntityDialog;
    private EntityModel currentEntity;
    private ISelectorWidget selectorWidget;
    private boolean autoInsertEnabled = false;    
    private IMenu selectorMenu;
    private boolean cantOpenEditor = false;
    private IToolBar selectorToolBar, editorToolBar;
    private boolean isEditorToolbarHidden;
    private ICommandToolBar selectorCommands, editorCommands;
    private final static String SELECTOR_WINDOW_KEY_NAME = "MainWindow";
    private IToolBarsManager toolBarsManager;    
    private boolean openingEditor = false;
    private boolean wasShown = false;
    private final Clipboard.ChangeListener clipboardListener = new Clipboard.ChangeListener() {
        @Override
        public void stateChanged() {
            selector.getActions().refresh();
            if (getSelectorWidget() != null) {
                getSelectorWidget().refresh(null);
            }
        }
    };

    public SelectorController(ISelector selector, UIController uiController) {
        super(selector.getEnvironment(), selector);
        this.selector = selector;
        this.uiController = uiController;
        progressHandle = selector.getEnvironment().getProgressHandleManager().newStandardProgressHandle();
        editorListener = new EmbeddedEditorListener(this);
    }

    private ISelector.IEditorSpace getEditorSpace() {
        return uiController.getEditorSpace();
    }

    protected SelectorListener getSelectorListener() {
        synchronized (this) {
            if (listener == null) {
                listener = createDefaultListener();
            }
            return listener;
        }
    }

    public IClientEnvironment getEnvironment() {
        return selector.getEnvironment();
    }

    public EntityModel getCurrentEntity() {
        return currentEntity;
    }

    void blockRedraw() {
        selector.blockRedraw();
    }

    void unblockRedraw() {
        selector.unblockRedraw();
    }

    protected abstract SelectorListener createDefaultListener();
    /*
     * Просматривает все родительские модели вплоть до rootModel и собирает
     * инстанции EntityModel и GroupModel.
     */

    static class ParentModelsCollector implements IModelFinder {

        final private Model rootModel;
        final private List<EntityModel> parentEntities = new java.util.LinkedList<>();
        final private List<GroupModel> parentGroups = new java.util.LinkedList<>();

        public ParentModelsCollector(final Model rootModel) {
            this.rootModel = rootModel;
        }

        @Override
        public boolean isTarget(Model model) {
            if (model instanceof GroupModel) {
                parentGroups.add((GroupModel) model);
            }
            if (model == rootModel) {//NOPMD
                return true;
            }
            if (model instanceof EntityModel) {
                if (model.getContext() instanceof IContext.SelectorRow) {
                    parentEntities.add(0, (EntityModel) model);
                } else if (model.getContext() instanceof IContext.InSelectorEditing) {
                    final IContext.InSelectorEditing context = (IContext.InSelectorEditing) model.getContext();
                    parentEntities.add(0, context.rowModel);
                }
            }
            return false;
        }

        public List<EntityModel> getParentEntities() {
            return Collections.unmodifiableList(parentEntities);
        }

        public List<GroupModel> getParentGroups() {
            return Collections.unmodifiableList(parentGroups);
        }
    }

    private static class EmbeddedEditorListener implements IEditor.EditorListener {

        private final SelectorListener selectorListener;

        public EmbeddedEditorListener(SelectorController controller) {
            this.selectorListener = controller.getSelectorListener();
        }

        @Override
        public void entityUpdated() {
            selectorListener.entityUpdated();
        }

        @Override
        public void entityRemoved() {//call of selectorListener.entityRemoved() in entityRemoved(Pid)
        }
    }

    private class CurrentEntityPresentationChangedHandler implements IPresentationChangedHandler {

        private final IPresentationChangedHandler oldPCH;

        public CurrentEntityPresentationChangedHandler(final EntityModel currentEntity) {
            oldPCH = currentEntity.getEntityContext().getPresentationChangedHandler();
        }

        @Override
        public EntityModel onChangePresentation(final RawEntityModelData rawData,
                final Id newPresentationClassId,
                final Id newPresentationId) {
            finishEdit();
            final EntityModel newEntityModel
                    = oldPCH.onChangePresentation(rawData, newPresentationClassId, newPresentationId);
            if (openingEditor) {
                newEntityModel.getEntityContext().setPresentationChangedHandler(new CurrentEntityPresentationChangedHandler(newEntityModel));
                currentEntity = newEntityModel;
            } else if (currentEntityDialog == null) {
                final IBlocakbleWidget blockableWidget
                        = selector instanceof IBlocakbleWidget ? (IBlocakbleWidget) selector : null;
                final boolean needUnblockRedraw;
                if (blockableWidget != null && !blockableWidget.isInternalPaintingActive()) {
                    blockableWidget.blockRedraw();
                    needUnblockRedraw = true;
                } else {
                    needUnblockRedraw = false;
                }
                try {
                    selector.leaveCurrentEntity(true);
                    selector.setCurrentEntity(newEntityModel);
                } finally {
                    if (needUnblockRedraw) {
                        selector.unblockRedraw();
                    }
                }
            } else {
                restorePresentationChangedHandler(currentEntity);
                currentEntity = null;
                getDefaCurrentEntityHandler().onLeaveCurrentEntity();
                setCurrentEntity(newEntityModel);
            }
            return newEntityModel;
        }

        public IPresentationChangedHandler getOldPCH() {
            return oldPCH;
        }
    }

    static enum SelectorState {

        DISABLED,
        OPENING_INTERRUPTED,
        NORMAL
    }
    SelectorState state = SelectorState.NORMAL;

    public abstract ISelectorMainWindow getSelectorMainWindow();

    IExplorerItemView insertEntityImpl(final boolean autoInsert, final boolean replace, final boolean askToSetCurrentIfExists) {
        if (getCurrentEntity() != null) {
            IExplorerItemView explorerItemView = group.findNearestExplorerItemView();
            if (explorerItemView != null) {
                if (!autoInsert) {
                    //RADIX-2348, RADIX-7253 Проверка был ли уже вставлен такой же объект выше по дереву.
                    final List<EntityModel> parentEntityModels = explorerItemView.getParentEntityModels();
                    for (EntityModel parentEntityModel : parentEntityModels) {
                        if (getCurrentEntity().getPid().equals(parentEntityModel.getPid())) {
                            explorerItemView = parentEntityModel.findNearestExplorerItemView();
                            if (askToSetCurrentIfExists) {
                                final String title
                                        = getEnvironment().getMessageProvider().translate("Selector", "Confirm to Change Current Explorer Item");
                                final String messageTemplate
                                        = getEnvironment().getMessageProvider().translate("Selector", "Object '%s' was already inserted.\nDo you want to make current corresponding explorer item?");
                                if (getEnvironment().messageConfirmation(title, String.format(messageTemplate, getCurrentEntity().getTitle()))) {
                                    explorerItemView.setCurrent();
                                }
                            }
                            return explorerItemView;
                        }
                    }
                }
                //Вставка родительских сущностей
                final List<EntityModel> parentEntities = parentModels.getParentEntities();
                if (!parentEntities.isEmpty()) {
                    EntityModel choosenParentEntity;
                    for (EntityModel parentEntity : parentEntities) {
                        //Новый объект для вставки
                        choosenParentEntity = parentEntity.openChoosenEditModel();
                        //Ищем элемент проводника соответствующей группы
                        final GroupModel parentGroup = ((IContext.SelectorRow) parentEntity.getContext()).parentGroupModel;
                        final IExplorerItemView tableExplorerItem = findChildExplorerItem(explorerItemView, parentGroup);
                        if (tableExplorerItem != null) {
                            explorerItemView = tableExplorerItem.insertEntity(0, choosenParentEntity, true);
                        } else {
                            explorerItemView = explorerItemView.insertEntity(0, choosenParentEntity, true);
                        }
                    }
                    final IExplorerItemView tableExplorerItem = findChildExplorerItem(explorerItemView, group);
                    if (tableExplorerItem != null) {
                        explorerItemView = tableExplorerItem;
                    }
                }

                final EntityModel choosen = getCurrentEntity().openChoosenEditModel();
                final IExplorerItemView result;
                if (autoInsert) {
                    result = explorerItemView.autoInsertEntity(choosen);
                } else {
                    if (replace) {
                        List<IExplorerItemView> inserted = explorerItemView.getChoosenEntities(choosen.getEditorPresentationDef().getTableId());
                        if (!inserted.isEmpty()) {
                            inserted.get(0).remove();
                        }
                    }
                    result = explorerItemView.insertEntity(0, choosen, true);
                }
                if (result != null) {
                    getEnvironment().getConfigStore().beginGroup(SettingNames.SYSTEM);
                    getEnvironment().getConfigStore().beginGroup(SettingNames.EXPLORER_TREE_GROUP);
                    getEnvironment().getConfigStore().beginGroup(SettingNames.ExplorerTree.EDITOR_GROUP);
                    final boolean editAfterInsert = getEnvironment().getConfigStore().readBoolean(SettingNames.ExplorerTree.Editor.EDIT_AFTER_INSERT, false);
                    getEnvironment().getConfigStore().endGroup();
                    getEnvironment().getConfigStore().endGroup();
                    getEnvironment().getConfigStore().endGroup();
                    if (editAfterInsert && !autoInsert) {
                        result.setCurrent();
                    } else {
                        selector.getActions().refresh();
                    }
                    getSelectorListener().insertedIntoTree(result);

                }
                return result;
            }
        }
        return null;
    }

    private static IExplorerItemView findChildExplorerItem(final IExplorerItemView explorerItemView, final GroupModel childGroup) {
        if (childGroup.getContext() instanceof IContext.TableSelect) {
            final Id explorerItemId = ((IContext.TableSelect) childGroup.getContext()).explorerItemDef.getId();
            final java.util.Stack<IExplorerItemView> stack = new java.util.Stack<>();
            final List<Id> paragraphIds = new java.util.LinkedList<>();

            stack.add(explorerItemView);
            IExplorerItemView currentExplorerItem, childExplorerItem;
            Id childExplorerItemId;
            while (!stack.isEmpty()) {
                currentExplorerItem = stack.pop();
                for (int i = currentExplorerItem.getChildsCount() - 1; i >= 0; i--) {
                    childExplorerItem = currentExplorerItem.getChild(i);
                    childExplorerItemId = childExplorerItem.getExplorerItemId();
                    if (childExplorerItem.isGroupView() && explorerItemId.equals(childExplorerItemId)) {
                        return childExplorerItem;
                    } else if (childExplorerItem.isParagraphView() && !paragraphIds.contains(childExplorerItemId)) {
                        paragraphIds.add(childExplorerItemId);
                        stack.push(childExplorerItem);
                    }
                }
            }
        }
        return null;
    }

    private void rereadImpl(final Pid pid, final boolean setCurrent, final boolean forced) throws ServiceClientException {
        final Pid dialogEntityPid;
        if (currentEntityDialog != null && !currentEntityDialog.dialogClosing()) {
            dialogEntityPid = currentEntityDialog.getEntityModel().getPid();
        } else {
            dialogEntityPid = null;
        }
        final GroupRestrictions restrictions = getGroupModel().getRestrictions();
        final boolean canChangePosition = restrictions.getIsChangePositionRestricted();
        selector.blockRedraw();
        restrictions.setChangePositionRestricted(false);
        final Pid currentEntityPid = getCurrentEntity() == null ? null : getCurrentEntity().getPid();//NOPMD
        if (!leaveCurrentEntityImpl(forced, false)) {//do not schedule refresh in leaveCurrentEntity - it will be performed later.
            selector.unblockRedraw();
            restrictions.setChangePositionRestricted(canChangePosition);
            return;
        }
        restrictions.setChangePositionRestricted(canChangePosition);

        currentEntity = null;
        final IProgressHandle progress;
        final boolean needForFinishProgress;
        if (getEnvironment().getProgressHandleManager().getActive() == null) {
            progress = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
            progress.startProgress(null, true);
            needForFinishProgress = true;
        } else {
            progress = getEnvironment().getProgressHandleManager().getActive();
            if (progress.wasCanceled()) {
                state = SelectorState.OPENING_INTERRUPTED;
                refresh();
                if (dialogEntityPid != null && getCurrentEntity() != null && dialogEntityPid.equals(getCurrentEntity().getPid())) {
                    runEditorDialogImpl(progress);
                }
                return;
            }
            needForFinishProgress = false;
        }
        final Pid pidWhenReadEntireObject;
        final boolean readEntireObjectAtFirstRow;
        if (!getGroupModel().getRestrictions().getIsEditorRestricted()) {
            if (setCurrent && (currentEntityPid != null || pid != null)) {
                readEntireObjectAtFirstRow = false;
                if (setupGroupModelForReadEntireObjectWithPid(pid == null ? currentEntityPid : pid, true)) {
                    pidWhenReadEntireObject = pid == null ? currentEntityPid : pid;
                } else {
                    pidWhenReadEntireObject = null;
                }
            } else {
                pidWhenReadEntireObject = null;
                readEntireObjectAtFirstRow = setupGroupModelForEntireObjectAtFirstRow(true);
            }
        } else {
            pidWhenReadEntireObject = null;
            readEntireObjectAtFirstRow = false;
        }
        try {
            try {
                if (getSelectorWidget() != null) {
                    if (setCurrent) {
                        selector.getSelectorWidget().rereadAndSetCurrent(pid);
                    } else {
                        selector.getSelectorWidget().reread();
                    }
                } else {
                    getGroupModel().reset();
                }
                if (progress.wasCanceled() && group.getEntitiesCount() == 0) {
                    state = SelectorState.OPENING_INTERRUPTED;
                    refresh();
                    if (selector.getSelectorWidget() != null) {
                        selector.getSelectorWidget().clear();
                    }
                } else {
                    state = SelectorState.NORMAL;
                }
                if (dialogEntityPid != null && getCurrentEntity() != null && dialogEntityPid.equals(getCurrentEntity().getPid())) {
                    refresh();
                    runEditorDialogImpl(progress);
                }
            } catch (InterruptedException ex) {
                if (dialogEntityPid != null && getCurrentEntity() != null && dialogEntityPid.equals(getCurrentEntity().getPid())) {
                    refresh();
                    runEditorDialogImpl(progress);
                }
            } catch (ServiceClientException ex) {
                if (dialogEntityPid != null && getCurrentEntity() != null && dialogEntityPid.equals(getCurrentEntity().getPid())) {
                    refresh();
                    runEditorDialogImpl(progress);
                }
                throw ex;
            } finally {
                try {
                    selector.unblockRedraw();
                    refresh();
                } finally {
                    if (needForFinishProgress) {
                        progress.finishProgress();
                    }
                }
                rereadSynchronizedEmbeddedViews();
            }
            getSelectorListener().afterReread(pid);
        } finally {
            if (readEntireObjectAtFirstRow) {
                setupGroupModelForEntireObjectAtFirstRow(false);
            }
            if (pidWhenReadEntireObject != null) {
                setupGroupModelForReadEntireObjectWithPid(pidWhenReadEntireObject, false);
            }
        }
    }

    private boolean setupGroupModelForEntireObjectAtFirstRow(final boolean readEntireObject) {
        final Set<Integer> rows = new HashSet<>(getGroupModel().getRowsWhenReadEntireObject());
        final Integer firstRow = Integer.valueOf(0);
        final boolean containsFirstRow = rows.contains(firstRow);
        if (readEntireObject) {
            if (containsFirstRow) {
                return false;
            } else {
                rows.add(firstRow);
            }
        } else {
            if (containsFirstRow) {
                rows.remove(firstRow);
            } else {
                return false;
            }
        }
        getGroupModel().setRowsWhenReadEntireObject(rows);
        return true;
    }

    private boolean setupGroupModelForReadEntireObjectWithPid(final Pid pid, final boolean readEntireObject) {
        final Set<Pid> pids = new HashSet<>(getGroupModel().getPidsWhenReadEntireObject());
        final boolean containsPid = pids.contains(pid);
        if (readEntireObject) {
            if (containsPid) {
                return false;
            } else {
                pids.add(pid);
            }
        } else {
            if (containsPid) {
                pids.remove(pid);
            } else {
                return false;
            }
        }
        getGroupModel().setPidsWhenReadEntireObject(pids);
        return true;
    }

    public void setCurrentEntity(EntityModel entityModel) {
        if (!leaveCurrentEntity(false) || entityModel == null) {
            refresh();
            return;
        }

        if (!(entityModel.getContext() instanceof IContext.SelectorRow)) {
            throw new IllegalArgumentError("current entity of selector should have selector row context");
        }
        final boolean needTotalRefresh = currentEntity == null;
        currentEntity = entityModel;
        currentEntity.getEntityContext().setPresentationChangedHandler(new CurrentEntityPresentationChangedHandler(currentEntity));
        uiController.getEditorSpace().hideException();
        /*
         * restoring editor if previous opening cause an error !!! qtjambi
         * crash - fix StandardSelectorWidgetController first if (cantOpenEditor
         * && splitter.isCollapsed(1)) splitter.restorePosition();
         */
        //onSetCurrentEntity.emit(entityModel);
        state = SelectorState.NORMAL;
        if (needTotalRefresh) {
            refresh();
        } else {
            refreshUI();
        }
        openCurrentEntityEditor();
        if (!getCurrentEntityEditor().isOpened()) {
            scheduleRefreshMenu();
        }
        getDefaCurrentEntityHandler().onSetCurrentEntity(currentEntity);

        if (autoInsertEnabled) {
            insertEntityImpl(true, false, false);
            if (selectorWidget instanceof IView && ((IView) selectorWidget).hasUI()) {
                //((IExplorerSelectorWidget) getSelectorWidget()).asQWidget().repaint();
                repaintSelectorWidget(selectorWidget);
            }
        }
    }

    public void showException(final Throwable exception) {
        if (exception != null) {
            state = SelectorState.NORMAL;
            uiController.getEditorSpace().showException(exception);
            refresh();
            getSelectorListener().onShowException(exception);
        }
    }

    public void hideException() {
        uiController.getEditorSpace().hideException();
        refresh();
    }

    private static void restorePresentationChangedHandler(final EntityModel entityModel) {
        if (entityModel.getEntityContext().getPresentationChangedHandler() instanceof CurrentEntityPresentationChangedHandler) {
            final CurrentEntityPresentationChangedHandler handler
                    = (CurrentEntityPresentationChangedHandler) entityModel.getEntityContext().getPresentationChangedHandler();
            entityModel.getEntityContext().setPresentationChangedHandler(handler.getOldPCH());
        }
    }

    public void closeCurrentEntityEditor() {
        if (getCurrentEntityEditor().isOpened()) {
            if (!uiController.getSplitter().isCollapsed(0)) {
                uiController.getSplitter().saveCurrentPosition();
            }
            getCurrentEntityEditor().getView().removeEditorListener(editorListener);
        } else if (currentEntityDialog != null) {
            currentEntityDialog.rejectDialog();
            currentEntityDialog = null;
        }
        getCurrentEntityEditor().close(true);
    }

    protected abstract CurrentEntityHandler getDefaCurrentEntityHandler();

    protected abstract void scheduleSelectorRefresh();

    protected abstract void repaintSelectorWidget(ISelectorWidget widget);

    private boolean leaveCurrentEntityImpl(final boolean forced, final boolean scheduleRefresh) {
        if (currentEntity != null) {
            if (getGroupModel().getRestrictions().getIsChangePositionRestricted() && !forced) {
                return false;
            }
            if (!forced) {
                if (group != null) {
                    group.finishEdit();
                }
            }
            if (!canChangeCurrentEntity(forced)) {
                return false;
            }
            if (currentEntity != null) {//вызов group.finishEdit() может привести к рекурсивному вызову leaveCurrentEntity
                restorePresentationChangedHandler(currentEntity);
                currentEntity = null;
                //В этот момент селектор может находится в неопределенном состоянии
                //Например, при выполнении перечитывания из deleteAll, поэтому
                //делается отложенный вызов.
                //QApplication.postEvent(this, new RefreshSelectorEvent());//RADIX-5104
                if (scheduleRefresh) {
                    scheduleSelectorRefresh();
                }
                closeCurrentEntityEditor();
                getDefaCurrentEntityHandler().onLeaveCurrentEntity();
                //onLeaveCurrentEntity.emit();
            }
        }
        return true;
    }

    public boolean leaveCurrentEntity(boolean forced) {
        return leaveCurrentEntityImpl(forced, true);
    }

    public void copyAll() {
        group.finishEdit();
        if (group.getSelection().isSingleObjectSelected()){
            final EntityModel entityModel = getSindleSelectedObject();
            if (entityModel!=null){
                group.getEnvironment().getClipboard().push(entityModel);
            }
        }else{
            group.getEnvironment().getClipboard().push(group);
        }
    }
    
    EntityModel getSindleSelectedObject(){
        final Pid pid = group.getSelection().getNormalized().getSelectedObjects().iterator().next();
        final int row = group.findEntityByPid(pid);
        if (row>=0){            
            try{
                return group.getEntity(row);                    
            }catch(BrokenEntityObjectException | ServiceClientException | InterruptedException exception){//NOPMD
                //do not expect this exceptions here
                return null;
            }
        }
        return null;
    }

    public boolean deleteAll() throws ServiceClientException {
            try {
                group.finishEdit();
                selector.blockRedraw();
                if (group.deleteAll(false)) {
                    rereadImpl(null, false, true);
                } else {
                    return false;
                }
            } catch (InterruptedException e) {
                return false;
            } finally {
                selector.unblockRedraw();
            }
            getSelectorListener().onDeleteAll();
            return true;
    }
    
    public BatchDeleteResult deleteSelected() throws ServiceClientException{
        final GroupModel groupModel = getGroupModel();
        groupModel.finishEdit();
        final EntityObjectsSelection selection = groupModel.getSelection().getNormalized();
        final Pid currentEntityPid = getCurrentEntity()==null ? null : getCurrentEntity().getPid();
        final boolean currentEntitySelected = currentEntityPid!=null && selection.isObjectSelected(currentEntityPid);
        
        final boolean needForReread = selection.getSelectionMode()==ESelectionMode.EXCLUSION;
        if (needForReread 
            && currentEntityPid!=null 
            && !selection.isObjectSelected(currentEntityPid)
            && !canChangeCurrentEntity(false)){
            return BatchDeleteResult.CANCELLED_BY_USER;
        }
        selector.blockRedraw();
        BatchDeleteResult result = null;
        try{            
            try{
                result = groupModel.deleteSelectedObjects(false);
            }catch(InterruptedException exception){
                return BatchDeleteResult.CANCELLED_BY_USER;
            }
            final int numberOfRejections = result.getNumberOfRejections();
            final MessageProvider mp = getEnvironment().getMessageProvider();
            if (numberOfRejections>1){
                final String message;
                if (result.getNumberOfDeletedObjects()>0){
                    message = mp.translate("Selector", "Following selected objects was not deleted:");
                }else{
                    message = mp.translate("Selector", "Selected objects was not deleted:");
                }
                uiController.showBatchOperationResult(result, message);
            }else if (numberOfRejections==1){
                final String title = mp.translate("Selector", "Selected Object Was not Deleted");
                final String message = result.getRejectionMessages().iterator().next();
                getEnvironment().messageError(title, message);
            }
            if (result.getNumberOfDeletedObjects()>0){
                if (needForReread){
                    rereadImpl(null, false, true);
                }else{
                    //remove from selector deleted objects except of current
                    for (Pid pid: selection.getSelectedObjects()){
                        if (result.objectWasDeleted(pid) && !Objects.equals(pid, currentEntityPid)){
                            selector.entityRemoved(pid);
                        }
                    }
                    //then remove current object if it was deleted
                    if (currentEntitySelected && 
                        result.objectWasDeleted(currentEntityPid)){
                        selector.entityRemoved(currentEntityPid);
                    }
                }
            }
        }finally{
            selector.unblockRedraw();
        }
        getSelectorListener().onDeleteSelected(result);
        return result;
    }

    public void finishEdit() {
        if (selector.getSelectorWidget() != null) {
            selector.getSelectorWidget().finishEdit();
        }
        if (getCurrentEntity() != null) {
            Collection<Property> properties = getCurrentEntity().getActiveProperties();
            for (Property property : properties) {
                property.finishEdit();
            }
            if (getCurrentEntityEditor().isOpened()) {
                getCurrentEntityEditor().getModel().finishEdit();
            }
        }
        finishEditInSynchronizedEmbeddedViews();
    }

    public void closeEvent() {
        if (wasClosed) {
            return;
        }
        wasClosed = true;
        if (group != null) {
            closeImpl();
        }

        if (selectorCommands != null) {
            selectorCommands.close();
        }
        if (editorCommands != null) {
            editorCommands.close();
        }

        {//disconnecting signals for GC
            uiController.disconnect();
            getActions().close();
        }

        getCurrentEntityEditor().close();
    }

    public boolean isNormal() {
        return state == SelectorState.NORMAL;
    }

    public void refresh() {
        if (uiController.isUIUpdatesEnabled()) {
            try {
                refreshUI();
                selector.getActions().refresh();
                if (selector.getSelectorWidget() != null) {
                    selector.getSelectorWidget().refresh(null);
                }
                scheduleRefreshMenu();
            } catch (Exception exception) {
                getEnvironment().getTracer().error(exception);
            }
        } else {
            scheduleSelectorRefresh();//RADIX-6537
        }
    }

    public void setAutoInsertEnabled(final boolean enabled) {
        autoInsertEnabled = enabled && selectorMenu != null;//RADIX-2426
        if (autoInsertEnabled && group != null) {
            group.finishEdit();
            insertEntityImpl(true, false, false);
        }
    }

    public boolean isAutoInsertEnabled() {
        return autoInsertEnabled;
    }

    public boolean disable() {
        if (state == SelectorState.DISABLED) {
            return true;
        }
        if (leaveCurrentEntity(false)) {
            state = SelectorState.DISABLED;
            refresh();
            getEditorSpace().setHidden(true);

            if (selectorWidget != null) {
                selectorWidget.clear();
            }
            return true;
        }
        return false;
    }

    public boolean isDisabled() {
        return state != SelectorState.NORMAL;
    }

    public boolean isCurrentEntityEditorHidden() {
        return (!getCurrentEntityEditor().isOpened() || uiController.getSplitter().isCollapsed(0) || getCurrentEntityEditor().isHidden())
                && getCurrentEntity() != null;
    }

    public boolean isEditorOperationsVisible() {
        if (isCurrentEntityEditorHidden()) {
            final boolean isAnyEditorActionAccessible = getActions().getCopyAction().isEnabled()
                    || getActions().getDeleteAction().isEnabled()
                    || !getCurrentEntity().getRestrictions().getIsUpdateRestricted();
            return isAnyEditorActionAccessible;
        }
        return false;
    }

    public void entityRemoved(final Pid pid) {
        final boolean currentEntityRemoving = getCurrentEntity() != null && getCurrentEntity().getPid().equals(pid);
        if (getSelectorWidget() != null) {
            getSelectorWidget().entityRemoved(pid);
        } else {
            if (currentEntityRemoving) {
                leaveCurrentEntity(true);
            }
            final int idx = getGroupModel().findEntityByPid(pid);
            if (idx > -1) {
                getGroupModel().removeRow(idx);
            }
            refresh();
        }
        if (currentEntityRemoving) {
            getSelectorListener().entityRemoved();
        }
    }

    public boolean setFocusedProperty(final Id propertyId) {
        if (getCurrentEntity() == null) {
            return false;
        }
        final Property property;
        try {
            property = getCurrentEntity().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message
                    = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getCurrentEntity().getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        return property != null && getSelectorWidget() != null && getSelectorWidget().setFocus(property);
    }

    public void setupToolBars() {
        getSelectorMainWindow().setUpdatesEnabled(false);
        try {
            if (editorToolBar != null) {
                editorToolBar.setHidden(isEditorToolbarHidden || !isEditorOperationsVisible());
            }
            if (editorCommands != null) {
                final boolean isEntityEditorHidden = //we can't check result of getCurrentEntityEditor().isOpened() here
                        (getGroupModel().getRestrictions().getIsEditorRestricted() || uiController.getSplitter().isCollapsed(0) || getCurrentEntityEditor().isHidden())
                        && getCurrentEntity() != null && getCurrentEntity().canOpenEntityView();

                if (isEntityEditorHidden) {
                    //Если для текущей EntityModel не открыт редактор - показываем
                    //панели инструментов и команд редактора
                    getActions().getRunEditorDialogAction().setVisible(true);
                    editorCommands.setModel(getCurrentEntity());
                    if (wasShown) {//DBP-1638
                        toolBarsManager.correctToolBarsPosition();
                    }
                } else {
                    getActions().getRunEditorDialogAction().setVisible(false);
                    editorCommands.setModel(null);
                    editorCommands.setHidden(true);
                }
            }
            if (selectorCommands != null) {
                selectorCommands.setEnabled(state == SelectorState.NORMAL);
            }
            getSelectorMainWindow().refreshFilterAndOrderToolbar();
        } finally {
            getSelectorMainWindow().setUpdatesEnabled(true);
        }
    }

    public ExplorerItemView insertEntity() {
        return (ExplorerItemView) insertEntityImpl(false, false, true);

    }

    public ExplorerItemView insertEntityWithReplace() {
        return (ExplorerItemView) insertEntityImpl(false, true, true);
    }

    public final void openCurrentEntityEditor() {
        //открытие синхронизированной модели для редактирования внутри селектора
        if (getCurrentEntity() == null || !getCurrentEntity().canOpenEntityView()) {
            return;
        }

        if (!getCurrentEntityEditor().isOpened()
                && !getGroupModel().getRestrictions().getIsEditorRestricted()
                && !uiController.getSplitter().isCollapsed(0)
                && !openingEditor) {
            openingEditor = true;
            cantOpenEditor = true;
            progressHandle.startProgress(getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Editor..."), true);
            final long time = System.currentTimeMillis();
            {
                final String message
                        = getEnvironment().getMessageProvider().translate("TraceMessage", "Start opening editor of \'%1$s\' entity object");
                getEnvironment().getTracer().debug(String.format(message, getCurrentEntity().getTitle()));
            }
            try {
                getCurrentEntityEditor().open();
                if (progressHandle.wasCanceled()) {
                    throw new InterruptedException();
                }
                cantOpenEditor = false;
            } catch (InterruptedException e) {
                getCurrentEntityEditor().close(true);
                uiController.forceUnblockRedraw();
                uiController.getSplitter().collapse(1);

            } catch (Exception ex) {
                if (getSelectorWidget() != null) {
                    getSelectorWidget().unlockInput();
                }
                getCurrentEntityEditor().close(true);
                uiController.forceUnblockRedraw();
                uiController.getSplitter().saveCurrentPosition();
                uiController.getSplitter().collapse(1);

                final CantOpenEditorError error;

                if (ex instanceof CantOpenEditorError) {//NOPMD
                    error = (CantOpenEditorError) ex;
                } else {
                    error = new CantOpenEditorError(getCurrentEntity(), ex);
                }
                getGroupModel().showException(error.getTitle(getGroupModel().getEnvironment().getMessageProvider()), error);
            } finally {
                openingEditor = false;
                if (cantOpenEditor) {
                    getCurrentEntityEditor().close(true);
                }

                progressHandle.finishProgress();
                if (getCurrentEntityEditor().isOpened()) {
                    if (isEditorToolbarHidden) {
                        getCurrentEntityEditor().getView().setToolBarHidden(isEditorToolbarHidden);
                    }
                    getCurrentEntityEditor().setVisible(true);
                    getCurrentEntityEditor().getView().addEditorListener(editorListener);
                    uiController.getSplitter().restorePosition();
                    refresh();
                } else if (!uiController.getSplitter().isCollapsed(0)) {
                    uiController.forceUnblockRedraw();
                    uiController.getSplitter().collapse(1);
                }
                getActions().refresh();
                setupToolBars();
                getCurrentEntityEditor().setUpdatesEnabled(true);
                if (getCurrentEntity() != null) {
                    final long elapsedTime = System.currentTimeMillis() - time;
                    final String message
                            = getEnvironment().getMessageProvider().translate("TraceMessage", "Opening editor of \'%1$s\' entity object finished. Elapsed time: %2$s mls");
                    getEnvironment().getTracer().debug(String.format(message, getCurrentEntity().getTitle(), elapsedTime));
                }
            }
        } else {
            getActions().refresh();
            setupToolBars();
        }
    }

    public void setToolBarHidden(final boolean hidden) {
        selectorToolBar.setHidden(hidden);
        if (!toolBarsManager.isToolBarsHaveCustomPositions()) {
            if (hidden) {
                getSelectorMainWindow().removeToolBarBreak(selectorToolBar);
            }
            setupToolBars();
        }
    }

    public void setEditorToolBarHidden(final boolean hidden) {
        editorToolBar.setHidden(hidden);
        if (hidden) {
            getSelectorMainWindow().removeToolBarBreak(editorToolBar);
            isEditorToolbarHidden = hidden;
        }
    }

    public void setCommandBarHidden(final boolean hidden) {
        selectorCommands.setPersistentHidden(hidden);
        if (!toolBarsManager.isToolBarsHaveCustomPositions()) {
            if (hidden) {
                getSelectorMainWindow().removeToolBarBreak(selectorCommands);
            }
            setupToolBars();
        }
    }

    public void setEditorCommandBarHidden(final boolean hidden) {
        editorCommands.setPersistentHidden(hidden);
        if (!toolBarsManager.isToolBarsHaveCustomPositions()) {
            if (hidden) {
                getSelectorMainWindow().removeToolBarBreak(editorCommands);
            }
            setupToolBars();
        }
    }

    public IToolBar getToolBar() {//DBP-723
        return selectorToolBar;
    }

    public IToolBarsManager getToolBarsManager() {
        return toolBarsManager;
    }

    public IToolBar getCommandBar() {//DBP-723
        return selectorCommands;
    }

    public IToolBar getEditorToolBar() {
        return editorToolBar;
    }

    public IToolBar getEditorCommandBar() {
        return editorCommands;
    }

    private boolean needForEditorSpace() {
        return (getCurrentEntity() != null && getCurrentEntity().canOpenEntityView()) || getEditorSpace().isExceptionShown();
    }

    private void refreshUI() {
        if (!isDisabled()) {
            if (!getGroupModel().getRestrictions().getIsEditorRestricted()
                    && getGroupModel().getEntitiesCount() > 0
                    && getEditorSpace().isHidden()
                    && needForEditorSpace() &&//RADIX-5104
                    wasShown()) {
                getEditorSpace().setVisible(true);
                if (!splitterInited()) {
                    uiController.loadSplitterSettings();
                }
            } else {
                getEditorSpace().setHidden(
                        !needForEditorSpace()
                        || getGroupModel().getRestrictions().getIsEditorRestricted());
            }
            if (wasShown()
                    && needForEditorSpace()
                    && !getGroupModel().getRestrictions().getIsEditorRestricted()
                    && !splitterInited()) {
                uiController.loadSplitterSettings();
            }
        }
        if (uiController.isUIUpdatesEnabled()) {
            setupToolBars();
        }
    }

    protected abstract void blockSignals(boolean block);

    public boolean isCurrentEntityModified() {
        final EntityModel entity = getCurrentEntity();
        if (entity != null && entity.isExists()) {
            if (getCurrentEntityEditor().isOpened()) {
                return getCurrentEntityEditor().getView().getController().isEdited() || entity.isEdited();
            } else {
                return entity.isEdited();
            }
        }
        return false;
    }

    public boolean update() throws ModelException, ServiceClientException, InterruptedException {
        final EntityModel entity = getCurrentEntity();
        if (entity != null) {
            getModel().finishEdit();
            blockRedraw();
            try {
                if (getCurrentEntityEditor().isOpened()) {
                    if (!getCurrentEntityEditor().getView().applyChanges()) {
                        return false;
                    }
                } else if (!entity.update()) {
                    return false;
                }
                updateSynchronizedEmbeddedViews();
            } finally {
                unblockRedraw();
            }
            getSelectorListener().entityUpdated();
            return true;
        }
        return false;
    }

    public void cancelChanges() {
        final EntityModel entity = getCurrentEntity();
        if (entity != null) {
            getModel().finishEdit();
            if (getCurrentEntityEditor().isOpened()) {
                getCurrentEntityEditor().getView().cancelChanges();
            } else {
                entity.cancelChanges();
            }
            super.cancelChangesInSynchronizedEmbeddedViews();
        }
    }

    private boolean canSafelyCleanCurrentEntity(final CleanModelController cleanController) {
        final EntityModel entity = getCurrentEntity();
        return entity == null ? true : entity.canSafelyClean(cleanController);
    }

    public final boolean canChangeCurrentEntity(final boolean forced) {
        final EntityModel entity = getCurrentEntity();
        if (entity != null && !forced) {
            if (getCurrentEntityEditor() == null) {
                if (isCurrentEntityModified()) {
                    if (getEnvironment().getApplication().getDefManager().getAdsVersion().isSupported()) {
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Save changes in editor of \'%s\'?");
                        Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);

                        EDialogButtonType answer = getEnvironment().messageBox(String.format(message, entity.getTitle()), getEnvironment().getMessageProvider().translate("ExplorerDialog", "Save Changes?"), EDialogIconType.QUESTION, buttons);

                        if (answer.equals(EDialogButtonType.YES)) {
                            blockSignals(true);//В случае смены презентации редактор переоткрыт не будет
                            try {
                                if (!update()) {
                                    return false;
                                }
                            } catch (InterruptedException ex) {
                                return false;
                            } catch (Exception ex) {
                                if (selectorWidget != null) {
                                    selectorWidget.unlockInput();
                                }
                                //forceUnblockRedraw();
                                selector.getModel().showException(selector.getActions().updateActionError, ex);
                                if (ex instanceof ObjectNotFoundError) {//NOPMD
                                    final ObjectNotFoundError objectNotFound = (ObjectNotFoundError) ex;
                                    if (objectNotFound.inContextOf(entity)) {
                                        return true;
                                    }
                                }
                                return false;
                            } finally {
                                blockSignals(false);
                            }
                        } else if (answer.equals(EDialogButtonType.NO)) { //Если не сделать будут проблемы при повторной синхронизации
                            cancelChanges();
                        } else {
                            return false;
                        }
                    } else {
                        final String title = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Close Editor?");
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Close editor of \'%s\' without saving changes?");
                        if (getEnvironment().messageConfirmation(title, String.format(message, entity.getTitle()))) {
                            cancelChanges();
                        } else {
                            return false;
                        }
                    }
                }
                return canSafelyCleanCurrentEntity(CleanModelController.DEFAULT_INSTANCE);
            } else {//if (getCurrentEntityEditor()==null)
                blockSignals(true);//В случае смены презентации редактор переоткрыт не будет
                try {
                    return entity.canSafelyClean(CleanModelController.DEFAULT_INSTANCE);
                } finally {
                    blockSignals(false);
                }
            }
        }//if (entity != null && !forced) 
        return true;
    }

    @Override
    public boolean canSafelyClose(final IView view, final CleanModelController cleanController) {
        //first warn about unsaved changes
        return super.canSafelyClose(view, cleanController) && canSafelyCleanCurrentEntity(cleanController);
    }

    public void notifyPropertyModificationStateChanged(final Property property, final boolean modified) {
        if (modified) {
            selector.getActions().setEntityModifiedState(true);
        } else if (getCurrentEntity() != null) {
            final List<Property> editedProperties = getCurrentEntity().getEditedProperties();
            if (editedProperties.size() == 1 && editedProperties.get(0).getId().equals(property.getId())) {
                selector.getActions().setEntityModifiedState(false);
            } else {
                selector.getActions().refreshEntityModifiedState();
            }
        }
    }

    public static List<EntityModel> paste(final GroupModel groupModel,
            final ISelectorWidget selectorWidget) {
        final Clipboard clipboard = groupModel.getEnvironment().getClipboard();
        if (clipboard.isCompatibleWith(groupModel)) {
            final List<EntityModel> entitiesInClipboard = new ArrayList<>();
            for (EntityModel srcEntity : clipboard) {
                if (groupModel.canPaste(srcEntity)) {
                    entitiesInClipboard.add(srcEntity);
                }
            }
            final BatchCopyResult result = copyObjects(groupModel, selectorWidget, entitiesInClipboard);
            return result.getNewObjects();
        }
        return Collections.emptyList();
    }
    
    private static BatchCopyResult copyObjects(final GroupModel groupModel,
                                             final ISelectorWidget selectorWidget,
                                             final Iterable<EntityModel> sourceEntities) {
        final BatchCopyResult result = new BatchCopyResult();
        final IClientEnvironment environment = groupModel.getEnvironment();
        final MessageProvider mp = environment.getMessageProvider();
        final Iterator<EntityModel> iterator = sourceEntities.iterator();
        final List<Pid> newEntities = new LinkedList<>();
        boolean askOnCancel = true;
        EntityModel srcEntity;
        while (iterator.hasNext()) {
            srcEntity = iterator.next();
            if (newEntities.contains(srcEntity.getPid())){
                continue;
            }
            if (srcEntity.getRestrictions().getIsCopyRestricted()){
                result.addCopyRestricted(srcEntity);
                continue;
            }
            try {
                EntityModel newEntity;
                IDialog.DialogResult creationResult;
                try {
                    newEntity = groupModel.openCreatingEntity(srcEntity.getClassId(), srcEntity);
                    if (newEntity == null) {
                        result.addCopyCancelledProgrammatically(srcEntity);
                        continue;
                    }
                    if (selectorWidget != null) {
                        selectorWidget.afterPrepareCreate(newEntity);
                    }
                    if (newEntity.isExists()){
                        creationResult = IDialog.DialogResult.ACCEPTED;
                    }else{
                        if (needForCreationDialog(newEntity)) {//RADIX-2567
                            final IEntityEditorDialog dialog = getCreationDialog(newEntity);
                            creationResult = dialog.execDialog();
                        } else {
                            if (doCreateEntity(newEntity)) {
                                creationResult = IDialog.DialogResult.ACCEPTED;
                            } else {
                                creationResult = IDialog.DialogResult.REJECTED;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    creationResult = IDialog.DialogResult.REJECTED;
                    newEntity = null;
                }
                if (creationResult == IDialog.DialogResult.REJECTED){//создание текущего объекта было отменено
                    result.addCopyCancelledByUser(srcEntity);
                    if (iterator.hasNext() && askOnCancel){
                        final IMessageBox confirmationDialog = createConfirmationDialog(environment);
                        if (confirmationDialog.execMessageBox()==EDialogButtonType.YES){
                            break;
                        }else{
                            askOnCancel = !confirmationDialog.isOptionActivated();
                            continue;
                        }
                    }
                }
                if (newEntity != null && newEntity.isExists()) {
                    result.addNewObject(srcEntity, newEntity);
                    newEntities.add(newEntity.getPid());
                }
            } catch (ObjectNotFoundError error){
                result.addException(srcEntity, error);
                if (error.getPid()==null || !error.getPid().equals(srcEntity.getPid())){
                    groupModel.showException(mp.translate("Selector", "Failed to create copy"), error);
                    if (iterator.hasNext()
                            && environment.messageConfirmation(null, mp.translate("Selector", "Do you want to cancel all next objects paste?"))) {
                        break;
                    }        
                }
            } catch (Exception e) {
                result.addException(srcEntity, e);
                groupModel.showException(mp.translate("Selector", "Failed to create copy"), e);
                if (iterator.hasNext()
                        && environment.messageConfirmation(null, mp.translate("Selector", "Do you want to cancel all next objects paste?"))) {
                    break;
                }
            }
        }
        return result;
    }
    
    private static IMessageBox createConfirmationDialog(final IClientEnvironment environment){
        final MessageProvider mp = environment.getMessageProvider();
        final Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);

        final IMessageBox cancelDialog
                    = environment.newMessageBoxDialog(mp.translate("Selector", "Do you want to cancel all next objects paste?"), mp.translate("Selector", "Question"), EDialogIconType.QUESTION, buttons);
        cancelDialog.setOptionText(mp.translate("Selector", "Do not ask again"));
        return cancelDialog;
    }

    public List<EntityModel> paste() {
        final GroupModel groupModel = getGroupModel();
        final List<EntityModel> result = paste(groupModel, getSelectorWidget());
        afterCopyObjects(groupModel, result);
        return result;
    }
    
    private void afterCopyObjects(final GroupModel groupModel, final List<EntityModel> newObjects){
        try {
            if (newObjects.size() == 1) {
                final Pid pid = newObjects.get(0).getPid();
                final RadSelectorPresentationDef selectorPresentation = groupModel.getSelectorPresentationDef();
                reread(selectorPresentation.isRestoringPositionEnabled() ? pid : null);
                if (pid != null) {
                    final EntityModel currentEntityModel = getCurrentEntity();
                    if (currentEntityModel == null || !pid.equals(currentEntityModel.getPid())) {
                        try {
                            openModalEditorOfCreatedEntity(newObjects.get(0), groupModel);
                        } catch (InterruptedException exception) {
                            return;
                        }
                    }
                }
            } else if (!newObjects.isEmpty()) {
                reread();
            }
            if (!newObjects.isEmpty()) {
                notifyEntityObjectsCreated(newObjects);
            }
        } catch (ServiceClientException ex) {
            selector.getModel().showException(selector.getActions().rereadActionError, ex);
        } 
    }

    public void reread() throws ServiceClientException {
        if (isDisabled() && getSelectorMainWindow().isFilterAndOrderToolbarVisible()) {
            getSelectorMainWindow().applyFilterAndOrderChanges();
        } else {
            final boolean needToRestorePosition
                    = state == SelectorState.NORMAL && getGroupModel().getSelectorPresentationDef().isRestoringPositionEnabled();
            rereadImpl(null, needToRestorePosition, false);
        }
    }

    public void reread(Pid pid) throws ServiceClientException {
        rereadImpl(pid, pid != null, false);
    }

    public EntityModel create() throws ServiceClientException {
        try {
            final EntityModel newEntity = createEntityImpl(null);
            return newEntity == null ? null : afterEntityCreation(newEntity);
        } catch (InterruptedException e) {
            return null;
        }
    }

    private EntityModel openModalEditorOfCreatedEntity(final EntityModel newEntity, final GroupModel groupModel) throws InterruptedException {
        final RadSelectorPresentationDef sPresentation = groupModel.getSelectorPresentationDef();
        final Id classId = sPresentation.getClassPresentation().getId();
        final IContext.Entity context = new IContext.InSelectorEditing(groupModel);
        final List<Id> editorPresentationIds = sPresentation.getEditorPresentationIds();
        if (editorPresentationIds.isEmpty()) {
            final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Failed to open object '%s' for editing: no editor presentations defined");
            getEnvironment().getTracer().error(String.format(message, newEntity.getTitle()));
            return newEntity;
        } else {
            try {
                final EntityModel entityForEditing = EntityModel.openModel(newEntity.getPid(), classId, editorPresentationIds, context);
                final IEntityEditorDialog dialog
                        = getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(entityForEditing);
                dialog.execDialog();
                return dialog.getEntityModel();
            } catch (ServiceClientException exception) {
                getModel().showException(selector.getActions().runEditorActionError, exception);
                return newEntity;
            }
        }
    }
    
    private EntityModel duplicateSingleObject(final EntityModel sourceEntity) throws ServiceClientException, InterruptedException {
        if (sourceEntity != null) {
            final EntityModel newEntity = createEntityImpl(sourceEntity);
            return newEntity == null ? null : afterEntityCreation(newEntity);
        }
        return null;
    }

    public EntityModel duplicate() throws ServiceClientException {
        try{
            return duplicateSingleObject(getCurrentEntity());
        }catch(InterruptedException ex){
            return null;
        }
    }
    
    public BatchCopyResult duplicateSelected(){
        final GroupModel groupModel = getGroupModel();
        if (groupModel.getSelection().isEmpty()){
            return new BatchCopyResult();
        } else if (groupModel.getSelection().isSingleObjectSelected()){
            final BatchCopyResult result = new BatchCopyResult();
            final EntityModel entityModel = getSindleSelectedObject();
            if (entityModel!=null){
                final EntityModel copy;
                try{
                    copy = duplicateSingleObject(entityModel);
                    if (copy==null){
                        result.addCopyCancelledProgrammatically(entityModel);
                    }else{
                        result.addNewObject(entityModel, copy);
                    }
                }catch(InterruptedException exception){
                    result.addCopyCancelledByUser(entityModel);                    
                }catch(ServiceClientException exception){
                    result.addException(entityModel, exception);
                }
            }        
            return result;
        } else {
            final IClientEnvironment environment = groupModel.getEnvironment();
            final GroupModelReader reader = 
                new GroupModelReader(groupModel, EnumSet.of(GroupModelReader.EReadingFlags.RESPECT_SELECTION));
            final BatchCopyResult result = copyObjects(groupModel, selectorWidget, reader);
            if (reader.wasException()){
                groupModel.showException(environment.getMessageProvider().translate("ExplorerError", "Can't copy objects"), reader.getServiceClientException());
            }
            final List<String> rejections = new ArrayList<>(result.getRejectedObjectPids());
            for (int i=rejections.size()-1; i>=0; i--){
                final String message = result.getRejectionMessage(rejections.get(i));
                if (message==null || message.isEmpty()){
                    rejections.remove(i);
                }                
            }            
            final int numberOfRejections = rejections.size();
            final MessageProvider mp = getEnvironment().getMessageProvider();
            if (numberOfRejections>1){
                final String message;
                if (result.getNumberOfCopies()>0){
                    message = mp.translate("Selector", "Following selected objects was not copied:");
                }else{
                    message = mp.translate("Selector", "Selected objects was not copied:");
                }
                uiController.showBatchOperationResult(result, message);
            }else if (numberOfRejections==1){
                final String pidAsStr = rejections.get(0);
                if (result.getRejectionReason(pidAsStr)!=EBatchCopyRejectionReason.CANCELLED_BY_USER
                    && result.getRejectionReason(pidAsStr)!=EBatchCopyRejectionReason.CANCELLED_PROGRAMMATICALLY){
                    final String title = mp.translate("Selector", "Selected Object Was not Copied");
                    final String message = result.getRejectionMessages().iterator().next();
                    getEnvironment().messageError(title, message);
                }
            }
            final List<EntityModel> newObject = result.getNewObjects();
            afterCopyObjects(groupModel, newObject);
            return result;
        }
    }

    private static boolean needForCreationDialog(final EntityModel entity) {//RADIX-2567
        return entity.getCustomViewId() != null
                || !entity.getEditorPresentationDef().getEditorPages().getTopLevelPages().isEmpty();
    }

    private static IEntityEditorDialog getCreationDialog(final EntityModel entity) throws ServiceClientException, InterruptedException {
        return entity.getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(entity);
    }

    private static boolean doCreateEntity(final EntityModel newEntity) {
        try {
            return newEntity.create() != EEntityCreationResult.CANCELED_BY_CLIENT;
        } catch (InterruptedException exception) {
            return false;
        } catch (Exception exception) {
            final String errorMessageTitle;
            final MessageProvider mp = newEntity.getEnvironment().getMessageProvider();
            if (newEntity.getSrcPid() == null) {
                errorMessageTitle = mp.translate("Editor", "Failed to create object");
            } else {
                errorMessageTitle = mp.translate("Editor", "Failed to create copy");
            }
            newEntity.showException(errorMessageTitle, exception);
            return false;
        }
    }

    public static EntityModel createEntity(final GroupModel groupModel, final EntityModel src, final ISelectorWidget selectorWidget) throws ServiceClientException, InterruptedException {
        final EntityModel newEntity;
        if (src == null) {
            newEntity = groupModel.openCreatingEntity(null, null);
        } else {
            newEntity = groupModel.openCreatingEntity(src.getClassId(), src);
        }
        if (newEntity == null) {
            return null;
        }
        if (selectorWidget != null) {
            selectorWidget.afterPrepareCreate(newEntity);
        }
        if (newEntity.isExists()){
            return newEntity;
        }
        if (needForCreationDialog(newEntity)) {//RADIX-2567
            final IEntityEditorDialog dialog = getCreationDialog(newEntity);
            if (dialog.execDialog() != IDialog.DialogResult.ACCEPTED){
                throw new InterruptedException("Creation was cancelled by user");
            }
            return newEntity.isExists() ? newEntity : null;
        } else {
            return doCreateEntity(newEntity) && newEntity.isExists() ? newEntity : null;
        }
    }

    private EntityModel createEntityImpl(final EntityModel src) throws ServiceClientException, InterruptedException {
        if (canChangeCurrentEntity(false)) {
            return createEntity(getGroupModel(), src, getSelectorWidget());
        }
        return null;
    }

    private EntityModel afterEntityCreation(final EntityModel newEntityModel) throws ServiceClientException, InterruptedException {
        final EntityModel resultEntityModel;
        if (state != SelectorState.NORMAL) {//RADIX-3734                    
            resultEntityModel = openModalEditorOfCreatedEntity(newEntityModel, getGroupModel());
        } else {
            final Pid pid = newEntityModel.getPid();
            final GroupModel groupModel = getGroupModel();
            final boolean isRestoringPositionEnabled = groupModel.getSelectorPresentationDef().isRestoringPositionEnabled();
            rereadImpl(pid, pid != null && isRestoringPositionEnabled, true);
            if (pid != null) {
                final EntityModel currentEntityModel = getCurrentEntity();
                if (currentEntityModel == null || !pid.equals(currentEntityModel.getPid())) {
                    resultEntityModel = openModalEditorOfCreatedEntity(newEntityModel, groupModel);
                } else {
                    resultEntityModel = newEntityModel;
                }
            } else {
                resultEntityModel = newEntityModel;
            }
        }
        notifyEntityObjectsCreated(Collections.singletonList(resultEntityModel));
        return resultEntityModel;
    }

    public void setCondition(org.radixware.schemas.xscml.Sqml condition) throws ServiceClientException, InterruptedException {
        getGroupModel().setCondition(condition);
        rereadImpl(null, false, false);
    }

    public void setCondition(SqmlExpression expression) throws ServiceClientException, InterruptedException {
        getGroupModel().setCondition(expression);
        rereadImpl(null, false, false);
    }

    public void runEditorDialog() throws ServiceClientException {
        runEditorDialogImpl(null);
    }

    private void runEditorDialogImpl(final IProgressHandle ph) throws ServiceClientException {
        if (getCurrentEntity() != null && (!getCurrentEntityEditor().isOpened() || getCurrentEntityEditor().close(false))) {
            final IProgressHandle progress;
            final String progressTitle = getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening Editor...");
            try {
                final EntityModel editorObject;
                if (ph == null) {
                    progress = progressHandle;
                    progress.startProgress(progressTitle, true);
                } else {
                    progress = ph;
                    progress.setText(progressTitle);
                }
                selector.blockRedraw();
                try {
                    editorObject = getCurrentEntity().openInSelectorEditModel();
                    if (progress.wasCanceled()) {
                        return;
                    }
                    currentEntityDialog = getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(editorObject);
                    if (isEditorToolbarHidden) {
                        currentEntityDialog.setToolBarHidden(true);
                    }
                    if (progress.wasCanceled()) {
                        return;
                    }
                } finally {
                    progress.finishProgress();
                    selector.unblockRedraw();
                }
                currentEntityDialog.execDialog();
            } finally {
                if (currentEntityDialog != null) {
                    if (currentEntityDialog.getEntityModel()!=null){
                        currentEntityDialog.getEntityModel().clean();
                    }
                    currentEntityDialog = null;
                }
            }
        }
    }

    private ISelector.Actions getActions() {
        return selector.getActions();
    }

    protected abstract void restoreSettings(GroupModel group);

    protected abstract IToolBar createToolBar(String title);

    protected void scheduleInitToolBars() {
    }

    protected void scheduleRefreshMenu() {
        refreshMenu();
    }

    protected abstract ICommandToolBar createCommandToolBar();

    public void createToolBars() {
        if (selectorToolBar != null) {
            return;
        }
        //Панель инструментов селектора        
        if (selectorToolBar == null) {
            selectorToolBar = createToolBar("selector tool bar");
            selectorToolBar.setObjectName("selectorToolBar");
            getSelectorMainWindow().addToolBar(selectorToolBar);
            selectorToolBar.setFloatable(false);
            selectorToolBar.addAction(getActions().getRunEditorDialogAction());
            selectorToolBar.addAction(getActions().getCreateAction());
            selectorToolBar.addAction(getActions().getDeleteAllAction());
            selectorToolBar.addAction(getActions().getRereadAction());
            selectorToolBar.addAction(getActions().getCopyAllAction());
            selectorToolBar.addAction(getActions().getDuplicateAction());
            selectorToolBar.addAction(getActions().getPasteAction());
            selectorToolBar.addAction(getActions().getViewAuditLogAction());
            selectorToolBar.addAction(getActions().getShowFilterAndOrderToolBarAction());
        }

        if (selectorCommands == null) {
            //Панель команд селектора
            selectorCommands = createCommandToolBar();
            selectorCommands.setObjectName("selectorCommandBar");
            getSelectorMainWindow().addToolBar(selectorCommands);

            selectorCommands.setButtonSize(selectorToolBar.getIconHeight(), selectorToolBar.getIconHeight());
        }
        if (editorToolBar == null) {
            getSelectorMainWindow().addToolBarBreak();
            //Панель инструментов редактора
            editorToolBar = createToolBar("editor tool bar");
            getSelectorMainWindow().addToolBar(editorToolBar);
            editorToolBar.setObjectName("editorToolBar");

            editorToolBar.setFloatable(false);
            editorToolBar.addAction(getActions().getDeleteAction());
            editorToolBar.addAction(getActions().getCopyAction());
            editorToolBar.addAction(getActions().getUpdateAction());
            editorToolBar.addAction(getActions().getCancelChangesAction());
        }
        if (editorCommands == null) {
            //Панель команд редактора
            editorCommands = createCommandToolBar();
            editorCommands.setObjectName("editorCommandBar");
            getSelectorMainWindow().addToolBar(editorCommands);
        }

    }

    public void initToolBars() {
        if (toolBarsManager != null || wasClosed()) {
            return;
        }
        if (group.findNearestExplorerItemView() != null) {
            selectorToolBar.insertAction(getActions().getRunEditorDialogAction(), getActions().getInsertAction());

            final IToolButton insertButton = selectorToolBar.getWidgetForAction(getActions().getInsertAction());
            if (selectorMenu != null)//RADIX-2426
            {
                insertButton.setAutoRaise(true);
                insertButton.setPopupMode(IToolButton.ToolButtonPopupMode.MenuButtonPopup);
                insertButton.addAction(getActions().getAutoInsertAction());
                insertButton.addAction(getActions().getInsertWithReplaceAction());
            }
        } else {
            selectorToolBar.removeAction(getActions().getInsertAction());
        }
        final List<IToolBar> toolBars = new ArrayList<>();
        toolBars.add(selectorToolBar);
        toolBars.add(selectorCommands);
        toolBars.add(editorToolBar);
        toolBars.add(editorCommands);
        toolBarsManager = createToolBarsManager(getSelectorMainWindow(), toolBars, getEnvironment().getConfigStore(), getGroupModel().getConfigStoreGroupName());
        applySettings();

        //апдейт положения тулбар панелей и состояния mainWindow
        final String configStoreGroupName = getModel().getConfigStoreGroupName();
        final ClientSettings settings = getEnvironment().getConfigStore();
        final String key = configStoreGroupName + "/" + SELECTOR_WINDOW_KEY_NAME;

        if (settings.contains(key)) {
            restoreToolBarState(settings, key);
        }
    }

    protected abstract IToolBarsManager createToolBarsManager(ISelectorMainWindow mainWindow, List<IToolBar> toolBars, ClientSettings configStore, String configStoreGroupName);

    protected abstract void viewAuditLog();

    public void applySettings() {

        //обновляем настройки значков в selectorToolBar, editorCommands
        final ClientSettings settings = getEnvironment().getConfigStore();

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        final Dimension iconSize = settings.readSize(SettingNames.Selector.Common.ICON_SIZE_IN_SELECTOR_TOOLBARS);
        if (iconSize != null) {
            selectorToolBar.setIconSize(iconSize.width, iconSize.height);
            selectorCommands.setButtonSize(iconSize.width, iconSize.height);
            editorToolBar.setIconSize(iconSize.width, iconSize.height);
            editorCommands.setButtonSize(iconSize.width, iconSize.height);
        }
        settings.endGroup();
        settings.endGroup();
        settings.endGroup();
    }

    protected abstract void restoreToolBarState(ClientSettings settings, String key);

    public void open(Model model_, final IMenu selectorMenu) {
        group = ((GroupModel) model_);
        if (group.findNearestExplorerItemView() != null) {
            final Model explorerItemModel = group.findNearestExplorerItemView().getModel();
            if (explorerItemModel != group) {//NOPMD
                parentModels = new ParentModelsCollector(explorerItemModel);
                group.findOwner(parentModels);
            } else {
                parentModels = new ParentModelsCollector(null);
            }
        } else {
            parentModels = new ParentModelsCollector(null);
        }

        this.selectorMenu = selectorMenu;
        restoreSettings(group);
        scheduleInitToolBars();
        final String selectorTitle;
        if (group.getContext() instanceof IContext.TableSelect) {
            final IContext.TableSelect context = (IContext.TableSelect) group.getContext();
            if (context.explorerItemDef != null && context.explorerItemDef.hasTitle()) {
                selectorTitle = context.explorerItemDef.getTitle();
            } else {
                selectorTitle = group.getTitle();
            }
        } else {
            selectorTitle = group.getTitle();
        }
        final String progressTitle = getEnvironment().getMessageProvider().translate("Wait Dialog", "Opening '%s'...");
        progressHandle.startProgress(String.format(progressTitle, selectorTitle), true);
        if (!getGroupModel().getRestrictions().getIsEditorRestricted()) {
            setupGroupModelForEntireObjectAtFirstRow(true);
        }
        try {
            try {
                getSelectorMainWindow().setupInitialFilterAndSorting();
            } catch (InterruptedException exception) {
                state = SelectorState.OPENING_INTERRUPTED;
                final String traceMessage = getEnvironment().getMessageProvider().translate("TraceMessage", "Opening of selector '%s' was interrupted");
                getEnvironment().getTracer().event(String.format(traceMessage, selectorTitle));
            }
            if (state != SelectorState.OPENING_INTERRUPTED) {
                if (getSelectorMainWindow().isInitialFilterNeedToBeApplyed()) {
                    selector.disable();
                    try {
                        group.readCommonSettings();
                    } catch (ServiceClientException ex) {
                        throw new CantOpenSelectorError(group, ex);
                    } catch (InterruptedException ex) {
                        state = SelectorState.OPENING_INTERRUPTED;
                        final String traceMessage = getEnvironment().getMessageProvider().translate("TraceMessage", "Opening of selector '%s' was interrupted");
                        getEnvironment().getTracer().event(String.format(traceMessage, selectorTitle));
                    }
                } else {
                    try {
                        try {
                            group.getEntity(0);
                        } catch (BrokenEntityObjectException ex) {
                            //first entity object is broken - just ignoring
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        final boolean canApplySomeFilterOrSorting
                                = !group.getFilters().isEmpty()
                                || !group.getSortings().isEmpty()
                                || group.getFilters().canCreateNew()
                                || group.getSortings().canCreateNew();

                        selector.getActions().getShowFilterAndOrderToolBarAction().setVisible(canApplySomeFilterOrSorting);
                    } catch (ServiceCallFault fault) {
                        if (fault.getFaultString().equals(ExceptionEnum.FILTER_IS_OBLIGATORY.toString())) {
                            selector.disable();
                        } else {
                            throw new CantOpenSelectorError(group, fault);
                        }
                    } catch (ServiceClientException ex) {
                        throw new CantOpenSelectorError(group, ex);
                    } catch (InterruptedException ex) {
                        state = SelectorState.OPENING_INTERRUPTED;
                        final String traceMessage = getEnvironment().getMessageProvider().translate("TraceMessage", "Opening of selector '%s' was interrupted");
                        getEnvironment().getTracer().event(String.format(traceMessage, selectorTitle));
                    } catch (ActivatingPropertyError err) {
                        throw new CantOpenSelectorError(group, err);
                    }
                }
            }
        } finally {
            progressHandle.finishProgress();
        }
        createToolBars();
        initToolBars();
        group.setView(selector);
        //затем связываем панель команд с моделью и она становится
        //видимой/невидимой в зависимости от наличия доступных команд
        //поверх сохраненных настроек
        selectorCommands.setModel(group);
        refresh();
        if (selectorMenu != null) {
            selectorMenu.setEnabled(true);
        }
        if (getSelectorMainWindow().isAnyFilter() || selector.getActions().getShowFilterAndOrderToolBarAction().isChecked()) {
            getSelectorMainWindow().updateFilterAndOrderToolbarVisible(true);
        }
        getEnvironment().getClipboard().addChangeListener(clipboardListener);
    }

    public void notifyOpened(IWidget content) {
        try {
            getSelectorListener().opened(content);
        } finally {
            if (!getGroupModel().getRestrictions().getIsEditorRestricted()) {
                setupGroupModelForEntireObjectAtFirstRow(false);
            }
        }
    }

    public void notifyEntityObjectsCreated(final List<EntityModel> entites) {
        getSelectorListener().entitiesCreated(entites);
    }

    public ISelectorWidget getSelectorWidget() {
        return selectorWidget;
    }

    public void setSelectorWidget(ISelectorWidget widget) {
        this.selectorWidget = widget;
        widget.bind();
        widget.setupSelectorToolBar(getToolBar());
    }

    public void refreshMenu() {
        if (selectorMenu != null) {
            selectorMenu.removeAllActions();
            //Пункты меню селектора
            if (group.findNearestExplorerItemView() != null) {
                selectorMenu.addAction(getActions().getInsertAction());
                selectorMenu.addAction(getActions().getInsertAndEditAction());
                selectorMenu.insertSeparator(null);
            }
            selectorMenu.addAction(getActions().getCreateAction());
            selectorMenu.addAction(getActions().getDeleteAllAction());
            selectorMenu.addAction(getActions().getRereadAction());
            selectorMenu.addAction(getActions().getCopyAllAction());
            selectorMenu.addAction(getActions().getDuplicateAction());
            selectorMenu.addAction(getActions().getPasteAction());
            selectorMenu.addAction(getActions().getShowFilterAndOrderToolBarAction());
            //Пункты меню редактора
            final boolean showEditorActions = (!getCurrentEntityEditor().isOpened() || uiController.getSplitter().isCollapsed(0))
                    && getCurrentEntity() != null;
            if (showEditorActions) {
                selectorMenu.insertSeparator(null);
                selectorMenu.addAction(getActions().getRunEditorDialogAction());
                selectorMenu.addAction(getActions().getDeleteAction());
                selectorMenu.addAction(getActions().getCopyAction());
                selectorMenu.addAction(getActions().getUpdateAction());
                selectorMenu.addAction(getActions().getCancelChangesAction());
            }
            if (RunParams.isDevelopmentMode()) {
                selectorMenu.insertSeparator(null);
                selectorMenu.addAction(getActions().getCopySelectorPresIdAction());
                if (showEditorActions) {
                    selectorMenu.addAction(getActions().getCopyEditorPresIdAction());
                }
            }
            if (getSelectorWidget() != null) {
                getSelectorWidget().setupSelectorMenu(selectorMenu);
            }
        }
    }

    private boolean wasClosed = false;

    public boolean close(boolean forced) {
        if (wasClosed) {
            return true;
        }
        if (group != null && !forced && !group.canSafelyClean(CleanModelController.DEFAULT_INSTANCE))//из group.canSafetyClose() будет вызов canChangeCurrentEntity();
        {
            return false;
        }
        try {
            closeImpl();
        } catch (RuntimeException ex) {
            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Exception on closing selector");
            getEnvironment().getTracer().error(title, ex);
        }            //onLeaveCurrentEntity.emit();

        return true;
    }

    public final GroupModel getGroupModel() {
        if (getCurrentEntity() != null) {
            final IContext.SelectorRow context
                    = (IContext.SelectorRow) getCurrentEntity().getContext();
            return context.parentGroupModel;//NOPMD
        }
        return group;
    }

    public Model getModel() {
        return group;
    }

    public boolean cantOpenEditor() {
        return cantOpenEditor;
    }

    public boolean showEvent() {
        if (wasShown || getGroupModel() == null) {
            return false;
        }
        if (uiController.getSplitter().getCurrentPosition() < 0) {
            if (getGroupModel().isEmpty() || getGroupModel().getRestrictions().getIsEditorRestricted()) {
                getEditorSpace().setVisible(false);
                //editorSpace.setVisible(false);
            } else {
                if (!getEditorSpace().isHidden()
                        && currentEntity != null
                        && !cantOpenEditor
                        && !getCurrentEntityEditor().isOpened()
                        && !getGroupModel().getRestrictions().getIsEditorRestricted()) {
                    try {
                        openCurrentEntityEditor();//если редактор уже был открыт - ничего не произойдет
                    } catch (Throwable ex) {
                        getEnvironment().processException(ex);
                        return false;
                    }
                }
                if (!uiController.splitterInited) {
                    if (uiController.getSplitter().isCollapsed(0) || getCurrentEntityEditor().isOpened()) {
                        uiController.loadSplitterSettings();
                    } else if (currentEntity != null && cantOpenEditor/*RADIX-6758*/) {
                        //if there was some error on opening editor do not try to restore splitter position in refresh()
                        uiController.splitterInited = true;
                    }
                }
            }
        }

        wasShown = true;
        setupToolBars();
        return true;
    }

    public boolean splitterInited() {
        return uiController.splitterInited;
    }

    public void initSplitter() {
        uiController.splitterInited = true;
    }

    boolean wasClosed() {
        return wasClosed;
    }

    public boolean wasShown() {
        return wasShown;
    }

    public IEmbeddedEditor getCurrentEntityEditor() {
        if (cee_ == null) {
            cee_ = uiController.createEntityEditor();
            cee_.setObjectName("entity editor");
        }
        return cee_;
    }

    void copySelectorPresId() {
        uiController.putTextToSystemClipboard(getGroupModel().getSelectorPresentationDef().getId().toString());
    }

    void copyEditorPresId() {        
        final EntityModel curEntity = getCurrentEntity();
        if (curEntity != null) {
            uiController.putTextToSystemClipboard(curEntity.getEditorPresentationDef().getId().toString());
        }
    }

    private void closeImpl() {
        getSelectorListener().closed();
        getEnvironment().getClipboard().removeChangeListener(clipboardListener);
        if (currentEntity != null) {
            restorePresentationChangedHandler(currentEntity);
            currentEntity = null;
            try {
                closeCurrentEntityEditor();
                getDefaCurrentEntityHandler().onLeaveCurrentEntity();
            } catch (RuntimeException ex) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Exception on closing selector");
                getEnvironment().getTracer().error(title, ex);
            }            //onLeaveCurrentEntity.emit();
        }

        if (selectorMenu != null) {
            selectorMenu.removeAllActions();
            selectorMenu.setEnabled(false);
            selectorMenu.disconnect(selector);
            selectorMenu = null;
        }
        if (selectorWidget != null) {
            uiController.closeSelectorWidget(selectorWidget);
            selectorWidget = null;
        }
        if (group != null) {
            uiController.storeSettings();
            group.setView(null);
            afterCloseView();
        }

        if (toolBarsManager != null) {
            toolBarsManager.close();
            toolBarsManager = null;
        }
        getSelectorMainWindow().clear();

        uiController.closeChildWidgets(group);
        group = null;
    }

}
