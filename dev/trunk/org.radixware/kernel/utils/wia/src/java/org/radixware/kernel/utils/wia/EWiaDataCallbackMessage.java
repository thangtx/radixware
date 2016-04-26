/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
package org.radixware.kernel.utils.wia;

import java.util.EnumSet;

public enum EWiaDataCallbackMessage {

    UNKNOWN(0x0), DATAHEADER(0x1), DATA(0x2), STATUS(0x3), TERMINATION(0x4),
    NEWPAGE(0x5), FILEPREVIEWDATA(0x6), FILEPREVIEWDATAHEADER(0x7);
    private final long message;

    private EWiaDataCallbackMessage(final long msg) {
        message = msg;
    }

    public long getMessage() {
        return message;
    }

    public static long toBitMask(final EnumSet<EWiaDataCallbackMessage> msgs) {
        long result = 0;
        if (msgs != null && !msgs.isEmpty()) {
            for (EWiaDataCallbackMessage msg : msgs) {
                result |= msg.getMessage();
            }
        }
        return result;
    }

    public static EWiaDataCallbackMessage fromLong(final long formatIdx) {
        for (EWiaDataCallbackMessage format : EWiaDataCallbackMessage.values()) {
            if (format.message == formatIdx) {
                return format;
            }
        }
        return UNKNOWN;
    }
}
