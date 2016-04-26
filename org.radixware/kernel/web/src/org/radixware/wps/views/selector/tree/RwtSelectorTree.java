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
package org.radixware.wps.views.selector.tree;

import java.awt.Color;
import java.util.*;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.enums.ESelectorColumnHeaderMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.*;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.SelectorController;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.dialogs.BrokenEntityMessageDialog;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.selector.RwtSelector;
import org.radixware.wps.views.selector.RwtSelectorWidgetDelegate;
import org.radixware.wps.views.selector.SelectorWidgetController;

/**
 * Виджет древовидного селектора
 * (Radix::Web.Widgets.SelectorTree::SelectorTree). Может содержать как
 * {@link SelectorTreeEntityModelNode узлы, отображающие значения свойств модели объекта сущности}
 * так и {@link SelectorTreeNode узлы, содержащие произвольные данные}. В
 * стандартной реализации на первом уровне дерева для каждого объекта сущности,
 * содержащегося в корневой модели группы, создается узел
 * {@link SelectorTreeEntityModelNode}. При развертывании он вызывает методы
 * дерева {@link #hasChildNodes(SelectorTreeNode)} и
 * {@link #initChildren(SelectorTreeNode)} для получения вложенных узлов и т.д.
 * При отображении значений свойств объектов вызывается метод
 * {@link #mapSelectorColumn(SelectorColumnModelItem, SelectorTreeEntityModelNode, GroupModel, EntityModel) mapSelectorColumn}.
 * Общая схема взаимодействия классов в древовидном селекторе при построении
 * древовидной структуры узлов: null {@link SelectorTreeChildNodes} -> {@link RwtSelectorTree} -> {@link SelectorTreeNode}/{@link SelectorTreeEntityModelNode}
 * -> {@link SelectorTreeChildNodes} -> {@link ChildGroupModelSettings} Пример
 * создания и открытия древовидного селектора в обработчике <code>opened</code>
 * диалога кастомного селектора:  <code>
 * GroupView.setSelectorWidget(new SelectorTree(GroupView,this));
 * </code>
 *
 */
public class RwtSelectorTree extends Tree implements ISelectorWidget, IRwtSelectorTree {

    private static class RootNode extends Node.DefaultNode {

        public RootNode(final IRwtSelectorTree tree) {
            super();
            final GroupModel rootGroupModel = tree.getRootGroupModel();
            if (rootGroupModel instanceof ISelectorTreeChildNodesProvider) {
                final List<SelectorTreeNode> childNodes
                        = ((ISelectorTreeChildNodesProvider) rootGroupModel).createChildNodes(tree, null);
                if (childNodes != null) {
                    for (Node node : childNodes) {
                        add(node);
                    }
                }
            } else {
                setChildNodes(new SelectorTreeChildNodes(tree, Collections.<GroupModel>singletonList(rootGroupModel)));
            }
        }
    }
    private final Tree.NodeListener selectionListener = new Tree.NodeListener() {
        @Override
        public void selectionChanged(Node oldSelection, Node newSelection) {
            if (oldSelection != newSelection && newSelection != null) {
                if (newSelection.getUserData() instanceof BrokenEntityModel) {
                    selector.showException(new BrokenEntityObjectException((BrokenEntityModel) newSelection.getUserData()));
                    updateActions();
                } else if (newSelection.getUserData() instanceof EntityModel) {
                    selector.setCurrentEntity((EntityModel) newSelection.getUserData());
                } else {
                    selector.refresh();
                }
                actions.refresh();
            }
        }
    };
    private final Tree.DoubleClickListener doubleClickListener = new Tree.DoubleClickListener() {
        @Override
        public void nodeDoubleClick(Node item) {
            if (item.getUserData() instanceof BrokenEntityModel) {
                new BrokenEntityMessageDialog((WpsEnvironment) getEnvironment(), (BrokenEntityModel) item.getUserData()).execDialog();
            }
        }
    };

