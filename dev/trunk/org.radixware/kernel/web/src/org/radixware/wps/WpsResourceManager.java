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

package org.radixware.wps;

import java.io.IOException;
import org.radixware.kernel.common.client.eas.resources.IFileDirResource;
import org.radixware.kernel.common.client.eas.resources.IFileResource;
import org.radixware.kernel.common.client.eas.resources.IFileTransitResource;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressDialogResource;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.schemas.eas.FileSelectRq;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.NextDialogRequest.MessageBox;
import org.radixware.wps.resources.FileResource;
import org.radixware.wps.resources.FileTransitResource;
import org.radixware.wps.resources.MessageDialogResource;
import org.radixware.wps.resources.ProgressDialogResource;
import org.radixware.wps.resources.WpsFileDirResource;


class WpsResourceManager implements IResourceManager {

    private final WpsEnvironment env;
    private ProgressDialogResource resource = null;

    public WpsResourceManager(WpsEnvironment env) {
        this.env = env;
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageDialogOpenRq request) {
        synchronized (this) {
            return MessageDialogResource.open(env, request);
        }
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageBox messageBox) {
        synchronized (this) {
            return MessageDialogResource.open(env, messageBox);
        }
    }

    @Override
    public IFileTransitResource startFileTransit(String fileName, EMimeType mimeType, boolean openAfterTransit) throws IOException, TerminalResourceException {
        synchronized (this){
            return new FileTransitResource(fileName, mimeType, openAfterTransit);
        }
    }
        
    @Override
    public IFileResource openFileResource(String fileName, EFileOpenMode openMode, EFileOpenShareMode share) throws IOException, TerminalResourceException {
        synchronized (this) {
            return new FileResource(fileName, openMode, share);
        }
    }  

    @Override
    public IProgressDialogResource getProgressDialogResource() {
        synchronized (this) {
            if (resource == null) {
                resource = new ProgressDialogResource(env);
            }
            return resource;
        }

    }

    @Override
    public IFileDirResource getFileDirResource() {
        return WpsFileDirResource.getInstance();
    }

    @Override
    public String selectFile(FileSelectRq request) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean checkFileAccess(String filePath, EFileAccessType accessType) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getFileSize(String filePath) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteFile(String fileName) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void moveFile(String srcFileName, String dstFileName, boolean overwrite) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyFile(String srcFileName, String dstFileName, boolean overwrite) throws IOException, TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFileExists(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }        
}
