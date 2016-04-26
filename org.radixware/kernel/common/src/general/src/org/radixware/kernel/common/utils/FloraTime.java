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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;


public final class FloraTime {

//	Converters
	//private static final BigDecimal timeZoneOffsetMilis = new BigDecimal(Calendar.getInstance().get(Calendar.DST_OFFSET) + Calendar.getInstance().get(Calendar.ZONE_OFFSET));
	private static final BigDecimal floraTimeOffsetSec = BigDecimal.valueOf(2177452800L); // seconds from 1901/01/01 00:00:00

	public final static BigDecimal getForValue(final java.util.Date val) {
		final long time = val.getTime();
		BigDecimal t = new BigDecimal(time); // miliseconds from 1970/01/01 00:00:00 (timezone offset ignored)
		t = t.add(timezoneOffsetMillis(time));// Timezone offset
		t = t.divide(BigDecimal.valueOf(1000)); //to seconds                
		//adding seconds (from 1901 to 1970)
		t = t.add(floraTimeOffsetSec);          // seconds from 1901/01/01 00:00:00
		return t;
	}

	/**
	 * Notice:
	 * If flora time is in an unexisting period
	 * (the unexisting period happens when time is switched forward due to Daylight Savings)
	 * then it will be moved forward for the period duration.
	 * So when we convert this time back to flora version it will have bigger value that will be
	 * in existing period.
	 * @param floraTime
	 * @return
	 */
	public final static java.sql.Timestamp toTimestamp(final BigDecimal floraTime) {
		BigDecimal t = floraTime.add(floraTimeOffsetSec.negate()).multiply(BigDecimal.valueOf(1000));//to miliseconds
		final BigDecimal timezoneOffsetMillisForShiftedValue = timezoneOffsetMillis(t.longValue());
		t = t.add(timezoneOffsetMillisForShiftedValue.negate());//adding timezone offset
		final BigDecimal timezoneOffsetMillisForOriginalValue = timezoneOffsetMillis(t.longValue());
		if (!timezoneOffsetMillisForOriginalValue.equals(timezoneOffsetMillisForShiftedValue)){
			//there was a switch to/from summer time
			//let's recalculate timezoneOffset
			t = t.add(timezoneOffsetMillisForShiftedValue);//adding timezone offset
			t = t.add(timezoneOffsetMillisForOriginalValue.negate());//adding timezone offset
		}
		return new Timestamp(t.longValue());
	}

	private static BigDecimal timezoneOffsetMillis(final long time) {
		final TimeZone timeZone = Calendar.getInstance().getTimeZone();
		return new BigDecimal(timeZone.getOffset(time));
	}
}
