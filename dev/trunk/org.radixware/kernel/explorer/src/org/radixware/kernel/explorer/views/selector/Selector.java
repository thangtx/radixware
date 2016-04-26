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

package org.radixware.kernel.explorer.views.selector;

import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.views.IEmbeddedEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.ISplitter;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QResizeEvent;
import java.util.List;

import org.radixware.kernel.explorer.env.Application;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import java.util.LinkedList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.AbstractBatchOperationResult;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.BatchDeleteResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.BatchCopyResult;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.SelectorController;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ComponentModificationRegistrator;
import org.radixware.kernel.explorer.widgets.EmbeddedEditor;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;
import org.radixware.kernel.explorer.widgets.commands.CommandToolBar;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.explorer.dialogs.AuditLogDialog;
import org.radixware.kernel.explorer.views.BlockableWidget;
import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.ErrorView;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;
import org.radixware.kernel.explorer.widgets.selector.IExplorerSelectorWidget;

public abstract class Selector extends BlockableWidget implements IExplorerView, ISelector,
        IModificationListener {

    public static final class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon INSERT_INTO_TREE = ClientIcon.Selector.INSERT_INTO_TREE;
        public static final ClientIcon AUTOINSERT_INTO_TREE = ClientIcon.Selector.AUTOINSERT_INTO_TREE;
        public static final ClientIcon INSERT_INTO_TREE_AND_EDIT = ClientIcon.Selector.INSERT_INTO_TREE_AND_EDIT;
        public static final ClientIcon REPLACE_IN_TREE = ClientIcon.Selector.REPLACE_IN_TREE;
        public static final ClientIcon COPY_ALL = ClientIcon.Selector.COPY_ALL;
        public static final ClientIcon CLONE = ClientIcon.Selector.CLONE;
        public static final ClientIcon OPEN_IN_EDITOR = ClientIcon.Selector.OPEN_IN_EDITOR;
        public static final ClientIcon AUDIT = ClientIcon.Selector.AUDIT;
    }

    private static class RefreshSelectorEvent extends QEvent {

        public RefreshSelectorEvent() {
            super(QEvent.Type.User);
        }
    }
    
    private static class MoveSplitterEvent extends QEvent{

        private final boolean isCollapsed;

        public MoveSplitterEvent(final boolean isCollapsed){
            super(QEvent.Type.User);
            this.isCollapsed = isCollapsed;
        }

        public boolean isCollapsed(){
            return isCollapsed;
        }
    }    
    
    private static class InitToolBarsEvent extends QEvent{        
        public InitToolBarsEvent(){
            super(QEvent.Type.User);
        }        
    }
    
    private static class RefreshMenuEvent extends QEvent{        
        public RefreshMenuEvent(){
            super(QEvent.Type.User);
        }
    }

    private class EditorSpace extends QWidget implements IEditorSpace {
        
        private boolean eventProcessing = false;        
        private final QStackedLayout stackedLayout;
        private final ErrorView errorViewer;        
        
        @SuppressWarnings("LeakingThisInConstructor")
        public EditorSpace(final QWidget parent) {
            super(parent);
            setObjectName("editorSpace");            
            final QVBoxLayout mainLayout = WidgetUtils.createVBoxLayout(this);
            stackedLayout = new QStackedLayout();
            stackedLayout.setContentsMargins(0, 0, 0, 0);
            mainLayout.addLayout(stackedLayout);            
            errorViewer  = new ErrorView(getEnvironment(), this);
            errorViewer.setObjectName("errorView");
            stackedLayout.addWidget(errorViewer);
            stackedLayout.addWidget((EmbeddedEditor)controller.getCurrentEntityEditor());
            stackedLayout.setCurrentIndex(1);
        }
        
        @Override
        protected void resizeEvent(final QResizeEvent event) {
            if (!eventProcessing && controller.wasShown()) {
                if (event.size().height() > 0 
                    && (!controller.getCurrentEntityEditor().isOpened() || event.oldSize().height()==0)
                    ){
                    QApplication.removePostedEvents(this, QEvent.Type.User.value());
                    Application.processEventWhenEasSessionReady(this, new MoveSplitterEvent(false));                    
                }
                else if (event.oldSize().height() > 0 && event.size().height() == 0){
                    QApplication.removePostedEvents(this, QEvent.Type.User.value());
                    Application.processEventWhenEasSessionReady(this, new MoveSplitterEvent(true));
                }
            }
            super.resizeEvent(event);
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof MoveSplitterEvent){
                final MoveSplitterEvent moveSplitterEvent = (MoveSplitterEvent)event;
                if (!Selector.this.isUpdatesEnabled()){
                    event.ignore();
                    Application.processEventWhenEasSessionReady(this, new MoveSplitterEvent(moveSplitterEvent.isCollapsed()));
                    return;
                }
                event.accept();
                if (eventProcessing==false){
                    eventProcessing = true;
                    try{
                        if (getCurrentEntity() != null) {
                            if (isVisible()){
                                splitter.updatePosition();
                            }
                            if (moveSplitterEvent.isCollapsed()) {
                                getActions().refresh();
                                controller.setupToolBars();
                                QApplication.postEvent(Selector.this, new RefreshMenuEvent());
                            } else{
                                if (!controller.getCurrentEntityEditor().isOpened()
                                        && !getGroupModel().getRestrictions().getIsEditorRestricted()) {
                                    splitter.saveCurrentPosition();
                                    splitter.blockSignals(true);
                                    try {
                                        openCurrentEntityEditor();
                                    } catch (Throwable ex) {
                                        splitter.collapse(1);
                                        controller.getCurrentEntityEditor().close(true);
                                        getEnvironment().processException(ex);
                                        return;
                                    } finally {
                                        splitter.blockSignals(false);
                                    }
                                } else {
                                    getActions().refresh();
                                    controller.setupToolBars();
                                    QApplication.postEvent(Selector.this, new RefreshMenuEvent());
                                }
                                controller.getToolBarsManager().correctToolBarsPosition();                                
                            }
                        }
                    }
                    finally{
                        eventProcessing = false;
                    }
                }
            }else{
                super.customEvent(event);
            }
        }        
        
        
        @Override
        public void showException(Throwable exception) {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            errorViewer.setError(mp.translate("ExplorerError", "Can't Open Editor"), exception);
            stackedLayout.setCurrentIndex(0);
        }

        @Override
        public void hideException() {
            stackedLayout.setCurrentIndex(1);
        }
               
        @Override
        public boolean isExceptionShown() {
            return stackedLayout.currentIndex()==0;
        }        
    }
    
    public final Actions actions;
    private ComponentModificationRegistrator modificationRegistrator;
    private final Splitter splitter;
    private final SelectorMainWindow selectorMainWindow;
    protected final ExplorerWidget content;
    private final EditorSpace editorSpace;
    private boolean refreshScheduled;
    
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    final public Signal0 onLeaveCurrentEntity = new Signal0();
    final public Signal1<EntityModel> onSetCurrentEntity = new Signal1<>();
    final public Signal1<Pid> afterReread = new Signal1<>();
    final public Signal1<List<EntityModel>> entitiesCreated = new Signal1<>();
    final public Signal0 entityRemoved = new Signal0();
    final public Signal0 entityUpdated = new Signal0();
    final public Signal0 onDeleteAll = new Signal0();
    final public Signal1<BatchDeleteResult> onDeleteSelected = new Signal1<>();
    final public Signal1<IExplorerItemView> insertedIntoTree = new Signal1<>();
    final public Signal1<EntityModel> entityActivated = new Signal1<>();
    final public Signal1<Boolean> modifiedStateChanged = new Signal1<>();
    final public Signal1<Throwable> onShowException = new Signal1<>();
    final public Signal0 onFocusChanged = new Signal0();
    
    private final static String SELECTOR_WINDOW_KEY_NAME = "MainWindow";
    final static String CUSTOM_TOOLBARS_POSITION = "customToolbarsPosition";
    private final static String SPLITTER_KEY_NAME = "splitterRatio";
    private final static String AUTOINSERT_KEY_NAME = "autoInsert";
    private final static String FILTER_AND_ORDER_KEY_NAME = "filterPanelIsVisible";
    
    private final SelectorController controller;
    private final List<SelectorListener> listeners = new LinkedList<>();

    private final class DefaultSelectorListener extends SelectorListener {

        @Override
        public void entityRemoved() {
            entityRemoved.emit();
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.entityRemoved();
                }
            }
        }

        @Override
        public void entityUpdated() {
            entityUpdated.emit();
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.entityUpdated();
                }
            }
        }

        @Override
        public void entitiesCreated(final List<EntityModel> entities) {
            entitiesCreated.emit(entities);
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.entitiesCreated(entities);
                }
            }
        }

        @Override
        public void modifiedStateChanged(boolean modified) {
            modifiedStateChanged.emit(Boolean.valueOf(modified));
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.modifiedStateChanged(modified);
                }
            }
        }

        @Override
        public void insertedIntoTree(IExplorerItemView explorerItemView) {
            insertedIntoTree.emit(explorerItemView);
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.insertedIntoTree(explorerItemView);
                }
            }
        }

        @Override
        public void onShowException(Throwable exception) {
            onShowException.emit(exception);
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.onShowException(exception);
                }
            }            
        }
        

        @Override
        public void onDeleteAll() {
            onDeleteAll.emit();
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.onDeleteAll();
                }
            }
        }

        @Override
        public void onDeleteSelected(final BatchDeleteResult deleteResult) {
            onDeleteSelected.emit(deleteResult);
            synchronized (listeners) {
                for (SelectorListener l : listeners){
                    l.onDeleteSelected(deleteResult);
                }
            }
        }        

        @Override
        public void afterReread(Pid pid) {
            afterReread.emit(pid);
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.afterReread(pid);
                }
            }
        }

        @Override
        public void opened(IWidget content) {
            opened.emit(Selector.this.content);
            synchronized (listeners){
                for (SelectorListener l : listeners) {
                    l.opened(content);
                }                
            }
        }

        @Override
        public void closed() {
            closed.emit();
            synchronized (listeners) {
                for (SelectorListener l : listeners) {
                    l.closed();
                }
            }
        }
    }

    @Override
    public void addSelectorListener(SelectorListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    @Override
    public void removeSelectorListener(SelectorListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected Selector(IClientEnvironment environment) {
                

        super(environment);        
        
        final SelectorController.UIController uiController = new SelectorController.UIController() {

            @Override
            protected void disconnect() {
            }

            @Override
            protected void storeSettings() {
                Selector.this.storeSettings();
            }

            @Override
            protected void restoreSettings(GroupModel group) {
                Selector.this.restoreSettings(group);
            }

            @Override
            protected void forceUnblockRedraw() {
                Selector.this.forceUnblockRedraw();
            }

            @Override
            protected void closeSelectorWidget(ISelectorWidget widget) {
                if (widget instanceof IExplorerSelectorWidget && ((IExplorerSelectorWidget) widget).asQWidget() != null) {
                    ((IExplorerSelectorWidget) widget).asQWidget().close();
                }
            }

            @Override
            protected void closeChildWidgets(GroupModel group) {
                WidgetUtils.closeChildrenEmbeddedViews(group, content);
            }

            @Override
            protected IEmbeddedEditor createEntityEditor() {
                return new EmbeddedEditor(getEnvironment(), Selector.this);
            }

            @Override
            protected void loadSplitterSettings() {
                Selector.this.loadSplitterSettings();
            }

            @Override
            protected ISplitter getSplitter() {
                return splitter;
            }

            @Override
            protected boolean isUIUpdatesEnabled() {
                return Selector.this.updatesEnabled();
            }

            @Override
            protected IEditorSpace getEditorSpace() {
                return Selector.this.editorSpace;
            }

            @Override
            protected void putTextToSystemClipboard(final String text) {
                QApplication.clipboard().setText(text, QClipboard.Mode.Clipboard);
            }

            @Override
            protected void showBatchOperationResult(final AbstractBatchOperationResult result, final String message) {
                final BatchOperationResultDialog dialog = 
                    new BatchOperationResultDialog(getEnvironment(), result, Selector.this);
                dialog.setMessage(message);
                dialog.exec();
            }
        };
        
        this.controller = new SelectorController(this, uiController) {

            @Override
            protected SelectorListener createDefaultListener() {
                return new DefaultSelectorListener();
            }

            @Override
            public ISelectorMainWindow getSelectorMainWindow() {
                return Selector.this.selectorMainWindow;
            }

            @Override
            protected CurrentEntityHandler getDefaCurrentEntityHandler() {
                return defaultCurrentEntityHandler;
            }

            @Override
            protected void scheduleSelectorRefresh() {                
                if (!refreshScheduled){
                    refreshScheduled = true;
                    Application.processEventWhenEasSessionReady(Selector.this, new RefreshSelectorEvent());//RADIX-5104
                }
            }

            @Override
            protected void scheduleRefreshMenu() {
                QApplication.postEvent(Selector.this, new RefreshMenuEvent());                
            }                        

            @Override
            protected void scheduleInitToolBars() {
                QApplication.postEvent(Selector.this, new InitToolBarsEvent());
                if (selectorMainWindow.isAnyFilter() 
                    || getActions().getShowFilterAndOrderToolBarAction().isChecked()){
                    selectorMainWindow.prepareFilterAndOrderToolbar((GroupModel)getModel());
                }
            }

            @Override
            protected void repaintSelectorWidget(ISelectorWidget widget) {
                if (widget instanceof IExplorerSelectorWidget) {
                    ((IExplorerSelectorWidget) widget).asQWidget().repaint();
                }
            }

            @Override
            protected void blockSignals(boolean block) {
                Selector.this.blockSignals(block);
            }

            @Override
            protected void restoreSettings(GroupModel group) {
                Selector.this.restoreSettings(group);
            }

            @Override
            protected IToolBar createToolBar(String objectName) {
                return new ExplorerToolBar(objectName);
            }

            @Override
            protected ICommandToolBar createCommandToolBar() {
                return new CommandToolBar(getEnvironment(), Selector.this);
            }

            @Override
            protected void restoreToolBarState(ClientSettings settings, String key) {
                ((SelectorMainWindow) getSelectorMainWindow()).restoreState(((ExplorerSettings) settings).readQByteArray(key));
            }

            @Override
            protected IToolBarsManager createToolBarsManager(ISelectorMainWindow mainWindow, List<IToolBar> toolBars, ClientSettings configStore, String configStoreGroupName) {
                List<QToolBar> qtbs = new LinkedList<>();
                for (IToolBar tb : toolBars) {
                    qtbs.add((QToolBar) tb);
                }
                return new ToolBarsManager((SelectorMainWindow) mainWindow, qtbs, configStore, configStoreGroupName);
            }                        

            @Override
            protected void viewAuditLog() {
                getModel().finishEdit();
                final Id tableId = getGroupModel().getSelectorPresentationDef().getTableId();
                AuditLogDialog.execAuditLogDialog(getEnvironment(), Selector.this, tableId, null, getGroupModel().getTitle());
            }

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(Selector.this);
            }            
        };
        //progressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        
        //разделитель между селектором и редактором
        splitter = new Splitter(this, (ExplorerSettings) environment.getConfigStore());
        splitter.setOrientation(Qt.Orientation.Vertical);
        setWidget(splitter);        
        
        //селектор
        selectorMainWindow = new SelectorMainWindow(this);
        splitter.addWidget(selectorMainWindow);
        content = selectorMainWindow.getSelectorContent();
        
        actions = new Actions(controller) {

            @Override
            protected Action createAction(ClientIcon icon, String title) {
                return new ExplorerAction(icon == null ? null : getEnvironment().getApplication().getImageManager().getIcon(icon), title, Selector.this);
            }
        };        
        
        controller.createToolBars();
        
        //редактор
        editorSpace = new EditorSpace(this);
        
        splitter.addWidget(editorSpace);
        splitter.setStretchFactor(0, 1);
        splitter.setCollapsible(0, false);
        splitter.setObjectName("splitter");
    }
    
    //Панели инструментов, команд и меню
    protected IMenu selectorMenu, editorMenu;    
    
    @Override
    public ISelector.Actions getActions() {
        return actions;
    }

    public void setMenu(final ExplorerMenu newSelectorMenu, final ExplorerMenu newEditorMenu) {
        if (selectorMenu != null) {
            selectorMenu.removeAllActions();
            selectorMenu.disconnect(this);
        }
        this.selectorMenu = newSelectorMenu;
        if (editorMenu != null) {
            editorMenu.removeAllActions();
            editorMenu.disconnect(this);
        }
        this.editorMenu = newEditorMenu;
        if (controller.getCurrentEntityEditor().isOpened()) {
            ((Editor) controller.getCurrentEntityEditor().getView()).setMenu(newEditorMenu);
        }
    }

    public IMenu getSelectorMenu() {
        return selectorMenu;
    }

    public IMenu getEditorMenu() {
        return editorMenu;
    }

    private void initMenu() {
        if (selectorMenu instanceof QMenu) {
            QMenu asQMenu = (QMenu) selectorMenu;
            if (asQMenu.parent() == null) {                
                final QMenuBar menuBar = selectorMainWindow.menuBar();
                final Qt.WindowFlags menuFlags = asQMenu.windowFlags();
                asQMenu.setParent(menuBar);
                asQMenu.setWindowFlags(menuFlags);
                menuBar.addMenu(asQMenu);                                
            } else {
                selectorMainWindow.setMenuWidget(null);
            }
            selectorMenu.setEnabled(true);            
        }
    }
    
    public void setToolButtonsSize(int size) {
        final QSize buttonSize = new QSize(size, size);
        getToolBar().setIconSize(buttonSize);
        getEditorToolBar().setIconSize(buttonSize);
        getCommandBar().setButtonSize(buttonSize);
        getEditorCommandBar().setButtonSize(buttonSize);
    }

    public int getToolButtonsSize() {
        return getToolBar().iconSize().height();
    }

    @Override
    public void setToolBarHidden(final boolean hidden) {
        controller.setToolBarHidden(hidden);
    }

    @Override
    public void setEditorToolBarHidden(boolean hidden) {
        controller.setEditorToolBarHidden(hidden);
    }
        
    @Override
    public void setCommandBarHidden(final boolean hidden) {
        controller.setCommandBarHidden(hidden);
    }

    @Override
    public void setEditorCommandBarHidden(boolean hidden) {
        controller.setEditorCommandBarHidden(hidden);
    }

    public QToolBar getToolBar() {//DBP-723
        return (QToolBar) controller.getToolBar();
    }

    public CommandToolBar getCommandBar() {//DBP-723
        return (CommandToolBar) controller.getCommandBar();
    }

    public QToolBar getEditorToolBar() {
        return (QToolBar) controller.getEditorToolBar();
    }

    public CommandToolBar getEditorCommandBar() {
        return (CommandToolBar) controller.getEditorCommandBar();
    }

    //таблица селектора
    @Override
    public void setSelectorWidget(ISelectorWidget selectorWidget) {
        controller.setSelectorWidget(selectorWidget);
    }

    @Override
    public ISelectorWidget getSelectorWidget() {
        return controller.getSelectorWidget();
    }

    public void setOrientation(Qt.Orientation orientation) {
        splitter.setOrientation(orientation);
    }

    public void open(final Model model_, final ComponentModificationRegistrator registrator) {
        open(model_);
        modificationRegistrator = registrator;
    }

    @Override
    public void open(Model model_) {
        controller.open(model_, selectorMenu);
        initMenu();
        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
    }
    
    protected final void notifyOpened(){
        controller.notifyOpened(content);
    }
    
    private final List<CurrentEntityHandler> customHandlers = new LinkedList<>();
    private ISelector.CurrentEntityHandler defaultCurrentEntityHandler = new CurrentEntityHandler() {

        @Override
        public void onSetCurrentEntity(EntityModel entity) {
            //Call to openCurrentEntityEditor() was moved into selector controller
            // openCurrentEntityEditor();
            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onSetCurrentEntity(entity);
                }
            }
            onSetCurrentEntity.emit(entity);
        }

        @Override
        public void onLeaveCurrentEntity() {
            //Call to closenCurrentEntityEditor() was moved into selector controller
            //closeCurrentEntityEditor();
            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onLeaveCurrentEntity();
                }
            }
            onLeaveCurrentEntity.emit();
        }
    };

    @Override
    public void addCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            customHandlers.add(handler);
        }
    }

    @Override
    public void removeCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            customHandlers.remove(handler);
        }
    }

    @Override
    public boolean close(boolean forced) {
        if (controller.close(forced)) {
            customHandlers.clear();
            super.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {        
        controller.closeEvent();
        customHandlers.clear();
    }

    @Override
    public void finishEdit() {
        controller.finishEdit();
    }

    private void storeSettings() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        settings.beginGroup(getModel().getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            settings.writeQByteArray(SELECTOR_WINDOW_KEY_NAME, selectorMainWindow.saveState());
            settings.writeBoolean(AUTOINSERT_KEY_NAME, isAutoInsertEnabled());
            settings.writeBoolean(FILTER_AND_ORDER_KEY_NAME, getActions().getShowFilterAndOrderToolBarAction().isChecked());
            if (controller.getToolBarsManager() != null) {
                settings.writeBoolean(CUSTOM_TOOLBARS_POSITION, controller.getToolBarsManager().isToolBarsHaveCustomPositions());
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
        if (!getGroupModel().getRestrictions().getIsEditorRestricted() && controller.isNormal()) {
            saveSplitterSettings();
        }
        selectorMainWindow.storeSettings();
    }

    private void restoreSettings(final GroupModel group) {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        settings.beginGroup(group.getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            if (settings.contains(AUTOINSERT_KEY_NAME)) {
                final boolean autoCheck = settings.readBoolean(AUTOINSERT_KEY_NAME);
                setAutoInsertEnabled(autoCheck);
                if (autoCheck) {
                    getActions().getAutoInsertAction().setChecked(true);
                }
            }
            if (settings.readBoolean(FILTER_AND_ORDER_KEY_NAME, false)) {
                getActions().getShowFilterAndOrderToolBarAction().setChecked(true);
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
    }

    @SuppressWarnings("unused")
    private void saveSplitterSettings() {
        if (controller.splitterInited() && getCurrentEntity() != null && !getGroupModel().getRestrictions().getIsEditorRestricted() && !controller.cantOpenEditor()) {
            final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
            final String groupName = getModel().getConfigStoreGroupName();
            final String key = groupName + "/" + SettingNames.SYSTEM + "/" + SPLITTER_KEY_NAME;
            if (splitter.isCollapsed(1)) {
                settings.writeDouble(key, 1.0);
            } else if (splitter.isCollapsed(0)) {
                settings.writeDouble(key, 0.0);
            } else {
                settings.writeDouble(key, ((double) splitter.getCurrentPosition()) / height());
            }
        }
    }

    private void loadSplitterSettings() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        final String key = getModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + SPLITTER_KEY_NAME;
        if (settings.contains(key)) {
            final double ratio = settings.readDouble(key);
            if (ratio == 0) {
                splitter.collapse(0);
            } else if (ratio == 1.0) {
                splitter.collapse(1);
            } else {
                //Установка позиции здесь позволяет избежать "мыргания"
                splitter.moveToPosition((int) (ratio * height()));
                //Отложенная установка позиции нужна на случай если значение height() 
                //в текущий момент неверное (например, в случае модального диалога).
                splitter.setRatio(ratio);
            }
        } else {
            splitter.moveToPosition(height() / 2);
        }
        splitter.updatePosition();
        controller.initSplitter();
    }

    public final boolean isSplitterCollapsed() {
        if (controller.splitterInited()) {
            return splitter.isCollapsed(1);
        } else {
            final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
            final String key = getModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + SPLITTER_KEY_NAME;
            if (settings.contains(key)) {
                final double ratio = settings.readDouble(key);
                return ratio == 1.0;
            }
            return false;
        }
    }    

    protected final void openCurrentEntityEditor() {
        controller.openCurrentEntityEditor();
    }

    protected void closeCurrentEntityEditor() {
        controller.closeCurrentEntityEditor();
    }

    @Override
    public void setCurrentEntity(EntityModel entityModel) {
        controller.setCurrentEntity(entityModel);
    }

    @Override
    public EntityModel getCurrentEntity() {
        return controller.getCurrentEntity();
    }

    @Override
    public boolean leaveCurrentEntity(boolean forced) {
        return controller.leaveCurrentEntity(forced);
    }

    @Override
    public final boolean canChangeCurrentEntity(final boolean forced) {
        return controller.canChangeCurrentEntity(forced);
    }

    @Override
    public void hideException() {
        controller.hideException();
    }

    @Override
    public void showException(Throwable exception) {
        controller.showException(exception);
    }
    
    @Override
    public void setCurrentFilter(FilterModel filter, boolean apply) {
        selectorMainWindow.setCurrentFilter(filter, apply);
    }

    @Override
    protected void focusInEvent(QFocusEvent event) {
        super.focusInEvent(event);
        selectorMainWindow.setFocus();
        onFocusChanged.emit();
    }

    @Override
    protected void focusOutEvent(QFocusEvent arg__1) {
        super.focusOutEvent(arg__1);
        onFocusChanged.emit();
    } 
    
    @Override
    protected void showEvent(QShowEvent event) {
        super.showEvent(event);
        if (controller.showEvent()) {
            splitter.splitterMoved.connect(this, "saveSplitterSettings()");
            final IToolBarsManager toolBarsManager = controller.getToolBarsManager();
            if (toolBarsManager!=null){
                toolBarsManager.sheduleCorrectToolBarsPosition();
            }
        }
    }
    private boolean resizeEventProcessing = false;

    @Override
    protected void resizeEvent(QResizeEvent event) {
        if (!resizeEventProcessing) {
            resizeEventProcessing = true;
            super.resizeEvent(event);
            if (controller.wasShown()) {                
                if (event.oldSize().width() != event.size().width() && controller.isEditorOperationsVisible()) {
                    controller.getToolBarsManager().correctToolBarsPosition();
                }
                splitter.updatePosition();
            }
            resizeEventProcessing = false;
        }
    }

    @Override
    public void entityRemoved(Pid pid) {
        controller.entityRemoved(pid);
    }
    
    public final void notifyEntityObjectsCreated(final List<EntityModel> entities) {
        this.controller.notifyEntityObjectsCreated(entities);
    }    

    @Override
    public final GroupModel getGroupModel() {
        return controller.getGroupModel();
    }

    @Override
    public /*final*/ Model getModel() {
        return controller.getModel();
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return controller.setFocusedProperty(propertyId);
    }

    //Операции, выполняемые селектором
    @Override
    public ExplorerItemView insertEntity() {
        return controller.insertEntity();
    }

    @Override
    public ExplorerItemView insertEntityWithReplace() {
        return controller.insertEntityWithReplace();
    }

    @Override
    public void runEditorDialog() throws ServiceClientException {
        controller.runEditorDialog();
    }

    @Override
    public void setAutoInsertEnabled(final boolean enabled) {
        controller.setAutoInsertEnabled(enabled);
    }

    @Override
    public boolean isAutoInsertEnabled() {
        return controller.isAutoInsertEnabled();
    }

    @Override
    public void reread() throws ServiceClientException {
        controller.reread();
    }

    @Override
    public void reread(Pid pid) throws ServiceClientException {
        controller.reread(pid);
    }

    /**
     * Создание новой сущности.
     * Метод создает модель для новой сущности и открывает для нее
     * модальный редактор. После успешного завершения операции создания производится перечитывание всех записей в
     * селекторе с поиском созданной сущности, которая становится текущей. Если сущность была успешно
     * создана генерируется сигнал {@link #entityCreated}.
     * @return Если сущность была создана возвращает ее модель, и null если создание было отменено
     * @throws ServiceClientException - ошибки при выполнении запросов на сервер
     * @see {@link #duplicate()}
     * @see {@link GroupModel#openCreatingEntity(String, EntityModel)}
     *
     */
    @Override
    public EntityModel create() throws ServiceClientException {
        return controller.create();
    }

    /**
     * Создание копии текущей модели сущности.
     * Метод создает модель для новой сущности, используя в качестве шаблона
     * {@link #getCurrentEntity() текущую модель сущности} и открывает для нее
     * модальный редактор. Если модель текущей сущности не задана возвращает null.
     * После успешного завершения операции создания производится перечитывание всех записей в
     * селекторе с поиском созданной сущности, которая становится текущей. Если сущность была успешно
     * создана генерируется сигнал {@link #entityCreated}.
     * @return Если сущность была создана возвращает ее модель, и null если создание было отменено
     * @throws ServiceClientException - ошибки при выполнении запросов на сервер
     * @see {@link #create()}
     * @see {@link GroupModel#openCreatingEntity(String, EntityModel)}
     */
    @Override
    public EntityModel duplicate() throws ServiceClientException {
        return controller.duplicate();
    }

    @Override
    public boolean applyChanges() throws ServiceClientException, ModelException {
        try{
            return controller.update();
        }
        catch(InterruptedException exception){
            return false;
        }
    }        

    @Override
    public void cancelChanges() {
        controller.cancelChanges();
    }        

    @Override
    public List<EntityModel> paste() {
        return controller.paste();
    }

    @Override
    public void copyAll() {
        controller.copyAll();
    }

    @Override
    public boolean deleteAll() throws ServiceClientException {
        return controller.deleteAll();
    }

    @Override
    public BatchDeleteResult deleteSelected() throws ServiceClientException {
        return controller.deleteSelected();
    }
    
    @Override
    public BatchCopyResult duplicateSelected(){
        return controller.duplicateSelected();
    }

    public void setCondition(org.radixware.schemas.xscml.Sqml condition) throws ServiceClientException, InterruptedException {
        controller.setCondition(condition);
    }

    public void setCondition(SqmlExpression expression) throws ServiceClientException, InterruptedException {
        controller.setCondition(expression);
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public boolean disable() {
        return controller.disable();
    }

    @Override
    public boolean isDisabled() {
        return controller.isDisabled();
    }

    public void applySettings() {
        controller.applySettings();
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        if (event.matches(QKeySequence.StandardKey.Save) && getActions().getUpdateAction().isEnabled()) {
            getActions().getUpdateAction().trigger();
        } else if (event.matches(QKeySequence.StandardKey.New) && getActions().getCreateAction().isEnabled()) {
            getActions().getCreateAction().trigger();
        } else if (event.matches(QKeySequence.StandardKey.Delete) && getActions().getDeleteAction().isEnabled()) {
            getActions().getDeleteAction().trigger();
        } else {
            super.keyPressEvent(event);
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof RefreshSelectorEvent) {
            event.accept();
            if (isInternalPaintingActive()){
                Application.processEventWhenEasSessionReady(Selector.this, new RefreshSelectorEvent());
            }
            else{
                refreshScheduled = false;
                if (getGroupModel() != null) {
                    refresh();
                }
            }                
        } else if (event instanceof RefreshMenuEvent){
            event.accept();
            controller.refreshMenu();
        } else if (event instanceof InitToolBarsEvent){
            event.accept();
            controller.initToolBars();
        } else {
            super.customEvent(event);
        }
    }

    @Override
    protected void filteredMouseEvent(final FilteredMouseEvent event) {
        if (getSelectorWidget() instanceof QObject){
            QApplication.sendEvent((QObject)getSelectorWidget(), new FilteredMouseEvent(event.getFilteredEventType()));
        }        
    }        

    @Override
    public QWidget asQWidget() {
        return this;
    }

    //Реализация интерфейса IModificationListener
    @Override
    public void childComponentWasClosed(IModifableComponent childComponent) {
        if (modificationRegistrator != null) {
            modificationRegistrator.unregisterChildComponent(childComponent);
        }
    }

    @Override
    public void notifyComponentModificationStateChanged(IModifableComponent childComponent, boolean modified) {
        if (modificationRegistrator != null) {
            modificationRegistrator.notifyModificationStateChanged(childComponent, modified);
        }
    }

    @Override
    public void notifyPropertyModificationStateChanged(Property property, boolean modified) {
        controller.notifyPropertyModificationStateChanged(property, modified);
    }

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(this);
    }

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(this);
    }

    @Override
    public boolean hasUI() {
        return nativePointer() != null;
    }

    @Override
    public boolean isUpdatesEnabled() {
        return updatesEnabled();
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }         
}