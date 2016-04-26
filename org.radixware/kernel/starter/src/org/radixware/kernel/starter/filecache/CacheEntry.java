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

package org.radixware.kernel.starter.filecache;

import java.io.IOException;


public abstract class CacheEntry implements Comparable<CacheEntry> {

    static final long IDLE_TIMEOUT = 10 * 60 * 1000; // 10 min in millesecs
    private volatile long expireTime;

    public abstract void close() throws IOException;

    public void touch(final long currentTime) {
        expireTime = currentTime + IDLE_TIMEOUT;
    }

    public boolean isExpired(final long currentTime) {
        return expireTime <= currentTime;
    }

    public abstract byte[] getData(String name) throws IOException;

    @Override
    public int compareTo(final CacheEntry e) {
        if (expireTime == e.expireTime) {
            return 0;
        }
        return (expireTime > e.expireTime) ? 1 : -1;
    }
}
