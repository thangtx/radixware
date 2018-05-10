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

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EFileSeekOriginType;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.ArteSocketException;
import org.radixware.schemas.eas.FileAccessMess;
import org.radixware.schemas.eas.FileAccessRq;
import org.radixware.schemas.eas.FileCloseMess;
import org.radixware.schemas.eas.FileCloseRq;
import org.radixware.schemas.eas.FileCopyMess;
import org.radixware.schemas.eas.FileCopyRq;
import org.radixware.schemas.eas.FileDeleteMess;
import org.radixware.schemas.eas.FileDeleteRq;
import org.radixware.schemas.eas.FileMask;
import org.radixware.schemas.eas.FileMoveMess;
import org.radixware.schemas.eas.FileMoveRq;
import org.radixware.schemas.eas.FileOpenMess;
import org.radixware.schemas.eas.FileOpenRq;
import org.radixware.schemas.eas.FileReadMess;
import org.radixware.schemas.eas.FileReadRq;
import org.radixware.schemas.eas.FileSeekMess;
import org.radixware.schemas.eas.FileSeekRq;
import org.radixware.schemas.eas.FileSelectMess;
import org.radixware.schemas.eas.FileSelectRq;
import org.radixware.schemas.eas.FileSizeMess;
import org.radixware.schemas.eas.FileSizeRq;
import org.radixware.schemas.eas.FileTransitMess;
import org.radixware.schemas.eas.FileTransitRq;
import org.radixware.schemas.eas.FileWriteMess;
import org.radixware.schemas.eas.FileWriteRq;
import org.radixware.schemas.eas.TestIfFileExistsMess;
import org.radixware.schemas.easWsdl.FileAccessDocument;
import org.radixware.schemas.easWsdl.FileCloseDocument;
import org.radixware.schemas.easWsdl.FileCopyDocument;
import org.radixware.schemas.easWsdl.FileDeleteDocument;
import org.radixware.schemas.easWsdl.FileMoveDocument;
import org.radixware.schemas.easWsdl.FileOpenDocument;
import org.radixware.schemas.easWsdl.FileReadDocument;
import org.radixware.schemas.easWsdl.FileSeekDocument;
import org.radixware.schemas.easWsdl.FileSelectDocument;
import org.radixware.schemas.easWsdl.FileSizeDocument;
import org.radixware.schemas.easWsdl.FileTransitDocument;
import org.radixware.schemas.easWsdl.FileWriteDocument;
import org.radixware.schemas.easWsdl.TestIfFileExistsDocument;

public class FileResource extends Resource {
    
    /////////////////////////////////// Static Methods ///////////////////////////////////
    /**
     * @return имя выбранного файла, пустую строку если не выбрано
     * @throws ResourceUsageTimeout 
     * @throws ResourceUsageException 
     * @throws InterruptedException 
     */
    public static String select(final Arte arte, final String title, final String fileName, final FileMask[] masks, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        FileSelectDocument fsDoc = FileSelectDocument.Factory.newInstance();
        FileSelectRq rq = fsDoc.addNewFileSelect().addNewFileSelectRq();
        rq.setTitle( title );
        rq.setFileName( fileName );
        rq.getItemList().addAll(Arrays.asList(masks));
        FileSelectMess fsMess = (FileSelectMess)arte.getArteSocket().invokeResource( fsDoc, FileSelectMess.class, timeout);
        if( !fsMess.isSetFileSelectRs() )
            throw new ResourceUsageException( "Expected response tag \"FileSelectRs\"", null );               
        return fsMess.getFileSelectRs().getFileName();
    }
    
