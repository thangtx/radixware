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

import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.tree.Node;

/**
 * Интерфейс виджета древовидного селектора (Radix::Web.Widgets.SelectorTree::ISelectorTree).
 * Методы этого интерфейса отвечают за построение древовидной структуры узлов.
 * Используется стандартными узлами и наборами узлов дерева селектора, для получения корневой модели группы и информации о дочерних узлах.
 */
public interface IRwtSelectorTree {
    /**
     * Получение корневой модели группы. Метод возвращает корневую модель группы.
     * @return корневая модель группы
     */
    GroupModel getRootGroupModel();
    
    /**
     * Получение списка колонок селектора. Метод возвращает список калонок селектора.
     * @return колонки селектора
     */
    List<SelectorColumnModelItem> getSelectorColumns();
    
    /**
     * Получение информации о наличии дочерних узлов. Метод предоставляет информацию о наличии вложенных узлов у заданного узла древовидного селектора.
     * Если метод возвращает <code>false</code>, то узел, переданный в параметре, считается конечным и не может быть раскрыт. 
     * В этом случае узлу <code>parentNode</code> будет установлен набор дочерних элементов {@link org.radixware.wps.rwt.tree.Node.Children.Leaf}.
     * @param parentNode узел в дереве селектора
     * @return <code>false</code>, если для переданного узла не существует дочерних узлов, иначе <code>true</code>
     */
    boolean hasChildNodes(SelectorTreeNode parentNode);
    
    /**
     * Создание подузлов дерева. У переданного узла в древовидном селекторе метод настраивает набор дочерних элементов.
     * @param parentNode узел в дереве селектора
     */
    void initChildren(SelectorTreeNode parentNode);
    
    /**
     * Создание дочерних моделей групп. Для переданного узла древовидного селектора метод возвращает список моделей групп.
     * На основе объектов, содержащихся в этих группах, будут созданы подузлы.
     * Дочерние модели группы также используются в операции создания подобъектов.
     * @param parentNode узел в дереве селектора
     * @param parentEntityModel модель объекта сущности, ассоциированная с данным узлом
     * @return список дочерних моделей групп
     * @throws InterruptedException - генерируется при отмене операции получения списка дочерних моделей групп
     * @throws ServiceClientException - генерируется при возникновении ошибок, связанных с получением дочерних моделей групп
     */
    List<GroupModel> createChildGroupModels(SelectorTreeNode parentNode, EntityModel parentEntityModel) throws InterruptedException, ServiceClientException;
    
    /**
     * Сопоставление колонки селектора свойству из модели сущности.
     * Если метод вернет <code>null</code>, то соответствующая ячейка в древовидном селекторе будет пустой.
     * @param column колонка селектора
     * @param parentNode родительский узел в дереве селектора. Для узлов первого уровня равен <code>null</code>.
     * @param childGroupModel модель группы, которая содержит модель сущности
     * @param childEntityModel модель сущности-владельца свойства
     * @return идентификатор свойства в модели сущности, соответствующего переданной колонки селектора
     */
    public Id mapSelectorColumn(SelectorColumnModelItem column, SelectorTreeNode parentNode, GroupModel childGroupModel, EntityModel childEntityModel);
    
    /**
     * Получение пиктограммы для узла дерева.
     * Пиктограмма будет показана в первой колонке древовидного селектора.
     * @param parentNode родительский узел в дереве селектора
     * @param childNode узел, которому нужно сопоставить пиктограмму
     * @return пиктограмма для узла <code>childNode</code>
     */
    public Icon getNodeIcon(SelectorTreeNode parentNode, Node childNode);
    
    /**
     * Обработчмк событий раскрытия и свертывания узла.
     * @param node узел дерева, который был раскрыт или свернут
     */
    public void onNodeStateChanged(Node node);
}