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

import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ProxyGroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.tree.Node;

/**
 * Стандартный набор узлов деревовидного селектора (Radix::Web.Widgets.SelectorTree::ChildNodes).
 * Класс создает и хранит стандартные узлы древовидного селектора (интстанции {@link SelectorTreeEntityModelNode})
 * на основе объектов сущностей, полученных из моделей групп.
 * Используется в качестве стандартного набора подузлов для узла {@link SelectorTreeEntityModelNode}.
 * В конструктор инстанции класса передается либо готовый набор моделей групп, либо набор параметров для их создания и настройки.
 * Если модели групп изначально не заданы, то для их получения будет использован метод древовидного селектора {@link IRwtSelectorTree#createChildGroupModels(SelectorTreeNode, EntityModel)   createChildGroupModels},
 * стандартная реализация которого использует набор параметров создания моделей групп. 
 */
public class SelectorTreeChildNodes extends Node.Children{
        
    private final EntityModel parentEntityModel;    
    private final IRwtSelectorTree tree;    
    private final List<ChildGroupModelSettings> childGroupModelSettings = new LinkedList<>();
    private SelectorTreeNode ownerNode;
    private List<GroupModel> childGroups;
    private boolean closed;
    
    /**
     * Конструктор стандартного набора узлов древовидного селектора.
     * Передается набор настроек создания моделей групп, на основе содержимого которых будут созданы узлы дерева.
     * @param parentEntityModel модель объекта сущности, ассоциированная с узлом, которому принадлежит данный набор подузлов. Может быть <code>null</code>.
     * @param groupsSettings набор параметров создания моделей групп
     */
    public SelectorTreeChildNodes(final EntityModel parentEntityModel, final List<ChildGroupModelSettings> groupsSettings){
        this.parentEntityModel = parentEntityModel;
        tree = null;        
    
        if (groupsSettings!=null && !groupsSettings.isEmpty()){
            for (ChildGroupModelSettings groupSettings: groupsSettings){
                childGroupModelSettings.add(groupSettings.createCopy());
            }
        }
    }

    /**
     * Конструктор стандартного набора узлов древовидного селектора.
     * Передаются настройки создания модели группы, на основе содержимого которой будут созданы узлы дерева.
     * @param parentEntityModel модель объекта сущности, ассоциированная с узлом, которому принадлежит данный набор подузлов. Может быть <code>null</code>.
     * @param groupSettings параметры создания модели группы 
     */
    public SelectorTreeChildNodes(final EntityModel parentEntityModel, ChildGroupModelSettings groupSettings){
        this(parentEntityModel, groupSettings==null ? null : Collections.<ChildGroupModelSettings>singletonList(groupSettings));
    }

    /**
     * Конструктор стандартного набора узлов древовидного селектора.
     * Этим конструктором будет создан пустой набор узлов. 
     * Для заполнения набора будет использоваться метод дерева {@link IRwtSelectorTree#createChildGroupModels(SelectorTreeNode, EntityModel)   createChildGroupModels}.
     * @param parentEntityModel модель объекта сущности, ассоциированная с узлом, которому принадлежит данный набор подузлов. Может быть <code>null</code>.
     */
    public SelectorTreeChildNodes(final EntityModel parentEntityModel){
        this(parentEntityModel, (List<ChildGroupModelSettings>)null);
    }

    /**
     * Конструктор стандартного набора узлов древовидного селектора.
     * Используется для создания набора подузлов для корневого узла дерева.
     * Передается список моделей групп, на основе содержимого которых будут созданы узлы древовидного селектора.
     * @param tree дерево селектора
     * @param groupModels список моделей групп
     */
    public SelectorTreeChildNodes(final IRwtSelectorTree tree, final List<GroupModel> groupModels){
        childGroups = new LinkedList<>(groupModels);        
        parentEntityModel = null;
        this.tree = tree;        
        for (GroupModel childGroup: groupModels){
            if (childGroup!=tree.getRootGroupModel()){
                changeChildGroupContext(childGroup, tree.getRootGroupModel().getGroupContext());                
                childGroup.getRestrictions().add(calcChildGroupRestrictions(childGroup));          
                childGroup.setView(tree.getRootGroupModel().getView());
            }
        }
    }
    
