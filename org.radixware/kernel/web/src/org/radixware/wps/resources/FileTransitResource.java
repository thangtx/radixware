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

package org.radixware.wps.resources;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IFileTransitResource;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;


public final class FileTransitResource extends FileResource implements IFileTransitResource{

    private final String fileName;
    private final EMimeType mimeType;
    private final boolean openAfterTransfer;    
    private boolean wasTransited;
    private boolean wasClosed;

    public FileTransitResource(final String fileName, final EMimeType mimeType, final boolean openAfterTransfer) throws IOException, TerminalResourceException{
        super(getTempFileName(fileName), EFileOpenMode.CREATE, EFileOpenShareMode.WRITE);
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.openAfterTransfer = openAfterTransfer;        
    }
  
    private static String getTempFileName(final String prefix){
        final File file = RadixLoader.getInstance().createTempFile("wps_transfer_file_"+prefix);
        return file.getAbsolutePath();
    }    
    
    @Override
    public void close() throws TerminalResourceException {
        if (!wasClosed){
            super.close();
            wasClosed = true;
        }
    }    

    @Override
    public void makeTransit(final IClientEnvironment environment) {
        if (!wasTransited){
            final String filePath = getFilePath();
            if (!wasClosed){
                try{
                    close();
                }catch(TerminalResourceException exception){
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to close file \'%1$s\'");
                    environment.getTracer().error(String.format(message, filePath) , exception);
                }
            }
            final File sourceFile = new File(filePath);
            final String mType = mimeType==null ? null : mimeType.getValue();            
            ((WpsEnvironment)environment).sendFileToTerminal(fileName, sourceFile, mType, openAfterTransfer, true);
            wasTransited  = true;
        }
    }
    
    
}
