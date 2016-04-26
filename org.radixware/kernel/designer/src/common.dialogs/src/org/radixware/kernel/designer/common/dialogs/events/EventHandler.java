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

package org.radixware.kernel.designer.common.dialogs.events;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.SwingUtilities;


public class EventHandler extends Handler {

    public class EventHandlerError extends RuntimeException {

        public EventHandlerError(String mess) {
            super(mess);
        }

        public EventHandlerError(final String mess, final Throwable cause) {
            super(mess, cause);
        }
    }

    public EventHandler() {
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }
    private boolean flag = false;

    @Override
    public void publish(final LogRecord record) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    EventsTopComponent.getTraceTableModel().putLogRecord(record);
                } catch (Exception e) {
                    if (!flag) {
                        flag = true;
                        throw new EventHandlerError("Unable to put record into events window", e);
                    }
                }
            }
        });
    }
}