    void setOwnerNode(final SelectorTreeNode owner){
        ownerNode = owner;
    }
    
    private SelectorTreeNode getOwnerSelectorTreeNode(){
        if (ownerNode==null){
            return getOwnerNode() instanceof SelectorTreeNode ? (SelectorTreeNode)getOwnerNode() : null;
        }
        else{
            return ownerNode;
        }
    }
    
    /**
     * Получение списка параметров создания моделей групп. Метод возвращает список параметров для создания моделей групп, переданный в конструкторе.
     * @return набор настроек для создания моделей групп
     */
    public final List<ChildGroupModelSettings> getChildGroupModelSettings(){
        return Collections.<ChildGroupModelSettings>unmodifiableList(childGroupModelSettings);
    }
        
    /**
     * Получение списка моделей групп. Метод возвращает список моделей групп, которые используются для создания узлов дерева.
     * Для создания моделей групп используется метод дерева {@link IRwtSelectorTree#createChildGroupModels(SelectorTreeNode, EntityModel)   createChildGroupModels}.
     * Результат работы этого метода кешируется до вызова метода {@link #reset() reset}.
     * Созданным моделям группы добавляются ограничения, взятые из метода {@link #calcChildGroupRestrictions(GroupModel) calcChildGroupRestrictions}
     * и устанавливается <code>view</code>, взятая из корневой модели группы дерева.
     * Если узел, которому принадлежит данный набор подузлов не ассоциирован с моделью сущности, то метод возвращает пустой список.
     * @return список моделей групп
     */
    protected final List<GroupModel> getChildGroupModels(){
        if (childGroups==null){
            if (closed){
                return Collections.<GroupModel>emptyList();
            }
            final EntityModel entityModel = getParentEntityModel();            
            if (entityModel!=null){                
                final IClientEnvironment environment = entityModel.getEnvironment();                
                final SelectorTreeNode parentNode = getOwnerSelectorTreeNode();
                try{
                    final List<GroupModel> groups;                
                    if (parentNode==null){
                        groups = ChildGroupModelSettings.createChildGroupModels(childGroupModelSettings, entityModel);                        
                    }
                    else{
                        groups = getSelectorTree().createChildGroupModels(parentNode, entityModel);
                    }
                    childGroups = groups==null ? Collections.<GroupModel>emptyList() : groups;
                    for (GroupModel childGroup: childGroups){
                        changeChildGroupContext(childGroup, getRootGroupModel().getGroupContext());
                        childGroup.getRestrictions().add(calcChildGroupRestrictions(childGroup));
                        if (entityModel.getEntityContext() instanceof IContext.SelectorRow){
                            final IContext.SelectorRow context = (IContext.SelectorRow)entityModel.getEntityContext();                            
                            prepareChildGroupFilter(context.parentGroupModel, childGroup);
                        }
                        childGroup.setView(getRootGroupModel().getView());
                    }
                }
                catch(Exception exception){                
                    final String title = environment.getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
                    environment.getTracer().error(String.format(title, entityModel.getTitle()), exception);
                    environment.getTracer().put(EEventSeverity.DEBUG,
                            String.format(title, entityModel.getTitle())+":\n"+
                            ClientException.exceptionStackToString(exception),
                            EEventSource.EXPLORER);             
                    childGroups = Collections.<GroupModel>emptyList();
                }                            
            }
            else{
                childGroups = Collections.<GroupModel>emptyList();
            }
        }
        return childGroups;
    }
    
