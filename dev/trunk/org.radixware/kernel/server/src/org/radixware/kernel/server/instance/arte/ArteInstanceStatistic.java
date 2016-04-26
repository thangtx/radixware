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

package org.radixware.kernel.server.instance.arte;

final class ArteInstanceStatistic {

    private long lastWaitStartMillis = 0;
    private long lastWorkStartMillis = 0;
    private long waitMillis = 0;
    private long workMillis = 0;

    synchronized void onFinishWork(final long curTimeMillis) {
        if (lastWorkStartMillis != 0) {
            workMillis += curTimeMillis - lastWorkStartMillis;
        }
        lastWorkStartMillis = 0;
    }

    synchronized void onStartWork() {
        lastWorkStartMillis = System.currentTimeMillis();
        if (lastWaitStartMillis != 0) {
            waitMillis += lastWorkStartMillis - lastWaitStartMillis;
        }
        lastWaitStartMillis = 0;
    }

    synchronized void onStartWait() {
        lastWaitStartMillis = System.currentTimeMillis();
    }

    private synchronized long getWaitMillis() {
        if (lastWaitStartMillis != 0) {
            return waitMillis + (System.currentTimeMillis() - lastWaitStartMillis);
        }
        return waitMillis;
    }

    private synchronized long getWorkMillis() {
        if (lastWorkStartMillis != 0) {
            return workMillis + (System.currentTimeMillis() - lastWorkStartMillis);
        }
        return workMillis;
    }

    protected synchronized byte getWorkTimePercent() {
        final long waitTime = getWaitMillis();
        final long workTime = getWorkMillis();
        final long totalTime = workTime + waitTime;
        if (totalTime != 0) {
            return (byte) (100.0 * workTime / totalTime);
        } else {
            return 0;
        }
    }
}
