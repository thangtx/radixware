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

package org.radixware.kernel.server.trace;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;

public class ViewTraceItem extends TraceItem {

	public ViewTraceItem(final EEventSeverity severity, final String localizedMessage, final String source, final String contextPresentation, final long timeMillis, final boolean isSensitive ) {
		super(null, severity, null, new ArrStr(localizedMessage), source, contextPresentation, isSensitive, timeMillis);
	}

}
