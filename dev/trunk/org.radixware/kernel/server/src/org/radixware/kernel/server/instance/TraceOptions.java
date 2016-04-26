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

package org.radixware.kernel.server.instance;

import org.radixware.kernel.server.trace.FileLogOptions;
import org.radixware.kernel.server.trace.TraceProfiles;


class TraceOptions {
    private final boolean globalSensTracingIsOn;

    private final TraceProfiles profiles;

    FileLogOptions getLogOptions() {
        return logOptions;
    }

    TraceProfiles getProfiles() {
        return profiles;
    }
    private final FileLogOptions logOptions;

    public TraceOptions(final TraceProfiles profiles, final FileLogOptions logOptions, final boolean globalSensTracingIsOn) {
        this.profiles = profiles;
        this.logOptions = logOptions;
        this.globalSensTracingIsOn = globalSensTracingIsOn;
    }

    boolean isGlobalSensitiveTracingOn() {
        return globalSensTracingIsOn;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraceOptions other = (TraceOptions) obj;
        if (this.globalSensTracingIsOn != other.globalSensTracingIsOn) {
            return false;
        }
        if (this.profiles != other.profiles && (this.profiles == null || !this.profiles.equals(other.profiles))) {
            return false;
        }
        if (this.logOptions != other.logOptions && (this.logOptions == null || !this.logOptions.equals(other.logOptions))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.globalSensTracingIsOn ? 1 : 0);
        hash = 31 * hash + (this.profiles != null ? this.profiles.hashCode() : 0);
        hash = 31 * hash + (this.logOptions != null ? this.logOptions.hashCode() : 0);
        return hash;
    }

    
}
