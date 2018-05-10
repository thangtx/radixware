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

package org.radixware.kernel.common.client.editors.traceprofile;

import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.TraceProfile;

/**
 * Интерфейс управления графическим представлением узлов дерева профиля трассировки.
 * Реализация данного интерфейса используется классом {@link TraceProfileTreeController} для создания, удаления и
 * изменения графических компонент, отображающих {@link TraceProfileTreeNode узел дерева профиля трассировки}
 *  в конкретной GUI-библиотеке.
 * @param <T> класс графического компонента, который используется для представления узлов дерева профиля трассировки
 * @see TraceProfileTreeNode
 * @see TraceProfileTreeController
 */
public interface ITraceProfileTreePresenter<T extends IWidget> {
    /**
     * Создает компонент для представления узла дерева профиля трассировки в GUI.
     * Вызывается для новых инстанций {@link TraceProfileTreeNode узлов дерева профиля трассировки}, 
     * созданных в классе {@link TraceProfileTreeController}.
     * @param treeNode узел дерева профиля трассировки
     * @return новая инстанция графического компонента
     */
    T createTreeNodeWidget(final TraceProfileTreeNode<T> treeNode);
    /**
     * Обновляет графический компонент для представления узла дерева профиля трассировки в GUI.
     * Вызывается, когда в {@link TraceProfileTreeNode узле дерева профиля трассировки} был изменен текущий
     * {@link TraceProfileTreeController.EventSeverity уровень важности сообщения} или 
     * заголовок источника событий или состояние {@link TraceProfileTreeNode#isReadOnly()}
     * и это необходимо отразить в графическом компоненте.
     * @param childNode узел дерева профиля трассировки
     */
    void presentWidget(final TraceProfileTreeNode<T> childNode);
    /**
     * Удаляет графические компоненты. 
     * Метод освобождает ресурсы, занятые графическими компонентами, созданными методом {@link #createTreeNodeWidget(TraceProfileTreeNode) }
     * Вызывается из метода {@link TraceProfileTreeController#rereadEventSources()  rereadEventSources}.
     */
    void destroyPresentations();

    /**
     * Создает диалог редактирования дополнительных настроек трассировки для заданного источника событий.
     * Вызывается при двойном клике по ячейке в колонке дополнительных настроек дерева профиля трассировки.
     * @param eventSource источник события, для которого нужно открыть диалог дополнительных настроек трассировки
     * @param eventSeverity текущий уровень трассировки для данного источника событий
     * @param options текущие значения дополнительных настроек трассировки для данного источника событий
     * @return инстанция диалога редактирования дополнительных настроек трассировки или <code>null</code> 
     * если для указанного источника событий нет дополнительных настроек трассировки.
     */
    ITraceProfileEventSourceOptionsEditor createEventSourceOptionsEditor(final EEventSource eventSource, final EEventSeverity eventSeverity, final TraceProfile.EventSourceOptions options);
}