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

package org.radixware.kernel.explorer.env.trace;

import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.enums.EEventSeverity;


public class ExplorerTraceBuffer extends AbstractTraceBuffer<ExplorerTraceItem>{            

    @Override
    protected ExplorerTraceItem createTraceItem(final String message, final EEventSeverity severity, final String source, final long timeMillis) {
        return new ExplorerTraceItem(message, severity, source, timeMillis);
    }

    @Override
    protected ExplorerTraceItem copyTraceItem(final ExplorerTraceItem source) {        
        return new ExplorerTraceItem(source);
    }

}
