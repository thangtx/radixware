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

import com.trolltech.qt.gui.QImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.progress.ICancellableTask;
import org.radixware.kernel.explorer.env.progress.IMeasurableTask;
import org.radixware.kernel.explorer.env.progress.ITitledTask;
import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.ETymed;
import org.radixware.kernel.utils.wia.EWiaDataCallbackMessage;
import org.radixware.kernel.utils.wia.EWiaDataCallbackStatus;
import org.radixware.kernel.utils.wia.EWiaImageFormat;
import org.radixware.kernel.utils.wia.FileStorage;
import org.radixware.kernel.utils.wia.IComObjectStream;
import org.radixware.kernel.utils.wia.StgMedium;
import org.radixware.kernel.utils.wia.WiaDataCallback;
import org.radixware.kernel.utils.wia.WiaDataCallbackHeader;
import org.radixware.kernel.utils.wia.WiaDataTransfer;
import org.radixware.kernel.utils.wia.WiaItem;
import org.radixware.kernel.utils.wia.ComLibrary;
import org.radixware.kernel.utils.wia.properties.ComProperty;
import org.radixware.kernel.utils.wia.properties.ComPropertyInt;
import org.radixware.kernel.utils.wia.properties.ComPropertyUUID;
import org.radixware.kernel.utils.wia.properties.EComPropSpecId;
import org.radixware.kernel.utils.wia.properties.WiaPropertyStorage;


final class ScanningTask extends WiaDataCallback implements Runnable, ICancellableTask, IMeasurableTask, ITitledTask{
    
    private static final class ScannedImageReader extends ImageReader{
        
        private final WiaItem imageItem;
        
        public ScannedImageReader(final IClientEnvironment environment, final WiaItem wiaItem, final boolean isBmp){
            super(environment,isBmp);
            this.imageItem = wiaItem;
        }

        @Override
        protected int getImageSize() {
            final ComPropertyInt bytesPerLineProperty = 
                new ComPropertyInt(EComPropSpecId.WIA_IPA_BYTES_PER_LINE, null);
            final ComPropertyInt numOfLinesProperty = 
                new ComPropertyInt(EComPropSpecId.WIA_IPA_NUMBER_OF_LINES, null);
            final MessageProvider mp = getEnvironment().getMessageProvider();
            try(WiaPropertyStorage propertiesStorage = imageItem.openPropertyStorage()){
                if (!propertiesStorage.readMultiple(Arrays.<ComProperty>asList(bytesPerLineProperty, numOfLinesProperty))){
                    final String errorTitle = mp.translate("IAD", "Unable to read image parameters");
                    onException(null, new IOException(errorTitle));
                    return -1;
                }
                final Integer bytesPerLine = bytesPerLineProperty.getValue();            
                final String errorTitleTemplate = mp.translate("IAD", "Unable to read value of \'%1$s\' property");
                if (bytesPerLine==null || bytesPerLine<=0){                
                    final IOException exception = 
                        new IOException(String.format(errorTitleTemplate,EComPropSpecId.WIA_IPA_BYTES_PER_LINE.getDescription()));
                    onException(null, exception);
                    return -1;
                }
                final Integer numOfLines = numOfLinesProperty.getValue();
                if (numOfLines==null || numOfLines<=0){
                    final IOException exception = 
                        new IOException(String.format(errorTitleTemplate,EComPropSpecId.WIA_IPA_NUMBER_OF_LINES.getDescription()));
                    onException(null, exception);                
                    return -1;
                }
                final String messageTemplate = 
                    mp.translate("IAD", "Image properties received:\nBytes per line: %1$s\nNumber of lines: %2$s");
                debugMsg(String.format(messageTemplate, bytesPerLine, numOfLines));
                return bytesPerLine*numOfLines;
            }catch(ComException exception){
                final String errorTitle = mp.translate("IAD", "Failed to read values of image properties");                
                onException(errorTitle, exception);
                return -1;
            }
        }
    }
        
    private static enum ReadStatus{ PREPARE_FOR_READ, READ_CANCEL, READ_ERROR, READING_HEADER, READING_BODY, READ_READY};
    
    private final static long DEFAULT_BUFFER_SIZE = 1024*1024;//1Mb
    private final static String BUFFER_SIZE_SYS_PROP_NAME="org.radixware.kernel.explorer.iad.wia.buffer-size";
    
    private final ETymed mediumType; 
    private final String imageFilePath;
    private final IClientEnvironment environment;
    private final IComObjectStream<WiaItem> imageItemStream;
    private final Object semaphore = new Object();
        
    private ReadStatus status= ReadStatus.PREPARE_FOR_READ;
    private int percentComplete;
        
    private ImageReader imageReader;
    private WiaItem imageItem;
    private String errorTitle;
    private ComException comException;
        
    private long transferredBytes;
    
