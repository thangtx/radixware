/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;
import org.radixware.kernel.common.svn.RadixSvnException;

public class SvnDeltaReader {

    private ByteBuffer myBuffer;

    private int myHeaderBytes;
    private long myLastSourceOffset;
    private int myLastSourceLength;
    private boolean myIsWindowSent;
    private byte myVersion;

    public SvnDeltaReader() {
        myBuffer = ByteBuffer.allocate(4096);
        myBuffer.clear();
        myBuffer.limit(0);
    }

    public void reset(String path, ISvnDeltaConsumer consumer) throws RadixSvnException {
        // if header was read, but data was not -> fire empty window.
        if (myHeaderBytes == 4 && !myIsWindowSent) {
            OutputStream os = consumer.textDeltaChunk(path, SvnDiffWindow.EMPTY);
            try {
                os.close();
            } catch (IOException ex) {
            }
        }
        myLastSourceLength = 0;
        myLastSourceOffset = 0;
        myHeaderBytes = 0;
        myIsWindowSent = false;
        myVersion = 0;

        myBuffer.clear();
        myBuffer.limit(0);
    }

    public void nextWindow(byte[] data, int offset, int length, String path, ISvnDeltaConsumer consumer) throws RadixSvnException {
        appendToBuffer(data, offset, length);
        if (myHeaderBytes < 4) {
            if (myBuffer.remaining() < 4) {
                return;
            }
            if (myBuffer.get(0) != 'S' || myBuffer.get(1) != 'V' || myBuffer.get(2) != 'N'
                    || (myBuffer.get(3) != '\0' && myBuffer.get(3) != '\1')) {
                throw new RadixSvnException("Invalid identifier of SvnDiff");
            }
            myVersion = myBuffer.get(3);
            myBuffer.position(4);
            int remainging = myBuffer.remaining();
            myBuffer.compact();
            myBuffer.position(0);
            myBuffer.limit(remainging);
            myHeaderBytes = 4;
        }
        while (true) {
            long sourceOffset = readLongOffset();
            if (sourceOffset < 0) {
                return;
            }
            int sourceLength = readOffset();
            if (sourceLength < 0) {
                return;
            }
            int targetLength = readOffset();
            if (targetLength < 0) {
                return;
            }
            int instructionsLength = readOffset();
            if (instructionsLength < 0) {
                return;
            }
            int newDataLength = readOffset();
            if (newDataLength < 0) {
                return;
            }
            if (sourceLength > 0
                    && (sourceOffset < myLastSourceOffset
                    || sourceOffset + sourceLength < myLastSourceOffset + myLastSourceLength)) {
                throw new RadixSvnException("SvnDiff has backward sliding source views");
            }
            if (myBuffer.remaining() < instructionsLength + newDataLength) {
                return;
            }
            myLastSourceOffset = sourceOffset;
            myLastSourceLength = sourceLength;
            SvnDiffWindow window = null;
            int allDataLength = newDataLength + instructionsLength;
            if (myVersion == 1) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int bufferPosition = myBuffer.position();
                try {
                    instructionsLength = deflate(instructionsLength, out);
                    newDataLength = deflate(newDataLength, out);
                } catch (IOException e) {
                    throw new RadixSvnException(e);
                }
                byte[] bytes = out.toByteArray();
                ByteBuffer decompressed = ByteBuffer.wrap(bytes);
                decompressed.position(0);
                window = new SvnDiffWindow(sourceOffset, sourceLength, targetLength, instructionsLength, newDataLength);
                window.setData(decompressed);
                myBuffer.position(bufferPosition);
            } else {
                window = new SvnDiffWindow(sourceOffset, sourceLength, targetLength, instructionsLength, newDataLength);
                window.setData(myBuffer);
            }
            int position = myBuffer.position();
            OutputStream os = consumer.textDeltaChunk(path, window);
            try {
                os.close();
            } catch (IOException ex) {
            }
            myBuffer.position(position + allDataLength);
            int remains = myBuffer.remaining();
            myIsWindowSent = true;

            // then clear the buffer, shift remaining to the beginning.
            myBuffer.compact();
            myBuffer.position(0);
            myBuffer.limit(remains);
        }
    }

    private int deflate(int compressedLength, OutputStream out) throws IOException {
        int originalPosition = myBuffer.position();
        int uncompressedLength = readOffset();
        // substract offset length from the total length.
        if (uncompressedLength == (compressedLength - (myBuffer.position() - originalPosition))) {
            int offset = myBuffer.arrayOffset() + myBuffer.position();
            out.write(myBuffer.array(), offset, uncompressedLength);
        } else {
            byte[] uncompressedData = new byte[uncompressedLength];
            byte[] compressed = myBuffer.array();
            int offset = myBuffer.arrayOffset() + myBuffer.position();
            InputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed, offset, compressedLength));
            int read = 0;
            while (read < uncompressedLength) {
                int r = in.read(uncompressedData, read, uncompressedLength - read);
                if (r < 0) {
                    break;
                }
                read += r;
            }
            out.write(uncompressedData);
        }
        myBuffer.position(originalPosition + compressedLength);
        return uncompressedLength;
    }

    private void appendToBuffer(byte[] data, int offset, int length) {
        int limit = myBuffer.limit(); // amount of pending data?
        if (myBuffer.capacity() < limit + length) {
            ByteBuffer newBuffer = ByteBuffer.allocate((limit + length) * 3 / 2);
            myBuffer.position(0);
            newBuffer.put(myBuffer);
            myBuffer = newBuffer;
        } else {
            myBuffer.limit(limit + length);
            myBuffer.position(limit);
        }
        myBuffer.put(data, offset, length);
        myBuffer.position(0);
        myBuffer.limit(limit + length);
    }

    private int readOffset() {
        myBuffer.mark();
        int offset = 0;
        byte b;
        while (myBuffer.hasRemaining()) {
            b = myBuffer.get();
            offset = (offset << 7) | (b & 0x7F);
            if ((b & 0x80) != 0) {
                continue;
            }
            return offset;
        }
        myBuffer.reset();
        return -1;
    }

    private long readLongOffset() {
        myBuffer.mark();
        long offset = 0;
        byte b;
        while (myBuffer.hasRemaining()) {
            b = myBuffer.get();
            offset = (offset << 7) | (b & 0x7F);
            if ((b & 0x80) != 0) {
                continue;
            }
            return offset;
        }
        myBuffer.reset();
        return -1;
    }

}
