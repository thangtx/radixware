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

package org.radixware.kernel.explorer.iad.wia;

import com.trolltech.qt.core.QByteArray;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;


final class BmpFileHeader {
    
    public final static int SIZE = 14;
    
    private final static byte FIRST_BYTE = 0x42;
    private final static byte SECOND_BYTE = 0x4d;    
    
    private final int dataSize;
    private final int fileSize;
    
    public BmpFileHeader(final int dataSize){
        this(dataSize, dataSize+SIZE+DibHeader.BITMAPINFOHEADER_SIZE);
    }
    
    public BmpFileHeader(final int dataSize, final int fileSize){
        this.dataSize = dataSize;
        this.fileSize = fileSize;        
    }
    
    public BmpFileHeader(final DibHeader dibHeader){
        this(dibHeader.getRawDataSize(),SIZE+dibHeader.getHeaderSize()+dibHeader.getColorTableSize()+dibHeader.getRawDataSize());
    }
    
    public int getRawDataSize(){
        return dataSize;
    }
    
    public int getFileSize(){
        return fileSize;
    }      
    
    public ByteBuffer getBytes(){
        final byte[] header = new byte[SIZE];
        final ByteBuffer result = ByteBuffer.wrap(header);
        writeTo(result);
        result.position(0);
        return result;
    }
    
    public void writeTo(final byte[] byteArray){
        writeTo(ByteBuffer.wrap(byteArray));
    }
    
    public void writeTo(final ByteBuffer bb){
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(FIRST_BYTE);
        bb.put(SECOND_BYTE);
        bb.putInt(getFileSize());
        bb.putChar((char)0);
        bb.putChar((char)0);
        bb.putInt(getFileSize()-getRawDataSize());      
    }
    
    public void writeTo(final QByteArray ba){
        final ByteBuffer bb = getBytes();
        for (int i=0,count=bb.limit(); i<count; i++){
            ba.append(bb.get());
        }
    }
    
    public void writeToFile(final String filePath) throws FileNotFoundException, IOException{
        final byte[] header = new byte[SIZE];
        writeTo(header);        
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
            file.write(header);
        }
    }
}
