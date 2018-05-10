/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.exceptions.RadixPublishedException;

public class DPCSendException extends RadixPublishedException {

    private static final long serialVersionUID = 5626108069719351939L;
    private boolean recoverable = false;
    protected boolean ioException = false;
    protected int throttlePeriodMillis = 0;

    public DPCSendException(String mess) {
        super(mess);
    }

    public DPCSendException(String mess, Throwable cause) {
        super(mess, cause);
    }

    public void setRecoverable(boolean recoverable) {
        this.recoverable = recoverable;
    }

    public boolean isRecoverable() {
        return recoverable;
    }

    public boolean wasIoException() {
        return ioException;
    }

    public boolean needThrottle() {
        return throttlePeriodMillis != 0;
    }

    public int getThrottlePeriodMillis() {
        return throttlePeriodMillis;
    }

    public DPCSendException setThrottle(int throttlePeriodMillis) {
        this.throttlePeriodMillis = throttlePeriodMillis;
        return this;
    }
    
    public DPCSendException throttle() {
        return setThrottle(-1);
    }
}