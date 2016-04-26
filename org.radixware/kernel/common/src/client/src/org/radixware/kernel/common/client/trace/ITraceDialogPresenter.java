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

package org.radixware.kernel.common.client.trace;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;

/**
 *Интерфейс управления графическим представлением диалога трассы.
 *Реализация данного интерфейса используется инстанцией {@link TraceDialogController} 
 *для работы с элементами пользовательского интерфейса в диалоге трассы.
 * @see TraceDialogController
 */
public interface ITraceDialogPresenter {
    /**
     * Создает инстанцию редактора профиля трассировки.
     * Метод создает отображаемый компонент редактора профиля трассировки в конкретной GUI-библиотеке и
     * размещает его в диалоге трассы.
     * @param environment инстанция окружения клиента
     * @return новая инстанция виджета редактора профиля трассировки. Не может быть null
     */
    ITraceProfileEditor createTraceProfileEditor(IClientEnvironment environment);
    /**
     * Устанавливает доступность редактора профиля трассировки.
     * Метод вызывается инстанцией {@link TraceDialogController} в тех случаях, когда необходимо
     * изменить доступность компонента редактора профиля трассировки для пользователя.
     * @param isEnabled <code>true</code> если компонент редактора профиля трассировки в настоящий момент должен 
     * быть доступен пользователю, <code>false</code>
     */
    void setProfileEditorEnabled(boolean isEnabled);
}