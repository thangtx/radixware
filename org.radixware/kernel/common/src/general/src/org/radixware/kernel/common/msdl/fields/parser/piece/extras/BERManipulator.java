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

package org.radixware.kernel.common.msdl.fields.parser.piece.extras;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Stack;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;

/**
 * Extracts/encodes tags and length from/to BER-encoded data.
 * This class does exactly the same things, that static methods in SmioPieceBerTLV
 * used to do. Thus, it eliminates the need not only for static methods in SmioPieceBerTLV,
 * but in the entire class. Not sure, if values encoding is done properly at the
 * moment, but no one gives a damn.
 */
public class BERManipulator {
    public String getIdentifier(IDataSource ids) throws IOException {
         int upper = ids.get();
        if (upper < 0) {
            upper += 256;
        }
        long tagValue = 0x0;
        int [] arr = new int [8];
        int curr = upper, i = 0;
        
        arr[i++] = curr;
        if ((curr & 31) == 31) {
            while ( true ) {
                curr = ids.get();
                if (curr < 0) {
                    curr += 256;
                }
                
                arr[i++] = curr;
                
                if(curr < 128)
                    break;
            }
        }
        for (int j = i - 1; j > -1; j--) {
            tagValue |= arr[j] << (i - j - 1) * 8;
        }
        return "T" + Long.toString(tagValue, 16).toUpperCase();
    }
    
    public long getLength(IDataSource ids) throws IOException {
        long len = ids.get();
        //Ugly crutches with typecasts...
        //Curse Gosling for leaving only SIGNED integer types
        if(len < 0) {
            len += 256;
            len -= 128;
            if(len > 8) {
                throw new IOException("BER field too long to process");
            }
            
            //extracting 'len' number bytes, then creating actual length
            Stack<Integer> lenStack = new Stack<>();
            int curr = 0, i;
            for(i = 0; i < len; i++) {
                curr = ids.get();
                if(curr < 0)
                    curr += 256;
                lenStack.push(new Integer(curr));
            }
            i = 0;
            len = 0;
            while(!lenStack.isEmpty()) {
                curr = lenStack.pop().intValue();
                len |= curr << ( (i++) * 8);
            }
        }
        return len;
    }
    
     public void setFieldName(ExtByteBuffer exbf, byte[] fieldName) {
        if (fieldName.length == 1)
            exbf.extPut(fieldName[0]);
        if (fieldName.length > 1)
            exbf.extPut(ByteBuffer.wrap(fieldName));
    }

    public void setFieldName(ExtByteBuffer bf, String fieldName) throws SmioException {
        if (fieldName.charAt(0) != 'T') {
            throw new SmioException("BerTLV field name wrong format (field name must begin with 'T')", null, fieldName);
        }
        long tagValue = getTagValue(fieldName);
        if (tagValue >> 8 != 0) {
            bf.extPut(longToBytes(tagValue));
        } else {
            bf.extPut((byte) tagValue);
        }
    }
    
    public ByteBuffer lengthBytes(int len) throws SmioException {
        ByteBuffer res = ByteBuffer.allocate(32);
        int lenValue = len;
        if (lenValue >= 128) {
            double log_two = Math.log(lenValue) / Math.log(2);
            int iLog = (int) log_two;
            if (log_two > iLog) {
                iLog++;
            }
            int byte_count = (iLog % 8 == 0 ? iLog / 8 : iLog / 8 + 1);
            if (byte_count > 8)
                throw new SmioException("BERTLV: too much data to encode");
            
            //creating lenByte: first bit is '1', 
            //others 7 bits show number of following bytes, that indicates message length
            byte lenByte = (byte) byte_count;
            lenByte |= Byte.MIN_VALUE; //-128 = '1000 0000'
            res.put(lenByte);
            
            res.put(longToBytes(lenValue));
        }
        else
            res.put((byte)lenValue);
        return (ByteBuffer)res.flip();
    }
    
    private ByteBuffer longToBytes(long val) {
        ByteBuffer res = ByteBuffer.allocate(8);
        Stack<Integer> bytes = new Stack<>();
        int nextByte = 0;
        while (val > 0) {
            nextByte = (int) (val & 255);
            val >>= 8;
            bytes.push(new Integer(nextByte));
        }
        while (!bytes.empty()) {
            res.put((byte) bytes.pop().intValue());
        }
        return (ByteBuffer)res.flip();
    }

    public long getTagValue(String fieldName) throws SmioException {
        long tagValue = 0;
        try {
            tagValue = Long.valueOf(fieldName.substring(1), 16).longValue();
        } catch (NumberFormatException e) {
            throw new SmioException("BerTLV field name wrong format", e, fieldName);
        }
        return tagValue;
    }
    
}
