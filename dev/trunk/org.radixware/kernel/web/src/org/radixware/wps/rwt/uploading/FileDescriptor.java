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

import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.wps.rwt.UIObject;


public class FileDescriptor {
    
    private final UIObject contextObject;
    private final String fileName;        
    private final Long fileSize;    
    
    private FileDescriptor(final UIObject contextObject,
                            final String fileName,
                            final Long fileSize){
        this.contextObject = contextObject;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
        
    public String getFileName(){
        return fileName;                
    }
    
    public Long getFileSize(){
        return fileSize;
    }
    
    static FileDescriptor createInstance(final FileUploader uploader, final String selectFileActionParam) throws WrongFormatError{
        final int spaceIdx = selectFileActionParam.indexOf(' ');
        if (spaceIdx==-1){
            throw new WrongFormatError("Failed to parse select file action parameter: \'"+selectFileActionParam+"\'");
        }
        final String fileSizeAsStr = selectFileActionParam.substring(0, spaceIdx);
        final long fileSize;
        try{
            fileSize = Long.valueOf(fileSizeAsStr);
        }catch(NumberFormatException exception){
            throw new WrongFormatError("Failed to parse select file action parameter: \'"+selectFileActionParam+"\'",exception);
        }
        final String fileName = selectFileActionParam.substring(spaceIdx+1);
        return new FileDescriptor(uploader.getContextObject(),
                                  fileName,
                                  fileSize==-1 ? null : Long.valueOf(fileSize));
    }

    public String getContextObjectId() {
        return contextObject.getHtmlId();
    }
}
