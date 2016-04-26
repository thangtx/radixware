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

import java.util.Collection;
import org.radixware.kernel.common.client.widgets.IWidget;

/**
 * Интерфейс редактора профиля трассировки (Radix::Client.Views::ITraceProfileEditor).
 * Редактор профиля трассировки позволяет для источника событий установить минимальный уровень важности сообщения,
 * при котором текст этого сообщения должен сохраниться в трассе.
 * <p>
 * Набор источников событий имеет древовидную структуру.
 * Если для события уровень важности сообщения не был явно задан с помощью вызова метода {@link #setTraceProfile(java.lang.String) setTraceProfile} 
 * или при помощи GUI-средств редактора, то считается, что он такой же как и у источника событий, расположенного уровнем выше по иерархии (т.е. наследуется).
 * Редактор позволяет задать уровень важности сообщений, который будет использоваться по умолчанию для источников событий верхнего уровня.
 * </p>
 * Для получения набора источников событий используется дефиниция набора перечислений Radix::Arte::EventSource.
 * Для получения набора уровней важности событий используется дефиниция набора перечислений Radix::Arte::EventSeverity.
 */
public interface ITraceProfileEditor extends IWidget{

    /**
     * Интерфейс обработчика изменения уровня важности события пользователем (Radix::Client.Views::ITraceProfileEditor.IEventSeverityChangeListener).
     */
    public static interface IEventSeverityChangeListener{
        /**
         * Обрабатывает изменения уровня важности события пользователем.
         * Этот метод вызывается когда пользователь изменяет уровень важности события в редакторе профиля трассировки.
         * Обработчик не вызывается во время программного изменения уровня важности в методах 
         * {@link #setTraceProfile(java.lang.String) setTraceProfile} и {@link #setRestrictedEventSources(java.util.Collection) setRestrictedEventSources}.
         * @param eventSource имя источника событий или <code>null</code>, если изменяется
         * минимальный уровень важности сообщения, используемый источниками событий верхнего уровня по умолчанию.
         * @param newEventSeverity новый уровень важности события или <code>null</code>, если
         * для данного источника событий теперь будет использоваться значение уровеня важности, взятое из источника событий верхнего уровня.
         */
        void afterChangeEventSeverity(final String eventSource, final String newEventSeverity, final String options);
    }
    
    /**
     * Устанавливает профиль трассировки. Метод позволяет задать текущие значения уровней важности событий.
     * В параметре передается строка, в которой указаны имена источников событий и уровень важности их сообщений.
     * Если для источника событий уровень важности сообщения не был указан явно, то его значение берется из первого
     * вышестоящего источника событий с явно указанным уровнем важности. 
     * В начале строки указывается уровень важности по умолчанию для источников событий верхнего уровня иерархии.
     * Общий формат строки с профилем трассировки:
     * <p>
     * {@code <Default event severity name>[;<event source name>=<event severity name>[;<event source name>=<event severity name>[;...]]]}
     * </p>
     * Пример строки с профилем трассировки:
     * <p>
     * {@code Error;Arte.DefManager=Debug}
     * </p>
     * Сразу после вызова данного метода, метод {@link #isEdited() isEdited} будет возвращать <code>false</code>.
     * Если в качестве параметра передано <code>null</code> или пустая строка, то уровень важности сообщений 
     * у всех источников событий будет сброшен (отнаследован), а уровень важности сообщений, который используется по умолчанию
     * источниками событий верхнего уровня, будет принят равным {@link org.radixware.kernel.common.enums.EEventSeverity.EEventSeverity#NONE "None"}.
     * Неизвестные источники событий пропускаются.
     * @param traceProfileAsStr строковое представление профиля трассировки.
     * Значение равное <code>null</code> или <code>""</code> равносильно значению <code>"None"</code>.
     * @throws  org.radixware.kernel.common.exceptions.WrongFormatError если переданная строка имеет неправильный формат или 
     * не найдено описание для указанного в ней уровеня важности сообщения.
     * @see #getTraceProfile()
     */
    void setTraceProfile(final String traceProfileAsStr);
    /**
     * Возвращает строковое представление профиля трассировки. Метод позволяет получить текущие значения уровней важности событий, явно заданные
     * с помощью вызова метода {@link #setTraceProfile(java.lang.String) setTraceProfile} или при помощи GUI-средств редактора.
     * В начале строки всегда указан уровень важности по умолчанию для источников событий верхнего уровня иерархии.
     * Общий формат строки с профилем трассировки:
     * <p>
     * {@code <Default event severity name>[;<event source name>=<event severity name>[;<event source name>=<event severity name>[;...]]]}
     * </p>
     * Пример строки с профилем трассировки:
     * <p>
     * {@code Error;Arte.DefManager=Debug}
     * </p>
     * @return строковое представление профиля трассировки. Значение не может быть <code>null</code> или пустой строкой.
     * @see #setTraceProfile(java.lang.String) 
     */
    String getTraceProfile();
    
