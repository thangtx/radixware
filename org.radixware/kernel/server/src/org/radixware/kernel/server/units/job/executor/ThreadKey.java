/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.job.executor;

import java.util.Objects;

/**
 *
 * @author dsafonov
 */
public class ThreadKey {

    private final String threadPoolClassGuid;
    private final String threadPoolPid;
    private final int threadKey;

    public ThreadKey(String threadPoolClassGuid, String threadPoolPid, int threadKey) {
        this.threadPoolClassGuid = threadPoolClassGuid;
        this.threadPoolPid = threadPoolPid;
        this.threadKey = threadKey;
    }

    public String getThreadPoolClassGuid() {
        return threadPoolClassGuid;
    }

    public String getThreadPoolPid() {
        return threadPoolPid;
    }

    public int getThreadKey() {
        return threadKey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.threadPoolClassGuid);
        hash = 97 * hash + Objects.hashCode(this.threadPoolPid);
        hash = 97 * hash + this.threadKey;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThreadKey other = (ThreadKey) obj;
        if (!Objects.equals(this.threadPoolClassGuid, other.threadPoolClassGuid)) {
            return false;
        }
        if (!Objects.equals(this.threadPoolPid, other.threadPoolPid)) {
            return false;
        }
        if (this.threadKey != other.threadKey) {
            return false;
        }
        return true;
    }

}
