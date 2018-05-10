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
package org.radixware.kernel.server.instance;

import java.util.Objects;

public class VersionInfo {

    private final String kernelVersion;
    private final String appVersion;
    private final long revision;

    public VersionInfo(
            String kernelVersion,
            String appVersion,
            long revision) {
        this.kernelVersion = kernelVersion;
        this.appVersion = appVersion;
        this.revision = revision;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public long getRevision() {
        return revision;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.kernelVersion);
        hash = 79 * hash + Objects.hashCode(this.appVersion);
        hash = 79 * hash + (int) (this.revision ^ (this.revision >>> 32));
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
        final VersionInfo other = (VersionInfo) obj;
        if (!Objects.equals(this.kernelVersion, other.kernelVersion)) {
            return false;
        }
        if (!Objects.equals(this.appVersion, other.appVersion)) {
            return false;
        }
        if (this.revision != other.revision) {
            return false;
        }
        return true;
    }

}
