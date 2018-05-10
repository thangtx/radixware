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

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.common.enums.EFileDirReadAttributes;
import org.radixware.kernel.common.enums.EFileDirReadProperties;
import org.radixware.kernel.common.enums.EFileDirReadSortMode;
import org.radixware.kernel.common.enums.EFileDirReadSortOptions;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.eas.FileDirReadItem;

/**
 * Класс для работы с локальными каталогами.
 * Содержит реализацию большинства методов интерфейса {@link org.radixware.kernel.common.client.eas.resources.IFileDirResource IFileDirResource},
 * за исключением методов запуска системного каталога.
 * @see org.radixware.kernel.server.arte.resources.FileDirResource
 */
public abstract class AbstractFileDirResource implements IFileDirResource{
    
    private final static class DirEntry{
        
        private final String name;
        private final boolean isDirectory;
        private final String type;
        private final long modifyTime;
        private final long size;
        
        public DirEntry(final Path entry) throws IOException{
            final File file = entry.toFile();
            name = file.getName();
            isDirectory = file.isDirectory();
            modifyTime = file.lastModified();
            size = file.length();
            final int pos = name.lastIndexOf('.');
            type = (pos >= 0 && pos < name.length() - 1 ? name.substring(pos + 1) : "");
        }

        public String getName() {
            return name;
        }
        
        public String getType(){
            return type;
        }

        public boolean isIsDirectory() {
            return isDirectory;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public long getSize() {
            return size;
        }
        
        public FileDirReadItem toFileDirReadItem(final List<EFileDirReadProperties> readProps){
            FileDirReadItem result = FileDirReadItem.Factory.newInstance();
            result.setName(name);
            if (readProps.contains(EFileDirReadProperties.ATTRIBUTES)){
                final List<EFileDirReadAttributes> attrs = new ArrayList<>();
                if (isDirectory){
                    attrs.add(EFileDirReadAttributes.DIR);
                }else{
                    attrs.add(EFileDirReadAttributes.FILE);
                }
                result.setAttributes(attrs);                
            }
            if (readProps.contains(EFileDirReadProperties.MODIFY_TIME)){
                final Calendar time = Calendar.getInstance();
                time.setTimeInMillis(modifyTime);
                result.setModifyTime(time);                
            }
            if (readProps.contains(EFileDirReadProperties.SIZE)){
                result.setSize(size);
            }
            return result;
        }
    }
    
    private final static class DirEntriesComparator implements Comparator<DirEntry>{
        
        private final EFileDirReadSortMode sortBy;
        private final boolean isDescending;
        private final boolean isDirectoriesFirst;
        private final boolean isCaseSensitive;        
                
        public DirEntriesComparator(final EFileDirReadSortMode sortBy, final List<EFileDirReadSortOptions> sortOptions){
            this.sortBy = sortBy;
            isDescending = sortOptions.contains(EFileDirReadSortOptions.DESCENDING);
            isDirectoriesFirst = sortOptions.contains(EFileDirReadSortOptions.DIRECTORIES_FIRST);
            isCaseSensitive = sortOptions.contains(EFileDirReadSortOptions.CASE_SENSITIVE);
        }