    private void readGroupModel(final GroupModel childGroupModel, final List<Node> childNodes) throws ServiceClientException, InterruptedException {        
        int i = 0;
        EntityModel entityModel = null;
        Node childNode;
        do{
            try{
                entityModel = childGroupModel.getEntity(i);
                if (entityModel!=null){
                    final Map<Id,Id> columnsMap = mapSelectorColumns(childGroupModel, entityModel);                    
                    childNode = 
                        new SelectorTreeEntityModelNode(entityModel, getSelectorTree(), columnsMap);
                    i++;
                }
                else{
                    childNode = null;
                }
            }
            catch(BrokenEntityObjectException exception){
                entityModel =  
                    new BrokenEntityModel(childGroupModel.getEnvironment(), childGroupModel.getSelectorPresentationDef(), exception);
                childNode = 
                        new SelectorTreeBrokenEntityModelNode(childGroupModel, (BrokenEntityModel)entityModel);                
                i++;
            }
            if (childNode!=null){
                final SelectorTreeNode parentNode = getOwnerSelectorTreeNode();
                try{
                    final WpsIcon icon = (WpsIcon)getSelectorTree().getNodeIcon(parentNode, childNode);
                    if (icon!=null){
                        childNode.setIcon(icon);
                    }
                }
                catch(Exception exception){
                    final IClientEnvironment environment = childGroupModel.getEnvironment();
                    final String title = environment.getMessageProvider().translate("ExplorerException", "Can't get icon for child %s");
                    environment.getTracer().error(String.format(title, ownerNode.getDescription()), exception);
                    environment.getTracer().put(EEventSeverity.DEBUG,
                            String.format(title, ownerNode.getDescription())+":\n"+
                            ClientException.exceptionStackToString(exception),
                            EEventSource.EXPLORER);                
                }
                childNodes.add(childNode);
            }
        }
        while(entityModel!=null);                
    }

