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

import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Tree;

/**
 * Стандартный узел древовидного селектора (Radix::Web.Widgets.SelectorTree::SelectorTreeNode).
 * Если набор вложенных узлов не был передан в конструкторе, то при регистрации узла в дереве (вызове метода <code>setParent</code>) 
 * происходит формирование узлов следующего уровня.
 * Для этого вызывается метод деревовидного селектора {@link IRwtSelectorTree#initChildren(SelectorTreeNode) initChildren}.
 * Узел считается конечным если метод дерева {@link IRwtSelectorTree#hasChildNodes(SelectorTreeNode)  hasChildNodes()} возвращает для него <code>false</code>.
 */
public class SelectorTreeNode extends Node.DefaultNode{
    
    private final IRwtSelectorTree tree;
    private int level = -1;
    private boolean doInitChildren = false;
    private Node.Children children;
    
    /**
     * @param displayName строка для показа в первой ячейке. Может быть <code>null</code>
     * @param icon пиктограмма для показа в первой ячейке. Может быть <code>null</code>
     * @param children набор вложенных узлов. Может быть <code>null</code>
     * @param selectorTree дерево селектора
     */
    public SelectorTreeNode(final String displayName, final WpsIcon icon, final Children children, final IRwtSelectorTree selectorTree) {
        super(displayName, icon, children);
        tree = selectorTree;
        this.children = children;
    }        
    
    /**
     * @param displayName строка для показа в первой ячейке. Может быть <code>null</code>
     * @param children набор вложенных узлов. Может быть <code>null</code>
     * @param selectorTree дерево селектора
     */
    public SelectorTreeNode(final String displayName, final Children children, final IRwtSelectorTree selectorTree) {
        this(displayName, null, children, selectorTree);
    }    

    /**
     * @param children набор вложенных узлов. Может быть <code>null</code>
     * @param selectorTree дерево селектора
     */
    public SelectorTreeNode(final Children children, final IRwtSelectorTree selectorTree) {
        this(null, null, children, selectorTree);
    }            

    /**
     * @param displayName строка для показа в первой ячейке. Может быть <code>null</code>
     * @param icon пиктограмма для показа в первой ячейке. Может быть <code>null</code>
     * @param selectorTree дерево селектора
     */
    public SelectorTreeNode(final String displayName, final WpsIcon icon, final IRwtSelectorTree selectorTree) {
        this(displayName,icon,null,selectorTree);
    }

    /**
     * @param displayName строка для показа в первой ячейке. Может быть <code>null</code>
     * @param selectorTree дерево селектора
     */    
    public SelectorTreeNode(final String displayName, final IRwtSelectorTree selectorTree) {
        this(displayName, null, null, selectorTree);
    }    
   
    /**
     * @param selectorTree дерево селектора
     */
    public SelectorTreeNode(final IRwtSelectorTree selectorTree){
        this(null,null,null,selectorTree);  
    }        
    
    /**
     * Получение инстанции виджета древовидного селектора. Метод возвращает интерфейс древовидного селектора, переданный в конструкторе.
     * @return интерфейс древовидного селектора
     */
    protected final IRwtSelectorTree getSelectorTree(){
        return tree;
    }

    /**
     * Получение набора вложенных узлов. Метод возвращает набор вложенных узлов, переданный в конструкторе или 
     * установленный в методе древовидного селектора {@link IRwtSelectorTree#initChildren(SelectorTreeNode) initChildren}.
     * @return набор вложенных узлов
     */
    protected final Node.Children getChildren(){
        return children;
    }

    /**
     * Установка графического объекта-владельца. Метод вызывается при регистрации данного узла в дереве.
     * После вызова метода базового класса происходит формирование набора вложенных узлов.
     * Если он не был установлен ранее, то вызывается метод деревовидного селектора {@link IRwtSelectorTree#initChildren(SelectorTreeNode) initChildren}.
     * Узел считается конечным если метод дерева {@link IRwtSelectorTree#hasChildNodes(SelectorTreeNode)  hasChildNodes()} возвращает для него <code>false</code>.    
     * @param parent графический объект-владелец данного узла
     */
    @Override
    public void setParent(final UIObject parent) {
        super.setParent(parent);    
        if (parent!=null){
            if (parent instanceof SelectorTreeNode){
                level = ((SelectorTreeNode)parent).getLevel()+1;
            }
            else{
                level = 0;
                for (UIObject node=parent; node instanceof Node; node=node.getParent()){
                    level++;
                }
            }
            if (getChildNodes()!=Node.Children.LEAF && getChildNodes()!=null){
                children = getChildNodes();
            }
            else{
                initChildren();
            }                
        }
    }
    
