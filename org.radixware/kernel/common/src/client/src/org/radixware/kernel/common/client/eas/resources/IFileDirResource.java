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

package org.radixware.kernel.common.client.eas.resources;

import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EFileDirReadAttributes;
import org.radixware.kernel.common.enums.EFileDirReadProperties;
import org.radixware.kernel.common.enums.EFileDirReadSortMode;
import org.radixware.kernel.common.enums.EFileDirReadSortOptions;
import org.radixware.schemas.eas.FileDirReadItem;

/**
 * Интерфейс для работы с локальными каталогами.
 * Используется для обработки EAS callback-запросов выполнения операций с клиентскими каталогами
 * @see org.radixware.kernel.server.arte.resources.FileDirResource
 */
public interface IFileDirResource{
    
    /**
     * Вызов системного диалога выбора каталога.
     * @param environment инстанция клиентского окружения. Не может быть <code>null</code>.
     * @param title заголовок диалога
     * @param dirName полный путь к начальному каталогу
     * @return полный путь к каталогу или <code>null</code> если пользователь отказался от выбора
     */    
    String select(final IClientEnvironment environment, final  String title, final String dirName);

    /**
     * Получение элементов каталога.
     * Метод предоставляет информацию о файлах и/или подкаталогах, находящихся внутри данного каталога (нерекурсивно).
     * @param dirName полный путь к каталогу, содержимое которого требуется получить. Не может быть <code>null</code>.
     * @param attribFilter набор фильтров на тип файла (обычный файл или каталог). Если этот параметр не равен <code>null</code>, 
     * то результат работы метода будет содержать информацию только о тех элементах, тип которых содержится в данном наборе.
     * Если этот параметр представляет собой пустой набор, то метод вернет пустой список.
     * @param mask маска элементов каталога. Позволяет отфильтровать элементы каталога по имени. Если этот параметр не равен <code>null</code>, 
     * то результат работы метода будет содержать информацию только о тех элементах, имя которых соответствует указанному шаблону.
     * Шаблон должен быть в формате {@link java.nio.file.FileSystem#getPathMatcher(java.lang.String) "glob"}. 
     * @param readProps набор свойств файла, значения которых необходимо получить. Если этот параметр равен <code>null</code>
     * или является пустым набором, то результат работы метода будет содержать только имена элементов.
     * @param sortBy свойство файла, по значению которого должна производиться сортировка результатов. 
     * Если этот параметр равен <code>null</code>, то порядок следования элементов в результирующем списке не определен.
     * @param sortOptions набор дополнительных настроек сортировки элементов. Если этот параметр равен <code>null</code>
     * или является пустым набором, то элементы сортируются (при условии что задан sortBy) по возрастанию без учета регистра символов.
     * @return список элементов, содержащих информацию о файлах и/или подкаталогах, находящихся внутри указанного каталога.
     * Порядок следования элементов определяется параметрами sortBy и sortOptions. Не может быть <code>null</code>.
     * @throws IOException ошибка ввода/вывода
     */   
    List<FileDirReadItem> read(final String dirName,
                               final List<EFileDirReadAttributes> attribFilter,
                               final String mask,
                               final List<EFileDirReadProperties> readProps,
                               final EFileDirReadSortMode sortBy,
                               final List<EFileDirReadSortOptions> sortOptions) throws IOException;

     /**
     * Создание каталога.
     * Метод позволяет создать каталог по указанному пути, включая все необходимые, но
     * несуществующие каталоги верхнего уровня.
     * @param dirName полный путь к создаваемому каталогу. Не может быть <code>null</code>.
     * @throws IOException ошибка ввода/вывода
     */
    void create(final String dirName) throws IOException;
    
    /**
     * Удаление каталога.
     * Метод позволяет удалить указанный каталог.
     * @param dirName полный путь к удаляемому каталогу. Не может быть <code>null</code>.
     * @return возвращает <code>true</code>, если операция завершилась успешно. и <code>false</code> если указанный каталог не существует.
     * @throws IOException ошибка ввода/вывода
     */
    boolean delete(final String dirName) throws IOException;

    /**
     * Перемещение (переименование) каталога.
     * Метод позволяет изменить путь к указанному каталогу. 
     * После успешного выполнения метода каталог будет доступен по новому пути. Содержимое каталога сохраняется.
     * Если каталог, путь к которому указан в newDirPath, уже существует, то выполнение метода завершится ошибкой.
     * Если значения параметров dirPath и newDirPath указывают на один и тотже каталог, 
     * то выполнение метода не будет иметь последствий.
     * @param dirPath текущий полный путь к каталогу. Не может быть <code>null</code>.
     * @param newDirPath новый полный путь к каталогу. Не может быть <code>null</code>.
     * @throws IOException ошибка ввода/вывода
     */
    void move(final String dirPath, final String newDirPath) throws IOException;
    
    /**
     * Получение пути к домашней директории пользователя.
     * @return путь к домашней директории пользователя
     */
    String getUserHomeDirPath();
    
    
    /**
     * Получение пути к каталогу Downloads.
     * Для desktop клиента, который запущен в О.С. Windows метод вернет значение ключа реестра
     * HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Folders\{374DE290-123F-4565-9164-39C4925E467B}.
     * В остальных случаях метод вернет результат конкатенации путь к директории Downloads внутри домашней директории пользователя.
     * @return путь к директории Downloads
     */    
    String getUserDownloadsDirPath();
    
    /**
     * Проверяет, что существует директория, путь к которой указан в параметре <code>dirPath<code>.
     * Если указанный путь является путем к файлу, а не к директории, то метод вернет <code>false<code>.
     * @param dirPath путь к директории
     * @return <code>true</code> если указанная директория существует, <code>false<code> в противном случае
     */
     boolean isExists(final String dirPath);
}
