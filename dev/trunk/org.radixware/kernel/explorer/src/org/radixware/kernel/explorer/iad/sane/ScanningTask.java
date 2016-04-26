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

package org.radixware.kernel.explorer.iad.sane;

import com.trolltech.qt.QNativePointer;
import com.trolltech.qt.gui.QImage;
import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneException;
import com.tuneology.sane.SaneParameters;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.env.progress.ICancellableTask;
import org.radixware.kernel.explorer.env.progress.IMeasurableTask;
import org.radixware.kernel.explorer.env.progress.ITitledTask;
import org.radixware.kernel.explorer.iad.IadException;


public final class ScanningTask implements Runnable, ICancellableTask, IMeasurableTask, ITitledTask{        
    
    public static enum ReadStatus{ PREPARE_FOR_READ, READ_ON_GOING, READ_ERROR, READ_CANCEL, READ_READY};
    
    private static int PREVIEW_FRAME_SIZE=100000;    
    
    private final SaneDevice device;
    private final byte[] frameData = new byte[PREVIEW_FRAME_SIZE];
    private QImage image;
    public final Object semaphore = new Object();
    private final boolean invertColors;   
    private volatile long bytesToRead;
    private int pixelsInFrame;
    private int readFrames;
    private volatile ReadStatus status;
    private volatile Exception exception;    
    private SaneParameters frameParameters;
    private int pixelX, pixelY;
    private int pixelIndex;
    private volatile boolean imageWasResized;
    private final int[] pxColors = new int[3];
    private int pxColorsIndex;
    private final MessageProvider messageProvider;
    
    public ScanningTask(final SaneDevice device,
                              final boolean invertColors, 
                              final MessageProvider messageProvider){
        this.device = device;
        this.invertColors = invertColors;
        this.messageProvider = messageProvider;
    }

    @Override
    public void run() {
        status = ReadStatus.PREPARE_FOR_READ;
        bytesToRead = 0;
        //m_saneStartDone = false;

        // Start the scanning with sane_start
        if (!startScanNextFrame()){
            return;
        }
        
        if (!readCurrentFrameParameters()){
            return;
        }

        pixelsInFrame = frameParameters.lines * frameParameters.bytes_per_line;
        // calculate data size
        if ((frameParameters.format == Sane.FRAME_RED) ||
            (frameParameters.format == Sane.FRAME_GREEN) ||
            (frameParameters.format == Sane.FRAME_BLUE))
        {
            // this is unfortunately calculated again for every frame....
            bytesToRead = pixelsInFrame*3;
        }
        else {
            bytesToRead = pixelsInFrame;
        }
        
        // create a new image
        if (frameParameters.lines > 0) {            
            image = new QImage(frameParameters.pixels_per_line, frameParameters.lines, QImage.Format.Format_RGB32);
        }
        else {
            // handscanners have the number of lines -1 -> make room for something
            image = new QImage(frameParameters.pixels_per_line, frameParameters.pixels_per_line, QImage.Format.Format_RGB32);
        }
        imageWasResized = true;
        image.fill(0xFFFFFFFF);
        
        status = ReadStatus.READ_ON_GOING;
        exception = null;
        readFrames = 0;
        while (status == ReadStatus.READ_ON_GOING) {
            readData();
        }
    }
    
    private boolean startScanNextFrame(){
        try{
            device.startScan();
            return true;
        }catch(SaneException exception){
            this.exception = exception;
            status = ReadStatus.READ_ERROR;            
            device.cancelScan();            
            return false;
        }        
    }
    
    private boolean readCurrentFrameParameters(){
        try{
            frameParameters = device.getFrameParameters();
            return true;
        }catch(SaneException exception){
            this.exception = exception;
            status = ReadStatus.READ_ERROR;
            device.cancelScan();            
            return false;
        }    
    }
    
    private void readData(){
        final int readBytes;
        try{
            readBytes = device.read(frameData);
        }catch(SaneException exception){
            this.exception = exception;
            status = ReadStatus.READ_ERROR;
            device.cancelScan();
            return;
        }
        synchronized(semaphore){
            if (readBytes<0){//current frame was read
                if (pixelIndex < pixelsInFrame) {
                    final String message = "Unable to read frame: expected frame size is %1$s but only %2$s bytes was read";
                    exception = new IadException(String.format(message, pixelsInFrame, pixelIndex));
                    status = ReadStatus.READ_ERROR;
                    return;
                }else if (frameParameters.last_frame){
                    status = ReadStatus.READ_READY;
                    return;
                }else{
                    // start reading next frame
                    if (!startScanNextFrame()){
                        return;
                    }

                    if (!readCurrentFrameParameters()){
                        return;
                    }                
                    pixelIndex = 0;
                    pixelX     = 0;
                    pixelY     = 0;
                    pxColorsIndex  = 0;
                    readFrames++;                
                }
            }       
            processFrameData(readBytes);        
        }
    }
    
