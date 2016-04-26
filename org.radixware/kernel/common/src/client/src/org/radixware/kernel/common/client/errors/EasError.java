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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.exceptions.ServiceCallFault;


public abstract class EasError extends Error{//NOPMD throws on some fault types when next EAS-request seems to be result with the same fault
    
    private static final long serialVersionUID = 1804442836823616427L;
    
    private final ServiceCallFault sourceFault;
    
    public EasError(final String message, final ServiceCallFault fault){
        super(message,fault);
        sourceFault = fault;
    }
    
    public final ServiceCallFault getSouceFault(){
        return sourceFault;
    }

}
