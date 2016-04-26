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

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;


public class TracerToLoggerAdapter extends Logger {

    private final LocalTracer tracer;

    public TracerToLoggerAdapter(final LocalTracer tracer) {
        super("", null);
        this.tracer = tracer;
    }

    @Override
    public void log(LogRecord record) {
        if (record == null || record.getLevel() == null) {
            return;
        }
        if (tracer != null) {
            if (record.getLevel().intValue() < Level.INFO.intValue()) {
                tracer.put(EEventSeverity.DEBUG, record.getMessage(), null, null, true);
            } else if (record.getLevel().intValue() < Level.WARNING.intValue()) {
                tracer.put(EEventSeverity.EVENT, record.getMessage(), null, null, true);
            } else if (record.getLevel().intValue() < Level.SEVERE.intValue()) {
                tracer.put(EEventSeverity.WARNING, record.getMessage(), null, null, true);
            } else {
                tracer.put(EEventSeverity.ERROR, record.getMessage(), null, null, true);
            }
        }
    }

    @Override
    public boolean isLoggable(Level level) {
        if (level == null) {
            return false;
        }
        if (level.intValue() < Level.INFO.intValue()) {
            return tracer.getMinSeverity() < EEventSeverity.EVENT.getValue();
        } else if (level.intValue() < Level.WARNING.intValue()) {
            return tracer.getMinSeverity() < EEventSeverity.WARNING.getValue();
        } else if (level.intValue() < Level.SEVERE.intValue()) {
            return tracer.getMinSeverity() < EEventSeverity.ERROR.getValue();
        } else if (level.intValue() < Level.OFF.intValue()) {
            return tracer.getMinSeverity() < EEventSeverity.NONE.getValue();
        } else {
            return false;
        }
    }
}
