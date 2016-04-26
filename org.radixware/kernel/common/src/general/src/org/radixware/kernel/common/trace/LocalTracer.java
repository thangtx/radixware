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

package org.radixware.kernel.common.trace;

import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;

public abstract class LocalTracer {

    public void debug(final String mess, final boolean isSensitive) {
        put(EEventSeverity.DEBUG, mess, null, null, isSensitive);
    }

    public abstract void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive);

    public void putFloodControlled(final String floodKey, EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
        put(severity, localizedMess, code, words, isSensitive);
    }
    
    public abstract long getMinSeverity();

    public abstract long getMinSeverity(String eventSource);

    public long getMinSeverity(final EEventSource eventSource) {
        return getMinSeverity(eventSource == null ? (String) null : eventSource.getValue());
    }
}
