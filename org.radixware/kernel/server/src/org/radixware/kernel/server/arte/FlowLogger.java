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

package org.radixware.kernel.server.arte;


public class FlowLogger {

    public static final boolean ENABLED = false;
    //
    private static final ThreadLocal<FlowLogger> STORAGE = new ThreadLocal<FlowLogger>() {
        @Override
        protected FlowLogger initialValue() {
            return new FlowLogger();
        }
    };
    private long lastLogMillis = 0;
    private long logStartedMillis = 0;

    public void doLog(final String event, int count) {
        if (!ENABLED) {
            return;
        }
        final long curTimeMillis = System.currentTimeMillis();
        final long durationMillis = curTimeMillis - lastLogMillis;
        System.out.println(Thread.currentThread().getName() + ": " + event + " + " + (lastLogMillis == 0 ? "first" : (durationMillis)) + " ms, total " + (curTimeMillis - logStartedMillis) + " ms" + (count > 0 ? (", " + count + ", " + 1. * durationMillis / count + " ms per step") : "") + (durationMillis > 400 ? " <---" : ""));
        if (lastLogMillis == 0) {
            logStartedMillis = curTimeMillis;
        }
        lastLogMillis = curTimeMillis;
    }

    public static void log(final String event) {
        STORAGE.get().doLog(event, -1);
    }

    public static void log(final String event, int count) {
        STORAGE.get().doLog(event, count);
    }
}
