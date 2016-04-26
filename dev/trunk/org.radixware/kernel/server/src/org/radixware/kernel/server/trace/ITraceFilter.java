/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.trace;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.server.trace.ServerTrace.ETraceDestination;

public interface ITraceFilter {

    public boolean canPut(
            EEventSeverity severity,
            final String localizedMessage,
            final String mlsId,
            final List<String> mlsArgs,
            final String source,
            final String context,
            final long millisOrMinusOne,
            final boolean isSensitive,
            final Collection<ETraceDestination> targetDestinations,
            final String floodKey);

}
