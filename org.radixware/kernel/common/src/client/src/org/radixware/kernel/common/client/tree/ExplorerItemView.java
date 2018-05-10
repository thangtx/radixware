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

package org.radixware.kernel.common.client.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.meta.filters.RadContextFilter;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamValue;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;

/**
 * Представление модели в дереве проводника
 */
public final class ExplorerItemView implements IExplorerItemView {// implements IPresentationChangedHandler  {

    private final ExplorerTreeNode node;
    private final IExplorerTree tree;
    private final Id explorerItemId;
    private final Id definitionId;
    private final Id definitionClassId;
    private final Model model;
    private EntityInfo entityInfo = null;
    private boolean autoInsert = false;
    private String title = null;
    private Icon icon = null;

    @SuppressWarnings("LeakingThisInConstructor")
    public ExplorerItemView(final Model radixModel, final Id explorerItemId, final ExplorerTreeNode treeNode) {
        super();
        node = treeNode;
        tree = treeNode.getExplorerTree();
        model = radixModel;
        this.explorerItemId = explorerItemId;
        definitionId = model.getDefinition().getId();
        definitionClassId = model.getDefinition().getOwnerClassId();        
        if (model instanceof EntityModel && model.getContext().getHolderModel() == null) {
            entityInfo = new EntityInfo((EntityModel) model);
        } else {
            entityInfo = null;
        }
    }

    /**
     *
     * @return идентификатор элемента проводника или
     * null если данное представление создано для модели
     * EntityModel, всатвленной в дерево из селектора.
     */
    @Override
    public Id getExplorerItemId() {
        return explorerItemId;
    }

    @Override
    public IExplorerItemsHolder getTopLevelExplorerItemsHolder(){
        return node.getTopLevelExplorerItemsHolder();
    }     

    @Override
    public IExplorerTree getExplorerTree() {
        return tree;
    }

    @Override
    public Id getDefinitionId() {
        return definitionId;
    }

    @Override
    public Id getDefinitionOwnerClassId() {
        return definitionClassId;
    }

