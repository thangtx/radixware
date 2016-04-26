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

package org.radixware.kernel.server.units.jms;

import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.units.Unit;

@Deprecated
public final class JmsHandlerUnitTracer extends LocalTracer {

    private final Unit unit;
    private final String eventSource;
    private final String prefixForPlainMess;

    public JmsHandlerUnitTracer(final Unit unit, final String eventSource, final String prefixForPlainMess) {
        super();
        this.unit = unit;
        this.eventSource = eventSource;
        this.prefixForPlainMess = prefixForPlainMess;
    }

    @Override
    public long getMinSeverity() {
        return unit.getTrace().getMinSeverity();
    }

    @Override
    public long getMinSeverity(final String eventSource) {
        return unit.getTrace().getMinSeverity(eventSource);
    }

    @Override
    public void put(final EEventSeverity sev, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
        unit.getTrace().put(sev, (code == null ? prefixForPlainMess+": "+localizedMess : localizedMess), code, words, eventSource, isSensitive);
    }
}
