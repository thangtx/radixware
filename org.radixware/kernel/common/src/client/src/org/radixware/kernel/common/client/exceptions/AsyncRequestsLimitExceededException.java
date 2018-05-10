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

package org.radixware.kernel.common.client.exceptions;

import org.radixware.kernel.common.exceptions.ServiceCallException;


public class AsyncRequestsLimitExceededException extends ServiceCallException{
    
    private static final long serialVersionUID = -3292469363452051765L;

    public AsyncRequestsLimitExceededException(){
        super("Too many async requests scheduled");
    }
    
}
