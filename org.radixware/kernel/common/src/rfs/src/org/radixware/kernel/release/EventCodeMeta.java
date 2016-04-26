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

package org.radixware.kernel.release;

import org.radixware.kernel.common.types.Id;


public class EventCodeMeta {

    private final Id bundleId;
    private final Id id;
    private final String eventSource;
    private final long severity;

    public EventCodeMeta(final Id bundleId, Id id, String eventSource, long severity) {
        this.bundleId = bundleId;
        this.id = id;
        this.eventSource = eventSource;
        this.severity = severity;
    }

    public Id getBundleId() {
        return bundleId;
    }

    public Id getId() {
        return id;
    }

    public String getEventSource() {
        return eventSource;
    }

    public long getSeverity() {
        return severity;
    }
    
    public String getQualifiedId() {
        return bundleId + "-" + id;
    }
}