    private void processFrameData(final int readBytes){
        if (invertColors) {
            if (frameParameters.depth >= 8) {
                for(int i=0; i< readBytes; i++) {
                    frameData[i] = (byte)(255 - byteToInt(frameData[i]));
                }
            }
            if (frameParameters.depth == 1) {
                for(int i=0; i<readBytes; i++) {
                    frameData[i] = (byte)~frameData[i];
                }
            }
        }
        switch (frameParameters.format)
        {
            case Sane.FRAME_GRAY:{
                if (frameParameters.depth == 1) {
                    int i, j;
                    for (i=0; i<readBytes; i++) {
                        if (pixelY >= image.height()) {
                            resizeImage(false);
                        }
                        for (j=7; j>=0; --j) {
                            if ((frameData[i] & (1<<j)) == 0) {
                                image.setPixel(pixelX, pixelY, 0xFFFFFF);
                            }else {                            
                                image.setPixel(pixelX, pixelY, 0);
                            }
                            pixelX++;
                            if(pixelX >= frameParameters.pixels_per_line) {
                                pixelX = 0;
                                pixelY++;
                                break;
                            }
                            if (pixelY >= frameParameters.lines){
                                break;
                            }
                        }
                        pixelIndex++;
                    }
                    return;
                } else if (frameParameters.depth == 8) {
                    int index;
                    QNativePointer imageData = image.bits();
                    for (int i=0; i<readBytes; i++) {
                        index = pixelIndex * 4;
                        if ((index + 2) >image.numBytes()) {
                            imageData = resizeImage(true);
                        }
                        imageData.setByteAt(index, frameData[i]);
                        imageData.setByteAt(index+1, frameData[i]);
                        imageData.setByteAt(index+2, frameData[i]);
                        pixelIndex++;
                    }
                    return;
                }
                else if (frameParameters.depth == 16) {
                    int index;
                    QNativePointer imageData = image.bits();
                    for (int i=0; i<readBytes; i++) {
                        if (pixelIndex%2 == 0) {
                            index = pixelIndex * 2;
                            if ((index + 2) > image.numBytes()) {
                                imageData = resizeImage(true);
                            }
                            imageData.setByteAt(index, frameData[i+1]);
                            imageData.setByteAt(index+1, frameData[i+1]);
                            imageData.setByteAt(index+2, frameData[i+1]);
                        }
                        pixelIndex++;
                    }
                    return;
                }
                break;
            }
            case Sane.FRAME_RGB:{
                if (frameParameters.depth == 8) {
                    for (int i=0; i<readBytes; i++) {
                        pxColors[pxColorsIndex] = frameData[i];
                        incColorIndex();
                        pixelIndex++;
                        if (pxColorsIndex == 0) {
                            if (pixelY >= image.height()) {
                                resizeImage(false);
                            }
                            image.setPixel(pixelX, pixelY, rgb(pxColors[0],pxColors[1],pxColors[2]));
                            toNextPixel();
                        }
                    }
                    return;
                }else if (frameParameters.depth == 16) {
                    for (int i=0; i<readBytes; i++) {
                        pixelIndex++;
                        if (pixelIndex%2==0) {
                            pxColors[pxColorsIndex] = frameData[i];
                            incColorIndex();
                            if (pxColorsIndex == 0) {
                                if (pixelY >= image.height()) {
                                    resizeImage(false);
                                }
                                image.setPixel(pixelX, pixelY, rgb(pxColors[0],pxColors[1],pxColors[2]));
                                toNextPixel();
                            }
                        }
                    }
                    return;
                }
                break;
            }
            case Sane.FRAME_RED:
            case Sane.FRAME_GREEN:
            case Sane.FRAME_BLUE:{
                readFrameData(readBytes, frameParameters.format);
                return;
            }
        }
        throwUnsupportedFrameFormat(frameParameters.format, frameParameters.depth);        
    }
    
