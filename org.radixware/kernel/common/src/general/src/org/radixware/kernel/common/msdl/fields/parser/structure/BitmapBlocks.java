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

package org.radixware.kernel.common.msdl.fields.parser.structure;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.schemas.msdl.EncodingDef;
import org.radixware.schemas.msdl.Structure.Bitmap;


public class BitmapBlocks {

    private final int bitmapBlockLength;
    private final boolean bitmapIsContinue;
    private final EncodingDef.Enum encoding;
    private CharsetEncoder encoderEBCDIC;
    private CharsetDecoder decoderEBCDIC;
    public int size;
    private int processedBlocksCount = 0;
    private boolean pendingBlocks = false;

    public BitmapBlocks(StructureFieldModel model) {
        Bitmap b = model.getField().getStructure().getBitmap();
        bitmapBlockLength = b.getBlockLength().intValue();
        bitmapIsContinue = b.getFirstBitBehavior().equals("Continue");
        encoding = EncodingDef.Enum.forString(b.getEncoding());
        if (encoding == EncodingDef.EBCDIC) {
            encoderEBCDIC = Charset.forName("IBM500").newEncoder();
            decoderEBCDIC = Charset.forName("IBM500").newDecoder();
        }
        size = 0;
        for (MsdlField cur : model.getFields()) {
            String fn = cur.getName();
            if (fn.charAt(0) != 'F' || fn.length() < 2) {
                continue;
            }
            try {
                if (Integer.decode(fn.substring(1)) == null) {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }
            int idx = Integer.decode(fn.substring(1));
            if (idx > size) {
                size = idx;
            }
        }
    }

    public int getIdxByName(String name) {
        int n = Integer.decode(name.substring(1));
//        if (bitmapIsContinue)
//            n -= 1;
        return n - 1;
    }

    public void merge(ExtByteBuffer ebf, boolean[] bitmap) throws IOException {
        int lastOnBit = 0;
        for (int i = 0; i < bitmap.length; i++) {
            if (bitmap[i]) {
                lastOnBit = i;
            }
        }
        int idx = getBitmapIndex();
        int blockCount = 0;
        boolean firstTwoBlocks = processedBlocksCount < 2 ? true : false;
        int oldProcessedBLocks = processedBlocksCount;

        while (idx <= lastOnBit && 
                (((blockCount < 1) && !firstTwoBlocks) 
                  || ((blockCount <2) && firstTwoBlocks ) )) {
            for (int byteCount = 0; byteCount < bitmapBlockLength; byteCount++) {
                byte b = 0;
                for (int i = 0; i < 8; i++) {
                    if (bitmapIsContinue && byteCount == 0 && i == 0) {
                        if (blockCount + oldProcessedBLocks < (lastOnBit / (bitmapBlockLength * 8))) {
                            b |= (1 << 7);
                            pendingBlocks = true;
                        }
                        else {
                            pendingBlocks = false;
                        }
                    }
                    if (idx >= bitmap.length) {
                        break;
                    }
                    if (bitmap[idx++]) {
                        b |= (1 << (7 - i));
                    }
                }
                ebf.extPut(ByteBuffer.wrap(encode(b)));
            }
            blockCount++;
        }
        processedBlocksCount += blockCount;
    }

    public byte[] parse(IDataSource ids, boolean[] bitmap) throws IOException {
        int idx = getBitmapIndex();
        int byteCount = 0;
        boolean nextBlock = false;
        ExtByteBuffer bf = new ExtByteBuffer();
        int l = getBitmapLengthInBytes() * bitmapBlockLength;
        while (idx < l) {
            byte b = decode(ids);
            bf.extPut(b);
            for (int i = 0; i < 8; i++) {
                boolean bit = (((b >> (7 - i)) & 1) == 1);
                if (byteCount == 0 && i == 0 && bitmapIsContinue) {
                    nextBlock = bit;
                    pendingBlocks = true;
//                    if (nextBlock)
//                        continue;
                }
                if (idx < bitmap.length) {
                    bitmap[idx++] = bit;
                }
            }
            byteCount++;
            if (byteCount == bitmapBlockLength) {
                processedBlocksCount++;
                if (nextBlock && processedBlocksCount == 1) {
                    nextBlock = false;
                    byteCount = 0;
                } else {
                    if (!nextBlock) {
                        pendingBlocks = false;
                    }
                    break;
                }
            }
        }
        return ParseUtil.extractByteBufferContent(bf.flip());
    }

    public byte decode(IDataSource ids) throws IOException {
        switch (encoding.intValue()) {
            case EncodingDef.INT_HEX:
                return Hex.decode(new String(ids.get(2), "US-ASCII"))[0];
            case EncodingDef.INT_EBCDIC:
                decoderEBCDIC.decode(ByteBuffer.wrap(ids.get(1)));
                break;
            default:
                break;
        }
        return ids.get();
    }

    public byte[] encode(byte b) throws IOException {
        switch (encoding.intValue()) {
            case EncodingDef.INT_HEX:
                return Hex.encode(new byte[]{b}).getBytes("US-ASCII");
            case EncodingDef.INT_EBCDIC:
                encoderEBCDIC.encode(CharBuffer.wrap(new char[]{(char) b}));
                break;
            default:
                break;
        }
        return new byte[]{b};
    }

    private int getBitmapLengthInBytes() {
        return size / (bitmapBlockLength) + 1;
    }

    public int getBlocksCount() {
        return getBitmapLengthInBytes() / 8;
    }

    public int getProcessedBlocksCount() {
        return processedBlocksCount;
    }

    public static int getFieldIndex(String fn) {
        if (fn.charAt(0) != 'F' || fn.length() < 2) {
            return -1;
        }
        try {
            if (Integer.decode(fn.substring(1)) == null) {
                return -2;
            }
        } catch (NumberFormatException e) {
            return -3;
        }
        return Integer.decode(fn.substring(1));
    }

    public boolean isIndexWithinProcessedBlocks(int index) {
        return (index < (getProcessedBlocksCount()-1) * bitmapBlockLength * 8);
    }

    public void reset() {
        processedBlocksCount = 0;
    }

    public boolean hasPendingBlocks() {
        return pendingBlocks;
    }

    private int getBitmapIndex() {
        return processedBlocksCount * bitmapBlockLength * 8;
    }
    
    public boolean mustMergeNextBitmapBlock(int index) {
        return bitmapIsContinue && pendingBlocks && index  > ( (processedBlocksCount -1) * bitmapBlockLength * 8);
    }
    
    public boolean hasBitmapIsContinue() {
        return bitmapIsContinue;
    }    
}
