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

package org.radixware.kernel.server.algo;

import java.math.BigDecimal;
import org.radixware.kernel.server.types.Algorithm;

public class AppBlockReportGenerator {
	//return 0 - wait
	static public int invoke(final Algorithm algo) throws Exception {
                AlgorithmExecutor executor = algo.getExecutor();
		BigDecimal timeout=(BigDecimal)algo.getProperty("timeout");
		if (executor.syncExecution || (timeout != null && timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0)) {
			//исполнить с синхронным ожиданием события
			if (timeout != null)
				java.lang.Thread.sleep(Math.round(timeout.doubleValue()*1000));
			return 0;
		}
		else {
			if (timeout != null)
				algo.scheduleTimeoutJob(timeout.doubleValue(), null); //подписка на onTimeout
			return -1;
		}
	}
	public static int resume(@SuppressWarnings("unused") final Algorithm algo) {
		//TODO ? AppWait::resume() 
		return 0;
	}
}