    public static boolean checkAccess(final Arte arte, final String fileName, final EFileAccessType access, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException
    {
        
        FileAccessDocument faDoc = FileAccessDocument.Factory.newInstance();
        FileAccessRq rq = faDoc.addNewFileAccess().addNewFileAccessRq();
        rq.setFileName( fileName );
        rq.setAccess( access );
        FileAccessMess faMess = (FileAccessMess)arte.getArteSocket().invokeResource( faDoc, FileAccessMess.class, timeout);
        if( !faMess.isSetFileAccessRs() )
            throw new ResourceUsageException( "Expected response tag \"FileAccessRs\"", null );        
        return faMess.getFileAccessRs().getOK();        
    }        

    public static void delete(final Arte arte, final String fileName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileDeleteDocument fdDoc = FileDeleteDocument.Factory.newInstance();
        final FileDeleteRq rq = fdDoc.addNewFileDelete().addNewFileDeleteRq();
        rq.setFileName(fileName);
        arte.getArteSocket().invokeResource( fdDoc, FileDeleteMess.class, timeout);        
    }
    
    public static void copy(final Arte arte, final String from, final String to, final boolean overwrite, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileCopyDocument fcDoc = FileCopyDocument.Factory.newInstance();
        final FileCopyRq rq = fcDoc.addNewFileCopy().addNewFileCopyRq();
        rq.setFileName(from);
        rq.setTargetFileName(to);
        rq.setOverwrite( overwrite );
        arte.getArteSocket().invokeResource( fcDoc, FileCopyMess.class, timeout);        
    }   
    
    public static void move(final Arte arte, final String from, final String to, final boolean overwrite, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileMoveDocument fmDoc = FileMoveDocument.Factory.newInstance();
        final FileMoveRq rq = fmDoc.addNewFileMove().addNewFileMoveRq();
        rq.setFileName(from);
        rq.setTargetFileName(to);
        rq.setOverwrite( overwrite );
        arte.getArteSocket().invokeResource( fmDoc, FileMoveMess.class, timeout);        
    }
    
    public static long getSize(final Arte arte, final String fileName, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileSizeDocument fsDoc = FileSizeDocument.Factory.newInstance();
        final FileSizeRq rq = fsDoc.addNewFileSize().addNewFileSizeRq();
        rq.setFileName(fileName);
        FileSizeMess fsMess = (FileSizeMess)arte.getArteSocket().invokeResource( fsDoc, FileSizeMess.class, timeout);
        if( !fsMess.isSetFileSizeRs() )
            throw new ResourceUsageException( "Expected response tag \"FileSizeRs\"", null );                                    
        return fsMess.getFileSizeRs().getSize();
    }    
    
    /**
     * Проверяет, что на клиенте существует файл, путь к которому указан в параметре <code>path<code>.
     * Если указанный путь является путем к директории, а не к файлу, то метод вернет <code>false<code>.
     * @param arte инстанция ARTE. Не может быть <code>null</code>.
     * @param path путь к директории на клиенте
     * @param timeout ограничение времени на ожидание ответа в секундах. Значение 0 интерпретируется как бесконечное ожидание.
     * @return <code>true</code> если указанный файл существует, <code>false<code> в противном случае
     * @throws ResourceUsageException при выполнении запроса на клиенте произошла ошибка или случилась ошибка связи
     * @throws ResourceUsageTimeout истекло время ожидания
     * @throws InterruptedException выполнение запроса было прервано
     */
    public static boolean isExists(final Arte arte, final String path, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final TestIfFileExistsDocument document = TestIfFileExistsDocument.Factory.newInstance();
        document.addNewTestIfFileExists().addNewTestIfFileExistsRq().setFilePath(path);
        TestIfFileExistsMess answer = (TestIfFileExistsMess)arte.getArteSocket().invokeResource(document, TestIfFileExistsMess.class, timeout);
        return answer.getTestIfFileExistsRs().getIsExists();
    }
    
    /**
     * @return - идентификатор открытого файла
     * @throws ResourceUsageTimeout 
     * @throws ResourceUsageException 
     * @throws InterruptedException 
     * @throws ServiceCallException 
     * @throws ArteSocketException 
     */
    protected static String open(final Arte arte, final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode shareMode, final int timeout ) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException
    {                        
        final FileOpenDocument foDoc = FileOpenDocument.Factory.newInstance();
        final FileOpenRq rq = foDoc.addNewFileOpen().addNewFileOpenRq();
        rq.setFileName(fileName);
        rq.setMode( openMode );        
        rq.setShare( shareMode );
        final FileOpenMess foMess = ( FileOpenMess )arte.getArteSocket().invokeResource( foDoc, FileOpenMess.class, timeout);
        if( !foMess.isSetFileOpenRs() )
            throw new ResourceUsageException( "Expected response tag \"FileOpenRs\"", null );
        return foMess.getFileOpenRs().getId();
    }        
    
    protected static void close(final Arte arte, final String openFileId, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException 
    {
        final FileCloseDocument foDoc = FileCloseDocument.Factory.newInstance();
        final FileCloseRq rq = foDoc.addNewFileClose().addNewFileCloseRq();
        rq.setId( openFileId );
        arte.getArteSocket().invokeResource( foDoc, FileCloseMess.class, timeout);
    }
    
    protected static String startTransit(final Arte arte, final String fileName, final EMimeType mimeType, final boolean openAfterTransit, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileTransitDocument document = FileTransitDocument.Factory.newInstance();
        final FileTransitRq rq = document.addNewFileTransit().addNewFileTransitRq();
        rq.setFileName(fileName);
        if (mimeType!=null){
            rq.setMimeType(mimeType);
        }
        if (openAfterTransit){
            rq.setOpenAfterTransit(true);
        }
        final FileTransitMess response = (FileTransitMess) arte.getArteSocket().invokeResource( document, FileTransitMess.class, timeout);
        if (!response.isSetFileTransitRs()){
            throw new ResourceUsageException( "Expected response tag \"FileTransitRs\"", null );
        }
        return response.getFileTransitRs().getId();
    }
    
    /** @return - абсолютная позиция 
     * @throws ResourceUsageTimeout 
     * @throws ResourceUsageException 
     * @throws InterruptedException */
    protected static long seek(final Arte arte, final String openFileId, final EFileSeekOriginType origin, final int offset, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException
    {        
        final FileSeekDocument fsDoc = FileSeekDocument.Factory.newInstance();
        final FileSeekRq rq = fsDoc.addNewFileSeek().addNewFileSeekRq();
        rq.setId( openFileId );
        rq.setOrigin( origin );
        rq.setOffset( offset );
        final FileSeekMess fsMess = (FileSeekMess)arte.getArteSocket().invokeResource( fsDoc, FileSeekMess.class, timeout);
        if( !fsMess.isSetFileSeekRs() )
            throw new ResourceUsageException( "Expected response tag \"FileSeekRs\"", null );                                    
        return fsMess.getFileSeekRs().getPos();
    }
    
    /**
     * @param openFileId - ид. открытого файла
     * @param buffer - сюда складываются данные, начиная с текущей позиции
     * @param len - buffer.position() + len <= buffer.capacity()
     * @param timeout - в миллисекундах
     * @return - файл дочитан до конца
     * @throws ResourceUsageTimeout 
     * @throws ResourceUsageException 
     * @throws InterruptedException 
     */
    protected static boolean read(final Arte arte, final String openFileId, ByteBuffer buffer, int len, int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final ReadResult rs = read(arte, openFileId, len, timeout);
        buffer.put( rs.getData() );
        return rs.isEOF();
    }
    
    static ReadResult read(final Arte arte, final String openFileId, int len, int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        final FileReadDocument frDoc = FileReadDocument.Factory.newInstance();
        final FileReadRq rq = frDoc.addNewFileRead().addNewFileReadRq();
        rq.setId( openFileId );
        rq.setLen( len );        
        final FileReadMess frMess = (FileReadMess)arte.getArteSocket().invokeResource( frDoc, FileReadMess.class, timeout);
        if( !frMess.isSetFileReadRs() )
            throw new ResourceUsageException( "Expected response tag \"FileReadRs\"", null );        
        return new ReadResult( frMess.getFileReadRs().getByteArrayValue(), frMess.getFileReadRs().getEOF() );
    }

    
    
    protected static void write(final Arte arte, final String openFileId, final Bin data, final int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException
    {
        write(arte, openFileId, data.get(), timeout);
    }
    
    protected static void write(final Arte arte, final String openFileId, byte[] data, int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException
    {
        final FileWriteDocument fwDoc = FileWriteDocument.Factory.newInstance();
        final FileWriteRq rq = fwDoc.addNewFileWrite().addNewFileWriteRq();
        rq.setId( openFileId );
        rq.setByteArrayValue( data );
        arte.getArteSocket().invokeResource( fwDoc, FileWriteMess.class, timeout);
    }

    static final class ReadResult {
        
        public ReadResult( byte[] data, boolean isEOF ) {
            this.data  = data == null ? null : Arrays.copyOf(data, data.length);
            this.isEOF = isEOF;
        }
                
        public final byte[] getData() {
            return data;
        }
        public final boolean isEOF() {
            return isEOF;
        }
        
        private final byte[] data;
        private final boolean isEOF;
    }
}
