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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;

import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EFileSeekOriginType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;

public class FileOutResource extends OutputStream {
    private static final String CLOSED_ID = "";
	    
    final private Arte arte;

	public FileOutResource(final Arte arte) {
    	this.arte = arte;
        allocateByteBuffer();
    }
    
    public FileOutResource(final Arte arte, final int blockSize ) {
    	this.arte = arte;
        this.blockSize = blockSize;
        allocateByteBuffer();
    }
    
    public FileOutResource(final Arte arte, final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode openShareMode )
    throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        this(arte);        
        open( fileName, openMode, openShareMode );
    }
    public FileOutResource(final Arte arte, final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode openShareMode, final int blockSize )
    throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{
        this(arte, blockSize );
        open( fileName, openMode, openShareMode );
    }
        
//    @Override
//	protected void finalize() throws Throwable {
//        //as the code will be executed in another thread it should be threadsafe
//		try {
//	        if (!id.equals(CLOSED_ID))
//	            close(); // contains arte call -> is not thread safe !!!
//		} finally {
//			super.finalize();
//		}
//    }
    
    protected int timeout   = Resource.DEFAULT_TIMEOUT; 
    protected int blockSize = Resource.DEFAULT_BLOCK_SIZE;
    
    private String fileName = null;
    private String id = CLOSED_ID;
    
    public void open(String fileName, EFileOpenMode openMode, EFileOpenShareMode shareMode)
    throws ResourceUsageException, ResourceUsageTimeout, InterruptedException{    
        this.fileName = fileName;
        id = FileResource.open(arte, fileName, openMode, shareMode, timeout );
    }
    
    public final String getId() { return id; }
    
    @Override
	public void close() throws IOException {
        if( !id.equals(CLOSED_ID) ){
            flush();
			try {
				FileResource.close(arte, id, timeout);
			} catch (ResourceUsageException e) {
				throw new IOException(e.getMessage(), e);
			} catch (ResourceUsageTimeout e) {
				throw new IOException(e.getMessage(), e);
			} catch (InterruptedException e) {
				throw new ClosedByInterruptException();
			}
            id = CLOSED_ID;
	    super.close();
        } else 
           throw new IOException("File is not opened");            
    }        
    public long seek( EFileSeekOriginType origin, int offset ) throws ResourceUsageException, ResourceUsageTimeout, IOException, InterruptedException {
        long result;
        if( !id.equals(CLOSED_ID) ) {
            flush();
            result = FileResource.seek(arte, id, origin, offset, timeout);            
        }
        else 
           throw new ResourceUsageException("File is not opened", null);
        return result;
    }    

    @Override
	public void write(int b) throws IOException{
        if( buffer.position() == buffer.capacity() )
            flush();
        buffer.put( (byte)b );
    }
    @Override
	public void flush() throws IOException {
        if ( buffer.position() > 0 ) {                
            byte[] data;
            int pos = buffer.position();
            buffer.rewind();
            if(pos == buffer.capacity())
                data = backedArray;
            else {
                data = new byte[ pos ];
                System.arraycopy( backedArray, 0, data, 0, pos );
            }                    
			try {
				FileResource.write(arte, id, data, timeout);
			} catch (ResourceUsageException e) {
				throw new IOException(e.getMessage(), e);
			} catch (ResourceUsageTimeout e) {
				throw new IOException(e.getMessage(), e);
			} catch (InterruptedException e) {
				throw new ClosedByInterruptException();
			}
            buffer.clear();
        }                
    }
        
    private ByteBuffer buffer;
    private byte[] backedArray = null;
    
    private void allocateByteBuffer() {   
        // такой вариант использования не оч. эффективен - для получения byte[] всегда выполняется перекладывание данных
        //buffer = ByteBuffer.allocate( blockSize );
        
        backedArray = new byte[ blockSize ];
        buffer = ByteBuffer.wrap( backedArray );        
    }
}
