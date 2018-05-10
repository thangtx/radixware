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

package org.radixware.kernel.server.arte.resources;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.enums.EFileDirReadAttributes;
import org.radixware.kernel.common.enums.EFileDirReadProperties;

import org.radixware.kernel.common.enums.EFileDirReadSortMode;
import org.radixware.kernel.common.enums.EFileDirReadSortOptions;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.schemas.eas.FileDirCreateMess;
import org.radixware.schemas.eas.FileDirCreateRq;
import org.radixware.schemas.eas.FileDirDeleteMess;
import org.radixware.schemas.eas.FileDirDeleteRq;
import org.radixware.schemas.eas.FileDirMoveMess;
import org.radixware.schemas.eas.FileDirMoveRq;
import org.radixware.schemas.eas.FileDirReadItem;
import org.radixware.schemas.eas.FileDirReadMess;
import org.radixware.schemas.eas.FileDirReadRq;
import org.radixware.schemas.eas.FileDirSelectMess;
import org.radixware.schemas.eas.TestIfDirExistsMess;
import org.radixware.schemas.eas.FileDirSelectRq;
import org.radixware.schemas.eas.FileDirGetUserHomeMess;
import org.radixware.schemas.eas.GetUserDownloadsDirMess;
import org.radixware.schemas.easWsdl.FileDirCreateDocument;
import org.radixware.schemas.easWsdl.FileDirDeleteDocument;
import org.radixware.schemas.easWsdl.FileDirGetUserHomeDocument;
import org.radixware.schemas.easWsdl.FileDirMoveDocument;
import org.radixware.schemas.easWsdl.FileDirReadDocument;
import org.radixware.schemas.easWsdl.FileDirSelectDocument;
import org.radixware.schemas.easWsdl.GetUserDownloadsDirDocument;
import org.radixware.schemas.easWsdl.TestIfDirExistsDocument;

/**
 * Класс для удаленной работы с каталогами.
 * Содержит набор статических методов, осуществляющих операции с каталогами на клиенте.
 */
public class FileDirResource extends Resource {

    /**
     * Вызов системного диалога выбора каталога.
     * Метод отправляет на клиент callback-запрос, при получении которого клиент должен показать диалог выбора каталога.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param title заголовок диалога
     * @param dirName полный путь к начальному каталогу
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return полный путь к каталогу или <code>null</code> если пользователь отказался от выбора
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static String select(final Arte arte, final  String title, final String dirName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileDirSelectDocument fdsDoc = FileDirSelectDocument.Factory.newInstance();
        final FileDirSelectRq rq =  fdsDoc.addNewFileDirSelect().addNewFileDirSelectRq();
        rq.setTitle( title );
        rq.setDirName( dirName );
        final FileDirSelectMess fdsMess = (FileDirSelectMess)arte.getArteSocket().invokeResource( fdsDoc, FileDirSelectMess.class, timeout);        
        if( !fdsMess.isSetFileDirSelectRs() )
            throw new ResourceUsageException( "Expected response tag \"FileDirSelectRs\"", null );               
        return fdsMess.getFileDirSelectRs().getDirName();
    }

    /**
     * Получение содержимого каталога.
     * Метод отправляет на клиент callback-запрос на чтение содержимого каталога. В ответ клиент присылает 
     * информацию о файлах и/или подкаталогах, находящихся внутри данного каталога (нерекурсивно).
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param dirName полный путь к каталогу, содержимое которого требуется получить. Не может быть <code>null</code>.
     * @param attribFilter набор фильтров на тип файла (обычный файл или каталог). Если этот параметр не равен <code>null</code>, 
     * то результат работы метода будет содержать информацию только о тех элементах, тип которых содержится в данном наборе.
     * Если этот параметр представляет собой пустой набор, то метод вернет пустой список.
     * @param mask маска элементов каталога. Позволяет отфильтровать элементы каталога по имени. Если этот параметр не равен <code>null</code>, 
     * то результат работы метода будет содержать информацию только о тех элементах, имя которых соответствует указанному шаблону.
     * Шаблон должен быть в формате {@link java.nio.file.FileSystem#getPathMatcher(java.lang.String) "glob"}. 
     * @param props набор свойств файла, значения которых необходимо получить. Если этот параметр равен <code>null</code>
     * или является пустым набором, то результат работы метода будет содержать только имена элементов.
     * @param sortBy свойство файла, по значению которого должна производиться сортировка результатов. 
     * Если этот параметр равен <code>null</code>, то порядок следования элементов в результирующем списке не определен.
     * @param sortOptions набор дополнительных настроек сортировки элементов. Если этот параметр равен <code>null</code>
     * или является пустым набором, то элементы сортируются (при условии что задан sortBy) по возрастанию без учета регистра символов.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return список элементов, содержащих информацию о файлах и/или подкаталогах, находящихся внутри указанного каталога.
     * Порядок следования элементов определяется параметрами sortBy и sortOptions. Не может быть <code>null</code>.
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static List<FileDirReadItem> read(
    		final Arte arte,
    		final String dirName,
                final EnumSet<EFileDirReadAttributes> attribFilter,
                final String mask,
                final EnumSet<EFileDirReadProperties> props,
                final EFileDirReadSortMode sortBy,
                final EnumSet<EFileDirReadSortOptions> sortOptions,
                final int timeout
    ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{        

        FileDirReadDocument fdrDoc = FileDirReadDocument.Factory.newInstance();
        FileDirReadRq rq = fdrDoc.addNewFileDirRead().addNewFileDirReadRq();
        rq.setDirName( dirName );
        if (attribFilter!=null){
            final List<EFileDirReadAttributes> attribFilterList = new ArrayList<>(attribFilter);
            rq.setAttributeFilter(attribFilterList);
        }
        rq.setMask( mask );
        if (props!=null){
            final List<EFileDirReadProperties> propsList = new ArrayList<>(props);
            rq.setProperties( propsList );
        }
        rq.setSortBy( sortBy );
        if (sortOptions!=null){
            final List<EFileDirReadSortOptions> sortOptsList = new ArrayList<>(sortOptions);
            rq.setSortOptions(sortOptsList);
        }
        FileDirReadMess fdrMess = (FileDirReadMess)arte.getArteSocket().invokeResource( fdrDoc, FileDirReadMess.class, timeout);
        if( !fdrMess.isSetFileDirReadRs() )
            throw new ResourceUsageException( "Expected response tag \"FileDirReadRs\"", null );               
        return fdrMess.getFileDirReadRs().getItemList();                
    }
    
    /**
     * Удаленное создание каталога.
     * Метод позволяет создать на клиенте каталог по указанному пути, включая все необходимые, но
     * несуществующие каталоги верхнего уровня.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param dirName полный путь к создаваемому каталогу. Не может быть <code>null</code>.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static void create(final Arte arte, final String dirName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        FileDirCreateDocument fdcDoc = FileDirCreateDocument.Factory.newInstance();
        FileDirCreateRq rq = fdcDoc.addNewFileDirCreate().addNewFileDirCreateRq();
        rq.setDirName( dirName );
        arte.getArteSocket().invokeResource( fdcDoc, FileDirCreateMess.class, timeout);         
    }
    
    /**
     * Удаление каталога на клиенте.
     * Метод позволяет удалить на клиенте указанный каталог (рекурсивно).
     * Если указанный каталог не существует, то метод завершается без ошибок.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param dirName полный путь к удаляемому каталогу. Не может быть <code>null</code>.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static void delete(final Arte arte, final String dirName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        FileDirDeleteDocument fddDoc = FileDirDeleteDocument.Factory.newInstance();
        FileDirDeleteRq rq = fddDoc.addNewFileDirDelete().addNewFileDirDeleteRq();
        rq.setDirName(dirName );
        arte.getArteSocket().invokeResource( fddDoc, FileDirDeleteMess.class, timeout); 
    }

    /**
     * Перемещение (переименование) каталога на клиенте.
     * Метод позволяет изменить путь к указанному каталогу на клиенте. 
     * После успешного выполнения метода каталог будет доступен по новому пути. Содержимое каталога сохраняется.
     * Если каталог, путь к которому указан в newName, уже существует, то выполнение метода завершится ошибкой.
     * Если значения параметров dirName и newName указывают на один и тотже каталог, 
     * то выполнение метода не будет иметь последствий.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param dirName текущий полный путь к каталогу. Не может быть <code>null</code>.
     * @param newName новый полный путь к каталогу. Не может быть <code>null</code>.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */    
    public static void move(final Arte arte, final String dirName, final String newName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        FileDirMoveDocument fdmDoc = FileDirMoveDocument.Factory.newInstance();
        FileDirMoveRq rq = fdmDoc.addNewFileDirMove().addNewFileDirMoveRq();
        rq.setDirName(dirName);
        rq.setNewName(newName);
        arte.getArteSocket().invokeResource( fdmDoc, FileDirMoveMess.class, timeout);                        
    }    
    
