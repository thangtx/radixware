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

package org.radixware.kernel.common.client.eas.resources;

import java.io.IOException;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.NextDialogRequest;


public interface IResourceManager {

    public IMessageDialogResource openMessageDialogResource(MessageDialogOpenRq request);

    public IMessageDialogResource openMessageDialogResource(NextDialogRequest.MessageBox messageBox);

    public IFileResource openFileResource(final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode share) throws IOException, TerminalResourceException;
    
    public IFileTransitResource startFileTransit(final String fileName, final EMimeType mimeType, final boolean openAfterTransit) throws IOException, TerminalResourceException;

    public IProgressDialogResource getProgressDialogResource();
    
    public IFileDirResource getFileDirResource();

    public String selectFile(org.radixware.schemas.eas.FileSelectRq request) throws TerminalResourceException;

    public boolean checkFileAccess(final String filePath, EFileAccessType accessType) throws TerminalResourceException;

    public long getFileSize(final String filePath) throws TerminalResourceException;

    public void deleteFile(final String fileName) throws TerminalResourceException;

    public void moveFile(final String srcFileName, final String dstFileName, final boolean overwrite) throws TerminalResourceException;

    public void copyFile(final String srcFileName, final String dstFileName, final boolean overwrite) throws IOException, TerminalResourceException;
    
    public boolean isFileExists(final String path);
}