    /**
     * Возвращает признак того, что уровень важности сообщения был изменен пользователем.
     * @return <code>true</code>, если пользователь изменил уровень важности события для хотябы одного источника, иначе - <code>false</code>
     */
    boolean isEdited();

    /**
     * Добавляет обработчик изменения уровня важности события пользователем.
     * @param listener инстанция обработчика, который необходимо зарегистрировать
     * @see #removeListener(IEventSeverityChangeListener) 
     */
    void addListener(IEventSeverityChangeListener listener);
    /**
     * Удаляет обработчик изменения уровня важности события пользователем.
     * @param listener инстанция обработчика, зарегистрированного в методе {@link #addListener(IEventSeverityChangeListener) }
     * @see #addListener(IEventSeverityChangeListener) 
     */
    void removeListener(IEventSeverityChangeListener listener);

    /**
     * Устанавливает режим "только чтение".
     * Если переданный параметр равен <code>true</code>, то пользователь не сможет изменять текущие значения уровней важности событий.
     * @param isReadOnly логическое значение включающее или выключающее режим "только чтение"
     * @see #isReadOnly()
     */
    void setReadOnly(boolean isReadOnly);
    /**
     * Возвращает признак работы редактора в режиме "только чтение"
     * @return <code>true</code> если редактор находится в режиме "только чтение" и <code>false</code> в противном случае.
     * @see #setReadOnly(boolean) 
     */
    boolean isReadOnly();

    /**
     * Устанавливает набор источников событий, с запрещенной трассировкой. 
     * Метод позволяет указать источники событий, сообщения от которых не должны попадать в трассу.
     * Для всех источников событий, имена которых указаны в наборе, уровень важности событий будет установлен равным
     * {@link org.radixware.kernel.common.enums.EEventSeverity.EEventSeverity#NONE None (без трассировки)} и запрещено его изменение,
     * у всех вложенных источников событий уровень важности будет помечен как унаследованный и его редактирование также будет запрещено.
     * Вызов этого метода не влияет на результат, возвращаемый методом {@link #isEdited() isEdited}.
     * @param eventSources набор c именами источников событий
     */
    void setRestrictedEventSources(Collection<String> eventSources);
    
    /**
     * Возвращает набор имен всех источников событий, отображаемых на данный момент в редакторе.
     * @return набор имен всех источников событий
     */
    Collection<String> getEventSources();
    
    /**
     * Обновляет текущий набор источников событий. 
     * Метод перечитывает набор источников событий, который мог измениться в результате подъема версии.
     * Текущий набор источников событий, для которых трассировка запрещена, остается без изменений.
     * В параметре метода можно указать новый профиль трассировки, который будет установлен после обновления набора источников событий.
     * Если параметр равен <codr>null</code>, то текущие значения уровней важности событий остаются без изменений.
     * @param profileAsStr строковое представление профиля трассировки, который следует установить после обновления набора источников событий.
     * Если значение равно <codr>null</code>, то будет установлен тот профиль трассировки, который был на момент вызова метода.
     */
    void rereadEventSources(String profileAsStr);
}