    private Map<Id,Id> mapSelectorColumns(final GroupModel groupModel, final EntityModel entityModel) {
        final Map<Id,Id> columnsMap = new HashMap<>();
        final IRwtSelectorTree selectorTree = getSelectorTree();
        if (selectorTree!=null){
            final List<SelectorColumnModelItem> columns = selectorTree.getSelectorColumns();        
            SelectorColumnModelItem column;
            Id propertyId;
            for (int i = 0; i < columns.size(); ++i) {
                column = columns.get(i);
                try {
                    final SelectorTreeNode parentNode = getOwnerSelectorTreeNode();
                    if (parentNode==null){
                        propertyId = mapSelectorColumn(entityModel, column);
                    }
                    else{
                        propertyId = selectorTree.mapSelectorColumn(column, parentNode, groupModel, entityModel);
                    }
                    if (propertyId != null) {
                        entityModel.getProperty(propertyId).getValueObject();//activateProperty
                        columnsMap.put(column.getId(), propertyId);
                    }
                } catch (Throwable ex) {
                    final IClientEnvironment environment = groupModel.getEnvironment();
                    final String title = environment.getMessageProvider().translate("ExplorerException", "Can't get property value corresponding to column #%s with title \'%s\'");
                    environment.getTracer().error(String.format(title, column.getId(), column.getTitle()), ex);
                    environment.getTracer().put(EEventSeverity.DEBUG, String.format(title, column.getId(), column.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                }
            }        
        }
        return columnsMap;
    }
    
    private void preprocessServiceCallFault(final IClientEnvironment environment, final ServiceCallFault fault){        
        ObjectNotFoundError objectNotFound = null;
        for (Throwable cause = fault; cause != null; cause = cause.getCause()) {
            if (cause instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) cause;
            }            
        }
                
        if (objectNotFound != null) {      
            EntityModel parentEntity;
            for (Node node = ownerNode==null ? getOwnerNode() : ownerNode; node != null; node = node.getParentNode()) {
                if (node.getUserData() instanceof EntityModel){
                    parentEntity = (EntityModel)node.getUserData();
                    if (parentEntity != null && !(parentEntity instanceof BrokenEntityModel) && objectNotFound.inContextOf(parentEntity)) {
                        objectNotFound.setContextEntity(parentEntity);
                        showServiceClientException(environment, objectNotFound);
                        return;
                    }
                }
            }
        }        
        showServiceClientException(environment, fault);
    }
    
    private void showServiceClientException(final IClientEnvironment environment, final ServiceClientException exception){
        final String title = environment.getMessageProvider().translate("ExplorerException", "Error on receiving data");
        if (getRootGroupModel()==null){
            environment.processException(exception);
        }
        else{
            getRootGroupModel().showException(title, exception);
        }
    }

    /**
    * Поиск узлов. Метод для поиска узлов, с которыми ассоциированы объекты сущности с указанным идентификатором.
    * Для получения объекта сущности из дочернего узла используется метод <code>getUserData()</code>.
    * @param pid идентификатор объекта сущности
    * @return список узлов данного набора, с которыми ассоциированы объекты сущности с указанным идентификатором
    */
    public List<Node> findNodes(Pid pid) {
        final List<Node> nodes = new LinkedList<>();
        if (pid!=null){
            for (Node node: getNodes()){
                if (node.getUserData() instanceof EntityModel && pid.equals( ((EntityModel)node.getUserData()).getPid() )){
                    nodes.add(node);                    
                }
            }
        }
        return nodes;
    }    

    /**
     * Получение инстанции виджета древовидного селектора. Метод возвращает интерфейс древовидного селектора.
     * @return интерфейс древовидного селектора
     */
    protected final IRwtSelectorTree getSelectorTree(){
        if (getOwnerSelectorTreeNode()==null){
            return tree;            
        }
        else{
            return getOwnerSelectorTreeNode().getSelectorTree();
        }
    }
    
    
    /**
     * Получение объекта сущности. Метод возвращает объект сущности, ассоциированный с узлом дерева, которому принадлежит данный набор подузлов.
     * @return объект сущности. Может быть <code>null</code>
     */
    protected final EntityModel getParentEntityModel(){
        if (parentEntityModel!=null){
            return parentEntityModel;
        }
        else if (getOwnerNode()!=null && getOwnerNode().getUserData() instanceof EntityModel){
            return (EntityModel)getOwnerNode().getUserData();
        }
        else {
            return null;
        }
    }

    /**
     * Получение корневой модели группы. Метод возвращает корневую модель группы дерева селектора.
     * @return модель группы
     */
    protected final GroupModel getRootGroupModel(){
        return getSelectorTree()==null ? null : getSelectorTree().getRootGroupModel();
    }

    /**
     * Получение модели группы для создания объекта сущности. Метод возвращает модель группы, которая будет использоваться при создании объекта.
     * Данный метод используется во время выполнения операции создания дочернего узла дерева.
     * Стандартная реализация использует список моделей групп, возвращаемый методом {@link #getChildGroupModels() getChildGroupModels}.
     * при этом возвращается первая модель группы, в которой нет ограничения на создание. Если такая
     * модель группы не найдена (или список пуст), то метод возвращает <code>null</code>.
     * @return модель группы. Может быть <code>null</code>
     */
    public GroupModel getGroupToCreateChild(){
        final List<GroupModel> groups = getChildGroupModels();
        for (GroupModel groupModel: groups){
            if (!groupModel.getRestrictions().getIsCreateRestricted()){
                return groupModel;
            }
        }
        return null;
    }
    
    /**
     * Обработчик события создания нового объекта сущности. Метод вызывается при выполнении операции создания дочернего узла дерева.
     * Стандартная реализация для переданной инстанции {@link EntityModel}, 
     * вызывает метод {@link ChildGroupModelSettings#fillNewObjectPropertyValues(EntityModel) fillNewObjectPropertyValues} у всех настроек моделей группы, указанных в конструкторе.
     * @param newObject новый объект сущности
     */
    public void afterPrepareCreateObject(EntityModel newObject) {
        for (ChildGroupModelSettings settings: childGroupModelSettings){
            settings.fillNewObjectPropertyValues(newObject);
        }
    }

    /**
     * Получение информации о наличии объектов в данном наборе.
     * Метод возвращает признак того, что данный набор узлов не является пустым.
     * Стандартная реализация метода работает следующим образом:
     * Если для данного набора уже созданы модели группы, 
     * то возвращается <code>true</code>, когда хотябы одна из них не пустая (метод {@link GroupModel#isEmpty() GroupModel.isEmpty} возвращает <code>false</code> или 
     * метод {@link GroupModel#hasMoreRows() GroupModel.hasMoreRows} возвращает <code>true</code>).
     * Если модели групп еще не созданы, то используются параметры моделей групп, переданные в конструкторе.
     * В этом случае метод возвращает <code>ture</code>, если хотя бы в одном из параметров модели группы 
     * метод {@link ChildGroupModelSettings#groupIsEmpty(EntityModel)  ChildGroupModelSettings.groupIsEmpty} возвращает <code>true</code>,
     * иначе метод вернет <code>false</code>.
     * @return признак того, что данный набор узлов не является пустым
     */
    public boolean hasObjects(){
        if (childGroups==null){
            if (!childGroupModelSettings.isEmpty() && getParentEntityModel()!=null){
                for (ChildGroupModelSettings groupModelSettings: childGroupModelSettings){
                    if (!groupModelSettings.groupIsEmpty(getParentEntityModel())){
                        return true;
                    }
                }
            }
        }
        else{
            for (GroupModel groupModel: getChildGroupModels()){
                if (!groupModel.isEmpty() || groupModel.hasMoreRows()){
                    return true;
                }
            }            
        }
        return false;
    }
    
    /**
     * Получение ограничений для указанной модели группы.
     * Вызывается для каждой модели группы, созданной в методе дерева {@link IRwtSelectorTree#createChildGroupModels(SelectorTreeEntityModelNode) createChildGroupModels}.
     * В стандартной реализации, если в корневой модели группы установлено ограничение на открытие редактора, то
     * метод возвращает контекстные ограничения корневой модели группы плюс ограничение на открытие редактора, иначе
     * метод возвращает только контекстные ограничения корневой модели группы.
     * @param childGroup модель группы, созданная для данного набора узлов
     * @return ограничения модели группы
     */
    protected Restrictions calcChildGroupRestrictions(final GroupModel childGroup){
        if (getRootGroupModel()!=null && getRootGroupModel().getRestrictions().getIsEditorRestricted()){
            //DBP-1681
            return Restrictions.EDITOR_RESTRICTION;
        }
        else{
            return Restrictions.NO_RESTRICTIONS;
        }
    }

    /**
     * Сопоставление колонки селектора свойству объекта сущности. Метод возвращает идентификатор свойства объекта сущности, ассоциированного с узлом-владельцем данного набора, для показа значения в указанной колонке селектора.
     * Метод используется при создании дочерних узлов. 
     * На основе результатов его работы будет заполнена карта соответствия идентификаторов свойств и колонок для передачи в {@link SelectorTreeEntityModelNode#SelectorTreeEntityModelNode(EntityModel, IRwtSelectorTree, Map, int) конструктор SelectorTreeEntityModelNode}.
     * Стандартная реализация использует параметры моделей групп, переданные в конструкторе. Для каждой инстанции настройки вызывается метод
     * {@link ChildGroupModelSettings#getPropertyIdForSelectorColumn(EntityModel, Id) ChildGroupModelSettings.getPropertyIdForSelectorColumn} и возвращается первый результат не равный <code>null</code>. 
     * Если метод  <code>getPropertyIdForSelectorColumn</code> для всех параметров вернул <code>null</code> или набор настроек пуст, то результатом работы метода будет идентификатор колонки селектора.
     * @param entityModel объект сущности, ассоциированный с узлом, которому принадлежит данный набор
     * @param column колонка селектора
     * @return идентификатор свойства
     */
    public Id mapSelectorColumn(final EntityModel entityModel, final SelectorColumnModelItem column) {
        Id propertyId;
        for (ChildGroupModelSettings settings: childGroupModelSettings){
            propertyId = settings.getPropertyIdForSelectorColumn(entityModel, column.getId());
            if (propertyId!=null){
                return propertyId;
            }
        }
        return column.getId();
    }
    
    /**
     * Получение пиктограммы для узла дерева. Метод возвращает пиктограмму для узла из данного набора.
     * Стандартная реализация использует параметры моделей групп, переданные в конструкторе.
     * Если для узла переданного в параметре метод <code>getUserData</code> возвращает инстанцию модели сущности, то для каждого параметра вызывается метод 
     * {@link ChildGroupModelSettings#getObjectIcon(EntityModel) ChildGroupModelSettings.getObjectIcon} и возвращается первый результат не равный <code>null</code>, 
     * иначе метод возвращает <code>null</code>.
     * @param node узел дерева принадлежащий данному набору
     * @return пиктограмма для узла
     */    
    public Icon getNodeIcon(final Node node){
        if (node.getUserData() instanceof EntityModel){
            final EntityModel entityModel = (EntityModel)node.getUserData();
            Icon icon;
            for (ChildGroupModelSettings settings: childGroupModelSettings){
                icon = settings.getObjectIcon(entityModel);
                if (icon!=null){
                    return icon;
                }
            }
        }
        return null;
    }
    
    /**
     * Получение списка узлов, содержащихся в данном наборе. Реализация абстрактного метода <code>Node.Children.createNodes</code>.
     * Метод создает узлы в данном наборе. 
     * Стандартная реализация получает список моделей групп из метода {@link #getChildGroupModels() getChildGroupModels} и 
     * для каждой инстанции {@link EntityModel}, содержащейся в группе создает инстанцию узла {@link SelectorTreeEntityModelNode}.
     * @return список узлов
     */
    @Override
    protected List<Node> createNodes() {
        //verifySelectorColumns
        final List<Node> childNodes = new LinkedList<>();
        for (GroupModel childGroupModel: getChildGroupModels()){
            try{
                readGroupModel(childGroupModel,childNodes);
            }
            catch (ServiceCallFault fault) {
                preprocessServiceCallFault(childGroupModel.getEnvironment(), fault);
            }
            catch (ServiceClientException exception){
                showServiceClientException(childGroupModel.getEnvironment(), exception);
            }
            catch (InterruptedException exception){                
            }
        }
        return childNodes;
    }

    /**
     * Пересоздание узов, содержащихся в данном наборе. Метод очищает список узлов и список моделей групп.
     * Последующее обращение к списку узлов приведет к вызову метода {@link #createNodes() createNodes}.
     */
    @Override
    public final void reset() {        
        if (childGroups!=null){
            for (GroupModel childGroup: childGroups){
                if (!ownerNodeIsRoot()){
                    changeChildGroupContext(childGroup, null);
                }
                childGroup.reset();
                if (!ownerNodeIsRoot()){
                    childGroup.setView(null);
                }
            }
            if (!ownerNodeIsRoot()){
                childGroups = null;
            }
        }
        super.reset();
    }
    
    private boolean ownerNodeIsRoot(){
        if (ownerNode==null){
            for (Node n=getOwnerNode(); n!=null; n=n.getParentNode()){
                if (n instanceof SelectorTreeNode){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    final void close() {
        closed = true;
        reset();        
    }
    
    final void open(){
        closed = false;
    }
    
    private static void changeChildGroupContext(final GroupModel childGroup, final IContext.Group rootGroupContext){
        if (childGroup instanceof ProxyGroupModel==false){
            final IContext.Group context = childGroup.getGroupContext().copy();
            context.setRootGroupContext(rootGroupContext);
            childGroup.setContext(context);
        }
    }    
    
    protected void prepareChildGroupFilter(final GroupModel parentGroup, final GroupModel childGroup){
        if (parentGroup.getCurrentFilter()!=null 
            && parentGroup.getDefinition().getOwnerClassId().equals(childGroup.getDefinition().getOwnerClassId())){
            final FilterModel currentFilter = parentGroup.getCurrentFilter();
            FilterModel childGroupFilter = childGroup.getFilters().findById(currentFilter.getId());
            if (childGroupFilter==null){
                if (currentFilter.isUserDefined()){
                    childGroupFilter = new FilterModel(childGroup.getEnvironment(),currentFilter.getFilterDef());
                    childGroupFilter.setContext(new IContext.Filter(childGroup));
                }else if (currentFilter.isCommon()){
                    final RadCommonFilter commonFilter = (RadCommonFilter)currentFilter.getFilterDef();
                    final RadUserFilter userFilterDef = 
                        commonFilter.convertToUserFilter(childGroup.getEnvironment(), childGroup.getSelectorPresentationDef());
                    childGroupFilter = new FilterModel(childGroup.getEnvironment(), userFilterDef);
                    childGroupFilter.setContext(new IContext.Filter(childGroup));                    
                }else{
                    final String traceMessage = 
                        parentGroup.getEnvironment().getMessageProvider().translate("ExplorerException", "Filter #%1$s was not found in child group model");
                    parentGroup.getEnvironment().getTracer().error(String.format(traceMessage, currentFilter.getDefinition().getId().toString()));
                    return;
                }
            }
            for (Property property: currentFilter.getActiveProperties()){
                childGroupFilter.getProperty(property.getId()).setValueObject(property.getValueObject());
            }
            try{
                childGroup.setFilter(childGroupFilter);
            }catch(PropertyIsMandatoryException | InvalidPropertyValueException | ServiceClientException exception){
                final String traceMessage = 
                    parentGroup.getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to apply filter in child group model");
                parentGroup.getEnvironment().getTracer().error(traceMessage, exception);
            }catch(InterruptedException exception){//NOPMD
                //cancelled by user
            }
        }
    }    
}
