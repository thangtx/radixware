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
package org.radixware.kernel.designer.eas.client;

import java.io.IOException;
import org.radixware.kernel.common.client.eas.resources.IFileDirResource;
import org.radixware.kernel.common.client.eas.resources.IFileResource;
import org.radixware.kernel.common.client.eas.resources.IFileTransitResource;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressMonitor;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.schemas.eas.FileSelectRq;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.NextDialogRequest;
import org.radixware.schemas.eas.ProgressDialogSetRq;
import org.radixware.schemas.eas.ProgressDialogStartProcessRq;
import org.radixware.schemas.eas.Trace;

/**
 *
 * @author npopov
 */
public class ResourceManagerStub implements IResourceManager {

    @Override
    public IMessageDialogResource openMessageDialogResource(MessageDialogOpenRq mdor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IMessageDialogResource openMessageDialogResource(NextDialogRequest.MessageBox mb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IFileResource openFileResource(String string, EFileOpenMode efom, EFileOpenShareMode efosm) throws IOException, TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IFileTransitResource startFileTransit(String string, EMimeType emt, boolean bln) throws IOException, TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IProgressDialogResource getProgressDialogResource() {
        return new IProgressDialogResource() {

            @Override
            public long startProcess(ProgressDialogStartProcessRq pdspr) {
                return 0;
            }

            @Override
            public boolean finishProcess() {
                return true;
            }

            @Override
            public void forceShowIfActive() {
            }

            @Override
            public void clear() {
            }

            @Override
            public boolean addTrace(Trace trace) {
                return true;
            }

            @Override
            public IProgressMonitor findProcess(long l) {
                return null;
            }

            @Override
            public boolean updateProcess(IProgressMonitor ipm, ProgressDialogSetRq pdsr) {
                return true;
            }
        };
    }

    @Override
    public IFileDirResource getFileDirResource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String selectFile(FileSelectRq fsr) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkFileAccess(String string, EFileAccessType efat) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getFileSize(String string) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteFile(String string) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveFile(String string, String string1, boolean bln) throws TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyFile(String string, String string1, boolean bln) throws IOException, TerminalResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFileExists(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}