/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.arte.resources;

import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.server.arte.Arte;


public final class FileTransitResource extends AbstractFileOutResource{    
    
    public FileTransitResource(final Arte arte, final String fileName, final EMimeType mimeType, final boolean openAfterTransit, final int blockSize) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        super(arte, blockSize);
        if (fileName==null || fileName.isEmpty()){
            throw new IllegalArgumentException("file name cannot not be null or empty string");
        }
        start(fileName, mimeType, openAfterTransit);
    }
    
    public FileTransitResource(final Arte arte, final String fileName, final EMimeType mimeType, final boolean openAfterTransit) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        this(arte, fileName, mimeType, openAfterTransit,Resource.DEFAULT_BLOCK_SIZE);
    }
    
    public FileTransitResource(final Arte arte, final String fileName, final EMimeType mimeType) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        this(arte, fileName, mimeType, false, Resource.DEFAULT_BLOCK_SIZE);
    }
    
    private void start(final String fileName, final EMimeType mimeType, final boolean openAfterTransit) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        setId(FileResource.startTransit(getArte(), fileName, mimeType, openAfterTransit, timeout));
    }

}
