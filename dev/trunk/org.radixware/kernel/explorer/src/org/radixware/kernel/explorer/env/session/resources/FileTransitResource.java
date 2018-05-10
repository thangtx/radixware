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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QDesktopServices;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IFileTransitResource;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.radixloader.RadixLoader;


final class FileTransitResource extends FileResource implements IFileTransitResource{
    
    private final EMimeType mimeType;
    private final boolean openAfterTransfer;
    private boolean wasTransited;
    private boolean wasClosed;

    public FileTransitResource(final String fileName, final EMimeType mimeType, final boolean openAfterTransfer) throws IOException, TerminalResourceException{
        super(getTempFileName(fileName), EFileOpenMode.CREATE, EFileOpenShareMode.WRITE);
        this.mimeType = mimeType;
        this.openAfterTransfer = openAfterTransfer;
    }
    
    private static String getTempFileName(final String prefix){
        final File file = RadixLoader.getInstance().createTempFile("explorer_transfer_file_"+prefix);
        return file.getAbsolutePath();
    }
    
    private String selectFileToTransitFor(final IClientEnvironment environment){
        final String title = environment.getMessageProvider().translate("ExplorerMessage", "Select File to Save to");
        final QFileDialog dialog;
        if (mimeType==null){
            dialog = new QFileDialog(null, title, QDir.homePath());
        }else{
            final String mask = mimeType.getTitle()+" "+mimeType.getQtFilter();
            dialog = new QFileDialog(null, title, QDir.homePath(),mask);
            dialog.setDefaultSuffix(mimeType.getExt());
        }
        dialog.setFileMode(QFileDialog.FileMode.AnyFile);
        dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptSave);
        environment.getProgressHandleManager().blockProgress();
        try {
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                return dialog.selectedFiles().get(0);
            } else {
                return null;
            }
        } finally {
            environment.getProgressHandleManager().unblockProgress();
        }        
    }    
    
    private void openFile(final String filePath){
        QDesktopServices.openUrl(QUrl.fromLocalFile(filePath));        
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
            wasTransited = true;
            final File sourceFile = new File(getFilePath());
            if (sourceFile.isFile()){
                final String targetFilePath = selectFileToTransitFor(environment);
                if (targetFilePath!=null){
                    try{
                        FileUtils.moveFile(sourceFile, new File(targetFilePath));
                        if (openAfterTransfer){
                            openFile(targetFilePath);
                        }
                    }catch(IOException exception){
                        final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to move file \'%1$s\' to \'%2$s\'");
                        environment.getTracer().error(String.format(message, filePath, targetFilePath) , exception);
                    }
                }                                    
            }            
            if (sourceFile.exists()){
                FileUtils.deleteFile(sourceFile);
            }
        }
    }
}