    /**
     * Получение уровня вложенности. Метод возвращает уровень вложенности данного узла дерева.
     * За нулевой уровень берется уровень неотображаемого корневого узла дерева.
     * @return уровень вложенности
     */
    public final int getLevel(){
        return level;
    }
    
    private void initChildren(){
        doInitChildren = true;
        try{
            try{
                tree.initChildren(this);
            }
            catch(Exception exception){
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child nodes for %s");
                getEnvironment().getTracer().error(String.format(title, getDescription()), exception);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, getDescription())+":\n"+
                        ClientException.exceptionStackToString(exception),
                        EEventSource.EXPLORER);                
            }            
            children = getChildNodes();
            if (children instanceof SelectorTreeChildNodes){
                ((SelectorTreeChildNodes)children).setOwnerNode(this);
                ((SelectorTreeChildNodes)children).open();
            }

            try{
                if (!tree.hasChildNodes(this)){
                    setChildNodes(Children.LEAF);
                }
            }
            catch(Exception exception){
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to create child nodes for %s");
                getEnvironment().getTracer().error(String.format(title, getDescription()), exception);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, getDescription())+":\n"+
                        ClientException.exceptionStackToString(exception),
                        EEventSource.EXPLORER);                        
            }
        }
        finally{
            doInitChildren = false;
        }
    }
    
    /**
     * Получение информации о наличии подузлов. 
     * Метод возвращает признак того, что для данного узла имеется набор узлов следующего уровня вложенности.
     * Метод возвращает <code>false</code> когда данный узел дерева является конечным (не может быть раскрыт) и <code>true</code> в противном случае.
     * Стандартная реализация не требует наличия инстанций моделей групп, содержащих подобъекты
     * и использует набор дочерних элементов, полученный из вызовом метода {@link #getChildren() getChildren}.
     * Если этот набор является инстанцией {@link  SelectorTreeChildNodes}, 
     * то возвращается результат работы метода {@link SelectorTreeChildNodes#hasObjects() SelectorTreeChildNodes.hasObjects()},
     * иначе, метод возвращает <code>false</code>, если текущий набор дочерних элементов представляет собой <code>Node.Children.LEAF</code> или равен <code>null</code> и <code>true</code>, если нет.
     * @return <code>false</code> когда данный узел дерева не содержит подузлов, иначе - <code>true</code>
     */
    public boolean hasChildNodes(){
        if (getChildren() instanceof SelectorTreeChildNodes){
            return ((SelectorTreeChildNodes)getChildren()).hasObjects();
        }
        else{
            return getChildren()!=null && getChildren()!=Node.Children.LEAF;
        }
    }
    
    /**
     * Сопоставление колонки селектора свойству объекта сущности. Метод возвращает идентификатор свойства объекта сущности, ассоциированного с узлом следующего уровня вложенности, для показа значения в указанной колонке селектора.
     * Метод используется при создании дочерних узлов. 
     * На основе результатов его работы будет заполнена карта соответствия идентификаторов свойств и колонок 
     * для передачи в {@link SelectorTreeEntityModelNode#SelectorTreeEntityModelNode(EntityModel, IRwtSelectorTree, Map, int)  конструктор SelectorTreeEntityModelNode}.
     * Стандартная реализация использует набор дочерних элементов, полученный из вызовом метода {@link #getChildren() getChildren}.
     * Если этот набор является инстанцией {@link SelectorTreeChildNodes}, то возвращается результат работы метода {@link SelectorTreeChildNodes#mapSelectorColumn(EntityModel, SelectorColumnModelItem) SelectorTreeChildNodes.mapSelectorColumn()},
     * иначе метод возвращает идентификатор колонки.
     * @param childEntityModel модель сущности для дочернего узла
     * @param column колонка селектора
     * @return идентификатор свойства
     */
    public Id mapSelectorColumnToChildProperty(final EntityModel childEntityModel, final SelectorColumnModelItem column){
        if (getChildren() instanceof SelectorTreeChildNodes){
            return ((SelectorTreeChildNodes)getChildren()).mapSelectorColumn(childEntityModel, column);
        }
        else{
            return column.getId();
        }        
    }    
    
    /**
     * Получение пиктограммы для подузла. Метод возвращает пиктограмму для узла следующего уровня вложенности.
     * Стандартная реализация использует набор дочерних элементов, полученный из вызовом метода {@link #getChildren() getChildren}.
     * Если этот набор является инстанцией {@link SelectorTreeChildNodes}, то возвращается результат работы метода {@link SelectorTreeChildNodes#getNodeIcon(Node) SelectorTreeChildNodes.getNodeIcon()},
     * иначе метод возвращает <code>null</code>.
     * @param childNode дочерний узел
     * @return пиктограмма
     */
    public Icon getChildNodeIcon(final Node childNode){
        if (getChildren() instanceof SelectorTreeChildNodes){
            return ((SelectorTreeChildNodes)getChildren()).getNodeIcon(childNode);
        }
        else{
            return null;
        }        
    }

    /**
     * Получение дочерней модели группы для создания объекта сущности. Метод возвращает модель группы, которая будет использоваться при создании объекта.
     * Данный метод используется во время выполнения операции создания дочернего узла дерева. 
     * Стандартная реализация использует набор дочерних элементов, полученный из вызовом метода {@link #getChildren() getChildren}.
     * Если этот набор является инстанцией {@link SelectorTreeChildNodes}, 
     * то возвращается результат работы метода {@link SelectorTreeChildNodes#getGroupToCreateChild() SelectorTreeChildNodes.getGroupToCreateChild()},
     * иначе метод возвращает <code>null</code>.
     * @return модель группы для выполнения операции создания дочернего объекта сущности или <code>null</code>, если данная операция не может быть выполнена
     */
    public GroupModel getGroupModelToCreateChildObject() {
        if (getChildren() instanceof SelectorTreeChildNodes){
            return ((SelectorTreeChildNodes)getChildren()).getGroupToCreateChild();
        }
        else{
            return null;
        }
    }
    
    /**
     * Обработчик события создания нового объекта сущности. Метод вызывается при выполнении операции создания дочернего узла дерева.
     * Стандартная реализация использует набор дочерних элементов, полученный из вызовом метода {@link #getChildren() getChildren}.
     * Если этот набор является инстанцией {@link SelectorTreeChildNodes}, то у нее будет вызван метод {@link SelectorTreeChildNodes#afterPrepareCreateObject(EntityModel)  afterPrepareCreateObject}.
     * @param childObject инстанция нового объекта сущности
     */
    public void afterPrepareCreateChildObject(final EntityModel childObject) {
        if (getChildren() instanceof SelectorTreeChildNodes){
            ((SelectorTreeChildNodes)getChildren()).afterPrepareCreateObject(childObject);
        }
    }
    
    /**
     * Пересоздание подузлов. Метод пересоздает набор узлов следующего уровня.
     * Стандартная реализация проверяет является ли текущий набор вложенных узлов инстанцией класса {@link SelectorTreeChildNodes}, 
     * и если является, то у нее вызывается метод {@link SelectorTreeChildNodes#close() close}.
     * Затем формируется новый набор в методе дерева {@link IRwtSelectorTree#initChildren(SelectorTreeNode)   createChildren}.
     */
    public void rereadChildren(){
        if (children instanceof SelectorTreeChildNodes){
            ((SelectorTreeChildNodes)children).close();
        }
        initChildren();
    }
    
    /**
     * Получение строки с описанием данного узла.
     * @return Возвращает строку, содержащую результат вызова метода <code>getDisplayName()</code>.
     */
    public String getDescription(){
        return String.format(getEnvironment().getMessageProvider().translate("Selector", "node \'%s\'"), getDisplayName());
    }    
    
    @Override
    public Tree getTree() {
        return doInitChildren ? null : super.getTree();
    }        
        
    @Override
    public void expand() {
        super.expand();
        if (isExpanded() && !isLeaf() && getChildCount()==0){
            setChildNodes(Children.LEAF);
            collapse();
        }
        tree.onNodeStateChanged(this);
    }

    @Override
    public void toggle() {
        super.toggle();
        if (isExpanded() && !isLeaf() && getChildCount()==0){
            setChildNodes(Children.LEAF);
            collapse();
        }        
        tree.onNodeStateChanged(this);
    }

    @Override
    public void collapse() {
        super.collapse();
        tree.onNodeStateChanged(this);
    }
    
    
}
