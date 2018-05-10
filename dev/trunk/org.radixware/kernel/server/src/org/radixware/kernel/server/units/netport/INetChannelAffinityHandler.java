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

package org.radixware.kernel.server.units.netport;

import java.util.Map;
import org.radixware.kernel.server.aio.AadcAffinity;

public interface INetChannelAffinityHandler {
    
    /**
     * Calculated affinity for AAS invocation.
     * 
     * @param seance - seance which initiates AAS invocation.
     * @param packet - received data, can be null for timeout or disconnect AAS call.
     * @param headers - received headers, can be null for timeout or disconnect AAS call.
     * @return 
     */
    public AadcAffinity getAadcAffinity(final Seance seance, final byte[] packet, final Map<String, String> headers);

}
