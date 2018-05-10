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

import java.util.Locale;
import java.util.Map;
import org.radixware.kernel.server.aio.AadcAffinity;

/**
 *
 * @author dsafonov
 */
public class AffinityHeaderAffinityHandler implements INetChannelAffinityHandler {

    private static final String AFFINITY_KEY_HEADER_NAME = "AadcAffinityKey";

    @Override
    public AadcAffinity getAadcAffinity(Seance seance, byte[] packet, Map<String, String> headers) {
        if (headers != null) {
            final String key = headers.get(AFFINITY_KEY_HEADER_NAME.toLowerCase(Locale.getDefault()));

            if (key != null) {
                try {
                    return new AadcAffinity(Integer.parseInt(key), 60000);
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }

}
