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

package org.radixware.kernel.common.client.widgets.selector;

import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceClientException;

/**
 * Интерфейс виджета селектора. Класс реализующий, этот интерфейс
 * используется для отображения данных селектора. <p>Виджет регистрируется
 * в селекторе вызовом метода
 * {@link org.radixware.kernel.explorer.views.Selector#setSelectorWidget(ISelectorWidget) setSelectorWidget(ISelectorWidget)}.
 * В этом методе будут вызваны {@link IWidget#bind() bind()} и {@link #setupSelectorToolBar(QToolBar)}.
 * В селекторе может быть зарегестрирован только один виджет селектора. После регистрации
 * при наступлении определенных событий селектор начнет вызывать соответствующие методы виджета.
 *
 */
public interface ISelectorWidget extends IModelWidget {
    
    /**
     * Запретить обработку сообщений мыши и клавиатуры в виджете.
     * Может использоваться, например, при смене текущей сущности селектора.
     * @see #unlockInput()
     */
    public void lockInput();

    /**
     * Разрешить обработку сообщений мыши и клавиатуры в виджете.
     * Может использоваться, например, при смене текущей сущности селектора.
     * @see #lockInput()
     */
    public void unlockInput();

    /**
     * Уведомляет виджет о необходимости обновить данные модели.
     */
    public void finishEdit();

    /**
     * Уведомляет виджет, о необходимости перечитать данные в селекторе с восстановлением заданной позиции.
     * <p>Для получения актуальных данных из модели группы
     * реализующий класс должен вызывать метод {@link org.radixware.kernel.explorer.models.GroupModel#reread()}.
     * <p>Вызывается при после создании новой сущности
     * (тогда pid - идентификатор созданной сущности), после выполнения операции
     * перечитывания в селекторе (тогда pid равен null).
     * В стандартной реализации если параметр равен null, то перечитываются все данные с восстановлением
     * текущей сущности, иначе перечитываются данные только текущей группы с позиционированием на
     * сущности с заданным идентификатором.
     * @param pid - идентификатор сущности, которая после перечитывания должна стать текущей.
     */
    public void rereadAndSetCurrent(Pid pid) throws InterruptedException, ServiceClientException;

    /**
     * Уведомляет виджет, о необходимости перечитать данные в селекторе без восстановления текущей позиции.
     * <p>Для получения актуальных данных из модели группы
     * реализующий класс должен вызывать метод {@link org.radixware.kernel.explorer.models.GroupModel#reread()}.
     * <p>
     * Вызывается после применения фильтра или сортировки.
     * В стандартной реализации после перечитывания текущей становится первая сущность в модели селектора
     */
    public void reread() throws InterruptedException, ServiceClientException;

    /**
     *Уведомляет виджет о том, что данные предоставляемые моделью группы стали неактуальны.
     *Вызывается при смене смене текущего фильтра в селекторе, но до его применения в группе.
     */
    public void clear();

    /**
     * Уведомляет виджет, о том, что сущность с идентификатором pid была удалена.
     * @param pid - идентификатор сущности, которая была удалена.
     */
    public void entityRemoved(Pid pid);

    /**
     * Вызывается при отображении главного меню селектора, после добавления в него
     * действий селектора
     * @param menu - меню, содержащее действия селектора.
     */
    public void setupSelectorMenu(IMenu menu);

    /**
     * Вызывается при установки виджета в селектор. Дает возможность добавить
     * действия виджета на панель инструментов селектора.
     * @param toolBar - панель инструментов селектора.
     */
    public void setupSelectorToolBar(IToolBar toolBar);

    /**
     * Вызывается при созданиии новой сущности после выполнения
     * запроса PrepareCreate, но до показа диалога редактирования.
     * Позволяет задать значения свойств создаваемой  сущности
     * @param entity - создаваемая сущность
     */
    public void afterPrepareCreate(EntityModel childEntity);
    
}
