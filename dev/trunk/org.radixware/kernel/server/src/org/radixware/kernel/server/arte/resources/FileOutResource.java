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
package org.radixware.kernel.server.arte.resources;

import java.io.IOException;

import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EFileSeekOriginType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;

public class FileOutResource extends AbstractFileOutResource {        

    public FileOutResource(final Arte arte) {
        super(arte, Resource.DEFAULT_BLOCK_SIZE);
    }

    public FileOutResource(final Arte arte, final int blockSize) {
        super(arte,blockSize);
    }

    public FileOutResource(final Arte arte, final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode openShareMode)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        super(arte, Resource.DEFAULT_BLOCK_SIZE);
        open(fileName, openMode, openShareMode);
    }

    public FileOutResource(final Arte arte, final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode openShareMode, final int blockSize)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        super(arte, blockSize);
        open(fileName, openMode, openShareMode);
    }    

    public final void open(final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode shareMode)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        setId(FileResource.open(getArte(), fileName, openMode, shareMode, timeout));
    }

    public final long seek(final EFileSeekOriginType origin, final int offset) throws ResourceUsageException, ResourceUsageTimeout, IOException, InterruptedException {
        long result;
        if (CLOSED_ID.equals(getId())) {
            throw new ResourceUsageException("File is not opened", null);
        } else {
            flush();
            result = FileResource.seek(getArte(), getId(), origin, offset, timeout);
        }
        return result;
    }
}
