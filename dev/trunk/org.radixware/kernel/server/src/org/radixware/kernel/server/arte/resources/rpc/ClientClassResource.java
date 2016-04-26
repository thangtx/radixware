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

package org.radixware.kernel.server.arte.resources.rpc;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.resources.Resource;


public abstract class ClientClassResource extends Resource{
    
    public static Object newRemoteInstance(final Arte arte, final Class remoteClassInterface, final Id classId){
        return newRemoteInstance(arte, remoteClassInterface, classId.toString());
    }
    
    public static Object newRemoteInstance(final Arte arte, final Class remoteClassInterface, final String remoteClassName){
        return RadixRpcClient.getInstance().newClientClassRemoteInstance(arte, remoteClassInterface, remoteClassName);
    }    
}
