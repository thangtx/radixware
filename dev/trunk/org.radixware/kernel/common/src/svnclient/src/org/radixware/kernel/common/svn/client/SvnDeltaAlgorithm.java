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

import java.nio.ByteBuffer;

/**
 * @version 1.3
 * @author TMate Software Ltd.
 */
public abstract class SvnDeltaAlgorithm {

    private ByteBuffer newDataBuffer;
    private ByteBuffer dataBuffer;
    private int newDataLength;
    private int infosLength;
    private SvnDiffInfo templateInstruction;

    public SvnDeltaAlgorithm() {
        newDataBuffer = ByteBuffer.allocate(1024);
        dataBuffer = ByteBuffer.allocate(2048);
        templateInstruction = new SvnDiffInfo(0, 0, 0);
    }

    public void reset() {
        newDataBuffer.clear();
        dataBuffer.clear();
        infosLength = 0;
        newDataLength = 0;
    }

    public abstract void computeDelta(byte[] a, int aLength, byte[] b, int bLength);

    public ByteBuffer getData() {
        if (newDataBuffer.position() > 0) {
            dataBuffer = ensureBufferSize(dataBuffer, newDataBuffer.position());
            dataBuffer.put(newDataBuffer.array(), 0, newDataBuffer.position());
            newDataBuffer.clear();
        }
        dataBuffer.flip();
        return dataBuffer;
    }

    public int getInstructionsLength() {
        return infosLength;
    }

    public int getNewDataLength() {
        return newDataLength;
    }

    protected void copyFromSource(int position, int length) {
        templateInstruction.type = SvnDiffInfo.INFO_TYPE_COPY_FROM_SOURCE;
        templateInstruction.offset = position;
        templateInstruction.length = length;
        dataBuffer = ensureBufferSize(dataBuffer, 10);
        templateInstruction.writeTo(dataBuffer);
        infosLength = dataBuffer.position();
    }

    protected void copyFromTarget(int position, int length) {
        templateInstruction.type = SvnDiffInfo.INFO_TYPE_COPY_FROM_TARGET;
        templateInstruction.offset = position;
        templateInstruction.length = length;
        dataBuffer = ensureBufferSize(dataBuffer, 10);
        templateInstruction.writeTo(dataBuffer);
        infosLength = dataBuffer.position();
    }

    protected void copyFromNewData(byte[] data, int offset, int length) {
        templateInstruction.type = SvnDiffInfo.INFO_TYPE_COPY_FROM_NEW_DATA;
        templateInstruction.offset = 0;
        templateInstruction.length = length;
        dataBuffer = ensureBufferSize(dataBuffer, 10);
        templateInstruction.writeTo(dataBuffer);
        infosLength = dataBuffer.position();
        newDataBuffer = ensureBufferSize(newDataBuffer, length);
        newDataBuffer.put(data, offset, length);
        newDataLength += length;
    }

    private static ByteBuffer ensureBufferSize(ByteBuffer buffer, int size) {
        if (buffer.remaining() < size) {
            ByteBuffer newBuffer = ByteBuffer.allocate((buffer.position() + size) * 3 / 2);
            newBuffer.put(buffer.array(), 0, buffer.position());
            buffer = newBuffer;
        }
        return buffer;
    }
}