    private void readFrameData(final int readBytes, final int frameType){
        final int colorDepth = frameParameters.depth;
        final int depthMultiplier;
        switch (colorDepth) {
            case 8:
                depthMultiplier=4;
                break;
            case 16:
                depthMultiplier=2;
                break;
            default:
                throwUnsupportedFrameFormat(frameType, colorDepth);
                return;
        }
        final int colorComponent;
        switch(frameType){
            case Sane.FRAME_RED:
                colorComponent = 2;
                break;
            case Sane.FRAME_GREEN:
                colorComponent = 1;
                break;
            case Sane.FRAME_BLUE:
                colorComponent = 0;
                break;
            default:
                throwUnsupportedFrameFormat(frameType, colorDepth);
                return;
        }        
        QNativePointer imageData = image.bits();
        int byteIndex;
        for (int i=0; i<readBytes; i++) {
            if (colorDepth == 8 || pixelIndex%2 == 0){
                byteIndex = pixelIndex*depthMultiplier+colorComponent;
                if (byteIndex > image.numBytes()) {
                    imageData = resizeImage(true);
                }
                imageData.setByteAt(byteIndex, frameData[i]);
            }
            pixelIndex++;
        }
    }
    
    private QNativePointer resizeImage(final boolean getDataPointer){
        image = image.copy(0, 0, image.width(), image.height() + image.width());        
        imageWasResized = true;
        return getDataPointer ? image.bits() : null;
        
    }
    
    private void incColorIndex(){
        pxColorsIndex++;
        if (pxColorsIndex==3)
            pxColorsIndex = 0;
    }
    
    private void toNextPixel(){
        pixelX++;
        if (pixelX>=frameParameters.pixels_per_line){
            pixelX = 0;
            pixelY++;
        }
    }
    
    private static int byteToInt(final byte b){
        return b&0xFF;
    }
    
    private static int rgb(int red, int green, int blue){
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;        
        return rgb;
    } 

    private void throwUnsupportedFrameFormat(final int frameType, final int colorDepth){
        final String frameTypeStr;
        switch(frameType){
            case Sane.FRAME_GREEN:
                frameTypeStr = "green";
                break;
            case Sane.FRAME_BLUE:
                frameTypeStr = "blue";
                break;
            case Sane.FRAME_GRAY:
                frameTypeStr = "gray";
                break;  
            case Sane.FRAME_RED:
                frameTypeStr = "red";
                break;
            case Sane.FRAME_RGB:
                frameTypeStr = "RGB";
                break;                
            default:
                frameTypeStr = "unknown type";
                break;                                
        }
        final String message = "Frame type %1$s (%2$s) and color depth %3$s is not currently supported";
        exception = new IadException(String.format(message, frameType, frameTypeStr, colorDepth));
        status = ReadStatus.READ_ERROR;
    }

    @Override
    public boolean cancel() {
        status = ReadStatus.READ_CANCEL;
        return true;
    }

    @Override
    public int getProgressMaxValue() {
        // handscanners have negative data size
        return bytesToRead <= 0 ? 0 : 100;
        
    }

    @Override
    public int getProgressCurValue() {
        if (bytesToRead<=0){
            return -1;
        }else{
            synchronized(semaphore){
                final int bytesRead;

                if (pixelsInFrame < bytesToRead) {
                    bytesRead = pixelIndex + (pixelsInFrame * readFrames);
                }
                else {
                    bytesRead = pixelIndex;
                }

                return (int)(((float)bytesRead * 100.0)/bytesToRead);
            }
        }
    }

    @Override
    public String getTitle() {
        if (status==ReadStatus.PREPARE_FOR_READ){
            return messageProvider.translate("IAD", "Waiting for the scan to start.");
        }else{
            return messageProvider.translate("IAD", "Scanning in progress...");
        }
    }    
        
    public ReadStatus getStatus(){
        return status;
    }
    
    public boolean imageWasResized(){
        return imageWasResized;
    }
    
    public QImage getImage() throws Exception{//it must be called from synchronized(semaphore) block
        if (status==ReadStatus.PREPARE_FOR_READ || status==ReadStatus.READ_CANCEL){
            return null;
        }else if (status==ReadStatus.READ_ERROR){
            throw exception;
        }else{
            imageWasResized = false;
            return image;
        }
    }
    
    public void freeImage(){
        if (status== ReadStatus.READ_READY){
            image = null;
        }
    }
    
}
