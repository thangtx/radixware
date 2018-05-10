/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;


public final class HttpSessionTerminatedError extends Error{
    
    static final long serialVersionUID = -2404464572526443932L;
    
    public HttpSessionTerminatedError(){
        super("HTTP session was terminated");
    }
    
    public void trace(final ClientTracer trace){        
        if (trace!=null){
            trace.put(EEventSeverity.EVENT, getMessage(), EEventSource.CLIENT);
        }
        Logger.getLogger(HttpSessionContext.class.getName()).log(Level.FINE, getMessage());
    }
    
    public void trace(){
        trace(null);
    }

}
