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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.zip.DeflaterOutputStream;

class SvnDiffWindow {

    public static final byte[] SVN_HEADER = new byte[]{'S', 'V', 'N', '\0'};

    public static final byte[] SVN1_HEADER = new byte[]{'S', 'V', 'N', '\1'};

    public static final SvnDiffWindow EMPTY = new SvnDiffWindow(0, 0, 0, 0, 0);

    private final long sourceViewOffset;
    private final int sourceViewLength;
    private final int targetViewLength;
    private final int newDataLength;
    private int infosLength;

    private SvnDiffInfo templateInfo = new SvnDiffInfo(0, 0, 0);
    private SvnDiffInfo templateNextInfo = new SvnDiffInfo(0, 0, 0);

    private byte[] dataBuffer;
    private int currentDataOffset;
    private int countOfInfos;

    public SvnDiffWindow(long sourceViewOffset, int sourceViewLength, int targetViewLength, int instructionsLength, int newDataLength) {
        this.sourceViewOffset = sourceViewOffset;
        this.sourceViewLength = sourceViewLength;
        this.targetViewLength = targetViewLength;
        this.infosLength = instructionsLength;
        this.newDataLength = newDataLength;
    }

    public int getInstructionsLength() {
        return infosLength;
    }

    public long getSourceViewOffset() {
        return sourceViewOffset;
    }

    public int getSourceViewLength() {
        return sourceViewLength;
    }

    public int getTargetViewLength() {
        return targetViewLength;
    }

    public int getNewDataLength() {
        return newDataLength;
    }

    public Iterator infos() {
        return infos(false);
    }

    public Iterator infos(boolean template) {
        return new SvnDiffInfoIterator(template);
    }

    public void apply(SvnDiffWindowApplyInfo applyInfo) throws IOException {
        if (applyInfo.targetData == null || applyInfo.targetViewSize < getTargetViewLength()) {
            applyInfo.targetData = new byte[getTargetViewLength()];
        }
        applyInfo.targetViewSize = getTargetViewLength();
        int length = 0;
        if (getSourceViewOffset() != applyInfo.sourceViewPosition || getSourceViewLength() > applyInfo.sourceViewLength) {
            byte[] oldSourceBuffer = applyInfo.sourceData;
            applyInfo.sourceData = new byte[getSourceViewLength()];
            if (applyInfo.sourceViewPosition + applyInfo.sourceViewLength > getSourceViewOffset()) {
                int start = (int) (getSourceViewOffset() - applyInfo.sourceViewPosition);
                System.arraycopy(oldSourceBuffer, start, applyInfo.sourceData, 0, (applyInfo.sourceViewLength - start));
                length = (applyInfo.sourceViewLength - start);
            }
        }
        if (length < getSourceViewLength()) {
            int toSkip = (int) (getSourceViewOffset() - (applyInfo.sourceViewPosition + applyInfo.sourceViewLength));
            if (toSkip > 0) {
                applyInfo.inputStream.skip(toSkip);
            }
            SvnUtil.readStreamData(applyInfo.inputStream, applyInfo.sourceData, length, applyInfo.sourceData.length - length);

        }
        applyInfo.sourceViewLength = getSourceViewLength();
        applyInfo.sourceViewPosition = getSourceViewOffset();

        int tpos = 0;
        int npos = infosLength;

        for (Iterator instructions = infos(true); instructions.hasNext();) {
            SvnDiffInfo instruction = (SvnDiffInfo) instructions.next();
            int iLength = instruction.length < getTargetViewLength() - tpos ? (int) instruction.length : getTargetViewLength() - tpos;
            switch (instruction.type) {
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_NEW_DATA:
                    System.arraycopy(dataBuffer, currentDataOffset + npos, applyInfo.targetData, tpos, iLength);
                    npos += iLength;
                    break;
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_TARGET:
                    int start = instruction.offset;
                    int end = instruction.offset + iLength;
                    int tIndex = tpos;
                    for (int j = start; j < end; j++) {
                        applyInfo.targetData[tIndex] = applyInfo.targetData[j];
                        tIndex++;
                    }
                    break;
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_SOURCE:
                    System.arraycopy(applyInfo.sourceData, instruction.offset, applyInfo.targetData, tpos, iLength);
                    break;
                default:
            }
            tpos += instruction.length;
            if (tpos >= getTargetViewLength()) {
                break;
            }
        }
        if (applyInfo.digest != null) {
            applyInfo.digest.update(applyInfo.targetData, 0, getTargetViewLength());
        }
        applyInfo.outputStream.write(applyInfo.targetData, 0, getTargetViewLength());
    }

