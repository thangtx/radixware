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
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IFileDirResource;
import org.radixware.kernel.common.client.eas.resources.IFileResource;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressDialogResource;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.eas.resources.ITerminalResource;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.schemas.eas.FileSelectRq;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.NextDialogRequest.MessageBox;


public class ExplorerResourceManager implements IResourceManager {

    private final IClientEnvironment environment;
    private final Map<String, ITerminalResource> resources = new HashMap<String, ITerminalResource>();
    private final ProgressDialogResource resource;

    public ExplorerResourceManager(IClientEnvironment environment) {
        this.environment = environment;
        resource = new ProgressDialogResource(environment);
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageDialogOpenRq request) {
        MessageDialogResource mdr = MessageDialogResource.open(environment, request);
        if (mdr != null) {
            resources.put(mdr.getId(), mdr);
        }
        return mdr;
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageBox messageBox) {
        MessageDialogResource mdr = MessageDialogResource.open(environment, messageBox);
        if (mdr != null) {
            resources.put(mdr.getId(), mdr);
        }
        return mdr;
    }

    @Override
    public IFileResource openFileResource(String fileName, EFileOpenMode openMode, EFileOpenShareMode share) throws IOException, TerminalResourceException {
        FileResource fr = new FileResource(fileName, openMode, share);
        if (fr != null) {
            resources.put(fr.getId(), fr);
        }

        return fr;
    }

    @Override
    public IFileDirResource getFileDirResource() {
        return FileDirResource.getInstance();
    }

    @Override
    public void freeResource(String id) throws TerminalResourceException {
        ITerminalResource r = resources.get(id);
        if (r != null) {
            r.free();
        }
    }

    @Override
    public void freeAllResources() throws TerminalResourceException {
        for (ITerminalResource r : resources.values()) {
            r.free();
        }
    }

    @Override
    public IProgressDialogResource getProgressDialogResource() {
        return resource;
    }

    @Override
    public String selectFile(FileSelectRq request) throws TerminalResourceException {
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
}
