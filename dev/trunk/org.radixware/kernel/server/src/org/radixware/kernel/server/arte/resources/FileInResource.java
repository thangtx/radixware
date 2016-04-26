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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;

import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EFileSeekOriginType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;

public class FileInResource extends InputStream {

    private static final String CLOSED_ID = "";
    private final Arte arte;

    public FileInResource(final Arte arte) {
        buffer = ByteBuffer.allocate(blockSize);
        this.arte = arte;
    }

    public FileInResource(final Arte arte, final int blockSize) {
        this.blockSize = blockSize;
        buffer = ByteBuffer.allocate(this.blockSize);
        this.arte = arte;
    }

    public FileInResource(final Arte arte, final String fileName, final EFileOpenShareMode openShareMode) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        this(arte);
        open(fileName, openShareMode);
    }

    public FileInResource(final Arte arte, final String fileName, final EFileOpenShareMode openShareMode, final int blockSize) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        this(arte, blockSize);
        open(fileName, openShareMode);
    }

//    @Override
//    protected void finalize() throws Throwable {
//        //as the code will be executed in another thread it should be threadsafe
//        try {
//            if ( !id.equals(CLOSED_ID) ) {
//                close();// contains arte call -> is not thread safe !!!
//            }
//        } finally {
//            super.finalize();
//        }
//    }
    public void open(final String fileName, final EFileOpenShareMode openShareMode) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        this.fileName = fileName;
        this.id = FileResource.open(arte, fileName, EFileOpenMode.READ, openShareMode, timeout);
        this.isLastBlockLoaded = false;
    }

    @Override
    public void close() throws IOException {
        if (!id.equals(CLOSED_ID)) {
            try {
                FileResource.close(arte, id, timeout);
                id = CLOSED_ID;
                super.close();
            } catch (ResourceUsageException e) {
                throw new IOException(e.getMessage(), e);
            } catch (ResourceUsageTimeout e) {
                throw new IOException(e.getMessage(), e);
            } catch (InterruptedException e) {
                throw new ClosedByInterruptException();
            }
        } else {
            throw new IOException("File not opened");
        }
    }

    public long getSize() throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        return FileResource.getSize(arte, fileName, timeout);
    }

    public final String getId() {
        return id;
    }

    public long seek(final EFileSeekOriginType origin, final int offset) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final long result;
        if (!id.equals(CLOSED_ID)) {
            result = FileResource.seek(arte, id, origin, offset, timeout);
            buffer.position(realCapacity);
        } else {
            throw new ResourceUsageException("File is not opened", null);
        }
        return result;
    }

    @Override
    public int read() throws IOException {
        if (readException != null) {
            throw readException;
        }
        if (buffer.position() == realCapacity) {
            try {
                try {
                    readFile();
                } catch (ResourceUsageException e) {
                    throw new IOException(e.getMessage(), e);
                } catch (ResourceUsageTimeout e) {
                    throw new IOException(e.getMessage(), e);
                } catch (InterruptedException e) {
                    throw new ClosedByInterruptException();
                }
            } catch (IOException ex) {
                readException = ex;
                throw ex;
            }
        }
        return buffer.position() == realCapacity ? -1 : buffer.get() & 0xFF;
    }

    @Override
    public int available() {
        return realCapacity - buffer.position();
    }
    int totally = 0;

    private void readFile() throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        if (id.equals(CLOSED_ID)) {
            throw new ResourceUsageException("File is not opened", null);
        }
        buffer.clear();
        if (isLastBlockLoaded) {
            realCapacity = 0;
            return;
        }
        isLastBlockLoaded = FileResource.read(arte, id, buffer, blockSize, timeout);
        realCapacity = buffer.position();
        buffer.rewind();
        totally += realCapacity;
    }
    protected int timeout = Resource.DEFAULT_TIMEOUT;
    protected int blockSize = Resource.DEFAULT_BLOCK_SIZE;
    private String fileName = null;
    private String id = CLOSED_ID;
    private boolean isLastBlockLoaded = false;
    private ByteBuffer buffer;
    private int realCapacity = 0;
    private IOException readException = null;
}
