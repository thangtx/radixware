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

package org.radixware.kernel.designer.environment.saveall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * Radix virtual FileObject.
 */
class RadixFileObject extends FileObject {

    private final RadixObject radixObject;

    protected RadixFileObject(final RadixObject radixObject) {
        this.radixObject = radixObject;
    }

    public RadixObject getRadixObject() {
        return radixObject;
    }

    @Override
    public String getName() {
        return radixObject.getQualifiedName();
    }

    @Override
    public String getExt() {
        return "";
    }

    @Override
    public boolean isValid() {
        return true;
    }
    private Map<String, Object> attributes = null;

    @Override
    public synchronized Object getAttribute(String attrName) {
        if (attributes != null) {
            return attributes.get(attrName);
        } else {
            return null;
        }
    }

    @Override
    public synchronized void setAttribute(String attrName, Object value) throws IOException {
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        attributes.put(attrName, value);
    }

    @Override
    public synchronized Enumeration<String> getAttributes() {
        if (attributes != null) {
            return Collections.enumeration(attributes.keySet());
        } else {
            final Set<String> emptySet = Collections.emptySet();
            return Collections.enumeration(emptySet);
        }
    }

    @Override
    public void addFileChangeListener(FileChangeListener fcl) {
    }

    @Override
    public void removeFileChangeListener(FileChangeListener fcl) {
    }
    private static final FileObject[] EMPTY_CHILDREN = new FileObject[]{};

    @Override
    public FileObject[] getChildren() {
        return EMPTY_CHILDREN;
    }

    @Override
    public FileObject getFileObject(String name, String ext) {
        return null;
    }

    @Override
    public FileSystem getFileSystem() throws FileStateInvalidException {
        return RadixFileSystem.getDefault();
    }

    @Override
    public FileObject getParent() {
        //To enable Save dialog on project closing.
        final File file = radixObject.getFile();
        if (file != null) {
            return RadixFileUtil.toFileObject(file);
        } else {
            return null;
        }
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public boolean isData() {
        return false;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isReadOnly() {
        return getRadixObject().isReadOnly();
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        throw new RadixObjectError("Attempt to get input stream of RadixFileObject.", getRadixObject());
    }

    @Override
    public OutputStream getOutputStream(FileLock lock) throws IOException {
        throw new RadixObjectError("Attempt to get output stream of RadixFileObject.", getRadixObject());
    }

    @Override
    @Deprecated
    public void setImportant(boolean b) {
        throw new RadixObjectError("Attempt to set important for RadixFileObject.", getRadixObject());
    }

    @Override
    public FileObject createData(String name, String ext) throws IOException {
        throw new RadixObjectError("Attempt to create data in RadixFileObject.", getRadixObject());
    }

    @Override
    public FileObject createFolder(String name) throws IOException {
        throw new RadixObjectError("Attempt to create folder in RadixFileObject.", getRadixObject());
    }

    @Override
    public void delete(FileLock lock) throws IOException {
        throw new RadixObjectError("Attempt to delete RadixFileObject.", getRadixObject());
    }

    @Override
    public Date lastModified() {
        throw new RadixObjectError("Attempt to get last modified date of RadixFileObject.", getRadixObject());
    }

    @Override
    public FileLock lock() throws IOException {
        throw new RadixObjectError("Attempt to lock RadixFileObject.", getRadixObject());
    }

    @Override
    public void rename(FileLock lock, String name, String ext) throws IOException {
        throw new RadixObjectError("Attempt to rename RadixFileObject.", getRadixObject());
    }
}
