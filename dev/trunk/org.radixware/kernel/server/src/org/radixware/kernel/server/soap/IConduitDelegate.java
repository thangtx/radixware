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

package org.radixware.kernel.server.soap;

import java.io.IOException;
import org.apache.cxf.ws.addressing.EndpointReferenceType;


public interface IConduitDelegate {
    
    public void setResponceDataListener(final IResponceDataListener listener);
    
    public void sendRequest(EndpointReferenceType endpointInfo, byte[] requestData) throws IOException;
    
}
