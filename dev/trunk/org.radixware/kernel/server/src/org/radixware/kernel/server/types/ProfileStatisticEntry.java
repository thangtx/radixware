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

package org.radixware.kernel.server.types;

/**
 * see rdx_profilng_tools.doc
 */
public class ProfileStatisticEntry {

    private final String context;
    private final String sectionId;
    private final long durationNanos;
    private final long count;
    private final long minDurationNanos;
    private final long maxDurationNanos;

    public ProfileStatisticEntry(
            final String context,
            final String sectionId,
            final long durationNanos,
            final long count,
            final long minDurationNanos,
            final long maxDurationNanos) {
        this.context = context;
        this.sectionId = sectionId;
        this.durationNanos = durationNanos;
        this.count = count;
        this.maxDurationNanos = maxDurationNanos;
        this.minDurationNanos = minDurationNanos;
    }

    public String getContext() {
        return context;
    }

    public long getMaxDurationNanos() {
        return maxDurationNanos;
    }

    public long getMinDurationNanos() {
        return minDurationNanos;
    }

    public long getCount() {
        return count;
    }

    public long getDurationNanos() {
        return durationNanos;
    }

    public String getSectionId() {
        return sectionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProfileStatisticEntry other = (ProfileStatisticEntry) obj;
        if ((this.context == null) ? (other.context != null) : !this.context.equals(other.context)) {
            return false;
        }
        if ((this.sectionId == null) ? (other.sectionId != null) : !this.sectionId.equals(other.sectionId)) {
            return false;
        }
        if (this.durationNanos != other.durationNanos) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.context != null ? this.context.hashCode() : 0);
        hash = 59 * hash + (this.sectionId != null ? this.sectionId.hashCode() : 0);
        hash = 59 * hash + (int) (this.durationNanos ^ (this.durationNanos >>> 32));
        hash = 59 * hash + (int) (this.count ^ (this.count >>> 32));
        return hash;
    }
}
