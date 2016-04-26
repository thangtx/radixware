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

package org.radixware.kernel.common.utils;

import java.util.Calendar;


public class ValueConverter {

    private static final long timeZoneOffsetMillis = Calendar.getInstance().get(Calendar.DST_OFFSET) + Calendar.getInstance().get(Calendar.ZONE_OFFSET);
    private static final long floraTimeOffsetSec = 2177452800L; // seconds from 1901/01/01 00:00:00

    public static double floraValueOf(final java.util.Date val) {
        long t = val.getTime();// miliseconds from 1970/01/01 00:00:00 (timezone offset ignored)
        t += timeZoneOffsetMillis;// Timezone offset
        t /= 1000; //to seconds
        //adding seconds (from 1901 to 1970)
        t += floraTimeOffsetSec;          // seconds from 1901/01/01 00:00:00
        return t;
    }
}
