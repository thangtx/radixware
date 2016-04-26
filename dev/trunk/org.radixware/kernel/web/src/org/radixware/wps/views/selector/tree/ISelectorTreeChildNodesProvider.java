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

/**
 * Интерфейс поставщика списка стандартных узлов древовидного селектора (Radix::Web.Widgets.SelectorTree::IChildGroupModelSettingsProvider).
 * Если модель сущности или модель группы реализуют данный интерфейс, 
 * то они могут быть использованы в древовидном селекторе при формировании набора дочерних узлов.
 */
public interface ISelectorTreeChildNodesProvider {
    /**
     * Создание списка дочерних узлов. Метод для узла <code>parentNode</code> в древовидном селектора <code>tree</code> возвращает список его вложенных узлов.
     * @param tree виджет древовидного селектора
     * @param parentNode узел древовидного селектора. Если равен <code>null</code>, то требуется сформировать список узлов первого уровня
     * @return список дочерних узлов. Может быть <code>null</code>
     */
    List<SelectorTreeNode> createChildNodes(final IRwtSelectorTree tree, final SelectorTreeNode parentNode);
}