        @Override
        @SuppressWarnings("PMD.MissingBreakInSwitch")
        public int compare(DirEntry entry1, DirEntry entry2) {
            if (entry1==null || entry2==null){
                throw new NullPointerException();
            }
            if (entry1==entry2){//NOPMD
                return 0;
            }
            if (isDirectoriesFirst){
                if (entry1.isIsDirectory() && !entry2.isIsDirectory()){
                    return -1;
                }else if (!entry1.isIsDirectory() && entry2.isIsDirectory()){
                    return 1;
                }
            }
            if (sortBy==null){
                return 0;
            }
            int result;
            switch(sortBy){
                case NAME:{
                    if (isCaseSensitive){
                        result = entry1.getName().compareTo(entry2.getName());
                    }else{
                        result = entry1.getName().compareToIgnoreCase(entry2.getName());
                    }
                    break;
                }
                case TYPE:{
                    if (isCaseSensitive){
                        result = entry1.getType().compareTo(entry2.getType());
                        if (result==0){
                            result = entry1.getName().compareTo(entry2.getName());    
                        }
                    }else{
                        result = entry1.getType().compareToIgnoreCase(entry2.getType());
                        if (result==0){
                            result = entry1.getName().compareToIgnoreCase(entry2.getName());
                        }
                    }                    
                    break;
                }
                case MODIFY_TIME:{
                    result = Long.compare(entry1.getModifyTime(), entry2.getModifyTime());
                    break;
                }
                case SIZE:{
                    result = Long.compare(entry1.getSize(), entry2.getSize());
                    break;                    
                }
                default:
                    result = 0;
            }
            return isDescending ? -result : result;
        }
    }
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
    @Override
    public List<FileDirReadItem> read(final String dirName,
                                      final List<EFileDirReadAttributes> attribFilter, 
                                      final String mask,
                                      final List<EFileDirReadProperties> readProps,
                                      final EFileDirReadSortMode sortBy, 
                                      final List<EFileDirReadSortOptions> sortOptions) throws IOException {
        final Path dir = Paths.get(dirName);
        final List<DirEntry> dirEntries = new ArrayList<>();        
        try (DirectoryStream<Path> stream = mask==null || mask.isEmpty() ? Files.newDirectoryStream(dir) : Files.newDirectoryStream(dir,mask)) {
            for (Path entry: stream) {
                if (attribFilter==null ||
                    (Files.isDirectory(entry) && attribFilter.contains(EFileDirReadAttributes.DIR)) || 
                    (!Files.isDirectory(entry) && attribFilter.contains(EFileDirReadAttributes.FILE))
                   ){
                    dirEntries.add(new DirEntry(entry));
                }
            }
        }
        Collections.sort(dirEntries, new DirEntriesComparator(sortBy, sortOptions==null ? Collections.<EFileDirReadSortOptions>emptyList() : sortOptions));
        final List<FileDirReadItem> result = new ArrayList<>();
        for (DirEntry entry: dirEntries){
            result.add(entry.toFileDirReadItem(readProps==null ? Collections.<EFileDirReadProperties>emptyList() : readProps));
        }
        return result;
    }

    /**
    * Создание каталога.
    * Метод позволяет создать каталог по указанному пути, включая все необходимые, но
    * несуществующие каталоги верхнего уровня.
    * @param dirName полный путь к создаваемому каталогу. Не может быть <code>null</code>.
    * @throws IOException ошибка ввода/вывода
    */
    @Override
    public void create(final String dirName) throws IOException{
        FileUtils.mkDirs(new File(dirName));        
    }

    /**
     * Удаление каталога.
     * Метод позволяет удалить указанный каталог.
     * @param dirName полный путь к удаляемому каталогу. Не может быть <code>null</code>.
     * @return возвращает <code>true</code>, если операция завершилась успешно. и <code>false</code> если указанный каталог не существует.
     * @throws IOException ошибка ввода/вывода
     */    
    @Override
    public boolean delete(final String dirName) throws IOException {
        return FileUtils.deleteDirectory(new File(dirName));
    }

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
    @Override
    public void move(final String dirPath, final String newDirPath) throws IOException {
        try{
            Files.move(Paths.get(dirPath), Paths.get(newDirPath), java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        }catch(AtomicMoveNotSupportedException exception){
            FileUtils.copyDirectory(new File(dirPath), new File(newDirPath));
            delete(dirPath);
        }
    }

    /**
     * Получение пути к домашней директории пользователя.
     * @return путь к домашней директории пользователя
     */    
    @Override
    public final String getUserHomeDirPath() {
        return System.getProperty("user.home");
    }

    /**
     * Получение пути к каталогу Downloads.
     * Для desktop клиента, который запущен в О.С. Windows метод вернет значение ключа реестра
     * HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Folders\{374DE290-123F-4565-9164-39C4925E467B}.
     * В остальных случаях метод вернет результат конкатенации путь к директории Downloads внутри домашней директории пользователя.
     * @return путь к директории Downloads
     */     
    @Override
    public String getUserDownloadsDirPath() {
        return Paths.get(getUserHomeDirPath(), "Downloads").toAbsolutePath().toString();
    }

    @Override    
    /**
     * Проверяет, что существует директория, путь к которой указан в параметре <code>dirPath<code>.
     * Если указанный путь является путем к файлу, а не к директории, то метод вернет <code>false<code>.
     * @param dirPath путь к директории
     * @return <code>true</code> если указанная директория существует, <code>false<code> в противном случае
     */    
    public final boolean isExists(final String dirPath) {
        return new File(dirPath).isDirectory();
    }        
}
