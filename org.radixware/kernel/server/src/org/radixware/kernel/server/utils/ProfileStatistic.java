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
package org.radixware.kernel.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.server.types.ProfileStatisticEntry;

public final class ProfileStatistic {

    private final Map<Key, Value> data = new HashMap<>();

    public void register(final ProfileStatisticEntry e) {
        register(e.getContext(), e.getSectionId(), e.getDurationNanos(), e.getCount(), e.getMinDurationNanos(), e.getMaxDurationNanos());
    }

    public void register(final String context, final String section, final long durationNanos) {
        register(context, section, durationNanos, 1, durationNanos, durationNanos);
    }

    public void register(final String context, final String section, final long durationNanos, final long count, final long minDuration, final long maxDuration) {
        final Key key = new Key(context, section);
        final Value val = data.get(key);
        if (val == null) {
            data.put(key, new Value(durationNanos, count, minDuration, maxDuration));
        } else {
            val.register(durationNanos, count, minDuration, maxDuration);
        }
    }

    public void clear() {
        data.clear();
    }

    public Collection<ProfileStatisticEntry> getResult() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        final Collection<ProfileStatisticEntry> res = new ArrayList<ProfileStatisticEntry>(data.size());
        for (Map.Entry<Key, Value> e : data.entrySet()) {
            res.add(new ProfileStatisticEntry(
                    e.getKey().context,
                    e.getKey().section,
                    e.getValue().durationNano,
                    e.getValue().count,
                    e.getValue().minDurationNano,
                    e.getValue().maxDurationNano));
        }
        return res;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    static final class Key {

        final String context;
        final String section;

        Key(final String context, final String section) {
            this.context = context;
            this.section = section;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if ((this.context == null) ? (other.context != null) : !this.context.equals(other.context)) {
                return false;
            }
            if ((this.section == null) ? (other.section != null) : !this.section.equals(other.section)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + (this.context != null ? this.context.hashCode() : 0);
            hash = 17 * hash + (this.section != null ? this.section.hashCode() : 0);
            return hash;
        }
    }

    static final class Value {

        long durationNano;
        long minDurationNano;
        long maxDurationNano;
        long count;

        Value(final long durationNano, final long count, final long minDuration, final long maxDuration) {
            this.count = count;
            this.durationNano = durationNano;
            this.minDurationNano = minDuration;
            this.maxDurationNano = maxDuration;
        }

        void register(final long durationNano, final long count, final long minDuration, final long maxDuration) {
            this.durationNano += durationNano;
            this.count += count;
            minDurationNano = Math.min(minDurationNano, minDuration);
            maxDurationNano = Math.max(maxDurationNano, maxDuration);
        }
    }
}