    public int apply(byte[] sourceBuffer, byte[] targetBuffer) {
        int dataOffset = infosLength;
        int tpos = 0;
        for (Iterator instructions = infos(true); instructions.hasNext();) {
            SvnDiffInfo instruction = (SvnDiffInfo) instructions.next();
            int iLength = instruction.length < getTargetViewLength() - tpos ? (int) instruction.length : getTargetViewLength() - tpos;
            switch (instruction.type) {
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_NEW_DATA:
                    System.arraycopy(dataBuffer, currentDataOffset + dataOffset, targetBuffer, tpos, iLength);
                    dataOffset += iLength;
                    break;
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_TARGET:
                    int start = instruction.offset;
                    int end = instruction.offset + iLength;
                    int tIndex = tpos;
                    for (int j = start; j < end; j++) {
                        targetBuffer[tIndex] = targetBuffer[j];
                        tIndex++;
                    }
                    break;
                case SvnDiffInfo.INFO_TYPE_COPY_FROM_SOURCE:
                    System.arraycopy(sourceBuffer, instruction.offset, targetBuffer, tpos, iLength);
                    break;
                default:
            }
            tpos += instruction.length;
            if (tpos >= getTargetViewLength()) {
                break;
            }
        }
        return getTargetViewLength();
    }

    public void setData(ByteBuffer buffer) {
        dataBuffer = buffer.array();
        currentDataOffset = buffer.position() + buffer.arrayOffset();
    }

    public boolean hasInstructions() {
        return infosLength > 0;
    }

    public void writeTo(OutputStream os, boolean writeHeader) throws IOException {
        writeTo(os, writeHeader, false);
    }

    public void writeTo(OutputStream os, boolean writeHeader, boolean compress) throws IOException {
        if (writeHeader) {
            os.write(compress ? SVN1_HEADER : SVN_HEADER);
        }
        if (!hasInstructions()) {
            return;
        }
        ByteBuffer offsets = ByteBuffer.allocate(100);
        SvnDiffInfo.writeLong(offsets, sourceViewOffset);
        SvnDiffInfo.writeInt(offsets, sourceViewLength);
        SvnDiffInfo.writeInt(offsets, targetViewLength);

        ByteBuffer instructions = null;
        ByteBuffer newData = null;
        int instLength = 0;
        int dataLength = 0;
        if (compress) {
            instructions = inflate(dataBuffer, currentDataOffset, infosLength);
            instLength = instructions.remaining();
            newData = inflate(dataBuffer, currentDataOffset + infosLength, newDataLength);
            dataLength = newData.remaining();
            SvnDiffInfo.writeInt(offsets, instLength);
            SvnDiffInfo.writeInt(offsets, dataLength);
        } else {
            SvnDiffInfo.writeInt(offsets, infosLength);
            SvnDiffInfo.writeInt(offsets, newDataLength);
        }
        os.write(offsets.array(), offsets.arrayOffset(), offsets.position());
        if (compress) {
            os.write(instructions.array(), instructions.arrayOffset(), instructions.remaining());
            os.write(newData.array(), newData.arrayOffset(), newData.remaining());
        } else {
            os.write(dataBuffer, currentDataOffset, infosLength);
            if (newDataLength > 0) {
                os.write(dataBuffer, currentDataOffset + infosLength, newDataLength);
            }
        }
    }

    public int getDataLength() {
        return newDataLength + infosLength;
    }

    public boolean hasCopyFromSourceInstructions() {
        for (Iterator instrs = infos(true); instrs.hasNext();) {
            SvnDiffInfo instruction = (SvnDiffInfo) instrs.next();
            if (instruction.type == SvnDiffInfo.INFO_TYPE_COPY_FROM_SOURCE) {
                return true;
            }
        }
        return false;
    }

