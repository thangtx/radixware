/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.trace;

import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;

public class EmptyLocalTracer extends LocalTracer {
    
    private static final EmptyLocalTracer INSTANCE = new EmptyLocalTracer();
    
    public static EmptyLocalTracer getInstance() {
        return INSTANCE;
    }
    
    private EmptyLocalTracer() {
    }

    @Override
    public final void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
    }

    @Override
    public final long getMinSeverity() {
        return EEventSeverity.NONE.getValue();
    }

    @Override
    public final long getMinSeverity(String eventSource) {
        return getMinSeverity();
    }

    @Override
    public final long getMinSeverity(EEventSource eventSource) {
        return getMinSeverity();
    }

    @Override
    public final void putFloodControlled(String floodKey, EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
    }

    @Override
    public final void debug(String mess, boolean isSensitive) {
    }
    
    

}
