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

package org.radixware.kernel.designer.environment.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.utils.FileOperations;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class FileOperationsImpl extends FileOperations {
    
    @Override
    public void rename(final File src, final String newBaseName, String newExt) throws IOException {
        final String newName = (newExt != null && !newExt.isEmpty() ? newBaseName + "." + newExt : newBaseName);
        
        final String error = "Unable to rename file '" + src.getName() + "' to '" + newName + "'";
        
        final FileObject fileObject = RadixFileUtil.toFileObject(src);
        if (fileObject == null) {
            throw new FileNotFoundException(error + ": source file not found.");
        }
        
        final File dest = new File(src.getParent(), newName);
        if (dest.exists()) {
            throw new FileNotFoundException(error + ": destination already exist.");
        }
        
        final FileLock lock = fileObject.lock();
        try {
            fileObject.rename(lock, newBaseName, newExt);
        } finally {
            lock.releaseLock();
        }
    }
    
    @Override
    public void deleteFile(final File file) throws IOException {
        final FileObject fileObject = RadixFileUtil.toFileObject(file);
        if (fileObject == null) {
            return; // already deleted
        }
        
        fileObject.delete();
    }
    
    @Override
    public OutputStream getOutputStream(final File file) throws IOException {
        FileObject fileObject = RadixFileUtil.toFileObject(file);
        if (fileObject == null) {
            fileObject = FileUtil.createData(file);
        }
        try {
            return fileObject.getOutputStream();
        } catch (FileAlreadyLockedException ex) {
            Logger.getLogger(FileOperationsImpl.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            return new FileOutputStream(file);
        }
    }
    
    @Override
    public void mkDirs(File dir) throws IOException {
        //akrylov: dir.getCanonicalFile() is used because of
        //fault on file operation for file with non-canonical
        //path like /home/akrylov/testradix/../radix/trunk/org.radixware
        //This kind of path may appear in inspector branch mode
        //when layer reference is relative to inspector branch
        //description
        FileUtil.createFolder(dir.getCanonicalFile());
    }
}
