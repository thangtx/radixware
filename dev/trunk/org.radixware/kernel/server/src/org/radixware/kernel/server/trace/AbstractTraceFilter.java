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

/**
 *
 * @author dsafonov
 */
public class AbstractTraceFilter implements ITraceFilter{

    @Override
    public boolean canPut(EEventSeverity severity, String localizedMessage, String mlsId, List<String> mlsArgs, String source, String context, long millisOrMinusOne, boolean isSensitive, Collection<ServerTrace.ETraceDestination> targetDestinations, String floodKey) {
        return true;
    }

}
