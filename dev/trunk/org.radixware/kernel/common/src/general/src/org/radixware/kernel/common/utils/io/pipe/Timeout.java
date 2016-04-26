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

package org.radixware.kernel.common.utils.io.pipe;


class Timeout {

    private final long endNanos;
    private final boolean isInfinite;

    /**
     *
     * @param timeOutMillis timeout. If  {@code timeOutMillis} &lt 0, then
     * timeout considered to be infinite. {@linkplain #isExpired()} will always
     * return true and time left will always be -1.
     */
    public Timeout(final long timeOutMillis) {
        if (timeOutMillis < 0) {
            isInfinite = true;
            endNanos = 0;
        } else {
            endNanos = System.nanoTime() + timeOutMillis * 1000000;
            isInfinite = false;
        }

    }

    public long nanosLeft() {
        if (isInfinite) {
            return -1;
        }
        return endNanos - System.nanoTime();
    }

    public long millisLeft() {
        if (isInfinite) {
            return -1;
        }
        return nanosLeft() / 1000000;
    }

    public boolean isExpired() {
        if (isInfinite) {
            return false;
        }
        return endNanos < System.nanoTime();
    }
}
