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

package org.radixware.wps.rwt.uploading;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;


public final class UploadHandler {
    
    private class UploadingListener implements WebServer.IUploadListener{
        @Override
        public void uploadingComplete() {
            doRead();
        }
    }
    
    private final IClientEnvironment environment;
    private final WebServer.UploadSystemHandler systemHandler;
    private final String contextObjectId;
    private final String id = UUID.randomUUID().toString();
    private final String fileName;
    private long fileSize;
    private final Object semaphore = new Object();
    private Exception uploadingException;
    private IOException ioException;
    private UploadingListener listener;
    private IUploadedDataReader reader;
    private boolean released;
    
    public UploadHandler(final IClientEnvironment environment,
                         final WebServer.UploadSystemHandler systemHandler,
                         final FileDescriptor descriptor,
                         final IUploadedDataReader reader){
        this.environment = environment;
        this.systemHandler = systemHandler;
        contextObjectId = descriptor.getContextObjectId();
        fileName = descriptor.getFileName();
        fileSize = descriptor.getFileSize()==null ? -1 : descriptor.getFileSize().longValue();
        if (reader!=null && setReader(reader)){
            systemHandler.setListener(listener);
        }
    }
    
    public String getId(){
        return id;
    }

    public boolean waitForReadyAndRead(final IUploadedDataReader reader, final boolean cancellable) throws InterruptedException, IOException {
        boolean setListener = false;
        synchronized(semaphore){            
            if (released){
                return true;
            }
            setListener = reader!=null && setReader(reader);
        }
        if (setListener){
            systemHandler.setListener(listener);
        }
        if (!systemHandler.isReady()){
            final IProgressHandle progressHandle;
            if (cancellable){
                final IProgressHandle.Cancellable cancelHandler = new IProgressHandle.Cancellable() {
                    @Override
                    public void onCancel() {
                        release();
                    }
                };
                progressHandle = environment.getProgressHandleManager().newProgressHandle(cancelHandler);
            }else{
                progressHandle = environment.getProgressHandleManager().newProgressHandle();
            }
            progressHandle.startProgress("Loading \'"+fileName+"\'", cancellable);
            final long hardMaxTimeLimit = ((WpsEnvironment)environment).getSessionTimeOut()/1000 - 5;
            final long hardMinTimeLimit = 10;
            final long timeLimit;
            if (fileSize>0){
                timeLimit = Math.max(fileSize/5000, hardMinTimeLimit);
            }else{
                timeLimit = hardMaxTimeLimit;
            }
            final boolean result;
            try{
                result = systemHandler.waitForReady(Math.min(timeLimit, hardMaxTimeLimit));
            }finally{
                progressHandle.finishProgress();
            }
            if (progressHandle.wasCanceled()){
                throw new InterruptedException();
            }
            synchronized(semaphore){
                if (ioException!=null){
                    final IOException exception = ioException;
                    ioException = null;
                    throw exception;
                }else if (uploadingException!=null){
                    final IOException exception = new IOException("Failed to upload", uploadingException);
                    uploadingException = null;
                    throw exception;
                }
            }
            return result;
        }
        return true;
    }
    
    public void readWhenReady(final IUploadedDataReader reader){
        if (reader!=null && setReader(reader)){
            systemHandler.setListener(listener);
        }
    }
    
    private boolean setReader(final IUploadedDataReader reader){
        synchronized(semaphore){
            if (released){
                throw new IllegalStateException("This handler was released");
            }
            if (this.reader!=reader){
                this.reader = reader;
                if (listener==null){
                    listener = new UploadingListener();
                    return true;
                }
            }
        }
        return false;
    }
    
    public long getFileSize(){
        synchronized(semaphore){
            return fileSize;
        }
    }
    
    public void release(){
        synchronized(semaphore){
            listener = null;
            reader = null;
            released = true;
        }
        systemHandler.dispose();
        ((WpsEnvironment)environment).getMainWindow().onUploadHandlerReleased(contextObjectId);
    }    
    
    private void doRead(){
        if (systemHandler.isDisposed()){
            synchronized(semaphore){
                listener = null;
                reader = null;
                return;
            }
        }        
        synchronized(semaphore){
            if (reader!=null){
                try{
                    final File file;
                    try{
                        file = systemHandler.getFile();                        
                    }catch(Exception ex){
                        uploadingException = ex;
                        return;
                    }
                    if (file==null){
                        return;
                    }
                    fileSize = file.length();
                    final InputStream stream = new FileInputStream(file);
                    try{
                        reader.readData(stream, fileName, fileSize);
                    }finally{
                        try{
                            stream.close();
                        }catch(IOException exception){//NOPMD
                        }
                    }
                }catch(IOException exception){
                    ioException = exception;
                }finally{
                    release();
                }
            }
        }
    }    
}
