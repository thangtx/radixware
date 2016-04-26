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

package org.radixware.kernel.common.msdl.fields.parser.piece;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Stack;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;


public class SmioPieceBerTLV extends SmioPiece {

    public SmioPieceBerTLV(SmioField smioField) throws SmioError {
        super(smioField);
    }

    public static String getFieldName(IDataSource ids) throws IOException {
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

    public static void setFieldName(ExtByteBuffer exbf, byte[] fieldName) {
        if (fieldName.length == 1)
            exbf.extPut(fieldName[0]);
        if (fieldName.length > 1)
            exbf.extPut(ByteBuffer.wrap(fieldName));
    }

    public static void setFieldName(ExtByteBuffer bf, String fieldName) throws SmioException {
        if (fieldName.charAt(0) != 'T') {
            throw new SmioException("BerTLV field name wrong format (field name must begin with 'T')", null, fieldName);
        }
        long tagValue = 0;
        try {
            tagValue = Long.valueOf(fieldName.substring(1), 16).longValue();
        } catch (NumberFormatException e) {
            throw new SmioException("BerTLV field name wrong format", e, fieldName);
        }
        if (tagValue >> 8 != 0) {
            Stack<Integer> bytes = new Stack<>();
            int nextByte = 0;
            while(tagValue > 0) {
                nextByte = (int) (tagValue & 255);
                tagValue >>= 8;
                bytes.push(new Integer(nextByte));
            }
            while(!bytes.empty()) {
                bf.extPut((byte)bytes.pop().intValue());
            }
        } else {
            bf.extPut((byte) tagValue);
        }
    }

    @Override
    public IDataSource parse(IDataSource ids) throws IOException {
        getFieldName(ids);
        long len = ids.get();
        //Ugly crutches with typecasts. Curse Gosling for leaving only SIGNED
        //integer types
        if(len < 0) {
            len += 256;
            if(len > 136) {
                throw new IOException("BER field too long to process");
            }
            len -= 128;
            
            //extracting 'len' number bytes, then creating actual length
            Stack<Integer> lenStack = new Stack<Integer>();
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
        return new DataSourceByteBuffer(ids.get((int)len));
    }

    @Override
    public ByteBuffer merge(ByteBuffer bf) throws SmioException {
        ByteBuffer res = ByteBuffer.allocate(32);
        int lenValue = bf.remaining();
        if(lenValue > 128) {
            double log_two = Math.log(lenValue) / Math.log(2);
            int iLog = (int)log_two;
            if(log_two > iLog)
                iLog++;
            
        }
        res.put((byte) lenValue);
        byte b[] = new byte[lenValue];
        bf.get(b);
        res.put(bf.get(b));
        return res;
    }
}
