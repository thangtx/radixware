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
import org.radixware.kernel.common.types.Id;

/**
 * Интерфейс поставщика списка параметров создания дочерних моделей групп (Radix::Web.Widgets.SelectorTree::IChildGroupModelSettingsProvider).
 * Если модель сущности или модель группы реализует этот интерфейс, 
 * то она может использоваться в древовидном селекторе для получения списка параметров создания дочерних моделей групп.
 * Модели группы, созданные по этим параметрам, будут использованы для генерации {@link SelectorTreeEntityModelNode стандартных узлов дерева, ассоциированных с моделью сущности}.
 */
public interface IChildGroupModelSettingsProvider {
    /**
     * Получeние списка параметров создания моделей групп. Метод для переданного узла в дерева и соответствующей ему модели сущности 
     * возвращает список параметров создания дочерних моделей групп.
     * @param selectorPresentationId идентификатор презентации, в которой создан древовидный селектор
     * @param treeNode узел дерева, для которого необходимо создать набор дочерних подузлов
     * @param nearestEntityModel модель сущности, соответствующая узлу дерева <code>treeNode</code>. 
     * Если с этим узлом модель сущности не была ассоциирована, то передается модель сущности ассоциированная с ближайшим узлом верхнего уровня.
     * @return список параметров моделей групп. Метод может возвращать <code>null</code>
     */
    List<ChildGroupModelSettings> getChildGroupModelSettings(final Id selectorPresentationId, final SelectorTreeNode treeNode, final EntityModel nearestEntityModel);
}