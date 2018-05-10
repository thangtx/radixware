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

package org.radixware.kernel.explorer.env.session.resources;

import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
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


public class ExplorerResourceManager implements IResourceManager {

    private final IClientEnvironment environment;
    private final ProgressDialogResource resource;

    public ExplorerResourceManager(IClientEnvironment environment) {
        this.environment = environment;
        resource = new ProgressDialogResource(environment);
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageDialogOpenRq request) {
        return MessageDialogResource.open(environment, request);
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageBox messageBox) {
        return MessageDialogResource.open(environment, messageBox);
    }

    @Override
    public IFileResource openFileResource(String fileName, EFileOpenMode openMode, EFileOpenShareMode share) throws IOException, TerminalResourceException {
        return new FileResource(fileName, openMode, share);
    }

    @Override
    public IFileTransitResource startFileTransit(String fileName, EMimeType mimeType, boolean openAfterTransit) throws IOException, TerminalResourceException {
        return new FileTransitResource(fileName, mimeType, openAfterTransit);
    }
        
    @Override
    public IFileDirResource getFileDirResource() {
        return FileDirResource.getInstance();
    }

    @Override
    public IProgressDialogResource getProgressDialogResource() {
        return resource;
    }

    @Override
    public String selectFile(final FileSelectRq request) throws TerminalResourceException {
        return FileResource.select(environment, request);
    }

    @Override
    public boolean checkFileAccess(String filePath, EFileAccessType accessType) throws TerminalResourceException {
        return FileResource.checkAccess(filePath, accessType);
    }

    @Override
    public long getFileSize(String filePath) throws TerminalResourceException {
        return FileResource.getSize(filePath);
    }

    @Override
    public void deleteFile(String fileName) throws TerminalResourceException {
        FileResource.delete(fileName);
    }

    @Override
    public void moveFile(String srcFileName, String dstFileName, boolean overwrite) throws TerminalResourceException {
        FileResource.move(srcFileName, dstFileName, overwrite);
    }

    @Override
    public void copyFile(String srcFileName, String dstFileName, boolean overwrite) throws IOException, TerminalResourceException {
        FileResource.copy(srcFileName, dstFileName, overwrite);
    }

    @Override
    public boolean isFileExists(final String path) {
        return FileResource.isExists(path);
    }    
}
