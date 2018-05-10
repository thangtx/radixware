/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.instance;

public final class ThreadStateSelfRegistrator {
    private final Thread thread;
    private final long threadId;
    private boolean alreadyRegistering = false;
    private long lastRegTimeMillis = System.currentTimeMillis();
    
    // settings
    private static final long SETTINGS_REFRESH_PERIOD_MILLIS = 60000L;
    private long lastSettingsRefreshTimeMillis = System.currentTimeMillis() - SETTINGS_REFRESH_PERIOD_MILLIS;
    private long regPeriodMillis = -1L;

    public ThreadStateSelfRegistrator(Thread thread) {
        this.thread = thread;
        this.threadId = thread.getId();
    }

    private boolean registrationEnabledBySettings() {
        if (System.currentTimeMillis() - lastSettingsRefreshTimeMillis >= SETTINGS_REFRESH_PERIOD_MILLIS) {
            if (Instance.get() != null && Instance.get().getArteStateWriter() != null) {
                regPeriodMillis = Instance.get().getThreadsStateGatherPeriodSec() * 1000L;
                lastSettingsRefreshTimeMillis = System.currentTimeMillis();
            }
        }
        return regPeriodMillis > 0;
    }

    public void selfRegisterIfRequired() {
        if (alreadyRegistering) {
            return;
        }
        if (!registrationEnabledBySettings()) {
            return;
        }
        
        final long now = System.currentTimeMillis();
        final long boundRegTimeMillis = now - regPeriodMillis;
        if (lastRegTimeMillis > boundRegTimeMillis) {
            return;
        }
        
        final Instance instance = Instance.get();
        final ArteStateWriter writer = instance == null ? null : instance.getArteStateWriter();
        if (writer == null) {  // Instance is stopping
            return;
        }
        
        lastRegTimeMillis = writer.getActualLastRegTimeMillis(now, boundRegTimeMillis, lastRegTimeMillis, thread);
        if (lastRegTimeMillis <= boundRegTimeMillis) {
            register();
        }
    }

    private void register() {
        alreadyRegistering = true;
        try {
            final InstanceThreadStateRecord rec = InstanceThreadStateRecord.create(thread, null);
            Instance.get().getArteStateWriter().registerRecord(threadId, rec);
        } finally {
            alreadyRegistering = false;
        }
    }

}
