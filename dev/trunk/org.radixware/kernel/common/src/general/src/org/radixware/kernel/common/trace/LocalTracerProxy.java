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


public class LocalTracerProxy extends LocalTracer {

    private LocalTracer backendTracer;
    private LocalTracer defaultTracer;

    public LocalTracerProxy(LocalTracer backendTracer, LocalTracer defaultTracer) {
        this.backendTracer = backendTracer;
        this.defaultTracer = defaultTracer;
    }

    public LocalTracer getBackendTracer() {
        return backendTracer;
    }

    public void setBackendTracer(LocalTracer backendTracer) {
        this.backendTracer = backendTracer;
    }

    public LocalTracer getDefaultTracer() {
        return defaultTracer;
    }

    public void setDefaultTracer(LocalTracer defaultTracer) {
        this.defaultTracer = defaultTracer;
    }

    private LocalTracer calcTracer() {
        return backendTracer != null ? backendTracer : (defaultTracer != null ? defaultTracer : null);
    }

    @Override
    public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
        final LocalTracer tracer = calcTracer();
        if (tracer != null) {
            tracer.put(severity, localizedMess, code, words, isSensitive);
        }
    }

    @Override
    public long getMinSeverity() {
        final LocalTracer tracer = calcTracer();
        if (tracer != null) {
            return tracer.getMinSeverity();
        }
        return EEventSeverity.NONE.getValue();
    }

    @Override
    public long getMinSeverity(String eventSource) {
        final LocalTracer tracer = calcTracer();
        if (tracer != null) {
            return tracer.getMinSeverity(eventSource);
        }
        return EEventSeverity.NONE.getValue();
    }
}
