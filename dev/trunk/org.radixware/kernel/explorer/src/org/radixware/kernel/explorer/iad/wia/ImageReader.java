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

import com.trolltech.qt.core.QBuffer;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImageReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;


class ImageReader {
            
    private final IClientEnvironment environment;    
    private boolean isValidBmpImage;
    private final QByteArray imageData = new QByteArray();
    private QImage image;
    private BmpFileHeader bmpFileHeader;
    private Exception exception;
    private String errorTitle;
            
    
    public ImageReader(final IClientEnvironment environment, final boolean isBmp){
        this.environment = environment;
        isValidBmpImage = isBmp;
    }    
    
    public final boolean readFragment(final long offset, final int fragmentLength, final ByteBuffer dataFrame) {
        int dataLength = fragmentLength;
        if (offset==0 && isValidBmpImage){
            final DibHeader dibHeader = DibHeader.read(dataFrame);                
            final String messageTemplate = 
                environment.getMessageProvider().translate("IAD", "Image header received: %1$s");
            debugMsg(String.format(messageTemplate, dibHeader.toString()));
            if (validateBmpHeader(dibHeader)){
                dataLength-=dibHeader.getHeaderSize();
                dibHeader.writeTo(imageData);
                bmpFileHeader = new BmpFileHeader(dibHeader);
            }else{
                isValidBmpImage = false;
                dataFrame.position(0);
                final int rawDataSize = getImageSize();
                if (rawDataSize>0){
                    bmpFileHeader = new BmpFileHeader(rawDataSize);
                }else{
                    return false;
                }                    

            }
        }
        readBmpImageFragment(dataFrame, dataLength);
        return true;
    }
    
    private void readBmpImageFragment(final ByteBuffer dataFrame, final int dataLength){
        for (int i=0; i<dataLength; i++){
            imageData.append(dataFrame.get());
        }
    }
    
    protected final void onException(final String title, final Exception exception){
            this.exception = exception;
            errorTitle = title;
    }
    
    private boolean validateBmpHeader(final DibHeader header){
        if (header.getHeaderSize()<DibHeader.BITMAPINFOHEADER_SIZE || header.getHeaderSize()>DibHeader.BITMAPV5HEADER_SIZE){
            final String messageTemplate = 
                environment.getMessageProvider().translate("IAD", "Unexpected size of image header: %1$s");
            debugMsg(String.format(messageTemplate, header.getHeaderSize()));
            return false;
        }
        if (header.getCompressionMethod()==0){
            final int imageSize = header.getBytesPerLine() * Math.abs(header.getBitmapHeightInPixels());
            if (imageSize!=header.getRawDataSize()){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("IAD", "Size of the image is %1$s but expected %2$s");
                debugMsg(String.format(messageTemplate, header.getRawDataSize(), imageSize));
                return false;
            }
        }
        return true;
    }        
    
    protected final IClientEnvironment getEnvironment(){
        return environment;
    }
    
    private void writeBmpFileHeader(){
        if (bmpFileHeader!=null){
            final int fileSize = imageData.size()+BmpFileHeader.SIZE;
            if (bmpFileHeader.getFileSize()!=fileSize){
                bmpFileHeader = new BmpFileHeader(bmpFileHeader.getRawDataSize(), fileSize);

            }
            final QByteArray fileHeader = new QByteArray();
            bmpFileHeader.writeTo(fileHeader);
            imageData.insert(0, fileHeader);
        }
    }
    
    public final Exception getException(){
        return exception;
    }
    
    public final QImage getImage() throws Exception{//NOPMD
        if (exception!=null){
            throw exception;
        }
        if (image==null){
            writeBmpFileHeader();
            image = new QImageReader(new QBuffer(imageData)).read();
            if (image==null || image.isNull()){                
                final String tempFilePath = File.createTempFile("WIA", null).getAbsolutePath();
                final QFile file = new QFile(tempFilePath);
                file.open(QIODevice.OpenModeFlag.WriteOnly);
                file.write(imageData);
                file.close();
                final QFile.FileError ioError = file.error();
                if (ioError==null || ioError==QFile.FileError.NoError){
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("IAD", "Unsupported image format. The image was stored into file: '%1$s'");
                    final String message = String.format(messageTemplate, tempFilePath);
                    throw new IOException(message);                    
                }else{
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("IAD", "Failed to write image into file '%1$s': %2$s");
                    environment.getTracer().error(String.format(messageTemplate, tempFilePath, ioError.toString()));
                    final String message = environment.getMessageProvider().translate("IAD", "Unsupported image format");
                    throw new IOException(message);                    
                }
            }
            imageData.clear();            
        }
        return image;
    }
    
    public final String getErrorTitle(){
        return errorTitle;
    }
    
    protected final void debugMsg(final String message){
        environment.getTracer().put(EEventSeverity.DEBUG, message, EEventSource.IAD);
    }       
    
    protected int getImageSize(){
        return -1;
    }
}
