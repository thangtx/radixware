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

package org.radixware.kernel.designer.common.dialogs.utils;


import java.io.ByteArrayInputStream;
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
import org.radixware.kernel.common.exceptions.RadixError;

/**
 * Radix virtual FileObject.
 *
 */
class DummyFileObject extends FileObject {

    private final byte[] content;
    private final String name;
    private final String ext;

    public DummyFileObject(final String name, final String ext, final byte[] content) {
        this.content = content;
        this.name = name;
        this.ext = ext;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExt() {
        return ext;
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
        return DummyFileSystem.getDefault();
    }

    @Override
    public FileObject getParent() {
        return null;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public boolean isData() {
        return true;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isReadOnly() {
        return true;
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
        return new ByteArrayInputStream(content);
    }

    @Override
    public OutputStream getOutputStream(FileLock lock) throws IOException {
        throw new RadixError("Attempt to get output stream of RadixFileObject.");
    }

    @Override
    @Deprecated
    public void setImportant(boolean b) {
        throw new RadixError("Attempt to set important for RadixFileObject.");
    }

    @Override
    public FileObject createData(String name, String ext) throws IOException {
        throw new RadixError("Attempt to create data in RadixFileObject.");
    }

    @Override
    public FileObject createFolder(String name) throws IOException {
        throw new RadixError("Attempt to create folder in RadixFileObject.");
    }

    @Override
    public void delete(FileLock lock) throws IOException {
        throw new RadixError("Attempt to delete RadixFileObject.");
    }

    @Override
    public Date lastModified() {
        return new Date(System.currentTimeMillis());        
    }

    @Override
    public FileLock lock() throws IOException {
        return null;
    }

    @Override
    public void rename(FileLock lock, String name, String ext) throws IOException {
        throw new RadixError("Attempt to rename RadixFileObject.");
    }
}
