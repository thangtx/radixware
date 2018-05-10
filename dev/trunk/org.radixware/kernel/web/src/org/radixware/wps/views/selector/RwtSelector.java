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
package org.radixware.wps.views.selector;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.BatchDeleteResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.BatchCopyResult;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IEmbeddedEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.ISelector.ISelectorMainWindow;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.views.SelectorController;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.ISplitter;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.dialogs.AuditLogDialog;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.Grid.Cell;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.Splitter;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.views.CommandToolBar;
import org.radixware.wps.views.ComponentModificationRegistrator;
import org.radixware.wps.views.ViewSupport;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editor.Editor;
import org.radixware.wps.views.editor.EmbeddedEditor;
import org.radixware.wps.views.editor.ErrorView;
import org.radixware.wps.views.selector.tree.RwtSelectorTree;

public abstract class RwtSelector extends Container implements ISelector, IModificationListener {

    public static abstract class CurrentEntitySetAdapter implements CurrentEntityHandler {

        @Override
        public void onLeaveCurrentEntity() {
            //do nothing
        }
    }

    public static abstract class CurrentEntityLeaveAdapter implements CurrentEntityHandler {

        @Override
        public void onSetCurrentEntity(EntityModel e) {
            //do nothing
        }
    }

    //Этот класс нужен для radix-дизайнера
    public static class SelectorListenerAdapter extends ISelector.SelectorListener {
    };

    private class EditorSpace extends VerticalBox implements IEditorSpace {

        private boolean isExceptionShown = false;
        private IEmbeddedEditor editor;
        private ErrorView errorView;

        @Override
        public boolean isHidden() {
            return !isVisible();
        }

        @Override
        public void setHidden(final boolean hidden) {
            setVisible(!hidden);
        }

        @Override
        public void setVisible(final boolean isVisible) {
            if (isVisible!=isVisible()){
                super.setVisible(isVisible);
                RwtSelector.this.afterChangeEditorSpaceVisibility(isVisible);
            }
        }

        @Override
        public void showException(Throwable exception) {
            if (errorView == null) {
                errorView = new ErrorView();
                errorView.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
                add(errorView);
            }
            errorView.setVisible(true);
            if (editor != null) {
                editor.setVisible(false);
            }
            final MessageProvider mp = getEnvironment().getMessageProvider();
            errorView.setError(mp.translate("ExplorerError", "Can't Open Editor"), exception);
            isExceptionShown = true;
        }

        @Override
        public void hideException() {
            if (errorView != null) {
                errorView.setVisible(false);
            }
            if (editor != null) {
                editor.setVisible(true);
            }
            isExceptionShown = false;
        }

        @Override
        public boolean isExceptionShown() {
            return isExceptionShown;
        }

