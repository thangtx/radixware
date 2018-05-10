/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.arte.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;


abstract class AbstractFileOutResource extends OutputStream{

    protected static final String CLOSED_ID = "";

    private final Arte arte;
    private ByteBuffer buffer;
    private byte[] backedArray = null;
    protected int timeout = Resource.DEFAULT_TIMEOUT;
    protected int blockSize = Resource.DEFAULT_BLOCK_SIZE;
    
    private String id = CLOSED_ID;
    
    protected AbstractFileOutResource(final Arte arte, final int blockSize) {
        this.arte = arte;
        this.blockSize = blockSize;
        allocateByteBuffer();
    }
    
    
    private void allocateByteBuffer() {
        // такой вариант использования не оч. эффективен - для получения byte[] всегда выполняется перекладывание данных
        //buffer = ByteBuffer.allocate( blockSize );

        backedArray = new byte[blockSize];
        buffer = ByteBuffer.wrap(backedArray);
    }
    
    public final String getId() {
        return id;
    }
    
    protected final void setId(final String fileId){
        id = fileId;
    }
    
    protected final Arte getArte(){
        return arte;
    }

    @Override
    public final void close() throws IOException {
        if (!id.equals(CLOSED_ID)) {
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
        } else {
            throw new IOException("File is not opened");
        }
    }    
    
    @Override
    public final void write(int b) throws IOException {
        if (buffer.position() == buffer.capacity()) {
            flush();
        }
        buffer.put((byte) b);
    }    
        
    @Override
    public final void flush() throws IOException {
        if (buffer.position() > 0) {
            byte[] data;
            final int pos = buffer.position();
            buffer.rewind();
            if (pos == buffer.capacity()) {
                data = backedArray;
            } else {
                data = new byte[pos];
                System.arraycopy(backedArray, 0, data, 0, pos);
            }
            try {
                FileResource.write(arte, id, data, timeout);
            } catch (ResourceUsageException | ResourceUsageTimeout e) {
                throw new IOException(e.getMessage(), e);
            } catch (InterruptedException e) {
                throw new ClosedByInterruptException();
            }
            buffer.clear();
        }
    }    
    
}
