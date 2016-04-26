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

package org.radixware.kernel.release.fs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


public abstract class ReleaseRepository {

    private Branch branch = null;
    private final Object branchAccessLock = new Object();

    public Branch getBranch() throws IOException {
        synchronized (branchAccessLock) {
            if (branch == null) {
                RadixObject.disableChangeTracking();
                uploadBranch();
            }
            return branch;
        }
    }

    protected abstract RevisionMeta getRevisionMeta();

    private void uploadBranch() throws IOException {
        RfsRepositoryBranch repository = new RfsRepositoryBranch(this);
        branch = Branch.Factory.newInstanceInMemory(repository);
    }

    public InputStream getRepositoryFileStream(final String fileName) throws RadixLoaderException {
        RevisionMeta meta = getRevisionMeta();
        final FileMeta fileMeta = meta.findFile(fileName);
        if (fileMeta == null) {
            throw new RadixLoaderException("File not found: " + fileName);
        }
        final byte[] data;
        try {
            data = RadixLoader.getInstance().readFileData(fileMeta, meta);
        } catch (IOException ex) {
            throw new RadixLoaderException("Error", ex);
        }
        return new ByteArrayInputStream(data);

    }

    public InputStream getRepositoryImageStream(final String imagesDir, Id imageId) throws RadixLoaderException {
        RevisionMeta meta = getRevisionMeta();
        for (ERadixIconType type : ERadixIconType.values()) {
            final String ext = type.getValue();
            final String name = imageId.toString() + "." + ext;

            final FileMeta fileMeta = meta.findFile(imagesDir + name);
            if (fileMeta != null) {

                final byte[] data;
                try {
                    data = RadixLoader.getInstance().readFileData(fileMeta, meta);
                } catch (IOException ex) {
                    throw new RadixLoaderException("Error", ex);
                }
                return new ByteArrayInputStream(data);

            }
        }
        throw new RadixLoaderException("Image not found: " + imagesDir + imageId.toString());
    }

    protected void close() {
        synchronized (branchAccessLock) {
            if (this.branch != null) {
                this.branch.cleanup();
            }
            this.branch = null;
        }
    }

    public abstract void processException(Throwable e);
}
