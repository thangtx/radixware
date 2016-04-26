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

package org.radixware.kernel.server.trace;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.TraceProfile;


public class TraceProfiles { //RADIX-1390

    public static final TraceProfiles DEFAULT = new TraceProfiles(null, null, null);
    private final String dbTraceProfile;
    private final String fileTraceProfile;
    private final String guiTraceProfile;
    private final TraceProfile dbTraceProfileObj;
    private final TraceProfile fileTraceProfileObj;
    private final TraceProfile guiTraceProfileObj;

    public TraceProfiles(
            final String dbTraceProfile,
            final String fileTraceProfile,
            final String guiTraceProfile) {
        this.dbTraceProfile = dbTraceProfile == null ? EEventSeverity.EVENT.getName() : dbTraceProfile;
        this.fileTraceProfile = fileTraceProfile == null ? EEventSeverity.EVENT.getName() : fileTraceProfile;
        this.guiTraceProfile = guiTraceProfile == null ? EEventSeverity.EVENT.getName() : guiTraceProfile;
        dbTraceProfileObj = new TraceProfile(dbTraceProfile);
        fileTraceProfileObj = new TraceProfile(fileTraceProfile);
        guiTraceProfileObj = new TraceProfile(guiTraceProfile);
    }

    public String getDbTraceProfile() {
        return dbTraceProfile;
    }

    public String getFileTraceProfile() {
        return fileTraceProfile;
    }

    public String getGuiTraceProfile() {
        return guiTraceProfile;
    }

    public TraceProfile getDbTraceProfileObj() {
        return dbTraceProfileObj;
    }

    public TraceProfile getFileTraceProfileObj() {
        return fileTraceProfileObj;
    }

    public TraceProfile getGuiTraceProfileObj() {
        return guiTraceProfileObj;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraceProfiles other = (TraceProfiles) obj;
        if ((this.dbTraceProfile == null) ? (other.dbTraceProfile != null) : !this.dbTraceProfile.equals(other.dbTraceProfile)) {
            return false;
        }
        if ((this.fileTraceProfile == null) ? (other.fileTraceProfile != null) : !this.fileTraceProfile.equals(other.fileTraceProfile)) {
            return false;
        }
        if ((this.guiTraceProfile == null) ? (other.guiTraceProfile != null) : !this.guiTraceProfile.equals(other.guiTraceProfile)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.dbTraceProfile != null ? this.dbTraceProfile.hashCode() : 0);
        hash = 67 * hash + (this.fileTraceProfile != null ? this.fileTraceProfile.hashCode() : 0);
        hash = 67 * hash + (this.guiTraceProfile != null ? this.guiTraceProfile.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "{\n\tDB: " + getDbTraceProfile() + ";\n\tFile: " + getFileTraceProfile() + ";\n\tGUI: " + getGuiTraceProfile() + "\n}";
    }
}