    private final static class Icons extends ClientIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon CREATE_CHILD_OBJECT = new Icons("classpath:images/add_child.svg");
        public static final ClientIcon PASTE_CHILD_OBJECT = new Icons("classpath:images/paste_child.svg");
        public static final Icons NEXT = new Icons("classpath:images/next.svg");
        public static final Icons PREV = new Icons("classpath:images/prev.svg");
        public static final Icons BEGIN = new Icons("classpath:images/begin.svg");
        public static final Icons END = new Icons("classpath:images/end.svg");
    }

    //Tree implementation
    private final ISelector selector;
    private final SelectorWidgetController controller;
    private final Map<SelectorColumnModelItem, Column> treeColumn2SelectorColumn = new HashMap<>();
    private final List<Node> expandedNodes = new ArrayList<>();
    private final RwtAction createChildObjectAction;
    private final RwtAction pasteChildObjectAction;
    private final SelectorModelDataLoader allDataLoader;
    private final Tree.HeaderClickListener headerClickListener = new Tree.HeaderClickListener() {
        @Override
        public void onClick(final Column column, final EnumSet<EKeyboardModifier> keyboardModifiers) {
            if (!selector.isDisabled()) {
                final SelectorColumnModelItem columnItem = (SelectorColumnModelItem) column.getUserData();
                if (columnItem != null) {
                    controller.processColumnHeaderClick(columnItem, keyboardModifiers);
                }
            }
        }
    };
    private String createChildObjectCustomTitle;
    private String pasteChildObjectCustomTitle;
    private Node rootNode;
    private boolean binded;

    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };

    /**
     * @param selector Селектор
     * @param rootGroupModel модель группы, которая будет использоваться в
     * качестве корневой
     */
    public RwtSelectorTree(final ISelector selector, final GroupModel rootGroupModel) {
        super();
        setSelectionStyle(EnumSet.of(IGrid.ESelectionStyle.ROW_FRAME,IGrid.ESelectionStyle.CELL_FRAME));
        setBrowserFocusFrameEnabled(false);
        this.selector = selector;
        this.setPersistenceKey("st" + rootGroupModel.getSelectorPresentationDef().getId().toString());
        ((WpsEnvironment) this.getEnvironment()).addSettingsChangeListener(l);
        actions = new Actions((WpsEnvironment) getEnvironment());
        controller = new SelectorWidgetController(rootGroupModel, selector) {

            @Override
            protected void clearWidget() {
                if (rootNode != null) {
                    rootNode.getChildNodes().reset();
                    actions.refresh();
                }
            }

            @Override
            protected void clearRows() {
                RwtSelectorTree.this.clearTree();
            }

            @Override
            public void readMore(final GroupModel model) {//empty implementation                
            }

            @Override
            protected void addRow(final EntityModel entity) {//empty implementation
            }

            @Override
            protected void onContentsChanged() {
            }

            @Override
            public int getRowCount() {
                if (RwtSelectorTree.this.getRootNode() != null) {
                    return RwtSelectorTree.this.getRootNode().getChildCount();//считаем только узлы верхнего уровня
                } else {
                    return -1;
                }
            }

            @Override
            protected void addColumn(final int index, final SelectorColumnModelItem selectorColumn) {
                final Tree.Column treeColumn = RwtSelectorTree.this.addColumn(index, selectorColumn);
                final Stack<Node> nodes = new Stack<>();
                for (int r = 0; r < getRootNode().getChildCount(); r++) {
                    nodes.push(getRootNode().getChildAt(r));
                }
                while (!nodes.isEmpty()) {
                    final Node n = nodes.pop();
                    if (n instanceof SelectorTreeNode) {
                        final EntityModel entity = getEntityImpl((SelectorTreeNode) n);
                        if (entity instanceof BrokenEntityModel == false) {
                            if (n instanceof SelectorTreeEntityModelNode) {
                                ((SelectorTreeEntityModelNode) n).setupCell(index, selectorColumn);
                            } else {
                                final Property property = entity.getProperty(selectorColumn.getPropertyDef().getId());
                                n.setCellValue(treeColumn, property);
                            }
                        }
                    }
                    if (!n.isLeaf()) {
                        for (Node child : n.getChildNodes().getCreatedNodes()) {
                            nodes.push(child);
                        }
                    }
                }
            }

        };

        addSelectionListener(selectionListener);
        addDoubleClickListener(doubleClickListener);
        {
            final String actionTitle
                    = getEnvironment().getMessageProvider().translate("Selector", "Create Child Object...");
            createChildObjectAction = new RwtAction(getEnvironment(), Icons.CREATE_CHILD_OBJECT);
            createChildObjectAction.setToolTip(actionTitle);
            createChildObjectAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    RwtSelectorTree.this.createChildObject();
                }
            });
        }
        {
            final String actionTitle
                    = getEnvironment().getMessageProvider().translate("Selector", "Paste Child Object...");
            pasteChildObjectAction = new RwtAction(getEnvironment(), Icons.PASTE_CHILD_OBJECT);
            pasteChildObjectAction.setToolTip(actionTitle);
            pasteChildObjectAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    RwtSelectorTree.this.pasteChildObject();
                }
            });
        }
        final String confirmMovingToLastObjectMessage
                = getEnvironment().getMessageProvider().translate("RwtSelectorTree", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(getEnvironment(), selector);
        allDataLoader.setDontAskButtonVisibleInConfirmationDialog(false);
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(getEnvironment().getMessageProvider().translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(getEnvironment().getMessageProvider().translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        showHeader(true);//always show header for selector
        applySettings();
    }

    /**
     * Стандартный обработчик изменения выбранного узла дерева. Если у
     * предыдущего выбранного узла метод <code>getUserData()</code> возвращает
     * инстанцию {@link EntityModel}, то возвращается результат работы метода
     * селектора
     * {@link ISelector#leaveCurrentEntity(boolean) leaveCurrentEntity} c
     * параметром <code>false</code>.
     *
     * @param oldSelection выбранный ранее узел дерева. Может быть
     * <code>null</code>
     * @param newSelection текущий выбранный узел дерева
     * @return <code>false</code>, если требуется отменить операцию изменения
     * выбранного узла дерева
     */
    @Override
    protected boolean onChangeSelection(final Node oldSelection, final Node newSelection) {
        if (oldSelection != null && oldSelection.getUserData() instanceof EntityModel
                && oldSelection.getUserData() instanceof BrokenEntityModel == false) {
            return selector.leaveCurrentEntity(false);
        }
        return true;
    }
    public final Actions actions;
    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH
            = SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    private final static String ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH
            = SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_RESTORING_POSITION;

    public final class Actions {
        //эти кнопки активны только при переходе по узлам верхнего уровня

        public final RwtAction prevAction;
        public final RwtAction nextAction;
        public final RwtAction beginAction;
        public final RwtAction endAction;

        public Actions(final WpsEnvironment environment) {
            final MessageProvider mp = environment.getMessageProvider();
            Action.ActionListener nextListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectNextRow();
                }
            };
            nextAction = createAction(environment, Icons.NEXT, mp.translate("RwtSelectorTree", "Next"), nextListener);

            Action.ActionListener prevListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectPrevRow();
                }
            };
            prevAction = createAction(environment, Icons.PREV, mp.translate("RwtSelectorTree", "Previous"), prevListener);

            Action.ActionListener beginListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectFirstRow();
                }
            };
            beginAction = createAction(environment, Icons.BEGIN, mp.translate("RwtSelectorTree", "First"), beginListener);

            Action.ActionListener endListener = new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    selectLastRow();
                }
            };
            endAction = createAction(environment, Icons.END, mp.translate("RwtSelectorTree", "Last"), endListener);
        }

        private RwtAction createAction(final WpsEnvironment environment,
                final ClientIcon icon,
                final String title,
                final Action.ActionListener listener) {
            final RwtAction action
                    = new RwtAction(environment, icon);
            action.setText(title);
            action.setToolTip(title);
            action.addActionListener(listener);
            action.setEnabled(false);
            return action;
        }

        public boolean currentEntityDefined() {
            if (selector.getCurrentEntity() == null && !selector.getGroupModel().isEmpty()) {
                final int currentIndex = rootNode.indexOfChild(getSelectedNode());
                return currentIndex > -1 && selector.getGroupModel().isBrokenEntity(currentIndex);
            } else {
                return true;
            }
        }

        public void refresh() {
            final boolean currentEntityDefined = currentEntityDefined();
            final int rows = rootNode.getChildCount();
            final int cur = rootNode.indexOfChild(getSelectedNode()) > -1 && currentEntityDefined ? rootNode.indexOfChild(getSelectedNode()) : -1;
            prevAction.setEnabled(true);
            beginAction.setEnabled(true);
            nextAction.setEnabled(true);
            endAction.setEnabled(true);
            if (cur == -1 || cur == 0) {
                prevAction.setEnabled(false);
                beginAction.setEnabled(false);
            }
            if (cur == -1 || cur == rows - 1) {
                nextAction.setEnabled(false);
                endAction.setEnabled(false);
            }
        }

        public void close() {
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
        }
    }

    public void selectNextRow() {
        if (getSelectedNode() != null && rootNode.getChildCount() > 0) {
            if (rootNode.indexOfChild(getSelectedNode()) + 1 < rootNode.getChildCount()) {
                Node next = rootNode.getChildAt(rootNode.indexOfChild(getSelectedNode()) + 1);
                setSelectedNode(next);
            }
        }
    }

    public void selectPrevRow() {
        if (getSelectedNode() != null && rootNode.getChildCount() > 0) {
            if (rootNode.indexOfChild(getSelectedNode()) > 0) {
                Node prev = rootNode.getChildAt(rootNode.indexOfChild(getSelectedNode()) - 1);
                setSelectedNode(prev);
            }
        }
    }

    public void selectFirstRow() {
        if (rootNode.getChildCount() > 0) {
            setSelectedNode(rootNode.getChildAt(0));
        }
    }

    public void selectLastRow() {
        final int rowsLoadingLimit
                = selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
        allDataLoader.setLoadingLimit(rowsLoadingLimit);
        allDataLoader.resetCounters();
        int loadedRows = -1;
        try {
            try {
                loadedRows = allDataLoader.loadAll(new RwtSelectorWidgetDelegate(controller, selector.getGroupModel()));

            } catch (InterruptedException exception) {
                loadedRows = selector.getGroupModel().getEntitiesCount();

            } catch (ServiceClientException exception) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                getEnvironment().getTracer().error(title, exception);
                selector.getGroupModel().showException(title, exception);
                loadedRows = -1;
            }
            if (loadedRows > 0 && selector.leaveCurrentEntity(false)) {
                Node lastNode = rootNode.getChildAt(loadedRows - 1);
                setSelectedNode(lastNode);
            }
        } finally {
            if (loadedRows > 0) {
            }
        }
    }

    @Override
    protected List<ColumnDescriptor> getVisibleColumnDescriptors(final List<ColumnDescriptor> all) {
        return controller.getVisibleColumnDescriptors(this, all);
    }

    @Override
    protected List<ColumnDescriptor> getAllColumnDescriptors() {
        return controller.getAllColumnDescriptors();
    }

    @Override
    protected void updateColumnsVisibility(List<ColumnDescriptor> visible) {
        controller.updateColumnsVisibility(this, visible);
    }

    private Column addColumn(final int index, final SelectorColumnModelItem item) {
        final ESelectorColumnHeaderMode headerMode = item.getHeaderMode();
        final String headerTitle = headerMode == ESelectorColumnHeaderMode.ONLY_ICON ? "" : item.getTitle();
        final Column c = addColumn(index, headerTitle);
        updateColumnIconAndToolTip(c, item);
        c.setUserData(item);
        c.setPersistenceKey(item.getId().toString());
        return c;
    }

    private EntityModel getEntityImpl(SelectorTreeNode node) {
        return node == null ? null : findNearestEntityModel(node);
    }

    public EntityModel getEntity(Node node) {
        if (node instanceof SelectorTreeNode) {
            return findNearestEntityModel((SelectorTreeNode) node);
        } else {
            return null;
        }
    }

    //IModelWidget implementation    
    /**
     * Открытие древовидного селектора. Реализация метода интерфейса
     * {@link IModelWidget}. В этом методе сначала производится настройка
     * заголовков колонок дерева. Затем, если метод селектора
     * <code>isDisabled</code> возвращает <code>false</code>, то производится
     * установка корневого узла, созданного в методе
     * {@link #createRootNode() createRootNode}.
     */
    @Override
    public final void bind() {
        if (binded) {
            return;
        }
        binded = true;
        for (SelectorColumnModelItem item : controller.getSelectorColumns()) {
            if (item.isVisible()) {
                final Column column;
                final ESelectorColumnHeaderMode headerMode = item.getHeaderMode();
                final String headerTitle = headerMode == ESelectorColumnHeaderMode.ONLY_ICON ? "" : item.getTitle();
                if (treeColumn2SelectorColumn.isEmpty()) {
                    column = getTreeColumn();
                    column.setTitle(headerTitle);
                } else {
                    column = addColumn(headerTitle);
                }
                treeColumn2SelectorColumn.put(item, column);
                updateColumnIconAndToolTip(column, item);
                column.setPersistenceKey(item.getId().toString());
                column.setUserData(item);
                item.subscribe(this);
            }
        }
        setRootVisible(false);
        if (!selector.isDisabled()) {
            rootNode = createRootNode();
            setRootNode(rootNode);
        }
        addHeaderClickListener(headerClickListener);
        controller.updateSortingIndicators(this);
        controller.updateColumnsSizePolicy(this);
        updateActions();
    }

    private void updateColumnIconAndToolTip(final Tree.Column treeColumn, final SelectorColumnModelItem selectorColumn) {
        final ESelectorColumnHeaderMode headerMode = selectorColumn.getHeaderMode();
        if (headerMode == ESelectorColumnHeaderMode.ONLY_TEXT) {
            treeColumn.setIcon(null);
        } else {
            treeColumn.setIcon((WpsIcon) selectorColumn.getHeaderIcon());
        }
        final String toolTip = selectorColumn.getHint();
        if (headerMode == ESelectorColumnHeaderMode.ONLY_ICON && (toolTip == null || toolTip.isEmpty())) {
            treeColumn.setToolTip(selectorColumn.getTitle());
        } else {
            treeColumn.setToolTip(toolTip);
        }
    }

    @Override
    public final void refresh(final ModelItem modelItem) {
        if (modelItem instanceof SelectorColumnModelItem) {
            final SelectorColumnModelItem selectorColumn = (SelectorColumnModelItem) modelItem;
            final Tree.Column treeColumn = treeColumn2SelectorColumn.get(selectorColumn);
            final ESelectorColumnHeaderMode headerMode = selectorColumn.getHeaderMode();
            if (headerMode == ESelectorColumnHeaderMode.ONLY_ICON) {
                treeColumn.setTitle("");
            } else {
                treeColumn.setTitle(selectorColumn.getTitle());
            }
            updateColumnIconAndToolTip(treeColumn, selectorColumn);
            controller.updateColumnsSizePolicy(this);
        }
        updateActions();
    }

    @Override
    public final boolean setFocus(final Property property) {
        return false;
    }

    //ISelectorWidget implementation
    @Override
    public void setupSelectorMenu(final IMenu menu) {//empty implementation
    }

    @Override
    public void setupSelectorToolBar(final IToolBar toolBar) {
        if (toolBar != null) {
            toolBar.insertAction(selector.getActions().getDeleteAllAction(), createChildObjectAction);
            toolBar.insertAction(selector.getActions().getViewAuditLogAction(), pasteChildObjectAction);

            final Action createAction = selector.getActions().getCreateAction();

            toolBar.insertAction(createAction, actions.beginAction);
            toolBar.insertAction(createAction, actions.prevAction);
            toolBar.insertAction(createAction, actions.nextAction);
            toolBar.insertAction(createAction, actions.endAction);
        }
    }

    /**
     * Обработчик события создания объекта сущности в древовидном селекторе.
     * Метод вызывается при выполнении операции создания узла дерева,
     * ассоциированного с моделью объекта сущности после создания инстанции
     * <code>EntityModel</code>, но до показа стандартного диалога создания
     * объекта сущности. Может использоваться для установки начальных значений
     * свойствам объекта сущности. Стандартная реализация вызывает метод
     * {@link SelectorTreeNode#afterPrepareCreateChildObject(EntityModel) afterPrepareCreateChildObject}
     * у текущего узла дерева или у узла верхнего уровня в зависимости от того
     * на каком уровне происходит создание.
     *
     * @param newObject новая инстанция модели сущности
     */
    @Override
    public void afterPrepareCreate(final EntityModel newObject) {
        final Node current = getSelectedNode();
        if (current != null && newObject.getContext() instanceof IContext.InSelectorCreating) {
            final GroupModel contextGroup = ((IContext.InSelectorCreating) newObject.getContext()).group;
            final Node parentNode;
            if (contextGroup == selector.getGroupModel()) {
                //создание сущности в текущей группе
                parentNode = current.getParentNode();
            } else {
                //создание сущности в дочерней группе
                parentNode = current;
            }
            if (parentNode instanceof SelectorTreeNode) {
                ((SelectorTreeNode) parentNode).afterPrepareCreateChildObject(newObject);
            }
        }
    }

    @Override
    public final void clear() {
        if (rootNode != null) {
            expandedNodes.clear();
            rootNode.getChildNodes().reset();
            setEnabled(false);
        }
        if (actions != null) {
            actions.close();
        }
    }

    @Override
    public final void entityRemoved(final Pid pid) {
        final Node currentNode = getSelectedNode();
        final Node parentNode = currentNode == null ? null : currentNode.getParentNode();
        final Node.Children branch = parentNode == null ? null : parentNode.getChildNodes();
        if (branch instanceof SelectorTreeChildNodes) {
            final List<Node> nodesToRemove = ((SelectorTreeChildNodes) branch).findNodes(pid);
            int currentRow;
            if (nodesToRemove.contains(currentNode)) {
                selector.getModel().finishEdit();
                currentRow = branch.getNodes().indexOf(currentNode);
            } else {
                currentRow = -1;
            }
            for (Node node : nodesToRemove) {
                //update expanded nodes list
                for (int i = expandedNodes.size() - 1; i >= 0; i--) {
                    if (node == expandedNodes.get(i) || node.isParentFor(expandedNodes.get(i))) {
                        expandedNodes.remove(i);
                    }
                }
                node.remove();
            }
            if (currentRow > -1) {//calc new selected node
                selector.leaveCurrentEntity(true);
                if (parentNode.isLeaf()) {//last node in branch was removed                    
                    if (parentNode == rootNode) {//tree is empty
                        setSelectedNode(null);
                        selector.refresh();
                    } else {
                        setSelectedNode(parentNode);
                    }
                } else {
                    final Node newSelectection
                            = parentNode.getChildAt(Math.min(currentRow, parentNode.getChildCount() - 1));
                    setSelectedNode(newSelectection);
                }
            }
            actions.refresh();
        }
    }

    @Override
    public final void finishEdit() {
        if (getSelectedNode() != null) {
            getSelectedNode().finishEdit();
        }
    }

    @Override
    public final void lockInput() {//empty implementation        
    }

    @Override
    public final void unlockInput() {//empty implementation        
    }

    @Override
    public void reread() throws InterruptedException, ServiceClientException {
        if (rootNode != null) {
            expandedNodes.clear();
            setSelectedNode(null);//to unsubscribe properties from renderer            
            rootNode.getChildNodes().reset();
        }
        rootNode = createRootNode();
        setRootNode(rootNode);

        rootNode.getChildNodes().getNodes();
        controller.updateSortingIndicators(this);
        selector.refresh();
        setEnabled(true);
    }

    public void storeHeaderSettings() {
        controller.storeHeaderSettings(this);
    }

    public void restoreHeaderSettings() {
        controller.restoreHeaderSettings(this);
    }

    @Override
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        final Node current = getSelectedNode();
        if (pid == null || current == null
                || current.getUserData() instanceof EntityModel == false
                || current.getParentNode() instanceof SelectorTreeNode == false) {
            reread(null, pid);
        } else {
            reread((SelectorTreeNode) current.getParentNode(), pid);
        }
    }

    /**
     * Перечитывание подузлов текущего узла. Если текущий узел является
     * инстанцией {@link SelectorTreeNode}, то вызов этого метода приведет к
     * пересозданию его подузлов.
     *
     * @throws InterruptedException генерируется если операция перечитывания
     * была прервана
     * @throws ServiceClientException генерируется при возникновении ошибок
     * связанных с взаимодействием с сервером
     */
    public void rereadChildrenForCurrentEntity() throws InterruptedException, ServiceClientException {
        final Node current = getSelectedNode();
        if (current instanceof SelectorTreeNode) {
            reread((SelectorTreeNode) current, null);
        }
    }

    private void reread(final SelectorTreeNode parentNode, final Pid pid) {
        final boolean wasSelection;
        final NodePath currentNodePath;
        final List<NodePath> nodesToExpand = new LinkedList<>();
        {//Сохранение текущего элемента
            final Node current = getSelectedNode();
            wasSelection = current != null;
            setSelectedNode(null);//to unsubscribe properties from renderer

            if (pid != null) {
                if (parentNode == null) {
                    currentNodePath = NodePath.fromPid(pid);
                } else {
                    currentNodePath
                            = NodePath.concatinate(NodePath.fromNode(parentNode, getRootNode()), NodePath.fromPid(pid));
                }
            } else if (current != null && parentNode == null) {
                currentNodePath = NodePath.fromNode(current, getRootNode());
            } else {
                currentNodePath = null;
            }
        }
        {//Сохранение раскрытых элементов               
            for (int i = expandedNodes.size() - 1; i >= 0; i--) {
                if (parentNode == null || parentNode.isParentFor(expandedNodes.get(i))) {
                    nodesToExpand.add(NodePath.fromNode(expandedNodes.get(i), parentNode == null ? getRootNode() : parentNode));
                    expandedNodes.remove(i);
                }
            }
        }

        final Node parent;
        if (parentNode == null) {
            if (rootNode.isLeaf()) {//tree is empty
                getRootGroupModel().reset();
            } else {
                rootNode.getChildNodes().reset();
            }
            rootNode = createRootNode();
            setRootNode(rootNode);
            parent = rootNode;
        } else {
            final EntityModel nearestEntityModel = findNearestEntityModel(parentNode);
            try {
                nearestEntityModel.read();
            } catch (InterruptedException ex) {
                return;
            } catch (Exception exception) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't read entity object \'%s\'");
                getEnvironment().getTracer().error(String.format(title, nearestEntityModel.getTitle()), exception);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, nearestEntityModel.getTitle()) + ":\n"
                        + ClientException.exceptionStackToString(exception),
                        EEventSource.EXPLORER);
                return;
            }
            parentNode.rereadChildren();
            parent = parentNode;
        }
        if (currentNodePath == null) {
            parent.expand();
        }
        if (wasSelection || currentNodePath != null) {//Восстановление текущего элемента        
            final Node current = currentNodePath == null ? null : currentNodePath.findNode(getRootNode(), true);
            if (current != null && current != getRootNode()) {
                setSelectedNode(current);
            } else if (parent.getChildCount() > 0) {
                setSelectedNode(parent.getChildAt(0));
            } else if (parent != getRootNode()) {
                setSelectedNode(parent);
            }
        }
        if (!nodesToExpand.isEmpty()) {//Восстановление раскрытых элементов
            Node expandedNode;
            for (NodePath nodePath : nodesToExpand) {
                expandedNode = nodePath.findNode(parentNode == null ? getRootNode() : parentNode, false);
                if (expandedNode != null) {
                    expandedNode.expand();
                }
            }
        }
        controller.updateSortingIndicators(this);
    }

    /**
     * Выполнение операции по созданию подобъекта для текущего объекта сущности.
     * В стандартной реализации для создания нового объекта используется модель
     * группы, которую возвращает метод
     * {@link SelectorTreeNode#getGroupModelToCreateChildObject() getGroupModelToCreateChildObject}
     * у текущего узла {@link SelectorTreeNode}. Если текущий узел не является
     * инстанцией {@link SelectorTreeNode}, то метод возвращает
     * <code>null</code>. После создания инстанции {@link EntityModel}
     * вызывается обработчик
     * {@link #afterPrepareCreate(EntityModel) afterPrepareCreate}, затем
     * показывается стандартный диалог создания объекта. В случае успешного
     * завершения операции создания происходит перечитывание дочерних узлов и
     * если у корневой модели группы в презентации селектора установлена
     * настройка "восстанавливать позицию", то будет выполнен переход к узлу
     * дерева, который соответствует созданному объекту сущности.
     *
     * @return новый дочерний объект сущности
     */
    public EntityModel createChildObject() {
        final Node current = getSelectedNode();
        if (current instanceof SelectorTreeNode) {
            try {
                final GroupModel group = ((SelectorTreeNode) current).getGroupModelToCreateChildObject();
                if (group != null && selector.canChangeCurrentEntity(false)) {
                    final EntityModel result = SelectorController.createEntity(group, null, this);
                    if (result != null) {
                        if (group.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                            reread((SelectorTreeNode) current, result.getPid());
                        } else {
                            reread((SelectorTreeNode) current, null);
                        }
                        ((RwtSelector) selector).notifyEntityObjectsCreated(Collections.singletonList(result));
                    }
                    return result;
                }
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create entity"), ex);
            }
        }
        return null;
    }

    /**
     * Выполнение операции вставки подобъекта(ов) из буфера обмена. В
     * стандартной реализации для создания нового объекта используется модель
     * группы, которую возвращает метод
     * {@link SelectorTreeNode#getGroupModelToCreateChildObject() getGroupModelToCreateChildObject}
     * у текущего узла {@link SelectorTreeNode}. Если текущий узел не является
     * инстанцией {@link SelectorTreeNode}, то вставка не выполняется. После
     * создания инстанции {@link EntityModel} вызывается обработчик
     * {@link #afterPrepareCreate(EntityModel) afterPrepareCreate}, затем
     * показывается стандартный диалог создания объекта. После выполнения
     * вставки происходит перечитывание дочерних узлов. Если был вставлен один
     * объект и у корневой модели группы в презентации селектора установлена
     * настройка "восстанавливать позицию", то будет выполнен переход к узлу
     * дерева, который соответствует созданному объекту сущности.
     */
    public void pasteChildObject() {
        final Node current = getSelectedNode();
        if (current instanceof SelectorTreeNode) {
            try {
                final GroupModel groupModel = ((SelectorTreeNode) current).getGroupModelToCreateChildObject();
                if (groupModel != null && selector.canChangeCurrentEntity(false)) {
                    final List<EntityModel> result = SelectorController.paste(groupModel, this);
                    if (result.size() == 1
                            && groupModel.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                        final Pid pid = result.get(0).getPid();
                        reread((SelectorTreeNode) current, pid);
                    } else if (result.size() > 0) {
                        reread((SelectorTreeNode) current, null);
                    }
                    if (!result.isEmpty()) {
                        ((RwtSelector) selector).notifyEntityObjectsCreated(result);
                    }
                }
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create object"), ex);
            }
        }
    }

    private DialogResult createEntityImpl(final EntityModel entity) throws ServiceClientException, InterruptedException {
        if (entity.getCustomViewId() != null
                && entity.getEditorPresentationDef().getEditorPages().getTopLevelPages().isEmpty()) {
            //RADIX-2567
            try {
                return entity.create() == EEntityCreationResult.SUCCESS ? DialogResult.ACCEPTED : DialogResult.REJECTED;
            } catch (Exception exception) {
                final String errorMessageTitle;
                if (entity.getSrcPid() != null) {
                    errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to create copy");
                } else {
                    errorMessageTitle = getEnvironment().getMessageProvider().translate("Editor", "Failed to create entity");
                }
                entity.showException(errorMessageTitle, exception);
                return DialogResult.REJECTED;
            }
        }
        final IEntityEditorDialog dialog
                = getEnvironment().getApplication().getStandardViewsFactory().newEntityEditorDialog(entity);
        return dialog.execDialog();
    }

    private void updateActions() {
        final Node currentNode = getSelectedNode();
        final GroupModel childGroup;
        if (rootNode != null && currentNode instanceof SelectorTreeNode) {
            childGroup = ((SelectorTreeNode) currentNode).getGroupModelToCreateChildObject();
        } else {
            childGroup = null;
        }
        final boolean canCreateChildObject
                = isEnabled() && childGroup != null && !childGroup.getRestrictions().getIsCreateRestricted();
        createChildObjectAction.setEnabled(canCreateChildObject);
        createChildObjectAction.setToolTip(getTitleOfCreateChildObjectActionImpl(childGroup));
        final Clipboard clipboard = getEnvironment().getClipboard();
        final boolean canPasteChildObject
                = canCreateChildObject && clipboard.size() > 0 && clipboard.isCompatibleWith(childGroup);
        pasteChildObjectAction.setEnabled(canPasteChildObject);
        pasteChildObjectAction.setToolTip(getTitleOfPasteChildObjectActionImpl(childGroup, clipboard));
    }

    /**
     * Создание корневого узла дерева. Вызывается в методах <code>bind</code> и
     * <code>reread</code> (при открытии селектора и перечитывании). Стандартная
     * реализация в качестве набора узлов первого уровня использует инстанцию
     * класса {@link SelectorTreeChildNodes}.
     *
     * @return узел, который будет установлен в качестве корневого
     */
    protected Node createRootNode() {
        return new RootNode(this);
    }

    //ISelectorTreeModel implementation
    @Override
    public final GroupModel getRootGroupModel() {
        return controller.getModel();
    }

    @Override
    public final List<SelectorColumnModelItem> getSelectorColumns() {
        return controller.getSelectorColumns();
    }

    /**
     * Получение информации о наличии дочерних узлов. Стандартная реализация
     * метода интерфейса {@link IRwtSelectorTree}, которая возвращает результат
     * работы метода
     * {@link SelectorTreeNode#hasChildNodes() parentNode.hasChildNodes()}.
     *
     * @return <code>false</code>, если для переданного узла не существует
     * дочерних узлов, иначе <code>true</code>
     * @see IRwtSelectorTree#hasChildNodes(SelectorTreeNode)
     */
    @Override
    public boolean hasChildNodes(final SelectorTreeNode parentNode) {
        return parentNode.hasChildNodes();
    }

    private static GroupModel findGroupModel(final SelectorTreeNode node) {
        if (node.getUserData() instanceof EntityModel) {
            final EntityModel entityModel = (EntityModel) node.getUserData();
            if (entityModel.getContext() instanceof IContext.SelectorRow) {
                return ((IContext.SelectorRow) entityModel.getContext()).parentGroupModel;
            }
        }
        return null;
    }

    private static EntityModel findNearestEntityModel(final SelectorTreeNode treeNode) {
        for (Node node = treeNode; node != null; node = node.getParentNode()) {
            if (node.getUserData() instanceof EntityModel) {
                return (EntityModel) node.getUserData();
            }
        }
        return null;
    }

    /**
     * Настройка подузлов дерева. Стандартная реализация метода интерфейса
     * {@link IRwtSelectorTree}. Метод устанавливает заданному родительскому
     * узлу набор дочерних узлов по следующему алгоритму: Если инстанция
     * {@link EntityModel}, соответствующая родительскому узлу, или инстанция
     * {@link GroupModel}, которая ее содержит реализует интерфейс
     * {@link ISelectorTreeChildNodesProvider}, то для такой инстанции
     * вызывается метод
     * {@link ISelectorTreeChildNodesProvider#createChildNodes(IRwtSelectorTree, Node) createChildNodes}.
     * Если полученный список узлов не равен <code>null</code>, узлы из него
     * устанавливаются в качестве дочерних. Если таким образом получить список
     * не удалось, то метод установит в качестве дочернего набора узлов новую
     * инстанцию класса {@link SelectorTreeChildNodes}. В качестве параметров
     * конструктора ей будут переданы модель сущности, ассоциированная с
     * переданным узлом дерева или с ближайшем узлом верхнего уровня, и
     * результат вызова метода
     * {@link #getChildGroupModelSettings(SelectorTreeNode) getChildGroupModelSettings}.
     *
     * @param parentNode узел в дереве селектора
     * @see IRwtSelectorTree#initChildren(SelectorTreeNode)
     */
    @Override
    public void initChildren(final SelectorTreeNode parentNode) {
        List<SelectorTreeNode> children = null;
        if (parentNode.getUserData() instanceof ISelectorTreeChildNodesProvider) {
            children
                    = ((ISelectorTreeChildNodesProvider) parentNode.getUserData()).createChildNodes(this, parentNode);
        }
        if (children == null) {
            final GroupModel groupModel = findGroupModel(parentNode);
            if (groupModel instanceof ISelectorTreeChildNodesProvider) {
                children
                        = ((ISelectorTreeChildNodesProvider) groupModel).createChildNodes(this, parentNode);
            }
        }
        if (children == null) {
            final EntityModel nearestEntityModel = findNearestEntityModel(parentNode);
            if (nearestEntityModel != null) {
                parentNode.setChildNodes(new SelectorTreeChildNodes(nearestEntityModel, getChildGroupModelSettings(parentNode)));
            }
        } else {
            for (Node node : children) {
                parentNode.add(node);
            }
        }
    }

    /**
     * Получение параметров создания дочерних моделей групп. Для переданного
     * узла объекта сущности метод возвращает список с параметрами создания
     * дочерних моделей групп. Модели группы, созданные по этим параметрам,
     * будут использованы для генерации узлов дерева следующего уровня.
     * Стандартная реализация создает список настроек по следующему алгоритму: У
     * переданного узла дерева берется ассоциированная с ним инстанция
     * {@link EntityModel} (если ассоциированной инстанции нет, то берется из
     * ближайшего узла верхнего уровня). Затем если эта инстанция
     * {@link EntityModel}, или инстанция {@link GroupModel}, которая ее
     * содержит реализует интерфейс {@link IChildGroupModelSettingsProvider}, то
     * для такой инстанции вызывается метод {@link IChildGroupModelSettingsProvider#getChildGroupModelSettings(Id, SelectorTreeNode, EntityModel).
     * Если полученный список настроек не равен
     * <code>null</code>, то он возвращается в качестве результата работы
     * метода. Если таким образом получить набор не удалось, то метод возвращает
     * <code>null</code>.
     *
     * @param parentNode узел в дереве селектора
     * @return список настроек дочерних моделей групп. Может быть
     * <code>null</code>
     */
    protected List<ChildGroupModelSettings> getChildGroupModelSettings(final SelectorTreeNode parentNode) {
        final EntityModel nearestEntityModel = findNearestEntityModel(parentNode);
        if (nearestEntityModel != null) {
            final Id selectorPresentationId = getRootGroupModel().getDefinition().getId();
            List<ChildGroupModelSettings> settings = null;
            if (nearestEntityModel instanceof IChildGroupModelSettingsProvider) {
                settings
                        = ((IChildGroupModelSettingsProvider) nearestEntityModel).getChildGroupModelSettings(selectorPresentationId, parentNode, nearestEntityModel);
            }
            if (settings == null && nearestEntityModel.getContext() instanceof IContext.SelectorRow) {
                final GroupModel nearestGroupModel
                        = ((IContext.SelectorRow) nearestEntityModel.getContext()).parentGroupModel;
                if (nearestGroupModel instanceof IChildGroupModelSettingsProvider) {
                    settings
                            = ((IChildGroupModelSettingsProvider) nearestGroupModel).getChildGroupModelSettings(selectorPresentationId, parentNode, nearestEntityModel);
                }
            }
            return settings;
        }
        return null;
    }

    /**
     * Создание дочерних моделей групп. Реализация метода интерфейса
     * {@link IRwtSelectorTree}. Стандартная реализация проверяет является ли
     * переданный узел инстанцией {@link SelectorTreeEntityModelNode} и если
     * является, то возвращает результат работы метода
     * {@link SelectorTreeEntityModelNode#createChildGroupModels() createChildGroupModels},
     * иначе проверяет является ли набор подузлов инстанцией
     * {@link SelectorTreeChildNodes} и если является, то возвращает список
     * групп, созданный по настройкам взятым из метода
     * {@link SelectorTreeChildNodes#getChildGroupModelSettings() getChildGroupModelSettings},
     * иначе метод возвращает пустой список.
     *
     * @param parentNode узел в дереве селектора
     * @return список дочерних моделей групп
     * @throws InterruptedException генерируется при отмене операции получения
     * списка дочерних моделей групп
     * @throws ServiceClientException генерируется при возникновении ошибок,
     * связанных с получением дочерних моделей групп
     * @see IRwtSelectorTree#createChildGroupModels(SelectorTreeNode)
     */
    @Override
    public List<GroupModel> createChildGroupModels(final SelectorTreeNode parentNode, final EntityModel parentEntityModel) throws InterruptedException, ServiceClientException {
        if (parentNode instanceof SelectorTreeEntityModelNode) {
            return ((SelectorTreeEntityModelNode) parentNode).createChildGroupModels();
        } else if (parentNode.getChildNodes() instanceof SelectorTreeChildNodes) {
            final List<ChildGroupModelSettings> settings
                    = ((SelectorTreeChildNodes) parentNode.getChildNodes()).getChildGroupModelSettings();
            return ChildGroupModelSettings.createChildGroupModels(settings, parentEntityModel);
        } else {
            return Collections.<GroupModel>emptyList();
        }
    }

    /**
     * Сопоставление колонки селектора свойству из модели сущности. Стандартная
     * реализация метода интерфейса {@link IRwtSelectorTree}. Стандартная
     * реализация возвращает результат работы метода
     * {@link SelectorTreeNode#mapSelectorColumnToChildProperty(EntityModel, SelectorColumnModelItem) parentNode.mapSelectorColumnToChildProperty},
     * если <code>parentNode</code> не равен <code>null</code> и
     * <code>column.getId()</code> в противном случае.
     *
     * @param column колонка селектора
     * @param parentNode родительский узел в дереве селектора. Для узлов первого
     * уровня равен <code>null</code>.
     * @param childGroupModel модель группы, которая содержит модель сущности
     * @param childEntityModel модель сущности-владельца свойства
     * @return идентификатор свойства в модели сущности, соответствующего
     * переданной колонки селектора
     * @see IRwtSelectorTree#mapSelectorColumn(SelectorColumnModelItem,
     * SelectorTreeNode, GroupModel, EntityModel)
     */
    @Override
    public Id mapSelectorColumn(final SelectorColumnModelItem column, final SelectorTreeNode parentNode, final GroupModel childGroupModel, final EntityModel childEntityModel) {
        return parentNode == null ? column.getId() : parentNode.mapSelectorColumnToChildProperty(childEntityModel, column);
    }

    /**
     * Получение пиктограммы для узла дерева. Стандартная реализация метода
     * интерфейса {@link IRwtSelectorTree}. Если метод
     * <code>getUserData()</code> у переданного узла <code>childNode</code>
     * возвращает инстанцию {@link EntityModel}, то для получения пиктограммы
     * вызывается метод
     * {@link #getChildEntityModelIcon(SelectorTreeNode, EntityModel) getChildEntityModelIcon}.
     * Если полученная пиктограмма не равена <code>null</code>, то она
     * возвращается в качестве результата. Если таким образом получить
     * пиктограмму не удалось, то возвращается результат работы метода
     * {@link SelectorTreeNode#getChildNodeIcon(Node) parentNode.getChildNodeIcon}.
     *
     * @param parentNode родительский узел в дереве селектора. Может быть
     * <code>null</code>.
     * @param childNode узел, которому нужно сопоставить пиктограмму
     * @return пиктограмма для узла <code>childNode</code>
     * @see IRwtSelectorTree#getNodeIcon(SelectorTreeNode, Node)
     */
    @Override
    public Icon getNodeIcon(final SelectorTreeNode parentNode, final Node childNode) {
        if (childNode.getUserData() instanceof EntityModel) {
            final Icon result = getChildEntityModelIcon(parentNode, (EntityModel) childNode.getUserData());
            if (result != null) {
                return result;
            }
        }
        return parentNode == null ? null : parentNode.getChildNodeIcon(childNode);
    }

    /**
     * Получение пиктограммы для узла дерева. Метод возвращает пиктограмму,
     * соответствующую модели сущности, для показа в первой колонке дерева.
     * Стандартная реализация возврашает <code>null</code>.
     *
     * @param parentNode родительский узел в дереве селектора. Может быть
     * <code>null</code>.
     * @param entityModel дочерняя модель сущности
     * @return пиктограмма для узла дерева
     */
    protected Icon getChildEntityModelIcon(final SelectorTreeNode parentNode, final EntityModel entityModel) {//?
        return null;
    }

    @Override
    public void onNodeStateChanged(Node node) {
        if (node.isExpanded()) {
            expandedNodes.add(node);
        } else {
            expandedNodes.remove(node);
        }
    }

    private String getTitleOfCreateChildObjectActionImpl(final GroupModel groupModel) {
        if (createChildObjectCustomTitle == null || createChildObjectCustomTitle.isEmpty()) {
            if (groupModel == null || !groupModel.getSelectorPresentationDef().getClassPresentation().hasObjectTitle()) {
                return getEnvironment().getMessageProvider().translate("Selector", "Create Child Object...");
            } else {
                final String childGroupTitle
                        = groupModel.getSelectorPresentationDef().getClassPresentation().getObjectTitle();
                final String actionTitle
                        = String.format(getEnvironment().getMessageProvider().translate("Selector", "Create subobject \"%s\"..."), childGroupTitle);
                return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), actionTitle);
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), createChildObjectCustomTitle);
        }
    }

    /**
     * Получение текущего заголовка действия создания вложенного элемента. Метод
     * возвращает текст всплывающей подсказки у кнопки создания элемента
     * нижележащего уровня.
     *
     * @return заголовок действия создания вложенного элемента
     */
    public final String getTitleOfCreateChildObjectAction() {
        final Node currentNode = getSelectedNode();
        final GroupModel childGroup;
        if (rootNode != null && currentNode instanceof SelectorTreeNode) {
            childGroup = ((SelectorTreeNode) currentNode).getGroupModelToCreateChildObject();
        } else {
            childGroup = null;
        }
        return getTitleOfCreateChildObjectActionImpl(childGroup);
    }

    /**
     * Изменение заголовка действия создания вложенного элемента. Метод
     * позволяет изменить текст всплывающей подсказки у кнопки создания элемента
     * нижележащего уровня.
     *
     * @param title новый заголовок действия создания вложенного элемента. Если
     * значение * * * * * * * * * равно <code>null</code>, или пустая строка, то
     * будет использоваться стандартный заголовок.
     */
    public final void setTitleOfCreateChildObjectAction(final String title) {
        createChildObjectCustomTitle = title;
        updateActions();
    }

    private String getTitleOfPasteChildObjectActionImpl(final GroupModel childGroup, final Clipboard clipboard) {
        if (pasteChildObjectCustomTitle == null || pasteChildObjectCustomTitle.isEmpty()) {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            if (childGroup == null || clipboard.size() == 0) {
                return mp.translate("Selector", "Paste");
            } else if (clipboard.size() == 1) {
                final String title = clipboard.iterator().next().getTitle();
                final String toolTipFormat = mp.translate("Selector", "Paste Child Object '%1$s'...");
                final String toolTip = String.format(toolTipFormat, title);
                return toolTip.length() > 100 ? mp.translate("Selector", "Paste Child Object...") : toolTip;
            } else {
                return mp.translate("Selector", "Paste Child Objects...");
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), pasteChildObjectCustomTitle);
        }
    }

    /**
     * Получение текущего заголовка действия вставки подобъекта(ов) из буфера
     * обмена. Метод возвращает текст всплывающей подсказки у кнопки вставки
     * объекта нижележащего уровня.
     *
     * @return заголовок действия вставки подобъекта(ов) из буфера обмена
     */
    public final String getTitleOfPasteChildObjectAction() {
        final Node currentNode = getSelectedNode();
        final GroupModel childGroup;
        if (rootNode != null && currentNode instanceof SelectorTreeNode) {
            childGroup = ((SelectorTreeNode) currentNode).getGroupModelToCreateChildObject();
        } else {
            childGroup = null;
        }
        return getTitleOfPasteChildObjectActionImpl(childGroup, getEnvironment().getClipboard());
    }

    /**
     * Изменение заголовка действия вставки подобъекта(ов) из буфера обмена.
     * Метод позволяет изменить текст всплывающей подсказки у кнопки вставки
     * объекта нижележащего уровня.
     *
     * @param title новый заголовок действия ставки подобъекта(ов) из буфера
     * обмена. Если значение * * * * равно <code>null</code>, или пустая строка,
     * то будет использоваться стандартный заголовок.
     */
    public final void setTitleOfPasteChildObjectAction(final String title) {
        pasteChildObjectCustomTitle = title;
        updateActions();
    }

    private void applySettings() {
        WpsSettings settings = ((WpsEnvironment) getEnvironment()).getConfigStore();
        if (settings != null) {
            try {
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.SELECTOR_GROUP);
                settings.beginGroup(SettingNames.Selector.COMMON_GROUP);

                final int sliderValue = settings.readInteger(SettingNames.Selector.Common.SLIDER_VALUE, 4);                
                shadeEvenRow(sliderValue);
                
                setCurrentNodeFrameColor(settings.readColor(SettingNames.Selector.Common.ROW_FRAME_COLOR,Color.decode("#3399ff")));
                setCurrentCellFrameColor(settings.readColor(SettingNames.Selector.Common.FRAME_COLOR,Color.decode("#404040")));
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
            applyHeaderAlignmentSettings();
            updateSelectorTextSettings();
        }
    }

    private void updateSelectorTextSettings() {
        for (int i = 0; rootNode != null && i < rootNode.getChildCount(); i++) {
            Node n = rootNode.getChildAt(i);

            if (n instanceof SelectorTreeEntityModelNode) {
                SelectorTreeEntityModelNode nodeModel = (SelectorTreeEntityModelNode) n;
                nodeModel.updateTextOptions();
            }
        }
    }

    private void updateSelectorDisplayableNames() {
        //обновляем значения в нодах селектора, чтобы в них не отображались гуиды свойств, а реальные значения
        for (int i = 0; rootNode != null && i < rootNode.getChildCount(); i++) {
            Node n = rootNode.getChildAt(i);

            if (n instanceof SelectorTreeEntityModelNode) {
                SelectorTreeEntityModelNode nodeModel = (SelectorTreeEntityModelNode) n;
                nodeModel.updateNodeDisplayName();
            }
        }
    }

    private void applyHeaderAlignmentSettings() {
        Alignment alignmentFlag;
        WpsSettings settings = ((WpsEnvironment) getEnvironment()).getConfigStore();
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);

            alignmentFlag = Alignment.getForValue(settings.readInteger(SettingNames.Selector.Common.TITLES_ALIGNMENT, Alignment.CENTER.ordinal()));
            setHeaderAlignment(alignmentFlag);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    @Override
    public void setParent(final UIObject parent) {
        if (parent == null) {//closing selector
            controller.close();
            if (l != null) {
                ((WpsEnvironment) getEnvironment()).removeSettingsChangeListener(l);
            }
        }
        super.setParent(parent);
    }

}