    public SvnDiffWindow clone(ByteBuffer targetData) {
        int targetOffset = targetData.position() + targetData.arrayOffset();
        int position = targetData.position();
        targetData.put(dataBuffer, currentDataOffset, infosLength + newDataLength);
        targetData.position(position);
        SvnDiffWindow clone = new SvnDiffWindow(getSourceViewOffset(), getSourceViewLength(), getTargetViewLength(),
                getInstructionsLength(), getNewDataLength());
        clone.setData(targetData);
        clone.currentDataOffset = targetOffset;
        return clone;
    }

    private static ByteBuffer inflate(byte[] src, int offset, int length) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(length * 2 + 2);
        SvnDiffInfo.writeInt(buffer, length);
        if (length < 512) {
            buffer.put(src, offset, length);
        } else {
            DeflaterOutputStream out = new DeflaterOutputStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    buffer.put((byte) (b & 0xFF));
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    buffer.put(b, off, len);
                }

                @Override
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                }
            });
            out.write(src, offset, length);
            out.finish();
            if (buffer.position() >= length) {
                buffer.clear();
                SvnDiffInfo.writeInt(buffer, length);
                buffer.put(src, offset, length);
            }
        }
        buffer.flip();
        return buffer;
    }

    private class SvnDiffInfoIterator implements Iterator {

        private SvnDiffInfo nextInsruction;
        private int myOffset;
        private int myNewDataOffset;
        private boolean myIsTemplate;

        public SvnDiffInfoIterator(boolean useTemplate) {
            myIsTemplate = useTemplate;
            nextInsruction = readNextInfo();
        }

        @Override
        public boolean hasNext() {
            return nextInsruction != null;
        }

        @Override
        public Object next() {
            if (nextInsruction == null) {
                return null;
            }

            if (myIsTemplate) {
                templateNextInfo.type = nextInsruction.type;
                templateNextInfo.length = nextInsruction.length;
                templateNextInfo.offset = nextInsruction.offset;
                nextInsruction = readNextInfo();
                return templateNextInfo;
            }
            Object next = nextInsruction;
            nextInsruction = readNextInfo();
            return next;
        }

        @Override
        public void remove() {
        }

        private SvnDiffInfo readNextInfo() {
            if (dataBuffer == null || myOffset >= infosLength) {
                return null;
            }
            SvnDiffInfo instruction = myIsTemplate ? templateInfo : new SvnDiffInfo();
            instruction.type = (dataBuffer[currentDataOffset + myOffset] & 0xC0) >> 6;
            instruction.length = dataBuffer[currentDataOffset + myOffset] & 0x3f;
            myOffset++;
            if (instruction.length == 0) {
                instruction.length = readInt();
            }
            if (instruction.type == 0 || instruction.type == 1) {
                instruction.offset = readInt();
            } else {
                instruction.offset = myNewDataOffset;
                myNewDataOffset += instruction.length;
            }
            return instruction;
        }

        private int readInt() {
            int result = 0;
            while (true) {
                byte b = dataBuffer[currentDataOffset + myOffset];
                result = result << 7;
                result = result | (b & 0x7f);
                if ((b & 0x80) != 0) {
                    myOffset++;
                    if (myOffset >= infosLength) {
                        return -1;
                    }
                    continue;
                }
                myOffset++;
                return result;
            }
        }
    }

    public SvnDiffInfo[] loadDiffInfos(SvnDiffInfo[] target) {
        int index = 0;
        for (Iterator instructions = infos(); instructions.hasNext();) {
            if (index >= target.length) {
                SvnDiffInfo[] newTarget = new SvnDiffInfo[index * 3 / 2];
                System.arraycopy(target, 0, newTarget, 0, index);
                target = newTarget;
            }
            target[index] = (SvnDiffInfo) instructions.next();
            index++;
        }
        countOfInfos = index;
        return target;
    }

    public int getInfosCount() {
        return countOfInfos;
    }

    public void submitNewData(ByteBuffer target, int offset, int length) {
        offset += currentDataOffset + infosLength;
        target.put(dataBuffer, offset, length);
    }

}