    @Override
    public Id getTableId() {
        if (model.getDefinition() instanceof RadPresentationDef) {
            return ((RadPresentationDef) model.getDefinition()).getTableId();
        }
        return definitionId;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public EntityInfo getChoosenEntityInfo() {
        return entityInfo;
    }

    /**
     * @return заголовок элемента проводника
     */
    @Override
    public String getTitle() {
        if (title != null) {
            return title;
        }
        if (model.getContext() instanceof IContext.ParentEntityEditing) {//RADIX-1012
            final IContext.ParentEntityEditing context = (IContext.ParentEntityEditing) model.getContext();
            RadParentRefExplorerItemDef explorerItemDef = context.explorerItemDef;
            if (explorerItemDef.hasTitle() && !explorerItemDef.getTitle().isEmpty()) {
                return explorerItemDef.getTitle() + ": " + model.getTitle();
            }
        }
        if (model.getContext() instanceof IContext.ParentEntityEditing
                || model.getContext() instanceof IContext.ReferencedChoosenEntityEditing
                || model.getContext() instanceof IContext.ContextlessEditing) {
            final boolean isTitleDefined;
            if (model.getDefinition() instanceof TitledDefinition){
                isTitleDefined = ((TitledDefinition)model.getDefinition()).hasTitle();
            }else{
                isTitleDefined = model.getDefinition().getTitle()!=null && !model.getDefinition().getTitle().isEmpty();
            }
            if (isTitleDefined) {
                return model.getDefinition().getTitle() + ": " + model.getTitle();                
            } else {
                return model.getTitle();
            }
        }
        if (isChoosenObject()) {
            final IContext.ChoosenEntityEditing context = (IContext.ChoosenEntityEditing) model.getContext();
            final IExplorerTreeNode parentNode = node.getParentNode();
            final IExplorerItemView parent = parentNode == null ? null : parentNode.getView();
            if (parent != null && (!parent.isGroupView() || parent.getModel() != context.getHolderModel())) {
                if (model.getDefinition().getTitle().isEmpty()) {
                    return model.getTitle();
                } else {
                    return model.getDefinition().getTitle() + ": " + model.getTitle();
                }
            }
        }
        return model.getTitle();
    }

    /**
     * @return пиктограмма элемента проводника
     */
    @Override
    public Icon getIcon() {
        return icon != null ? icon : model.getIcon();
    }

    @Override
    public void setTitle(final String title) {
        if (!Objects.equals(title, this.title)){
            this.title = title;
            refresh();
        }
    }

    public void setIcon(final Icon icon) {
        if (icon!=this.icon){
            this.icon = icon;
            refresh();
        }
    }

    @Override
    public void setVisible(final boolean visible) {
        tree.setNodeVisible(node, visible);
    }

    @Override
    public boolean isVisible() {
        return tree.isNodeVisible(node);
    }

    @Override
    public boolean isParagraphView() {
        return model instanceof ParagraphModel;
    }

    //selector
    @Override
    public boolean isGroupView() {
        return model instanceof GroupModel;
    }

    //editor
    @Override
    public boolean isEntityView() {
        return model instanceof EntityModel;
    }
    
    @Override
    public boolean isUserItemView(){
        return explorerItemId!=null && explorerItemId.getPrefix()== EDefinitionIdPrefix.USER_EXPLORER_ITEM;
    }

    /**
     *  перечитать настройки представления модели в дереве проводника
     */
    @Override
    public void refresh() {
        tree.update(node);
    }

    /**
     * Удалить элемент проводника из дерева
     */
    @Override
    public void remove() {
        model.getEnvironment().getTracer().put(EEventSeverity.DEBUG, "Removing explorer item \"" + node.getPath() + "\" from tree.",
                EEventSource.EXPLORER);
        model.getEnvironment().getTracer().put(EEventSeverity.DEBUG, "Explorer item \"" + toString() + "\" was removed from tree.",
                EEventSource.EXPLORER);
        tree.removeNode(node);
    }

    @Override
    public List<IExplorerItemView> getChoosenEntities() {
        List<IExplorerItemView> result = new ArrayList<>();
        List<IExplorerTreeNode> childs = node.getChildNodesRecursively();
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                ExplorerItemView view = (ExplorerItemView) child.getView();
                if (view.isChoosenObject()) {
                    result.add(view);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<IExplorerItemView> getChoosenEntities(final Id tableId) {
        List<IExplorerItemView> result = new ArrayList<>();
        List<IExplorerTreeNode> childs = node.getChildNodes();
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                ExplorerItemView view = (ExplorerItemView) child.getView();
                if (view.isChoosenObject() && view.getTableId().equals(tableId)) {
                    result.add(view);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }        

    /**
     * Удалить дочерние представления EntityModel,
     * всатвленные в дерево из селектора.
     */
    @Override
    public void removeChoosenEntities() {
        tree.lock();
        try {
            for (IExplorerTreeNode child = findFirstChildRecursively(); child != null; child = findFirstChildRecursively()) {
                tree.removeNode(child);
            }
        } finally {
            tree.unlock();
        }
    }

    private IExplorerTreeNode findFirstChildRecursively() {
        List<IExplorerTreeNode> childs = node.getChildNodesRecursively();
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                ExplorerItemView view = (ExplorerItemView) child.getView();
                if (view.isChoosenObject()) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Удалить дочерние представления EntityModel с идентификатором pid,
     * всатвленные в дерево из селектора.
     * @param pid
     */
    @Override
    public void removeChoosenEntity(final Pid pid) {
        List<IExplorerTreeNode> childs = node.getChildNodes();
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                ExplorerItemView view = (ExplorerItemView) child.getView();
                if (view.isChoosenObject() && view.getChoosenEntityInfo().pid.equals(pid)) {
                    tree.removeNode(child);
                    break;
                }
            }
        }
    }

    /**
     * Создает дочернее представление для модели entity и
     * вставляет ее в дерево проводника
     * @param entity - EntityModel с контекстом Context.ChoosenEntityEditing
     * @return представление модели entity в дереве проводника
     */
    @Override
    public ExplorerItemView insertEntity(final int index, EntityModel entity, final boolean expandAfterInsert) {
        ExplorerItemView result = findChoosenEntityByPid(entity.getPid());
        if (result != null) {
            //Эта модель уже есть в дереве
            return result;
        }
        final ExplorerItemView autoInsertedEntity = findAutoInsertedEntity(entity.getEditorPresentationDef().getTableId());
        if (autoInsertedEntity != null && ((EntityModel) autoInsertedEntity.model).getPid().equals(entity.getPid())) {
            //если эта модель уже была добавлена при автовставке -
            //убираем признак автовставки.
            autoInsertedEntity.autoInsert = false;
            //Теперь эта модель вставлена обычным образом
            return autoInsertedEntity;
        }

        tree.lock();
        try {
            return insertImpl(index, entity, expandAfterInsert);
        } finally {
            tree.unlock();
        }
    }

    @Override
    public ExplorerItemView insertEntity(EntityModel entity) {
        return insertEntity(0, entity, false);
    }

    /**
     * Создает дочернее представление для модели entity и
     * вставляет ее в дерево проводника с пометкой автовставки
     * @param entity - EntityModel с контекстом Context.ChoosenEntityEditing
     * @return представление модели entity в дереве проводника
     */
    @Override
    public ExplorerItemView autoInsertEntity(EntityModel entity) {
        ExplorerItemView result = findChoosenEntityByPid(entity.getPid());
        if (result != null) {
            //Эта модель уже есть в дереве
            return result;
        }
        final ExplorerItemView autoInsertedEntity = findAutoInsertedEntity(entity.getEditorPresentationDef().getTableId());
        tree.lock();
        try {
            if (autoInsertedEntity != null) {
                autoInsertedEntity.remove();
            }
            result = insertImpl(0, entity, true);
            result.autoInsert = true;
            return result;
        } finally {
            tree.unlock();
        }
    }

    @Override
    public IExplorerItemView insertUserExplorerItem(final GroupModel groupModel, final String title){
        if (groupModel.getContext() instanceof IContext.TableSelect==false){
            throw new IllegalArgumentException("Expected group model with \'TableSelect\' context");
        }
        final IContext.TableSelect context = (IContext.TableSelect)groupModel.getContext();
        final RadSelectorExplorerItemDef explorerItemDef = (RadSelectorExplorerItemDef)context.explorerItemDef;        
        final Collection<RadFilterParamValue> filterParamValues = new LinkedList<>();
        for (Property filterParam: groupModel.getCurrentFilter().getActiveProperties()){
            final ValAsStr valAsStr;
            final Object value = filterParam.getValueObject();                
            final EValType valType = ValueConverter.serverValType2ClientValType(filterParam.getType());
            if (value instanceof IKernelEnum) {
                valAsStr = ValAsStr.Factory.newInstance(((IKernelEnum) value).getValue(),valType);
            }else{
                valAsStr = ValueConverter.obj2ValAsStr(value, valType);
            }
            filterParamValues.add(new RadFilterParamValue(filterParam.getId(), filterParam.getType(), valAsStr));
        }
        final List<RadContextFilter> contextFilters = new LinkedList<>();
        contextFilters.addAll(context.getFilters());
        final RadFilterDef additionalFilter = groupModel.getCurrentFilter().getFilterDef();
        if (additionalFilter instanceof RadUserFilter){
            contextFilters.addAll(RadContextFilter.Factory.splitUserFilter((RadUserFilter)additionalFilter, filterParamValues, explorerItemId));
        }else{
            contextFilters.add(RadContextFilter.Factory.newInstance(additionalFilter, filterParamValues, explorerItemId));
        }
        final RadSelectorUserExplorerItemDef userItem = 
            RadSelectorUserExplorerItemDef.Factory.newInstance(title, 
                                                               explorerItemDef,
                                                               contextFilters,
                                                               groupModel.getCurrentSorting());
        final IExplorerItemsHolder itemsHolder = node.getTopLevelExplorerItemsHolder();
        final Id ownerDefId = UserExplorerItemsStorage.getContextId(itemsHolder);
        final UserExplorerItemsStorage storage = UserExplorerItemsStorage.getInstance(tree.getEnvironment());
        storage.registerUserExplorerItem(ownerDefId, userItem);
        boolean userEIWasInserted = false;
        IExplorerItemView resultView = null;
        tree.lock();
        try{
            if (itemsHolder instanceof RadEditorPresentationDef){//добавление пользовательского элемента в контексте объекта
                final Id tableId = ((RadEditorPresentationDef)itemsHolder).getTableId();
                //Нужно добавить элемент во все вставленные в дерево объекты этой сущности
                final List<IExplorerTreeNode> rootNodes =  tree.getRootNodes();
                final Collection<IExplorerTreeNode> allNodes = new LinkedList<>();
                final Id sourceExplorerItemId = explorerItemDef.getId();
                for (IExplorerTreeNode rootNode: rootNodes){
                    allNodes.addAll(rootNode.getChildNodesRecursively());
                }
                for (IExplorerTreeNode treeNode: allNodes){
                    if (treeNode.isValid() 
                        && sourceExplorerItemId.equals(treeNode.getView().getExplorerItemId())
                        && treeNode.getView().isVisible()
                       ){
                       final ExplorerItemView view = (ExplorerItemView)treeNode.getView();
                       final IExplorerItemsHolder treeNodeHolder = treeNode.getView().getTopLevelExplorerItemsHolder();
                       if (treeNodeHolder instanceof RadEditorPresentationDef
                           && tableId.equals(((RadEditorPresentationDef)treeNodeHolder).getTableId())
                          ){
                            final ExplorerTreeNode parent = view.node.getParentNode();
                            final int selfIndex = parent.getChildNodes().indexOf(view.node);
                            final IExplorerTreeNode newNode = tree.addUserExplorerItem(parent, userItem, selfIndex+1);
                            if (newNode!=null && newNode.isValid()){
                                userEIWasInserted = true;
                                getModel().afterInsertChildItem(newNode.getView());
                                final String traceMessage = "Explorer item \"" + newNode.toString() + "\" was inserted into tree";
                                tree.getEnvironment().getTracer().put(EEventSeverity.DEBUG, traceMessage , EEventSource.EXPLORER);
                                if (view==this){
                                    resultView = newNode.getView();
                                }
                            }

                       }
                    }
                }
            }else{
                final ExplorerTreeNode parent = node.getParentNode();
                final int selfIndex = parent.getChildNodes().indexOf(node);
                final IExplorerTreeNode newNode = tree.addUserExplorerItem(parent, userItem, selfIndex+1);
                userEIWasInserted = newNode!=null && newNode.isValid();
                if (userEIWasInserted){
                    final String traceMessage = "Explorer item \"" + newNode.toString() + "\" was inserted into tree";
                    tree.getEnvironment().getTracer().put(EEventSeverity.DEBUG, traceMessage , EEventSource.EXPLORER);
                    resultView = newNode.getView();
                }
            }
        }finally{
            tree.unlock();
            if (!userEIWasInserted){
                storage.removeUserExplorerItem(ownerDefId, userItem.getId(), null);
            }
        }
        
        return resultView;
    }        
    
    @Override
    public String suggestUserExplorerItemTitle(final GroupModel groupModel){
        if (groupModel.getContext() instanceof IContext.TableSelect==false){
            throw new IllegalArgumentException("Expected group model with \'TableSelect\' context");
        }
        final IContext.TableSelect context = (IContext.TableSelect)groupModel.getContext();
        RadSelectorExplorerItemDef sourceExplorerItem = (RadSelectorExplorerItemDef)context.explorerItemDef;
        if (sourceExplorerItem instanceof RadSelectorUserExplorerItemDef){
            sourceExplorerItem = ((RadSelectorUserExplorerItemDef)sourceExplorerItem).getTargetExplorerItem();
        }
        final Collection<String> existingTitles = new LinkedList<>();
        if (sourceExplorerItem!=null){
            List<IExplorerTreeNode> nodes = node.getParentNode().getChildNodes();
            for (IExplorerTreeNode childNode: nodes){
                if (childNode.isValid()){
                    existingTitles.add(childNode.getView().getTitle());
                }
            }
        }
        final String initialTitle;
        if (groupModel.getCurrentFilter()!=null){
            final String titleFormat = 
                groupModel.getEnvironment().getMessageProvider().translate("ExplorerTree", "%1$s with filter \'%2$s\'");
            initialTitle = String.format(titleFormat, groupModel.getTitle(), groupModel.getCurrentFilter().getTitle());
        }else{
            initialTitle = groupModel.getTitle();
        }
        
        String suggestedTitle = initialTitle;
        for (int i=2; i<Integer.MAX_VALUE; i++){
            if (existingTitles.contains(suggestedTitle)){
                suggestedTitle = initialTitle + " ("+String.valueOf(i)+")";//NOPMD
            }else{
                return suggestedTitle;
            }
        }
        return suggestedTitle;
    }
        

    /**
     * Сделать данное представление текущим в дереве проводника
     * @return
     */
    @Override
    public boolean setCurrent() {
        return tree.getCurrent()==node ? true : tree.setCurrent(node);
    }

    /**
     * @return true, если данное представление создано  для EntityModel,
     * всатвленной в дерево из селектора
     */
    @Override
    public boolean isChoosenObject() {
        return entityInfo != null;
    }

    /**
     * @return true, если существуют дочерние представления EntityModel,
     * всатвленные в дерево из селектора
     */
    @Override
    public boolean hasChoosenObjects() {
        return findFirstChildRecursively() != null;
    }

    @Override
    public IExplorerItemView getChild(final int index) {
        List<IExplorerTreeNode> childs = node.getChildNodes();
        for (int i = index; i < childs.size(); i++) {
            if (childs.get(i).isValid()) {
                return childs.get(i).getView();
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getChildsCount() {
        List<IExplorerTreeNode> childs = node.getChildNodes();
        int counter = 0;
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public void expand() {
        tree.expand(node);
    }

    public void collapse() {
        tree.collapse(node);
    }

    public boolean isExpanded() {
        return tree.isExpanded(node);
    }

    @Override
    public String toString() {
        return getTitle();
    }

    private ExplorerItemView insertImpl(final int index, final EntityModel entity, final boolean expandAfterInsert) {
        IContext.Entity context = (IContext.Entity) entity.getContext();
        if (context == null) {
            throw new IllegalUsageError("Can`t insert entity with no context");
        }
        if (!(context instanceof IContext.ChoosenEntityEditing)
                && !(context instanceof IContext.ReferencedChoosenEntityEditing)
                && !(context instanceof IContext.ParentEntityEditing)
                && !(context instanceof IContext.ContextlessEditing)) {
            throw new IllegalUsageError(String.format("Can`t insert entity with %s context", context.getClass().getSimpleName()));
        }
        final String msg = "Creating new explorer item for entity model \"" + entity.getTitle() + "\" with parent \"" + node.getPath() + "\"";
        model.getEnvironment().getTracer().put(EEventSeverity.DEBUG, msg,
                EEventSource.EXPLORER);

        //Перед вставкой нового, возможно, надо удалить все предыдущие.
		/*Environment.getConfigStore().beginGroup(SettingNames.SYSTEM);
        Environment.getConfigStore().beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        Environment.getConfigStore().beginGroup(SettingNames.ExplorerTree.EDITOR_GROUP);
        final boolean onlyOneChoosenEntity = Environment.getConfigStore().readBoolean(SettingNames.ExplorerTree.Editor.SHOW_ONE_EDITOR, false);
        Environment.getConfigStore().endGroup();
        Environment.getConfigStore().endGroup();
        Environment.getConfigStore().endGroup();
        if (onlyOneChoosenEntity)
        removeChoosenEntities();*/

        IExplorerTreeNode newNode = tree.addChoosenEntity(node, entity, index);
        getModel().afterInsertChildItem(newNode.getView());
        refresh();
        if (expandAfterInsert) {
            final int childCount = newNode.getChildNodes().size();
            tree.expand(node);
            if (childCount > 0) {
                tree.expand(newNode);
            }
        }

        model.getEnvironment().getTracer().put(EEventSeverity.DEBUG, "Explorer item \"" + newNode.toString() + "\" was inserted into tree",
                EEventSource.EXPLORER);

        return (ExplorerItemView) newNode.getView();
    }

    private ExplorerItemView findAutoInsertedEntity(final Id tableId) {
        List<IExplorerTreeNode> childs = node.getChildNodes();
        ExplorerItemView view;
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                view = (ExplorerItemView) child.getView();
                if (view.autoInsert && view.isChoosenObject() && view.getTableId().equals(tableId)) {
                    return view;
                }
            }
        }
        return null;
    }

    private ExplorerItemView findChoosenEntityByPid(final Pid pid) {
        List<IExplorerTreeNode> childs = node.getChildNodes();
        ExplorerItemView view;
        for (IExplorerTreeNode child : childs) {
            if (child.isValid()) {
                view = (ExplorerItemView) child.getView();
                if (!view.autoInsert && view.isChoosenObject() && view.getChoosenEntityInfo().pid.equals(pid)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * Возвращает список моделей родительских сущностей в дереве проводника в направлении от
     * ближайшей родительской сущности в дереве до корня.
     * @return
     */
    @Override
    public List<EntityModel> getParentEntityModels() {
        List<EntityModel> entities = new ArrayList<>();
        for (IExplorerTreeNode n = node.getParentNode(); n != null; n = n.getParentNode()) {
            if (n.isValid() && n.getView().isEntityView()) {
                entities.add((EntityModel) n.getView().getModel());
            }
        }
        return Collections.unmodifiableList(entities);
    }

    /**
     * Возвращает список моделей родительских групп в дереве проводника в направлении от
     * ближайшей родительской группы дерева до корня.
     * @return
     */
    public List<GroupModel> getParentGroupModels() {
        List<GroupModel> groups = new ArrayList<>();
        for (IExplorerTreeNode n = node.getParentNode(); n != null; n = n.getParentNode()) {
            if (n.isValid() && n.getView().isGroupView()) {
                groups.add((GroupModel) n.getView().getModel());
            }
        }
        return Collections.unmodifiableList(groups);
    }

    /**
     * Возвращает список родительских моделей в дереве проводника в направлении от
     * ближайшего родителя в дереве до корня (последняя модель в списке - модель корневого параграфа).
     * @return
     */
    public List<Model> getParentModels() {
        List<Model> models = new ArrayList<>();
        for (IExplorerTreeNode n = node.getParentNode(); n != null; n = n.getParentNode()) {
            if (n.isValid()) {
                models.add(n.getView().getModel());
            }
        }
        return Collections.unmodifiableList(models);
    }

    @Override
    public Model getParentModel() {
        IExplorerTreeNode parentNode = node.getParentNode();
        return parentNode != null && parentNode.isValid() ? parentNode.getView().getModel() : null;
    }

    public String getPath() {
        return node.getPath();
    }    
}
