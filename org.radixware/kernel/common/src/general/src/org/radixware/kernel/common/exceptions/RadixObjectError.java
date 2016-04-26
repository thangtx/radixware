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

package org.radixware.kernel.common.exceptions;

/**
 * Radix object error.
 */
public class RadixObjectError extends RadixError {

    private static final long serialVersionUID = 1204939182837689581L;

    public RadixObjectError(String mess) {
        super(mess);
    }

    public RadixObjectError(String mess, Throwable cause) {
        super(mess, cause);
    }

    public RadixObjectError(String mess, Object source) {
        super(mess + " Source: " + String.valueOf(source)+".");
    }

    public RadixObjectError(String mess, Object source, Throwable cause) {
        super(mess + " Source: " + String.valueOf(source)+".", cause);
    }
}
