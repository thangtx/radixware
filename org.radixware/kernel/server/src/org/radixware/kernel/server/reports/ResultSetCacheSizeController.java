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
package org.radixware.kernel.server.reports;

public class ResultSetCacheSizeController {

    private final long maxResultSetCacheSize;
    
    private long freeSpace;

    public ResultSetCacheSizeController(long maxResultSetCacheSize) {
        this.maxResultSetCacheSize = maxResultSetCacheSize;
        freeSpace = maxResultSetCacheSize;
    }

    public boolean decreaseAvailableSpace(long size) {
        if (freeSpace - size >= 0) {
            freeSpace -= size;
            return true;
        }
        return false;
    }
    
    public void increaseFreeSpace(long size) {
        freeSpace = freeSpace < 0 ? size : freeSpace + size;
    }
    
    public long getFreeSpace() {
        return freeSpace;
    }
    
    public long getUsedSpace() {
        return maxResultSetCacheSize - freeSpace;
    }

    public long getMaxResultSetCacheSize() {
        return maxResultSetCacheSize;
    }        
    
    public void freeSpace() {
        freeSpace = maxResultSetCacheSize;
    }
}