        @Override
        public void add(UIObject child) {
            if (child instanceof IEmbeddedEditor) {
                editor = (IEmbeddedEditor) child;
            }
            super.add(child);
        }
    }
    private final WpsEnvironment env;
    private final EditorSpace editorSpace;
    private FilterAndOrderToolBar filtersAndSortingsToolBar;
    private final Actions actions;
    private final ViewSupport<RwtSelector> viewSupport;
    private ComponentModificationRegistrator modificationRegistrator;
    private final List<SelectorListener> listeners = new LinkedList<>();
    private final List<CurrentEntityHandler> customHandlers = new LinkedList<>();
    final Splitter mainSplitter;
    private final Splitter splitter;
    private final Container contentHolder = new Container();
    private final PushButton pbApplyFilter;
    private final RwtSelectorMainWindow mainWindow;
    protected IMenu selectorMenu, editorMenu;
    private ISelector.CurrentEntityHandler defaultCurrentEntityHandler = new CurrentEntityHandler() {
        @Override
        public void onSetCurrentEntity(EntityModel entity) {
            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onSetCurrentEntity(entity);
                }
            }
        }

        @Override
        public void onLeaveCurrentEntity() {
            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onLeaveCurrentEntity();
                }
            }
        }
    };
    private SelectorController controller;
    private float filterAntOrderPart = 0.1f;

    void showFilterAndOrder(boolean show, FilterModel defaultFilter, RadSortingDef defaultSorting) {
        if (show) {
            GroupModel model = (GroupModel) getModel();
            if (model != null) {
                if (filtersAndSortingsToolBar == null) {
                    FilterAndOrderToolBar tb = new FilterAndOrderToolBar(env, model, defaultFilter, defaultSorting);
                    mainSplitter.add(0, tb);
                    filtersAndSortingsToolBar = tb;
                } else if (mainSplitter.indexOf(filtersAndSortingsToolBar) < 0) {
                    mainSplitter.add(0, filtersAndSortingsToolBar);
                } else {
                    return;
                }
                mainSplitter.setPart(0, filterAntOrderPart);
                mainSplitter.expand(0);
                final String groupName = model.getConfigStoreGroupName();
                mainSplitter.setRatioSettingKey(groupName + "/" + SettingNames.SYSTEM + "/mainSplitterRatio");
                mainSplitter.restorePosition();
            }
        } else {
            mainSplitter.saveCurrentPosition();
            if (filtersAndSortingsToolBar != null) {
                mainSplitter.remove(filtersAndSortingsToolBar);
            }
        }
    }

    void applyFilterAndOrderChanges() {
        if (filtersAndSortingsToolBar != null) {
            filtersAndSortingsToolBar.applyChanges();
        }
    }
    
    void switchToContentMode(){        
        pbApplyFilter.setVisible(false);
        splitter.setVisible(true);
    }
    
    void switchToApplyFilterMode(){
        splitter.setVisible(false);
        pbApplyFilter.getHtml().setCss("display", "block");
    }

    boolean isFilterAndOrderVisible() {
        return filtersAndSortingsToolBar != null && !splitter.isCollapsed(0);
    }

    void refreshFilterAndOrder() {
        if (filtersAndSortingsToolBar != null) {
            filtersAndSortingsToolBar.refresh();
        }
    }

    void closeFilterAndOrder() {
        if (filtersAndSortingsToolBar != null) {
            filtersAndSortingsToolBar.close();
            filtersAndSortingsToolBar = null;
        }
    }

    FilterModel getCurrentFilter() {
        return filtersAndSortingsToolBar.getCurrentFilter();
    }

    boolean filterWasApplied() {
        return filtersAndSortingsToolBar.filterWasApplied();
    }

    public RwtSelector(final IClientEnvironment env) {
        super();
        mainSplitter = new Splitter(env.getConfigStore(), null);//set splitter ratio
        splitter = new Splitter(env.getConfigStore(), null);

        this.viewSupport = new ViewSupport<>(this);
        this.env = (WpsEnvironment) env;
        this.mainWindow = new RwtSelectorMainWindow(this);
        mainSplitter.setOrientation(Splitter.Orientation.VERTICAL);
        mainSplitter.setTop(0);
        mainSplitter.setLeft(0);
        mainSplitter.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        mainSplitter.getAnchors().setRight(new Anchors.Anchor(1, -1));
        add(mainSplitter);
        splitter.setOrientation(Splitter.Orientation.VERTICAL);
        splitter.setTop(0);
        splitter.setLeft(0);
        splitter.setVCoverage(100);
        splitter.setHCoverage(100);
        contentHolder.add(splitter);
        contentHolder.setTop(0);
        contentHolder.setLeft(0);
        contentHolder.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        contentHolder.setObjectName("rx_selector_content_holder");
        mainSplitter.add(contentHolder);
        
        
        pbApplyFilter = new PushButton(env.getMessageProvider().translate("Selector", "Apply Filter"));        
        pbApplyFilter.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_OK));
        pbApplyFilter.setTop(5);
        pbApplyFilter.getHtml().setCss("position", "relative");        
        pbApplyFilter.getHtml().setCss("margin", "auto");
        pbApplyFilter.addClickHandler(new IButton.ClickHandler(){

            @Override
            public void onClick(final IButton source) {
                applyFilterAndOrderChanges();
            }
            
        });        
        pbApplyFilter.setObjectName("rx_pb_apply_filter");
        pbApplyFilter.setVisible(false);    
        contentHolder.add(pbApplyFilter);
        
        final SelectorController.UIController uiController = new SelectorController.UIController() {
            @Override
            protected void disconnect() {
            }

            @Override
            protected void storeSettings() {
                RwtSelector.this.storeSettings();
            }

            @Override
            protected void restoreSettings(GroupModel group) {
                RwtSelector.this.restoreSettings(group);
            }

            @Override
            protected void forceUnblockRedraw() {
            }

            @Override
            protected void closeSelectorWidget(ISelectorWidget widget) {
                if (widget instanceof UIObject) {
                    splitter.remove((UIObject) widget);
                }
            }

            @Override
            protected void closeChildWidgets(GroupModel group) {
            }

            @Override
            protected void loadSplitterSettings() {
                //RwtSelector.this.loadSplitterSettings();
            }

            @Override
            protected IEmbeddedEditor createEntityEditor() {
                EmbeddedEditor ee = new EmbeddedEditor(RwtSelector.this);
                ee.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
                editorSpace.add(ee);
                return ee;
            }

            @Override
            protected ISplitter getSplitter() {
                return splitter;
            }

            @Override
            protected IEditorSpace getEditorSpace() {
                return editorSpace;
            }

            @Override
            protected boolean isUIUpdatesEnabled() {
                return true;
            }

            @Override
            protected void putTextToSystemClipboard(final String text) {
                IDialog dlg = getEnvironment().getApplication().getDialogFactory().newMessageWithDetailsDialog(env, null, "Information", text, null, EDialogIconType.INFORMATION);
                dlg.execDialog();
            }
        };
        this.controller = new SelectorController(this, uiController) {

            @Override
            protected SelectorListener createDefaultListener() {
                return new SelectorListener() {
                    @Override
                    public void entityRemoved() {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.entityRemoved();
                            }
                        }
                    }

                    @Override
                    public void entityUpdated() {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.entityUpdated();
                            }
                        }
                    }

                    @Override
                    public void entitiesCreated(List<EntityModel> entities) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.entitiesCreated(entities);
                            }
                        }
                    }

                    @Override
                    public void modifiedStateChanged(boolean modified) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.modifiedStateChanged(modified);
                            }
                        }
                    }

                    @Override
                    public void insertedIntoTree(IExplorerItemView explorerItemView) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.insertedIntoTree(explorerItemView);
                            }
                        }
                    }

                    @Override
                    public void onShowException(Throwable exception) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.onShowException(exception);
                            }
                        }
                    }

                    @Override
                    public void onDeleteAll() {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.onDeleteAll();
                            }
                        }
                    }

                    @Override
                    public void afterReread(Pid pid) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.afterReread(pid);
                            }
                        }
                    }

                    @Override
                    public void opened(IWidget content) {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.opened(content);
                            }
                        }
                    }

                    @Override
                    public void closed() {
                        synchronized (listeners) {
                            for (SelectorListener l : listeners) {
                                l.closed();
                            }
                        }
                    }
                };
            }

            @Override
            public ISelectorMainWindow getSelectorMainWindow() {
                return mainWindow;
            }

            @Override
            protected CurrentEntityHandler getDefaCurrentEntityHandler() {
                return defaultCurrentEntityHandler;
            }

            @Override
            protected void scheduleSelectorRefresh() {
            }

            @Override
            protected void repaintSelectorWidget(ISelectorWidget widget) {
            }

            @Override
            protected void blockSignals(boolean block) {
            }

            @Override
            protected void restoreSettings(GroupModel group) {
                RwtSelector.this.restoreSettings(group);
            }

            @Override
            protected IToolBar createToolBar(String title) {
                return new ToolBar();
            }

            @Override
            protected ICommandToolBar createCommandToolBar() {
                return new CommandToolBar();
            }

            @Override
            protected IToolBarsManager createToolBarsManager(ISelectorMainWindow mainWindow, List<IToolBar> toolBars, ClientSettings configStore, String configStoreGroupName) {
                return new RwtToolBarsManager();
            }

            @Override
            protected void restoreToolBarState(ClientSettings settings, String key) {
            }

            @Override
            protected void viewAuditLog() {
                AuditLogDialog.execAuditLogDialog(env, getGroupModel().getSelectorPresentationDef().getTableId(), null, getGroupModel().getTitle());
            }

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }

            @Override
            protected void execPresentationInfoDialog(String title, String classId, String className, String presentationId, String presentationName, String explorerItemId, String pid) {
                RwtPresentationInfoDialog dlg = new RwtPresentationInfoDialog(title, classId, className, presentationId, presentationName, explorerItemId, pid);
                dlg.execDialog();
            }
            
        };

        this.actions = new Actions(controller) {
            @Override
            protected Action createAction(ClientIcon icon, String title) {
                RwtAction action = new RwtAction(env, icon, title);
                action.setToolTip(title);
                return action;
            }
        };

        this.editorSpace = new EditorSpace();
        splitter.add(this.editorSpace);
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public Actions getActions() {
        return actions;
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public boolean isUpdatesEnabled() {
        return true;
    }

    @Override
    public boolean canChangeCurrentEntity(final boolean force) {
        return controller.canChangeCurrentEntity(force);
    }

    @Override
    public EntityModel getCurrentEntity() {
        return controller.getCurrentEntity();
    }

    @Override
    public void setCurrentEntity(EntityModel entity) {
        controller.setCurrentEntity(entity);
//        this.currentEntity = entity;
//        List<CurrentEntityHandler> handlers;
//        synchronized (currentEntityHandlers) {
//            handlers = new ArrayList<CurrentEntityHandler>(currentEntityHandlers);
//        }
//        for (CurrentEntityHandler h : handlers) {
//            h.onSetCurrentEntity(entity);
//        }
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
    public void entityRemoved(Pid pid) {
        controller.entityRemoved(pid);
    }

    @Override
    public void repaint() {
    }

    @Override
    public boolean disable() {
        return controller.disable();
    }

    @Override
    public boolean leaveCurrentEntity(boolean force) {
        return controller.leaveCurrentEntity(force);
    }
//    abstract UIObject getMainComponent();
//
//    int getMainComponentTop() {
//        return isToolBarHidden && isCommandBarHidden ? 0 : 33;
//    }

    @Override
    public void setToolBarHidden(boolean hidden) {
        controller.setToolBarHidden(hidden);
        mainWindow.checkTbHandleVisibility();
    }

    @Override
    public void setEditorToolBarHidden(boolean hidden) {
        controller.setEditorToolBarHidden(hidden);
        mainWindow.checkTbHandleVisibility();
    }

    @Override
    public void setCommandBarHidden(boolean hidden) {
        controller.setCommandBarHidden(hidden);
        mainWindow.checkTbHandleVisibility();
    }

    @Override
    public void setEditorCommandBarHidden(boolean hidden) {
        controller.setEditorCommandBarHidden(hidden);
        mainWindow.checkTbHandleVisibility();
    }
    
    public ToolBar getToolBar() {//DBP-723
        return (ToolBar) controller.getToolBar();
    }

    public CommandToolBar getCommandBar() {//DBP-723
        return (CommandToolBar) controller.getCommandBar();
    }

    public ToolBar getEditorToolBar() {
        return (ToolBar) controller.getEditorToolBar();
    }

    public CommandToolBar getEditorCommandBar() {
        return (CommandToolBar) controller.getEditorCommandBar();
    }
    

    @Override
    public void setSelectorWidget(ISelectorWidget widget) {
        if (widget == null) {
            if (controller.getSelectorWidget() != null) {
                splitter.remove((UIObject) controller.getSelectorWidget());
            }
        } else {
            int index = splitter.indexOf(editorSpace);
            splitter.add(index, (UIObject) widget);
            ((UIObject) widget).setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            ((UIObject) widget).getHtml().setAttr("role", "selectorWidget");
            controller.setSelectorWidget(widget);
            if (getSelectorWidget() instanceof RwtSelectorGrid) {
                ((RwtSelectorGrid) getSelectorWidget()).restoreHeaderSettings();
            } else if (getSelectorWidget() instanceof RwtSelectorTree) {
                ((RwtSelectorTree) getSelectorWidget()).restoreHeaderSettings();
            }
        }
    }

    @Override
    public void setMenu(IMenu newSelectorMenu, IMenu newEditorMenu) {
        if (selectorMenu != null) {
            selectorMenu.clear();
        }
        this.selectorMenu = newSelectorMenu;
        if (editorMenu != null) {
            editorMenu.clear();
        }
        this.editorMenu = newEditorMenu;
        if (controller.getCurrentEntityEditor().isOpened()) {
            ((Editor) controller.getCurrentEntityEditor().getView()).setMenu(newEditorMenu);
        }
    }

    @Override
    public ISelectorWidget getSelectorWidget() {
        return controller.getSelectorWidget();
    }

    @Override
    public GroupModel getGroupModel() {
        return controller.getGroupModel();
    }

    @Override
    public final void addCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            if (!customHandlers.contains(handler)) {
                customHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            customHandlers.remove(handler);
        }
    }

    public void open(final Model model_, final ComponentModificationRegistrator registrator) {
        open(model_);
        modificationRegistrator = registrator;
    }

    @Override
    public void open(Model model) {
        if (getSelectorWidget() instanceof RwtSelectorGrid) {
            ((RwtSelectorGrid) getSelectorWidget()).restoreHeaderSettings();
        } else if (getSelectorWidget() instanceof RwtSelectorTree) {
            ((RwtSelectorTree) getSelectorWidget()).restoreHeaderSettings();
        }
        controller.open(model, selectorMenu);
        controller.showEvent();
        setObjectName("rx_selector_view_#"+model.getDefinition().getId());
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_DELETE));
    }

    protected final void notifyOpened() {
        this.controller.notifyOpened(this);
    }

    public final void notifyEntityObjectsCreated(final List<EntityModel> entities) {
        this.controller.notifyEntityObjectsCreated(entities);
    }

    //for code generator unification
    protected final void fireOpened() {
        notifyOpened();
    }

    @Override
    public void setFocus() {
        ISelectorWidget selectorWidget = getSelectorWidget();
        if (selectorWidget != null && selectorWidget instanceof RwtSelectorGrid) {
            Cell cell = ((RwtSelectorGrid)selectorWidget).getCell(0, 1);
            if (cell != null) {
                ((RwtSelectorGrid)selectorWidget).setCurrentCell(cell);
                cell.setFocused(true);
            }
        }
    }

    @Override
    public boolean setFocusedProperty(Id id) {
        return controller.setFocusedProperty(id);
    }

    @Override
    public void setCurrentFilter(FilterModel filter, boolean apply) {
    }

    @Override
    public boolean close(boolean forced) {
        if (controller.close(forced)) {
            customHandlers.clear();
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
    public void finishEdit() {
        controller.finishEdit();
    }

    @Override
    public Model getModel() {
        return controller.getModel();
    }

    public IMenu getEditorMenu() {
        return editorMenu;
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }

    @Override
    public void reread() throws ServiceClientException {
        controller.reread();
    }

    @Override
    public void reread(Pid pid) throws ServiceClientException {
        controller.reread(pid);//
    }

    @Override
    public boolean applyChanges() throws ServiceClientException, ModelException {
        try {
            return controller.update();
        } catch (InterruptedException exception) {
            return false;
        }
    }

    @Override
    public void cancelChanges(){
        controller.cancelChanges();
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return controller.isDisabled();
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

    @Override
    public void blockRedraw() {
    }

    @Override
    public void copyAll() {
        controller.copyAll();
    }

    @Override
    public EntityModel create() throws ServiceClientException {
        return controller.create();
    }

    @Override
    public List<EntityModel> createMultiple() throws ServiceClientException {
        return controller.createMultiple();
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
    public EntityModel duplicate() throws ServiceClientException {
        return controller.duplicate();
    }

    @Override
    public BatchCopyResult duplicateSelected() {
        return controller.duplicateSelected();
    }

    @Override
    public ExplorerItemView insertEntity() {
        return controller.insertEntity(null);
    }

    @Override
    public ExplorerItemView insertEntity(final EntityModel entity) {
        return controller.insertEntity(entity);
    }        

    @Override
    public ExplorerItemView insertEntityWithReplace() {
        return controller.insertCurrentEntityWithReplace();
    }        

    @Override
    public boolean isAutoInsertEnabled() {
        return controller.isAutoInsertEnabled();
    }

    @Override
    public List<EntityModel> paste() {
        return controller.paste();
    }

    @Override
    public void runEditorDialog() throws ServiceClientException {
        controller.runEditorDialog();
    }

    @Override
    public void setAutoInsertEnabled(boolean enabled) {
        controller.setAutoInsertEnabled(enabled);
    }

    @Override
    public void unblockRedraw() {
    }
    private final static String AUTOINSERT_KEY_NAME = "autoInsert";
    private final static String FILTER_AND_ORDER_KEY_NAME = "filterPanelIsVisible";
    private final static String CUSTOM_TOOLBARS_POSITION = "customToolbarsPosition";

    private void restoreSettings(GroupModel group) {
        WpsSettings settings = env.getConfigStore();
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

    private void storeSettings() {
        WpsSettings settings = env.getConfigStore();
        settings.beginGroup(getModel().getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            settings.writeBoolean(AUTOINSERT_KEY_NAME, isAutoInsertEnabled());
            settings.writeBoolean(FILTER_AND_ORDER_KEY_NAME, getActions().getShowFilterAndOrderToolBarAction().isChecked());
            if (controller.getToolBarsManager() != null) {
                settings.writeBoolean(CUSTOM_TOOLBARS_POSITION, controller.getToolBarsManager().isToolBarsHaveCustomPositions());
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
        //mainWindow.storeSettings();
        if (filtersAndSortingsToolBar != null) {
            filtersAndSortingsToolBar.storeSettings();
        }
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
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

    protected final void openCurrentEntityEditor() {
        controller.openCurrentEntityEditor();
    }

    private void afterChangeEditorSpaceVisibility(final boolean isVisible){
        if (getCurrentEntity() != null){
            if (isVisible && !controller.getCurrentEntityEditor().isOpened()
                    && !getGroupModel().getRestrictions().getIsEditorRestricted()) {
                try {
                    openCurrentEntityEditor();
                } catch (Throwable ex) {
                    splitter.collapse(1);
                    controller.getCurrentEntityEditor().close(true);
                    getEnvironment().processException(ex);
                }
            } else {
                getActions().refresh();
            }
        }
    }
    
    @Override
    protected void processHtmlEvent(final HtmlEvent event) {
        if (event instanceof KeyDownHtmlEvent){
            processKeyDownHtmlEvent((KeyDownHtmlEvent)event);
        }else{
            super.processHtmlEvent(event);
        }
    }    
    
    private void processKeyDownHtmlEvent(final KeyDownHtmlEvent event){
        if (event.getButton()==EKeyEvent.VK_DELETE.getValue() &&
            event.getKeyboardModifiers().isEmpty()){
            if (getActions().getDeleteAllAction().isEnabled()
                && controller.getGroupModel()!=null
                && !controller.getGroupModel().getSelection().isEmpty()){
                getActions().getDeleteAllAction().trigger();
            }else if (getActions().getDeleteAction().isEnabled()){
                getActions().getDeleteAction().trigger();
            }           
        }
    }
}