    public ScanningTask(final IClientEnvironment environment, 
                        final IComObjectStream<WiaItem> imageItemStream,
                        final String imageFilePath){
        this.environment = environment;
        this.imageItemStream = imageItemStream;
        this.imageFilePath = imageFilePath;
        if (imageFilePath==null || imageFilePath.isEmpty()){
            mediumType = ETymed.CALLBACK;
        }else{
            mediumType = ETymed.FILE;
        }        
    }

    @Override
    public void run() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                environment.getTracer().error(e);
            }
        });
        final MessageProvider mp = environment.getMessageProvider();
        final ComLibrary library;
        if (!ComLibrary.wasInitialized()){
            try{
                library = ComLibrary.initialize();
            }catch(ComException exception){
                final String errorTemplate = mp.translate("IAD", "Failed to transfer image");
                onComException(errorTemplate, exception);
                return;                
            }
        }else{
            library = null;
        }
        try{
            try {
                imageItem = imageItemStream.readObjectAndRelease();
            }catch(ComException ex){
                final String errorTemplate = mp.translate("IAD", "Failed to transfer image");
                onComException(errorTemplate, ex);
                return;
            }
            try {
                final List<ComProperty> properties = new LinkedList<>();
                final EWiaImageFormat imageFormat;
                if (mediumType==ETymed.CALLBACK){
                    imageFormat = EWiaImageFormat.MEMORYBMP;
                }else{
                    imageFormat = EWiaImageFormat.BMP;
                }
                properties.add(new ComPropertyUUID(EComPropSpecId.WIA_IPA_FORMAT, imageFormat.getGuid()));
                properties.add(new ComPropertyInt(EComPropSpecId.WIA_IPA_TYMED, mediumType.getValue()));        
                try (WiaPropertyStorage propertyStorage = imageItem.openPropertyStorage()){
                    propertyStorage.writeMultiple(properties);
                }catch(ComException exception){
                    final String errorTemplate = mp.translate("IAD", "Failed to set image transfer parameters");                
                    onComException(errorTemplate, exception);
                    return;
                }
                try (WiaDataTransfer dataTransfer = imageItem.createDataTransfer()){
                    if (mediumType==ETymed.CALLBACK){
                        long bufferSize;
                        final String bufferSizeAsStr = System.getProperty(BUFFER_SIZE_SYS_PROP_NAME);
                        if (bufferSizeAsStr!=null && !bufferSizeAsStr.isEmpty()){
                            try{
                                bufferSize = Long.parseLong(bufferSizeAsStr);
                            }catch(NumberFormatException ex){
                                bufferSize = DEFAULT_BUFFER_SIZE;
                            }
                        }else{
                            bufferSize = DEFAULT_BUFFER_SIZE;
                        }
                        dataTransfer.idtGetBandedData(bufferSize, true, this);                
                        if (!wasCancelled() && !wasError()){
                            final String messageTemplate = 
                                mp.translate("IAD", "Image transferring complete. Total number of received bytes: %1$s");
                            debugMsg(String.format(messageTemplate, this.transferredBytes));
                        }                
                    }else{
                        final FileStorage stgFile = StgMedium.Factory.createFileStorage(imageFilePath);
                        dataTransfer.idtGetData(stgFile, this);
                        if (!wasCancelled() && !wasError()){
                            final String messageTemplate = 
                                mp.translate("IAD", "Image transferring complete. The image was stored in file: %1$s");
                            debugMsg(String.format(messageTemplate, imageFilePath));
                        }
                    }
                    if (wasCancelled()){
                        mp.translate("IAD", "Image transferring was cancelled");
                    }else{
                        setStatus(ReadStatus.READ_READY);
                    }
                }catch(ComException ex){
                    final String errorTemplate = mp.translate("IAD", "Failed to transfer image");
                    onComException(errorTemplate, ex);
                }
            }finally{
                if (imageItem!=null){
                    try{
                        imageItem.close();
                    }catch(ComException exception){
                        environment.getTracer().error(exception);
                    }
                }
            }
        }finally{
            if (library!=null){
                library.close();
            }
        }
    }
    
    private void checkException()  throws Exception{
        if (comException!=null){
            throw comException;
        }
        final Exception exception = imageReader==null ? null : imageReader.getException();
        if (exception!=null){
            throw exception;
        }
    }
        
    public QImage getImage() throws Exception {
        synchronized(semaphore){
            checkException();
            return status == ReadStatus.READ_READY ? imageReader.getImage() : null;
        }
    }
    
    public String getExceptionTitle(){
        final String imageReaderError = imageReader==null ? null : imageReader.getErrorTitle();
        return imageReaderError==null || imageReaderError.isEmpty() ? errorTitle : imageReaderError;
    }
    
    public String getImageFilePath() throws Exception {
        synchronized(semaphore){
            checkException();
            if (status != ReadStatus.READ_READY){
                return null;
            }
            return imageFilePath;
        }
    }
    
    private void setStatus(ReadStatus newStatus){
        synchronized(semaphore){
            if (status!=ReadStatus.READ_CANCEL && status!=ReadStatus.READ_ERROR){
                status = newStatus;
            }
        }
    }
    
    private boolean wasCancelled(){
        synchronized(semaphore){
            return status == ReadStatus.READ_CANCEL;
        }
    }
    
    private boolean wasError(){
        synchronized(semaphore){
            return status == ReadStatus.READ_ERROR;
        }
    }
    
    private void onComException(final String title, final ComException exception){
        synchronized(semaphore){
            comException = exception;
            errorTitle = title;
            status = ReadStatus.READ_ERROR;
        }
    }
    

    @Override
    public boolean cancel() {
        setStatus(ReadStatus.READ_CANCEL);
        debugMsg(environment.getMessageProvider().translate("IAD", "Scanning was cancelled"));
        return true;
    }

    @Override
    public int getProgressMaxValue() {
        synchronized(semaphore){
            if (status==ReadStatus.READING_BODY || status==ReadStatus.READING_HEADER){
                return 100;
            }else{
                return 0;
            }
        }
    }

    @Override
    public int getProgressCurValue() {
        synchronized(semaphore){
            if (status==ReadStatus.READING_BODY || status==ReadStatus.READING_HEADER){
                return percentComplete;
            }else{
                return -1;
            }
        }
    }

    @Override
    public String getTitle() {
        synchronized(semaphore){
            final MessageProvider messageProvider = environment.getMessageProvider();
            switch(status){
                case PREPARE_FOR_READ:
                    return messageProvider.translate("IAD", "Waiting for the scan to start.");
                case READ_CANCEL:
                case READ_ERROR:
                    return messageProvider.translate("IAD", "Cancelling scan");
                default:
                    return messageProvider.translate("IAD", "Scanning in progress...");
            }
        }
    }        

    @Override
    protected boolean headerCallback(final WiaDataCallbackHeader header) throws ComException {
        setStatus(ReadStatus.READING_HEADER);
        {
            final String messageTemplate = 
                environment.getMessageProvider().translate("IAD", "Data header received:\n%1$s");
            debugMsg(String.format(messageTemplate, header.toString()));
        }
        if (mediumType==ETymed.CALLBACK){
            final boolean isBmp = EWiaImageFormat.MEMORYBMP.getGuid().equals(header.getImageFormatGUID());
            synchronized(semaphore){
                imageReader = new ScannedImageReader(environment, imageItem, isBmp);
            }
            if (!isBmp){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("IAD", "Image format is not as expected");
                debugMsg(String.format(messageTemplate, header.toString()));            
            }
        }
        return !wasCancelled() && !wasError();
    }

    @Override
    protected boolean bandedDataCallback(EWiaDataCallbackMessage messageType, 
                                         EWiaDataCallbackStatus status, 
                                         long percentComplete, 
                                         long offset, 
                                         long length, 
                                         ByteBuffer dataFrame) throws ComException {
        setStatus(ReadStatus.READING_BODY);
        {
            final StringBuilder logMessage = new StringBuilder();            
            logMessage.append("Message ");
            logMessage.append(messageType.name());
            logMessage.append(" received:\n");
            if (status==null){
                logMessage.append("\tno status information\n");
            }else{
                logMessage.append("\tcurrent status is ");
                logMessage.append(status.name());
                logMessage.append('\n');
            }
            logMessage.append("\tPercent Complete: ");
            logMessage.append(String.valueOf(percentComplete));
            logMessage.append('\n');
            logMessage.append("\tFragment offset: ");
            logMessage.append(String.valueOf(offset));
            logMessage.append('\n');
            logMessage.append("\tFragment length: ");
            logMessage.append(String.valueOf(length));
            
            final String messageTemplate = 
                environment.getMessageProvider().translate("IAD", "Data received from device: {\n%1$s\n}");
            debugMsg(String.format(messageTemplate, logMessage.toString()));
        }
        if (messageType==EWiaDataCallbackMessage.STATUS){
            this.percentComplete = (int)percentComplete;
        }
        else if (messageType==EWiaDataCallbackMessage.DATA && mediumType==ETymed.CALLBACK && dataFrame!=null && length>0){
            this.percentComplete = (int)percentComplete;
            transferredBytes+=length;
            synchronized(semaphore){
                if (!imageReader.readFragment(offset, (int)length, dataFrame)){
                    this.status = ReadStatus.READ_ERROR;
                    return false;
                }
            }            
        }
        return !wasCancelled() && !wasError();
    }
    
    public void deleteTemporaryImageFile(){
        synchronized(semaphore){
            if (imageFilePath!=null){
                FileUtils.deleteFile(new File(imageFilePath));
            }
        }
    }
    
    private void debugMsg(final String message){
        environment.getTracer().put(EEventSeverity.DEBUG, message, EEventSource.IAD);
    }
}
