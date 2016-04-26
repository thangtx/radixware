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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


final class DibHeader {
    
    public static final int BITMAPINFOHEADER_SIZE=40;
    public static final int BITMAPV5HEADER_SIZE=124;
    
    
    private int headerSize;
    private int width;
    private int height;
    private int colorPanes;
    private int colorDepth;
    private int compression;
    private int size;
    private int hResolution;
    private int vResolution;
    private int numColors;
    private int numImpColors;
    private byte[] extraData;
    
    private DibHeader(){        
    }
    
    public static DibHeader read(final byte[] arrBytes){
        return read(ByteBuffer.wrap(arrBytes));
    }
    
    public static DibHeader read(final ByteBuffer bb){                
        final DibHeader header = new DibHeader();
        bb.order(ByteOrder.LITTLE_ENDIAN);
        header.headerSize = bb.getInt();
        header.width = bb.getInt();
        header.height = bb.getInt();
        header.colorPanes = bb.getChar();
        header.colorDepth = bb.getChar();
        header.compression = bb.getInt();
        header.size = bb.getInt();
        header.hResolution = bb.getInt();
        header.vResolution = bb.getInt();
        header.numColors = bb.getInt();
        header.numImpColors = bb.getInt(); 
        if (header.headerSize>BITMAPINFOHEADER_SIZE && header.headerSize<=BITMAPV5HEADER_SIZE){
            final int extraDataLength = header.headerSize - BITMAPINFOHEADER_SIZE;
            header.extraData = new byte[extraDataLength];
            bb.get(header.extraData);
        }
        return header;        
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public int getBitmapWidthInPixels() {
        return width;
    }

    public int getBitmapHeightInPixels() {
        return height;
    }

    public int getNumberOfColorPanes() {
        return colorPanes;
    }

    public int getColorDepth() {
        return colorDepth;
    }

    public int getCompressionMethod() {
        return compression;
    }

    public int getRawDataSize() {
        return size;
    }

    public int getHorizontalResolution() {
        return hResolution;
    }

    public int getVerticalResolution() {
        return vResolution;
    }

    public int getNumberOfColors() {
        return numColors;
    }

    public int getNumberOfImportantColors() {
        return numImpColors;
    }
    
    public int getColorTableSize(){
        return getNumberOfColors()*4;
    }
    
    public int getBytesPerLine(){
        return ((getColorDepth()*Math.abs(getBitmapWidthInPixels())+31)/32) * 4;
    }
    
    public DibHeader createCopyWithNewHeight(final int height){
        final DibHeader header = new DibHeader();
        header.headerSize = this.headerSize;
        header.width = this.width;
        header.height = height;
        header.colorPanes = this.colorPanes;
        header.colorDepth = this.colorDepth;
        header.compression = this.compression;
        header.size = Math.abs(height)*getBytesPerLine();
        header.hResolution = this.hResolution;
        header.vResolution = this.vResolution;
        header.numColors = this.numColors;
        header.numImpColors = this.numImpColors; 
        if (this.extraData!=null){
            header.extraData = new byte[this.extraData.length];
            System.arraycopy(this.extraData, 0, header.extraData, 0, this.extraData.length);
        }
        return header;
    }
    
    public void writeTo(final ByteBuffer bb){
        bb.order(ByteOrder.LITTLE_ENDIAN);        
        bb.putInt(headerSize);
        bb.putInt(width);
        bb.putInt(height);
        bb.putChar((char)colorPanes);
        bb.putChar((char)colorDepth);
        bb.putInt(compression);
        bb.putInt(size);
        bb.putInt(hResolution);
        bb.putInt(vResolution);
        bb.putInt(numColors);
        bb.putInt(numImpColors);
        if (extraData!=null && extraData.length>0){
            bb.put(extraData);
        }        
    }
    
    public void writeTo(final QByteArray ba){
        final byte[] header = new byte[getHeaderSize()];
        writeTo(ByteBuffer.wrap(header));
        for (int i=0; i<header.length; i++){
            ba.append(header[i]);
        }
    }
    
    @Override
    public String toString(){
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{\n");
        appendField("Header size", headerSize, strBuilder);
        appendField("Bitmap width in pixels", width, strBuilder);
        appendField("Bitmap height in pixels", height, strBuilder);
        appendField("Number of color panes", colorPanes, strBuilder);
        appendField("Color depth", colorDepth, strBuilder);
        appendField("Compression method", compression, strBuilder);
        appendField("Image size", size, strBuilder);
        appendField("Horizontal resolution", hResolution, strBuilder);
        appendField("Vertical resolution", vResolution, strBuilder);
        appendField("The number of colors in the color palette", numColors, strBuilder);
        appendField("The number of important colors used", numImpColors, strBuilder);
        final int extraDataLength = extraData==null ? 0 : extraData.length;
        appendField("Extra data length", extraDataLength, strBuilder);
        strBuilder.append('}');
        return strBuilder.toString();
    }
    
    private static void appendField(final String fieldName, final int fieldValue, final StringBuilder dest){
        dest.append('\t');
        dest.append(fieldName);
        dest.append(": ");
        dest.append(fieldValue);
        dest.append('\n');
    }
}