    /**
     * Получение пути к домашнему каталогу пользователя на клиенте.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return путь к домашней директории пользователя
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static String getUserHomeDirPath(final Arte arte, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileDirGetUserHomeDocument document = FileDirGetUserHomeDocument.Factory.newInstance();
        document.addNewFileDirGetUserHome().addNewFileDirGetUserHomeRq();
        final FileDirGetUserHomeMess answer = (FileDirGetUserHomeMess)arte.getArteSocket().invokeResource(document, FileDirGetUserHomeMess.class, timeout);
        return answer.getFileDirGetUserHomeRs().getDirName();
    }
    
    /**
     * Получение пути к каталогу Downloads на клиенте.
     * Для desktop клиента, который запущен в О.С. Windows метод вернет значение ключа реестра
     * HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Folders\{374DE290-123F-4565-9164-39C4925E467B}.
     * В остальных случаях метод вернет результат конкатенации путь к директории Downloads внутри домашней директории пользователя.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return путь к директории Downloads на клиенте
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static String getUserDownloadsDirPath(final Arte arte, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final GetUserDownloadsDirDocument document = GetUserDownloadsDirDocument.Factory.newInstance();
        document.addNewGetUserDownloadsDir().addNewGetUserDownloadsDirRq();
        final GetUserDownloadsDirMess answer = (GetUserDownloadsDirMess)arte.getArteSocket().invokeResource(document, GetUserDownloadsDirMess.class, timeout);
        return answer.getGetUserDownloadsDirRs().getDirName();
    }
    
    /**
     * Проверяет, что на клиенте существует директория, путь к которой указан в параметре <code>path<code>.
     * Если указанный путь является путем к файлу, а не к директории, то метод вернет <code>false<code>.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param path путь к директории на клиенте
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return <code>true</code> если указанная директория существует, <code>false<code> в противном случае
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static boolean isExists(final Arte arte, final String path, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final TestIfDirExistsDocument document = TestIfDirExistsDocument.Factory.newInstance();
        document.addNewTestIfDirExists().addNewTestIfDirExistsRq().setDirPath(path);
        final TestIfDirExistsMess answer = (TestIfDirExistsMess)arte.getArteSocket().invokeResource(document, TestIfDirExistsMess.class, timeout);
        return answer.getTestIfDirExistsRs().getIsExists();
    }      
}